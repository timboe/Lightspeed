package com.timboe.lightspeed;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class LIGHTSPEED extends Applet implements Runnable, MouseWheelListener, MouseMotionListener, MouseListener, KeyListener {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5213789766366330870L;
	float TIME_OF_FRAME = 0;
	float TIME_OF_LAST_SECOND = 0;
	int FPS_LAST_SECOND = 0;
	float TIME_TO_RENDER = 0;
	int FPS = 0;
	int FRAMES=0;
	int FRAMES_LAST_SECOND=0;
	int SLEEP = 30;
	
	int _DESIRED_FPS = 30;
	int _TICKS_PER_RENDER = 10;
	long _TIME_OF_NEXT_FRAME;
	long _TIME_OF_LAST_SECOND;
	int _FRAMES_LAST_SECOND;
	int _FRAMES_CUR_SECOND;
	int _TICKS_CUR_SEC;
	int _TICKS_LAST_SEC;
	int _TICK;
	
	
	int Tick=0;

	private Image dbImage;
	private Graphics dbg;
	

	
	Utility U = Utility.GetUtility();
	PhotonManager P = PhotonManager.GetPhotonManager();
	//Rectangle R1;
	//Rectangle R2;
	///Rectangle R3;
	
	//SpriteShip pew_pew_ship;

	int last_X = -1;
	int last_Y = -1;
	Point CurMouse;
	Point2D MouseTF;


	boolean mouseClick = false;
	boolean mouseDrag = false;
	boolean sendMouseDragPing = false;
	
	//Display parameters
	float ZOOM=0.7f;
	float YSHEAR=0.5f;
	float ROTATE = 0f;
	float TRANSLATE_X = 0f;
	float TRANSLATE_Y = 0f;
	float TOP_ROTATE;

	boolean N;
	boolean E;
	boolean S;
	boolean W;
	
	boolean speed_up;
	boolean speed_dn;
	
	boolean c_up;
	boolean c_dn;

	
	private Thread th;


    RenderingHints aa_on = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    RenderingHints aa_off = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
    private boolean aa = true;
    private boolean disable_aa = true;
    AffineTransform af_none = null;
    AffineTransform af_backing = null;
    AffineTransform af = null;
    AffineTransform af_translate_zoom = null;
    AffineTransform af_shear_rotate = null;
    Font myFont = new Font(Font.MONOSPACED, Font.BOLD, 20);
    


    
    @Override
	public void destroy() { }

	public boolean GetAA() {
    	return aa;
    }

	@Override
	public void init() {
		
		setSize(U.world_x_pixels, U.world_y_pixels);

		addMouseWheelListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		addKeyListener(this);
		

		for (int n=0; n<50; ++n) {
			Rectangle R = new Rectangle(U.R.nextFloat()*U.world_x_pixels,
					U.R.nextFloat()*U.world_y_pixels,
					7+U.R.nextInt(5)-2,
					Color.magenta);
			R.speed = (float) ((1./100.)*n);//(float) (U.R.nextFloat() * 0.5);
			U.list_of_rectangles.add(R);
		}
		
		//U.list_of_rectangles.add(new Rectangle(300f, 400f, 7, Color.green));

		//pew_pew_ship = new SpriteShip(200,400);
		U.player = new PlayerShip(200,200);
		
	    aa_on.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			U.debug = !U.debug;
			System.out.println("Debug="+U.debug);
		}
		if (e.getKeyChar() == 'c' || e.getKeyChar() == 'C') {
			U.show_all_locations = !U.show_all_locations;
			System.out.println("show_all_locations="+U.show_all_locations);
		}
		
		if (e.getKeyChar() == 'w' || e.getKeyChar() == 'W') N = true;
		if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D') E = true;
		if (e.getKeyChar() == 's' || e.getKeyChar() == 'S') S = true;
		if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A') W = true;

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			c_dn = true;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_UP) c_up = true;
		if (e.getKeyCode() == KeyEvent.VK_LEFT) speed_dn = true;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) speed_up = true;



	}

	@Override
	public void keyTyped(KeyEvent e) {


	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseDrag = true;
		CurMouse = e.getPoint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		CurMouse = e.getPoint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		mouseClick=true;
		U.GameOn = true;
		U.show_all_locations = false;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseClick=false;
		mouseDrag=false;

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) {
			ZOOM = ZOOM * 1.2f;
		}
		else {
			ZOOM = ZOOM * 0.8f;
		}
		if (ZOOM > 10.) ZOOM = 10.f;
		if (ZOOM < 0.1) ZOOM = 0.1f;
	}

	@Override
	public void paint (Graphics g) {
		final float TIME = (System.nanoTime() / 1000000);
		TIME_TO_RENDER = TIME - TIME_OF_FRAME;
		TIME_OF_FRAME = TIME;
		++FRAMES;
		if (TIME > TIME_OF_LAST_SECOND) {
			FPS = FRAMES - FRAMES_LAST_SECOND;
			FRAMES_LAST_SECOND = FRAMES;
			TIME_OF_LAST_SECOND = (float) (TIME + 1000.);
		}

		
		
		final Graphics2D g2 = (Graphics2D)g;


		
		g2.setColor (Color.black);
		g2.fillRect (0, 0, U.world_x_pixels, U.world_y_pixels);
		
		if (U.debug == true) {
			P.RenderShells(g2);
		}
				
		
		if (U.show_all_locations == true) {
			for (Rectangle _r : U.list_of_rectangles) {
				_r.RenderReal(g2);
			}
			//pew_pew_ship.RenderReal(g2);
		}
		
		//Draw player
		U.player.Render(g2);
		
		//draw what player sees
		P.RenderFromLocation(g2,Math.round(U.player.x),Math.round(U.player.y));

		g2.setFont(myFont);
		g2.setColor(Color.white);
		g2.drawString(" FPS:"+_FRAMES_LAST_SECOND+" TPS:"+_TICKS_LAST_SEC,10,20);//+" DEATHS:"+P.DEATHS+" WINS:"+P.WINS, 10, 20);
		g2.drawString(" Speed of Light:"+U.c_pixel, 10, 40);
		g2.drawString(" Speed Multiplier of Asteroids:"+U.velocity, 10, 60);
		
		if (GameOverCheck() == true) {
			U.GameOn = false;
			U.show_all_locations = true;
		}

	}

	void Tick() {
		++U.shellTime;
		
//		if (N == true) U.player.accelerate_V(DirectionEnum.N);
//		if (E == true) U.player.accelerate_H(DirectionEnum.E);
//		if (S == true) U.player.accelerate_V(DirectionEnum.S);
//		if (W == true) U.player.accelerate_H(DirectionEnum.W);
		
		if (N == true) U.player.accelerate(+1);
		if (E == true) U.player.changeDirection(+1);
		if (S == true) U.player.accelerate(-1);
		if (W == true) U.player.changeDirection(-1);

		
		if (c_dn == true) {
			U.c_pixel -= 0.01;
		}
		if (c_up == true) U.c_pixel += 0.01;

		if (speed_dn == true) U.velocity -= 0.01;
		if (speed_up == true) U.velocity += 0.01;
		
		if (U.c_pixel > 1) U.c_pixel = 1;
		else if (U.c_pixel <= 0) U.c_pixel = 0.01f;

		if (U.velocity > 2) U.velocity = 2;
		else if (U.velocity <= 1) U.velocity = 1f;
		
		if (CurMouse != null && (mouseClick||mouseDrag)) {
			U.player.x = (int) CurMouse.getX();
			U.player.y = (int) CurMouse.getY();
		}
		
		for (Rectangle _r : U.list_of_rectangles) {
			_r.Walk();
			_r.Tick(_TICK);
		}
		//pew_pew_ship.Tick(_TICK);
		U.player.Walk();
		
		if (U.shellTime % 1 == 0) P.Cleanup(); //photon manager
		
	}
	
	public boolean GameOverCheck() {
		//cheat!
//		for (Rectangle R : U.list_of_rectangles) {
//			float ss2 = R.shape_size/2f;
//			float sep_x = (R.x + ss2) - U.player.x;
//			float sep_y = (R.y + ss2) - U.player.y;
//			double sep = Math.sqrt( (sep_x*sep_x) + (sep_y*sep_y) );
//			if (sep < (ss2 + 4.)) return true;
//		}
		return false;
	}

	
	@Override
	public void run() {
		// lower ThreadPriority
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		// run a long while (true) this means in our case "always"
		while (true) {
			if (_TIME_OF_NEXT_FRAME  >  System.currentTimeMillis()) {
				// too soon to repaint, wait...
			    try {
					Thread.sleep(Math.abs(_TIME_OF_NEXT_FRAME - System.currentTimeMillis()));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			_TIME_OF_NEXT_FRAME = System.currentTimeMillis() + Math.round(1000f/300f);
			++_TICK;

			if (_TICK % _TICKS_PER_RENDER == 0) {
				++_FRAMES_CUR_SECOND;
				repaint();
			}// else {
				++_TICKS_CUR_SEC;
				if (U.GameOn == true) Tick();
			//}
			if (System.currentTimeMillis() > _TIME_OF_LAST_SECOND + 1000) {
				_TIME_OF_LAST_SECOND = System.currentTimeMillis();
				_FRAMES_LAST_SECOND = _FRAMES_CUR_SECOND;
				_FRAMES_CUR_SECOND = 0;
				_TICKS_LAST_SEC = _TICKS_CUR_SEC;
				_TICKS_CUR_SEC = 0;
			}
			
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
	}

	public void SetAA(Graphics2D _g2, boolean _on) {
    	if (disable_aa == true) {
    		_g2.setRenderingHints(aa_off);
    		aa = false;
    		return;
    	}
    	if (_on == true && aa == false) {
    		_g2.setRenderingHints(aa_on);
    	} else if (_on == false && aa == true) {
    		_g2.setRenderingHints(aa_off);
    	}
    	aa = _on;
    }

	@Override
	public void start() {
		th = new Thread (this);
		th.start ();
	}
	@Override
	public void stop() { }

	@Override
	public void update (Graphics g)	{

		final Graphics2D g2 = (Graphics2D)g;

		// initialize buffer
		if (dbImage == null) {
			dbImage = createImage (this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics ();
		}

		// clear screen in background
		dbg.setColor (getBackground ());
		dbg.fillRect (0, 0, this.getSize().width, this.getSize().height);

		// draw elements in background
		dbg.setColor (getForeground());
		paint (dbg);

		// draw image on the screen
		g2.drawImage (dbImage, 0, 0, this);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyChar() == 'w' || e.getKeyChar() == 'W') N = false;
		if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D') E = false;
		if (e.getKeyChar() == 's' || e.getKeyChar() == 'S') S = false;
		if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A') W = false;
		
		if (e.getKeyCode() == KeyEvent.VK_DOWN) c_dn = false;
		if (e.getKeyCode() == KeyEvent.VK_UP) c_up = false;
		if (e.getKeyCode() == KeyEvent.VK_LEFT) speed_dn = false;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) speed_up = false;
		
	}


}

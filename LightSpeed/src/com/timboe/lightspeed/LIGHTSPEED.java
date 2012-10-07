package com.timboe.lightspeed;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
//import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

public class LIGHTSPEED extends Applet implements Runnable, MouseMotionListener, MouseListener, KeyListener {
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
	ButtonManager B = ButtonManager.GetButtonManager();

	boolean mouseClick = false, mouseDrag = false;
	boolean speed_up, speed_dn, c_up, c_dn;
	boolean N=false,E=false,S=false,W=false;
	
	private Thread th;

//    RenderingHints aa_on = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
//    RenderingHints aa_off = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
//    private boolean aa = true;
//    private boolean disable_aa = true;
    Font myFont = new Font(Font.MONOSPACED, Font.BOLD, 20);

    @Override
	public void destroy() { }

	@Override
	public void init() {
		
		setSize(U.world_x_pixels, U.world_y_pixels);
		U.MAIN = this;

		addMouseMotionListener(this);
		addMouseListener(this);
		addKeyListener(this);

		for (int i=0; i<U.twinkle_stars; ++i) {
			U.list_of_stars.add(new BackgroundStar(U.R.nextFloat()*U.world_x_pixels - U.world_x_pixels2,
					U.R.nextFloat()*(U.world_y_pixels-U.UI) - U.world_y_pixels2 + U.UI));
		}
		
	    //aa_on.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
	}
	
	public void NewGame() {
		synchronized (U.list_of_rectangles_sync) {
			U.list_of_rectangles_sync.clear();
		}
		synchronized (U.list_of_debris_sync) {
			U.list_of_debris_sync.clear();
		}
		P.Clear();
		
		for (int n=1; n<=50; ++n) {
			int rx=0, ry=0;
			while (rx < 200 && rx > -200) {
				rx = (int) (U.R.nextFloat()*(U.world_x_pixels - 10) - U.world_x_pixels2);
			}
			while (ry < 200 && ry > -200 ) {
				ry = (int) (U.R.nextFloat()*(U.world_y_pixels - 10 - U.UI) - U.world_y_pixels2 + U.UI);
			}
			Rectangle R = new Rectangle(rx,
					ry,
					7+U.R.nextInt(5)-2, //size
					(float) ((1./100.)*n));//speed //(float) (U.R.nextFloat() * 0.5);
			synchronized (U.list_of_rectangles_sync) {
				U.list_of_rectangles.add(R);
			}
		}
		
		U.player = new PlayerShip(0 - U.world_x_pixels2, 0 - U.world_y_pixels2);
		
		U.Level = 1;
		U.Lives = U.MaxLives;
		U.c_pixel = 1f;
		U.velocity = 1f;
		B.PlayingScore.SetValue(0);
		U.show_all_locations = false;
	}

	

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseClick=false;
		mouseDrag=false;
		B.ProcessMouseClick();
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
		if (U.af_none == null) U.af_none = g2.getTransform();
		g2.setTransform(U.af_none);
		
		g2.setColor (Color.black);
		g2.fillRect (0, 0, U.world_x_pixels, U.world_y_pixels);
		
		g2.setFont(myFont);
		g2.setColor(Color.white);
		B.Render(g2); //Do buttons		
		
		if (U.currentMode == GameMode.GameOn || U.currentMode == GameMode.GameOver) {
			paint_Game(g2);
		} else if (U.currentMode == GameMode.Title) {
			paint_Title(g2);
		}


	}
	
	public void paint_Game(Graphics2D g2) {
		float GammaX = U.player.GetGamma(+1);
		float GammaY = U.player.GetGamma(-1);
		
		g2.translate(U.world_x_pixels2, U.world_y_pixels2);
		if (U.option_Length == true) g2.scale(GammaX, GammaY);
		
		if (U.option_Time == true) {
			U.time_dilation_X = 1 - ((1 - GammaX) * 8);// (super mode)
			U.time_dilation_Y = 1 - ((1 - GammaY) * 8);// (super mode)
		} else {
			U.time_dilation_X = 1;
			U.time_dilation_Y = 1;
		}
		
		g2.setColor(Color.white);
		g2.drawRect(0 - U.world_x_pixels2, 0 - U.world_y_pixels2 + U.UI, U.world_x_pixels, U.world_y_pixels - U.UI);
		
		if (U.debug == true) P.RenderShells(g2);
				
		for (BackgroundStar _s : U.list_of_stars) _s.Render(g2);
		
		if (U.show_all_locations == true) {
			synchronized (U.list_of_rectangles_sync) {
				for (Rectangle _r : U.list_of_rectangles) _r.RenderReal(g2);
			}
			synchronized (U.list_of_debris_sync) {
				for (Debris _d : U.list_of_debris_sync) _d.RenderReal(g2);
			}
		}
		
		//draw what player sees
		P.RenderFromLocation(g2,Math.round(U.player.x),Math.round(U.player.y));
		
		//Do this LAST as it alters the renderer
		//Draw player
		U.player.Render(g2);
		
		if (CollisionCheck() == true) { //true for no lives left
			U.currentMode = GameMode.GameOver;
			U.show_all_locations = true;
		}
	}
	
	public void paint_Title(Graphics2D g2) {
		
		g2.translate(U.world_x_pixels2, U.world_y_pixels2);
		for (BackgroundStar _s : U.list_of_stars) _s.Render(g2);
		
		g2.setTransform(U.af_none);
		
		if (U.titleCascade <= 256) {
			for (int x=1; x<=U.titleCascade; x+=5) {
				if (x > 256) break;
			    g2.setColor(new Color(1-(1f/x),1-(1f/x),1-(1f/x)));
			    g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, x/3));
				g2.drawString("LIGHTSPEED", 50+x, 50+x);
				if (x == U.titleCascade || x == 256) {
				    g2.setColor(Color.gray);
					g2.drawString("LIGHTSPEED", 50+x, 50+x);
					g2.drawString("LIGHTSPEED", 58+x, 58+x);
				    g2.setColor(Color.white);
					g2.drawString("LIGHTSPEED", 54+x, 54+x);
				}
			}
		} else if (U.titleCascade <= 512) {
			for (int x=1; x<=256; x+=5) {
				if (x < U.titleCascade-256) continue;
				else g2.setColor(Color.white);
			    g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, x/3));
				g2.drawString("LIGHTSPEED", 50+x, 50+x);
				if (x == 256) {
				    g2.setColor(Color.gray);
					g2.drawString("LIGHTSPEED", 50+x, 50+x);
					g2.drawString("LIGHTSPEED", 58+x, 58+x);
				    g2.setColor(Color.white);
					g2.drawString("LIGHTSPEED", 54+x, 54+x);
				}
			}
		} else {
		    g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 256/3));
		    g2.setColor(Color.gray);
			g2.drawString("LIGHTSPEED", 50+256, 50+256);
			g2.drawString("LIGHTSPEED", 58+256, 58+256);
		    g2.setColor(Color.white);
			g2.drawString("LIGHTSPEED", 54+256, 54+256);
		}
		U.titleCascade += 5;
		//if (U.titleCascade >= 3*256) U.titleCascade = 1;	


	}

	void Tick() {
		
		if (U.currentMode == GameMode.GameOn || U.currentMode == GameMode.GameOver) {
			Tick_Game();
		}		
	}
	
	void Tick_Game(){
		++U.shellTime;

		if (N == true) U.player.accelerate(+1);
		if (E == true) U.player.changeDirection(+1);
		if (S == true) U.player.accelerate(-1);
		if (W == true) U.player.changeDirection(-1);
		
//		if (c_dn == true) U.c_pixel -= 0.01;
//		if (c_up == true) U.c_pixel += 0.01;
//
//		if (speed_dn == true) U.velocity -= 0.01;
//		if (speed_up == true) U.velocity += 0.01;
//		
//		if (U.c_pixel > 1) U.c_pixel = 1;
//		else if (U.c_pixel <= 0) U.c_pixel = 0.01f;
//
//		if (U.velocity > 2) U.velocity = 2;
//		else if (U.velocity <= 1) U.velocity = 1f;
//		
//		if (U.CurMouse != null && (mouseClick||mouseDrag)) {
//			U.player.x = (int) U.CurMouse.getX() - (U.world_x_pixels2);
//			U.player.y = (int) U.CurMouse.getY() - (U.world_y_pixels2);
//			U.player.vx = 0;
//			U.player.vy = 0;
//		}
		
		synchronized (U.list_of_rectangles_sync) {
			for (Rectangle _r : U.list_of_rectangles_sync) {
				_r.Walk();
				_r.Tick(_TICK);
			}
		}
		synchronized (U.list_of_debris_sync) {
			for (Debris _d : U.list_of_debris_sync) {
				_d.Walk();
				_d.Tick(_TICK);
			}
		}
		U.player.Walk();
		
		//levels and difficulty
		if (_TICK % U.TicksPerLevel == 0) ++U.Level;
		
		if (U.c_pixel > U.min_c) U.c_pixel -= U.c_red;
		if (U.velocity < U.max_v_multiplier) U.velocity += U.v_inc;
		
		
		P.Cleanup(); //photon manager cleanup
		DebrisCleanup();
	}
	
	public void DebrisCleanup() {
		LinkedList<Debris> ToRemove = new LinkedList<Debris>();
		synchronized (U.list_of_debris_sync) {
			for (Debris _d : U.list_of_debris_sync) {
				if (_d.GetDead() == true) ToRemove.add(_d);
			}
		}
		synchronized (U.list_of_debris_sync) {
			U.list_of_debris_sync.removeAll(ToRemove);
		}
	}
	
	public boolean CollisionCheck() {
		//Also does points
		float pointsScored = 0;
		LinkedList<Rectangle> ToRemove = new LinkedList<Rectangle>();
		synchronized (U.list_of_rectangles_sync) {
			for (Rectangle R : U.list_of_rectangles_sync) {
				float sep_x = (R.x + R.shape_size2) - U.player.x;
				float sep_y = (R.y + R.shape_size2) - U.player.y;
				double sep = Math.hypot(sep_x, sep_y);
				pointsScored += 1f / sep;
				if (sep < (R.shape_size2 + 4.)) {
					for (int i=0; i < U.NDebris; ++i) {
						synchronized (U.list_of_debris_sync) {
							U.list_of_debris_sync.add(new Debris(R));
						}
					}
					ToRemove.add(R);
					--U.Lives;
					if (U.Lives < 0) U.Lives = 0;
					U.player.Damage();
				}
			}
			pointsScored *= U.Level;
			B.PlayingScore.AddValue(pointsScored);
			if (ToRemove.size() > 0){
				U.list_of_rectangles_sync.removeAll(ToRemove);
				//if (U.Lives == 0) return true; //CHEAT
			}
		}
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
			}
			++_TICKS_CUR_SEC;
			if (U.currentMode == GameMode.GameOn || U.currentMode == GameMode.FreePlay) Tick();
			
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

//	public void SetAA(Graphics2D _g2, boolean _on) {
//    	if (disable_aa == true) {
//    		_g2.setRenderingHints(aa_off);
//    		aa = false;
//    		return;
//    	}
//    	if (_on == true && aa == false) {
//    		_g2.setRenderingHints(aa_on);
//    	} else if (_on == false && aa == true) {
//    		_g2.setRenderingHints(aa_off);
//    	}
//    	aa = _on;
//    }

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

		if (e.getKeyCode() == KeyEvent.VK_DOWN)	c_dn = true;
		if (e.getKeyCode() == KeyEvent.VK_UP) c_up = true;
		if (e.getKeyCode() == KeyEvent.VK_LEFT) speed_dn = true;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) speed_up = true;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseDrag = true;
		U.CurMouse = e.getPoint();
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		U.CurMouse = e.getPoint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseClick=true;
		//U.GameOn = true;
		//U.show_all_locations = false;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}


}

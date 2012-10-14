package com.timboe.lightspeed;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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
	long _TIME_OF_LAST_FRAME = 0; //Internal
	long _FPS = 0;
	int _DO_FPS_EVERY_X_FRAMES = 10; //refresh FPS after how many frames
	long _TIME_OF_NEXT_TICK; //Internal
	int _TICK; //Counter
	int _FRAME; //Counter
	int _DESIRED_TPS = 300; //Ticks per second to aim for
	int _TICKS_PER_RENDER = 10; //Render every X ticks


	private Image dbImage;
	private Graphics dbg;

	Utility U = Utility.GetUtility();
	PhotonManager P = PhotonManager.GetPhotonManager();
	ButtonManager B = ButtonManager.GetButtonManager();

	boolean N=false,E=false,S=false,W=false;

	private Thread th;

    public void Clear() {
		synchronized (U.list_of_rectangles_sync) {
			U.list_of_rectangles_sync.clear();
		}
		synchronized (U.list_of_debris_sync) {
			U.list_of_debris_sync.clear();
		}
		P.Clear();
	}

	public boolean CollisionCheck() {
		//Also does points
		float pointsScored = 0;
		final LinkedList<Rectangle> ToRemove = new LinkedList<Rectangle>();
		synchronized (U.list_of_rectangles_sync) {
			for (final Rectangle R : U.list_of_rectangles_sync) {
				final float sep_x = (R.x + R.shape_size2) - U.player.x;
				final float sep_y = (R.y + R.shape_size2) - U.player.y;
				final double sep = Math.hypot(sep_x, sep_y);
				pointsScored += 1f / sep;
				if (sep < (R.shape_size2 + 4.)) {
					for (int i=0; i < U.NDebris; ++i) {
						synchronized (U.list_of_debris_sync) {
							U.list_of_debris_sync.add(new Debris(R));
						}
					}
					ToRemove.add(R);
					--U.Lives;
					if (U.Lives <= 0) {
						U.Lives = 0;
					} else {
						U.player.Damage();
					}
				}
			}
			pointsScored *= U.Level;
			if (U.currentMode == GameMode.GameOn) {
				B.PlayingScore.AddValue(pointsScored);
			}
			if (ToRemove.size() > 0){
				synchronized (U.list_of_debris_sync) {
					U.list_of_rectangles_sync.removeAll(ToRemove);
				}
				if (U.Lives == 0) return true; //CHEAT
			}
		}
		return false;
	}

	public void DebrisCleanup() {
		final LinkedList<Debris> ToRemove = new LinkedList<Debris>();
		synchronized (U.list_of_debris_sync) {
			for (final Debris _d : U.list_of_debris_sync) {
				if (_d.GetDead() == true) {
					ToRemove.add(_d);
				}
			}
		}
		synchronized (U.list_of_debris_sync) {
			U.list_of_debris_sync.removeAll(ToRemove);
		}
	}

	@Override
	public void destroy() { }

	void doLengthContractionTransform(Graphics2D g2) {
		g2.rotate(U.player.GetHeading());
		g2.scale(1, 1/U.player.GetGamma());
		g2.rotate(-U.player.GetHeading());
		//g2.scale(0.4,0.4);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 'w' || e.getKeyChar() == 'W' || e.getKeyCode() == KeyEvent.VK_UP) {
			N = true;
		}
		if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D' || e.getKeyCode() == KeyEvent.VK_RIGHT) {
			E = true;
		}
		if (e.getKeyChar() == 's' || e.getKeyChar() == 'S' || e.getKeyCode() == KeyEvent.VK_DOWN) {
			//S = true;
		}
		if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A' || e.getKeyCode() == KeyEvent.VK_LEFT) {
			W = true;
		}
	}

	@Override
	public void init() {

		setSize(U.world_x_pixels+(2*U.world_x_offset), U.world_y_pixels+(2*U.world_y_offset));
		U.MAIN = this;
		System.out.println("World is " + (U.world_x_pixels+(2*U.world_x_offset)) + "x" + (U.world_y_pixels+(2*U.world_y_offset)));

		addMouseMotionListener(this);
		addMouseListener(this);
		addKeyListener(this);

		for (int i=0; i<U.twinkle_stars; ++i) {
			U.list_of_stars.add(new BackgroundStar(U.R.nextFloat()*U.world_x_pixels - U.world_x_pixels2,
					U.R.nextFloat()*(U.world_y_pixels-U.UI) - U.world_y_pixels2 + U.UI));
		}

		if (U.currentMode == GameMode.GameOn)
		 {
			NewGame(); //DEBUG
		}
		if (U.currentMode == GameMode.Creative) {
			NewCreative(); //DEBUG
			U.list_of_rectangles_sync.add( new Rectangle(
					(int) (U.R.nextFloat()*(U.world_x_pixels - 10) - U.world_x_pixels2),
					(int) (U.R.nextFloat()*(U.world_y_pixels - 10 - U.UI) - U.world_y_pixels2 + U.UI),
					7+U.R.nextInt(5)-2,
					1f) );
			U.show_all_locations = true;
			U.c_pixel = 1f;
			U.velocity = 0.95f;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyChar() == 'w' || e.getKeyChar() == 'W' || e.getKeyCode() == KeyEvent.VK_UP) {
			N = false;
		}
		if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D' || e.getKeyCode() == KeyEvent.VK_RIGHT) {
			E = false;
		}
		if (e.getKeyChar() == 's' || e.getKeyChar() == 'S' || e.getKeyCode() == KeyEvent.VK_DOWN) {
			S = false;
		}
		if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A' || e.getKeyCode() == KeyEvent.VK_LEFT) {
			W = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		U.CurMouse = e.getPoint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		U.CurMouse = e.getPoint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		U.CurMouse = e.getPoint();
		U.mouseClick=true;
		U.ticks_with_mouse_down = -200;
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		U.mouseClick=false;
		if (U.ticks_with_mouse_down < 0) {
			B.ProcessMouseClick();
		}
	}

	public void NewCreative() {
		Clear();

		U.player = new PlayerShip(0 - U.world_x_pixels2, 0 - U.world_y_pixels2);

		U.c_pixel = 0.8f;
		U.velocity = 0.5f;
		U.show_all_locations = false;
		U.show_light_cones = false;
	}

	public void NewGame() {
		Clear();

		for (int n=1; n<=50; ++n) {
			int rx=0, ry=0;
			while (rx < 200 && rx > -200) {
				rx = (int) (U.R.nextFloat()*(U.world_x_pixels - 10) - U.world_x_pixels2);
			}
			while (ry < 200 && ry > -200 ) {
				ry = (int) (U.R.nextFloat()*(U.world_y_pixels - 10 - U.UI) - U.world_y_pixels2 + U.UI);
			}
			final Rectangle R = new Rectangle(rx,
					ry,
					7+U.R.nextInt(5)-2, //size
					(float) ((1./100.)*n));//speed //(float) (U.R.nextFloat() * 0.5);
			synchronized (U.list_of_rectangles_sync) {
				U.list_of_rectangles_sync.add(R);
			}
		}

		U.player = new PlayerShip(0 - U.world_x_pixels2, 0 - U.world_y_pixels2);

		U.Level = 1;
		U.Lives = U.MaxLives;
		U.c_pixel = 1f;
		U.velocity = 1f;
		B.PlayingScore.SetValue(0);
		U.show_all_locations = false;
		U.show_light_cones = false;
	}

	@Override
	public void paint (Graphics g) {

		final Graphics2D g2 = (Graphics2D)g;
		if (U.af_none == null) {
			U.af_none = g2.getTransform();
			U.af_none.translate(U.world_x_offset, U.world_y_offset);
		}
		g2.setTransform(U.af_none);
		//Main transform

		//fill black
		g2.setColor (Color.black);
		g2.fillRect (-U.world_x_offset, -U.world_y_offset, U.world_x_pixels+(2*U.world_x_offset), U.world_y_pixels+(2*U.world_y_offset));

		if (U.currentMode == GameMode.Title) {
			g2.translate(U.world_x_pixels2, U.world_y_pixels2);
			for (final BackgroundStar _s : U.list_of_stars) {
				_s.Render(g2);
			}
			g2.setTransform(U.af_none);

			if (U.titleCascade <= 256) {
				for (int x=1; x<=U.titleCascade; x+=5) {
					if (x > 256) {
						break;
					}
				    g2.setColor(new Color(1-(1f/x),1-(1f/x),1-(1f/x)));
				    g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, x/3));
					g2.drawString("LIGHTSPEED.", 50+x, 50+x);
					if (x == U.titleCascade || x == 256) {
					    g2.setColor(Color.gray);
						g2.drawString("LIGHTSPEED.", 50+x, 50+x);
						g2.drawString("LIGHTSPEED.", 58+x, 58+x);
					    g2.setColor(Color.white);
						g2.drawString("LIGHTSPEED.", 54+x, 54+x);
					}
				}
			} else if (U.titleCascade <= 512) {
				for (int x=1; x<=256; x+=5) {
					if (x < U.titleCascade-256) {
						continue;
					} else {
						g2.setColor(Color.white);
					}
				    g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, x/3));
					g2.drawString("LIGHTSPEED.", 50+x, 50+x);
					if (x == 256) {
					    g2.setColor(Color.gray);
						g2.drawString("LIGHTSPEED.", 50+x, 50+x);
						g2.drawString("LIGHTSPEED.", 58+x, 58+x);
					    g2.setColor(Color.white);
						g2.drawString("LIGHTSPEED.", 54+x, 54+x);
					}
				}
			} else {
			    g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 256/3));
			    g2.setColor(Color.gray);
				g2.drawString("LIGHTSPEED.", 50+256, 50+256);
				g2.drawString("LIGHTSPEED.", 58+256, 58+256);
			    g2.setColor(Color.white);
				g2.drawString("LIGHTSPEED.", 54+256, 54+256);
			}
			U.titleCascade += 5;
		} else { //CREATIVE, GAME ON, GAME OVER
			g2.translate(U.world_x_pixels2, U.world_y_pixels2);

			if (U.option_Length == true) {
				doLengthContractionTransform(g2);
			}

			for (final BackgroundStar _s : U.list_of_stars) {
				_s.Render(g2);
			}

			//draw what player sees
			P.RenderFromLocation(g2,Math.round(U.player.x),Math.round(U.player.y));

			//If drawing everything
			if (U.show_all_locations == true) {
				synchronized (U.list_of_rectangles_sync) {
					for (final Rectangle _r : U.list_of_rectangles) {
						_r.RenderReal(g2);
					}
				}
				synchronized (U.list_of_debris_sync) {
					for (final Debris _d : U.list_of_debris_sync) {
						_d.RenderReal(g2);
					}
				}
			}
			if (U.show_light_cones == true) {
				P.RenderShells(g2);
			}

			U.player.Render(g2); //Draw players ship

			//Always want collision check to run, only act on it if game is running
			if (CollisionCheck() == true && U.currentMode == GameMode.GameOn) { 
				U.currentMode = GameMode.GameOver;
				U.show_all_locations = true;
			}
			
			//Blackout
			g2.setColor(Color.black);
			g2.fillRect(0 - U.world_x_pixels2 - 20*U.world_x_offset,
					0 - U.world_y_pixels2 - 5*U.world_y_offset,
					U.world_x_offset*20,
					U.world_y_pixels + 10*U.world_y_offset);
			g2.fillRect(0 + U.world_x_pixels2,
					0 - U.world_y_pixels2 - 5*U.world_y_offset,
					U.world_x_offset*20,
					U.world_y_pixels + 10*U.world_y_offset);
			g2.fillRect(0 - U.world_x_pixels2 - 5*U.world_x_offset,
					0 - U.world_y_pixels2 - 5*U.world_y_offset,
					U.world_x_pixels + 10*U.world_x_offset,
					U.world_y_offset*7);
			g2.fillRect(0 - U.world_x_pixels2 - 5*U.world_x_offset,
					0 + U.world_y_pixels2,
					U.world_x_pixels + 10*U.world_x_offset,
					U.world_y_offset*7);
			
			//Outer box
			g2.setColor(Color.white);
			g2.drawRect(0 - U.world_x_pixels2,
					0 - U.world_y_pixels2 + U.UI,
					U.world_x_pixels,
					U.world_y_pixels - U.UI);
//			g2.drawRect(0 - U.world_x_pixels2,
//				0 - U.world_y_pixels2 - U.world_y_pixels + U.UI ,
//				U.world_x_pixels,
//				U.world_y_pixels + U.world_y_pixels + U.world_y_pixels - U.UI);
//			g2.drawRect(0 - U.world_x_pixels2 - U.world_x_pixels,
//				0 - U.world_y_pixels2 + U.UI,
//				U.world_x_pixels + U.world_x_pixels + U.world_x_pixels,
//				U.world_y_pixels - U.UI);
			
		}
		
		g2.setTransform(U.af_none);
		g2.translate(0, -U.world_y_offset);
		B.Render(g2); //Do buttons
	}

	@Override
	public void run() {
		// lower ThreadPriority
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		// run a long while (true) this means in our case "always"
		while (true) {
			if (_TIME_OF_NEXT_TICK  >  System.currentTimeMillis()) {
				// too soon to repaint, wait...
			    try {
					Thread.sleep(Math.abs(_TIME_OF_NEXT_TICK - System.currentTimeMillis()));
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
			_TIME_OF_NEXT_TICK = System.currentTimeMillis() + Math.round(1000f/_DESIRED_TPS);

			++_TICK;
			if (U.currentMode == GameMode.GameOn || U.currentMode == GameMode.Creative) {
				Tick();
			}

			if (_TICK % _TICKS_PER_RENDER == 0) {
				++_FRAME;
				if (_FRAME % _DO_FPS_EVERY_X_FRAMES == 0) {
					_FPS =  Math.round(1./(System.currentTimeMillis() - _TIME_OF_LAST_FRAME)*1000.*_DO_FPS_EVERY_X_FRAMES);
					_TIME_OF_LAST_FRAME = System.currentTimeMillis();
				}
				repaint();
			}
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
	}

	@Override
	public void start() {
		th = new Thread (this);
		th.start ();
	}

	@Override
	public void stop() { }
	void Tick(){
		if (U.mouseClick == true) {
			if (++U.ticks_with_mouse_down > 0 && U.ticks_with_mouse_down % 50 == 0) {
				B.ProcessMouseClick();
			}
		}

		if (U.currentMode == GameMode.Creative
				&& U.CurMouse != null
				&& (U.mouseClick||U.mouseDrag)
				&& U.CurMouse.y > U.UI + U.world_y_offset
				&& U.CurMouse.y < U.world_y_pixels + U.world_y_offset
				&& U.CurMouse.x > 0 + U.world_x_offset
				&& U.CurMouse.x < U.world_x_pixels + U.world_x_offset) {
			U.player.x = (int) U.CurMouse.getX() - (U.world_x_pixels2) - U.world_x_offset;
			U.player.y = (int) U.CurMouse.getY() - (U.world_y_pixels2) - U.world_y_offset;
			U.player.vx = 0;
			U.player.vy = 0;
			U.player.a = 0;//(float) Math.PI/2f;
		}

		if (N == true) {
			U.player.accelerate(+1);
		}
		if (E == true) {
			U.player.changeDirection(+1);
		}
		if (S == true) {
			U.player.accelerate(-1);
		}
		if (W == true) {
			U.player.changeDirection(-1);
		}

		U.player.Walk();

		++U.shellTime;

		//break point
		//if (_TICK%U.time_dilation == 0) return;

		synchronized (U.list_of_rectangles_sync) {
			for (final Rectangle _r : U.list_of_rectangles_sync) {
				_r.Walk();
				_r.Tick(_TICK);
			}
		}
		synchronized (U.list_of_debris_sync) {
			for (final Debris _d : U.list_of_debris_sync) {
				_d.Walk();
				_d.Tick(_TICK);
			}
		}

		if (U.currentMode == GameMode.GameOn) {
			//levels and difficulty
			if (_TICK % U.TicksPerLevel == 0) {
				++U.Level;
			}
			if (U.c_pixel > U.min_c) {
				U.c_pixel -= U.c_red;
			}
			if (U.velocity < U.max_v_multiplier) {
				U.velocity += U.v_inc;
			}
		}

		P.Cleanup(); //photon manager cleanup
		DebrisCleanup();
	}
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


}

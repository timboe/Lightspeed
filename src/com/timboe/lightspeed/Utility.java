package com.timboe.lightspeed;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public final class Utility {
	private static final Utility singleton = new Utility();

	public static Utility GetUtility() {
		return singleton;
	}

	public final Random R = new Random(); // All randoms come from this object
	float version_number = 1.0f;
	AffineTransform af_none = null;
	LIGHTSPEED MAIN; // Pointer to main game class
	// world parameters
	final int world_x_pixels = 1000;
	final int world_y_pixels = 600;
	final int world_x_pixels2 = world_x_pixels / 2;
	final int world_y_pixels2 = world_y_pixels / 2;
	final int world_x_offset = (int) (world_x_pixels * 0.03);
	final int world_y_offset = (int) (world_y_pixels * 0.05);

	private final LinkedList<Rectangle> list_of_rectangles = new LinkedList<Rectangle>();
	private final LinkedList<Debris> list_of_debris = new LinkedList<Debris>();
	public Collection<Debris> list_of_debris_sync = Collections
			.synchronizedCollection(list_of_debris);
	public Collection<Rectangle> list_of_rectangles_sync = Collections
			.synchronizedCollection(list_of_rectangles);
	public LinkedList<BackgroundStar> list_of_stars = new LinkedList<BackgroundStar>();
	// Star list never changes so does not need synchronisation

	// user interface pixels
	public int UI = 60;

	// Ship pointer
	public PlayerShip player;

	// Size at which photon shells may be culled
	public float max_radius = (float) Math.hypot(world_x_pixels, world_y_pixels - UI);

	// VELOCITY MULTIPLIER FOR RECTANGLES
	private float velocity = 1f;
	// SPEED OF LIGHT
	private float c_pixel = 1f; // SPEED OF LIGHT!
	final public float granularity = 15f; // how often to drop a light cone (12
											// works)

	public int GID = 0; // Global ID

	public int twinkle_speed = 5;
	public float twinkle_prob = 0.001f;
	public int twinkle_stars = 500;
	float length_cont_red = 0.9f; //Length contraction reduction % (to be more playable)
	int titleCascade = 1;
	int NDebris = 3;

	int superLumiSpikeSize = 10;
	int superLumiSpikes = 5;
	public int shellTime = 0; // Keep track of how big light cones have got
	GameMode currentMode = GameMode.Title;

	int Level = 1;

	int TicksPerLevel = 500;

	int Lives = 0;

	int MaxLives = 7;
	int nEnemies = 50;
	float min_c = 0.75f; // arcade minimum C
	float c_red = 0.00001f * 0.9f; // arcade speed of reducing C (tweak with
	// second value)
	float max_v_multiplier = 1f; // arcade maximum asteroid multiplier
	float v_inc = 1.5f * c_red; // arcade speed to increase asteroids
	
	// mouse location
	public Point CurMouse;
	int ticks_with_mouse_down;
	boolean mouseClick = false;
	boolean mouseDrag = false;
	private boolean option_Doppler = true;
	private boolean option_Length = true;
//	boolean option_Time = false;
	private boolean option_Torus = true;
	boolean show_light_cones = false;
	boolean show_all_locations = false;

	public Color default_colour = new Color(128, 0, 128);
	int highScore = 0;
	private Utility() {

	}
	public float getC() {
		return c_pixel;
	}
	
	public boolean getDoppler() {
		return option_Doppler;
	}
	public boolean getLength() {
		return option_Length;
	}
	public boolean getTorus() {
		return option_Torus;
	}
	public void flipDoppler() {
		option_Doppler = !option_Doppler;
	}
	public void flipLength() {
		option_Length = !option_Length;
	}
	public void flipTorus() {
		option_Torus = !option_Torus;
	}
	
	public int getNRectangles() {
		return list_of_rectangles.size();
	}

	public float getV() {
		return velocity;
	}
	public void modifyC(float mod) {
		setC(c_pixel + mod);
	}

	// rectangles need updating if this canges
	public void modifyV(float mod) {
		setV(velocity + mod);
	}

	// Change to speed of light, player object must be updated!
	public void setC(float C) {
		if (C > 1f) {
			C = 1f;
		} else if (C < 0.2f) {
			C = 0.2f;
		}
		c_pixel = C;
		if (player != null) {
			player.ChangeFrame();
		}
	}

	public void setV(float V) {
		if (V > 1f) {
			V = 1f;
		} else if (V < 0.f) {
			V = 0f;
		}
		velocity = V;
		synchronized (list_of_rectangles_sync) {
			for (final Rectangle R : list_of_rectangles_sync) {
				R.ChangeFrame();
			}
		}
	}

}

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

    public final Random R = new Random(); //All randoms come from this object

    AffineTransform af_none = null;

	LIGHTSPEED MAIN; //Pointer to main game class
	//world parameters
	final int world_x_pixels = 1000;
	final int world_y_pixels = 600;
	final int world_x_pixels2 = world_x_pixels/2;
	final int world_y_pixels2 = world_y_pixels/2;
	final int world_x_offset = (int) (world_x_pixels * 0.01); 
	final int world_y_offset = (int) (world_y_pixels * 0.05); 


	public int UI = 60;
	//player temp params
	PlayerShip player;
	final float player_max_v = 0.99f;

	final float player_acceleration = 0.005f;
	public float max_radius = (float) Math.hypot(world_x_pixels, world_y_pixels-UI);

	public float velocity = 1f; //multiplier for rectangles
	public float c_pixel = 1f; //SPEED OF LIGHT!

	final public int granularity = 12; //how often to drop a light cone

	public int GID = 0; //Global ID
	public int twinkle_speed = 5;
	public float twinkle_prob = 0.001f;

	public int twinkle_stars = 500;

	public float doppler_range = 1.0f; //Relative doppler range -x to +x to map Blue to Red colour

	LinkedList<Rectangle> list_of_rectangles = new LinkedList<Rectangle>();
	LinkedList<BackgroundStar> list_of_stars = new LinkedList<BackgroundStar>();
	LinkedList<Debris> list_of_debris = new LinkedList<Debris>();
	Collection<Debris> list_of_debris_sync = Collections.synchronizedCollection(list_of_debris);
	Collection<Rectangle> list_of_rectangles_sync = Collections.synchronizedCollection(list_of_rectangles);

	int titleCascade = 1;

	int NDebris = 5;
	
	int superLumiSpikeSize = 10;
	int superLumiSpikes=5;
	
//	public float time_dilation_X = 1;
//	public float time_dilation_Y = 1;
//	public int time_dilation = 1000;

	public int shellTime = 0; //Keep track of how big light cones have got
	
	boolean show_light_cones = false;
	boolean show_all_locations = false;
	
	GameMode currentMode = GameMode.Title;
	
	int Level = 1;
	int TicksPerLevel = 500;
	int Lives = 0;
	int MaxLives = 7;
	float min_c = 0.75f; //arcade minimum C
	float c_red = 0.00001f * 0.9f; //arcade speed of reducing C (tweak with second value)
	float max_v_multiplier = 2.25f; //arcade maximum asteroid multiplier
	float v_inc = 2.5f*c_red; //arcade speed to increase asteroids
	
	//mouse location
	public Point CurMouse;
	int ticks_with_mouse_down;
	boolean mouseClick = false;
	
	float gamma_max = 500f;
	float gamma_reduction_factor = 3000f;
	//500 5000 = 0.9
	//500 4000 = 0.875
	//500 3000 = 0.8333333333333334
	//500 2000 = 0.75
	//500 1000 = 0.5
	//1000 10000 = 0.5

	boolean mouseDrag = false;
	boolean option_Doppler = true;
	boolean option_Length = true;
	boolean option_Time = false;
	boolean option_Torus = false;

	public Color default_colour = new Color(128,0,128);

	int highScore=0;

	private Utility() {

	}

}

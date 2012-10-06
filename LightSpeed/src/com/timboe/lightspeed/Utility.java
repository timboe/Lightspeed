package com.timboe.lightspeed;

import java.util.LinkedList;
import java.util.Random;

public final class Utility {
	

	public final Random R = new Random();
	
	//world parameters
	final int world_x_pixels = 1000;
	final int world_y_pixels = 600;
	final int tile_pixels = 10;
	
	//player temp params
	PlayerShip player;
	final float player_max_v = 0.99f;
	final float player_acceleration = 0.005f;
	
	public float max_radius = 1160f;
	
	
	public float velocity = 1f;
	
	//public final float c_cell = 0.1f;
	public float c_pixel = 1f;
	
	final public int granularity = 12;
	
	public int GID = 0;
	
	final int cells_x = world_x_pixels/tile_pixels;
	final int cells_y = world_y_pixels/tile_pixels;
	
	public int twinkle_speed = 5;
	public float twinkle_prob = 0.0001f;
	
	private static final Utility singleton = new Utility();
	
	public float doppler_range = 1.0f; //Gamma range -x to +x to map Blue to Red colour 
	
	LinkedList<Rectangle> list_of_rectangles = new LinkedList<Rectangle>();
	LinkedList<BackgroundStar> list_of_stars = new LinkedList<BackgroundStar>();

	int superLumiSpikeSize = 10;
	int superLumiSpikes=5;
	
	public float gamma_range = 1f; //small, not much distoprtion.  big, lots of distortion
	public float gamma_suppression = 0.1f; //speed of distortion
	
	public float time_dilation = 1;
	
	public int shellTime = 0;
	
	
	
	boolean debug = false;
	boolean show_all_locations = false;

	boolean GameOn = true;
	
	private Utility() {
		
	}

	public static Utility GetUtility() {
		return singleton;
	}
	
}

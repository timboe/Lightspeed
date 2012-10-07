package com.timboe.lightspeed;

import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public final class Utility {
	

	public final Random R = new Random();
	
    AffineTransform af_none = null;

	
	//world parameters
	final int world_x_pixels = 1000;
	final int world_y_pixels = 600;

	final int world_x_pixels2 = world_x_pixels/2;
	final int world_y_pixels2 = world_y_pixels/2;
	
	//player temp params
	PlayerShip player;
	final float player_max_v = 0.99f;
	final float player_acceleration = 0.005f;
	
	public float max_radius = 1160f;
	
	
	public float velocity = 1f;
	
	//public final float c_cell = 0.1f;
	public float c_pixel = 1f;
	
	final public int granularity = 20;
	
	public int GID = 0;
	
	public int twinkle_speed = 5;
	public float twinkle_prob = 0.0001f;
	public int twinkle_stars = 50;
	
	private static final Utility singleton = new Utility();
	
	public float doppler_range = 1.0f; //Gamma range -x to +x to map Blue to Red colour 
	
	LinkedList<Rectangle> list_of_rectangles = new LinkedList<Rectangle>();
	LinkedList<BackgroundStar> list_of_stars = new LinkedList<BackgroundStar>();
	LinkedList<Debris> list_of_debris = new LinkedList<Debris>();
	Collection<Debris> list_of_debris_sync = Collections.synchronizedCollection(list_of_debris);
	
	int NDebris = 10;

	int superLumiSpikeSize = 10;
	int superLumiSpikes=5;
	
	public float gamma_range = 1f; //small, not much distoprtion.  big, lots of distortion
	public float gamma_suppression = 0.1f; //speed of distortion
	
	public float time_dilation = 1;
	
	public int shellTime = 0;
	
	public int UI = 50;
	
	boolean debug = false;
	boolean show_all_locations = false;

	boolean GameOn = true;
	
	private Utility() {
		
	}

	public static Utility GetUtility() {
		return singleton;
	}
	
}

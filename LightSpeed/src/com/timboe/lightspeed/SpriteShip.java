package com.timboe.lightspeed;

import java.awt.Graphics2D;
import java.util.LinkedList;

//THIS CLASS WAS AN AWFULL IDEA

public class SpriteShip {

	private Utility U = Utility.GetUtility();
	LinkedList<Rectangle> myShapes = new LinkedList<Rectangle>();
	
	float x;
	float y;
	int r;
	float vel_x;
	float vel_y;
	
	void Tick(int _tick) {
		if (Math.abs(vel_x) != U.velocity) {
			if (vel_x > 0) vel_x = U.velocity;
			else vel_x = -U.velocity;
			if (vel_y > 0) vel_y = U.velocity;
			else vel_y = -U.velocity;

		}
		
		
		//check if need to change dir^n
		if (y - r < 0) { //off top
			vel_y = Math.abs(vel_y);
		} else if (y + r > U.world_y_pixels ) {
			vel_y = -Math.abs(vel_y);
		}
		
		if (x - r < 0) { //off left
			vel_x = Math.abs(vel_x);
		} else if (x + r > U.world_x_pixels ) {
			vel_x = -Math.abs(vel_x);
		}
		x += vel_x;
		y += vel_y;
		for (Rectangle _r : myShapes) {
			_r.ExternalMove(vel_x, vel_y);
			_r.Tick(_tick);
		}
	}
 	
	SpriteShip(int _x, int _y) {
		vel_x = U.velocity;
		vel_y = U.velocity;
		
		x = _x;
		y = _y;
		r = (5*4);
		//make the shape
//		int _s = 5;
//		int _r = 5;
//		myShapes.add( new Rectangle(_x - _r, _y - (3*_r), _s, new Color(112,146,190)) );//light blue
//		myShapes.add( new Rectangle(_x + _r, _y - (3*_r), _s, new Color(112,146,190)) );//light blue
//		
//		myShapes.add( new Rectangle(_x - _r, _y - (2*_r), _s, new Color(112,146,190)) );//light blue
//		myShapes.add( new Rectangle(_x + _r, _y - (2*_r), _s, new Color(112,146,190)) );//light blue
//		myShapes.add( new Rectangle(_x     , _y - (2*_r), _s, new Color(181,230,29 )) );//lime
//
//		myShapes.add( new Rectangle(_x - _r, _y - _r, _s, new Color(181,230,29 )) );//lime
//		myShapes.add( new Rectangle(_x + _r, _y - _r, _s, new Color(181,230,29 )) );//lime
//		myShapes.add( new Rectangle(_x     , _y - _r, _s, new Color(181,230,29 )) );//lime
//		
//		myShapes.add( new Rectangle(_x - (2*_r), _y, _r, new Color(163,73 ,164)) );//violet
//		myShapes.add( new Rectangle(_x - (1*_r), _y, _r, new Color(163,73 ,164)) );//violet
//		myShapes.add( new Rectangle(_x         , _y, _r, new Color(163,73 ,164)) );//violet
//		myShapes.add( new Rectangle(_x + (1*_r), _y, _r, new Color(163,73 ,164)) );//violet
//		myShapes.add( new Rectangle(_x + (2*_r), _y, _r, new Color(163,73 ,164)) );//violet
//		
//		myShapes.add( new Rectangle(_x - (2*_r), _y + _r, _s, new Color(163,73 ,164)) );//violet
//		myShapes.add( new Rectangle(_x - (1*_r), _y + _r, _s, new Color(63 ,72 ,204)) );//blue
//		myShapes.add( new Rectangle(_x         , _y + _r, _s, new Color(163,73 ,164)) );//violet
//		myShapes.add( new Rectangle(_x + (1*_r), _y + _r, _s, new Color(63 ,72 ,204)) );//blue
//		myShapes.add( new Rectangle(_x + (2*_r), _y + _r, _s, new Color(163,73 ,164)) );//violet
//		
//		myShapes.add( new Rectangle(_x - (3*_r), _y + (2*_r), _s, new Color(34 ,177,76 )) );//green
//		myShapes.add( new Rectangle(_x - (2*_r), _y + (2*_r), _s, new Color(34 ,177,76 )) );//green
//		myShapes.add( new Rectangle(_x - (1*_r), _y + (2*_r), _s, new Color(63 ,72 ,204)) );//blue
//		myShapes.add( new Rectangle(_x         , _y + (2*_r), _s, new Color(63 ,72 ,204)) );//blue
//		myShapes.add( new Rectangle(_x + (1*_r), _y + (2*_r), _s, new Color(63 ,72 ,204)) );//blue
//		myShapes.add( new Rectangle(_x + (2*_r), _y + (2*_r), _s, new Color(34 ,177,76 )) );//green
//		myShapes.add( new Rectangle(_x + (3*_r), _y + (2*_r), _s, new Color(34 ,177,76 )) );//green
//
//		myShapes.add( new Rectangle(_x - (3*_r), _y + (3*_r), _s, new Color(34 ,177,76 )) );//green
//		myShapes.add( new Rectangle(_x         , _y + (3*_r), _s, new Color(237,28 ,36 )) );//red
//		myShapes.add( new Rectangle(_x + (3*_r), _y + (3*_r), _s, new Color(34 ,177,76 )) );//green
		
	}
	
	void RenderReal(Graphics2D _g2) {
		for (Rectangle _r : myShapes) {
			_g2.setColor(_r.shape_color);
			_g2.drawRoundRect((int)_r.x, (int)_r.y, _r.shape_size, _r.shape_size, _r.shape_size/2, _r.shape_size/2);
		}
	}
	
}

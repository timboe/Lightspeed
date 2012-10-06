package com.timboe.lightspeed;

import java.awt.Color;
import java.awt.Graphics2D;

public class Rectangle extends DopplerObject {
	
	public int shape_size;
	
	private double a;
	private PhotonManager P = PhotonManager.GetPhotonManager();
	float speed;
	
	float dist_to_tick;
	
	
	int GID;
	
	Rectangle(float _x, float _y, int _shape_size, Color _shape_color) {
		super(_x, _y, 0, 0);
		
		shape_size = _shape_size;
		shape_color = _shape_color;
		
		a = U.R.nextFloat()*Math.PI;

		speed = 0.99f;
		
		GID = ++U.GID;
	}

	
	void Walk() {
		vx = (float) (speed * Math.cos(a) * U.velocity);
		vy = (float) (speed * Math.sin(a) * U.velocity);
		x += vx;
		y += vy;
		dist_to_tick += U.velocity + U.c_pixel;
		Constrain();
	}
	
	void ExternalMove(float _plus_x, float _plus_y) {
		x += _plus_x;
		y += _plus_y;
		dist_to_tick += Math.abs(_plus_x) + Math.abs(_plus_y) + U.c_pixel;
		//dist_to_tick += ;
		//Constrain();
	}
	
	void Tick(int _tick) {
		//Walk();
		if (dist_to_tick > U.granularity) {
		
			dist_to_tick = 0;
			P.AddShell( new PhotonShell(x, y, vx, vy, shape_size, SuperLumi, GID) );
		}
	}
	
	void Constrain() {
		if (x < 0) {
			a = +Math.PI - a;
		} else if (x + shape_size >= U.world_x_pixels) {
			a = -Math.PI - a;
		}
		
		if (y < 0) {
			a = 2*Math.PI - a;
		} else if (y + shape_size >= U.world_y_pixels) {
			a = -2*Math.PI - a;
		}
	}
	
	void RenderReal(Graphics2D _g2) {
		CalculateColour();
		float ss2 = shape_size/2f;
		_g2.setColor(shape_color);
		_g2.fillRoundRect((int)x, (int)y, shape_size, shape_size, (int)ss2, (int)ss2);
		DoSuperLumiSpikes(_g2, ss2);
	}
	


	
}

package com.timboe.lightspeed;

import java.awt.Color;
import java.awt.Graphics2D;

public class PlayerShip {
	private Utility U = Utility.GetUtility();
	
	public float x;
	public float y;
	
	public float vx;
	public float vy;
	
	private double a;
	
	private int r = 4;
	
	public PlayerShip(int _x, int _y) {
		x = _x + (U.world_x_pixels/2);
		y = _y + (U.world_y_pixels/2);
		a=Math.PI/2.;
	}
	
//	public void accelerate_V(DirectionEnum direction) {
//		double fraction = U.player_acceleration * (U.c_pixel - Math.abs(vy));
//		
//		if (direction == DirectionEnum.S && vy < U.player_max_v*U.c_pixel) {
//			vy += fraction;
//		} else if (direction == DirectionEnum.N && vy > -U.player_max_v*U.c_pixel){
//			vy -= fraction;
//		}
//	}
	
//	public void accelerate_H(DirectionEnum direction) {
//		double fraction = U.player_acceleration * (U.c_pixel - Math.abs(vx));
//		
//		if (direction == DirectionEnum.E && vx < U.player_max_v*U.c_pixel) {
//			vx += fraction;
//		} else if (direction == DirectionEnum.W && vy > -U.player_max_v*U.c_pixel){
//			vx -= fraction;
//		}
//	}
//	
	
	public void changeDirection(int dir) {
		if (dir > 0) {
			a += Math.PI * (3./360.);
		} else {
			a -= Math.PI * (3./360.);
		}
	}
	
	public void accelerate(int dir) {
		vx += Math.cos(a) * U.player_acceleration * dir;
		vy += Math.sin(a) * U.player_acceleration * dir;
		
	}
	
	void Walk() {
		x += vx;
		y += vy;
		Constrain();
	}
	
	void Constrain() {
		if (x < (0 - U.world_x_pixels/2) ) {
			vx = Math.abs(vx);
			a = +Math.PI - a;
		} else if (x + (2*r) >= U.world_x_pixels/2) {
			vx = -(Math.abs(vx));
			a = -Math.PI - a;
		}
		
		if (y < (0 - U.world_y_pixels/2) ) {
			vy = Math.abs(vy);
			a = 2*Math.PI - a;
		} else if (y + (2*r) >= U.world_y_pixels/2) {
			vy = -(Math.abs(vy));
			a = -2*Math.PI - a;
		}
		
		float speed = (float) Math.hypot(vx, vy);
		if (speed > U.c_pixel) {
			vx /= speed/U.c_pixel;
			vy /= speed/U.c_pixel;
		}
	}
	
	public float GetGamma(int dir) {
		//float velocity = (float) Math.hypot(vx, vy);
		float gamma;
		if (dir > 0) {
			gamma = (float) Math.abs(vx / ( 1. - ((vx*vx)/(U.c_pixel*U.c_pixel)) ));
		} else {
			gamma = (float) Math.abs(vy / ( 1. - ((vy*vy)/(U.c_pixel*U.c_pixel)) ));
		}
		if (gamma > U.gamma_range) gamma = U.gamma_range;
		return 1 - (gamma * U.gamma_suppression);
	}
	
	
	public void Render(Graphics2D _g2) {
		_g2.setColor(Color.white);
		_g2.fillOval(Math.round(x) - r, Math.round(y) - r, 2*r, 2*r);	
		int x2 = (int)Math.round(x + (3 * Math.cos(a+Math.PI)));
		int y2 = (int)Math.round(y + (3 * Math.sin(a+Math.PI)));
		_g2.setColor(Color.red);
		_g2.fillOval(x2 - 3, y2 - 3, 6, 6);	

	}

}

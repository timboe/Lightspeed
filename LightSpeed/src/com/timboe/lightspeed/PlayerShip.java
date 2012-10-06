package com.timboe.lightspeed;

import java.awt.Color;
import java.awt.Graphics2D;

public class PlayerShip {
	private Utility U = Utility.GetUtility();
	
	public float x;
	public float y;
	
	public double vx;
	public double vy;
	
	private double a;
	
	private int r = 4;
	
	public PlayerShip(int _x, int _y) {
		x = _x;
		y = _y;
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
		if (x < 0) {
			vx = Math.abs(vx);
		} else if (x + (2*r) >= U.world_x_pixels) {
			vx = -(Math.abs(vx));
		}
		
		if (y < 0) {
			vy = Math.abs(vy);
		} else if (y + (2*r) >= U.world_y_pixels) {
			vy = -(Math.abs(vy));
		}
		
		if (vx > U.c_pixel) {
			vx = U.c_pixel;
		} else if (vx < -U.c_pixel) {
			vx = -U.c_pixel;
		}
		
		if (vy > U.c_pixel) {
			vy = U.c_pixel;
		} else if (vy < -U.c_pixel) {
			vy = -U.c_pixel;
		}
		
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

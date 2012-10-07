package com.timboe.lightspeed;

import java.awt.Graphics2D;

public class PlayerShip {
	private Utility U = Utility.GetUtility();
	
	public float x;
	public float y;
	
	public float vx;
	public float vy;
	
	float a;
	
	private int r = 4;
	
	private ShipGraphic ship;
	
	boolean accelerating = false;
	
	int dmg;
	
	
	public PlayerShip(int _x, int _y) {
		x = _x + U.world_x_pixels2;
		y = _y + U.world_y_pixels2;
		a=(float) (Math.PI/2.);
		
		ship = new ShipGraphic(1);
	}
	
	public void changeDirection(int dir) {
		if (dir > 0) {
			a += Math.PI * (3./360.);
		} else {
			a -= Math.PI * (3./360.);
		}
	}
	
	public void Damage() {
		dmg = 20;
	}
	
	private boolean IsDamaged() {
		if (dmg > 0) {
			--dmg;
			return true;
		}
		return false;
	}
	
	public void accelerate(int dir) {
		vx += Math.cos(a) * U.player_acceleration * dir;
		vy += Math.sin(a) * U.player_acceleration * dir;
		accelerating = true;
		
	}
	
	void Walk() {
		x += vx;
		y += vy;
		Constrain();
	}
	
	void Constrain() {
		if (x < (0 - U.world_x_pixels2) ) {
			vx = Math.abs(vx);
			a = (float) (+Math.PI - a);
		} else if (x + (2*r) >= U.world_x_pixels2) {
			vx = -(Math.abs(vx));
			a = (float) (-Math.PI - a);
		}
		
		if (y < (0 - U.world_y_pixels2 + U.UI) ) {
			vy = Math.abs(vy);
			a = (float) (2*Math.PI - a);
		} else if (y + (2*r) >= U.world_y_pixels2) {
			vy = -(Math.abs(vy));
			a = (float) (-2*Math.PI - a);
		}
		
		float speed = (float) Math.hypot(vx, vy);
		if (speed >= U.c_pixel) {
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
		//WARNING - altering renderer
		ship.Render(_g2, Math.round(x), Math.round(y), a, accelerating, IsDamaged());
		accelerating = false;
	}

}

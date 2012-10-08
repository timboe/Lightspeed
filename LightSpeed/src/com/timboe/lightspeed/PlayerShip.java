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
		a= 0;//(float) (Math.PI/2.);
		
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
		if (U.option_Torus == true) {
			if (x < (0 - U.world_x_pixels2) )        x += U.world_x_pixels;
			else if (x + (2*r) >= U.world_x_pixels2) x -= U.world_x_pixels;
			if (y < (0 - U.world_y_pixels2 + U.UI) ) y += U.world_y_pixels - U.UI;
			else if (y + (2*r) >= U.world_y_pixels2) y -= U.world_y_pixels - U.UI;
		} else {
			if (x < (0 - U.world_x_pixels2) ) {
				vx = Math.abs(vx);
				a = (float) (+Math.PI - GetHeading() - Math.PI/2.);
			} else if (x + (2*r) >= U.world_x_pixels2) {
				vx = -(Math.abs(vx));
				a = (float) (-Math.PI - GetHeading() - Math.PI/2.);
			}
			
			if (y < (0 - U.world_y_pixels2 + U.UI) ) {
				vy = Math.abs(vy);
				a = (float) (2*Math.PI - GetHeading() + Math.PI/2.);
			} else if (y + (2*r) >= U.world_y_pixels2) {
				vy = -(Math.abs(vy));
				a = (float) (-2*Math.PI - GetHeading() + Math.PI/2.);
			}
		}
		
		
		

		
		float speed = (float) Math.hypot(vx, vy);
		if (speed >= U.c_pixel) {
			vx /= speed/U.c_pixel;
			vy /= speed/U.c_pixel;
		}
	}
	
	public double GetGamma() {
		double velocity = (float) Math.hypot(vx, vy);
		return (float) Math.abs(1. / ( 1. - ((velocity*velocity)/(U.c_pixel*U.c_pixel)) ));
	}
	
	public double GetGameGamma() {
		//It's Relativistic gamma which is mapped to effect magnitude
		double gamma = GetGamma();
		if (gamma > 500f) gamma = 500f;
		gamma /= 3000f;
		gamma = 1f - gamma;
		System.out.println("Debug game gamma: "+gamma); 
		return gamma;
	}
	
	public float GetHeading() {
		return (float) (Math.atan2(vx,vy));
	}
	
	
	public void Render(Graphics2D _g2) {
		//WARNING - altering renderer
		ship.Render(_g2, Math.round(x), Math.round(y), a, accelerating, IsDamaged());
		accelerating = false;
	}

}

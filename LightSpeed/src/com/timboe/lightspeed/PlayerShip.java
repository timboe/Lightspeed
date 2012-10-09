package com.timboe.lightspeed;

import java.awt.Graphics2D;

public class PlayerShip {
	private final Utility U = Utility.GetUtility();

	public float x;
	public float y;

	public float vx;
	public float vy;

	float a;
	private final int r = 4;
	private final ShipGraphic ship;
	boolean accelerating = false;
	int dmg;

	public PlayerShip(int _x, int _y) {
		x = _x + U.world_x_pixels2;
		y = _y + U.world_y_pixels2;
		a= 0;//(float) (Math.PI/2.);

		ship = new ShipGraphic(1);
	}

	public void accelerate(int dir) {
		vx += Math.cos(a) * U.player_acceleration * dir;
		vy += Math.sin(a) * U.player_acceleration * dir;
		accelerating = true;
		Constrain_V();
	}

	public void changeDirection(int dir) {
		if (dir > 0) {
			a += Math.PI * (3./360.);
		} else {
			a -= Math.PI * (3./360.);
		}
		Constrain_V();
	}

	void Constrain_Pos() {
		if (U.option_Torus == true) {
			if (x < (0 - U.world_x_pixels2) ) {
				x += U.world_x_pixels;
			} else if (x + (2*r) > U.world_x_pixels2) {
				x -= U.world_x_pixels;
			}
			if (y < (0 - U.world_y_pixels2 + U.UI) ) {
				y += U.world_y_pixels - U.UI;
			} else if (y + (2*r) > U.world_y_pixels2) {
				y -= U.world_y_pixels - U.UI;
			}
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
	}

	void Constrain_V() {
		final float speed = (float) Math.hypot(vx, vy);
		if (speed >= U.c_pixel) {
			vx /= speed/U.c_pixel;
			vy /= speed/U.c_pixel;
		}	
	}
	
	public void Damage() {
		dmg = 20;
	}

	public double GetGameGamma() {
		//It's Relativistic gamma which is mapped to effect magnitude
		double gamma = GetGamma();
		System.out.println("Debug gamma: "+gamma);

		if (gamma > U.gamma_max || gamma < -U.gamma_max || gamma != gamma) {
			gamma = U.gamma_max;
		}
		gamma /= U.gamma_reduction_factor;
		gamma = 1f - gamma;
		System.out.println("Debug game gamma: "+gamma);
		
		//System.out.println("log500(500) : " +  Math.log(5000) );
		
		//500 5000 = 0.9
		//500 4000 = 0.875
		//500 3000 = 0.8333333333333334
		//500 2000 = 0.75
		//500 1000 = 0.5
		//500 600 = 16666666666666663
		//500 500 = 0

		return gamma;
	}

	public double GetGamma() {
		final double velocity = (float) Math.hypot(vx, vy);
		return (float) Math.abs(1. / Math.sqrt( 1. - ((velocity*velocity)/(U.c_pixel*U.c_pixel)) ));
	}

	public float GetHeading() {
		return (float) (Math.atan2(vx,vy));
	}

	private boolean IsDamaged() {
		if (dmg > 0) {
			--dmg;
			return true;
		}
		return false;
	}

	public void Render(Graphics2D _g2) {
		//WARNING - altering renderer
		ship.Render(_g2, Math.round(x), Math.round(y), a, accelerating, IsDamaged());
		accelerating = false;
	}


	void Walk() {
		x += vx;
		y += vy;
		Constrain_Pos();
	}

}

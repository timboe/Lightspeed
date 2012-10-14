package com.timboe.lightspeed;

import java.awt.Graphics2D;

public class PlayerShip {
	private final Utility U = Utility.GetUtility();

	public float x;
	public float y;

	public float vx;
	public float vy;
	
	//test acceleration
//	float vel = 0f;
	float acc = 0.01f;
	//float E = 0f;
	float PX = 0;
	float PY = 0;
	float M = 1f;

	float a;
	private final int r = 4;
	private final ShipGraphic ship;
	boolean accelerating = false;
	int dmg;
	
	//int recalculate_tick;

	public PlayerShip(int _x, int _y) {
		x = _x + U.world_x_pixels2;
		y = _y + U.world_y_pixels2;
		a= (float) Math.toRadians(0);//(float) (Math.PI/2.);

		ship = new ShipGraphic(1);
	}
	
	public float GetRapidity() {
		float P = getP();
		float E = getE();
		return (float) (0.5f * Math.log((E + (P*U.c_pixel))/(E - (P*U.c_pixel))));
	}
	
	public float getE() {
		return (float) Math.hypot(getM(),getP());
	}
	
	public float getP() {
		return (float) Math.hypot(PX, PY);
	}


	public void accelerate(int dir) {
		//if (dir >0 ) 
		PX += acc * Math.cos(a);
		PY += acc * Math.sin(a);
		float vel = (float) (U.c_pixel * Math.tanh(GetRapidity()/U.c_pixel));
		
		float P = getP();
		float ratio = vel/P;
		
		vx = PX * ratio;//(float)(Math.cos(a) * vel);
		vy = PY * ratio;//(float)(Math.sin(a) * vel);
		
		
		
		//vx += Math.cos(a) * U.player_acceleration * dir;
		//vy += Math.sin(a) * U.player_acceleration * dir;
		//accelerating = true;
		//Constrain_V();
	}

	public void changeDirection(int dir) {
		if (dir > 0) {
			a += Math.PI * (3./360.);
		} else {
			a -= Math.PI * (3./360.);
		}
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

	
	public void Damage() {
		dmg = 20;
	}

	public float getM() {
		return (float) (M * GetGamma());
	}

	public double GetGamma() {
		final double velocity = (float) Math.hypot(vx, vy);
		
		//float vel = (float) (U.c_pixel * Math.tanh(GetRapidity()/U.c_pixel));
		
		float gamma = (float) Math.abs(1. / Math.sqrt( 1. - ((velocity*velocity)/(U.c_pixel*U.c_pixel)) ));
		
		//System.out.println(" gamma: "+gamma+" E:"+getE()+" cos(a)"+Math.cos(a));

		return gamma;
	}

	public double GetBeta() {
		final double velocity = (float) Math.hypot(vx, vy);
		return (velocity/U.c_pixel);
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

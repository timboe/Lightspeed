package com.timboe.lightspeed;

import java.awt.Graphics2D;

public class PlayerShip {
	private final Utility U = Utility.GetUtility();
	// Position, velocity components, velocity
	public float x;
	public float y;
	public float vx;
	public float vy;
	private float v;
	// Ship behaviour tweaked by changing its acceleration and rest mass
	float acceleration = 0.015f;
	float M = 5f; // Mass
	// These quantities need to be updated when changing inertial frames
	// (rotation doesn't count)
	private float E; // energy
	private float P; // momentum (scalar)
	private float PX; // momentum * X unit vector, signed
	private float PY; // momentum * Y unit vector, signed
	private double RAPIDITY; // Ship rapidity
	private double GAMMA; // Lorentz factor
	private double BETA; // Fractional velocity of c
	private float HEADING; //Angle of motion
	// Position and misc
	private float a; // Angle (ship pointing)
	private final int r; // Radius
	private final ShipGraphic ship; // Ship design
	private boolean accelerating = false; // Show acceleration graphic
	private int dmg; // Show damage graphic (for dmg frames)

	public PlayerShip(int _x, int _y) {
		x = _x + U.world_x_pixels2;
		y = _y + U.world_y_pixels2;
		ship = new ShipGraphic(1);
		r = 4;
		Reset();
	}

	public void accelerate(float offset, float multiplier) {
		PX += acceleration * Math.pow(U.getC(), 2) * Math.cos(getA() + offset) * multiplier;
		PY += acceleration * Math.pow(U.getC(), 2) * Math.sin(getA() + offset) * multiplier;
		// Multiplier allows front booster to be less powerful than the rear
		// booster
		P = (float) Math.hypot(PX, PY);
		ChangeFrame();
	}

	public void changeDirection(int dir) {
		if (dir > 0) {
			a = (float) (getA() + Math.PI * (3. / 360.));
		} else {
			a = (float) (getA() - Math.PI * (3. / 360.));
		}
	}

	public void ChangeFrame() {
		// Energy through relativistic energy-momentum relationship
		E = (float) Math.sqrt(Math.pow(M * U.getC() * U.getC(), 2)
				+ Math.pow(P * U.getC(), 2));
		// Energy and momentum define ship rapidity
		RAPIDITY = (float) (0.5f * Math.log((E + (P * U.getC()))
				/ (E - (P * U.getC()))));
		// Velocity is the hyperbolic tangent of rapidity
		v = (float) (U.getC() * Math.tanh(RAPIDITY / U.getC()));

		// Components vx and vy are scaled from the momentum vectors
		final float ratio_v_to_P = v / P;
		if (ratio_v_to_P == ratio_v_to_P) { // Check for NaN (at velocity === 0)
			vx = PX * ratio_v_to_P;
			vy = PY * ratio_v_to_P;
			HEADING = (float) (Math.atan2(vy, vx));
		}

		BETA = (v / U.getC());
		GAMMA = (float) Math.abs(1. / Math.sqrt(1. - BETA * BETA));
		// System.out.println("ratio_v_to_P:"+ratio_v_to_P);
	}

	private void Constrain() {
		if (U.getTorus() == true) {
			if (x < (0 - U.world_x_pixels2)) {
				x += U.world_x_pixels;
			} else if (x > U.world_x_pixels2) {
				x -= U.world_x_pixels;
			}
			if (y < (0 - U.world_y_pixels2 + U.UI)) {
				y += U.world_y_pixels - U.UI;
			} else if (y > U.world_y_pixels2) {
				y -= U.world_y_pixels - U.UI;
			}
		} else {
			if (x < (0 - U.world_x_pixels2)) {
				PX = Math.abs(PX);
				//a = ((float) (+Math.PI - getHeading()));
				ChangeFrame();
			} else if (x + (2 * r) >= U.world_x_pixels2) {
				PX = -(Math.abs(PX));
				//a = ((float) (-Math.PI - getHeading()));
				ChangeFrame();
			}

			if (y < (0 - U.world_y_pixels2 + U.UI)) {
				PY = Math.abs(PY);
				//a = ((float) (2 * Math.PI - getHeading()));
				ChangeFrame();
			} else if (y + (2 * r) >= U.world_y_pixels2) {
				PY = -(Math.abs(PY));
				//a = ((float) (-2 * Math.PI - getHeading()));
				ChangeFrame();
			}
		}
	}

	public void Damage() {
		dmg = 20;
	}

	public float getA() {
		return a;
	}

	public double getBeta() {
		return BETA;
	}

	public double getGamma() {
		return GAMMA;
	}

	public float getHeading() {
		return HEADING;
	}

	private boolean IsDamaged() {
		if (dmg > 0) {
			--dmg;
			return true;
		}
		return false;
	}

	public void Render(Graphics2D _g2) {
		ship.Render(_g2, Math.round(x), Math.round(y), getA(), accelerating,
				IsDamaged());
		accelerating = false;
	}

	public void Reset() {
		PX = 0f;
		PY = 0f;
		vx = 0f;
		vy = 0f;
		E = 0f;
		P = 0f;
		v = 0f;
		RAPIDITY = 0f;
		GAMMA = 1f;
		BETA = 0f;
		a = ((float) Math.toRadians(-90));
	}

	public void Walk() {
		x += vx;
		y += vy;
		Constrain();
	}

}

package com.timboe.lightspeed;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class PlayerShip {
	private Utility U = Utility.GetUtility();
	
	public float x;
	public float y;
	
	public float vx;
	public float vy;
	
	private float a;
	
	private int r = 4;
	
	private int scale = 1;
	
	boolean accelerating = false;
	
	Polygon ship = new Polygon();
	Color ship_color;
	
	public PlayerShip(int _x, int _y) {
		x = _x + U.world_x_pixels2;
		y = _y + U.world_y_pixels2;
		a=(float) (Math.PI/2.);
		
		ship.addPoint(0*scale  ,0*scale );
		ship.addPoint(10*scale ,0*scale );
		ship.addPoint(-5*scale ,5*scale );
		ship.addPoint(-2*scale ,0*scale );
		ship.addPoint(-5*scale ,-5*scale);
		ship.addPoint(10*scale ,0*scale );

		ship_color = new Color(122,111,55);
	}
	
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

//		int x2 = (int)Math.round(x + (3 * Math.cos(a+Math.PI)));
//		int y2 = (int)Math.round(y + (3 * Math.sin(a+Math.PI)));
//		_g2.setColor(Color.red);
//		_g2.fillOval(x2 - 3, y2 - 3, 6, 6);	
		


//		p.addPoint((int)Math.round(x + (-4 * Math.cos(a))), (int)Math.round(y + (-6 * Math.sin(a))));
//		p.addPoint((int)Math.round(x),                      (int)Math.round(y + ( 8 * Math.cos(a))));
//		p.addPoint((int)Math.round(x + ( 4 * Math.cos(a))), (int)Math.round(y + (-6 * Math.sin(a))));
//		p.addPoint((int)Math.round(x),                      (int)Math.round(y + (-4 * Math.cos(a))));
		


		//p.addPoint( (int)Math.round(x + (40 * Math.cos(a))), (int)Math.round(y - (20 * Math.sin(a))) );

		//WARNING - altering renderer
		//_g2.setTransform(U.af_none);
		_g2.translate(x, y);
		_g2.rotate(a);

		if (accelerating == true) {
			_g2.setColor(Color.orange);
			_g2.fillOval(-5*scale, -2*scale, 4*scale, 4*scale);	
			_g2.setColor(Color.red);
			for (int S=0; S < U.superLumiSpikes; ++S) {
				double randomAngle = (U.R.nextFloat() * Math.PI * 2);
				int sx = (int) (-3*scale + (scale * 5 * Math.cos(randomAngle)));
				int sy = (int) (0        + (scale * 5 * Math.sin(randomAngle)));
				_g2.drawLine(-3*scale, 0, sx, sy);
			}
		}
		accelerating = false;
		
		_g2.setColor(Color.green);
		_g2.fillPolygon(ship);
		_g2.setColor(Color.gray);
		_g2.fillOval(-2*scale, -2*scale, 4*scale, 4*scale);	
		_g2.setColor(Color.white);
		_g2.drawPolygon(ship);
		



	}

}

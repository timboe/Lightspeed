package com.timboe.lightspeed;

import java.awt.Color;
import java.awt.Graphics2D;

public class Rectangle extends DopplerObject {
	private final PhotonManager P = PhotonManager.GetPhotonManager();

	public int shape_size;
	public int shape_size2;

	float a;
	float speed;
	
	float time_dilation_factor;
	
	float dist_to_tick;
	int GID;
	
	Color shell_colour;

	Rectangle(float _x, float _y, int _shape_size, float _speed) {
		super(_x, _y, 0, 0, (short)0, (short)0);

		shape_size = _shape_size;
		shape_size2 = _shape_size/2;
		shape_color = U.default_colour;
		a = (float) (U.R.nextFloat()*Math.PI);;//(float) (Math.PI/2f);//0;//(float) (Math.PI/2f);//(float) (U.R.nextFloat()*Math.PI);
		speed = _speed;
		GID = ++U.GID;
		
		final int R = U.R.nextInt(255);
		final int G = U.R.nextInt(255);
		int B = 255 - R - G;
		if (B < 0) {
			B = 0;
		}
		shell_colour = new Color(R,G,B);
	}


	void Constrain() {
		if (U.option_Torus == true) {
			if (x + shape_size < (0 - U.world_x_pixels2)) {
				x += U.world_x_pixels;
			} else if (x >= U.world_x_pixels2) {
				x -= U.world_x_pixels;
			}
			if (y + shape_size < (0 - U.world_y_pixels2 + U.UI) ) {
				y += U.world_y_pixels - U.UI;
			} else if (y >= U.world_y_pixels2) {
				y -= U.world_y_pixels - U.UI;
			}
		} else {
			if (x < (0 - U.world_x_pixels2)) {
				a = (float) (+Math.PI - a);
			} else if (x + shape_size >= U.world_x_pixels2) {
				a = (float) (-Math.PI - a);
			}
			if (y < (0 - U.world_y_pixels2 + U.UI) ) {
				a = (float) (2*Math.PI - a);
			} else if (y + shape_size >= U.world_y_pixels2) {
				a = (float) (-2*Math.PI - a);
			}
		}
	}

//	void ExternalMove(float _plus_x, float _plus_y) {
//		x += _plus_x;
//		y += _plus_y;
//		dist_to_tick += Math.abs(_plus_x) + Math.abs(_plus_y) + U.c_pixel;
//	}

	void RenderReal(Graphics2D _g2) {
		if (U.option_Doppler == true) {
			SetDopplerColourMap();
		} else {
			shape_color = U.default_colour;
		}
		if (Math.hypot(vx, vy) > U.c_pixel) {
			shape_color = Color.yellow;
			DoSuperLumiSpikes(_g2, shape_size2);
		}
		_g2.setColor(shape_color);
		_g2.drawRoundRect(Math.round(x), Math.round(y), shape_size, shape_size, shape_size2, shape_size2);
	}

	void Tick(int _tick) {
		if (dist_to_tick > U.granularity) {
			dist_to_tick = 0;
			P.AddShell( new PhotonShell(x, y, vx, vy, (short)0, (short)0, shape_size, shell_colour, GID, false) );
			if (U.option_Torus == true) {
				//P.AddShell( new PhotonShell(x, y, vx, vy, (short)(x-U.world_x_pixels), (short)0,                    shape_size, GID, false) );
				//P.AddShell( new PhotonShell(x, y, vx, vy, (short)(x+U.world_x_pixels), (short)0,                    shape_size, GID, false) );
				//P.AddShell( new PhotonShell(x, y, vx, vy, (short)0,                    (short)(y-U.world_y_pixels), shape_size, GID, false) );
				//P.AddShell( new PhotonShell(x, y, vx, vy, (short)0,                    (short)(y+U.world_y_pixels), shape_size, GID, false) );
			}
		}
	}

	void Walk() {
		if (U.option_Time == true) {
			//work out length factor
			time_dilation_factor = (float) GetRelativeGamma(false); //relative to player, no inversion
			//if (time_dilation_factor > 0f) time_dilation_factor = 0f;
			time_dilation_factor = Math.abs(time_dilation_factor);
			time_dilation_factor *= 0.5; 
			time_dilation_factor = 1f - time_dilation_factor;
			System.out.println("RelGammaFalse:" + GetRelativeGamma(false) +" TDL Factor: "+time_dilation_factor);

		} else {
			time_dilation_factor = 1f;
		}
		//IS RELATIVE GAMMA!
		
		vx = (float) (speed * Math.cos(a) * U.velocity);
		vy = (float) (speed * Math.sin(a) * U.velocity);
		
		if (Math.hypot(vx, vy) < U.c_pixel) {
			vx *= time_dilation_factor;
			vy *= time_dilation_factor;
		}
		
		x += vx;
		y += vy;
		dist_to_tick += U.velocity + U.c_pixel;
		Constrain();
	}




}
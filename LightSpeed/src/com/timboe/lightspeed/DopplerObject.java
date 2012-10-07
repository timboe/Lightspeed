package com.timboe.lightspeed;

import java.awt.Color;
import java.awt.Graphics2D;

public class DopplerObject {
	protected Utility U = Utility.GetUtility();

	public float x;
	public float y;
	
	public float vx;
	public float vy;
	
	public Color shape_color;
	
	public boolean SuperLumi=false;
	
	DopplerObject(float _x, float _y, float _vx, float _vy) {
		x = _x;
		y = _y;
		vx = _vx;
		vy = _vy;
	}
	
	void CalculateColour() {
		double angle = Math.atan2((x - U.player.x), (y - U.player.y));
		//System.out.println("angle to player: "+ Math.toDegrees(angle));
		
		//calculate my velocity along this axis
		double my_vx_axis = vx * Math.sin(angle);
		double my_vy_axis = vy * Math.cos(angle);
		double my_axis_velocity = Math.hypot(my_vx_axis, my_vy_axis);
		
		double pl_vx_axis = U.player.vx * Math.sin(angle);
		double pl_vy_axis = U.player.vy * Math.cos(angle);
		double pl_axis_velocity = Math.hypot(pl_vx_axis, pl_vy_axis);	

		SuperLumi = false;
		double doppler = (my_axis_velocity-pl_axis_velocity) / ( 1 - ((my_axis_velocity*pl_axis_velocity)/(U.c_pixel*U.c_pixel)) );

		if ((my_vx_axis+my_vy_axis)+(pl_vx_axis+pl_vy_axis) < 0) doppler *= -1;

		if (doppler > U.doppler_range) doppler = U.doppler_range;
		else if (doppler < -U.doppler_range) doppler = -U.doppler_range;

		//System.out.println("speed to player: "+ pl_axis_velocity + " angle " + Math.toDegrees(angle) + " DOPPLER " + doppler);
		SetDopplerColourMap(doppler);

	}
	
	void SetDopplerColourMap(double doppler) {
		int fractionRed = (int) (((doppler + U.doppler_range) / (2 * U.doppler_range)) * 255);
		int fractionBlu = 255 - fractionRed;
		shape_color = new Color(fractionRed, 0, fractionBlu);
	}
	
	void DoSuperLumiSpikes(Graphics2D _g2, float ss2) {
		if (SuperLumi == true) {
			for (int S=0; S < U.superLumiSpikes; ++S) {
				double randomAngle = (U.R.nextFloat() * Math.PI * 2);
				int sx = (int) (x + ss2 + ((U.R.nextInt(U.superLumiSpikeSize) + 5) * Math.cos(randomAngle)));
				int sy = (int) (y + ss2 + ((U.R.nextInt(U.superLumiSpikeSize) + 5) * Math.sin(randomAngle)));
				_g2.drawLine((int)(x+ss2), (int)(y+ss2), sx, sy);
			}
		}
	}
}

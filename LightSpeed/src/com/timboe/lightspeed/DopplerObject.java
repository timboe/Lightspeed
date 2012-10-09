package com.timboe.lightspeed;

import java.awt.Color;
import java.awt.Graphics2D;

public class DopplerObject {
	protected Utility U = Utility.GetUtility();

	public float x;
	public float y;

	public float vx;
	public float vy;

	//for torus
	short x_offset;
	short y_offset;

	public Color shape_color;

	//public boolean SuperLumi=false;

	DopplerObject(float _x, float _y, float _vx, float _vy, short _xo, short _yo) {
		x = _x;
		y = _y;
		vx = _vx;
		vy = _vy;
		x_offset = _xo;
		y_offset = _yo;
	}

	double GetRelativeGamma(boolean doInversion) {
		final double angle = Math.atan2((x - U.player.x), (y - U.player.y));
		//System.out.println("angle to player: "+ Math.toDegrees(angle));

		//calculate my velocity along this axis
		final double my_vx_axis = vx * Math.sin(angle);
		final double my_vy_axis = vy * Math.cos(angle);
		final double my_axis_velocity = Math.hypot(my_vx_axis, my_vy_axis);

		final double pl_vx_axis = U.player.vx * Math.sin(angle);
		final double pl_vy_axis = U.player.vy * Math.cos(angle);
		final double pl_axis_velocity = Math.hypot(pl_vx_axis, pl_vy_axis);

		//SuperLumi = false;
		double doppler = (my_axis_velocity-pl_axis_velocity) / Math.sqrt( 1 - ((my_axis_velocity*pl_axis_velocity)/(U.c_pixel*U.c_pixel)) );
		//System.out.println("Debug game gamma: "+doppler);
		
		if (doInversion == true && (my_vx_axis+my_vy_axis)+(pl_vx_axis+pl_vy_axis) < 0) {
			doppler *= -1;
		}

		if (doppler > U.doppler_range) {
			doppler = U.doppler_range;
		} else if (doppler < -U.doppler_range) {
			doppler = -U.doppler_range;
		}
		
		return doppler;

		//System.out.println("speed to player: "+ pl_axis_velocity + " angle " + Math.toDegrees(angle) + " DOPPLER " + doppler);
	}

	void DoSuperLumiSpikes(Graphics2D _g2, float ss2) {
		//if (SuperLumi == true) {
			for (int S=0; S < U.superLumiSpikes; ++S) {
				final double randomAngle = (U.R.nextFloat() * Math.PI * 2);
				final int sx = (int) (x + ss2 + ((U.R.nextInt(U.superLumiSpikeSize) + 5) * Math.cos(randomAngle)));
				final int sy = (int) (y + ss2 + ((U.R.nextInt(U.superLumiSpikeSize) + 5) * Math.sin(randomAngle)));
				_g2.drawLine((int)(x+ss2), (int)(y+ss2), sx, sy);
			}
		//}
	}

	void SetDopplerColourMap() {
		int fractionRed = (int) (((GetRelativeGamma(true) + U.doppler_range) / (2 * U.doppler_range)) * 255);
		int fractionBlu = 255 - fractionRed;
		if (x_offset != (short)0 || y_offset != (short)0) {
			fractionRed /= 4;
			fractionBlu /= 4;
		}
		shape_color = new Color(fractionRed, 0, fractionBlu);
	}
}

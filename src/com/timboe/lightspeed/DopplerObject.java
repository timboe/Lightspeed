package com.timboe.lightspeed;

import java.awt.Color;
import java.awt.Graphics2D;

public class DopplerObject {
	protected Utility U = Utility.GetUtility();
	protected float x;
	protected float y;
	protected float vx;
	protected float vy;
	protected Color shape_color;

	protected DopplerObject(float _x, float _y, float _vx, float _vy,
			short _xo, short _yo) {
		x = _x;
		y = _y;
		vx = _vx;
		vy = _vy;
		// x_offset = _xo;
		// y_offset = _yo;
	}

	protected void DoSuperLumiSpikes(Graphics2D _g2, float ss2) {
		_g2.setColor(shape_color);
		for (int S = 0; S < U.superLumiSpikes; ++S) {
			final double randomAngle = (U.R.nextFloat() * Math.PI * 2);
			final int sx = (int) (x + ss2 + ((U.R.nextInt(U.superLumiSpikeSize) + 5) * Math
					.cos(randomAngle)));
			final int sy = (int) (y + ss2 + ((U.R.nextInt(U.superLumiSpikeSize) + 5) * Math
					.sin(randomAngle)));
			_g2.drawLine((int) (x + ss2), (int) (y + ss2), sx, sy);
		}
	}

	protected double GetRelativeDoppler() {
		final double angle = Math.atan2((x - U.player.x), (y - U.player.y));
		// System.out.println("angle to player: "+ Math.toDegrees(angle));

		// calculate my velocity along this axis
		final double my_vx_axis = vx * Math.sin(angle);
		final double my_vy_axis = vy * Math.cos(angle);
		final double my_axis_velocity = Math.hypot(my_vx_axis, my_vy_axis);

		final double pl_vx_axis = U.player.vx * Math.sin(angle);
		final double pl_vy_axis = U.player.vy * Math.cos(angle);
		final double pl_axis_velocity = Math.hypot(pl_vx_axis, pl_vy_axis);

		double doppler = (my_axis_velocity - pl_axis_velocity)
				/ Math.sqrt(1 - ((my_axis_velocity * pl_axis_velocity) / (U
						.getC() * U.getC())));
		// System.out.println("Debug game gamma: "+doppler);

		if (my_vx_axis + my_vy_axis + pl_vx_axis + pl_vy_axis < 0) {
			doppler *= -1;
		}

		if (doppler > 1f) {
			doppler = 1f;
		} else if (doppler < -1f) {
			doppler = -1f;
		}
		return doppler;

		// System.out.println("speed to player: "+ pl_axis_velocity + " angle "
		// + Math.toDegrees(angle) + " DOPPLER " + doppler);
	}

	protected void SetDopplerColourMap() {
		final int fractionRed = (int) (((GetRelativeDoppler() + 1f) / 2f) * 255);
		final int fractionBlu = 255 - fractionRed;
		// if (x_offset != (short)0 || y_offset != (short)0) {
		// fractionRed /= 4;
		// fractionBlu /= 4;
		// }
		shape_color = new Color(fractionRed, 0, fractionBlu);
	}
}

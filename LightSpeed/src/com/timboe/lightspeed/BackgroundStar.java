package com.timboe.lightspeed;

import java.awt.Color;
import java.awt.Graphics2D;

public class BackgroundStar {
	private Utility U = Utility.GetUtility();

	float x;
	float y;
	int twinkle = 0;
	int twinkle_size = 0;
	
	public BackgroundStar(float _x, float _y) {
		x = _x;
		y = _y;
	}
	
	public void Render(Graphics2D _g2) {
		_g2.setColor(Color.white);
		_g2.fillRect((int)x, (int)y, 1, 1);

		if (twinkle == 0 && U.R.nextFloat() < U.twinkle_prob ) {
			twinkle=1;
			twinkle_size=1;
		} else if (twinkle > 0) {
			_g2.fillRect((int)x+twinkle_size, (int)y, 1, 1);
			_g2.fillRect((int)x, (int)y+twinkle_size, 1, 1);
			_g2.fillRect((int)x-twinkle_size, (int)y, 1, 1);
			_g2.fillRect((int)x, (int)y-twinkle_size, 1, 1);
			++twinkle;
			if (twinkle%U.twinkle_speed == 0) ++twinkle_size;
			if (twinkle == U.twinkle_speed*4) twinkle = 0;
		}
	}
	
}

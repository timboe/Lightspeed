package com.timboe.lightspeed;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class ShipGraphic {
	private Utility U = Utility.GetUtility();

	Polygon ship = new Polygon();
	Color ship_color;
	int scale;
	
	public ShipGraphic(int _scale) {
		scale = _scale;
		ship.addPoint(0*scale  ,0*scale );
		ship.addPoint(10*scale ,0*scale );
		ship.addPoint(-5*scale ,5*scale );
		ship.addPoint(-2*scale ,0*scale );
		ship.addPoint(-5*scale ,-5*scale);
		ship.addPoint(10*scale ,0*scale );

		ship_color = new Color(122,111,55);	
	}
	
	public void Render(Graphics2D _g2, int x, int y, float a, boolean accelerating, boolean dmg) {
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
		
		_g2.setColor(Color.green);
		_g2.fillPolygon(ship);
		_g2.setColor(Color.gray);
		_g2.fillOval(-2*scale, -2*scale, 4*scale, 4*scale);	
		_g2.setColor(Color.white);
		_g2.drawPolygon(ship);
		
		if (dmg == true) {
			_g2.setColor(Color.red);
			for (int S=0; S < U.superLumiSpikes*2; ++S) {
				double randomAngle = (U.R.nextFloat() * Math.PI * 2);
				int sx = (int) (0 + (scale * 8 * Math.cos(randomAngle)));
				int sy = (int) (0 + (scale * 8 * Math.sin(randomAngle)));
				_g2.drawLine(0, 0, sx, sy);
			}
		}
		
		_g2.rotate(-a);
		_g2.translate(-x, -y);
	}

}
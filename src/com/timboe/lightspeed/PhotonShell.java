package com.timboe.lightspeed;

import java.awt.Color;
import java.awt.Graphics2D;

public class PhotonShell extends DopplerObject implements Comparable<PhotonShell> {

	private final Utility U = Utility.GetUtility();

	int pixel_size; //size of rectangle
	int pixel_size2; //size of rectangle/2
	int createTime; //used to calculate radius
	float radius; //radius of shell
	boolean dead; //to be cleaned up
	private boolean seen;
	int GID; //matches with a rectangle
	//int G2ID; //unique to this shell
	boolean isDebris;

	Color shell_color;

	PhotonShell(float _x, float _y, float _vx, float _vy, short _xo, short _yo, int _shape_size, Color _sc, int _GID, boolean _isDebris) {
		super(_x, _y, _vx, _vy, _xo, _yo);

		pixel_size = _shape_size;
		pixel_size2 = _shape_size/2;
		radius = 0f;
		dead = false;
		GID = _GID;
		//G2ID = ++U.GID;
		createTime = U.shellTime;
		isDebris = _isDebris;

//		final int R = U.R.nextInt(255);
//		final int G = U.R.nextInt(255);
//		int B = 255 - R - G;
//		if (B < 0) {
//			B = 0;
//		}
		shell_color = _sc;
	}

	@Override
	public int compareTo(PhotonShell toComp) {
		return GID - toComp.GID;
	}

	boolean GetDead() {
		return dead;
	}

	float GetRadius(){
		radius = ((U.shellTime - createTime) * U.c_pixel);
		if (U.option_Torus == false && radius > U.max_radius) {
			Kill();
		}
		return radius;
	}

	public boolean GetSeen() {
		return seen;
	}

	public void Kill() {
		dead = true;
	}

	void Render(Graphics2D _g2) {
		if (isDebris == true) {
			_g2.setColor(Color.gray);
			_g2.fillRoundRect(Math.round(x),
					Math.round(y),
					pixel_size,
					pixel_size,
					pixel_size2,
					pixel_size2);
			if (U.show_all_locations == true) {
				synchronized (U.list_of_debris_sync) {
					for (final Debris D : U.list_of_debris_sync) {
						if (D.GID == GID) {
							_g2.drawLine((int)(x+pixel_size2),
									(int)(y+pixel_size2),
									(int)(D.x+pixel_size2),
									(int)(D.y+pixel_size2));
							break;
						}
					}
				}
			}
			return;
		}

		if (U.option_Doppler == true) {
			SetDopplerColourMap();
		} else {
			shape_color = U.default_colour;
		}
		if (Math.hypot(vx, vy) > U.c_pixel) {
			shape_color = Color.yellow;
			DoSuperLumiSpikes(_g2, pixel_size2);
		}
		_g2.setColor(shape_color);
		_g2.fillRoundRect(Math.round(x), //+x_offset
				Math.round(y), //+y_offset
				pixel_size,
				pixel_size,
				pixel_size2,
				pixel_size2);
	}
	
	public void RenderLink(Graphics2D _g2) {
		for (final Rectangle R : U.list_of_rectangles_sync) {
			if (R.GID == GID) {
				_g2.drawLine((int)(x+pixel_size2), //+x_offset
						(int)(y+pixel_size2), //+y_offset
						(int)(R.x+pixel_size2), 
						(int)(R.y+pixel_size2));
				break;
			}
		}
	}
	
	public void RenderShell(Graphics2D _g2) {
		_g2.setColor(shell_color);
		_g2.drawOval((int)(x - GetRadius()), //-x_offset
				(int) (y - GetRadius()), //-y_offset
				(int) (2*GetRadius()),
				(int) (2*GetRadius()));

	}

	public void SetSeen() {
		seen = true;
	}

}

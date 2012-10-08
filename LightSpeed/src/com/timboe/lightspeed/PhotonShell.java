package com.timboe.lightspeed;

import java.awt.Color;
import java.awt.Graphics2D;

public class PhotonShell extends DopplerObject implements Comparable<PhotonShell> {

	private Utility U = Utility.GetUtility();
	
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
	
	PhotonShell(float _x, float _y, float _vx, float _vy, short _xo, short _yo, int _shape_size, int _GID, boolean _isDebris) {
		super(_x, _y, _vx, _vy, _xo, _yo);
		
		pixel_size = _shape_size;
		pixel_size2 = _shape_size/2;
		radius = 0f;
		dead = false;
		GID = _GID;
		//G2ID = ++U.GID;
		createTime = U.shellTime;
		isDebris = _isDebris;
		
		int R = U.R.nextInt(255);
		int G = U.R.nextInt(255);
		int B = 255 - R - G;
		if (B < 0) B = 0;
		shell_color = new Color(R,G,B);
	}
	
	public void SetSeen() {
		seen = true;
	}
	
	public boolean GetSeen() {
		return seen;
	}
	
	public void Kill() {
		dead = true;
	}
	
	boolean GetDead() {
		return dead;
	}
	
	float GetRadius(){
		radius = ((U.shellTime - createTime) * U.c_pixel);
		if (U.option_Torus == true && radius > U.max_radius_toroid) Kill();
		else if (U.option_Torus == false && radius > U.max_radius) Kill();
		return radius;
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
					for (Debris D : U.list_of_debris_sync) {
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
		
		if (U.option_Doppler == true) CalculateColour();
		else shape_color = U.default_colour;
		if (Math.hypot(vx, vy) > U.c_pixel) {
			shape_color = Color.yellow;
			DoSuperLumiSpikes(_g2, pixel_size2);
			//SuperLumi = true;
		}
		_g2.setColor(shape_color);
		_g2.fillRoundRect(Math.round(x+x_offset), 
				Math.round(y+y_offset), 
				pixel_size, 
				pixel_size, 
				pixel_size2, 
				pixel_size2);
	
		
		if (U.show_all_locations == true) {
			synchronized (U.list_of_rectangles_sync) {
				for (Rectangle R : U.list_of_rectangles_sync) {
					if (R.GID == GID) {
						_g2.drawLine((int)(x+x_offset+pixel_size2), 
								(int)(y+y_offset+pixel_size2), 
								(int)(R.x+pixel_size2), 
								(int)(R.y+pixel_size2));
						break;
					}
				}				
			}
		}
	}

	public int compareTo(PhotonShell toComp) {
		return GID - toComp.GID;
	}
	
}

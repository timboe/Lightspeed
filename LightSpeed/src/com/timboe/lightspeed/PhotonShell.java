package com.timboe.lightspeed;

import java.awt.Graphics2D;

public class PhotonShell extends DopplerObject implements Comparable<PhotonShell> {

	private Utility U = Utility.GetUtility();
//	private PhotonManager P;
//	Color debug_color;
	
	
	int pixel_size;

	
	int createTime;
	
	private float radius; //radius of shell
	
	boolean dead;	
	
	int GID;
	
	
	PhotonShell(float _x, float _y, float _vx, float _vy, int _shape_size, boolean _SuperLumi, int _GID) {
		super(_x,_y,_vx,_vy);
		
		pixel_size = _shape_size;
		
		SuperLumi = _SuperLumi;
		
		radius = 0f;
		dead = false;
		GID = _GID;
		
		createTime = U.shellTime;
	}
	
	boolean GetDead() {
		return dead;
	}
	
	float GetRadius(){
		radius = ((U.shellTime - createTime) * U.c_pixel);
		if (radius > 1160) dead = true;
		return radius;
	}
	
	void Render(Graphics2D _g2) {
		float ss2 = (pixel_size/2f);
		
		CalculateColour();
		_g2.setColor(shape_color);
		_g2.fillRoundRect(Math.round(x), Math.round(y), pixel_size, pixel_size, (int)ss2, (int)ss2);
	
		DoSuperLumiSpikes(_g2, ss2);
		
		if (U.show_all_locations == true) {
			for (Rectangle R : U.list_of_rectangles) {
				if (R.GID == GID) {
					_g2.drawLine((int)(x+ss2), (int)(y+ss2), (int)(R.x+ss2), (int)(R.y+ss2));
					break;
				}
			}
		}
	}

	public int compareTo(PhotonShell toComp) {
		return GID - toComp.GID;
	}
	
}

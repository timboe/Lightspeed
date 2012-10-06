package com.timboe.lightspeed;

import java.awt.Graphics2D;

public class PhotonShell extends DopplerObject implements Comparable<PhotonShell> {

	private Utility U = Utility.GetUtility();
	
	int pixel_size; //size of rectangle
	int createTime; //used to calculate radius
	float radius; //radius of shell
	boolean dead; //to be cleaned up
	private boolean seen;
	int GID; //matches with a rectangle
	
	PhotonShell(float _x, float _y, float _vx, float _vy, int _shape_size, boolean _SuperLumi, int _GID) {
		super(_x, _y, _vx, _vy);
		
		pixel_size = _shape_size;
		SuperLumi = _SuperLumi;
		radius = 0f;
		dead = false;
		GID = _GID;
		createTime = U.shellTime;
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
		if (radius > U.max_radius) Kill();
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

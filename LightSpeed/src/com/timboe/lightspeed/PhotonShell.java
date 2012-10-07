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
	boolean isDebris;
	
	PhotonShell(float _x, float _y, float _vx, float _vy, int _shape_size, boolean _SuperLumi, int _GID, boolean _isDebris) {
		super(_x, _y, _vx, _vy);
		
		pixel_size = _shape_size;
		pixel_size2 = _shape_size/2;
		SuperLumi = _SuperLumi;
		radius = 0f;
		dead = false;
		GID = _GID;
		createTime = U.shellTime;
		isDebris = _isDebris;
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

		if (isDebris == true) {
			_g2.setColor(Color.gray);
			_g2.fillRoundRect(Math.round(x), Math.round(y), pixel_size, pixel_size, pixel_size2, pixel_size2);
			return;
		}
		
		CalculateColour();
		_g2.setColor(shape_color);
		_g2.fillRoundRect(Math.round(x), Math.round(y), pixel_size, pixel_size, pixel_size2, pixel_size2);
	
		DoSuperLumiSpikes(_g2, pixel_size2);
		
		if (U.show_all_locations == true) {
			for (Rectangle R : U.list_of_rectangles) {
				if (R.GID == GID) {
					_g2.drawLine((int)(x+pixel_size2), (int)(y+pixel_size2), (int)(R.x+pixel_size2), (int)(R.y+pixel_size2));
					break;
				}
			}
		}
	}

	public int compareTo(PhotonShell toComp) {
		return GID - toComp.GID;
	}
	
}

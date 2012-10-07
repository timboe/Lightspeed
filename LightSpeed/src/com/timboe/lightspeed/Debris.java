package com.timboe.lightspeed;

import java.awt.Color;
import java.awt.Graphics2D;

public class Debris extends DopplerObject {

	private Utility U = Utility.GetUtility();
	private PhotonManager P = PhotonManager.GetPhotonManager();

	float speed;
	float dist_to_tick;
	int GID;
	boolean dead;
	
	Debris(Rectangle R) {
		super(R.x+R.shape_size2, R.y+R.shape_size2, R.vx, R.vy);
		
		vx *= 1+U.R.nextFloat();
		vy *= 1+U.R.nextFloat();
		
		float speed = (float) Math.hypot(vx, vy);
		if (speed >= U.c_pixel) {
			vx /= speed/U.c_pixel;
			vy /= speed/U.c_pixel;
		}
		
		dead = false;
		GID = ++U.GID;
	}
	
	public void Kill() {
		dead = true;
	}
	
	public float GetSmear() {
		return (1+((U.R.nextInt(20) - 10) * 0.2f));
	}
	
	public boolean GetDead() {
		return dead;
	}

	void Walk() {
		if (GetDead() == true) return;
		x += vx;
		y += vy;
		dist_to_tick += U.velocity + U.c_pixel;
		Constrain();
	}
	
	void Constrain() {
		if (x < (0 - U.world_x_pixels2)) 				Kill();
		else if (x + 2 >= U.world_x_pixels2) 			Kill();
		else if (y < (0 - U.world_y_pixels2 + U.UI) ) 	Kill();
		else if (y + 2 >= U.world_y_pixels2) 			Kill();
	}
	
	void Tick(int _tick) {
		if (GetDead() == true) return;
		if (dist_to_tick > U.granularity) {
			dist_to_tick = 0;
			P.AddShell( new PhotonShell(x, y, vx, vy, 4, false, GID, true) );
		}
	}
	
	public void RenderReal(Graphics2D _g2) {
		_g2.setColor(Color.gray);
		_g2.drawOval((int)x-1, (int)y-1, 2, 2);
	}
	
	
}

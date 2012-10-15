package com.timboe.lightspeed;

import java.awt.Color;
import java.awt.Graphics2D;

public class Debris extends DopplerObject {
	private final Utility U = Utility.GetUtility();
	private final PhotonManager P = PhotonManager.GetPhotonManager();
	private float speed;
	private float dist_to_tick;
	private final int GID;
	private boolean dead;
	private final int r;// radius

	public Debris(Rectangle R) {
		super(R.x + R.shape_size2, R.y + R.shape_size2, R.vx, R.vy, (short) 0,
				(short) 0);

		vx *= 1 + U.R.nextFloat();
		vy *= 1 + U.R.nextFloat();

		float speed = (float) Math.hypot(vx, vy);
		if (speed >= U.getC()) {
			vx /= speed / U.getC();
			vy /= speed / U.getC();
			speed = (float) Math.hypot(vx, vy);
		} else if (speed < 0.4f) {
			vx /= speed / 0.4f;
			vy /= speed / 0.4f;
			speed = (float) Math.hypot(vx, vy);
		}

		r = 4;
		dead = false;
		GID = ++U.GID;
	}

	public void Constrain() {
		if (x < (0 - U.world_x_pixels2)) {
			Kill();
		} else if (x + 2 * r >= U.world_x_pixels2) {
			Kill();
		} else if (y < (0 - U.world_y_pixels2 + U.UI)) {
			Kill();
		} else if (y + 2 * r >= U.world_y_pixels2) {
			Kill();
		}
	}

	public boolean GetDead() {
		return dead;
	}

	public int getGID() {
		return GID;
	}

	public float GetSmear() {
		return (1 + ((U.R.nextInt(20) - 10) * 0.3f));
	}

	public void Kill() {
		dead = true;
	}

	public void RenderReal(Graphics2D _g2) {
		_g2.setColor(Color.gray);
		_g2.drawOval(Math.round(x - r), Math.round(y - r), r * 2, r * 2);
	}

	public void Tick(int _tick) {
		if (GetDead() == true)
			return;
		if (dist_to_tick > U.granularity) {
			dist_to_tick = 0;
			P.AddShell(new PhotonShell(x, y, vx, vy, (short) 0, (short) 0, r,
					Color.gray, GID, true));
		}
	}

	public void Walk() {
		if (GetDead() == true)
			return;
		x += vx;
		y += vy;
		dist_to_tick += ((speed * 2f) + (U.getC() * 2f));
		Constrain();
	}

}

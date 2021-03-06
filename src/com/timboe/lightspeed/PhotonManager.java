package com.timboe.lightspeed;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

public class PhotonManager {
	private final Utility U = Utility.GetUtility();
	private static final PhotonManager singleton = new PhotonManager();
	public static PhotonManager GetPhotonManager() {
		return singleton;
	}
	private final LinkedList<PhotonShell> list_of_shells = new LinkedList<PhotonShell>();

	private final Collection<PhotonShell> list_of_shells_sync = Collections
			.synchronizedCollection(list_of_shells);

	private PhotonManager() {
	}

	public void AddShell(PhotonShell _ps) {
		synchronized (list_of_shells_sync) {
			list_of_shells_sync.add(_ps);
		}
	}

	public void Cleanup() {
		// Clear clutter
		final LinkedList<PhotonShell> ToRemove = new LinkedList<PhotonShell>();
		synchronized (list_of_shells_sync) {
			for (final PhotonShell _p : list_of_shells_sync) {
				if (_p.GetDead() == true) {
					ToRemove.add(_p);
				}
			}
		}
		synchronized (list_of_shells_sync) {
			list_of_shells_sync.removeAll(ToRemove);
		}
	}

	public void Clear() {
		synchronized (list_of_shells_sync) {
			list_of_shells_sync.clear();
		}
	}

	public void RenderFromLocation(Graphics2D _g2, float _x, float _y) {
		// get list of ting to draw
		final HashSet<PhotonShell> toDraw = new HashSet<PhotonShell>();
		synchronized (list_of_shells_sync) {
			for (final PhotonShell _p : list_of_shells_sync) {
				final float sep = Seperation(_x, _y, _p);
				if (sep < U.granularity / 2 && sep > -U.granularity / 2) {
					toDraw.add(_p);
					_p.SetSeen();
				} else if (U.currentMode == GameMode.GameOn
						&& U.getTorus() == false && _p.GetSeen() == true) {
					_p.Kill();
				}
			}

			// TreeSet<PhotonShell> toDraw = new TreeSet<PhotonShell>();
			// synchronized (list_of_shells_sync) {
			// for (PhotonShell _p : list_of_shells_sync) {
			// float sep = Seperation(_x , _y, _p);
			// if (sep < U.granularity && sep > -U.granularity) {
			// //is it the closest?
			// PhotonShell _comparison = toDraw.floor(_p);
			// if (_comparison != null && _comparison.GID == _p.GID) {
			// //I'm already in, which is closer?
			// if (Seperation(_x , _y, toDraw.floor(_p)) < sep) {
			// toDraw.remove(_comparison);
			// toDraw.add(_p);
			// }
			// } else {
			// toDraw.add( _p );
			// }
			// }
			// }
		}

		for (final PhotonShell _p : toDraw) {
			_p.Render(_g2);
			// _p.RenderShell(_g2);
			if (U.show_all_locations == true) {
				_p.RenderLink(_g2);
			}
		}

	}

	public void RenderShells(Graphics2D _g2) {
		synchronized (list_of_shells_sync) {
			for (final PhotonShell _p : list_of_shells_sync) {
				_p.RenderShell(_g2);
			}
		}
	}

	private float Seperation(float _x, float _y, PhotonShell _p) {
		return (float) Math.hypot((_x - _p.x), (_y - _p.y)) - _p.GetRadius();
	}

}

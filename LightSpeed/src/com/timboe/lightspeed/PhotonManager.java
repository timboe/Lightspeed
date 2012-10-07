package com.timboe.lightspeed;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

public class PhotonManager {

	private Utility U = Utility.GetUtility();
	
	private final LinkedList<PhotonShell> list_of_shells = new LinkedList<PhotonShell>();
    Collection<PhotonShell> list_of_shells_sync = Collections.synchronizedCollection(list_of_shells);

    public void AddShell(PhotonShell _ps) {
		synchronized (list_of_shells_sync) {
			list_of_shells_sync.add( _ps );
		}
    }
    
	public void Cleanup() {
		//Clear clutter
		LinkedList<PhotonShell> ToRemove = new LinkedList<PhotonShell>();
		synchronized (list_of_shells_sync) {
			for (PhotonShell _p : list_of_shells_sync) {
				if (_p.GetDead() == true) ToRemove.add(_p);
			}
		}
		synchronized (list_of_shells_sync) {
			list_of_shells_sync.removeAll(ToRemove);
		}
	}
	
	private static final PhotonManager singleton = new PhotonManager();
	
	private PhotonManager() {
	}
	
	public static PhotonManager GetPhotonManager() {
		return singleton;
	}
	
	float Seperation(int _x, int _y, PhotonShell _p) {
		return (float) Math.hypot((_x - _p.x), (_y - _p.y)) - _p.GetRadius();
	}
	
	public void RenderFromLocation(Graphics2D _g2, int _x, int _y) {
		//get list of ting to draw
		HashSet<PhotonShell> toDraw = new HashSet<PhotonShell>();
		synchronized (list_of_shells_sync) {
			for (PhotonShell _p : list_of_shells_sync) {
				float sep = Seperation(_x , _y, _p);
				if (sep < U.granularity/2 && sep > -U.granularity/2) {
					toDraw.add( _p );
					_p.SetSeen();
				} else if (_p.GetSeen() == true) {
					_p.Kill();
				}
			} 
			
//			TreeSet<PhotonShell> toDraw = new TreeSet<PhotonShell>();
//			synchronized (list_of_shells_sync) {
//				for (PhotonShell _p : list_of_shells_sync) {
//					float sep = Seperation(_x , _y, _p);
//					if (sep < U.granularity && sep > -U.granularity) {
//						//is it the closest?
//						PhotonShell _comparison = toDraw.floor(_p);
//						if (_comparison != null && _comparison.GID == _p.GID) {
//							//I'm already in, which is closer?
//							if (Seperation(_x , _y, toDraw.floor(_p)) < sep) {
//								toDraw.remove(_comparison);
//								toDraw.add(_p);
//							}
//						} else {
//							toDraw.add( _p );
//						}
//					}
//				}
		}
		
		for (PhotonShell _p : toDraw) {
			_p.Render(_g2);		
		}
		
	}
	
	void RenderShells(Graphics2D _g2) {
		synchronized (list_of_shells_sync) {
			for (PhotonShell _p : list_of_shells_sync) {
				if (_p.GID == 49) {
					float c = 1-(_p.radius/U.max_radius);
					_g2.setColor(new Color(c,c,c));
					_g2.drawOval((int)(_p.x - _p.GetRadius()),(int) (_p.y - _p.GetRadius()),(int) (2*_p.GetRadius()),(int) (2*_p.GetRadius()));
				}
			}
		}
	}

}

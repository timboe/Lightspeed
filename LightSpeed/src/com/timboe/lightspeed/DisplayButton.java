package com.timboe.lightspeed;

import java.awt.Color;
import java.awt.Graphics2D;

public class DisplayButton {
	private final Utility U = Utility.GetUtility();

	int x;
	int y;
	int w;
	int h;
	int b; //bevel
	int b2;
	String display;
	String display2;
	float var = -1;
	String var_str = "";
	int text_x = 10;
	int text_y = 22;

	boolean precision = true;
	private boolean Clickable = false;

	boolean isYesNo = false;
	private boolean yesNoValue;

	public DisplayButton(int _x, int _y, int _w, int _h, String _d, String _d2, boolean _c) {
		x = _x;
		y = _y;
		w = _w;
		h = _h;
		b = 6; //bevel
		b2 = b/2;
		display = _d;
		display2 = _d2;
		Clickable = _c;
	}

	public void AddValue(float _var) {
		SetValue(var + _var);
	}

	public boolean GetHover() {
		if (Clickable == false) return false;
		if (U.CurMouse == null) return false;
		if (U.CurMouse.x <= x) return false;
		if (U.CurMouse.x >= x+w) return false;
		if (U.CurMouse.y <= y) return false;
		if (U.CurMouse.y >= y+h) return false;
		return true;
	}

	public float GetValue() {
		return var;
	}

	public void Render(Graphics2D _g2) {
		final boolean hov = GetHover();
		if (hov == true) {
			_g2.setColor(Color.white);
		} else if (Clickable == true) {
			_g2.setColor(Color.gray);
		} else {
			_g2.setColor(Color.black);
		}
		_g2.fillRect(x, y, w, h);
		if (hov == true) {
			_g2.setColor(Color.black);
		} else {
			_g2.setColor(Color.white);
		}
		_g2.drawRect(x, y, w, h);
		_g2.drawRect(x+b2, y+b2, w-b, h-b);
		_g2.drawLine(x, y, x+b2, y+b2);
		_g2.drawLine(x, y+h, x+b2, y+h-b2);
		_g2.drawLine(x+w, y, x+w-b2, y+b2);
		_g2.drawLine(x+w, y+h, x+w-b2, y+h-b2);
		_g2.drawString(display + var_str + display2, x+text_x, y+text_y);

		if (isYesNo == true) {
			String yn = "ON";
			if (yesNoValue == false) {
				yn = "OFF";
			}
			_g2.drawString(yn, x+w/2, y+(h/2)+15);
		}


	}

	public void SetClickable(boolean _c) {
		Clickable = _c;
	}

	public void SetValue(float _var) {
		var = _var;
		if (precision == true) {
			var_str = String.format("%.2f", var);
		} else {
			var_str = String.format("%.0f", var);
		}
	}

	public void SetYesNoValue(boolean _yn) {
		yesNoValue = _yn;
	}

}

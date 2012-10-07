package com.timboe.lightspeed;

import java.awt.Graphics2D;

public class ButtonManager {
	private Utility U = Utility.GetUtility();
	private static final ButtonManager singleton = new ButtonManager();
	
	DisplayButton PlayingC;
	DisplayButton PlayingAsteroidSpeed;
	DisplayButton PlayingScore;
	DisplayButton PlayingLevel;
	ShipGraphic LivesShip;
	
	DisplayButton StartArcade;
	DisplayButton StartCreative;
	DisplayButton Start_Toggle_Doppler;
	DisplayButton Start_Toggle_LengthCon;
	DisplayButton Start_Toggle_TimeCon;

	DisplayButton Quit;
	
	private ButtonManager() {
		int b2x = 0   + 70;
		int b3x = b2x + 320;
		int b4x = b3x + 175;
		Quit = new DisplayButton(0, 0, b2x, U.UI, "Quit","", true);
		Quit.text_y *= 1.5;
		//Playing game
		PlayingC             = new DisplayButton(b2x, 0,      b3x-b2x, U.UI/2, "Speed of Light: ","", false);
		PlayingAsteroidSpeed = new DisplayButton(b2x, U.UI/2, b3x-b2x, U.UI/2, "Max Asteroid Speed: ","c", false);
		PlayingLevel         = new DisplayButton(b3x, 0,      b4x-b3x, U.UI/2, "Level: ","", false);
		PlayingScore         = new DisplayButton(b3x, U.UI/2, b4x-b3x, U.UI/2, "Score: ","", false);
		PlayingLevel.precision = false;
		PlayingScore.precision = false;
		//lives
		LivesShip = new ShipGraphic(3);
		//
		//title screen
		StartArcade            = new DisplayButton(50, 400, 200, 50, "Start Arcade", "", true);
		StartCreative          = new DisplayButton(50, 500, 200, 50, "Start Creative", "", true);
		Start_Toggle_Doppler   = new DisplayButton(400, 400, 250, 50, "Doppler Shift", "", true);
		Start_Toggle_LengthCon = new DisplayButton(700, 400, 250, 50, "Length Contraction", "", true);
		Start_Toggle_TimeCon   = new DisplayButton(550, 500, 250, 50, "Time Dialation", "", true);
		Start_Toggle_Doppler.isYesNo = true;
		Start_Toggle_LengthCon.isYesNo = true;
		Start_Toggle_TimeCon.isYesNo = true;
		
	}

	public static ButtonManager GetButtonManager() {
		return singleton;
	}
	
	public void Render(Graphics2D _g2) {
		if (U.currentMode == GameMode.GameOn || U.currentMode == GameMode.GameOver) {
			Quit.Render(_g2);
			PlayingC.SetValue(U.c_pixel);
			PlayingC.Render(_g2);
			PlayingAsteroidSpeed.SetValue((U.velocity * 0.5f)/U.c_pixel);
			PlayingAsteroidSpeed.Render(_g2);
			PlayingLevel.SetValue(U.Level);
			PlayingLevel.Render(_g2);
			PlayingScore.Render(_g2);
			RenderLives(_g2);
		} else if (U.currentMode == GameMode.Title) {
			StartArcade.Render(_g2);
			StartCreative.Render(_g2);
			Start_Toggle_Doppler.yesNoValue = U.option_Doppler;
			Start_Toggle_LengthCon.yesNoValue = U.option_Length;
			Start_Toggle_TimeCon.yesNoValue = U.option_Time;
			Start_Toggle_Doppler.Render(_g2);
			Start_Toggle_LengthCon.Render(_g2);
			Start_Toggle_TimeCon.Render(_g2);
		}
		
	}
	
	public void RenderLives(Graphics2D _g2) {
		if (U.Lives == 0) return;
		int offset = 50;
		for (int L=1; L <= U.Lives; ++L) {
			LivesShip.Render(_g2, U.world_x_pixels - (L*offset), 5+U.UI/2, -(float)Math.PI/2f, false, false);
		}	
	}
	
	public void ProcessMouseClick() {
		if (U.currentMode == GameMode.GameOn || U.currentMode == GameMode.GameOver) {
			if (Quit.GetHover() == true) {
				U.currentMode = GameMode.Title;
				U.titleCascade = 1;
			}
		} else if (U.currentMode == GameMode.Title) {
			if (StartArcade.GetHover() == true) {
				U.MAIN.NewGame();
				U.currentMode = GameMode.GameOn;
			} else if (Start_Toggle_Doppler.GetHover() == true) {
				U.option_Doppler = !U.option_Doppler;
			} else if (Start_Toggle_LengthCon.GetHover() == true) {
				U.option_Length = !U.option_Length;
			} else if (Start_Toggle_TimeCon.GetHover() == true) {
				U.option_Time = !U.option_Time;
			}
		}
	}

}

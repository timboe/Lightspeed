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
	
	DisplayButton CreativeC;
	DisplayButton CreativeC_Plus;
	DisplayButton CreativeC_Minus;
	DisplayButton CreativeAsteroids;
	DisplayButton CreativeAsteroids_Plus;
	DisplayButton CreativeAsteroids_Minus;
	DisplayButton CreativeAsteroidV;
	DisplayButton CreativeAsteroidV_Plus;
	DisplayButton CreativeAsteroidV_Minus;
	DisplayButton CreativeShowTrue;
	DisplayButton CreativeShowLight;
	

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
		//creative mode
		CreativeAsteroids       = new DisplayButton(b2x,            0,      220,    U.UI/2, "Asteroids:", "", false);
		CreativeAsteroids_Plus  = new DisplayButton(b2x+220,        0,      U.UI/2, U.UI/2, "+", "", true);
		CreativeAsteroids_Minus = new DisplayButton(b2x+220+U.UI/2, 0,      U.UI/2, U.UI/2, "-", "", true);
		CreativeAsteroidV       = new DisplayButton(b2x,            U.UI/2, 220,    U.UI/2, "Asteroid V:", "", false);
		CreativeAsteroidV_Plus  = new DisplayButton(b2x+220,        U.UI/2, U.UI/2, U.UI/2, "+", "", true);
		CreativeAsteroidV_Minus = new DisplayButton(b2x+220+U.UI/2, U.UI/2, U.UI/2, U.UI/2, "-", "", true);
		CreativeC               = new DisplayButton(350,            0,      260,    U.UI/2, "Speed of Light:", "", false);
		CreativeC_Plus          = new DisplayButton(350+260,        0,      U.UI/2, U.UI/2, "+", "", true);
		CreativeC_Minus         = new DisplayButton(350+260+U.UI/2, 0,      U.UI/2, U.UI/2, "-", "", true);
		CreativeShowTrue        = new DisplayButton(670,            0,      175,    U.UI,   "True Position", "", true);
		CreativeShowLight       = new DisplayButton(670+175,        0,      155,    U.UI,   "Light Cones", "", true);
		CreativeAsteroids.precision = false;
		CreativeShowTrue.isYesNo = true;
		CreativeShowLight.isYesNo = true;
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
			Start_Toggle_Doppler.SetYesNoValue(U.option_Doppler);
			Start_Toggle_LengthCon.SetYesNoValue(U.option_Length);
			Start_Toggle_TimeCon.SetYesNoValue(U.option_Time);
			Start_Toggle_Doppler.Render(_g2);
			Start_Toggle_LengthCon.Render(_g2);
			Start_Toggle_TimeCon.Render(_g2);
		} else if (U.currentMode == GameMode.Creative) {
			Quit.Render(_g2);
			CreativeC.SetValue(U.c_pixel);
			CreativeC.Render(_g2);
			CreativeC_Plus.Render(_g2);
			CreativeC_Minus.Render(_g2);
			CreativeAsteroids.SetValue(U.list_of_rectangles.size());
			CreativeAsteroids.Render(_g2);
			CreativeAsteroids_Plus.Render(_g2);
			CreativeAsteroids_Minus.Render(_g2);
			CreativeAsteroidV.SetValue(U.velocity);
			CreativeAsteroidV.Render(_g2);
			CreativeAsteroidV_Plus.Render(_g2);
			CreativeAsteroidV_Minus.Render(_g2);
			CreativeShowTrue.SetYesNoValue(U.show_all_locations);
			CreativeShowLight.SetYesNoValue(U.show_light_cones);
			CreativeShowTrue.Render(_g2);
			CreativeShowLight.Render(_g2);
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
		if (U.currentMode == GameMode.GameOn 
				|| U.currentMode == GameMode.GameOver 
				|| U.currentMode == GameMode.Creative) {
			if (Quit.GetHover() == true) {
				U.currentMode = GameMode.Title;
				U.titleCascade = 1;
			}
		}
		if (U.currentMode == GameMode.Title) {
			if (StartArcade.GetHover() == true) {
				U.MAIN.NewGame();
				U.currentMode = GameMode.GameOn;
			} else if (StartCreative.GetHover() == true) {
				U.MAIN.NewCreative();
				U.currentMode = GameMode.Creative;
			} else if (Start_Toggle_Doppler.GetHover() == true) {
				U.option_Doppler = !U.option_Doppler;
			} else if (Start_Toggle_LengthCon.GetHover() == true) {
				U.option_Length = !U.option_Length;
			} else if (Start_Toggle_TimeCon.GetHover() == true) {
				U.option_Time = !U.option_Time;
			}
		} else if (U.currentMode == GameMode.Creative) {
			if (CreativeShowLight.GetHover() == true) {
				U.show_light_cones = !U.show_light_cones;
			} else if (CreativeShowTrue.GetHover() == true) {
				U.show_all_locations = !U.show_all_locations;
			} else if (CreativeAsteroidV_Plus.GetHover() == true) {
				U.velocity += 0.05f;
				if (U.velocity > 1.25f) U.velocity = 1.25f;
			} else if (CreativeAsteroidV_Minus.GetHover() == true) {
				U.velocity -= 0.05;
				if (U.velocity < 0.f) U.velocity = 0f;
			} else if (CreativeC_Plus.GetHover() == true) {
				U.c_pixel += 0.05f;
				if (U.c_pixel > 1f) U.c_pixel = 1f;
			} else if (CreativeC_Minus.GetHover() == true) {
				U.c_pixel -= 0.05;
				if (U.c_pixel < 0.2f) U.c_pixel = 0.2f;
			} else if (CreativeAsteroids_Plus.GetHover() == true) {
				synchronized (U.list_of_rectangles_sync) {
					U.list_of_rectangles_sync.add( new Rectangle(
							(int) (U.R.nextFloat()*(U.world_x_pixels - 10) - U.world_x_pixels2),
							(int) (U.R.nextFloat()*(U.world_y_pixels - 10 - U.UI) - U.world_y_pixels2 + U.UI), 
							7+U.R.nextInt(5)-2,
							U.velocity) );
				}
			} else if (CreativeAsteroids_Minus.GetHover() == true) {
				synchronized (U.list_of_rectangles_sync) {
					U.list_of_rectangles.pollLast();
				}
			}
		}
	}

}

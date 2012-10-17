package com.timboe.lightspeed;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class ButtonManager {
	public static ButtonManager GetButtonManager() {
		return singleton;
	}

	private final Utility U = Utility.GetUtility();
	private static final ButtonManager singleton = new ButtonManager();
	Font display_font = new Font(Font.MONOSPACED, Font.BOLD, 20);
	Button PlayingC;
	Button PlayingAsteroidSpeed;
	Button PlayingScore;
	Button PlayingLevel;
	//
	ShipGraphic LivesShip;
	Button StartArcade;
	Button StartCreative;
	Button Start_Toggle_Doppler;
	Button Start_Toggle_LengthCon;
	// Button Start_Toggle_TimeCon;
	//
	Button Start_Toggle_Toroid;
	Button CreativeC;
	Button CreativeC_Plus;
	Button CreativeC_Minus;
	Button CreativeAsteroids;
	Button CreativeAsteroids_Plus;
	Button CreativeAsteroids_Minus;
	Button CreativeAsteroidV;
	Button CreativeAsteroidV_Plus;
	Button CreativeAsteroidV_Minus;
	Button CreativeGamma;
	Button CreativeBeta;
	Button CreativeShowTrue;
	Button CreativeHeading;
	Button CreativePointing;
	int heading_x;
	int heading_y;
	int pointing_x;
	int pointing_y;
	int arrow_size;
	Button CreativeShowLight;
	//
	Button NewGame;
	//
	Button Quit;

	private ButtonManager() {
		final int b2x = 0 + 70;
		final int b3x = b2x + 320;
		final int b4x = b3x + 175;
		Quit = new Button(0, 0, b2x, U.UI, "Quit", "", true);
		// Quit.text_y *= 1.5;
		// End of game
		NewGame = new Button(800, 0, 200, U.UI, "New Game", "", true);
		// Playing game
		PlayingC = new Button(b2x, 0, b3x - b2x, U.UI / 2, "Speed of Light:",
				"", false);
		PlayingAsteroidSpeed = new Button(b2x, U.UI / 2, b3x - b2x, U.UI / 2,
				"Max Asteroid Speed:", "c", false);
		PlayingLevel = new Button(b3x, 0, b4x - b3x, U.UI / 2, "Level:", "",
				false);
		PlayingScore = new Button(b3x, U.UI / 2, b4x - b3x, U.UI / 2, "Score:",
				"", false);
		PlayingLevel.setPrecision(false);
		PlayingScore.setPrecision(false);
		// lives
		LivesShip = new ShipGraphic(3);
		//
		// title screen
		StartArcade = new Button(50, 400, 200, 50, "Arcade Mode", "", true);
		StartCreative = new Button(50, 500, 200, 50, "Sandbox Mode", "", true);
		Start_Toggle_Doppler = new Button(0+150, 0, 250, 50, "Doppler Shift", "",
				true);
		Start_Toggle_LengthCon = new Button(300+150, 0, 250, 50,
				"Length Contraction", "", true);
		// Start_Toggle_TimeCon = new Button(400, 0, 250, 50, "Time Dialation",
		// "", true);
		Start_Toggle_Toroid = new Button(600+150, 0, 250, 50, "Toroidal Universe",
				"", true);
		Start_Toggle_Doppler.setIsYesNo(true);
		Start_Toggle_LengthCon.setIsYesNo(true);
		// Start_Toggle_TimeCon.setIsYesNo(true);
		Start_Toggle_Toroid.setIsYesNo(true);
		// creative mode
		CreativeAsteroids = new Button(b2x, 0, 210, U.UI / 2, "Asteroids:", "",
				false);
		CreativeAsteroids_Minus = new Button(b2x + 210, 0, U.UI / 2, U.UI / 2,
				"-", "", true);
		CreativeAsteroids_Plus = new Button(b2x + 210 + U.UI / 2, 0, U.UI / 2,
				U.UI / 2, "+", "", true);
		CreativeAsteroidV = new Button(b2x, U.UI / 2, 210, U.UI / 2,
				"Asteroid V:", "c", false);
		CreativeAsteroidV_Minus = new Button(b2x + 210, U.UI / 2, U.UI / 2,
				U.UI / 2, "-", "", true);
		CreativeAsteroidV_Plus = new Button(b2x + 210 + U.UI / 2, U.UI / 2,
				U.UI / 2, U.UI / 2, "+", "", true);
		CreativeC = new Button(340, 0, 260, U.UI / 2, "Speed of Light:", "",
				false);
		CreativeC_Minus = new Button(340 + 260, 0, U.UI / 2, U.UI / 2, "-", "",
				true);
		CreativeC_Plus = new Button(340 + 260 + U.UI / 2, 0, U.UI / 2,
				U.UI / 2, "+", "", true);
		CreativeBeta = new Button(340, U.UI / 2, 260 / 2, U.UI / 2, "β:", "",
				false);
		CreativeGamma = new Button(340 + 260 / 2, U.UI / 2, 260 / 2, U.UI / 2,
				"γ:", "", false);
		CreativeShowTrue = new Button(660, 0, 170, U.UI, "True Position", "",
				true);
		CreativeShowLight = new Button(660 + 170, 0, 170, U.UI,
				"Photon Shells", "", true);
		CreativeHeading = new Button(340 + 260, U.UI / 2, U.UI / 2, U.UI / 2,
				"", "", false);
		CreativePointing = new Button(340 + 260 + U.UI / 2, U.UI / 2, U.UI / 2,
				U.UI / 2, "", "", false);
		CreativeAsteroids.setPrecision(false);
		CreativeShowTrue.setIsYesNo(true);
		CreativeShowLight.setIsYesNo(true);
		CreativeBeta.setHighPrecision(true);
		CreativeGamma.setHighPrecision(true);
		heading_x = 340 + 260 + U.UI / 4;
		heading_y = 3 * U.UI / 4;
		pointing_x = 340 + 260 + (U.UI / 4) + (U.UI / 2);
		pointing_y = 3 * U.UI / 4;
		arrow_size = U.UI / 5;
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
				U.flipDoppler();
			} else if (Start_Toggle_LengthCon.GetHover() == true) {
				U.flipLength();
//  		} else if (Start_Toggle_TimeCon.GetHover() == true) {
// 				U.option_Time = !U.option_Time;
			} else if (Start_Toggle_Toroid.GetHover() == true) {
				U.flipTorus();
			}
		} else if (U.currentMode == GameMode.Creative) {
			if (CreativeShowLight.GetHover() == true) {
				U.show_light_cones = !U.show_light_cones;
			} else if (CreativeShowTrue.GetHover() == true) {
				U.show_all_locations = !U.show_all_locations;
			} else if (CreativeAsteroidV_Plus.GetHover() == true) {
				U.modifyV(0.05f*U.getC());
			} else if (CreativeAsteroidV_Minus.GetHover() == true) {
				U.modifyV(-0.05f*U.getC());
			} else if (CreativeC_Plus.GetHover() == true) {
				U.modifyC(0.05f);
			} else if (CreativeC_Minus.GetHover() == true) {
				U.modifyC(-0.05f);
			} else if (CreativeAsteroids_Plus.GetHover() == true) {
				synchronized (U.list_of_rectangles_sync) {
					U.list_of_rectangles_sync
							.add(new Rectangle(
									(int) (U.R.nextFloat()
											* (U.world_x_pixels - 10) - U.world_x_pixels2),
									(int) (U.R.nextFloat()
											* (U.world_y_pixels - 10 - U.UI)
											- U.world_y_pixels2 + U.UI),
									7 + U.R.nextInt(5) - 2, 1f));
				}
			} else if (CreativeAsteroids_Minus.GetHover() == true) {
				Rectangle toRemove = null;
				synchronized (U.list_of_rectangles_sync) {
					for (final Rectangle R : U.list_of_rectangles_sync) {
						toRemove = R;
						break;
					}
				} 
				synchronized (U.list_of_rectangles_sync) {
					U.list_of_rectangles_sync.remove(toRemove);
				}
			}
		} else if (U.currentMode == GameMode.GameOver) {
			if (NewGame.GetHover() == true) {
				U.MAIN.NewGame();
				U.currentMode = GameMode.GameOn;
			}
		}
	}

	public void Render(Graphics2D _g2) {
		_g2.setFont(display_font);
		_g2.setColor(Color.black);
		_g2.fillRect(0, 0, U.world_x_pixels, U.UI);
		if (U.currentMode == GameMode.GameOver) {
			NewGame.Render(_g2);
			_g2.setColor(Color.white);
			if (PlayingScore.GetValue() > U.highScore) {
				U.highScore = (int) PlayingScore.GetValue();
				_g2.drawString("New High Score!", 590, (U.UI / 2) + 8);
			} else {
				_g2.drawString((int) (U.highScore - PlayingScore.GetValue())
						+ " Points From", 590, (U.UI / 2) - 3);
				_g2.drawString("A High Score.", 610, (U.UI / 2) + 17);
			}
		}
		if (U.currentMode == GameMode.GameOn
				|| U.currentMode == GameMode.GameOver) {
			Quit.Render(_g2);
			PlayingC.SetValue(U.getC());
			PlayingC.Render(_g2);
			PlayingAsteroidSpeed.SetValue(U.getV() / U.getC());
			PlayingAsteroidSpeed.Render(_g2);
			PlayingLevel.SetValue(U.Level);
			PlayingLevel.Render(_g2);
			PlayingScore.Render(_g2);
			RenderLives(_g2);
		} else if (U.currentMode == GameMode.Title) {
			StartArcade.Render(_g2);
			StartCreative.Render(_g2);
			Start_Toggle_Doppler.SetYesNoValue(U.getDoppler());
			Start_Toggle_LengthCon.SetYesNoValue(U.getLength());
			// Start_Toggle_TimeCon.SetYesNoValue(U.option_Time);
			Start_Toggle_Toroid.SetYesNoValue(U.getTorus());
			Start_Toggle_Doppler.Render(_g2);
			Start_Toggle_LengthCon.Render(_g2);
			// Start_Toggle_TimeCon.Render(_g2);
			Start_Toggle_Toroid.Render(_g2);
			_g2.setColor(Color.white);
			_g2.drawString(String.format("Version: %.1f", U.version_number),
					U.world_x_offset, 655);
		} else if (U.currentMode == GameMode.Creative) {
			Quit.Render(_g2);
			CreativeBeta.SetValue((float) U.player.getBeta());
			CreativeBeta.Render(_g2);
			CreativeGamma.SetValue((float) U.player.getGamma());
			CreativeGamma.Render(_g2);
			CreativeC.SetValue(U.getC());
			CreativeC.Render(_g2);
			CreativeC_Plus.Render(_g2);
			CreativeC_Minus.Render(_g2);
			CreativeAsteroids.SetValue(U.getNRectangles());
			CreativeAsteroids.Render(_g2);
			CreativeAsteroids_Plus.Render(_g2);
			CreativeAsteroids_Minus.Render(_g2);
			CreativeAsteroidV.SetValue(U.getV() / U.getC());
			CreativeAsteroidV.Render(_g2);
			CreativeAsteroidV_Plus.Render(_g2);
			CreativeAsteroidV_Minus.Render(_g2);
			CreativeShowTrue.SetYesNoValue(U.show_all_locations);
			CreativeShowLight.SetYesNoValue(U.show_light_cones);
			CreativeShowTrue.Render(_g2);
			CreativeShowLight.Render(_g2);
			CreativeHeading.Render(_g2);
			CreativePointing.Render(_g2);
			// Draw heading
			_g2.setColor(Color.RED);
			_g2.drawLine(
					heading_x,
					heading_y,
					(int) Math.round(heading_x
							+ (arrow_size * Math.cos(U.player.getA()))),
					(int) Math.round(heading_y
							+ (arrow_size * Math.sin(U.player.getA()))));
			_g2.setColor(Color.BLUE);
			_g2.drawLine(
					pointing_x,
					pointing_y,
					(int) Math.round(pointing_x
							+ (arrow_size * U.player.getBeta() * Math
									.cos(U.player.getHeading()))),
					(int) Math.round(pointing_y
							+ (arrow_size * U.player.getBeta() * Math
									.sin(U.player.getHeading()))));
		}
		// Do FPS
		_g2.setColor(Color.white);
		_g2.drawString("FPS: " + U.MAIN.getFPS(), 900, 655);

	}

	public void RenderLives(Graphics2D _g2) {
		if (U.Lives == 0)
			return;
		final int offset = 50;
		for (int L = 1; L <= U.Lives; ++L) {
			LivesShip.Render(_g2, U.world_x_pixels - (L * offset),
					5 + U.UI / 2, -(float) Math.PI / 2f, false, false);
		}
	}

}

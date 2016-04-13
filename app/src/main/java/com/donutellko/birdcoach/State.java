package com.donutellko.birdcoach;

import android.graphics.Bitmap;

/**
 * Created by donat on 4/13/16.
 */
public class State {

	public static States state = States.MAIN;
	public static boolean victory = true;

	public State() {
		super();
	}

	public static boolean isMoving() {
		return !(state == States.MAIN || state == States.MENU || state == States.GAME || state == States.LEVEL);
	}

	public static States MovingTo() {
		if (!isMoving()) return state;

		if (state == States.MAIN_MENU || state == States.GAME_MENU || state == States.LEVEL_MENU)
			return States.MENU;
		else if (state == States.MENU_GAME || state == States.LEVEL_GAME)
			return States.GAME;
		else if (state == States.GAME_LEVEL)
			return States.LEVEL;
		else if (state == States.MENU_MAIN)
			return States.MAIN;
		else return null;
	}

	public static States MovingFrom() {
		if (!isMoving()) return state;

		if (state == States.MAIN_MENU)
			return States.MAIN;
		else if (state == States.MENU_GAME || state == States.MENU_MAIN)
			return States.MENU;
		else if (state == States.GAME_LEVEL || state == States.GAME_MENU)
			return States.GAME;
		else if (state == States.LEVEL_GAME || state == States.LEVEL_MENU)
			return States.LEVEL;
		else return null;
	}

	public static void draw() {
		float allX = MainView.allX;
		float Width = MainView.Width;

		if (MovingFrom() == States.MAIN)
			MainView.drawB(mResources.scr1, allX, 0);
		else if (MovingFrom() == States.MENU)
			MainView.drawB(mResources.scr2, allX, 0);
		else if (MovingFrom() == States.GAME)
			MainView.drawB(mResources.josh, allX, 0);
		else if (MovingFrom() == States.LEVEL)
			MainView.drawB((victory) ? mResources.victory : mResources.lalka, allX, 0);

		if (MovingTo() == States.MAIN)
			MainView.drawB(mResources.scr1, allX + Width, 0);
		else if (MovingTo() == States.MENU)
			MainView.drawB(mResources.scr2, allX + Width, 0);
		else if (MovingTo() == States.GAME)
			MainView.drawB(mResources.josh, allX + Width, 0);
		else if (MovingTo() == States.LEVEL)
			MainView.drawB((victory) ? mResources.victory : mResources.lalka, allX + Width, 0);
	}


	public static void animate(int[] drawCounter) {
		float allX = MainView.allX;
		float Width = MainView.Width, Height = MainView.Height;

		if (State.MovingFrom() == States.MAIN || State.MovingTo() == States.MAIN) {
			float x = (State.MovingFrom() == States.MAIN) ? allX : allX + Width;

			if (drawCounter[0] > 0) drawB(mResources.scr1_blue, x, 0);
			else if (drawCounter[0] < -163) drawCounter[0] = 47;
			if (drawCounter[1] > 0) drawB(mResources.scr1_dark, x + Width / 4 - 1, 0);
			else if (drawCounter[1] < -75) drawCounter[1] = 52;
			if (drawCounter[2] > 0) drawB(mResources.scr1_red, x + Width / 2, 0);
			else if (drawCounter[2] < -32) drawCounter[2] = 29;
			if (drawCounter[3] > 0) drawB(mResources.scr1_purple, x + Width * 3 / 4 + 5, Height / 2);
			else if (drawCounter[3] < -59) drawCounter[3] = 148;

			MainView.canvas.drawText("Нажмите, чтобы продолжить...", x + 30, Height - Height / 22, mResources.textInfo);
		}

		if (State.MovingFrom() == States.MENU || State.MovingTo() == States.MENU) {
			float x = (State.MovingFrom() == States.MENU) ? allX : allX + Width;

			if (drawCounter[4] < -30) drawCounter[4] = 30;
			drawB((drawCounter[4] > 0) ? mResources.scr3_orange1 : mResources.scr3_orange2, x + Width / 4, 5);

			if (drawCounter[5] > 50)

			if (drawCounter[5] > 50) drawB(mResources.scr2_purple2, x, Height / 2);
			else if (drawCounter[5] < -100) drawCounter[5] = 100;
			else if (drawCounter[5] < -50) drawB(mResources.scr2_purple1, x + Width * 1 / 2, Height / 2);
		}

		MainView.drawCounter = drawCounter;
	}

	public static void drawB(Bitmap res, float x, float y) {
		MainView.canvas.drawBitmap(res, x, y, mResources.paint);
	}
}

enum States {
	MAIN, MAIN_MENU,
	MENU, MENU_GAME, MENU_MAIN,

	GAME, GAME_LEVEL, GAME_MENU,
	LEVEL, LEVEL_GAME, LEVEL_MENU
}
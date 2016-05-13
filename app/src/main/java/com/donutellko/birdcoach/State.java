package com.donutellko.birdcoach;

import android.graphics.Bitmap;

public class State {

	public static States state = States.MAIN;
	public static boolean victoryBool = true;

	public State() {
		super();
	}

	public static boolean isMoving() {
		return !(state == States.MAIN || state == States.MENU || state == States.GAME || state == States.LEVEL || state == States.SETTINGS || state == States.RULES);
	}

	public static States MovingTo() {
		if (!isMoving()) return state;
		States result = null;

		switch (state) {
			case MAIN_MENU: case RULES_MENU: case SETTINGS_MENU: case LEVEL_MENU: case GAME_MENU:
				result = States.MENU; break;
			case MENU_GAME: case LEVEL_GAME:
				result = States.GAME; break;
			case GAME_LEVEL:
				result = States.LEVEL; break;
			case MENU_MAIN:
				result = States.MAIN; break;
			case MENU_SETTINGS:
				result = States.SETTINGS; break;
			case MENU_RULES:
				result = States.RULES; break;
		}

		return result;
	}

	public static States MovingFrom() {
		States result = null;

		if (!isMoving()) return state;
		else if (state  != null) {

			switch (state) {
				case MAIN_MENU:
					result = States.MAIN;
					break;

				case MENU_GAME:
				case MENU_MAIN:
					result = States.MENU;
					break;

				case GAME_LEVEL:
				case GAME_MENU:
					result = States.GAME;
					break;

				case LEVEL_GAME:
				case LEVEL_MENU:
					result = States.LEVEL;
					break;

				case SETTINGS_MENU:
					result = States.SETTINGS;
					break;

				case RULES_MENU:
					result = States.RULES;
					break;
			}
		}
		return result;
	}

	public static boolean CheckTo(States st) {
		return MovingTo() == st;
	}

	public static boolean CheckFrom(States st) {
		return MovingFrom() == st;
	}

	public static boolean CheckToFrom(States st) {
		return MovingTo() == st || MovingFrom() == st;
	}

	public static void animate(int[] drawCounter) {
		float forwardX = mainView.forwardX;
		float Width = mainView.Width, Height = mainView.Height;

		if (State.CheckToFrom(States.MAIN)) {
			float x = (State.MovingFrom() == States.MAIN) ? forwardX : forwardX + Width;

			if (drawCounter[0] > 0) drawB(Res.animMain[0], x, 0);
			else if (drawCounter[0] < -163) drawCounter[0] = 47;
			if (drawCounter[1] > 0) drawB(Res.animMain[1], x + Width / 4 - 1, 0);
			else if (drawCounter[1] < -75) drawCounter[1] = 52;
			if (drawCounter[2] > 0) drawB(Res.animMain[2], x + Width / 2, 0);
			else if (drawCounter[2] < -32) drawCounter[2] = 29;
			if (drawCounter[3] > 0) drawB(Res.animMain[3], x + Width * 3 / 4 + 5, Height / 2);
			else if (drawCounter[3] < -59) drawCounter[3] = 148;

			mainView.sCanvas.drawText((Res.loadedMain) ? "Нажмите, чтобы продолжить..." : "Подождите...", x + 30, Height - Height / 22, Res.textInfo);
		}

		if (CheckToFrom(States.MENU) || CheckToFrom(States.LEVEL) || CheckToFrom(States.SETTINGS) || CheckToFrom(States.RULES)) {
			float x = (CheckFrom(States.MENU) || CheckFrom(States.LEVEL) || CheckToFrom(States.SETTINGS) || CheckToFrom(States.RULES)) ? forwardX : forwardX + Width;

			if (drawCounter[4] < -30) drawCounter[4] = 30;
			drawB((drawCounter[4] > 0) ? Res.animVictory[0] : Res.animVictory[1], x + Width / 4, 5);

			if (drawCounter[5] > 50)

			if (drawCounter[5] > 50) drawB(Res.animMenu[2], x, Height / 2);
			else if (drawCounter[5] < -100) drawCounter[5] = 100;
			else if (drawCounter[5] < -50) drawB(Res.animMenu[1], x + Width * 1 / 2, Height / 2);
		}

		mainView.drawCounter = drawCounter;
	}

	public static void drawB(Bitmap res, float x, float y) {
		mainView.sCanvas.drawBitmap(res, x, mainView.forwardY + y, Res.paint);
	}

	public static States getState() {
		return state;
	}
}

enum States {
	MAIN, MAIN_MENU,
	MENU, MENU_GAME, MENU_MAIN,
	RULES, SETTINGS, MENU_RULES, MENU_SETTINGS, RULES_MENU, SETTINGS_MENU,

	GAME, GAME_LEVEL, GAME_MENU,
	LEVEL, LEVEL_GAME, LEVEL_MENU
}

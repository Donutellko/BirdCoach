package com.donutellko.birdcoach;

import android.graphics.Bitmap;

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
		float forwardX = mainView.forwardX;
		float Width = mainView.Width;

		if (MovingFrom() == States.MAIN)
			drawB(Res.forwardMain, forwardX, 0);
		else if (MovingFrom() == States.MENU)
			drawB(Res.forwardMenu, forwardX, 0);
		else if (MovingFrom() == States.GAME)
			drawB(Res.bush, forwardX, 0);
		else if (MovingFrom() == States.LEVEL)
			drawB((victory) ? Res.forwardVictory : Res.forwardLose, forwardX, 0);

		if (MovingTo() == States.MAIN)
			drawB(Res.forwardMain, forwardX + Width, 0);
		else if (MovingTo() == States.MENU)
			drawB(Res.forwardMenu, forwardX + Width, 0);
		else if (MovingTo() == States.GAME)
			drawB(Res.bush, forwardX + Width, 0);
		else if (MovingTo() == States.LEVEL)
			drawB((victory) ? Res.forwardVictory : Res.forwardLose, forwardX + Width, 0);
	}


	public static void animate(int[] drawCounter) {
		float forwardX = mainView.forwardX;
		float Width = mainView.Width, Height = mainView.Height;

		if (State.MovingFrom() == States.MAIN || State.MovingTo() == States.MAIN) {
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

		if (State.MovingFrom() == States.MENU || State.MovingTo() == States.MENU || State.MovingFrom() == States.LEVEL || State.MovingTo() == States.LEVEL) {
			float x = (State.MovingFrom() == States.MENU || State.MovingFrom() == States.LEVEL) ? forwardX : forwardX + Width;

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
		mainView.sCanvas.drawBitmap(res, x, y, Res.paint);
	}

	public static States getState() {
		return state;
	}
}

enum States {
	MAIN, MAIN_MENU,
	MENU, MENU_GAME, MENU_MAIN,

	GAME, GAME_LEVEL, GAME_MENU,
	LEVEL, LEVEL_GAME, LEVEL_MENU
}
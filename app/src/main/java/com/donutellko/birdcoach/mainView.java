package com.donutellko.birdcoach;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

import static com.donutellko.birdcoach.ViewElements.*;

public class mainView extends View {
	static Level LevelThread;
	static Context context;
	static Canvas sCanvas;

	static List<Birds> birds = new LinkedList<>();
	Birds activeBird;

	static int Width, Height;

	static float
			  forwardX, Xspeed, Xacc,
			  forwardY, Yspeed, Yacc;

	static int[] drawCounter = new int[10];
	static float deltaX, deltaY;
	float startX, startY;

	static forwElement screenMain, screenMenu, screenGame, screenVictory, screenLose;
	static forwElement gameMp, gameNote, gameBush;
	static backElement hills, weed, wire, sky = new backElement(null, 0, -1);


	static int
			  BIRDS_BITMAP_COLUMNS = 9, BIRDS_BITMAP_STRINGS = 5,
			  BIRDS_HEIGHT, BIRDS_WIDTH;

	public mainView(Context context) {
		super(context);

		mainView.context = context;
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		Width = (w > h) ? w : h;
		Height = (w > h) ? h : w;

		BIRDS_WIDTH = Height / 7;
		BIRDS_HEIGHT = BIRDS_WIDTH * 248 / 335;

		Res.loadText();
		if (!Res.resizedMain && Res.loadedMain) Res.changeSizeMain();
		if (!Res.loadedAnother) Res.loadAnother();

		sky = new backElement(Res.backSky, 0, -1);
		hills = new backElement(Res.backHills, Height - Width * 650 / 1920, 0.5f);
		weed = new backElement(Res.backWeed, Height - Width * 295 / 1920, 1f);
		wire = new backElement(Res.wire, 0, 1.0f);

		screenMain = new forwElement(Res.forwardMain, 0, 0);
		screenMenu = new forwElement(Res.forwardMenu, 0, 0);
		screenGame = new forwElement(Res.forwardGame, 0, 0);
		screenVictory = new forwElement(Res.forwardVictory, 0, 0);
		screenLose = new forwElement(Res.forwardLose, 0, 0);

		gameMp = new forwElement(Res.megaphone, Width - 3 * BIRDS_WIDTH, Height - BIRDS_HEIGHT * 3, BIRDS_HEIGHT * 3 * 650 / 750, BIRDS_HEIGHT * 3);
		gameNote = new forwElement(Res.note, Width / 2 - BIRDS_WIDTH / 4, Height / 20, BIRDS_HEIGHT * 2 / 3, BIRDS_HEIGHT * 2 / 3);
		gameBush = new forwElement(Res.forwardGame, Width - 4 * BIRDS_WIDTH, Height * 8 / 11, w, h);

		Xacc = -Width / 1000;
		Yacc = Height / 600;

		LevelThread = new Level("LevelThread");
		LevelThread.start();

		new Timer(100000000000000L, 10).start();
	}

	protected void onDraw(Canvas canvas) {
		sCanvas = canvas;

		sky.draw();
		hills.draw();
		weed.draw();
		wire.draw();

		if (State.MovingFrom() != null)
		switch (State.MovingFrom()) {
			case MAIN: screenMain.draw(); break;
			case MENU:
			case RULES:
			case SETTINGS:
				screenMenu.draw(); break;
			case GAME: screenGame.draw(); break;
			case LEVEL:
				if (State.victoryBool) screenVictory.draw();
				else screenLose.draw();
				 break;
		}

		if (State.MovingTo() != null)
		switch (State.MovingTo()) {
			case MAIN: screenMain.draw(+ Width); break;
			case MENU: screenMenu.draw(Width); break;
			case RULES: screenMenu.draw(); break;
			case SETTINGS: screenMenu.draw(); break;
			case GAME: screenGame.draw(+ Width); break;
			case LEVEL:
				if (State.victoryBool) screenVictory.draw(+ Width);
				else screenLose.draw(+ Width);
				break;
		}

		State.animate(drawCounter);

		if (State.CheckToFrom(States.GAME));
			for (Birds b : birds) b.drawBird(canvas);

		if (State.CheckToFrom(States.LEVEL))
			Dialogs.writeComment();


		if (State.state == States.GAME) {
			canvas.drawText("Score: " + Level.score, Height / 30, Height / 30, Res.textScores);
			canvas.drawText("Time: " + Level.timeText(), Width * 7 / 8, Height / 30, Res.textTime);

			for (int i = 0; i < Level.lifes; i++)
				canvas.drawBitmap(Res.lifesBitmap, 30 + BIRDS_HEIGHT * i * 2 / 3, Height / 20, Res.paint);

			gameNote.draw();
			gameMp.draw();
			gameBush.draw();

			for (int i = 0; i < Level.melodyOrder.length; i++)
				if (Level.birdPlaced[i] == null)
					canvas.drawBitmap(Res.fantom, (float) Level.wirePlaceX[i] * Width, (float) Level.wirePlaceY[i] * Height, Res.paint);
		}

		invalidate();
	}

	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX(), y = event.getY();

		if (State.state == States.MAIN && event.getAction() == MotionEvent.ACTION_DOWN)
			State.state = States.MAIN_MENU;

		if (State.state == States.MENU && event.getAction() == MotionEvent.ACTION_DOWN) {
			if (x > Width * 35 / 110 && x < Width * 6 / 11 && y > Height * 20 / 65 && y < Height * 40 / 65) {
				Level.newGame();
				State.state = States.MENU_GAME;
			} else if (x > Width * 2 / 11 && x < Width * 35 / 110 && y > Height * 20 / 65 && y < Height * 35 / 65)
				State.state = States.MENU_RULES;
			else if (x > Width * 6 / 11 && x < Width * 7 / 11 && y > Height * 20 / 65 && y < Height * 35 / 65)
				State.state = States.MENU_SETTINGS;
		}

		if (State.state == States.RULES && event.getAction() == MotionEvent.ACTION_DOWN)
			State.state = States.RULES_MENU;
		else if (State.state == States.SETTINGS && event.getAction() == MotionEvent.ACTION_DOWN)
			State.state = States.SETTINGS_MENU;

		if (State.state == States.GAME) {
			switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN:
					startX = x;
					startY = y;

					if (gameNote.checkTouch(x, y)) Level.checkMelody();
					else if (gameMp.checkTouch(x, y)) Level.playMelody(Level.melodyOrder);
					else for (Birds b : birds)
							if (b.checkTouch(x, y)) {
								deltaX = b.x - x;
								deltaY = b.y - y;
								activeBird = b;
								activeBird.touched = true;
							}
					break;

				case MotionEvent.ACTION_MOVE:
					if (activeBird != null) {
						activeBird.x = x + deltaX;
						activeBird.y = y + deltaY;
					}
					break;

				case MotionEvent.ACTION_UP:
					if (Math.abs(x - startX) < 10 && Math.abs(y - startY) < 10 && activeBird != null) {
						activeBird.x = startX + deltaX;
						activeBird.y = startY + deltaY;
						activeBird.sound();
					}
					if (activeBird != null) {
						Level.checkPlace(activeBird);
						activeBird.touched = false;
						activeBird = null;
					}
					break;
			}
		}

		if (State.state == States.LEVEL) {
			if (!State.victoryBool) Level.nextLevel();
			else Level.newGame();

			State.state = States.LEVEL_GAME;
		}

		return true;
	}

	class Timer extends CountDownTimer {

		int c = 0;

		public Timer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			c++;
			if (State.state == States.GAME && Level.timeBool && c % 50 == 0) {
				Level.time--;
				Log.i("sgjsfkfjjhtrvhbyjjhdfvh", "Time: " + Level.time);
			}

			if (Res.player != null) {
				if (Res.player.isPlaying()) {
					if (State.state == States.GAME || !Level.musicBool || MainActivity.paused)
						Res.player.pause();
				} else if (Level.musicBool && !MainActivity.paused && State.state != States.GAME)
					Res.player.start();
			}

			sky.moveX();

			for (int i = 0; i < drawCounter.length; i++)
				drawCounter[i] += (drawCounter[i] < 200) ? -1 : 100;

			if (State.isMoving()) {
				boolean MR = false, MS = false, RM = false, SM = false;
				switch (State.state) {
					case MENU_RULES: MR = true; break;
					case MENU_SETTINGS: MS = true; break;
					case RULES_MENU: RM = true; break;
					case SETTINGS_MENU: SM = true; break;
				}

				if (MR || MS || RM || SM) {
					if (MR || MS) {
						if (forwardY >= Height * 0.66) {
							State.state = (MR) ? States.RULES : States.SETTINGS;
							Yspeed = 0;
						}
						if (forwardY < Height / 3) Yspeed += Yacc;
						else Yspeed = (Yspeed <= 1) ? 1 : Yspeed - Yacc;
					} else {
						if (forwardY <= 0) {
							State.state = States.MENU;
							forwardY = 0;
						}
						if (forwardY > Height / 3) Yspeed -= Yacc;
						else if (Yspeed < -2) Yspeed += Yacc;
					}

					forwardY += Yspeed;
					sky.moveY();
					hills.moveY();
					weed.moveY();
					wire.moveY(); 
					screenMenu.moveY();
				} else {
					hills.moveX();
					weed.moveX();
					wire.moveX();
					forwardX += Xspeed;

					if (forwardX <= -Width) {
						Xspeed = 0;
						forwardX = 0;
						State.state = State.MovingTo();
					} else {
						if (forwardX > -Width / 2) Xspeed += Xacc;
						else Xspeed = (Xspeed >= -2) ? -2 : Xspeed - Xacc;
					}
				}
			}

			// Log.i("", "Xspeed = " + Xspeed);
		}

		@Override
		public void onFinish() {
			start();
		}
	}
}

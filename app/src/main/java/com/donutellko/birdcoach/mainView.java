package com.donutellko.birdcoach;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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

	static float Xspeed, Xacc, forwardX;

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

		LevelThread = new Level("LevelThread");
		LevelThread.start();

		new Timer(100000000000000L, 10).start();
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
		weed = new backElement(Res.backWeed, Height - Width * 295 / 1920, 0.9f);
		wire = new backElement(Res.wire, 0, 1.0f);

		screenMain = new forwElement(Res.forwardMain, 0, 0);
		screenMenu = new forwElement(Res.forwardMenu, 0, 0);
		screenVictory = new forwElement(Res.forwardVictory, 0, 0);
		screenLose = new forwElement(Res.forwardLose, 0, 0);

		gameMp = new forwElement(Res.megaphone, Width - 3 * BIRDS_WIDTH, Height - BIRDS_HEIGHT * 3, BIRDS_HEIGHT * 3 * 650 / 750, BIRDS_HEIGHT * 3);
		gameNote = new forwElement(Res.note, Width / 2 - BIRDS_WIDTH / 4, Height / 20, BIRDS_HEIGHT * 2 / 3, BIRDS_HEIGHT * 2 / 3);
		gameBush = new forwElement(Res.bush, Width - 4 * BIRDS_WIDTH, Height * 8 / 11, w, h);

		Xacc = -Width / 1000;

		Log.i("onSizeChanged: ", "Screen:\t" + Width + "x" + Height);
		Log.i("onSizeChanged: ", "Birds:\t" + BIRDS_WIDTH + "x" + BIRDS_HEIGHT);
		Log.i("onSizeChanged: ", "Xacc:\t" + Xacc);
	}

	protected void onDraw(Canvas canvas) {
		sCanvas = canvas;

		sky.draw();
		hills.draw();
		weed.draw();
		wire.draw();

		State.draw();
		State.animate(drawCounter);

		if (State.MovingTo() == States.GAME || State.MovingFrom() == States.GAME)
			for (Birds b : birds) b.drawBird(canvas);

		if (State.MovingTo() == States.LEVEL || State.MovingFrom() == States.LEVEL)
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
				Dialogs.rules();
			else if (x > Width * 6 / 11 && x < Width * 7 / 11 && y > Height * 20 / 65 && y < Height * 35 / 65)
				Dialogs.settings();
		}

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
			if (State.victory) Level.nextLevel();
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
			if (State.state == States.GAME && Level.timeBool && c == 100) Level.time++;

			if (Res.player != null) {
				if (Res.player.isPlaying()) {
					if (State.state == States.GAME || !Level.musicBool || MainActivity.paused)
						Res.player.pause();
				} else if (Level.musicBool && !MainActivity.paused && State.state != States.GAME)
					Res.player.start();
			}

			sky.move();

			for (int i = 0; i < drawCounter.length; i++)
				drawCounter[i] += (drawCounter[i] < 200) ? -1 : 100;

			if (State.isMoving()) {

				hills.move();
				weed.move();
				wire.move();
				forwardX += Xspeed;

				if (forwardX <= -Width
					//(int) forwardX / 10 == -Width / 10 && forwardX < Width / 2
						  ) {
					Xspeed = 0;
					forwardX = 0;
					State.state = State.MovingTo();
				} else {
					if (forwardX > -Width / 2) Xspeed += Xacc;
					else Xspeed = (Xspeed >= -2) ? -2 : Xspeed - Xacc;
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

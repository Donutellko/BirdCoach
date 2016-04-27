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

public class mainView extends View {
	static Level LevelThread;
	static Context context;
	static Canvas sCanvas;

	static List<Birds> birds = new LinkedList<>();
	Birds activeBird;

	static boolean musicBool = true;

	static int Width, Height;

	float Xspeed, Xacc;
	float forwSm = 1, hillsSm = 0.7f, weedSm = 0.9f; // speed multipliers (for Xspeed);
	static float forwardX;
	float hillsX, hillsY;
	float weedX, weedY;
	float skyX, skyXspeed;

	static int[] drawCounter = new int[10];
	static float deltaX;
	static float deltaY;
	float startX;
	float startY;

	static int
			  BIRDS_BITMAP_COLUMNS = 9, BIRDS_BITMAP_STRINGS = 5,
			  BIRDS_HEIGHT, BIRDS_WIDTH;
	public mainView(Context context) {
		super(context);

		mainView.context = context;

		LevelThread = new Level("LevelThread");
		LevelThread.run();

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

		weedY = Height - Width * 295 / 1920;
		hillsY = Height - Width * 650 / 1920;

		skyXspeed = Width / 500;
		Xacc = - Width / 1000;
	}

	protected void onDraw(Canvas canvas) {
		sCanvas = canvas;

		drawB(Res.backSky, skyX, 0);
		drawB(Res.backSky, skyX - Width, 0);
		drawB(Res.backHills, hillsX, hillsY);
		drawB(Res.backHills, hillsX + Width, hillsY);
		drawB(Res.backWeed, weedX, weedY);
		drawB(Res.backWeed, weedX + Width, weedY);
		drawB(Res.wire, forwardX, 0);
		drawB(Res.wire, forwardX + Width, 0);

		State.draw();
		State.animate(drawCounter);

		if (State.MovingTo() == States.GAME || State.MovingFrom() == States.GAME)
			for (Birds b : birds) b.drawBird(canvas);

		if (State.MovingTo() == States.LEVEL || State.MovingFrom() == States.LEVEL) {
			float textX = forwardX + ((State.state == States.LEVEL || State.MovingFrom() == States.LEVEL) ? 0 : Width);
			canvas.drawText(Level.comment, textX + Width * 7 / 16, Height * 0.55f, Res.textComment);
			canvas.drawText(Level.scoresText(), textX + 30, Height - Height / 18, Res.textInfo);
		}

		if (State.state == States.GAME) {
			canvas.drawText("Score: " + Level.score, Height / 30, Height / 30, Res.textScores);
			canvas.drawText("Time: " +  Level.timeText(), Width * 7 / 8, Height / 30, Res.textTime);

			for (int i = 0; i < Level.lifes; i++)
				drawB(Res.lifesBitmap, 30 + BIRDS_HEIGHT * i * 2 / 3, Height / 20);

			drawB(Res.note, Width / 2 - BIRDS_WIDTH / 4, Height / 20);
			drawB(Res.megaphone, Width - 3 * BIRDS_WIDTH, Height - BIRDS_HEIGHT * 3);
			drawB(Res.bush, Width - 4 * BIRDS_WIDTH, Height * 8 / 11);

			for (int i = 0; i < Level.melodyOrder.length; i++)
				if (Level.birdPlaced[i] == null)
					drawB(Res.fantom, (float) Level.wirePlaceX[i] * Width, (float) Level.wirePlaceY[i] * Height);
		}

		invalidate();
	}

	public static void drawB(Bitmap res, float x, float y) {
		sCanvas.drawBitmap(res, x, y, Res.paint);
	}

	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX(), y = event.getY();

		if (Res.loadedMain) {
			if (State.state == States.MAIN && event.getAction() == MotionEvent.ACTION_DOWN)
				State.state = States.MAIN_MENU;

			if (State.state == States.MENU && event.getAction() == MotionEvent.ACTION_DOWN)
				if (x > Width * 35 / 110 && x < Width * 6 / 11 && y > Height * 20 / 65 && y < Height * 40 / 65) {
					Level.newGame();
					State.state = States.MENU_GAME;
				} else if (x > Width * 2 / 11 && x < Width * 35 / 110 && y > Height * 20 / 65 && y < Height * 35 / 65) {
					Dialogs.rules();
				} else if (x > Width * 6 / 11 && x < Width * 7 / 11 && y > Height * 20 / 65 && y < Height * 35 / 65) {
					Dialogs.settings();
				}


			if (State.state == States.GAME) {
				switch (event.getAction()) {

					case MotionEvent.ACTION_DOWN:
						startX = x;
						startY = y;
						for (Birds b : birds)
							if (b.checkTouch(x, y)) {
								deltaX = b.x - x;
								deltaY = b.y - y;
								activeBird = b;
								activeBird.touched = true;
							}

						if (x > Width / 2 - BIRDS_WIDTH / 2 && x < Width / 2 + BIRDS_WIDTH / 2 && y < BIRDS_HEIGHT)
							Level.checkMelody();
						else if (x > Width - 4 * BIRDS_HEIGHT && y > Height * 7 / 11)
							Level.playMelody(Level.melodyOrder);
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

			if (Res.player.isPlaying()) {
				if (State.state == States.GAME || !musicBool || MainActivity.paused) Res.player.pause();
			} else if (musicBool && !MainActivity.paused && State.state != States.GAME) Res.player.start();

			skyX = (skyX < Width) ? skyX + skyXspeed : 0;

			for (int i = 0; i < drawCounter.length; i++) drawCounter[i] += (drawCounter[i] < 200) ? -1 : 100;

			if (State.isMoving()) {
				if (forwardX <= -Width
						  //(int) forwardX / 10 == -Width / 10 && forwardX < Width / 2
						   ) {
					Xspeed = 0;
					forwardX = 0;
					State.state = State.MovingTo();
				} else {
					if (forwardX > - Width / 2) Xspeed += Xacc;
					else  Xspeed = (Xspeed >= -2) ? -2 : Xspeed - Xacc;
				}

				hillsX += (hillsX > -Width) ? Xspeed * hillsSm : Width;
				weedX += (weedX > -Width) ? Xspeed * weedSm : Width;
				forwardX += Xspeed * forwSm;
			}

			// Log.i("", "Xspeed = " + Xspeed);
		}

		@Override
		public void onFinish() {
			start();
		}
	}
/*
	class viewElement {
		float speedMult;
		Bitmap bitmap;
		int w, h;
		int x, y;
		viewElement(float speedMult, Bitmap bitmap, int w, int h, int x, int y) {
			this.speedMult = speedMult;
			this.bitmap = bitmap;
			this.w = w;
			this.h = h;
			this.x = x;
			this.y = y;
		}
	}
	*/
}

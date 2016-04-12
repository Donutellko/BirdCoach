package com.donutellko.birdcoach;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.List;


public class MainView extends View {

	public static mResources ResThread;
	public static Level LevelThread;
	public static Context context;
	public static MediaPlayer mediaPlayer;
	public static boolean musicBool = true, hardBool = false;

	private Birds activeBird;
	static List<Birds> birds = new LinkedList<>();

	public static int Width, Height;
	static float allX = 0, allXspeed = 0;
	float deltaX, deltaY, startX, startY;
	float cloudsX = 0, cloudsXspeed = -1;
	public static String state = "Main", stateL;

	int[] drawCounter = new int[10];

	public static final int
			  BIRDS_BITMAP_COLUMNS = 9,
			  BIRDS_BITMAP_STRINGS = 5;
	public static int
			  BIRDS_HEIGHT = 100,
			  BIRDS_WIDTH = BIRDS_HEIGHT * 248 / 335;
	private Canvas canvas;

	public MainView(Context context) {
		super(context);
		MainView.context = context;

		ResThread = new mResources();
		LevelThread = new Level(this, "LevelThread");

		new Timer(100000000000000L, 1000).start();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (mediaPlayer == null) {
			mediaPlayer = MediaPlayer.create(context, R.raw.hopper);
			mediaPlayer.setLooping(true);
		}

		if (getWidth() > getHeight()) {
			Width = getWidth();
			Height = getHeight();
		} else {
			Width = getHeight();
			Height = getWidth();
		}

		BIRDS_HEIGHT = MainView.Height / 7;
		BIRDS_WIDTH = BIRDS_HEIGHT * 248 / 335;

		ResThread.run();
	}

	protected void onDraw(Canvas canvas) {
		this.canvas = canvas;

		if (musicBool && !mediaPlayer.isPlaying() && !MainActivity.paused && !state.equals("Game"))
			mediaPlayer.start();
		else if (!musicBool && mediaPlayer.isPlaying())
			mediaPlayer.pause();

		mResources.loadBackgrounds();
		cloudsX += cloudsXspeed;
		allX += allXspeed;

		if (cloudsX <= -Width) cloudsX = Width + cloudsX;
		if (state.equals("Main+Menu") || state.equals("Menu+Game") || state.equals("Game+Level") || state.equals("Level+Game")) {
			allXspeed += (Math.abs(allX) <= Width / 2) ? -0.6f : 0.58f;
			if ((int) allX / 10 == -Width / 10) {
				allXspeed = 0;
				allX = 0;
				if (state.equals("Main+Menu")) state = "Menu";
				else if (state.equals("Menu+Game") || state.equals("Level+Game")) state = "Game";
				else if (state.equals("Game+Level")) state = "Level";
			}
		}

		if (state.equals("Game") && mediaPlayer.isPlaying()) mediaPlayer.pause();
		if (!state.equals("Game") && !mediaPlayer.isPlaying() && MainActivity.paused == false && musicBool)
			mediaPlayer.start();

		if (state.equals("Main") || state.equals("Menu") || state.equals("Game") || state.equals("Level")) {
			drawB(mResources.back, 0, 0);
			drawB(mResources.clouds, cloudsX, 0);
			drawB(mResources.clouds, cloudsX + Width, 0);
			drawB(mResources.wire, 0, 0);
		} else {
			drawB(mResources.back, allX, 0);
			drawB(mResources.back, allX + Width, 0);
			drawB(mResources.clouds, cloudsX, 0);
			drawB(mResources.clouds, cloudsX + Width, 0);
			drawB(mResources.wire, allX, 0);
			drawB(mResources.wire, allX + Width, 0);
		}

		// Отрисовка интерьера:

		if (state.equals("Main")) {
			drawB(mResources.scr1, 0, 0);
			for (int i = 0; i < drawCounter.length; i++) drawCounter[i]--;

			if (drawCounter[0] > 0) drawB(mResources.scr1_blue, 1, 0);
			else if (drawCounter[0] < -163) drawCounter[0] = 47;

			if (drawCounter[1] > 0) drawB(mResources.scr1_dark, Width / 4 - 1, 0);
			else if (drawCounter[1] < -75) drawCounter[1] = 52;

			if (drawCounter[2] > 0) drawB(mResources.scr1_red, Width / 2, 0);
			else if (drawCounter[2] < -32) drawCounter[2] = 29;

			if (drawCounter[3] > 0) drawB(mResources.scr1_purple, Width * 3 / 4 + 5, Height / 2);
			else if (drawCounter[3] < -59) drawCounter[3] = 148;

			/*
			rand = Level.random.nextInt(1000);
			if (drawCounter[0] > 0) { drawB(mResources.scr1_blue, 1, 0); drawCounter[0]--; }
				else if (rand < 10) drawCounter[0] = 50;
			if (drawCounter[1] > 0) { drawB(mResources.scr1_dark, Width / 4 - 1, 0); drawCounter[1]--; }
				else if (rand > 980) drawCounter[1] = 140;
			if (drawCounter[2] > 0) { drawB(mResources.scr1_red, Width / 2, 0); drawCounter[2]--; }
				else if (rand < 10) drawCounter[2] = 90;
			if (drawCounter[3] > 0) { drawB(mResources.scr1_purple, Width * 3 / 4 + 5, Height / 2); drawCounter[3]--; }
				else if (rand > 990) drawCounter[3] = 120;
		*/

			canvas.drawText("Нажмите, чтобы продолжить...", 30, Height - Height / 22, mResources.textInfo);
		} else if (state.equals("Main+Menu"))
			drawB(mResources.scr1, allX, 0);

		if (state.equals("Menu") || state.equals("Menu+Game"))
			drawB(mResources.scr2, allX, 0);
		if (state.equals("Main+Menu"))
			drawB(mResources.scr2, allX + Width, 0);

		if (state.equals("Game") || state.equals("Game+Level"))
			drawB(mResources.josh, allX, 0);
		if (state.equals("Menu+Game") || state.equals("Level+Game"))
			drawB(mResources.josh, allX + Width, 0);


		if (state.equals("Game+Level")) {
			if (stateL.equals("Victory")) drawB(mResources.victory, allX + Width, 0);
			else drawB(mResources.lalka, allX + Width, 0);
		} else if (state.equals("Level+Game") || state.equals("Level")) {
			if (stateL.equals("Victory")) drawB(mResources.victory, allX, 0);
			else drawB(mResources.lalka, allX, 0);
		}

		if (state.equals("Level") || state.equals("Level+Game") || state.equals("Game+Level")) {
			if (state.equals("Game+Level"))
				canvas.drawText(Level.comment, allX + Width + Width * 7 / 16, Height * 0.55f, mResources.textComment);
			else
				canvas.drawText(Level.comment, allX + Width * 7 / 16, Height * 0.55f, mResources.textComment);
			canvas.drawText("Scores: " + Level.score, 30, Height - Height / 18, mResources.textInfo);
		}

		// Отрисовка элементов игры:

		if (state.equals("Game") || state.equals("Menu+Game") || state.equals("Game+Level") || state.equals("Level+Game"))
			for (Birds b : birds) b.drawBird(canvas);

		if (state.equals("Game")) {
			String TS = "Time: " + Level.time / 60 + ":";
			TS += (Level.time % 60 < 10) ? "0" + Level.time % 60 : Level.time % 60;

			canvas.drawText("Scores: " + Level.score, MainView.Height / 30, MainView.Height / 30, mResources.textScores);
			canvas.drawText(TS, MainView.Width * 7 / 8, MainView.Height / 30, mResources.textTime);

			for (int i = 0; i < Level.lifes; i++)
				drawB(mResources.lifesBitmaps, 30 + BIRDS_HEIGHT * i * 2 / 3, MainView.Height / 20);

			drawB(mResources.note, MainView.Width / 2 - BIRDS_WIDTH / 4, MainView.Height / 20);
			drawB(mResources.mfBitmap, Width - 4 * BIRDS_WIDTH, Height * 8 / 11);

			for (int i = 0; i < Level.melodyOrder.length; i++)
				if (Level.birdPlaced[i] == null)
					drawB(mResources.fantom, (float) Level.wirePlaceX[i] * Width, (float) Level.wirePlaceY[i] * Height);
		}

		invalidate();
	}

	private void drawB(Bitmap res, float x, float y) {
		canvas.drawBitmap(res, x, y, mResources.paint);
	}

	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX(), y = event.getY();

		if (state.equals("Main") && event.getAction() == MotionEvent.ACTION_DOWN)
			state = "Main+Menu";

		if (state.equals("Menu") && event.getAction() == MotionEvent.ACTION_DOWN)
			if (x > Width * 35 / 110 && x < Width * 6 / 11 && y > Height * 20 / 65 && y < Height * 40 / 65 && Level.level == 0) {
				Level.newGame();
				state = "Menu+Game";
			} else if (x > Width * 2 / 11 && x < Width * 35 / 110 && y > Height * 20 / 65 && y < Height * 35 / 65 && Level.level == 0) {
				Level.rulesDialog();
			} else if (x > Width * 6 / 11 && x < Width * 7 / 11 && y > Height * 20 / 65 && y < Height * 35 / 65 && Level.level == 0) {
				Level.settingsDialog();
			}


		if (state.equals("Game")) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					for (Birds b : birds)
						if (x > b.x && x < (b.x + BIRDS_WIDTH * 1.2) && y > b.y && y < (b.y + BIRDS_HEIGHT * 1.2)) {
							// if ((x - b.x) * (x - b.x) + (y - b.y) * (y - b.y) < BIRDS_HEIGHT * BIRDS_HEIGHT * 2 / 3) {
							deltaX = b.x - x;
							deltaY = b.y - y;
							activeBird = b;
						}

					if (x > Width / 2 - BIRDS_WIDTH / 2 && x < Width / 2 + BIRDS_WIDTH / 2 && y < BIRDS_HEIGHT)
						//if (LevelThread.isAlive()) LevelThread.run(); else LevelThread.start();
						Level.checkMelody();
					if (x > Width - 3 * BIRDS_HEIGHT && y > Height * 8 / 11)
						Level.playMelody(Level.melodyOrder);

					startX = x;
					startY = y;
					break;
				case MotionEvent.ACTION_MOVE:
					if (activeBird != null) {
						activeBird.x = x + deltaX;
						activeBird.y = y + deltaY;
					}
					break;
				case MotionEvent.ACTION_UP:
					if (Math.abs(x - startX) < 5 && Math.abs(y - startY) < 5 && activeBird != null)
						activeBird.sound(context);
					if (activeBird != null) Level.checkPlace(activeBird);
					activeBird = null;
					break;
			}
		}

		if (state.equals("Level")) {
			if (stateL.equals("Loose")) {
				Level.newGame();
				state = "Level+Game";
			}
			if (stateL.equals("Victory")) {
				Level.nextLevel();
				state = "Level+Game";
			}
		}

		return true;
	}

	class Timer extends CountDownTimer {

		public Timer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			//rand = Level.random.nextInt(100);

			if (state.equals("Game") && Level.timeBool) Level.time++;
		}

		@Override
		public void onFinish() {
			start();
		}
	}
}

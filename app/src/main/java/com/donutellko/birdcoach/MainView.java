package com.donutellko.birdcoach;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import static android.graphics.Bitmap.createBitmap;


public class MainView extends View {

	public static mResources ResThread;
	public static Level LevelThread;
	public static Context context;
	public static MediaPlayer mediaPlayer;
	public static boolean musicBool = true;

	private Birds activeBird;
	static List<Birds> birds = new LinkedList<>();

	public static int Width, Height;
	static float allX = 0, allXspeed = 0;
	float deltaX, deltaY, startX, startY;
	boolean fase_scr1 = true;
	float cloudsX = 0, cloudsXspeed = -1;
	public static String state = "Main", stateL;

	public static final int
			  BIRDS_BITMAP_COLUMNS = 9,
			  BIRDS_BITMAP_STRINGS = 5;
	public static int
			  BIRDS_HEIGHT = 100,
			  BIRDS_WIDTH = BIRDS_HEIGHT * 248 / 335;


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
		mResources.loadBackgrounds();
		cloudsX += cloudsXspeed;
		allX += allXspeed;

		if (cloudsX <= -Width) cloudsX = Width + cloudsX;
		if (state.equals("Main+Menu") || state.equals("Menu+Game") || state.equals("Game+Level") || state.equals("Level+Game")) {
			allXspeed += (Math.abs(allX) <= Width / 2) ? - 0.6f : 0.58f;
			if ((int) allX / 10 == - Width / 10) {
				allXspeed = 0; allX = 0;
				if (state.equals("Main+Menu")) state = "Menu";
				else if (state.equals("Menu+Game") || state.equals("Level+Game")) state = "Game";
				else if (state.equals("Game+Level")) state = "Level";
			}
		}

		if (state.equals("Game") && mediaPlayer.isPlaying()) mediaPlayer.pause();
		if (!state.equals("Game") && !mediaPlayer.isPlaying() && MainActivity.paused == false && musicBool) mediaPlayer.start();

		if (state.equals("Main") || state.equals("Menu") || state.equals("Game") || state.equals("Level")) {
			canvas.drawBitmap(mResources.back,   0, 0, mResources.paint);
			canvas.drawBitmap(mResources.clouds, cloudsX, 0, mResources.paint);
			canvas.drawBitmap(mResources.clouds, cloudsX + Width, 0, mResources.paint);
			canvas.drawBitmap(mResources.wire, 0, 0, mResources.paint);
		}
		else {
			canvas.drawBitmap(mResources.back,   allX, 			0, mResources.paint);
			canvas.drawBitmap(mResources.back, 	 allX + Width, 0, mResources.paint);
			canvas.drawBitmap(mResources.clouds, cloudsX,		0, mResources.paint);
			canvas.drawBitmap(mResources.clouds, cloudsX + Width, 0, mResources.paint);
			canvas.drawBitmap(mResources.wire, 	 allX, 			0, mResources.paint);
			canvas.drawBitmap(mResources.wire, 	 allX + Width, 0, mResources.paint);
		}

		// Отрисовка интерьера:

		if (state.equals("Main")) {
			if (fase_scr1) canvas.drawBitmap(mResources.scr1, 0, 0, mResources.paint);
			else canvas.drawBitmap(mResources.scr1_2, 0, 0, mResources.paint);
			canvas.drawText("Нажмите, чтобы продолжить...", 30, Height - Height / 22, mResources.textInfo);
		}
		else if (state.equals("Main+Menu"))
			if (fase_scr1) canvas.drawBitmap(mResources.scr1, allX, 0, mResources.paint);
			else canvas.drawBitmap(mResources.scr1_2, allX, 0, mResources.paint);

		if (state.equals("Menu") || state.equals("Menu+Game"))
			canvas.drawBitmap(mResources.scr2, allX, 0, mResources.paint);
		if (state.equals("Main+Menu"))
			canvas.drawBitmap(mResources.scr2, allX + Width, 0, mResources.paint);

		if (state.equals("Game") || state.equals("Game+Level"))
			canvas.drawBitmap(mResources.josh, allX, 0, mResources.paint);
		if (state.equals("Menu+Game") || state.equals("Level+Game"))
			canvas.drawBitmap(mResources.josh, allX + Width, 0, mResources.paint);



		if (state.equals("Game+Level")) {
			if (stateL.equals("Victory")) canvas.drawBitmap(mResources.victory, allX + Width, 0, mResources.paint);
			else canvas.drawBitmap(mResources.lalka, allX + Width, 0, mResources.paint); }
		else if (state.equals("Level+Game") || state.equals("Level")) {
			if (stateL.equals("Victory")) canvas.drawBitmap(mResources.victory, allX, 0, mResources.paint);
			else canvas.drawBitmap(mResources.lalka, allX, 0, mResources.paint); }

		if (state.equals("Level") || state.equals("Level+Game") || state.equals("Game+Level")) {
			if (state.equals("Game+Level")) canvas.drawText(Level.comment, allX + Width + Width * 7 / 16, Height * 0.55f, mResources.textComment);
			else canvas.drawText(Level.comment, allX + Width * 7 / 16, Height * 0.55f, mResources.textComment);
			canvas.drawText("Scores: " + Level.score, 30, Height - Height / 18, mResources.textInfo);}

		// Отрисовка элементов игры:

		if (state.equals("Game") || state.equals("Menu+Game") || state.equals("Game+Level"))
			for (Birds b : birds) b.drawBird(canvas);

		if (state.equals("Game")) {
			String TS = "Time: " + Level.time / 60 + ":";
			TS += (Level.time % 60 < 10) ? "0" + Level.time % 60 : Level.time % 60;

			canvas.drawText("Scores: " + Level.score, MainView.Height / 30 , MainView.Height / 30, mResources.textScores);
			canvas.drawText(TS, MainView.Width * 7 / 8, MainView.Height / 30, mResources.textTime);

			for (int i = 0; i < Level.lifes; i++) canvas.drawBitmap(mResources.lifesBitmaps, 30 + BIRDS_HEIGHT * i * 2 / 3, MainView.Height / 20, mResources.paint);

			canvas.drawBitmap(mResources.note, MainView.Width / 2 - BIRDS_WIDTH / 4, MainView.Height / 20, mResources.paint);
			canvas.drawBitmap(mResources.mfBitmap, Width - 4 * BIRDS_WIDTH, Height * 8 / 11, mResources.paint);

			for (int i = 0; i < Level.melodyOrder.length; i++)
				if (Level.birdPlaced[i] == null) canvas.drawBitmap(mResources.fantom, (float) Level.wirePlaceX[i] * Width, (float) Level.wirePlaceY[i] * Height, mResources.paint);
		}

		invalidate();
	}

	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX(), y = event.getY();

		if (state.equals("Main") && event.getAction() == MotionEvent.ACTION_DOWN)
			state = "Main+Menu";

		if (state.equals("Menu") && event.getAction() == MotionEvent.ACTION_DOWN)
			if (x > Width * 35/110 && x < Width * 6/11 && y > Height * 20/65 && y < Height * 40/65 && Level.level == 0) {
				Level.newGame();
				state = "Menu+Game";
			} else if (x > Width * 2/11 && x < Width * 35/110 && y > Height * 20/65 && y < Height * 35/65 && Level.level == 0) {
				Level.rulesDialog();
			} else if (x > Width * 6/11 && x < Width * 7/11 && y > Height * 20/65 && y < Height * 35/65 && Level.level == 0) {
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
						Level.checkMelody();
					startX = x;
					startY = y;
					if (x > Width - 3 * BIRDS_HEIGHT && y > Height * 8 / 11)
						Level.playMelody(Level.melodyOrder);
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
			if (state.equals("Game") && Level.timeBool) Level.time++;
			fase_scr1 = !fase_scr1;
		}

		@Override
		public void onFinish() {
			start();
		}
	}
}


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
	public static Canvas canvas;
	public static MediaPlayer mediaPlayer;
	public static States state = State.state;
	private Birds activeBird;
	static List<Birds> birds = new LinkedList<>();

	public static boolean musicBool = true;

	public static int Width, Height;
	static float allX = 0, allXspeed = 0;
	float cloudsX = 0, cloudsXspeed = -1;
	static int[] drawCounter = new int[10];
	float deltaX, deltaY, startX, startY;

	public static final int
			  BIRDS_BITMAP_COLUMNS = 9,
			  BIRDS_BITMAP_STRINGS = 5 - 2;
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
		state = State.state;
		MainView.canvas = canvas;

		if (musicBool && !mediaPlayer.isPlaying() && !MainActivity.paused && State.state != States.GAME)
			mediaPlayer.start();
		else if (!musicBool && mediaPlayer.isPlaying()) mediaPlayer.pause();

		allX += allXspeed;
		cloudsX += cloudsXspeed;
		for (int i = 0; i < drawCounter.length; i++) drawCounter[i] += (drawCounter[i] < 200) ? -1 : 100;
		if (cloudsX <= -Width) cloudsX = Width + cloudsX;

		if (State.isMoving()) {
			allXspeed += (Math.abs(allX) <= Width / 2) ? -0.6f : 0.58f;
			if ((int) allX / 10 == -Width / 10 && allX < Width / 2) {
				allX = 0;
				allXspeed = 0;
				State.state = State.MovingTo();
			}
		}

		if (State.state == States.GAME) {
			if (mediaPlayer.isPlaying()) mediaPlayer.pause();
		} else if (!mediaPlayer.isPlaying() && !MainActivity.paused && musicBool)
			mediaPlayer.start();

		drawB(mResources.back, allX, 0);
		drawB(mResources.back, allX + Width, 0);
		drawB(mResources.clouds, cloudsX, 0);
		drawB(mResources.clouds, cloudsX + Width, 0);
		drawB(mResources.wire, allX, 0);
		drawB(mResources.wire, allX + Width, 0);

		State.draw();
		State.animate(drawCounter);

		if (State.MovingTo() == States.GAME || State.MovingFrom() == States.GAME)
			for (Birds b : birds)
				b.drawBird(canvas);

		if (State.MovingTo() == States.LEVEL || State.MovingFrom() == States.LEVEL) {
			float textX = (State.state == States.LEVEL || State.MovingFrom() == States.LEVEL) ? textX = allX : allX + Width;
			canvas.drawText(Level.comment, textX + Width * 7 / 16, Height * 0.55f, mResources.textComment);
			canvas.drawText((Level.newRecord) ? "Новый рекорд! " + Level.score + ((Level.score % 10 < 4 && Level.score != 0 && (Level.score < 5 || Level.score > 20)) ? " очка" : " очков") : Level.score + ((Level.score % 10 < 4) ? " очка" : " очков") + ((Level.hardBool) ?
					  ((Level.recordHard != 0) ? ", рекорд - " + Level.recordHard : "") :
					  ((Level.recordEasy != 0) ? ", рекорд - " + Level.recordEasy : "")), textX + 30, Height - Height / 18, mResources.textInfo);
		}

		if (State.state == States.GAME) {
			String TS = "Time: " + Level.time / 60 + ":";
			TS += (Level.time % 60 < 10) ? "0" + Level.time % 60 : Level.time % 60;

			canvas.drawText("Score: " + Level.score,
					  // + ((Level.score % 10 < 4 && Level.score != 0 && (Level.score < 5 || Level.score > 20)) ? " очка" : " очков")
					  MainView.Height / 30, MainView.Height / 30, mResources.textScores);
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

	public static void drawB(Bitmap res, float x, float y) {
		canvas.drawBitmap(res, x, y, mResources.paint);
	}

	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX(), y = event.getY();

		if (State.state == States.MAIN && event.getAction() == MotionEvent.ACTION_DOWN)
			State.state = States.MAIN_MENU;

		if (State.state == States.MENU && event.getAction() == MotionEvent.ACTION_DOWN)
			if (x > Width * 35 / 110 && x < Width * 6 / 11 && y > Height * 20 / 65 && y < Height * 40 / 65) {
				Level.newGame();
				State.state = States.MENU_GAME;
			} else if (x > Width * 2 / 11 && x < Width * 35 / 110 && y > Height * 20 / 65 && y < Height * 35 / 65) {
				Level.rulesDialog();
			} else if (x > Width * 6 / 11 && x < Width * 7 / 11 && y > Height * 20 / 65 && y < Height * 35 / 65) {
				Level.settingsDialog();
			}


		if (State.state == States.GAME) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					for (Birds b : birds)
						if (x > b.x + BIRDS_WIDTH * 0.2 && x < (b.x + BIRDS_WIDTH * 1.2) && y > b.y + BIRDS_HEIGHT * 0.2 && y < (b.y + BIRDS_HEIGHT * 1.2)) {
							deltaX = b.x - x;
							deltaY = b.y - y;
							activeBird = b;
						}

					if (x > Width / 2 - BIRDS_WIDTH / 2 && x < Width / 2 + BIRDS_WIDTH / 2 && y < BIRDS_HEIGHT)
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

		if (State.state == States.LEVEL) {
			if (State.victory) Level.nextLevel();
			else Level.newGame();

			State.state = States.LEVEL_GAME;
		}

		return true;
	}

	class Timer extends CountDownTimer {

		public Timer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			if (State.state == States.GAME && Level.timeBool) Level.time++;
		}

		@Override
		public void onFinish() {
			start();
		}
	}
}
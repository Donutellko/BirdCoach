package com.donutellko.birdcoach;

/**
 * Класс содержит методы, осуществляющие отрисовку, загрузку ресурсов, управляет игрой (таймер).
 */

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

	static forwElement screenMain, screenMenu, screenRules, screenGame, screenVictory, screenLose;
	static forwElement gameMp, gameNote, gameBush;
	static backElement hills, weed, wire, sky = new backElement(null, 0, -1);


	static int
			  BIRDS_BITMAP_COLUMNS = 9, BIRDS_BITMAP_STRINGS = 5,
			  BIRDS_HEIGHT, BIRDS_WIDTH;

	static int timer = 0;

	static CountDownTimer m_Timer;

	public mainView(Context context) {
		super(context);

		mainView.context = context;
		if (m_Timer == null)
			m_Timer = new Timer(100000000000000L, 1).start();
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
		screenRules = new forwElement(Res.forwardRules, 0, (int) (-Height * 0.66));


		gameMp = new forwElement(Res.megaphone, Width - 3 * BIRDS_WIDTH, Height - BIRDS_HEIGHT * 3, BIRDS_HEIGHT * 3 * 650 / 750, BIRDS_HEIGHT * 3);
		gameNote = new forwElement(Res.note, Width / 2 - BIRDS_WIDTH / 4, Height / 20, BIRDS_HEIGHT * 2 / 3, BIRDS_HEIGHT * 2 / 3);
		gameBush = new forwElement(Res.forwardGame, Width - 4 * BIRDS_WIDTH, Height * 8 / 11, w, h);

		Xacc = -Width / 1000f;
		Yacc = Height / 600f;

		//
		if (Xacc < -2) Xacc = -1.3f;
		if (Yacc > 1.8) Yacc = 1.8f;
		//

		Log.i("Xacc+Width", "Xacc = " + Xacc + "Yacc = \" + Yacc +; Width = " + Width);

		LevelThread = new Level("LevelThread");
		LevelThread.start();

	}

	protected void onDraw(Canvas canvas) {
		sCanvas = canvas;

		sky.draw();
		hills.draw();
		weed.draw();
		wire.draw();

		if (State.MovingFrom() != null)
			switch (State.MovingFrom()) {
				case MAIN:
					screenMain.draw();
					break;
				case MENU:
					screenMenu.draw();
					break;
				case RULES:
					screenMenu.draw();
					screenRules.draw(0, (int) (-Height * 0.66));
					break;
				case SETTINGS:
				case RECORDS:
					screenMenu.draw();
					break;
				case GAME:
					screenGame.draw();
					break;
				case LEVEL:
					if (State.victoryBool) screenVictory.draw();
					else screenLose.draw();
					break;
			}

		if (State.MovingTo() != null)
			switch (State.MovingTo()) {
				case MAIN:
					screenMain.draw(+Width);
					break;
				case MENU:
					screenMenu.draw(Width);
					break;
				case RULES:
					screenMenu.draw();
					screenRules.draw(0, (int) (-Height * 0.66));
					break;
				case SETTINGS:
				case RECORDS:
					screenMenu.draw();
					break;
				case GAME:
					screenGame.draw(+Width);
					break;
				case LEVEL:
					if (State.victoryBool) screenVictory.draw(+Width);
					else screenLose.draw(+Width);
					break;
			}

		State.animate(drawCounter);

		if (State.CheckToFrom(States.GAME)) ;
		for (Birds b : birds) b.drawBird(canvas);

		if (State.CheckToFrom(States.LEVEL))
			Dialogs.writeComment();

		if (State.CheckToFrom(States.SETTINGS)) {
			float plusY = (float) (forwardY - Height * 0.6);
			int x = Width / 2;
			int mult = Height / 11;
			canvas.drawText(context.getString(R.string.s_details), 20, plusY + 20, Res.textSettings);
			canvas.drawText((Level.musicBool ? context.getString(R.string.s_mus_off) : context.getString(R.string.s_mus_on)), x, plusY + mult * 3, Res.textSettings);
			canvas.drawText(context.getString(R.string.s_reset_table), x, plusY + mult * 4, Res.textSettings);
			canvas.drawText((Level.hardBool ? context.getString(R.string.s_diff_master) : context.getString(R.string.s_diff_newbie)), x, plusY + mult * 5, Res.textSettings);
			canvas.drawText(context.getString(R.string.s_back), x, plusY + mult * 6, Res.textSettings);
		} else if (State.CheckToFrom(States.RECORDS)) {
			float plusY = (float) (forwardY - Height * 0.6);
			int x = Width / 2;
			int mult = Height / 11;
			canvas.drawText(context.getString(R.string.s_rec_table), Width * 35 / 100, plusY + mult, Res.textSettings);

			canvas.drawText("Новичок:", Width * 2 / 10, plusY + mult * 2, Res.textSettings);
			canvas.drawText("Маэстро:", Width * 6 / 10, plusY + mult * 2, Res.textSettings);
			for (int i = 0; i < 5; i++) {
				canvas.drawText((i + 1) + ".", Width * 2 / 10, plusY + mult * (3 + i), Res.textSettings);
				canvas.drawText((i + 1) + ".", Width * 6 / 10, plusY + mult * (3 + i), Res.textSettings);

				canvas.drawText(Level.nameRecordEasy[i] + "", Width * 25 / 100, plusY + mult * (3 + i), Res.textSettings);
				canvas.drawText(Level.nameRecordHard[i] + "", Width * 65 / 100, plusY + mult * (3 + i), Res.textSettings);

				canvas.drawText(Level.recordEasy[i] + "", Width * 35 / 100, plusY + mult * (3 + i), Res.textSettings);
				canvas.drawText(Level.recordHard[i] + "", Width * 75 / 100, plusY + mult * (3 + i), Res.textSettings);
			}
		}

		if (State.state == States.GAME) {
			canvas.drawText(context.getString(R.string.s_scores) + Level.score, Height / 30, Height / 30, Res.textScores);
			canvas.drawText(context.getString(R.string.s_time) + Level.timeText(), Width * 7 / 8, Height / 30, Res.textTime);

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

		Log.i("Touch", "onTouchEvent" + " " + State.state + " " + Width + " " + forwardX + " " + Xspeed + " " + Xacc);

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
			else if (x > Width * 17 / 160 && x < Width * 31 / 160 && y > Height * 21 / 80 && y < Height * 19 / 40)
				State.state = States.MENU_RECORDS;
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

		if (State.state == States.RULES && event.getAction() == MotionEvent.ACTION_UP)
			State.state = States.RULES_MENU;
		else if (State.state == States.RECORDS && event.getAction() == MotionEvent.ACTION_UP)
			State.state = States.RECORDS_MENU;
		if (State.state == States.SETTINGS && event.getAction() == MotionEvent.ACTION_UP) {
			if (y > Height / 11 * 3 && y < Height / 11 * 4) {
				Level.musicBool = !Level.musicBool;
			} else if (y > Height / 11 * 4 && y < Height / 11 * 5) {
				Level.recordEasy = new int[5];
				Level.recordHard = new int[5];
			} else if (y > Height / 11 * 5 && y < Height / 11 * 6) {
				Level.hardBool = !Level.hardBool;
			} else if (y > Height / 11 * 6 && y < Height / 11 * 7) {
				State.state = States.SETTINGS_MENU;
			}
		}


		if (State.state == States.LEVEL) {
			if (State.victoryBool) Level.nextLevel();
			else Level.newGame();

			State.state = States.LEVEL_GAME;
		}

		return true;
	}

	public class Timer extends CountDownTimer {

		public Timer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			mainView.timer++;
			if (State.state == States.GAME && Level.timeBool && mainView.timer % 60 == 0) {
				Level.time--;
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
				drawCounter[i] += (drawCounter[i] < 2 * 200) ? -1 : 100;

			if (State.isMoving()) {
				if (State.CheckToFrom(States.MENU) && (State.CheckToFrom(States.RULES) || State.CheckToFrom(States.SETTINGS) || State.CheckToFrom(States.RECORDS))) {
					if (State.CheckTo(States.RULES) || State.CheckTo(States.SETTINGS) || State.CheckTo(States.RECORDS)) {
						if (forwardY >= Height * 0.66) {
							State.state = State.MovingTo();
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
					screenRules.moveY();
				} else {
					hills.moveX();
					weed.moveX();
					wire.moveX();
					forwardX += Xspeed;

					if (forwardX <= -Width) {
						Xspeed = 0;
						forwardX = 0;
						State.state = State.MovingTo();
						if (State.state == States.MENU) Level.level = 0;
					} else {
						if (forwardX > -Width / 2) Xspeed += Xacc;
						else Xspeed = (Xspeed >= -2) ? -2 : Xspeed - Xacc;
					}
				}
			}
		}

		@Override
		public void onFinish() {
			start();
		}
	}
}

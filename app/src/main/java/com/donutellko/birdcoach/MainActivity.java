package com.donutellko.birdcoach;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Индивидуальный проект Шергалиса Доната (donutellko@gmail.com).
 * Info: Игра BirdCoach ("Птичий тренер"), призвана развивать у детей музыкальный слух в игровой форме.
 * Основная информация находится в файле BirdCoach.pdf
 * <p/>
 * Условные обозначения в комментариях:
 * _ : временное решение
 * - : не выполненная задача
 * + : выполненная задача
 * : проблемный участок
 * . : задача выполнена и работает, как надо.
 * <p/>
 * Префиксы:
 * b : кнопка
 * c : onClick method
 * t : временная переменная
 * e : EditText
 */

/*
 Список Activity:
     _ MainActivity
     _ EndlessActivity для бесконечного режима игры

 */

public class MainActivity extends AppCompatActivity {

	static boolean paused = false;
	static SharedPreferences sPref;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new MainView(this));
		Level.recordHard = loadScore(true);
		Level.recordEasy = loadScore(false);
	}

	@Override
	protected void onStop() {
		super.onStop();
		saveScore(Level.recordEasy, false);
		saveScore(Level.recordHard, true);

		paused = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (MainView.mediaPlayer != null)
			if (MainView.mediaPlayer.isPlaying()) MainView.mediaPlayer.pause();
		paused = true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		paused = false;
		if (MainView.mediaPlayer != null && paused == false)
			if (!MainView.mediaPlayer.isPlaying())
				MainView.mediaPlayer.pause();
	}

	public void onBackPressed() {
		if (State.state == States.MAIN) finish();
		else if (State.state == States.MENU) State.state = States.MENU_MAIN;
		else if (State.state == States.GAME) {
			Level.timeBool = false;
			pauseDialog();
		} else if (State.state == States.LEVEL) pauseDialog();
	}

	private void pauseDialog() {
		AlertDialog.Builder pauseDialog = new AlertDialog.Builder(MainActivity.this);
		String[] variants = {"Продолжить", "Меню", "Выйти"};

		pauseDialog.
				  setTitle("Настройки")
				  .setIcon(R.mipmap.ic_launcher)
				  .setCancelable(true)
				  .setItems(variants, new DialogInterface.OnClickListener() {
					  @Override
					  public void onClick(DialogInterface dialog, int which) {
						  if (which == 0) {
							  if (State.state == States.GAME) Level.timeBool = true;
							  dialog.cancel();
						  } else if (which == 1)
							  State.state = (State.state == States.GAME) ? States.GAME_MENU : States.LEVEL_MENU;
						  else finish();
					  }
				  });
		AlertDialog alert = pauseDialog.create();
		alert.show();
	}

	static int loadScore(boolean hard) {
		sPref = MainView.context.getSharedPreferences("Scores", Context.MODE_PRIVATE);
		return sPref.getInt((hard) ? "Hard" : "Easy", 0);
	}

	static void saveScore(int score, boolean hard) {
		sPref = MainView.context.getSharedPreferences("Scores", Context.MODE_PRIVATE);
		SharedPreferences.Editor ed = sPref.edit();
		ed.putInt((hard) ? "Hard" : "Easy", score);
		ed.commit();
	}
}
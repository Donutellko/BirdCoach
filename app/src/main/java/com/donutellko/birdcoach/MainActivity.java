package com.donutellko.birdcoach;


import android.app.AlertDialog;
import android.content.DialogInterface;
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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new MainView(this));
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
/*
	public void onBackPressed() {
		if (MainView.state.equals("Main")) finish();
		else if (MainView.state.equals("Menu")) MainView.state = "Menu+Main";
		else if (MainView.state.equals("Game")) {
			Level.timeBool = false;
			pauseDialog();
		} else if (MainView.state.equals("Level")) pauseDialog();
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
							  if (MainView.state.equals("Game")) Level.timeBool = true;
							  dialog.cancel();
						  } else if (which == 1)
							  MainView.state = (MainView.state.equals("Game")) ? "Game+Menu" : "Level+Menu";
						  else finish();
					  }
				  });
		AlertDialog alert = pauseDialog.create();
		alert.show();
	}
	*/
}
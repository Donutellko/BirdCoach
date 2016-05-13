package com.donutellko.birdcoach;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	static boolean paused = false;
	static SharedPreferences sPref;
	public static Res ResThread;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new mainView(this));

		ResThread = new Res();
		ResThread.start();

		Level.recordHard = loadScore(true);
		Level.recordEasy = loadScore(false);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (Res.player != null && !paused)
			if (!Res.player.isPlaying())
				Res.player.pause();

		paused = false;
	}


	@Override
	protected void onPause() {
		super.onPause();

		if (Res.player != null)
			if (Res.player.isPlaying()) Res.player.pause();

		paused = true;
	}

	@Override
	protected void onStop() {
		super.onStop();

		saveScore(Level.recordEasy, false);
		saveScore(Level.recordHard, true);

		paused = true;
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
		String[] variants = {"Продолжить", "Меню", (Level.musicBool) ? "Выключить музыку" : "Включить музыку", "Выйти"};

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
						  } else if (which == 1) {
							  State.state = (State.state == States.GAME) ? States.GAME_MENU : States.LEVEL_MENU;
						  } else if (which == 2) {
							  Level.musicBool = !Level.musicBool;
						  } else finish();
					  }
				  });
		AlertDialog alert = pauseDialog.create();
		alert.show();
	}

	static int loadScore(boolean hard) {
		sPref = mainView.context.getSharedPreferences("Scores", Context.MODE_PRIVATE);
		return sPref.getInt((hard) ? "Hard" : "Easy", 0);
	}

	static void saveScore(int score, boolean hard) {
		sPref = mainView.context.getSharedPreferences("Scores", Context.MODE_PRIVATE);
		SharedPreferences.Editor ed = sPref.edit();
		ed.putInt((hard) ? "Hard" : "Easy", score);
		ed.apply();
	}

}
package com.donutellko.birdcoach;

/**
 * Класс обрабатывает нажатие кнопки "Назад", отображает диалог паузы, загружает рекорды.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

	static boolean paused = false;
	static SharedPreferences sPref;
	public static Res ResThread;

	protected void onCreate(Bundle savedInstanceState) {
		Log.e("", "onCreate()_1");
		super.onCreate(savedInstanceState);
		Log.e("", "onCreate()_2");
		setContentView(new mainView(this));
		Log.e("", "onCreate()_3");
		ResThread = new Res();
		ResThread.start();

		Level.recordHard = loadScore(true);
		Level.recordEasy = loadScore(false);
		Log.e("", "onCreate()_4");
	}

	@Override
	protected void onResume() {
		Log.e("", "onResume()_1");
		if (Res.player != null && !paused)
			if (!Res.player.isPlaying())
				Res.player.pause();

		paused = false;

		loadProgress();
		Log.e("", "onResume()2");
		super.onResume();
		Log.e("", "onCreate()3");
	}


	@Override
	protected void onPause() {
		Log.e("", "onPause()1");
		super.onPause();
		Log.e("", "onPause()2");
		if (Res.player != null)
			if (Res.player.isPlaying()) Res.player.pause();

		paused = true;

		saveScore(Level.recordEasy, false);
		saveScore(Level.recordHard, true);

		if (Level.level > 0)
			saveProgress(true);
		else saveProgress(false);

		Log.e("", "onPause()3");

	}

	@Override
	protected void onStop() {
		paused = true;
		Log.e("", "onStop()1");
		super.onStop();
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
		String[] variants = {getString(R.string.s_cancel), getString(R.string.s_menu), (Level.musicBool) ? getString(R.string.s_mus_off) : getString(R.string.s_mus_on), getString(R.string.s_exit)};

		pauseDialog.
				  setTitle(getString(R.string.s_settings))
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

	static int[] loadScore(boolean hard) {
		Log.i("", "Loading progress...");
		int[] tmp = new int[5];
		sPref = mainView.context.getSharedPreferences("Scores", Context.MODE_PRIVATE);
		tmp[0] = sPref.getInt((hard) ? "Hard0" : "Easy0", 0);
		tmp[1] = sPref.getInt((hard) ? "Hard1" : "Easy1", 0);
		tmp[2] = sPref.getInt((hard) ? "Hard2" : "Easy2", 0);
		tmp[3] = sPref.getInt((hard) ? "Hard3" : "Easy3", 0);
		tmp[4] = sPref.getInt((hard) ? "Hard4" : "Easy4", 0);
		return tmp;
	}

	static void saveScore(int[] score, boolean hard) {
		Log.i("", "Saving scores to memory...");
		sPref = mainView.context.getSharedPreferences("Scores", Context.MODE_PRIVATE);
		SharedPreferences.Editor ed = sPref.edit();
		ed.putInt((hard) ? "Hard0" : "Easy0", score[0]);
		ed.putInt((hard) ? "Hard1" : "Easy1", score[1]);
		ed.putInt((hard) ? "Hard2" : "Easy2", score[2]);
		ed.putInt((hard) ? "Hard3" : "Easy3", score[3]);
		ed.putInt((hard) ? "Hard4" : "Easy4", score[4]);
		ed.apply();
	}

	static void saveProgress(boolean is) {
		Log.e("", "Saving progress to memory...");
		sPref = mainView.context.getSharedPreferences("Scores", Context.MODE_PRIVATE);
		SharedPreferences.Editor ed = sPref.edit();
		if (is) {
			Log.i("", "Saving progress to memory...");
			ed.putBoolean("is", true);
			ed.putInt("level", Level.level);
			ed.putBoolean("hardBool", Level.hardBool);
			ed.putInt("melodyLength", Level.melodyOrder.length);
			for (int i = 0; i < Level.melodyOrder.length; i++)
				ed.putInt("melody_" + i, Level.melodyOrder[i]);
			ed.putInt("time", Level.time);
			Log.i("", "Progress saved.");
		} else {
			ed.putBoolean("is", false);
			Log.i("", "Nothing to save.");
		}
		ed.apply();
	}

	static void loadProgress() {
		Log.e("", "Loading progress...");
		if (sPref.getBoolean("is", false)) {
			Level.newGame();
			sPref = mainView.context.getSharedPreferences("Scores", Context.MODE_PRIVATE);
			Level.level = sPref.getInt("level", 0);
			Level.hardBool = sPref.getBoolean("hardBool", false);
			Level.time = sPref.getInt("time", 0);
			int l = sPref.getInt("melodyLength", 0);
			Level.melodyOrder = new int[l];
			for (int i = 0; i < l; i++)
				Level.melodyOrder[i] = sPref.getInt("melody_" + i, 0);
			Log.i("", "Progress loaded.");
		} else {
			Log.i("", "Nothing to load");
		}
	}
}

package com.donutellko.birdcoach;

import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.Random;

/**
 * level:   1   2   3   4   5   6   7   8   9   10  11  12
 * N:       4   4:1 5   5:1 6   6:2 7   7:2 8   8:3 9   9:3
 */


public class Level extends Thread {
	static int level = 0, score = 0, time = 0, lifes = 3, howMushTimesYouCanListenTheMelody;

	static boolean timeBool = false;
	static AlertDialog.Builder dialog = new AlertDialog.Builder(MainView.context);

	static int[] melodyOrder = new int[1];
	static Birds[] birdPlaced = new Birds[9];

	static double[] wirePlaceX = {0.014, 0.104, 0.200, 0.296, 0.394, 0.490, 0.579, 0.676, 0.779};
	static double[] wirePlaceY = {0.177, 0.192, 0.206, 0.209, 0.205, 0.200, 0.193, 0.172, 0.156};

	static double[] bushPlaceX = {0.134, 0.176, 0.196, 0.215, 0.281, 0.287, 0.329, 0.380, 0.396, 0.435, 0.472, 0.540};
	static double[] bushPlaceY = {0.767, 0.560, 0.691, 0.814, 0.612, 0.730, 0.855, 0.740, 0.582, 0.814, 0.657, 0.764};

	@Override
	public void run() {
		checkMelody();
	}

	public static void newGame() {
		score = 0;
		lifes = 3;
		level = 0;
		nextLevelDialog("Новая игра", 4);
		AlertDialog alert = dialog.create();
		alert.show();
		nextLevel();
	}

	public static void nextLevel() {
		for (int i = 0; i < birdPlaced.length; i++) birdPlaced[i] = null;
		if (level > 0) {
			if (level != 0) score += level * 100 + (level * 20 - time) % (level * 100);
		}
		level++;
		time = 0;
		timeBool = false;
		melodyOrder = generateMelody((level + 1) / 2 + 3);
		howMushTimesYouCanListenTheMelody = 3;
		createBirds(melodyOrder);

		if (level > 1) {
			nextLevelDialog("Следующий уровень", melodyOrder.length);
			AlertDialog alert = dialog.create();
			alert.show();
		}
	}

	public static void createBirds(int[] order) {
		MainView.birds.clear();
		for (int i = 0; i < order.length; i++) {
			MainView.birds.add(new Birds((float) (bushPlaceX[i]) * MainView.Width, (float) (bushPlaceY[i]) * MainView.Height, order[i]));
		}
	}

	public static void checkPlace(Birds b) {
		int r = MainView.BIRDS_WIDTH;
		for (int i = 0; i < melodyOrder.length; i++) {
			float X = (float) (wirePlaceX[i] * MainView.Width);
			float Y = (float) (wirePlaceY[i] * MainView.Height);

			if ((b.x - X) * (b.x - X) + (b.y - Y) * (b.y - Y) < r * r) {
				if (birdPlaced[i] == null) {
					birdPlaced[i] = b;
					b.x = X;
					b.y = Y;
				}
			} else if (b == birdPlaced[i]) birdPlaced[i] = null;
		}
	}

	private static int[] generateMelody(int count) {
		int[] order = new int[count];
		Random random = new Random();
		for (int i = 0; i < count; i++)
			order[i] = (random.nextInt(9));
		System.out.println("playMelody" + order.length);
		return order;
	}

	public static void checkMelody() {
		int i = 0;
		int c = 0;
		if (birdPlaced[0] != null) {
			for (Birds b : birdPlaced) {
				if (birdPlaced[i] != null) {
					if (b.type == melodyOrder[i]) {
						c++;
						b.sound(MainView.context);
						try {
							Thread.sleep(330);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else break;
				} else break;
				i++;
			}
			if (c == i && i == melodyOrder.length) {
				MainView.state = "Game+Level";
				MainView.stateL = "Victory";
			} else {
				Level.lifes--;
				mResources.sounds.play(mResources.mistakeSound, 1.0f, 1.0f, 1, 0, 1.0f);
				// mResources.sounds.play(mResources.mistakeSound, 1.0f, 1.0f, )
				if (lifes == 0) {
					MainView.state = "Game+Level";
					MainView.stateL = "Loose";
				}
				mistakeDialog();
			}
		}
	}

	public static void playMelody(int[] order) {
		if (howMushTimesYouCanListenTheMelody != 0)
		for (int i = 0; i < order.length; i++) {
			playBirdSound(order[i]);
			try {
				Thread.sleep(330);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		howMushTimesYouCanListenTheMelody--;
	}

	public static void playBirdSound(int type) {
		//float speed = (float) (0.5 + (EndlessView.BIRDS_BITMAP_COLUMNS - type) * (1.5 / EndlessView.BIRDS_BITMAP_COLUMNS));
		int sound = mResources.birdSounds[MainView.BIRDS_BITMAP_COLUMNS - type - 1];
		mResources.sounds.play(sound, 1.0f, 1.0f, type, 0, 1.0f);
	}

	public static void nextLevelDialog(String title, int count) {
		timeBool = false;
		String s;
		if (count % 10 <= 4 && (count > 20 || count < 10)) s = " птицы";
		else s = " птиц";

		dialog.setTitle(title)
				  .setMessage("Уровень " + (level + 1) + "\n" + count + s)
				  .setIcon(R.mipmap.ic_launcher)
				  .setCancelable(false)
				  .setNegativeButton("Начать",
							 new DialogInterface.OnClickListener() {
								 public void onClick(DialogInterface dialog, int id) {
									 // Level.playMelody(Level.melodyOrder);
									 Level.timeBool = true;
									 dialog.cancel();
								 }
							 });
	}

	private static void mistakeDialog() {
		dialog.setTitle("Упс!")
				  .setMessage("Кажется, ты перепутал звуки\nПрослушай мелодию заново и попробуй ещё раз!")
				  .setIcon(R.mipmap.ic_launcher)
				  .setCancelable(false)
				  .setNegativeButton("Прослушать",
							 new DialogInterface.OnClickListener() {
								 public void onClick(DialogInterface dialog, int id) {
									 Level.playMelody(Level.melodyOrder);
									 Level.timeBool = true;
									 dialog.cancel();
								 }
							 });
	}
}
package com.donutellko.birdcoach;

import android.app.AlertDialog;
import java.util.Random;

/**
 * level:   1   2   3   4   5   6   7   8   9   10  11  12
 * N:       4   4:1 5   5:1 6   6:2 7   7:2 8   8:3 9   9:3
 */


public class Level {
	static int level = 0, score = 0, time = 0, lifes = 3;  // Количество оставшихся жизней игрока.
	static int[] melodyOrder = new int[1];
	static boolean timeBool = false;

	static Birds[] birdPlaced = new Birds[9];

	static double[] wirePlaceX = {0.104, 0.014, 0.200, 0.296, 0.394, 0.490, 0.579, 0.676, 0.779};
	static double[] wirePlaceY = {0.206, 0.182, 0.211, 0.214, 0.210, 0.204, 0.198, 0.177, 0.156};

	static double[] bushPlaceX = {0.134, 0.176, 0.196, 0.215, 0.281, 0.287, 0.329, 0.380, 0.396, 0.435, 0.472, 0.540};
	static double[] bushPlaceY = {0.772, 0.565, 0.696, 0.819, 0.617, 0.735, 0.860, 0.745, 0.587, 0.819, 0.662, 0.769};


	public static void newGame() {
		AlertDialog alert = mResources.builder.create();
		alert.show();
		score = 0;
		lifes = 3;
		level = 0;
		nextLevel();
	}

	public static void nextLevel() {
		if (level > 1)
			score += level * 100 + (level * 20 - time) % (level * 100);
		level++;
		time = 0;
		timeBool = false;
		//melodyOrder = generateMelody(12);
		melodyOrder = generateMelody(level + 3);
		createBirds(melodyOrder);
	}

	public static void restartLevel() {
		score -= level * 50 * (4 - lifes);
		lifes--;
		createBirds(melodyOrder);
		playMelody(melodyOrder);
	}


	public static void createBirds(int[] order) {
		Random random = new Random();
		for (Birds b : EndlessView.birds) EndlessView.birds.remove(b);
		for (int i = 0; i < order.length; i++) {
			EndlessView.birds.add(new Birds((float) (bushPlaceX[i]) * MainView.Width, (float) (bushPlaceY[i]) * MainView.Height, order[i]));
		}
	}

	public static void checkPlace(Birds b) {
		int r = EndlessView.BIRDS_WIDTH;
		for (int i = 0; i < 9; i++) {
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
		//int count = level
		for (Birds b : birdPlaced) {
			if (birdPlaced[i] != null)
				if (b.type == melodyOrder[i]) {
					b.sound(EndlessView.context);
					try {
						Thread.sleep(330);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else break;
			i++;
		}
	}

	public static void playMelody(int[] order) {
		System.out.println("playMelody" + order.length);
		for (int i = 0; i < order.length; i++) {
			playBirdSound(order[i]);
			try {
				Thread.sleep(330);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void playBirdSound(int type) {
		//float speed = (float) (0.5 + (EndlessView.BIRDS_BITMAP_COLUMNS - type) * (1.5 / EndlessView.BIRDS_BITMAP_COLUMNS));
		int sound = mResources.birdSounds[EndlessView.BIRDS_BITMAP_COLUMNS - type - 1];
		mResources.sounds.play(sound, 1.0f, 1.0f, type, 0, 1.0f);
	}
}
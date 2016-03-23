package com.donutellko.birdcoach;

import android.app.AlertDialog;

import java.util.Random;

/**
 * level:   1   2   3   4   5   6   7   8   9   10  11  12
 * N:       4   4:1 5   5:1 6   6:2 7   7:2 8   8:3 9   9:3
 */


public class Level {
	static int level = 0;  // Номер уровня, из него вычисляется количество птиц в мелодии и избыточных птиц.
	static int score = 0;  // Количество очков за игру (за все уровни до поражения или рестарта игры)
	static int time = 0;   // Время, затраченное на данный уровень
	static int lifes = 3;  // Количество оставшихся жизней игрока.
	static int[] melodyOrder = new int[1];

	static double[] wirePlaceX = {0.104, 0.014, 0.200, 0.296, 0.394, 0.490, 0.579, 0.676, 0.779};
	static double[] wirePlaceY = {0.206, 0.182, 0.211, 0.214, 0.210, 0.204, 0.198, 0.177, 0.156};

	//static double[] wirePlaceX = {0.103685, 0.013844, 0.200354, 0.296120, 0.393998, 0.490112, 0.579214, 0.675597, 0.778858};
	//static double[] wirePlaceY = {0.205975, 0.181875, 0.210666, 0.213823, 0.210041, 0.204156, 0.198212, 0.176853, 0.156238};

	static double[] bushPlaceX = {0.134, 0.176, 0.196, 0.215, 0.281, 0.287, 0.329, 0.380, 0.396, 0.435, 0.472, 0.540};
	static double[] bushPlaceY = {0.772, 0.565, 0.696, 0.819, 0.617, 0.735, 0.860, 0.745, 0.587, 0.819, 0.662, 0.769};

	//static double[] bushPlaceX = {0.134398, 0.176492, 0.196130, 0.215707, 0.281342, 0.287648, 0.329985, 0.380666, 0.396218, 0.435892, 0.472123, 0.540161};
	//static double[] bushPlaceY = {0.772262, 0.565192, 0.696147, 0.819338, 0.617925, 0.735689, 0.860821, 0.745906, 0.587282, 0.819180, 0.662847, 0.769174};
	static Birds[] birdPlaced = new Birds[9];

	public static void nextLevel() {
		if (level > 1)
			score += level * 100 /* гарантированные очки */ + (level * 20 - time) % (level * 100) /* дополнительные очки */;
		level++;
		time = 0;


		melodyOrder = generateMelody(12);


		//melodyOrder = generateMelody(level + 4);
		createBirds(melodyOrder);
	}

	public static void restartLevel() {
		score -= level * 50 * (4 - lifes);
		lifes--;
		createBirds(melodyOrder);
	}

	public static void newGame() {
		AlertDialog alert = mResources.builder.create();
		alert.show();
		score = 0;
		lifes = 3;
		level = 0;
		nextLevel();
	}

	public static void createBirds(int[] order) {
		Random random = new Random();
		for (Birds b : EndlessView.birds)
			EndlessView.birds.remove(b);
		for (int i = 0; i < order.length; i++) {
			EndlessView.birds.add(new Birds((float) (bushPlaceX[i]) * EndlessView.Width, (float) (bushPlaceY[i]) * EndlessView.Height, order[i]));
		}

		//for (int i = 0; i < 12; i++) wirePlaceX[i] = (int) (400 + EndlessView.BIRDS_WIDTH * i);
	}

	public static void checkPlace(Birds b) {
		int a = 200;
		int s = EndlessView.BIRDS_WIDTH;
		for (int i = 0; i < 9; i++) {
			float X = (float) (wirePlaceX[i] * EndlessView.Width);
			float Y = (float) (wirePlaceY[i] * EndlessView.Height);

			if (b.x + s + a > X && b.y + s + a > Y && b.x - a < X && b.y - a < Y) {
				if (birdPlaced[i] == null) {
					birdPlaced[i] = b;
					b.x = X;
					b.y = Y;
				}
			} else if (b == birdPlaced[i]) birdPlaced[i] = null;
		}
	}

	public static void checkMelody() {
		int i = 0;
		for (Birds b : EndlessView.birds) {
			if (b.type == melodyOrder[i]) {
				b.sound(EndlessView.context);
			}
			i++;
		}
	}

	private static int[] generateMelody(int count) {
		int[] order = new int[count];
		Random random = new Random();
		for (int i = 0; i < count; i++)
			order[i] = (random.nextInt(9));
		return order;
	}
}

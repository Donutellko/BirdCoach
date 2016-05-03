package com.donutellko.birdcoach;

import android.util.Log;
import java.util.Random;

/**
 * level:   1   2   3   4   5   6   7   8   9   10  11  12
 * N:       4   4:1 5   5:1 6   6:2 7   7:2 8   8:3 9   9:3
 */


public class Level extends Thread {
	static int level = 0, score = 0, time = 0, lifes = 3, howMushTimesYouCanListenTheMelody;
	// static int levels[] = {3, 4, 4, 5, 5, 6, 6, 6, 7, 7, 8, 8, 9};

	static boolean timeBool = false, hardBool = false, musicBool = true;

	static int[] melodyOrder = new int[1];
	static Birds[] birdPlaced = new Birds[9];

	static Random random = new Random();

	static double[] wirePlaceX = {0.014, 0.104, 0.200, 0.296, 0.394, 0.490, 0.579, 0.676, 0.779};
	static double[] wirePlaceY = {0.172, 0.188, 0.196, 0.205, 0.200, 0.200, 0.190, 0.170, 0.150};

	static double[] bushPlaceX = {0.134, 0.176, 0.196, 0.215, 0.281, 0.287, 0.329, 0.380, 0.396, 0.435, 0.472, 0.540};
	static double[] bushPlaceY = {0.767, 0.560, 0.691, 0.814, 0.612, 0.730, 0.855, 0.740, 0.582, 0.814, 0.657, 0.764};

	 static int[]
			  easySame =  {2, 0, 	2, 0, 	3, 2, 0, 	3, 2, 0, 	4, 3, 2, 0,		4, 3, 2, 0},
			  easyCount = {3, 3, 	4, 4,		5, 5, 5, 	6, 6, 6, 	7, 7, 7, 7, 	8, 8, 8, 8};


	static String comment;
	static String[]
			  positiveComment = {"Чудненько!", "Восхитительно!", "Да вы растёте на глазах!", "Вам дорога в консерваторию!","Вы воспитываете супер-певцов", "Гергиев отдыхает",
			  "Паганини курит в сторонке", "Браво, маэстро!", "Вы идеальны!", "Да у вас абсолютный слух!"},
			  negativeComment = {"Медвед топтался на ваших ушах?", "Вы играете на наших нервах!", "Не мучайте нас, почистите уши!", "Птичку... Жалко...",
						 "Вы как Бетховен! Тоже глухой..", "Выньте уже бананы из ушей!",  "Чижик-Пыжик, где ты был?"},
			  counts = {"Трио", "Квартет", "Квинтет", "Секстет", "Септет", "Октет", "Нонет"};

	public static int recordHard, recordEasy;
	static boolean newRecord;


	public Level(String levelThread) {
		super("LevelThread");
	}

	@Override
	public void run() {
		// checkMelody();
	}

	public static void newGame() {
		newRecord = false;
		score = 0;
		lifes = 3;
		level = (hardBool) ? 5 : 0;
		if (hardBool)
			Dialogs.nextLevel("Новая сложная игра, секстет птичек", level - 4);
		else
			Dialogs.nextLevel("Новая игра, трио птичек", level + 1);
		nextLevel();
	}

	public static void nextLevel() {
		level++;

		double tmpX, tmpY; // Перемешивание
		for (int i = 0; i < bushPlaceX.length; i++) {
			int r = Level.random.nextInt(bushPlaceX.length);

			tmpX = bushPlaceX[i];
			tmpY = bushPlaceY[i];

			bushPlaceX[i] = bushPlaceX[r];
			bushPlaceY[i] = bushPlaceY[r];

			bushPlaceX[r] = tmpX;
			bushPlaceY[r] = tmpY;
		}

		melodyOrder = generateMelody(level);
		createBirds(melodyOrder);

		for (int i = 0; i < birdPlaced.length; i++) birdPlaced[i] = null;
		time = 0;
		timeBool = false;
		howMushTimesYouCanListenTheMelody = melodyOrder.length * ((hardBool) ? 1 : 2);

		if (level > 1 && !hardBool)
			Dialogs.nextLevel(counts[melodyOrder.length - 3] + " птичек", level);
		else if (hardBool && !(level == 6))
			Dialogs.nextLevel(counts[melodyOrder.length - 3] + " птичек", level - 5);
	}

	public static void createBirds(int[] order) {
		mainView.birds.clear();
		for (int i = 0; i < order.length; i++) {
			mainView.birds.add(new Birds((float) (bushPlaceX[i]) * mainView.Width, (float) (bushPlaceY[i]) * mainView.Height, order[i]));
		}
		Log.i("Game", order.length + " birds have been created and placed on the bush.");
	}

	public static void checkPlace(Birds b) {
		int r = mainView.BIRDS_WIDTH;
		for (int i = 0; i < melodyOrder.length; i++) {
			float X = (float) (wirePlaceX[i] * mainView.Width);
			float Y = (float) (wirePlaceY[i] * mainView.Height);

			if ((b.x - X) * (b.x - X) + (b.y - Y) * (b.y - Y) < r * r) {
				if (birdPlaced[i] == null) {
					birdPlaced[i] = b;
					b.x = X;
					b.y = Y;
				}
			} else if (b == birdPlaced[i]) birdPlaced[i] = null;
		}
	}

	private static int[] generateMelody(int level) {
		int count;
		int same = 0;

		if (level <=  18) {
			same = easySame[level - 1];
			count = easyCount[level - 1];
		} else {
			count = 9;
			if (level <= 23) same = 9 - (level - 18);
		}

		int s = (random.nextInt(9));
		int[] order = new int[count];
		for (int i = 0; i < count; i++) {
			if (i < same) order[i] = s;
			else order[i] = -1;

			while (order[i] == s || order[i] == -1) order[i] = (random.nextInt(9));
		}

		for (int i = 0; i < order.length; i++) {	// Перемешивание
			int r = Level.random.nextInt(order.length);
			int tmp = order[i];
			order[i] = order[r];
			order[r] = tmp;
		}

		Log.i("Game", "Level " + level + ". Generated melody of " + count + " sounds with " + same + " similar. ");

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
						b.sound();
						try {
							Thread.sleep(330);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						b.y += mainView.BIRDS_HEIGHT;
						birdPlaced[i] = null;
						break;
					}
				} else break;
				i++;
			}
			if (c == i && i == melodyOrder.length) {	// Мелодия правильная
				comment = Level.positiveComment[Level.random.nextInt(Level.positiveComment.length)];

				score += level * 100;
				score += (time < melodyOrder.length * 5 && level != 0) ? melodyOrder.length * (melodyOrder.length * 5 - time) : 0;

				com.donutellko.birdcoach.State.state = com.donutellko.birdcoach.States.GAME_LEVEL;
				com.donutellko.birdcoach.State.victory = true;

				Log.i("Game", "Checked " + i + " sounds, melody is right.");
			} else {
				comment = Level.negativeComment[Level.random.nextInt(Level.negativeComment.length)];
				Level.lifes--;
				Res.sounds.play(Res.mistakeSound, 1.0f, 1.0f, 1, 0, 1.0f);
				if (lifes == 0) {
					com.donutellko.birdcoach.State.state = com.donutellko.birdcoach.States.GAME_LEVEL;
					com.donutellko.birdcoach.State.victory = false;
					if (hardBool && recordHard < score) { recordHard = score; newRecord = true; }
					else if (!hardBool && recordEasy < score) { recordEasy = score; newRecord = true; }
				}
				Log.i("Game", "Checked " + i + " sounds, melody is wrong.");
				//mistakeDialog();
			}
		}
	}

	static void playMelody(int[] order) {
		timeBool = true;
		if (howMushTimesYouCanListenTheMelody != 0) {
			howMushTimesYouCanListenTheMelody--;
			for (int i = 0; i < order.length; i++) {
				playBirdSound(order[i]);
				try {
					Thread.sleep(330);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	static void playBirdSound(int type) {
		//float speed = (float) (0.5 + (EndlessView.BIRDS_BITMAP_COLUMNS - type) * (1.5 / EndlessView.BIRDS_BITMAP_COLUMNS));
		int sound = Res.birdSounds[mainView.BIRDS_BITMAP_COLUMNS - type - 1];
		Res.sounds.play(sound, 1.0f, 1.0f, type, 0, 1.0f);
	}


	public static String scoresText() {
		String s = "";
		if (newRecord && ((hardBool) ? recordEasy : recordHard) != 0) s += "Новый рекорд! ";
		s += score + ((score % 10 < 4 && score != 0 && (score < 5 || score > 20)) ? " очка!" : " очков!");
		if (hardBool && recordHard != 0) s += "Рекорд - " + recordHard + ".";
		if (!hardBool && recordEasy != 0) s += "Рекорд - " + recordEasy + ".";

		return s;
	}

	public static String timeText() {
		String s = Level.time / 60 + ":";
		s += (Level.time % 60 < 10) ? "0" + Level.time % 60 : Level.time % 60;
		return s;
	}
}
package com.donutellko.birdcoach;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import java.util.Random;

/**
 * level:   1   2   3   4   5   6   7   8   9   10  11  12
 * N:       4   4:1 5   5:1 6   6:2 7   7:2 8   8:3 9   9:3
 */


public class Level extends Thread {
	static int level = 0, score = 0, time = 0, lifes = 3, howMushTimesYouCanListenTheMelody;
	static int levels[] = {3, 4, 4, 5, 5, 6, 6, 6, 7, 7, 8, 8, 9};

	static boolean timeBool = false, hardBool = false;

	static int[] melodyOrder = new int[1];
	static Birds[] birdPlaced = new Birds[9];

	static Random random = new Random();

	static double[] wirePlaceX = {0.014, 0.104, 0.200, 0.296, 0.394, 0.490, 0.579, 0.676, 0.779};
	static double[] wirePlaceY = {0.177, 0.192, 0.206, 0.209, 0.205, 0.200, 0.193, 0.172, 0.156};

	static double[] bushPlaceX = {0.134, 0.176, 0.196, 0.215, 0.281, 0.287, 0.329, 0.380, 0.396, 0.435, 0.472, 0.540};
	static double[] bushPlaceY = {0.767, 0.560, 0.691, 0.814, 0.612, 0.730, 0.855, 0.740, 0.582, 0.814, 0.657, 0.764};

	static String comment;
	static String[]
			  positiveComment = {"Чудненько!", "Восхитительно!", "Да вы растёте на глазах!", "Вам дорога в консерваторию!","Вы воспитываете супер-певцов", "Гергиев отдыхает",
			  "Паганини курит в сторонке", "Браво, маэстро!", "Вы идеальны!", "Да у вас абсолютный слух!"},
			  negativeComment = {"Медвед топтался на ваших ушах?", "Вы играете на наших нервах!", "Не мучайте нас, почистите уши!", "Птичку... Жалко...",
						 "Вы как Бетховен! Тоже глухой..", "Выньте уже бананы из ушей!",  "Чижик-Пыжик, где ты был?"},
			  counts = {"Трио", "Квартет", "Квинтет", "Секстет", "Септет", "Октет", "Нонет"};

	public static int recordHard, recordEasy;
	static boolean newRecord;

	/*
	 "Вы зачислены в консерваторию.",
	  "Сольфеджио часто прогуливали?",
	 "Лучший наставник для наших птичек",
	 Медвед основательно потоптался на ваших ушах
	 "Ах какая невезуха - абсолютно\n нету слуха",
	 "Попробуйте ещё раз!\nИ выньте, наконец, бананы из ушей!",
	 "Гринпис против издевательства над животными!",
	 "Не мучайте птичек, лучше почистите уши",
	 "Не всем дано отличить до диез от ля мажор",
	  */


	public Level(MainView mainView, String levelThread) {
		super("LevelThread");
	}

	@Override
	public void run() {
		checkMelody();
	}

	public static void newGame() {
		newRecord = false;
		score = 0;
		lifes = 3;
		level = (hardBool) ? 5 : 0;
		if (hardBool)
			nextLevelDialog("Новая сложная игра, секстет птичек", level - 4);
		else
			nextLevelDialog("Новая игра, трио птичек", level + 1);
		nextLevel();
	}

	public static void nextLevel() {
		level++;
		int x = 9;
		if (levels[level - 1] < 9) x = levels[level - 1];

		mixPositions();

		melodyOrder = generateMelody(x);
		createBirds(melodyOrder);

		for (int i = 0; i < birdPlaced.length; i++) birdPlaced[i] = null;
		time = 0;
		timeBool = false;
		howMushTimesYouCanListenTheMelody = melodyOrder.length;

		if (level > 1 && !hardBool)
			nextLevelDialog(counts[melodyOrder.length - 3] + " птичек", level);
		else if (hardBool && !(level == 6))
			nextLevelDialog(counts[melodyOrder.length - 3] + " птичек", level - 5);
	}

	private static void mixPositions() {
		double tmpX, tmpY;
		for (int i = 0; i < bushPlaceX.length; i++) {
			int r = Level.random.nextInt(bushPlaceX.length);

			tmpX = bushPlaceX[i];
			tmpY = bushPlaceY[i];

			bushPlaceX[i] = bushPlaceX[r];
			bushPlaceY[i] = bushPlaceY[r];

			bushPlaceX[r] = tmpX;
			bushPlaceY[r] = tmpY;
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
					} else {
						b.y += MainView.BIRDS_HEIGHT;
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
			} else {												// Ошибка пщщщщ БУМ!
				comment = Level.negativeComment[Level.random.nextInt(Level.negativeComment.length)];
				Level.lifes--;
				mResources.sounds.play(mResources.mistakeSound, 1.0f, 1.0f, 1, 0, 1.0f);
				if (lifes == 0) {
					com.donutellko.birdcoach.State.state = com.donutellko.birdcoach.States.GAME_LEVEL;
					com.donutellko.birdcoach.State.victory = false;
					if (hardBool && recordHard < score) { recordHard = score; newRecord = true; }
					else if (!hardBool && recordEasy < score) { recordEasy = score; newRecord = true; }
				}
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
		int sound = mResources.birdSounds[MainView.BIRDS_BITMAP_COLUMNS - type - 1];
		mResources.sounds.play(sound, 1.0f, 1.0f, type, 0, 1.0f);
	}

	static void nextLevelDialog(String title, int level) {
		AlertDialog.Builder levelDialog = new AlertDialog.Builder(MainView.context);

		timeBool = false;
		String s = "";

		if (level > 1 && !(level == 5 && hardBool))  {
			s = "\nУ вас уже " + score;
			s += ((Level.score % 10 < 4 && Level.score != 0 && (Level.score < 5 || Level.score > 20)) ? " очка" : " очков")  +
					  ((hardBool) ?
								 ((recordHard < score) ? " Предыдущий рекорд на большой сложности - " + recordHard : "") :
								 (recordHard < score) ? " Предыдущий рекорд на низкой сложности - " + recordEasy : "" );
		}

		levelDialog.setTitle(title)
				  .setMessage("Уровень " + level + "\n" + s)
				  .setIcon(R.mipmap.ic_launcher)
				  .setCancelable(false)
				  .setNegativeButton("Начать",
							 new DialogInterface.OnClickListener() {
								 public void onClick(DialogInterface dialog, int id) {
									 // Level.playMelody(Level.melodyOrder);
									 dialog.cancel();
								 }
							 });
		AlertDialog alert = levelDialog.create();
		alert.show();
	}

	static void mistakeDialog() {
		AlertDialog.Builder mistakeDialog = new AlertDialog.Builder(MainView.context);

		mistakeDialog.setTitle("Упс!")
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
		mistakeDialog.show();
	}

	static void rulesDialog() {
		AlertDialog.Builder rulesDialog = new AlertDialog.Builder(MainView.context);

		rulesDialog.setTitle("Правила")
				  .setMessage("Правила игры: \n" +
							 "•   Ваша задача — рассадить птичек на проводе в соответствии с мелодией, которую можно услышать, нажав на мегафон в правом нижнем углу экрана. По мере игры длина мелодии увеличивается. \n" +
							 "•   У вас есть три жизни на одну игру (право на ошибку). Оставшиеся жизни отображаются в верхнем углу экрана. \n" +
							 "•   На каждом уровне мелодию можно прослушать столько раз, сколько птичек нужно рассадить. \n" +
							 "•   За каждый уровень вы набираете очки (верхний правый угол), зависящие от количества прослушиваний и времени, затраченного на этот уровень (верхний левый угол).\n" +
							 "•   В игре есть несколько уровней сложности, он изменяется в настройках. При уровне Новичок вы начинаете играть с трёх птичек, а при уровне Маэстро - с шести.")
				  .setIcon(R.mipmap.ic_launcher)
				  .setCancelable(true)
				  .setPositiveButton("Усвоил",
							 new DialogInterface.OnClickListener() {
								 public void onClick(DialogInterface dialog, int id) {
									 dialog.cancel();
								 }
							 });
		AlertDialog alert = rulesDialog.create();
		alert.show();
	}

	static void settingsDialog() {
		AlertDialog.Builder settingsDialog = new AlertDialog.Builder(MainView.context);

		String settings[] = new String[3];
		settings[0] = (MainView.musicBool) ? "Выключить музыку" : "Включить музыку";
		settings[1] = (hardBool) ? "Текущая сложность: Маэстро" : "Текущая сложность: Новичок";
		settings[2] = "Закрыть";

		settingsDialog.
				  setTitle("Настройки")
				  .setIcon(R.mipmap.ic_launcher)
				  .setCancelable(true)
				  .setItems(settings, new DialogInterface.OnClickListener() {
					  @Override
					  public void onClick(DialogInterface dialog, int which) {
						  if (which == 0) MainView.musicBool = !MainView.musicBool;
						  else if (which == 1) hardBool = !hardBool;
						  else dialog.cancel();
					  }
				  });
		AlertDialog alert = settingsDialog.create();
		alert.show();
	}

}
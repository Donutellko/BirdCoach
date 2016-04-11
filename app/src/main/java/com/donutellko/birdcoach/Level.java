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
	static int levels[] = {3, 4, 4, 5, 5, 6, 6, 6, 7, 7, 8, 8, 9};

	static boolean timeBool = false;
	static AlertDialog.Builder levelDialog = new AlertDialog.Builder(MainView.context);
	static AlertDialog.Builder settingsDialog = new AlertDialog.Builder(MainView.context);
	static AlertDialog.Builder rulesDialog = new AlertDialog.Builder(MainView.context);

	static int[] melodyOrder = new int[1];
	static Birds[] birdPlaced = new Birds[9];

	static Random random = new Random();

	static double[] wirePlaceX = {0.104, 0.200, 0.296, 0.394, 0.490, 0.579, 0.676, 0.779};
	static double[] wirePlaceY = {0.177, 0.192, 0.206, 0.209, 0.205, 0.200, 0.193, 0.172, 0.156};

	static double[] bushPlaceX = {0.134, 0.176, 0.196, 0.215, 0.281, 0.287, 0.329, 0.380, 0.396, 0.435, 0.472, 0.540};
	static double[] bushPlaceY = {0.767, 0.560, 0.691, 0.814, 0.612, 0.730, 0.855, 0.740, 0.582, 0.814, 0.657, 0.764};

	static String comment;
	static String[]
			  positiveComment = {"Чудненько!", "Восхитительно!", "Да вы растёте на глазах!", "Вы воспитываете супер-певцов", "Гергиев отдыхает", "Паганини курит в сторонке", "Браво, маэстро",
			  "Вы идеальны", "Вы зачислены в Консерваторию", "Да у вас абсолютный слух!"},
			  negativeComment = {"Медвед топтался на ваших ушах?", "Вы играете на наших нервах", "Не мучайте птичек, почистите уши!", "Птичку... Жалко...",
						 "Да вы просто Бетховен... Тоже глухой", "Выньте, наконец, бананы из ушей", "Сольфеджио часто прогуливали?", "Чижик-Пыжик, где ты был?"},
			  counts = {"Трио", "Квартет", "Квинтет", "Секстет", "Септет", "Октет", "Нонет"};

	/*
	 Поздр
	 авляю, вы зачислены в Консерват
	 орию!
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
		score = 0;
		lifes = 3;
		level = 0;
		nextLevelDialog("Новая игра", levels[0], level + 1);
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

		if (level > 1) {
			nextLevelDialog(counts[melodyOrder.length - 3] + " птичек.", melodyOrder.length, level);
		}
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
					} else break;
				} else break;
				i++;
			}
			if (c == i && i == melodyOrder.length) {
				comment = Level.positiveComment[Level.random.nextInt(Level.positiveComment.length)];

				score += level * 100;
				score += (time < melodyOrder.length * 5 && level != 0) ? melodyOrder.length * (melodyOrder.length * 5 - time) : 0;

				MainView.state = "Game+Level";
				MainView.stateL = "Victory";
			} else {
				comment = Level.negativeComment[Level.random.nextInt(Level.negativeComment.length)];
				Level.lifes--;
				mResources.sounds.play(mResources.mistakeSound, 1.0f, 1.0f, 1, 0, 1.0f);
				if (lifes == 0) {
					MainView.state = "Game+Level";
					MainView.stateL = "Loose";
				}
				mistakeDialog();
			}
		}
	}

	public static void playMelody(int[] order) {
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

	public static void playBirdSound(int type) {
		//float speed = (float) (0.5 + (EndlessView.BIRDS_BITMAP_COLUMNS - type) * (1.5 / EndlessView.BIRDS_BITMAP_COLUMNS));
		int sound = mResources.birdSounds[MainView.BIRDS_BITMAP_COLUMNS - type - 1];
		mResources.sounds.play(sound, 1.0f, 1.0f, type, 0, 1.0f);
	}

	public static void nextLevelDialog(String title, int count, int level) {
		timeBool = false;
		String m = "";
		m += (count <= 4) ? " птички." : " птичек";
		String s = "";
		if (level > 1) s = "\nУ вас уже " + score + " очков!";

		levelDialog.setTitle(title)
				  .setMessage("Уровень " + level + "\n" + count + m + s)
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

	private static void mistakeDialog() {
		levelDialog.setTitle("Упс!")
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

	public static void rulesDialog() {
		rulesDialog.setTitle("Правила")
				  .setMessage("Правила игры: \n" +
							 "• Ваша задача — рассадить птичек на проводе в соответствии с мелодией, которую можно услышать, нажав на мегафон в правом нижнем углу экрана. С каждым последующим уровнем длина мелодии увеличивается. \n" +
							 "• У вас есть три жизни на одну игру (право на ошибку). Оставшиеся жизни отображаются в верхнем углу экрана. \n" +
							 "• На каждом уровне мелодию можно прослушать столько раз, сколько птичек нужно рассадить. \n" +
							 "• За каждый уровень вы набираете очки (верхний правый угол), зависящие от количества прослушиваний и времени, затраченного на этот уровень (верхний левый угол).")
				  .setIcon(R.mipmap.ic_launcher)
				  .setCancelable(true)
				  .setPositiveButton("Усвоил",
							 new DialogInterface.OnClickListener() {
								 public void onClick(DialogInterface dialog, int id) {
									 dialog.cancel();
								 }
							 });
		AlertDialog alert = Level.rulesDialog.create();
		alert.show();
	}

	public static void settingsDialog() {
		settingsDialog.setTitle("Настройки")
				  .setMessage("Включить музыку в меню?")
				  .setIcon(R.mipmap.ic_launcher)
				  .setCancelable(true)
				  .setNegativeButton("Нет",
							 new DialogInterface.OnClickListener() {
								 public void onClick(DialogInterface dialog, int id) {
									 if (MainView.mediaPlayer.isPlaying())
										 MainView.mediaPlayer.pause();
									 MainView.musicBool = false;
									 dialog.cancel();
								 }
							 })
				  .setPositiveButton("Да",
							 new DialogInterface.OnClickListener() {
								 public void onClick(DialogInterface dialog, int id) {
									 if (!MainView.mediaPlayer.isPlaying())
										 MainView.mediaPlayer.start();
									 MainView.musicBool = true;
									 dialog.cancel();
								 }
							 });
		AlertDialog alert = Level.settingsDialog.create();
		alert.show();
	}
}
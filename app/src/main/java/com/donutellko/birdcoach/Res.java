package com.donutellko.birdcoach;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import static android.graphics.Bitmap.createBitmap;

/**
 * Created by donat on 4/25/16.
 */
public class Res extends Thread {

	static int H, W;
	static Context context;
	public static SoundPool sounds;
	public static int[] birdSounds = new int[mainView.BIRDS_BITMAP_COLUMNS];
	public static int mistakeSound;
	static Paint paint, textComment = new Paint(), textScores = new Paint(), textTime = new Paint(), textInfo = new Paint();
	static boolean loadedMain = false, resizedMain = false, loadedAnother, resizedAnother;
	static Bitmap
			  backSky, backHills, backWeed, wire,
			  forwardMain, forwardMenu,forwardVictory, forwardLose, forwardGame,
			  allBirds, lifesBitmap, fantom, note, megaphone,
			  birdBitmaps[][] = new Bitmap[mainView.BIRDS_BITMAP_COLUMNS][mainView.BIRDS_BITMAP_STRINGS];

	static Bitmap[]
			  animMain = new Bitmap[4],
			  animMenu = new Bitmap[3],
			  animVictory = new Bitmap[2];
			  // animLose = new Bitmap[2];

	public static MediaPlayer player;

	Res() {
		super("ResThread");
	}

	@Override
	public void run() {
		loadNoSize();
	}

	public static void loadNoSize() {
		if (context == null) context = mainView.context;

		backSky = BitmapFactory.decodeResource(context.getResources(), R.drawable.back_sky);
		backHills = BitmapFactory.decodeResource(context.getResources(), R.drawable.back_hills);
		backWeed = BitmapFactory.decodeResource(context.getResources(), R.drawable.back_weed);
		wire = BitmapFactory.decodeResource(context.getResources(), R.drawable.wire);
		forwardMain = BitmapFactory.decodeResource(context.getResources(), R.drawable.forward_main);

		player = MediaPlayer.create(context, R.raw.hopper);
		player.setLooping(true);
		if (Level.musicBool) player.start();

		animMain[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.main_blue);
		animMain[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.main_dark);
		animMain[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.main_red);
		animMain[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.main_purple);

		loadedMain = true;

		if (mainView.Height != 0) changeSizeMain();

	}

	public static void changeSizeMain() {
		H = mainView.Height;
		W = mainView.Width;

		backSky = resize(backSky, W , H /* W * 675 / 1920 */);
		backHills = resize(backHills, W, W * 480 / 1920);
		backWeed = resize(backWeed, W, W * 295 / 1920);
		wire = resize(wire, W, H);
		forwardMain = resize(forwardMain, W, H);

		animMain[0] = resize(animMain[0], W / 4, H / 2);
		animMain[1] = resize(animMain[1], W / 4, H / 2);
		animMain[2] = resize(animMain[2], W / 4, H);
		animMain[3] = resize(animMain[3], W / 4, H / 2);

		resizedMain = true;
	}

	public static void loadAnother() {
		H = mainView.Height;
		W = mainView.Width;

		int bW = mainView.BIRDS_WIDTH, bH = mainView.BIRDS_HEIGHT;


		forwardMenu = BitmapFactory.decodeResource(context.getResources(), R.drawable.forward_menu);
		animMenu[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu_green);
		animMenu[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu_purple1);
		animMenu[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu_purple2);
		forwardMenu = resize(forwardMenu, W, H);
		animMenu[0] = resize(animMenu[0], W / 4, H / 2);
		animMenu[1] = resize(animMenu[1], W / 4, H / 2);
		animMenu[2] = resize(animMenu[2], W / 4, H / 2);

		forwardGame = BitmapFactory.decodeResource(context.getResources(), R.drawable.bush);
		forwardGame = resize(forwardGame, W, H);

		forwardVictory = BitmapFactory.decodeResource(context.getResources(), R.drawable.forward_victory);
		animVictory[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.scr3_orange1);
		animVictory[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.scr3_orange2);
		forwardVictory = resize(forwardVictory, W, H);
		animVictory[0] = resize(animVictory[0], W / 4, H / 2);
		animVictory[1] = resize(animVictory[1], W / 4, H / 2);

		forwardLose = BitmapFactory.decodeResource(context.getResources(), R.drawable.forward_lose);
		// animLose[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.scr3_orange1);
		// animLose[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.scr3_orange2);
		forwardLose = resize(forwardLose, W, H);
		// animLose[0] = resize(animLose[0], W / 4, H / 2);
		// animLose[1] = resize(animLose[1], W / 4, H / 2);

		allBirds = BitmapFactory.decodeResource(context.getResources(), R.drawable.all_birds);
		allBirds = resize(allBirds, bW * 9, bH * 6);

		for (int type = 0; type < mainView.BIRDS_BITMAP_COLUMNS; type++)
			for (int pose = 0; pose < mainView.BIRDS_BITMAP_STRINGS; pose++)
				birdBitmaps[type][pose] = createBitmap(allBirds, bW * type, bH * pose, bW, bH);

		lifesBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
		megaphone = BitmapFactory.decodeResource(context.getResources(), R.drawable.megafon);
		fantom = BitmapFactory.decodeResource(context.getResources(), R.drawable.fantom);
		note = BitmapFactory.decodeResource(context.getResources(), R.drawable.note);

		lifesBitmap = resize(lifesBitmap, bH / 2, bH / 2);
		megaphone = resize(megaphone, bH * 3 * 650 / 750, bH * 3);
		fantom = resize(fantom, bW, bH);
		note = resize(note, bH * 2 / 3, bH * 2 / 3);

		loadSounds();

		resizedAnother = true;
	}

	private static Bitmap resize(Bitmap bitmap, int w, int h) {
		return Bitmap.createScaledBitmap(bitmap, w, h, false);
	}

	public static void loadText() {
		// Typeface type = Typeface.createFromAsset(MainView.context.getAssets(), "thickhead.ttf");
		Typeface type = Typeface.createFromAsset(mainView.context.getAssets(), "comic.ttf");

		textComment.setColor(0xFFFFFFFF);
		textComment.setTextSize(mainView.Height / 22);
		textComment.setTextAlign(Paint.Align.CENTER);
		textComment.setTypeface(type);

		textInfo.setColor(0xFFFFFFFF);
		textInfo.setTextSize(mainView.Height / 22);
		textInfo.setTextAlign(Paint.Align.LEFT);
		textInfo.setTypeface(type);

		// textTime.setColor(0xffff0000); - теперь в level.timeText();
		textTime.setTextSize(mainView.Height / 24);
		textTime.setTextAlign(Paint.Align.LEFT);
		textTime.setTypeface(type);

		textScores.setColor(0xFFFF5511);
		textScores.setTextSize(mainView.Height / 24);
		textScores.setTextAlign(Paint.Align.LEFT);
		textScores.setTypeface(type);
	}

	public static void loadSounds() {
		sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		mistakeSound = sounds.load(context, R.raw.mistake, 1);

		birdSounds[0] = sounds.load(context, R.raw.s1c, 1);
		birdSounds[1] = sounds.load(context, R.raw.s1d, 1);
		birdSounds[2] = sounds.load(context, R.raw.s1e, 1);
		birdSounds[3] = sounds.load(context, R.raw.s1f, 1);
		birdSounds[4] = sounds.load(context, R.raw.s1g, 1);

		birdSounds[5] = sounds.load(context, R.raw.s2a, 1);
		birdSounds[6] = sounds.load(context, R.raw.s2b, 1);
		birdSounds[7] = sounds.load(context, R.raw.s2c, 1);
		birdSounds[8] = sounds.load(context, R.raw.s2d, 1);
	}
}

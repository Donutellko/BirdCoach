package com.donutellko.birdcoach;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;

import static android.graphics.Bitmap.createBitmap;

public class mResources extends Thread {
	static Context context;

	static Paint paint = new Paint();
	static Paint textComment = new Paint(), textScores = new Paint(), textTime = new Paint(), textInfo = new Paint();

	public static SoundPool sounds;
	public static int[] birdSounds = new int[MainView.BIRDS_BITMAP_COLUMNS];
	public static int mistakeSound;
	static Bitmap
			  back, clouds, wire, josh, scr1, scr2, lalka, victory,
			  lifesBitmaps, birdsBitmap, mfBitmap, fantom, note,
			  scr1_blue, scr1_dark, scr1_purple, scr1_red,
			  scr2_purple1, scr2_purple2,
			  scr3_orange1, scr3_orange2;
	static Bitmap[][] birdBitmaps = new Bitmap[MainView.BIRDS_BITMAP_COLUMNS][MainView.BIRDS_BITMAP_STRINGS];

	mResources() {
		super("ResThread");
	}

	public static void loadResources() {
		if (context == null) context = MainView.context;

		loadSounds();
		if (MainView.Width > 0 && MainView.Height > 0)
			loadBitmaps();
		loadText();
		loadBirds();
	}


	private static void loadText() {
		// Typeface type = Typeface.createFromAsset(MainView.context.getAssets(), "thickhead.ttf");
		Typeface type = Typeface.createFromAsset(MainView.context.getAssets(), "comic.ttf");

		textComment.setColor(0xFFFFFFFF);
		textComment.setTextSize(MainView.Height / 22);
		textComment.setTextAlign(Paint.Align.CENTER);
		textComment.setTypeface(type);

		textInfo.setColor(0xFFFFFFFF);
		textInfo.setTextSize(MainView.Height / 22);
		textInfo.setTextAlign(Paint.Align.LEFT);
		textInfo.setTypeface(type);

		textTime.setColor(0xffff0000);
		textTime.setTextSize(MainView.Height / 24);
		textTime.setTextAlign(Paint.Align.LEFT);
		textTime.setTypeface(type);

		textScores.setColor(0xffff0000);
		textScores.setTextSize(MainView.Height / 24);
		textScores.setTextAlign(Paint.Align.LEFT);
		textScores.setTypeface(type);
	}

	public static void loadBitmaps() {
		if (context == null) context = MainView.context;

		if (back == null)
			back = loadRes(R.drawable.back, 1, 1);
		if (josh == null)
			josh = loadRes(R.drawable.josh, 1, 1);
		if (wire == null)
			wire = loadRes(R.drawable.wire, 1, 1);
		if (clouds == null)
			clouds = loadRes(R.drawable.clouds, 1, 1);
		if (scr1 == null)
			scr1 = loadRes(R.drawable.scr1, 1, 1);
		if (scr2 == null)
			scr2 = loadRes(R.drawable.scr2, 1, 1);

		if (lifesBitmaps == null)
			lifesBitmaps = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher), MainView.BIRDS_HEIGHT / 2, MainView.BIRDS_HEIGHT / 2, false);
		if (mfBitmap == null)
			mfBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.megafon), MainView.BIRDS_HEIGHT * 2 * 650 / 750, MainView.BIRDS_HEIGHT * 2, false);
		if (fantom == null)
			fantom = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fantom), MainView.BIRDS_HEIGHT, MainView.BIRDS_WIDTH, false);
		if (note == null)
			note = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.note), MainView.BIRDS_HEIGHT * 2 / 3, MainView.BIRDS_HEIGHT * 2 / 3, false);


		if (lalka == null)
			lalka = loadRes(R.drawable.lalka, 1, 1);
		if (victory == null)
			victory = loadRes(R.drawable.victory, 1, 1);


		if (scr1_blue == null)
			scr1_blue = loadRes(R.drawable.scr1_blue, 0.25f, 0.5f);
		if (scr1_dark == null)
			scr1_dark = loadRes(R.drawable.scr1_dark, 0.25f, 0.5f);
		if (scr1_purple == null)
			scr1_purple = loadRes(R.drawable.scr1_purple, 0.25f, 0.5f);
		if (scr1_red == null)
			scr1_red = loadRes(R.drawable.scr1_red, 0.25f, 1);


		if (scr2_purple1 == null)
			scr2_purple1 = loadRes(R.drawable.scr2_purple1, 0.25f, 0.5f);
		if (scr2_purple2 == null)
			scr2_purple2 = loadRes(R.drawable.scr2_purple2, 0.5f, 0.5f);
		if (scr3_orange1 == null)
			scr3_orange1 = loadRes(R.drawable.scr3_orange1, 0.25f, 0.5f);
		if (scr3_orange2 == null)
			scr3_orange2 = loadRes(R.drawable.scr3_orange2, 0.25f, 0.5f);

	}

	private static Bitmap loadRes(int resource, float multWidth, float multHeight) {
		return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), resource), (int) (MainView.Width * multWidth), (int) (MainView.Height * multHeight), false);
	}

	public static void loadBirds() {
		for (int type = 0; type < MainView.BIRDS_BITMAP_COLUMNS; type++) {
			for (int pose = 0; pose < MainView.BIRDS_BITMAP_STRINGS; pose++) {
				if (birdBitmaps[type][pose] == null) {
					int w = MainView.Width;
					int h = MainView.Width / 2;
					if (birdsBitmap == null)
						birdsBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.birds), w, h, false);
					// birdBitmaps[type][pose] = Bitmap.createScaledBitmap(createBitmap(birdsBitmap, (5 + type * 673 < MainView.Width) ? (5 + type * 673) : (5 + type * 336), pose * 494 + 5, 670, 494), MainView.BIRDS_HEIGHT, MainView.BIRDS_WIDTH, false);
					birdBitmaps[type][pose] = Bitmap.createScaledBitmap(createBitmap(birdsBitmap, w/9 * type, h/6 * pose, w / 9, h / 6), MainView.BIRDS_HEIGHT, MainView.BIRDS_WIDTH, false);
				}
			}
		}
		birdsBitmap = null;
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
		birdSounds[8] = sounds.load(context, R.raw.s2e, 1);
	}

	@Override
	public void run() {
		loadResources();
	}
}

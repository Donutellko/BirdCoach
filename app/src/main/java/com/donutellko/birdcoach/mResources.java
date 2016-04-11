package com.donutellko.birdcoach;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
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
			  back, clouds, wire, josh, scr1, scr1_2, scr2, lalka, victory,
			  lifesBitmaps, birdsBitmap, mfBitmap, fantom, note;
	static Bitmap[][] birdBitmaps = new Bitmap[MainView.BIRDS_BITMAP_COLUMNS][MainView.BIRDS_BITMAP_STRINGS];

	mResources () {
		super("ResThread");
	}

	public static void loadResources() {
		if (context == null) context = MainView.context;

		loadBackgrounds();
		loadText();
		loadBirds();
		loadBitmaps();
		loadSounds();
	}

	private static void loadBitmaps() {
		if (lifesBitmaps == null)
			lifesBitmaps = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher), MainView.BIRDS_HEIGHT / 2, MainView.BIRDS_HEIGHT / 2, false);
		if (mfBitmap == null)
			mfBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.megafon), MainView.BIRDS_HEIGHT * 2 * 650 / 750, MainView.BIRDS_HEIGHT * 2, false);
		if (fantom == null)
			fantom = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fantom), MainView.BIRDS_HEIGHT, MainView.BIRDS_WIDTH, false);
		if (note == null)
			note = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.note), MainView.BIRDS_HEIGHT * 2 / 3, MainView.BIRDS_HEIGHT * 2 / 3, false);
	}

	private static void loadText() {
		textComment.setColor(0xFFFFFFFF);
		textComment.setTextSize(MainView.Height / 22);
		textComment.setTextAlign(Paint.Align.CENTER);

		textInfo.setColor(0xFFFFFFFF);
		textInfo.setTextSize(MainView.Height / 22);
		textInfo.setTextAlign(Paint.Align.LEFT);

		textTime.setColor(0xffff0000);
		textTime.setTextSize(MainView.Height / 24);
		textTime.setTextAlign(Paint.Align.LEFT);

		textScores.setColor(0xffff0000);
		textScores.setTextSize(MainView.Height / 24);
		textScores.setTextAlign(Paint.Align.LEFT);
	}

	public static void loadBackgrounds() {
		if (context == null) context = MainView.context;

		if (back == null)
			back = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.back), MainView.Width, MainView.Height, false);
		if (josh == null)
			josh = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.josh), MainView.Width, MainView.Height, false);
		if (wire == null)
			wire = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.wire), MainView.Width, MainView.Height, false);
		if (clouds == null)
			clouds = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.clouds), MainView.Width, MainView.Height, false);
		if (scr1 == null)
			scr1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.scr1), MainView.Width, MainView.Height, false);
		if (scr1_2 == null)
			scr1_2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.scr1_2), MainView.Width, MainView.Height, false);
		if (scr2 == null)
			scr2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.scr2), MainView.Width, MainView.Height, false);

		if (lalka == null)
			lalka = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.lalka), MainView.Width, MainView.Height, false);
		if (victory == null)
			victory = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.victory), MainView.Width, MainView.Height, false);

	}

	public static void loadBirds() {
		if (birdsBitmap == null)
			birdsBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.birds);
		for (int type = 0; type < MainView.BIRDS_BITMAP_COLUMNS; type++) {
			for (int pose = 0; pose < MainView.BIRDS_BITMAP_STRINGS; pose++) {
				if (birdBitmaps[type][pose] == null)
					birdBitmaps[type][pose] = Bitmap.createScaledBitmap(createBitmap(birdsBitmap, 5 + type * 673, pose * 494 + 5, 670, 494), MainView.BIRDS_HEIGHT, MainView.BIRDS_WIDTH, false);
			}
		}
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

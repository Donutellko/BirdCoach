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
	static Paint textScores = new Paint();
	static Paint textTime = new Paint();

	static AlertDialog.Builder builder = new AlertDialog.Builder(MainView.context);
	public static SoundPool sounds;
	public static int[] birdSounds = new int[EndlessView.BIRDS_BITMAP_COLUMNS];
	static Bitmap
			  bg, clouds, wire, tree, josh,
			  lifesBitmaps, birdsBitmap;
	static Bitmap[][] birdBitmaps = new Bitmap[EndlessView.BIRDS_BITMAP_COLUMNS][EndlessView.BIRDS_BITMAP_STRINGS];

	public static void loadResources() {
		if (context == null) context = MainView.context;

		loadBackgrounds();
		loadText();
		loadBirds();
		loadBitmaps();
		loadSounds();
		loadDialogs();
	}

	private static void loadBitmaps() {
		if (lifesBitmaps == null)
			lifesBitmaps = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher), EndlessView.BIRDS_HEIGHT / 2, EndlessView.BIRDS_HEIGHT / 2, false);
	}

	private static void loadDialogs() {
		builder.setTitle("Новая игра")
				  .setMessage("Первый уровень, 5 птиц.")
				  .setIcon(R.mipmap.ic_launcher)
				  .setCancelable(false)
				  .setNegativeButton("Начать",
							 new DialogInterface.OnClickListener() {
								 public void onClick(DialogInterface dialog, int id) {
									 dialog.cancel();
								 }
							 });
	}

	private static void loadText() {
		textTime.setColor(0xffff0000);
		textTime.setTextSize(MainView.Height / 24);
		textTime.setTextAlign(Paint.Align.LEFT);

		textScores.setColor(0xffff0000);
		textScores.setTextSize(MainView.Height / 24);
		textScores.setTextAlign(Paint.Align.LEFT);
	}

	public static void loadBackgrounds() {
		if (context == null) context = MainView.context;

		if (bg == null)
			bg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bg), MainView.Width, MainView.Height, false);
		if (josh == null)
			josh = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.josh), MainView.Height * 137 / 108, MainView.Height, false);
		if (tree == null)
			tree = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.tree), MainView.Width, MainView.Width * 11 / 16, false);
		if (wire == null)
			wire = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.wire), MainView.Width, MainView.Height, false);
		if (clouds == null)
			clouds = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.clouds), MainView.Width * 2, MainView.Width * 7 / 40, false);
	}

	public static void loadBirds() {
		if (birdsBitmap == null)
			birdsBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.birds);
		for (int type = 0; type < EndlessView.BIRDS_BITMAP_COLUMNS; type++) {
			for (int pose = 0; pose < EndlessView.BIRDS_BITMAP_STRINGS; pose++) {
				if (birdBitmaps[type][pose] == null)
					birdBitmaps[type][pose] = Bitmap.createScaledBitmap(createBitmap(birdsBitmap,
										 5 + type * 673, pose * 494 + 5,                                     // X, Y on birdsBitmap
										 670, 494),                                                          // Height, Width from birdsBitmap
							  EndlessView.BIRDS_HEIGHT, EndlessView.BIRDS_WIDTH, false);          // Height, Width for result
			}
		}
	}

	public static void loadSounds() {
		sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
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

	@Override
	public void run() {
		loadResources();
	}
}

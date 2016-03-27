package com.donutellko.birdcoach;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class Birds {
	private static final Paint paint = new Paint();
	float x, y;
	int type;   // Тип птицы
	int pose;
	int poseTimer = 100;
	Random random = new Random();

	public Birds(float x, float y, int type) {
		super();
		this.x = x;         // Координата по горизонтали
		this.y = y;         // Координата по вертикали
		this.type = type;   // Цвет и издаваемый звук

	}

	public void drawBird(Canvas canvas) {

		if (poseTimer != 0)
			poseTimer--;
		else {
			pose = random.nextInt(3);
			if (pose == 0) poseTimer = 50;
			else poseTimer = 10;
		}
		if (mResources.birdBitmaps[type][pose] == null)
			mResources.loadBirds();

		canvas.drawBitmap(mResources.birdBitmaps[type][pose], x, y, paint);
	}

	public void sound(Context c) {
		Level.playBirdSound(type);
	}

	public void moveTo(float x, float y) {

	}
}
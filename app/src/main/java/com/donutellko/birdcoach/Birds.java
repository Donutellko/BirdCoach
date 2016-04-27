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
	boolean touched = false;
	Random random = new Random();

	public Birds(float x, float y, int type) {
		super();
		this.x = x;         // Координата по горизонтали
		this.y = y;         // Координата по вертикали
		this.type = type;   // Цвет и издаваемый звук

	}

	public void drawBird(Canvas canvas) {
		float xw = 0;

		if (touched) {
			pose = 3;
		} else {
			if (poseTimer != 0)
				poseTimer--;
			else {
				pose = random.nextInt(3);
				if (pose == 0) poseTimer = 50;
				else poseTimer = 10;
			}
			//if (Res.birdBitmaps[type][pose] == null) Res.loadBirds();



			if (State.state == States.GAME) xw = 0;
			else if (State.MovingTo() == States.GAME) xw = mainView.forwardX + mainView.Width;
			else if (State.MovingFrom() == States.GAME) xw = mainView.forwardX;
		}

		canvas.drawBitmap(Res.birdBitmaps[type][pose], xw + x, y, paint);
	}

	public void sound() {
		Level.playBirdSound(type);
	}

	public boolean checkTouch(float tx, float ty) {
		return tx > (x - mainView.BIRDS_WIDTH * 0.2) &&
				  tx < (x + mainView.BIRDS_WIDTH * 1.2) &&
				  ty > (y - mainView.BIRDS_HEIGHT * 0.2) &&
				  ty < (y + mainView.BIRDS_HEIGHT * 1.2);
	}
}
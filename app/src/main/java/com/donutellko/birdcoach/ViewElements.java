package com.donutellko.birdcoach;

import android.graphics.Bitmap;

/**
 * Created by donat on 4/27/16.
 */
public class ViewElements {


		/*
		void resize(int newW, int newH) {
			bitmap = Bitmap.createScaledBitmap(bitmap, newW, newH, false);
			W = newW;
			H = newH;
		}
		*/


	/*
	static class skyElement {
		float speed;
		Bitmap bitmap;
		int W, H;
		float X, Y;

		skyElement(Bitmap bitmap, int y, float speed) {
			this.bitmap = bitmap;
			Y = y;
			this.speed = speed;
		}

		void draw() {
			mainView.sCanvas.drawBitmap(bitmap, X, Y, Res.paint);
			mainView.sCanvas.drawBitmap(bitmap, X - W, Y, Res.paint);
		}

		public void move() {
			X -= speed;
			if (X < -W) X = 0;
		}
	}
	*/

	static class backElement {
		float speedMult;
		Bitmap bitmap;
		float X, Y;

		backElement(Bitmap bitmap, int y, float speedMult) {
			this.bitmap = bitmap;
			Y = y;
			this.speedMult = speedMult;
		}

		void draw() {
			mainView.sCanvas.drawBitmap(bitmap, X, Y, Res.paint);
			mainView.sCanvas.drawBitmap(bitmap, X + (speedMult / Math.abs(speedMult)) * mainView.Width, Y, Res.paint);
		}

		void move() {
			X += (speedMult > 0) ? speedMult * mainView.Xspeed : 1;
			if (Math.abs(X) > Math.abs(mainView.Width)) X = 0;
		}
	}

	static class forwElement {
		Bitmap bitmap;
		int W, H;
		float X, Y;

		forwElement(Bitmap bitmap, int x, int y) {
			this.bitmap = bitmap;
			X = x;
			Y = y;
		}

		forwElement(Bitmap bitmap, int x, int y, int w, int h) {
			this.bitmap = bitmap;
			X = x;
			Y = y;
			W = w;
			H = h;
		}

		void move() {
			X += mainView.Xspeed;
		}

		void draw() {
			mainView.sCanvas.drawBitmap(bitmap, X, Y, Res.paint);
		}

		boolean checkTouch(float x, float y) {
			return (x > X && x < X + W && y > Y && y < Y + H);
		}
	}
}

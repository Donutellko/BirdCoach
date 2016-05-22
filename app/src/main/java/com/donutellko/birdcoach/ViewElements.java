package com.donutellko.birdcoach;

import android.graphics.Bitmap;

/**
 * Created by donat on 4/27/16.
 */
public class ViewElements {

	static class backElement {
		float speedMult;
		Bitmap bitmap;
		float X, Y, Y0;

		backElement(Bitmap bitmap, int y, float speedMult) {
			this.bitmap = bitmap;
			Y = Y0 = y;
			this.speedMult = speedMult;
		}

		void draw() {
			mainView.sCanvas.drawBitmap(bitmap, X, (speedMult == -1) ? 0 : Y, Res.paint);
			mainView.sCanvas.drawBitmap(bitmap, X + (speedMult / Math.abs(speedMult)) * mainView.Width, (speedMult == -1) ? 0 : Y, Res.paint);
		}

		void moveX() {
			X += (speedMult > 0) ? speedMult * mainView.Xspeed : 1;
			if (Math.abs(X) > Math.abs(mainView.Width)) X = 0;
		}

		void moveY() {
			if (mainView.forwardY <= 0) Y = Y0;
			else Y += mainView.Yspeed;
		}
	}

	static class forwElement {
		Bitmap bitmap;
		int W, H;
		float  X, Y, Y0;

		forwElement(Bitmap bitmap, int x, int y) {
			this.bitmap = bitmap;
			X = x;
			Y = y;
		}

		forwElement(Bitmap bitmap, int x, int y, int w, int h) {
			this.bitmap = bitmap;
			X = x;
			Y = Y0 = y;
			W = w;
			H = h;
		}

		void moveX() {
			X += mainView.Xspeed;
		}

		void moveY() {
			if (mainView.forwardY <= 0) Y = Y0;
			else Y += mainView.Yspeed;
		}

		void draw() {
			mainView.sCanvas.drawBitmap(bitmap, X + mainView.forwardX, Y, Res.paint);
		}

		void draw(int plusX, int plusY) {
			mainView.sCanvas.drawBitmap(bitmap, 0, mainView.forwardY + plusY, Res.paint);
		}

		public void draw(int plusX) {
			mainView.sCanvas.drawBitmap(bitmap, X + plusX + mainView.forwardX, Y, Res.paint);
		}

		boolean checkTouch(float x, float y) {
			return (x > X && x < X + W && y > Y && y < Y + H);
		}

	}
}

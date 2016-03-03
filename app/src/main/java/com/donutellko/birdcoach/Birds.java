/**
 * Created by donat on 2/22/16.
 */

package com.donutellko.birdcoach;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Birds {
    private static final Paint paint = new Paint();
    float x, y; // Где отрисовать птицо
    int size;
    Bitmap bird;

    public Birds(float x, float y, Bitmap bird, int size) {
        super();
        this.x = x;
        this.y = y;
        this.bird = bird;
        this.size = size;

    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bird, x, y, paint);
    }
}


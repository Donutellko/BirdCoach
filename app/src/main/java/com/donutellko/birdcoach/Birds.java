package com.donutellko.birdcoach;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

import static android.graphics.Bitmap.createBitmap;

public class Birds {
    private static final Paint paint = new Paint();
    float x, y;
    int size;   // Ширина изображения
    int type;   // Тип птицы
    Bitmap usual;

    public Birds(float x, float y, int type) {
        super();
        this.x = x;         // Координата по горизонтали
        this.y = y;         // Координата по вертикали
        this.type = type;   // Цвет и издаваемый звук
        size = EndlessView.birdSize;

        usual = Bitmap.createScaledBitmap(createBitmap(EndlessView.birdsBitmap, (type - 1) * (335 * 2 + 2) + 5, 5, 335 * 2 - 2, 248 * 2 - 2),
                size, size * 248 / 335, false); // Стандартное изобржение птицы.
    }

    public void drawBird(Canvas canvas) {
        Random random = new Random();
        if (random.nextInt(100) < 99)   // Случайно определяется момент включения анимации.
            canvas.drawBitmap(usual, x, y, paint);
        else canvas.drawBitmap(usual, x, y, paint);
    }
}
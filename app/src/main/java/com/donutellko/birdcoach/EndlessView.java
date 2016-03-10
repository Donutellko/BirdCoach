package com.donutellko.birdcoach;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static android.graphics.Bitmap.createBitmap;


public class EndlessView extends View {

    public static int birdSize = 100;
    // ширина и высота экрана
    public int Width, Height;
    private Birds activeBird;
    float deltaX, deltaY;
    Paint paint = new Paint();
    Bitmap bg;
    static Bitmap birdsBitmap;

    public static List<Birds> birds = new LinkedList<>();

    public EndlessView(Context context) {
        super(context);
        birdsBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.birds);
        // for (int i = 1; i < 10; i++) birds.add(new Birds(100 * i, 200, i));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Width = getWidth();
        Height = getHeight();

        bg = BitmapFactory.decodeResource(getResources(), R.drawable.back);
        bg = Bitmap.createScaledBitmap(bg, Width, Height, false);
    }

    protected void onDraw(Canvas canvas) {
        // Отобазить фон:
        canvas.drawBitmap(bg, 0, 0, paint);
        // Отобразить птиц:
        for (Birds b : birds) {
            b.drawBird(canvas);
        }
        // Отобразить номер уровня, количество очков и жизней!

        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        // Если игра не начата, начать игру:
        if (Level.level == 0) {
            Level.newGame();
            return true;
        }

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (Birds b : birds)
                    if (x > b.x && x < (b.x + b.size) && y > b.y && y < (b.y + b.size)) {
                        deltaX = b.x - x;
                        deltaY = b.y - y;
                        activeBird = b;
                    }
                break;
            case MotionEvent.ACTION_MOVE:
                if (activeBird != null) {
                    activeBird.x = x + deltaX;
                    activeBird.y = y + deltaY;
                }
                break;
            case MotionEvent.ACTION_UP:
                activeBird = null;
                break;
        }
        return true;
    }
}
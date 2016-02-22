package com.donutellko.birdcoach;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;


public class EndlessView extends View {

    // ширина и высота экрана
    public int w, h;
    // подсчёт очков:
    public int time = 0, level = 1, scores = 0;

    public EndlessView(Context context) {
        super(context);

    }

    protected void onDraw(Canvas canvas) {
        w = getWidth();
        h = getHeight();

        // отобразить фон
        drawBG(canvas);


        // отобразить птиц


        invalidate();

    }

    public void drawBG(Canvas canvas) {
        Paint paint = new Paint();

        // загружаем элементы фона
        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);    // фон
        Bitmap bg_wire = BitmapFactory.decodeResource(getResources(), R.drawable.bg_wire); // столб и провод

        // масштабируем их под размер экрана
        bg = Bitmap.createScaledBitmap(bg, w, h, false);
        bg_wire = Bitmap.createScaledBitmap(bg_wire, w, h, false);

        // отображаем фон
        canvas.drawBitmap(bg, 0, 0, paint);
        canvas.drawBitmap(bg_wire, 0, 0, paint);

    }

    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN)

            if (level == 0) {
                level = 1;
                // скрыть приветствие.

                // запустить первый уровень.
                startLevel(1);
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                // определяем, к какой птице прикоснулись

                // меняем её координаты

            }

        return true;
    }

    private void startLevel(int level) {
        // сгенерировать новый уровень

        // отобразить птиц

        // запустить секундомер

    }
}
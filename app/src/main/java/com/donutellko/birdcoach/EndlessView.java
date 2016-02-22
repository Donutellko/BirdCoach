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


public class EndlessView extends View {

    // ширина и высота экрана
    public int w, h;
    // подсчёт очков:
    public int time = 0, level = 1, scores = 0;

    Bitmap[] birdImg = new Bitmap[9];   //
    int[][] position = new int[20][2];   // список координат, по которым будут раскиданы птицы. [][0] - X, [][1] - Y

    int[] order = new int[1];

    private List<Birds> birds = new LinkedList<Birds>();

    public EndlessView(Context context) {
        super(context);

    }

    protected void onDraw(Canvas canvas) {
        w = getWidth();
        h = getHeight();

        // отобразить фон
        drawBG(canvas);

        // отобразить птиц
        for (Birds b : birds) {
            b.draw(canvas);
        }

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
            } else {    // если прикоснулись к птице
                for (Birds b : birds) {
                    // if (getX() >= b.x)
                }
            }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
                // определяем, к какой птице прикоснулись
            for (Birds b : birds) {
                if (getX() > b.x && getX() < b.x + b.size && getY() > b.y && getY() < b.y + b.size) {

                }
            }
                // меняем её координаты

        }

        return true;
    }

    private void startLevel(int level) {
        // сгенерировать новый уровень
        int count = 4 + level / 4;

        order = generateMelody(count);
        // отобразить птиц
        for (int i = 0; i < count; i++) {
            birds.add(new Birds(position[i][0], position[i][1], birdImg[order[i]], h / 15));
        }
        // запустить секундомер

    }

    private int[] generateMelody(int count) {
        int order[] = new int[count];
        Random random = new Random();
        for (int i = 0; i < count; i++)
            order[i] = (random.nextInt(6) + 1);
        return order;
    }
}
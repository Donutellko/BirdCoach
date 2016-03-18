package com.donutellko.birdcoach;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.View;
import java.util.LinkedList;
import java.util.List;

import static android.graphics.Bitmap.createBitmap;


public class EndlessView extends View {

// NUMBERS, COORDINATES AND SIZES:
    public int Width, Height;
    float deltaX, deltaY, startX, startY;
    static int birdSize = 100;
    boolean moved = false;
// Images:
    static Bitmap birdsBitmap;
    Paint paint = new Paint();
    Bitmap bg;
// SOUNDS:
    static SoundPool soundPool;
    static int birdsSound;
        //static MediaPlayer mPlayer;
// OBJECTS:
    private Birds activeBird;
    static List<Birds> birds = new LinkedList<>();
    Context context;


    public EndlessView(Context context) {
        super(context);
        birdsBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.birds);

        /* MediaPlayer
        this.context = context;
        mPlayer =
        */

        /* SoundPool: */
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        birdsSound = soundPool.load(context, R.raw.sound1, 1);
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
        // ФОН:
        canvas.drawBitmap(bg, 0, 0, paint);
        // ПТИЦЫ:
        for (Birds b : birds) b.drawBird(canvas);
        // Отобразить номер уровня, количество очков и жизней!

        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        // Если игра не начата, начать игру:
        if (Level.level == 0) { Level.newGame();  return true; }

        float x = event.getX(), y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (Birds b : birds)
                    if (x > b.x && x < (b.x + b.size) && y > b.y && y < (b.y + b.size)) {
                        deltaX = b.x - x; deltaY = b.y - y;
                        activeBird = b;
                    }
                startX = x; startY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (activeBird != null) { activeBird.x = x + deltaX; activeBird.y = y + deltaY; }
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(x - startX) < 5 && Math.abs(y - startY) < 5 && activeBird != null) activeBird.sound(context);
                if (activeBird != null) Level.checkPlace(activeBird);
                activeBird = null;
                break;
        }
        return true;
    }
}
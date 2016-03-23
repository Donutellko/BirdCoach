package com.donutellko.birdcoach;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import java.util.LinkedList;
import java.util.List;

public class EndlessView extends View {

    public static final int
        BIRDS_BITMAP_COLUMNS = 9,
        BIRDS_BITMAP_STRINGS = 5;
    public static int
        BIRDS_HEIGHT         = 100,
        BIRDS_WIDTH          = BIRDS_HEIGHT * 248 / 335;

    public static int Width, Height;
    float deltaX, deltaY, startX, startY;

// OBJECTS:
    private Birds activeBird;
    static List<Birds> birds = new LinkedList<>();
    static Context context;

    public EndlessView(Context context) {
        super(context);
        this.context = context;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Width = getWidth();
        Height = getHeight();

        BIRDS_HEIGHT = Height / 7;
        BIRDS_WIDTH = BIRDS_HEIGHT * 248 / 335;

        mResources.loadResources();
        Level.newGame();
    }

    protected void onDraw(Canvas canvas) {
        Level.time++;
        if (mResources.background == null) mResources.loadBackground();
        canvas.drawBitmap(mResources.background, 0, 0, mResources.paint);                           // BG

        for (Birds b : birds) b.drawBird(canvas);                                                   // Birds

        String TS = Level.time / 60 + ":";
        if (Level.time % 60 < 10) TS += "0" + Level.time % 60;
        else TS += Level.time % 60;
        canvas.drawText("Scores: " + Level.score, 30, Height / 30, mResources.textScores);          // Scores
        canvas.drawText("Time: " + TS, Width  * 7/8, Height / 30, mResources.textTime);             // Time

        for (int i = 0; i < Level.lifes; i++) {
            canvas.drawBitmap(mResources.lifesBitmaps,30 + BIRDS_HEIGHT * i * 2 / 3, Height / 20, mResources.paint);
        }

        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (Level.level == 0) { Level.newGame(); return true;}

        float x = event.getX(), y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (Birds b : birds)
                    if (x > b.x && x < (b.x + BIRDS_WIDTH) && y > b.y && y < (b.y + BIRDS_HEIGHT)) {
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
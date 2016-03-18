package com.donutellko.birdcoach;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

import static android.graphics.Bitmap.createBitmap;

public class Birds {
    private static final Paint paint = new Paint();
    float x, y;
    int size;   // Ширина изображения
    int type;   // Тип птицы
    int poseTimer = 100;
    static boolean loaded = false;
    Bitmap usual, pose2, pose3;
    Bitmap currentBitmap;
    Random random = new Random();

    public Birds(float x, float y, int type) {
        super();
        this.x = x;         // Координата по горизонтали
        this.y = y;         // Координата по вертикали
        this.type = type;   // Цвет и издаваемый звук

        size = EndlessView.birdSize;

        currentBitmap = usual = loadPose(1);
    }

    public void drawBird1(Canvas canvas) { canvas.drawBitmap(usual, x, y, paint); }

    public void drawBird(Canvas canvas) {
        int r = random.nextInt(3);

        if (!loaded && poseTimer < 3) {
            loaded = true;
            pose2 = loadPose(2);
            pose3 = loadPose(3);
        }

        if (poseTimer != 0) poseTimer--;
        else {
            poseTimer = 5;
            switch (r) {
                case    2:  currentBitmap = pose2;                  break;
                case    3:  currentBitmap = pose3;                  break;
                default  :  currentBitmap = usual; poseTimer = 15;  break; // стандартное
            }
        }
        canvas.drawBitmap(currentBitmap, x, y, paint);
    }

    public void sound(Context c) {
        float speed = (5 + type) / 10;
        EndlessView.soundPool.play(EndlessView.birdsSound, 1 / 2, 1 / 2, 1, 0, speed);

        /*
        if (mPlayer != null) mPlayer.release();
        mPlayer = MediaPlayer.create(c, R.raw.sound1);
        mPlayer.start();
        */
    }

    private Bitmap loadPose(int pose) {
        return Bitmap.createScaledBitmap(createBitmap(EndlessView.birdsBitmap,
                        (type - 1) * (335 * 2 + 2) + 5, 5,
                                //+ (pose - 1) * (335 * 2 + 2),
                                // X, Y on birdsBitmap
                        335 * 2 - 2, 248 * 2 - 2),                                       // Height, Width from birdsBitmap
                        size, size * 248 / 335, false);                                  // Height, Width of result, filter
    }
}
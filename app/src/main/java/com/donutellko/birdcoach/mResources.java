package com.donutellko.birdcoach;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import static android.graphics.Bitmap.createBitmap;

public class mResources {
    static Bitmap background, lifesBitmaps;
    static Paint paint = new Paint();
    static Paint textScores = new Paint();
    static Paint textTime = new Paint();
    static AlertDialog.Builder builder = new AlertDialog.Builder(EndlessView.context);
    static Bitmap[][] birdBitmaps = new Bitmap[EndlessView.BIRDS_BITMAP_COLUMNS][EndlessView.BIRDS_BITMAP_STRINGS];

    public static void loadResources() {
        loadBackground();
        loadBirds();
        loadBitmaps();
        loadSounds();
        loadDialods();
        loadText();
    }

    private static void loadBitmaps() {
        Context context = EndlessView.context;
        lifesBitmaps = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher), EndlessView.BIRDS_HEIGHT / 2, EndlessView.BIRDS_HEIGHT/ 2, false);
    }

    private static void loadDialods() {
        builder.setTitle("Новая игра")
                .setMessage("Первый уровень, 5 птиц.")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNegativeButton("Начать",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
    }

    private static void loadText() {
        textScores.setColor(0xffff0000);
        textScores.setTextSize(EndlessView.Height / 24);
        textScores.setTextAlign(Paint.Align.LEFT);

        textTime.setColor(0xffff0000);
        textTime.setTextSize(EndlessView.Height / 24);
        textTime.setTextAlign(Paint.Align.LEFT);
    }

    public static void loadBackground() {
        Context context = EndlessView.context;
        background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.back), EndlessView.Width, EndlessView.Height, false);
    }

    public static void loadBirds() {
        Context context = EndlessView.context;

        Bitmap birdsBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.birds);
        for (int type = 0; type < EndlessView.BIRDS_BITMAP_COLUMNS; type++) {
            for(int pose = 0; pose < EndlessView.BIRDS_BITMAP_STRINGS; pose++) {
                if (birdBitmaps[type][pose] == null)
                    birdBitmaps[type][pose] = Bitmap.createScaledBitmap(createBitmap(birdsBitmap,
                                5 + type * 673, pose * 494 + 5,                                     // X, Y on birdsBitmap
                                670, 494),                                                          // Height, Width from birdsBitmap
                                EndlessView.BIRDS_HEIGHT, EndlessView.BIRDS_WIDTH, false);          // Height, Width for result
            }
        }
    }

    public static void loadSounds() {

    }

}

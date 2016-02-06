package com.donutellko.birdcoach;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;


public class EndlessView extends View {

    public int w;
    public int h;

    public EndlessView(Context context) {
        super(context);

    }

    protected void onDraw(Canvas canvas) {
        w = getWidth();
        h = getHeight();
        drawBG(canvas);

        System.out.println(w + " фхтаанг " + h);
    }

    public void drawBG(Canvas canvas) {
        Paint paint = new Paint();

        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        Bitmap bg_wire = BitmapFactory.decodeResource(getResources(), R.drawable.bg_wire);

        bg = Bitmap.createScaledBitmap(bg, w, h, false);
        bg_wire = Bitmap.createScaledBitmap(bg_wire, w, h, false);

        canvas.drawBitmap(bg, 0, 0, paint);
        canvas.drawBitmap(bg_wire, 0, 0, paint);
    }

}

package com.donutellko.birdcoach;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;


public class MainView extends View {

	public static Context context;
	public static mResources ResThread;
	public static int Width, Height;
	float bgX = 0, treeX = 0, treeY = 0, cloudsX = 0;
	float treeYspeed = 0, treeXspeed = 0, bgXspeed = 0.2f;
	boolean res_loaded = false;

	public MainView(Context context) {
		super(context);
		MainView.context = context;

		ResThread = new mResources();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		Width = getWidth();
		Height = getHeight();

	}

	protected void onDraw(Canvas canvas) {

		if (ResThread != null && !res_loaded) {
			ResThread.run();
			res_loaded = true;
		}

		if (cloudsX == 0) cloudsX -= MainView.Width;
		cloudsX++;
		//if (Width <= bgX + Height * 2 && bgX <= 0) bgX -= bgXspeed;
		//else { bgXspeed = - bgXspeed; bgX -= 2 * bgXspeed; }

		if (Height < MainView.Width * 11 / 16 + treeY) treeY -= treeYspeed;
		treeX -= treeXspeed;

		canvas.drawBitmap(mResources.bg, bgX, 0, mResources.paint);
		canvas.drawBitmap(mResources.clouds, cloudsX, 0, mResources.paint);
		canvas.drawBitmap(mResources.tree, treeX, treeY, mResources.paint);

		if (treeX + Width < 0){
			Intent intent = new Intent(getContext(), EndlessActivity.class);
			getContext().startActivity(intent);
		}

		invalidate();
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN)
			if (treeYspeed == 0) treeYspeed = 4;
			else treeXspeed = 7;
		return true;
	}
}

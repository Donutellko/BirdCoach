package com.donutellko.birdcoach;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by donat on 4/21/16.
 */
public class Dialogs {

	static void nextLevel(String title, int level) {
		AlertDialog.Builder levelDialog = new AlertDialog.Builder(mainView.context);
		Typeface type = Typeface.createFromAsset(mainView.context.getAssets(), "comic.ttf");

		Level.timeBool = false;
		String s = "";

		if (level > 1 && !(level == 5 && Level.hardBool))  {
			s = "\n\t\tУ вас уже " + Level.score;
			s += ((Level.score % 10 < 4 && Level.score != 0 && (Level.score < 5 || Level.score > 20)) ? " очка" : " очков")  + "\n";
			if ((Level.hardBool) ? (Level.recordHard > 0) : (Level.recordEasy > 0))
				s+= ((Level.hardBool) ?
						  ((Level.recordHard < Level.score) ? " Предыдущий рекорд на большой сложности - " + Level.recordHard : "") :
						  (Level.recordHard < Level.score) ? " Предыдущий рекорд на низкой сложности - " + Level.recordEasy : "" );
		}



		TextView tv = new TextView(mainView.context);

		tv.setText("\t\tУровень " + level + "\n" + s);
		tv.setTypeface(type);
		tv.setTextSize(mainView.Height / 35);


		levelDialog.setView(tv)
				  .setTitle(title)
				  .setCancelable(false)
				  .setIcon(R.mipmap.ic_launcher)
				  .setNegativeButton("Начать",
							 new DialogInterface.OnClickListener() {
								 public void onClick(DialogInterface dialog, int id) {
									 // Level.playMelody(Level.melodyOrder);
									 dialog.cancel();
								 }
							 });

		/*
		levelDialog.setTitle(title)
				  .setMessage("Уровень " + level + "\n" + s)
				  .setIcon(R.mipmap.ic_launcher)
				  .setCancelable(false)
				  .setNegativeButton("Начать",
							 new DialogInterface.OnClickListener() {
								 public void onClick(DialogInterface dialog, int id) {
									 // Level.playMelody(Level.melodyOrder);
									 dialog.cancel();
								 }
							 });

		*/

		AlertDialog alert = levelDialog.create();
		alert.show();
	}

	static void writeComment() {
		float textX = mainView.forwardX + ((State.state == States.LEVEL || State.MovingFrom() == States.LEVEL) ? 0 : mainView.Width);
		mainView.sCanvas.drawText(Level.comment, textX + mainView.Width * 7 / 16, mainView.Height * 0.55f, Res.textComment);
		mainView.sCanvas.drawText(Level.scoresText(), textX + 30, mainView.Height - mainView.Height / 18, Res.textInfo);
	}
}

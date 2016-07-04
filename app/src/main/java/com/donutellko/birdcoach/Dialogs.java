package com.donutellko.birdcoach;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Класс содержит методы для отображения диалогов и генерации текста, выводимого на экран.
 */
public class Dialogs {

	public static String NAM;

	static void nextLevel(String title, int level) {
		AlertDialog.Builder levelDialog = new AlertDialog.Builder(mainView.context);
		Typeface type = Typeface.createFromAsset(mainView.context.getAssets(), "comic.ttf");

		Level.timeBool = false;
		String s = "";

		if (level > 1 && !(level <= 8 && Level.hardBool)) {
			s = "\n\t\t" + mainView.context.getString(R.string.s_already_got) + " " + Level.score + " " + wordForm("score", Level.score) + "\n";
			if ((Level.hardBool) ? (Level.recordHard[0] > 0) : (Level.recordEasy[0] > 0))
				s += ((Level.hardBool) ?
						  ((Level.recordHard[0] < Level.score) ? mainView.context.getString(R.string.s_prev_hard) + " " + Level.recordHard[0] : "") :
						  (Level.recordHard[0] < Level.score) ? mainView.context.getString(R.string.s_prev_easy) + " " + Level.recordEasy[0] : "");
		}

		TextView tv = new TextView(mainView.context);

		tv.setText("\t\t" + mainView.context.getString(R.string.s_level) + " " + level + "\n" + s);
		tv.setTypeface(type);
		tv.setTextSize(mainView.Height / 40); // / 35);


		levelDialog.setView(tv)
				  .setTitle(title)
				  .setCancelable(false)
				  .setIcon(R.mipmap.ic_launcher)
				  .setNegativeButton(mainView.context.getString(R.string.s_start),
							 new DialogInterface.OnClickListener() {
								 public void onClick(DialogInterface dialog, int id) {
									 dialog.cancel();
								 }
							 });

		AlertDialog alert = levelDialog.create();
		alert.show();
	}

	static void writeComment() {
		float textX = mainView.forwardX + ((State.state == States.LEVEL || State.MovingFrom() == States.LEVEL) ? 0 : mainView.Width);
		mainView.sCanvas.drawText(Level.comment, textX + mainView.Width * 7 / 16, mainView.Height * 0.55f, Res.textComment);
		mainView.sCanvas.drawText(Level.scoresText(), textX + 30, mainView.Height - mainView.Height / 18, Res.textInfo);
	}

	static String wordForm(String s, int n) {
		int m100 = n % 100;
		int m10 = n % 10;
		switch (s) {
			case "score":
				if (m10 == 0 || m10 % 10 >= 5 || m100 >= 5 && m100 <= 20)
					return mainView.context.getString(R.string.s_scores_mult);
				else if (m10 >= 1)
					return mainView.context.getString(R.string.s_scores_some);
				else
					return mainView.context.getString(R.string.s_scores_one);
		}
		return "";
	}

	public static String getName() {
		if (NAM == null) NAM = "";
		boolean notGood = true;
		final EditText edittext = new EditText(mainView.context);
		edittext.setText(NAM.equals("") ? "BIRDIE" : NAM);
		AlertDialog.Builder nameDialog = new AlertDialog.Builder(mainView.context);
		Typeface type = Typeface.createFromAsset(mainView.context.getAssets(), "comic.ttf");

		nameDialog.setTitle("Новый рекорд! ")
				  .setMessage("Вы под номером " + (Level.change_table + 1) + " в данной сложности. Введите своё имя (от одного до шести символов):")
				  .setView(edittext)
				  .setCancelable(false)
				  .setIcon(R.mipmap.ic_launcher)
				  .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					  public void onClick(DialogInterface dialog, int whichButton) {
						  NAM = edittext.getText().toString().toUpperCase();
						  if (NAM.length() > 7) {
							  Toast toast = Toast.makeText(mainView.context, "Не более 6 символов!", Toast.LENGTH_SHORT);
							  toast.show();
						  } else if (NAM.length() == 0) {
							  Toast toast = Toast.makeText(mainView.context, "Минимум один символ!", Toast.LENGTH_SHORT);
							  toast.show();
						  } else {
							  dialog.cancel();

							  if (Level.change_table >= 0) {
								  if (Level.hardBool) {
									  Level.nameRecordHard[Level.change_table] = NAM;
								  } else {
									  Level.nameRecordEasy[Level.change_table] = NAM;
								  }

							  }

							  Toast toast = Toast.makeText(mainView.context, "Сохранено: " + NAM, Toast.LENGTH_SHORT);
							  toast.show();
						  }
					  }
				  });

		nameDialog.show();

/*
		while (notGood) {
			nameDialog.show();
			if (NAM.length() > 7) {
				Toast toast = Toast.makeText(mainView.context, "Не более 6 символов!", Toast.LENGTH_SHORT);
				toast.show();
			} else if (NAM.length() == 0) {
				Toast toast = Toast.makeText(mainView.context, "Минимум один символ!", Toast.LENGTH_SHORT);
				toast.show();
			} else {
				Toast toast = Toast.makeText(mainView.context, "Сохранено: " + NAM, Toast.LENGTH_SHORT);
				toast.show();
				notGood = false;
			}
		}
		*/
		return NAM;
	}
}

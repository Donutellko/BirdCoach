package com.donutellko.birdcoach;

import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by donat on 4/21/16.
 */
public class Dialogs {

	static void nextLevel(String title, int level) {
		AlertDialog.Builder levelDialog = new AlertDialog.Builder(mainView.context);

		Level.timeBool = false;
		String s = "";

		if (level > 1 && !(level == 5 && Level.hardBool))  {
			s = "\nУ вас уже " + Level.score;
			s += ((Level.score % 10 < 4 && Level.score != 0 && (Level.score < 5 || Level.score > 20)) ? " очка" : " очков")  +
					  ((Level.hardBool) ?
								 ((Level.recordHard < Level.score) ? " Предыдущий рекорд на большой сложности - " + Level.recordHard : "") :
								 (Level.recordHard < Level.score) ? " Предыдущий рекорд на низкой сложности - " + Level.recordEasy : "" );
		}

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
		AlertDialog alert = levelDialog.create();
		alert.show();
	}

	static void mistake() {
		AlertDialog.Builder mistakeDialog = new AlertDialog.Builder(mainView.context);

		mistakeDialog.setTitle("Упс!")
				  .setMessage("Кажется, ты перепутал звуки\nПрослушай мелодию заново и попробуй ещё раз!")
				  .setIcon(R.mipmap.ic_launcher)
				  .setCancelable(false)
				  .setNegativeButton("Прослушать",
							 new DialogInterface.OnClickListener() {
								 public void onClick(DialogInterface dialog, int id) {
									 Level.playMelody(Level.melodyOrder);
									 Level.timeBool = true;
									 dialog.cancel();
								 }
							 });
		mistakeDialog.show();
	}

	static void rules() {
		AlertDialog.Builder rulesDialog = new AlertDialog.Builder(mainView.context);

		rulesDialog.setTitle("Правила")
				  .setMessage("Правила игры: \n" +
							 "•   Ваша задача — рассадить птичек на проводе в соответствии с мелодией, которую можно услышать, нажав на мегафон в правом нижнем углу экрана. По мере игры длина мелодии увеличивается. \n" +
							 "•   У вас есть три жизни на одну игру (право на ошибку). Оставшиеся жизни отображаются в верхнем углу экрана. \n" +
							 "•   На каждом уровне мелодию можно прослушать столько раз, сколько птичек нужно рассадить. \n" +
							 "•   За каждый уровень вы набираете очки (верхний правый угол), зависящие от количества прослушиваний и времени, затраченного на этот уровень (верхний левый угол).\n" +
							 "•   В игре есть несколько уровней сложности, он изменяется в настройках. При уровне Новичок вы начинаете играть с трёх птичек, а при уровне Маэстро - с шести.")
				  .setIcon(R.mipmap.ic_launcher)
				  .setCancelable(true)
				  .setPositiveButton("Усвоил",
							 new DialogInterface.OnClickListener() {
								 public void onClick(DialogInterface dialog, int id) {
									 dialog.cancel();
								 }
							 });
		AlertDialog alert = rulesDialog.create();
		alert.show();
	}

	static void settings() {
		AlertDialog.Builder settingsDialog = new AlertDialog.Builder(mainView.context);

		String settings[] = new String[3];
		settings[0] = (Level.musicBool) ? "Выключить музыку" : "Включить музыку";
		settings[1] = (Level.hardBool) ? "Текущая сложность: Маэстро" : "Текущая сложность: Новичок";
		settings[2] = "Закрыть";

		settingsDialog.
				  setTitle("Настройки")
				  .setIcon(R.mipmap.ic_launcher)
				  .setCancelable(true)
				  .setItems(settings, new DialogInterface.OnClickListener() {
					  @Override
					  public void onClick(DialogInterface dialog, int which) {
						  if (which == 0) Level.musicBool = !Level.musicBool;
						  else if (which == 1) Level.hardBool = !Level.hardBool;
						  else dialog.cancel();
					  }
				  });
		AlertDialog alert = settingsDialog.create();
		alert.show();
	}

	static void writeComment() {
		float textX = mainView.forwardX + ((State.state == States.LEVEL || State.MovingFrom() == States.LEVEL) ? 0 : mainView.Width);
		mainView.sCanvas.drawText(Level.comment, textX + mainView.Width * 7 / 16, mainView.Height * 0.55f, Res.textComment);
		mainView.sCanvas.drawText(Level.scoresText(), textX + 30, mainView.Height - mainView.Height / 18, Res.textInfo);
	}
}

package com.donutellko.birdcoach;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/*
   Индивидуальный проект Шергалиса Доната (donutellko@gmail.com).
   Info: Игра BirdCoach ("Птичий тренер"), призвана развивать у детей музыкальный слух в игровой форме.
   Основная информация находится в файле BirdCoach.pdf

   Условные обозначения в комментариях:
       _ : временное решение
       - : не выполненная задача
       + : выполненная задача
       : проблемный участок
       . : задача выполнена и работает, как надо.
        <p/>
   Префиксы:
       b : кнопка
       c : onClick method
       t : временная переменная
       e : EditText
 */

/*
 Список Activity:
     _ MainActivity
     _

 */


public class MainActivity extends AppCompatActivity {

    /**
     * Главное активити.
     * Здесь распологаются:
     * 1) Кнопка выбора режима игры    -
     * 2) Кнопка "Настройки"           -
     * 3) Кнопка "Как играть?"         - HowToPlayActivity
     */

    /*
        Используемые названия:
            bPlay - кнопка вызова меню выбора режима игры
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
    }

    public void cPlayEndless(View view) {
        Intent intent = new Intent(MainActivity.this, EndlessActivity.class);
        startActivity(intent);
    }

}

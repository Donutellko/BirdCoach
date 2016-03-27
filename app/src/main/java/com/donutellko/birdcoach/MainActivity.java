package com.donutellko.birdcoach;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
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
     _ EndlessActivity для бесконечного режима игры

 */

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(new MainView(this));
    }
}
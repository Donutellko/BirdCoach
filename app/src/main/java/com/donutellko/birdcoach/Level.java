package com.donutellko.birdcoach;

import java.util.Random;

/**
 * level:   1   2   3   4   5   6   7   8   9   10  11  12
 * N:       4   4:1 5   5:1 6   6:2 7   7:2 8   8:3 9   9:3
 */

public class Level {
    static int level = 0;  // Номер уровня, из него вычисляется количество птиц в мелодии и избыточных птиц.
    static int score = 0;  // Количество очков за игру (за все уровни до поражения или рестарта игры)
    static int time = 0;   // Время, затраченное на данный уровень
    static int lives = 3;  // Количество оставшихся жизней игрока.
    static int[] melodyOrder = new int[1];

    public static void nextLevel() {
        level++;
        time = 0;
        score += level * 100 /* гарантированные очки */ + (level * 20 - time) % (level * 100) /* дополнительные очки */;
        melodyOrder = generateMelody(level + 4);
        createBirds(melodyOrder);
    }

    public static void restartLevel() {
        score -= level * 50 * (4 - lives);
        lives--;
        createBirds(melodyOrder);
    }

    public static void newGame() {
        score = 0;
        lives = 3;
        level = 0;
        nextLevel();
    }

    public static void createBirds(int[] order) {
        Random random = new Random();
        for (Birds b : EndlessView.birds)
            EndlessView.birds.remove(b);
        for (int i = 0; i < order.length; i++) {
            EndlessView.birds.add(new Birds(200 + 120 * i, (float) (500 + Math.sin(random.nextInt(100)) * 90), order[i]));
        }
    }

    private static int[] generateMelody(int count) {
        int[] order = new int[count];
        Random random = new Random();
        for (int i = 0; i < count; i++)
            order[i] = (random.nextInt(6) + 1);
        return order;
    }
}

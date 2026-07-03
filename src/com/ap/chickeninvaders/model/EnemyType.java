package com.ap.chickeninvaders.model;

import java.awt.*;

public enum EnemyType {
    NORMAL(10, new Color(245, 225, 90)),
    FAST(15, new Color(255, 150, 70)),
    ZIGZAG(20, new Color(190, 130, 255));

    private final int score;
    private final Color color;

    EnemyType(int score, Color color) {
        this.score = score;
        this.color = color;
    }

    public int getScore() {
        return score;
    }

    public Color getColor() {
        return color;
    }
}


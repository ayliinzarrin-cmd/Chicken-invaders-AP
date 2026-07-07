package com.ap.chickeninvaders.model;

import java.awt.*;

public class Enemy {
    private int x;
    private int y;
    private final int width = 42;
    private final int height = 34;
    private final EnemyType type;
    private double zigzagPhase;

    public Enemy(int x, int y, EnemyType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void move(int dx) {
        int realDx = type == EnemyType.FAST ? dx * 2 : dx;
        x += realDx;

        if (type == EnemyType.ZIGZAG) {
            zigzagPhase += 0.18;
            y += (int) Math.round(Math.sin(zigzagPhase));
        }
    }

    public void moveDown(int dy) {
        y += dy;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int centerX() {
        return x + width / 2;
    }

    public int centerY() {
        return y + height / 2;
    }

    public int getScore() {
        return type.getScore();
    }

    public void draw(Graphics2D g) {
        g.setColor(type.getColor());
        g.fillOval(x, y, width, height);

        g.setColor(new Color(255, 80, 60));
        g.fillOval(x + 14, y - 8, 14, 14);

        g.setColor(Color.BLACK);
        g.fillOval(x + 10, y + 12, 5, 5);
        g.fillOval(x + 27, y + 12, 5, 5);

        g.setColor(new Color(255, 170, 80));
        g.fillPolygon(
                new int[]{x + width / 2 - 5, x + width / 2 + 5, x + width / 2},
                new int[]{y + 21, y + 21, y + 28},
                3
        );
    }
}

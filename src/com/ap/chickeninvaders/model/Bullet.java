package com.ap.chickeninvaders.model;

import java.awt.*;

public class Bullet {
    private int x;
    private int y;
    private final int speed = 9;
    private final boolean echoBullet;

    public Bullet(int x, int y) {
        this(x, y, false);
    }

    public Bullet(int x, int y, boolean echoBullet) {
        this.x = x;
        this.y = y;
        this.echoBullet = echoBullet;
    }

    public void update() {
        y -= speed;
    }

    public boolean isOutOfScreen() {
        return y < -20;
    }

    public Rectangle getBounds() {
        return new Rectangle(x - 3, y - 12, 6, 16);
    }

    public void draw(Graphics2D g) {
        if (echoBullet) {
            g.setColor(new Color(255, 90, 220, 90));
            g.fillOval(x - 7, y - 16, 14, 22);
            g.setColor(new Color(255, 180, 245));
        } else {
            g.setColor(new Color(100, 240, 255));
        }
        g.fillRoundRect(x - 3, y - 12, 6, 16, 6, 6);
    }
}

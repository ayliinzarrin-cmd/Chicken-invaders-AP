package com.ap.chickeninvaders.model;

import java.awt.*;

public class Bullet {
    private int x;
    private int y;
    private final int speed = 9;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
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
        g.setColor(new Color(100, 240, 255));
        g.fillRoundRect(x - 3, y - 12, 6, 16, 6, 6);
    }
}

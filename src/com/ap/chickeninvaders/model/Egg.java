package com.ap.chickeninvaders.model;

import java.awt.*;

public class Egg {
    private int x;
    private int y;
    private final int speed = 4;

    public Egg(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y += speed;
    }

    public boolean isOutOfScreen(int height) {
        return y > height + 30;
    }

    public Rectangle getBounds() {
        return new Rectangle(x - 7, y - 9, 14, 18);
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(255, 245, 210));
        g.fillOval(x - 7, y - 9, 14, 18);
        g.setColor(new Color(220, 180, 120));
        g.drawOval(x - 7, y - 9, 14, 18);
    }
}


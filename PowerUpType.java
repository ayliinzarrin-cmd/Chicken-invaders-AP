package com.ap.chickeninvaders.model;

import java.awt.*;

public class Egg {
    private double x;
    private double y;
    private final double dx;
    private final double dy;

    public Egg(int x, int y) {
        this(x, y, 0, 4);
    }

    public Egg(int x, int y, double dx, double dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    public void update() {
        x += dx;
        y += dy;
    }

    public boolean isOutOfScreen(int height) {
        return y > height + 30 || y < -30 || x < -30 || x > 830;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x - 7, (int) y - 9, 14, 18);
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(255, 245, 210));
        g.fillOval((int) x - 7, (int) y - 9, 14, 18);
        g.setColor(new Color(220, 180, 120));
        g.drawOval((int) x - 7, (int) y - 9, 14, 18);
    }
}


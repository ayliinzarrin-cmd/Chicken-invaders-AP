package com.ap.chickeninvaders.model;

import java.awt.*;

public class Explosion {
    private final double x;
    private final double y;
    private int age;
    private final int maxAge = 24;

    public Explosion(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        age++;
    }

    public boolean isDone() {
        return age >= maxAge;
    }

    public void draw(Graphics2D g) {
        int radius = 8 + age * 2;
        int alpha = Math.max(0, 220 - age * 9);
        g.setColor(new Color(255, 170, 40, alpha));
        g.fillOval((int) x - radius, (int) y - radius, radius * 2, radius * 2);
        g.setColor(new Color(255, 240, 160, Math.max(0, alpha - 60)));
        g.drawOval((int) x - radius - 4, (int) y - radius - 4, radius * 2 + 8, radius * 2 + 8);
    }
}


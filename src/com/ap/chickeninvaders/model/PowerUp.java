package com.ap.chickeninvaders.model;

import java.awt.*;

public class PowerUp {
    private double x;
    private double y;
    private final PowerUpType type;

    public PowerUp(double x, double y, PowerUpType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void update() {
        y += 2;
    }

    public boolean isOutOfScreen(int height) {
        return y > height + 30;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x - 16, (int) y - 14, 32, 28);
    }

    public PowerUpType getType() {
        return type;
    }

    public void draw(Graphics2D g) {
        g.setColor(type.getColor());
        g.fillRoundRect((int) x - 18, (int) y - 14, 36, 28, 10, 10);
        g.setColor(Color.BLACK);
        g.drawString(type.getLabel(), (int) x - 16, (int) y + 5);
    }
}


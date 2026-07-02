package com.ap.chickeninvaders.model;

import java.awt.*;

public class Enemy {
    private int x;
    private int y;
    private final int width = 42;
    private final int height = 34;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int dx) {
        x += dx;
    }

    public void moveDown(int dy) {
        y += dy;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(245, 225, 90));
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


package com.ap.chickeninvaders.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Boss {
    private int x = 325;
    private int y = 70;
    private final int width = 150;
    private final int height = 95;
    private final int maxHp = 50;
    private int hp = maxHp;
    private int dx = 2;
    private int verticalDirection = 1;
    private long lastShotTime;

    public void update(int panelWidth) {
        x += dx;
        y += verticalDirection;

        if (x < 20 || x + width > panelWidth - 20) {
            dx *= -1;
        }
        if (y < 55 || y > 110) {
            verticalDirection *= -1;
        }
    }

    public List<Egg> tryShoot(long now) {
        if (now - lastShotTime < 1500) {
            return List.of();
        }
        lastShotTime = now;

        int centerX = x + width / 2;
        int centerY = y + height / 2;
        List<Egg> eggs = new ArrayList<>();
        eggs.add(new Egg(centerX, centerY, 0, 4));
        eggs.add(new Egg(centerX, centerY, 0, -4));
        eggs.add(new Egg(centerX, centerY, 4, 0));
        eggs.add(new Egg(centerX, centerY, -4, 0));
        return eggs;
    }

    public void damage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(255, 180, 80));
        g.fillOval(x, y, width, height);
        g.setColor(new Color(255, 70, 50));
        g.fillOval(x + width / 2 - 18, y - 18, 36, 28);
        g.setColor(Color.BLACK);
        g.fillOval(x + 45, y + 35, 12, 12);
        g.fillOval(x + 95, y + 35, 12, 12);
        drawHealthBar(g);
    }

    private void drawHealthBar(Graphics2D g) {
        int barWidth = 260;
        int barX = 270;
        int barY = 35;

        g.setColor(Color.DARK_GRAY);
        g.fillRect(barX, barY, barWidth, 14);
        int filled = (int) (barWidth * (hp / (double) maxHp));
        g.setColor(new Color(70, 220, 100));
        g.fillRect(barX, barY, filled, 14);
        g.setColor(Color.WHITE);
        g.drawRect(barX, barY, barWidth, 14);
        g.drawString("Boss HP: " + hp + "/" + maxHp, barX + 85, barY - 4);
    }
}


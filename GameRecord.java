package com.ap.chickeninvaders.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Boss {
    private int x;
    private int y;
    private final int level;
    private final int width;
    private final int height;
    private final int maxHp;
    private int hp;
    private int dx;
    private int verticalDirection = 1;
    private long lastShotTime;

    public Boss(int level) {
        this.level = level;
        this.width = level == 8 ? 210 : 150;
        this.height = level == 8 ? 130 : 95;
        this.maxHp = level == 8 ? 100 : 50;
        this.hp = maxHp;
        this.dx = level == 8 ? 3 : 2;
        this.x = (800 - width) / 2;
        this.y = level == 8 ? 55 : 70;
    }

    public void update(int panelWidth) {
        x += dx;
        y += verticalDirection;

        if (x < 20 || x + width > panelWidth - 20) {
            dx *= -1;
        }

        int minY = level == 8 ? 45 : 55;
        int maxY = level == 8 ? 145 : 110;
        if (y < minY || y > maxY) {
            verticalDirection *= -1;
        }
    }

    public List<Egg> tryShoot(long now) {
        long delay = level == 8 ? 1000 : 1500;
        if (now - lastShotTime < delay) {
            return List.of();
        }
        lastShotTime = now;

        int centerX = x + width / 2;
        int centerY = y + height / 2;
        List<Egg> eggs = new ArrayList<>();

        if (level == 8) {
            for (int i = 0; i < 8; i++) {
                double angle = Math.toRadians(i * 45);
                eggs.add(new Egg(centerX, centerY, Math.cos(angle) * 5, Math.sin(angle) * 5));
            }
        } else {
            eggs.add(new Egg(centerX, centerY, 0, 4));
            eggs.add(new Egg(centerX, centerY, 0, -4));
            eggs.add(new Egg(centerX, centerY, 4, 0));
            eggs.add(new Egg(centerX, centerY, -4, 0));
        }

        return eggs;
    }

    public void damage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public int getLevel() {
        return level;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g) {
        g.setColor(level == 8 ? new Color(220, 80, 255) : new Color(255, 180, 80));
        g.fillOval(x, y, width, height);

        g.setColor(new Color(255, 70, 50));
        g.fillOval(x + width / 2 - 18, y - 18, 36, 28);

        g.setColor(Color.BLACK);
        g.fillOval(x + width / 3, y + height / 3, 12, 12);
        g.fillOval(x + width * 2 / 3, y + height / 3, 12, 12);

        drawHealthBar(g);
    }

    private void drawHealthBar(Graphics2D g) {
        int barWidth = 300;
        int barX = 250;
        int barY = 35;

        g.setColor(Color.DARK_GRAY);
        g.fillRect(barX, barY, barWidth, 14);

        int filled = (int) (barWidth * (hp / (double) maxHp));
        g.setColor(level == 8 ? new Color(255, 80, 120) : new Color(70, 220, 100));
        g.fillRect(barX, barY, filled, 14);

        g.setColor(Color.WHITE);
        g.drawRect(barX, barY, barWidth, 14);
        g.drawString("Boss " + level + " HP: " + hp + "/" + maxHp, barX + 90, barY - 4);
    }
}

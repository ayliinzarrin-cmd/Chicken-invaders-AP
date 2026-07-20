package com.ap.chickeninvaders.model;

import java.awt.*;
import java.util.List;

public abstract class Boss {
    protected double x;
    protected double y;
    private final int level;
    private final int width;
    private final int height;
    private final int maxHp;
    private int hp;
    private long lastShotTime;

    protected Boss(int level, int width, int height, int maxHp, double x, double y) {
        this.level = level;
        this.width = width;
        this.height = height;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.x = x;
        this.y = y;
    }

    public static Boss create(int level) {
        return switch (level) {
            case 4 -> new BossLevel4();
            case 8 -> new BossLevel8();
            default -> throw new IllegalArgumentException("Boss is only available in levels 4 and 8.");
        };
    }

    public abstract void update(int panelWidth);

    public abstract List<Egg> tryShoot(long now);

    protected final boolean canShoot(long now, long delay) {
        if (now - lastShotTime < delay) {
            return false;
        }
        lastShotTime = now;
        return true;
    }

    protected final int centerX() {
        return (int) Math.round(x) + width / 2;
    }

    protected final int centerY() {
        return (int) Math.round(y) + height / 2;
    }

    protected final int getBossWidth() {
        return width;
    }

    public final void damage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public final boolean isDead() {
        return hp <= 0;
    }

    public final int getLevel() {
        return level;
    }

    public final Rectangle getBounds() {
        return new Rectangle((int) Math.round(x), (int) Math.round(y), width, height);
    }

    public final void draw(Graphics2D g) {
        int drawX = (int) Math.round(x);
        int drawY = (int) Math.round(y);
        g.setColor(bodyColor());
        g.fillOval(drawX, drawY, width, height);
        g.setColor(new Color(255, 70, 50));
        g.fillOval(drawX + width / 2 - 18, drawY - 18, 36, 28);
        g.setColor(Color.BLACK);
        g.fillOval(drawX + width / 3, drawY + height / 3, 12, 12);
        g.fillOval(drawX + width * 2 / 3, drawY + height / 3, 12, 12);
        drawHealthBar(g);
    }

    protected abstract Color bodyColor();

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

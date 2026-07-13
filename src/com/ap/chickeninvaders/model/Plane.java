package com.ap.chickeninvaders.model;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Plane {
    private int x;
    private int y;
    private final int width = 46;
    private final int height = 42;
    private final int speed = 5;
    private int lives = 3;
    private int fireCount = 1;
    private long lastShotTime;
    private long rapidUntil;
    private long shieldUntil;

    public Plane(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(boolean[] keys, int panelWidth, int panelHeight) {
        if (isPressed(keys, KeyEvent.VK_LEFT) || isPressed(keys, KeyEvent.VK_A)) {
            x -= speed;
        }
        if (isPressed(keys, KeyEvent.VK_RIGHT) || isPressed(keys, KeyEvent.VK_D)) {
            x += speed;
        }
        if (isPressed(keys, KeyEvent.VK_UP) || isPressed(keys, KeyEvent.VK_W)) {
            y -= speed;
        }
        if (isPressed(keys, KeyEvent.VK_DOWN) || isPressed(keys, KeyEvent.VK_S)) {
            y += speed;
        }

        x = Math.max(0, Math.min(panelWidth - width, x));
        y = Math.max(60, Math.min(panelHeight - height, y));
    }

    public List<Bullet> tryShoot(long now) {
        long cooldown = isRapid(now) ? 100 : 300;
        if (now - lastShotTime < cooldown) {
            return List.of();
        }
        lastShotTime = now;

        List<Bullet> bullets = new ArrayList<>();
        int spacing = 12;
        int firstX = x + width / 2 - ((fireCount - 1) * spacing) / 2;
        for (int i = 0; i < fireCount; i++) {
            bullets.add(new Bullet(firstX + i * spacing, y));
        }
        return bullets;
    }

    public void applyPowerUp(PowerUpType type, long now) {
        switch (type) {
            case ADD_FIRE -> fireCount = Math.min(6, fireCount + 1);
            case RAPID_FIRE -> rapidUntil = now + 8000;
            case EXTRA_LIFE -> lives = Math.min(5, lives + 1);
            case SHIELD -> shieldUntil = now + 10000;
            case FREEZE_BOMB -> {
                // GamePanel handles the global freeze timer.
            }
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x + 6, y + 6, width - 12, height - 8);
    }

    public void loseLife() {
        lives = Math.max(0, lives - 1);
    }

    public int getLives() {
        return lives;
    }

    public int getFireCount() {
        return fireCount;
    }

    public boolean isRapid(long now) {
        return now < rapidUntil;
    }

    public boolean isShielded(long now) {
        return now < shieldUntil;
    }

    public long rapidSecondsLeft(long now) {
        return Math.max(0, (rapidUntil - now + 999) / 1000);
    }

    public long shieldSecondsLeft(long now) {
        return Math.max(0, (shieldUntil - now + 999) / 1000);
    }

    private boolean isPressed(boolean[] keys, int keyCode) {
        return keyCode >= 0 && keyCode < keys.length && keys[keyCode];
    }

    public void draw(Graphics2D g) {
        if (isShielded(System.currentTimeMillis())) {
            g.setColor(new Color(100, 150, 255, 90));
            g.fillOval(x - 10, y - 10, width + 20, height + 20);
        }

        Polygon body = new Polygon();
        body.addPoint(x + width / 2, y);
        body.addPoint(x + width, y + height);
        body.addPoint(x + width / 2, y + height - 12);
        body.addPoint(x, y + height);

        g.setColor(new Color(70, 180, 255));
        g.fillPolygon(body);
        g.setColor(Color.WHITE);
        g.fillOval(x + width / 2 - 6, y + 12, 12, 12);
    }
}

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
    private long lastShotTime;

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
        if (now - lastShotTime < 300) {
            return List.of();
        }
        lastShotTime = now;

        List<Bullet> bullets = new ArrayList<>();
        bullets.add(new Bullet(x + width / 2, y));
        return bullets;
    }

    private boolean isPressed(boolean[] keys, int keyCode) {
        return keyCode >= 0 && keyCode < keys.length && keys[keyCode];
    }

    public void draw(Graphics2D g) {
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


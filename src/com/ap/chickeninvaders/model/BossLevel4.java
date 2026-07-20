package com.ap.chickeninvaders.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BossLevel4 extends Boss {
    private double horizontalSpeed = 1.5;
    private double verticalPhase;

    public BossLevel4() {
        super(4, 150, 95, 50, 325, 70);
    }

    @Override
    public void update(int panelWidth) {
        x += horizontalSpeed;
        if (x < 20 || x + getBossWidth() > panelWidth - 20) {
            x = Math.max(20, Math.min(panelWidth - 20 - getBossWidth(), x));
            horizontalSpeed *= -1;
        }
        verticalPhase += 0.04;
        y = 82 + Math.sin(verticalPhase) * 25;
    }

    @Override
    public List<Egg> tryShoot(long now) {
        if (!canShoot(now, 1500)) {
            return List.of();
        }
        List<Egg> eggs = new ArrayList<>();
        eggs.add(new Egg(centerX(), centerY(), 0, 4));
        eggs.add(new Egg(centerX(), centerY(), 0, -4));
        eggs.add(new Egg(centerX(), centerY(), 4, 0));
        eggs.add(new Egg(centerX(), centerY(), -4, 0));
        return eggs;
    }

    @Override
    protected Color bodyColor() {
        return new Color(255, 180, 80);
    }
}

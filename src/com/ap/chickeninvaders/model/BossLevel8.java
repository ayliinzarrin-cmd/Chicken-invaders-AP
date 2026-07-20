package com.ap.chickeninvaders.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BossLevel8 extends Boss {
    private final Random random = new Random();
    private double horizontalVelocity = 2.0;
    private double verticalPhase;

    public BossLevel8() {
        super(8, 210, 130, 100, 295, 55);
    }

    @Override
    public void update(int panelWidth) {
        // The final boss accelerates gently and occasionally changes direction mid-flight.
        horizontalVelocity += Math.copySign(0.012, horizontalVelocity);
        if (Math.abs(horizontalVelocity) > 3.4) {
            horizontalVelocity = Math.copySign(1.7, horizontalVelocity);
        }
        if (random.nextDouble() < 0.003) {
            horizontalVelocity *= -1;
        }

        x += horizontalVelocity;
        if (x < 20 || x + getBossWidth() > panelWidth - 20) {
            x = Math.max(20, Math.min(panelWidth - 20 - getBossWidth(), x));
            horizontalVelocity *= -1;
        }

        verticalPhase += 0.035;
        y = 95 + Math.sin(verticalPhase) * 50;
    }

    @Override
    public List<Egg> tryShoot(long now) {
        if (!canShoot(now, 1000)) {
            return List.of();
        }
        List<Egg> eggs = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            double angle = Math.toRadians(i * 45);
            eggs.add(new Egg(centerX(), centerY(), Math.cos(angle) * 5, Math.sin(angle) * 5));
        }
        return eggs;
    }

    @Override
    protected Color bodyColor() {
        return new Color(220, 80, 255);
    }
}

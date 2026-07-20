package com.ap.chickeninvaders.model;

public class ZigzagEnemy extends Enemy {
    private double phase;

    public ZigzagEnemy(Cell cell, int level) {
        super(cell, level);
    }

    public ZigzagEnemy(Cell cell, double spawnX, double spawnY, int level) {
        super(cell, spawnX, spawnY, level);
    }

    @Override
    protected double settledYOffset(double formationSpeed) {
        phase += 0.18;
        return Math.sin(phase) * 8;
    }

    @Override
    protected double spawnTargetXOffset() {
        phase += 0.25;
        return Math.sin(phase) * 26;
    }
}

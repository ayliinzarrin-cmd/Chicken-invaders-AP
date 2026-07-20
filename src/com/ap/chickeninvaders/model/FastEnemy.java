package com.ap.chickeninvaders.model;

public class FastEnemy extends Enemy {
    private double offset;
    private int direction = 1;

    public FastEnemy(Cell cell, int level) {
        super(cell, level);
    }

    public FastEnemy(Cell cell, double spawnX, double spawnY, int level) {
        super(cell, spawnX, spawnY, level);
    }

    @Override
    protected double settledXOffset(double formationSpeed) {
        offset += direction * formationSpeed * 2.0;
        if (Math.abs(offset) >= 14) {
            offset = Math.copySign(14, offset);
            direction *= -1;
        }
        return offset;
    }
}

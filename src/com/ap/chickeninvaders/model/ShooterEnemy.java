package com.ap.chickeninvaders.model;

public class ShooterEnemy extends Enemy {
    public ShooterEnemy(Cell cell, int level) {
        super(cell, level);
    }

    public ShooterEnemy(Cell cell, double spawnX, double spawnY, int level) {
        super(cell, spawnX, spawnY, level);
    }
}

package com.ap.chickeninvaders.model;

public class NormalEnemy extends Enemy {
    public NormalEnemy(Cell cell, int level) {
        super(cell, level);
    }

    public NormalEnemy(Cell cell, double spawnX, double spawnY, int level) {
        super(cell, spawnX, spawnY, level);
    }
}

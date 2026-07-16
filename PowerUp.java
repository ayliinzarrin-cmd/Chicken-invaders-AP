package com.ap.chickeninvaders.model;

public class Cell {
    private double x;
    private double y;
    private final EnemyType enemyType;
    private int remainingEnemies;

    public Cell(double x, double y, EnemyType enemyType, int enemyCount) {
        this.x = x;
        this.y = y;
        this.enemyType = enemyType;
        this.remainingEnemies = enemyCount;
    }

    public void move(double dx) {
        x += dx;
    }

    public void moveDown(double dy) {
        y += dy;
    }

    public void registerKill() {
        remainingEnemies = Math.max(0, remainingEnemies - 1);
    }

    public boolean needsReplacement() {
        return remainingEnemies > 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public EnemyType getEnemyType() {
        return enemyType;
    }

    public int getRemainingEnemies() {
        return remainingEnemies;
    }
}

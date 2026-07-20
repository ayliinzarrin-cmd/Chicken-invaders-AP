package com.ap.chickeninvaders.model;

import java.awt.*;

public abstract class Enemy {
    private double x;
    private double y;
    private final int width = 42;
    private final int height = 34;
    private final Cell cell;
    private final int maxHealth;
    private int health;
    private boolean settled;
    private int hitFlashFrames;

    protected Enemy(Cell cell, int level) {
        this(cell, cell.getX(), cell.getY(), level, true);
    }

    protected Enemy(Cell cell, double spawnX, double spawnY, int level) {
        this(cell, spawnX, spawnY, level, false);
    }

    private Enemy(Cell cell, double x, double y, int level, boolean settled) {
        this.cell = cell;
        this.x = x;
        this.y = y;
        this.settled = settled;
        this.maxHealth = cell.getEnemyType().getHealthForLevel(level);
        this.health = maxHealth;
    }

    public static Enemy create(Cell cell, int level) {
        return createByType(cell, cell.getX(), cell.getY(), level, false);
    }

    public static Enemy createReplacement(Cell cell, double spawnX, double spawnY, int level) {
        return createByType(cell, spawnX, spawnY, level, true);
    }

    private static Enemy createByType(Cell cell, double x, double y, int level, boolean replacement) {
        return switch (cell.getEnemyType()) {
            case NORMAL -> replacement
                    ? new NormalEnemy(cell, x, y, level)
                    : new NormalEnemy(cell, level);
            case FAST -> replacement
                    ? new FastEnemy(cell, x, y, level)
                    : new FastEnemy(cell, level);
            case ZIGZAG -> replacement
                    ? new ZigzagEnemy(cell, x, y, level)
                    : new ZigzagEnemy(cell, level);
            case SHOOTER -> replacement
                    ? new ShooterEnemy(cell, x, y, level)
                    : new ShooterEnemy(cell, level);
        };
    }

    public final void update(double formationSpeed) {
        if (hitFlashFrames > 0) {
            hitFlashFrames--;
        }

        if (!settled) {
            moveTowardCell();
            return;
        }

        x = cell.getX() + settledXOffset(formationSpeed);
        y = cell.getY() + settledYOffset(formationSpeed);
    }

    private void moveTowardCell() {
        double centerDx = cell.getX() - x;
        double centerDy = cell.getY() - y;
        if (Math.abs(centerDy) <= 7 && Math.abs(centerDx) <= 28) {
            x = cell.getX();
            y = cell.getY();
            settled = true;
            return;
        }

        double targetX = cell.getX() + spawnTargetXOffset();
        double targetY = cell.getY() + spawnTargetYOffset();
        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.max(1, Math.hypot(dx, dy));
        double spawnSpeed = 6.0;
        x += dx / distance * spawnSpeed;
        y += dy / distance * spawnSpeed;
    }

    protected double settledXOffset(double formationSpeed) {
        return 0;
    }

    protected double settledYOffset(double formationSpeed) {
        return 0;
    }

    protected double spawnTargetXOffset() {
        return 0;
    }

    protected double spawnTargetYOffset() {
        return 0;
    }

    public final boolean damage(int amount) {
        health = Math.max(0, health - amount);
        hitFlashFrames = 5;
        return health == 0;
    }

    public final Rectangle getBounds() {
        return new Rectangle((int) Math.round(x), (int) Math.round(y), width, height);
    }

    public final int centerX() {
        return (int) Math.round(x) + width / 2;
    }

    public final int centerY() {
        return (int) Math.round(y) + height / 2;
    }

    public final int getScore() {
        return getType().getScore();
    }

    public final EnemyType getType() {
        return cell.getEnemyType();
    }

    public final Cell getCell() {
        return cell;
    }

    public final int getHealth() {
        return health;
    }

    public final int getMaxHealth() {
        return maxHealth;
    }

    public final boolean isSettled() {
        return settled;
    }

    public final void draw(Graphics2D g) {
        int drawX = (int) Math.round(x);
        int drawY = (int) Math.round(y);

        g.setColor(hitFlashFrames > 0 ? Color.WHITE : getType().getColor());
        g.fillOval(drawX, drawY, width, height);

        g.setColor(new Color(255, 80, 60));
        g.fillOval(drawX + 14, drawY - 8, 14, 14);
        g.setColor(Color.BLACK);
        g.fillOval(drawX + 10, drawY + 12, 5, 5);
        g.fillOval(drawX + 27, drawY + 12, 5, 5);
        g.setColor(new Color(255, 170, 80));
        g.fillPolygon(
                new int[]{drawX + width / 2 - 5, drawX + width / 2 + 5, drawX + width / 2},
                new int[]{drawY + 21, drawY + 21, drawY + 28},
                3
        );

        drawHealthBar(g, drawX, drawY);
        drawCellCounter(g, drawX, drawY);
    }

    private void drawHealthBar(Graphics2D g, int drawX, int drawY) {
        int barY = drawY + height + 3;
        g.setColor(new Color(45, 45, 55));
        g.fillRect(drawX, barY, width, 4);
        g.setColor(new Color(80, 230, 120));
        g.fillRect(drawX, barY, (int) Math.round(width * health / (double) maxHealth), 4);
    }

    private void drawCellCounter(Graphics2D g, int drawX, int drawY) {
        int badgeX = drawX + width - 7;
        int badgeY = drawY - 7;
        g.setColor(new Color(25, 28, 45));
        g.fillOval(badgeX, badgeY, 18, 18);
        g.setColor(Color.WHITE);
        g.drawOval(badgeX, badgeY, 18, 18);
        g.drawString(String.valueOf(cell.getRemainingEnemies()), badgeX + 5, badgeY + 14);
    }
}

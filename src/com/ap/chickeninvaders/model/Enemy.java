package com.ap.chickeninvaders.model;

import java.awt.*;

public class Enemy {
    private double x;
    private double y;
    private final int width = 42;
    private final int height = 34;
    private final Cell cell;
    private final int maxHealth;
    private int health;
    private boolean settled;
    private double zigzagPhase;
    private double fastOffset;
    private int fastDirection = 1;
    private int hitFlashFrames;

    public Enemy(Cell cell, int level) {
        this(cell, cell.getX(), cell.getY(), level, true);
    }

    public Enemy(Cell cell, double spawnX, double spawnY, int level) {
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

    public void update(double formationSpeed) {
        if (hitFlashFrames > 0) {
            hitFlashFrames--;
        }

        if (!settled) {
            moveTowardCell();
            return;
        }

        x = cell.getX();
        y = cell.getY();

        if (getType() == EnemyType.FAST) {
            fastOffset += fastDirection * Math.max(2.0, formationSpeed * 2.0);
            if (Math.abs(fastOffset) >= 12) {
                fastOffset = Math.copySign(12, fastOffset);
                fastDirection *= -1;
            }
            x += fastOffset;
        } else if (getType() == EnemyType.ZIGZAG) {
            zigzagPhase += 0.18;
            y += Math.sin(zigzagPhase) * 8;
        }
    }

    private void moveTowardCell() {
        double targetX = cell.getX();
        double targetY = cell.getY();
        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.hypot(dx, dy);
        double spawnSpeed = 6.0;

        if (distance <= spawnSpeed) {
            x = targetX;
            y = targetY;
            settled = true;
            return;
        }

        x += dx / distance * spawnSpeed;
        y += dy / distance * spawnSpeed;
    }

    public boolean damage(int amount) {
        health = Math.max(0, health - amount);
        hitFlashFrames = 5;
        return health == 0;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) Math.round(x), (int) Math.round(y), width, height);
    }

    public int centerX() {
        return (int) Math.round(x) + width / 2;
    }

    public int centerY() {
        return (int) Math.round(y) + height / 2;
    }

    public int getScore() {
        return getType().getScore();
    }

    public EnemyType getType() {
        return cell.getEnemyType();
    }

    public Cell getCell() {
        return cell;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isSettled() {
        return settled;
    }

    public void draw(Graphics2D g) {
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

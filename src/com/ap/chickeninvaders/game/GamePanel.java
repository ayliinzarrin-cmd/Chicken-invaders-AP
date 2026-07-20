package com.ap.chickeninvaders.game;

import com.ap.chickeninvaders.GameMain;
import com.ap.chickeninvaders.model.Bullet;
import com.ap.chickeninvaders.model.Boss;
import com.ap.chickeninvaders.model.Cell;
import com.ap.chickeninvaders.model.Egg;
import com.ap.chickeninvaders.model.EchoPlane;
import com.ap.chickeninvaders.model.Enemy;
import com.ap.chickeninvaders.model.EnemyType;
import com.ap.chickeninvaders.model.Explosion;
import com.ap.chickeninvaders.model.Plane;
import com.ap.chickeninvaders.model.PlayerSnapshot;
import com.ap.chickeninvaders.model.PowerUp;
import com.ap.chickeninvaders.model.PowerUpType;
import com.ap.chickeninvaders.model.User;
import com.ap.chickeninvaders.sound.SoundManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private static final long ECHO_MEMORY_MS = 5000;
    private static final long ECHO_COOLDOWN_MS = 15000;

    private final GameMain app;
    private final User user;
    private final SoundManager soundManager;
    private final Timer timer = new Timer(16, this);
    private final boolean[] keys = new boolean[256];
    private final Plane plane = new Plane(380, 500);
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<Cell> cells = new ArrayList<>();
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Egg> eggs = new ArrayList<>();
    private final List<PowerUp> powerUps = new ArrayList<>();
    private final List<Explosion> explosions = new ArrayList<>();
    private final Deque<PlayerSnapshot> echoHistory = new ArrayDeque<>();
    private final Random random = new Random();
    private EchoPlane echoPlane;
    private long echoReadyAt;
    private Boss boss;
    private int enemyDirection = 1;
    private int score;
    private int level = 1;
    private long lastEggTime;
    private long lastShooterTime;
    private long freezeUntil;
    private GameState state = GameState.RUNNING;
    private boolean saved;

    public GamePanel(GameMain app, User user, SoundManager soundManager) {
        this.app = app;
        this.user = user;
        this.soundManager = soundManager;
        setFocusable(true);
        setBackground(Color.BLACK);
        addKeyListener(this);
        startLevel(1);
        timer.start();
    }

    private void startLevel(int nextLevel) {
        level = nextLevel;
        cells.clear();
        enemies.clear();
        bullets.clear();
        eggs.clear();
        powerUps.clear();
        explosions.clear();
        boss = null;
        enemyDirection = 1;
        lastEggTime = System.currentTimeMillis();
        lastShooterTime = lastEggTime;

        if (level == 4 || level == 8) {
            boss = Boss.create(level);
            return;
        }

        int startX = 95;
        int startY = 85;
        int gapX = 72;
        int gapY = 48;
        int enemyCount = enemyCountForLevel();

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 8; col++) {
                Cell cell = new Cell(
                        startX + col * gapX,
                        startY + row * gapY,
                        enemyTypeFor(row, col),
                        enemyCount
                );
                cells.add(cell);
                enemies.add(Enemy.create(cell, level));
            }
        }
    }

    private EnemyType enemyTypeFor(int row, int col) {
        return switch (level) {
            case 1 -> EnemyType.NORMAL;
            case 2 -> (row + col) % 3 == 0 ? EnemyType.FAST : EnemyType.NORMAL;
            case 3 -> (row + col) % 2 == 0 ? EnemyType.ZIGZAG : EnemyType.NORMAL;
            case 5 -> (row + col) % 2 == 0 ? EnemyType.SHOOTER : EnemyType.FAST;
            case 6 -> (row + col) % 2 == 0 ? EnemyType.ZIGZAG : EnemyType.SHOOTER;
            case 7 -> {
                EnemyType[] all = {EnemyType.NORMAL, EnemyType.FAST, EnemyType.ZIGZAG, EnemyType.SHOOTER};
                yield all[(row + col) % all.length];
            }
            default -> EnemyType.NORMAL;
        };
    }

    private int enemyCountForLevel() {
        return switch (level) {
            case 1, 2 -> 2;
            case 3, 5 -> 3;
            case 6, 7 -> 4;
            default -> 1;
        };
    }

    private double formationSpeed() {
        return switch (level) {
            case 1 -> 1.0;
            case 2 -> 1.5;
            case 3 -> 2.0;
            case 5 -> 2.5;
            case 6 -> 3.0;
            case 7 -> 3.5;
            default -> 1.0;
        };
    }

    private int verticalStep() {
        return switch (level) {
            case 1, 2 -> 20;
            case 3, 5 -> 25;
            case 6, 7 -> 30;
            default -> 20;
        };
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (state == GameState.RUNNING) {
            updateGame();
        }
        repaint();
    }

    private void updateGame() {
        long now = System.currentTimeMillis();
        plane.update(keys, getWidth(), getHeight());
        boolean playerShot = false;

        if (isPressed(KeyEvent.VK_SPACE)) {
            List<Bullet> newBullets = plane.tryShoot(now);
            if (!newBullets.isEmpty()) {
                bullets.addAll(newBullets);
                soundManager.playShot();
                playerShot = true;
            }
        }

        recordEchoSnapshot(now, playerShot);
        updateEcho(now);

        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.update();
            if (bullet.isOutOfScreen()) {
                iterator.remove();
            }
        }

        boolean frozen = now < freezeUntil;

        if (boss == null) {
            if (!frozen) {
                updateEnemies();
            }
            checkBulletEnemyCollisions();
        } else {
            if (!frozen) {
                updateBoss();
            }
            checkBulletBossCollisions();
        }
        updateEggs(frozen);
        updatePowerUps(now);
        updateExplosions();
        checkEggPlaneCollisions();
    }

    private void recordEchoSnapshot(long now, boolean shot) {
        // Keep only the rolling five-second window used by Echo Squadron.
        echoHistory.addLast(new PlayerSnapshot(
                now,
                plane.getX(),
                plane.getY(),
                plane.getFireCount(),
                shot
        ));

        while (!echoHistory.isEmpty()
                && now - echoHistory.peekFirst().getCapturedAt() > ECHO_MEMORY_MS) {
            echoHistory.removeFirst();
        }
    }

    private void updateEcho(long now) {
        if (echoPlane == null) {
            return;
        }

        bullets.addAll(echoPlane.update(now));
        if (!echoPlane.isActive()) {
            echoPlane = null;
        }
    }

    private void activateEcho(long now) {
        if (echoPlane != null || now < echoReadyAt || echoHistory.size() < 2) {
            return;
        }

        long recordedTime = echoHistory.peekLast().getCapturedAt()
                - echoHistory.peekFirst().getCapturedAt();
        if (recordedTime < 1000) {
            return;
        }

        echoPlane = new EchoPlane(new ArrayList<>(echoHistory), now);
        echoReadyAt = now + ECHO_COOLDOWN_MS;
        soundManager.playEcho();
    }

    private void updateBoss() {
        boss.update(getWidth());
        eggs.addAll(boss.tryShoot(System.currentTimeMillis()));
    }

    private void updateEnemies() {
        double speed = formationSpeed();
        double dx = enemyDirection * speed;
        boolean hitEdge = false;
        int panelWidth = getWidth() > 0 ? getWidth() : 800;

        for (Cell cell : cells) {
            double nextX = cell.getX() + dx;
            if (nextX < 22 || nextX + 54 > panelWidth - 10) {
                hitEdge = true;
                break;
            }
        }

        if (hitEdge) {
            enemyDirection *= -1;
            for (Cell cell : cells) {
                cell.moveDown(verticalStep());
            }
        } else {
            for (Cell cell : cells) {
                cell.move(dx);
            }
        }

        for (Enemy enemy : enemies) {
            enemy.update(speed);
        }

        long now = System.currentTimeMillis();
        long eggDelay = switch (level) {
            case 1 -> 3000;
            case 2 -> 2000;
            case 3 -> 1500;
            case 5 -> 1000;
            case 6 -> 800;
            case 7 -> 700;
            default -> 1200;
        };
        if (!enemies.isEmpty() && now - lastEggTime > eggDelay) {
            Enemy enemy = randomSettledEnemy();
            if (enemy != null) {
                eggs.add(new Egg(enemy.centerX(), enemy.centerY()));
            }
            lastEggTime = now;
        }

        if (now - lastShooterTime > 1300) {
            for (Enemy enemy : enemies) {
                if (enemy.isSettled() && enemy.getType() == EnemyType.SHOOTER
                        && random.nextDouble() < 0.08) {
                    double horizontalSpeed = plane.getBounds().getCenterX() >= enemy.centerX() ? 5 : -5;
                    eggs.add(new Egg(
                            enemy.centerX(),
                            enemy.centerY(),
                            horizontalSpeed,
                            0
                    ));
                }
            }
            lastShooterTime = now;
        }
    }

    private Enemy randomSettledEnemy() {
        if (enemies.isEmpty()) {
            return null;
        }

        int start = random.nextInt(enemies.size());
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get((start + i) % enemies.size());
            if (enemy.isSettled()) {
                return enemy;
            }
        }
        return null;
    }

    private void updateEggs(boolean frozen) {
        Iterator<Egg> iterator = eggs.iterator();
        while (iterator.hasNext()) {
            Egg egg = iterator.next();
            if (!frozen) {
                egg.update();
            }
            if (egg.isOutOfScreen(getHeight())) {
                iterator.remove();
            }
        }
    }

    private void updatePowerUps(long now) {
        Iterator<PowerUp> iterator = powerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            powerUp.update();
            if (powerUp.getBounds().intersects(plane.getBounds())) {
                if (powerUp.getType() == PowerUpType.FREEZE_BOMB) {
                    freezeUntil = now + 3000;
                }
                plane.applyPowerUp(powerUp.getType(), now);
                iterator.remove();
            } else if (powerUp.isOutOfScreen(getHeight())) {
                iterator.remove();
            }
        }
    }

    private void updateExplosions() {
        Iterator<Explosion> iterator = explosions.iterator();
        while (iterator.hasNext()) {
            Explosion explosion = iterator.next();
            explosion.update();
            if (explosion.isDone()) {
                iterator.remove();
            }
        }
    }

    private void checkBulletEnemyCollisions() {
        for (int bulletIndex = bullets.size() - 1; bulletIndex >= 0; bulletIndex--) {
            Bullet bullet = bullets.get(bulletIndex);

            for (int enemyIndex = enemies.size() - 1; enemyIndex >= 0; enemyIndex--) {
                Enemy enemy = enemies.get(enemyIndex);
                if (bullet.getBounds().intersects(enemy.getBounds())) {
                    bullets.remove(bulletIndex);

                    if (enemy.damage(1)) {
                        enemies.remove(enemyIndex);
                        Cell cell = enemy.getCell();
                        cell.registerKill();
                        score += enemy.getScore();
                        explosions.add(new Explosion(enemy.centerX(), enemy.centerY()));
                        soundManager.playExplosion();
                        maybeDropPowerUp(enemy.centerX(), enemy.centerY());

                        if (cell.needsReplacement()) {
                            enemies.add(createReplacement(cell));
                        } else if (enemies.isEmpty()) {
                            finishLevel();
                        }
                    }
                    return;
                }
            }
        }
    }

    private Enemy createReplacement(Cell cell) {
        int panelWidth = getWidth() > 0 ? getWidth() : 800;
        double spawnX = random.nextBoolean() ? -55 : panelWidth + 15;
        return Enemy.createReplacement(cell, spawnX, 15, level);
    }

    private void checkBulletBossCollisions() {
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            if (bullet.getBounds().intersects(boss.getBounds())) {
                bulletIterator.remove();
                boss.damage(1);
                if (boss.isDead()) {
                    Rectangle bounds = boss.getBounds();
                    explosions.add(new Explosion(bounds.getCenterX(), bounds.getCenterY()));
                    soundManager.playExplosion();
                    if (boss.getLevel() == 4) {
                        score += 500;
                        JOptionPane.showMessageDialog(this, "Boss 4 defeated! Level 5 starts.");
                        startLevel(5);
                    } else {
                        score += 1000;
                        JOptionPane.showMessageDialog(this, "Final boss defeated! You win!");
                        endGame("WIN");
                    }
                }
                return;
            }
        }
    }

    private void checkEggPlaneCollisions() {
        Iterator<Egg> iterator = eggs.iterator();
        while (iterator.hasNext()) {
            Egg egg = iterator.next();
            if (egg.getBounds().intersects(plane.getBounds())) {
                iterator.remove();
                explosions.add(new Explosion(plane.getBounds().getCenterX(), plane.getBounds().getCenterY()));
                soundManager.playExplosion();
                if (!plane.isShielded(System.currentTimeMillis())) {
                    plane.loseLife();
                }
                if (plane.getLives() <= 0) {
                    endGame("GAME_OVER");
                }
                return;
            }
        }
    }

    private void maybeDropPowerUp(double x, double y) {
        if (random.nextDouble() > 0.20) {
            return;
        }
        PowerUpType[] types = PowerUpType.values();
        powerUps.add(new PowerUp(x, y, types[random.nextInt(types.length)]));
    }

    private void finishLevel() {
        score += 200;
        if (level < 3) {
            JOptionPane.showMessageDialog(this, "Level " + level + " cleared!");
            startLevel(level + 1);
        } else if (level == 3) {
            JOptionPane.showMessageDialog(this, "Level 3 cleared! Boss fight starts.");
            startLevel(4);
        } else if (level < 7) {
            JOptionPane.showMessageDialog(this, "Level " + level + " cleared!");
            startLevel(level + 1);
        } else {
            JOptionPane.showMessageDialog(this, "Level 7 cleared! Final boss starts.");
            startLevel(8);
        }
    }

    private void endGame(String status) {
        if (saved) {
            return;
        }
        saved = true;
        state = GameState.GAME_OVER;
        timer.stop();
        soundManager.playEnd("WIN".equals(status));
        JOptionPane.showMessageDialog(this, status + "\nScore: " + score);
        app.finishGame(score, level, status);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBackground(g2);
        drawHud(g2);
        if (echoPlane != null) {
            echoPlane.draw(g2);
        }
        plane.draw(g2);
        for (Bullet bullet : bullets) {
            bullet.draw(g2);
        }
        for (PowerUp powerUp : powerUps) {
            powerUp.draw(g2);
        }
        for (Enemy enemy : enemies) {
            enemy.draw(g2);
        }
        if (boss != null) {
            boss.draw(g2);
        }
        for (Egg egg : eggs) {
            egg.draw(g2);
        }
        for (Explosion explosion : explosions) {
            explosion.draw(g2);
        }

        if (state == GameState.PAUSED) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36f));
            g2.drawString("PAUSED", 325, 300);
        }
    }

    private void drawBackground(Graphics2D g2) {
        g2.setColor(new Color(8, 10, 26));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(new Color(180, 210, 255, 120));
        for (int i = 0; i < 70; i++) {
            int x = (i * 97) % 800;
            int y = (i * 53) % 600;
            g2.fillRect(x, y, 2, 2);
        }
    }

    private void drawHud(Graphics2D g2) {
        long now = System.currentTimeMillis();
        g2.setColor(new Color(255, 255, 255, 220));
        g2.fillRoundRect(12, 10, 760, 58, 8, 8);
        g2.setColor(Color.BLACK);
        g2.drawString("User: " + user.getUsername(), 24, 30);
        g2.drawString("Score: " + score, 150, 30);
        g2.drawString("Level: " + level, 240, 30);
        g2.drawString("Lives: " + plane.getLives(), 310, 30);
        g2.drawString("Fire: " + plane.getFireCount(), 390, 30);
        String targetText = boss == null ? String.valueOf(remainingEnemyCount()) : "BOSS";
        g2.drawString("Targets: " + targetText, 24, 48);
        g2.drawString("Rapid: " + plane.rapidSecondsLeft(now), 130, 48);
        g2.drawString("Shield: " + plane.shieldSecondsLeft(now), 220, 48);
        g2.drawString("Freeze: " + Math.max(0, (freezeUntil - now + 999) / 1000), 320, 48);
        g2.drawString("Echo: " + echoStatus(now), 430, 48);
        g2.drawString("Goal: clear every enemy and survive.", 24, 64);
    }

    private String echoStatus(long now) {
        if (echoPlane != null) {
            return "ACTIVE";
        }
        if (echoHistory.size() < 2
                || echoHistory.peekLast().getCapturedAt() - echoHistory.peekFirst().getCapturedAt() < 1000) {
            return "RECORDING";
        }
        long seconds = Math.max(0, (echoReadyAt - now + 999) / 1000);
        return seconds == 0 ? "READY [E]" : seconds + "s";
    }

    private int remainingEnemyCount() {
        int total = 0;
        for (Cell cell : cells) {
            total += cell.getRemainingEnemies();
        }
        return total;
    }

    private boolean isPressed(int keyCode) {
        return keyCode >= 0 && keyCode < keys.length && keys[keyCode];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code >= 0 && code < keys.length) {
            keys[code] = true;
        }

        if (code == KeyEvent.VK_P) {
            if (state == GameState.RUNNING) {
                state = GameState.PAUSED;
            } else if (state == GameState.PAUSED) {
                state = GameState.RUNNING;
            }
        }

        if (code == KeyEvent.VK_E && state == GameState.RUNNING) {
            activateEcho(System.currentTimeMillis());
        }

        if (code == KeyEvent.VK_ESCAPE) {
            endGame("ESCAPE");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code >= 0 && code < keys.length) {
            keys[code] = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}

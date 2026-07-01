package com.ap.chickeninvaders.game;

import com.ap.chickeninvaders.GameMain;
import com.ap.chickeninvaders.model.Bullet;
import com.ap.chickeninvaders.model.Plane;
import com.ap.chickeninvaders.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private final GameMain app;
    private final User user;
    private final Timer timer = new Timer(16, this);
    private final boolean[] keys = new boolean[256];
    private final Plane plane = new Plane(380, 500);
    private final List<Bullet> bullets = new ArrayList<>();
    private boolean paused;

    public GamePanel(GameMain app, User user) {
        this.app = app;
        this.user = user;
        setFocusable(true);
        setBackground(Color.BLACK);
        addKeyListener(this);
        timer.start();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!paused) {
            updateGame();
        }
        repaint();
    }

    private void updateGame() {
        plane.update(keys, getWidth(), getHeight());

        if (isPressed(KeyEvent.VK_SPACE)) {
            bullets.addAll(plane.tryShoot(System.currentTimeMillis()));
        }

        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.update();
            if (bullet.isOutOfScreen()) {
                iterator.remove();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBackground(g2);
        drawHud(g2);
        plane.draw(g2);
        for (Bullet bullet : bullets) {
            bullet.draw(g2);
        }

        if (paused) {
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
        g2.setColor(new Color(255, 255, 255, 220));
        g2.fillRoundRect(12, 10, 360, 44, 8, 8);
        g2.setColor(Color.BLACK);
        g2.drawString("User: " + user.getUsername(), 24, 30);
        g2.drawString("Day 4: Movement + Shooting", 160, 30);
        g2.drawString("P: Pause   Esc: Menu", 24, 48);
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
            paused = !paused;
        }

        if (code == KeyEvent.VK_ESCAPE) {
            timer.stop();
            app.showScreen("menu");
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


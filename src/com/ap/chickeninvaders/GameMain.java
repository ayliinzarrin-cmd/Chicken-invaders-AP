package com.ap.chickeninvaders;

import com.ap.chickeninvaders.ui.LoginPanel;
import com.ap.chickeninvaders.ui.MainMenuPanel;
import com.ap.chickeninvaders.ui.HighScorePanel;
import com.ap.chickeninvaders.ui.HowToPlayPanel;
import com.ap.chickeninvaders.ui.RegisterPanel;
import com.ap.chickeninvaders.ui.SettingsPanel;
import com.ap.chickeninvaders.db.DatabaseManager;
import com.ap.chickeninvaders.game.GamePanel;
import com.ap.chickeninvaders.model.User;
import com.ap.chickeninvaders.sound.SoundManager;

import javax.swing.*;
import java.awt.*;

public class GameMain extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel root = new JPanel(cardLayout);
    private final DatabaseManager databaseManager = new DatabaseManager();
    private final SoundManager soundManager = new SoundManager();
    private final SettingsPanel settingsPanel = new SettingsPanel(this);
    private final HighScorePanel highScorePanel = new HighScorePanel(this);
    private User currentUser;

    public GameMain() {
        super("Chicken Invaders AP");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setContentPane(root);

        addScreen("menu", new MainMenuPanel(this));
        addScreen("login", new LoginPanel(this));
        addScreen("register", new RegisterPanel(this));
        addScreen("scores", highScorePanel);
        addScreen("settings", settingsPanel);
        addScreen("how", new HowToPlayPanel(this));
    }

    public void addScreen(String name, JPanel panel) {
        root.add(panel, name);
    }

    public void showScreen(String name) {
        if ("settings".equals(name)) {
            settingsPanel.refresh();
        }
        if ("scores".equals(name)) {
            highScorePanel.refreshScores();
        }
        cardLayout.show(root, name);
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        soundManager.setUser(user);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void startNewGame() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please login or register first.");
            showScreen("login");
            return;
        }
        GamePanel gamePanel = new GamePanel(this, currentUser, soundManager);
        root.add(gamePanel, "game");
        showScreen("game");
        gamePanel.requestFocusInWindow();
    }

    public void finishGame(int score, int levelReached, String status) {
        if (currentUser != null) {
            databaseManager.saveGameRecord(currentUser, score, levelReached, status);
            currentUser = databaseManager.findUser(currentUser.getUsername());
            soundManager.setUser(currentUser);
        }
        showScreen("menu");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameMain app = new GameMain();
            app.setVisible(true);
        });
    }
}

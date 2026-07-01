package com.ap.chickeninvaders;

import com.ap.chickeninvaders.ui.LoginPanel;
import com.ap.chickeninvaders.ui.MainMenuPanel;
import com.ap.chickeninvaders.ui.PlaceholderPanel;
import com.ap.chickeninvaders.ui.RegisterPanel;
import com.ap.chickeninvaders.db.DatabaseManager;
import com.ap.chickeninvaders.game.GamePanel;
import com.ap.chickeninvaders.model.User;

import javax.swing.*;
import java.awt.*;

public class GameMain extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel root = new JPanel(cardLayout);
    private final DatabaseManager databaseManager = new DatabaseManager();
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
        addScreen("scores", new PlaceholderPanel(this, "High Scores will be added later."));
        addScreen("settings", new PlaceholderPanel(this, "Sound Settings will be added later."));
        addScreen("how", new PlaceholderPanel(this, "How to Play will be completed later."));
    }

    public void addScreen(String name, JPanel panel) {
        root.add(panel, name);
    }

    public void showScreen(String name) {
        cardLayout.show(root, name);
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
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
        GamePanel gamePanel = new GamePanel(this, currentUser);
        root.add(gamePanel, "game");
        showScreen("game");
        gamePanel.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameMain app = new GameMain();
            app.setVisible(true);
        });
    }
}

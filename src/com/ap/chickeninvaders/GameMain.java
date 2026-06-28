package com.ap.chickeninvaders;

import com.ap.chickeninvaders.ui.LoginPanel;
import com.ap.chickeninvaders.ui.MainMenuPanel;
import com.ap.chickeninvaders.ui.PlaceholderPanel;
import com.ap.chickeninvaders.ui.RegisterPanel;

import javax.swing.*;
import java.awt.*;

public class GameMain extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel root = new JPanel(cardLayout);
    private String currentUsername;

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

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void startNewGame() {
        if (currentUsername == null || currentUsername.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please login or register first.");
            showScreen("login");
            return;
        }
        JOptionPane.showMessageDialog(this, "GamePanel will be added in the next days.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameMain app = new GameMain();
            app.setVisible(true);
        });
    }
}


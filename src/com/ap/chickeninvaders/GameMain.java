package com.ap.chickeninvaders;

import javax.swing.*;
import java.awt.*;

public class GameMain extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel root = new JPanel(cardLayout);

    public GameMain() {
        super("Chicken Invaders AP");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setContentPane(root);

        root.add(createMainMenu(), "menu");
        root.add(createPlaceholderPanel("Login/Register will be added later."), "login");
        root.add(createPlaceholderPanel("High Scores will be added later."), "scores");
        root.add(createPlaceholderPanel("Settings will be added later."), "settings");
        root.add(createPlaceholderPanel("How to Play will be added later."), "how");
    }

    private JPanel createMainMenu() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(12, 14, 34));

        JPanel buttons = new JPanel(new GridLayout(0, 1, 10, 10));
        buttons.setOpaque(false);
        buttons.setPreferredSize(new Dimension(260, 330));

        JLabel title = new JLabel("Chicken Invaders", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 30f));

        JButton newGame = new JButton("New Game");
        JButton highScores = new JButton("High Scores");
        JButton settings = new JButton("Settings");
        JButton howToPlay = new JButton("How to Play");
        JButton exit = new JButton("Exit");

        newGame.addActionListener(e -> showScreen("login"));
        highScores.addActionListener(e -> showScreen("scores"));
        settings.addActionListener(e -> showScreen("settings"));
        howToPlay.addActionListener(e -> showScreen("how"));
        exit.addActionListener(e -> System.exit(0));

        buttons.add(title);
        buttons.add(newGame);
        buttons.add(highScores);
        buttons.add(settings);
        buttons.add(howToPlay);
        buttons.add(exit);

        panel.add(buttons);
        return panel;
    }

    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(10, 14, 30));

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 22f));

        JButton back = new JButton("Back to Menu");
        back.addActionListener(e -> showScreen("menu"));

        panel.add(label, BorderLayout.CENTER);
        panel.add(back, BorderLayout.SOUTH);
        return panel;
    }

    private void showScreen(String name) {
        cardLayout.show(root, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameMain app = new GameMain();
            app.setVisible(true);
        });
    }
}


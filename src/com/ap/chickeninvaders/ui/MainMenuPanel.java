package com.ap.chickeninvaders.ui;

import com.ap.chickeninvaders.GameMain;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(GameMain app) {
        setLayout(new GridBagLayout());
        setBackground(new Color(12, 14, 34));

        JPanel buttons = new JPanel(new GridLayout(0, 1, 10, 10));
        buttons.setOpaque(false);
        buttons.setPreferredSize(new Dimension(380, 390));

        JLabel title = new JLabel("Chicken Invaders: Echo Squadron", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));

        JButton newGame = new JButton("New Game");
        JButton login = new JButton("Login");
        JButton register = new JButton("Register");
        JButton highScores = new JButton("High Scores");
        JButton settings = new JButton("Settings");
        JButton howToPlay = new JButton("How to Play");
        JButton exit = new JButton("Exit");

        newGame.addActionListener(e -> app.startNewGame());
        login.addActionListener(e -> app.showScreen("login"));
        register.addActionListener(e -> app.showScreen("register"));
        highScores.addActionListener(e -> app.showScreen("scores"));
        settings.addActionListener(e -> app.showScreen("settings"));
        howToPlay.addActionListener(e -> app.showScreen("how"));
        exit.addActionListener(e -> System.exit(0));

        buttons.add(title);
        buttons.add(newGame);
        buttons.add(login);
        buttons.add(register);
        buttons.add(highScores);
        buttons.add(settings);
        buttons.add(howToPlay);
        buttons.add(exit);

        add(buttons);
    }
}


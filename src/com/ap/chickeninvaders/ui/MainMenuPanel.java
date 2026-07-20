package com.ap.chickeninvaders.ui;

import com.ap.chickeninvaders.GameMain;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(GameMain app) {
        setLayout(new GridBagLayout());
        setBackground(UiStyle.BACKGROUND);

        JPanel buttons = new JPanel(new GridLayout(0, 1, 10, 10));
        buttons.setOpaque(false);
        buttons.setPreferredSize(new Dimension(380, 390));

        JLabel title = new JLabel("Chicken Invaders: Echo Squadron", SwingConstants.CENTER);
        title.setForeground(UiStyle.ECHO);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));

        JButton newGame = new JButton("New Game");
        JButton login = new JButton("Login");
        JButton register = new JButton("Register");
        JButton highScores = new JButton("High Scores");
        JButton settings = new JButton("Settings");
        JButton howToPlay = new JButton("How to Play");
        JButton exit = new JButton("Exit");

        UiStyle.styleButton(newGame, true);
        UiStyle.styleButton(login, false);
        UiStyle.styleButton(register, false);
        UiStyle.styleButton(highScores, false);
        UiStyle.styleButton(settings, false);
        UiStyle.styleButton(howToPlay, false);
        UiStyle.styleButton(exit, false);

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


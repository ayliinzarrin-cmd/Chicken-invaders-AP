package com.ap.chickeninvaders.ui;

import com.ap.chickeninvaders.GameMain;

import javax.swing.*;
import java.awt.*;

public class PlaceholderPanel extends JPanel {
    public PlaceholderPanel(GameMain app, String message) {
        setLayout(new BorderLayout());
        setBackground(new Color(10, 14, 30));

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 22f));

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> app.showScreen("menu"));

        add(label, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }
}


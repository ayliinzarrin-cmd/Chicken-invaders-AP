package com.ap.chickeninvaders.ui;

import com.ap.chickeninvaders.GameMain;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    public RegisterPanel(GameMain app) {
        setLayout(new GridBagLayout());
        setBackground(UiStyle.BACKGROUND);

        JPanel form = new JPanel(new GridLayout(0, 1, 8, 8));
        form.setPreferredSize(new Dimension(320, 340));
        UiStyle.stylePanel(form);

        JLabel title = new JLabel("Register", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setForeground(UiStyle.ECHO);

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField repeatPasswordField = new JPasswordField();
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        UiStyle.styleField(usernameField);
        UiStyle.styleField(passwordField);
        UiStyle.styleField(repeatPasswordField);
        UiStyle.styleButton(registerButton, true);
        UiStyle.styleButton(backButton, false);

        form.add(title);
        form.add(UiStyle.label("Username"));
        form.add(usernameField);
        form.add(UiStyle.label("Password"));
        form.add(passwordField);
        form.add(UiStyle.label("Repeat Password"));
        form.add(repeatPasswordField);
        form.add(registerButton);
        form.add(backButton);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String repeatPassword = new String(repeatPasswordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty.");
                return;
            }
            if (!password.equals(repeatPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }

            boolean registered = app.getDatabaseManager().register(username, password);
            if (!registered) {
                JOptionPane.showMessageDialog(this, "This username already exists or input is invalid.");
                return;
            }

            JOptionPane.showMessageDialog(this, "Registered successfully. Now login.");
            app.showScreen("login");
        });

        backButton.addActionListener(e -> app.showScreen("menu"));
        add(form);
    }
}

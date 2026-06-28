package com.ap.chickeninvaders.ui;

import com.ap.chickeninvaders.GameMain;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    public RegisterPanel(GameMain app) {
        setLayout(new GridBagLayout());
        setBackground(new Color(10, 14, 30));

        JPanel form = new JPanel(new GridLayout(0, 1, 8, 8));
        form.setPreferredSize(new Dimension(280, 250));

        JLabel title = new JLabel("Register", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField repeatPasswordField = new JPasswordField();
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        form.add(title);
        form.add(new JLabel("Username"));
        form.add(usernameField);
        form.add(new JLabel("Password"));
        form.add(passwordField);
        form.add(new JLabel("Repeat Password"));
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

            app.setCurrentUsername(username);
            JOptionPane.showMessageDialog(this, "Registered as " + username + ".");
            app.showScreen("menu");
        });

        backButton.addActionListener(e -> app.showScreen("menu"));
        add(form);
    }
}


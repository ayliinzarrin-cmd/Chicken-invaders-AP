package com.ap.chickeninvaders.ui;

import com.ap.chickeninvaders.GameMain;
import com.ap.chickeninvaders.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    public LoginPanel(GameMain app) {
        setLayout(new GridBagLayout());
        setBackground(new Color(10, 14, 30));

        JPanel form = new JPanel(new GridLayout(0, 1, 8, 8));
        form.setPreferredSize(new Dimension(280, 230));

        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        form.add(title);
        form.add(new JLabel("Username"));
        form.add(usernameField);
        form.add(new JLabel("Password"));
        form.add(passwordField);
        form.add(loginButton);
        form.add(backButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter username and password.");
                return;
            }
            User user = app.getDatabaseManager().login(username, password);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Wrong username or password.");
                return;
            }

            app.setCurrentUser(user);
            JOptionPane.showMessageDialog(this, "Logged in as " + user.getUsername() + ".");
            app.showScreen("menu");
        });

        backButton.addActionListener(e -> app.showScreen("menu"));
        add(form);
    }
}

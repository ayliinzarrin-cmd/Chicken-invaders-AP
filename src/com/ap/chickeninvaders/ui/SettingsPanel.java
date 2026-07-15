package com.ap.chickeninvaders.ui;

import com.ap.chickeninvaders.GameMain;
import com.ap.chickeninvaders.model.User;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    private final GameMain app;
    private final JCheckBox musicBox = new JCheckBox("Background Music");
    private final JCheckBox shotBox = new JCheckBox("Shot Sound");
    private final JCheckBox explosionBox = new JCheckBox("Explosion Sound");
    private final JCheckBox endBox = new JCheckBox("Game Over / Win Sound");

    public SettingsPanel(GameMain app) {
        this.app = app;
        setLayout(new GridBagLayout());
        setBackground(new Color(10, 14, 30));

        JPanel form = new JPanel(new GridLayout(0, 1, 10, 10));
        form.setPreferredSize(new Dimension(320, 280));

        JLabel title = new JLabel("Sound Settings", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));

        JButton saveButton = new JButton("Save Settings");
        JButton testButton = new JButton("Test Sound");
        JButton backButton = new JButton("Back");

        form.add(title);
        form.add(musicBox);
        form.add(shotBox);
        form.add(explosionBox);
        form.add(endBox);
        form.add(saveButton);
        form.add(testButton);
        form.add(backButton);

        saveButton.addActionListener(e -> saveSettings());
        testButton.addActionListener(e -> app.getSoundManager().playMenuBeep());
        backButton.addActionListener(e -> app.showScreen("menu"));

        add(form);
    }

    public void refresh() {
        User user = app.getCurrentUser();
        if (user == null) {
            musicBox.setSelected(true);
            shotBox.setSelected(true);
            explosionBox.setSelected(true);
            endBox.setSelected(true);
            return;
        }

        musicBox.setSelected(user.isMusicOn());
        shotBox.setSelected(user.isShotSoundOn());
        explosionBox.setSelected(user.isExplosionSoundOn());
        endBox.setSelected(user.isEndSoundOn());
    }

    private void saveSettings() {
        User user = app.getCurrentUser();
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Please login first.");
            app.showScreen("login");
            return;
        }

        user.setSoundSettings(
                musicBox.isSelected(),
                shotBox.isSelected(),
                explosionBox.isSelected(),
                endBox.isSelected()
        );
        app.getDatabaseManager().updateUser(user);
        app.getSoundManager().setUser(user);
        JOptionPane.showMessageDialog(this, "Sound settings saved.");
    }
}


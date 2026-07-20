package com.ap.chickeninvaders.ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public final class UiStyle {
    public static final Color BACKGROUND = new Color(15, 16, 22);
    public static final Color PANEL = new Color(29, 31, 40);
    public static final Color PANEL_LIGHT = new Color(49, 52, 65);
    public static final Color TEXT = new Color(244, 247, 255);
    public static final Color ACCENT = new Color(80, 225, 220);
    public static final Color ECHO = new Color(245, 95, 190);

    private UiStyle() {
    }

    public static void stylePanel(JPanel panel) {
        panel.setBackground(PANEL);
        Border line = BorderFactory.createLineBorder(new Color(85, 90, 110));
        Border padding = BorderFactory.createEmptyBorder(14, 18, 14, 18);
        panel.setBorder(BorderFactory.createCompoundBorder(line, padding));
    }

    public static void styleButton(JButton button, boolean primary) {
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBackground(primary ? ACCENT : PANEL_LIGHT);
        button.setForeground(primary ? new Color(10, 25, 28) : TEXT);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 14f));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primary ? ACCENT : new Color(95, 100, 120)),
                BorderFactory.createEmptyBorder(7, 14, 7, 14)
        ));
    }

    public static void styleField(JTextField field) {
        field.setBackground(new Color(245, 247, 252));
        field.setForeground(new Color(20, 22, 28));
        field.setCaretColor(new Color(20, 22, 28));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)
        ));
    }

    public static JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        return label;
    }
}

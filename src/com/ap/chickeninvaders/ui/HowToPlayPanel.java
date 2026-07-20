package com.ap.chickeninvaders.ui;

import com.ap.chickeninvaders.GameMain;

import javax.swing.*;
import java.awt.*;

public class HowToPlayPanel extends JPanel {
    public HowToPlayPanel(GameMain app) {
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(10, 14, 30));
        setBorder(BorderFactory.createEmptyBorder(24, 40, 24, 40));

        JLabel title = new JLabel("How to Play", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 28f));
        add(title, BorderLayout.NORTH);

        JEditorPane guide = new JEditorPane("text/html", guideText());
        guide.setEditable(false);
        guide.setFocusable(false);
        guide.setOpaque(false);
        guide.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        guide.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(guide);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(80, 90, 125)));
        scrollPane.getViewport().setBackground(new Color(18, 23, 45));
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> app.showScreen("menu"));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        bottom.add(backButton);
        add(bottom, BorderLayout.SOUTH);
    }

    private String guideText() {
        return "<html><body style='font-family:sans-serif; color:#eef2ff; background:#12172d; padding:16px;'>"
                + "<h2>Mission</h2>"
                + "<p>Destroy every enemy, survive all eight levels, and defeat both bosses.</p>"
                + "<h2>Controls</h2>"
                + "<table cellpadding='6'>"
                + "<tr><td><b>W / Arrow Up</b></td><td>Move up</td></tr>"
                + "<tr><td><b>S / Arrow Down</b></td><td>Move down</td></tr>"
                + "<tr><td><b>A / Arrow Left</b></td><td>Move left</td></tr>"
                + "<tr><td><b>D / Arrow Right</b></td><td>Move right</td></tr>"
                + "<tr><td><b>Space</b></td><td>Shoot</td></tr>"
                + "<tr><td><b>E</b></td><td>Activate Echo Squadron</td></tr>"
                + "<tr><td><b>P</b></td><td>Pause or resume</td></tr>"
                + "<tr><td><b>Esc</b></td><td>Finish the game and return to the menu</td></tr>"
                + "</table>"
                + "<h2>Echo Squadron</h2>"
                + "<p>The game remembers your last five seconds. Press E to create a holographic wingmate that repeats those movements and shots.</p>"
                + "<p>Echo bullets damage enemies. After activation, the ability needs 15 seconds to recharge.</p>"
                + "<h2>Power-Ups</h2>"
                + "<p><b>Add Fire:</b> adds another bullet to every shot.</p>"
                + "<p><b>Rapid Fire:</b> temporarily increases the shooting speed.</p>"
                + "<p><b>Extra Life:</b> adds one life, up to five.</p>"
                + "<p><b>Shield:</b> temporarily blocks damage.</p>"
                + "<p><b>Freeze Bomb:</b> freezes enemies and eggs for three seconds.</p>"
                + "<h2>Important</h2>"
                + "<p>Your lives continue between levels. Eggs remove one life unless the shield is active.</p>"
                + "<p>Enemies have health bars and may need several hits. The number badge shows how many enemies remain in that grid cell.</p>"
                + "<p>When a cell still has enemies left, a replacement flies in from a top corner and can be hit during its flight.</p>"
                + "</body></html>";
    }
}

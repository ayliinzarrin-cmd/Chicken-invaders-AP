package com.ap.chickeninvaders.ui;

import com.ap.chickeninvaders.GameMain;
import com.ap.chickeninvaders.model.GameRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HighScorePanel extends JPanel {
    private final GameMain app;
    private final DefaultTableModel tableModel;
    private final JLabel messageLabel = new JLabel(" ", SwingConstants.CENTER);

    public HighScorePanel(GameMain app) {
        this.app = app;
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(10, 14, 30));
        setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));

        JLabel title = new JLabel("High Scores", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 28f));
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"Rank", "Username", "Score", "Level", "Result", "Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(45);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(75);
        table.getColumnModel().getColumn(3).setPreferredWidth(55);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);
        table.getColumnModel().getColumn(5).setPreferredWidth(170);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back to Menu");
        refreshButton.addActionListener(e -> refreshScores());
        backButton.addActionListener(e -> app.showScreen("menu"));

        messageLabel.setForeground(new Color(220, 225, 240));
        JPanel bottom = new JPanel(new BorderLayout(10, 0));
        bottom.setOpaque(false);
        bottom.add(messageLabel, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        buttons.add(refreshButton);
        buttons.add(backButton);
        bottom.add(buttons, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);
    }

    public void refreshScores() {
        tableModel.setRowCount(0);
        List<GameRecord> records = app.getDatabaseManager().getHighScores(20);

        for (int i = 0; i < records.size(); i++) {
            GameRecord record = records.get(i);
            tableModel.addRow(new Object[]{
                    i + 1,
                    record.getUsername(),
                    record.getScore(),
                    record.getLevelReached(),
                    record.getStatus(),
                    record.getPlayedAt()
            });
        }

        if (records.isEmpty()) {
            messageLabel.setText("No finished games yet. Play a game to create the first score.");
        } else {
            messageLabel.setText("Best result for each player - top " + records.size());
        }
    }
}

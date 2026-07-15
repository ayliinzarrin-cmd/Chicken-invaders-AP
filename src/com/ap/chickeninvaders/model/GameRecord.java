package com.ap.chickeninvaders.model;

public class GameRecord {
    private final String username;
    private final int score;
    private final int levelReached;
    private final String status;
    private final String playedAt;

    public GameRecord(String username, int score, int levelReached, String status, String playedAt) {
        this.username = username;
        this.score = score;
        this.levelReached = levelReached;
        this.status = status;
        this.playedAt = playedAt;
    }

    public static GameRecord fromFileLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 5) {
            return null;
        }

        try {
            return new GameRecord(
                    parts[0],
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2]),
                    parts[3],
                    parts[4]
            );
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public int getLevelReached() {
        return levelReached;
    }

    public String getStatus() {
        return status;
    }

    public String getPlayedAt() {
        return playedAt;
    }
}

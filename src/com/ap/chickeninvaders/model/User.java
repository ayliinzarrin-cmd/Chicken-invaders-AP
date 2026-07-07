package com.ap.chickeninvaders.model;

public class User {
    private final String username;
    private final String password;
    private int highScore;
    private int lastLevel;
    private boolean musicOn;
    private boolean shotSoundOn;
    private boolean explosionSoundOn;
    private boolean endSoundOn;

    public User(String username, String password, int highScore, int lastLevel,
                boolean musicOn, boolean shotSoundOn, boolean explosionSoundOn, boolean endSoundOn) {
        this.username = username;
        this.password = password;
        this.highScore = highScore;
        this.lastLevel = lastLevel;
        this.musicOn = musicOn;
        this.shotSoundOn = shotSoundOn;
        this.explosionSoundOn = explosionSoundOn;
        this.endSoundOn = endSoundOn;
    }

    public static User newUser(String username, String password) {
        return new User(username, password, 0, 1, true, true, true, true);
    }

    public String toFileLine() {
        return username + "|" + password + "|" + highScore + "|" + lastLevel + "|"
                + bool(musicOn) + "|" + bool(shotSoundOn) + "|"
                + bool(explosionSoundOn) + "|" + bool(endSoundOn);
    }

    public static User fromFileLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 8) {
            return null;
        }
        return new User(
                parts[0],
                parts[1],
                parseInt(parts[2], 0),
                parseInt(parts[3], 1),
                "1".equals(parts[4]),
                "1".equals(parts[5]),
                "1".equals(parts[6]),
                "1".equals(parts[7])
        );
    }

    private static int parseInt(String text, int fallback) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private String bool(boolean value) {
        return value ? "1" : "0";
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getLastLevel() {
        return lastLevel;
    }

    public void setLastLevel(int lastLevel) {
        this.lastLevel = lastLevel;
    }

    public boolean isMusicOn() {
        return musicOn;
    }

    public boolean isShotSoundOn() {
        return shotSoundOn;
    }

    public boolean isExplosionSoundOn() {
        return explosionSoundOn;
    }

    public boolean isEndSoundOn() {
        return endSoundOn;
    }
}


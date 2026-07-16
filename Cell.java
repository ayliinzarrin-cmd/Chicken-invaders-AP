package com.ap.chickeninvaders.db;

import com.ap.chickeninvaders.model.User;
import com.ap.chickeninvaders.model.GameRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {
    private final File dataDirectory = new File("data");
    private final File usersFile = new File(dataDirectory, "users.txt");
    private final File recordsFile = new File(dataDirectory, "game_records.txt");

    public DatabaseManager() {
        initialize();
    }

    public void initialize() {
        try {
            if (!dataDirectory.exists()) {
                dataDirectory.mkdirs();
            }
            if (!usersFile.exists()) {
                usersFile.createNewFile();
            }
            if (!recordsFile.exists()) {
                recordsFile.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize database files.", e);
        }
    }

    public boolean register(String username, String password) {
        username = clean(username);
        password = clean(password);

        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }
        if (findUser(username) != null) {
            return false;
        }

        List<User> users = readUsers();
        users.add(User.newUser(username, password));
        writeUsers(users);
        return true;
    }

    public User login(String username, String password) {
        username = clean(username);
        password = clean(password);

        User user = findUser(username);
        if (user == null) {
            return null;
        }
        if (!user.getPassword().equals(password)) {
            return null;
        }
        return user;
    }

    public User findUser(String username) {
        username = clean(username);
        for (User user : readUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void updateUser(User updatedUser) {
        List<User> users = readUsers();
        boolean found = false;

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                found = true;
                break;
            }
        }

        if (!found) {
            users.add(updatedUser);
        }
        writeUsers(users);
    }

    public void saveGameRecord(User user, int score, int levelReached, String status) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String line = user.getUsername() + "|" + score + "|" + levelReached + "|" + status + "|" + time
                + "|" + flag(user.isMusicOn())
                + "|" + flag(user.isShotSoundOn())
                + "|" + flag(user.isExplosionSoundOn())
                + "|" + flag(user.isEndSoundOn());

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(recordsFile, true), StandardCharsets.UTF_8))) {
            writer.println(line);
        } catch (IOException e) {
            throw new RuntimeException("Could not save game record.", e);
        }

        if (score > user.getHighScore()) {
            user.setHighScore(score);
        }
        if (levelReached > user.getLastLevel()) {
            user.setLastLevel(levelReached);
        }
        updateUser(user);
    }

    public List<GameRecord> getHighScores(int limit) {
        Map<String, GameRecord> bestByUser = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(recordsFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                GameRecord record = GameRecord.fromFileLine(line);
                if (record == null) {
                    continue;
                }

                GameRecord currentBest = bestByUser.get(record.getUsername());
                boolean hasBetterScore = currentBest == null || record.getScore() > currentBest.getScore();
                boolean hasNewerEqualScore = currentBest != null
                        && record.getScore() == currentBest.getScore()
                        && record.getPlayedAt().compareTo(currentBest.getPlayedAt()) > 0;

                if (hasBetterScore || hasNewerEqualScore) {
                    bestByUser.put(record.getUsername(), record);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read game records.", e);
        }

        List<GameRecord> records = new ArrayList<>(bestByUser.values());
        records.sort((first, second) -> {
            int scoreOrder = Integer.compare(second.getScore(), first.getScore());
            if (scoreOrder != 0) {
                return scoreOrder;
            }
            return second.getPlayedAt().compareTo(first.getPlayedAt());
        });

        if (limit > 0 && records.size() > limit) {
            return new ArrayList<>(records.subList(0, limit));
        }
        return records;
    }

    private List<User> readUsers() {
        List<User> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(usersFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                User user = User.fromFileLine(line);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read users file.", e);
        }

        return users;
    }

    private void writeUsers(List<User> users) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(usersFile), StandardCharsets.UTF_8))) {
            for (User user : users) {
                writer.println(user.toFileLine());
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not write users file.", e);
        }
    }

    private String clean(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("|", "").trim();
    }

    private String flag(boolean value) {
        return value ? "1" : "0";
    }
}


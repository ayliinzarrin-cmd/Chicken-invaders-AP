package com.ap.chickeninvaders.db;

import com.ap.chickeninvaders.model.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private final File dataDirectory = new File("data");
    private final File usersFile = new File(dataDirectory, "users.txt");

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
}


package main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

//Passwords are salted + SHA-256 hashed
public class UserManager {

    private static final String SAVES_DIR = "saves";
    private static final String USERS_FILE = SAVES_DIR + File.separator + "users.properties";

    private final Properties users = new Properties();
    private String currentUser = null;

    public UserManager() {
        ensureSavesDir();
        loadUsers();
    }

    public java.util.Set<Object> getAllUsernames() {
        return users.keySet();
    }

    // PUBLIC API

    public String createAccount(String username, String password) {
        username = username.trim();

        if (username.isEmpty()) return "Username cannot be empty.";
        if (password.length() < 4) return "Password must be at least 4 characters.";
        if (!username.matches("[\\w]{3,16}")) return "Username: 3-16 letters/numbers/underscore only.";
        if (users.containsKey(username)) return "Username already exists.";

        String salt = generateSalt();
        String hash = hash(password, salt);
        users.setProperty(username, salt + ":" + hash);
        saveUsers();
        return null;
    }

    public String login(String username, String password) {
        username = username.trim();

        if (!users.containsKey(username)) return "Account not found.";

        String stored = users.getProperty(username);
        String[] parts = stored.split(":", 2);
        if (parts.length != 2) return "Corrupted account data.";

        String salt = parts[0];
        String expectedHash = parts[1];
        String actualHash = hash(password, salt);

        if (!expectedHash.equals(actualHash)) return "Incorrect password.";

        currentUser = username;
        return null;
    }

    public void logout() {
        currentUser = null;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public File getSaveFile() {
        if (currentUser == null) throw new IllegalStateException("Not logged in.");
        return new File(SAVES_DIR + File.separator + currentUser + ".dat");
    }

    // HELPERS

    private void ensureSavesDir() {
        File dir = new File(SAVES_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    private void loadUsers() {
        File f = new File(USERS_FILE);
        if (!f.exists()) return;
        try (InputStream in = new FileInputStream(f)) {
            users.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (OutputStream out = new FileOutputStream(USERS_FILE)) {
            users.store(out, "Ink & Blood — User Accounts (DO NOT EDIT MANUALLY)");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String generateSalt() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public String hash(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String salted = salt + password;
            byte[] digest = md.digest(salted.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    public boolean userExists(String username) {
        return users.containsKey(username.trim());
    }

    public void deleteAccount(String username) {
        users.remove(username.trim());
        saveUsers();

        // also delete their save file
        File saveFile = new File("saves" + File.separator + username.trim() + ".dat");
        if (saveFile.exists()) saveFile.delete();
    }
}
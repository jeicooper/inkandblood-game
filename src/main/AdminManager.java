package main;

import java.io.*;
import java.util.Properties;

public class AdminManager {

    private static final String ADMIN_FILE =
            System.getProperty("user.home") + File.separator +
                    "InkAndBlood" + File.separator + "saves" + File.separator + "admin.properties";
    private static final String DEFAULT_ADMIN_PASSWORD = "adminpower";

    private final UserManager   userManager;
    private final Properties    adminProps = new Properties();

    public AdminManager(UserManager userManager) {
        this.userManager = userManager;
        ensureAdminFile();
        load();
    }

    public boolean verifyAdmin(String password) {
        String stored = adminProps.getProperty("admin_hash");
        if (stored == null) return false;
        String[] parts = stored.split(":", 2);
        if (parts.length != 2) return false;
        return parts[1].equals(userManager.hash(password, parts[0]));
    }

    public java.util.List<String> getAllUsernames() {
        java.util.List<String> list = new java.util.ArrayList<>();
        for (Object key : userManager.getAllUsernames()) list.add(key.toString());
        java.util.Collections.sort(list);
        return list;
    }

    public java.util.List<String> searchByName(String query) {
        return userManager.searchByName(query);
    }

    public UserManager.StudentProfile getProfile(String username) {
        return userManager.getProfile(username);
    }

    public String resetPassword(String username, String newPassword) {
        username = username.trim();
        if (!userManager.userExists(username))
            return "Account '" + username + "' not found.";
        return userManager.resetPassword(username, newPassword);
    }

    public String resetAccount(String username) {
        username = username.trim();
        if (!userManager.userExists(username))
            return "Account '" + username + "' not found.";
        userManager.deleteAccount(username);

        try {
            NPCDatabase.deleteForUser(username);
        } catch (Exception ignored) {}
        return "Account '" + username + "' has been deleted.";
    }

    private void ensureAdminFile() {
        File f = new File(ADMIN_FILE);
        if (f.exists()) return;
        String salt = userManager.generateSalt();
        String hash = userManager.hash(DEFAULT_ADMIN_PASSWORD, salt);
        adminProps.setProperty("admin_hash", salt + ":" + hash);
        save();
    }

    public String changeAdminPassword(String currentPassword, String newPassword) {
        if (!verifyAdmin(currentPassword))
            return "Current password is incorrect.";
        if (newPassword.length() < 8)
            return "New password must be at least 8 characters.";

        String salt = userManager.generateSalt();
        String hash = userManager.hash(newPassword, salt);
        adminProps.setProperty("admin_hash", salt + ":" + hash);
        save();
        return null; // null = success
    }

    private void load() {
        File f = new File(ADMIN_FILE);
        if (!f.exists()) return;
        try (InputStream in = new FileInputStream(f)) { adminProps.load(in); }
        catch (IOException e) { e.printStackTrace(); }
    }

    private void save() {
        try (OutputStream out = new FileOutputStream(ADMIN_FILE)) {
            adminProps.store(out, "Ink & Blood Admin — DO NOT SHARE");
        } catch (IOException e) { e.printStackTrace(); }
    }
}
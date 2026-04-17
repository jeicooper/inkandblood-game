package main;

import java.io.*;
import java.util.Properties;

public class AdminManager {

    private static final String ADMIN_FILE = "saves" + File.separator + "admin.properties";
    private static final String DEFAULT_ADMIN_PASSWORD = "adminpower";

    private final UserManager userManager;
    private final Properties adminProps = new Properties();

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
        for (Object key : userManager.getAllUsernames()) {
            list.add(key.toString());
        }
        java.util.Collections.sort(list);
        return list;
    }

    public String resetAccount(String username) {
        username = username.trim();
        if (!userManager.userExists(username)) {
            return "Account '" + username + "' not found.";
        }
        userManager.deleteAccount(username);
        NPCDatabase.deleteForUser(username);
        return "Account '" + username + "' has been deleted. The student can create a new one.";
    }

    private void ensureAdminFile() {
        File f = new File(ADMIN_FILE);
        if (f.exists()) return;

        String salt = userManager.generateSalt();
        String hash = userManager.hash(DEFAULT_ADMIN_PASSWORD, salt);
        adminProps.setProperty("admin_hash", salt + ":" + hash);
        save();
        System.out.println("Admin password created: " + DEFAULT_ADMIN_PASSWORD);
        System.out.println("Change this immediately in saves/admin.properties or via the reset tool.");
    }

    private void load() {
        File f = new File(ADMIN_FILE);
        if (!f.exists()) return;
        try (InputStream in = new FileInputStream(f)) {
            adminProps.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        try (OutputStream out = new FileOutputStream(ADMIN_FILE)) {
            adminProps.store(out, "Ink & Blood Admin — DO NOT SHARE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

public class UserManager {

    private static final String SAVES_DIR   = "saves";
    private static final String USERS_FILE  = SAVES_DIR + File.separator + "users.properties";
    private static final String PROFILE_FILE = SAVES_DIR + File.separator + "profiles.properties";

    private final Properties users    = new Properties();
    private final Properties profiles = new Properties(); // key=username, value=CSV profile
    private String currentUser = null;


    public UserManager() {
        ensureSavesDir();
        loadUsers();
        loadProfiles();
    }

    public static String usernameFromStudentId(String studentId) {
        String trimmed = studentId.trim();
        if (trimmed.length() < 4) return trimmed;
        return trimmed.substring(trimmed.length() - 4);
    }

    public static boolean isValidStudentId(String id) {
        return id != null && id.trim().matches("20(0[5-9]|[12]\\d|3[0-5])-100-\\d{3,4}");
    }

    public static boolean isValidYearSection(String ys) {
        if (ys == null) return false;
        String s = ys.trim();
        return s.matches("[1-4][-\\s]?[1-9]")
                || s.matches("[1-4][-\\s]?[A-Za-z]");
    }

    public String createAccount(String firstName, String lastName, String middleInitial,
                                String suffix, String yearSection, String studentId,
                                String password) {

        firstName   = firstName.trim();
        lastName    = lastName.trim();
        middleInitial = middleInitial.trim();
        suffix      = suffix.trim();
        yearSection = yearSection.trim();
        studentId   = studentId.trim().toUpperCase();

        // --- Validation ---
        if (firstName.isEmpty())  return "First name cannot be empty.";
        if (lastName.isEmpty())   return "Last name cannot be empty.";
        if (yearSection.isEmpty()) return "Year & Section cannot be empty.";
        if (!isValidStudentId(studentId))
            return "Invalid student ID. Format must be: 202X-100-XXXX";
        if (password.length() < 8)
            return "Password must be at least 8 characters.";

        String username = usernameFromStudentId(studentId);

        if (users.containsKey(username))
            return "An account with that student ID already exists (ID ends: " + username + ").";

        // --- Store credentials ---
        String salt = generateSalt();
        String hash = hash(password, salt);
        users.setProperty(username, salt + ":" + hash);
        saveUsers();

        String profileValue = encode(firstName) + "|" + encode(lastName) + "|"
                + encode(middleInitial) + "|" + encode(suffix) + "|"
                + encode(yearSection)   + "|" + encode(studentId);
        profiles.setProperty(username, profileValue);
        saveProfiles();

        return null;
    }

    public String createAccount(String username, String password) {
        username = username.trim();
        if (username.isEmpty()) return "Username cannot be empty.";
        if (password.length() < 8) return "Password must be at least 8 characters.";
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

        String salt         = parts[0];
        String expectedHash = parts[1];
        String actualHash   = hash(password, salt);

        if (!expectedHash.equals(actualHash)) return "Incorrect password.";

        currentUser = username;
        return null;
    }

    public void logout()              { currentUser = null; }
    public String getCurrentUser()    { return currentUser; }
    public boolean isLoggedIn()       { return currentUser != null; }

    public File getSaveFile() {
        if (currentUser == null) throw new IllegalStateException("Not logged in.");
        return new File(SAVES_DIR + File.separator + currentUser + ".dat");
    }

    public java.util.Set<Object> getAllUsernames() { return users.keySet(); }

    public StudentProfile getProfile(String username) {
        String raw = profiles.getProperty(username.trim());
        if (raw == null) return null;
        String[] p = raw.split("\\|", -1);
        if (p.length < 6) return null;
        StudentProfile sp = new StudentProfile();
        sp.username      = username.trim();
        sp.firstName     = decode(p[0]);
        sp.lastName      = decode(p[1]);
        sp.middleInitial = decode(p[2]);
        sp.suffix        = decode(p[3]);
        sp.yearSection   = decode(p[4]);
        sp.studentId     = decode(p[5]);
        return sp;
    }

    public java.util.List<String> searchByName(String query) {
        query = query.trim().toLowerCase();
        java.util.List<String> results = new java.util.ArrayList<>();
        for (Object key : users.keySet()) {
            String uname = key.toString();
            StudentProfile sp = getProfile(uname);
            if (sp == null) {
                if (uname.toLowerCase().contains(query)) results.add(uname);
                continue;
            }
            if (sp.firstName.toLowerCase().contains(query)
                    || sp.lastName.toLowerCase().contains(query)) {
                results.add(uname);
            }
        }
        java.util.Collections.sort(results);
        return results;
    }

    public boolean userExists(String username)  { return users.containsKey(username.trim()); }

    public void deleteAccount(String username) {
        username = username.trim();
        users.remove(username);
        profiles.remove(username);
        saveUsers();
        saveProfiles();

        File saveFile = new File(SAVES_DIR + File.separator + username + ".dat");
        if (saveFile.exists()) saveFile.delete();
    }

    public String resetPassword(String username, String newPassword) {
        username = username.trim();
        if (!users.containsKey(username)) return "Account not found.";
        if (newPassword.length() < 8)     return "Password must be at least 8 characters.";
        String salt = generateSalt();
        String hash = hash(newPassword, salt);
        users.setProperty(username, salt + ":" + hash);
        saveUsers();
        return null;
    }

    public String generateSalt() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public String hash(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest((salt + password).getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    private void ensureSavesDir() {
        File dir = new File(SAVES_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    private void loadUsers() {
        File f = new File(USERS_FILE);
        if (!f.exists()) return;
        try (InputStream in = new FileInputStream(f)) { users.load(in); }
        catch (IOException e) { e.printStackTrace(); }
    }

    private void saveUsers() {
        try (OutputStream out = new FileOutputStream(USERS_FILE)) {
            users.store(out, "Ink & Blood — User Accounts (DO NOT EDIT MANUALLY)");
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void loadProfiles() {
        File f = new File(PROFILE_FILE);
        if (!f.exists()) return;
        try (InputStream in = new FileInputStream(f)) { profiles.load(in); }
        catch (IOException e) { e.printStackTrace(); }
    }

    private void saveProfiles() {
        try (OutputStream out = new FileOutputStream(PROFILE_FILE)) {
            profiles.store(out, "Ink & Blood — Student Profiles (DO NOT EDIT MANUALLY)");
        } catch (IOException e) { e.printStackTrace(); }
    }

    private static String encode(String s) {
        try {
            return java.net.URLEncoder.encode(s == null ? "" : s, "UTF-8");
        } catch (Exception e) { return s == null ? "" : s; }
    }

    private static String decode(String s) {
        try {
            return java.net.URLDecoder.decode(s == null ? "" : s, "UTF-8");
        } catch (Exception e) { return s == null ? "" : s; }
    }

    public static class StudentProfile {
        public String username;
        public String firstName;
        public String lastName;
        public String middleInitial;
        public String suffix;
        public String yearSection;
        public String studentId;

        public String displayName() {
            StringBuilder sb = new StringBuilder();
            sb.append(lastName.toUpperCase()).append(", ").append(firstName);
            if (!middleInitial.isEmpty()) sb.append(" ").append(middleInitial).append(".");
            if (!suffix.isEmpty())        sb.append(", ").append(suffix);
            return sb.toString();
        }
    }
}
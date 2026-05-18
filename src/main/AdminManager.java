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

    public String editProfile(String username,
                              String firstName, String lastName,
                              String middleInitial, String suffix,
                              String yearSection, String studentId) {
        return userManager.updateProfile(username, firstName, lastName,
                middleInitial, suffix, yearSection, studentId);
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

    public String exportToExcel() {
        java.util.List<String> usernames = getAllUsernames();
        if (usernames.isEmpty()) return "No accounts to export.";

        // Use the same relative path as the .dat save files
        String exportPath = "saves" + File.separator + "user_export.xls";

        try {
            File f = new File(exportPath);
            f.getParentFile().mkdirs();

            try (java.io.PrintWriter pw = new java.io.PrintWriter(
                    new java.io.OutputStreamWriter(new FileOutputStream(f),
                            java.nio.charset.StandardCharsets.UTF_8))) {

                // SpreadsheetML — opens natively in Excel and LibreOffice, no library needed
                pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                pw.println("<?mso-application progid=\"Excel.Sheet\"?>");
                pw.println("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"");
                pw.println(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\">");
                pw.println("<Worksheet ss:Name=\"Students\">");
                pw.println("<Table>");

                // Header row
                pw.println("<Row>");
                for (String h : new String[]{
                        "Username", "First Name", "Last Name", "Middle Initial",
                        "Suffix", "Year &amp; Section", "Student ID", "Game Progress"}) {
                    pw.println("<Cell><Data ss:Type=\"String\">" + h + "</Data></Cell>");
                }
                pw.println("</Row>");

                // Data rows
                for (String username : usernames) {
                    UserManager.StudentProfile sp = getProfile(username);
                    String progress = getProgressLabel(username);

                    pw.println("<Row>");
                    pw.println(cell(username));
                    if (sp != null) {
                        pw.println(cell(sp.firstName));
                        pw.println(cell(sp.lastName));
                        pw.println(cell(sp.middleInitial));
                        pw.println(cell(sp.suffix));
                        pw.println(cell(sp.yearSection));
                        pw.println(cell(sp.studentId));
                    } else {
                        for (int i = 0; i < 6; i++) pw.println(cell("—"));
                    }
                    pw.println(cell(progress));
                    pw.println("</Row>");
                }

                pw.println("</Table>");
                pw.println("</Worksheet>");
                pw.println("</Workbook>");
            }

            return null; // null = success

        } catch (Exception e) {
            e.printStackTrace();
            return "Export failed: " + e.getMessage();
        }
    }

    private String getProgressLabel(String username) {
        File sf = new File("saves" + File.separator + username + ".dat");
        if (!sf.exists()) return "No save data";
        try (java.io.ObjectInputStream ois = new java.io.ObjectInputStream(
                new java.io.FileInputStream(sf))) {
            main.SaveData sd = (main.SaveData) ois.readObject();
            switch (sd.currentQuest) {
                case 0: return "Chapter 1 — Quest 1: Find the Siblings";
                case 1: return "Chapter 1 — Quest 2: Art and Writing";
                case 2: return "Chapter 2 — Quest 3: Enrollment at Ateneo";
                case 3: return "Chapter 2 — Quest 4: The Competitions";
                case 4: return "Chapter 3 — Quest 5: Writing Noli Me Tangere";
                case 5: return "Chapter 3 — Quest 6: El Filibusterismo";
                case 6: return "Chapter 4 — Quest 7: The Final Days";
                default: return "Quest " + (sd.currentQuest + 1);
            }
        } catch (Exception e) {
            return "Save file unreadable";
        }
    }

    private String cell(String value) {
        if (value == null) value = "";
        value = value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        return "<Cell><Data ss:Type=\"String\">" + value + "</Data></Cell>";
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
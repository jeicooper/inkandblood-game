package main;

import java.io.*;
import java.util.Properties;

/**
 * Single operations + data layer for the admin panel.
 *
 * An "admin" is the teacher. There is exactly one kind of privileged account:
 * each admin registers a username + password (replacing the old single shared
 * admin password) and, once logged in, can:
 *   - create room codes (one per course / year / section) to hand to students,
 *   - view and export the students grouped under those room codes,
 *   - search / view / edit / reset / delete any student account,
 *   - export all students.
 *
 * Student account data is delegated to {@link UserManager}. Everything else
 * (admin accounts + room codes + their storage) lives directly in this class.
 *
 * Files (under ~/InkAndBlood/saves):
 *   admins.properties        username  -> salt:hash
 *   admin_names.properties   username  -> encoded full name
 *   rooms.properties         ROOMCODE  -> owner|course|year|section|createdMillis
 */
public class AdminManager {

    private static final String SAVES_DIR =
            System.getProperty("user.home") + File.separator +
                    "InkAndBlood" + File.separator + "saves";

    private static final String ADMINS_FILE     = SAVES_DIR + File.separator + "admins.properties";
    private static final String ADMIN_NAME_FILE = SAVES_DIR + File.separator + "admin_names.properties";
    private static final String ROOMS_FILE       = SAVES_DIR + File.separator + "rooms.properties";

    // Code generation alphabet — excludes easily-confused characters (0/O, 1/I).
    private static final String CODE_ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int    CODE_LENGTH   = 6;

    private final UserManager userManager;        // student accounts + hashing helpers
    private final Properties  admins     = new Properties();
    private final Properties  adminNames = new Properties();
    private final Properties  rooms      = new Properties();

    private String currentAdmin = null;

    public AdminManager(UserManager userManager) {
        this.userManager = userManager;
        ensureDir();
        load(ADMINS_FILE,     admins);
        load(ADMIN_NAME_FILE, adminNames);
        load(ROOMS_FILE,      rooms);
    }

    // ===== Admin accounts =================================================

    /** @return error message, or null on success. */
    public String registerAdmin(String username, String fullName, String password) {
        username = username == null ? "" : username.trim();
        fullName = fullName == null ? "" : fullName.trim();
        if (username.isEmpty())                 return "Username cannot be empty.";
        if (!username.matches("[\\w]{3,16}"))   return "Username: 3-16 letters / numbers / underscore.";
        if (fullName.isEmpty())                 return "Full name cannot be empty.";
        if (password == null || password.length() < 8)
            return "Password must be at least 8 characters.";
        if (admins.containsKey(username))       return "An admin with that username already exists.";

        String salt = userManager.generateSalt();
        String hash = userManager.hash(password, salt);
        admins.setProperty(username, salt + ":" + hash);
        adminNames.setProperty(username, encode(fullName));
        save(ADMINS_FILE,     admins,     "Ink & Blood — Admin Accounts");
        save(ADMIN_NAME_FILE, adminNames, "Ink & Blood — Admin Names");
        return null;
    }

    /** @return error message, or null on success (sets the current admin). */
    public String loginAdmin(String username, String password) {
        username = username == null ? "" : username.trim();
        if (!admins.containsKey(username)) return "Admin account not found.";
        String stored = admins.getProperty(username);
        String[] parts = stored.split(":", 2);
        if (parts.length != 2) return "Corrupted account data.";
        String actual = userManager.hash(password, parts[0]);
        if (!parts[1].equals(actual)) return "Incorrect password.";
        currentAdmin = username;
        return null;
    }

    public void    logoutAdmin()        { currentAdmin = null; }
    public String  getCurrentAdmin()    { return currentAdmin; }
    public boolean isLoggedIn()         { return currentAdmin != null; }
    public boolean adminExists(String username) {
        return username != null && admins.containsKey(username.trim());
    }

    public String getCurrentAdminName() { return getAdminName(currentAdmin); }

    public String getAdminName(String username) {
        if (username == null) return "";
        String raw = adminNames.getProperty(username.trim());
        return raw == null ? username.trim() : decode(raw);
    }

    // ===== Room codes =====================================================

    /** Creates a room owned by the current admin; returns the generated code (or null if not logged in). */
    public String createRoom(String course, String year, String section) {
        String owner = currentAdmin;
        if (owner == null) return null;
        String code = generateUniqueCode();
        String value = encode(owner) + "|" + encode(course.trim()) + "|"
                + encode(year.trim()) + "|" + encode(section.trim()) + "|"
                + System.currentTimeMillis();
        rooms.setProperty(code, value);
        save(ROOMS_FILE, rooms, "Ink & Blood — Room Codes");
        return code;
    }

    public boolean roomExists(String code) {
        return code != null && rooms.containsKey(code.trim().toUpperCase());
    }

    public Room getRoom(String code) {
        if (code == null) return null;
        String raw = rooms.getProperty(code.trim().toUpperCase());
        if (raw == null) return null;
        String[] p = raw.split("\\|", -1);
        Room r = new Room();
        r.code    = code.trim().toUpperCase();
        r.owner   = p.length > 0 ? decode(p[0]) : "";
        r.course  = p.length > 1 ? decode(p[1]) : "";
        r.year    = p.length > 2 ? decode(p[2]) : "";
        r.section = p.length > 3 ? decode(p[3]) : "";
        try { r.created = p.length > 4 ? Long.parseLong(p[4]) : 0L; }
        catch (NumberFormatException e) { r.created = 0L; }
        return r;
    }

    /** All rooms owned by the current admin, newest first. */
    public java.util.List<Room> getRoomsForCurrentAdmin() {
        java.util.List<Room> list = new java.util.ArrayList<>();
        if (currentAdmin == null) return list;
        String owner = currentAdmin.trim();
        for (Object key : rooms.keySet()) {
            Room r = getRoom(key.toString());
            if (r != null && owner.equals(r.owner)) list.add(r);
        }
        list.sort((a, b) -> Long.compare(b.created, a.created));
        return list;
    }

    public void deleteRoom(String code) {
        if (code == null) return;
        rooms.remove(code.trim().toUpperCase());
        save(ROOMS_FILE, rooms, "Ink & Blood — Room Codes");
    }

    /** Sorted usernames assigned to a room code. */
    public java.util.List<String> getStudentsInRoom(String code) {
        return userManager.getUsernamesByRoom(code);
    }

    public int countStudentsInRoom(String code) {
        return userManager.getUsernamesByRoom(code).size();
    }

    private String generateUniqueCode() {
        java.security.SecureRandom rnd = new java.security.SecureRandom();
        String code;
        int guard = 0;
        do {
            StringBuilder sb = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++)
                sb.append(CODE_ALPHABET.charAt(rnd.nextInt(CODE_ALPHABET.length())));
            code = sb.toString();
        } while (rooms.containsKey(code) && ++guard < 1000);
        return code;
    }

    // ===== Student operations =============================================

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
        try { NPCDatabase.deleteForUser(username); } catch (Exception ignored) {}
        return "Account '" + username + "' has been deleted.";
    }

    // ===== Excel export ===================================================

    /** Export every student account. */
    public String exportToExcel() {
        return writeWorkbook(getAllUsernames(),
                "saves" + File.separator + "user_export.xls",
                "Ink and Blood: Rizal's Adventure - Student Records",
                "All Students");
    }

    /** Export only the students belonging to one room code. */
    public String exportRoomToExcel(String code) {
        Room room = getRoom(code);
        if (room == null) return "Room not found.";
        java.util.List<String> students = getStudentsInRoom(code);
        if (students.isEmpty()) return "No students in this room yet.";

        String safe = code.replaceAll("[^A-Za-z0-9_-]", "");
        String subtitle = "Room " + room.code
                + (room.course.isEmpty()      ? "" : "  •  " + room.course)
                + (room.yearSection().isEmpty() ? "" : "  •  " + room.yearSection());
        return writeWorkbook(students,
                "saves" + File.separator + "room_" + safe + "_export.xls",
                "Ink and Blood: Rizal's Adventure - Student Records",
                subtitle);
    }

    private String writeWorkbook(java.util.List<String> usernames,
                                 String exportPath, String title, String subtitle) {
        if (usernames.isEmpty()) return "No accounts to export.";

        String exportDate = new java.text.SimpleDateFormat("MMMM dd, yyyy  hh:mm a")
                .format(new java.util.Date());

        try {
            File f = new File(exportPath);
            f.getParentFile().mkdirs();

            try (java.io.PrintWriter pw = new java.io.PrintWriter(
                    new java.io.OutputStreamWriter(new FileOutputStream(f),
                            java.nio.charset.StandardCharsets.UTF_8))) {

                pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                pw.println("<?mso-application progid=\"Excel.Sheet\"?>");
                pw.println("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"");
                pw.println(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\">");

                pw.println("<Styles>");

                pw.println("<Style ss:ID=\"title\">");
                pw.println("  <Font ss:Bold=\"1\" ss:Size=\"15\" ss:Color=\"#1F3864\"/>");
                pw.println("  <Alignment ss:Vertical=\"Center\"/>");
                pw.println("</Style>");

                pw.println("<Style ss:ID=\"meta\">");
                pw.println("  <Font ss:Italic=\"1\" ss:Size=\"10\" ss:Color=\"#595959\"/>");
                pw.println("  <Alignment ss:Vertical=\"Center\"/>");
                pw.println("</Style>");

                pw.println("<Style ss:ID=\"header\">");
                pw.println("  <Font ss:Bold=\"1\" ss:Size=\"11\" ss:Color=\"#FFFFFF\"/>");
                pw.println("  <Interior ss:Color=\"#1F3864\" ss:Pattern=\"Solid\"/>");
                pw.println("  <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\"/>");
                pw.println("  <Borders>");
                pw.println("    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"2\" ss:Color=\"#C9A227\"/>");
                pw.println("  </Borders>");
                pw.println("</Style>");

                pw.println("<Style ss:ID=\"rowEven\">");
                pw.println("  <Font ss:Size=\"10\" ss:Color=\"#1F3864\"/>");
                pw.println("  <Interior ss:Color=\"#DCE6F1\" ss:Pattern=\"Solid\"/>");
                pw.println("  <Alignment ss:Vertical=\"Center\"/>");
                pw.println("  <Borders>");
                pw.println("    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\" ss:Color=\"#BDD7EE\"/>");
                pw.println("  </Borders>");
                pw.println("</Style>");

                pw.println("<Style ss:ID=\"rowOdd\">");
                pw.println("  <Font ss:Size=\"10\" ss:Color=\"#1F3864\"/>");
                pw.println("  <Interior ss:Color=\"#FFFFFF\" ss:Pattern=\"Solid\"/>");
                pw.println("  <Alignment ss:Vertical=\"Center\"/>");
                pw.println("  <Borders>");
                pw.println("    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\" ss:Color=\"#BDD7EE\"/>");
                pw.println("  </Borders>");
                pw.println("</Style>");

                pw.println("<Style ss:ID=\"progressEven\">");
                pw.println("  <Font ss:Bold=\"1\" ss:Size=\"10\" ss:Color=\"#375623\"/>");
                pw.println("  <Interior ss:Color=\"#DCE6F1\" ss:Pattern=\"Solid\"/>");
                pw.println("  <Alignment ss:Vertical=\"Center\"/>");
                pw.println("  <Borders>");
                pw.println("    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\" ss:Color=\"#BDD7EE\"/>");
                pw.println("  </Borders>");
                pw.println("</Style>");

                pw.println("<Style ss:ID=\"progressOdd\">");
                pw.println("  <Font ss:Bold=\"1\" ss:Size=\"10\" ss:Color=\"#375623\"/>");
                pw.println("  <Interior ss:Color=\"#FFFFFF\" ss:Pattern=\"Solid\"/>");
                pw.println("  <Alignment ss:Vertical=\"Center\"/>");
                pw.println("  <Borders>");
                pw.println("    <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\" ss:Color=\"#BDD7EE\"/>");
                pw.println("  </Borders>");
                pw.println("</Style>");

                pw.println("</Styles>");

                pw.println("<Worksheet ss:Name=\"Students\">");
                pw.println("<Table ss:DefaultRowHeight=\"18\">");

                // Column widths
                pw.println("<Column ss:Width=\"90\"/>");   // Username
                pw.println("<Column ss:Width=\"100\"/>");  // First Name
                pw.println("<Column ss:Width=\"100\"/>");  // Last Name
                pw.println("<Column ss:Width=\"70\"/>");   // M.I.
                pw.println("<Column ss:Width=\"60\"/>");   // Suffix
                pw.println("<Column ss:Width=\"80\"/>");   // Year & Section
                pw.println("<Column ss:Width=\"110\"/>");  // Student ID
                pw.println("<Column ss:Width=\"80\"/>");   // Room Code
                pw.println("<Column ss:Width=\"240\"/>");  // Game Progress

                // Title row
                pw.println("<Row ss:Height=\"28\">");
                pw.println("  <Cell ss:StyleID=\"title\"><Data ss:Type=\"String\">" + esc(title) + "</Data></Cell>");
                pw.println("</Row>");

                // Subtitle (which set of students this is)
                pw.println("<Row ss:Height=\"18\">");
                pw.println("  <Cell ss:StyleID=\"meta\"><Data ss:Type=\"String\">" + esc(subtitle) + "</Data></Cell>");
                pw.println("</Row>");

                // Export date row
                pw.println("<Row ss:Height=\"18\">");
                pw.println("  <Cell ss:StyleID=\"meta\"><Data ss:Type=\"String\">Exported: " + exportDate + "</Data></Cell>");
                pw.println("</Row>");

                // Total count row
                pw.println("<Row ss:Height=\"16\">");
                pw.println("  <Cell ss:StyleID=\"meta\"><Data ss:Type=\"String\">Total Students: " + usernames.size() + "</Data></Cell>");
                pw.println("</Row>");

                // Blank spacer
                pw.println("<Row ss:Height=\"10\"/>");

                // Column header row
                pw.println("<Row ss:Height=\"22\">");
                for (String h : new String[]{
                        "Username", "First Name", "Last Name", "Middle Initial",
                        "Suffix", "Year & Section", "Student ID", "Room Code", "Game Progress"}) {
                    pw.println("  <Cell ss:StyleID=\"header\"><Data ss:Type=\"String\">" + h + "</Data></Cell>");
                }
                pw.println("</Row>");

                // Data rows
                int rowNum = 0;
                for (String username : usernames) {
                    UserManager.StudentProfile sp = getProfile(username);
                    String progress = getProgressLabel(username);
                    boolean even = (rowNum % 2 == 0);
                    String rowStyle      = even ? "rowEven"      : "rowOdd";
                    String progressStyle = even ? "progressEven" : "progressOdd";
                    rowNum++;

                    pw.println("<Row ss:Height=\"18\">");
                    pw.println("  " + styledCell(username, rowStyle));
                    if (sp != null) {
                        pw.println("  " + styledCell(sp.firstName,     rowStyle));
                        pw.println("  " + styledCell(sp.lastName,      rowStyle));
                        pw.println("  " + styledCell(sp.middleInitial, rowStyle));
                        pw.println("  " + styledCell(sp.suffix,        rowStyle));
                        pw.println("  " + styledCell(sp.yearSection,   rowStyle));
                        pw.println("  " + styledCell(sp.studentId,     rowStyle));
                        pw.println("  " + styledCell(sp.roomCode == null || sp.roomCode.isEmpty()
                                ? "—" : sp.roomCode,                   rowStyle));
                    } else {
                        for (int i = 0; i < 7; i++)
                            pw.println("  " + styledCell("-", rowStyle));
                    }
                    pw.println("  " + styledCell(progress, progressStyle));
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
        File sf = userManager.getSaveFileForUser(username);
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

    private String styledCell(String value, String styleID) {
        return "<Cell ss:StyleID=\"" + styleID + "\"><Data ss:Type=\"String\">" + esc(value) + "</Data></Cell>";
    }

    private static String esc(String value) {
        if (value == null) value = "";
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    // ===== IO helpers =====================================================

    private void ensureDir() {
        File dir = new File(SAVES_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    private void load(String path, Properties props) {
        File f = new File(path);
        if (!f.exists()) return;
        try (InputStream in = new FileInputStream(f)) { props.load(in); }
        catch (IOException e) { e.printStackTrace(); }
    }

    private void save(String path, Properties props, String header) {
        try (OutputStream out = new FileOutputStream(path)) { props.store(out, header); }
        catch (IOException e) { e.printStackTrace(); }
    }

    private static String encode(String s) {
        try { return java.net.URLEncoder.encode(s == null ? "" : s, "UTF-8"); }
        catch (Exception e) { return s == null ? "" : s; }
    }

    private static String decode(String s) {
        try { return java.net.URLDecoder.decode(s == null ? "" : s, "UTF-8"); }
        catch (Exception e) { return s == null ? "" : s; }
    }

    // ===== Data holder ====================================================

    public static class Room {
        public String code;
        public String owner;
        public String course;
        public String year;
        public String section;
        public long   created;

        /** Short one-line label, e.g. "Rizal 101  •  1-2". */
        public String label() {
            StringBuilder sb = new StringBuilder();
            if (course != null && !course.isEmpty()) sb.append(course);
            String ys = yearSection();
            if (!ys.isEmpty()) {
                if (sb.length() > 0) sb.append("  •  ");
                sb.append(ys);
            }
            return sb.length() == 0 ? "(unnamed room)" : sb.toString();
        }

        public String yearSection() {
            String y = year    == null ? "" : year.trim();
            String s = section == null ? "" : section.trim();
            if (!y.isEmpty() && !s.isEmpty()) return y + "-" + s;
            return (y + s).trim();
        }
    }
}
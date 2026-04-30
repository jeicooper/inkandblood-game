package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;


public class LogIn {

    private BufferedImage loginBG;

    private final AdminManager adminManager;
    private int adminMode = 0;

    private final StringBuilder adminPassField = new StringBuilder();
    private final StringBuilder searchField    = new StringBuilder();

    private String adminError   = "";
    private String adminSuccess = "";

    private java.util.List<String> accountList     = new java.util.ArrayList<>();
    private java.util.List<String> filteredList    = new java.util.ArrayList<>();
    private int listCursor       = 0;
    private int listScrollOffset = 0;
    private static final int MAX_VISIBLE = 5;
    private String selectedAccount = null;
    private UserManager.StudentProfile selectedProfile = null;

    private boolean confirmingDelete  = false;
    private boolean resettingPassword = false;
    private final StringBuilder newPasswordField    = new StringBuilder();
    private final StringBuilder confirmPasswordField = new StringBuilder();
    private boolean resetPwFocusOnConfirm = false;

    private final GamePanel   gp;
    private final UserManager userManager;
    private final SaveManager saveManager;

    public int mode = 0;

    private int menuCursor  = 0;
    private String errorMessage = "";

    // LogIn Form
    private final StringBuilder usernameField = new StringBuilder();
    private final StringBuilder passwordField = new StringBuilder();
    private boolean focusOnPassword = false;

    // Sign Up Form
    // personal info (FN, LN, MI, Suffix)
    // academic info (Year & Section, Student ID)
    // password (Password, Confirm Password)

    private int signupStep    = 0;
    private int signupFocus   = 0;

    // Personal Info
    private final StringBuilder fnField     = new StringBuilder();
    private final StringBuilder lnField     = new StringBuilder();
    private final StringBuilder miField     = new StringBuilder();
    private final StringBuilder sfxField    = new StringBuilder();

    // Acad Info
    private final StringBuilder ysField     = new StringBuilder();
    private final StringBuilder idField     = new StringBuilder();

    // Password Info
    private final StringBuilder pw1Field    = new StringBuilder();
    private final StringBuilder pw2Field    = new StringBuilder();

    private static final Color GOLD       = new Color(255, 220, 80);
    private static final Color LIGHT_GREY = new Color(180, 180, 180);
    private static final Color ERROR_RED  = new Color(230,  80,  80);
    private static final Color OK_GREEN   = new Color( 80, 220,  80);
    private static final Color DIM_GREY   = new Color(100, 100, 100);


    public LogIn(GamePanel gp, UserManager userManager, SaveManager saveManager) {
        this.gp          = gp;
        this.userManager = userManager;
        this.saveManager = saveManager;
        this.adminManager = new AdminManager(userManager);

        try {
            loginBG = ImageIO.read(getClass().getResourceAsStream("/images/introduction.png"));
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void reset() {
        mode = 0;
        menuCursor = 0;
        clearLoginFields();
        clearSignupFields();
        errorMessage = "";
    }

    public void handleKey(int code, char keyChar) {
        if (adminMode > 0) {
            handleAdminKey(code, keyChar);
        } else if (mode == 0) {
            handleMenuKey(code);
        } else if (mode == 1) {
            handleLoginKey(code, keyChar);
        } else if (mode == 2) {
            handleSignupKey(code, keyChar);
        }
    }

    private void handleMenuKey(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            menuCursor = (menuCursor - 1 + 4) % 4;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            menuCursor = (menuCursor + 1) % 4;
        }
        if (code == KeyEvent.VK_ENTER) {
            switch (menuCursor) {
                case 0: // Log In
                    mode = 1;
                    clearLoginFields();
                    errorMessage = "";
                    break;
                case 1: // Sign Up
                    mode = 2;
                    signupStep  = 0;
                    signupFocus = 0;
                    clearSignupFields();
                    errorMessage = "";
                    break;
                case 2: // Admin
                    adminMode = 1;
                    adminPassField.setLength(0);
                    searchField.setLength(0);
                    adminError = "";
                    adminSuccess = "";
                    selectedAccount = null;
                    selectedProfile = null;
                    confirmingDelete = false;
                    listCursor = 0;
                    listScrollOffset = 0;
                    break;
                case 3: // Quit
                    System.exit(0);
                    break;
            }
        }
    }

    // LOGIN
    private void handleLoginKey(int code, char keyChar) {
        errorMessage = "";

        if (code == KeyEvent.VK_ESCAPE) { mode = 0; clearLoginFields(); return; }

        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN) {
            focusOnPassword = !focusOnPassword;
            return;
        }

        if (code == KeyEvent.VK_BACK_SPACE) {
            StringBuilder active = focusOnPassword ? passwordField : usernameField;
            if (active.length() > 0) active.deleteCharAt(active.length() - 1);
            return;
        }

        if (code == KeyEvent.VK_ENTER) { submitLogin(); return; }

        if (keyChar >= 32 && keyChar != 127) {
            if (!focusOnPassword && usernameField.length() < 4) {
                if (Character.isDigit(keyChar)) usernameField.append(keyChar);
            } else if (focusOnPassword && passwordField.length() < 32) {
                passwordField.append(keyChar);
            }
        }
    }

    private void submitLogin() {
        String username = usernameField.toString().trim();
        String password = passwordField.toString();
        String error    = userManager.login(username, password);
        if (error != null) { errorMessage = error; }
        else               { onLoginSuccess(); }
    }

    // SIGN UP

    private void handleSignupKey(int code, char keyChar) {
        errorMessage = "";

        if (code == KeyEvent.VK_ESCAPE) {
            if (signupStep == 0) { mode = 0; clearSignupFields(); }
            else                 { signupStep--; signupFocus = 0; }
            return;
        }

        if (code == KeyEvent.VK_DOWN) {
            signupFocus = (signupFocus + 1) % fieldsInStep(signupStep);
            return;
        }
        if (code == KeyEvent.VK_UP) {
            signupFocus = (signupFocus - 1 + fieldsInStep(signupStep)) % fieldsInStep(signupStep);
            return;
        }

        if (code == KeyEvent.VK_ENTER) { submitSignupStep(); return; }

        if (code == KeyEvent.VK_BACK_SPACE) {
            StringBuilder active = activeSignupField();
            if (active != null && active.length() > 0)
                active.deleteCharAt(active.length() - 1);
            return;
        }

        // Printable characters
        if (keyChar >= 32 && keyChar != 127) {
            StringBuilder active = activeSignupField();
            if (active == null) return;
            int limit = fieldLimit(signupStep, signupFocus);
            if (active.length() < limit) {
                if (signupStep == 1 && signupFocus == 0) {
                    appendYearSectionChar(keyChar);
                } else if (signupStep == 1 && signupFocus == 1) {
                    appendStudentIdChar(keyChar);
                } else {
                    active.append(keyChar);
                }
            }
        }
    }

    private void appendYearSectionChar(char c) {
        if (!Character.isDigit(c)) return;
        String raw = ysField.toString().replaceAll("-", "");
        if (raw.length() >= 3) return;
        raw += c;
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < raw.length(); i++) {
            formatted.append(raw.charAt(i));
            if (i == 0 && raw.length() > 1) formatted.append('-');
        }
        ysField.setLength(0);
        ysField.append(formatted);
    }

    private void appendStudentIdChar(char c) {
        if (!Character.isDigit(c)) return;
        String raw = idField.toString().replaceAll("[^0-9]", "");
        if (raw.length() >= 11) return;
        raw += c;
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < raw.length(); i++) {
            if (i == 4 || i == 7) formatted.append('-');
            formatted.append(raw.charAt(i));
        }
        idField.setLength(0);
        idField.append(formatted);
    }

    private void submitSignupStep() {
        switch (signupStep) {
            case 0: validateStep0(); break;
            case 1: validateStep1(); break;
            case 2: validateStep2(); break;
        }
    }

    private void validateStep0() {
        if (fnField.toString().trim().isEmpty())  { errorMessage = "First name is required.";  return; }
        if (lnField.toString().trim().isEmpty())  { errorMessage = "Last name is required.";   return; }
        signupStep  = 1;
        signupFocus = 0;
    }

    private void validateStep1() {
        if (ysField.toString().trim().isEmpty()) { errorMessage = "Year & Section is required."; return; }
        if (!ysField.toString().trim().matches("\\d-\\d{2}")) { errorMessage = "Year & Section must be in X-XX format."; return; }
        if (!UserManager.isValidStudentId(idField.toString())) {
            errorMessage = "Invalid ID format. Use: 202X-100-XXXX";
            return;
        }
        String username = UserManager.usernameFromStudentId(idField.toString());
        if (userManager.userExists(username)) {
            errorMessage = "That student ID is already registered.";
            return;
        }
        signupStep  = 2;
        signupFocus = 0;
    }

    private void validateStep2() {
        if (pw1Field.length() < 8) { errorMessage = "Password must be at least 8 characters."; return; }
        if (!pw1Field.toString().equals(pw2Field.toString())) { errorMessage = "Passwords do not match."; return; }

        String error = userManager.createAccount(
                fnField.toString().trim(),
                lnField.toString().trim(),
                miField.toString().trim(),
                sfxField.toString().trim(),
                ysField.toString().trim(),
                idField.toString().trim(),
                pw1Field.toString()
        );

        if (error != null) {
            errorMessage = error;
        } else {
            // Auto-login after successful registration
            String username = UserManager.usernameFromStudentId(idField.toString());
            userManager.login(username, pw1Field.toString());
            onLoginSuccess();
        }
    }

    // ADMIN

    private void handleAdminKey(int code, char keyChar) {
        adminError   = "";
        adminSuccess = "";

        if (code == KeyEvent.VK_ESCAPE) {
            if (adminMode == 3) {
                if (confirmingDelete) {
                    confirmingDelete = false;
                } else if (resettingPassword) {
                    resettingPassword = false;
                    newPasswordField.setLength(0);
                    confirmPasswordField.setLength(0);
                    resetPwFocusOnConfirm = false;
                    adminError = "";
                } else {
                    adminMode = 2;
                    selectedAccount = null;
                    selectedProfile = null;
                }
            } else {
                adminMode = 0;
            }
            return;
        }

        if (adminMode == 1) {
            if (code == KeyEvent.VK_BACK_SPACE) {
                if (adminPassField.length() > 0) adminPassField.deleteCharAt(adminPassField.length() - 1);
                return;
            }
            if (code == KeyEvent.VK_ENTER) {
                if (adminManager.verifyAdmin(adminPassField.toString())) {
                    adminMode = 2;
                    adminPassField.setLength(0);
                    searchField.setLength(0);
                    refreshList("");
                    listCursor = 0;
                    listScrollOffset = 0;
                } else {
                    adminError = "Incorrect admin password.";
                }
                return;
            }
            if (keyChar >= 32 && keyChar != 127 && adminPassField.length() < 32)
                adminPassField.append(keyChar);
            return;
        }

        if (adminMode == 2) {
            if (code == KeyEvent.VK_BACK_SPACE) {
                if (searchField.length() > 0) {
                    searchField.deleteCharAt(searchField.length() - 1);
                    refreshList(searchField.toString());
                    listCursor = 0;
                    listScrollOffset = 0;
                }
                return;
            }
            if (code == KeyEvent.VK_UP) {
                if (listCursor > 0) {
                    listCursor--;
                    if (listCursor < listScrollOffset) listScrollOffset--;
                }
                return;
            }
            if (code == KeyEvent.VK_DOWN) {
                if (listCursor < filteredList.size() - 1) {
                    listCursor++;
                    if (listCursor >= listScrollOffset + MAX_VISIBLE) listScrollOffset++;
                }
                return;
            }
            if (code == KeyEvent.VK_ENTER) {
                if (!filteredList.isEmpty()) {
                    selectedAccount = filteredList.get(listCursor);
                    selectedProfile = adminManager.getProfile(selectedAccount);
                    confirmingDelete = false;
                    adminMode = 3;
                }
                return;
            }
            if (keyChar >= 32 && keyChar != 127 && searchField.length() < 30) {
                searchField.append(keyChar);
                refreshList(searchField.toString());
                listCursor = 0;
                listScrollOffset = 0;
            }
            return;
        }

        if (adminMode == 3) {
            if (resettingPassword) {
                // Password reset form
                if (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN) {
                    resetPwFocusOnConfirm = !resetPwFocusOnConfirm;
                    return;
                }
                if (code == KeyEvent.VK_BACK_SPACE) {
                    StringBuilder active = resetPwFocusOnConfirm ? confirmPasswordField : newPasswordField;
                    if (active.length() > 0) active.deleteCharAt(active.length() - 1);
                    adminError = "";
                    return;
                }
                if (code == KeyEvent.VK_ENTER) {
                    String np = newPasswordField.toString();
                    String cp = confirmPasswordField.toString();
                    if (np.length() < 8) {
                        adminError = "Password must be at least 8 characters.";
                    } else if (!np.equals(cp)) {
                        adminError = "Passwords do not match.";
                    } else {
                        String err = adminManager.resetPassword(selectedAccount, np);
                        if (err != null) {
                            adminError = err;
                        } else {
                            adminSuccess = "Password reset for '" + selectedAccount + "'.";
                            resettingPassword = false;
                            newPasswordField.setLength(0);
                            confirmPasswordField.setLength(0);
                            resetPwFocusOnConfirm = false;
                            adminError = "";
                        }
                    }
                    return;
                }
                if (keyChar >= 32 && keyChar != 127) {
                    StringBuilder active = resetPwFocusOnConfirm ? confirmPasswordField : newPasswordField;
                    if (active.length() < 32) active.append(keyChar);
                }
                return;
            }

            if (!confirmingDelete) {
                if (code == KeyEvent.VK_D) {
                    confirmingDelete = true;
                }
                if (code == KeyEvent.VK_P) {
                    resettingPassword = true;
                    newPasswordField.setLength(0);
                    confirmPasswordField.setLength(0);
                    resetPwFocusOnConfirm = false;
                    adminError = "";
                    adminSuccess = "";
                }
            } else {
                if (code == KeyEvent.VK_ENTER && selectedAccount != null) {
                    adminManager.resetAccount(selectedAccount);
                    adminSuccess = "'" + selectedAccount + "' deleted.";
                    selectedAccount = null;
                    selectedProfile = null;
                    confirmingDelete = false;
                    adminMode = 2;
                    refreshList(searchField.toString());
                    listCursor = 0;
                    listScrollOffset = 0;
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        // Background
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        if (loginBG != null)
            g2.drawImage(loginBG, 0, 0, gp.screenWidth, gp.screenHeight, null);
        else if (gp.ui.cutsceneBG != null)
            g2.drawImage(gp.ui.cutsceneBG, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.setColor(new Color(0, 0, 0, 70));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        if (adminMode > 0) { drawAdminPanel(g2); return; }
        if (mode == 0)      drawMenu(g2);
        else if (mode == 1) drawLoginForm(g2);
        else if (mode == 2) drawSignupForm(g2);
    }

    // MENU

    private void drawMenu(Graphics2D g2) {
        int cx = gp.screenWidth / 2;

        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 62f));
        g2.setColor(new Color(0, 0, 0, 100));
        String title = "Ink & Blood: Rizal's Adventure";
        g2.drawString(title, cx - strW(g2, title) / 2 + 4, gp.tileSize * 3 + 5);
        g2.setColor(Color.black);
        g2.drawString(title, cx - strW(g2, title) / 2, gp.tileSize * 3);

        String[] items = { "Log In", "Sign Up", "Admin Reset", "Quit" };
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 38f));
        int itemY = gp.tileSize * 6 + 10;
        for (int i = 0; i < items.length; i++) {
            boolean sel = (i == menuCursor);
            g2.setColor(sel ? GOLD : Color.white);
            int ix = cx - strW(g2, items[i]) / 2;
            g2.drawString(items[i], ix, itemY);
            if (sel) g2.drawString(">", ix - gp.tileSize, itemY);
            itemY += gp.tileSize;
        }

        g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 20f));
        g2.setColor(LIGHT_GREY);
        String hint = "[ UP/DOWN ] Navigate    [ ENTER ] Select";
        g2.drawString(hint, cx - strW(g2, hint) / 2, gp.screenHeight - 20);
    }

    // LOGIN FORM

    private void drawLoginForm(Graphics2D g2) {
        int panelW = gp.tileSize * 10;
        int panelH = gp.tileSize * 7;
        int panelX = gp.screenWidth  / 2 - panelW / 2;
        int panelY = gp.screenHeight / 2 - panelH / 2;
        gp.ui.drawSubWindow(g2, panelX, panelY, panelW, panelH);

        int pad = gp.tileSize;
        int cx  = panelX + panelW / 2;

        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 36f));
        g2.setColor(GOLD);
        String t = "Log In";
        g2.drawString(t, cx - strW(g2, t) / 2, panelY + pad);

        g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 18f));
        g2.setColor(LIGHT_GREY);
        String sub = "Username: last 4 digits of your Student ID";
        g2.drawString(sub, cx - strW(g2, sub) / 2, panelY + pad + 24);

        int fieldW = panelW - pad * 2;
        int fieldX = panelX + pad;
        int fieldY = panelY + pad + 52;

        drawField(g2, "ID Tail (4 digits)", usernameField.toString(),
                false, !focusOnPassword, fieldX, fieldY, fieldW);

        fieldY += gp.tileSize + 32;
        drawField(g2, "Password", getMaskedValue(passwordField.toString()),
                true, focusOnPassword, fieldX, fieldY, fieldW);

        if (!errorMessage.isEmpty()) {
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 22f));
            g2.setColor(ERROR_RED);
            g2.drawString(errorMessage, cx - strW(g2, errorMessage) / 2, fieldY + gp.tileSize + 36);
        }

        g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 18f));
        g2.setColor(LIGHT_GREY);
        String hint = "[ UP/DOWN ] Switch    [ ENTER ] Login    [ ESC ] Back";
        g2.drawString(hint, cx - strW(g2, hint) / 2, panelY + panelH - 14);
    }

    // SIGN UP FORM

    private void drawSignupForm(Graphics2D g2) {
        int panelW = gp.tileSize * 14;
        int panelH = gp.tileSize * 8;
        int panelX = gp.screenWidth  / 2 - panelW / 2;
        int panelY = gp.screenHeight / 2 - panelH / 2;
        gp.ui.drawSubWindow(g2, panelX, panelY, panelW, panelH);

        int cx = panelX + panelW / 2;

        // STEP INDICATOR
        drawStepIndicator(g2, panelX, panelY + gp.tileSize/2, panelW);

        // ---- Step title ----
        String[] stepTitles = { "Personal Information", "Academic Details", "Set Password" };
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 30f));
        g2.setColor(GOLD);
        String title = stepTitles[signupStep];
        g2.drawString(title, cx - strW(g2, title) / 2, panelY + gp.tileSize + 52);

        int gridW   = (int)(panelW * 0.80);
        int gridX   = panelX + (panelW - gridW) / 2;
        int colGap  = 20;
        int cellW   = (gridW - colGap) / 2;

        int rowH    = gp.tileSize + 14;
        int rowGap  = 28;
        int row1Y   = panelY + gp.tileSize * 3;
        int row2Y   = row1Y + rowH + rowGap;

        int col1X   = gridX;
        int col2X   = gridX + cellW + colGap;

        switch (signupStep) {

            // ---- Step 0: Personal info — 2×2 equal grid ----
            case 0: {
                // Row 1: First Name | Last Name
                drawField(g2, "First Name *", fnField.toString(),   false, signupFocus == 0, col1X, row1Y, cellW);
                drawField(g2, "Last Name *",  lnField.toString(),   false, signupFocus == 1, col2X, row1Y, cellW);

                // Row 2: Middle Initial | Suffix  — same width as row 1
                drawField(g2, "M.I.",                    miField.toString(),  false, signupFocus == 2, col1X, row2Y, cellW);
                drawField(g2, "Suffix  (Jr., III, etc.)", sfxField.toString(), false, signupFocus == 3, col2X, row2Y, cellW);

                // "* Required fields" note below row 2
                int noteY = row2Y + rowH + 8;
                g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 17f));
                g2.setColor(LIGHT_GREY);
                g2.drawString("* Required fields", gridX, noteY);
                break;
            }

            // ---- Step 1: Academic details — 2×2 equal grid ----
            case 1: {
                // Row 1: Year & Section | Student ID
                drawField(g2, "Year & Section * (X-XX)",              ysField.toString(), false, signupFocus == 0,
                        col1X, row1Y, cellW);
                drawField(g2, "Student ID *  (202X-100-XXXX)", idField.toString(), false, signupFocus == 1, col2X, row1Y, cellW);

                // Live username preview sits below row 1 on the right column
                String rawId = idField.toString().trim();
                int previewY = row1Y + rowH + 10;
                if (UserManager.isValidStudentId(rawId)) {
                    String derived = UserManager.usernameFromStudentId(rawId);
                    g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 19f));
                    g2.setColor(OK_GREEN);
                    g2.drawString("Login username:  " + derived, col2X, previewY);
                } else if (!rawId.isEmpty()) {
                    g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 18f));
                    g2.setColor(DIM_GREY);
                    g2.drawString("Enter full ID to see username", col2X, previewY);
                }
                break;
            }

            // ---- Step 2: Password ----
            case 2: {
                // Row 1: Password | Confirm Password
                drawField(g2, "Password  (min. 8 characters)", getMaskedValue(pw1Field.toString()), true, signupFocus == 0, col1X, row1Y, cellW);
                drawField(g2, "Confirm Password", getMaskedValue(pw2Field.toString()),
                        true, signupFocus == 1, col2X, row1Y, cellW);

                // Password match indicator below row 1
                if (pw1Field.length() > 0 && pw2Field.length() > 0) {
                    boolean match = pw1Field.toString().equals(pw2Field.toString());
                    int matchY = row1Y + rowH + 10;
                    g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 19f));
                    g2.setColor(match ? OK_GREEN : ERROR_RED);
                    g2.drawString(match ? "Passwords match" : "Passwords do not match",
                            cx - strW(g2, match ? "Passwords match" : "Passwords do not match") / 2, matchY);
                }
                break;
            }
        }

        // ---- Error message ----
        if (!errorMessage.isEmpty()) {
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 21f));
            g2.setColor(ERROR_RED);
            g2.drawString(errorMessage, cx - strW(g2, errorMessage) / 2, panelY + panelH - 36);
        }

        // ---- Nav hint ----
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 17f));
        g2.setColor(LIGHT_GREY);
        String hint = (signupStep == 0)
                ? "[ UP/DOWN ] Switch field    [ ENTER ] Next    [ ESC ] Back"
                : (signupStep < 2)
                ? "[ UP/DOWN ] Switch field    [ ENTER ] Next    [ ESC ] Previous step"
                : "[ UP/DOWN ] Switch field    [ ENTER ] Create Account    [ ESC ] Previous step";
        g2.drawString(hint, cx - strW(g2, hint) / 2, panelY + panelH - 14);
    }

    private void drawStepIndicator(Graphics2D g2, int panelX, int panelY, int panelW) {
        int r      = 10;
        int gap    = 50;
        int totalW = 3 * r * 2 + 2 * gap;
        int startX = panelX + panelW / 2 - totalW / 2 + r;
        int stepY  = panelY + 18;
        String[] labels = { "1", "2", "3" };

        for (int i = 0; i < 3; i++) {
            int cx = startX + i * (r * 2 + gap);
            boolean done   = i < signupStep;
            boolean active = i == signupStep;

            g2.setColor(done   ? OK_GREEN
                    : active ? GOLD
                    : DIM_GREY);
            g2.fillOval(cx - r, stepY - r, r * 2, r * 2);

            g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 16f));
            g2.setColor(Color.black);
            g2.drawString(labels[i], cx - strW(g2, labels[i]) / 2, stepY + 5);

            // Connector line
            if (i < 2) {
                g2.setColor(i < signupStep ? OK_GREEN : DIM_GREY);
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(cx + r, stepY, cx + r + gap, stepY);
            }
        }
    }

    // ADMIN PANEL

    private void drawAdminPanel(Graphics2D g2) {
        int panelW = gp.tileSize * 12;
        int panelH = gp.tileSize * 9;
        int panelX = gp.screenWidth  / 2 - panelW / 2;
        int panelY = gp.screenHeight / 2 - panelH / 2;
        int cx     = panelX + panelW / 2;
        int pad    = gp.tileSize;

        gp.ui.drawSubWindow(g2, panelX, panelY, panelW, panelH);

        if (adminMode == 1) {
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 30f));
            g2.setColor(new Color(255, 100, 100));
            String t = "Admin Access";
            g2.drawString(t, cx - strW(g2, t) / 2, panelY + pad);

            g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 20f));
            g2.setColor(LIGHT_GREY);
            String sub = "Enter the admin password to continue.";
            g2.drawString(sub, cx - strW(g2, sub) / 2, panelY + pad + 26);

            int fieldW = panelW - pad * 2;
            int fieldX = panelX + pad;
            int fieldY = panelY + pad + 56;
            drawField(g2, "Admin Password", getMaskedValue(adminPassField.toString()),
                    true, true, fieldX, fieldY, fieldW);

            if (!adminError.isEmpty()) {
                g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 22f));
                g2.setColor(ERROR_RED);
                g2.drawString(adminError, cx - strW(g2, adminError) / 2, fieldY + gp.tileSize + 36);
            }

            g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 18f));
            g2.setColor(LIGHT_GREY);
            String hint = "[ ENTER ] Confirm    [ ESC ] Cancel";
            g2.drawString(hint, cx - strW(g2, hint) / 2, panelY + panelH - 14);
            return;
        }

        if (adminMode == 2) {
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 28f));
            g2.setColor(new Color(255, 100, 100));
            String t = "Manage Student Accounts";
            g2.drawString(t, cx - strW(g2, t) / 2, panelY + pad);

            // Search field
            int fieldW  = panelW - pad * 2;
            int fieldX  = panelX + pad;
            int fieldY  = panelY + pad + 28;
            drawField(g2, "Search by First Name or Last Name", searchField.toString(),
                    false, true, fieldX, fieldY, fieldW);

            int cardX     = panelX + pad;
            int cardW     = panelW - pad * 2;
            int cardH     = gp.tileSize - 4;
            int cardGap   = 6;
            int cardStartY = fieldY + gp.tileSize + 8;

            if (searchField.length() == 0) {
                g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 22f));
                g2.setColor(new Color(150, 150, 150));
                String prompt = "Type a name to search for students.";
                g2.drawString(prompt, cx - strW(g2, prompt) / 2, cardStartY + cardH);

            } else if (filteredList.isEmpty()) {
                g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 22f));
                g2.setColor(new Color(150, 150, 150));
                String none = "No matching students found.";
                g2.drawString(none, cx - strW(g2, none) / 2, cardStartY + cardH);

            } else {
                for (int i = 0; i < MAX_VISIBLE; i++) {
                    int idx = listScrollOffset + i;
                    if (idx >= filteredList.size()) break;

                    String uname = filteredList.get(idx);
                    boolean sel  = (idx == listCursor);
                    int cardY    = cardStartY + i * (cardH + cardGap);

                    g2.setColor(sel ? new Color(180, 140, 40, 220) : new Color(40, 40, 40, 180));
                    g2.fillRoundRect(cardX, cardY, cardW, cardH, 8, 8);
                    g2.setColor(sel ? GOLD : DIM_GREY);
                    g2.setStroke(new BasicStroke(sel ? 2.5f : 1f));
                    g2.drawRoundRect(cardX, cardY, cardW, cardH, 8, 8);

                    // Show display name if profile exists
                    UserManager.StudentProfile sp = adminManager.getProfile(uname);
                    String displayName = (sp != null) ? sp.displayName() : uname;
                    String section     = (sp != null) ? "  [" + sp.yearSection + "]" : "";

                    g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 22f));
                    g2.setColor(sel ? Color.black : Color.white);
                    g2.drawString(displayName, cardX + 12, cardY + cardH - 8);

                    g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 18f));
                    g2.setColor(sel ? new Color(60, 40, 0) : new Color(140, 140, 140));
                    g2.drawString(section, cardX + 12 + strW(g2, displayName) + 4, cardY + cardH - 8);
                }

                if (filteredList.size() > MAX_VISIBLE) {
                    g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 17f));
                    g2.setColor(LIGHT_GREY);
                    String sc = "";
                    if (listScrollOffset > 0) sc += "▲ ";
                    if (listScrollOffset + MAX_VISIBLE < filteredList.size()) sc += "▼";
                    g2.drawString(sc.trim(), cx - strW(g2, sc.trim()) / 2,
                            cardStartY + MAX_VISIBLE * (cardH + cardGap));
                }
            }

            if (!adminSuccess.isEmpty()) {
                g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 20f));
                g2.setColor(OK_GREEN);
                g2.drawString(adminSuccess, cx - strW(g2, adminSuccess) / 2, panelY + panelH - 36);
            }

            g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 17f));
            g2.setColor(LIGHT_GREY);
            String hint = "[ UP/DOWN ] Navigate    [ ENTER ] View Profile    [ ESC ] Back";
            g2.drawString(hint, cx - strW(g2, hint) / 2, panelY + panelH - 14);
            return;
        }

        if (adminMode == 3) {
            if (!confirmingDelete) {
                drawProfileView(g2, panelX, panelY, panelW, panelH, cx, pad);
            } else {
                drawDeleteConfirm(g2, panelX, panelY, panelW, panelH, cx, pad);
            }
        }
    }

    private void drawProfileView(Graphics2D g2, int panelX, int panelY, int panelW, int panelH, int cx, int pad) {
        // Route to password reset form if active
        if (resettingPassword) {
            drawPasswordResetForm(g2, panelX, panelY, panelW, panelH, cx, pad);
            return;
        }

        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 28f));
        g2.setColor(new Color(255, 100, 100));
        String t = "Student Profile";
        g2.drawString(t, cx - strW(g2, t) / 2, panelY + pad);

        int ly = panelY + pad + 36;
        int lx = panelX + pad;
        int labelW = 180;

        if (selectedProfile == null) {
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.PLAIN, 24f));
            g2.setColor(Color.white);
            g2.drawString("Username: " + selectedAccount, lx, ly);
            ly += 30;
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 20f));
            g2.setColor(LIGHT_GREY);
            g2.drawString("(No profile on file for this account)", lx, ly);
        } else {
            UserManager.StudentProfile sp = selectedProfile;

            // Display name banner
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 26f));
            g2.setColor(GOLD);
            g2.drawString(sp.displayName(), cx - strW(g2, sp.displayName()) / 2, ly);
            ly += 34;

            // Profile rows
            drawProfileRow(g2, lx, ly, labelW, "First Name:",     sp.firstName);     ly += 28;
            drawProfileRow(g2, lx, ly, labelW, "Last Name:",      sp.lastName);      ly += 28;
            drawProfileRow(g2, lx, ly, labelW, "Middle Initial:", sp.middleInitial.isEmpty() ? "—" : sp.middleInitial); ly += 28;
            drawProfileRow(g2, lx, ly, labelW, "Suffix:",         sp.suffix.isEmpty() ? "—" : sp.suffix);              ly += 28;
            drawProfileRow(g2, lx, ly, labelW, "Year & Section:", sp.yearSection);   ly += 28;
            drawProfileRow(g2, lx, ly, labelW, "Student ID:",     sp.studentId);     ly += 28;
            drawProfileRow(g2, lx, ly, labelW, "Login Username:", sp.username);      ly += 10;

            // Save file indicator
            File sf = new File("saves" + File.separator + sp.username + ".dat");
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 19f));

            if (sf.exists()) {
                // Read the quest progress from the save file
                String questLabel = "Unknown progress";
                try (java.io.ObjectInputStream ois = new java.io.ObjectInputStream(new java.io.FileInputStream(sf))) {
                    SaveData sd = (SaveData) ois.readObject();
                    switch (sd.currentQuest) {
                        case 0: questLabel = "Chapter 1 — Quest 1: Find the Siblings";      break;
                        case 1: questLabel = "Chapter 1 — Quest 2: Art and Writing";         break;
                        case 2: questLabel = "Chapter 2 — Quest 3: Enrollment at Ateneo";    break;
                        case 3: questLabel = "Chapter 2 — Quest 4: The Competitions";        break;
                        case 4: questLabel = "Chapter 3 — Quest 5: Writing Noli Me Tangere"; break;
                        case 5: questLabel = "Chapter 3 — Quest 6: El Filibusterismo";       break;
                        case 6: questLabel = "Chapter 4 — Quest 7: The Final Days";          break;
                        default: questLabel = "Quest " + (sd.currentQuest + 1);              break;
                    }
                } catch (Exception e) {
                    questLabel = "Save file unreadable";
                }

                g2.setColor(OK_GREEN);
                g2.drawString("Progress: " + questLabel, (int)(lx + gp.tileSize * 1.5), ly + 28);

            } else {
                g2.setColor(LIGHT_GREY);
                g2.drawString("No saved game data yet", (int)(lx + gp.tileSize * 3.5), ly + 28);
            }
        }

        if (!adminSuccess.isEmpty()) {
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 20f));
            g2.setColor(OK_GREEN);
            g2.drawString(adminSuccess, cx - strW(g2, adminSuccess) / 2, panelY + panelH - 58);
        }

        // Action hints at the bottom
        int hintY = panelY + panelH - 36;
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 19f));
        g2.setColor(new Color(80, 160, 255));
        String pHint = "[ P ] Reset Password";
        g2.drawString(pHint, cx - strW(g2, pHint) / 2 - 100, hintY);

        g2.setColor(ERROR_RED);
        String dHint = "[ D ] Delete Account";
        g2.drawString(dHint, cx - strW(g2, dHint) / 2 + 100, hintY);

        g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 17f));
        g2.setColor(LIGHT_GREY);
        String hint = "[ ESC ] Back to Search";
        g2.drawString(hint, cx - strW(g2, hint) / 2, panelY + panelH - 14);
    }

    private void drawPasswordResetForm(Graphics2D g2, int panelX, int panelY, int panelW, int panelH, int cx, int pad) {
        // Title
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 28f));
        g2.setColor(new Color(80, 160, 255));
        String t = "Reset Password";
        g2.drawString(t, cx - strW(g2, t) / 2, panelY + pad);

        // Who we are resetting for
        String forName = (selectedProfile != null) ? selectedProfile.displayName() : selectedAccount;
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 20f));
        g2.setColor(LIGHT_GREY);
        String sub = "for: " + forName;
        g2.drawString(sub, cx - strW(g2, sub) / 2, panelY + pad + 28);

        int fieldW = panelW - pad * 2;
        int fieldX = panelX + pad;
        int fieldY = panelY + pad + 70;

        // New password field
        drawField(g2, "New Password (min. 8 chars)",
                getMaskedValue(newPasswordField.toString()),
                true, !resetPwFocusOnConfirm, fieldX, fieldY, fieldW);

        fieldY += gp.tileSize + 32;

        // Confirm password field
        drawField(g2, "Confirm New Password",
                getMaskedValue(confirmPasswordField.toString()),
                true, resetPwFocusOnConfirm, fieldX, fieldY, fieldW);

        // Error message
        if (!adminError.isEmpty()) {
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 20f));
            g2.setColor(ERROR_RED);
            g2.drawString(adminError, cx - strW(g2, adminError) / 2, fieldY + gp.tileSize + 30);
        }

        // Hints
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 17f));
        g2.setColor(LIGHT_GREY);
        String hint = "[ UP/DOWN ] Switch field    [ ENTER ] Confirm    [ ESC ] Cancel";
        g2.drawString(hint, cx - strW(g2, hint) / 2, panelY + panelH - 14);
    }

    private void drawProfileRow(Graphics2D g2, int x, int y, int labelW, String label, String value) {
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 20f));
        g2.setColor(LIGHT_GREY);
        g2.drawString(label, x, y);

        g2.setFont(gp.ui.maruMonica.deriveFont(Font.PLAIN, 26f));
        g2.setColor(Color.white);
        g2.drawString(value, x + labelW, y);
    }

    private void drawDeleteConfirm(Graphics2D g2, int panelX, int panelY, int panelW, int panelH, int cx, int pad) {
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 28f));
        g2.setColor(ERROR_RED);
        String t = "Delete Account?";
        g2.drawString(t, cx - strW(g2, t) / 2, panelY + pad);

        String displayName = (selectedProfile != null) ? selectedProfile.displayName() : selectedAccount;

        g2.setFont(gp.ui.maruMonica.deriveFont(Font.PLAIN, 24f));
        g2.setColor(Color.white);
        String msg1 = "You are about to delete:";
        g2.drawString(msg1, cx - strW(g2, msg1) / 2, panelY + pad + 50);

        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 28f));
        g2.setColor(GOLD);
        g2.drawString(displayName, cx - strW(g2, displayName) / 2, panelY + pad + 88);

        if (selectedProfile != null) {
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.PLAIN, 20f));
            g2.setColor(LIGHT_GREY);
            String sub = selectedProfile.yearSection + "  |  ID: " + selectedProfile.studentId;
            g2.drawString(sub, cx - strW(g2, sub) / 2, panelY + pad + 116);
        }

        g2.setFont(gp.ui.maruMonica.deriveFont(Font.PLAIN, 22f));
        g2.setColor(new Color(200, 200, 200));
        String msg2 = "This will delete their account and save file.";
        g2.drawString(msg2, cx - strW(g2, msg2) / 2, panelY + pad + 152);

        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 22f));
        g2.setColor(ERROR_RED);
        String msg3 = "This cannot be undone.";
        g2.drawString(msg3, cx - strW(g2, msg3) / 2, panelY + pad + 180);

        g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 18f));
        g2.setColor(LIGHT_GREY);
        String hint = "[ ENTER ] Confirm Delete    [ ESC ] Cancel";
        g2.drawString(hint, cx - strW(g2, hint) / 2, panelY + panelH - 14);
    }


    private void drawField(Graphics2D g2, String label, String value,boolean isPassword, boolean focused, int x, int y, int w) {
        int h = gp.tileSize - 4;

        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 19f));
        g2.setColor(LIGHT_GREY);
        g2.drawString(label, x, y - 4);

        Color boxColor = focused ? GOLD : DIM_GREY;
        g2.setColor(new Color(20, 20, 20, 200));
        g2.fillRoundRect(x, y, w, h, 8, 8);
        g2.setColor(boxColor);
        g2.setStroke(new BasicStroke(focused ? 2.5f : 1.5f));
        g2.drawRoundRect(x, y, w, h, 8, 8);

        g2.setFont(gp.ui.maruMonica.deriveFont(Font.PLAIN, 24f));
        g2.setColor(Color.white);
        String display = value + (focused && blinkCursor() ? "|" : "");
        g2.drawString(display, x + 10, y + h - 8);
    }

    private void onLoginSuccess() {
        gp.resetForLoad();
        gp.ui.titleScreenState = 0;
        gp.ui.commandNum = 0;
        gp.gameState = gp.titleState;
    }

    private void clearLoginFields() {
        usernameField.setLength(0);
        passwordField.setLength(0);
        focusOnPassword = false;
    }

    private void clearSignupFields() {
        fnField.setLength(0);  lnField.setLength(0);
        miField.setLength(0);  sfxField.setLength(0);
        ysField.setLength(0);  idField.setLength(0);
        pw1Field.setLength(0); pw2Field.setLength(0);
        signupStep  = 0;
        signupFocus = 0;
    }

    private int fieldsInStep(int step) {
        switch (step) {
            case 0: return 4; // FN, LN, MI, Suffix
            case 1: return 2; // Year&Section, ID
            case 2: return 2; // Password, Confirm
            default: return 1;
        }
    }

    private StringBuilder activeSignupField() {
        switch (signupStep) {
            case 0:
                switch (signupFocus) {
                    case 0: return fnField;
                    case 1: return lnField;
                    case 2: return miField;
                    case 3: return sfxField;
                }
            case 1:
                switch (signupFocus) {
                    case 0: return ysField;
                    case 1: return idField;
                }
            case 2:
                switch (signupFocus) {
                    case 0: return pw1Field;
                    case 1: return pw2Field;
                }
        }
        return null;
    }

    private int fieldLimit(int step, int focus) {
        if (step == 0 && focus == 0) return 20;  // First Name
        if (step == 0 && focus == 1) return 20;  // Last Name
        if (step == 0 && focus == 2) return 2;   // M.I
        if (step == 0 && focus == 3) return 3;   // Suffix
        if (step == 1 && focus == 0) return 10;   // Year & Section
        if (step == 1 && focus == 1) return 13;  // Student ID
        if (step == 2) return 13;                // Password
        return 20;
    }

    private void refreshList(String search) {
        accountList  = adminManager.getAllUsernames();
        filteredList = search.isEmpty()
                ? new java.util.ArrayList<>(accountList)
                : adminManager.searchByName(search);
    }

    private boolean blinkCursor() {
        return (System.currentTimeMillis() / 500) % 2 == 0;
    }

    private int strW(Graphics2D g2, String s) {
        return g2.getFontMetrics().stringWidth(s);
    }

    private String getMaskedValue(String raw) {
        if (raw.isEmpty()) return "";
        StringBuilder masked = new StringBuilder();
        for (int i = 0; i < raw.length() - 1; i++) {
            masked.append('•');
        }
        masked.append(raw.charAt(raw.length() - 1));
        return masked.toString();
    }
}
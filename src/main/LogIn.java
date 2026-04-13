package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;


public class LogIn {

    private BufferedImage loginBG;

    // ADMIN MANAGER
    private AdminManager adminManager;
    private int adminMode = 0;
    private final StringBuilder adminPassField    = new StringBuilder();
    private final StringBuilder resetUsernameField = new StringBuilder();
    private String adminError   = "";
    private String adminSuccess = "";
    private boolean adminPassConfirmed = false;

    private final GamePanel   gp;
    private final UserManager userManager;
    private final SaveManager saveManager;

    public int mode = 0;

    private final StringBuilder usernameField = new StringBuilder();
    private final StringBuilder passwordField = new StringBuilder();
    private boolean focusOnUsername = true;

    private String errorMessage = "";
    private int    menuCursor   = 0;

    // Colours
    private static final Color GOLD        = new Color(255, 220, 80);
    private static final Color LIGHT_GREY  = new Color(180, 180, 180);
    private static final Color ERROR_RED   = new Color(230, 80,  80);
    private static final Color OK_GREEN    = new Color(80,  220, 80);

    public LogIn(GamePanel gp, UserManager userManager, SaveManager saveManager) {
        this.gp          = gp;
        this.userManager = userManager;
        this.saveManager = saveManager;
        this.adminManager = new AdminManager(userManager);

        try {
            loginBG = ImageIO.read(getClass().getResourceAsStream("/images/introduction.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        mode = 0;
        menuCursor = 0;
        clearFields();
        errorMessage = "";
    }

    private void clearFields() {
        usernameField.setLength(0);
        passwordField.setLength(0);
        focusOnUsername = true;
    }

    //KEY HANDLERS

    public void handleKey(int code, char keyChar) {
        if (adminMode > 0) {
            handleAdminKey(code, keyChar);
        } else if (mode == 0) {
            handleMenuKey(code);
        } else {
            handleFormKey(code, keyChar);
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
            if (menuCursor == 0) {
                mode = 1; clearFields(); errorMessage = "";
            }
            if (menuCursor == 1) {
                mode = 2; clearFields(); errorMessage = "";
            }
            if (menuCursor == 2) {
                adminMode = 1;
                adminPassField.setLength(0);
                resetUsernameField.setLength(0);
                adminError = "";
                adminSuccess = "";
                adminPassConfirmed = false;
            }
            if (menuCursor == 3) {
                System.exit(0);
            }
        }
    }

    private void handleFormKey(int code, char keyChar) {
        errorMessage = "";

        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_UP) {
            focusOnUsername = !focusOnUsername;
            return;
        }

        if (code == KeyEvent.VK_ESCAPE) {
            mode = 0;
            clearFields();
            return;
        }

        // BACKSPACE
        if (code == KeyEvent.VK_BACK_SPACE) {
            StringBuilder active = focusOnUsername ? usernameField : passwordField;
            if (active.length() > 0) active.deleteCharAt(active.length() - 1);
            return;
        }

        // ENTER = submit
        if (code == KeyEvent.VK_ENTER) {
            submit();
            return;
        }

        // Printable characters (cap field lengths)
        if (keyChar >= 32 && keyChar != 127) {
            if (focusOnUsername && usernameField.length() < 16) {
                usernameField.append(keyChar);
            } else if (!focusOnUsername && passwordField.length() < 32) {
                passwordField.append(keyChar);
            }
        }
    }

    private void submit() {
        String username = usernameField.toString().trim();
        String password = passwordField.toString();

        if (mode == 1) {
            // Sign in
            String error = userManager.login(username, password);
            if (error != null) {
                errorMessage = error;
            } else {
                onLoginSuccess();
            }
        } else if (mode == 2) {
            String error = userManager.createAccount(username, password);
            if (error != null) {
                errorMessage = error;
            } else {
                userManager.login(username, password);
                onLoginSuccess();
            }
        }
    }

    private void onLoginSuccess() {
        gp.gameState = gp.titleState;
    }

    public void draw(Graphics2D g2) {
        // Background
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        if (loginBG != null) {
            g2.drawImage(loginBG, 0, 0, gp.screenWidth, gp.screenHeight, null);
        } else if (gp.ui.cutsceneBG != null) {
            g2.drawImage(gp.ui.cutsceneBG, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }

        g2.setColor(new Color(0, 0, 0, 70));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        if (adminMode > 0) {
            drawAdminPanel(g2); return;
        }
        if (mode == 0) drawMenu(g2);
        else           drawForm(g2);
    }

    private void drawMenu(Graphics2D g2) {
        int cx = gp.screenWidth / 2;

        // Title
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 62f));
        g2.setColor(new Color(0,0,0));
        String title = "Ink & Blood: Rizal's Adventure";
        g2.drawString(title, cx - strW(g2, title) / 2, gp.tileSize * 3);
        g2.setColor(new Color(0,0,0, 150));
        g2.drawString(title, (cx - strW(g2, title) / 2)+4, (gp.tileSize * 3)+5);


        // Menu items
        String[] items = { "Log In", "Sign Up","Admin Reset", "Quit" };
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 38f));
        int itemY = gp.tileSize * 6 + 10;
        for (int i = 0; i < items.length; i++) {
            boolean selected = (i == menuCursor);
            g2.setColor(selected ? GOLD : Color.white);
            int ix = cx - strW(g2, items[i]) / 2;
            g2.drawString(items[i], ix, itemY);
            if (selected) {
                g2.drawString(">", ix - gp.tileSize, itemY);
            }
            itemY += gp.tileSize;
        }

        // Hint
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 20f));
        g2.setColor(LIGHT_GREY);
        String hint = "[ W / S ] Navigate    [ ENTER ] Select";
        g2.drawString(hint, cx - strW(g2, hint) / 2, gp.screenHeight - 20);
    }

    private void drawAdminPanel(Graphics2D g2) {
        int panelW = gp.tileSize * 10;
        int panelH = gp.tileSize * 7;
        int panelX = gp.screenWidth  / 2 - panelW / 2;
        int panelY = gp.screenHeight / 2 - panelH / 2;
        int cx     = panelX + panelW / 2;
        int pad    = gp.tileSize;

        gp.ui.drawSubWindow(g2, panelX, panelY, panelW, panelH);

        // title
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 30f));
        g2.setColor(new Color(255, 100, 100));
        String title = adminMode == 1 ? "Admin Access" : "Reset Account";
        g2.drawString(title, cx - strW(g2, title) / 2, panelY + pad);

        g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 20f));
        g2.setColor(new Color(180, 180, 180));
        String sub = adminMode == 1
                ? "Enter the admin password to continue."
                : "Enter the student's username to delete their account.";
        g2.drawString(sub, cx - strW(g2, sub) / 2, panelY + pad + 30);

        // input field
        int fieldW = panelW - pad * 2;
        int fieldX = panelX + pad;
        int fieldY = panelY + pad + 60;
        String label = adminMode == 1 ? "Admin Password" : "Student Username";
        String value = adminMode == 1
                ? "•".repeat(adminPassField.length())
                : resetUsernameField.toString();

        drawField(g2, label, value, adminMode == 1, true, fieldX, fieldY, fieldW);

        // error
        if (!adminError.isEmpty()) {
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 22f));
            g2.setColor(new Color(230, 80, 80));
            g2.drawString(adminError, cx - strW(g2, adminError) / 2, fieldY + gp.tileSize + 40);
        }

        // success
        if (!adminSuccess.isEmpty()) {
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 22f));

            // BLACK OUTLINE
            g2.setColor(Color.black);
            g2.drawString(adminSuccess, (cx - strW(g2, adminSuccess) / 2) - 1, (fieldY + gp.tileSize + 40) - 1);
            g2.drawString(adminSuccess, (cx - strW(g2, adminSuccess) / 2) + 1, (fieldY + gp.tileSize + 40) - 1);
            g2.drawString(adminSuccess, (cx - strW(g2, adminSuccess) / 2) - 1, (fieldY + gp.tileSize + 40) + 1);
            g2.drawString(adminSuccess, (cx - strW(g2, adminSuccess) / 2) + 1, (fieldY + gp.tileSize + 40) + 1);

            g2.setColor(new Color(80, 220, 80));
            g2.drawString(adminSuccess, cx - strW(g2, adminSuccess) / 2, fieldY + gp.tileSize + 40);

        }

        // hint
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 20f));
        g2.setColor(new Color(180, 180, 180));
        String hint = "[ ENTER ] Confirm    [ ESC ] Cancel";
        g2.drawString(hint, cx - strW(g2, hint) / 2, panelY + panelH - 14);
    }

    private void drawForm(Graphics2D g2) {
        int panelW = gp.tileSize * 10;
        int panelH = gp.tileSize * 7;
        int panelX = gp.screenWidth  / 2 - panelW / 2;
        int panelY = gp.screenHeight / 2 - panelH / 2;

        gp.ui.drawSubWindow(g2, panelX, panelY, panelW, panelH);

        int pad  = gp.tileSize;
        int cx   = panelX + panelW / 2;

        // Title
        String formTitle = (mode == 1) ? "Log In" : "Sign Up";
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 36f));
        g2.setColor(GOLD);
        g2.drawString(formTitle, cx - strW(g2, formTitle) / 2, panelY + pad);

        int fieldW = panelW - pad * 2;
        int fieldX = panelX + pad;
        int fieldY = panelY + pad + 44;

        // Username field
        drawField(g2, "Username", usernameField.toString(), false,
                focusOnUsername, fieldX, fieldY, fieldW);

        fieldY += gp.tileSize + 30;

        // Password field (mask with dots)
        String masked = "•".repeat(passwordField.length());
        drawField(g2, "Password", masked, true,
                !focusOnUsername, fieldX, fieldY, fieldW);

        // Error / hint
        if (!errorMessage.isEmpty()) {
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 22f));
            g2.setColor(ERROR_RED);
            g2.drawString(errorMessage, cx - strW(g2, errorMessage) / 2, fieldY + gp.tileSize + 40);
        }

        // Bottom hints
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 20f));
        g2.setColor(LIGHT_GREY);
        String nav = "[ ARROW KEYS ] Switch field    [ ENTER ] Confirm    [ ESC ] Back";
        g2.drawString(nav, cx - strW(g2, nav) / 2, panelY + panelH - 14);
    }

    private void drawField(Graphics2D g2, String label, String value, boolean isPassword, boolean focused, int x, int y, int w) {
        int h = gp.tileSize - 4;

        // Label
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 22f));
        g2.setColor(LIGHT_GREY);
        g2.drawString(label, x, y - 4);

        // Box
        Color boxColor = focused ? GOLD : new Color(100, 100, 100);
        g2.setColor(new Color(20, 20, 20, 200));
        g2.fillRoundRect(x, y, w, h, 8, 8);
        g2.setColor(boxColor);
        g2.setStroke(new BasicStroke(focused ? 2.5f : 1.5f));
        g2.drawRoundRect(x, y, w, h, 8, 8);

        // Value + cursor
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.PLAIN, 26f));
        g2.setColor(Color.white);
        String display = value + (focused && blinkCursor() ? "|" : "");
        g2.drawString(display, x + 10, y + h - 8);
    }

    private void handleAdminKey(int code, char keyChar) {
        adminError = "";
        adminSuccess = "";

        if (code == KeyEvent.VK_ESCAPE) {
            adminMode = 0;
            return;
        }

        if (code == KeyEvent.VK_BACK_SPACE) {
            StringBuilder active = (adminMode == 1) ? adminPassField : resetUsernameField;
            if (active.length() > 0) active.deleteCharAt(active.length() - 1);
            return;
        }

        if (code == KeyEvent.VK_ENTER) {
            if (adminMode == 1) {
                // verify admin password
                if (adminManager.verifyAdmin(adminPassField.toString())) {
                    adminMode = 2;
                    adminPassField.setLength(0);
                } else {
                    adminError = "Incorrect admin password.";
                }
            } else if (adminMode == 2) {
                // reset the entered username
                String result = adminManager.resetAccount(resetUsernameField.toString());
                adminSuccess = result;
                resetUsernameField.setLength(0);
            }
            return;
        }

        if (keyChar >= 32 && keyChar != 127) {
            if (adminMode == 1 && adminPassField.length() < 32) {
                adminPassField.append(keyChar);
            } else if (adminMode == 2 && resetUsernameField.length() < 16) {
                resetUsernameField.append(keyChar);
            }
        }
    }

    private boolean blinkCursor() {
        return (System.currentTimeMillis() / 500) % 2 == 0;
    }

    private int strW(Graphics2D g2, String s) {
        return g2.getFontMetrics().stringWidth(s);
    }
}
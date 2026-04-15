package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;


public class LogIn {

    private BufferedImage loginBG;

    // ADMIN MANAGER
    private AdminManager adminManager;
    private int adminMode = 0;

    private final StringBuilder adminPassField     = new StringBuilder();
    private final StringBuilder searchField        = new StringBuilder();
    private final StringBuilder resetUsernameField = new StringBuilder();

    private String adminError   = "";
    private String adminSuccess = "";
    private boolean adminPassConfirmed = false;

    private final GamePanel   gp;
    private final UserManager userManager;
    private final SaveManager saveManager;

    private java.util.List<String> accountList     = new java.util.ArrayList<>();
    private java.util.List<String> filteredList    = new java.util.ArrayList<>();
    private int    listCursor      = 0;
    private int    listScrollOffset = 0;
    private static final int MAX_VISIBLE = 5;
    private String selectedAccount = null;

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
                searchField.setLength(0);
                resetUsernameField.setLength(0);
                adminError   = "";
                adminSuccess = "";
                adminPassConfirmed = false;
                selectedAccount = null;
                listCursor = 0;
                listScrollOffset = 0;
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
        g2.setColor(new Color(0,0,0, 100));
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
        int panelH = gp.tileSize * 8;
        int panelX = gp.screenWidth  / 2 - panelW / 2;
        int panelY = gp.screenHeight / 2 - panelH / 2;
        int cx     = panelX + panelW / 2;
        int pad    = gp.tileSize;

        gp.ui.drawSubWindow(g2, panelX, panelY, panelW, panelH);

        // MODE 1 — password entry
        if (adminMode == 1) {
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 30f));
            g2.setColor(new Color(255, 100, 100));
            String title = "Admin Access";
            g2.drawString(title, cx - strW(g2, title) / 2, panelY + pad);

            g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 20f));
            g2.setColor(new Color(180, 180, 180));
            String sub = "Enter the admin password to continue.";
            g2.drawString(sub, cx - strW(g2, sub) / 2, panelY + pad + 28);

            int fieldW = panelW - pad * 2;
            int fieldX = panelX + pad;
            int fieldY = panelY + pad + 60;
            drawField(g2, "Admin Password", "•".repeat(adminPassField.length()),
                    true, true, fieldX, fieldY, fieldW);

            if (!adminError.isEmpty()) {
                g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 22f));
                g2.setColor(new Color(230, 80, 80));
                g2.drawString(adminError, cx - strW(g2, adminError) / 2,
                        fieldY + gp.tileSize + 40);
            }

            g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 20f));
            g2.setColor(new Color(180, 180, 180));
            String hint = "[ ENTER ] Confirm    [ ESC ] Cancel";
            g2.drawString(hint, cx - strW(g2, hint) / 2, panelY + panelH - 14);
            return;
        }

        // MODE 2 — account list
        if (adminMode == 2) {
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 28f));
            g2.setColor(new Color(255, 100, 100));
            String title = "Manage Accounts";
            g2.drawString(title, cx - strW(g2, title) / 2, panelY + pad);

            // search field
            int fieldW = panelW - pad * 2;
            int fieldX = panelX + pad;
            int fieldY = panelY + pad + 30;
            drawField(g2, "Search", searchField.toString(), false, true,
                    fieldX, fieldY, fieldW);

            // account cards
            int cardX = panelX + pad;
            int cardW = panelW - pad * 2;
            int cardH = gp.tileSize - 4;
            int cardGap = 6;
            int cardStartY = fieldY + gp.tileSize + 10;

            if (searchField.length() == 0) {
                g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 22f));
                g2.setColor(new Color(150, 150, 150));
                String prompt = "Type a username to search.";
                g2.drawString(prompt, cx - strW(g2, prompt) / 2, cardStartY + cardH);
            } else if (filteredList.isEmpty()) {
                g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 22f));
                g2.setColor(new Color(150, 150, 150));
                String none = "No accounts found.";
                g2.drawString(none, cx - strW(g2, none) / 2, cardStartY + cardH);
            } else {
                for (int i = 0; i < MAX_VISIBLE; i++) {
                    int idx = listScrollOffset + i;
                    if (idx >= filteredList.size()) break;

                    String name = filteredList.get(idx);
                    boolean selected = (idx == listCursor);
                    int cardY = cardStartY + i * (cardH + cardGap);

                    g2.setColor(selected
                            ? new Color(180, 140, 40, 220)
                            : new Color(40, 40, 40, 180));
                    g2.fillRoundRect(cardX, cardY, cardW, cardH, 8, 8);

                    g2.setColor(selected ? GOLD : new Color(100, 100, 100));
                    g2.setStroke(new BasicStroke(selected ? 2.5f : 1f));
                    g2.drawRoundRect(cardX, cardY, cardW, cardH, 8, 8);

                    g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 24f));
                    g2.setColor(selected ? Color.black : Color.white);
                    g2.drawString(name, cardX + 12, cardY + cardH - 8);

                    File sf = new File("saves" + File.separator + name + ".dat");
                    String tag = sf.exists() ? "  [has save]" : "  [no save]";
                    g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 18f));
                    g2.setColor(selected
                            ? new Color(60, 40, 0)
                            : new Color(140, 140, 140));
                    g2.drawString(tag, cardX + 12 + strW(g2, name) + 4, cardY + cardH - 8);
                }

                if (filteredList.size() > MAX_VISIBLE) {
                    g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 18f));
                    g2.setColor(new Color(180, 180, 180));
                    String scroll = (listScrollOffset + MAX_VISIBLE < filteredList.size())
                            ? "v more" : "";
                    if (listScrollOffset > 0) scroll = "^ " + scroll;
                    g2.drawString(scroll.trim(), cx - strW(g2, scroll.trim()) / 2,
                            cardStartY + MAX_VISIBLE * (cardH + cardGap));
                }
            }

            // success message
            if (!adminSuccess.isEmpty()) {
                g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 22f));
                g2.setColor(new Color(80, 220, 80));
                g2.drawString(adminSuccess, cx - strW(g2, adminSuccess) / 2,
                        panelY + panelH - 36);
            }

            g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 20f));
            g2.setColor(new Color(180, 180, 180));
            String hint = "[ UP/DOWN ] Navigate    [ ENTER ] Select    [ ESC ] Back";
            g2.drawString(hint, cx - strW(g2, hint) / 2, panelY + panelH - 14);
            return;
        }

        // MODE 3 — confirm delete
        if (adminMode == 3) {
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 28f));
            g2.setColor(new Color(255, 100, 100));
            String title = "Delete Account?";
            g2.drawString(title, cx - strW(g2, title) / 2, panelY + pad);

            g2.setFont(gp.ui.maruMonica.deriveFont(Font.PLAIN, 24f));
            g2.setColor(Color.white);
            String msg1 = "You are about to delete:";
            g2.drawString(msg1, cx - strW(g2, msg1) / 2, panelY + pad + 50);

            // highlighted account name
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 32f));
            g2.setColor(GOLD);
            g2.drawString(selectedAccount,
                    cx - strW(g2, selectedAccount) / 2, panelY + pad + 90);

            g2.setFont(gp.ui.maruMonica.deriveFont(Font.PLAIN, 22f));
            g2.setColor(new Color(200, 200, 200));
            String msg2 = "This will delete their account and save file.";
            g2.drawString(msg2, cx - strW(g2, msg2) / 2, panelY + pad + 130);
            String msg3 = "This cannot be undone.";
            g2.setColor(new Color(230, 80, 80));
            g2.drawString(msg3, cx - strW(g2, msg3) / 2, panelY + pad + 158);

            g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 20f));
            g2.setColor(new Color(180, 180, 180));
            String hint = "[ ENTER ] Confirm Delete    [ ESC ] Go Back";
            g2.drawString(hint, cx - strW(g2, hint) / 2, panelY + panelH - 14);
        }
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
        adminError   = "";
        adminSuccess = "";

        if (code == KeyEvent.VK_ESCAPE) {
            if (adminMode == 3) {
                adminMode = 2; // go back to list from confirm
            } else {
                adminMode = 0;
            }
            return;
        }

        // MODE 1 — password entry
        if (adminMode == 1) {
            if (code == KeyEvent.VK_BACK_SPACE) {
                if (adminPassField.length() > 0)
                    adminPassField.deleteCharAt(adminPassField.length() - 1);
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

        // MODE 2 — account list with search
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
                    if (listCursor >= listScrollOffset + MAX_VISIBLE)
                        listScrollOffset++;
                }
                return;
            }
            if (code == KeyEvent.VK_ENTER) {
                if (searchField.length() > 0 && !filteredList.isEmpty()) {
                    selectedAccount = filteredList.get(listCursor);
                    adminMode = 3;
                }
                return;
            }
            if (keyChar >= 32 && keyChar != 127 && searchField.length() < 16) {
                searchField.append(keyChar);
                refreshList(searchField.toString());
                listCursor = 0;
                listScrollOffset = 0;
            }
            return;
        }

        // MODE 3 — confirm delete
        if (adminMode == 3) {
            if (code == KeyEvent.VK_ENTER) {
                if (selectedAccount != null) {
                    adminManager.resetAccount(selectedAccount);
                    adminSuccess = "'" + selectedAccount + "' deleted.";
                    selectedAccount = null;
                    adminMode = 2;
                    refreshList(searchField.toString());
                    listCursor = 0;
                    listScrollOffset = 0;
                }
            }
            return;
        }
    }

    private void refreshList(String search) {
        accountList = adminManager.getAllUsernames();
        filteredList = new java.util.ArrayList<>();
        for (String name : accountList) {
            if (search.isEmpty() || name.toLowerCase().contains(search.toLowerCase()))
                filteredList.add(name);
        }
    }


    private boolean blinkCursor() {
        return (System.currentTimeMillis() / 500) % 2 == 0;
    }

    private int strW(Graphics2D g2, String s) {
        return g2.getFontMetrics().stringWidth(s);
    }
}
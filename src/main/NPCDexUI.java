package main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class NPCDexUI {

    private final GamePanel gp;
    private final UI        ui;

    private static final int VISIBLE_ROWS = 10;
    private static final int ROW_H        = 36;

    private BufferedImage dexIcon;

    // Chapter accent colours
    private static final Color COL_CH1   = new Color(29, 158, 117);
    private static final Color COL_CH2   = new Color(55, 138, 221);
    private static final Color COL_CH3   = new Color(186, 117,  23);
    private static final Color COL_LOCK  = new Color(80,  80,  80);

    // bobbing animation
    private int   bobTick   = 0;
    private float bobOffset = 0f;

    public NPCDexUI(GamePanel gp, UI ui) {
        this.gp = gp;
        this.ui = ui;

        try {
            dexIcon = ImageIO.read(getClass().getResourceAsStream("/objects/npcdex.png"));
        } catch (Exception e) {
            dexIcon = null;
            e.printStackTrace();
        }
    }

    public void tick() {
        bobTick++;
        bobOffset = (float)(Math.sin(bobTick / 22.0) * 2.5);
    }

    // HUD ICON
    public void drawHUDIcon(Graphics2D g2) {
        NPCDatabase db = gp.npcDatabase;

        int iconSize = gp.tileSize - 6;


        int iconX = gp.screenWidth - (gp.tileSize * 2);
        int iconY = (int)(gp.tileSize / 2 + gp.tileSize * 2 + 8 + bobOffset);

        // Background pill
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRoundRect(iconX - 5, iconY - 4, iconSize + 10, iconSize + 8, 12, 12);
        g2.setColor(new Color(255, 255, 255, 50));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(iconX - 5, iconY - 4, iconSize + 10, iconSize + 8, 12, 12);

        // Book symbol
        g2.drawImage(dexIcon, iconX, iconY, iconSize, iconSize, null);

        // Met-count badge (green pill, top-right of icon)
        int met = db.getUnlockedCount();
        if (met > 0) {
            g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 13f));
            FontMetrics fm = g2.getFontMetrics();
            String badge = String.valueOf(met);
            int bw = fm.stringWidth(badge) + 8;
            int bh = 15;
            int bx = iconX + iconSize - 2;
            int by = iconY - 5;
            g2.setColor(new Color(29, 158, 117));
            g2.fillRoundRect(bx, by, bw, bh, 8, 8);
            g2.setColor(Color.white);
            g2.drawString(badge, bx + 4, by + bh - 3);
        }

        // Key hint below icon
        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 15f));
        g2.setColor(new Color(200, 200, 200, 180));
        g2.drawString("[R]", iconX + iconSize / 2 - 9, iconY + iconSize + 13);
    }



    // DEX SCREEN
    public void draw(Graphics2D g2) {
        NPCDatabase db    = gp.npcDatabase;
        int         total = db.getTotalCount();

        // Dim background
        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Outer frame
        int fX = gp.tileSize / 2;
        int fY = gp.tileSize / 2;
        int fW = gp.screenWidth  - gp.tileSize;
        int fH = gp.screenHeight - gp.tileSize;
        ui.drawSubWindow(g2, fX, fY, fW, fH);

        // ── Header ────────────────────────────────────────────────────────
        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 30f));
        g2.setColor(new Color(255, 220, 80));
        g2.drawString("CHARACTER ALMANAC", fX + 18, fY + 38);

        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 18f));
        g2.setColor(new Color(160, 160, 160));
        g2.drawString("Characters Encountered: " + db.getUnlockedCount() + " / " + total,
                fX + 18, fY + 60);

        g2.setColor(new Color(255, 255, 255, 35));
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(fX + 12, fY + 68, fX + fW - 12, fY + 68);

        int listX   = fX + 12;
        int listY   = fY + 78;
        int listW   = 215;
        int detailX = listX + listW + 12;
        int detailY = listY;
        int detailW = fW - listW - 36;
        int detailH = fH - 100;

        drawSidebar(g2, db, listX, listY, listW, total);
        drawDetail(g2,  db, detailX, detailY, detailW, detailH);

            //HINTS
        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 17f));
        g2.setColor(new Color(160, 160, 160));
        String hint = "[ W / S ]  Navigate     [ R ]  Close";
        int hw = g2.getFontMetrics().stringWidth(hint);
        g2.drawString(hint, fY+gp.tileSize, fY + fH - 12);
    }

    // ── Left sidebar: scrollable list of NPC entries ──────────────────────────
    private void drawSidebar(Graphics2D g2, NPCDatabase db,
                             int x, int y, int w, int total) {
        int sel    = db.dexSelectedIndex;
        int scroll = db.dexScrollOffset;

        for (int i = 0; i < VISIBLE_ROWS && (i + scroll) < total; i++) {
            int     idx      = i + scroll;
            boolean unlocked = db.isUnlocked(db.getId(idx));
            boolean selected = (idx == sel);
            int     rowX     = x;
            int     rowY     = y + i * ROW_H;

            // Selection highlight
            if (selected) {
                g2.setColor(new Color(255, 220, 80, 30));
                g2.fillRoundRect(rowX, rowY, w, ROW_H - 3, 8, 8);
                g2.setColor(new Color(255, 220, 80, 100));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(rowX, rowY, w, ROW_H - 3, 8, 8);
            }

            // Chapter colour dot
            g2.setColor(unlocked ? chapterColor(db.getChapter(idx)) : COL_LOCK);
            g2.fillOval(rowX + 8, rowY + ROW_H / 2 - 5, 10, 10);

            // Entry index
            g2.setFont(ui.maruMonica.deriveFont(Font.PLAIN, 13f));
            g2.setColor(new Color(110, 110, 110));
            g2.drawString(String.format("%02d", idx + 1), rowX + 22, rowY + ROW_H / 2 + 5);

            // Name (or ??? if locked)
            g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 20f));
            g2.setColor(unlocked ? Color.white : new Color(70, 70, 70));
            String display = unlocked ? db.getName(idx) : "???";
            // Clip if too long
            FontMetrics fm = g2.getFontMetrics();
            while (fm.stringWidth(display) > w - 52 && display.length() > 4) {
                display = display.substring(0, display.length() - 2) + "…";
            }
            g2.drawString(display, rowX + 44, rowY + ROW_H / 2 + 6);
        }

        // Scroll arrows
        if (total > VISIBLE_ROWS) {
            g2.setFont(ui.maruMonica.deriveFont(Font.PLAIN, 14f));
            g2.setColor(new Color(110, 110, 110));
            if (scroll > 0)
                g2.drawString("▲", x + w / 2 - 5, y - 20);
            if (scroll + VISIBLE_ROWS < total)
                g2.drawString("▼", x + w / 2 - 5, y + VISIBLE_ROWS * ROW_H + 12);
        }
    }

    // Right Panel
    private void drawDetail(Graphics2D g2, NPCDatabase db,
                            int x, int y, int w, int h) {
        int     idx      = db.dexSelectedIndex;
        boolean unlocked = db.isUnlocked(db.getId(idx));

        // Panel bg
        g2.setColor(new Color(255, 255, 255, 10));
        g2.fillRoundRect(x, y, w, h, 14, 14);
        g2.setColor(new Color(255, 255, 255, 30));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(x, y, w, h, 14, 14);

        // Chapter accent stripe on left edge
        Color accent = unlocked ? chapterColor(db.getChapter(idx)) : COL_LOCK;
        g2.setColor(accent);
        g2.fillRoundRect(x, y + 2, 4, h - 4, 4, 4);

        if (!unlocked) {
            drawLockedPanel(g2, x, y, w, h);
            return;
        }

        int pad = 18;
        int cx  = x + pad + 6;
        int cy  = y + pad;

        //Avatar circle with initials
        int r = 30;
        g2.setColor(accent.darker());
        g2.fillOval(cx, cy, r * 2, r * 2);
        g2.setColor(accent);
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval(cx, cy, r * 2, r * 2);

        String initials = getInitials(db.getName(idx));
        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 21f));
        g2.setColor(Color.white);
        FontMetrics fmI = g2.getFontMetrics();
        g2.drawString(initials,
                cx + r - fmI.stringWidth(initials) / 2,
                cy + r + fmI.getAscent() / 2 - 2);

        // Name, nickname, role
        int textX = cx + r * 2 + 14;
        int textY = cy + 20;

        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 22f));
        g2.setColor(Color.white);
        g2.drawString(db.getName(idx), textX, textY);

        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 17f));
        g2.setColor(new Color(180, 180, 180));
        g2.drawString("\"" + db.getNickname(idx) + "\"", textX, textY + 22);

        g2.setFont(ui.maruMonica.deriveFont(Font.PLAIN, 16f));
        g2.setColor(new Color(150, 200, 155));
        g2.drawString(db.getRole(idx), textX, textY + 42);

        // Chapter badge
        int badgeY = cy + r * 2 + 14;
        drawBadge(g2, cx, badgeY, db.getChapter(idx), accent);

        // Separator
        int sepY = badgeY + 26;
        g2.setColor(new Color(255, 255, 255, 30));
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(x + 10, sepY, x + w - 10, sepY);

        // Bio
        int bioY = sepY + 22;
        g2.setFont(ui.maruMonica.deriveFont(Font.PLAIN, 16f));
        g2.setColor(new Color(205, 205, 205));
        drawWrapped(g2, db.getBio(idx), cx, bioY, w - pad * 2, 22);

        // First met footer
        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 15f));
        g2.setColor(new Color(110, 110, 110));
        g2.drawString("First encountered: " + db.getChapter(idx)
                        + "  —  " + db.getQuest(idx),
                cx, y + h - 18);
    }

    private void drawLockedPanel(Graphics2D g2, int x, int y, int w, int h) {
        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 52f));
        g2.setColor(new Color(55, 55, 55));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString("?", x + w / 2 - fm.stringWidth("?") / 2, y + h / 2 + 18);

        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 18f));
        g2.setColor(new Color(85, 85, 85));
        String msg = "Talk to this character to unlock their entry.";
        fm = g2.getFontMetrics();
        g2.drawString(msg, x + w / 2 - fm.stringWidth(msg) / 2, y + h / 2 + 50);
    }

    private void drawBadge(Graphics2D g2, int x, int y, String text, Color color) {
        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 14f));
        FontMetrics fm = g2.getFontMetrics();
        int bw = fm.stringWidth(text) + 14;
        int bh = 20;
        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 55));
        g2.fillRoundRect(x, y, bw, bh, 8, 8);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(x, y, bw, bh, 8, 8);
        g2.drawString(text, x + 7, y + bh - 4);
    }

    // INPUT
    public void handleKey(int code) {
        NPCDatabase db    = gp.npcDatabase;
        int         total = db.getTotalCount();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            if (db.dexSelectedIndex > 0) {
                db.dexSelectedIndex--;
                if (db.dexSelectedIndex < db.dexScrollOffset)
                    db.dexScrollOffset = db.dexSelectedIndex;
            }
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            if (db.dexSelectedIndex < total - 1) {
                db.dexSelectedIndex++;
                if (db.dexSelectedIndex >= db.dexScrollOffset + VISIBLE_ROWS)
                    db.dexScrollOffset = db.dexSelectedIndex - VISIBLE_ROWS + 1;
            }
        }
        if (code == KeyEvent.VK_R || code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
    }

    private Color chapterColor(String chapter) {
        if (chapter == null) return COL_LOCK;
        switch (chapter) {
            case "Chapter 1": return COL_CH1;
            case "Chapter 2": return COL_CH2;
            case "Chapter 3": return COL_CH3;
            default:          return COL_LOCK;
        }
    }

    private String getInitials(String name) {
        if (name == null || name.isEmpty()) return "?";
        String[] parts = name.split("\\s+");
        if (parts.length == 1) return parts[0].substring(0, 1).toUpperCase();
        return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
    }

    private void drawWrapped(Graphics2D g2, String text,
                             int x, int y, int maxW, int lineH) {
        if (text == null || text.isEmpty()) return;
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int drawY = y;
        for (String word : words) {
            String test = line.length() == 0 ? word : line + " " + word;
            if (g2.getFontMetrics().stringWidth(test) > maxW) {
                g2.drawString(line.toString(), x, drawY);
                drawY += lineH;
                line  = new StringBuilder(word);
            } else {
                line = new StringBuilder(test);
            }
        }
        if (line.length() > 0) g2.drawString(line.toString(), x, drawY);
    }
}
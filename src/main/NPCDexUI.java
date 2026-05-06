package main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class NPCDexUI {

    private final GamePanel gp;
    private final UI        ui;


    private static final int VISIBLE_ROWS = 10;
    private static final int ROW_HEIGHT   = 36;

    private BufferedImage dexIcon;
    private final BufferedImage[] dexPortraits;

    // COLOR PER CHAPTER
    private static final Color COLOR_CHAPTER1 = new Color(29, 158, 117);
    private static final Color COLOR_CHAPTER2 = new Color(55, 138, 221);
    private static final Color COLOR_CHAPTER3 = new Color(186, 117,  23);
    private static final Color COLOR_LOCKED   = new Color(80,  80,  80);

    private int   bobTick   = 0;
    private float bobOffset = 0f;


    public NPCDexUI(GamePanel gp, UI ui) {
        this.gp = gp;
        this.ui = ui;

        try {
            dexIcon = ImageIO.read(getClass().getResourceAsStream("/objects/npcdex.png"));
        } catch (Exception e) {
            dexIcon = null;
        }

        String[] portraitPaths = {
                "/npc/teodora/teodora_down_1",
                "/npc/francisco/francisco_down_1",
                "/npc/grave",
                "/npc/saturnina/saturnina_down_1",
                "/npc/paciano/paciano_down_1",
                "/npc/narcisa/narcisa_down_1",
                "/npc/olimpia/olimpia_down_1",
                "/npc/lucia/lucia_down_1",
                "/npc/maria/maria_down_1",
                "/npc/josefa/josefa_down_1",
                "/npc/trinidad/trinidad_down_1",
                "/npc/soledad/soledad_down_1",
                "/npc/jose/jose_down_1",
                "/npc/manuel/manuel_down_1",
                "/npc/gregorio/gregorio_down_1",
                "/npc/ferrando/ferrando_down_1",
                "/npc/burgos/burgos_down_1",
                "/npc/professor/guro_down_1",
                "/npc/interno/interno_down_1",
                "/npc/externo/externo_down_1",
                "/npc/mariano/mariano_down_1",
                "/npc/rector/rector_down_1",
                "/npc/perfect/perfect_down_1",
                "/npc/casimiro/casimiro_down_1",
                "/npc/millano/millano_down_1",
                "/npc/desanctis/desanctis_down_1",
                "/npc/pedro/pedro_down_1",
                "/npc/consuelo/consuelo_down_1",
                "/npc/maximo/maximo_down_1",
                "/npc/paciano/paciano_down_1",
        };

        dexPortraits = new BufferedImage[portraitPaths.length];
        main.UtilityTool uTool = new main.UtilityTool();
        for (int i = 0; i < portraitPaths.length; i++) {
            try {
                var stream = getClass().getResourceAsStream(portraitPaths[i] + ".png");
                if (stream != null) {
                    BufferedImage raw = ImageIO.read(stream);
                    dexPortraits[i] = uTool.scaleImage(raw, gp.tileSize, gp.tileSize);
                }
            } catch (Exception e) {
                dexPortraits[i] = null;
            }
        }
    }

    public void tick() {
        bobTick++;
        bobOffset = (float)(Math.sin(bobTick / 22.0) * 2.5);
    }

    private BufferedImage getSprite(int npcIndex) {
        if (npcIndex < 0 || npcIndex >= dexPortraits.length) return null;
        return dexPortraits[npcIndex];
    }

    public void drawHUDIcon(Graphics2D g2) {
        NPCDatabase db       = gp.npcDatabase;
        int         iconSize = gp.tileSize - 6;

        int iconX = gp.screenWidth - (gp.tileSize * 2);
        int iconY = (int)(gp.tileSize / 2 + gp.tileSize * 4 + 8 + bobOffset);

        // Dark pill background
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRoundRect(iconX - 5, iconY - 4, iconSize + 10, iconSize + 8, 12, 12);
        g2.setColor(new Color(255, 255, 255, 50));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(iconX - 5, iconY - 4, iconSize + 10, iconSize + 8, 12, 12);

        // Icon image
        if (dexIcon != null) {
            g2.drawImage(dexIcon, iconX, iconY, iconSize, iconSize, null);
        } else {
            g2.setColor(new Color(255, 220, 80));
            g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 18f));
            g2.drawString("?", iconX + 4, iconY + iconSize - 4);
        }

        // counter
        int metCount = db.getUnlockedCount();
        if (metCount > 0) {
            String      badgeText   = String.valueOf(metCount);
            g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 13f));
            FontMetrics fm          = g2.getFontMetrics();
            int         badgeWidth  = fm.stringWidth(badgeText) + 8;
            int         badgeHeight = 15;
            int         badgeX      = iconX + iconSize - 2;
            int         badgeY      = iconY - 5;
            g2.setColor(new Color(29, 158, 117));
            g2.fillRoundRect(badgeX, badgeY, badgeWidth, badgeHeight, 8, 8);
            g2.setColor(Color.white);
            g2.drawString(badgeText, badgeX + 4, badgeY + badgeHeight - 3);
        }

        // hint
        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 15f));
        g2.setColor(new Color(200, 200, 200, 180));
        g2.drawString("[R]", iconX + iconSize / 2 - 9, iconY + iconSize + 13);
    }

    private int panelLeft() {
        return gp.tileSize / 2;
    }
    private int panelTop() {
        return gp.tileSize / 2;
    }
    private int panelWidth() {
        return gp.screenWidth  - gp.tileSize;
    }// total width
    private int panelHeight() {
        return gp.screenHeight - gp.tileSize;
    }

    private static final int SIDEBAR_WIDTH = 215;

    public void draw(Graphics2D g2) {
        NPCDatabase db    = gp.npcDatabase;
        int         total = db.getTotalCount();

        // Dim the game behind the panel
        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int left   = panelLeft();
        int top    = panelTop();
        int width  = panelWidth();
        int height = panelHeight();
        ui.drawSubWindow(g2, left, top, width, height);

        int titleY     = top + 38;
        int subtitleY  = top + 60;
        int separatorY = top + 68;

        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 30f));
        g2.setColor(new Color(255, 220, 80));
        g2.drawString("CHARACTER ALMANAC", left + 18, titleY);

        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 18f));
        g2.setColor(new Color(160, 160, 160));
        g2.drawString("Characters Encountered: " + db.getUnlockedCount() + " / " + total,
                left + 18, subtitleY);

        g2.setColor(new Color(255, 255, 255, 35));
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(left + 12, separatorY, left + width - 12, separatorY);

        int contentLeft   = left + 12;
        int contentTop    = separatorY + 10;
        int contentHeight = height - (contentTop - top) - 30;

        int sidebarX = contentLeft;
        int sidebarY = contentTop;

        int detailLeft   = sidebarX + SIDEBAR_WIDTH + 12;
        int detailTop    = contentTop;
        int detailWidth  = width - SIDEBAR_WIDTH - 36;
        int detailHeight = contentHeight;

        drawSidebar(g2, db, sidebarX, sidebarY, SIDEBAR_WIDTH, total);
        drawDetail(g2, db, detailLeft, detailTop, detailWidth, detailHeight);

        int footerY   = top + height - 12;
        String hint   = "[ W / S ]  Navigate     [ R ]  Close";
        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 17f));
        g2.setColor(new Color(160, 160, 160));
        int hintWidth = g2.getFontMetrics().stringWidth(hint);
        g2.drawString(hint, left + width / 2 - hintWidth / 2, footerY);
    }

    private void drawSidebar(Graphics2D g2, NPCDatabase db,
                             int sidebarX, int sidebarY, int sidebarWidth, int total) {
        int selectedIndex = db.dexSelectedIndex;
        int scrollOffset  = db.dexScrollOffset;

        for (int row = 0; row < VISIBLE_ROWS && (row + scrollOffset) < total; row++) {
            int     npcIndex   = row + scrollOffset;
            boolean unlocked   = db.isUnlocked(db.getId(npcIndex));
            boolean isSelected = (npcIndex == selectedIndex);
            int     rowX       = sidebarX;
            int     rowY       = sidebarY + row * ROW_HEIGHT;

            // Yellow highlight on selected row
            if (isSelected) {
                g2.setColor(new Color(255, 220, 80, 30));
                g2.fillRoundRect(rowX, rowY, sidebarWidth, ROW_HEIGHT - 3, 8, 8);
                g2.setColor(new Color(255, 220, 80, 100));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(rowX, rowY, sidebarWidth, ROW_HEIGHT - 3, 8, 8);
            }

            // Coloured dot: chapter colour if met, grey if locked
            g2.setColor(unlocked ? chapterColor(db.getChapter(npcIndex)) : COLOR_LOCKED);
            g2.fillOval(rowX + 8, rowY + ROW_HEIGHT / 2 - 5, 10, 10);

            // Entry number
            g2.setFont(ui.maruMonica.deriveFont(Font.PLAIN, 13f));
            g2.setColor(new Color(110, 110, 110));
            g2.drawString(String.format("%02d", npcIndex + 1), rowX + 22, rowY + ROW_HEIGHT / 2 + 5);

            // Name (or ??? if locked)
            g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 20f));
            g2.setColor(unlocked ? Color.white : new Color(70, 70, 70));
            String displayName = unlocked ? db.getName(npcIndex) : "???";
            FontMetrics fm = g2.getFontMetrics();
            while (fm.stringWidth(displayName) > sidebarWidth - 52 && displayName.length() > 4) {
                displayName = displayName.substring(0, displayName.length() - 2) + "…";
            }
            g2.drawString(displayName, rowX + 44, rowY + ROW_HEIGHT / 2 + 6);
        }

        // Scroll arrows
        if (total > VISIBLE_ROWS) {
            g2.setFont(ui.maruMonica.deriveFont(Font.PLAIN, 14f));
            g2.setColor(new Color(110, 110, 110));
            if (scrollOffset > 0)
                g2.drawString("▲", sidebarX + sidebarWidth / 2 - 5, sidebarY - 6);
            if (scrollOffset + VISIBLE_ROWS < total)
                g2.drawString("▼", sidebarX + sidebarWidth / 2 - 5, sidebarY + VISIBLE_ROWS * ROW_HEIGHT + 12);
        }
    }

    // RIGHT PANEL
    private void drawDetail(Graphics2D g2, NPCDatabase db,
                            int panelX, int panelY, int panelWidth, int panelHeight) {
        int     npcIndex = db.dexSelectedIndex;
        boolean unlocked = db.isUnlocked(db.getId(npcIndex));

        //BG TRANSPARENCY
        g2.setColor(new Color(255, 255, 255, 10));
        g2.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 14, 14);
        g2.setColor(new Color(255, 255, 255, 30));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 14, 14);

        Color accentColor = unlocked ? chapterColor(db.getChapter(npcIndex)) : COLOR_LOCKED;
        g2.setColor(accentColor);
        g2.fillRoundRect(panelX, panelY + 2, 4, panelHeight - 4, 4, 4);

        if (!unlocked) {
            drawLockedPanel(g2, panelX, panelY, panelWidth, panelHeight);
            return;
        }

        // INTERIOR LAYOUT
        int padding  = 18;
        int contentX = panelX + padding + 6;  // left edge of all content
        int contentY = panelY + padding;       // top of the portrait row

        // PORTRAIT
        int portraitSize = gp.tileSize + 8;   // slightly bigger than one tile
        int portraitX    = contentX;
        int portraitY    = contentY;

        BufferedImage sprite = getSprite(npcIndex);
        if (sprite != null) {
            // Rounded frame behind the sprite
            g2.setColor(accentColor.darker());
            g2.fillRoundRect(portraitX - 2, portraitY - 2,
                    portraitSize + 4, portraitSize + 4, 10, 10);
            g2.setColor(accentColor);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(portraitX - 2, portraitY - 2,
                    portraitSize + 4, portraitSize + 4, 10, 10);
            g2.drawImage(sprite, portraitX, portraitY, portraitSize, portraitSize, null);
        } else {

            g2.setColor(accentColor.darker());
            g2.fillOval(portraitX, portraitY, portraitSize, portraitSize);
            g2.setColor(accentColor);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(portraitX, portraitY, portraitSize, portraitSize);
            String      initials  = getInitials(db.getName(npcIndex));
            int         radius    = portraitSize / 2;
            g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 21f));
            g2.setColor(Color.white);
            FontMetrics fmI = g2.getFontMetrics();
            g2.drawString(initials,
                    portraitX + radius - fmI.stringWidth(initials) / 2,
                    portraitY + radius + fmI.getAscent() / 2 - 2);
        }

        // NAME, NICKNAME, RELATIONSHIP
        int nameX = portraitX + portraitSize + 14;
        int nameY = contentY + 20;

        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 24f));
        g2.setColor(Color.white);
        g2.drawString(db.getName(npcIndex), nameX, nameY);

        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 20f));
        g2.setColor(new Color(180, 180, 180));
        g2.drawString("\"" + db.getNickname(npcIndex) + "\"", nameX, nameY + 22);

        g2.setFont(ui.maruMonica.deriveFont(Font.PLAIN, 16f));
        g2.setColor(new Color(150, 200, 155));
        g2.drawString(db.getRole(npcIndex), nameX, nameY + 42);

        // CHAPTER BADGE
        int badgeY = contentY + portraitSize + 14;
        drawBadge(g2, contentX, badgeY, db.getChapter(npcIndex), accentColor);

        // LINE
        int separatorY = badgeY + 26;
        g2.setColor(new Color(255, 255, 255, 30));
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(panelX + 10, separatorY, panelX + panelWidth - 10, separatorY);

        // BODY TEXT
        int bioY        = separatorY + 22;
        int bioMaxWidth = panelWidth - padding * 2;
        int bioLineH    = 22;
        g2.setFont(ui.maruMonica.deriveFont(Font.PLAIN, 20f));
        g2.setColor(new Color(205, 205, 205));
        drawWrapped(g2, db.getBio(npcIndex), contentX, bioY, bioMaxWidth, bioLineH);

        int footerY = panelY + panelHeight - 18;
        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 15f));
        g2.setColor(new Color(110, 110, 110));
        g2.drawString("First encountered: " + db.getChapter(npcIndex)
                + "  —  " + db.getQuest(npcIndex), contentX, footerY);
    }

    // LOCKED UI
    private void drawLockedPanel(Graphics2D g2, int panelX, int panelY,
                                 int panelWidth, int panelHeight) {
        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 52f));
        g2.setColor(new Color(55, 55, 55));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString("?",
                panelX + panelWidth  / 2 - fm.stringWidth("?") / 2,
                panelY + panelHeight / 2 + 18);

        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 18f));
        g2.setColor(new Color(85, 85, 85));
        String msg = "Talk to this character to unlock their entry.";
        fm = g2.getFontMetrics();
        g2.drawString(msg,
                panelX + panelWidth  / 2 - fm.stringWidth(msg) / 2,
                panelY + panelHeight / 2 + 50);
    }

    // LABELS
    private void drawBadge(Graphics2D g2, int badgeX, int badgeY,
                           String text, Color color) {
        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 14f));
        FontMetrics fm      = g2.getFontMetrics();
        int         badgeW  = fm.stringWidth(text) + 14;
        int         badgeH  = 20;
        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 55));
        g2.fillRoundRect(badgeX, badgeY, badgeW, badgeH, 8, 8);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(badgeX, badgeY, badgeW, badgeH, 8, 8);
        g2.drawString(text, badgeX + 7, badgeY + badgeH - 4);
    }

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
        if (chapter == null) return COLOR_LOCKED;
        switch (chapter) {
            case "Chapter 1": return COLOR_CHAPTER1;
            case "Chapter 2": return COLOR_CHAPTER2;
            case "Chapter 3": return COLOR_CHAPTER3;
            default:          return COLOR_LOCKED;
        }
    }

    private String getInitials(String name) {
        if (name == null || name.isEmpty()) return "?";
        String[] parts = name.split("\\s+");
        if (parts.length == 1) return parts[0].substring(0, 1).toUpperCase();
        return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
    }

    private void drawWrapped(Graphics2D g2, String text,
                             int startX, int startY, int maxWidth, int lineHeight) {
        if (text == null || text.isEmpty()) return;
        String[]      words    = text.split(" ");
        StringBuilder line     = new StringBuilder();
        int           currentY = startY;
        for (String word : words) {
            String testLine = line.length() == 0 ? word : line + " " + word;
            if (g2.getFontMetrics().stringWidth(testLine) > maxWidth) {
                g2.drawString(line.toString(), startX, currentY);
                currentY += lineHeight;
                line      = new StringBuilder(word);
            } else {
                line = new StringBuilder(testLine);
            }
        }
        if (line.length() > 0) g2.drawString(line.toString(), startX, currentY);
    }
}
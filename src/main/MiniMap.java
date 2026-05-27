package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MiniMap {

    private static final int CELL = 3;

    private static final int HUD_W = 168;
    private static final int HUD_H = 108;
    private static final int HUD_PAD = 10;

    private static final int VIEW_COLS = 42;
    private static final int VIEW_ROWS = 26;

    private static final float FADE_SPEED = 0.09f;

    private static final String MAP_CH1 = "/maps/Chapter1.txt";
    private static final String MAP_CH2 = "/maps/Chapter2.txt";
    private static final String MAP_CH3 = "/maps/Chapter3.txt";
    private static final String MAP_CH4 = "/maps/Chapter4.txt";

    private static final Map<String, String> MAP_LABELS = new HashMap<>();
    static {
        MAP_LABELS.put(MAP_CH1, "Calamba, Laguna  —  Chapter 1");
        MAP_LABELS.put(MAP_CH2, "Manila  —  Chapter 2");
        MAP_LABELS.put(MAP_CH3, "Europe  —  Chapter 3");
        MAP_LABELS.put(MAP_CH4, "Intramuros / Fort Santiago  —  Chapter 4");
    }

    private static final Color COL_GRASS     = new Color( 76, 124,  58);
    private static final Color COL_PATHWAY   = new Color(178, 158, 108);
    private static final Color COL_ROAD      = new Color(110, 108, 100);
    private static final Color COL_WATER     = new Color( 55, 105, 178);
    private static final Color COL_SAND      = new Color(212, 188, 132);
    private static final Color COL_FLOOR     = new Color( 68,  62,  54);
    private static final Color COL_WALL      = new Color( 38,  34,  30);
    private static final Color COL_TREE      = new Color( 32,  88,  28);
    private static final Color COL_BRIDGE    = new Color(152, 118,  68);
    private static final Color COL_CARPET    = new Color(148,  42,  42);
    private static final Color COL_FURNITURE = new Color( 90,  72,  52);
    private static final Color COL_DEFAULT   = new Color( 72,  66,  60);


    private static final int[] COLOR_INDEX;
    private static final Color[] PALETTE = {
            COL_GRASS, COL_PATHWAY, COL_ROAD, COL_WATER, COL_SAND,
            COL_FLOOR, COL_WALL, COL_TREE, COL_BRIDGE, COL_CARPET,
            COL_FURNITURE, COL_DEFAULT
    };
    // ordinal constants matching PALETTE above
    private static final int I_GRASS=0,I_PATHWAY=1,I_ROAD=2,I_WATER=3,I_SAND=4,
            I_FLOOR=5,I_WALL=6,I_TREE=7,I_BRIDGE=8,I_CARPET=9,
            I_FURNITURE=10,I_DEFAULT=11;

    // Maximum tile ID we need to handle
    private static final int MAX_TILE_ID = 723;

    static {
        COLOR_INDEX = new int[MAX_TILE_ID + 1];
        java.util.Arrays.fill(COLOR_INDEX, I_DEFAULT);

        // ── GRASS ──
        setRange(COLOR_INDEX, I_GRASS,   101, 101);
        setIds  (COLOR_INDEX, I_GRASS,   224, 225, 226, 227);

        // ── PATHWAY ──
        setIds  (COLOR_INDEX, I_PATHWAY, 102, 107, 108, 109, 110, 111, 112, 113, 114, 125, 305);

        // ── ROAD ──
        setIds  (COLOR_INDEX, I_ROAD,    132);

        // ── WATER ──
        setIds  (COLOR_INDEX, I_WATER,   118, 263);

        // ── SAND ──
        setIds  (COLOR_INDEX, I_SAND,    135, 149, 151, 153, 155, 156, 157, 697);

        // ── TREE ──
        setRange(COLOR_INDEX, I_TREE,    160, 171);
        setRange(COLOR_INDEX, I_TREE,    605, 612);
        setRange(COLOR_INDEX, I_TREE,    706, 711);

        // ── FLOOR (walkable interior) ──
        setIds  (COLOR_INDEX, I_FLOOR,   176, 202, 203, 204, 205,
                250, 251, 254, 255, 264, 265,
                451, 519, 520, 571);
        setRange(COLOR_INDEX, I_FLOOR,   414, 439);  // dining / interior floor
        setRange(COLOR_INDEX, I_FLOOR,   546, 570);  // npc_house interior tiles

        // ── FURNITURE (solid indoor objects) ──
        setRange(COLOR_INDEX, I_FURNITURE, 210, 217);   // beds 1-2
        setIds  (COLOR_INDEX, I_FURNITURE, 248, 249, 252, 253, 266, 267);
        setRange(COLOR_INDEX, I_FURNITURE, 268, 283);   // fountain
        setRange(COLOR_INDEX, I_FURNITURE, 320, 323);   // circ tables
        setRange(COLOR_INDEX, I_FURNITURE, 393, 413);   // furniture1
        setRange(COLOR_INDEX, I_FURNITURE, 466, 474);   // bed5
        setRange(COLOR_INDEX, I_FURNITURE, 478, 485);   // bed6/7
        setRange(COLOR_INDEX, I_FURNITURE, 495, 511);   // sofa
        setRange(COLOR_INDEX, I_FURNITURE, 521, 545);   // study_room
        setIds  (COLOR_INDEX, I_FURNITURE, 614, 615, 616, 617); // ateneo tables
        setRange(COLOR_INDEX, I_FURNITURE, 674, 677);   // table2
        setRange(COLOR_INDEX, I_FURNITURE, 712, 722);   // party tables

        // ── BRIDGE / DOCK ──
        setRange(COLOR_INDEX, I_BRIDGE,  284, 300);
        setRange(COLOR_INDEX, I_BRIDGE,  592, 600);
        setRange(COLOR_INDEX, I_BRIDGE,  633, 641);

        // ── CARPET / RUG ──
        setRange(COLOR_INDEX, I_CARPET,  241, 247);
        setRange(COLOR_INDEX, I_CARPET,  440, 445);
        setRange(COLOR_INDEX, I_CARPET,  457, 465);
        setRange(COLOR_INDEX, I_CARPET,  486, 494);
        setIds  (COLOR_INDEX, I_CARPET,  514, 515, 516, 517, 518);
        setRange(COLOR_INDEX, I_CARPET,  618, 632);
        setRange(COLOR_INDEX, I_CARPET,  642, 655);
        setRange(COLOR_INDEX, I_CARPET,  678, 687);

        // ── WALL / SOLID ──
        setIds  (COLOR_INDEX, I_WALL,    103, 104);             // benches
        setIds  (COLOR_INDEX, I_WALL,    126, 127, 128);        // bush / log
        setIds  (COLOR_INDEX, I_WALL,    115, 116, 117);        // stone walls
        setRange(COLOR_INDEX, I_WALL,    119, 124);             // well
        setRange(COLOR_INDEX, I_WALL,    136, 148, 2);          // sand collision edges (every other)
        setIds  (COLOR_INDEX, I_WALL,    137, 139, 141, 143, 144, 146);
        setRange(COLOR_INDEX, I_WALL,    172, 175);             // statues
        setRange(COLOR_INDEX, I_WALL,    177, 201);             // pillars
        setIds  (COLOR_INDEX, I_WALL,    206, 208, 209);
        setRange(COLOR_INDEX, I_WALL,    218, 223);             // dividers / pillarmiddoor
        setRange(COLOR_INDEX, I_WALL,    228, 240);             // fences / shelf / dividers
        setRange(COLOR_INDEX, I_WALL,    256, 262);
        setRange(COLOR_INDEX, I_WALL,    302, 304);
        setRange(COLOR_INDEX, I_WALL,    306, 319);             // edges
        setIds  (COLOR_INDEX, I_WALL,    324);
        setRange(COLOR_INDEX, I_WALL,    325, 349);             // houses
        setRange(COLOR_INDEX, I_WALL,    350, 377);             // rocks
        setIds  (COLOR_INDEX, I_WALL,    446, 447, 448, 449, 450, 452, 453);
        setIds  (COLOR_INDEX, I_WALL,    475, 476, 477);        // pots
        setRange(COLOR_INDEX, I_WALL,    572, 583);             // ateneo exterior
        setIds  (COLOR_INDEX, I_WALL,    601, 602, 603, 604);   // ateneo bookshelf
        setIds  (COLOR_INDEX, I_WALL,    613);                  // ateneo painting
        setIds  (COLOR_INDEX, I_WALL,    656, 657);             // windows
        setRange(COLOR_INDEX, I_WALL,    658, 673);             // prison cells
        setRange(COLOR_INDEX, I_WALL,    688, 696);             // bagumbayan walls
    }

    private static void setRange(int[] arr, int color, int from, int to) {
        for (int i = from; i <= to && i < arr.length; i++) arr[i] = color;
    }
    private static void setRange(int[] arr, int color, int from, int to, int step) {
        for (int i = from; i <= to && i < arr.length; i += step) arr[i] = color;
    }
    private static void setIds(int[] arr, int color, int... ids) {
        for (int id : ids) if (id >= 0 && id < arr.length) arr[id] = color;
    }

    private final GamePanel gp;

    private final Map<String, BufferedImage> terrainCache = new HashMap<>();
    private BufferedImage currentTerrain = null;
    private String        currentMapPath = "";

    private boolean expanded = false;
    private boolean visible  = true;

    private float expandAlpha = 0f;

    private int   pulseTick = 0;
    private float pulseR    = 0f;

    private static final Color PANEL_BG    = new Color(  0,   0,   0, 210);
    private static final Color PANEL_BORD  = new Color(255, 215,  60, 190);
    private static final Color PLAYER_COL  = new Color(255, 220,  60);
    private static final Color PLAYER_RING = new Color(255, 180,   0, 100);
    private static final Color NPC_COL     = new Color( 80, 200, 200);
    private static final Color OBJ_COL     = new Color(200, 200,  80);
    private static final Color LABEL_COL   = new Color(255, 215,  60);
    private static final Color COORD_COL   = new Color(180, 180, 180);
    private static final Color VIEWPORT_COL = new Color(255, 255, 255, 28);

    public MiniMap(GamePanel gp) {
        this.gp = gp;
    }

    public void update() {
        if (!gp.currentMap.equals(currentMapPath)) {
            currentMapPath = gp.currentMap;
            currentTerrain = getOrBuildTerrain(currentMapPath);
        }

        pulseTick++;
        pulseR = 1f + 0.35f * (float) Math.sin(pulseTick / 11.0);

        if (expanded) {
            expandAlpha = Math.min(1f, expandAlpha + FADE_SPEED);
        } else {
            expandAlpha = Math.max(0f, expandAlpha - FADE_SPEED);
        }
    }

    public void draw(Graphics2D g2) {
        if (!visible) return;
        if (gp.gameState != gp.playState && gp.gameState != gp.dialogueState) return;
        if (gp.cutsceneManager.isExecutionWalkActive()) return;
        if (gp.cutsceneManager.isStatsScreenActive()) return;
        if (gp.ui.quizPanelOpen) return;

        drawHUD(g2);

        if (expandAlpha > 0f) {
            drawFullMap(g2);
        }
    }

    public void toggle() {
        expanded = !expanded;
        gp.playSE(2);
    }

    public void toggleVisible() {
        visible = !visible;
    }

    private void drawHUD(Graphics2D g2) {
        if (currentTerrain == null) return;

        int panelX = gp.tileSize / 2 + 10;
        int panelY = gp.tileSize * 2 + 10;

        // ── background ──
        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.88f));
        g2.setColor(PANEL_BG);
        g2.fillRoundRect(panelX - 5, panelY - 5, HUD_W + 10, HUD_H + 10, 12, 12);
        g2.setComposite(old);

        // ── gold border ──
        g2.setColor(PANEL_BORD);
        g2.setStroke(new BasicStroke(1.6f));
        g2.drawRoundRect(panelX - 5, panelY - 5, HUD_W + 10, HUD_H + 10, 12, 12);

        // ── scrolling terrain viewport centred on player ──
        int worldCols = gp.maxWorldCol;
        int worldRows = gp.maxWorldRow;

        // pixel size of the full terrain image
        float imgW = worldCols * CELL;
        float imgH = worldRows * CELL;

        // pixel size of one tile in the HUD  (may be fractional)
        float hudCellW = (float) HUD_W / VIEW_COLS;
        float hudCellH = (float) HUD_H / VIEW_ROWS;

        // player's position in terrain-image pixels
        float pxInImg = (gp.player.worldX / (float) gp.tileSize) * CELL;
        float pyInImg = (gp.player.worldY / (float) gp.tileSize) * CELL;

        // viewport half-sizes in terrain-image pixels
        float halfW = (VIEW_COLS / 2f) * CELL;
        float halfH = (VIEW_ROWS / 2f) * CELL;

        // source rect clamped to terrain image bounds
        float srcX = Math.max(0, Math.min(pxInImg - halfW, imgW - VIEW_COLS * CELL));
        float srcY = Math.max(0, Math.min(pyInImg - halfH, imgH - VIEW_ROWS * CELL));
        float srcW = VIEW_COLS * CELL;
        float srcH = VIEW_ROWS * CELL;

        g2.setClip(panelX, panelY, HUD_W, HUD_H);
        g2.drawImage(currentTerrain,
                panelX, panelY, panelX + HUD_W, panelY + HUD_H,
                (int) srcX, (int) srcY, (int)(srcX + srcW), (int)(srcY + srcH),
                null);

        // ── NPC dots ──
        for (int i = 0; i < gp.npc.length; i++) {
            if (gp.npc[i] == null) continue;
            float nx = panelX + ((gp.npc[i].worldX / (float) gp.tileSize) * CELL - srcX) * (HUD_W / srcW);
            float ny = panelY + ((gp.npc[i].worldY / (float) gp.tileSize) * CELL - srcY) * (HUD_H / srcH);
            if (nx < panelX || nx > panelX + HUD_W || ny < panelY || ny > panelY + HUD_H) continue;
            g2.setColor(NPC_COL);
            g2.fillOval((int) nx - 2, (int) ny - 2, 4, 4);
        }

        // ── object dots ──
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == null) continue;
            float ox = panelX + ((gp.obj[i].worldX / (float) gp.tileSize) * CELL - srcX) * (HUD_W / srcW);
            float oy = panelY + ((gp.obj[i].worldY / (float) gp.tileSize) * CELL - srcY) * (HUD_H / srcH);
            if (ox < panelX || ox > panelX + HUD_W || oy < panelY || oy > panelY + HUD_H) continue;
            g2.setColor(OBJ_COL);
            g2.fillRect((int) ox - 1, (int) oy - 1, 3, 3);
        }

        // ── player dot (always centred in the HUD) ──
        int dotX = panelX + HUD_W / 2;
        int dotY = panelY + HUD_H / 2;

        int pr = (int)(3 * pulseR);
        g2.setColor(PLAYER_RING);
        g2.fillOval(dotX - pr, dotY - pr, pr * 2, pr * 2);
        g2.setColor(PLAYER_COL);
        g2.fillOval(dotX - 3, dotY - 3, 6, 6);

        g2.setClip(null);

        // ── hint ──
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 15f));
        g2.setColor(new Color(200, 200, 200, 180));
        g2.drawString("[M]", panelX + gp.tileSize + 20, panelY + HUD_H + 20);
    }

    private void drawFullMap(Graphics2D g2) {
        if (currentTerrain == null) return;

        int sw = gp.screenWidth;
        int sh = gp.screenHeight;
        int margin = gp.tileSize;
        int titleH = 28;
        int mapX = margin;
        int mapY = margin + titleH + 6;
        int mapW = sw - margin * 2;
        int mapH = sh - margin * 2 - titleH - 30;

        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, expandAlpha * 0.96f));

        // ── dark background ──
        g2.setColor(new Color(6, 6, 6, 235));
        g2.fillRoundRect(mapX - 7, mapY - titleH - 10,
                mapW + 14, mapH + titleH + 17, 18, 18);

        // ── gold border ──
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, expandAlpha));
        g2.setColor(PANEL_BORD);
        g2.setStroke(new BasicStroke(2.2f));
        g2.drawRoundRect(mapX - 7, mapY - titleH - 10,
                mapW + 14, mapH + titleH + 17, 18, 18);

        // ── title ──
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 22f));
        g2.setColor(LABEL_COL);
        String label = MAP_LABELS.getOrDefault(currentMapPath, gp.currentSublocation);
        int lw = g2.getFontMetrics().stringWidth(label);
        g2.drawString(label, sw / 2 - lw / 2, mapY - 8);

        // ── terrain ──
        g2.setClip(mapX, mapY, mapW, mapH);
        g2.drawImage(currentTerrain, mapX, mapY, mapW, mapH, null);

        // ── subtle grid ──
        g2.setColor(new Color(255, 255, 255, 10));
        g2.setStroke(new BasicStroke(0.5f));
        int worldCols = gp.maxWorldCol;
        int worldRows = gp.maxWorldRow;
        float tileW = (float) mapW / worldCols;
        float tileH = (float) mapH / worldRows;
        for (int c = 0; c <= worldCols; c += 10) {
            int gx = mapX + (int)(c * tileW);
            g2.drawLine(gx, mapY, gx, mapY + mapH);
        }
        for (int r = 0; r <= worldRows; r += 10) {
            int gy = mapY + (int)(r * tileH);
            g2.drawLine(mapX, gy, mapX + mapW, gy);
        }

        // ── viewport rectangle (shows what the HUD shows) ──
        float pxFull = mapX + (gp.player.worldX / (float) gp.tileSize) * tileW;
        float pyFull = mapY + (gp.player.worldY / (float) gp.tileSize) * tileH;
        float vHalfW = (VIEW_COLS / 2f) * tileW;
        float vHalfH = (VIEW_ROWS / 2f) * tileH;
        g2.setColor(VIEWPORT_COL);
        g2.fillRect((int)(pxFull - vHalfW), (int)(pyFull - vHalfH),
                (int)(vHalfW * 2), (int)(vHalfH * 2));
        g2.setColor(new Color(255, 215, 60, 80));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRect((int)(pxFull - vHalfW), (int)(pyFull - vHalfH),
                (int)(vHalfW * 2), (int)(vHalfH * 2));

        // ── NPC dots ──
        for (int i = 0; i < gp.npc.length; i++) {
            if (gp.npc[i] == null) continue;
            float nx = mapX + (gp.npc[i].worldX / (float) gp.tileSize) * tileW;
            float ny = mapY + (gp.npc[i].worldY / (float) gp.tileSize) * tileH;
            if (nx < mapX || nx > mapX + mapW || ny < mapY || ny > mapY + mapH) continue;
            g2.setColor(NPC_COL);
            g2.fillOval((int) nx - 3, (int) ny - 3, 6, 6);
            if (gp.npc[i].name != null) {
                g2.setFont(gp.ui.maruMonica.deriveFont(Font.PLAIN, 10f));
                g2.setColor(new Color(160, 235, 235, 190));
                g2.drawString(gp.npc[i].name, (int) nx + 5, (int) ny + 4);
            }
        }

        // ── object dots ──
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == null) continue;
            float ox = mapX + (gp.obj[i].worldX / (float) gp.tileSize) * tileW;
            float oy = mapY + (gp.obj[i].worldY / (float) gp.tileSize) * tileH;
            if (ox < mapX || ox > mapX + mapW || oy < mapY || oy > mapY + mapH) continue;
            g2.setColor(OBJ_COL);
            g2.fillRect((int) ox - 3, (int) oy - 3, 6, 6);
        }

        // ── player dot (pulsing) ──
        int bigR = (int)(9 * pulseR);
        g2.setColor(new Color(255, 200, 0, 50));
        g2.fillOval((int) pxFull - bigR, (int) pyFull - bigR, bigR * 2, bigR * 2);
        g2.setColor(new Color(255, 220, 60, 160));
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval((int) pxFull - 7, (int) pyFull - 7, 14, 14);
        g2.setColor(PLAYER_COL);
        g2.fillOval((int) pxFull - 5, (int) pyFull - 5, 10, 10);

        // "YOU" label
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 13f));
        g2.setColor(LABEL_COL);
        g2.drawString("YOU", (int) pxFull + 9, (int) pyFull + 5);

        g2.setClip(null);

        // ── bottom bar (inside panel) ──
        int barY = mapY + mapH - 22;

        // coordinate readout — bottom left
        int tileX = gp.player.worldX / gp.tileSize;
        int tileY = gp.player.worldY / gp.tileSize;

        // close hint — bottom centre
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 14f));
        g2.setColor(new Color(200, 200, 200, 200));
        String closeHint = "[ M ] Close";
        int cw = g2.getFontMetrics().stringWidth(closeHint);
        g2.drawString(closeHint, sw / 2 - cw / 2, barY);

        // legend — bottom right
        drawLegend(g2, mapX + mapW - 180, barY);

        g2.setComposite(old);
    }

    private void drawLegend(Graphics2D g2, int x, int y) {
        Object[][] entries = {
                { PLAYER_COL, "Player" },
                { NPC_COL,    "NPC"    },
                { OBJ_COL,    "Object" },
        };
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 16f));
        int cx = x;
        for (Object[] e : entries) {
            g2.setColor((Color) e[0]);
            g2.fillOval(cx, y - 7, 7, 7);
            g2.setColor(new Color(200, 200, 200, 200));
            g2.drawString((String) e[1], cx + 10, y);
            cx += g2.getFontMetrics().stringWidth((String) e[1]) + 20;
        }
    }

    private BufferedImage getOrBuildTerrain(String mapPath) {
        if (terrainCache.containsKey(mapPath)) return terrainCache.get(mapPath);
        BufferedImage img = buildTerrain(mapPath);
        if (img != null) terrainCache.put(mapPath, img);
        return img;
    }

    private BufferedImage buildTerrain(String mapPath) {
        int cols = gp.maxWorldCol;
        int rows = gp.maxWorldRow;

        int[][] tileNums = new int[cols][rows];
        try {
            InputStream is = getClass().getResourceAsStream(mapPath);
            if (is == null) {
                System.out.println("MiniMap: map not found – " + mapPath);
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < rows) {
                String[] parts = line.trim().split("\\s+");
                for (int col = 0; col < parts.length && col < cols; col++) {
                    try { tileNums[col][row] = Integer.parseInt(parts[col]); }
                    catch (NumberFormatException ignored) {}
                }
                row++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        int imgW = cols * CELL;
        int imgH = rows * CELL;
        BufferedImage img = new BufferedImage(imgW, imgH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D ig = img.createGraphics();
        ig.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                ig.setColor(tileColour(tileNums[c][r]));
                ig.fillRect(c * CELL, r * CELL, CELL, CELL);
            }
        }

        ig.setColor(new Color(0, 0, 0, 40));
        for (int c = 0; c < cols; c += 10) ig.drawLine(c * CELL, 0, c * CELL, imgH);
        for (int r = 0; r < rows; r += 10) ig.drawLine(0, r * CELL, imgW, r * CELL);

        ig.dispose();
        return img;
    }

    private Color tileColour(int idx) {
        if (idx < 0 || idx >= COLOR_INDEX.length) return COL_DEFAULT;
        return PALETTE[COLOR_INDEX[idx]];
    }
}
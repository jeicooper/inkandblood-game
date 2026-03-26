package main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CutsceneManager {

    GamePanel gp;

    // ── Narration lines ───────────────────────────────────────
    // Each entry is one "page" the player advances through with ENTER.
    // Edit these freely to write your Chapter 2 narration.
    private String[][] chapter2Lines = {
            { "Several years have passed..." },
            { "Pepe has grown. The boy who once chased", "his siblings through the streets of Calamba..." },
            { "...is now a young man with fire in his eyes." },
            { "A new journey begins." }
    };

    private int currentLine = 0;

    // fade state: 0 = fading in, 1 = showing, 2 = fading out
    private int fadeState   = 0;
    private float alpha     = 0f;
    private static final float FADE_SPEED = 0.03f;

    // whether the cutscene has already applied its changes
    private boolean applied = false;

    // ── Configuration — set these to match your Chapter 2 ────
    // Map file for Chapter 2 (place it in your /maps/ folder)
    private static final String CHAPTER2_MAP    = "/maps/Chapter2.txt";
    // Sprite folder name under /player/ for the older Pepe
    private static final String CHAPTER2_SPRITE = "pepe_older";
    // Where to spawn the player on the new map (in tiles)
    private static final int SPAWN_TILE_X = 10;
    private static final int SPAWN_TILE_Y = 10;

    public CutsceneManager(GamePanel gp) {
        this.gp = gp;
    }

    // ── Call this to start the Chapter 2 cutscene ────────────
    public void startChapter2() {
        currentLine = 0;
        fadeState   = 0;
        alpha       = 0f;
        applied     = false;
        gp.gameState = gp.cutsceneState;
    }

    // ── Update (called every frame while in cutsceneState) ───
    public void update() {
        if (fadeState == 0) {
            alpha += FADE_SPEED;
            if (alpha >= 1f) { alpha = 1f; fadeState = 1; }
        }
        else if (fadeState == 2) {
            alpha -= FADE_SPEED;
            if (alpha <= 0f) {
                alpha = 0f;
                // Apply map/sprite/position changes once when fade-out finishes
                if (!applied) {
                    applied = true;
                    applyChapter2Changes();
                }
                gp.gameState = gp.playState;
                gp.ui.questPageNum = 1; // jump to Chapter 2 in quest panel
            }
        }
    }

    // Called when player presses ENTER
    public void advance() {
        if (fadeState != 1) return; // ignore input during fades

        currentLine++;
        if (currentLine >= chapter2Lines.length) {
            // Last line reached — start fade out
            fadeState = 2;
        }
    }

    // ── Draw ─────────────────────────────────────────────────
    public void draw(Graphics2D g2) {
        // Black background
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        if (currentLine >= chapter2Lines.length) return;

        // Apply fade alpha
        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // Draw narration lines centered
        g2.setColor(Color.white);
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.PLAIN, 32f));

        String[] lines = chapter2Lines[currentLine];
        int lineH      = 42;
        int totalH     = lines.length * lineH;
        int startY     = gp.screenHeight / 2 - totalH / 2 + lineH;

        for (String line : lines) {
            int w = g2.getFontMetrics().stringWidth(line);
            int drawX = gp.screenWidth / 2 - w / 2;
            g2.drawString(line, drawX, startY);
            startY += lineH;
        }

        // ENTER prompt at the bottom (only when fully visible)
        if (fadeState == 1) {
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 20f));
            g2.setColor(new Color(180, 180, 180));
            String prompt = currentLine < chapter2Lines.length - 1
                    ? "[ ENTER ] Continue" : "[ ENTER ] Begin";
            int pw = g2.getFontMetrics().stringWidth(prompt);
            g2.drawString(prompt, gp.screenWidth / 2 - pw / 2,
                    gp.screenHeight - gp.tileSize);
        }

        g2.setComposite(old);
    }

    // ── Applies map swap, sprite swap, player reposition ─────
    private void applyChapter2Changes() {
        // Load new map
        gp.tileM.loadMap(CHAPTER2_MAP);

        // Swap player sprite
        gp.player.loadSprite(CHAPTER2_SPRITE);

        // Reposition player
        gp.player.worldX = SPAWN_TILE_X * gp.tileSize;
        gp.player.worldY = SPAWN_TILE_Y * gp.tileSize;

        // Clear all NPCs and objects from Chapter 1
        for (int i = 0; i < gp.npc.length; i++) gp.npc[i] = null;
        for (int i = 0; i < gp.obj.length; i++) gp.obj[i] = null;

        // Set up Chapter 2 NPCs and objects here
        // gp.aSetter.activateChapter2();
    }
}

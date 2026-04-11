package main;

import java.awt.*;

public class CutsceneManager {

    GamePanel gp;

    private enum Scene { NONE, CHAPTER2, ENROLLMENT, QUEST4, CHAPTER3, QUEST6_INTRO}
    private Scene activeScene = Scene.NONE;

    // TRANSITION TO CHAP 2
    private final String[][] chapter2Lines = {
            { "Several years have passed..." },
            { "Pepe has grown. The boy who once chased",
                    "his siblings through the streets of Calamba..." },
            { "...is now a young man with fire in his eyes." },
            { "A new journey begins." }
    };

    // QUEST 3 TRANSITION
    private final String[][] enrollmentLines = {
            { "Against all odds, Jose Rizal was enrolled." },
            { "The gates of Ateneo Municipal de Manila opened before him for the first time." },
            { "A new chapter — not just in his life, but in the history of a nation." },
            { "His journey as a scholar had begun." }
    };

    // QUEST 4 TRANSITION
    private final String[][] quest4Lines = {
            { "A month of relentless study has passed." },
            { "The classroom is no longer just a room of desks—" },
            {"it is a map of two clashing empires."},
            { "The competition for this month has concluded" },
            { "The Carthaginian Empire has fought bravely"},
            {"But their lines have broken under the superior Latin and Greek recitation."}
    };

    // CHAPTER 3 TRANSITION
    private final String[][] chapter3Lines = {
            { "After earning the medals throughout my years in Ateneo",
                    "I moved to the University of Santo Tomas to study Philosophy and Letters",
                    "but the atmosphere was different." },
            { "At Ateneo, we were empires at play; here",
                    "the hostility of the Dominican professors is no game." },
            { "I see it every day in the lecture halls, the way they look at us.",
                    "Filipino students are not treated as scholars",
                    "but as subjects to be reminded of our 'place.'" },
            { "The discrimination is a silent wall we hit",
                    "every time we raise our hands." },
            { "Even the lessons feel like they belong to a dead century.",
                    "The instruction is obsolete, designed to repress our curiosity",
                    "rather than set it on fire." },
            {"I am a 'Sobresaliente' student, yet here,",
                    "my mind feels like it is in a cage. "
            },
            {"If the 'Walls of Wisdom' have turned into the bars of",
                    "a prison then I cannot stay. I must find a place where"," the sun of knowledge actually shines."}
    };

    private final String[][] quest6IntroLines = {
            { "The ink of the Noli Me Tangere had barely dried ",
              "before the fires began. The celebration in Berlin",
              "was short-lived As the first 2,000 copies left",
              "the press, the world Jose knew began to crumble.",
            },

            { "In Manila, the ‘Comisión Permanente de Censura’ ",
                    "convened in a panic. They did not see a novel...",
                    "they saw a THREAT, a declaration of war. Every copy",
                    " discovered was a death warrant for its owner."
            },

            { "But the true cost was not measured in ink or paper.",
                    "It was measured in blood and tears..." },

           { "Paciano, and his brothers-in-law were banished to Mindoro." },

            { "The Dominican friars, angered by the book’s portrayal,",
                "raised the rents in Calamba. When the townsfolk",
                "could not pay, the military arrived. Houses were",
                "torn down, and the Rizal was disowned by its family"
            },


            { "Leonor Rivera was pressured by her mother to marry an",
                    "Englishman, believing Jose would never return alive.",
                    "It grew frantic, then faded into a painful silence.",
                    "And the distance between them was no longer just ocean,",
                    "it was a shadow of a man that is branded 'Filibustero'"
            }
    };


    private int   currentLine = 0;
    private int   fadeState   = 0;
    private float alpha       = 0f;
    private static final float FADE_SPEED = 0.03f;
    private boolean applied   = false;

    // CHAP 2 CONFIG
    private static final String CHAPTER2_MAP    = "/maps/Chapter2.txt";
    private static final String CHAPTER2_SPRITE = "rizal_adult";
    private static final int    SPAWN_TILE_X    = 59;
    private static final int    SPAWN_TILE_Y    = 25;

    private static final int SPAWN_TILE_X2 = 46;
    private static final int SPAWN_TILE_Y2 = 47;

    // CHAP 3 CONFIG
    private static final String CHAPTER3_MAP    = "/maps/Chapter3.txt";
    private static final String CHAPTER3_SPRITE = "pepe_older";
    private static final int    SPAWN_TILE_X3    = 23;
    private static final int    SPAWN_TILE_Y3    = 35;

    public CutsceneManager(GamePanel gp) {
        this.gp = gp;
    }

    public void startChapter2() {
        reset(Scene.CHAPTER2);
    }

    public void startEnrollmentCutscene() {
        reset(Scene.ENROLLMENT);
    }

    public void startQuest4Cutscene() {
        reset(Scene.QUEST4);
    }

    public void startChapter3() {
        reset(Scene.CHAPTER3);
    }

    public void startQuest6StartCutscene() {
        reset(Scene.QUEST6_INTRO);
    }


    public void update() {
        if (fadeState == 0) {
            alpha += FADE_SPEED;
            if (alpha >= 1f) { alpha = 1f; fadeState = 1; }
        } else if (fadeState == 2) {
            alpha -= FADE_SPEED;
            if (alpha <= 0f) {
                alpha = 0f;
                if (!applied) {
                    applied = true;
                    applyEndOfScene();
                }
                gp.gameState = gp.playState;
            }
        }
    }

    public void advance() {
        if (fadeState != 1) return;
        currentLine++;
        if (currentLine >= activeLines().length) fadeState = 2;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        String[][] lines = activeLines();
        if (currentLine >= lines.length) return;

        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        g2.setColor(Color.white);
        g2.setFont(gp.ui.maruMonica.deriveFont(Font.PLAIN, 32f));

        String[] block = lines[currentLine];
        int lineH      = 42;
        int totalH     = block.length * lineH;
        int startY     = gp.screenHeight / 2 - totalH / 2 + lineH;

        for (String line : block) {
            int w = g2.getFontMetrics().stringWidth(line);
            g2.drawString(line, gp.screenWidth / 2 - w / 2, startY);
            startY += lineH;
        }

        if (fadeState == 1) {
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 20f));
            g2.setColor(new Color(180, 180, 180));
            String prompt = (currentLine < lines.length - 1)
                    ? "[ ENTER ] Continue" : "[ ENTER ] Begin";
            int pw = g2.getFontMetrics().stringWidth(prompt);
            g2.drawString(prompt, gp.screenWidth / 2 - pw / 2,
                    gp.screenHeight - gp.tileSize);
        }

        g2.setComposite(old);
    }


    private String[][] activeLines() {
        switch (activeScene) {
            case ENROLLMENT:
                return enrollmentLines;
            case QUEST4:
                return quest4Lines;
            case CHAPTER2:
                return chapter2Lines;
            case CHAPTER3:
                return chapter3Lines;
            case QUEST6_INTRO:
                return quest6IntroLines;

            default:
                return chapter2Lines;
        }
    }

    private void applyEndOfScene() {
        switch (activeScene) {

            case CHAPTER2:
                applyChapter2Changes();
                gp.ui.questPageNum = 1;
                break;

            case ENROLLMENT:
                applyEnrollment();
                gp.questManager.onEnrollmentCutsceneDone();
                break;

            case QUEST4:
                gp.questManager.startQuest4();
                break;

            case CHAPTER3:
                applyChapter3Changes();
                gp.ui.questPageNum = 2;
                break;

            case QUEST6_INTRO:
                gp.questManager.startQuest6();
                break;

            default:
                break;
        }
    }

    private void applyChapter2Changes() {
        gp.tileM.loadMap(CHAPTER2_MAP);
        gp.player.loadSprite2(CHAPTER2_SPRITE);
        gp.player.worldX = SPAWN_TILE_X * gp.tileSize;
        gp.player.worldY = SPAWN_TILE_Y * gp.tileSize;
        gp.player.direction = "down";

        for (int i = 0; i < gp.npc.length; i++) gp.npc[i] = null;
        for (int i = 0; i < gp.obj.length; i++) gp.obj[i] = null;

        gp.aSetter.activateChapter2();
    }

    private void applyEnrollment() {
        gp.tileM.loadMap(CHAPTER2_MAP);
        gp.player.loadSprite2(CHAPTER2_SPRITE);
        gp.player.worldX = SPAWN_TILE_X2 * gp.tileSize;
        gp.player.worldY = SPAWN_TILE_Y2 * gp.tileSize;
        gp.player.direction = "down";

        for (int i = 0; i < gp.npc.length; i++) gp.npc[i] = null;
        for (int i = 0; i < gp.obj.length; i++) gp.obj[i] = null;

        gp.aSetter.activateEnrollment();
    }

    private void applyChapter3Changes() {
        gp.tileM.loadMap(CHAPTER3_MAP);
        gp.player.loadSprite3(CHAPTER3_SPRITE);
        gp.player.worldX = SPAWN_TILE_X3 * gp.tileSize;
        gp.player.worldY = SPAWN_TILE_Y3 * gp.tileSize;
        gp.player.direction = "down";

        for (int i = 0; i < gp.npc.length; i++) gp.npc[i] = null;
        for (int i = 0; i < gp.obj.length; i++) gp.obj[i] = null;

        gp.aSetter.activateChapter3();
    }

    private void reset(Scene scene) {
        activeScene = scene;
        currentLine = 0;
        fadeState = 0;
        alpha = 0f;
        applied = false;
        gp.gameState = gp.cutsceneState;
    }}
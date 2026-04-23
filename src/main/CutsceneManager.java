package main;

import java.awt.*;

public class CutsceneManager {

    GamePanel gp;

    private enum Scene { NONE, INTRO, CHAPTER2, ENROLLMENT, QUEST4, CHAPTER3, QUEST6_INTRO, CHAPTER4_INTRO, FORT_SANTIAGO, EXECUTION,
        EXECUTION_ANIM, EXECUTION_WALK  }

    private Scene activeScene = Scene.NONE;

    // INTRO
    private final String [][] introLine = {
            {"June 19, 1861."},
            {"In a mall stone house in Calamba, a boy named José was born."},
            {"He was different from the start...",
                    "quiet, observant, and curious."},
            {"Raised with discipline and faith by Francisco and Teodora.. ",
                    "he was baptized three days later"},
            {"and he was endearly called 'Pepe'."}
    };

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

    // QUEST 6 INTRO
    private final String[][] quest6IntroLines = {
            {"The ink of the Noli Me Tangere had barely dried ",
                    "before the fires began. The celebration in Berlin",
                    "was short-lived As the first 2,000 copies left",
                    "the press, the world Jose knew began to crumble.",
            },

            {"In Manila, the ‘Comisión Permanente de Censura’ ",
                    "convened in a panic. They did not see a novel...",
                    "they saw a THREAT, a declaration of war. Every copy",
                    " discovered was a death warrant for its owner."
            },

            {"But the true cost was not measured in ink or paper.",
                    "It was measured in blood and tears..."},

            {"Paciano, and his brothers-in-law were banished to Mindoro."},

            {"The Dominican friars, angered by the book’s portrayal,",
                    "raised the rents in Calamba. When the townsfolk",
                    "could not pay, the military arrived. Houses were",
                    "torn down, and the Rizal was disowned by its family"
            },


            {"Leonor Rivera was pressured by her mother to marry an",
                    "Englishman, believing Jose would never return alive.",
                    "It grew frantic, then faded into a painful silence.",
                    "And the distance between them was no longer just ocean,",
                    "it was a shadow of a man that is branded 'Filibustero'"
            }
    };

    // QUEST 7 INTRO
    private final String[][] chapter4IntroLines = {
            {"The years of writing in the cold of Europe had ended. "},

            {"June 26, 1892"},

            {"After my return to my native land, a group of patriots,",
                    "including Apolinario Mabini, Deodato Arellano, Andres Bonifacio",
                    "and many others greeted me and founded"},

            {"the 'La liga Filipina' which means...",
                    "'The Filipino League'. “Unus Instar Omnium” or “One Like All”…"},

            {"out great motto in the association giving purpose",
                    "UNITE, PROTECT, DEFEND,",
                    "DEVELOP, and REFORM."},

            {" Three days later... July 7, 1892.",
                    "I was arrested."},

            {"Illegal Association.",
                    "Founding La Liga Filipina to organize a separatist front."},

            {"Rebellion",
                    "Serving as the spiritual head and inspiration for the Katipunan."},

            {"Sedition",
                    "Writing subversive novels to incite hatred against",
                    "the Motherland. I was thrown to Dapitan as a punishment",
                    "but also to have a chance to live at the parish convent"},

            {"Four years in Dapitan. Despite my successes, I am not happy",
                    "Loneliness haunts the quiet after work. "},

            {"I miss Europe, Calamba, and my family."},

            {"The death of Leonor Rivera left a void in my soul"},

            {"until Josephine Bracken arrived"},

            {"Dr.Blumentritt suggested an offer to trade my services",
                    "as military doctor in Cuba exchange for ending my exiles,",
                    "and after months..."},

            {"I finally received a letter from Governor-General",
                    "Ramon Blanco, informing him that his offer had been accepted"},

            {"While boarding the ship Isla de Panay en route to Barcelona,",
                    "was advised by fellow passengers Don Pedro Roxas and his son to",
                    "stay in Singapore and seek British protection, fearing",
                    " for his safety..."},

            {"however, trusting the promise of Governor Blanco",
                    "who gave his 'word of honor'. had a secretly plotted against me..."},

            {"He sees me as a threat to Spanish rule due to",
                    "my influence on the Philippine Revolution"},

            {"Governor conspired to have him arrested. By the time",
                    "the ship cleared Port Said, the whispers of the passengers",
                    "confirmed my fears: The Governor’s promise was the ink,"},

            {"but my arrest was the final period."}
    };

    // FORT SANTIAGO
    private final String[][] fortSantiagoLines = {
            {"December 26, 1896."},
            {"The Judge of Cuartel de España has signed",
                    "my death warrant."},
            {"I was taken to Fort Santiago —",
                    "your final cell."},
            {"The morning of December 30 draws near."}
    };

    //
    private final String[][] executionLines = {
            {"December 30, 1896. 6:30 AM."},
            {"The march to Bagumbayan.",
                    "My pulse is steady… the doctor says it is normal."},
            {"How strange, to have a heart that beats so calmly",
                    "when it is about to be pierced."},
    };

    public boolean isExecutionWalkActive() {
        return activeScene == Scene.EXECUTION_WALK;
    }

    // EXECUTION WALK SCENE
    private int ewPhase = 0;
    private int ewTick = 0;
    private int ewFrame = 0;
    private float ewBlackAlpha = 0f;
    private boolean ewApplied = false;

    // Rizal position
    private float ewRizalX, ewRizalY;
    // Guard positions (4 on each side, 8 total)
    private float[] ewGuardX = new float[8];
    private float[] ewGuardY = new float[8];

    // Dialogue
    private String ewDialogue = "";
    private int ewDialogueTick = 0;
    private boolean ewShowDialogue = false;

    private java.awt.image.BufferedImage ewWalkDown1, ewWalkDown2;
    private java.awt.image.BufferedImage ewWalkRight1, ewWalkRight2;
    private java.awt.image.BufferedImage ewRizalFall, ewRizalDead;
    private java.awt.image.BufferedImage ewKillerDown1, ewKillerDown2;
    private java.awt.image.BufferedImage ewKillerRight1, ewKillerRight2;
    private java.awt.image.BufferedImage ewKillerAim1, ewKillerAim2, ewKillerAim3;
    private java.awt.image.BufferedImage ewKillerFire;

    // Guard facing: "down", "right", "left"
    private String ewGuardFacing = "down";
    private int    ewAimFrame    = 0;
    private boolean ewFlash      = false;
    private int     ewFlashTick  = 0;
    private float   ewFallAngle  = 0f;

    private int currentLine = 0;
    private int fadeState = 0;
    private float alpha = 0f;
    private static final float FADE_SPEED = 0.03f;
    private boolean applied = false;

    // CHAP 2 CONFIG
    private static final String CHAPTER2_MAP = "/maps/Chapter2.txt";
    private static final String CHAPTER2_SPRITE = "rizal_adult";
    private static final int SPAWN_TILE_X = 59;
    private static final int    SPAWN_TILE_Y = 25;

    private static final int SPAWN_TILE_X2 = 46;
    private static final int SPAWN_TILE_Y2 = 47;

    // CHAP 3 CONFIG
    private static final String CHAPTER3_MAP    = "/maps/Chapter3.txt";
    private static final String CHAPTER3_SPRITE = "pepe_older";
    private static final int SPAWN_TILE_X3 = 23;
    private static final int SPAWN_TILE_Y3 = 35;

    //CHAP 4 CONFIG
    private static final String CHAPTER4_MAP = "/maps/Chapter4.txt";
    private static final int SPAWN_TILE_X4 = 60;
    private static final int SPAWN_TILE_Y4 = 29;

    //DAPITAN CONFIG
    private static final String DAPITAN = "/maps/Chapter4.txt";
    private static final int SPAWN_TILE_X5 = 51;
    private static final int SPAWN_TILE_Y5 = 36;

    public CutsceneManager(GamePanel gp) {
        this.gp = gp;
    }

    public void startIntroCutscene() {
        reset(Scene.INTRO);
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

    public void startQuest7Intro() {
        reset(Scene.CHAPTER4_INTRO);
    }

    public void startExileCutscene() {
        reset(Scene.FORT_SANTIAGO);
    }

    public void startQuest7EndCutscene() {
        reset(Scene.EXECUTION);
    }

    private void clearWorld() {
        for (int i = 0; i < gp.npc.length; i++) gp.npc[i] = null;
        for (int i = 0; i < gp.obj.length; i++) gp.obj[i] = null;
    }


    public void update() {
        if (activeScene == Scene.EXECUTION_WALK) {
            updateExecutionWalk();
            return;
        }

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
                if (activeScene != Scene.EXECUTION_WALK) {
                    gp.gameState = gp.playState;
                }
            }
        }
    }

    public void advance() {
        if (fadeState != 1) return;
        currentLine++;
        if (currentLine >= activeLines().length) fadeState = 2;
    }

    public void draw(Graphics2D g2) {
        if (activeScene == Scene.EXECUTION_WALK) {
            drawExecutionWalk(g2);
            return;
        }

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
            case INTRO:
                return introLine;
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
            case CHAPTER4_INTRO:
                return chapter4IntroLines;
            case FORT_SANTIAGO:
                return fortSantiagoLines;
            case EXECUTION:
                return executionLines;
            case EXECUTION_WALK:
                return executionLines;


            default:
                return chapter2Lines;
        }
    }

    private void applyEndOfScene() {
        switch (activeScene) {

            case INTRO:
                gp.gameState = gp.playState;
                gp.playMusic(0);
                break;

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

            case CHAPTER4_INTRO:
                applyChapter4Changes();
                clearWorld();
                gp.questManager.startQuest7();
                gp.ui.questPageNum = 3;
                break;

            case FORT_SANTIAGO:
                applyFortSantiagoChanges();
                gp.questManager.onFortSantiagoCutsceneDone();
                break;

            case EXECUTION:
                startExecutionWalk();
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
        gp.saveManager.save();
    }

    private void applyChapter4Changes() {
        gp.tileM.loadMap(CHAPTER4_MAP);
        gp.player.worldX = SPAWN_TILE_X4 * gp.tileSize;
        gp.player.worldY = SPAWN_TILE_Y4 * gp.tileSize;
        gp.player.direction = "down";

        for (int i = 0; i < gp.npc.length; i++) gp.npc[i] = null;
        for (int i = 0; i < gp.obj.length; i++) gp.obj[i] = null;

        gp.aSetter.activateQuest7Intramuros();
    }

    private void applyFortSantiagoChanges() {
        gp.tileM.loadMap(DAPITAN);
        gp.player.worldX = SPAWN_TILE_X5 * gp.tileSize;
        gp.player.worldY = SPAWN_TILE_Y5 * gp.tileSize;
        gp.player.direction = "down";

        for (int i = 0; i < gp.npc.length; i++) gp.npc[i] = null;
        for (int i = 0; i < gp.obj.length; i++) gp.obj[i] = null;

        gp.aSetter.activateQuest7FortSantiago();
    }

    public void startExecutionWalk() {
        activeScene   = Scene.EXECUTION_WALK;
        ewPhase       = 0;
        ewTick        = 0;
        ewFrame       = 0;
        ewBlackAlpha  = 0f;
        ewApplied     = false;
        ewShowDialogue = false;
        ewDialogue    = "";
        ewGuardFacing = "down";
        ewAimFrame    = 0;
        ewFlash       = false;
        ewFlashTick   = 0;
        ewFallAngle   = 0f;

        int ts = gp.tileSize;

        ewRizalX = 51 * ts;
        ewRizalY = 35 * ts;

        ewGuardX[0] = ewRizalX; ewGuardY[0] = ewRizalY - 1 * ts;
        ewGuardX[1] = ewRizalX; ewGuardY[1] = ewRizalY - 2 * ts;
        ewGuardX[2] = ewRizalX; ewGuardY[2] = ewRizalY - 3 * ts;
        ewGuardX[3] = ewRizalX; ewGuardY[3] = ewRizalY - 4 * ts;
        ewGuardX[4] = ewRizalX; ewGuardY[4] = ewRizalY - 5 * ts;
        ewGuardX[5] = ewRizalX; ewGuardY[5] = ewRizalY - 6 * ts;
        ewGuardX[6] = ewRizalX; ewGuardY[6] = ewRizalY - 7 * ts;
        ewGuardX[7] = ewRizalX; ewGuardY[7] = ewRizalY - 8 * ts;

        // Load Rizal sprites
        ewWalkDown1  = loadSingle("/player/walking_down_1.png");
        ewWalkDown2  = loadSingle("/player/walking_down_2.png");
        ewWalkRight1 = loadSingle("/player/walking_right_1.png");
        ewWalkRight2 = loadSingle("/player/walking_right_2.png");
        ewRizalFall  = loadSingle("/player/rizal_fall.png");
        ewRizalDead  = loadSingle("/player/rizal_dead_.png");

        // Load guard sprites
        ewKillerDown1 = loadSingle("/npc/Killer/killer_down_1.png");
        ewKillerDown2 = loadSingle("/npc/Killer/killer_down_2.png");
        ewKillerRight1 = loadSingle("/npc/Killer/killer_right_1.png");
        ewKillerRight2 = loadSingle("/npc/Killer/killer_right_2.png");
        ewKillerAim1  = loadSingle("/npc/Killer/killer_right_3.png");
        ewKillerAim2  = loadSingle("/npc/Killer/killer_right_4.png");
        ewKillerAim3  = loadSingle("/npc/Killer/killer_right_5.png");
        ewKillerFire  = loadSingle("/npc/Killer/killer_right_6.png");

        gp.stopMusic();
        gp.playMusic(2);
        gp.gameState = gp.cutsceneState;
    }

    private java.awt.image.BufferedImage loadSingle(String path) {
        try {
            return javax.imageio.ImageIO.read(getClass().getResourceAsStream(path));
        } catch (Exception e) {
            System.out.println("CutsceneManager: could not load " + path);
            return null;
        }
    }

    private void updateExecutionWalk() {
        ewTick++;
        int ts = gp.tileSize;
        float speed = 1.2f;

        if (ewTick % 14 == 0) ewFrame = (ewFrame + 1) % 2;

        if (ewShowDialogue) {
            ewDialogueTick--;
            if (ewDialogueTick <= 0) ewShowDialogue = false;
        }

        if (ewPhase == 0) {
            float targetY = 70 * ts;
            ewRizalY += speed;

            ewGuardX[0] = ewRizalX; ewGuardY[0] = ewRizalY - 1 * ts;
            ewGuardX[1] = ewRizalX; ewGuardY[1] = ewRizalY - 2 * ts;
            ewGuardX[2] = ewRizalX; ewGuardY[2] = ewRizalY - 3 * ts;
            ewGuardX[3] = ewRizalX; ewGuardY[3] = ewRizalY - 4 * ts;
            ewGuardX[4] = ewRizalX; ewGuardY[4] = ewRizalY - 5 * ts;
            ewGuardX[5] = ewRizalX; ewGuardY[5] = ewRizalY - 6 * ts;
            ewGuardX[6] = ewRizalX; ewGuardY[6] = ewRizalY - 7 * ts;
            ewGuardX[7] = ewRizalX; ewGuardY[7] = ewRizalY - 8 * ts;

            if (ewRizalY >= targetY) {
                ewRizalY = targetY;
                ewPhase = 1;
                ewTick  = 0;
                ewFrame = 0;
                ewGuardFacing = "right";
            }

        } else if (ewPhase == 1) {
            // Walk RIGHT — Rizal leads, guards maintain V but now sideways
            float targetX = 59 * ts;
            ewRizalX += speed;

            ewGuardX[0] = ewRizalX - 1 * ts; ewGuardY[0] = ewRizalY;
            ewGuardX[1] = ewRizalX - 2 * ts; ewGuardY[1] = ewRizalY;
            ewGuardX[2] = ewRizalX - 3 * ts; ewGuardY[2] = ewRizalY;
            ewGuardX[3] = ewRizalX - 4 * ts; ewGuardY[3] = ewRizalY;
            ewGuardX[4] = ewRizalX - 5 * ts; ewGuardY[4] = ewRizalY;
            ewGuardX[5] = ewRizalX - 6 * ts; ewGuardY[5] = ewRizalY;
            ewGuardX[6] = ewRizalX - 7 * ts; ewGuardY[6] = ewRizalY;
            ewGuardX[7] = ewRizalX - 8 * ts; ewGuardY[7] = ewRizalY;

            if (ewRizalX >= targetX) {
                ewRizalX = targetX;
                ewPhase  = 2;
                ewTick   = 0;
                ewFrame  = 0;
            }

        } else if (ewPhase == 2) {
            float[] targetX = {
                    ewRizalX - 4 * ts, ewRizalX - 3 * ts, ewRizalX - 2 * ts, ewRizalX - 1 * ts,
                    ewRizalX - 4 * ts, ewRizalX - 3 * ts, ewRizalX - 2 * ts, ewRizalX - 1 * ts
            };
            float[] targetY = {
                    ewRizalY - 1 * ts, ewRizalY - 1 * ts, ewRizalY - 1 * ts, ewRizalY - 1 * ts,
                    ewRizalY + 1 * ts, ewRizalY + 1 * ts, ewRizalY + 1 * ts, ewRizalY + 1 * ts
            };

            boolean allDone = true;
            for (int i = 0; i < 8; i++) {
                if (Math.abs(ewGuardX[i] - targetX[i]) > speed) {
                    ewGuardX[i] += (targetX[i] > ewGuardX[i]) ? speed : -speed;
                    allDone = false;
                } else ewGuardX[i] = targetX[i];

                if (Math.abs(ewGuardY[i] - targetY[i]) > speed) {
                    ewGuardY[i] += (targetY[i] > ewGuardY[i]) ? speed : -speed;
                    allDone = false;
                } else ewGuardY[i] = targetY[i];
            }

            if (allDone) {
                ewPhase = 3;
                ewTick  = 0;
                ewGuardFacing = "right";
            }

        } else if (ewPhase == 3) {
            // Guards aim — cycle aim frames slowly
            if (ewTick % 20 == 0) ewAimFrame = (ewAimFrame + 1) % 3;

            // "Preparen Apunten" at tick 60 — give player time to read
            if (ewTick == 60) {
                ewDialogue     = "¡Preparen! ¡Apunten!";
                ewShowDialogue = true;
                ewDialogueTick = 120; // 2 seconds
            }

            // Rizal's last words after guards' dialogue finishes
            if (ewTick == 200) {
                ewDialogue     = "Consummatum est...";
                ewShowDialogue = true;
                ewDialogueTick = 120; // 2 seconds
            }

            if (ewTick >= 340) {
                ewPhase = 4;
                ewTick  = 0;
            }

        } else if (ewPhase == 4) {
            // FUEGO — guards fire
            if (ewTick == 1) {
                ewDialogue     = "¡FUEGO!";
                ewShowDialogue = true;
                ewDialogueTick = 100;
                // gp.playSE(your_gunshot_index);
            }

            if (ewTick >= 80) {
                ewPhase = 5;
                ewTick  = 0;
            }

        } else if (ewPhase == 5) {
            // Show rizal_fall sprite for 60 ticks
            if (ewTick >= 60) {
                ewPhase = 6;
                ewTick  = 0;
            }

        } else if (ewPhase == 6) {
            // Show rizal_dead sprite and fade to black
            ewBlackAlpha += 0.006f;
            if (ewBlackAlpha >= 1f) {
                ewBlackAlpha = 1f;
                if (!ewApplied) {
                    ewApplied = true;
                    gp.stopMusic();
                    gp.gameState           = gp.titleState;
                    gp.ui.titleScreenState = 0;
                    gp.ui.commandNum       = 0;
                }
            }
        }
    }

    private void drawExecutionWalk(Graphics2D g2) {
        int spriteSize = gp.tileSize;
        int ts = gp.tileSize;
        int sw = gp.screenWidth;
        int sh = gp.screenHeight;

        // Temporarily move player to Rizal's position so tile renderer uses it as camera
        int prevX = gp.player.worldX;
        int prevY = gp.player.worldY;
        gp.player.worldX = (int)ewRizalX;
        gp.player.worldY = (int)ewRizalY;
        gp.tileM.draw(g2);
        gp.player.worldX = prevX;
        gp.player.worldY = prevY;

        // Camera offset for drawing sprites
        int camX = (int)ewRizalX - sw / 2 + ts / 2;
        int camY = (int)ewRizalY - sh / 2 + ts / 2;

        Composite old = g2.getComposite();

        // --- DRAW GUARDS ---
        for (int i = 0; i < 8; i++) {
            int gsx = (int)ewGuardX[i] - camX;
            int gsy = (int)ewGuardY[i] - camY;

            java.awt.image.BufferedImage guardImg = null;

            if (ewPhase >= 4) {
                guardImg = ewKillerFire;
            } else if (ewPhase == 3) {
                if      (ewAimFrame == 0) guardImg = ewKillerAim1;
                else if (ewAimFrame == 1) guardImg = ewKillerAim2;
                else                      guardImg = ewKillerAim3;
            } else {
                guardImg = (ewFrame == 0) ? ewKillerDown1 : ewKillerDown2;
            }

            if (guardImg != null) {
                if (i < 4 && (ewGuardFacing.equals("right") || ewGuardFacing.equals("left"))) {
                    java.awt.geom.AffineTransform at = new java.awt.geom.AffineTransform();
                    at.translate(gsx + ts, gsy);
                    at.scale(-1, 1);
                    g2.drawImage(guardImg, at, null);
                } else {
                    g2.drawImage(guardImg, gsx, gsy, ts, ts, null);
                }
            }
        }

        // --- DRAW RIZAL ---
        int rx = (int)ewRizalX - camX;
        int ry = (int)ewRizalY - camY;

        if (ewPhase >= 6) {
            // Dead — draw rizal_dead as-is, no rotation
            if (ewRizalDead != null) g2.drawImage(ewRizalDead, rx, ry, ts, ts, null);
        } else if (ewPhase == 5) {
            // Falling — draw rizal_fall as-is, no rotation
            if (ewRizalFall != null) g2.drawImage(ewRizalFall, rx, ry, spriteSize, spriteSize, null);

        } else {
            // Walking
            java.awt.image.BufferedImage rizalImg;
            if (ewPhase == 0) {
                rizalImg = (ewFrame == 0) ? ewWalkDown1 : ewWalkDown2;
            } else {
                rizalImg = (ewFrame == 0) ? ewWalkRight1 : ewWalkRight2;
            }
            if (rizalImg != null) g2.drawImage(rizalImg, rx, ry, spriteSize, spriteSize, null);
        }

        // --- DIALOGUE BOX ---
        if (ewShowDialogue && !ewDialogue.isEmpty()) {
            int boxW = 500;
            int boxH = 60;
            int boxX = sw / 2 - boxW / 2;
            int boxY = sh - boxH - 30;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
            g2.setColor(Color.black);
            g2.fillRoundRect(boxX, boxY, boxW, boxH, 14, 14);
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(boxX, boxY, boxW, boxH, 14, 14);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.PLAIN, 22f));
            g2.setColor(Color.white);
            int tw = g2.getFontMetrics().stringWidth(ewDialogue);
            g2.drawString(ewDialogue, sw / 2 - tw / 2, boxY + 38);
        }

        // --- FADE TO BLACK ---
        if (ewBlackAlpha > 0f) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ewBlackAlpha));
            g2.setColor(Color.black);
            g2.fillRect(0, 0, sw, sh);
        }

        g2.setComposite(old);
    }

    private void reset(Scene scene) {
        activeScene = scene;
        currentLine = 0;
        fadeState = 0;
        alpha = 0f;
        applied = false;
        gp.gameState = gp.cutsceneState;
    }
}
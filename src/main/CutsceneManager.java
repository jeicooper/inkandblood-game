package main;

import java.awt.*;

public class CutsceneManager {

    GamePanel gp;

    private enum Scene { NONE, INTRO, CHAPTER2, ENROLLMENT, QUEST4, CHAPTER3, QUEST6_INTRO, CHAPTER4_INTRO, FORT_SANTIAGO, EXECUTION }

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

    //CHAP 4 CONFIG
    private static final String CHAPTER4_MAP    = "/maps/Chapter4.txt";
    private static final int    SPAWN_TILE_X4    = 23;
    private static final int    SPAWN_TILE_Y4    = 35;

    //DAPITAN CONFIG
    private static final String DAPITAN    = "/maps/Dapitan.txt";
    private static final int    SPAWN_TILE_X5    = 23;
    private static final int    SPAWN_TILE_Y5    = 35;

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
                clearWorld();
                gp.questManager.startQuest7();
                gp.ui.questPageNum = 3;
                break;

            case FORT_SANTIAGO:
                gp.questManager.onFortSantiagoCutsceneDone();
                break;

            case EXECUTION:
                gp.stopMusic();
                gp.gameState       = gp.titleState;
                gp.ui.titleScreenState = 0;
                gp.ui.commandNum   = 0;
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
    }
}
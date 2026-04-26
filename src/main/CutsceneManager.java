package main;

import java.awt.*;

public class CutsceneManager {

    GamePanel gp;

    private enum Scene { NONE, INTRO, CHAPTER2, ENROLLMENT, QUEST4, CHAPTER3, QUEST6_INTRO, CHAPTER4_INTRO, FORT_SANTIAGO, EXECUTION, EXECUTION_WALK, ENDING_SCROLL }

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
            {"In Manila, the 'Comisión Permanente de Censura' ",
                    "convened in a panic. They did not see a novel...",
                    "they saw a THREAT, a declaration of war. Every copy",
                    " discovered was a death warrant for its owner."
            },
            {"But the true cost was not measured in ink or paper.",
                    "It was measured in blood and tears..."},
            {"Paciano, and his brothers-in-law were banished to Mindoro."},
            {"The Dominican friars, angered by the book's portrayal,",
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
                    "'The Filipino League'. 'Unus Instar Omnium' or 'One Like All'"},
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
                    "confirmed my fears: The Governor's promise was the ink,"},
            {"but my arrest was the final period."}
    };

    // FORT SANTIAGO
    private final String[][] fortSantiagoLines = {
            {"December 26, 1896."},
            {"The Judge of Cuartel de Espa\u00f1a has signed",
                    "my death warrant."},
            {"I was taken to Fort Santiago \u2014",
                    "your final cell."},
            {"The morning of December 30 draws near."}
    };

    private final String[][] executionLines = {
            {"December 30, 1896. 6:30 AM."},
            {"The march to Bagumbayan.",
                    "My pulse is steady\u2026 the doctor says it is normal."},
            {"How strange, to have a heart that beats so calmly",
                    "when it is about to be pierced."},
    };

    // ENDING SCROLL
    private float   esScrollY    = 0f;
    private float   esSpeed      = 0.8f;
    private boolean esDone       = false;
    private float   esFadeAlpha  = 0f;
    private boolean esApplied    = false;

    private static final String[][] ENDING_CONTENT = {
            // ── SECTION 1 ──
            { "§ THE LEGACY OF JOSE RIZAL §",
                    "Jose Protacio Rizal Mercado y Alonzo Realonda was born on June 19, 1861",
                    "in Calamba, Laguna. He was the seventh of eleven children of Francisco",
                    "Mercado Rizal and Teodora Alonzo Realonda — a family that nurtured in him",
                    "a lifelong love of learning, faith, and country."
            },
            // ── SECTION 2  ──
            { "§ THE SCHOLAR §",
                    "Rizal excelled at every institution he attended. At Ateneo Municipal de Manila,",
                    "he earned the title 'Sobresaliente' — the highest academic distinction —",
                    "and collected five gold medals across all disciplines.",
                    "He later studied philosophy, medicine, and the arts across Europe,",
                    "mastering over twenty languages in his lifetime."
            },
            // ── SECTION 3 ──
            { "§ THE WRITER §",
                    "From the cold of Europe, Rizal wrote two novels that would shake an empire.",
                    "Noli Me Tangere (1887) exposed the corruption of the colonial church",
                    "and the suffering of the Filipino people under Spanish rule.",
                    "El Filibusterismo (1891), its darker sequel, was a cry for reform —",
                    "dedicated to the martyred priests Gomez, Burgos, and Zamora (GOMBURZA).",
                    "Both books were banned in the Philippines. Owning a copy",
                    "was a death warrant."
            },
            // ── SECTION 4 ──
            { "§ LA LIGA FILIPINA §",
                    "On June 26, 1892, Rizal returned to the Philippines and founded",
                    "La Liga Filipina — a civic organization that called for unity,",
                    "mutual protection, and peaceful reform.",
                    "Three days later, on July 7, 1892, he was arrested.",
                    "The charges: illegal association, rebellion, and sedition."
            },
            // ── SECTION 5 ──
            { "§ EXILE IN DAPITAN §",
                    "Rizal was exiled to Dapitan, Zamboanga del Norte for four years.",
                    "Despite his isolation, he practiced medicine, built a school,",
                    "introduced clean water to the community, and continued his scientific work.",
                    "It was here that Josephine Bracken entered his life —",
                    "his companion until the very end."
            },
            // ── SECTION 6 ──
            { "§ THE TRIAL §",
                    "On December 26, 1896, Rizal stood before the judge of Cuartel de Espana.",
                    "He was found guilty of rebellion, sedition, and illegal association.",
                    "The sentence: death by firing squad.",
                    "He was 35 years old."
            },
            // ── SECTION 7 ──
            { "§ MI ULTIMO ADIOS §",
                    "On the night before his execution, Rizal hid his final poem",
                    "inside an alcohol stove and passed it to his sister Trinidad.",
                    "Mi Ultimo Adios — My Last Farewell — is considered one of the",
                    "greatest works in Philippine literature.",
                    "It was a love letter to the country he died for."
            },
            // ── SECTION 8 ──
            { "§ HIS ENDURING LEGACY",
                    "Jose Rizal was proclaimed the National Hero of the Philippines",
                    "by Governor-General William Howard Taft and the Philippine Commission",
                    "through Act No. 137 on July 31, 1901.",
                    "His face appears on the Philippine one-peso coin.",
                    "December 30 is celebrated as Rizal Day — a national holiday.",
                    "His childhood home in Calamba stands as a national shrine.",
                    "He remains the symbol of the Filipino struggle for freedom,",
                    "education, and dignity."
            },
            // ── CLOSING ──
            { "",
                    " ``I want to show to those who deprive people the right to love of country,",
                    "that when we know how to sacrifice ourselves for our duties and convictions,",
                    "death does not matter if one dies for those one loves —",
                    "for his country and for others dear to him.``",
                    "",
                    "— Jose Rizal"
            },
    };

    public boolean isExecutionWalkActive() {
        return activeScene == Scene.EXECUTION_WALK;
    }

    private int     ewPhase        = 0;
    private int     ewTick         = 0;
    private int     ewFrame        = 0;
    private float   ewBlackAlpha   = 0f;
    private boolean ewApplied      = false;

    private float ewRizalX, ewRizalY;

    private float[] ewGuardX = new float[8];
    private float[] ewGuardY = new float[8];

    private String  ewDialogue      = "";
    private int     ewDialogueTick  = 0;
    private boolean ewShowDialogue  = false;
    private boolean ewIsRizalLine   = false;

    private java.awt.image.BufferedImage ewWalkDown1,  ewWalkDown2;
    private java.awt.image.BufferedImage ewWalkRight1, ewWalkRight2;
    private java.awt.image.BufferedImage ewRizalFall,  ewRizalDead;
    private java.awt.image.BufferedImage ewKillerDown1, ewKillerDown2;
    private java.awt.image.BufferedImage ewKillerRight1, ewKillerRight2;
    private java.awt.image.BufferedImage ewKillerAim1, ewKillerAim2, ewKillerAim3;
    private java.awt.image.BufferedImage ewKillerFire;

    private int     ewAimFrame  = 0;

    private int   currentLine = 0;
    private int   fadeState   = 0;
    private float alpha       = 0f;
    private static final float FADE_SPEED = 0.03f;
    private boolean applied = false;

    // MAP SPAWNS
    private static final String CHAPTER2_MAP    = "/maps/Chapter2.txt";
    private static final String CHAPTER2_SPRITE = "rizal_adult";
    private static final int    SPAWN_TILE_X    = 59;
    private static final int    SPAWN_TILE_Y    = 25;
    private static final int    SPAWN_TILE_X2   = 46;
    private static final int    SPAWN_TILE_Y2   = 47;

    private static final String CHAPTER3_MAP    = "/maps/Chapter3.txt";
    private static final String CHAPTER3_SPRITE = "pepe_older";
    private static final int    SPAWN_TILE_X3   = 23;
    private static final int    SPAWN_TILE_Y3   = 35;

    private static final String CHAPTER4_MAP  = "/maps/Chapter4.txt";
    private static final int    SPAWN_TILE_X4 = 60;
    private static final int    SPAWN_TILE_Y4 = 29;

    private static final String DAPITAN       = "/maps/Chapter4.txt";
    private static final int    SPAWN_TILE_X5 = 51;
    private static final int    SPAWN_TILE_Y5 = 36;

    private static final Color GUARD_DIALOGUE_COLOR = new Color(220, 60, 60);
    private static final Color RIZAL_DIALOGUE_COLOR = new Color(255, 220, 100);

    public CutsceneManager(GamePanel gp) { this.gp = gp; }

    public void startIntroCutscene()       { reset(Scene.INTRO);         }
    public void startChapter2()            { reset(Scene.CHAPTER2);      }
    public void startEnrollmentCutscene()  { reset(Scene.ENROLLMENT);    }
    public void startQuest4Cutscene()      { reset(Scene.QUEST4);        }
    public void startChapter3()            { reset(Scene.CHAPTER3);      }
    public void startQuest6StartCutscene() { reset(Scene.QUEST6_INTRO);  }
    public void startQuest7Intro()         { reset(Scene.CHAPTER4_INTRO);}
    public void startExileCutscene()       { reset(Scene.FORT_SANTIAGO); }
    public void startQuest7EndCutscene()   { reset(Scene.EXECUTION);     }
    public void startEndingScroll() {
        activeScene = Scene.ENDING_SCROLL;
        esScrollY   = gp.screenHeight;
        esDone      = false;
        esFadeAlpha = 0f;
        esApplied   = false;
        gp.gameState = gp.cutsceneState;
    }

    private void clearWorld() {
        for (int i = 0; i < gp.npc.length; i++) gp.npc[i] = null;
        for (int i = 0; i < gp.obj.length; i++) gp.obj[i] = null;
    }

    public void update() {
        if (activeScene == Scene.EXECUTION_WALK) {
            updateExecutionWalk();
            return;
        } if (activeScene == Scene.ENDING_SCROLL) {
            updateEndingScroll();
            return;
        }

        if (fadeState == 0) {
            alpha += FADE_SPEED;
            if (alpha >= 1f) { alpha = 1f; fadeState = 1; }
        } else if (fadeState == 2) {
            alpha -= FADE_SPEED;
            if (alpha <= 0f) {
                alpha = 0f;
                if (!applied) { applied = true; applyEndOfScene(); }
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
        } if (activeScene == Scene.ENDING_SCROLL) {
            drawEndingScroll(g2);
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

        String[] block  = lines[currentLine];
        int lineH  = 42;
        int totalH = block.length * lineH;
        int startY = gp.screenHeight / 2 - totalH / 2 + lineH;

        for (String line : block) {
            int w = g2.getFontMetrics().stringWidth(line);
            g2.drawString(line, gp.screenWidth / 2 - w / 2, startY);
            startY += lineH;
        }

        if (fadeState == 1) {
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 20f));
            g2.setColor(new Color(180, 180, 180));
            String prompt = (currentLine < lines.length - 1) ? "[ ENTER ] Continue" : "[ ENTER ] Begin";
            int pw = g2.getFontMetrics().stringWidth(prompt);
            g2.drawString(prompt, gp.screenWidth / 2 - pw / 2, gp.screenHeight - gp.tileSize);
        }
        g2.setComposite(old);
    }

    private String[][] activeLines() {
        switch (activeScene) {
            case INTRO:          return introLine;
            case ENROLLMENT:     return enrollmentLines;
            case QUEST4:         return quest4Lines;
            case CHAPTER2:       return chapter2Lines;
            case CHAPTER3:       return chapter3Lines;
            case QUEST6_INTRO:   return quest6IntroLines;
            case CHAPTER4_INTRO: return chapter4IntroLines;
            case FORT_SANTIAGO:  return fortSantiagoLines;
            case EXECUTION:
            case EXECUTION_WALK: return executionLines;
            case ENDING_SCROLL: return executionLines;
            default:             return chapter2Lines;
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
        gp.player.worldX    = SPAWN_TILE_X  * gp.tileSize;
        gp.player.worldY    = SPAWN_TILE_Y  * gp.tileSize;
        gp.player.direction = "down";
        for (int i = 0; i < gp.npc.length; i++) gp.npc[i] = null;
        for (int i = 0; i < gp.obj.length; i++) gp.obj[i] = null;
        gp.aSetter.activateChapter2();
    }

    private void applyEnrollment() {
        gp.tileM.loadMap(CHAPTER2_MAP);
        gp.player.loadSprite2(CHAPTER2_SPRITE);
        gp.player.worldX    = SPAWN_TILE_X2 * gp.tileSize;
        gp.player.worldY    = SPAWN_TILE_Y2 * gp.tileSize;
        gp.player.direction = "down";
        for (int i = 0; i < gp.npc.length; i++) gp.npc[i] = null;
        for (int i = 0; i < gp.obj.length; i++) gp.obj[i] = null;
        gp.aSetter.activateEnrollment();
    }

    private void applyChapter3Changes() {
        gp.tileM.loadMap(CHAPTER3_MAP);
        gp.player.loadSprite3(CHAPTER3_SPRITE);
        gp.player.worldX    = SPAWN_TILE_X3 * gp.tileSize;
        gp.player.worldY    = SPAWN_TILE_Y3 * gp.tileSize;
        gp.player.direction = "down";
        for (int i = 0; i < gp.npc.length; i++) gp.npc[i] = null;
        for (int i = 0; i < gp.obj.length; i++) gp.obj[i] = null;
        gp.aSetter.activateChapter3();
        gp.saveManager.save();
    }

    private void applyChapter4Changes() {
        gp.tileM.loadMap(CHAPTER4_MAP);
        gp.player.worldX    = SPAWN_TILE_X4 * gp.tileSize;
        gp.player.worldY    = SPAWN_TILE_Y4 * gp.tileSize;
        gp.player.direction = "down";
        for (int i = 0; i < gp.npc.length; i++) gp.npc[i] = null;
        for (int i = 0; i < gp.obj.length; i++) gp.obj[i] = null;
        gp.aSetter.activateQuest7Intramuros();
    }

    private void applyFortSantiagoChanges() {
        gp.tileM.loadMap(DAPITAN);
        gp.player.worldX    = SPAWN_TILE_X5 * gp.tileSize;
        gp.player.worldY    = SPAWN_TILE_Y5 * gp.tileSize;
        gp.player.direction = "down";
        for (int i = 0; i < gp.npc.length; i++) gp.npc[i] = null;
        for (int i = 0; i < gp.obj.length; i++) gp.obj[i] = null;
        gp.aSetter.activateQuest7FortSantiago();
    }

    public void startExecutionWalk() {
        activeScene    = Scene.EXECUTION_WALK;
        ewPhase        = 0;
        ewTick         = 0;
        ewFrame        = 0;
        ewBlackAlpha   = 0f;
        ewApplied      = false;
        ewShowDialogue = false;
        ewDialogue     = "";
        ewIsRizalLine  = false;
        ewAimFrame     = 0;

        int ts = gp.tileSize;

        ewRizalX = (float) (51.5 * ts);
        ewRizalY = 40 * ts;

        setGuardsVertical(ts);

        // Rizal sprites
        ewWalkDown1  = loadSingle("/player/walking_down_1.png");
        ewWalkDown2  = loadSingle("/player/walking_down_2.png");
        ewWalkRight1 = loadSingle("/player/walking_right_1.png");
        ewWalkRight2 = loadSingle("/player/walking_right_2.png");
        ewRizalFall  = loadSingle("/player/rizal_fall.png");
        ewRizalDead  = loadSingle("/player/rizal_dead_.png");

        // Guard sprites
        ewKillerDown1  = loadSingle("/npc/Killer/killer_down_1.png");
        ewKillerDown2  = loadSingle("/npc/Killer/killer_down_2.png");
        ewKillerRight1 = loadSingle("/npc/Killer/killer_right_1.png");
        ewKillerRight2 = loadSingle("/npc/Killer/killer_right_2.png");
        ewKillerAim1   = loadSingle("/npc/Killer/killer_right_3.png");
        ewKillerAim2   = loadSingle("/npc/Killer/killer_right_4.png");
        ewKillerAim3   = loadSingle("/npc/Killer/killer_right_5.png");
        ewKillerFire   = loadSingle("/npc/Killer/killer_right_6.png");

        gp.stopMusic();
        gp.playMusic(2);
        gp.gameState = gp.cutsceneState;
    }

    private void setGuardsVertical(int ts) {
        float gap = ts + (ts * 0.5f);
        ewGuardX[0] = ewRizalX - ts;
        ewGuardY[0] = ewRizalY - (gap * 1);

        ewGuardX[1] = ewRizalX - ts;
        ewGuardY[1] = ewRizalY - (gap * 2);

        ewGuardX[2] = ewRizalX - ts;
        ewGuardY[2] = ewRizalY - (gap * 3);

        ewGuardX[3] = ewRizalX - ts;
        ewGuardY[3] = ewRizalY - (gap * 4);

        ewGuardX[4] = ewRizalX + ts;
        ewGuardY[4] = ewRizalY - (gap * 1);

        ewGuardX[5] = ewRizalX + ts;
        ewGuardY[5] = ewRizalY - (gap * 2);

        ewGuardX[6] = ewRizalX + ts;
        ewGuardY[6] = ewRizalY - (gap * 3);

        ewGuardX[7] = ewRizalX + ts;
        ewGuardY[7] = ewRizalY - (gap * 4);
    }

    private void setGuardsHorizontal(int ts) {
        float gap = ts + (ts * 0.5f);

        ewGuardX[0] = ewRizalX - (gap * 1);
        ewGuardY[0] = ewRizalY - ts;

        ewGuardX[1] = ewRizalX - (gap * 2);
        ewGuardY[1] = ewRizalY - ts;

        ewGuardX[2] = ewRizalX - (gap * 3);
        ewGuardY[2] = ewRizalY - ts;

        ewGuardX[3] = ewRizalX - (gap * 4);
        ewGuardY[3] = ewRizalY - ts;

        ewGuardX[4] = ewRizalX - (gap * 1);
        ewGuardY[4] = ewRizalY + ts;

        ewGuardX[5] = ewRizalX - (gap * 2);
        ewGuardY[5] = ewRizalY + ts;

        ewGuardX[6] = ewRizalX - (gap * 3);
        ewGuardY[6] = ewRizalY + ts;

        ewGuardX[7] = ewRizalX - (gap * 4);
        ewGuardY[7] = ewRizalY + ts;
    }

    private java.awt.image.BufferedImage loadSingle(String path) {
        try {
            return javax.imageio.ImageIO.read(getClass().getResourceAsStream(path));
        } catch (Exception e) {
            System.out.println("CutsceneManager: could not load " + path);
            return null;
        }
    }

    private void showDialogue(String text, boolean isRizal, int durationTicks) {
        ewDialogue     = text;
        ewIsRizalLine  = isRizal;
        ewShowDialogue = true;
        ewDialogueTick = durationTicks;
    }

    private void updateExecutionWalk() {
        ewTick++;
        int   ts    = gp.tileSize;
        float speed = 1.2f;

        if (ewShowDialogue) {
            ewDialogueTick--;
            if (ewDialogueTick <= 0) ewShowDialogue = false;
        }

        if (ewPhase == 0) {
            float targetY = 70 * ts;
            boolean moving = ewRizalY < targetY;

            if (moving) {
                ewRizalY += speed;
                if (ewRizalY > targetY) ewRizalY = targetY;
                if (ewTick % 14 == 0) ewFrame = (ewFrame + 1) % 2;
            }

            setGuardsVertical(ts);

            if (!moving) {
                ewFrame = 0;
                ewPhase = 1;
                ewTick  = 0;
            }

        } else if (ewPhase == 1) {
            float targetX = 59 * ts;
            boolean moving = ewRizalX < targetX;

            if (moving) {
                ewRizalX += speed;
                if (ewRizalX > targetX) ewRizalX = targetX;
                if (ewTick % 14 == 0) ewFrame = (ewFrame + 1) % 2;
            }
            setGuardsHorizontal(ts);

            if (!moving) {
                ewFrame = 0;
                ewPhase = 2;
                ewTick  = 0;
            }

        } else if (ewPhase == 2) {
            float[] tX = {
                    ewRizalX - 3*ts, ewRizalX - 4*ts, ewRizalX - 5*ts, ewRizalX - 6*ts,
                    ewRizalX - 3*ts, ewRizalX - 4*ts, ewRizalX - 5*ts, ewRizalX - 6*ts
            };
            float[] tY = {
                    ewRizalY - ts, ewRizalY - ts, ewRizalY - ts, ewRizalY - ts,
                    ewRizalY + ts, ewRizalY + ts, ewRizalY + ts, ewRizalY + ts
            };

            boolean allDone = true;
            for (int i = 0; i < 8; i++) {
                if (Math.abs(ewGuardX[i] - tX[i]) > speed) {
                    ewGuardX[i] += (tX[i] > ewGuardX[i]) ? speed : -speed;
                    allDone = false;
                } else ewGuardX[i] = tX[i];

                if (Math.abs(ewGuardY[i] - tY[i]) > speed) {
                    ewGuardY[i] += (tY[i] > ewGuardY[i]) ? speed : -speed;
                    allDone = false;
                } else ewGuardY[i] = tY[i];
            }

            if (allDone) {
                ewPhase = 3;
                ewTick  = 0;
            }

        } else if (ewPhase == 3) {
            if (ewTick % 20 == 0) ewAimFrame = (ewAimFrame + 1) % 3;

            if (ewTick == 60)  showDialogue("\u00a1Preparen! \u00a1Apunten!", false, 120);
            if (ewTick == 200) showDialogue("Consummatum est...",             true,  120);

            if (ewTick >= 340) { ewPhase = 4; ewTick = 0; }

        } else if (ewPhase == 4) {
            if (ewTick == 1) {
                showDialogue("\u00a1FUEGO!", false, 100);
                gp.playSE(6);
            }
            if (ewTick >= 80) { ewPhase = 5; ewTick = 0; }

        } else if (ewPhase == 5) {
            if (ewTick >= 60) { ewPhase = 6; ewTick = 0; }

        } else if (ewPhase == 6) {
            ewBlackAlpha += 0.006f;
            if (ewBlackAlpha >= 1f) {
                ewBlackAlpha = 1f;
                if (!ewApplied) {
                    ewApplied = true;
                    gp.stopMusic();
                    gp.playMusic(1);
                    startEndingScroll();
                }
            }
        }
    }

    private void drawExecutionWalk(Graphics2D g2) {
        int spriteSize = (int) (gp.tileSize * 1.2);
        int ts  = gp.tileSize;
        int sw  = gp.screenWidth;
        int sh  = gp.screenHeight;

        int prevX = gp.player.worldX;
        int prevY = gp.player.worldY;
        gp.player.worldX = (int)ewRizalX;
        gp.player.worldY = (int)ewRizalY;
        gp.tileM.draw(g2);
        gp.player.worldX = prevX;
        gp.player.worldY = prevY;

        int camX = (int)ewRizalX - sw / 2 + ts / 2;
        int camY = (int)ewRizalY - sh / 2 + ts / 2;

        Composite old = g2.getComposite();

        for (int i = 0; i < 8; i++) {
            int gsx = (int)ewGuardX[i] - camX;
            int gsy = (int)ewGuardY[i] - camY;

            java.awt.image.BufferedImage guardImg;

            if (ewPhase >= 4) {
                guardImg = ewKillerFire;
            } else if (ewPhase == 3) {
                if      (ewAimFrame == 0) guardImg = ewKillerAim1;
                else if (ewAimFrame == 1) guardImg = ewKillerAim2;
                else                      guardImg = ewKillerAim3;
            } else if (ewPhase == 1 || ewPhase == 2) {
                // Walking / moving right — use right-facing sprites
                guardImg = (ewFrame == 0) ? ewKillerRight1 : ewKillerRight2;
            } else {
                // Phase 0 — walking down
                guardImg = (ewFrame == 0) ? ewKillerDown1 : ewKillerDown2;
            }

            if (guardImg != null) {
                g2.drawImage(guardImg, gsx, gsy, spriteSize, spriteSize, null);
            }
        }

        int rx = (int)ewRizalX - camX;
        int ry = (int)ewRizalY - camY;

        java.awt.image.BufferedImage rizalImg = null;
        if (ewPhase >= 6) {
            rizalImg = ewRizalDead;
        } else if (ewPhase == 5) {
            rizalImg = ewRizalFall;
        } else if (ewPhase == 0) {
            rizalImg = (ewFrame == 0) ? ewWalkDown1 : ewWalkDown2;
        } else {
            rizalImg = (ewFrame == 0) ? ewWalkRight1 : ewWalkRight2;
        }
        if (rizalImg != null) g2.drawImage(rizalImg, rx, ry, spriteSize, spriteSize, null);

        if (ewShowDialogue && !ewDialogue.isEmpty()) {
            int boxW = 560;
            int boxH = 64;
            int boxX = sw / 2 - boxW / 2;
            int boxY = sh - boxH - 30;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.88f));
            g2.setColor(Color.black);
            g2.fillRoundRect(boxX, boxY, boxW, boxH, 14, 14);

            g2.setColor(ewIsRizalLine ? RIZAL_DIALOGUE_COLOR : GUARD_DIALOGUE_COLOR);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(boxX, boxY, boxW, boxH, 14, 14);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.PLAIN, 22f));
            g2.setColor(ewIsRizalLine ? RIZAL_DIALOGUE_COLOR : GUARD_DIALOGUE_COLOR);
            int tw = g2.getFontMetrics().stringWidth(ewDialogue);
            g2.drawString(ewDialogue, sw / 2 - tw / 2, boxY + 40);
        }

        if (ewBlackAlpha > 0f) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ewBlackAlpha));
            g2.setColor(Color.black);
            g2.fillRect(0, 0, sw, sh);
        }

        g2.setComposite(old);
    }

    private void updateEndingScroll() {
        if (!esDone) {
            esScrollY -= esSpeed;

            int lineH   = 28;
            int titleH  = 38;
            int sectionGap = 50;
            int totalH  = 0;
            for (String[] section : ENDING_CONTENT) {
                totalH += titleH;
                totalH += (section.length - 1) * lineH;
                totalH += sectionGap;
            }
            if (esScrollY < -totalH) {
                esDone = true;
            }
        } else {
            esFadeAlpha += 0.008f;
            if (esFadeAlpha >= 1f) {
                esFadeAlpha = 1f;
                if (!esApplied) {
                    esApplied = true;
                    gp.stopMusic();
                    gp.gameState           = gp.titleState;
                    gp.ui.titleScreenState = 0;
                    gp.ui.commandNum       = 0;
                }
            }
        }
    }

    private void drawEndingScroll(Graphics2D g2) {
        int sw = gp.screenWidth;
        int sh = gp.screenHeight;

        g2.setColor(Color.black);
        g2.fillRect(0, 0, sw, sh);

        Composite old = g2.getComposite();

        int lineH      = 28;
        int titleH     = 38;
        int sectionGap = 50;
        int marginX    = sw / 6;

        float currentY = esScrollY;

        for (String[] section : ENDING_CONTENT) {

            //TITLES
            String title = section[0];
            if (!title.isEmpty()) {
                g2.setFont(gp.ui.maruMonica.deriveFont(Font.BOLD, 32f));
                g2.setColor(new Color(255, 210, 80));
                int tw = g2.getFontMetrics().stringWidth(title);
                if (currentY > -titleH && currentY < sh + titleH) {
                    g2.drawString(title, sw / 2 - tw / 2, (int)currentY);
                }
            }
            currentY += titleH;

            g2.setFont(gp.ui.maruMonica.deriveFont(Font.PLAIN, 26f));
            g2.setColor(Color.white);
            for (int i = 1; i < section.length; i++) {

                String line = section[i];

                if (currentY > -lineH && currentY < sh + lineH) {

                    if (line.startsWith("\"") || line.startsWith("—")) {

                        g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 17f));
                        g2.setColor(new Color(200, 200, 200));
                    } else {
                        g2.setFont(gp.ui.maruMonica.deriveFont(Font.PLAIN, 17f));
                        g2.setColor(Color.white);
                    }
                    int lw = g2.getFontMetrics().stringWidth(line);
                    g2.drawString(line, sw / 2 - lw / 2, (int)currentY);
                }
                currentY += lineH;
            }
            currentY += sectionGap;
        }

        if (esFadeAlpha > 0f) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, esFadeAlpha));
            g2.setColor(Color.black);
            g2.fillRect(0, 0, sw, sh);
        }

        g2.setComposite(old);
    }

    private void reset(Scene scene) {
        activeScene = scene;
        currentLine = 0;
        fadeState   = 0;
        alpha       = 0f;
        applied     = false;
        gp.gameState = gp.cutsceneState;
    }
}
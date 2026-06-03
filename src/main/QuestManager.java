package main;

import entity.Entity;
import object.OBJ_Boots;
import object.OBJ_SobreMedal;

public class QuestManager {

    GamePanel gp;

    private int cutsceneDelay = 0;
    private int cutsceneDelay1 = 0;
    private int cutsceneDelay2 = 0;
    private int cutsceneDelay4 = 0;
    private int cutsceneDelay5 = 0;
    private int cutsceneDelay6 = 0;
    private int cutsceneDelay7 = 0;
    private boolean pendingQuest7IntroCutscene = false;
    private boolean pendingQuest7MidCutscene   = false;
    private boolean pendingQuest7EndCutscene   = false;

    private boolean pendingChapter2Cutscene = false;
    private boolean pendingQuest4Cutscene = false;
    private boolean pendingQuestMemoriesCutscene = false;
    private boolean pendingQuestKeepsakesCutscene = false;
    private boolean pendingChapter3Cutscene = false;
    private boolean pendingQuest6StartCutscene = false;
    private int cutsceneDelay3 = 0;
    private int cutsceneDelay8 = 0;

    //STATISTICS
    public long gameStartTime = 0L;
    public long gameEndTime = 0L;
    public long bootsStartTime = 0L;
    public long bootsEndTime = 0L;
    public int firstQuizScore = -1;
    public int quizAttempts = 0;
    public int assessmentScore = -1;


    public static final int QUEST1          = 0;
    public static final int QUEST2         = 2;
    public static final int QUEST3         = 3;
    public static final int QUEST4         = 4;
    public static final int QUEST_MEMORIES  = 8;
    public static final int QUEST_KEEPSAKES = 9;
    public static final int QUEST5         = 5;
    public static final int QUEST6         = 6;
    public static final int QUEST7         = 7;


    //STATES
    public static final int STATE_INACTIVE = 0;
    public static final int STATE_ACTIVE = 1;
    public static final int STATE_COMPLETED = 2;

    public int currentQuest = QUEST1;
    public int[] questState = new int[11];

    // QUEST 1
    public static final int QUEST1_NOT_STARTED   = 0;
    public static final int QUEST1_STARTED       = 1;
    public static final int QUEST1_RETURN_TEODORA = 2;
    public int quest1Stage = QUEST1_NOT_STARTED;

    public int siblingsFound   = 0;
    public final int SIBLINGS_REQUIRED = 9;
    public boolean conchaVisited = false;

    private int debugTimer = 0;
    public boolean quest0JustCompleted = false;

    // QUEST 2
    public static final int JOSE_INACTIVE = 0;
    public static final int JOSE_WAITING = 1;
    public static final int JOSE_DONE = 2;
    public static final int MANUEL_RUNNING = 3;
    public static final int MANUEL_DONE = 4;
    public static final int GREGORIO_WAITING = 5;
    public static final int GREGORIO_DONE = 6;

    public int quest2Stage = JOSE_INACTIVE;

    public final int PAINT_BUCKETS_REQUIRED = 6;
    public final int PAINTBRUSH_REQUIRED = 1;
    public final int CANVAS_REQUIRED = 1;
    public final int QUILL_REQUIRED = 1;
    public final int NOTEBOOK_REQUIRED = 1;

    public int checkpointsHit = 0;
    public final int TOTAL_CHECKPOINTS = 10;
    public boolean courseCompleted = false;
    public boolean bootsActive = false;

    private int normalSpeed;
    private static final int BOOST_SPEED = 10;

    public int[][] checkpoints;
    public boolean[] checkpointHit;
    public final int CHECKPOINT_RADIUS = 60;

    // QUEST 3
    public static final int TALK_FERRANDO = 0;
    public static final int TALK_BURGOS = 1;
    public static final int CUTSCENE_DONE = 2;
    public static final int TALK_PROFESSOR = 3;
    public static final int TALK_STUDENT = 4;
    public static final int QUIZ_FAILED = 5;
    public static final int TALK_FERRANDO_REWARD = 6;
    public static final int QUEST3_DONE = 7;

    public int quest3Stage = TALK_FERRANDO;
    public boolean ferrandoShooed = false;

    //QUEST 4
    public static final int TALK_PROFESSOR_Q4 = 0;
    public static final int TALK_MARIANO = 1;
    public static final int TALK_RECTOR = 2;
    public static final int DISCIPLINES_ACTIVE = 3;
    public static final int TALK_RECTOR_END = 4;
    public static final int QUEST4_DONE = 5;
    public static final int MEDALS_REQUIRED = 5;

    public int quest4Stage = TALK_PROFESSOR_Q4;
    public boolean[] disciplineMedalEarned = new boolean[5];
    public int medalsEarned = 0;
    public boolean[] disciplineAnswered = new boolean[5];
    public int disciplinesCompleted = 0;

    // QUEST_MEMORIES (between Quest4 and Quest5)
    public static final int QM_TALK_MAXIMO_FIRST   = 0;
    public static final int QM_COLLECT_5           = 1;
    public static final int QM_RETURN_MAXIMO_MID   = 2;
    public static final int QM_COLLECT_3           = 3;
    public static final int QM_RETURN_MAXIMO_FINAL = 4;
    public static final int QUEST_MEM_DONE         = 5;

    public int questMemStage = QM_TALK_MAXIMO_FIRST;
    public int memFirstCollected  = 0;   // tracks batch-1 (6 objects)
    public int memSecondCollected = 0;   // tracks batch-2 (3 objects)
    public boolean[] memFirstParts  = new boolean[6];
    public boolean[] memSecondParts = new boolean[3];

    // QUEST_KEEPSAKES (between QUEST_MEMORIES and QUEST5)
    public static final int QK_INTERACT_BOX = 0;
    public static final int QK_COLLECT      = 1;
    public static final int QK_RETURN_BOX   = 2;
    public static final int QUEST_KS_DONE   = 3;

    public int questKsStage     = QK_INTERACT_BOX;
    public int keepsakeCount    = 0;
    public boolean[] keepsakeFound = new boolean[9];

    public static final int QUEST8 = 10;

    public static final int Q8_NOT_STARTED     = 0;
    public static final int Q8_CUTSCENE        = 1;
    public static final int Q8_TALK_MARCELO    = 2;
    public static final int Q8_FIND_MALOLOS    = 3;
    public static final int Q8_RETURN_MALOLOS  = 4;
    public static final int Q8_FIND_CENTURY    = 5;
    public static final int Q8_RETURN_CENTURY  = 6;
    public static final int Q8_FIND_INDOLENCE  = 7;
    public static final int Q8_RETURN_INDOLENCE = 8;
    public static final int Q8_FINAL_TALK      = 9;
    public static final int Q8_DONE            = 10;

    public int quest8Stage = Q8_NOT_STARTED;
    public boolean q8MalolosCollected   = false;
    public boolean q8CenturyCollected   = false;
    public boolean q8IndolenceCollected = false;

    private boolean pendingQuest8Cutscene = false;
    private int cutsceneDelay9 = 0;


    // ===== QUEST_KEEPSAKES =====
    public void onKeepsakeBoxOpened() {
        if (questKsStage != QK_INTERACT_BOX) return;
        questKsStage = QK_COLLECT;
        gp.aSetter.spawnKeepsakeObjects();
        gp.ui.showMessage("Find 9 keepsakes scattered around the room.");
    }

    public void onKeepsakeFound(int index) {
        if (index < 0 || index >= 9) return;
        if (keepsakeFound[index]) return;
        keepsakeFound[index] = true;
        keepsakeCount++;
        if (keepsakeCount >= 9) {
            questKsStage = QK_RETURN_BOX;
            gp.aSetter.spawnKeepsakeBoxReturn();
        }
    }

    public void completeQuestKeepsakes() {
        if (questKsStage == QUEST_KS_DONE) return;
        questKsStage = QUEST_KS_DONE;
        questState[QUEST_KEEPSAKES] = STATE_COMPLETED;

        gp.player.exp += 2;
        gp.player.age += 1;
        gp.player.charisma += 3;
        gp.player.perception += 2;

        gp.ui.showMessage("Quest " + questDisplayNumber(QUEST_KEEPSAKES) + ": Done!");

        pendingQuestKeepsakesCutscene = true;
        cutsceneDelay8 = 60;
        gp.saveManager.save();
    }

    // QUEST 5
    public static final int TALK_PEDRO = 0;
    public static final int TALK_CONSUELO = 1;
    public static final int FIND_LETTER = 2;
    public static final int COLLECT_OBJECTS = 3;
    public static final int TALK_MAXIMO = 4;
    public static final int QUEST5_DONE = 5;
    public static final int OBJECTS_REQUIRED = 7;

    public int quest5Stage = TALK_PEDRO;
    public int objectsCollected = 0;
    public boolean[] manuscriptParts = new boolean[7];

    // QUEST 6
    public static final int TALK_PACIANO_Q6 = 0;
    public static final int FIND_DRAFT = 1;
    public static final int COLLECT_OBJECTS_Q6 = 2;
    public static final int RETURN_PACIANO = 3;
    public static final int QUEST6_DONE = 4;
    public static final int Q6_OBJECTS_REQUIRED = 4;

    public int quest6Stage = TALK_PACIANO_Q6;
    public int q6ObjectsCollected = 0;
    public boolean[] elFiliParts = new boolean[5];

    public static final int CREATIVITY_PER_OBJECT = 2;
    public static final int QUEST5_MIN_OBJECTS = 3;
    public static final int QUEST6_MIN_OBJECTS = 2;

    public int effectiveQuest5Objects() {
        int reduced = OBJECTS_REQUIRED - (gp.player.creativity / CREATIVITY_PER_OBJECT);
        return Math.max(reduced, QUEST5_MIN_OBJECTS);
    }

    public int effectiveQuest6Objects() {
        int reduced = Q6_OBJECTS_REQUIRED - (gp.player.creativity / CREATIVITY_PER_OBJECT);
        return Math.max(reduced, QUEST6_MIN_OBJECTS);
    }

    // QUEST 7
    public static final int Q7_TALK_GUARDIA   = 0;
    public static final int Q7_TALK_JUDGE     = 1;
    public static final int Q7_TALK_JOSEPHINE = 2;
    public static final int Q7_INTERACT_PAPER = 3;
    public static final int Q7_INTERACT_STOVE = 4;
    public static final int Q7_TALK_TRINIDAD  = 5;
    public static final int Q7_DONE           = 6;

    public int quest7Stage = Q7_TALK_GUARDIA;

    public static int questDisplayNumber(int questId) {
        switch (questId) {
            case QUEST1:           return 1;
            case QUEST2:           return 2;
            case QUEST3:           return 3;
            case QUEST4:           return 4;
            case QUEST_MEMORIES:   return 5;
            case QUEST_KEEPSAKES:  return 6;
            case QUEST8:           return 7;
            case QUEST5:           return 8;
            case QUEST6:           return 9;
            case QUEST7:           return 10;
            default:               return questId + 1;
        }
    }

    public static String questDisplayTitle(int questId) {
        switch (questId) {
            case QUEST1:           return "Familya Rizal";
            case QUEST2:           return "Pangangaral ng mga Tiyo";
            case QUEST3:           return "Pagpasok sa Ateneo";
            case QUEST4:           return "Ang Kampeon ng Roma";
            case QUEST_MEMORIES:   return "Mga Alaala";
            case QUEST_KEEPSAKES:  return "Mga Lihim ng Puso";
            case QUEST5:           return "Noli Me Tangere";
            case QUEST6:           return "El Filibusterismo";
            case QUEST8:           return "Mga Liham at Sanaysay";
            case QUEST7:           return "Ang Huling Araw";
            default:               return "Quest " + questDisplayNumber(questId);
        }
    }

    // CONSTRUCTOR
    public QuestManager(GamePanel gp) {
        this.gp = gp;
        questState[QUEST1] = STATE_ACTIVE;
        gameStartTime = System.currentTimeMillis();
    }

    //CHECKPOINTS
    public void init() {
        checkpoints = new int[][]{
                {29 * gp.tileSize, 34 * gp.tileSize},
                {54 * gp.tileSize, 34 * gp.tileSize},
                {53 * gp.tileSize, 48 * gp.tileSize},
                {57 * gp.tileSize, 61 * gp.tileSize},
                {61 * gp.tileSize, 71 * gp.tileSize},
                {69 * gp.tileSize, 81 * gp.tileSize},
                {80 * gp.tileSize, 76 * gp.tileSize},
                {74 * gp.tileSize, 61 * gp.tileSize},
                {68 * gp.tileSize, 48 * gp.tileSize},
                {74 * gp.tileSize, 35 * gp.tileSize}
        };
        checkpointHit = new boolean[TOTAL_CHECKPOINTS];
    }

    // UPDATES
    public void update() {
        if (currentQuest == QUEST1 && questState[QUEST1] == STATE_ACTIVE) {
            updateQuest1();
        }
        if (currentQuest == QUEST2 && questState[QUEST2] == STATE_ACTIVE){
            updateQuest2();
        }

        // TRANSITION TO CHAPTER 2
        if (pendingChapter2Cutscene) {
            cutsceneDelay--;

            if (cutsceneDelay <= 0) {

                pendingChapter2Cutscene = false;
                gp.cutsceneManager.startChapter2();

                currentQuest = QUEST3;
                questState[QUEST3] = STATE_ACTIVE;
                quest3Stage = TALK_FERRANDO;

                ferrandoShooed = false;
            }
        }

        //TRANSITION TO QUEST 4
        if (pendingQuest4Cutscene) {
            cutsceneDelay1--;

            if (cutsceneDelay1 <= 0) {

                pendingQuest4Cutscene = false;
                gp.cutsceneManager.startQuest4Cutscene();

                currentQuest = QUEST4;
                questState[QUEST4] = STATE_ACTIVE;
                quest4Stage = TALK_PROFESSOR_Q4;

                gp.ui.questPageNum = 1; // page 1: Q3+Q4
            }
        }

        // TRANSITION TO CHAPTER 3 (starts Mga Alaala / Quest Memories)
        if (pendingChapter3Cutscene) {
            cutsceneDelay2--;
            if (cutsceneDelay2 <= 0) {
                pendingChapter3Cutscene = false;
                gp.cutsceneManager.startChapter3();
                currentQuest = QUEST_MEMORIES;
                questState[QUEST_MEMORIES] = STATE_ACTIVE;
                questMemStage = QM_TALK_MAXIMO_FIRST;
                gp.ui.questPageNum = 2; // page 2: Q5+Q6 (Memories+Keepsakes)
            }
        }

        // TRANSITION FROM QUEST_MEMORIES TO QUEST_KEEPSAKES
        if (pendingQuestMemoriesCutscene) {
            cutsceneDelay3--;
            if (cutsceneDelay3 <= 0) {
                pendingQuestMemoriesCutscene = false;
                gp.cutsceneManager.startQuestKeepsakesIntro();
                currentQuest = QUEST_KEEPSAKES;
                questState[QUEST_KEEPSAKES] = STATE_ACTIVE;
                questKsStage = QK_INTERACT_BOX;
                gp.ui.questPageNum = 2; // page 2: Q5+Q6 (Memories+Keepsakes)
                gp.aSetter.activateQuestKeepsakes();
                gp.saveManager.save();
            }
        }

        // TRANSITION FROM QUEST_KEEPSAKES TO QUEST8 (La Solidaridad)
        if (pendingQuestKeepsakesCutscene) {
            cutsceneDelay8--;
            if (cutsceneDelay8 <= 0) {
                pendingQuestKeepsakesCutscene = false;
                startQuest8();
            }
        }

        // TRANSITION TO START QUEST 6
        if (pendingQuest6StartCutscene) {
            cutsceneDelay4--;
            if (cutsceneDelay4 <= 0) {
                pendingQuest6StartCutscene = false;
                gp.cutsceneManager.startQuest6StartCutscene();
            }
        }

        // Quest 7 INTRO cutscene
        if (pendingQuest7IntroCutscene) {
            cutsceneDelay5--;
            if (cutsceneDelay5 <= 0) {
                pendingQuest7IntroCutscene = false;
                gp.cutsceneManager.startQuest7Intro();
            }
        }

        // Quest 7 MID cutscene
        if (pendingQuest7MidCutscene) {
            cutsceneDelay6--;
            if (cutsceneDelay6 <= 0) {
                pendingQuest7MidCutscene = false;
                gp.cutsceneManager.startExileCutscene();
            }
        }

        // Quest 7 END cutscene
        if (pendingQuest7EndCutscene) {
            cutsceneDelay7--;
            if (cutsceneDelay7 <= 0) {
                pendingQuest7EndCutscene = false;
                gp.cutsceneManager.startQuest7EndCutscene();
            }
        }

        // QUEST 8 INTRO cutscene
        if (pendingQuest8Cutscene) {
            cutsceneDelay9--;
            if (cutsceneDelay9 <= 0) {
                pendingQuest8Cutscene = false;
                gp.cutsceneManager.startQuest8Intro();
            }
        }
    }

    // ===== QUEST 1 =====
    private void updateQuest1() {

        if (quest1Stage == QUEST1_NOT_STARTED) return;

        int following = 0;
        for (int i = 0; i < gp.npc.length; i++) {
            if (gp.npc[i] instanceof entity.NPC_Sibling) {
                entity.NPC_Sibling s = (entity.NPC_Sibling) gp.npc[i];
                if (s.isFollowing) following++;
            }
        }
        siblingsFound = following + (conchaVisited ? 1 : 0);
        gp.player.rebuildCongoLine();

        debugTimer++;
        if (debugTimer >= 60) {
            debugTimer = 0;
        }

        if (following >= SIBLINGS_REQUIRED && conchaVisited) {
            quest1Stage = QUEST1_RETURN_TEODORA;
        }
    }

    public void completeQuest1FromTeodora() {
        questState[QUEST1] = STATE_COMPLETED;
        gp.ui.showMessage("Quest " + questDisplayNumber(QUEST1) + ": Done!");

        //STATS GAINED
        gp.player.exp += 3;
        gp.player.perception += 2;
        gp.player.charisma += 2;

        for (int i = 0; i < gp.npc.length; i++) {
            if (gp.npc[i] instanceof entity.NPC_Sibling) {
                gp.npc[i] = null;
            }
        }
        currentQuest = QUEST2;
        questState[QUEST2] = STATE_ACTIVE;
        quest2Stage = JOSE_INACTIVE;
        gp.aSetter.activateQuest2();
        gp.saveManager.save();
    }

    // ===== QUEST 2 =====
    private void updateQuest2() {
        if (quest2Stage == MANUEL_RUNNING && bootsActive) {
            checkCourseCheckpoints();
        }
    }

    private void checkCourseCheckpoints() {
        for (int i = 0; i < TOTAL_CHECKPOINTS; i++) {
            if (!checkpointHit[i]) {
                int dx = gp.player.worldX - checkpoints[i][0];
                int dy = gp.player.worldY - checkpoints[i][1];
                double dist = Math.sqrt(dx * dx + dy * dy);
                if (dist <= CHECKPOINT_RADIUS) {
                    checkpointHit[i] = true;
                    checkpointsHit++;
                    gp.ui.showMessage("Checkpoint " + checkpointsHit + "/" + TOTAL_CHECKPOINTS + "!");
                }
            }
        }

        if (checkpointsHit >= TOTAL_CHECKPOINTS && !courseCompleted) {
            courseCompleted = true;
            gp.ui.showMessage("Course complete!");

            //STATS GAINED
            gp.player.charisma += 2;
        }
    }

    public boolean hasAllArtSupplies() {
        return countItem("Paint Bucket") >= PAINT_BUCKETS_REQUIRED &&
                countItem("Paintbrush")   >= PAINTBRUSH_REQUIRED    &&
                countItem("Canvas")       >= CANVAS_REQUIRED;
    }

    public int countItem(String itemName) {
        int count = 0;
        for (Entity e : gp.player.inventory) {
            if (e != null && e.name.equals(itemName)) count++;
        }
        return count;
    }

    public void removeArtSupplies() {
        removeItems("Paint Bucket", PAINT_BUCKETS_REQUIRED);
        removeItems("Paintbrush",   PAINTBRUSH_REQUIRED);
        removeItems("Canvas",       CANVAS_REQUIRED);

        //STATS GAINED
        gp.player.creativity = 2;
    }

    public boolean hasWritingSupplies() {
        return countItem("Quill")    >= QUILL_REQUIRED &&
                countItem("Notebook") >= NOTEBOOK_REQUIRED;
    }

    public void removeWritingSupplies() {
        removeItems("Quill",    QUILL_REQUIRED);
        removeItems("Notebook", NOTEBOOK_REQUIRED);

        //STATS GAINED
        gp.player.intellect += 2;
    }

    private void removeItems(String itemName, int amount) {
        int removed = 0;
        for (int i = 0; i < gp.player.inventory.size() && removed < amount; i++) {
            if (gp.player.inventory.get(i) != null &&
                    gp.player.inventory.get(i).name.equals(itemName)) {
                gp.player.inventory.remove(i);
                i--;
                removed++;
            }
        }
    }

    public void giveBoots() {
        OBJ_Boots boots = new OBJ_Boots(gp);
        gp.player.inventory.add(boots);
        normalSpeed = gp.player.speed;
        gp.player.speed = BOOST_SPEED;
        bootsActive = true;
        bootsStartTime = System.currentTimeMillis();
    }

    public void removeBoots() {
        removeItems("Boots", 1);
        gp.player.speed = normalSpeed;
        bootsActive = false;
        bootsEndTime = System.currentTimeMillis();
    }


    public void completeQuest2() {
        if (questState[QUEST2] == STATE_COMPLETED) return;
        questState[QUEST2] = STATE_COMPLETED;

        quest2Stage = GREGORIO_DONE;

        //STATS GAINED
        gp.player.inventory.add(new object.OBJ_Poem(gp));
        gp.player.exp += 4;
        gp.player.age += 3;

        gp.ui.showMessage("Quest " + questDisplayNumber(QUEST2) + ": Done!");
        pendingChapter2Cutscene = true;
        cutsceneDelay = 60;
        gp.saveManager.save();
    }

    // ===== QUEST 3 =====
    public void onFerrandoShooed() {
        ferrandoShooed = true;
        if (quest3Stage == TALK_FERRANDO) {
            quest3Stage = TALK_BURGOS;
        }
    }

    public void onBurgosDialogueDone() {
        if (ferrandoShooed && quest3Stage == TALK_BURGOS) {
            gp.cutsceneManager.startEnrollmentCutscene();
        }
    }

    public void onEnrollmentCutsceneDone() {
        quest3Stage = TALK_PROFESSOR;
    }

    public void onProfessorDone() {
        quest3Stage = TALK_STUDENT;
    }

    public void onQuizResult(int score) {
        quizAttempts++;

        if (firstQuizScore == -1) {
            firstQuizScore = score;
        }

        if (score >= 10) {
            quest3Stage = TALK_FERRANDO_REWARD;
        } else {
            quest3Stage = QUIZ_FAILED;
            gp.ui.showMessage("Score: " + score + "/10. Try again!");
        }
    }

    public void giveMedal() {
        OBJ_SobreMedal medal = new OBJ_SobreMedal(gp);
        gp.player.inventory.add(medal);
    }

    public void completeQuest3() {
        if (quest3Stage == QUEST3_DONE) return;

        giveMedal();

        //STATS GAINED
        gp.player.exp += 1;
        gp.player.intellect += 3;
        gp.player.age += 5;

        gp.ui.showMessage("You received a Sobresaliente medal! Quest " + questDisplayNumber(QUEST3) + ": Done!");

        quest3Stage = QUEST3_DONE;
        questState[QUEST3] = STATE_COMPLETED;

        pendingQuest4Cutscene = true;
        cutsceneDelay1 = 60;
        gp.saveManager.save();
    }

    // ===== QUEST 4 =====
    public void startQuest4() {
        currentQuest = QUEST4;
        questState[QUEST4] = STATE_ACTIVE;
        quest4Stage = TALK_PROFESSOR_Q4;
        medalsEarned = 0;
        disciplinesCompleted = 0;
        disciplineMedalEarned = new boolean[5];
        disciplineAnswered = new boolean[5];
        gp.ui.questPageNum = 1; // page 1: Q3+Q4
        gp.aSetter.activateQuest4();
    }

    public void onProfessorQ4Done() {
        if (quest4Stage == TALK_PROFESSOR_Q4) quest4Stage = TALK_MARIANO;
    }

    public void onMarianoDone() {
        if (quest4Stage == TALK_MARIANO) quest4Stage = TALK_RECTOR;
    }

    public void onRectorInitiate() {
        if (quest4Stage == TALK_RECTOR) {
            quest4Stage = DISCIPLINES_ACTIVE;
            disciplineAnswered = new boolean[5];
            disciplinesCompleted = 0;
        }
    }

    public void onDisciplineResult(int disciplineIndex, boolean correct) {
        if (disciplineAnswered[disciplineIndex]) return;

        disciplineAnswered[disciplineIndex] = true;
        disciplinesCompleted++;

        if (correct) {
            disciplineMedalEarned[disciplineIndex] = true;
            medalsEarned++;
            gp.ui.showMessage("Medal earned! (" + medalsEarned + "/" + MEDALS_REQUIRED + ")");
        }

        if (disciplinesCompleted >= 5) {
            quest4Stage = TALK_RECTOR_END;
        }
    }

    public void onRectorEnd() {
        if (quest4Stage != TALK_RECTOR_END) return;
        quest4Stage = QUEST4_DONE;
        questState[QUEST4] = STATE_COMPLETED;

        //STATS GAINED
        gp.player.exp += 3;
        gp.player.intellect += 3;
        gp.player.age += 7;

        gp.ui.showMessage("Noli Me Tangere Begins! Quest " + questDisplayNumber(QUEST4) + ": Done!");

        pendingChapter3Cutscene = true;
        cutsceneDelay2 = 60;
    }

    // ===== QUEST_MEMORIES =====
    public void onMemMaximoFirstTalked() {
        if (questMemStage == QM_TALK_MAXIMO_FIRST) {
            questMemStage = QM_COLLECT_5;
            gp.ui.showMessage("Find 6 mementos from your past.");
        }
    }

    public void onMemFirstPartCollected(int index) {
        if (index < 0 || index >= 6) return;
        if (memFirstParts[index]) return;
        memFirstParts[index] = true;
        memFirstCollected++;
        gp.ui.showMessage("Memento found! (" + memFirstCollected + "/6)");
        if (memFirstCollected >= 6) {
            questMemStage = QM_RETURN_MAXIMO_MID;
            gp.ui.showMessage("Return to Maximo Viola.");
        }
    }

    public void onMemMaximoMidTalked() {
        if (questMemStage == QM_RETURN_MAXIMO_MID) {
            questMemStage = QM_COLLECT_3;
            gp.ui.showMessage("Find 3 more mementos.");
        }
    }

    public void onMemSecondPartCollected(int index) {
        if (index < 0 || index >= 3) return;
        if (memSecondParts[index]) return;
        memSecondParts[index] = true;
        memSecondCollected++;
        gp.ui.showMessage("Memento found! (" + memSecondCollected + "/3)");
        if (memSecondCollected >= 3) {
            questMemStage = QM_RETURN_MAXIMO_FINAL;
            gp.ui.showMessage("Return to Maximo Viola.");
        }
    }

    public void completeQuestMemories() {
        if (questMemStage == QUEST_MEM_DONE) return;
        questMemStage = QUEST_MEM_DONE;
        questState[QUEST_MEMORIES] = STATE_COMPLETED;

        gp.player.exp += 2;
        gp.player.age += 1;
        gp.player.perception += 2;
        gp.player.creativity += 2;

        gp.ui.showMessage("Quest " + questDisplayNumber(QUEST_MEMORIES) + ": Done!");

        pendingQuestMemoriesCutscene = true;
        cutsceneDelay3 = 60;
        gp.saveManager.save();
    }

    public void onPedroDone() {
        if (quest5Stage == TALK_PEDRO) quest5Stage = TALK_CONSUELO;
    }

    public void onConsueloDone() {
        if (quest5Stage == TALK_CONSUELO) quest5Stage = FIND_LETTER;
    }

    public void onNoliDraftFound() {
        if (quest5Stage == FIND_LETTER) {
            quest5Stage = COLLECT_OBJECTS;
            int need = effectiveQuest5Objects();
            if (need < OBJECTS_REQUIRED)
                gp.ui.showMessage("Your creativity guides you - only " + need + " pieces needed!");
            else
                gp.ui.showMessage("Find " + need + " objects to write your manuscript.");
        }
    }

    public void onManuscriptPartCollected(int index) {
        if (manuscriptParts[index]) return;
        manuscriptParts[index] = true;
        objectsCollected++;
        if (objectsCollected >= effectiveQuest5Objects()) {
            quest5Stage = TALK_MAXIMO;
            gp.ui.showMessage("Manuscript complete!");
        }
    }

    public void completeQuest5() {
        if (quest5Stage == QUEST5_DONE) return;
        quest5Stage = QUEST5_DONE;
        questState[QUEST5] = STATE_COMPLETED;

        gp.player.exp += 3;
        gp.player.age += 3;
        gp.player.intellect += 2;
        gp.player.perception +=3;
        gp.player.charisma +=2;

        gp.ui.showMessage("El Filibusterismo begins! Quest " + questDisplayNumber(QUEST5) + ": Done!");

        pendingQuest6StartCutscene = true;
        cutsceneDelay4 = 60;
        gp.saveManager.save();
    }

    // QUEST 6
    public void startQuest6() {
        currentQuest = QUEST6;
        questState[QUEST6] = STATE_ACTIVE;
        quest6Stage = TALK_PACIANO_Q6;
        q6ObjectsCollected = 0;
        elFiliParts = new boolean[5];
        gp.ui.questPageNum = 4; // page 4: Q9+Q10
        gp.aSetter.activateQuest6();
        gp.saveManager.save();
    }

    public void onPacianoQ6Talked() {
        if (quest6Stage == TALK_PACIANO_Q6) {
            quest6Stage = FIND_DRAFT;
            gp.ui.showMessage("Find the El Fili Draft!");
        }
    }

    public void onElFiliDraftFound() {
        if (quest6Stage == FIND_DRAFT) {
            quest6Stage = COLLECT_OBJECTS_Q6;
            int need = effectiveQuest6Objects();
            if (need < Q6_OBJECTS_REQUIRED)
                gp.ui.showMessage("Your creativity guides you - only " + need + " pieces needed!");
            else
                gp.ui.showMessage("Find " + need + " objects to write your manuscript.");
        }
    }

    public void onq6ObjectsCollected(int index) {
        if (elFiliParts[index]) return;
        elFiliParts[index] = true;
        q6ObjectsCollected++;
        if (q6ObjectsCollected >= effectiveQuest6Objects()) {
            quest6Stage = RETURN_PACIANO;
            gp.ui.showMessage("Manuscript Complete!");
        }
    }

    public void giveElFiliBook() {
        object.OBJ_Draft2 book = new object.OBJ_Draft2(gp);
        gp.player.inventory.add(book);
    }

    public void onPacianoItemsReturned() {
        if (quest6Stage == RETURN_PACIANO) {
            completeQuest6();
        }
    }

    public void completeQuest6() {
        if (quest6Stage == QUEST6_DONE) return;
        quest6Stage = QUEST6_DONE;
        questState[QUEST6] = STATE_COMPLETED;

        gp.player.exp += 3;
        gp.player.age += 9;

        gp.player.intellect += 5;
        gp.player.perception += 6;
        gp.player.creativity += 8;
        gp.player.charisma += 5;

        gp.ui.showMessage("Quest " + questDisplayNumber(QUEST6) + ": Done!");

        pendingQuest7IntroCutscene = true;
        cutsceneDelay5 = 60;
        gp.saveManager.save();
    }

    // QUEST 7
    public void startQuest7() {
        currentQuest = QUEST7;
        questState[QUEST7] = STATE_ACTIVE;
        quest7Stage = Q7_TALK_GUARDIA;
        gp.ui.questPageNum = 4; // page 4: Q9+Q10
        gp.aSetter.activateQuest7Intramuros();
        gp.saveManager.save();
    }

    public void onGuardiaDone() {
        if (quest7Stage == Q7_TALK_GUARDIA)
            quest7Stage = Q7_TALK_JUDGE;
    }

    public void onJudgeDone() {
        if (quest7Stage != Q7_TALK_JUDGE) return;
        pendingQuest7MidCutscene = true;
        cutsceneDelay6 = 60;
    }

    public void onFortSantiagoCutsceneDone() {
        quest7Stage = Q7_TALK_JOSEPHINE;
        gp.aSetter.activateQuest7FortSantiago();
    }

    public void onJosephineDone() {
        if (quest7Stage == Q7_TALK_JOSEPHINE)
            quest7Stage = Q7_INTERACT_PAPER;
    }

    public void onFinalPaperInteracted() {
        if (quest7Stage == Q7_INTERACT_PAPER)
            quest7Stage = Q7_INTERACT_STOVE;
    }

    public void onAlcoholStoveInteracted() {
        if (quest7Stage == Q7_INTERACT_STOVE)
            quest7Stage = Q7_TALK_TRINIDAD;
    }

    public void giveUltimoAdios() {
        quest7Stage = Q7_DONE;
        gameEndTime = System.currentTimeMillis();
        gp.player.inventory.add(new object.OBJ_MiUltimoAdios(gp));
        gp.ui.showMessage("You received Mi Ultimo Adios.");
        questState[QUEST7] = STATE_COMPLETED;
        gp.player.exp += 3;

        gp.player.intellect += 5;
        gp.player.creativity += 5;
        gp.player.perception += 5;
        gp.player.charisma += 5;
        pendingQuest7EndCutscene = true;
        cutsceneDelay7 = 90;
        gp.saveManager.save();
    }

    // QUEST 8
    public void startQuest8() {
        currentQuest = QUEST8;
        questState[QUEST8] = STATE_ACTIVE;
        quest8Stage  = Q8_CUTSCENE;
        gp.ui.questPageNum = 3; // page 3: Q7+Q8 (Liham+Noli)
        gp.aSetter.activateQuest8();
        pendingQuest8Cutscene = true;
        cutsceneDelay9 = 60;
        gp.saveManager.save();
    }

    public void onQuest8CutsceneDone() {
        quest8Stage = Q8_TALK_MARCELO;
    }

    public void onMarceloInitialTalk() {
        quest8Stage = Q8_FIND_MALOLOS;
    }

    public void onMalolosPickup() {
        if (quest8Stage == Q8_FIND_MALOLOS) {
            q8MalolosCollected = true;
            quest8Stage        = Q8_RETURN_MALOLOS;
        }
    }

    public void onMarceloMalolosDone() {
        quest8Stage = Q8_FIND_CENTURY;
        gp.aSetter.spawnCenturyHence();
    }

    public void onCenturyPickup() {
        if (quest8Stage == Q8_FIND_CENTURY) {
            q8CenturyCollected = true;
            quest8Stage        = Q8_RETURN_CENTURY;
        }
    }

    public void onMarceloCenturyDone() {
        quest8Stage = Q8_FIND_INDOLENCE;
        gp.aSetter.spawnIndolenceEssay();
    }

    public void onIndolencePickup() {
        if (quest8Stage == Q8_FIND_INDOLENCE) {
            q8IndolenceCollected = true;
            quest8Stage          = Q8_RETURN_INDOLENCE;
        }
    }

    public void onMarceloIndolenceDone() {
        quest8Stage = Q8_FINAL_TALK;
    }

    public void completeQuest8() {
        quest8Stage  = Q8_DONE;
        questState[QUEST8] = STATE_COMPLETED;

        gp.player.exp += 3;
        gp.player.intellect += 4;
        gp.player.creativity += 3;
        gp.player.perception += 2;

        gp.ui.showMessage("Quest " + questDisplayNumber(QUEST8) + ": Done!");

        // Transition to Noli Me Tangere
        currentQuest = QUEST5;
        questState[QUEST5] = STATE_ACTIVE;
        quest5Stage = TALK_PEDRO;
        gp.ui.questPageNum = 3; // page 3: Q7+Q8 (Liham+Noli)
        gp.aSetter.activateChapter3();
        gp.saveManager.save();
    }

    public boolean isQuestActive(int quest) {
        return questState[quest] == STATE_ACTIVE;
    }
    public boolean isQuestCompleted(int quest) {
        return questState[quest] == STATE_COMPLETED;
    }


    public void setPendingChapter2Cutscene(boolean v) {
        pendingChapter2Cutscene = v;    if (v) cutsceneDelay  = 60; }
    public void setPendingQuest4Cutscene(boolean v) {
        pendingQuest4Cutscene = v;      if (v) cutsceneDelay1 = 60; }
    public void setPendingChapter3Cutscene(boolean v) {
        pendingChapter3Cutscene = v;    if (v) cutsceneDelay2 = 60; }
    public void setPendingQuestMemoriesCutscene(boolean v) {
        pendingQuestMemoriesCutscene = v; if (v) cutsceneDelay3 = 60; }
    public void setPendingQuestKeepsakesCutscene(boolean v) {
        pendingQuestKeepsakesCutscene = v; if (v) cutsceneDelay8 = 60; }
    public void setPendingQuest6StartCutscene(boolean v) {
        pendingQuest6StartCutscene = v; if (v) cutsceneDelay4 = 60; }
    public void setPendingQuest7IntroCutscene(boolean v) {
        pendingQuest7IntroCutscene = v; if (v) cutsceneDelay5 = 60; }
    public void setPendingQuest7MidCutscene(boolean v) {
        pendingQuest7MidCutscene = v;   if (v) cutsceneDelay6 = 60; }
    public void setPendingQuest7EndCutscene(boolean v) {
        pendingQuest7EndCutscene = v;   if (v) cutsceneDelay7 = 60; }
    public void setPendingQuest8Cutscene(boolean v) {
        pendingQuest8Cutscene = v;      if (v) cutsceneDelay9 = 60; }
}
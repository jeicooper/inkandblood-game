package main;

import entity.Entity;
import object.OBJ_Boots;
import object.OBJ_SobreMedal;

public class QuestManager {

    GamePanel gp;

    private int cutsceneDelay = 0;
    private int cutsceneDelay1 = 0;
    private int cutsceneDelay2 = 0;
//    private int cutsceneDelay3 = 0;
    private int cutsceneDelay4 = 0;
    private int cutsceneDelay5 = 0;
    private int cutsceneDelay6 = 0;
    private int cutsceneDelay7 = 0;
    private boolean pendingQuest7IntroCutscene = false;
    private boolean pendingQuest7MidCutscene   = false;
    private boolean pendingQuest7EndCutscene   = false;

    private boolean pendingChapter2Cutscene = false;
    private boolean pendingQuest4Cutscene = false;
    private boolean pendingChapter3Cutscene = false;
    private boolean pendingQuest6StartCutscene = false;


    // QUEST IDS
    public static final int QUEST1 = 0;
    public static final int QUEST2 = 1;
    public static final int QUEST3 = 2;
    public static final int QUEST4 = 3;
    public static final int QUEST5 = 4;
    public static final int QUEST6 = 5;
    public static final int QUEST7 = 6;


    //STATES
    public static final int STATE_INACTIVE = 0;
    public static final int STATE_ACTIVE = 1;
    public static final int STATE_COMPLETED = 2;

    public int currentQuest = QUEST1;
    public int[] questState = new int[10];

    // QUEST 1
    public static final int QUEST1_NOT_STARTED = 0;
    public static final int QUEST1_STARTED = 1;
    public int quest1Stage = QUEST1_NOT_STARTED;

    public int siblingsFound   = 0;
    public final int SIBLINGS_REQUIRED = 9;
    public boolean conchaVisited = false;

    public int deliveryWorldX;
    public int deliveryWorldY;
    public int deliveryRadius;

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
    public static final int DISCIPLINES_ACTIVE = 3;  // all 5 judges available
    public static final int TALK_RECTOR_END = 4;
    public static final int QUEST4_DONE = 5;
    public static final int MEDALS_REQUIRED = 5;

    public int quest4Stage = TALK_PROFESSOR_Q4;
    public boolean[] disciplineMedalEarned = new boolean[5];
    public int medalsEarned = 0;
    public boolean[] disciplineAnswered = new boolean[5];
    public int disciplinesCompleted = 0;

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

    // QUEST 7
    public static final int Q7_TALK_GUARDIA = 0;
    public static final int Q7_TALK_JUDGE = 1;
    public static final int Q7_TALK_JOSEPHINE = 2;
    public static final int Q7_INTERACT_PAPER = 3;
    public static final int Q7_INTERACT_STOVE = 4;
    public static final int Q7_TALK_TRINIDAD  = 5;
    public static final int Q7_DONE = 6;

    public int quest7Stage = Q7_TALK_GUARDIA;

    // CONSTRUCTOR
    public QuestManager(GamePanel gp) {
        this.gp = gp;
        questState[QUEST1] = STATE_ACTIVE;
    }

    //CHECKPOINTS
    public void init() {
        deliveryWorldX = 75 * gp.tileSize;
        deliveryWorldY = 28 * gp.tileSize;
        deliveryRadius = gp.tileSize * 2;

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

                gp.ui.questPageNum = 2;
            }
        }

        // TRANSITION TO CHAPTER 3
        if (pendingChapter3Cutscene) {
            cutsceneDelay2--;
            if (cutsceneDelay2 <= 0) {
                pendingChapter3Cutscene = false;
                gp.cutsceneManager.startChapter3();
                currentQuest = QUEST5;
                questState[QUEST5] = STATE_ACTIVE;
                gp.ui.questPageNum = 2;
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

        if (following >= SIBLINGS_REQUIRED && conchaVisited && playerInDeliveryZone()) {
            completeQuest1();
        }
    }

    private boolean playerInDeliveryZone() {
        int dx = gp.player.worldX - deliveryWorldX;
        int dy = gp.player.worldY - deliveryWorldY;
        return (dx * dx + dy * dy) <= (deliveryRadius * deliveryRadius);
    }

    private void completeQuest1() {
        questState[QUEST1] = STATE_COMPLETED;
        gp.ui.showMessage("Quest 1: Done!");

        //STATS GAINED
        gp.player.exp += 1;
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
    }

    public void removeBoots() {
        removeItems("Boots", 1);
        gp.player.speed = normalSpeed;
        bootsActive = false;
    }


    public void completeQuest2() {
        if (questState[QUEST2] == STATE_COMPLETED) return;
        questState[QUEST2] = STATE_COMPLETED;

        quest2Stage = GREGORIO_DONE;

        //STATS GAINED
        gp.player.inventory.add(new object.OBJ_Poem(gp));
        gp.player.exp += 3;
        gp.player.age += 3;

        gp.ui.showMessage("Quest 2: Done!");
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
        if (score >= 10) {
            quest3Stage = TALK_FERRANDO_REWARD;
        } else {
            quest3Stage = QUIZ_FAILED;
            gp.ui.showMessage("Score: " + score + "/5. Try again!");
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

        gp.ui.showMessage("You received a Sobresaliente medal! Quest 3: Done!");

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
        gp.ui.questPageNum = 1;
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
        if (disciplineAnswered[disciplineIndex]) return; // guard against repeat

        disciplineAnswered[disciplineIndex] = true;
        disciplinesCompleted++;

        if (correct) {
            disciplineMedalEarned[disciplineIndex] = true;
            medalsEarned++;
            gp.ui.showMessage("Medal earned! (" + medalsEarned + "/" + MEDALS_REQUIRED + ")");
        } else {

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
        gp.player.exp += 2;
        gp.player.intellect += 3;
        gp.player.age += 7;

        gp.ui.showMessage("Quest 4: Done!");

        pendingChapter3Cutscene = true;
        cutsceneDelay2 = 60;
        gp.saveManager.save();
    }

    // ===== QUEST 5 =====
    public void onPedroDone() {
        if (quest5Stage == TALK_PEDRO) quest5Stage = TALK_CONSUELO;
    }

    public void onConsueloDone() {
        if (quest5Stage == TALK_CONSUELO) quest5Stage = FIND_LETTER;
    }

    public void onNoliDraftFound() {
        if (quest5Stage == FIND_LETTER) {
            quest5Stage = COLLECT_OBJECTS;
            gp.ui.showMessage("Find objects around the room to write your manuscript.");
            gp.saveManager.save();
        }
    }

    public void onManuscriptPartCollected(int index) {
        if (manuscriptParts[index]) return;
        manuscriptParts[index] = true;
        objectsCollected++;
        if (objectsCollected >= OBJECTS_REQUIRED) {
            quest5Stage = TALK_MAXIMO;
            gp.ui.showMessage("Manuscript complete!");
            gp.saveManager.save();
        }
    }

    public void completeQuest5() {
        if (quest5Stage == QUEST5_DONE) return;
        quest5Stage = QUEST5_DONE;
        questState[QUEST5] = STATE_COMPLETED;

        gp.player.exp += 2;
        gp.player.age += 3;
        gp.player.intellect += 2;
        gp.player.perception +=3;
        gp.player.charisma +=2;

        gp.ui.showMessage(" Noli Me Tangere begins! Quest 5: Done!");

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
        gp.ui.questPageNum = 2;
        gp.aSetter.activateQuest6();
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
            gp.ui.showMessage("Find objects around the room to write your manuscript.");
            gp.saveManager.save();
        }
    }

    public void onq6ObjectsCollected (int index){
        if (elFiliParts[index]) return;
        elFiliParts[index] = true;
        q6ObjectsCollected++;

        if (q6ObjectsCollected >= Q6_OBJECTS_REQUIRED){
            quest6Stage = RETURN_PACIANO;
            gp.ui.showMessage("Manuscript Complete!");
            gp.saveManager.save();
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

        gp.player.exp += 2;
        gp.player.age += 9;

        gp.player.intellect += 1;
        gp.player.perception += 2;
        gp.player.creativity += 3;

        gp.ui.showMessage("El Filibusterismo begins! Quest 6: Done!");

        pendingQuest7IntroCutscene = true;
        cutsceneDelay4 = 60;
        gp.saveManager.save();
    }

    // QUEST 7
    public void startQuest7() {
        currentQuest = QUEST7;
        questState[QUEST7] = STATE_ACTIVE;
        quest7Stage = Q7_TALK_GUARDIA;
        gp.ui.questPageNum = 3;
        gp.aSetter.activateQuest7Intramuros();
    }

    public void onGuardiaDone() {
        if (quest7Stage == Q7_TALK_GUARDIA)
            quest7Stage = Q7_TALK_JUDGE;
    }

    public void onJudgeDone() {
        if (quest7Stage != Q7_TALK_JUDGE) return;
        pendingQuest7MidCutscene = true;
        cutsceneDelay6 = 90;
        gp.saveManager.save();
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
        if (quest7Stage != Q7_TALK_TRINIDAD) return;
        quest7Stage = Q7_DONE;
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

    public boolean isQuestActive(int quest) {
        return questState[quest] == STATE_ACTIVE;
    }
    public boolean isQuestCompleted(int quest) {
        return questState[quest] == STATE_COMPLETED;
    }

    public boolean isPendingChapter2Cutscene()    { return pendingChapter2Cutscene; }
    public boolean isPendingQuest4Cutscene()      { return pendingQuest4Cutscene; }
    public boolean isPendingChapter3Cutscene()    { return pendingChapter3Cutscene; }
    public boolean isPendingQuest6StartCutscene() { return pendingQuest6StartCutscene; }
    public boolean isPendingQuest7IntroCutscene() { return pendingQuest7IntroCutscene; }
    public boolean isPendingQuest7MidCutscene()   { return pendingQuest7MidCutscene; }
    public boolean isPendingQuest7EndCutscene()   { return pendingQuest7EndCutscene; }

    public void setPendingChapter2Cutscene(boolean v)    { pendingChapter2Cutscene = v;    if (v) cutsceneDelay  = 60; }
    public void setPendingQuest4Cutscene(boolean v)      { pendingQuest4Cutscene = v;      if (v) cutsceneDelay1 = 60; }
    public void setPendingChapter3Cutscene(boolean v)    { pendingChapter3Cutscene = v;    if (v) cutsceneDelay2 = 60; }
    public void setPendingQuest6StartCutscene(boolean v) { pendingQuest6StartCutscene = v; if (v) cutsceneDelay4 = 60; }
    public void setPendingQuest7IntroCutscene(boolean v) { pendingQuest7IntroCutscene = v; if (v) cutsceneDelay5 = 60; }
    public void setPendingQuest7MidCutscene(boolean v)   { pendingQuest7MidCutscene = v;   if (v) cutsceneDelay6 = 60; }
    public void setPendingQuest7EndCutscene(boolean v)   { pendingQuest7EndCutscene = v;   if (v) cutsceneDelay7 = 60; }
}
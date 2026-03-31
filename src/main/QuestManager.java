package main;

import entity.Entity;
import object.OBJ_Boots;
import object.OBJ_SobreMedal;

public class QuestManager {

    GamePanel gp;

    // QUEST IDS
    public static final int QUEST1   = 0;
    public static final int QUEST2   = 1;
    public static final int QUEST3 = 2;


    //STATES
    public static final int STATE_INACTIVE   = 0;
    public static final int STATE_ACTIVE     = 1;
    public static final int STATE_COMPLETED  = 2;

    public int currentQuest = QUEST1;
    public int[] questState = new int[10];

    // QUEST 1
    public static final int QUEST1_NOT_STARTED = 0;
    public static final int QUEST1_STARTED     = 1;
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
    public static final int JOSE_INACTIVE  = 0;
    public static final int JOSE_WAITING   = 1;
    public static final int JOSE_DONE      = 2;
    public static final int MANUEL_RUNNING = 3;
    public static final int MANUEL_DONE    = 4;
    public static final int GREGORIO_WAITING = 5;
    public static final int GREGORIO_DONE    = 6;

    public int quest2Stage = JOSE_INACTIVE;

    public final int PAINT_BUCKETS_REQUIRED = 6;
    public final int PAINTBRUSH_REQUIRED    = 1;
    public final int CANVAS_REQUIRED        = 1;
    public final int QUILL_REQUIRED    = 1;
    public final int NOTEBOOK_REQUIRED = 1;

    public int checkpointsHit          = 0;
    public final int TOTAL_CHECKPOINTS = 6;
    public boolean courseCompleted     = false;
    public boolean bootsActive         = false;

    private int normalSpeed;
    private static final int BOOST_SPEED = 10;

    public int[][] checkpoints;
    public boolean[] checkpointHit;
    public final int CHECKPOINT_RADIUS = 60;

    // QUEST 3
    public static final int TALK_FERRANDO  = 0;
    public static final int TALK_BURGOS    = 1;
    public static final int CUTSCENE_DONE  = 2;
    public static final int TALK_PROFESSOR = 3;
    public static final int TALK_STUDENT   = 4;
    public static final int QUIZ_FAILED    = 5;
    public static final int TALK_FERRANDO_REWARD = 6;
    public static final int QUEST3_DONE          = 7;

    public int     quest3Stage          = TALK_FERRANDO;
    public boolean ferrandoShooed       = false;

    // CONSTRUCTOR
    public QuestManager(GamePanel gp) {
        this.gp = gp;
        questState[QUEST1] = STATE_ACTIVE;
    }


    //CHECKPOINTS
    public void init() {
        deliveryWorldX = 74 * gp.tileSize;
        deliveryWorldY = 26 * gp.tileSize;
        deliveryRadius = gp.tileSize * 2;

        checkpoints = new int[][]{
                {74 * gp.tileSize, 34 * gp.tileSize},
                {74 * gp.tileSize, 34 * gp.tileSize},
                {74 * gp.tileSize, 34 * gp.tileSize},
                {74 * gp.tileSize, 34 * gp.tileSize},
                {74 * gp.tileSize, 34 * gp.tileSize},
                {74 * gp.tileSize, 34 * gp.tileSize},
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

        gp.player.exp += 1;
        gp.player.perception += 1;
        for (int i = 0; i < gp.npc.length; i++) {
            if (gp.npc[i] instanceof entity.NPC_Sibling) {
                gp.npc[i] = null;
            }
        }

        currentQuest = QUEST2;
        questState[QUEST2] = STATE_ACTIVE;
        quest2Stage = JOSE_INACTIVE;

        gp.aSetter.activateQuest2();
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
            gp.player.charisma += 1;
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
        gp.player.creativity = 1;
    }

    public boolean hasWritingSupplies() {
        return countItem("Quill")    >= QUILL_REQUIRED &&
                countItem("Notebook") >= NOTEBOOK_REQUIRED;
    }

    public void removeWritingSupplies() {
        removeItems("Quill",    QUILL_REQUIRED);
        removeItems("Notebook", NOTEBOOK_REQUIRED);
        gp.player.intellect += 1;
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

    public void onManuelDone() {
        quest2Stage = MANUEL_DONE;
        gp.questManager.removeBoots();
        gp.aSetter.activateGregorio();
    }

    public void completeQuest2() {
        quest2Stage = GREGORIO_DONE;
        questState[QUEST2] = STATE_COMPLETED;
        gp.player.exp += 1;
        gp.player.age += 3;

        gp.cutsceneManager.startChapter2();
        currentQuest = QUEST3;
        questState[QUEST3] = STATE_ACTIVE;
        quest3Stage  = TALK_FERRANDO;
        ferrandoShooed = false;
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
        if (score >= 5) {
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

    public void onFerrandoReward() {
        quest3Stage = QUEST3_DONE;
        questState[QUEST3] = STATE_COMPLETED;
        giveMedal();
        gp.player.exp += 1;
        gp.ui.showMessage("You received a medal!");
        gp.ui.showMessage("Quest 3: Done!");
        gp.player.intellect += 2;
    }

    public void completeQuest3() {
        quest3Stage = QUEST3_DONE;
        questState[QUEST3] = STATE_COMPLETED;
        gp.ui.showMessage("You received a medal!");
        gp.ui.showMessage("Quest 3: Done!");
        gp.player.exp += 1;
        gp.player.intellect += 2;
    }

    public boolean isQuestActive(int quest) {
        return questState[quest] == STATE_ACTIVE;
    }
    public boolean isQuestCompleted(int quest) {
        return questState[quest] == STATE_COMPLETED;
    }


}
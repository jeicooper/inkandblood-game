package main;

import entity.Entity;
import object.OBJ_Boots;

public class QuestManager {

    GamePanel gp;

    // QUESTS
    public static final int QUEST1   = 0;   // find all siblings
    public static final int QUEST2   = 1;


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

    public int quest2Stage = JOSE_INACTIVE;

    public final int PAINT_BUCKETS_REQUIRED = 6;
    public final int PAINTBRUSH_REQUIRED    = 1;
    public final int CANVAS_REQUIRED        = 1;

    public int checkpointsHit          = 0;
    public final int TOTAL_CHECKPOINTS = 4;
    public boolean courseCompleted     = false;
    public boolean bootsActive         = false;

    private int normalSpeed;
    private static final int BOOST_SPEED = 10;

    public int[][] checkpoints;
    public boolean[] checkpointHit;
    public final int CHECKPOINT_RADIUS = 60;

    // ── Constructor ────────────────────────────────────────────
    public QuestManager(GamePanel gp) {
        this.gp = gp;
        questState[QUEST1] = STATE_ACTIVE;
    }

    public void init() {
        deliveryWorldX = 74 * gp.tileSize;
        deliveryWorldY = 26 * gp.tileSize;
        deliveryRadius = gp.tileSize * 2;

        checkpoints = new int[][]{
                {20 * gp.tileSize, 20 * gp.tileSize},
                {90 * gp.tileSize, 20 * gp.tileSize},
                {90 * gp.tileSize, 85 * gp.tileSize},
                {20 * gp.tileSize, 85 * gp.tileSize},
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

    // QUEST 1
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
        System.out.println("Quest done. Player exp = " + gp.player.exp);

        for (int i = 0; i < gp.npc.length; i++) {
            if (gp.npc[i] instanceof entity.NPC_Sibling) {
                gp.npc[i] = null;
            }
        }

        currentQuest = QUEST2;
        questState[QUEST2] = STATE_ACTIVE;
        quest2Stage = JOSE_INACTIVE;
        System.out.println("Quest 2 started. currentQuest = " + currentQuest + " stage = " + quest2Stage);

        gp.aSetter.activateQuest2();
    }

    // QUEST 2
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
            gp.ui.showMessage("Course complete! Go back to Uncle Manuel!");
        }
    }

    // ── Art supplies helpers ───────────────────────────────────
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
        questState[QUEST2] = STATE_COMPLETED;
        gp.ui.showMessage("Quest 2: Done!");
        gp.player.exp += 1;
        System.out.println("Quest 2 done. Player exp = " + gp.player.exp);
    }

    public boolean isQuestActive(int quest) {
        return questState[quest] == STATE_ACTIVE;
    }

    public boolean isQuestCompleted(int quest) {
        return questState[quest] == STATE_COMPLETED;
    }
}
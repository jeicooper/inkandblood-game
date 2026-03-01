package main;

public class QuestManager {

    GamePanel gp;

    // ── Quest indices ──────────────────────────────────────────
    public static final int QUEST_CHAP1_1   = 0;   // find all siblings
    public static final int QUEST_CHAP1_2   = 1;


    // ── Quest states ──────────────────────────────────────────
    public static final int STATE_INACTIVE   = 0;
    public static final int STATE_ACTIVE     = 1;
    public static final int STATE_COMPLETED  = 2;

    // Current active quest (-1 = none)
    public int currentQuest = QUEST_CHAP1_1;

    // Per-quest state array
    public int[] questState = new int[10];

    // ── Quest 0 : Familya Rizal ────────────────────────────────
    public int siblingsFound   = 0;
    public final int SIBLINGS_REQUIRED = 9; // 9 living siblings need to be delivered
    public boolean conchaVisited = false;

    // Delivery zone (world coords, tile-based – set in setupGame after gp.tileSize is known)
    public int deliveryWorldX;
    public int deliveryWorldY;
    public int deliveryRadius;   // pixels

    public boolean quest0JustCompleted = false;   // one-frame flag for message

    // ── Constructor ────────────────────────────────────────────
    public QuestManager(GamePanel gp) {
        this.gp = gp;
        questState[QUEST_CHAP1_1] = STATE_ACTIVE;
    }

    // Call this after tileSize is ready
    public void init() {
        // Delivery zone: Teodora's house area – adjust tile coords to match your map
        deliveryWorldX = 74 * gp.tileSize;
        deliveryWorldY = 26 * gp.tileSize;
        deliveryRadius = gp.tileSize * 2;
    }

    // ── Called every frame from GamePanel.update() ─────────────
    public void update() {
        if (currentQuest == QUEST_CHAP1_1 && questState[QUEST_CHAP1_1] == STATE_ACTIVE) {
            updateQuest0();
        }
    }

    private int debugTimer = 0;
    private void updateQuest0() {
        int following = 0;
        for (int i = 0; i < gp.npc.length; i++) {
            if (gp.npc[i] instanceof entity.NPC_Sibling) {
                entity.NPC_Sibling s = (entity.NPC_Sibling) gp.npc[i];
                if (s.isFollowing) following++;
            }
        }
        siblingsFound = following + (conchaVisited ? 1 : 0);

        gp.player.rebuildCongoLine();

        // print every 60 frames so console isn't flooded
        debugTimer++;
        if (debugTimer >= 60) {
            System.out.println("following: " + following + " | conchaVisited: " + conchaVisited + " | inZone: " + playerInDeliveryZone());
            debugTimer = 0;
        }

        if (following >= SIBLINGS_REQUIRED && conchaVisited && playerInDeliveryZone()) {
            completeQuest0();
        }
    }

    private boolean playerInDeliveryZone() {
        int dx = gp.player.worldX - deliveryWorldX;
        int dy = gp.player.worldY - deliveryWorldY;
        return (dx * dx + dy * dy) <= (deliveryRadius * deliveryRadius);
    }

    private void completeQuest0() {
        questState[QUEST_CHAP1_1] = STATE_COMPLETED;
        gp.ui.showMessage("Quest 1: Done!");

        gp.player.exp += 1;
        System.out.println("Quest done. Player exp = " + gp.player.exp);

        // remove all siblings from the NPC array
        for (int i = 0; i < gp.npc.length; i++) {
            if (gp.npc[i] instanceof entity.NPC_Sibling) {
                gp.npc[i] = null;
            }
        }
    }

    public boolean isQuestActive(int quest) {
        return questState[quest] == STATE_ACTIVE;
    }

    public boolean isQuestCompleted(int quest) {
        return questState[quest] == STATE_COMPLETED;
    }
}
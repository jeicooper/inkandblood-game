package entity;

import main.GamePanel;

import java.util.LinkedList;

public class NPC_Sibling extends Entity {

    // ── Identity ──────────────────────────────────────────────
    public String siblingName;
    // ── Follow state ──────────────────────────────────────────
    public boolean isFollowing = false;
    public boolean delivered   = false;

    // The entity this sibling is tailing (player or the sibling ahead)
    public Entity followTarget = null;

    // History queue: stores past (worldX, worldY) of the target so the
    // sibling walks exactly where the target walked, producing a smooth
    // congo-line effect.
    private final LinkedList<int[]> posHistory = new LinkedList<>();
    private static final int HISTORY_DELAY = 30; // frames of lag between each link

    // ── Dialogue ──────────────────────────────────────────────
    private String[] greetDialogue  = new String[3];
    private String[] followDialogue = new String[3];

    // ── Constructor ───────────────────────────────────────────
    public NPC_Sibling(GamePanel gp, String siblingName, String spritePath) {
        super(gp);
        this.siblingName = siblingName;

        setHitbox();
        direction = "down";
        speed = 3; // match player speed

        loadSprites(spritePath);
        buildDialogue();
    }

    // ── Sprite loading ────────────────────────────────────────
    private void loadSprites(String basePath) {
        // Fall back to a default sprite path if specific ones don't exist.
        // All directions use the same base; swap to directional if you have them.
        up1    = setup(basePath + "_up_1");
        up2    = setup(basePath + "_up_2");
        down1  = setup(basePath + "_down_1");
        down2  = setup(basePath + "_down_2");
        left1  = setup(basePath + "_left_1");
        left2  = setup(basePath + "_left_2");
        right1 = setup(basePath + "_right_1");
        right2 = setup(basePath + "_right_2");

        // If directional sprites failed, fall back to generic sibling sprite
        if (down1 == null) {
            String fallback = "/npc/sibling/sibling_default";
            up1 = up2 = down1 = down2 = left1 = left2 = right1 = right2 = setup(fallback);
        }
    }

    // ── Dialogue ──────────────────────────────────────────────
    private void buildDialogue() {
        greetDialogue[0]  = "Pepe! There you are. \nI've been looking for you everywhere!";
        greetDialogue[1]  = "Ate/Kuya " + siblingName + " will follow you now. \nLet's head back home to Nanay.";

        followDialogue[0] = "I'm right behind you, Pepe!";
        followDialogue[1] = "Don't walk too fast, Pepe!";
        followDialogue[2] = "Are we almost there?";

        dialogues = greetDialogue; // expose via Entity field (re-assign for speak())
    }

    // ── Entity.speak() override ───────────────────────────────
    @Override
    public void speak() {
        if (!isFollowing) {
            // First interaction – greet and start following
            dialogues = greetDialogue;
            super.speak();
            startFollowing();
        } else {
            // Already following – give a follow line
            dialogues = followDialogue;
            super.speak();
        }
    }

    public void startFollowing() {
        isFollowing = true;
        posHistory.clear();
    }

    // ── setAction / update ────────────────────────────────────
    @Override
    public void setAction() {

    }

    @Override
    public void update() {
        if (delivered) return;

        if (isFollowing && followTarget != null) {
            followUpdate();

            // sprite animation while following
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

    // ── Congo-line following logic ─────────────────────────────
    private void followUpdate() {
        // Record target's current position into history
        posHistory.addLast(new int[]{followTarget.worldX, followTarget.worldY});

        // Only move once we have enough history (creates the lag / gap)
        if (posHistory.size() > HISTORY_DELAY) {
            int[] targetPos = posHistory.removeFirst();
            int destX = targetPos[0];
            int destY = targetPos[1];

            int dx = destX - worldX;
            int dy = destY - worldY;
            double dist = Math.sqrt(dx * dx + dy * dy);

            if (dist > speed) {
                // Move toward recorded position
                double stepX = (dx / dist) * speed;
                double stepY = (dy / dist) * speed;
                worldX += (int) stepX;
                worldY += (int) stepY;

                // Update facing direction
                if (Math.abs(dx) > Math.abs(dy)) {
                    direction = (dx > 0) ? "right" : "left";
                } else {
                    direction = (dy > 0) ? "down" : "up";
                }
            }
        }
    }

    // ── Helpers ───────────────────────────────────────────────
    private void facePlayer() {
        int dx = gp.player.worldX - worldX;
        int dy = gp.player.worldY - worldY;
        if (Math.abs(dx) > Math.abs(dy)) {
            direction = (dx > 0) ? "right" : "left";
        } else {
            direction = (dy > 0) ? "down" : "up";
        }
    }
}
package entity;

import main.GamePanel;
import main.QuestManager;

import java.util.LinkedList;

public class NPC_Sibling extends Entity {

    public String siblingName;
    public boolean isFollowing = false;
    public boolean delivered   = false;

    public Entity followTarget = null;

    private final LinkedList<int[]> posHistory = new LinkedList<>();
    private static final int HISTORY_DELAY = 30;

    private String[] greetDialogue  = new String[3];
    private String[] followDialogue = new String[3];

    public NPC_Sibling(GamePanel gp, String siblingName, String spritePath) {
        super(gp);
        this.siblingName = siblingName;

        setHitbox();
        direction = "down";
        speed = 3; // match player speed

        loadSprites(spritePath);
        buildDialogue();
    }

    private void loadSprites(String basePath) {

        up1    = setup(basePath + "_up_1");
        up2    = setup(basePath + "_up_2");
        down1  = setup(basePath + "_down_1");
        down2  = setup(basePath + "_down_2");
        left1  = setup(basePath + "_left_1");
        left2  = setup(basePath + "_left_2");
        right1 = setup(basePath + "_right_1");
        right2 = setup(basePath + "_right_2");

        if (down1 == null) {
            String fallback = "/npc/sibling/sibling_default";
            up1 = up2 = down1 = down2 = left1 = left2 = right1 = right2 = setup(fallback);
        }
    }

    private void buildDialogue() {
        greetDialogue[0]  = "What is it Pepe?. Oh! is it time to eat?";

        dialogues = greetDialogue;
    }

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = siblingName;

        if (gp.questManager.quest1Stage == QuestManager.QUEST1_NOT_STARTED) {
            gp.ui.currentDialogue = "...";
            dialogueIndex = 0;

            switch (gp.player.direction){
                case "up":    direction = "down";  break;
                case "down":  direction = "up";    break;
                case "left":  direction = "right"; break;
                case "right": direction = "left";  break;
            }
            return;
        }

        if (!isFollowing) {
            dialogues = greetDialogue;
            super.speak();
            startFollowing();
        } else {
            dialogues = followDialogue;
            super.speak();
        }
    }

    public void startFollowing() {
        isFollowing = true;
        posHistory.clear();
    }

    @Override
    public void setAction() {

    }

    @Override
    public void update() {
        if (delivered) return;

        if (isFollowing && followTarget != null) {
            followUpdate();

            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

    private void followUpdate() {
        posHistory.addLast(new int[]{followTarget.worldX, followTarget.worldY});

        if (posHistory.size() > HISTORY_DELAY) {
            int[] targetPos = posHistory.removeFirst();
            int destX = targetPos[0];
            int destY = targetPos[1];

            int dx = destX - worldX;
            int dy = destY - worldY;
            double dist = Math.sqrt(dx * dx + dy * dy);

            if (dist > speed) {
                // figure out direction first
                if (Math.abs(dx) > Math.abs(dy)) {
                    direction = (dx > 0) ? "right" : "left";
                } else {
                    direction = (dy > 0) ? "down" : "up";
                }

                // check tile collision before moving
                collisionOn = false;
                gp.cChecker.checkTile(this);
                gp.cChecker.checkObject(this, false);

                if (!collisionOn) {
                    double stepX = (dx / dist) * speed;
                    double stepY = (dy / dist) * speed;
                    worldX += (int) stepX;
                    worldY += (int) stepY;
                } else {
                    // blocked — try moving on just one axis
                    collisionOn = false;
                    if (Math.abs(dx) > Math.abs(dy)) {
                        // try vertical instead
                        direction = (dy > 0) ? "down" : "up";
                        gp.cChecker.checkTile(this);
                        if (!collisionOn) worldY += (dy > 0) ? speed : -speed;
                    } else {
                        // try horizontal instead
                        direction = (dx > 0) ? "right" : "left";
                        gp.cChecker.checkTile(this);
                        if (!collisionOn) worldX += (dx > 0) ? speed : -speed;
                    }
                }
            }
        }
    }

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
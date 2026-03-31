package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Jose extends Entity {

    public int dialogueStage = 0;

    public NPC_Jose(GamePanel gp) {
        super(gp);

        direction = "down";
        speed = 0;

        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        getImage();
        setDialogue();
    }

    public void getImage() {
        up1    = setup("/npc/jose/jose_up_1");
        up2    = setup("/npc/jose/jose_up_2");
        down1  = setup("/npc/jose/jose_down_1");
        down2  = setup("/npc/jose/jose_down_2");
        left1  = setup("/npc/jose/jose_left_1");
        left2  = setup("/npc/jose/jose_left_2");
        right1 = setup("/npc/jose/jose_right_1");
        right2 = setup("/npc/jose/jose_right_2");
    }

    public void setDialogue() {}

    @Override
    public void setAction() {
    }

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Uncle Jose";

        if (dialogueStage == 0) {
            dialogues[0] = "Pepe! I have been waiting for you.";
            dialogues[1] = "To learn the basics of colors, you will need to gather some supplies.";
            dialogues[2] = "Find 6 paint buckets, 1 paintbrush, and 1 blank canvas.";
            dialogues[3] = "Bring them back to me when you have collected them all.";
            dialogues[4] = null;

            super.speak();

            if (dialogueIndex == 0) {
                dialogueStage = 1;
                gp.questManager.quest2Stage = QuestManager.JOSE_WAITING;
            }

        } else if (dialogueStage == 1) {
            if (gp.questManager.hasAllArtSupplies()) {
                dialogues[0] = "Excellent, Pepe! You have gathered everything we need.";
                dialogues[1] = "Let me take those from you.";
                dialogues[2] = "You have learned the basics of colors. Well done!";
                dialogues[3] = "Now go find your Uncle Manuel. He will train your body.";
                dialogues[4] = null;

                super.speak();

                if (dialogueIndex == 0) {
                    dialogueStage = 2;
                    gp.questManager.quest2Stage = QuestManager.JOSE_DONE;
                    gp.questManager.removeArtSupplies();
                }
            } else {
                int buckets = gp.questManager.countItem("Paint Bucket");
                int brush   = gp.questManager.countItem("Paintbrush");
                int canvas  = gp.questManager.countItem("Canvas");
                dialogues[0] = "You still need more supplies, Pepe.";
                dialogues[1] = "Paint Buckets: " + buckets + "/6\nPaintbrush: " + brush + "/1\nCanvas: " + canvas + "/1";
                dialogues[2] = null;
                dialogueIndex = 0;
                super.speak();
            }

        } else if (dialogueStage == 2) {
            dialogues[0] = "Go find Uncle Manuel, Pepe. He is waiting for you.";
            dialogues[1] = null;
            dialogueIndex = 0;
            super.speak();
        }
    }

    @Override
    public void update() {
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

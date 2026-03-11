package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Gregorio extends Entity {

    public int dialogueStage = 0;
    // 0 = first meeting, send to house
    // 1 = waiting for quill and notebook
    // 2 = items submitted, show poem, congratulate

    public NPC_Gregorio(GamePanel gp) {
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
    }

    public void getImage() {
        up1    = setup("/npc/gregorio/gregorio_up_1");
        up2    = setup("/npc/gregorio/gregorio_up_2");
        down1  = setup("/npc/gregorio/gregorio_down_1");
        down2  = setup("/npc/gregorio/gregorio_down_2");
        left1  = setup("/npc/gregorio/gregorio_left_1");
        left2  = setup("/npc/gregorio/gregorio_left_2");
        right1 = setup("/npc/gregorio/gregorio_right_1");
        right2 = setup("/npc/gregorio/gregorio_right_2");

        if (down1 == null) {
            down1 = down2 = up1 = up2 = left1 = left2 = right1 = right2
                    = setup("/npc/teodora/teodora_down_1");
        }
    }

    @Override
    public void setAction() {}

    @Override
    public void update() {
        facePlayer();
    }

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Uncle Gregorio";

        // only accessible after Manuel is done
        if (gp.questManager.quest2Stage < QuestManager.MANUEL_DONE) {
            dialogues[0] = "Hello Pepe. Go see your\nUncle Manuel first.";
            dialogues[1] = null;
            dialogueIndex = 0;
            super.speak();
            return;
        }

        if (dialogueStage == 0) {
            dialogues[0] = "Pepe! I have been expecting you.";
            dialogues[1] = "It is time to learn the power\nof words and writing.";
            dialogues[2] = "Go to my house and find\nmy quill and notebook.";
            dialogues[3] = "Bring them back to me\nwhen you have found them.";
            dialogues[4] = null;

            super.speak();

            if (dialogueIndex == 0) {
                dialogueStage = 1;
                gp.questManager.quest2Stage = QuestManager.GREGORIO_WAITING;
            }

        } else if (dialogueStage == 1) {
            if (gp.questManager.hasWritingSupplies()) {
                dialogues[0] = "Excellent! You found them, Pepe.";
                dialogues[1] = "Let me show you something\nvery important.";
                dialogues[2] = null;

                super.speak();

                if (dialogueIndex == 0) {
                    dialogueStage = 2;
                    gp.questManager.removeWritingSupplies();
                    gp.ui.showPoemPanel = true; // trigger poem panel
                }
            } else {
                int quill    = gp.questManager.countItem("Quill");
                int notebook = gp.questManager.countItem("Notebook");
                dialogues[0] = "You still need the supplies, Pepe.";
                dialogues[1] = "Quill: " + quill + "/1\nNotebook: " + notebook + "/1";
                dialogues[2] = null;
                dialogueIndex = 0;
                super.speak();
            }

        } else if (dialogueStage == 2) {
            dialogues[0] = "This is a poem about our language,\nPepe. Never forget it.";
            dialogues[1] = "You have done well today.\nYour education has begun.";
            dialogues[2] = "Chapter 1 complete, Pepe.\nYour journey continues.";
            dialogues[3] = null;

            super.speak();

            if (dialogueIndex == 0) {
                dialogueStage = 3;
                gp.questManager.completeQuest2();
            }
        } else if (dialogueStage == 3) {
            dialogues[0] = "Remember the poem, Pepe.\nIt will guide you always.";
            dialogues[1] = null;
            dialogueIndex = 0;
            super.speak();
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
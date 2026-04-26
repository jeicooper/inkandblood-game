package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Gregorio extends Entity {

    public int dialogueStage = 0;

    public NPC_Gregorio(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 0;
        dexId = "gregorio";

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
    public void speak() {
        gp.ui.currentSpeakerName = "Uncle Gregorio";

        if (gp.questManager.quest2Stage < QuestManager.MANUEL_DONE) {
            dialogues[0] = "Hello Pepe. Go see your Uncle Manuel first.";
            dialogues[1] = null;
            dialogueIndex = 0;
            super.speak();
            return;
        }

        if (dialogueStage == 0) {
            dialogues[0] = "Pepe! I have been expecting you.";
            dialogues[1] = "It is time to learn the power of words and writing.";
            dialogues[2] = "Go to my house and find my quill and notebook.";
            dialogues[3] = "Bring them back to me when you have found them.";
            dialogues[4] = null;

            super.speak();

            if (dialogueIndex == 0) {
                dialogueStage = 1;
                gp.questManager.quest2Stage = QuestManager.GREGORIO_WAITING;
            }

        } else if (dialogueStage == 1) {
            if (gp.questManager.hasWritingSupplies()) {
                dialogues[0] = "Excellent! You found them, Pepe.";
                dialogues[1] = "Now show me what you can do.";
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
            dialogues[0] = "Wow, im very impressed";
            dialogues[1] = "You have managed to create a poem about our language";
            dialogues[2] = "Well done. You should be off to school now!";
            dialogues[3] = null;

            super.speak();

            if (dialogueIndex == 0) {
                dialogueStage = 3;
                gp.questManager.completeQuest2();
            }
        } else if (dialogueStage == 3) {
            dialogues[0] = "Remember the poem, Pepe. It will guide you always.";
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
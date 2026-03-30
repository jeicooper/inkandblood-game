package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Burgos extends Entity{
    public int dialogueStage = 0;

    public NPC_Burgos(GamePanel gp) {
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
        up1    = setup("/npc/burgos/burgos_up_1");
        up2    = setup("/npc/burgos/burgos_up_2");
        down1  = setup("/npc/burgos/burgos_down_1");
        down2  = setup("/npc/burgos/burgos_down_2");
        left1  = setup("/npc/burgos/burgos_left_1");
        left2  = setup("/npc/burgos/burgos_left_2");
        right1 = setup("/npc/burgos/burgos_right_1");
        right2 = setup("/npc/burgos/burgos_right_2");
    }

    public void setDialogue() {}

    @Override
    public void setAction() {}

    @Override
    public void speak(){
        gp.ui.currentSpeakerName = "Manuel Xerxes Burgos";

        boolean ferrandoDone = gp.questManager.ferrandoShooed;

        if (!ferrandoDone && dialogueStage < 2) {
            dialogues[0] = "Ah, good day pepe! Are you looking to enroll?";
            dialogues[1] = "Before I can assist you, you will need to speak with Father Ferrando at\nthe entrance first.";
            dialogues[2] = "He handles all initial assessments. Please see him and then come back\nto me.";
            dialogues[3] = null;

            super.speak();
            if (dialogueIndex == 0) {
                dialogueStage = 1;
            }
        }

        else if (ferrandoDone && dialogueStage < 2) {
            dialogues[0] = "Ah, you must be the young man Father Ferrando turned away at\nthe gate.";
            dialogues[1] = "Do not be discouraged. He is strict with everyone.";
            dialogues[2] = "I have reviewed your record from Calamba. Your marks are... \nsurprisingly impressive.";
            dialogues[4] = "Come. Let us get you enrolled. I will speak on your behalf.";
            dialogues[5] = null;

            super.speak();

            if (dialogueIndex == 0) {
                dialogueStage = 2;
            }
        }

        else {
            dialogues[0] = "Welcome to Ateneo, Jose. Study hard.";
            dialogues[1] = null;
            super.speak();
        }
    }

    @Override
    public void update(){

        if (dialogueStage == 2 && gp.gameState == gp.playState) {
            dialogueStage = 3;
            gp.questManager.onBurgosDialogueDone();
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
package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Ferrando extends Entity{
    public int dialogueStage = 0;

    public NPC_Ferrando(GamePanel gp) {
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
        up1    = setup("/npc/ferrando/ferrando_up_1");
        up2    = setup("/npc/ferrando/ferrando_up_2");
        down1  = setup("/npc/ferrando/ferrando_down_1");
        down2  = setup("/npc/ferrando/ferrando_down_2");
        left1  = setup("/npc/ferrando/ferrando_left_1");
        left2  = setup("/npc/ferrando/ferrando_left_2");
        right1 = setup("/npc/ferrando/ferrando_right_1");
        right2 = setup("/npc/ferrando/ferrando_right_2");
    }

    public void setDialogue() {}

    @Override
    public void setAction() {}

    @Override
    public void speak(){

        for (int i = 0; i < dialogues.length; i++) dialogues[i] = null;
        gp.ui.currentSpeakerName = "Fr. Magin Ferrando";

        if (dialogueStage == 0) {
            dialogues[0] = "Welcome to Ateneo Municipal de Manila! How can i help you?";
            dialogues[1] = "I’m sorry, young man. You are late for registration";
            dialogues[2] = "Frankly, you look far too frail and underweight for an eleven-year-\nold";
            dialogues[3] = "You won't survive the rigor here.";
            dialogues[4] = null;

            super.speak();
            if (dialogueIndex == 0){
                dialogueStage = 1;

                if (gp.questManager.currentQuest == QuestManager.QUEST3) {
                    gp.questManager.onFerrandoShooed();
                }
            }
        }

        else if (dialogueStage == 2){
            dialogues[0] = "I told you — speak to Senor Burgos if you must. Do not bother\n me!";
            dialogues[1] = null;
            super.speak();
        }
    }

    @Override
    public void update(){
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


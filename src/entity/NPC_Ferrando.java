package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Ferrando extends Entity {

    public int dialogueStage = 0;

    public NPC_Ferrando(GamePanel gp) {
        super(gp);
        direction = "down";
        speed     = 0;

        solidArea.x       = 8;
        solidArea.y       = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width   = 32;
        solidArea.height  = 32;

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
    public void speak() {
        gp.ui.currentSpeakerName = "Fr. Magin Ferrando";

        if (dialogueStage == 0) {
            dialogues[0] = "Stop right there!";
            dialogues[1] = "This is Ateneo Municipal de Manila.";
            dialogues[2] = "Frankly, you look far too frail and underweight for an eleven\nyear-old.";
            dialogues[3] = "You won't survive the rigor here.";
            dialogues[4] = null;

            super.speak();

            if (dialogueIndex == 0) {
                dialogueStage = 1;
                gp.questManager.onFerrandoShooed();
            }

        } else {
            dialogues[0] = "I told you — speak to Senor Burgos! Do not waste my time.";
            dialogues[1] = null;
            dialogueIndex = 0;
            super.speak();
        }
    }

    @Override
    public void update() {
        direction = "down";
    }
}
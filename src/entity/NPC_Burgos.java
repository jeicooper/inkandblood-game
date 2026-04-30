package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Burgos extends Entity {

    public int dialogueStage = 0;

    public NPC_Burgos(GamePanel gp) {
        super(gp);
        direction = "down";
        speed     = 0;
        dexId = "burgos";

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
    public void speak() {
        gp.ui.currentSpeakerName = "Manuel Xerxes Burgos";

        if (dialogueStage == 0 && !gp.questManager.ferrandoShooed) {
            dialogues[0] = "Ah, good day! Are you looking to enroll?";
            dialogues[1] = "Before I can assist you, you will need to speak with **Father\nFerrando** at the entrance first.";
            dialogues[2] = "He is **the school's registrar**. Please see him and then come\nback to me.";
            dialogues[3] = null;
            super.speak();

        } else if (dialogueStage == 0 && gp.questManager.ferrandoShooed) {
            dialogues[0] = "Ah, you must be the young man Father Ferrando turned away at\nthe gate.";
            dialogues[1] = "Do not be discouraged. He is strict with everyone.";
            dialogues[2] = "I have reviewed your record from Calamba. Your marks are...\nsurprisingly impressive.";
            dialogues[3] = "I will speak with the rector on your behalf.";
            dialogues[4] = "Come — let us get you enrolled.";
            dialogues[5] = null;

            super.speak();

            if (dialogueIndex == 0) {
                dialogueStage = 1;
                gp.questManager.onBurgosDialogueDone();
            }

        } else {
            dialogues[0] = "Welcome to Ateneo, Jose. Study hard.";
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
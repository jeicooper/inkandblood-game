package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Mariano extends Entity {

    public int dialogueStage = 0;

    public NPC_Mariano(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 0;
        dexId = "mariano";

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
        up1 = setup("/npc/mariano/mariano_up_1");
        up2 = setup("/npc/mariano/mariano_up_2");
        down1 = setup("/npc/mariano/mariano_down_1");
        down2 = setup("/npc/mariano/mariano_down_2");
        left1 = setup("/npc/mariano/mariano_left_1");
        left2 = setup("/npc/mariano/mariano_left_2");
        right1 = setup("/npc/mariano/mariano_right_1");
        right2 = setup("/npc/mariano/mariano_right_2");
    }

    public void setDialogue() {}

    @Override
    public void setAction() {}

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Mariano";

        int q4stage = gp.questManager.quest4Stage;

        // Not yet Mariano's turn
        if (q4stage == QuestManager.TALK_PROFESSOR_Q4) {
            dialogues[0] = "...";
            dialogues[1] = null;
            super.speak();

            // Mariano's turn
        } else if (q4stage == QuestManager.TALK_MARIANO) {
            dialogues[0] = "...Jose.";
            dialogues[1] = "I won't pretend it doesn't sting. I trained hard for that title.";
            dialogues[2] = "But you earned it. Your Latin recitation, your composure — I had\nno answer for it.";
            dialogues[3] = "I concede. You are the better student this month.";
            dialogues[4] = "Do not let it go to your head.";
            dialogues[5] = null;
            super.speak();
            // advance quest when all lines are exhausted (dialogueIndex wraps to 0)
            if (dialogueIndex == 0) {
                gp.questManager.onMarianoDone();
            }

            // After Mariano is done
        } else {
            dialogues[0] = "The better student won. Nothing more to say.";
            dialogues[1] = null;
            super.speak();
        }
    }

    @Override
    public void update() {
        direction = "down";
    }
}
package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Professor extends Entity {

    public int dialogueStage = 0;

    public NPC_Professor(GamePanel gp) {
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
        up1    = setup("/npc/professor/guro_up_1");
        up2    = setup("/npc/professor/guro_up_2");
        down1  = setup("/npc/professor/guro_down_1");
        down2  = setup("/npc/professor/guro_down_2");
        left1  = setup("/npc/professor/guro_left_1");
        left2  = setup("/npc/professor/guro_left_2");
        right1 = setup("/npc/professor/guro_right_1");
        right2 = setup("/npc/professor/guro_right_2");
    }

    public void setDialogue() {}

    @Override
    public void setAction() {}

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Professor";

        if (dialogueStage == 0) {
            dialogues[0] = "Ah — you must be the new student.";
            dialogues[1] = "Welcome to Ateneo. I am your class professor.";
            dialogues[2] = "Before we begin, I want you to meet one of your classmates. She\nwill help you get settled in.";
            dialogues[3] = "Go speak with her — she is just over there.";
            dialogues[4] = null;

            super.speak();

            // Cycle just wrapped — advance quest stage
            if (dialogueIndex == 0) {
                dialogueStage = 1;
                gp.questManager.onProfessorDone();
            }

        } else {
            dialogues[0] = "Go speak with your classmate. She is waiting for you.";
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
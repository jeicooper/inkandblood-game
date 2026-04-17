package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Professor extends Entity {

    public int dialogueStage = 0;

    public NPC_Professor(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 0;
        dexId = "professor";

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
        up1 = setup("/npc/professor/guro_up_1");
        up2 = setup("/npc/professor/guro_up_2");
        down1 = setup("/npc/professor/guro_down_1");
        down2 = setup("/npc/professor/guro_down_2");
        left1 = setup("/npc/professor/guro_left_1");
        left2 = setup("/npc/professor/guro_left_2");
        right1 = setup("/npc/professor/guro_right_1");
        right2 = setup("/npc/professor/guro_right_2");
    }

    public void setDialogue() {}

    @Override
    public void setAction() {}

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Professor";

        if (gp.questManager.quest3Stage < QuestManager.TALK_PROFESSOR
                && !gp.questManager.isQuestActive(QuestManager.QUEST4)) {
            dialogues[0] = "...";
            dialogues[1] = null;
            super.speak();
            return;
        }

        if (dialogueStage == 0) {
            dialogues[0] = "Ah — you must be the new student.";
            dialogues[1] = "Welcome to Ateneo. I am your class professor.";
            dialogues[2] = "Before we begin, I want you to meet one of your classmates. She\nwill help you get settled in.";
            dialogues[3] = "Go speak with her — she is just over there.";
            dialogues[4] = null;

            super.speak();

            if (dialogueIndex == 0) {
                dialogueStage = 1;
                gp.questManager.onProfessorDone();
            }

        } else if (dialogueStage == 1){
            dialogues[0] = "Go speak with your classmate. She is waiting for you.";
            dialogues[1] = null;
            dialogueIndex = 0;
            super.speak();
        } else if (dialogueStage == 2) {
            dialogues[0] = "Jose! I have wonderful news.";
            dialogues[1] = "You have been crowned Emperor of the Romans in this month's\ncompetition.";
            dialogues[2] = "Your performance across all subjects has been nothing short\nof extraordinary.";
            dialogues[3] = "I am proud of you. Now go — speak with Mariano. He has\nsomething to say to you as well.";
            dialogues[4] = null;
            super.speak();

            if (dialogueIndex == 0) {
                dialogueStage = 3;
                gp.questManager.onProfessorQ4Done();
            }

        } else if (dialogueStage == 3) {
            dialogues[0] = "Go speak with Mariano first.";
            dialogues[1] = null;
            super.speak();

        } else if (dialogueStage == 4) {
            dialogues[0] = "Keep up the excellent work, Emperor.";
            dialogues[1] = null;
            super.speak();
        }
    }

    @Override
    public void update() {
        direction = "down";

        if (dialogueStage == 0
                && gp.questManager.isQuestActive(QuestManager.QUEST4)
                && gp.questManager.quest4Stage == QuestManager.TALK_PROFESSOR_Q4) {
            dialogueStage = 2;
        }

        if (gp.questManager.quest4Stage > QuestManager.TALK_PROFESSOR_Q4
                && dialogueStage == 2) {
            dialogueStage = 4;
        }
    }
}
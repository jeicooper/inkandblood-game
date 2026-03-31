package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Student extends Entity {

    public int dialogueStage = 0;

    public NPC_Student(GamePanel gp) {
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
        up1    = setup("/npc/interno/interno_up_1");
        up2    = setup("/npc/interno/interno_up_2");
        down1  = setup("/npc/interno/interno_down_1");
        down2  = setup("/npc/interno/interno_down_2");
        left1  = setup("/npc/interno/interno_left_1");
        left2  = setup("/npc/interno/interno_left_2");
        right1 = setup("/npc/interno/interno_right_1");
        right2 = setup("/npc/interno/interno_right_2");
    }

    public void setDialogue() {}

    @Override
    public void setAction() {}

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Student Interno";

        int stage = gp.questManager.quest3Stage;

        // Player hasn't been sent here yet
        if (stage < QuestManager.TALK_STUDENT) {
            dialogues[0] = "Oh! Hi there. You look new.";
            dialogues[1] = "The professor will introduce you properly. Go talk to him first!";
            dialogues[2] = null;
            dialogueIndex = 0;
            super.speak();
            return;
        }

        if (stage == QuestManager.QUEST3_DONE) {
            dialogues[0] = "Great job on the quiz! We are going to be good classmates\nI can tell.";
            dialogues[1] = null;
            dialogueIndex = 0;
            super.speak();
            return;
        }

        if (dialogueStage == 0) {
            dialogues[0] = "Hey! You must be Jose. I'm your new classmate.";
            dialogues[1] = "Professor asked me to give you a small quiz to see how much\nyou already know.";
            dialogues[2] = "You need to get all 5 questions right. Ready? Let's go!";
            dialogues[3] = null;

            super.speak();

            if (dialogueIndex == 0) {
                dialogueStage = 1;
                gp.ui.openQuizPanel();
            }

        } else if (dialogueStage == 1) {
            dialogues[0] = "Don't give up! Let's try the quiz again.";
            dialogues[1] = "Remember — you need a perfect score of 5 out of 5.";
            dialogues[2] = null;

            super.speak();

            if (dialogueIndex == 0) {
                gp.ui.openQuizPanel();
            }
        }
    }

    @Override
    public void update() {
        direction = "down";

        if (gp.questManager.quest3Stage == QuestManager.QUIZ_FAILED
                && !gp.ui.quizPanelOpen
                && dialogueStage == 1) {
            dialogueStage = 1;
        }
    }
}
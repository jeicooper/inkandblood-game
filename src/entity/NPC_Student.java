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
        for (int i = 0; i < dialogues.length; i++) dialogues[i] = null;
        gp.ui.currentSpeakerName = "Student Intermo";

        int stage = gp.questManager.quest3Stage;

        if (stage == QuestManager.TALK_PROFESSOR || stage < QuestManager.TALK_STUDENT) {
            dialogues[0] = "Oh! Hi there. You look new.";
            dialogues[1] = "The professor will introduce you properly. Go talk to him first!";
            dialogues[2] = null;
            super.speak();
            return;
        }

        if (stage == QuestManager.QUEST3_DONE) {
            dialogues[0] = "Great job on the quiz! We are going to be good classmates, I can tell.";
            dialogues[1] = null;
            super.speak();
            return;
        }

        if (dialogueStage == 0) {
            dialogues[0] = "Hey! You must be Jose. I'm your new classmate.";
            dialogues[1] = "Professor asked me to give you a small quiz to see how much you already\nknow.";
            dialogues[2] = "You need to get all 5 questions right. Ready? Let's go!";
            dialogues[3] = null;
            dialogueStage = 1;
        } else {
            dialogues[0] = "Don't give up! Let's try the quiz again.";
            dialogues[1] = "Remember — you need a perfect score of 5 out of 5.";
            dialogues[2] = null;
            dialogueStage = 1;
        }

        super.speak();
    }

    @Override
    public void update() {

        int stage = gp.questManager.quest3Stage;

        boolean shouldOpenQuiz = (stage == QuestManager.TALK_STUDENT
                || stage == QuestManager.QUIZ_FAILED)
                && dialogueStage == 1
                && gp.gameState == gp.playState;

        if (shouldOpenQuiz) {
            dialogueStage = 2;
            gp.ui.openQuizPanel();
        }

        if (stage == QuestManager.QUIZ_FAILED && dialogueStage == 2
                && !gp.ui.quizPanelOpen) {
            dialogueStage = 0;
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
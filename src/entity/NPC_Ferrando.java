package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Ferrando extends Entity {

    public int dialogueStage = 0;
    private boolean dedicationAnswered = false;

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
        up1 = setup("/npc/ferrando/ferrando_up_1");
        up2 = setup("/npc/ferrando/ferrando_up_2");
        down1 = setup("/npc/ferrando/ferrando_down_1");
        down2 = setup("/npc/ferrando/ferrando_down_2");
        left1 = setup("/npc/ferrando/ferrando_left_1");
        left2 = setup("/npc/ferrando/ferrando_left_2");
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

        } else if (dialogueStage == 1) {
            dialogues[0] = "...";
            dialogues[1] = null;
            super.speak();

        } else if (dialogueStage == 2) {
            dialogues[0] = "So... you passed. I must admit, I did not expect that.";
            dialogues[1] = "Perhaps I was wrong about you, young Rizal.";
            dialogues[2] = "You have earned this. Wear it with pride.";
            dialogues[3] = null;
            super.speak();

            if (dialogueIndex == 0) {
                dialogueStage = 3;
                gp.questManager.completeQuest3();
            }

        } else if (dialogueStage == 3) {
            dialogues[0] = "Study hard, Jose. You have a bright future ahead of you.";
            dialogues[1] = null;
            super.speak();
        } else if (dialogueStage == 4) {
            dialogues[0] = "So — you have come to me last.";
            dialogues[1] = "I am judging you on Dedication. The quality that\nbrought you through these gates against all odds.";
            dialogues[2] = "One question. Answer correctly for the medal.\nThere is no second chance.";
            dialogues[3] = "How many languages did Rizal learn to speak\nin his lifetime?";
            dialogues[4] = null;
            super.speak();

            if (dialogueIndex == 0 && !dedicationAnswered) {
                dedicationAnswered = true;
                dialogueStage = 5;
                gp.ui.quizPanel.openSingleQuestion(
                        "How many languages did Rizal learn to speak in his lifetime?",
                        new String[]{
                                "More than 20 languages.",
                                "Exactly 5 languages.",
                                "Only Spanish and Tagalog."
                        },
                        0,
                        (correct) -> gp.questManager.onDisciplineResult(4, correct)
                );
            }
        } else if (dialogueStage == 5) {
            dialogues[0] = gp.questManager.disciplineMedalEarned[4]
                    ? "Dedication brought you this far. Never lose it."
                    : "You have had your one chance here.";
            dialogues[1] = null;
            super.speak();
        }
    }

    @Override
    public void update() {
        direction = "down";

        int q3stage = gp.questManager.quest3Stage;

        if (dialogueStage == 1 && q3stage == QuestManager.TALK_FERRANDO_REWARD) {
            dialogueStage = 2;
        }

        if (q3stage == QuestManager.QUEST3_DONE && dialogueStage < 3) {
            dialogueStage = 3;
        }

        if (dialogueStage == 3
                && gp.questManager.isQuestActive(QuestManager.QUEST4)
                && gp.questManager.quest4Stage == QuestManager.DISCIPLINE_DEDICATION) {
            dialogueStage = 4;
        }
    }
}
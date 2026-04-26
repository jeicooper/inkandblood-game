package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Ferrando extends Entity {

    public int dialogueStage = 0;
    private boolean dedicationAnswered = false;

    public NPC_Ferrando(GamePanel gp) {
        super(gp);
        direction = "down";
        dexId = "ferrando";

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

        int q3stage = gp.questManager.quest3Stage;
        int q4stage = gp.questManager.quest4Stage;
        boolean q4active = gp.questManager.isQuestActive(QuestManager.QUEST4);

        // QUEST 3 — initial shoo
        if (q3stage == QuestManager.TALK_FERRANDO) {
            dialogues[0] = "Stop right there!";
            dialogues[1] = "This is Ateneo Municipal de Manila.";
            dialogues[2] = "Frankly, you look far too frail and underweight for an\neleven year-old.";
            dialogues[3] = "You won't survive the rigor here.";
            dialogues[4] = null;
            super.speak();
            if (dialogueIndex == 0) {
                gp.questManager.onFerrandoShooed();
            }

            // QUEST 3 — waiting after Burgos, before reward
        } else if (q3stage == QuestManager.TALK_BURGOS
                || q3stage == QuestManager.CUTSCENE_DONE
                || q3stage == QuestManager.TALK_PROFESSOR
                || q3stage == QuestManager.TALK_STUDENT
                || q3stage == QuestManager.QUIZ_FAILED) {
            dialogues[0] = "...";
            dialogues[1] = null;
            super.speak();

            // QUEST 3 — player passed quiz, come back for reward
        } else if (q3stage == QuestManager.TALK_FERRANDO_REWARD) {
            dialogues[0] = "So... you passed. I must admit, I did not expect that.";
            dialogues[1] = "Perhaps I was wrong about you, young Rizal.";
            dialogues[2] = "You have earned this. Wear it with pride.";
            dialogues[3] = null;
            super.speak();
            if (dialogueIndex == 0) {
                gp.questManager.completeQuest3();
            }

            // QUEST 3 done, QUEST 4 not yet started
        } else if (q3stage == QuestManager.QUEST3_DONE && !q4active) {
            dialogues[0] = "Study hard, Jose. You have a bright future ahead of you.";
            dialogues[1] = null;
            super.speak();

            // QUEST 4 — dedication discipline
        } else if (q4active && q4stage == QuestManager.DISCIPLINES_ACTIVE
                && !gp.questManager.disciplineAnswered[4]) {
            dialogues[0] = "I am judging you on Dedication. The quality that\nbrought you through these gates against all odds.";
            dialogues[1] = "One question. Answer correctly for the medal.\nThere is no second chance.";
            dialogues[2] = null;
            super.speak();
            if (dialogueIndex == 0 && !dedicationAnswered) {
                dedicationAnswered = true;
                gp.ui.quizPanel.openSingleQuestion(
                        "How many languages did Jose Rizal learn to speak\nin his lifetime?",
                        new String[]{
                                "More than 20 languages.",
                                "Exactly 5 languages.",
                                "Only Spanish and Tagalog."
                        },
                        0,
                        (correct) -> gp.questManager.onDisciplineResult(4, correct)
                );
            }

            // QUEST 4 — after dedication answered
        } else if (q4active && gp.questManager.disciplineAnswered[4]) {
            dialogues[0] = gp.questManager.disciplineMedalEarned[4]
                    ? "Dedication brought you this far. Never lose it."
                    : "You have had your one chance here.";
            dialogues[1] = null;
            super.speak();

        } else {
            dialogues[0] = "...";
            dialogues[1] = null;
            super.speak();
        }
    }

    @Override
    public void update() {
        direction = "down";

    }
}
package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Casimiro extends Entity {

    public int dialogueStage = 0;
    private boolean paintingAnswered = false;

    public NPC_Casimiro(GamePanel gp) {
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
    }

    public void getImage() {
        up1 = setup("/npc/casimiro/casimiro_up_1");
        up2 = setup("/npc/casimiro/casimiro_up_2");
        down1 = setup("/npc/casimiro/casimiro_down_1");
        down2 = setup("/npc/casimiro/casimiro_down_2");
        left1 = setup("/npc/casimiro/casimiro_left_1");
        left2 = setup("/npc/casimiro/casimiro_left_2");
        right1 = setup("/npc/casimiro/casimiro_right_1");
        right2 = setup("/npc/casimiro/casimiro_right_2");
    }

    @Override
    public void setAction() {}

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Brother Casimiro";

        if (dialogueStage == 0) {
            dialogues[0] = "Come back when Fr. Rector sends you.";
            dialogues[1] = null;
            super.speak();

        } else if (dialogueStage == 1) {
            dialogues[0] = "I am Brother Casimiro, judge of Painting.";
            dialogues[1] = "Art is not merely skill — it is the expression of the soul.";
            dialogues[2] = "Answer my question correctly and the medal is yours. There\nis no retry.";
            dialogues[3] = "What technique did you use to create small figures as a child?";
            dialogues[4] = null;
            super.speak();

            if (dialogueIndex == 0 && !paintingAnswered) {
                paintingAnswered = true;
                dialogueStage = 2;
                gp.ui.quizPanel.openSingleQuestion(
                        "What technique did you use to create small figures as a child?",
                        new String[]{
                                "Watercolor on canvas.",
                                "Carving from beeswax with a knife.",
                                "Sketching with charcoal on stone."
                        },
                        1,
                        (correct) -> gp.questManager.onDisciplineResult(1, correct)
                );
            }

        } else if (dialogueStage == 2) {
            dialogues[0] = gp.questManager.disciplineMedalEarned[1]
                    ? "A fine eye for art. Your medal is well earned."
                    : "You have already had your chance here.";
            dialogues[1] = null;
            super.speak();
        }
    }

    @Override
    public void update() {
        direction = "down";

        if (dialogueStage == 0
                && gp.questManager.isQuestActive(QuestManager.QUEST4)
                && gp.questManager.quest4Stage == QuestManager.DISCIPLINES_ACTIVE) {
            dialogueStage = 1;
        }

    }
}
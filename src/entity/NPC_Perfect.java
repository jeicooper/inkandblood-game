package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Perfect extends Entity {

    public int dialogueStage = 0;
    private boolean conductAnswered = false;

    public NPC_Perfect(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 0;
        dexId = "perfect";

        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        getImage();
    }

    public void getImage() {
        up1 = setup("/npc/perfect/perfect_up_1");
        up2 = setup("/npc/perfect/perfect_up_2");
        down1 = setup("/npc/perfect/perfect_down_1");
        down2 = setup("/npc/perfect/perfect_down_2");
        left1 = setup("/npc/perfect/perfect_left_1");
        left2 = setup("/npc/perfect/perfect_left_2");
        right1 = setup("/npc/perfect/perfect_right_1");
        right2 = setup("/npc/perfect/perfect_right_2");
    }

    @Override
    public void setAction() {}

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Father Prefect";

        if (dialogueStage == 0) {
            dialogues[0] = "Come back when Fr. Rector sends you.";
            dialogues[1] = null;
            super.speak();

        } else if (dialogueStage == 1) {
            dialogues[0] = "I am Father Prefect, judge of Conduct.";
            dialogues[1] = "Conduct is the foundation of all learning here at Ateneo.";
            dialogues[2] = "Answer my question correctly and I will award you the medal.\nYou will not get a second chance.";
            dialogues[3] = "What does proper conduct demand of a student at Ateneo?";
            dialogues[4] = null;
            super.speak();

            if (dialogueIndex == 0 && !conductAnswered) {
                conductAnswered = true;
                dialogueStage = 2;
                gp.ui.quizPanel.openSingleQuestion(
                        "What does proper conduct demand of a student at Ateneo?",
                        new String[]{
                                "To obey rules, respect teachers, and set a good example.",
                                "To win every argument with classmates.",
                                "To study only the subjects you enjoy."
                        },
                        0,
                        (correct) -> gp.questManager.onDisciplineResult(0, correct)
                );
            }

        } else if (dialogueStage == 2) {
            dialogues[0] = gp.questManager.disciplineMedalEarned[0]
                    ? "You earned your Conduct medal. Carry it with honour."
                    : "Your chance with me has passed.";
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
package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Millano extends Entity {

    public int dialogueStage = 0;
    private boolean frenchAnswered = false;

    public NPC_Millano(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 0;
        dexId = "millano";

        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        getImage();
    }

    public void getImage() {
        up1 = setup("/npc/millano/millano_up_1");
        up2 = setup("/npc/millano/millano_up_2");
        down1 = setup("/npc/millano/millano_down_1");
        down2 = setup("/npc/millano/millano_down_2");
        left1 = setup("/npc/millano/millano_left_1");
        left2 = setup("/npc/millano/millano_left_2");
        right1 = setup("/npc/millano/millano_right_1");
        right2 = setup("/npc/millano/millano_right_2");
    }

    @Override
    public void setAction() {}

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Pastor Millano";

        if (dialogueStage == 0) {
            dialogues[0] = "Come back when Fr. Rector sends you.";
            dialogues[1] = null;
            super.speak();

        } else if (dialogueStage == 1) {
            dialogues[0] = "I am Pastor Millano, judge of French Language.";
            dialogues[1] = "Language is the bridge between peoples and nations.";
            dialogues[2] = "One question. Answer correctly for the medal. No second\nattempts.";
            dialogues[3] = "What is the correct French translation of 'I love my country'?";
            dialogues[4] = null;
            super.speak();

            if (dialogueIndex == 0 && !frenchAnswered) {
                frenchAnswered = true;
                dialogueStage = 2;
                gp.ui.quizPanel.openSingleQuestion(
                        "What is the correct French translation of 'I love my country'?",
                        new String[]{
                                "Je deteste mon pays.",
                                "J'aime mon pays.",
                                "Mon pays est grand."
                        },
                        1,
                        (correct) -> gp.questManager.onDisciplineResult(2, correct)
                );
            }

        } else if (dialogueStage == 2) {
            dialogues[0] = gp.questManager.disciplineMedalEarned[2]
                    ? "Tres bien, Jose. Your French medal is deserved."
                    : "Your opportunity here has passed.";
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
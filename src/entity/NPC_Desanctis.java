package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Desanctis extends Entity {

    public int dialogueStage = 0;
    private boolean rhetoricAnswered = false;

    public NPC_Desanctis(GamePanel gp) {
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
        up1 = setup("/npc/desanctis/desanctis_up_1");
        up2 = setup("/npc/desanctis/desanctis_up_2");
        down1 = setup("/npc/desanctis/desanctis_down_1");
        down2 = setup("/npc/desanctis/desanctis_down_2");
        left1 = setup("/npc/desanctis/desanctis_left_1");
        left2 = setup("/npc/desanctis/desanctis_left_2");
        right1 = setup("/npc/desanctis/desanctis_right_1");
        right2 = setup("/npc/desanctis/desanctis_right_2");
    }

    @Override
    public void setAction() {}

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Father de Sanctis";

        if (dialogueStage == 0) {
            dialogues[0] = "Come back when Fr. Rector sends you.";
            dialogues[1] = null;
            super.speak();

        } else if (dialogueStage == 1) {
            dialogues[0] = "I am Father de Sanctis, judge of Rhetoric.";
            dialogues[1] = "Words are the most powerful weapons ever devised by man.";
            dialogues[2] = "One question stands between you and this medal.\nAnswer wrong and it is gone.";
            dialogues[3] = "Which rhetorical device repeats a word at the start\nof successive clauses?";
            dialogues[4] = null;
            super.speak();

            if (dialogueIndex == 0 && !rhetoricAnswered) {
                rhetoricAnswered = true;
                dialogueStage = 2;
                gp.ui.quizPanel.openSingleQuestion(
                        "Which rhetorical device repeats a word at the start of successive clauses?",
                        new String[]{
                                "Metaphor",
                                "Hyperbole",
                                "Anaphora"
                        },
                        2,
                        (correct) -> gp.questManager.onDisciplineResult(3, correct)
                );
            }

        } else if (dialogueStage == 2) {
            dialogues[0] = gp.questManager.disciplineMedalEarned[3]
                    ? "Well argued. Your Rhetoric medal is yours."
                    : "You have had your chance at Rhetoric.";
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
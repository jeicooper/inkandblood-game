package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Rector extends Entity {

    public int dialogueStage = 0;

    public NPC_Rector(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 0;
        dexId = "rector";

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
        up1 = setup("/npc/rector/rector_up_1");
        up2 = setup("/npc/rector/rector_up_2");
        down1 = setup("/npc/rector/rector_down_1");
        down2 = setup("/npc/rector/rector_down_2");
        left1 = setup("/npc/rector/rector_left_1");
        left2 = setup("/npc/rector/rector_left_2");
        right1 = setup("/npc/rector/rector_right_1");
        right2 = setup("/npc/rector/rector_right_2");
    }

    public void setDialogue() {}

    @Override
    public void setAction() {}

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Fr. Rector";

        if (dialogueStage == 0) {
            dialogues[0] = "Ah, young Rizal. Your victory in the monthly competition has\nnot gone unnoticed.";
            dialogues[1] = "But a true scholar is measured across all fields, not just one.";
            dialogues[2] = "I am setting before you a challenge. Seek out the judges of five\ndisciplines within this school.";
            dialogues[3] = "Each will test you in their field: Conduct, Painting, French\nLanguage, Rhetoric, and Dedication.";
            dialogues[4] = "Answer correctly and they will award you a medal. Miss — and that\nchance is gone forever.";
            dialogues[5] = "Prove that your excellence is not a fluke. Begin.";
            dialogues[6] = null;
            super.speak();

            if (dialogueIndex == 0) {

                dialogueStage = 1;
                gp.questManager.onRectorInitiate();
            }

        } else if (dialogueStage == 1) {
            int remaining = QuestManager.DISCIPLINES_ACTIVE
                    - gp.questManager.quest4Stage + 1;
            remaining = Math.max(0, remaining);
            dialogues[0] = remaining > 0
                    ? "You still have " + remaining + " discipline"
                    + (remaining > 1 ? "s" : "") + " remaining. Keep going."
                    : "Return to me once you have seen all five judges.";
            dialogues[1] = null;
            super.speak();

        } else if (dialogueStage == 2) {
            dialogues[0] = "You have returned.";
            dialogues[1] = "I have reviewed the results from all five judges.";
            dialogues[2] = "You earned " + gp.questManager.medalsEarned
                    + " out of " + QuestManager.MEDALS_REQUIRED + " medals.";
            dialogues[3] = gp.questManager.medalsEarned == QuestManager.MEDALS_REQUIRED
                    ? "A perfect score. Extraordinary, Jose. Truly extraordinary."
                    : "Not a perfect score — but you faced each challenge without\nflinching. That itself is admirable.";
            dialogues[4] = "You have proven that your talent runs deep. Ateneo is proud to\ncall you its student.";
            dialogues[5] = null;
            super.speak();

            if (dialogueIndex == 0) {

                dialogueStage = 3;
                gp.questManager.onRectorEnd();
            }

        } else if (dialogueStage == 3) {
            dialogues[0] = "Continue to excel, Rizal. The future is watching.";
            dialogues[1] = null;
            super.speak();
        }
    }

    @Override
    public void update() {
        direction = "down";

        if (dialogueStage == 1 && gp.questManager.quest4Stage == QuestManager.TALK_RECTOR_END) {

            dialogueStage = 2;
        }
    }
}
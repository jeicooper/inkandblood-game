package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Josephine extends Entity {

    public NPC_Josephine(GamePanel gp) {
        super(gp);
        name  = "Josephine Bracken";
        dexId = "josephine";
        direction = "down";
        setHitbox();
        getImage();
    }

    public void getImage() {
        down1  = setup("/npc/bracken/bracken_down_1");
        down2  = setup("/npc/bracken/bracken_down_2");
        up1    = setup("/npc/bracken/bracken_up_1");
        up2    = setup("/npc/bracken/bracken_up_2");
        left1  = setup("/npc/bracken/bracken_left_1");
        left2  = setup("/npc/bracken/bracken_left_2");
        right1 = setup("/npc/bracken/bracken_right_1");
        right2 = setup("/npc/bracken/bracken_right_2");
    }

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Josephine Bracken";

        if (gp.questManager.currentQuest != QuestManager.QUEST7
                || !gp.questManager.isQuestActive(QuestManager.QUEST7)) {
            dialogues[0] = "...";
            dialogues[1] = null;
            dialogueIndex = 0;
            super.speak();
            return;
        }

        int stage = gp.questManager.quest7Stage;

        if (stage == QuestManager.Q7_TALK_JOSEPHINE) {
            dialogues[0] = "Jose, please... look at me one last time.";
            dialogues[1] = "They say the dawn is coming, Joe.";
            dialogues[2] = "But for us, it feels like the sun is setting.";
            dialogues[3] = null;

            super.speak();

            if (dialogueIndex == 0) {
                gp.questManager.onJosephineDone();
            }
        } else {
            dialogues[0] = "Be brave, Jose. The world will remember you.";
            dialogues[1] = null;
            dialogueIndex = 0;
            super.speak();
        }
    }

    @Override
    public void update() {
        direction = "down";
    }
}
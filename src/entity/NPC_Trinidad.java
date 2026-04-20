package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Trinidad extends Entity {

    public NPC_Trinidad(GamePanel gp) {
        super(gp);
        name  = "Trinidad Rizal";
        dexId = "trinidad";
        direction = "down";
        setHitbox();
        getImage();
    }

    public void getImage() {
        down1  = setup("/npc/trinidad/trinidad_down_1");
        down2  = setup("/npc/trinidad/trinidad_down_2");
        up1    = setup("/npc/trinidad/trinidad_up_1");
        up2    = setup("/npc/trinidad/trinidad_up_2");
        left1  = setup("/npc/trinidad/trinidad_left_1");
        left2  = setup("/npc/trinidad/trinidad_left_2");
        right1 = setup("/npc/trinidad/trinidad_right_1");
        right2 = setup("/npc/trinidad/trinidad_right_2");
    }

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Trinidad Rizal";

        if (gp.questManager.currentQuest != QuestManager.QUEST7
                || !gp.questManager.isQuestActive(QuestManager.QUEST7)) {
            dialogues[0] = "Pepe...";
            dialogues[1] = null;
            dialogueIndex = 0;
            super.speak();
            return;
        }

        int stage = gp.questManager.quest7Stage;

        if (stage == QuestManager.Q7_TALK_TRINIDAD) {
            dialogues[0] = "There is something inside the stove…";
            dialogues[1] = "Pepe said… 'Do not let the flame go out.'";
            dialogues[2] = null;

            super.speak();

            if (dialogueIndex == 0) {
                // Give the player the Mi Ultimo Adios as an inventory item
                gp.questManager.giveUltimoAdios();
            }
        } else {
            dialogues[0] = "Keep his memory alive.";
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
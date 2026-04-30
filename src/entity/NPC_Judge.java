package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Judge extends Entity {

    public NPC_Judge(GamePanel gp) {
        super(gp);
        name  = "Judge";
        dexId = "judge";
        direction = "down";
        setHitbox();
        getImage();
    }

    public void getImage() {
        down1  = setup("/npc/judge/judge_down");
        down2  = setup("/npc/judge/judge_down");
        up1    = setup("/npc/judge/judge_up");
        up2    = setup("/npc/judge/judge_up");
        left1  = setup("/npc/judge/judge_left");
        left2  = setup("/npc/judge/judge_left");
        right1 = setup("/npc/judge/judge_right");
        right2 = setup("/npc/judge/judge_right");
    }

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "The Judge";

        if (gp.questManager.currentQuest != QuestManager.QUEST7
                || !gp.questManager.isQuestActive(QuestManager.QUEST7)) {
            dialogues[0] = "...";
            dialogues[1] = null;
            dialogueIndex = 0;
            super.speak();
            return;
        }

        int stage = gp.questManager.quest7Stage;

        if (stage == QuestManager.Q7_TALK_JUDGE) {
            dialogues[0] = "José Protacio Rizal Mercado y Alonso Realonda...";
            dialogues[1] = "By the unanimous agreement of this Council... we find you **GUILTY\nas charged**.";
            dialogues[2] = "The Councils has heard enough. We see no doctor here...but a\nthreat to citizens!";
            dialogues[3] = "The decision of this Council is unanimous.";
            dialogues[4] = "The verdict is to be shot at the back on the **morning of December\n30 at Bagumbayan Field!**";
            dialogues[5] = null;

            super.speak();

            if (dialogueIndex == 0) {
                gp.questManager.onJudgeDone();
            }
        } else {
            dialogues[0] = "We have nothing else to talk about.";
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
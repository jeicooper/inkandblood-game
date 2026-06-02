package entity;
import main.GamePanel;
import main.QuestManager;

import java.awt.*;
import java.util.Random;

public class NPC_Francisco extends Entity {

    public int dialogueStage = 0;

    public NPC_Francisco(GamePanel gp){
        super(gp);

        setHitbox();
        direction = "down";
        dexId = "francisco";

        getImage();
    }

    public void getImage() {
        up1 = setup("/npc/francisco/francisco_up_1");
        up2 = setup("/npc/francisco/francisco_up_2");

        down1 = setup("/npc/francisco/francisco_down_1");
        down2 = setup("/npc/francisco/francisco_down_2");

        left1 = setup("/npc/francisco/francisco_left_1");
        left2 = setup("/npc/francisco/francisco_left_2");

        right1 = setup("/npc/francisco/francisco_right_1");
        right2 = setup("/npc/francisco/francisco_right_2");
    }

    public void speak(){
        gp.ui.currentSpeakerName = "Francisco Engracio Rizal Mercado";

        int qhStage = gp.questManager.questHistoryStage;

        if (!gp.questManager.isQuestActive(QuestManager.QUEST_HISTORY)
                && !gp.questManager.isQuestCompleted(QuestManager.QUEST_HISTORY)) {
            dialogues[0] = "Good morning, Pepe. Have you seen my coffee mug?";
            dialogues[1] = null;
            super.speak();
            return;
        }

        if (gp.questManager.isQuestActive(QuestManager.QUEST_HISTORY)
                && qhStage == QuestManager.QH_TALK_FRANCISCO) {

            if (dialogueStage == 0) {
                dialogues[0] = "Pepe, now that your siblings are settled, it is time\nyou focused on your studies.";
                dialogues[1] = "A man without knowledge of his history is like a tree\nwithout roots.";
                dialogues[2] = "I left three history books scattered around the library\nfor you to find and read.";
                dialogues[3] = "Find them, Pepe. Read them carefully. They will serve\nyou well in the years ahead.";
                dialogues[4] = null;
                super.speak();

                if (dialogueIndex == 0) {
                    dialogueStage = 1;
                    gp.questManager.onFranciscoHistoryTalked();
                }
            } else {
                dialogues[0] = "Have you found all three books yet, Pepe?";
                dialogues[1] = "They are somewhere around our home. Look carefully.";
                dialogues[2] = null;
                dialogueIndex = 0;
                super.speak();
            }

        } else if (gp.questManager.isQuestActive(QuestManager.QUEST_HISTORY)
                && qhStage == QuestManager.QH_COLLECT_BOOKS) {

            int found = gp.questManager.historyBooksFound;
            dialogues[0] = "You have found " + found + " of 3 books so far.";
            dialogues[1] = "Keep looking, Pepe. They are nearby.";
            dialogues[2] = null;
            dialogueIndex = 0;
            super.speak();

        } else if (gp.questManager.isQuestActive(QuestManager.QUEST_HISTORY)
                && qhStage == QuestManager.QH_RETURN_FRANCISCO) {

            dialogues[0] = "You have found all three! Excellent, Pepe.";
            dialogues[1] = "Read them well. History is the lamp that lights our path\nforward.";
            dialogues[2] = "Now keep them with you — they may prove useful in your\njourney ahead.";
            dialogues[3] = null;
            super.speak();

            if (dialogueIndex == 0) {
                gp.questManager.completeQuestHistory();
            }

        } else if (gp.questManager.isQuestCompleted(QuestManager.QUEST_HISTORY)) {
            dialogues[0] = "Study hard, Pepe. The books I gave you hold great wisdom.";
            dialogues[1] = null;
            dialogueIndex = 0;
            super.speak();

        } else {
            // fallback
            dialogues[0] = "Good morning, Pepe.";
            dialogues[1] = null;
            super.speak();
        }
    }

    @Override
    public void setAction(){
    }

    @Override
    public void update() {
        direction = "down";
    }
}
package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Maximo extends Entity {

    public int dialogueStage = 0;

    public NPC_Maximo(GamePanel gp) {
        super(gp);
        name = "Maximo Viola";
        speed = 0;
        direction = "down";
        dexId = "maximo";

        up1 = setup("/npc/maximo/maximo_up_1");
        up2 = setup("/npc/maximo/maximo_up_2");
        down1 = setup("/npc/maximo/maximo_down_1");
        down2 = setup("/npc/maximo/maximo_down_2");
        left1 = setup("/npc/maximo/maximo_left_1");
        left2 = setup("/npc/maximo/maximo_left_2");
        right1 = setup("/npc/maximo/maximo_right_1");
        right2 = setup("/npc/maximo/maximo_right_2");
        setHitbox();
    }

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Maximo Viola";

        if (gp.questManager.currentQuest == QuestManager.QUEST_MEMORIES) {
            int memStage = gp.questManager.questMemStage;

            if (memStage == QuestManager.QM_TALK_MAXIMO_FIRST) {
                dialogues[0] = "Jose, you have been pacing this room for an hour.";
                dialogues[1] = "Your mind is weighed down by the miles you have traveled.";
                dialogues[2] = "Before you sit at that desk to write, gather your mementos.\nRemember what you have seen, the good and the bitter.";
                dialogues[3] = null;
                super.speak();
                if (dialogueIndex == 0) {
                    gp.questManager.onMemMaximoFirstTalked();
                }

            } else if (memStage == QuestManager.QM_COLLECT_5) {
                int done = gp.questManager.memFirstCollected;
                dialogues[0] = "You still need " + (6 - done) + " more mementos. Keep searching.";
                dialogues[1] = null;
                dialogueIndex = 0;
                super.speak();

            } else if (memStage == QuestManager.QM_RETURN_MAXIMO_MID) {
                dialogues[0] = "The Old World has taught you much, Jose.";
                dialogues[1] = "But your travels extended far beyond Europe. Do not forget\nthe lessons of the Far East and the West.";
                dialogues[2] = "Find the last three pieces before you rest.";
                dialogues[3] = null;
                super.speak();
                if (dialogueIndex == 0) {
                    gp.questManager.onMemMaximoMidTalked();
                }

            } else if (memStage == QuestManager.QM_COLLECT_3) {
                int done = gp.questManager.memSecondCollected;
                dialogues[0] = "Still " + (3 - done) + " more to find. Do not rush this, Jose.";
                dialogues[1] = null;
                dialogueIndex = 0;
                super.speak();

            } else if (memStage == QuestManager.QM_RETURN_MAXIMO_FINAL) {
                dialogues[0] = "From the sorrow in Brussels to the peaceful shores of Biarritz...";
                dialogues[1] = "You have seen the beauty of the world, and you have seen its\nrot.";
                dialogues[2] = "The time for traveling is paused. The time for writing is now.";
                dialogues[3] = null;
                super.speak();
                if (dialogueIndex == 0) {
                    gp.questManager.completeQuestMemories();
                }

            } else {
                dialogues[0] = "Your memories will guide your pen, Jose.";
                dialogues[1] = null;
                super.speak();
            }
            return;
        }

        int stage = gp.questManager.quest5Stage;

        if (stage == QuestManager.TALK_MAXIMO) {
            dialogues[0] = "Jose... what you have written here is unlike anything I have ever\nread.";
            dialogues[1] = "This is not just a novel. This is a mirror held up to an empire.";
            dialogues[2] = "I will fund the printing myself. The Philippines must read this.";
            dialogues[3] = "Noli Me Tangere will not stay locked in this room much longer.";
            dialogues[4] = null;
            super.speak();
            if (dialogueIndex == 0) {
                gp.questManager.completeQuest5();
            }

        } else if (stage < QuestManager.TALK_MAXIMO) {
            dialogues[0] = "Jose, I am here whenever you are ready. Take your time.";
            dialogues[1] = null;
            super.speak();

        } else {
            dialogues[0] = "History will remember this room, Jose. And you.";
            dialogues[1] = null;
            super.speak();
        }
    }

    @Override
    public void update() {
        direction = "down";
    }
}
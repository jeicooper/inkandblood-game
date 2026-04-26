package entity;
import main.GamePanel;
import main.QuestManager;

import java.util.Random;

public class NPC_Teodora extends Entity{

    public boolean spoke = false;
    public NPC_Teodora(GamePanel gp){
        super(gp);

        setHitbox();
        direction = "down";
        dexId = "teodora";

        getImage();
        setDialogue();
    }

    public void getImage(){
        up1 = setup("/npc/teodora/teodora_up_1");
        up2 = setup("/npc/teodora/teodora_up_2");

        down1 = setup("/npc/teodora/teodora_down_1");
        down2 = setup("/npc/teodora/teodora_down_2");

        left1 = setup("/npc/teodora/teodora_left_1");
        left2 = setup("/npc/teodora/teodora_left_2");

        right1 = setup("/npc/teodora/teodora_right_1");
        right2 = setup("/npc/teodora/teodora_right_2");

    }

    public void setDialogue(){

    }

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Teodora Alonso Realonda";

        if (!spoke) {
            dialogues[0] = "Pepe! Thank goodness you are here.";
            dialogues[1] = "Your siblings are scattered all around the house.";
            dialogues[2] = "Please find them and bring them all back for dinner.";
            dialogues[3] = "Now go, Pepe!";
            dialogues[4] = null;

            gp.questManager.gameStartTime = System.currentTimeMillis();

            super.speak();

            if (dialogueIndex == 0) {
                spoke = true;
                gp.questManager.quest1Stage = QuestManager.QUEST1_STARTED;
                for (int i = 0; i < gp.npc.length; i++) {
                    if (gp.npc[i] instanceof NPC_Sibling) {
                        gp.npc[i].dialogueIndex = 0;
                    }
                }
            }

        } else if (gp.questManager.quest1Stage == QuestManager.QUEST1_RETURN_TEODORA) {
            dialogues[0] = "Pepe, my son... you have proven your diligence in such a young age.";
            dialogues[1] = "Treasure it and stay humble as you live.";
            dialogues[2] = " Go find your tiyo’s tomorrow.";
            dialogues[3] = null;

            super.speak();

            if (dialogueIndex == 0) {
                gp.questManager.completeQuest1FromTeodora();
            }

        } else if (gp.questManager.quest1Stage == QuestManager.QUEST1_STARTED) {
            int found    = gp.questManager.siblingsFound;
            int required = gp.questManager.SIBLINGS_REQUIRED;
            dialogues[0] = "You haven't found all of them yet, Pepe.";
            dialogues[1] = found + " of " + required + " siblings found.";
            dialogues[2] = null;
            dialogueIndex = 0;
            super.speak();

        } else {
            dialogues[0] = "Have you finished your homework?";
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
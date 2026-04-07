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
        speed = 1;

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

        dialogues[0] = "Good morning pepe. Have you seen your siblings? Go fetch them \nfor me will you? It's time to eat breakfast";
    }

    public void setAction(){


        actionLockCounter++;

        if (actionLockCounter == 180){
            Random random = new Random();
            int i = random.nextInt(100)+1;

            if (i <= 50){
                direction = "up";
            }
            if (i >= 50){
                direction = "down";
            }

            actionLockCounter = 0;
        }
    }

    public void speak(){
        gp.ui.currentSpeakerName = "Teodora Alonso Realonda";

        if (!spoke) {
            dialogues[0] = "Pepe! Thank goodness you are here.";
            dialogues[1] = "Your siblings are scattered all around the town.";
            dialogues[2] = "Please find them and bring them all back home safely.";
            dialogues[3] = "Now go, Pepe!";
            dialogues[4] = null;

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
        } else {
            dialogues[0] = "Please hurry and find your siblings, Pepe.";
            dialogues[1] = null;
            dialogueIndex = 0;
            super.speak();
        }
    }
}
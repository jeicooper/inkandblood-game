package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Manuel extends Entity {

    public int dialogueStage = 0;
    // 0 = first meeting, give boots and instructions
    // 1 = waiting for course completion
    // 2 = course done, congratulate

    public NPC_Manuel(GamePanel gp) {
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
        up1    = setup("/npc/manuel/manuel_up_1");
        up2    = setup("/npc/manuel/manuel_up_2");
        down1  = setup("/npc/manuel/manuel_down_1");
        down2  = setup("/npc/manuel/manuel_down_2");
        left1  = setup("/npc/manuel/manuel_left_1");
        left2  = setup("/npc/manuel/manuel_left_2");
        right1 = setup("/npc/manuel/manuel_right_1");
        right2 = setup("/npc/manuel/manuel_right_2");
    }

    @Override
    public void setAction() {
        // stands still
    }

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Uncle Manuel";
        // only accessible after Jose's part is done
        if (gp.questManager.quest2Stage < QuestManager.JOSE_DONE) {
            dialogues[0] = "Hello Pepe. Go see your\nUncle Jose Alberto first.";
            dialogues[1] = null;
            dialogueIndex = 0;
            super.speak();
            return;
        }

        if (dialogueStage == 0) {
            dialogues[0] = "Pepe! Jose tells me you have learned\nthe basics of colors. Good.";
            dialogues[1] = "Now it is time to train your body.\nA healthy mind needs a healthy body.";
            dialogues[2] = "Take these boots. They will make\nyou run faster than the wind.";
            dialogues[3] = "Run the course around the town.\nI will be watching.";
            dialogues[4] = null;
            dialogueIndex = 0;
            dialogueStage = 1;
            gp.questManager.quest2Stage = QuestManager.MANUEL_RUNNING;
            gp.questManager.giveBoots();

        } else if (dialogueStage == 1) {
            if (gp.questManager.courseCompleted) {
                dialogues[0] = "You did it, Pepe! Incredible speed!";
                dialogues[1] = "Your physical training is complete.\nYou have made me proud.";
                dialogues[2] = "The boots have served their purpose.\nYou will not need them anymore.";
                dialogues[3] = null;
                dialogueIndex = 0;
                dialogueStage = 2;
                gp.questManager.quest2Stage = QuestManager.MANUEL_DONE;
                gp.questManager.removeBoots();
                gp.questManager.completeQuest2();
            } else {
                int hit = gp.questManager.checkpointsHit;
                int total = gp.questManager.TOTAL_CHECKPOINTS;
                dialogues[0] = "You have not finished the course yet!";
                dialogues[1] = "Checkpoints reached: " + hit + "/" + total;
                dialogues[2] = null;
                dialogueIndex = 0;
            }

        } else if (dialogueStage == 2) {
            dialogues[0] = "You have completed your training, Pepe.\nI am proud of you.";
            dialogues[1] = null;
            dialogueIndex = 0;
        }

        super.speak();
    }


    @Override
    public void update() {
        direction = "down";
    }
}
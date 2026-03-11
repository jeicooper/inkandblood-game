package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Manuel extends Entity {

    public int dialogueStage = 0;

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

    public void speak() {
        gp.ui.currentSpeakerName = "Uncle Manuel";

        if (gp.questManager.quest2Stage < QuestManager.JOSE_DONE) {
            dialogues[0] = "Hello Pepe. Go see your Uncle Jose Alberto first.";
            dialogues[1] = null;
            dialogueIndex = 0;
            super.speak();
            return;
        }

        if (dialogueStage == 0) {
            dialogues[0] = "Pepe! Jose tells me you have learned the basics of colors. Good.";
            dialogues[1] = "Now it is time to train your body. A healthy mind needs a healthy body.";
            dialogues[2] = "Take these boots. They will make you run faster than the wind.";
            dialogues[3] = "Run the course around the town. I will be watching.";
            dialogues[4] = null;

            super.speak();

            // only trigger after last line
            if (dialogueIndex == 0) {
                dialogueStage = 1;
                gp.questManager.quest2Stage = QuestManager.MANUEL_RUNNING;
                gp.questManager.giveBoots();
            }

        } else if (dialogueStage == 1) {
            if (gp.questManager.courseCompleted) {
                dialogues[0] = "You did it, Pepe! Incredible speed!";
                dialogues[1] = "Your physical training is complete. You have made me proud.";
                dialogues[2] = "The boots have served their purpose. You will not need them anymore.";
                dialogues[3] = null;

                super.speak();

                if (dialogueIndex == 0) {
                    dialogueStage = 2;
                    gp.questManager.quest2Stage = QuestManager.MANUEL_DONE;
                    gp.questManager.removeBoots();
                    gp.aSetter.activateGregorio();
                }

            } else {
                int hit   = gp.questManager.checkpointsHit;
                int total = gp.questManager.TOTAL_CHECKPOINTS;
                dialogues[0] = "You have not finished the course yet!";
                dialogues[1] = "Checkpoints reached: " + hit + "/" + total;
                dialogues[2] = null;
                dialogueIndex = 0;
                super.speak();
            }

        } else if (dialogueStage == 2) {
            dialogues[0] = "You have completed your training, Pepe. I am proud of you.";
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
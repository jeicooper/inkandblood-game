package entity;
import main.GamePanel;

import java.util.Random;

public class NPC_Teodora extends Entity{

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

            if (i <= 25){
                direction = "up";
            }
            if (i > 25){
                direction = "down";
            }

            if (i > 50 && i <= 75){
                direction = "left";
            }
            if (i > 75 && i <= 100){
                direction = "right";
            }

            actionLockCounter = 0;
        }
    }

    public void speak(){

        super.speak();
    }
}

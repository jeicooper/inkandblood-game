package entity;
import main.GamePanel;

import java.util.Random;

public class NPC_Concha extends Entity{

    public NPC_Concha (GamePanel gp){
        super(gp);

        setHitbox();
        direction = "down";

        getImage();
        setDialogue();
    }

    public void getImage(){
        up1 = setup("/npc/grave");
        up2 = setup("/npc/grave");

        down1 = setup("/npc/grave");
        down2 = setup("/npc/grave");

        left1 = setup("/npc/grave");
        left2 = setup("/npc/grave");

        right1 = setup("/npc/grave");
        right2 = setup("/npc/grave");

    }

    public void setDialogue(){

        dialogues[0] = "This is the grave of Conception Alonso 'Concha' Rizal y Mercado. \nShe Died at only 3 years of age.  ...";
        dialogues[1] = "Born: 1862, Died: 1865. We will miss you forever.";
    }

    public void speak(){

        super.speak();
    }
}
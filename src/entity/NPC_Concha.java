package entity;
import main.GamePanel;

import java.util.Random;

public class NPC_Concha extends Entity{

    public boolean visited = false;

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

        dialogues[0] = "Born: 1862, Died: 1865. We will miss you forever.";
    }

    @Override
    public void speak() {

        gp.ui.currentSpeakerName = "Concepcion 'Concha' Rizal";
        super.speak();
        if (!visited) {
            visited = true;
            gp.questManager.conchaVisited = true;
        }
    }
}
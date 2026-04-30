package entity;
import main.GamePanel;

public class NPC_Concha extends Entity{

    public boolean visited = false;

    public NPC_Concha (GamePanel gp){
        super(gp);

        setHitbox();
        direction = "down";
        dexId = "concha";

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

        dialogues[0] = "Concepcion 'Concha' Rizal. Born: 1862, Died: 1865.";
        dialogues[1] = "Our **eighth sibling** and my dearest sister. Died at the very young age\nof 3";
        dialogues[2] = "The food is ready sis. I miss you everyday";
        dialogues[3] = null;
    }

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Jose 'Pepe' Rizal";
        super.speak();
        if (!visited) {
            visited = true;
            gp.questManager.conchaVisited = true;
        }
    }
}
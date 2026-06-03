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
            dialogues[0] = "Good morning, Pepe. Have you seen my coffee mug?";
            dialogues[1] = null;
            super.speak();
            return;
    }

    @Override
    public void setAction(){
    }

    @Override
    public void update() {
        direction = "down";
    }
}
package entity;
import main.GamePanel;

import java.awt.*;
import java.util.Random;

public class NPC_Francisco extends Entity {
    public boolean spoke = false;

    public NPC_Francisco(GamePanel gp){
        super(gp);

        setHitbox();
        direction = "down";

        getImage();
        setDialogue();
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

    public void setDialogue(){

        dialogues[0] = "Good morning pepe. Have you seen my coffee mug?";
    }

    @Override
    public void setAction(){

    }

    @Override
    public void update() {
    }

    private void facePlayer() {
        int dx = gp.player.worldX - worldX;
        int dy = gp.player.worldY - worldY;
        if (Math.abs(dx) > Math.abs(dy)) {
            direction = (dx > 0) ? "right" : "left";
        } else {
            direction = (dy > 0) ? "down" : "up";
        }
    }
}

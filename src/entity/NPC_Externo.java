package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Externo extends Entity{
    public NPC_Externo (GamePanel gp){
        super(gp);
        direction = "down";
        dexId = null;

        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        getImage();
    }

    public void getImage() {
        up1 = setup("/npc/externo/externo_up_1");
        up2 = setup("/npc/externo/externo_up_2");
        down1 = setup("/npc/externo/externo_down_1");
        down2 = setup("/npc/externo/externo_down_2");
        left1 = setup("/npc/externo/externo_left_1");
        left2 = setup("/npc/externo/externo_left_2");
        right1 = setup("/npc/externo/externo_right_1");
        right2 = setup("/npc/externo/externo_right_2");
    }

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Student (Externo)";
        dialogues[0]="...";
        dialogues[1]=null  ;
        super.speak();
    }

    @Override
    public void update() {
        direction = "down";

    }
}

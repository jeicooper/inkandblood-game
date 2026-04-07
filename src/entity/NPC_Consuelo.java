package entity;

import main.GamePanel;

public class NPC_Consuelo extends Entity {

    public NPC_Consuelo(GamePanel gp) {
        super(gp);
        name = "Consuelo";
        speed = 0;
        direction = "down";

        up1 = setup("/npc/consuelo/consuelo_up_1");
        up2 = setup("/npc/consuelo/consuelo_up_2");
        down1 = setup("/npc/consuelo/consuelo_down_1");
        down2 = setup("/npc/consuelo/consuelo_down_2");
        left1 = setup("/npc/consuelo/consuelo_left_1");
        left2 = setup("/npc/consuelo/consuelo_left_2");
        right1 = setup("/npc/consuelo/consuelo_right_1");
        right2 = setup("/npc/consuelo/consuelo_right_2");

        setHitbox();
        setDialogue();
    }

    public void setDialogue() {
        dialogues[0] = "So you are the famous Jose Rizal. I have heard much\nabout you.";
        dialogues[1] = "They say you write like the angels but think like a\nrevolutionary.";
        dialogues[2] = "I found something left on the table. A paper with your\nname on it. Perhaps it belongs to you?";
    }

    @Override
    public void speak() {
        direction = "down";
        gp.ui.currentSpeakerName = name;
        super.speak();
        if (dialogueIndex == 0) {
            gp.questManager.onConsueloDone();
        }
    }
}
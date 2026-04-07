package entity;

import main.GamePanel;

public class NPC_Pedro extends Entity {

    public NPC_Pedro(GamePanel gp) {
        super(gp);
        name = "Pedro";
        speed = 0;
        direction = "down";

        up1 = setup("/npc/pedro/pedro_up_1");
        up2 = setup("/npc/pedro/pedro_up_2");
        down1 = setup("/npc/pedro/pedro_down_1");
        down2 = setup("/npc/pedro/pedro_down_2");
        left1 = setup("/npc/pedro/pedro_left_1");
        left2 = setup("/npc/pedro/pedro_left_2");
        right1 = setup("/npc/pedro/pedro_right_1");
        right2 = setup("/npc/pedro/pedro_right_2");
        setHitbox();
        setDialogue();
    }

    public void setDialogue() {
        dialogues[0] = "Ah, Jose! Welcome to Berlin.\nYou look like you haven't eaten in days.";
        dialogues[1] = "This is where great minds suffer for even greater ideas.";
        dialogues[2] = "Let me introduce you to someone. This is Consuelo — she has\nbeen asking about you.";
    }

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = name;
        super.speak();
        // after all dialogues exhaust, notify quest
        if (dialogueIndex == 0) {
            gp.questManager.onPedroDone();
        }
    }
}

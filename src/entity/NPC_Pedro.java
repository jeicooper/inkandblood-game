package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Pedro extends Entity {

    public NPC_Pedro(GamePanel gp) {
        super(gp);
        name = "Pedro Paterno";
        direction = "down";
        speed = 0;

        up1 = setup("/npc/pedro/pedro_up_1");
        up2 = setup("/npc/pedro/pedro_up_2");
        down1 = setup("/npc/pedro/pedro_down_1");
        down2 = setup("/npc/pedro/pedro_down_2");
        left1 = setup("/npc/pedro/pedro_left_1");
        left2 = setup("/npc/pedro/pedro_left_2");
        right1 = setup("/npc/pedro/pedro_right_1");
        right2 = setup("/npc/pedro/pedro_right_2");

        setHitbox();
    }

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Pedro Paterno";
        direction = "down";

        int stage = gp.questManager.quest5Stage;

        if (stage == QuestManager.TALK_PEDRO) {
            dialogues[0] = "Ah, Jose! Welcome to Berlin. You look like you haven't eaten in\ndays.";
            dialogues[1] = "This is where great minds suffer for even greater ideas.";
            dialogues[2] = "Let me introduce you to someone. This is Consuelo — she has been\nasking about you.";
            dialogues[3] = null;
            super.speak();
            if (dialogueIndex == 0) {
                gp.questManager.onPedroDone();
            }

        } else {
            dialogues[0] = "Berlin is cold, but the ideas here burn brighter than any fire\nback home.";
            dialogues[1] = null;
            super.speak();
        }
    }


}

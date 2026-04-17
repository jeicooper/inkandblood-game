package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Maximo extends Entity {

    public NPC_Maximo(GamePanel gp) {
        super(gp);
        name = "Maximo Viola";
        speed = 0;
        direction = "down";
        dexId = "maximo";

        up1 = setup("/npc/maximo/maximo_up_1");
        up2 = setup("/npc/maximo/maximo_up_2");
        down1 = setup("/npc/maximo/maximo_down_1");
        down2 = setup("/npc/maximo/maximo_down_2");
        left1 = setup("/npc/maximo/maximo_left_1");
        left2 = setup("/npc/maximo/maximo_left_2");
        right1 = setup("/npc/maximo/maximo_right_1");
        right2 = setup("/npc/maximo/maximo_right_2");
        setHitbox();

    }

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Maximo Viola";

        int stage = gp.questManager.quest5Stage;

        if (stage == QuestManager.TALK_MAXIMO) {
            dialogues[0] = "Jose... what you have written here is unlike anything I have ever\nread.";
            dialogues[1] = "This is not just a novel. This is a mirror held up to an empire.";
            dialogues[2] = "I will fund the printing myself. The Philippines must read this.";
            dialogues[3] = "Noli Me Tangere will not stay locked in this room much longer.";
            dialogues[4] = null;
            super.speak();
            if (dialogueIndex == 0) {
                gp.questManager.completeQuest5();
            }

        } else if (stage < QuestManager.TALK_MAXIMO) {
            dialogues[0] = "Jose, I am here whenever you are ready. Take your time.";
            dialogues[1] = null;
            super.speak();

        } else {
            dialogues[0] = "History will remember this room, Jose. And you.";
            dialogues[1] = null;
            super.speak();
        }
    }

    @Override
    public void update() {
        direction = "down";
    }
}
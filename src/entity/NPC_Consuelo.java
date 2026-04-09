package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Consuelo extends Entity {

    public NPC_Consuelo(GamePanel gp) {
        super(gp);
        name = "Consuelo Ortiga";
        direction = "down";
        speed = 0;

        up1 = setup("/npc/consuelo/consuelo_up_1");
        up2 = setup("/npc/consuelo/consuelo_up_2");
        down1 = setup("/npc/consuelo/consuelo_down_1");
        down2 = setup("/npc/consuelo/consuelo_down_2");
        left1 = setup("/npc/consuelo/consuelo_left_1");
        left2 = setup("/npc/consuelo/consuelo_left_2");
        right1 = setup("/npc/consuelo/consuelo_right_1");
        right2 = setup("/npc/consuelo/consuelo_right_2");

        setHitbox();
    }

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Consuelo Ortiga";
        direction = "down";

        int stage = gp.questManager.quest5Stage;

        if (stage == QuestManager.TALK_CONSUELO) {
            dialogues[0] = "So you are the famous Jose Rizal. I have heard much about you.";
            dialogues[1] = "They say you write like the angels but think like a revolutionary.";
            dialogues[2] = "I found something left on the table. A paper with your name on it. Perhaps\nit belongs to you?";
            dialogues[3] = "Writing stuff like that, you should be careful Jose...";
            dialogues[4] = null;
            super.speak();
            if (dialogueIndex == 0) {
                gp.questManager.onConsueloDone();
            }

        } else if (stage == QuestManager.FIND_LETTER) {
            dialogues[0] = "The paper is somewhere on the table.\nDon't lose it, Jose.";
            dialogues[1] = null;
            super.speak();

        } else {
            dialogues[0] = "Be careful with what you write.\nNot everyone here is a friend.";
            dialogues[1] = null;
            super.speak();
        }
    }

    @Override
    public void update() {
        direction = "down";
    }
}
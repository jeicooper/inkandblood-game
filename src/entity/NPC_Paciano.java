package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Paciano extends Entity {

    public int dialogueStage = 0;

    public NPC_Paciano(GamePanel gp) {
        super(gp);

        name = "Paciano Mercado";
        direction = "down";
        speed = 0;

        up1 = setup("/npc/paciano/paciano_up_1");
        up2 = setup("/npc/paciano/paciano_up_2");
        down1 = setup("/npc/paciano/paciano_down_1");
        down2 = setup("/npc/paciano/paciano_down_2");
        left1 = setup("/npc/paciano/paciano_left_1");
        left2 = setup("/npc/paciano/paciano_left_2");
        right1 = setup("/npc/paciano/paciano_right_1");
        right2 = setup("/npc/paciano/paciano_right_2");

        setHitbox();
    }
    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Paciano Rizal";

        if (dialogueStage == 0) {
            dialogues[0] = "Pepe... I have terrible news.";
            dialogues[1] = "Three priests were executed at Bagumbayan. Gomez. Burgos.\nZamora.";
            dialogues[2] = "Hanged for a revolt they had no part in. The Spanish have shown us\nwhat they think of our people.";
            dialogues[3] = "Noli Me Tangere told the truth about our wounds. But the world\nneeds to feel the fire, not just see it.";
            dialogues[4] = "You started a draft after Noli — a sequel. Find it. Let their deaths\nfuel your pen.";
            dialogues[5] = "Once you have the draft, gather what you need to write the\nmanuscript. Then come back to me.";
            dialogues[6] = null;

            super.speak();

            if (dialogueIndex == 0) {
                dialogueStage = 1;
                gp.questManager.onPacianoQ6Talked();
            }

        } else if (dialogueStage == 1) {
            if (gp.questManager.quest6Stage == QuestManager.RETURN_PACIANO) {
                dialogues[0] = "You have everything. The draft, the memories, the fire.";
                dialogues[1] = "Let the death of GomBurza be the prologue. Let every Filipino\nwho suffered be a character.";
                dialogues[2] = "Finish what you started, Pepe. El Filibusterismo must see the\nlight.";
                dialogues[3] = null;

                super.speak();

                if (dialogueIndex == 0) {
                    dialogueStage = 2;
                    gp.questManager.onPacianoItemsReturned();
                }
            } else {
                int collected = gp.questManager.q6ObjectsCollected;
                int required  = QuestManager.Q6_OBJECTS_REQUIRED;
                dialogues[0] = "You still need more, Pepe.";
                dialogues[1] = "Manuscript parts: " + collected + "/" + required;
                dialogues[2] = null;
                dialogueIndex = 0;
                super.speak();
            }

        } else if (dialogueStage == 2) {
            dialogues[0] = "Write well, Pepe. The nation is listening.";
            dialogues[1] = null;
            dialogueIndex = 0;
            super.speak();
        }
    }

    @Override
    public void update() {
        direction = "down";
    }
}
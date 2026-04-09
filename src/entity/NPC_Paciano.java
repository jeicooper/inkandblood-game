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
        gp.ui.currentSpeakerName = "Paciano Rizal" +
                "";

        if (dialogueStage == 0) {
            dialogues[0] = "Pepe... I have terrible news.";
            dialogues[1] = "Three priests were executed at Bagumbayan.\nGomez. Burgos. Zamora.";
            dialogues[2] = "Hanged for a revolt they had no part in.\nThe Spanish have shown us what they think of our people.";
            dialogues[3] = "Noli Me Tangere told the truth about our wounds.\nBut the world needs to feel the fire, not just see it.";
            dialogues[4] = "You started a draft after Noli — a sequel.\nFind it. Let their deaths fuel your pen.";
            dialogues[5] = "Bring me the El Fili Draft, a letter about GomBurza,\nand an ink bottle. Then we will talk.";
            dialogues[6] = null;

            super.speak();

            if (dialogueIndex == 0) {
                dialogueStage = 1;
                gp.questManager.onPacianoQ6Talked();
            }

        } else if (dialogueStage == 1) {
            if (gp.questManager.hasAllInspirationItems()) {
                dialogues[0] = "You have it all — the draft, the letter, and the ink.";
                dialogues[1] = "Let the death of GomBurza be the prologue.\nLet every Filipino who suffered be a character.";
                dialogues[2] = "Finish what you started, Pepe.\nEl Filibusterismo must see the light.";
                dialogues[3] = null;

                super.speak();

                if (dialogueIndex == 0) {
                    dialogueStage = 2;
                    gp.questManager.onPacianoItemsReturned();
                }
            } else {
                int draft  = gp.questManager.countItem("El Fili Draft");
                int letter = gp.questManager.countItem("GomBurza Letter");
                int ink    = gp.questManager.countItem("Ink Bottle");
                dialogues[0] = "You still need more, Pepe.";
                dialogues[1] = "El Fili Draft: " + draft + "/1\nGomBurza Letter: " + letter + "/1\nInk Bottle: " + ink + "/1";
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


}

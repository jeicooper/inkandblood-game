package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_GuardiaCivil extends Entity {

    public NPC_GuardiaCivil(GamePanel gp) {
        super(gp);
        name  = "Guardia Civil";
        dexId = "guardiacivil";
        direction = "down";
        setHitbox();
        getImage();
    }

    public void getImage() {
        down1  = setup("/npc/gwardya/gwardya_down_1");
        down2  = setup("/npc/gwardya/gwardya_down_2");
        up1    = setup("/npc/gwardya/gwardya_up_1");
        up2    = setup("/npc/gwardya/gwardya_up_2");
        left1  = setup("/npc/gwardya/ggwardya_left_1");
        left2  = setup("/npc/gwardya/gwardya_left_2");
        right1 = setup("/npc/gwardya/gwardya_right_1");
        right2 = setup("/npc/gwardya/gwardya_right_2");
    }

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Guardia Civil";

        if (gp.questManager.currentQuest != QuestManager.QUEST7
                || !gp.questManager.isQuestActive(QuestManager.QUEST7)) {
            dialogues[0] = "Move along, citizen.";
            dialogues[1] = null;
            dialogueIndex = 0;
            super.speak();
            return;
        }

        int stage = gp.questManager.quest7Stage;

        if (stage == QuestManager.Q7_TALK_GUARDIA) {
            dialogues[0] = "We have letters, Doctor.";
            dialogues[1] = "Letters from your colleagues found in the caves of the rebels.";
            dialogues[2] = "They use your name as their war cry. Your photo hangs in their halls.";
            dialogues[3] = "How do you explain this?";
            dialogues[4] = "Not talking? But your **'reforms'** are merely a mask for revolution.";
            dialogues[5] = "Your words were the spark, Doctor... you cannot now blame the fire\nfor burning.";
            dialogues[6] = null;

            super.speak();

            if (dialogueIndex == 0) {
                gp.questManager.onGuardiaDone();
            }
        } else {
            dialogues[0] = "The Council has spoken. There is nothing more to say.";
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
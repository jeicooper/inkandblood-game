package entity;

import main.GamePanel;
import main.QuestManager;

public class NPC_Marcelo extends Entity {

    public int dialogueStage = 0;

    public NPC_Marcelo(GamePanel gp) {
        super(gp);
        name      = "Marcelo H. del Pilar";
        direction = "down";
        speed     = 0;
        dexId     = "marcelo";

        solidArea.x       = 8;
        solidArea.y       = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width   = 32;
        solidArea.height  = 32;

        getImage();
    }

    public void getImage() {
        up1    = setup("/npc/marcelo/marcelo_up");
        up2    = setup("/npc/marcelo/marcelo_up");
        down1  = setup("/npc/marcelo/marcelo_down");
        down2  = setup("/npc/marcelo/marcelo_down");
        left1  = setup("/npc/marcelo/marcelo_left");
        left2  = setup("/npc/marcelo/marcelo_left");
        right1 = setup("/npc/marcelo/marcelo_right");
        right2 = setup("/npc/marcelo/marcelo_right");
    }

    @Override
    public void setAction() {}

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = "Marcelo H. del Pilar";

        if (!gp.questManager.isQuestActive(QuestManager.QUEST8)
                && !gp.questManager.isQuestCompleted(QuestManager.QUEST8)) {
            dialogues[0] = "The pen is mightier than the sword, Jose.";
            dialogues[1] = null;
            super.speak();
            return;
        }

        int stage = gp.questManager.quest8Stage;

        if (stage == QuestManager.Q8_TALK_MARCELO) {
            dialogues[0] = "Jose, the reform movement needs the might of your pen to\nawaken our countrymen's minds.";
            dialogues[1] = "Review your drafts on the desk before we send them to the\npress and the mail.";
            dialogues[2] = null;
            super.speak();
            if (dialogueIndex == 0) {
                dialogueStage = 1;
                gp.questManager.onMarceloInitialTalk();
            }

        } else if (stage == QuestManager.Q8_RETURN_MALOLOS) {
            dialogues[0] = "Rizal, you wrote this Tagalog letter at my request to honor\nthe twenty brave young women of Malolos.";
            dialogues[1] = "You perfectly captured how they courageously sustained their\nagitation to open a night school, triumphing despite Father\nFelipe Garcia's initial objections.";
            dialogues[2] = "I get you...you want to afford our women the same educational\nopportunities enjoyed by men. You are urging them to liberate\nthemselves, reject the spiritual authority of the friars, and\nprotect their dignity.";
            dialogues[3] = "It is a bold and necessary move to advise young men to choose\nlifetime partners based on firmness of character";
            dialogues[4] = "Rather than physical beauty, and to counsel our young women\nto seek noble men who are incapable of being slaves.";
            dialogues[5] = null;
            super.speak();
            if (dialogueIndex == 0) {
                gp.questManager.onMarceloMalolosDone();
            }

        } else if (stage == QuestManager.Q8_FIND_CENTURY) {
            dialogues[0] = "Find your draft of 'The Philippines a Century Hence' from the desk.";
            dialogues[1] = null;
            super.speak();

        } else if (stage == QuestManager.Q8_RETURN_CENTURY) {
            dialogues[0] = "This is a brilliant piece you've given us for La Solidaridad.";
            dialogues[1] = "You wrote this to forecast the future of our country a hundred\nyears from now, dissecting the miseries caused by Spain's\nmilitary policies, the deterioration of our indigenous culture,\nand our own passivity.";
            dialogues[2] = "You firmly stressed that if the Philippines is to peacefully\nremain under Spanish rule, the government must initiate reforms.";
            dialogues[3] = "Freedom of the press, representation in the Spanish Cortes,\nand the granting of Spanish citizenship...you made these\ndemands undeniable.";
            dialogues[4] = null;
            super.speak();
            if (dialogueIndex == 0) {
                gp.questManager.onMarceloCenturyDone();
            }

        } else if (stage == QuestManager.Q8_FIND_INDOLENCE) {
            dialogues[0] = "Now retrieve your draft of 'The Indolence of the Filipinos'.";
            dialogues[1] = null;
            super.speak();

        } else if (stage == QuestManager.Q8_RETURN_INDOLENCE) {
            dialogues[0] = "This essay defends Filipinos against accusations of laziness,\narguing it isn't inherent but caused by the tropical climate\nand a lack of moral or material incentives.";
            dialogues[1] = "You correctly noted that Europeans surrounded by servants\nare the truly lazy ones here, while our own industry was\nruined by forced labor, government neglect, and friar-owned\nestates.";
            dialogues[2] = "Furthermore, a lack of national sentiment and an inferiority\ncomplex have paralyzed our progress, leaving us with little\nresistance against harmful colonial policies.";
            dialogues[3] = null;
            super.speak();
            if (dialogueIndex == 0) {
                gp.questManager.onMarceloIndolenceDone();
            }

        } else if (stage == QuestManager.Q8_FINAL_TALK) {
            dialogues[0] = "These words will shatter the patriarchal illusions, expose\nthe colonizers' faults, and give our people a vision for\ntomorrow.";
            dialogues[1] = "Let us hope our countrymen have the courage to read them,\nand the wisdom to act upon them.";
            dialogues[2] = null;
            super.speak();
            if (dialogueIndex == 0) {
                gp.questManager.completeQuest8();
            }

        } else {
            dialogues[0] = "The truth is in print now, Jose. Let history judge.";
            dialogues[1] = null;
            super.speak();
        }
    }

    @Override
    public void update() {
        direction = "down";
    }
}
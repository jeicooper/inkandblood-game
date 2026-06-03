package entity;

import main.GamePanel;
import main.KeyHandler;
import main.QuestManager;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends  Entity{

    KeyHandler keyP;

    public final int screenX;
    public final int screenY;
    private int lastTileX = -1;
    private int lastTileY = -1;

    private static final String[] LOCATION_MAPS = {
            "/maps/Chapter1.txt",
            "/maps/Chapter2.txt",
            "/maps/Chapter3.txt",
            "/maps/Chapter4.txt",
    };

    private static final int[][] LOCATION_TILES = {
            //chapter 1
            { 63, 18, 0 },
            { 63, 19, 0 },
            { 72, 29, 0 },
            { 73, 29, 0 },
            { 74, 29, 0 },

            { 54, 21, 0 },
            { 54, 22, 0 },
            { 55, 22, 0 },
            { 56, 22, 0 },
            { 56, 21, 0 },

            //chapter 2
            { 49, 44, 1 },
            { 49, 45, 1 },

            //chapter 3
            { 22, 50, 2 },
            { 23, 50, 2 },
            { 24, 50, 2 },
            { 53, 74, 2 },
            { 53, 75, 2 },

            //chapter 4
            { 50, 32, 3 },
            { 51, 32, 3 },
            { 52, 32, 3 },
    };
    private static final String[] LOCATION_TILE_NAMES = {
            "The Rizal's Family Home",
            "The Rizal's Family Home",
            "The Rizal's Family Home",
            "The Rizal's Family Home",
            "The Rizal's Family Home",

            "Our Lady of Peace and Good Voyage",
            "Our Lady of Peace and Good Voyage",
            "Our Lady of Peace and Good Voyage",
            "Our Lady of Peace and Good Voyage",
            "Our Lady of Peace and Good Voyage",

            "Ateneo Municipal De Manila",
            "Ateneo Municipal De Manila",

            "Banquet Hall",
            "Banquet Hall",
            "Banquet Hall",
            "Jose's Dormitory",
            "Jose's Dormitory",

            "Fort Santiago",
            "Fort Santiago",
            "Fort Santiago",
    };

    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;

    public Player(GamePanel gp, KeyHandler keyP){

        super(gp);
        this.keyP = keyP;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 16;
        solidArea.y = 32;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        solidArea.width = 24;
        solidArea.height = 24;

        setDefaultValues();
        getPlayerImage();

        setItems();
    }

    public void setDefaultValues(){

        //player position in the map
        worldX = gp.tileSize * 64;
        worldY = gp.tileSize * 18;
        speed = 6;
        direction = "down";

        // PLAYER STATS
        maxExp = 20; // 1 = halfexp
        exp = 0;

        intellect = 0;
        creativity = 0;
        perception = 0;
        charisma = 0;
        age = 8;

    }

    public void setItems (){

    }

    public void getPlayerImage(){

        up1 = setup("/player/boy_up_1");
        up2 = setup("/player/boy_up_2");
        down1 = setup("/player/boy_down_1");
        down2 = setup("/player/boy_down_2");
        left1 = setup("/player/boy_left_1");
        left2 = setup("/player/boy_left_2");
        right1 = setup("/player/boy_right_1");
        right2 = setup("/player/boy_right_2");
        down3 = setup("/player/pepe_down_1");

    }

    public void loadSprite2(String folder){
        up1    = setup("/player/ateneo_up_1");
        up2    = setup("/player/ateneo_up_2");
        down1  = setup("/player/ateneo_down_1");
        down2  = setup("/player/ateneo_down_2");
        left1  = setup("/player/ateneo_left_1");
        left2  = setup("/player/ateneo_left_2");
        right1 = setup("/player/ateneo_right_1");
        right2 = setup("/player/ateneo_right_2");
    }

    public void loadSprite3(String folder){
        up1    = setup("/player/rizal_adult_up_1");
        up2    = setup("/player/rizal_adult_up_2");
        down1  = setup("/player/rizal_adult_down_1");
        down2  = setup("/player/rizal_adult_down_2");
        left1  = setup("/player/rizal_adult_left_1");
        left2  = setup("/player/rizal_adult_left_2");
        right1 = setup("/player/rizal_adult_right_1");
        right2 = setup("/player/rizal_adult_right_2");
    }

    public BufferedImage setup(String imageName){

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {

            image = ImageIO.read(getClass().getResourceAsStream(imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        }catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }

    public void update(){

        if (keyP.upPressed || keyP.downPressed || keyP.leftPressed || keyP.rightPressed){

            if (keyP.upPressed)         direction = "up";
            else if (keyP.downPressed)  direction = "down";
            else if (keyP.leftPressed)  direction = "left";
            else if (keyP.rightPressed) direction = "right";

            // CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // CHECK OBJ COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // CHECK NPC COLLISION
            gp.cChecker.checkEntity(this, gp.npc);

            if (!collisionOn){
                switch (direction){
                    case "up":    worldY -= speed; break;
                    case "down":  worldY += speed; break;
                    case "left":  worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            spriteCounter++;
            if (spriteCounter > 12){
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }

        int tileX = worldX / gp.tileSize;
        int tileY = worldY / gp.tileSize;
        if (tileX != lastTileX || tileY != lastTileY) {
            System.out.println("Tile X: " + tileX + " | Tile Y: " + tileY);
            lastTileX = tileX;
            lastTileY = tileY;

            for (int i = 0; i < LOCATION_TILES.length; i++) {
                int mapIndex = LOCATION_TILES[i][2];
                if (tileX == LOCATION_TILES[i][0]
                        && tileY == LOCATION_TILES[i][1]
                        && gp.currentMap.equals(LOCATION_MAPS[mapIndex])) {
                    gp.ui.showLocationPopup(LOCATION_TILE_NAMES[i]);
                    break;
                }
            }
        }

        // F INTERACTION
        if (keyP.fPressed) {
            keyP.fPressed = false;

            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // Try all 4 directions for object interaction
            String originalDirection = direction;
            int fObjIndex = 999;

            String[] dirs = {"up", "down", "left", "right"};
            for (String dir : dirs) {
                direction = dir;
                fObjIndex = gp.cChecker.checkObject(this, true);
                if (fObjIndex != 999) break;
            }
            direction = originalDirection;

            interactObject(fObjIndex);
        }
    }

    public void interactNPC(int i){
        if (i != 999){
            gp.talkingTo = gp.npc[i];
            gp.gameState = gp.dialogueState;
            gp.npc[i].speak();
        }
    }


    public void pickUpObject(int i){

        if (i != 999) {

            String objectName = gp.obj[i].name;

            if (objectName.equals("Final Thoughts"))   return;
            if (objectName.equals("Alcohol Stove"))    return;
            if (objectName.equals("Wooden Keepsake Box")) return;
            if (getKeepsakeIndex(objectName) >= 0) return;
            if (getManuscriptIndex(objectName) >= 0) return;
            if (getManuscriptIndexQ6(objectName) >= 0) return;
            if (getMemFirstIndex(objectName) >= 0) return;
            if (getMemSecondIndex(objectName) >= 0) return;
            if (objectName.equals("Draft of Noli Me Tangere")) return;
            if (objectName.equals("Draft of El Filibusterismo")) return;
            if (gp.obj[i] instanceof object.OBJ_HistoryBook) return;

            if (inventory.size() != maxInventorySize) {

                objectName = gp.obj[i].name;
                inventory.add(gp.obj[i]);
                gp.ui.showPickup(objectName);
                gp.obj[i] = null;
                gp.playSE(1);
            } else {
                gp.ui.showMessage("Your inventory is full!");
            }
        }
    }

    public void interactObject(int i) {
        if (i == 999) return;
        String objectName = gp.obj[i].name;

        // HISTORY BOOKS (Quest History)
        if (gp.obj[i] instanceof object.OBJ_HistoryBook) {
            object.OBJ_HistoryBook hb = (object.OBJ_HistoryBook) gp.obj[i];
            if (!gp.questManager.isQuestActive(QuestManager.QUEST_HISTORY)
                    || gp.questManager.questHistoryStage < QuestManager.QH_COLLECT_BOOKS) {
                gp.ui.showMessage("Tatay hasn't asked me to read yet.");
                return;
            }

            gp.playSE(1);

            gp.questManager.onHistoryBookPickedUp(hb.bookIndex);
            gp.gameState = gp.playState;
            gp.ui.activeLetter = objectName;
            gp.ui.showPoemPanel = true;
            return;
        }

        // NOLI DRAFT
        if (objectName.equals("Draft of Noli Me Tangere")) {
            if (gp.questManager.quest5Stage != QuestManager.FIND_LETTER) {
                gp.ui.showMessage("I'm not ready for this yet.");
                return;
            }
            if (inventory.size() < maxInventorySize) {
                inventory.add(gp.obj[i]);
                gp.obj[i] = null;
                gp.playSE(1);
                gp.ui.showMessage("You found the draft!");
                gp.questManager.onNoliDraftFound();
            } else {
                gp.ui.showMessage("Your inventory is full!");
            }
        }

        // EL FILI DRAFT
        if (objectName.equals("Draft of El Filibusterismo")) {
            if (gp.questManager.quest6Stage != QuestManager.FIND_DRAFT) {
                gp.ui.showMessage("I'm not ready for this yet.");
                return;
            }
            if (inventory.size() < maxInventorySize) {
                inventory.add(gp.obj[i]);
                gp.obj[i] = null;
                gp.playSE(1);
                gp.ui.showMessage("You found the draft!");
                gp.questManager.onElFiliDraftFound();
            } else {
                gp.ui.showMessage("Your inventory is full!");
            }
        }

        if (objectName.equals("Final Thoughts")) {
            if (gp.questManager.currentQuest != QuestManager.QUEST7) return;
            if (gp.questManager.quest7Stage != QuestManager.Q7_INTERACT_PAPER) {
                gp.ui.showMessage("I'm not ready for this yet.");
                return;
            }
            gp.ui.currentSpeakerName = "Jose Rizal";
            gp.ui.currentDialogue =
                    "The ink is dry. My 'Ultimo Adios' is finished.It is not just a poem;\nit is a map for those who will follow.";
            gp.gameState = gp.dialogueState;
            gp.obj[i] = null;
            gp.questManager.onFinalPaperInteracted();
            return;
        }

        if (objectName.equals("Alcohol Stove")) {
            if (gp.questManager.currentQuest != QuestManager.QUEST7) return;
            if (gp.questManager.quest7Stage != QuestManager.Q7_INTERACT_STOVE) {
                gp.ui.showMessage("I'm not ready for this yet.");
                return;
            }
            gp.ui.currentSpeakerName = "Jose Rizal";
            gp.ui.currentDialogue =
                    "Inside the stove, folded tightly, a small roll of paper. I press it into\nTrinidad's hand.";
            gp.gameState = gp.dialogueState;
            gp.obj[i] = null;
            gp.questManager.onAlcoholStoveInteracted();
            return;
        }

        // QUEST_KEEPSAKES — Step 1: Open the box
        if (objectName.equals("Wooden Keepsake Box") &&
                gp.questManager.currentQuest == QuestManager.QUEST_KEEPSAKES) {

            if (gp.questManager.questKsStage == QuestManager.QK_INTERACT_BOX) {
                gp.ui.currentSpeakerName = "Jose Rizal";
                gp.ui.currentDialogue =
                        "Before I face the heavy burdens of my country, I must briefly\n" +
                                "revisit the faces of those who offered me fleeting solace.\n" +
                                "Each item here holds a memory, a laugh, or a tear that I can\n" +
                                "never fully leave behind.";
                gp.gameState = gp.dialogueState;
                gp.obj[i] = null;
                gp.questManager.onKeepsakeBoxOpened();

            } else if (gp.questManager.questKsStage == QuestManager.QK_RETURN_BOX) {
                // Step 3: Return to box — closing dialogue
                object.OBJ_KeepsakeBox box = (object.OBJ_KeepsakeBox) gp.obj[i];
                String[] closingLines = {
                        "But the greatest romance of my life... the one that demands\n" +
                                "my ultimate devotion... is my Motherland.",

                        "It is time to lock the box.\n" +
                                "The chapter of my youth is closed.",

                        "Now, I must return to the manuscript.\n" +
                                "Paciano is right... El Filibusterismo must be finished."
                };
                if (box.lineIndex < closingLines.length) {
                    gp.ui.currentDialogue = closingLines[box.lineIndex];
                    gp.ui.currentSpeakerName = "Jose Rizal";
                    gp.gameState = gp.dialogueState;
                    box.lineIndex++;
                    if (box.lineIndex >= closingLines.length) {
                        gp.obj[i] = null;
                        gp.questManager.completeQuestKeepsakes();
                    }
                }
            }
            return;
        }

        int ksIndex = getKeepsakeIndex(objectName);
        if (ksIndex >= 0 &&
                gp.questManager.currentQuest == QuestManager.QUEST_KEEPSAKES &&
                gp.questManager.questKsStage == QuestManager.QK_COLLECT) {
            String[] messages = {
                    "Segunda Katigbak was my puppy love, but our relationship was hopeless\n" +
                            "right from the very start because she was already set to marry a\n" +
                            "fellow-townsman in Batangas, Manuel Luz.",

                    "I met my second object of affection, Leonor 'Orang' Valenzuela, who was\n" +
                            "literally the girl-next-door, when I was a sophomore medical student\n" +
                            "at the University of Santo Tomas.",

                    "Leonor Rivera was my sweetheart for 11 years, though sadly her mother\n" +
                            "disapproved of her daughter's relationship with me.",

                    "I probably fell in love with Consuelo Ortiga when she asked me for\n" +
                            "romantic verses, but I suddenly backed out before it turned into a\n" +
                            "serious romance because I wanted to remain loyal to Leonor Rivera and\n" +
                            "did not want to destroy my friendship with Eduardo de Lete,\n" +
                            "who was madly in love with her.",

                    "O Sei-San, a Japanese samurai's daughter, helped improve my knowledge\n" +
                            "of the Japanese language and taught me the Japanese art of painting\n" +
                            "known as su-mie.",

                    "Gertrude Beckett, a blue-eyed and buxom girl who was the oldest of\n" +
                            "the three Beckett daughters, fell deeply in love with me.",

                    "Antonio Luna, Juan's brother and a frequent visitor of the Bousteads,\n" +
                            "courted Nellie Bousted, but she was deeply infatuated with me instead.",

                    "Suzanne Jacoby and I fell deeply in love with each other, and cried\n" +
                            "when I left Brussels, later writing to me when I was in Madrid.",

                    "In the last days of February 1895 while still in Dapitan, I met\n" +
                            "Josephine Bracken, an 18-year-old petite Irish girl with bold blue eyes,\n" +
                            "brown hair, and a happy disposition, who stayed with my family in Manila\n" +
                            "until she returned to Dapitan where I tried to arrange a marriage\n" +
                            "with Father Antonio Obach."
            };
            gp.ui.currentDialogue = messages[ksIndex];
            gp.ui.currentSpeakerName = "Jose Rizal";
            gp.gameState = gp.dialogueState;
            gp.obj[i] = null;
            gp.questManager.onKeepsakeFound(ksIndex);
            return;
        }

        int memFirstIndex = getMemFirstIndex(objectName);
        if (memFirstIndex >= 0 &&
                gp.questManager.currentQuest == QuestManager.QUEST_MEMORIES &&
                gp.questManager.questMemStage == QuestManager.QM_COLLECT_5) {
            String[] messages = {
                    //Medical Books
                    "During my time in Spain from 1882 to 1885, I observed life and culture,\n" +
                    "joined the Circulo Hispano Filipino, and enrolled at the Universidad\n" +
                    "Central de Madrid to study medicine and Philosophy and Letters.",

                    //Letters and Postcards
                    "While on the Grand Tour of Europe in 1887, I toured around different\n" +
                    "places and  sceneries, but the most profound moment was finally\n " +
                    "meeting my true brother of the soul, Ferdinand Blumentritt.",

                    //Dusty Manuscript
                    "In London from 1888 to 1889, I worked to improve in the English\n" +
                    "Language and labored to annotate Morga's work, the Sucesos de las\n" +
                    "Islas Filipinas, to prove the richness of our history.",

                    //Legal Docs
                    "Starting in Brussels in 1890 where I received bad news about my\nfamily, I" +
                    "rushed to Madrid from 1890 to 1891 to seek legal means\n" +
                    " for them and the Calamba Tenants.",

                    //Envelope
                    "It was also during my stay in Madrid from 1890 to 1891 that I suffered\n" +
                    "the heartbreaking blow of discovering that my Leonor Rivera had\n" +
                    "gotten married to a British Engineer.",

                    //Ophthalmoscope
                    "From 1885 to 1887, traveling from Paris to Berlin, I worked as an\n" +
                    "assistant to Dr. Louis de Wrecker to improve my knowledge in\n" +
                            "ophthalmology so I could cure my mother's failing eyesight."
            };
            gp.ui.currentDialogue = messages[memFirstIndex];
            gp.ui.currentSpeakerName = "Jose Rizal";
            gp.gameState = gp.dialogueState;
            gp.obj[i] = null;
            gp.questManager.onMemFirstPartCollected(memFirstIndex);
            return;
        }

        // QUEST_MEMORIES — Batch 2 (3 mementos)
        int memSecondIndex = getMemSecondIndex(objectName);
        if (memSecondIndex >= 0 &&
                gp.questManager.currentQuest == QuestManager.QUEST_MEMORIES &&
                gp.questManager.questMemStage == QuestManager.QM_COLLECT_3) {
            String[] messages = {
                    "In Japan in 1888, I met O-Sei-San, a girl full of beauty, charm, and\nmodesty who patiently taught me Nihonggo.",

                    "During my tour of the USA in 1888, we were quarantined due to a\nCholera outbreak and the illusion of equality was shattered by the\nrampant racial prejudice I witnessed.",

                    "In Hongkong from 1891 to 1892, I found a brief moment of peace\nworking as an Ophthalmic Surgeon and was even able to spend\na blessed Christmas with my family."
            };
            gp.ui.currentDialogue = messages[memSecondIndex];
            gp.ui.currentSpeakerName = "Jose Rizal";
            gp.gameState = gp.dialogueState;
            gp.obj[i] = null;
            gp.questManager.onMemSecondPartCollected(memSecondIndex);
            return;
        }
        int manuscriptIndex = getManuscriptIndex(objectName);
        if (manuscriptIndex >= 0 &&
                gp.questManager.quest5Stage == QuestManager.COLLECT_OBJECTS) {
            String[] messages = {
                    "'Touch Me Not.' Our country has a cancer so sensitive that the\nslightest touch causes agony. [Title and Preface finalized.]",

                    "I see myself in Crisostomo Ibarra, the dreamer returning home\nonly to find the soil poisone. [Main Character Arc defined.]",

                    "She is the Philippines — beautiful, weeping, silenced.\n[The Idyl on the Terrace defined.]",

                    "Father Damaso. The cross he wears is not for salvation, but\nfor control.[The Feast added to manuscript.]",

                    "I shall call her Sisa. The soul of the Filipino family, driven\nto madness by cruelty. [Sisa added to manuscript.]",

                    "The man of action. The mirror of what I fear I might become if\nthe pen fails. [The Voice of the Hunted added.]",

                    "My hunger is not just for bread, but for justice. Every word is\npaid for with my own body. [Desperate Resolve added.]"
            };
            gp.ui.currentDialogue = messages[manuscriptIndex];
            gp.ui.currentSpeakerName = "Jose Rizal";
            gp.gameState = gp.dialogueState;
            gp.obj[i] = null;
            gp.questManager.onManuscriptPartCollected(manuscriptIndex);
        }

        // EL FILI
        int manuscriptIndexQ6 = getManuscriptIndexQ6(objectName);
        if (manuscriptIndexQ6 >= 0 &&
                gp.questManager.quest6Stage == QuestManager.COLLECT_OBJECTS_Q6) {
            String[] messages = {
                    "To change the world, one must sometimes walk among the corrupt.\nSimoun will wear these. He will hide his eyes so they cannot see the\nfire of his soul.",

                    "So, the garden is closed. The girl who waited by the window has\nvanished into the mist of another man’s name. Ibarra...that poor,\n naive fool. He died in the lake.",

                    "The friars have moved from insults to fire. They have turned\nfarmers intofugitives.I will create Cabesang Tales...not as a " +
                            "hero\nbut as a warning of what happens when a man is pushed",

                    "I can still hear the snap of the wood in the morning air of\nBagumbayan...it has been nineteen years, yet the silence they " +
                            "left\nbehind is deafening.",
            };
            gp.ui.currentDialogue = messages[manuscriptIndexQ6];
            gp.ui.currentSpeakerName = "Jose Rizal";
            gp.gameState = gp.dialogueState;
            gp.obj[i] = null;
            gp.questManager.onq6ObjectsCollected(manuscriptIndexQ6);
        }
    }

    private int getManuscriptIndex(String name) {
        switch (name) {
            case "Scalpel":
                return 0;
            case "Mirror":
                return 1;
            case "Dried Flower":
                return 2;
            case "Rosary":
                return 3;
            case "Portrait":
                return 4;
            case "Scrap Metal":
                return 5;
            case "Empty Plate":
                return 6;
            default:
                return -1;
        }
    }

    private int getManuscriptIndexQ6 (String name){
        switch (name) {
            case "Glasses":
                return 0;
            case "Newspaper":
                return 1;
            case "Old Letter":
                return 2;
            case "Worn Letter":
                return 3;
            default:
                return -1;
        }
    }

    private int getKeepsakeIndex(String name) {
        switch (name) {
            case "Paper Rose": return 0;
            case "Invisible Ink": return 1;
            case "Locket": return 2;
            case "Abanico": return 3;
            case "Suzuri": return 4;
            case "Clay Knife": return 5;
            case "Bible": return 6;
            case "Belgian Biscuits": return 7;
            case "Lense": return 8;
            default: return -1;
        }
    }

    private int getMemFirstIndex(String name) {
        switch (name) {
            case "Medical Books":         return 0;
            case "Letters and Postcards": return 1;
            case "Dusty Manuscript":      return 2;
            case "Legal Docs":            return 3;
            case "Envelope":              return 4;
            case "Ophthalmoscope":        return 5;
            default:                      return -1;
        }
    }

    private int getMemSecondIndex(String name) {
        switch (name) {
            case "Origami Crane": return 0;
            case "Ship Ticket":   return 1;
            case "Medical Bag":   return 2;
            default:              return -1;
        }
    }

    public void rebuildCongoLine() {
        ArrayList<NPC_Sibling> followers = new ArrayList<>();
        for (int i = 0; i < gp.npc.length; i++) {
            if (gp.npc[i] instanceof NPC_Sibling s) {
                if (s.isFollowing && !s.delivered) {
                    followers.add(s);
                }
            }
        }

        followers.sort((a, b) -> Integer.compare(a.recruitOrder, b.recruitOrder));

        Entity leader = this;
        for (NPC_Sibling s : followers) {
            s.followTarget = leader;
            leader = s;
        }
    }


    public void draw(Graphics2D g2){

        BufferedImage image = null;

        switch (direction){
            case"up":
                if(spriteNum == 1) { image = up1; }
                if (spriteNum == 2){ image = up2; }
                break;
            case"down":
                if(spriteNum == 1) { image = down1; }
                if (spriteNum == 2){ image = down2; }
                break;
            case"left":
                if(spriteNum == 1) { image = left1; }
                if (spriteNum == 2){ image = left2; }
                break;
            case"right":
                if(spriteNum == 1) { image = right1; }
                if (spriteNum == 2){ image = right2; }
                break;
        }
        g2.drawImage(image, screenX, screenY, null);
    }
}
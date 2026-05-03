package entity;

import main.GamePanel;
import main.KeyHandler;
import main.QuestManager;
import main.UtilityTool;
import object.OBJ_Quil;

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

            int tileX = worldX / gp.tileSize;
            int tileY = worldY / gp.tileSize;
            if (tileX != lastTileX || tileY != lastTileY) {
                System.out.println("Tile X: " + tileX + " | Tile Y: " + tileY);
                lastTileX = tileX;
                lastTileY = tileY;
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
            if (getManuscriptIndex(objectName) >= 0) return;
            if (getManuscriptIndexQ6(objectName) >= 0) return;
            if (objectName.equals("Draft of Noli Me Tangere")) return;
            if (objectName.equals("Draft of El Filibusterismo")) return;

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

        // NOLI ME TANGERE
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

    public void rebuildCongoLine() {
        Entity leader = this;
        int count = 0;

        for (int i = 0; i < gp.npc.length; i++) {
            if (gp.npc[i] instanceof NPC_Sibling) {
                NPC_Sibling s = (NPC_Sibling) gp.npc[i];
                if (s.isFollowing && !s.delivered) {
                    s.followTarget = leader;
                    leader = s;
                    count++;
                }
            }
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
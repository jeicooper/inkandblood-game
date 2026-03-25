package entity;

import main.GamePanel;
import main.KeyHandler;
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
        worldX = gp.tileSize * 73;
        worldY = gp.tileSize * 28;
        speed = 10;
        direction = "down";

        // PLAYER STATS
        maxExp = 10; // 1 = halfexp
        exp = 0;

        intellect = 1;
        creativity = 1;
        perception = 1;
        charisma = 1;

        //itemName = new OBJ_itemName(gp);

    }

    public void setItems (){
        //inventory.add(itemName);

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
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // MOVE ONLY ONCE AFTER COLLISION CHECK
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
    }

    public void interactNPC(int i){

        if (i != 999 && gp.keyP.fPressed == true){

                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
        }

        gp.keyP.fPressed = false;
    }


    public void pickUpObject(int i){

        if (i != 999){

            if (inventory.size() != maxInventorySize){

                String objectName = gp.obj[i].name;
                inventory.add(gp.obj[i]);
                gp.obj[i] = null;
                gp.playSE(1);
                gp.ui.showMessage("Got a " + objectName + "!");
            }
            else {
                gp.ui.showMessage("Your inventory is full!");
            }
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
                if(spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2){
                    image = up2;
                }
                break;
            case"down":
                if(spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2){
                    image = down2;
                }
                break;
            case"left":
                if(spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2){
                    image = left2;
                }
                break;
            case"right":
                if(spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2){
                    image = right2;
                }
                break;
        }
        g2.drawImage(image, screenX, screenY, null);
    }
}

package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends  Entity{

    KeyHandler keyP;

    public final int screenX;
    public final int screenY;

    //INVENTORY
    public int hasQuil = 0;

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
    }

    public void setDefaultValues(){

        //player position in the map
        worldX = gp.tileSize * 74;
        worldY = gp.tileSize * 28;
        speed = 3;
        direction = "down";

        // PLAYER STATS
        maxLife = 10; // 1 = half heart
        life = maxLife;

        intellect = 1;
        creativity = 1;
        perception = 1;
        charisma = 1;



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

        if (keyP.upPressed == true || keyP.downPressed == true || keyP.leftPressed == true || keyP.rightPressed == true ){

            if (keyP.upPressed == true){
                direction = "up";
                if (!collisionOn){
                    worldY -= speed;
                }

            }
            else if (keyP.downPressed == true){
                direction = "down";
                if (!collisionOn){
                    worldY += speed;
                }

            }
            else if (keyP.leftPressed == true){
                direction = "left";
                if (!collisionOn){
                    worldX -= speed;
                }

            }
            else if (keyP.rightPressed == true){
                direction = "right";
                if (!collisionOn){
                    worldX += speed;
                }

            }

            //  CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // CHECK OBJ COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            //CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (!collisionOn){

                switch (direction){
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            spriteCounter++;
            if (spriteCounter > 12){
                if (spriteNum == 1){
                    spriteNum = 2;
                }
                else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }


    }

    public void pickUpObject(int i){

        if (i != 999){

            String objectName = gp.obj[i].name;

            switch (objectName){
//                case "Quil":
//                    gp.playSE(1);
//                    hasQuil++;
//                    gp.obj[i] = null;
//                    gp.ui.showMessage("You found a quil!");
//                    break;
//
//                case "Book":
//                    if (hasQuil > 0) {
//                        gp.playSE(1);
//                        gp.obj[i] = null;
//                        hasQuil--;
//                        gp.ui.showMessage("You wrote on the book!");
//                    } else{
//                        gp.ui.showMessage("You need a Quill!");
//                    }
//                    break;
            }
        }
    }

    public void interactNPC(int i){

        if (i != 999) {

            if (gp.keyP.fPressed == true){

                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }

        }

        gp.keyP.fPressed = false;
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

package main;
import entity.Entity;
import java.awt.Rectangle;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }

    public void checkTile(Entity entity) {

        // Use solidArea with offsets
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;

                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];

                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }

                break;

            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;

                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }

                break;

            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;

                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];

                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }

                break;

            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;

                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];

                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }

                break;
        }


    }

    public int checkObject(Entity entity, boolean player){
        int index = 999;

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null) {

                Rectangle entityRect = new Rectangle(
                        entity.worldX + entity.solidAreaDefaultX,
                        entity.worldY + entity.solidAreaDefaultY,
                        entity.solidArea.width,
                        entity.solidArea.height
                );

                Rectangle objRect = new Rectangle(
                        gp.obj[i].worldX + gp.obj[i].solidAreaDefaultX,
                        gp.obj[i].worldY + gp.obj[i].solidAreaDefaultY,
                        gp.obj[i].solidArea.width,
                        gp.obj[i].solidArea.height
                );

                switch (entity.direction){
                    case "up":    entityRect.y -= entity.speed; break;
                    case "down":  entityRect.y += entity.speed; break;
                    case "left":  entityRect.x -= entity.speed; break;
                    case "right": entityRect.x += entity.speed; break;
                }

                if (entityRect.intersects(objRect)){
                    if (gp.obj[i].collision) entity.collisionOn = true;
                    if (player) index = i;
                }
            }
        }
        return index;
    }

    //NPC COLLISION
    public int checkEntity(Entity entity, Entity[] target){
        int index = 999;

        for (int i = 0; i < target.length; i++) {
            if (target[i] != null) {

                Rectangle entityRect = new Rectangle(
                        entity.worldX + entity.solidAreaDefaultX,
                        entity.worldY + entity.solidAreaDefaultY,
                        entity.solidArea.width,
                        entity.solidArea.height
                );

                Rectangle targetRect = new Rectangle(
                        target[i].worldX + target[i].solidAreaDefaultX,
                        target[i].worldY + target[i].solidAreaDefaultY,
                        target[i].solidArea.width,
                        target[i].solidArea.height
                );

                switch (entity.direction){
                    case "up":    entityRect.y -= entity.speed; break;
                    case "down":  entityRect.y += entity.speed; break;
                    case "left":  entityRect.x -= entity.speed; break;
                    case "right": entityRect.x += entity.speed; break;
                }

                if (entityRect.intersects(targetRect)){
                    entity.collisionOn = true;
                    index = i;
                }
            }
        }
        return index;
    }

    public void checkPlayer(Entity entity){

        Rectangle entityRect = new Rectangle(
                entity.worldX + entity.solidAreaDefaultX,
                entity.worldY + entity.solidAreaDefaultY,
                entity.solidArea.width,
                entity.solidArea.height
        );

        Rectangle playerRect = new Rectangle(
                gp.player.worldX + gp.player.solidAreaDefaultX,
                gp.player.worldY + gp.player.solidAreaDefaultY,
                gp.player.solidArea.width,
                gp.player.solidArea.height
        );

        switch (entity.direction){
            case "up":    entityRect.y -= entity.speed; break;
            case "down":  entityRect.y += entity.speed; break;
            case "left":  entityRect.x -= entity.speed; break;
            case "right": entityRect.x += entity.speed; break;
        }

        if (entityRect.intersects(playerRect)){
            entity.collisionOn = true;
        }
    }
}

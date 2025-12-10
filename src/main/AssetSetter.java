package main;

import entity.NPC_Teodora;
import object.OBJ_Book;
import object.OBJ_Quil;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObject(){
//
//        gp.obj[0] = new OBJ_Quil(gp);
//        gp.obj[0].worldX = 71 * gp.tileSize;
//        gp.obj[0].worldY = 24 * gp.tileSize;
//
//        gp.obj[1] = new OBJ_Book(gp);
//        gp.obj[1].worldX = 75 * gp.tileSize;
//        gp.obj[1].worldY = 30 * gp.tileSize;
//
//        gp.obj[2] = new OBJ_Quil(gp);
//        gp.obj[2].worldX = 74 * gp.tileSize;
//        gp.obj[2].worldY = 35 * gp.tileSize;

    }

    public void setNPC() {

        gp.npc[0] = new NPC_Teodora(gp);
        gp.npc[0].worldX = 75 * gp.tileSize;
        gp.npc[0].worldY = 24 * gp.tileSize;

    }
}

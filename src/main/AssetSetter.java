package main;

import entity.NPC_Concha;
import entity.NPC_Teodora;
import object.OBJ_Quil;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObject(){

        int i = 0;
        gp.obj[i] = new OBJ_Quil(gp);
        gp.obj[i].worldX = 57 * gp.tileSize;
        gp.obj[i].worldY = 19 * gp.tileSize;
        i++;



        gp.obj[i] = new OBJ_Quil(gp);
        gp.obj[i].worldX = 65 * gp.tileSize;
        gp.obj[i].worldY = 14 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_Quil(gp);
        gp.obj[i].worldX = 23 * gp.tileSize;
        gp.obj[i].worldY = 9 * gp.tileSize;
        i++;
    }

    public void setNPC() {

        gp.npc[0] = new NPC_Teodora(gp);
        gp.npc[0].worldX = 75 * gp.tileSize;
        gp.npc[0].worldY = 24 * gp.tileSize;

        gp.npc[1] = new NPC_Concha(gp);
        gp.npc[1].worldX = 58 * gp.tileSize;
        gp.npc[1].worldY = 19 * gp.tileSize;

    }
}

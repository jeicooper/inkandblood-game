package main;

import entity.NPC_Concha;
import entity.NPC_Teodora;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObject(){

//          gp.obj[0] = new OBJ_Grave(gp);
//          gp.obj[0].worldX = 58 * gp.tileSize;
//          gp.obj[0].worldY = 19 * gp.tileSize;
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

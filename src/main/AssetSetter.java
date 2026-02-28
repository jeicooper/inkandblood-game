package main;

import entity.NPC_Concha;
import entity.NPC_Teodora;
import entity.NPC_Sibling;
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

        placeSibling(2,  "Saturnina",  "/npc/saturnina/saturnina",  70, 35);
        placeSibling(3,  "Paciano",    "/npc/paciano/paciano",      71, 35);
        placeSibling(4,  "Narcisa",    "/npc/narcisa/narcisa",      72, 35);
        placeSibling(5,  "Olimpia",    "/npc/olimpia/olimpia",      73, 35);
        placeSibling(6,  "Lucia",      "/npc/lucia/lucia",          69, 35);
        placeSibling(7,  "Maria",      "/npc/maria/maria",          66, 35);
        placeSibling(8,  "Josefa",     "/npc/josefa/josefa",        68, 35);
        placeSibling(9,  "Trinidad",   "/npc/trinidad/trinidad",    67, 35);
        placeSibling(10, "Soledad",    "/npc/soledad/soledad",      80, 35);
        placeSibling(11, "Concepcion", "/npc/grave",78, 35);
    }

    private void placeSibling(int index, String name, String spritePath, int tileX, int tileY) {
        NPC_Sibling s = new NPC_Sibling(gp, name, spritePath);
        s.worldX = tileX * gp.tileSize;
        s.worldY = tileY * gp.tileSize;
        gp.npc[index] = s;
    }
}

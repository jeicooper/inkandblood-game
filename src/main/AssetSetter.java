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

        placeSibling(2,  "Saturnina 'Neneng' Rizal",  "/npc/saturnina/saturnina",  24, 53);
        placeSibling(3,  "Paciano 'Ute' Rizal ",    "/npc/paciano/paciano",      38, 23);
        placeSibling(4,  "Narcisa 'Sisa' Rizal",    "/npc/narcisa/narcisa",      32, 36);
        placeSibling(5,  "Olimpia 'Ypia' Rizal",    "/npc/olimpia/olimpia",      29, 41);
        placeSibling(6,  "Lucia 'Lucing' Rizal",      "/npc/lucia/lucia",          39, 53);
        placeSibling(7,  "Maria 'Biang' Rizal",      "/npc/maria/maria",          37, 25);
        placeSibling(8,  "Josefa 'Panggoy' Rizal",     "/npc/josefa/josefa",        22, 50);
        placeSibling(9,  "Trinidad 'Trining' Rizal",   "/npc/trinidad/trinidad",    49, 54);
        placeSibling(10, "Soledad 'Choleng' Rizal",    "/npc/soledad/soledad",      20, 38);
        placeSibling(11, "Concepcion 'Concha' Rizal", "/npc/grave",78, 35);
    }

    private NPC_Sibling placeSibling(int index, String name, String spritePath, int tileX, int tileY) {
        NPC_Sibling s = new NPC_Sibling(gp, name, spritePath);
        s.worldX = tileX * gp.tileSize;
        s.worldY = tileY * gp.tileSize;
        gp.npc[index] = s;
        return s;
    }
}

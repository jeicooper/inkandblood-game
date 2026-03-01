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

//        int i = 0;
//        gp.obj[i] = new OBJ_Quil(gp);
//        gp.obj[i].worldX = 57 * gp.tileSize;
//        gp.obj[i].worldY = 19 * gp.tileSize;
//        i++;

    }

    public void setNPC() {

        gp.npc[0] = new NPC_Teodora(gp);
        gp.npc[0].worldX = 64 * gp.tileSize;
        gp.npc[0].worldY = 24 * gp.tileSize;

        gp.npc[1] = new NPC_Concha(gp);
        gp.npc[1].worldX = 58 * gp.tileSize;
        gp.npc[1].worldY = 19 * gp.tileSize;

        placeSibling(2,  "Saturnina 'Neneng' Rizal",  "/npc/saturnina/saturnina",  75, 34);
        placeSibling(3,  "Paciano 'Ute' Rizal ",    "/npc/paciano/paciano",      76, 34);
        placeSibling(4,  "Narcisa 'Sisa' Rizal",    "/npc/narcisa/narcisa",      71, 34);
        placeSibling(5,  "Olimpia 'Ypia' Rizal",    "/npc/olimpia/olimpia",      72, 34);
        placeSibling(6,  "Lucia 'Lucing' Rizal",      "/npc/lucia/lucia",          73, 34);
        placeSibling(7,  "Maria 'Biang' Rizal",      "/npc/maria/maria",          74, 34);
        placeSibling(8,  "Josefa 'Panggoy' Rizal",     "/npc/josefa/josefa",        77, 34);
        placeSibling(9,  "Trinidad 'Trining' Rizal",   "/npc/trinidad/trinidad",    78, 34);
        placeSibling(10, "Soledad 'Choleng' Rizal",    "/npc/soledad/soledad",      79, 34);

    }

    private NPC_Sibling placeSibling(int index, String name, String spritePath, int tileX, int tileY) {
        NPC_Sibling s = new NPC_Sibling(gp, name, spritePath);
        s.worldX = tileX * gp.tileSize;
        s.worldY = tileY * gp.tileSize;
        gp.npc[index] = s;
        return s;
    }
}

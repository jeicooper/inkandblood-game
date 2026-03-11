package main;

import entity.*;
import object.*;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObject(){
        //
//        int i = 0;
//        gp.obj[i] = new OBJ_ObjectName(gp);
//        gp.obj[i].worldX = X * gp.tileSize;
//        gp.obj[i].worldY = Y * gp.tileSize;
//        i++;

    }

    public void setNPC() {

        gp.npc[0] = new NPC_Teodora(gp);
        gp.npc[0].worldX = 64 * gp.tileSize;
        gp.npc[0].worldY = 24 * gp.tileSize;

        gp.npc[1] = new NPC_Concha(gp);
        gp.npc[1].worldX = 58 * gp.tileSize;
        gp.npc[1].worldY = 21 * gp.tileSize;

        placeSibling(2,  "Saturnina 'Neneng' Rizal",
                "/npc/saturnina/saturnina",  80, 31);
        placeSibling(3,  "Paciano 'Ute' Rizal ",
                "/npc/paciano/paciano",      76, 46);
        placeSibling(4,  "Narcisa 'Sisa' Rizal",
                "/npc/narcisa/narcisa",      57, 54);
        placeSibling(5,  "Olimpia 'Ypia' Rizal",
                "/npc/olimpia/olimpia",      88, 53);
        placeSibling(6,  "Lucia 'Lucing' Rizal",
                "/npc/lucia/lucia",          73, 11);
        placeSibling(7,  "Maria 'Biang' Rizal",
                "/npc/maria/maria",          30, 23);
        placeSibling(8,  "Josefa 'Panggoy' Rizal",
                "/npc/josefa/josefa",        15, 27);
        placeSibling(9,  "Trinidad 'Trining' Rizal",
                "/npc/trinidad/trinidad",    22, 35);
        placeSibling(10, "Soledad 'Choleng' Rizal",
                "/npc/soledad/soledad",      21, 44);

        gp.npc[11] = new NPC_Francisco(gp);
        gp.npc[11].worldX = 65 * gp.tileSize;
        gp.npc[11].worldY = 24 * gp.tileSize;

    }

    private NPC_Sibling placeSibling(int index, String name, String spritePath, int tileX, int tileY) {
        NPC_Sibling s = new NPC_Sibling(gp, name, spritePath);
        s.worldX = tileX * gp.tileSize;
        s.worldY = tileY * gp.tileSize;
        gp.npc[index] = s;
        return s;
    }

    public void activateQuest2() {
        // Uncles
        gp.npc[12] = new NPC_Jose(gp);
        gp.npc[12].worldX = 80 * gp.tileSize;
        gp.npc[12].worldY = 36 * gp.tileSize;

        gp.npc[13] = new NPC_Manuel(gp);
        gp.npc[13].worldX = 81 * gp.tileSize;
        gp.npc[13].worldY = 50 * gp.tileSize;

        gp.npc[14] = new NPC_Gregorio(gp);
        gp.npc[14].worldX = 60 * gp.tileSize;
        gp.npc[14].worldY = 39 * gp.tileSize;


        // Art supplies
        int i = 0;
        gp.obj[i] = new OBJ_PaintBucket(gp, "/objects/black_paint");
        gp.obj[i].worldX = 65 * gp.tileSize;
        gp.obj[i].worldY = 53 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_PaintBucket(gp, "/objects/blue_paint");
        gp.obj[i].worldX = 72 * gp.tileSize;
        gp.obj[i].worldY = 57 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_PaintBucket(gp, "/objects/green_paint");
        gp.obj[i].worldX = 69 * gp.tileSize;
        gp.obj[i].worldY = 43 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_PaintBucket(gp, "/objects/red_paint");
        gp.obj[i].worldX = 69 * gp.tileSize;
        gp.obj[i].worldY = 38 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_PaintBucket(gp, "/objects/white_paint");
        gp.obj[i].worldX = 50 * gp.tileSize;
        gp.obj[i].worldY = 60 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_PaintBucket(gp, "/objects/yellow_paint");
        gp.obj[i].worldX = 72 * gp.tileSize;
        gp.obj[i].worldY = 79 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_PaintBrush(gp);
        gp.obj[i].worldX = 60 * gp.tileSize;
        gp.obj[i].worldY = 38 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_Canvas(gp);
        gp.obj[i].worldX = 73 * gp.tileSize;
        gp.obj[i].worldY = 78 * gp.tileSize;
    }

    public void activateGregorio() {
        int i = 9;
        gp.obj[i] = new OBJ_Quil(gp);
        gp.obj[i].worldX = 62 * gp.tileSize; // calibrate to Gregorio's house
        gp.obj[i].worldY = 37 * gp.tileSize;
        i++;
        gp.obj[i] = new OBJ_Notebook(gp);
        gp.obj[i].worldX = 60 * gp.tileSize;
        gp.obj[i].worldY = 37 * gp.tileSize;
    }
}

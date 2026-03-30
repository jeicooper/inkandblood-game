package main;

import entity.*;
import object.*;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObject(){

    }

    public void setNPC() {

        gp.npc[0] = new NPC_Teodora(gp);
        gp.npc[0].worldX = 64 * gp.tileSize;
        gp.npc[0].worldY = 24 * gp.tileSize;

        gp.npc[1] = new NPC_Concha(gp);
        gp.npc[1].worldX = 65 * gp.tileSize;
        gp.npc[1].worldY = 34 * gp.tileSize;

        placeSibling(2,  "Saturnina 'Neneng' Rizal",
                "/npc/saturnina/saturnina",  66, 34);
        placeSibling(3,  "Paciano 'Ute' Rizal ",
                "/npc/paciano/paciano",      67, 34);
        placeSibling(4,  "Narcisa 'Sisa' Rizal",
                "/npc/narcisa/narcisa",      68, 34);
        placeSibling(5,  "Olimpia 'Ypia' Rizal",
                "/npc/olimpia/olimpia",      69, 34);
        placeSibling(6,  "Lucia 'Lucing' Rizal",
                "/npc/lucia/lucia",          70, 34);
        placeSibling(7,  "Maria 'Biang' Rizal",
                "/npc/maria/maria",          71, 34);
        placeSibling(8,  "Josefa 'Panggoy' Rizal",
                "/npc/josefa/josefa",        72, 34);
        placeSibling(9,  "Trinidad 'Trining' Rizal",
                "/npc/trinidad/trinidad",    73, 34);
        placeSibling(10, "Soledad 'Choleng' Rizal",
                "/npc/soledad/soledad",      74, 34);

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
        gp.npc[12].worldX = 70 * gp.tileSize;
        gp.npc[12].worldY = 34 * gp.tileSize;

        gp.npc[13] = new NPC_Manuel(gp);
        gp.npc[13].worldX = 75 * gp.tileSize;
        gp.npc[13].worldY = 34 * gp.tileSize;

        gp.npc[14] = new NPC_Gregorio(gp);
        gp.npc[14].worldX = 80 * gp.tileSize;
        gp.npc[14].worldY = 34 * gp.tileSize;


        // Art supplies
        int i = 0;
        gp.obj[i] = new OBJ_PaintBucket(gp, "/objects/black_paint");
        gp.obj[i].worldX = 71 * gp.tileSize;
        gp.obj[i].worldY = 34 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_PaintBucket(gp, "/objects/blue_paint");
        gp.obj[i].worldX = 72 * gp.tileSize;
        gp.obj[i].worldY = 34 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_PaintBucket(gp, "/objects/green_paint");
        gp.obj[i].worldX = 73 * gp.tileSize;
        gp.obj[i].worldY = 34 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_PaintBucket(gp, "/objects/red_paint");
        gp.obj[i].worldX = 74 * gp.tileSize;
        gp.obj[i].worldY = 34 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_PaintBucket(gp, "/objects/white_paint");
        gp.obj[i].worldX = 65 * gp.tileSize;
        gp.obj[i].worldY = 34 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_PaintBucket(gp, "/objects/yellow_paint");
        gp.obj[i].worldX = 66 * gp.tileSize;
        gp.obj[i].worldY = 34 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_PaintBrush(gp);
        gp.obj[i].worldX = 67 * gp.tileSize;
        gp.obj[i].worldY = 34 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_Canvas(gp);
        gp.obj[i].worldX = 68 * gp.tileSize;
        gp.obj[i].worldY = 34 * gp.tileSize;
    }

    public void activateGregorio() {
        int i = 9;
        gp.obj[i] = new OBJ_Quil(gp);
        gp.obj[i].worldX = 69 * gp.tileSize;
        gp.obj[i].worldY = 34 * gp.tileSize;
        i++;
        gp.obj[i] = new OBJ_Notebook(gp);
        gp.obj[i].worldX = 76 * gp.tileSize;
        gp.obj[i].worldY = 34 * gp.tileSize;
    }

    public void activateChapter2() {
        gp.npc[14] = new NPC_Ferrando(gp);
        gp.npc[14].worldX = 51 * gp.tileSize;
        gp.npc[14].worldY = 43 * gp.tileSize;

        gp.npc[15] = new NPC_Burgos(gp);
        gp.npc[15].worldX = 36 * gp.tileSize;
        gp.npc[15].worldY = 29 * gp.tileSize;

        gp.npc[16] = new NPC_Professor(gp);
        gp.npc[16].worldX = 36 * gp.tileSize;
        gp.npc[16].worldY = 45 * gp.tileSize;

        gp.npc[17] = new NPC_Student(gp);
        gp.npc[17].worldX = 40 * gp.tileSize;
        gp.npc[17].worldY = 43 * gp.tileSize;

    }
}

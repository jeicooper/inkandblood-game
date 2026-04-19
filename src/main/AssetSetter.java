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
        gp.npc[0].worldX = 63 * gp.tileSize;
        gp.npc[0].worldY = 27 * gp.tileSize;

        gp.npc[1] = new NPC_Concha(gp);
        gp.npc[1].worldX = 58 * gp.tileSize;
        gp.npc[1].worldY = 22 * gp.tileSize;

        gp.npc[2] = new NPC_Francisco(gp);
        gp.npc[2].worldX = 65 * gp.tileSize;
        gp.npc[2].worldY = 24 * gp.tileSize;


        placeSibling(3,  "Saturnina 'Neneng' Rizal",
                "/npc/saturnina/saturnina",  71, 19);
        placeSibling(4,  "Paciano 'Ute' Rizal ",
                "/npc/paciano/paciano",      53, 22);
        placeSibling(5,  "Narcisa 'Sisa' Rizal",
                "/npc/narcisa/narcisa",      74, 19);
        placeSibling(6,  "Olimpia 'Ypia' Rizal",
                "/npc/olimpia/olimpia",      76, 27);
        placeSibling(7,  "Lucia 'Lucing' Rizal",
                "/npc/lucia/lucia",          80, 18);
        placeSibling(8,  "Maria 'Biang' Rizal",
                "/npc/maria/maria",          55, 31);
        placeSibling(9,  "Josefa 'Panggoy' Rizal",
                "/npc/josefa/josefa",        85, 18);
        placeSibling(10,  "Trinidad 'Trining' Rizal",
                "/npc/trinidad/trinidad",    93, 30);
        placeSibling(11, "Soledad 'Choleng' Rizal",
                "/npc/soledad/soledad",      60, 17);

    }

    private NPC_Sibling placeSibling(int index, String name, String spritePath, int tileX, int tileY) {
        NPC_Sibling s = new NPC_Sibling(gp, name, spritePath);
        s.worldX = tileX * gp.tileSize;
        s.worldY = tileY * gp.tileSize;
        gp.npc[index] = s;
        s.dexId = siblingDexId(name);
        return s;
    }

    private String siblingDexId(String name) {
        if (name.contains("Saturnina")) return "saturnina";
        if (name.contains("Paciano"))   return "paciano";
        if (name.contains("Narcisa"))   return "narcisa";
        if (name.contains("Olimpia"))   return "olimpia";
        if (name.contains("Lucia"))     return "lucia";
        if (name.contains("Maria"))     return "maria";
        if (name.contains("Josefa"))    return "josefa";
        if (name.contains("Trinidad"))  return "trinidad";
        if (name.contains("Soledad"))   return "soledad";
        return null;
    }

    public void activateQuest2() {
        // Uncles
        gp.npc[12] = new NPC_Jose(gp);
        gp.npc[12].worldX = 66 * gp.tileSize;
        gp.npc[12].worldY = 42 * gp.tileSize;

        gp.npc[13] = new NPC_Manuel(gp);
        gp.npc[13].worldX = 27 * gp.tileSize;
        gp.npc[13].worldY = 34 * gp.tileSize;

        gp.npc[14] = new NPC_Gregorio(gp);
        gp.npc[14].worldX = 79 * gp.tileSize;
        gp.npc[14].worldY = 51 * gp.tileSize;

        // Art supplies
        int i = 0;
        gp.obj[i] = new OBJ_PaintBucket(gp, "/objects/black_paint");
        gp.obj[i].worldX = 69 * gp.tileSize;
        gp.obj[i].worldY = 37 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_PaintBucket(gp, "/objects/blue_paint");
        gp.obj[i].worldX = 51 * gp.tileSize;
        gp.obj[i].worldY = 36 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_PaintBucket(gp, "/objects/green_paint");
        gp.obj[i].worldX = 55 * gp.tileSize;
        gp.obj[i].worldY = 58 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_PaintBucket(gp, "/objects/red_paint");
        gp.obj[i].worldX = 66 * gp.tileSize;
        gp.obj[i].worldY = 59 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_PaintBucket(gp, "/objects/white_paint");
        gp.obj[i].worldX = 72 * gp.tileSize;
        gp.obj[i].worldY = 63 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_PaintBucket(gp, "/objects/yellow_paint");
        gp.obj[i].worldX = 65 * gp.tileSize;
        gp.obj[i].worldY = 54 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_PaintBrush(gp);
        gp.obj[i].worldX = 60 * gp.tileSize;
        gp.obj[i].worldY = 37 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_Canvas(gp);
        gp.obj[i].worldX = 70 * gp.tileSize;
        gp.obj[i].worldY = 43 * gp.tileSize;

    }

    public void activateGregorio() {
        int i = 9;
        gp.obj[i] = new OBJ_Quil(gp);
        gp.obj[i].worldX = 71 * gp.tileSize;
        gp.obj[i].worldY = 53 * gp.tileSize;
        i++;
        gp.obj[i] = new OBJ_Notebook(gp);
        gp.obj[i].worldX = 71 * gp.tileSize;
        gp.obj[i].worldY = 58 * gp.tileSize;
    }

    public void activateChapter2() {
        gp.npc[15] = new NPC_Ferrando(gp);
        gp.npc[15].worldX = 48 * gp.tileSize;
        gp.npc[15].worldY = 45 * gp.tileSize;

        gp.npc[16] = new NPC_Burgos(gp);
        gp.npc[16].worldX = 36 * gp.tileSize;
        gp.npc[16].worldY = 29 * gp.tileSize;

        gp.npc[17] = new NPC_Professor(gp);
        gp.npc[17].worldX = 36 * gp.tileSize;
        gp.npc[17].worldY = 45 * gp.tileSize;

        gp.npc[18] = new NPC_Interno(gp);
        gp.npc[18].worldX = 40 * gp.tileSize;
        gp.npc[18].worldY = 43 * gp.tileSize;

        gp.npc[19] = new NPC_Mariano(gp);
        gp.npc[19].worldX = 40 * gp.tileSize;
        gp.npc[19].worldY = 47 * gp.tileSize;

        gp.npc[20] = new NPC_Externo(gp);
        gp.npc[20].worldX = 43 * gp.tileSize;
        gp.npc[20].worldY = 43 * gp.tileSize;

        gp.npc[21] = new NPC_Externo(gp);
        gp.npc[21].worldX = 43 * gp.tileSize;
        gp.npc[21].worldY = 47 * gp.tileSize;

        gp.npc[22] = new NPC_Externo(gp);
        gp.npc[22].worldX = 46 * gp.tileSize;
        gp.npc[22].worldY = 43 * gp.tileSize;


    }

    public void activateEnrollment(){
        gp.npc[23] = new NPC_Ferrando(gp);
        gp.npc[23].worldX = 36 * gp.tileSize;
        gp.npc[23].worldY = 41 * gp.tileSize;

        gp.npc[24] = new NPC_Burgos(gp);
        gp.npc[24].worldX = 36 * gp.tileSize;
        gp.npc[24].worldY = 29 * gp.tileSize;

        gp.npc[25] = new NPC_Professor(gp);
        gp.npc[25].worldX = 36 * gp.tileSize;
        gp.npc[25].worldY = 45 * gp.tileSize;

        gp.npc[26] = new NPC_Interno(gp);
        gp.npc[26].worldX = 40 * gp.tileSize;
        gp.npc[26].worldY = 43 * gp.tileSize;

        gp.npc[27] = new NPC_Mariano(gp);
        gp.npc[27].worldX = 40 * gp.tileSize;
        gp.npc[27].worldY = 47 * gp.tileSize;

        gp.npc[28] = new NPC_Externo(gp);
        gp.npc[28].worldX = 43 * gp.tileSize;
        gp.npc[28].worldY = 43 * gp.tileSize;

        gp.npc[29] = new NPC_Externo(gp);
        gp.npc[29].worldX = 43 * gp.tileSize;
        gp.npc[29].worldY = 47 * gp.tileSize;

        gp.npc[30] = new NPC_Externo(gp);
        gp.npc[30].worldX = 46 * gp.tileSize;
        gp.npc[30].worldY = 43 * gp.tileSize;

    }

    public void activateQuest4() {
        for (int i = 0; i < gp.npc.length; i++) gp.npc[i] = null;
        for (int i = 0; i < gp.obj.length; i++) gp.obj[i] = null;

        gp.npc[31] = new NPC_Professor(gp);
        gp.npc[31].worldX = 36 * gp.tileSize;
        gp.npc[31].worldY = 45 * gp.tileSize;

        gp.npc[32] = new NPC_Interno(gp);
        gp.npc[32].worldX = 40 * gp.tileSize;
        gp.npc[32].worldY = 43 * gp.tileSize;

        gp.npc[33] = new NPC_Mariano(gp);
        gp.npc[33].worldX = 40 * gp.tileSize;
        gp.npc[33].worldY = 47 * gp.tileSize;

        gp.npc[34] = new NPC_Externo(gp);
        gp.npc[34].worldX = 43 * gp.tileSize;
        gp.npc[34].worldY = 43 * gp.tileSize;

        gp.npc[35] = new NPC_Externo(gp);
        gp.npc[35].worldX = 43 * gp.tileSize;
        gp.npc[35].worldY = 47 * gp.tileSize;

        gp.npc[36] = new NPC_Externo(gp);
        gp.npc[36].worldX = 46 * gp.tileSize;
        gp.npc[36].worldY = 43 * gp.tileSize;

        gp.npc[37] = new NPC_Rector(gp);
        gp.npc[37].worldX = 36 * gp.tileSize;
        gp.npc[37].worldY = 49 * gp.tileSize;

        gp.npc[38] = new NPC_Perfect(gp);
        gp.npc[38].worldX = 43 * gp.tileSize;
        gp.npc[38].worldY = 53 * gp.tileSize;

        gp.npc[39] = new NPC_Casimiro(gp);
        gp.npc[39].worldX = 43 * gp.tileSize;
        gp.npc[39].worldY = 58 * gp.tileSize;

        gp.npc[40] = new NPC_Millano(gp);
        gp.npc[40].worldX = 38 * gp.tileSize;
        gp.npc[40].worldY = 62 * gp.tileSize;

        gp.npc[41] = new NPC_Desanctis(gp);
        gp.npc[41].worldX = 31 * gp.tileSize;
        gp.npc[41].worldY = 60 * gp.tileSize;

        gp.npc[42] = new NPC_Ferrando(gp);
        gp.npc[42].worldX = 31 * gp.tileSize;
        gp.npc[42].worldY = 55 * gp.tileSize;
    }

    public void activateChapter3() {

        //npc
        gp.npc[43] = new NPC_Pedro(gp);
        gp.npc[43].worldX = 28 * gp.tileSize;
        gp.npc[43].worldY = 47 * gp.tileSize;

        gp.npc[44] = new NPC_Consuelo(gp);
        gp.npc[44].worldX = 31 * gp.tileSize;
        gp.npc[44].worldY = 47 * gp.tileSize;

        gp.npc[45] = new NPC_Maximo(gp);
        gp.npc[45].worldX = 32 * gp.tileSize;
        gp.npc[45].worldY = 34 * gp.tileSize;

        //letter
        gp.obj[0] = new OBJ_Draft(gp);
        gp.obj[0].worldX = 17 * gp.tileSize;
        gp.obj[0].worldY = 32 * gp.tileSize;

        //obj
        gp.obj[1] = new OBJ_Scalpel(gp);
        gp.obj[1].worldX = 65 * gp.tileSize;
        gp.obj[1].worldY = 82 * gp.tileSize;

        gp.obj[2] = new OBJ_Mirror(gp);
        gp.obj[2].worldX = 65 * gp.tileSize;
        gp.obj[2].worldY = 72 * gp.tileSize;

        gp.obj[3] = new OBJ_DriedFlower(gp);
        gp.obj[3].worldX = 62 * gp.tileSize;
        gp.obj[3].worldY = 73 * gp.tileSize;

        gp.obj[4] = new OBJ_Rosary(gp);
        gp.obj[4].worldX = 57 * gp.tileSize;
        gp.obj[4].worldY = 80 * gp.tileSize;

        gp.obj[5] = new OBJ_Portrait(gp);
        gp.obj[5].worldX = 55 * gp.tileSize;
        gp.obj[5].worldY = 82 * gp.tileSize;

        gp.obj[6] = new OBJ_ScrapMetal(gp);
        gp.obj[6].worldX = 51 * gp.tileSize;
        gp.obj[6].worldY = 70 * gp.tileSize;

        gp.obj[7] = new OBJ_EmptyPlate(gp);
        gp.obj[7].worldX = 58 * gp.tileSize;
        gp.obj[7].worldY = 74 * gp.tileSize;
    }

    public void activateQuest6() {
        for (int i = 0; i < gp.npc.length; i++) gp.npc[i] = null;
        for (int i = 0; i < gp.obj.length; i++) gp.obj[i] = null;

        gp.npc[46] = new NPC_Paciano(gp);
        gp.npc[46].worldX = 20 * gp.tileSize;
        gp.npc[46].worldY = 54 * gp.tileSize;

        gp.obj[0] = new OBJ_OldLetter(gp);
        gp.obj[0].worldX = 12 * gp.tileSize;
        gp.obj[0].worldY = 32 * gp.tileSize;

        gp.obj[1] = new OBJ_Newspaper(gp);
        gp.obj[1].worldX = 21 * gp.tileSize;
        gp.obj[1].worldY = 32 * gp.tileSize;

        gp.obj[2] = new OBJ_WornLetter(gp);
        gp.obj[2].worldX = 14 * gp.tileSize;
        gp.obj[2].worldY = 36 * gp.tileSize;

        gp.obj[3] = new OBJ_Glasses(gp);
        gp.obj[3].worldX = 35 * gp.tileSize;
        gp.obj[3].worldY = 48 * gp.tileSize;

        gp.obj[4] = new OBJ_Draft2(gp);
        gp.obj[4].worldX = 17 * gp.tileSize;
        gp.obj[4].worldY = 32 * gp.tileSize;
    }
}

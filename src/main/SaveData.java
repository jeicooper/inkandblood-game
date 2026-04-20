package main;

import java.io.Serializable;

public class SaveData implements Serializable {

    private static final long serialVersionUID = 2L;

    public int worldX, worldY;
    public int speed;
    public String direction;

    public int exp, maxExp;
    public int intellect, creativity, perception, charisma, age;

    public boolean pendingChapter2Cutscene;
    public boolean pendingQuest4Cutscene;
    public boolean pendingChapter3Cutscene;
    public boolean pendingQuest6StartCutscene;
    public boolean pendingQuest7IntroCutscene;
    public boolean pendingQuest7MidCutscene;
    public boolean pendingQuest7EndCutscene;

    public int cutsceneDelay;
    public int cutsceneDelay1;
    public int cutsceneDelay2;
    public int cutsceneDelay4;

    //PROGRESS
    public int   currentQuest;
    public int[] questState   = new int[10];

    public int  quest1Stage;
    public int  siblingsFound;
    public boolean conchaVisited;

    public int  quest2Stage;
    public int  checkpointsHit;
    public boolean courseCompleted;

    public int  quest3Stage;
    public boolean ferrandoShooed;

    public int  quest4Stage;
    public int  medalsEarned;
    public int  disciplinesCompleted;
    public boolean[] disciplineMedalEarned = new boolean[5];
    public boolean[] disciplineAnswered    = new boolean[5];

    public int  quest5Stage;
    public int  objectsCollected;
    public boolean[] manuscriptParts = new boolean[7];

    public int  quest6Stage;
    public int  q6ObjectsCollected;
    public boolean[] elFiliParts = new boolean[5];

    public int quest7Stage;

    public String[] inventoryItemNames = new String[0];

    public String currentMap;
    public int    spriteVersion;

    public int questPageNum;
}
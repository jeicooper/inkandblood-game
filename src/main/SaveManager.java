package main;

import entity.Entity;
import object.*;

import java.io.*;

public class SaveManager {

    private final GamePanel   gp;
    private final UserManager userManager;

    public SaveManager(GamePanel gp, UserManager userManager) {
        this.gp = gp;
        this.userManager = userManager;
    }

    public void save() {
        if (!userManager.isLoggedIn()) return;

        SaveData d = new SaveData();

        // Player
        d.worldX = gp.player.worldX;
        d.worldY = gp.player.worldY;
        d.speed = gp.player.speed;
        d.direction = gp.player.direction;
        d.exp = gp.player.exp;
        d.maxExp = gp.player.maxExp;
        d.intellect = gp.player.intellect;
        d.creativity = gp.player.creativity;
        d.perception = gp.player.perception;
        d.charisma = gp.player.charisma;
        d.age = gp.player.age;

        // Inventory
        d.inventoryItemNames = gp.player.inventory.stream().map(e -> e.name).toArray(String[]::new);

        // Quest manager
        QuestManager qm = gp.questManager;
        d.currentQuest = qm.currentQuest;
        d.questState = qm.questState.clone();

        d.quest1Stage = qm.quest1Stage;
        d.siblingsFound = qm.siblingsFound;
        d.conchaVisited = qm.conchaVisited;

        d.quest2Stage = qm.quest2Stage;
        d.checkpointsHit = qm.checkpointsHit;
        d.courseCompleted = qm.courseCompleted;

        d.quest3Stage = qm.quest3Stage;
        d.ferrandoShooed = qm.ferrandoShooed;

        d.quest4Stage = qm.quest4Stage;
        d.medalsEarned = qm.medalsEarned;
        d.disciplinesCompleted = qm.disciplinesCompleted;
        d.disciplineMedalEarned = qm.disciplineMedalEarned.clone();
        d.disciplineAnswered = qm.disciplineAnswered.clone();

        d.quest5Stage = qm.quest5Stage;
        d.objectsCollected = qm.objectsCollected;
        d.manuscriptParts = qm.manuscriptParts.clone();

        d.quest6Stage = qm.quest6Stage;
        d.q6ObjectsCollected = qm.q6ObjectsCollected;
        d.elFiliParts = qm.elFiliParts.clone();

        d.pendingChapter2Cutscene = qm.isPendingChapter2Cutscene();
        d.pendingQuest4Cutscene = qm.isPendingQuest4Cutscene();
        d.pendingChapter3Cutscene = qm.isPendingChapter3Cutscene();
        d.pendingQuest6StartCutscene = qm.isPendingQuest6StartCutscene();
        d.pendingQuest7IntroCutscene = qm.isPendingQuest7IntroCutscene();
        d.pendingQuest7MidCutscene   = qm.isPendingQuest7MidCutscene();
        d.pendingQuest7EndCutscene   = qm.isPendingQuest7EndCutscene();

        // UI
        d.questPageNum  = gp.ui.questPageNum;
        d.spriteVersion = detectSpriteVersion();
        d.gameCompleted = gp.cutsceneManager.isGameCompleted();

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(userManager.getSaveFile()))) {
            oos.writeObject(d);
        } catch (IOException e) {
            e.printStackTrace();
            gp.ui.showMessage("Save failed!");
        }

        gp.npcDatabase.save();
    }

    public boolean load() {
        if (!userManager.isLoggedIn()) return false;

        File f = userManager.getSaveFile();
        if (!f.exists()) return false;

        SaveData d;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            d = (SaveData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            gp.ui.showMessage("Save file corrupted — starting fresh.");
            return false;
        }

        // Stash saved player state — applied AFTER applyChapterState so spawn overrides don't win
        int    savedWorldX   = d.worldX;
        int    savedWorldY   = d.worldY;
        int    savedSpeed    = d.speed;
        String savedDir      = (d.direction != null) ? d.direction : "down";

        // Non-positional stats are safe to set now
        gp.player.exp        = d.exp;
        gp.player.maxExp     = d.maxExp;
        gp.player.intellect  = d.intellect;
        gp.player.creativity = d.creativity;
        gp.player.perception = d.perception;
        gp.player.charisma   = d.charisma;
        gp.player.age        = d.age;

        // Inventory
        gp.player.inventory.clear();
        for (String name : d.inventoryItemNames) {

            Entity item = createItemByName(name);
            if (item != null) gp.player.inventory.add(item);
        }

        // Quest
        QuestManager qm = gp.questManager;
        qm.currentQuest = d.currentQuest;
        qm.questState = d.questState.clone();

        qm.quest1Stage = d.quest1Stage;
        qm.siblingsFound = d.siblingsFound;
        qm.conchaVisited = d.conchaVisited;

        qm.quest2Stage = d.quest2Stage;
        qm.checkpointsHit = d.checkpointsHit;
        qm.courseCompleted = d.courseCompleted;

        qm.quest3Stage = d.quest3Stage;
        qm.ferrandoShooed = d.ferrandoShooed;

        qm.quest4Stage = d.quest4Stage;
        qm.medalsEarned = d.medalsEarned;
        qm.disciplinesCompleted = d.disciplinesCompleted;
        qm.disciplineMedalEarned = d.disciplineMedalEarned.clone();
        qm.disciplineAnswered = d.disciplineAnswered.clone();

        qm.quest5Stage = d.quest5Stage;
        qm.objectsCollected = d.objectsCollected;
        qm.manuscriptParts = d.manuscriptParts.clone();

        qm.quest6Stage = d.quest6Stage;
        qm.q6ObjectsCollected = d.q6ObjectsCollected;
        qm.elFiliParts = d.elFiliParts.clone();

        qm.setPendingChapter2Cutscene(d.pendingChapter2Cutscene);
        qm.setPendingQuest4Cutscene(d.pendingQuest4Cutscene);
        qm.setPendingChapter3Cutscene(d.pendingChapter3Cutscene);
        qm.setPendingQuest6StartCutscene(d.pendingQuest6StartCutscene);
        qm.setPendingQuest7IntroCutscene(d.pendingQuest7IntroCutscene);
        qm.setPendingQuest7MidCutscene(d.pendingQuest7MidCutscene);
        qm.setPendingQuest7EndCutscene(d.pendingQuest7EndCutscene);

        // UI
        gp.ui.questPageNum = d.questPageNum;

        // Apply world state in correct order
        fixQuestProgression();
        applyChapterState(qm);   // loads map + sprites + NPCs — intentionally sets a spawn pos
        removeCollectedObjects();
        removeCompletedNPCs();

        // Restore saved player position/speed/direction — overrides the hardcoded spawn above
        gp.player.worldX   = savedWorldX;
        gp.player.worldY   = savedWorldY;
        gp.player.speed    = savedSpeed;
        gp.player.direction = savedDir;

        gp.npcDatabase.load();

        // If the player had already finished the game, show the stats screen again
        // (call the internal setup directly to avoid a redundant re-save)
        if (d.gameCompleted) {
            gp.cutsceneManager.setGameCompleted(true);
            gp.cutsceneManager.restoreStatsScreen();
        }

        return true;
    }

    public boolean hasSave() {
        return userManager.isLoggedIn() && userManager.getSaveFile().exists();
    }


    private int detectSpriteVersion() {
        int q = gp.questManager.currentQuest;
        if (q >= QuestManager.QUEST5) return 3;
        if (q >= QuestManager.QUEST3) return 2;
        return 1;
    }


    private void fixQuestProgression() {
        QuestManager qm = gp.questManager;

        if (qm.currentQuest == QuestManager.QUEST1
                && qm.isQuestCompleted(QuestManager.QUEST1)) {
            qm.currentQuest = QuestManager.QUEST2;
            qm.questState[QuestManager.QUEST2] = QuestManager.STATE_ACTIVE;
            gp.ui.questPageNum = 0;
        }

        if (qm.currentQuest == QuestManager.QUEST2
                && qm.isQuestCompleted(QuestManager.QUEST2)) {
            qm.currentQuest = QuestManager.QUEST3;
            qm.questState[QuestManager.QUEST3] = QuestManager.STATE_ACTIVE;
            if (qm.quest3Stage == QuestManager.TALK_FERRANDO) qm.ferrandoShooed = false;
            gp.ui.questPageNum = 1;
            qm.setPendingChapter2Cutscene(false);
        }

        if (qm.currentQuest == QuestManager.QUEST3
                && qm.isQuestCompleted(QuestManager.QUEST3)) {
            qm.currentQuest = QuestManager.QUEST4;
            qm.questState[QuestManager.QUEST4] = QuestManager.STATE_ACTIVE;
            gp.ui.questPageNum = 1;
            qm.setPendingQuest4Cutscene(false);
        }

        if (qm.currentQuest == QuestManager.QUEST4
                && qm.isQuestCompleted(QuestManager.QUEST4)) {
            qm.currentQuest = QuestManager.QUEST5;
            qm.questState[QuestManager.QUEST5] = QuestManager.STATE_ACTIVE;
            gp.ui.questPageNum = 2;
            qm.setPendingChapter3Cutscene(false);
        }

        if (qm.currentQuest == QuestManager.QUEST5
                && qm.isQuestCompleted(QuestManager.QUEST5)) {
            qm.currentQuest = QuestManager.QUEST6;
            qm.questState[QuestManager.QUEST6] = QuestManager.STATE_ACTIVE;
            gp.ui.questPageNum = 2;
            qm.setPendingQuest6StartCutscene(false);
        }
    }

    private void applyChapterState(QuestManager qm) {

        for (int i = 0; i < gp.npc.length; i++) gp.npc[i] = null;
        for (int i = 0; i < gp.obj.length; i++) gp.obj[i] = null;

        int cq = qm.currentQuest;

        // QUEST 6
        if (cq >= QuestManager.QUEST6) {
            gp.tileM.loadMap("/maps/Chapter3.txt");
            gp.player.loadSprite3("");
            gp.aSetter.activateQuest6();
        }

        // QUEST 5
        else if (cq == QuestManager.QUEST5) {
            gp.tileM.loadMap("/maps/Chapter3.txt");
            gp.player.loadSprite3("");
            gp.aSetter.activateChapter3();
        }

        // QUEST 4
        else if (cq == QuestManager.QUEST4) {
            gp.tileM.loadMap("/maps/Chapter2.txt");
            gp.player.loadSprite2("");
            gp.aSetter.activateQuest4();
        }

        // QUEST 3
        else if (cq == QuestManager.QUEST3) {
            gp.tileM.loadMap("/maps/Chapter2.txt");
            gp.player.loadSprite2("");
            if (qm.quest3Stage >= QuestManager.TALK_PROFESSOR) {
                gp.aSetter.activateEnrollment();
            } else {
                gp.aSetter.activateChapter2();
            }
        }

        // QUEST 2
        else if (cq == QuestManager.QUEST2) {
            gp.tileM.loadMap("/maps/Chapter1.txt");

            gp.npc[0] = new entity.NPC_Teodora(gp);
            gp.npc[0].worldX = 64 * gp.tileSize;
            gp.npc[0].worldY = 24 * gp.tileSize;

            gp.npc[11] = new entity.NPC_Francisco(gp);
            gp.npc[11].worldX = 65 * gp.tileSize;
            gp.npc[11].worldY = 24 * gp.tileSize;

            gp.aSetter.activateQuest2();

            if (qm.quest2Stage >= QuestManager.MANUEL_DONE
                    && qm.quest2Stage < QuestManager.GREGORIO_DONE) {
                gp.aSetter.activateGregorio();
            }
        }

        // QUEST 1
        else {
            gp.tileM.loadMap("/maps/Chapter1.txt");
            gp.aSetter.setNPC();
        }
    }

    private void removeCollectedObjects() {
        try {
            QuestManager qm = gp.questManager;

            // QUEST 5
            if (qm.currentQuest == QuestManager.QUEST5) {

                if (qm.quest5Stage > QuestManager.FIND_LETTER) {
                    for (int i = 0; i < gp.obj.length; i++) {
                        if (gp.obj[i] == null) continue;
                        if (gp.obj[i].name.equals("Draft of Noli Me Tangere"))
                            gp.obj[i] = null;
                    }
                }

                if (qm.quest5Stage >= QuestManager.COLLECT_OBJECTS) {
                    String[] names = {
                            "Scalpel", "Mirror", "Dried Flower",
                            "Rosary", "Portrait", "Scrap Metal", "Empty Plate"
                    };
                    for (int i = 0; i < gp.obj.length; i++) {
                        if (gp.obj[i] == null) continue;
                        for (int j = 0; j < names.length; j++) {
                            if (gp.obj[i] == null) break;
                            if (gp.obj[i].name.equals(names[j]) && qm.manuscriptParts[j])
                                gp.obj[i] = null;
                        }
                    }
                }
            }

            // QUEST 6
            if (qm.currentQuest == QuestManager.QUEST6) {

                if (qm.quest6Stage > QuestManager.FIND_DRAFT) {
                    for (int i = 0; i < gp.obj.length; i++) {
                        if (gp.obj[i] == null) continue;
                        if (gp.obj[i].name.equals("Draft of El Filibusterismo"))
                            gp.obj[i] = null;
                    }
                }

                if (qm.quest6Stage >= QuestManager.COLLECT_OBJECTS_Q6) {
                    String[] names = { "Glasses", "Newspaper", "Old Letter", "Worn Letter" };
                    for (int i = 0; i < gp.obj.length; i++) {
                        if (gp.obj[i] == null) continue;
                        for (int j = 0; j < names.length; j++) {
                            if (gp.obj[i] == null) break;
                            if (j < qm.elFiliParts.length
                                    && gp.obj[i].name.equals(names[j])
                                    && qm.elFiliParts[j])
                                gp.obj[i] = null;
                        }
                    }
                }
            }

            // QUEST 2
            if (qm.currentQuest == QuestManager.QUEST2) {

                if (qm.quest2Stage == QuestManager.JOSE_WAITING) {
                    int bktInv = qm.countItem("Paint Bucket");
                    int brInv  = qm.countItem("Paintbrush");
                    int cnInv  = qm.countItem("Canvas");
                    int bktRm = 0, brRm = 0, cnRm = 0;
                    for (int i = 0; i < gp.obj.length; i++) {
                        if (gp.obj[i] == null) continue;
                        if (gp.obj[i].name.equals("Paint Bucket") && bktRm < bktInv) {
                            gp.obj[i] = null; bktRm++;
                        } else if (gp.obj[i].name.equals("Paintbrush") && brRm < brInv) {
                            gp.obj[i] = null; brRm++;
                        } else if (gp.obj[i].name.equals("Canvas") && cnRm < cnInv) {
                            gp.obj[i] = null; cnRm++;
                        }
                    }
                }

                if (qm.quest2Stage >= QuestManager.JOSE_DONE) {
                    String[] art = { "Paint Bucket", "Paintbrush", "Canvas" };
                    for (int i = 0; i < gp.obj.length; i++) {
                        if (gp.obj[i] == null) continue;
                        for (String s : art) {
                            if (gp.obj[i] == null) break;
                            if (gp.obj[i].name.equals(s)) gp.obj[i] = null;
                        }
                    }
                }

                if (qm.quest2Stage >= QuestManager.GREGORIO_DONE) {
                    for (int i = 0; i < gp.obj.length; i++) {
                        if (gp.obj[i] == null) continue;
                        if (gp.obj[i].name.equals("Quill") ||
                                gp.obj[i].name.equals("Notebook"))
                            gp.obj[i] = null;
                    }
                } else if (qm.quest2Stage == QuestManager.GREGORIO_WAITING) {
                    boolean hasQuill    = qm.countItem("Quill")    >= 1;
                    boolean hasNotebook = qm.countItem("Notebook") >= 1;
                    for (int i = 0; i < gp.obj.length; i++) {
                        if (gp.obj[i] == null) continue;
                        if (hasQuill    && gp.obj[i].name.equals("Quill"))    gp.obj[i] = null;
                        if (hasNotebook && gp.obj[i].name.equals("Notebook")) gp.obj[i] = null;
                    }
                }
            }

        } catch (NullPointerException e) {
            System.out.println("removeCollectedObjects: caught NPE — " + e.getMessage());
        }
    }

    private void removeCompletedNPCs() {
        QuestManager qm = gp.questManager;

        if (qm.currentQuest == QuestManager.QUEST2) {
            int stage = qm.quest2Stage;
            if (stage >= QuestManager.JOSE_DONE) gp.npc[12] = null;
            if (stage >= QuestManager.MANUEL_DONE) gp.npc[13] = null;
            if (stage >= QuestManager.GREGORIO_DONE) gp.npc[14] = null;
        }
    }

    // ITEMS
    private Entity createItemByName(String name) {
        switch (name) {
            case "Sa Aking Mga Kabata":
                return new OBJ_Poem(gp);
            case "Draft of Noli Me Tangere":
                return new OBJ_Draft(gp);
            case "Draft of El Filibusterismo":
                return new OBJ_Draft2(gp);
            case "Paint Bucket":
                return new OBJ_PaintBucket(gp, "/objects/red_paint");
            case "Paintbrush":
                return new OBJ_PaintBrush(gp);
            case "Canvas":
                return new OBJ_Canvas(gp);
            case "Quill":
                return new OBJ_Quil(gp);
            case "Notebook":
                return new OBJ_Notebook(gp);
            case "Boots":
                return new OBJ_Boots(gp);
            case "Sobresaliente Medal":
                return new OBJ_SobreMedal(gp);
            case "Scalpel":
                return new OBJ_Scalpel(gp);
            case "Mirror":
                return new OBJ_Mirror(gp);
            case "Dried Flower":
                return new OBJ_DriedFlower(gp);
            case "Rosary":
                return new OBJ_Rosary(gp);
            case "Portrait":
                return new OBJ_Portrait(gp);
            case "Scrap Metal":
                return new OBJ_ScrapMetal(gp);
            case "Empty Plate":
                return new OBJ_EmptyPlate(gp);
            case "Glasses":
                return new OBJ_Glasses(gp);
            case "Newspaper":
                return new OBJ_Newspaper(gp);
            case "Old Letter":
                return new OBJ_OldLetter(gp);
            case "Worn Letter":
                return new OBJ_WornLetter(gp);
            default:
                System.out.println("SaveManager: unknown item '" + name + "', skipping.");
                return null;
        }
    }
}
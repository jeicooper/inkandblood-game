package main;

import entity.Entity;
import object.*;

import java.io.*;
import java.util.ArrayList;

public class SaveManager {

    private final GamePanel   gp;
    private final UserManager userManager;

    public SaveManager(GamePanel gp, UserManager userManager) {
        this.gp          = gp;
        this.userManager = userManager;
    }

    // SAVE
    public void save() {
        if (!userManager.isLoggedIn()) return;

        SaveData d = new SaveData();

        // Player
        d.worldX    = gp.player.worldX;
        d.worldY    = gp.player.worldY;
        d.speed     = gp.player.speed;
        d.direction = gp.player.direction;
        d.exp       = gp.player.exp;
        d.maxExp    = gp.player.maxExp;
        d.intellect   = gp.player.intellect;
        d.creativity  = gp.player.creativity;
        d.perception  = gp.player.perception;
        d.charisma    = gp.player.charisma;
        d.age         = gp.player.age;

        // Inventory (names only)
        d.inventoryItemNames = gp.player.inventory.stream()
                .map(e -> e.name)
                .toArray(String[]::new);

        // Quest manager
        QuestManager qm = gp.questManager;
        d.currentQuest        = qm.currentQuest;
        d.questState          = qm.questState.clone();

        d.quest1Stage         = qm.quest1Stage;
        d.siblingsFound       = qm.siblingsFound;
        d.conchaVisited       = qm.conchaVisited;

        d.quest2Stage         = qm.quest2Stage;
        d.checkpointsHit      = qm.checkpointsHit;
        d.courseCompleted     = qm.courseCompleted;

        d.quest3Stage         = qm.quest3Stage;
        d.ferrandoShooed      = qm.ferrandoShooed;

        d.quest4Stage         = qm.quest4Stage;
        d.medalsEarned        = qm.medalsEarned;
        d.disciplinesCompleted = qm.disciplinesCompleted;
        d.disciplineMedalEarned = qm.disciplineMedalEarned.clone();
        d.disciplineAnswered    = qm.disciplineAnswered.clone();

        d.quest5Stage         = qm.quest5Stage;
        d.objectsCollected    = qm.objectsCollected;
        d.manuscriptParts     = qm.manuscriptParts.clone();

        d.quest6Stage         = qm.quest6Stage;
        d.q6ObjectsCollected  = qm.q6ObjectsCollected;
        d.elFiliParts         = qm.elFiliParts.clone();
        d.questPageNum = gp.ui.questPageNum;

        d.spriteVersion = detectSpriteVersion();

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(userManager.getSaveFile()))) {
            oos.writeObject(d);
        } catch (IOException e) {
            e.printStackTrace();
            gp.ui.showMessage("Save failed!");
        }
    }

    //LOAD
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

        // Player
        gp.player.worldX    = d.worldX;
        gp.player.worldY    = d.worldY;
        gp.player.speed     = d.speed;
        gp.player.direction = d.direction;
        gp.player.exp       = d.exp;
        gp.player.maxExp    = d.maxExp;
        gp.player.intellect   = d.intellect;
        gp.player.creativity  = d.creativity;
        gp.player.perception  = d.perception;
        gp.player.charisma    = d.charisma;
        gp.player.age         = d.age;

        // Inventory
        gp.player.inventory.clear();
        for (String name : d.inventoryItemNames) {
            Entity item = createItemByName(name);
            if (item != null) gp.player.inventory.add(item);
        }

        // Quest manager
        QuestManager qm = gp.questManager;
        qm.currentQuest        = d.currentQuest;
        qm.questState          = d.questState.clone();

        qm.quest1Stage         = d.quest1Stage;
        qm.siblingsFound       = d.siblingsFound;
        qm.conchaVisited       = d.conchaVisited;

        qm.quest2Stage         = d.quest2Stage;
        qm.checkpointsHit      = d.checkpointsHit;
        qm.courseCompleted     = d.courseCompleted;

        qm.quest3Stage         = d.quest3Stage;
        qm.ferrandoShooed      = d.ferrandoShooed;

        qm.quest4Stage         = d.quest4Stage;
        qm.medalsEarned        = d.medalsEarned;
        qm.disciplinesCompleted = d.disciplinesCompleted;
        qm.disciplineMedalEarned = d.disciplineMedalEarned.clone();
        qm.disciplineAnswered    = d.disciplineAnswered.clone();

        qm.quest5Stage         = d.quest5Stage;
        qm.objectsCollected    = d.objectsCollected;
        qm.manuscriptParts     = d.manuscriptParts.clone();

        qm.quest6Stage         = d.quest6Stage;
        qm.q6ObjectsCollected  = d.q6ObjectsCollected;
        qm.elFiliParts         = d.elFiliParts.clone();

        // UI
        gp.ui.questPageNum = d.questPageNum;

        applyChapterState(d.spriteVersion, qm.currentQuest);
        removeCollectedObjects();
        removeCompletedNPCs();

        return true;
    }

    public boolean hasSave() {
        return userManager.isLoggedIn() && userManager.getSaveFile().exists();
    }

    // HELPERS

    private int detectSpriteVersion() {
        int q = gp.questManager.currentQuest;
        if (q >= QuestManager.QUEST5) return 3;
        if (q >= QuestManager.QUEST3) return 2;
        return 1;
    }

    private void applyChapterState(int spriteVersion, int currentQuest) {

        for (int i = 0; i < gp.npc.length; i++) gp.npc[i] = null;
        for (int i = 0; i < gp.obj.length;  i++) gp.obj[i]  = null;

        if (currentQuest >= QuestManager.QUEST6) {
            gp.tileM.loadMap("/maps/Chapter3.txt");
            gp.player.loadSprite3("");
            gp.aSetter.activateQuest6();

        } else if (currentQuest == QuestManager.QUEST5) {
            gp.tileM.loadMap("/maps/Chapter3.txt");
            gp.player.loadSprite3("");
            gp.aSetter.activateChapter3();

        } else if (currentQuest == QuestManager.QUEST4) {
            gp.tileM.loadMap("/maps/Chapter2.txt");
            gp.player.loadSprite2("");
            gp.aSetter.activateQuest4();

        } else if (currentQuest == QuestManager.QUEST3) {
            gp.tileM.loadMap("/maps/Chapter2.txt");
            gp.player.loadSprite2("");

            int q3stage = gp.questManager.quest3Stage;
            if (q3stage >= QuestManager.TALK_PROFESSOR) {
                gp.aSetter.activateEnrollment();
            } else {
                gp.aSetter.activateChapter2();
            }

        } else if (currentQuest == QuestManager.QUEST2) {

            int q2stage = gp.questManager.quest2Stage;

            gp.npc[0] = new entity.NPC_Teodora(gp);
            gp.npc[0].worldX = 64 * gp.tileSize;
            gp.npc[0].worldY = 24 * gp.tileSize;

            gp.npc[11] = new entity.NPC_Francisco(gp);
            gp.npc[11].worldX = 65 * gp.tileSize;
            gp.npc[11].worldY = 24 * gp.tileSize;

            gp.aSetter.activateQuest2();

            if (q2stage >= QuestManager.MANUEL_DONE) {
                gp.aSetter.activateGregorio();
            }

        } else {
            gp.aSetter.setNPC();
        }
    }
    private Entity createItemByName(String name) {
        switch (name) {
            case "Sa Aking Mga Kabata":         return new OBJ_Poem(gp);
            case "Draft of Noli Me Tangere":    return new OBJ_Draft(gp);
            case "Draft of El Filibusterismo":  return new OBJ_Draft2(gp);
            case "Paint Bucket":                return new OBJ_PaintBucket(gp, "/objects/red_paint");
            case "Paintbrush":                  return new OBJ_PaintBrush(gp);
            case "Canvas":                      return new OBJ_Canvas(gp);
            case "Quill":                       return new OBJ_Quil(gp);
            case "Notebook":                    return new OBJ_Notebook(gp);
            case "Boots":                       return new OBJ_Boots(gp);
            case "Sobresaliente Medal":         return new OBJ_SobreMedal(gp);
            case "Scalpel":                     return new OBJ_Scalpel(gp);
            case "Mirror":                      return new OBJ_Mirror(gp);
            case "Dried Flower":                return new OBJ_DriedFlower(gp);
            case "Rosary":                      return new OBJ_Rosary(gp);
            case "Portrait":                    return new OBJ_Portrait(gp);
            case "Scrap Metal":                 return new OBJ_ScrapMetal(gp);
            case "Empty Plate":                 return new OBJ_EmptyPlate(gp);
            case "Glasses":                     return new OBJ_Glasses(gp);
            case "Newspaper":                   return new OBJ_Newspaper(gp);
            case "Old Letter":                  return new OBJ_OldLetter(gp);
            case "Worn Letter":                 return new OBJ_WornLetter(gp);
            default:
                System.out.println("SaveManager: unknown item '" + name + "', skipping.");
                return null;
        }
    }

    private void removeCollectedObjects() {
        QuestManager qm = gp.questManager;

        // QUEST 5
        if (qm.currentQuest == QuestManager.QUEST5) {

            if (qm.quest5Stage > QuestManager.FIND_LETTER) {
                for (int i = 0; i < gp.obj.length; i++) {
                    if (gp.obj[i] == null) continue;
                    if (gp.obj[i].name.equals("Draft of Noli Me Tangere")) {
                        gp.obj[i] = null;
                    }
                }
            }

            if (qm.quest5Stage >= QuestManager.COLLECT_OBJECTS) {
                String[] manuscriptNames = {
                        "Scalpel", "Mirror", "Dried Flower",
                        "Rosary", "Portrait", "Scrap Metal", "Empty Plate"
                };
                for (int i = 0; i < gp.obj.length; i++) {
                    if (gp.obj[i] == null) continue;
                    for (int j = 0; j < manuscriptNames.length; j++) {
                        if (gp.obj[i] == null) break;
                        if (gp.obj[i].name.equals(manuscriptNames[j])
                                && qm.manuscriptParts[j]) {
                            gp.obj[i] = null;
                        }
                    }
                }
            }
        }

        // QUEST 6
        if (qm.currentQuest == QuestManager.QUEST6) {

            if (qm.quest6Stage > QuestManager.FIND_DRAFT) {
                for (int i = 0; i < gp.obj.length; i++) {
                    if (gp.obj[i] == null) continue;
                    if (gp.obj[i].name.equals("Draft of El Filibusterismo")) {
                        gp.obj[i] = null;
                    }
                }
            }

            if (qm.quest6Stage >= QuestManager.COLLECT_OBJECTS_Q6) {
                String[] elFiliNames = {
                        "Glasses", "Newspaper", "Old Letter", "Worn Letter"
                };
                for (int i = 0; i < gp.obj.length; i++) {
                    if (gp.obj[i] == null) continue;
                    for (int j = 0; j < elFiliNames.length; j++) {
                        if (gp.obj[i] == null) break;
                        if (j < qm.elFiliParts.length
                                && gp.obj[i].name.equals(elFiliNames[j])
                                && qm.elFiliParts[j]) {
                            gp.obj[i] = null;
                        }
                    }
                }
            }
        }

        // QUEST 2
        if (qm.currentQuest == QuestManager.QUEST2) {

            if (qm.quest2Stage == QuestManager.JOSE_WAITING) {
                int bucketsInInventory = gp.questManager.countItem("Paint Bucket");
                int brushInInventory   = gp.questManager.countItem("Paintbrush");
                int canvasInInventory  = gp.questManager.countItem("Canvas");
                int bucketsRemoved = 0, brushRemoved = 0, canvasRemoved = 0;

                for (int i = 0; i < gp.obj.length; i++) {
                    if (gp.obj[i] == null) continue;
                    if (gp.obj[i].name.equals("Paint Bucket") && bucketsRemoved < bucketsInInventory) {
                        gp.obj[i] = null; bucketsRemoved++;
                    } else if (gp.obj[i].name.equals("Paintbrush") && brushRemoved < brushInInventory) {
                        gp.obj[i] = null; brushRemoved++;
                    } else if (gp.obj[i].name.equals("Canvas") && canvasRemoved < canvasInInventory) {
                        gp.obj[i] = null; canvasRemoved++;
                    }
                }
            }

            if (qm.quest2Stage >= QuestManager.JOSE_DONE) {
                String[] artSupplies = { "Paint Bucket", "Paintbrush", "Canvas" };
                for (int i = 0; i < gp.obj.length; i++) {
                    if (gp.obj[i] == null) continue;
                    for (String s : artSupplies) {
                        if (gp.obj[i] == null) break;
                        if (gp.obj[i].name.equals(s)) gp.obj[i] = null;
                    }
                }
            }

            if (qm.quest2Stage >= QuestManager.GREGORIO_DONE) {
                for (int i = 0; i < gp.obj.length; i++) {
                    if (gp.obj[i] == null) continue;
                    if (gp.obj[i].name.equals("Quill") ||
                            gp.obj[i].name.equals("Notebook")) {
                        gp.obj[i] = null;
                    }
                }

            } else if (qm.quest2Stage == QuestManager.GREGORIO_WAITING) {
                boolean hasQuill    = gp.questManager.countItem("Quill")    >= 1;
                boolean hasNotebook = gp.questManager.countItem("Notebook") >= 1;
                for (int i = 0; i < gp.obj.length; i++) {
                    if (gp.obj[i] == null) continue;
                    if (hasQuill    && gp.obj[i].name.equals("Quill"))    gp.obj[i] = null;
                    if (hasNotebook && gp.obj[i].name.equals("Notebook")) gp.obj[i] = null;
                }
            }
        }
    }

    private void removeCompletedNPCs() {
        QuestManager qm = gp.questManager;

        if (qm.currentQuest == QuestManager.QUEST2) {
            int stage = qm.quest2Stage;

            if (stage >= QuestManager.JOSE_DONE) {
                gp.npc[12] = null;
            }
            if (stage >= QuestManager.MANUEL_DONE) {
                gp.npc[13] = null;
            }
            if (stage >= QuestManager.GREGORIO_DONE) {
                gp.npc[14] = null;
            }
        }
    }
}
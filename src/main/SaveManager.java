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

        if (gp.cutsceneManager.isExecutionWalkActive()
                || gp.cutsceneManager.isGameCompleted()) return;

        SaveData d = new SaveData();

        d.exp        = gp.player.exp;
        d.maxExp     = gp.player.maxExp;
        d.intellect  = gp.player.intellect;
        d.creativity = gp.player.creativity;
        d.perception = gp.player.perception;
        d.charisma   = gp.player.charisma;
        d.age        = gp.player.age;
        d.speed      = gp.player.speed;
        d.direction  = "down";
        d.worldX     = 0;
        d.worldY     = 0;

        // Inventory
        d.inventoryItemNames = gp.player.inventory.stream()
                .map(e -> e.name).toArray(String[]::new);

        // Quest progress
        QuestManager qm = gp.questManager;
        d.currentQuest = qm.currentQuest;
        d.questState   = qm.questState.clone();

        d.quest1Stage   = qm.quest1Stage;
        d.siblingsFound = qm.siblingsFound;
        d.conchaVisited = qm.conchaVisited;

        d.quest2Stage     = qm.quest2Stage;
        d.checkpointsHit  = qm.checkpointsHit;
        d.courseCompleted = qm.courseCompleted;

        d.quest3Stage    = qm.quest3Stage;
        d.ferrandoShooed = qm.ferrandoShooed;

        d.quest4Stage           = qm.quest4Stage;
        d.medalsEarned          = qm.medalsEarned;
        d.disciplinesCompleted  = qm.disciplinesCompleted;
        d.disciplineMedalEarned = qm.disciplineMedalEarned.clone();
        d.disciplineAnswered    = qm.disciplineAnswered.clone();

        d.quest5Stage      = qm.quest5Stage;
        d.objectsCollected = qm.objectsCollected;
        d.manuscriptParts  = qm.manuscriptParts.clone();

        d.quest6Stage        = qm.quest6Stage;
        d.q6ObjectsCollected = qm.q6ObjectsCollected;
        d.elFiliParts        = qm.elFiliParts.clone();

        d.quest7Stage = qm.quest7Stage;

        d.pendingChapter2Cutscene    = false;
        d.pendingQuest4Cutscene      = false;
        d.pendingChapter3Cutscene    = false;
        d.pendingQuest6StartCutscene = false;
        d.pendingQuest7IntroCutscene = false;
        d.pendingQuest7MidCutscene   = false;
        d.pendingQuest7EndCutscene   = false;

        // UI
        d.questPageNum  = gp.ui.questPageNum;
        d.spriteVersion = detectSpriteVersion();

        d.gameCompleted = gp.cutsceneManager.isGameCompleted();

        // Statistics
        d.gameStartTime  = qm.gameStartTime;
        d.gameEndTime    = qm.gameEndTime;
        d.bootsStartTime = qm.bootsStartTime;
        d.bootsEndTime   = qm.bootsEndTime;
        d.firstQuizScore = qm.firstQuizScore;
        d.quizAttempts   = qm.quizAttempts;
        d.totalNPCMet    = gp.npcDatabase.getUnlockedCount();

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

        // ---- Player stats ----
        gp.player.exp        = d.exp;
        gp.player.maxExp     = d.maxExp;
        gp.player.intellect  = d.intellect;
        gp.player.creativity = d.creativity;
        gp.player.perception = d.perception;
        gp.player.charisma   = d.charisma;
        gp.player.age        = d.age;
        gp.player.speed      = (d.speed > 0) ? d.speed : 10;

        // ---- Inventory ----
        gp.player.inventory.clear();
        for (String name : d.inventoryItemNames) {
            Entity item = createItemByName(name);
            if (item != null) gp.player.inventory.add(item);
        }

        // ---- Quest state ----
        QuestManager qm = gp.questManager;
        qm.currentQuest = d.currentQuest;
        qm.questState   = d.questState.clone();

        qm.quest1Stage   = d.quest1Stage;
        qm.siblingsFound = d.siblingsFound;
        qm.conchaVisited = d.conchaVisited;

        qm.quest2Stage     = d.quest2Stage;
        qm.checkpointsHit  = d.checkpointsHit;
        qm.courseCompleted = d.courseCompleted;

        qm.quest3Stage    = d.quest3Stage;
        qm.ferrandoShooed = d.ferrandoShooed;

        qm.quest4Stage           = d.quest4Stage;
        qm.medalsEarned          = d.medalsEarned;
        qm.disciplinesCompleted  = d.disciplinesCompleted;
        qm.disciplineMedalEarned = d.disciplineMedalEarned.clone();
        qm.disciplineAnswered    = d.disciplineAnswered.clone();

        qm.quest5Stage      = d.quest5Stage;
        qm.objectsCollected = d.objectsCollected;
        qm.manuscriptParts  = d.manuscriptParts.clone();

        qm.quest6Stage        = d.quest6Stage;
        qm.q6ObjectsCollected = d.q6ObjectsCollected;
        qm.elFiliParts        = d.elFiliParts.clone();

        qm.quest7Stage = d.quest7Stage;

        // Always clear all pending cutscene flags on load
        qm.setPendingChapter2Cutscene(false);
        qm.setPendingQuest4Cutscene(false);
        qm.setPendingChapter3Cutscene(false);
        qm.setPendingQuest6StartCutscene(false);
        qm.setPendingQuest7IntroCutscene(false);
        qm.setPendingQuest7MidCutscene(false);
        qm.setPendingQuest7EndCutscene(false);

        // Statistics
        qm.gameStartTime  = d.gameStartTime;
        qm.gameEndTime    = d.gameEndTime;
        qm.bootsStartTime = d.bootsStartTime;
        qm.bootsEndTime   = d.bootsEndTime;
        qm.firstQuizScore = d.firstQuizScore;
        qm.quizAttempts   = d.quizAttempts;

        // UI
        gp.ui.questPageNum = d.questPageNum;

        // ---- Game already completed: just show stats screen ----
        if (d.gameCompleted) {
            gp.cutsceneManager.setGameCompleted(true);
            gp.cutsceneManager.restoreStatsScreen();
            gp.npcDatabase.load();
            return true;
        }

        fixQuestProgression(qm);
        applyChapterState(qm);
        removeCollectedObjects();
        removeCompletedNPCs();

        gp.npcDatabase.load();
        return true;
    }

    public boolean hasSave() {
        return userManager.isLoggedIn() && userManager.getSaveFile().exists();
    }

    private void fixQuestProgression(QuestManager qm) {

        if (qm.currentQuest == QuestManager.QUEST1
                && qm.isQuestCompleted(QuestManager.QUEST1)) {
            qm.currentQuest = QuestManager.QUEST2;
            qm.questState[QuestManager.QUEST2] = QuestManager.STATE_ACTIVE;
            gp.ui.questPageNum = 0;

        } else if (qm.currentQuest == QuestManager.QUEST2
                && qm.isQuestCompleted(QuestManager.QUEST2)) {
            qm.currentQuest = QuestManager.QUEST3;
            qm.questState[QuestManager.QUEST3] = QuestManager.STATE_ACTIVE;
            qm.ferrandoShooed = false;
            qm.quest3Stage = QuestManager.TALK_FERRANDO;
            gp.ui.questPageNum = 1;

        } else if (qm.currentQuest == QuestManager.QUEST3
                && qm.isQuestCompleted(QuestManager.QUEST3)) {
            qm.currentQuest = QuestManager.QUEST4;
            qm.questState[QuestManager.QUEST4] = QuestManager.STATE_ACTIVE;
            qm.quest4Stage = QuestManager.TALK_PROFESSOR_Q4;
            gp.ui.questPageNum = 1;

        } else if (qm.currentQuest == QuestManager.QUEST4
                && qm.isQuestCompleted(QuestManager.QUEST4)) {
            qm.currentQuest = QuestManager.QUEST5;
            qm.questState[QuestManager.QUEST5] = QuestManager.STATE_ACTIVE;
            qm.quest5Stage = QuestManager.TALK_PEDRO;
            gp.ui.questPageNum = 2;

        } else if (qm.currentQuest == QuestManager.QUEST5
                && qm.isQuestCompleted(QuestManager.QUEST5)) {
            qm.currentQuest = QuestManager.QUEST6;
            qm.questState[QuestManager.QUEST6] = QuestManager.STATE_ACTIVE;
            qm.quest6Stage = QuestManager.TALK_PACIANO_Q6;
            gp.ui.questPageNum = 2;

        } else if (qm.currentQuest == QuestManager.QUEST6
                && qm.isQuestCompleted(QuestManager.QUEST6)) {
            qm.currentQuest = QuestManager.QUEST7;
            qm.questState[QuestManager.QUEST7] = QuestManager.STATE_ACTIVE;
            qm.quest7Stage = QuestManager.Q7_TALK_GUARDIA;
            gp.ui.questPageNum = 3;
        }
    }

    private void applyChapterState(QuestManager qm) {

        for (int i = 0; i < gp.npc.length; i++) gp.npc[i] = null;
        for (int i = 0; i < gp.obj.length; i++) gp.obj[i] = null;

        int cq = qm.currentQuest;

        // QUEST 7
        if (cq == QuestManager.QUEST7) {
            gp.tileM.loadMap("/maps/Chapter4.txt");
            gp.player.loadSprite3("");
            if (qm.quest7Stage >= QuestManager.Q7_DONE) {
                gp.cutsceneManager.startQuest7EndCutscene();
                placePlayer(51, 36);
                return;
            }

            if (qm.quest7Stage >= QuestManager.Q7_TALK_JOSEPHINE) {
                gp.aSetter.activateQuest7FortSantiago();
                placePlayer(51, 36);
            } else {
                gp.aSetter.activateQuest7Intramuros();
                placePlayer(60, 29);
            }
        }

        // QUEST 6
        else if (cq == QuestManager.QUEST6) {
            gp.tileM.loadMap("/maps/Chapter3.txt");
            gp.player.loadSprite3("");
            gp.aSetter.activateQuest6();
            placePlayer(23, 35);
        }

        // QUEST 5
        else if (cq == QuestManager.QUEST5) {
            gp.tileM.loadMap("/maps/Chapter3.txt");
            gp.player.loadSprite3("");
            gp.aSetter.activateChapter3();
            placePlayer(23, 35);
        }

        // QUEST 4
        else if (cq == QuestManager.QUEST4) {
            gp.tileM.loadMap("/maps/Chapter2.txt");
            gp.player.loadSprite2("");
            gp.aSetter.activateQuest4();
            placePlayer(46, 47);
        }

        // QUEST 3
        else if (cq == QuestManager.QUEST3) {
            gp.tileM.loadMap("/maps/Chapter2.txt");
            gp.player.loadSprite2("");
            if (qm.quest3Stage >= QuestManager.TALK_PROFESSOR) {
                gp.aSetter.activateEnrollment();
                placePlayer(46, 47);
            } else {
                gp.aSetter.activateChapter2();
                placePlayer(59, 25);
            }
        }

        // QUEST 2
        else if (cq == QuestManager.QUEST2) {
            gp.tileM.loadMap("/maps/Chapter1.txt");
            gp.npc[0]  = new entity.NPC_Teodora(gp);
            gp.npc[0].worldX  = 64 * gp.tileSize;
            gp.npc[0].worldY  = 24 * gp.tileSize;
            gp.npc[11] = new entity.NPC_Francisco(gp);
            gp.npc[11].worldX = 65 * gp.tileSize;
            gp.npc[11].worldY = 24 * gp.tileSize;
            gp.aSetter.activateQuest2();
            if (qm.quest2Stage >= QuestManager.MANUEL_DONE
                    && qm.quest2Stage < QuestManager.GREGORIO_DONE) {
                gp.aSetter.activateGregorio();
            }
            placePlayer(64, 18);
        }

        // QUEST 1
        else {
            gp.tileM.loadMap("/maps/Chapter1.txt");
            gp.aSetter.setNPC();
            placePlayer(64, 18);
        }
    }

    private void placePlayer(int tileX, int tileY) {
        gp.player.worldX    = tileX * gp.tileSize;
        gp.player.worldY    = tileY * gp.tileSize;
        gp.player.direction = "down";
    }

    private void removeCollectedObjects() {
        try {
            QuestManager qm = gp.questManager;

            // Quest 5
            if (qm.currentQuest == QuestManager.QUEST5) {
                if (qm.quest5Stage > QuestManager.FIND_LETTER) {
                    for (int i = 0; i < gp.obj.length; i++)
                        if (gp.obj[i] != null && gp.obj[i].name.equals("Draft of Noli Me Tangere"))
                            gp.obj[i] = null;
                }
                if (qm.quest5Stage >= QuestManager.COLLECT_OBJECTS) {
                    String[] names = {"Scalpel","Mirror","Dried Flower","Rosary","Portrait","Scrap Metal","Empty Plate"};
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

            // Quest 6
            if (qm.currentQuest == QuestManager.QUEST6) {
                if (qm.quest6Stage > QuestManager.FIND_DRAFT) {
                    for (int i = 0; i < gp.obj.length; i++)
                        if (gp.obj[i] != null && gp.obj[i].name.equals("Draft of El Filibusterismo"))
                            gp.obj[i] = null;
                }
                if (qm.quest6Stage >= QuestManager.COLLECT_OBJECTS_Q6) {
                    String[] names = {"Glasses","Newspaper","Old Letter","Worn Letter"};
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

            // Quest 2
            if (qm.currentQuest == QuestManager.QUEST2) {
                if (qm.quest2Stage == QuestManager.JOSE_WAITING) {
                    int bktInv = qm.countItem("Paint Bucket");
                    int brInv  = qm.countItem("Paintbrush");
                    int cnInv  = qm.countItem("Canvas");
                    int bktRm = 0, brRm = 0, cnRm = 0;
                    for (int i = 0; i < gp.obj.length; i++) {
                        if (gp.obj[i] == null) continue;
                        if      (gp.obj[i].name.equals("Paint Bucket") && bktRm < bktInv) { gp.obj[i] = null; bktRm++; }
                        else if (gp.obj[i].name.equals("Paintbrush")   && brRm  < brInv)  { gp.obj[i] = null; brRm++;  }
                        else if (gp.obj[i].name.equals("Canvas")       && cnRm  < cnInv)  { gp.obj[i] = null; cnRm++;  }
                    }
                }
                if (qm.quest2Stage >= QuestManager.JOSE_DONE) {
                    String[] art = {"Paint Bucket","Paintbrush","Canvas"};
                    for (int i = 0; i < gp.obj.length; i++) {
                        if (gp.obj[i] == null) continue;
                        for (String s : art) {
                            if (gp.obj[i] == null) break;
                            if (gp.obj[i].name.equals(s)) gp.obj[i] = null;
                        }
                    }
                }
                if (qm.quest2Stage >= QuestManager.GREGORIO_DONE) {
                    for (int i = 0; i < gp.obj.length; i++)
                        if (gp.obj[i] != null &&
                                (gp.obj[i].name.equals("Quill") || gp.obj[i].name.equals("Notebook")))
                            gp.obj[i] = null;
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
            System.out.println("removeCollectedObjects NPE: " + e.getMessage());
        }
    }

    private void removeCompletedNPCs() {
        QuestManager qm = gp.questManager;
        if (qm.currentQuest == QuestManager.QUEST2) {
            int stage = qm.quest2Stage;
            if (stage >= QuestManager.JOSE_DONE)     gp.npc[12] = null;
            if (stage >= QuestManager.MANUEL_DONE)   gp.npc[13] = null;
            if (stage >= QuestManager.GREGORIO_DONE) gp.npc[14] = null;
        }
    }

    private int detectSpriteVersion() {
        int q = gp.questManager.currentQuest;
        if (q >= QuestManager.QUEST5) return 3;
        if (q >= QuestManager.QUEST3) return 2;
        return 1;
    }

    private Entity createItemByName(String name) {
        switch (name) {
            case "Sa Aking Mga Kabata":        return new OBJ_Poem(gp);
            case "Draft of Noli Me Tangere":   return new OBJ_Draft(gp);
            case "Draft of El Filibusterismo": return new OBJ_Draft2(gp);
            case "Paint Bucket":               return new OBJ_PaintBucket(gp, "/objects/red_paint");
            case "Paintbrush":                 return new OBJ_PaintBrush(gp);
            case "Canvas":                     return new OBJ_Canvas(gp);
            case "Quill":                      return new OBJ_Quil(gp);
            case "Notebook":                   return new OBJ_Notebook(gp);
            case "Boots":                      return new OBJ_Boots(gp);
            case "Medal":                      return new OBJ_SobreMedal(gp);
            case "Scalpel":                    return new OBJ_Scalpel(gp);
            case "Mirror":                     return new OBJ_Mirror(gp);
            case "Dried Flower":               return new OBJ_DriedFlower(gp);
            case "Rosary":                     return new OBJ_Rosary(gp);
            case "Portrait":                   return new OBJ_Portrait(gp);
            case "Scrap Metal":                return new OBJ_ScrapMetal(gp);
            case "Empty Plate":                return new OBJ_EmptyPlate(gp);
            case "Glasses":                    return new OBJ_Glasses(gp);
            case "Newspaper":                  return new OBJ_Newspaper(gp);
            case "Old Letter":                 return new OBJ_OldLetter(gp);
            case "Worn Letter":                return new OBJ_WornLetter(gp);
            case "Mi Ultimo Adios":            return new OBJ_MiUltimoAdios(gp);
            default:
                System.out.println("SaveManager: unknown item '" + name + "', skipping.");
                return null;
        }
    }
}
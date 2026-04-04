package main;

import entity.Entity;
import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable {

    //SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 3 ;

    public final int tileSize = (originalTileSize * scale);

    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;

    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    //WORLD SETTINGS
    public final int maxWorldCol = 110;
    public final int maxWorldRow = 108;

    //FULL SCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;

    BufferedImage temporaryScreen;
    Graphics2D g2;

    public boolean fullscreenOn = false;

    //FPS
    int fps = 60;

    //SYSTEM
    TileManager tileM = new TileManager(this);
    public KeyHandler keyP = new KeyHandler(this);
    Sound music = new Sound();
    Sound sound = new Sound();

    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Config config = new Config(this);
    public QuestManager questManager = new QuestManager(this);
    public CutsceneManager cutsceneManager = new CutsceneManager(this);

    Thread gameThread;


    //ENTITY AND OBJECT
    public Player player = new Player(this,keyP);
    public Entity obj[] = new Entity[99];
    public Entity npc [] = new Entity[50];
    public Entity talkingTo = null;

    ArrayList<Entity> entityList = new ArrayList<>();
    private final Comparator<Entity> entitySorter = Comparator.comparingInt(e -> e.worldY);

    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionState = 5;
    public final int questState = 6;
    public final int cutsceneState = 7;


    public GamePanel(){

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyP);
        this.setFocusable(true);
    }

    public void setupGame(){

        aSetter.setObject();
        aSetter.setNPC();
//      playMusic(0);

        questManager.init();
        gameState = titleState;

        temporaryScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)temporaryScreen.getGraphics();

        if (fullscreenOn == true){
            setFullScreen();
        }

    }

    public void setFullScreen(){

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        gd.setFullScreenWindow(Main.window);

        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();

    }

    public void startGameThread(){

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000/fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null){

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime)/ drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;


            if (delta >=1){
                update();
                drawToTempScreen();
                drawToScreen();

                delta--;
                drawCount++;
            }

            else {
                try {
                    long remaining = (long)((1 - delta) * drawInterval / 1000000);
                    if (remaining > 1) Thread.sleep(remaining - 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (timer >= 1000000000){
//                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }



        }
    }

    public void update(){

        if (gameState == playState){
            //PLAYER
            player.update();

            //NPC
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null){
                    npc[i].update();
                }
            }

            questManager.update();
        }
        if (gameState == pauseState){
            //
        }
        if (gameState == cutsceneState){
            cutsceneManager.update();
        }

    }
    public void drawToTempScreen() {
        //DEBUG
        long drawStart = 0;
        if (keyP.checkDrawTime == true) {
            drawStart = System.nanoTime();
        }

        if (gameState == titleState) {
            ui.draw(g2);
        }
        else if (gameState == cutsceneState) {
            cutsceneManager.draw(g2);
        }

        //OTHERS
        else {

            //TILES
            tileM.draw(g2);

            //ADD ENTITIES
            entityList.add(player);
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    entityList.add(npc[i]);
                }
            }

            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    entityList.add(obj[i]);
                }
            }

            //SORT ENTITIES
            Collections.sort(entityList, entitySorter);

            //DRAW ENTITIES
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }

            //EMPTY ENTITY LIST
            entityList.clear();

            if (questManager.isQuestActive(QuestManager.QUEST1)) {
                drawDeliveryZone(g2);
            }

            if (questManager.isQuestActive(QuestManager.QUEST2) &&
                    questManager.quest2Stage == QuestManager.MANUEL_RUNNING) {
                drawCheckpoints(g2);
            }

            //QUEST ARROW
            drawQuestArrow(g2);

            //UI
            ui.draw(g2);

            //DEBUGGING
            if (keyP.checkDrawTime == true) {

                long drawEnd = System.nanoTime();
                long passed = drawEnd - drawStart;
                g2.setColor(Color.white);
                g2.drawString("Draw Time: " + passed, 10, 400);
                System.out.println("Draw Time: " + passed);
            }
        }
    }

    private void drawDeliveryZone(Graphics2D g2) {

        int screenX = questManager.deliveryWorldX - player.worldX + player.screenX;
        int screenY = questManager.deliveryWorldY - player.worldY + player.screenY;
        int r       = questManager.deliveryRadius;

        // Only draw if on screen
        if (screenX + r < 0 || screenX - r > screenWidth ||
                screenY + r < 0 || screenY - r > screenHeight) return;

        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
        g2.setColor(new Color(255, 220, 50));
        g2.fillOval(screenX - r, screenY - r, r * 2, r * 2);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2.setColor(new Color(255, 200, 0));
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(screenX - r, screenY - r, r * 2, r * 2);
        g2.setComposite(old);
    }

    private void drawCheckpoints(Graphics2D g2) {
        if (questManager.checkpoints == null) return;

        for (int i = 0; i < questManager.TOTAL_CHECKPOINTS; i++) {
            if (questManager.checkpointHit[i]) continue;

            int screenX = questManager.checkpoints[i][0] - player.worldX + player.screenX;
            int screenY = questManager.checkpoints[i][1] - player.worldY + player.screenY;
            int r       = questManager.CHECKPOINT_RADIUS;

            if (screenX + r < 0 || screenX - r > screenWidth ||
                    screenY + r < 0 || screenY - r > screenHeight) continue;

            Composite old = g2.getComposite();

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
            g2.setColor(new Color(50, 200, 255));
            g2.fillOval(screenX - r, screenY - r, r * 2, r * 2);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g2.setColor(new Color(0, 180, 255));
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(screenX - r, screenY - r, r * 2, r * 2);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            g2.setColor(Color.white);
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.drawString(String.valueOf(i + 1), screenX - 6, screenY + 7);

            g2.setComposite(old);
        }
    }

    private void drawQuestArrow(Graphics2D g2) {
        java.util.List<entity.Entity> targets = getQuestTargets();
        if (targets.isEmpty()) return;

        g2.setFont(new Font("Arial", Font.BOLD, 22));
        FontMetrics fm = g2.getFontMetrics();
        int bob = (int)(Math.sin(System.currentTimeMillis() / 300.0) * 4);

        for (entity.Entity target : targets) {
            int aw = fm.stringWidth("v");
            int ah = fm.getAscent();
            int screenX = target.worldX - player.worldX + player.screenX + (tileSize / 2) - (aw / 2);
            int screenY = target.worldY - player.worldY + player.screenY - ah +10;

            if (screenX < 0 || screenX > screenWidth || screenY < 0 || screenY > screenHeight) continue;

            g2.setColor(new Color(0, 0, 0, 150));
            g2.drawString("v", screenX + 1, screenY + bob + 1);
            g2.setColor(new Color(255, 220, 0));
            g2.drawString("v", screenX, screenY + bob);
        }
    }

    private java.util.List<entity.Entity> getQuestTargets() {
        java.util.List<entity.Entity> targets = new java.util.ArrayList<>();

        // Quest 1
        if (questManager.isQuestActive(QuestManager.QUEST1)) {
            if (questManager.quest1Stage == QuestManager.QUEST1_NOT_STARTED) {
                if (npc[0] != null) targets.add(npc[0]); // Teodora
            } else {
                // all siblings not yet following
                for (int i = 0; i < npc.length; i++) {
                    if (npc[i] instanceof entity.NPC_Sibling) {
                        entity.NPC_Sibling s = (entity.NPC_Sibling) npc[i];
                        if (!s.isFollowing) targets.add(s);
                    }
                }
            }
        }

        // Quest 2
        if (questManager.isQuestActive(QuestManager.QUEST2)) {
            int stage = questManager.quest2Stage;
            if (stage == QuestManager.JOSE_INACTIVE || stage == QuestManager.JOSE_WAITING) {
                if (npc[12] != null) targets.add(npc[12]); // Uncle Jose
            } else if (stage == QuestManager.JOSE_DONE || stage == QuestManager.MANUEL_RUNNING) {
                if (npc[13] != null) targets.add(npc[13]); // Uncle Manuel
            } else if (stage == QuestManager.MANUEL_DONE || stage == QuestManager.GREGORIO_WAITING) {
                if (npc[14] != null) targets.add(npc[14]); // Uncle Gregorio
            }
        }

        // Quest 3
        if (questManager.isQuestActive(QuestManager.QUEST3)) {
            int stage = questManager.quest3Stage;
            if (stage == QuestManager.TALK_FERRANDO
                    || stage == QuestManager.TALK_FERRANDO_REWARD) {
                if (npc[15] != null) targets.add(npc[15]);
            } else if (stage == QuestManager.TALK_BURGOS) {
                if (npc[16] != null) targets.add(npc[16]);
            } else if (stage == QuestManager.TALK_PROFESSOR) {
                if (npc[17] != null) targets.add(npc[17]);
            } else if (stage == QuestManager.TALK_STUDENT
                    || stage == QuestManager.QUIZ_FAILED) {
                if (npc[18] != null) targets.add(npc[18]);
            }
        }

        // Quest 4
        if (questManager.isQuestActive(QuestManager.QUEST4)) {
            int stage = questManager.quest4Stage;

            if (stage == QuestManager.TALK_PROFESSOR_Q4) {
                if (npc[20] != null) targets.add(npc[20]); // Professor

            } else if (stage == QuestManager.TALK_MARIANO) {
                if (npc[21] != null) targets.add(npc[21]); // Mariano

            } else if (stage == QuestManager.TALK_RECTOR
                    || stage == QuestManager.TALK_RECTOR_END) {
                if (npc[22] != null) targets.add(npc[22]); // Rector

            } else if (stage == QuestManager.DISCIPLINES_ACTIVE) {
                // Arrow over every judge that hasn't been answered yet
                int[] judgeSlots = { 23, 24, 25, 26, 27 };
                for (int i = 0; i < judgeSlots.length; i++) {
                    if (!questManager.disciplineAnswered[i] && npc[judgeSlots[i]] != null) {
                        targets.add(npc[judgeSlots[i]]);
                    }
                }
            }
        }

        return targets;
    }

    public void drawToScreen(){

        Graphics g = getGraphics();
        g.drawImage(temporaryScreen, 0,0,screenWidth2, screenHeight2, null);
        g.dispose();

    }

    public void playMusic(int i){

        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(){

        music.stop();
    }

    public void playSE(int i){

        sound.setFile(i);
        sound.play();
    }

}
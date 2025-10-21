package main;

import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    //SCREEN SETTINGS
    final int originalTileSize = 32;
    final int scale = 2 ;

    public final int tileSize = (originalTileSize * scale);

    public final int maxScreenCol = 20;
    public final int maxScreenRow = 11;

    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    //WORLD SETTINGS
    public final int maxWorldCol = 500;
    public final int maxWorldRow = 500;

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
    Thread gameThread;


    //ENTITY AND OBJECT
    public Player player = new Player(this,keyP);
    public SuperObject obj[] = new SuperObject[10];
    public Entity npc [] = new Entity[50];

    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;






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

        gameState = titleState;
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

                repaint();

                delta--;
                drawCount++;
            }

            if (timer >= 1000000000){
                System.out.println("FPS: " + drawCount);
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
            
        }
        if (gameState == pauseState){
            //
        }


    }

    public void paintComponent(Graphics g){


        Graphics2D g2 = (Graphics2D)g;
        super.paintComponent(g);

        //DEBUG
        long drawStart = 0;
        if (keyP.checkDrawTime == true){
            drawStart = System.nanoTime();
        }

        //TITLE SCREEN
        if (gameState == titleState){
            ui.draw(g2);

        }

        //OTHERS
        else {

            //TILES
            tileM.draw(g2);

            //OBJECTS
            for (int i = 0; i < obj.length; i++) {
                if(obj [i] != null){
                    obj[i].draw(g2, this);
                }
            }

            //NPC
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].draw(g2);
                }
            }

            //PLAYER
            player.draw(g2);

            //UI
            ui.draw(g2);
        }

        //DEBUGGING
        if (keyP.checkDrawTime == true){

            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: " + passed, 10, 400);
            System.out.println("Draw Time: " + passed);
        }


        g2.dispose();
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

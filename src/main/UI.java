package main;
import object.OBJ_Book;
import object.OBJ_Heart;
import object.OBJ_Quil;
import object.SuperObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font maruMonica;

    //BUFFERED IMAGES FOR OBJECT
    //  BufferedImage quilImage;
    BufferedImage heart_full, heart_half, heart_blank;

    public boolean messageOn = false;
    public String message = "";
    int msgCounter = 0;
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0;

    public BufferedImage titleBackground;

    public BufferedImage cutsceneBG;

    public UI (GamePanel gp){
        this.gp = gp;

        try {

            InputStream is = getClass().getResourceAsStream("/fonts/x12y16pxMaruMonica.ttf");

            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);

        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            titleBackground = ImageIO.read(getClass().getResource("/images/introduction.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            cutsceneBG = ImageIO.read(getClass().getResourceAsStream("/images/cutscenebg.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

//        OBJ_Quil quill = new OBJ_Quil(gp);
//        quilImage = quill.image;

        SuperObject heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;




    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2){

        this.g2 = g2;
        g2.setFont(maruMonica);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.white);

        //TITLE SCREEN
        if (gp.gameState == gp.titleState){
            drawTitleScreen();
        }

        //PLAY STATE
        if (gp.gameState == gp.playState){

            drawPlayerLife();
        }

        //PAUSE STATE
        if (gp.gameState == gp.pauseState ){

            drawPlayerLife();
            drawPauseScreen();
        }

        //DIAOGUE STATE
        if (gp.gameState == gp.dialogueState) {

            drawPlayerLife();
            drawDialogueScreen();
        }

//        g2.drawImage(quilImage, gp.tileSize/3, gp.tileSize/3, gp.tileSize, gp.tileSize, null);
//        g2.drawString("x "+ gp.player.hasQuil, 100, 65);

    }
    public void drawPlayerLife(){

        gp.player.life = 10;

        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;

        //BLANK HEART
        while (i < gp.player.maxLife/2){

            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.tileSize;

        }

        // RESET
        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;

        //CURRENT HEART
        while (i < gp.player.life){
            g2.drawImage(heart_half, x, y, null);
            i++;

            if (i < gp.player.life){
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }
    }

    public void drawTitleScreen(){

        if (titleScreenState == 0) {

            g2.drawImage(titleBackground, 0, 0, gp.screenWidth, gp.screenHeight, null);

            //TITLENAME
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,97F));
            String text = "Ink & Blood: Rizal's Adventure";
            int x = getXforCenter(text);
            int y = gp.tileSize*2;

            //SHADOW
            g2.setColor(new Color(0,0,0, 150));
            g2.drawString(text, x+4, y+5);

            //TEXT COLOR
            g2.setColor(new Color(0,0,0));
            g2.drawString(text, x, y);

            //IMAGE
            x = gp.screenWidth/2 - (gp.tileSize*2)/2;
            y += gp.tileSize*1;
            g2.drawImage(gp.player.down3, x, y, gp.tileSize*2, gp.tileSize*2, null);

            //MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));

            text = "NEW GAME";
            x = getXforCenter(text);
            y += gp.tileSize*3;
            g2.drawString(text, x, y);
            if (commandNum == 0){
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "LOAD GAME";
            x = getXforCenter(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 1){
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "CREDITS";
            x = getXforCenter(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 2){
                g2.drawString(">", (int) (x-(gp.tileSize*1.5)), y);
            }

            text = "OPTIONS";
            x = getXforCenter(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 3){
                g2.drawString(">", (int) (x-(gp.tileSize*1.5)), y);
            }

            text = "QUIT";
            x = getXforCenter(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 4){
                g2.drawString(">", x-(gp.tileSize*2), y);
            }
        }

        //CUTSCENE SCREEN
        else if (titleScreenState == 1) {

            g2.drawImage(cutsceneBG, 0, 0, gp.screenWidth, gp.screenHeight, null);

            g2.setColor(new Color(123, 84, 47));
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,42f));

            String text = "This is a story about a great hero in \nthe Philippines";
            int x = getXforCenter(text);
            int y = gp.tileSize*4;
            g2.drawString(text, x, y);


            text = "Enter Game";
            x = getXforCenter(text);
            y += gp.tileSize*3;
            g2.drawString(text, x, y);
            if (commandNum == 0){
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "Go Back";
            x = getXforCenter(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 1){
                g2.drawString(">", (int) (x-gp.tileSize *1.4), y);
            }
        }

        //CREDIT SCREEN
        else if (titleScreenState == 2) {

            g2.drawImage(cutsceneBG, 0, 0, gp.screenWidth, gp.screenHeight, null);

            g2.setColor(new Color(123, 84, 47));
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,42f));

            String text = "CREDITS";
            int x = getXforCenter(text);
            int y = gp.tileSize*3;
            g2.drawString(text, x, y);

            text = "Lead Programmer -- Joyce Orog";
            x = getXforCenter(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);

            text = "Story Director -- Miko Lamoste";
            x = getXforCenter(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);

            text = "Music & Sound Director -- Shella Dizon";
            x = getXforCenter(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);

            text = "Story Consultant -- Ms.Alyssa Repuya";
            x = getXforCenter(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);

            text = "Go Back";
            x = getXforCenter(text);
            y += gp.tileSize*2;
            g2.drawString(text, x, y);
            if (commandNum == 0){
                g2.drawString(">", x-gp.tileSize, y);
            }
        }

    }

    public void drawPauseScreen(){

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
        String text = "PAUSED";
        int x = getXforCenter(text);
        int y = gp.tileSize*6;

        g2.drawString(text, x, y);
    }

    public void drawDialogueScreen(){

        //Window
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 6;
        int width = gp.screenWidth - (gp.tileSize*4);
        int height = gp.tileSize*4;
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD,35F));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialogue.split("\n")){
            g2.drawString(line, x, y);
            y += 40;
        }

    }

    public void drawSubWindow(int x, int y, int width, int height){

        Color c = new Color(0, 0, 0, 200);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5 , width - 10, height - 10, 25, 25);
    }

    public int getXforCenter(String text){

        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }
}

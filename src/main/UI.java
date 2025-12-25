package main;
import entity.Entity;
import object.OBJ_EXP;

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
    BufferedImage full_exp, half_exp, empty_exp;

    public boolean messageOn = false;
//  public boolean gameFinished = false;

    public String message = "";
    public String currentDialogue = "";

    int msgCounter = 0;
    public int commandNum = 0;
    public int titleScreenState = 0;
    public int optionSubState = 0;

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

        Entity exp = new OBJ_EXP(gp);
            empty_exp = exp.image;
            half_exp = exp.image2;
            full_exp = exp.image3;




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

            drawPlayerExp();
        }

        //PAUSE STATE
        if (gp.gameState == gp.pauseState ){

            drawPlayerExp();
            drawPauseScreen();
        }

        //DIAOGUE STATE
        if (gp.gameState == gp.dialogueState) {

            drawPlayerExp();
            drawDialogueScreen();
        }

        //CHARACTER STATE
        if (gp.gameState == gp.characterState) {
            drawCharacterScreen();
        }

        //OPTION STATE
        if (gp.gameState == gp.optionState) {
            drawOptionScreen();
        }

        //QUEST STATE
        if (gp.gameState == gp.questState) {
            drawQuestScreen();
        }

//        g2.drawImage(quilImage, gp.tileSize/3, gp.tileSize/3, gp.tileSize, gp.tileSize, null);
//        g2.drawString("x "+ gp.player.hasQuil, 100, 65);

    }
    public void drawPlayerExp(){

        gp.player.exp = 1;

        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;

        int spacing = gp.tileSize + 6;

        //BLANK EXP
        while (i < gp.player.maxExp/2){

            g2.drawImage(empty_exp, x, y, null);
            i++;
            x += spacing;

        }

        // RESET
        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;

        //CURRENT EXP
        while (i < gp.player.exp){
            g2.drawImage(half_exp, x, y, null);
            i++;

            if (i < gp.player.exp){
                g2.drawImage(full_exp, x, y, null);
            }
            i++;
            x += spacing;
        }
    }
    public void drawTitleScreen(){

        if (titleScreenState == 0) {

            g2.drawImage(titleBackground, 0, 0, gp.screenWidth, gp.screenHeight, null);

            //TITLENAME
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,80F));
            String text = "Ink & Blood: Rizal's Adventure";
            int x = getXforCenter(text);
            int y = gp.tileSize*3;

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

            text = "QUIT";
            x = getXforCenter(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 3){
                g2.drawString(">", x-(gp.tileSize*2), y);
            }
        }

        //CUTSCENE SCREEN
        else if (titleScreenState == 1) {

            g2.drawImage(cutsceneBG, 0, 0, gp.screenWidth, gp.screenHeight, null);
            int tailX = gp.tileSize*18;

            g2.setColor(new Color(123, 84, 47));
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,42f));

            String text = "This is a story about a great hero in the Philippines";
            int x = getXforCenter(text);
            int y = gp.tileSize*3;
            g2.drawString(text, x, y);

            g2.setColor(Color.darkGray);
            text = "--------------------------------------------------------";
            x = getXforCenter(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);

            g2.setColor(new Color(152, 112, 44));

            text = "Movement: W A S D";
            x = gp.tileSize;
            y = gp.tileSize*5;
            g2.drawString(text, x, y);

            text = "Options: ESC";
            x = gp.tileSize;
            y += gp.tileSize;
            g2.drawString(text, x, y);

            text = "Interact: F";
            x = gp.tileSize;
            y += gp.tileSize;
            g2.drawString(text, x, y);

            text = "Pause: P";
            x = getXforAlignToRight(text,tailX);
            y = gp.tileSize*5;
            g2.drawString(text, x, y);

            text = "Stats/Inventory: C";
            x = getXforAlignToRight(text, tailX);
            y += gp.tileSize;
            g2.drawString(text, x, y);

            text = "Confirm: Enter";
            x = getXforAlignToRight(text, tailX);
            y += gp.tileSize;
            g2.drawString(text, x, y);

            text = "Quest Panel: E";
            x = getXforCenter(text);
            y += gp.tileSize;
            g2.drawString(text, x,y);

            g2.setColor(Color.darkGray);
            text = "--------------------------------------------------------";
            x = getXforCenter(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);

            g2.setColor(new Color(123, 84, 47));
            text = "Enter Game";
            x = getXforCenter(text);
            y += gp.tileSize;
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
            int tailX = gp.tileSize*18;

            g2.setColor(new Color(123, 84, 47));
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,48f));

            String text = "INK AND BLOOD";
            int x = getXforCenter(text);
            int y = gp.tileSize*2;
            g2.drawString(text, x, y);

            g2.setColor(Color.darkGray);
            text = "----------------------------------------------------";
            x = getXforCenter(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);

            //LEFT SIDE
            g2.setColor(new Color(123, 84, 47));
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,35f));
            text = "SCRIPT DIRECTOR";
            x = gp.tileSize*2;
            y += gp.tileSize;
            g2.drawString(text, x, y);

            text = "ART AND GRAPHICS";
            x = gp.tileSize*2;
            y += gp.tileSize;
            g2.drawString(text, x, y);

            text = "SOUND DESIGNER";
            x = gp.tileSize*2;
            y += gp.tileSize*2;
            g2.drawString(text, x, y);

            text = "MUSIC COMPOSER";
            x = gp.tileSize*2;
            y += gp.tileSize;
            g2.drawString(text, x, y);


            //RIGHT SIDE
            text = "Miko Lamoste";
            x = getXforAlignToRight(text,tailX);
            y = gp.tileSize*4;
            g2.drawString(text, x, y);

            text = "Shella Dizon";
            x = getXforAlignToRight(text, tailX);
            y += gp.tileSize*3;
            g2.drawString(text, x, y);

            text = "Next Page";
            x = getXforCenter(text);
            y += gp.tileSize*3;
            g2.drawString(text, x, y);
            if (commandNum == 1){
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "Go Back";
            x = getXforCenter(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 0){
                g2.drawString(">", x-gp.tileSize, y);
            }


        }

        //CREDITS NEXT PAGE
        else if (titleScreenState == 3) {
            g2.drawImage(cutsceneBG, 0, 0, gp.screenWidth, gp.screenHeight, null);
            int tailX = gp.tileSize*18;

            g2.setColor(new Color(123, 84, 47));
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,48f));

            String text = "INK AND BLOOD";
            int x = getXforCenter(text);
            int y = gp.tileSize*2;
            g2.drawString(text, x, y);

            g2.setColor(Color.darkGray);
            text = "----------------------------------------------------";
            x = getXforCenter(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);

            //LEFT SIDE
            g2.setColor(new Color(123, 84, 47));
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,35f));
            text = "GAME DIRECTOR";
            x = gp.tileSize*2;
            y += gp.tileSize;
            g2.drawString(text, x, y);

            text = "PROGRAM DEVELOPMENT";
            x = gp.tileSize*2;
            y += gp.tileSize;
            g2.drawString(text, x, y);

            text = "STORY CONSULTANT";
            x = gp.tileSize*2;
            y += gp.tileSize*2;
            g2.drawString(text, x, y);

            text = "DEVELOPMENT CONSULTANT";
            x = gp.tileSize*2;
            y += gp.tileSize*2;
            g2.drawString(text, x, y);

            //RIGHT SIDE
            text = "Joyce Orog";
            x = getXforAlignToRight(text,tailX);
            y = gp.tileSize*4;
            g2.drawString(text, x, y);

            text = "Alyssa Repuya";
            x = getXforAlignToRight(text, tailX);
            y += gp.tileSize*3;
            g2.drawString(text, x, y);

            text = "Joshua Penaranda";
            x = getXforAlignToRight(text, tailX);
            y += gp.tileSize*2;
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

        g2.setFont(g2.getFont().deriveFont(Font.BOLD,80F));
        String text = "PAUSED";
        int x = getXforCenter(text);
        int y = gp.tileSize*6;

        g2.drawString(text, x, y);
    }
    public void drawDialogueScreen(){

        //Window
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 8;
        int width = gp.screenWidth - (gp.tileSize*4);
        int height = gp.tileSize*3;
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD,25F));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialogue.split("\n")){
            g2.drawString(line, x, y);
            y += 40;
        }
    }
    public void drawCharacterScreen(){
        //FRAME
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize*5 ;
        final int frameHeight = gp.tileSize*10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        //TEXT
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 32;

        //NAMES
        g2.drawString("Pepe", textX, textY);
        textY += lineHeight;
        g2.drawString("Age: 8 years old", textX, textY);
        textY += lineHeight;
        g2.drawString("--------------------", textX, textY);
        textY += lineHeight;
        g2.drawString("Intellect:", textX, textY);
        textY += lineHeight;
        g2.drawString("Creativity:", textX, textY);
        textY += lineHeight;
        g2.drawString("Perception:", textX, textY);
        textY += lineHeight;
        g2.drawString("Charisma:", textX, textY);
        textY += lineHeight;

        //STAT VALUES
        int tailX = (frameX + frameWidth) - 30;
        textY = frameY + gp.tileSize *3;
        String value;

        //INTELECT STAT
        value = String.valueOf(gp.player.intellect);
        textX = getXforAlignToRight(value, tailX);
        g2.drawString(value, textX,textY);
        textY += lineHeight;

        //CREATIVITY STAT
        value = String.valueOf(gp.player.creativity);
        textX = getXforAlignToRight(value, tailX);
        g2.drawString(value, textX,textY);
        textY += lineHeight;

        //PERCEPTION STAT
        value = String.valueOf(gp.player.perception);
        textX = getXforAlignToRight(value, tailX);
        g2.drawString(value, textX,textY);
        textY += lineHeight;

        //CHARISMA STAT
        value = String.valueOf(gp.player.charisma);
        textX = getXforAlignToRight(value, tailX);
        g2.drawString(value, textX,textY);
        textY += lineHeight;
    }
    public void drawOptionScreen(){
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        //SUB WINDOW
        final int frameX = gp.tileSize*6;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize*8 ;
        final int frameHeight = gp.tileSize*10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch (optionSubState) {
            case 0:
                options_top(frameX, frameY);
            break;
            case 1:
                options_notif(frameX, frameY);
            break;
            case 2:
                options_control(frameX, frameY);
            break;
            case 3:
                option_endGame(frameX, frameY);
            break;
        }

        gp.keyP.enterPressed = false;
    }

    public void drawQuestScreen(){
        //FRAME
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize*18 ;
        final int frameHeight = gp.tileSize*10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32f));

        g2.setFont(g2.getFont().deriveFont(Font.BOLD,42f));
        String text = "Quests";
        int x = getXforCenter(text);
        int y = frameY + gp.tileSize;
        g2.drawString(text, x, y);

        //1ST QUEST
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC ,32f));
        text = "“Familya Rizal” - Help Pepe find his 10 siblings.";
        x = frameX + gp.tileSize;
        y += gp.tileSize;
        g2.drawString(text, x, y);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN ,32f));
        text = " -Saturnina                -Maria";
        x = frameX + gp.tileSize;
        y += gp.tileSize/2;
        g2.drawString(text, x, y);

        text = " -Paciano                 -Josefa";
        x = frameX + gp.tileSize;
        y += gp.tileSize/2;
        g2.drawString(text, x, y);

        text = " -Narcisa                -Trinidad";
        x = frameX + gp.tileSize;
        y += gp.tileSize/2;
        g2.drawString(text, x, y);

        text = " -Olimpia                 -Solidad";
        x = frameX + gp.tileSize;
        y += gp.tileSize/2;
        g2.drawString(text, x, y);

        text = " -Lucia                       -???";
        x = frameX + gp.tileSize;
        y += gp.tileSize/2;
        g2.drawString(text, x, y);

        //2ND QUEST
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC ,32f));
        text = "“Pangako para sa Pamilya”: Assist Mother in preparing flowers";
        x = frameX + gp.tileSize;
        y += gp.tileSize;
        g2.drawString(text, x, y);

        text = "for the Virgin of Antipolo offering.";
        x = frameX + gp.tileSize;
        y += gp.tileSize/2;
        g2.drawString(text, x, y);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,32f));
        text = "- Gather 8 flowers from the family garden.";
        x = frameX + gp.tileSize;
        y += gp.tileSize/2;
        g2.drawString(text, x, y);

        text = "- Offer the flowers for the Virgin of Antipolo.";
        x = frameX + gp.tileSize;
        y += gp.tileSize/2;
        g2.drawString(text, x, y);

    }

    public void options_top(int frameX, int frameY){
        int textX;
        int textY;

        //TITLE
        String text = "Options";
        textX = getXforCenter(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX,textY);


        //SCREEN OPTIONS
        textX = frameX +gp.tileSize;
        textY += gp.tileSize*2;
        g2.drawString("Full Screen", textX, textY);
        if (commandNum == 0){
            g2.drawString(">", textX-25, textY);

            if (gp.keyP.enterPressed == true) {
                if (gp.fullscreenOn == false) {
                    gp.fullscreenOn = true;
                }
                else if (gp.fullscreenOn == true) {
                    gp.fullscreenOn = false;
                }
                optionSubState = 1;
            }

        }

        //MUSIC OPTIONS
        textX = frameX +gp.tileSize;
        textY += gp.tileSize;
        g2.drawString("Music", textX, textY);
        if (commandNum == 1){
            g2.drawString(">", textX-25, textY);
        }

        //SOUND EFFECTS OPTIONS
        textX = frameX +gp.tileSize;
        textY += gp.tileSize;
        g2.drawString("Sound Effects", textX, textY);
        if (commandNum == 2){
            g2.drawString(">", textX-25, textY);
        }

        //CONTROLS OPTIONS
        textX = frameX +gp.tileSize;
        textY += gp.tileSize;
        g2.drawString("Controls", textX, textY);
        if (commandNum == 3){
            g2.drawString(">", textX-25, textY);

            if (gp.keyP.enterPressed == true){
                optionSubState = 2;
                commandNum = 0;
            }
        }

        //EXIT
        textX = getXforCenter("Exit Game");
        textY += gp.tileSize*2;
        g2.drawString("Exit Game", textX, textY);
        if (commandNum == 4){
            g2.drawString(">", textX-25, textY);
            if (gp.keyP.enterPressed == true){
                optionSubState = 3;
                commandNum = 0;
            }
        }

        //BACK
        textX = getXforCenter("Back");
        textY += gp.tileSize;
        g2.drawString("Back", textX, textY);
        if (commandNum == 5){
            g2.drawString(">", textX-25, textY);
            if (gp.keyP.enterPressed == true){
                gp.gameState = gp.playState;
                commandNum = 0;
            }
        }

        //SCREEN CHECKBOX
        textX = frameX + (int) (gp.tileSize*4.5);
        textY = frameY + gp.tileSize*2 + 24;
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(textX, textY, 24, 24);

        if (gp.fullscreenOn == true){
            g2.fillRect(textX+3, textY+3, 18, 18);
        }

        //MUSIC
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24);
        int volumeWidth = 23 * gp.music.volumeScale;
        g2.fillRect(textX+3, textY+3, volumeWidth, 18);

        //SOUND EFFECT
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24);
        volumeWidth = 23 * gp.sound.volumeScale;
        g2.fillRect(textX+3, textY+3, volumeWidth, 18);

        gp.config.saveConfig();
    }
    public void options_notif(int frameX, int frameY){
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize*3;

        currentDialogue = "The change will take \neffect after restarting \nthe game.";

        for (String line : currentDialogue.split("\n")){
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        textY = frameY + gp.tileSize * 9;
        g2.drawString("Back", textX + (gp.tileSize*2), textY);
        if (commandNum == 0){
            g2.drawString(">", textX-25+(gp.tileSize*2), textY);
            if (gp.keyP.enterPressed == true){
                optionSubState = 0;
            }
        }
    }
    public void options_control(int frameX, int frameY){
        int textX;
        int textY;

        //TITLE
        String text = "Controls";
        textX = getXforCenter(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        //Left Side
        int leftX = frameX + gp.tileSize;
        int leftY = frameY + gp.tileSize*2;
        g2.drawString("Move", leftX, leftY); leftY += gp.tileSize;
        g2.drawString("Confirm", leftX, leftY); leftY += gp.tileSize;
        g2.drawString("Interact", leftX, leftY); leftY += gp.tileSize;
        g2.drawString("Stats/Inventory", leftX, leftY); leftY += gp.tileSize;
        g2.drawString("Pause", leftX, leftY); leftY += gp.tileSize;
        g2.drawString("Quests", leftX, leftY); leftY += gp.tileSize;
        g2.drawString("Options", leftX, leftY); leftY += gp.tileSize;

        //Right Side
        int rightX = frameX + gp.tileSize*6;
        int rightY = frameY + gp.tileSize*2;
        g2.drawString("WASD", rightX, rightY); rightY += gp.tileSize;
        g2.drawString("Enter", rightX, rightY); rightY += gp.tileSize;
        g2.drawString("F", rightX, rightY); rightY += gp.tileSize;
        g2.drawString("C", rightX, rightY); rightY += gp.tileSize;
        g2.drawString("P", rightX, rightY); rightY += gp.tileSize;
        g2.drawString("E", rightX, rightY); rightY += gp.tileSize;
        g2.drawString("ESC", rightX, rightY); rightY += gp.tileSize;

        textX = getXforCenter("Back");
        textY += gp.tileSize*8;
        g2.drawString("Back", textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX-25, textY);
            if (gp.keyP.enterPressed == true){
                optionSubState = 0;
                commandNum = 3;
            }
        }
    }
    public void option_endGame(int frameX, int frameY){

        int textX = frameX +gp.tileSize;
        int textY = frameY +gp.tileSize*3;

        currentDialogue = "Are you sure you \nwant to quit?";

        for (String line : currentDialogue.split("\n")){
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        String text = "Exit to Menu";
        textX = getXforCenter(text);
        textY = gp.tileSize * 8;
        g2.drawString(text, textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyP.enterPressed) {
                gp.gameState = gp.ui.titleScreenState = 0;
                gp.keyP.enterPressed = false;
            }
        }

        text = "Exit to Desktop";
        textX = getXforCenter(text);
        textY = gp.tileSize * 9;
        g2.drawString(text, textX, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyP.enterPressed) {
                System.exit(0);
            }
        }

        text = "Back";
        textX = getXforCenter(text);
        textY = gp.tileSize * 10;
        g2.drawString(text, textX, textY);
        if (commandNum == 2) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyP.enterPressed) {
                optionSubState = 0;
                commandNum = 0;
            }
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
    public int getXforAlignToRight(String text, int tailX){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }
}

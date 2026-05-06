package main;
import entity.Entity;
import object.OBJ_EXP;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    public Font maruMonica;

    //BUFFERED IMAGES FOR OBJECT
    BufferedImage full_exp_start, half_exp_start, empty_exp_start;
    BufferedImage full_exp_mid, half_exp_mid, empty_exp_mid;
    BufferedImage full_exp_end, half_exp_end, empty_exp_end;

    public boolean messageOn = false;
    public boolean showPoemPanel = false;

    public QuizPanel quizPanel;
    public boolean   quizPanelOpen = false;

    public String message = "";
    public String currentDialogue = "";
    public String currentSpeakerName = "";
    public String activeLetter = "";

    int msgCounter = 0;
    public int commandNum = 0;
    public int titleScreenState = 0;
    public int optionSubState = 0;
    public int newGameConfirmCursor = 0;

    public int slotCol = 0;
    public int slotRow = 0;

    public int questPageNum = 0;

    private String  pickupItemName  = "";
    private int     pickupTimer     = 0;
    private static final int PICKUP_DURATION = 120;

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


        Entity exp = new OBJ_EXP(gp);
        full_exp_start = exp.image;
        full_exp_mid = exp.image2;
        full_exp_end = exp.image3;

        half_exp_start  = exp.image4;
        half_exp_mid = exp.image5;
        half_exp_end = exp.image6;

        empty_exp_start = exp.image7;
        empty_exp_mid = exp.image8;
        empty_exp_end = exp.image9;

        quizPanel = new QuizPanel(gp, this);


    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }
    public void showPickup(String itemName) {
        pickupItemName = itemName;
        pickupTimer    = PICKUP_DURATION;
    }

    public void openQuizPanel() {
        quizPanel.open();
    }

    public void draw(Graphics2D g2){

        this.g2 = g2;
        g2.setFont(maruMonica);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.white);

        //LOG IN SCREEN
        if (gp.gameState == gp.loginState) return;

        //TITLE SCREEN
        if (gp.gameState == gp.titleState){
            drawTitleScreen();
        }

        //PLAY STATE
        if (gp.gameState == gp.playState){

            drawPlayerExp();
            drawHints();
            drawQuestHUD();

            if (messageOn){
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35F));
                g2.setColor(Color.white);
                int x = getXforCenter(message);
                g2.drawString(message, x, gp.tileSize * 8);
                msgCounter++;
                if (msgCounter > 120){
                    messageOn = false;
                    msgCounter = 0;
                }
            }

            if (showPoemPanel) {
                if (activeLetter.equals("Draft of Noli Me Tangere")) {
                    drawLetterPanel();
                } else if (activeLetter.equals("Draft of El Filibusterismo")) {
                    drawElFiliPanel();
                }else if (activeLetter.equals("Mi Ultimo Adios")) {
                    drawMiUltimoAdiosPanel();
                }else {
                    drawPoemPanel();
                }
            }

            if (quizPanelOpen) {
                quizPanel.draw(g2);
            }

            if (pickupTimer > 0) {
                pickupTimer--;

                float alpha = pickupTimer < 60 ? pickupTimer / 60f : 1f;
                Composite oldC = g2.getComposite();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

                int boxW = 220;
                int boxH = 36;
                int boxX = 14;
                int boxY = (int) (gp.screenHeight - (gp.tileSize*3.5));

                // Background
                g2.setColor(new Color(0, 0, 0, 180));
                g2.fillRoundRect(boxX, boxY, boxW, boxH, 10, 10);

                // Border
                g2.setColor(new Color(255, 210, 80));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(boxX, boxY, boxW, boxH, 10, 10);

                // Text
                g2.setFont(maruMonica.deriveFont(Font.BOLD, 20f));
                g2.setColor(Color.white);
                g2.drawString(pickupItemName, boxX + 10, boxY + 23);

                g2.setComposite(oldC);
            }
        }

        //DIAOGUE STATE
        if (gp.gameState == gp.dialogueState) {

            drawPlayerExp();
            drawQuestHUD();
            drawDialogueScreen();
        }

        //CHARACTER STATE
        if (gp.gameState == gp.characterState) {
            drawCharacterScreen();
            drawInventory();
        }

        //OPTION STATE
        if (gp.gameState == gp.optionState) {
            drawOptionScreen();
        }

        //QUEST STATE
        if (gp.gameState == gp.questState) {
            drawQuestScreen();
        }

        //CONFIRMATION
        if (gp.gameState == gp.newGameConfirmState) {
            drawNewGameConfirm();
        }

        //NPC DEX STATE
        if (gp.gameState == gp.dexState) {
            gp.npcDexUI.draw(g2);
        }


    }
    public void drawPlayerExp() {
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int spacing = gp.tileSize - 2;
        int totalSlots = gp.player.maxExp / 2;

        int fullSlots = gp.player.exp / 2;
        boolean hasHalf = (gp.player.exp % 2) == 1;

        for (int i = 0; i < totalSlots; i++) {
            // pick position type
            BufferedImage emptyImg, fullImg, halfImg;
            if (i == 0) {
                emptyImg = empty_exp_start;
                fullImg  = full_exp_start;
                halfImg  = half_exp_start;
            } else if (i == totalSlots - 1) {
                emptyImg = empty_exp_end;
                fullImg  = full_exp_end;
                halfImg  = half_exp_end;
            } else {
                emptyImg = empty_exp_mid;
                fullImg  = full_exp_mid;
                halfImg  = half_exp_mid;
            }

            // pick fill state
            BufferedImage toDraw;
            if (i < fullSlots) {
                toDraw = fullImg;
            } else if (i == fullSlots && hasHalf) {
                toDraw = halfImg;
            } else {
                toDraw = emptyImg;
            }

            g2.drawImage(toDraw, x, y, null);
            x += spacing;
        }

        drawLocationLabel();
    }
    public void drawLocationLabel() {
        int currentQ = gp.questManager.currentQuest;

        String location;
        String sublocation;

        if (currentQ <= QuestManager.QUEST2) {
            location    = " CALAMBA, LAGUNA";
        } else if (currentQ <= QuestManager.QUEST4) {
            location    = " ATENEO MUNICIPAL DE MANILA";
        } else if (currentQ <= QuestManager.QUEST6){
            location    = " EUROPE";
        } else {
            location    = " INTRAMUROS";
        }


        int labelX = gp.tileSize / 2;
        int labelY = gp.tileSize / 2 + gp.tileSize + 18;

        // Shadow
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22f));
        g2.setColor(new Color(0, 0, 0, 180));
        g2.drawString(location, labelX + 1, labelY + 1);

        g2.setColor(new Color(255, 220, 80));
        g2.drawString(location, labelX, labelY);

    }

    public void drawHints(){

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 25f));

        //LEFT
        int hintLeftX = gp.tileSize/2;
        int hintLeftY = gp.tileSize*10;

        g2.drawString("[ W A S D ]     to move", hintLeftX, hintLeftY); hintLeftY += gp.tileSize/2;
        g2.drawString("[ ENTER ]    to confirm", hintLeftX, hintLeftY); hintLeftY += gp.tileSize/2;
        g2.drawString("[ ESC ]     for options", hintLeftX, hintLeftY); hintLeftY += gp.tileSize/2;
        g2.drawString("[ C ] to view inventory", hintLeftX, hintLeftY); hintLeftY += gp.tileSize/2;

        //RIGHT
        int hintRightX = gp.tileSize * 15;
        int hintRightY = gp.tileSize * 10;

        g2.drawString("[ F ]       to interact", hintRightX, hintRightY); hintRightY += gp.tileSize/2;
        g2.drawString("[ Q ]   to view quests", hintRightX, hintRightY); hintRightY += gp.tileSize/2;
        g2.drawString("[ P ]   to save progress", hintRightX, hintRightY); hintRightY += gp.tileSize/2;

    }

    public void drawTitleScreen(){

        if (titleScreenState == 0) {

            g2.drawImage(titleBackground, 0, 0, gp.screenWidth, gp.screenHeight, null);

            //TITLENAME
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,62F));
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
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,38F));

            text = "NEW GAME";
            x = getXforCenter(text);
            y += gp.tileSize*3;

            g2.setColor(commandNum == 0 ? new Color(255, 220, 80) : new Color(0, 0, 0));
            g2.drawString(text, x, y);
            if (commandNum == 0){
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "LOAD GAME";
            x = getXforCenter(text);
            y += gp.tileSize - 10;

            g2.setColor(commandNum == 1 ? new Color(255, 220, 80) : new Color(0, 0, 0));
            g2.drawString(text, x, y);
            if (commandNum == 1){
                g2.drawString(">", x-gp.tileSize, y);
            }

            text = "CREDITS";
            x = getXforCenter(text);
            y += gp.tileSize - 10;

            g2.setColor(commandNum == 2 ? new Color(255, 220, 80) : new Color(0, 0, 0));
            g2.drawString(text, x, y);
            if (commandNum == 2){
                g2.drawString(">", (int) (x-(gp.tileSize*1.5)), y);
            }

            text = "LOG OUT";
            x = getXforCenter(text);
            y += gp.tileSize - 10;

            g2.setColor(commandNum == 3 ? new Color(255, 220, 80) : new Color(0, 0, 0));
            g2.drawString(text, x, y);
            if (commandNum == 3){
                g2.drawString(">", x-(gp.tileSize*2), y);
            }

            text = "QUIT";
            x = getXforCenter(text);
            y += gp.tileSize - 10;

            g2.setColor(commandNum == 4 ? new Color(255, 220, 80) : new Color(0, 0, 0));
            g2.drawString(text, x, y);
            if (commandNum == 4){
                g2.drawString(">", x-(gp.tileSize*2), y);
            }

            // Hint
            g2.setFont(gp.ui.maruMonica.deriveFont(Font.ITALIC, 20f));
            g2.setColor(Color.LIGHT_GRAY);
            String hint = "[ W / S ] Navigate    [ ENTER ] Select";
            g2.drawString(hint, getXforCenter(hint), gp.screenHeight - 20);
        }

        //CUTSCENE SCREEN
        else if (titleScreenState == 1) {

            g2.setColor(Color.black);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
            int tailX = gp.tileSize*18;

            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,38f));

            String text = "This is a story about a great hero in the Philippines";
            int x = getXforCenter(text);
            int y = gp.tileSize*5;
            g2.drawString(text, x, y);

            text = "Enter Game";
            x = getXforCenter(text);
            y += gp.tileSize*4;
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

        // NO SAVE FILE SCREEN
        else if (titleScreenState == 4) {

            g2.drawImage(cutsceneBG, 0, 0, gp.screenWidth, gp.screenHeight, null);

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48f));
            g2.setColor(new Color(230, 80, 80));
            String text = "No Save File Found";
            int x = getXforCenter(text);
            int y = gp.tileSize * 4;
            g2.drawString(text, x, y);

            g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 30f));
            g2.setColor(new Color(180, 180, 180));
            String sub = "Start a New Game first to create a save.";
            x = getXforCenter(sub);
            y += gp.tileSize;
            g2.drawString(sub, x, y);

            // GO BACK option
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 38f));
            g2.setColor(Color.white);
            String back = "Go Back";
            x = getXforCenter(back);
            y += gp.tileSize * 2;
            g2.drawString(back, x, y);
            if (commandNum == 0) {
                g2.drawString(">", x - gp.tileSize, y);
            }
        }

    }
    public void drawDialogueScreen(){

        //Window
        int x = gp.tileSize * 2;
        int y = gp.tileSize * 8;
        int width = gp.screenWidth - (gp.tileSize*4);
        int height = gp.tileSize*3;
        drawSubWindow(x, y, width, height);

        if (gp.keyP.enterPressed) {
            gp.keyP.enterPressed = false;
            if (gp.talkingTo != null) {
                gp.talkingTo.speak();
            }
        }

        if (currentSpeakerName != null && !currentSpeakerName.isEmpty()) {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35F));

            int nameX = x + gp.tileSize;
            int nameY = y - 15;

            // BLACK OUTLINE
            g2.setColor(Color.black);
            g2.drawString(currentSpeakerName, nameX - 1, nameY - 1);
            g2.drawString(currentSpeakerName, nameX + 1, nameY - 1);
            g2.drawString(currentSpeakerName, nameX - 1, nameY + 1);
            g2.drawString(currentSpeakerName, nameX + 1, nameY + 1);

            // GOLD TEXT ON TOP
            g2.setColor(new Color(255, 220, 80));
            g2.drawString(currentSpeakerName, nameX, nameY);
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 25F));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialogue.split("\n")) {
            String[] segments = line.split("\\*\\*");
            int drawX = x;
            for (int s = 0; s < segments.length; s++) {
                boolean isHighlight = (s % 2 == 1);
                g2.setColor(isHighlight ? new Color(255, 220, 80) : Color.white);
                g2.drawString(segments[s], drawX, y);
                drawX += g2.getFontMetrics().stringWidth(segments[s]);
            }
            y += 40;
        }

        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
        g2.setColor(new Color(200, 200, 200));
        int promptX = (gp.tileSize * 2) + (gp.screenWidth - (gp.tileSize*4)) - gp.tileSize;
        int promptY = (gp.tileSize * 8) + (gp.tileSize * 3) - 15;
        g2.drawString("[ ENTER ] / [ F ]", promptX - 80, promptY);
    }
    public void drawCharacterScreen(){
        //FRAME
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize*5 ;
        final int frameHeight = gp.tileSize*10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // LABEL
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28f));
        g2.setColor(new Color(255, 220, 80));
        String invLabel = "CHARACTER STATS";
        int labelX = frameX + (frameWidth - g2.getFontMetrics().stringWidth(invLabel)) / 2;
        g2.drawString(invLabel, labelX, frameY - 6);

        //TEXT
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 32;

        String playerName;
        int currentQ = gp.questManager.currentQuest;
        boolean q1done = gp.questManager.isQuestCompleted(main.QuestManager.QUEST1);
        boolean q2done = gp.questManager.isQuestCompleted(main.QuestManager.QUEST2);
        boolean q3done = gp.questManager.isQuestCompleted(main.QuestManager.QUEST3);
        boolean q4done = gp.questManager.isQuestCompleted(main.QuestManager.QUEST4);
        boolean q5done = gp.questManager.isQuestCompleted(main.QuestManager.QUEST5);

        if (q5done) {
            playerName = "Dr. Jose P. Rizal";
        } else if (q3done && q4done) {
            playerName = "Dr. Jose P. Rizal";
        } else if (q3done || q4done) {
            playerName = "Jose P. Rizal";
        } else if (q2done) {
            playerName = "Jose Rizal";
        } else {
            playerName = "Pepe";
        }

        //NAMES
        g2.drawString(playerName, textX, textY);
        textY += lineHeight;
        g2.drawString("Age:", textX, textY);
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
        textY = frameY + gp.tileSize + lineHeight;
        String value;

        // AGE
        value = String.valueOf(gp.player.age);
        textX = getXforAlignToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight * 2;

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

    public void drawInventory(){

        //FRAME
        int frameX = gp.tileSize * 13;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 5;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // LABEL
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28f));
        g2.setColor(new Color(255, 220, 80));
        String invLabel = "INVENTORY";
        int labelX = frameX + (frameWidth - g2.getFontMetrics().stringWidth(invLabel)) / 2;
        g2.drawString(invLabel, labelX, frameY - 6);

        //SLOTS
        int totalGridWidth = gp.tileSize * 5;
        int totalGridHeight = gp.tileSize * 4;

        final int slotXstart = frameX + (frameWidth - totalGridWidth) / 2;
        final int slotYstart = frameY + (frameHeight - totalGridHeight) / 2;
        int slotX = slotXstart;
        int slotY = slotYstart;

        // DRAW EMPTY SLOT OUTLINES (all 20 slots)
        g2.setStroke(new BasicStroke(1.5f));
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 5; col++) {
                int sx = slotXstart + col * gp.tileSize;
                int sy = slotYstart + row * gp.tileSize;
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(sx, sy, gp.tileSize, gp.tileSize, 6, 6);
                g2.setColor(new Color(255, 255, 255, 80));
                g2.drawRoundRect(sx, sy, gp.tileSize, gp.tileSize, 6, 6);
            }
        }

        // DRAW PLAYER ITEM
        slotX = slotXstart;
        slotY = slotYstart;
        for (int i = 0; i < gp.player.inventory.size(); i++) {

            g2.drawImage(gp.player.inventory.get(i).image, slotX, slotY, gp.tileSize, gp.tileSize, null);

            slotX += gp.tileSize;

            if (i == 4 || i == 9 || i == 14){
                slotX = slotXstart;
                slotY += gp.tileSize;
            }
        }

        //CURSOR
        int cursorX = slotXstart + (gp.tileSize * slotCol);
        int cursorY = slotYstart + (gp.tileSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        //DESC FRAME
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize * 4;

        int textX = dFrameX + 20;
        int textY = dFrameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(28F));

        int itemIndex = getItemIndexOnSlot();

        if (itemIndex < gp.player.inventory.size()){

            if (itemIndex < gp.player.inventory.size()){

                drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);

                Entity selectedItem = gp.player.inventory.get(itemIndex);
                for (String line : selectedItem.description.split("\n")) {
                    g2.drawString(line, textX, textY);
                    textY += 32;
                }
            }
        }
    }

    public void drawNewGameConfirm() {
        // dim background
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int panelW = gp.tileSize * 12;
        int panelH = gp.tileSize * 7;
        int panelX = gp.screenWidth  / 2 - panelW / 2;
        int panelY = gp.screenHeight / 2 - panelH / 2;
        drawSubWindow(panelX, panelY, panelW, panelH);

        int cx = panelX + panelW / 2;

        // title
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32f));
        g2.setColor(new Color(230, 80, 80));
        String title = "Start New Game?";
        g2.drawString(title, cx - getXWidth(title) / 2, panelY + gp.tileSize);

        // warning — only shown if save exists
        if (gp.saveManager.hasSave()) {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22f));
            g2.setColor(new Color(255, 180, 60));
            String warn1 = "You have a saved game.";
            g2.drawString(warn1, cx - getXWidth(warn1) / 2, panelY + gp.tileSize + 40);

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20f));
            g2.setColor(new Color(200, 200, 200));
            String warn2 = "Starting a new game will permanently";
            String warn3 = "delete your current progress.";
            g2.drawString(warn2, cx - getXWidth(warn2) / 2, panelY + gp.tileSize + 68);
            g2.drawString(warn3, cx - getXWidth(warn3) / 2, panelY + gp.tileSize + 92);
        } else {
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 22f));
            g2.setColor(new Color(200, 200, 200));
            String msg = "Begin your adventure as Jose Rizal.";
            g2.drawString(msg, cx - getXWidth(msg) / 2, panelY + gp.tileSize + 54);
        }

        // Yes / No options
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 34f));
        int optY = panelY + gp.tileSize * 5;

        g2.setColor(newGameConfirmCursor == 0 ? new Color(255, 220, 80) : Color.white);
        String yes = "Yes, start new game";
        g2.drawString(yes, cx - getXWidth(yes) / 2, optY);
        if (newGameConfirmCursor == 0) {
            g2.drawString(">", cx - getXWidth(yes) / 2 - gp.tileSize, optY);
        }

        g2.setColor(newGameConfirmCursor == 1 ? new Color(255, 220, 80) : Color.white);
        String no = "Go back";
        g2.drawString(no, cx - getXWidth(no) / 2, optY + gp.tileSize);
        if (newGameConfirmCursor == 1) {
            g2.drawString(">", cx - getXWidth(no) / 2 - gp.tileSize, optY + gp.tileSize);
        }
    }

    private int getXWidth(String text) {
        return (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
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

        // EXIT
        textX = getXforCenter("Exit Game");
        textY += gp.tileSize*2;
        g2.drawString("Exit Game", textX, textY);
        if (commandNum == 5) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyP.enterPressed == true) {
                optionSubState = 3;
                commandNum = 0;
            }
        }

        // BACK
        textX = getXforCenter("Back");
        textY += gp.tileSize;
        g2.drawString("Back", textX, textY);
        if (commandNum == 6) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyP.enterPressed == true) {
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
        g2.drawString("Q", rightX, rightY); rightY += gp.tileSize;
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

    public void drawQuestScreen(){
        int frameX = gp.tileSize;
        int frameY = gp.tileSize/2;
        int frameWidth  = gp.tileSize * 18;
        int frameHeight = gp.tileSize * 11;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // page header
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 39f));
        g2.setColor(Color.white);
        String header = "Quests  [Chapter: " + (questPageNum + 1) + "/4]";
        int x = getXforCenter(header);
        int y = frameY + gp.tileSize;
        g2.drawString(header, x, y);

        // ===== Quest 1 page =====
        if (questPageNum == 0) {
            boolean q1done    = gp.questManager.isQuestCompleted(QuestManager.QUEST1);
            boolean q2active  = gp.questManager.isQuestActive(QuestManager.QUEST2);
            boolean q2done    = gp.questManager.isQuestCompleted(QuestManager.QUEST2);
            int     q2stage   = gp.questManager.quest2Stage;

            final int PAD     = gp.tileSize;
            final int MID     = frameX + frameWidth / 2;
            final int LEFT_X  = frameX + PAD;
            final int RIGHT_X = MID + PAD / 2;

            final int TITLE_SIZE = 27;
            final int DESC_SIZE = 27;
            final int BODY_SIZE = 25;
            final int HINT_SIZE = 21;
            final int LINE_H     = 27;
            final int HINT_H     = 22;

            final int TOP_Y = frameY + gp.tileSize*2;

            int ly = TOP_Y;

            g2.setFont(g2.getFont().deriveFont(Font.BOLD | Font.ITALIC, (float) TITLE_SIZE));
            g2.setColor(q1done ? new Color(100, 230, 100) : new Color(255, 220, 80));
            g2.drawString("Quest 1:", LEFT_X, ly);
            ly += LINE_H - 2;
            g2.drawString("\"Familya Rizal\""
                            + (q1done ? " COMPLETE" : ""),
                    LEFT_X, ly);

            g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) DESC_SIZE));
            g2.setColor(Color.lightGray);
            ly += HINT_H + 2;
            g2.drawString("Find all siblings and bring them home.", LEFT_X, ly);

            ly += LINE_H + 2;

            String[] siblingNames = {
                    "Saturnina", "Paciano",
                    "Narcisa",   "Olimpia",
                    "Lucia",     "Maria",
                    "Josefa",    "Trinidad",
                    "Soledad",   "Concepcion"
            };

            boolean[] sibFound = new boolean[10];
            for (int i = 0; i < gp.npc.length; i++) {
                if (gp.npc[i] instanceof entity.NPC_Sibling) {
                    entity.NPC_Sibling s = (entity.NPC_Sibling) gp.npc[i];
                    int slot = i - 2;
                    if (slot >= 0 && slot < 9) sibFound[slot] = s.isFollowing;
                }
            }
            sibFound[9] = !q1done && gp.questManager.conchaVisited;

            final int SUB_LEFT  = LEFT_X;
            final int SUB_RIGHT = LEFT_X + (MID - LEFT_X) / 2;

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) BODY_SIZE));
            int sibY = ly;
            for (int i = 0; i < 10; i++) {
                boolean found = q1done || sibFound[i];
                g2.setColor(found ? new Color(80, 220, 80) : Color.white);
                String label = "- " + siblingNames[i];
                int cx = (i % 2 == 0) ? SUB_LEFT : SUB_RIGHT;
                g2.drawString(label, cx, sibY);
                if (i % 2 == 1) sibY += LINE_H;
            }
            ly = sibY;

            // delivery zone hint
            if (gp.questManager.quest1Stage == QuestManager.QUEST1_STARTED
                    && !q1done) {
                int found    = gp.questManager.siblingsFound;
                int required = gp.questManager.SIBLINGS_REQUIRED + 1;
                ly += 4;
                g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) HINT_SIZE));
                g2.setColor(new Color(200, 200, 100));
                g2.drawString("Siblings found: " + found + "/" + required, LEFT_X, ly);
                if (found >= required
                        || gp.questManager.quest1Stage == QuestManager.QUEST1_RETURN_TEODORA) {
                    ly += HINT_H;
                    g2.setColor(new Color(100, 255, 100));
                    g2.drawString("Return to Nanay Teodora!", LEFT_X, ly);
                }
            }

            int ry = TOP_Y;

            Color q2color = q2done   ? new Color(100, 230, 100)
                    : q2active ? new Color(255, 220, 80)
                    : Color.gray;

            g2.setFont(g2.getFont().deriveFont(Font.BOLD | Font.ITALIC, (float) TITLE_SIZE));
            g2.setColor(q2color);
            g2.drawString("Quest 2:", RIGHT_X, ry);
            ry += LINE_H - 2;
            g2.drawString("\"Pangangaral ng mga Tiyo\""
                            + (q2done ? " COMPLETE" : ""),
                    RIGHT_X, ry);

            g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) DESC_SIZE));
            g2.setColor(q2active || q2done ? Color.lightGray : Color.gray);
            ry += HINT_H + 2;
            g2.drawString("Learn from your uncles.", RIGHT_X, ry);

            ry += LINE_H + 2;

            if (!q2active && !q2done) {
                g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) HINT_SIZE));
                g2.setColor(Color.gray);
                g2.drawString("[Complete Quest 1 to unlock]", RIGHT_X, ry);
            } else {
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) BODY_SIZE));

                // Uncle Jose
                boolean joseDone = q2done || q2stage >= QuestManager.JOSE_DONE;
                g2.setColor(joseDone ? new Color(80, 220, 80) : Color.white);
                g2.drawString("- Meet Uncle Jose Alberto", RIGHT_X, ry);
                ry += LINE_H;

                if (!joseDone && q2stage == QuestManager.JOSE_WAITING) {
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) HINT_SIZE));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("  Buckets: "
                                    + gp.questManager.countItem("Paint Bucket") + "/6"
                                    + "  Brush: "
                                    + gp.questManager.countItem("Paintbrush") + "/1",
                            RIGHT_X, ry);
                    ry += HINT_H;
                    g2.drawString("  Canvas: "
                                    + gp.questManager.countItem("Canvas") + "/1",
                            RIGHT_X, ry);
                    ry += HINT_H;
                    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) BODY_SIZE));
                }

                // Uncle Manuel
                boolean manuelDone = q2done || q2stage >= QuestManager.MANUEL_DONE;
                g2.setColor(manuelDone ? new Color(80, 220, 80)
                        : q2stage >= QuestManager.JOSE_DONE ? Color.white : Color.gray);
                g2.drawString("- Train with Uncle Manuel", RIGHT_X, ry);
                ry += LINE_H;

                if (q2stage == QuestManager.MANUEL_RUNNING) {
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) HINT_SIZE));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("  Checkpoints: "
                                    + gp.questManager.checkpointsHit
                                    + "/" + gp.questManager.TOTAL_CHECKPOINTS,
                            RIGHT_X, ry);
                    ry += HINT_H;
                    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) BODY_SIZE));
                }

                // Uncle Gregorio
                boolean gregorioDone = q2done || q2stage >= QuestManager.GREGORIO_DONE;
                g2.setColor(gregorioDone ? new Color(80, 220, 80)
                        : q2stage >= QuestManager.MANUEL_DONE ? Color.white : Color.gray);
                g2.drawString("- Study with Uncle Gregorio", RIGHT_X, ry);
                ry += LINE_H;

                int quillCount    = gp.questManager.countItem("Quill");
                int notebookCount = gp.questManager.countItem("Notebook");

                if (q2stage == QuestManager.GREGORIO_WAITING && (quillCount < 1 || notebookCount < 1)) {

                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) HINT_SIZE));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("  Find a Quill and a Notebook!", RIGHT_X, ry);
                    ry += HINT_H;
                    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) BODY_SIZE));

                } else if (q2stage == QuestManager.GREGORIO_WAITING && quillCount >= 1 && notebookCount >= 1) {

                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) HINT_SIZE));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("  Quill: " + quillCount + "/1"
                                    + "  Notebook: " + notebookCount + "/1",
                            RIGHT_X, ry);
                    ry += HINT_H;
                    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) BODY_SIZE));
                }


                if (q2stage == QuestManager.GREGORIO_DONE) {
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) HINT_SIZE));
                    g2.setColor(new Color(100, 255, 100));
                    g2.drawString("All lessons complete!", RIGHT_X, ry);
                }
            }
        }

        // ===== Chapter 2 page =====
        else if (questPageNum == 1) {

            boolean q3done = gp.questManager.isQuestCompleted(QuestManager.QUEST3);
            boolean q3active = gp.questManager.isQuestActive(QuestManager.QUEST3);
            int q3stage = gp.questManager.quest3Stage;

            boolean q4done = gp.questManager.isQuestCompleted(QuestManager.QUEST4);
            boolean q4active = gp.questManager.isQuestActive(QuestManager.QUEST4);
            int q4stage = gp.questManager.quest4Stage;

            final int PAD = gp.tileSize;
            final int MID = frameX + frameWidth / 2;
            final int LEFT_X = frameX + PAD;
            final int RIGHT_X = MID + PAD/2;

            final int TITLE_SIZE = 27;
            final int DESC_SIZE = 27;
            final int BODY_SIZE = 25;
            final int HINT_SIZE = 21;
            final int LINE_H = 27;
            final int HINT_H = 22;

            final int TOP_Y = frameY + gp.tileSize*2;
            int ly = TOP_Y;

            Color q3color = q3done   ? new Color(100, 230, 100)
                    : q3active ? new Color(255, 220, 80)
                    : Color.gray;

            g2.setFont(g2.getFont().deriveFont(Font.BOLD | Font.ITALIC, (float) TITLE_SIZE));
            g2.setColor(q3color);
            g2.drawString("Quest 3:", LEFT_X, ly);
            ly += LINE_H - 2;
            g2.drawString("\"Ang Bagong Simula\""
                            + (q3done ? " COMPLETE" : ""),
                    LEFT_X, ly);

            g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) DESC_SIZE));
            g2.setColor(q3active || q3done ? Color.lightGray : Color.gray);
            ly += HINT_H + 2;
            g2.drawString("Enroll at Ateneo de Manila.", LEFT_X, ly);

            ly += LINE_H + 2;

            if (!q3active && !q3done) {
                g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) HINT_SIZE));
                g2.setColor(Color.gray);
                g2.drawString("[Complete Ch.1 to unlock]", LEFT_X, ly);
            } else {
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) BODY_SIZE));

                boolean s1 = q3done || q3stage > QuestManager.TALK_FERRANDO;
                g2.setColor(s1 ? new Color(80, 220, 80) : Color.white);
                g2.drawString("- Talk to Fr. Ferrando", LEFT_X, ly);
                ly += LINE_H;

                boolean s2 = q3done || q3stage > QuestManager.TALK_BURGOS;
                g2.setColor(s2 ? new Color(80, 220, 80)
                        : q3stage == QuestManager.TALK_BURGOS ? Color.white : Color.gray);
                g2.drawString("- Talk to Senor Burgos", LEFT_X, ly);
                ly += LINE_H;

                boolean s3 = q3done || q3stage > QuestManager.CUTSCENE_DONE;
                g2.setColor(s3 ? new Color(80, 220, 80) : Color.gray);
                g2.drawString("- Get enrolled", LEFT_X, ly);
                ly += LINE_H;

                boolean s4 = q3done || q3stage > QuestManager.TALK_PROFESSOR;
                g2.setColor(s4 ? new Color(80, 220, 80)
                        : q3stage == QuestManager.TALK_PROFESSOR ? Color.white : Color.gray);
                g2.drawString("- Meet your professor", LEFT_X, ly);
                ly += LINE_H;

                boolean q3s5active = q3stage == QuestManager.TALK_STUDENT
                        || q3stage == QuestManager.QUIZ_FAILED;
                boolean q3s5done   = q3done || q3stage >= QuestManager.TALK_FERRANDO_REWARD;
                g2.setColor(q3s5done   ? new Color(80, 220, 80)
                        : q3s5active ? Color.white : Color.gray);
                g2.drawString("- Pass the quiz (10/10)", LEFT_X, ly);
                ly += LINE_H;

                if (q3stage == QuestManager.QUIZ_FAILED) {
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) HINT_SIZE));
                    g2.setColor(new Color(230, 100, 100));
                    g2.drawString("  Try again!", LEFT_X, ly);
                    ly += HINT_H;
                    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) BODY_SIZE));
                }

                boolean s6active = q3stage == QuestManager.TALK_FERRANDO_REWARD;
                boolean s6done   = q3done || q3stage == QuestManager.QUEST3_DONE;
                g2.setColor(s6done   ? new Color(80, 220, 80)
                        : s6active ? Color.white : Color.gray);
                g2.drawString("- Return to Fr. Ferrando", LEFT_X, ly);
            }

            int ry = TOP_Y;

            Color q4color = q4done   ? new Color(100, 230, 100)
                    : q4active ? new Color(255, 220, 80)
                    : Color.gray;

            g2.setFont(g2.getFont().deriveFont(Font.BOLD | Font.ITALIC, (float) TITLE_SIZE));
            g2.setColor(q4color);
            g2.drawString("Quest 4:", RIGHT_X, ry);
            ry += LINE_H - 2;
            g2.drawString("\"Ang Kampeon ng Roma\""
                            + (q4done ? " COMPLETE" : ""),
                    RIGHT_X, ry);

            g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) DESC_SIZE));
            g2.setColor(q4active || q4done ? Color.lightGray : Color.gray);
            ry += HINT_H + 2;
            g2.drawString("Excel in all disciplines.", RIGHT_X, ry);

            ry += LINE_H + 2;

            if (!q4active && !q4done) {
                g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) HINT_SIZE));
                g2.setColor(Color.gray);
                g2.drawString("[Complete Quest 3 to unlock]", RIGHT_X, ry);
            } else {
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) BODY_SIZE));

                boolean sp = q4done || q4stage > QuestManager.TALK_PROFESSOR_Q4;
                g2.setColor(sp ? new Color(80, 220, 80) : Color.white);
                g2.drawString("- Speak with your professor", RIGHT_X, ry);
                ry += LINE_H;

                boolean sm = q4done || q4stage > QuestManager.TALK_MARIANO;
                g2.setColor(sm ? new Color(80, 220, 80)
                        : q4stage == QuestManager.TALK_MARIANO ? Color.white : Color.gray);
                g2.drawString("- Speak with Mariano", RIGHT_X, ry);
                ry += LINE_H;

                boolean sr = q4done || q4stage > QuestManager.TALK_RECTOR;
                g2.setColor(sr ? new Color(80, 220, 80)
                        : q4stage == QuestManager.TALK_RECTOR ? Color.white : Color.gray);
                g2.drawString("- Speak with Fr. Rector", RIGHT_X, ry);
                ry += LINE_H;

                if (q4stage >= QuestManager.DISCIPLINES_ACTIVE || q4done) {
                    String[] disciplines = {
                            "Conduct", "Painting",
                            "French Language", "Rhetoric", "Dedication"
                    };
                    for (int i = 0; i < 5; i++) {
                        boolean answered = q4done || gp.questManager.disciplineAnswered[i];
                        boolean active   = q4stage == QuestManager.DISCIPLINES_ACTIVE && !answered;
                        boolean earned   = gp.questManager.disciplineMedalEarned[i];

                        g2.setColor(answered && earned  ? new Color(80, 220, 80)
                                : answered && !earned ? new Color(180, 80, 80)
                                : active              ? Color.white
                                :                       Color.gray);

                        String mark = answered && earned  ? "/ "
                                : answered && !earned ? "X "
                                : "- ";
                        g2.drawString(mark + disciplines[i], RIGHT_X + 10, ry);
                        ry += LINE_H;
                    }

                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) HINT_SIZE));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("  Medals: " + gp.questManager.medalsEarned
                                    + "/" + QuestManager.MEDALS_REQUIRED,
                            RIGHT_X, ry);
                    ry += HINT_H;
                    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) BODY_SIZE));

                } else {
                    g2.setColor(Color.gray);
                    g2.drawString("- 5 discipline challenges", RIGHT_X, ry);
                    ry += LINE_H;
                }

                boolean sre = q4done || q4stage == QuestManager.TALK_RECTOR_END;
                g2.setColor(q4done ? new Color(80, 220, 80)
                        : sre ? Color.white : Color.gray);
                g2.drawString("- Return to Fr. Rector", RIGHT_X, ry);
                ry += LINE_H;

                if (q4stage == QuestManager.TALK_RECTOR_END) {
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) HINT_SIZE));
                    g2.setColor(new Color(100, 255, 100));
                    g2.drawString("  Go back to Fr. Rector!", RIGHT_X, ry);
                }
            }
        }

        // ===== Chapter 3 page =====
        else if (questPageNum == 2) {

            boolean q5done   = gp.questManager.isQuestCompleted(QuestManager.QUEST5);
            boolean q5active = gp.questManager.isQuestActive(QuestManager.QUEST5);
            int     q5stage  = gp.questManager.quest5Stage;

            boolean q6active = gp.questManager.isQuestActive(QuestManager.QUEST6);
            boolean q6done   = gp.questManager.isQuestCompleted(QuestManager.QUEST6);
            int     q6stage  = gp.questManager.quest6Stage;

            final int PAD = gp.tileSize;
            final int MID = frameX + frameWidth / 2;
            final int LEFT_X = frameX + PAD;
            final int RIGHT_X = MID + PAD/2;

            final int TITLE_SIZE = 27;
            final int DESC_SIZE = 27;
            final int BODY_SIZE = 25;
            final int HINT_SIZE = 21;
            final int LINE_H = 27;
            final int HINT_H = 22;

            final int TOP_Y = frameY + gp.tileSize * 2;
            int ly = TOP_Y;

            Color q5color = q5done   ? new Color(100, 230, 100)
                    : q5active ? new Color(255, 220, 80)
                    :            Color.gray;

            g2.setFont(g2.getFont().deriveFont(Font.BOLD | Font.ITALIC, (float) TITLE_SIZE));
            g2.setColor(q5color);
            g2.drawString("Quest 5:", LEFT_X, ly);
            ly += LINE_H - 2;
            g2.drawString("\"Noli Me Tangere\""
                    + (q5done ? " COMPLETE" : ""), LEFT_X, ly);

            g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) DESC_SIZE));
            g2.setColor(q5active || q5done ? Color.lightGray : Color.gray);
            ly += HINT_H + 2;
            g2.drawString("Write the novel that will wake a nation.", LEFT_X, ly);

            ly += LINE_H + 2;

            if (!q5active && !q5done) {
                g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) HINT_SIZE));
                g2.setColor(Color.gray);
                g2.drawString("[Complete Ch.2 to unlock]", LEFT_X, ly);
            } else {
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) BODY_SIZE));

                boolean s3 = q5done || q5stage > QuestManager.FIND_LETTER;
                g2.setColor(s3 ? new Color(80, 220, 80)
                        : q5stage == QuestManager.FIND_LETTER ? Color.white : Color.gray);
                g2.drawString("- Find the draft of Noli Me Tangere", LEFT_X, ly);
                ly += LINE_H;

                boolean s4active = q5stage == QuestManager.COLLECT_OBJECTS;
                boolean s4done   = q5done || q5stage > QuestManager.COLLECT_OBJECTS;
                g2.setColor(s4done   ? new Color(80, 220, 80)
                        : s4active ? Color.white : Color.gray);
                g2.drawString("- Collect manuscript inspirations", LEFT_X, ly);
                ly += LINE_H;

                if (s4active || s4done) {
                    String[] parts = {
                            "Scalpel (Title)",
                            "Mirror (Ibarra)",
                            "Dried Flower (Maria Clara)",
                            "Rosary (The Friars)",
                            "Portrait (Sisa)",
                            "Scrap Metal (Elias)",
                            "Empty Plate (Berlin)"
                    };
                    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) (BODY_SIZE - 2)));
                    for (int i = 0; i < 7; i++) {
                        boolean done = q5done || gp.questManager.manuscriptParts[i];
                        g2.setColor(done ? new Color(80, 220, 80) : Color.white);
                        g2.drawString((done ? "/ " : "- ") + parts[i], LEFT_X + 10, ly);
                        ly += LINE_H - 2;
                    }
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) HINT_SIZE));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("  Parts: " + gp.questManager.objectsCollected + "/7", LEFT_X, ly);
                    ly += HINT_H;
                    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) BODY_SIZE));
                }

                boolean s5 = q5done || q5stage >= QuestManager.TALK_MAXIMO;
                g2.setColor(q5done ? new Color(80, 220, 80)
                        : s5 ? Color.white : Color.gray);
                g2.drawString("- Talk to Maximo Viola", LEFT_X, ly);
            }

            int ry = TOP_Y;

            Color q6color = q6done   ? new Color(100, 230, 100)
                    : q6active ? new Color(255, 220, 80)
                    :            Color.gray;

            g2.setFont(g2.getFont().deriveFont(Font.BOLD | Font.ITALIC, (float) TITLE_SIZE));
            g2.setColor(q6color);
            g2.drawString("Quest 6:", RIGHT_X, ry);
            ry += LINE_H - 2;
            g2.drawString("\"El Filibusterismo\""
                    + (q5done ? " COMPLETE" : ""), RIGHT_X, ry);

            g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) DESC_SIZE));
            g2.setColor(q6active || q6done ? Color.lightGray : Color.gray);
            ry += HINT_H + 2;
            g2.drawString("Become finish your sequel novel.", RIGHT_X, ry);

            ry += LINE_H + 2;

            if (!q6active && !q6done) {
                g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) HINT_SIZE));
                g2.setColor(Color.gray);
                g2.drawString("[Complete Quest 5 to unlock]", RIGHT_X, ry);
            } else {
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) BODY_SIZE));

                boolean s4 = q6done || q6stage > QuestManager.FIND_DRAFT;
                g2.setColor(s4 ? new Color(80, 220, 80)
                        : q6stage == QuestManager.FIND_DRAFT ? Color.white : Color.gray);
                g2.drawString("- Find the draft of El Filibusterismo", RIGHT_X, ry);
                ry += LINE_H;

                boolean s6active = q6stage == QuestManager.COLLECT_OBJECTS;
                boolean s6done   = q6done || q6stage > QuestManager.COLLECT_OBJECTS;
                g2.setColor(s6done   ? new Color(80, 220, 80)
                        : s6active ? Color.white : Color.gray);
                g2.drawString("- Collect manuscript inspirations", RIGHT_X, ry);
                ry += LINE_H;

                if (s6active || s6done) {
                    String[] parts = {
                            "Glasses",
                            "Newspaper",
                            "Old Letter",
                            "Worn Letter"
                    };

                    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) (BODY_SIZE - 2)));
                    for (int i = 0; i < 4; i++) {
                        boolean done = q6done || gp.questManager.elFiliParts[i];
                        g2.setColor(done ? new Color(80, 220, 80) : Color.white);
                        g2.drawString((done ? "/ " : "- ") + parts[i], RIGHT_X + 10, ry);
                        ry += LINE_H - 2;
                    }

                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) HINT_SIZE));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("  Parts: " + gp.questManager.objectsCollected + "/4", RIGHT_X, ry);
                    ry += HINT_H;
                    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) BODY_SIZE));
                }

                boolean s6 = q6done || q6stage >= QuestManager.TALK_PACIANO_Q6;
                g2.setColor(q6done ? new Color(80, 220, 80)
                        : s6 ? Color.white : Color.gray);
                g2.drawString("- Talk to Paciano", RIGHT_X, ry);
            }

        }

        // ===== Chapter 4 page =====
        else if (questPageNum == 3) {
            boolean q7done   = gp.questManager.isQuestCompleted(QuestManager.QUEST7);
            boolean q7active = gp.questManager.isQuestActive(QuestManager.QUEST7);
            int     q7stage  = gp.questManager.quest7Stage;

            final int PAD        = gp.tileSize;
            final int LEFT_X     = frameX + PAD;
            final int TITLE_SIZE = 27;
            final int DESC_SIZE  = 27;
            final int BODY_SIZE  = 25;
            final int HINT_SIZE  = 21;
            final int LINE_H     = 27;
            final int HINT_H     = 22;
            final int TOP_Y      = frameY + gp.tileSize * 2;

            int ly = TOP_Y;

            Color q7color = q7done   ? new Color(100, 230, 100)
                    : q7active ? new Color(255, 220, 80)
                    :            Color.gray;

            g2.setFont(g2.getFont().deriveFont(Font.BOLD | Font.ITALIC, (float) TITLE_SIZE));
            g2.setColor(q7color);
            g2.drawString("Quest 7:", LEFT_X, ly);
            ly += LINE_H - 2;
            g2.drawString("\"Consummatum Est\""
                    + (q7done ? " COMPLETE" : ""), LEFT_X, ly);

            g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) DESC_SIZE));
            g2.setColor(q7active || q7done ? Color.lightGray : Color.gray);
            ly += HINT_H + 2;
            g2.drawString("Face the trial. Write your last words.", LEFT_X, ly);

            ly += LINE_H + 2;

            if (!q7active && !q7done) {
                g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) HINT_SIZE));
                g2.setColor(Color.gray);
                g2.drawString("[Complete Quest 6 to unlock]", LEFT_X, ly);
            } else {
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float) BODY_SIZE));

                // Guardia Civil
                boolean s1 = q7done || q7stage > QuestManager.Q7_TALK_GUARDIA;
                g2.setColor(s1 ? new Color(80, 220, 80) : Color.white);
                g2.drawString("- Face the Guardia Civil", LEFT_X, ly);
                ly += LINE_H;

                // Judge
                boolean s2 = q7done || q7stage > QuestManager.Q7_TALK_JUDGE;
                g2.setColor(s2 ? new Color(80, 220, 80)
                        : q7stage == QuestManager.Q7_TALK_JUDGE ? Color.white : Color.gray);
                g2.drawString("- Hear the Judge's verdict", LEFT_X, ly);
                ly += LINE_H;

                // Josephine
                boolean s3 = q7done || q7stage > QuestManager.Q7_TALK_JOSEPHINE;
                g2.setColor(s3 ? new Color(80, 220, 80)
                        : q7stage == QuestManager.Q7_TALK_JOSEPHINE ? Color.white : Color.gray);
                g2.drawString("- Talk to Josephine Bracken", LEFT_X, ly);
                ly += LINE_H;

                // Final thoughts paper
                boolean s4 = q7done || q7stage > QuestManager.Q7_INTERACT_PAPER;
                g2.setColor(s4 ? new Color(80, 220, 80)
                        : q7stage == QuestManager.Q7_INTERACT_PAPER ? Color.white : Color.gray);
                g2.drawString("- Write your final thoughts", LEFT_X, ly);
                ly += LINE_H;

                // Alcohol stove
                boolean s5 = q7done || q7stage > QuestManager.Q7_INTERACT_STOVE;
                g2.setColor(s5 ? new Color(80, 220, 80)
                        : q7stage == QuestManager.Q7_INTERACT_STOVE ? Color.white : Color.gray);
                g2.drawString("- Hide the poem in the stove", LEFT_X, ly);
                ly += LINE_H;

                // Trinidad
                boolean s6 = q7done || q7stage > QuestManager.Q7_TALK_TRINIDAD;
                g2.setColor(s6 ? new Color(80, 220, 80)
                        : q7stage == QuestManager.Q7_TALK_TRINIDAD ? Color.white : Color.gray);
                g2.drawString("- Give the poem to Trinidad", LEFT_X, ly);
                ly += LINE_H;

                if (q7done) {
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, (float) HINT_SIZE));
                    g2.setColor(new Color(100, 255, 100));
                    g2.drawString("Consummatum est.", LEFT_X, ly);
                }
            }
        }

        // PREV / NEXT buttons
        int btnY = frameY + frameHeight - 28;

        if (questPageNum > 0) {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22f));
            g2.setColor(new Color(255, 220, 80));
            g2.drawString("< PREV  [A]", frameX + gp.tileSize, btnY);
        }

        if (questPageNum < 1) {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22f));
            g2.setColor(new Color(255, 220, 80));
            String next = "NEXT >  [D]";
            int nx = frameX + frameWidth - gp.tileSize
                    - (int) g2.getFontMetrics().getStringBounds(next, g2).getWidth();
            g2.drawString(next, nx, btnY);
        }

        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 20f));
        g2.setColor(new Color(180, 180, 180));
        String close = "[ Q ] Close";
        g2.drawString(close, getXforCenter(close), btnY);
    }
    public void drawQuestHUD() {
        if (gp.questManager == null) return;

        if (gp.cutsceneManager.isExecutionWalkActive()
                || gp.cutsceneManager.isStatsScreenActive()
                || gp.cutsceneManager.isGameCompleted()) return;

        if (gp.cutsceneManager.isExecutionWalkActive()) {
            // skip all HUD drawing
        } else {
            int currentQ = gp.questManager.currentQuest;
            if (gp.questManager.isQuestCompleted(currentQ) &&
                    currentQ == QuestManager.QUEST1) return;

            int panelW = gp.tileSize * 7;
            int panelH = (int) (gp.tileSize * 2.5);
            int panelX = gp.screenWidth - panelW - (gp.tileSize / 2);
            int panelY = gp.tileSize / 2;

            drawSubWindow(panelX, panelY, panelW, panelH);

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22F));

            // ===== QUEST 1 =====
            if (currentQ == main.QuestManager.QUEST1 && gp.questManager.isQuestActive(main.QuestManager.QUEST1)) {

                g2.setColor(new Color(255, 220, 80));
                g2.drawString("Familya Rizal", panelX + 12, panelY + 28);
                g2.setColor(Color.white);
                g2.setFont(g2.getFont().deriveFont(20F));

                if (gp.questManager.quest1Stage == main.QuestManager.QUEST1_NOT_STARTED) {
                    g2.drawString("Talk to Nanay Teodora.", panelX + 12, panelY + 52);

                } else {
                    int found    = gp.questManager.siblingsFound;
                    int required = gp.questManager.SIBLINGS_REQUIRED + 1;
                    // Count only the 9 living siblings (exclude Concha)
                    int siblingsOnly = found - (gp.questManager.conchaVisited ? 1 : 0);

                    g2.drawString("Siblings: " + siblingsOnly + " / 9", panelX + 12, panelY + 52);

                    // Show Concha hint once all 9 siblings are found but Concha not yet visited
                    if (siblingsOnly >= 9 && !gp.questManager.conchaVisited) {
                        g2.setColor(new Color(255, 180, 60));
                        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                        g2.drawString("Find Concha's grave nearby!", panelX + 12, panelY + 72);
                        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 16F));
                        g2.setColor(new Color(180, 180, 180));
                        g2.drawString("(Look around the garden)", panelX + 12, panelY + 90);

                    } else if (found >= required
                            || gp.questManager.quest1Stage == QuestManager.QUEST1_RETURN_TEODORA) {
                        g2.setColor(new Color(100, 255, 100));
                        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                        g2.drawString("Return to Nanay Teodora!", panelX + 12, panelY + 72);
                    }
                }

            }

            // ===== QUEST 2 =====
            else if (currentQ == QuestManager.QUEST2 && gp.questManager.isQuestActive(QuestManager.QUEST2)) {

                g2.setColor(new Color(255, 220, 80));
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22F));
                g2.drawString("Pangangaral ng mga Tiyo", panelX + 12, panelY + 28);

                g2.setColor(Color.white);
                g2.setFont(g2.getFont().deriveFont(20F));

                int stage = gp.questManager.quest2Stage;

                if (stage == QuestManager.JOSE_INACTIVE) {
                    g2.drawString("Find Uncle Jose Alberto.", panelX + 12, panelY + 52);
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 17F));
                    g2.setColor(new Color(180, 180, 180));
                    g2.drawString("He teaches the basics of art.", panelX + 12, panelY + 72);

                } else if (stage == QuestManager.JOSE_WAITING) {
                    int buckets = gp.questManager.countItem("Paint Bucket");
                    int brush   = gp.questManager.countItem("Paintbrush");
                    int canvas  = gp.questManager.countItem("Canvas");
                    g2.drawString("Paint Buckets: " + buckets + " / 6", panelX + 12, panelY + 52);
                    g2.drawString("Brush: " + brush + "/1   Canvas: " + canvas + "/1", panelX + 12, panelY + 70);
                    if (gp.questManager.hasAllArtSupplies()) {
                        g2.setColor(new Color(100, 255, 100));
                        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                        g2.drawString("Return to Uncle Jose!", panelX + 12, panelY + 90);
                    }

                } else if (stage == QuestManager.JOSE_DONE) {
                    g2.drawString("Find Uncle Manuel.", panelX + 12, panelY + 52);

                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.setColor(new Color(180, 180, 180));
                    g2.drawString("He will train your body ensuring you will gain", panelX + 12, panelY + 72);
                    g2.drawString("agility, endurance, and strength.", panelX + 12, panelY + 90);

                } else if (stage == QuestManager.MANUEL_RUNNING) {
                    int hit   = gp.questManager.checkpointsHit;
                    int total = gp.questManager.TOTAL_CHECKPOINTS;
                    g2.drawString("Checkpoints: " + hit + " / " + total, panelX + 12, panelY + 52);
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("Run around the map to find the blue checkpoint", panelX + 12, panelY + 70);
                    g2.drawString("circles!", panelX + 12, panelY + 86);
                    if (gp.questManager.courseCompleted) {
                        g2.setColor(new Color(100, 255, 100));
                        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                        g2.drawString("Return to Uncle Manuel!", panelX + 12, panelY + 104);
                    }

                } else if (stage == QuestManager.MANUEL_DONE) {
                    g2.drawString("Find Uncle Gregorio.", panelX + 12, panelY + 52);
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.setColor(new Color(180, 180, 180));
                    g2.drawString("He will teach you to write.", panelX + 12, panelY + 72);

                } else if (stage == QuestManager.GREGORIO_WAITING) {
                    int quill    = gp.questManager.countItem("Quill");
                    int notebook = gp.questManager.countItem("Notebook");
                    g2.drawString("Quill: " + quill + " / 1", panelX + 12, panelY + 52);
                    g2.drawString("Notebook: " + notebook + " / 1", panelX + 12, panelY + 70);
                    if (gp.questManager.hasWritingSupplies()) {
                        g2.setColor(new Color(100, 255, 100));
                        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                        g2.drawString("Return to Uncle Gregorio!", panelX + 12, panelY + 90);
                    }

                } else if (stage == QuestManager.GREGORIO_DONE) {
                    g2.setColor(new Color(100, 230, 100));
                    g2.drawString("Quest 2 Complete!", panelX + 12, panelY + 52);
                }
            }

            // ===== QUEST 3 =====
            else if (currentQ == QuestManager.QUEST3 && gp.questManager.isQuestActive(QuestManager.QUEST3)) {

                g2.setColor(new Color(255, 220, 80));
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22F));
                g2.drawString("Ang Bagong Simula", panelX + 12, panelY + 28);

                g2.setColor(Color.white);
                g2.setFont(g2.getFont().deriveFont(20F));

                int stage = gp.questManager.quest3Stage;

                if (stage == QuestManager.TALK_FERRANDO) {
                    g2.drawString("Talk to Fr. Ferrando.", panelX + 12, panelY + 52);

                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("Enroll to Ateneo Municipal de Manila", panelX + 12, panelY + 72);

                } else if (stage == QuestManager.TALK_BURGOS) {
                    g2.drawString("Talk to Senor Burgos.", panelX + 12, panelY + 52);

                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("Enroll to Ateneo Municipal de Manila", panelX + 12, panelY + 72);

                } else if (stage == QuestManager.CUTSCENE_DONE
                        || stage == QuestManager.TALK_PROFESSOR) {
                    g2.drawString("Find your professor.", panelX + 12, panelY + 52);

                } else if (stage == QuestManager.TALK_STUDENT) {
                    g2.drawString("Talk to your classmate.", panelX + 12, panelY + 52);

                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("Take a quiz lead by your classmate", panelX + 12, panelY + 72);

                } else if (stage == QuestManager.QUIZ_FAILED) {
                    g2.setColor(new Color(230, 100, 100));
                    g2.drawString("Try the quiz again!", panelX + 12, panelY + 52);

                } else if (stage == QuestManager.TALK_FERRANDO_REWARD) {
                    g2.setColor(Color.white);
                    g2.drawString("Return to Fr. Ferrando.", panelX + 12, panelY + 52);

                } else if (stage == QuestManager.QUEST3_DONE) {
                    g2.setColor(new Color(100, 230, 100));
                    g2.drawString("Quest 3 Complete!", panelX + 12, panelY + 52);
                }
            }

            // ===== QUEST 4 =====
            else if (currentQ == QuestManager.QUEST4 && gp.questManager.isQuestActive(QuestManager.QUEST4)) {

                g2.setColor(new Color(255, 220, 80));
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22F));
                g2.drawString("Ang Kampeon ng Roma", panelX + 12, panelY + 28);

                g2.setColor(Color.white);
                g2.setFont(g2.getFont().deriveFont(20F));

                int stage = gp.questManager.quest4Stage;

                if (stage == QuestManager.TALK_PROFESSOR_Q4) {
                    g2.drawString("Talk to your professor.", panelX + 12, panelY + 52);

                } else if (stage == QuestManager.TALK_MARIANO) {
                    g2.drawString("Talk to Mariano.", panelX + 12, panelY + 52);

                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("He was feeling down after you dethroned him.", panelX + 12, panelY + 72);

                } else if (stage == QuestManager.TALK_RECTOR) {
                    g2.drawString("Talk to Fr. Rector.", panelX + 12, panelY + 52);

                } else if (stage == QuestManager.DISCIPLINES_ACTIVE) {
                    int done = gp.questManager.disciplinesCompleted;
                    g2.drawString("Disciplines: " + done + "/5", panelX + 12, panelY + 52);
                    g2.setColor(new Color(200, 200, 100));
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.drawString("Medals: " + gp.questManager.medalsEarned + "/5", panelX + 12, panelY + 72);
                } else if (stage == QuestManager.TALK_RECTOR_END) {
                    g2.drawString("Medals: " + gp.questManager.medalsEarned + "/5", panelX + 12, panelY + 52);
                    g2.setColor(new Color(100, 255, 100));
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.drawString("Return to Fr. Rector!", panelX + 12, panelY + 72);

                } else if (stage == QuestManager.QUEST4_DONE) {
                    g2.setColor(new Color(100, 230, 100));
                    g2.drawString("Quest 4 Complete!", panelX + 12, panelY + 52);
                }
            }

            // ===== QUEST 5 =====
            else if (currentQ == QuestManager.QUEST5 && gp.questManager.isQuestActive(QuestManager.QUEST5)) {

                g2.setColor(new Color(255, 220, 80));
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22F));
                g2.drawString("Noli Me Tangere", panelX + 12, panelY + 28);

                g2.setColor(Color.white);
                g2.setFont(g2.getFont().deriveFont(20F));

                int stage = gp.questManager.quest5Stage;

                if (stage == QuestManager.TALK_PEDRO) {
                    g2.drawString("Talk to Pedro.", panelX + 12, panelY + 52);

                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("Talk to your fellow Filipino ilustrado", panelX + 12, panelY + 72);

                } else if (stage == QuestManager.TALK_CONSUELO) {
                    g2.drawString("Talk to Consuelo.", panelX + 12, panelY + 52);

                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("She has been throwing you flirty looks.", panelX + 12, panelY + 72);

                } else if (stage == QuestManager.FIND_LETTER) {
                    g2.drawString("Find the letter.", panelX + 12, panelY + 52);

                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("Secure the draft you have written.", panelX + 12, panelY + 72);

                } else if (stage == QuestManager.COLLECT_OBJECTS) {
                    int done = gp.questManager.objectsCollected;
                    g2.drawString("Manuscript: " + done + "/7", panelX + 12, panelY + 52);
                } else if (stage == QuestManager.TALK_MAXIMO) {
                    g2.setColor(Color.white);
                    g2.drawString("Talk to Maximo Viola.", panelX + 12, panelY + 52);

                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("He will be supporting your novel", panelX + 12, panelY + 72);

                } else if (stage == QuestManager.QUEST5_DONE) {
                    g2.setColor(new Color(100, 230, 100));
                    g2.drawString("Quest 5 Complete!", panelX + 12, panelY + 52);
                }
            }

            // ===== QUEST 6 =====
            else if (currentQ == QuestManager.QUEST6 && gp.questManager.isQuestActive(QuestManager.QUEST6)) {

                g2.setColor(new Color(255, 220, 80));
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22F));
                g2.drawString("El Filibusterismo", panelX + 12, panelY + 28);

                g2.setColor(Color.white);
                g2.setFont(g2.getFont().deriveFont(20F));

                int stage = gp.questManager.quest6Stage;

                if (stage == QuestManager.TALK_PACIANO_Q6) {
                    g2.drawString("Talk to Kuya Paciano.", panelX + 12, panelY + 52);

                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("He came to deliver you some bad news", panelX + 12, panelY + 72);

                } else if (stage == QuestManager.FIND_DRAFT) {
                    g2.drawString("Find the El Filibusterismo Draft.", panelX + 12, panelY + 52);

                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("Continue your sequel novel dedicated to", panelX + 12, panelY + 72);
                    g2.drawString("GOMBURZA", panelX + 12, panelY + 90);

                } else if (stage == QuestManager.COLLECT_OBJECTS_Q6) {
                    int done = gp.questManager.q6ObjectsCollected;
                    g2.drawString("Manuscript: " + done + "/4", panelX + 12, panelY + 52);

                } else if (stage == QuestManager.RETURN_PACIANO) {
                    g2.drawString("Return to Kuya Paciano.", panelX + 12, panelY + 52);
                    g2.setColor(new Color(100, 255, 100));
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));

                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("Bid your farewell to your Kuya", panelX + 12, panelY + 90);

                    g2.drawString("Manuscript complete!", panelX + 12, panelY + 72);

                } else if (stage == QuestManager.QUEST6_DONE) {
                    g2.setColor(new Color(100, 230, 100));
                    g2.drawString("Quest 6 Complete!", panelX + 12, panelY + 52);
                }
            }

            // ===== QUEST 7 ======
            else if (currentQ == QuestManager.QUEST7 && gp.questManager.isQuestActive(QuestManager.QUEST7)) {

                g2.setColor(new Color(255, 220, 80));
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22F));
                g2.drawString("Consummatum Est", panelX + 12, panelY + 28);

                g2.setColor(Color.white);
                g2.setFont(g2.getFont().deriveFont(20F));

                int stage = gp.questManager.quest7Stage;

                if (stage == QuestManager.Q7_TALK_GUARDIA) {
                    g2.drawString("Speak with the Guardia Civil.", panelX + 12, panelY + 52);

                } else if (stage == QuestManager.Q7_TALK_JUDGE) {
                    g2.drawString("Face the Judge.", panelX + 12, panelY + 52);
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("Hear the verdict of the Council.", panelX + 12, panelY + 72);

                } else if (stage == QuestManager.Q7_TALK_JOSEPHINE) {
                    g2.drawString("Talk to Josephine Bracken.", panelX + 12, panelY + 52);

                } else if (stage == QuestManager.Q7_INTERACT_PAPER) {
                    g2.drawString("Find your final thoughts.", panelX + 12, panelY + 52);
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("Read the paper on the table.", panelX + 12, panelY + 72);

                } else if (stage == QuestManager.Q7_INTERACT_STOVE) {
                    g2.drawString("Examine the alcohol stove.", panelX + 12, panelY + 52);
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("Do not let the flame go out.", panelX + 12, panelY + 72);

                } else if (stage == QuestManager.Q7_TALK_TRINIDAD) {
                    g2.drawString("Talk to Trinidad.", panelX + 12, panelY + 52);
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.setColor(new Color(200, 200, 100));
                    g2.drawString("Give her what the flame kept safe.", panelX + 12, panelY + 72);

                } else if (stage == QuestManager.Q7_DONE) {
                    g2.setColor(new Color(100, 230, 100));
                    g2.drawString("Consummatum est.", panelX + 12, panelY + 52);
                }
            }
        }

    }
    public void drawPoemPanel() {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int panelW = gp.tileSize * 15;
        int panelH = gp.tileSize * 10;
        int panelX = gp.screenWidth / 2 - panelW / 2;
        int panelY = gp.screenHeight / 2 - panelH / 2;
        drawSubWindow(panelX, panelY, panelW, panelH);

        int tailX = panelX + gp.tileSize;

        // title
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
        g2.setColor(new Color(255, 220, 80));
        String title = "Sa Aking Mga Kabata";
        int titleX = getXforCenter(title);
        g2.drawString(title, titleX, panelY + 45);


        // LEFT column — stanzas 1 and 2
        String[] left = {
                "Kapagka ang baya'y sadyang umiibig",
                "sa kanyang salitang kaloob ng langit,",
                "sanlang kalayaan nasa ring masapit",
                "katulad ng ibong nasa himpapawid.",
                "",
                "Pagkat ang salita'y isang kahatulan",
                "sa bayan, sa nayon't mga kaharian,",
                "at ang isang tao'y katulad, kabagay",
                "ng alin mang likha noong kalayaan.",
                "",
                "Ang hindi magmahal sa kanyang salita",
                "mahigit sa hayop at malansang isda,",
                "kaya ang marapat pagyamaning kusa",
                "na tulad sa isang tunay na nagpala.",
        };

        // RIGHT column — stanzas 3 and 4
        String[] right = {
                "Ang wikang Tagalog tulad din sa Latin,",
                "sa Ingles, Kastila at salitang anghel,",
                "sapagka't ang Poong maalam tumingin",
                "ang siyang naggawad, nagbigay sa atin.",
                "",
                "Ang salita nati'y huwad din sa iba",
                "na may alpabeto at sariling letra,",
                "na kaya nawala'y dinatnan ng sigwa",
                "ang lunday sa lawa noong dakong una.",
        };

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24F));
        g2.setColor(Color.white);

        int leftX  = panelX + 25;
        int rightX = panelX + panelW / 2 + 10;
        int startY = panelY + 85;
        int lineH  = 24;

        for (String line : left) {
            g2.drawString(line, leftX, startY);
            startY += lineH;
        }

        startY = panelY + 85;
        for (String line : right) {
            g2.drawString(line, rightX, startY);
            startY += lineH;
        }

        // prompt
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
        g2.setColor(new Color(200, 200, 200));
        g2.drawString("[ ENTER ] to continue", panelX + panelW - 210, panelY + panelH - 15);

        // close
        if (gp.keyP.enterPressed) {
            gp.keyP.enterPressed = false;
            showPoemPanel = false;
        }
    }
    public void drawLetterPanel() {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int panelW = gp.tileSize * 15;
        int panelH = gp.tileSize * 10;
        int panelX = gp.screenWidth / 2 - panelW / 2;
        int panelY = gp.screenHeight / 2 - panelH / 2;
        drawSubWindow(panelX, panelY, panelW, panelH);

        int tailX = panelX + gp.tileSize;

        // title
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
        g2.setColor(new Color(255, 220, 80));
        String title = "Draft Page - Noli Me Tangere";
        int titleX = getXforCenter(title);
        g2.drawString(title, titleX, panelY + 45);

        String[] left = {

                "I write not for fame, nor for praise but",
                "for those whose voices are buried beneath",
                "the weight of silence. Our land is",
                "beautiful yet beneath its green fields",
                "and quiet towns lies a sickness no one",
                "dares to name. Many pretend not to see",
                "it. Others benefit from it. But the suffering",
                "of the people grows deeper each day.",
                "",

                "If a wound is hidden, it festers. If a disease",
                "is ignored it spreads. Thus I must write—not",
                "to entertain, but to reveal. My pen must",
                "become a mirror reflecting the truth of our",
                "society so that all who look upon it may",
                "recognize",
                "",

        };

        String[] right = {

                "the illness within. In the towns and villages",
                "of our country live men who preach virtue",
                " yet rule with fear. There are officials",
                " who claim justice but serve only power.",
                " Meanwhile the common people endure humiliation",
                "poverty and the quiet loss of hope.",
                "",

                "But there are also those who begin to awaken.",
                "This book will not written to accuse blindly",
                " but to expose what has long been concealed.",
                "If society refusesto confront its own reflection,",
                "then it will never heal. And so I begin..."
        };


        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        g2.setColor(Color.white);

        int leftX  = panelX + 25;
        int rightX = panelX + panelW / 2 + 10;
        int startY = panelY + 85;
        int lineH  = 24;

        for (String line : left) {
            g2.drawString(line, leftX, startY);
            startY += lineH;
        }

        startY = panelY + 85;
        for (String line : right) {
            g2.drawString(line, rightX, startY);
            startY += lineH;
        }

        // prompt
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
        g2.setColor(new Color(200, 200, 200));
        g2.drawString("[ ENTER ] to continue", panelX + panelW - 210, panelY + panelH - 15);

        // close
        if (gp.keyP.enterPressed) {
            gp.keyP.enterPressed = false;
            showPoemPanel = false;
        }
    }
    public void drawElFiliPanel() {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int panelW = gp.tileSize * 15;
        int panelH = gp.tileSize * 10;
        int panelX = gp.screenWidth / 2 - panelW / 2;
        int panelY = gp.screenHeight / 2 - panelH / 2;
        drawSubWindow(panelX, panelY, panelW, panelH);

        // title
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
        g2.setColor(new Color(255, 220, 80));
        String title = "Draft of El Filibusterismo";
        int titleX = getXforCenter(title);
        g2.drawString(title, titleX, panelY + 45);

        String[] left = {
                "The ink was no longer a medicine. It had",
                "become gunpowder. In 1891, the first copies",
                "were smuggled into Manila in the dark of",
                "night. They did not arrive in the hands of",
                "scholars but in the calloused palms of",
                "workers and the hidden pockets of students.",
                "",
                "The Noli Me Tangere had taught the Filipino",
                "people how to weep for their country. The",
                "El Filibusterismo taught them how to fight",
                "for it. Across the islands, the 'Social",
                "Cancer' was no longer a metaphor.",
                "",
                " In the quiet of the fields, farmers",
                " stopped praying for a harvest and started",
                "looking at their bolos.",
        };

        // RIGHT column
        String[] right = {
                "In the university halls, the whispers of reform",
                "turned into the blueprints for revolution. The",
                "Spanish authorities did not see a story; they",
                "saw a fuse. They did not see a protagonist;",
                " they saw a ghost of a dreamer returned as a",
                "jeweler of death, ready to burn the house down",
                "to save the soul within.",

                "",
                "He did not regret a single letter. For some",
                "truths are too heavy to carry alone. They",
                "must be written down, passed on, and ignited.",
                "That is what a nation is made of.",
        };

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        g2.setColor(Color.white);

        int leftX  = panelX + 25;
        int rightX = panelX + panelW / 2 + 10;
        int startY = panelY + 85;
        int lineH  = 24;

        for (String line : left) {
            g2.drawString(line, leftX, startY);
            startY += lineH;
        }

        startY = panelY + 85;
        for (String line : right) {
            g2.drawString(line, rightX, startY);
            startY += lineH;
        }

        // prompt
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
        g2.setColor(new Color(200, 200, 200));
        g2.drawString("[ ENTER ] to continue", panelX + panelW - 210, panelY + panelH - 15);

        // close
        if (gp.keyP.enterPressed) {
            gp.keyP.enterPressed = false;
            showPoemPanel = false;
        }

    }
    public void drawMiUltimoAdiosPanel() {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int panelW = gp.tileSize * 15;
        int panelH = gp.tileSize * 10;
        int panelX = gp.screenWidth  / 2 - panelW / 2;
        int panelY = gp.screenHeight / 2 - panelH / 2;
        drawSubWindow(panelX, panelY, panelW, panelH);

        // title
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F));
        g2.setColor(new Color(255, 220, 80));
        String title = "Mi Último Adiós  —  José Rizal, 1896";
        int titleX = getXforCenter(title);
        g2.drawString(title, titleX, panelY + 42);

        String[] left = {
                "Farewell, adored Fatherland, region of the sun caressed.",
                "I give you my life, now saddened and repressed.",
                "Were it brighter, fresher, or more blessed,",
                "still would I give it for your sake and your rest.",
                "",
                "On the field of battle, fighting with delirium,",
                "others give their lives without hesitation.",
                "It matters not where—cypress, laurel,",
                "or lily, gibbet or open field, combat or cruel martyrdom.",
                "",
                "I die as I see the sky glow with color,",
                "announcing the day after a night of pain.",
                "If you need pigment to dye your dawn,",
                "pour out my blood to give it a crimson stain.",
        };

        String[] right = {
                "My dreams when I was a child,",
                "my dreams when a young man full of vigor,",
                "Farewell, parents and brothers,",
                "fragments of my soul, friends of my childhood in the lost home.",
                "",
                "Give thanks that I rest from the wearisome day.",
                "I go where there are no slaves, no hangmen, no oppressors,",
                "where faith does not kill, and where God reigns.",
                "",
                "Farewell, loved ones. To die is to rest.",
        };

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 19F));
        g2.setColor(Color.white);

        int leftX  = panelX + 22;
        int rightX = panelX + panelW / 2 + 8;
        int startY = panelY + 74;
        int lineH  = 22;

        for (String line : left)  { g2.drawString(line, leftX,  startY); startY += lineH; }

        startY = panelY + 74;
        for (String line : right) { g2.drawString(line, rightX, startY); startY += lineH; }

        // prompt
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
        g2.setColor(new Color(200, 200, 200));
        g2.drawString("[ ENTER ] to continue", panelX + panelW - 210, panelY + panelH - 15);

        if (gp.keyP.enterPressed) {
            gp.keyP.enterPressed = false;
            showPoemPanel = false;
        }
    }

    public int getItemIndexOnSlot(){
        int itemIndex = slotCol + (slotRow*5);
        return itemIndex;
    }
    public void drawSubWindow(int x, int y, int width, int height){
        drawSubWindow(this.g2, x, y, width, height);
    }
    public void drawSubWindow(Graphics2D g2, int x, int y, int width, int height){
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
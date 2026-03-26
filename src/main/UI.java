package main;
import entity.Entity;
import object.OBJ_EXP;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    public Font maruMonica;

    //BUFFERED IMAGES FOR OBJECT
//  BufferedImage quilImage;
    BufferedImage full_exp, half_exp, empty_exp;

    public boolean messageOn = false;
    public boolean showPoemPanel = false;

    public String message = "";
    public String currentDialogue = "";
    public String currentSpeakerName = "";

    int msgCounter = 0;
    public int commandNum = 0;
    public int titleScreenState = 0;
    public int optionSubState = 0;

    public int slotCol = 0;
    public int slotRow = 0;

    public int questPageNum = 0;

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
            drawHints();
            drawQuestHUD();

            if (messageOn){
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35F));
                g2.setColor(Color.white);
                int x = getXforCenter(message);
                g2.drawString(message, x, gp.tileSize * 8);
                msgCounter++;
                if (msgCounter > 120){   // show for ~3 seconds at 60fps
                    messageOn = false;
                    msgCounter = 0;
                }
            }

            if (showPoemPanel) {
                drawPoemPanel();
            }
        }

        //PAUSE STATE
        if (gp.gameState == gp.pauseState ){

            drawPlayerExp();
            drawPauseScreen();
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


//        g2.drawImage(quilImage, gp.tileSize/3, gp.tileSize/3, gp.tileSize, gp.tileSize, null);
//        g2.drawString("x "+ gp.player.hasQuil, 100, 65);

    }
    public void drawPlayerExp() {
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int spacing = gp.tileSize + 6;

        // draw all slots as empty first
        for (int i = 0; i < gp.player.maxExp; i++) {
            g2.drawImage(empty_exp, x, y, null);
            x += spacing;
        }

        // reset and draw filled exp on top
        x = gp.tileSize / 2;
        for (int i = 0; i < gp.player.exp; i++) {
            g2.drawImage(full_exp, x, y, null);
            x += spacing;
        }
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
        g2.drawString("[ P ]          to pause", hintRightX, hintRightY); hintRightY += gp.tileSize/2;


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

            text = "Quest Panel: Q";
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

        if (gp.keyP.enterPressed) {
            gp.keyP.enterPressed = false;
            // find current NPC and advance
            for (int i = 0; i < gp.npc.length; i++) {
                if (gp.npc[i] != null) {
                    int dx = Math.abs(gp.npc[i].worldX - gp.player.worldX);
                    int dy = Math.abs(gp.npc[i].worldY - gp.player.worldY);
                    if (dx < gp.tileSize * 2 && dy < gp.tileSize * 2) {
                        gp.npc[i].speak();
                        break;
                    }
                }
            }
        }

        if (currentSpeakerName != null && !currentSpeakerName.isEmpty()) {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35F));

            int nameX = x + gp.tileSize;
            int nameY = y - 15;

            // WHITE OUTLINE
            g2.setColor(Color.black);
            g2.drawString(currentSpeakerName, nameX - 1, nameY - 1);
            g2.drawString(currentSpeakerName, nameX + 1, nameY - 1);
            g2.drawString(currentSpeakerName, nameX - 1, nameY + 1);
            g2.drawString(currentSpeakerName, nameX + 1, nameY + 1);

            // GOLD TEXT ON TOP
            g2.setColor(new Color(255, 220, 80));
            g2.drawString(currentSpeakerName, nameX, nameY);
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD,25F));
        g2.setColor(Color.white);
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialogue.split("\n")){
            g2.drawString(line, x, y);
            y += 40;
        }

        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
        g2.setColor(new Color(200, 200, 200));
        int promptX = (gp.tileSize * 2) + (gp.screenWidth - (gp.tileSize*4)) - gp.tileSize;
        int promptY = (gp.tileSize * 8) + (gp.tileSize * 3) - 15;
        g2.drawString("[ ENTER ]", promptX - 60, promptY);
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
    public void drawInventory(){

        //FRAME
        int frameX = gp.tileSize * 13;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 5;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        //SLOTS
        int totalGridWidth = gp.tileSize * 5;
        int totalGridHeight = gp.tileSize * 4;

        final int slotXstart = frameX + (frameWidth - totalGridWidth) / 2;
        final int slotYstart = frameY + (frameHeight - totalGridHeight) / 2;
        int slotX = slotXstart;
        int slotY = slotYstart;


        // DRAW PLAYER ITEM
        for (int i = 0; i < gp.player.inventory.size(); i++) {

            g2.drawImage(gp.player.inventory.get(i).image, slotX, slotY, gp.tileSize, gp.tileSize, null);

            slotX += gp.tileSize;

            if (i == 4 || i  == 9 || i ==14 ){
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
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10 );

        //DESC FRAME
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight =  gp.tileSize*3;

        int textX = dFrameX + 20;
        int textY = dFrameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(28F));

        int itemIndex = getItemIndexOnSlot();

        if (itemIndex < gp.player.inventory.size()){

            if (itemIndex < gp.player.inventory.size()){

                drawSubWindow(dFrameX,dFrameY,dFrameWidth,dFrameHeight);

                for (String line: gp.player.inventory.get(itemIndex).description.split("\n")){

                    g2.drawString(line, textX, textY);
                    textY += 32;
                }
            }



        }

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
            boolean q1done = gp.questManager.isQuestCompleted(QuestManager.QUEST1);

            //QUEST 1
            g2.setFont(g2.getFont().deriveFont(Font.BOLD | Font.ITALIC, 27f));
            g2.setColor(q1done ? new Color(100, 230, 100) : new Color(255, 220, 80));
            g2.drawString("Quest 1: \"Familya Rizal\"" + (q1done ? "    COMPLETE" : ""), frameX + gp.tileSize, y += gp.tileSize);

            g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 27f));
            g2.setColor(Color.lightGray);
            y += 28;
            g2.drawString("Find all of Pepe's siblings and bring them home.", frameX + gp.tileSize, y);
            y += gp.tileSize/2;

            String[] siblingNames = {
                    "Saturnina",
                    "Paciano",
                    "Narcisa",
                    "Olimpia",
                    "Lucia",
                    "Maria",
                    "Josefa",
                    "Trinidad",
                    "Soledad",
                    "Concepcion"
            };

            boolean[] sibFound = new boolean[10];
            for (int i = 0; i < gp.npc.length; i++) {
                if (gp.npc[i] instanceof entity.NPC_Sibling) {
                    entity.NPC_Sibling s = (entity.NPC_Sibling) gp.npc[i];
                    int slot = i - 2;
                    if (slot >= 0 && slot < 9) {
                        sibFound[slot] = s.isFollowing;
                    }
                }
            }
            sibFound[9] = !q1done && gp.questManager.conchaVisited;

            int col1X = frameX + gp.tileSize;
            int col2X = frameX + (frameWidth / 2) + 10;
            int rowY  = y;
            int lineH = 30;

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 26f));
            for (int i = 0; i < 10; i++) {
                boolean found = sibFound[i];
                g2.setColor(found ? new Color(80, 220, 80) : Color.white);
                String label = (found ? "- " : "- ") + siblingNames[i];
                int cx = (i % 2 == 0) ? col1X : col2X;
                g2.drawString(label, cx, rowY);
                if (i % 2 == 1) rowY += lineH;
            }
            y = rowY;

            //QUEST 2
            boolean q2active    = gp.questManager.isQuestActive(QuestManager.QUEST2);
            boolean q2completed = gp.questManager.isQuestCompleted(QuestManager.QUEST2);
            int stage = gp.questManager.quest2Stage;

            g2.setFont(g2.getFont().deriveFont(Font.BOLD | Font.ITALIC, 27f));
            Color titleColor = q2completed ? new Color(100, 230, 100)
                    : (q2active   ? new Color(255, 220, 80)
                    : Color.gray);
            g2.setColor(titleColor);
            g2.drawString("Quest 2: \"Pangangaral ng mga Tiyo\"" + (q2completed ? "    COMPLETE" : ""), frameX + gp.tileSize, y);

            g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 27f));
            g2.setColor(q2active || q2completed ? Color.lightGray : Color.gray);
            y += 28;
            g2.drawString("Learn valuable lessons from your uncles.", frameX + gp.tileSize, y);
            lineH = 24;
            y += lineH;

            if (!q2active && !q2completed) {
                g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 27f));
                g2.setColor(Color.gray);
                g2.drawString("[Complete Quest 1 to unlock]", frameX + gp.tileSize*6, y);
            } else {
                lineH = 24;
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 27f));

                // Uncle Jose
                boolean joseDone = q2completed || stage >= QuestManager.JOSE_DONE;
                g2.setColor(joseDone ? new Color(80, 220, 80) : Color.white);
                g2.drawString((joseDone ? "- " : "- ") + "Meet Uncle Jose Alberto", frameX + gp.tileSize, y);
                if (!joseDone && stage == QuestManager.JOSE_WAITING) {
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 25f));
                    g2.setColor(new Color(200, 200, 100));
                    y += 24;
                    g2.drawString("  Paint Buckets: " + gp.questManager.countItem("Paint Bucket") + "/6"
                                    + "   Brush: " + gp.questManager.countItem("Paintbrush") + "/1"
                                    + "   Canvas: " + gp.questManager.countItem("Canvas") + "/1",
                            frameX + gp.tileSize, y);
                    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 27f));
                }
                y += lineH;

                // Uncle Manuel
                boolean manuelDone = q2completed || stage >= QuestManager.MANUEL_DONE;
                g2.setColor(manuelDone ? new Color(80, 220, 80)
                        : stage >= QuestManager.JOSE_DONE ? Color.white : Color.gray);
                g2.drawString((manuelDone ? "- " : "- ") + "Train with Uncle Manuel", frameX + gp.tileSize, y);
                if (stage == QuestManager.MANUEL_RUNNING) {
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 25f));
                    g2.setColor(new Color(200, 200, 100));
                    y += 24;
                    g2.drawString("  Checkpoints: " + gp.questManager.checkpointsHit + "/" + gp.questManager.TOTAL_CHECKPOINTS,
                            frameX + gp.tileSize, y);
                    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 27f));
                }
                y += lineH;

                // Uncle Gregorio
                boolean gregorioDone = q2completed || stage >= QuestManager.GREGORIO_DONE;
                g2.setColor(gregorioDone ? new Color(80, 220, 80)
                        : stage >= QuestManager.MANUEL_DONE ? Color.white : Color.gray);
                g2.drawString((gregorioDone ? "- " : "- ") + "Study with Uncle Gregorio", frameX + gp.tileSize, y);
                if (stage == QuestManager.GREGORIO_WAITING) {
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 25f));
                    g2.setColor(new Color(200, 200, 100));
                    y += 24;
                    g2.drawString("  Quill: " + gp.questManager.countItem("Quill") + "/1"
                                    + "   Notebook: " + gp.questManager.countItem("Notebook") + "/1",
                            frameX + gp.tileSize, y);
                }
            }
        }

        // ===== Chapter 2 page =====
        else if (questPageNum == 1) {

        }

        // ===== Chapter 3 page =====
        else if (questPageNum == 2) {

        }

        // ===== Chapter 4 page =====
        else if (questPageNum == 3) {

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

    public int getItemIndexOnSlot(){
        int itemIndex = slotCol + (slotRow*5);
        return itemIndex;
    }

    public void drawQuestHUD() {
        if (gp.questManager == null) return;

        int currentQ = gp.questManager.currentQuest;
        if (gp.questManager.isQuestCompleted(currentQ) &&
                currentQ == QuestManager.QUEST1) return;

        int panelW = gp.tileSize * 7;
        int panelH = gp.tileSize * 2;
        int panelX = gp.screenWidth - panelW - (gp.tileSize / 2);
        int panelY = gp.tileSize / 2;

        drawSubWindow(panelX, panelY, panelW, panelH);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22F));

        if (currentQ == main.QuestManager.QUEST1 &&
                gp.questManager.isQuestActive(main.QuestManager.QUEST1)) {

            g2.setColor(new Color(255, 220, 80));
            g2.drawString("Familya Rizal", panelX + 12, panelY + 28);
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(20F));

            if (gp.questManager.quest1Stage == main.QuestManager.QUEST1_NOT_STARTED) {
                g2.drawString("Talk to Nanay Teodora.", panelX + 12, panelY + 52);

            } else {
                int found    = gp.questManager.siblingsFound;
                int required = gp.questManager.SIBLINGS_REQUIRED + 1;
                g2.drawString("Siblings: " + found + " / " + required, panelX + 12, panelY + 52);

                if (found >= required) {
                    g2.setColor(new Color(100, 255, 100));
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.drawString("Go to the golden circle!", panelX + 12, panelY + 72);
                }
            }
        }

        else if (currentQ == QuestManager.QUEST2 &&
                gp.questManager.isQuestActive(QuestManager.QUEST2)) {

            g2.setColor(new Color(255, 220, 80));
            g2.drawString("Pangangaral ng mga Tiyo", panelX + 12, panelY + 28);

            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(20F));

            int stage = gp.questManager.quest2Stage;

            if (stage == QuestManager.JOSE_INACTIVE) {
                g2.drawString("Find Uncle Jose Alberto.", panelX + 12, panelY + 52);

            } else if (stage == QuestManager.JOSE_WAITING) {
                g2.drawString("Buckets: 6  Brush: 1  Canvas: 1", panelX + 12, panelY + 52);
                if (gp.questManager.hasAllArtSupplies()) {
                    g2.setColor(new Color(100, 255, 100));
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.drawString("Return to Uncle Jose!", panelX + 12, panelY + 72);
                }

            } else if (stage == QuestManager.JOSE_DONE) {
                g2.drawString("Find Uncle Manuel.", panelX + 12, panelY + 52);

            } else if (stage == QuestManager.MANUEL_RUNNING) {
                int hit   = gp.questManager.checkpointsHit;
                int total = gp.questManager.TOTAL_CHECKPOINTS;
                g2.drawString("Checkpoints: " + hit + " / " + total, panelX + 12, panelY + 52);
                if (gp.questManager.courseCompleted) {
                    g2.setColor(new Color(100, 255, 100));
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.drawString("Return to Uncle Manuel!", panelX + 12, panelY + 72);
                }

            } else if (stage == QuestManager.MANUEL_DONE) {
                g2.drawString("Find Uncle Gregorio.", panelX + 12, panelY + 52);

            } else if (stage == QuestManager.GREGORIO_WAITING) {
                g2.drawString("Quill: 1  Notebook: 1", panelX + 12, panelY + 52);
                if (gp.questManager.hasWritingSupplies()) {
                    g2.setColor(new Color(100, 255, 100));
                    g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 18F));
                    g2.drawString("Return to Uncle Gregorio!", panelX + 12, panelY + 72);
                }

            } else if (stage == QuestManager.GREGORIO_DONE) {
                g2.drawString("Quest 2 Complete!", panelX + 12, panelY + 52);
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
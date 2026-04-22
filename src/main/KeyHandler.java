package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;

    public boolean upPressed, downPressed, leftPressed, rightPressed, fPressed, cPressed, qPressed, enterPressed;

    //DEBUGGING
    boolean checkDrawTime = false;

    public KeyHandler(GamePanel gp){
        this.gp = gp;

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        // LOGIN STATE
        if (gp.gameState == gp.loginState) {
            char ch = e.getKeyChar();
            gp.loginPanel.handleKey(code, ch);
            return;
        }
        //TITLE STATE
        if (gp.gameState == gp.titleState) {
            titleState(code);
        }
        //PLAY STATE
        if (gp.gameState == gp.playState) {
            playState(code);
        }

        //DIALOGUE STATE
        else if (gp.gameState == gp.dialogueState) {
            dialogueState(code);
        }
        //CUTSCENE STATE
        else if (gp.gameState == gp.cutsceneState) {
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                gp.cutsceneManager.advance();
            }
        }
        //CHARACTER STATE
        else if (gp.gameState == gp.characterState) {
            characterState(code);
        }
        //OPTION STATE
        else if (gp.gameState == gp.optionState) {
            optionState(code);
        }
        //QUEST STATE
        else if (gp.gameState == gp.questState) {
            questState(code);
        }
        //NEW GAME
        else if (gp.gameState == gp.newGameConfirmState) {
            newGameConfirmState(code);
        }
        else if (gp.gameState == gp.dexState) {
            gp.npcDexUI.handleKey(code);
        }
    }

    public void titleState(int code){


        //MENU SCREEN
        if (gp.ui.titleScreenState == 0){

            if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 4;
                }

            }
            if(code == KeyEvent.VK_S  || code == KeyEvent.VK_DOWN){
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 4) {
                    gp.ui.commandNum = 0;
                }
            }

            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {

                //new game
                if (gp.ui.commandNum == 0) {
                    gp.ui.newGameConfirmCursor = 0;
                    gp.inputDelay = 20;
                    gp.gameState = gp.newGameConfirmState;
                }

                //load game
                if (gp.ui.commandNum == 1){
                    if (gp.saveManager.hasSave()) {
                        if (gp.saveManager.load()) {
                            gp.gameState = gp.playState;
                            gp.playMusic(0);
                        }
                    } else {
                        gp.ui.titleScreenState = 4;
                        gp.ui.commandNum = 0;
                    }
                }

                //credits
                if (gp.ui.commandNum == 2){
                    gp.ui.titleScreenState = 2;
                }

                //log out
                if (gp.ui.commandNum == 3){
                    gp.stopMusic();
                    gp.userManager.logout();
                    gp.loginPanel.reset();
                    gp.gameState = gp.loginState;
                    gp.ui.commandNum = 0;
                }

                //quit
                if (gp.ui.commandNum == 4){
                    System.exit(0);
                }
            }
        }

        //CUTSCENE SCREEN
        else if (gp.ui.titleScreenState == 1){

            if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 1;
                }

            }
            if(code == KeyEvent.VK_S  || code == KeyEvent.VK_DOWN){
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 1) {
                    gp.ui.commandNum = 0;
                }
            }

            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                if (gp.ui.commandNum == 0) {
                    gp.cutsceneManager.startIntroCutscene();
                }
                if (gp.ui.commandNum == 1){
                    gp.ui.titleScreenState = 0;
                }
            }
        }


        //CREDITS SCREEN
        else if (gp.ui.titleScreenState == 2){

            if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 1;
                }

            }
            if(code == KeyEvent.VK_S  || code == KeyEvent.VK_DOWN){
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 1) {
                    gp.ui.commandNum = 0;
                }
            }

            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                //GO BACK
                if (gp.ui.commandNum == 0){
                    gp.ui.titleScreenState = 0;
                }
                //NEXT PAGE
                if (gp.ui.commandNum == 1){
                    gp.ui.titleScreenState = 3;
                }
            }
        }

        //CREDITS SCREEN SECOND PAGE
        else if (gp.ui.titleScreenState == 3){

            if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 1;
                }

            }
            if(code == KeyEvent.VK_S  || code == KeyEvent.VK_DOWN){
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 1) {
                    gp.ui.commandNum = 0;
                }
            }

            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                //GO BACK
                if (gp.ui.commandNum == 0){
                    gp.ui.titleScreenState = 2;
                }
            }
        }

        // NO SAVE FILE SCREEN
        else if (gp.ui.titleScreenState == 4) {
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                gp.ui.titleScreenState = 0;
                gp.ui.commandNum = 0;
            }
        }

    }
    public void playState(int code){
        if (gp.ui.quizPanelOpen) {
            gp.ui.quizPanel.handleKey(code);
            return;
        }

        if(code == KeyEvent.VK_W){
            upPressed = true;
        }
        if(code == KeyEvent.VK_S){
            downPressed = true;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = true;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = true;
        }
        //press P to save game
        if(code == KeyEvent.VK_P){
            gp.saveManager.save();
            gp.ui.showMessage("Game Saved!");
            return;
        }
        //press F to speak to NPC
        if(code == KeyEvent.VK_F){
            fPressed = true;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        //press C to view character stats
        if (code == KeyEvent.VK_C){
            gp.gameState = gp.characterState;
            cPressed = true;
        }
        //press Q to view quest
        if (code == KeyEvent.VK_Q){
            gp.gameState = gp.questState;
            qPressed = true;
        }
        //press ESC to open options
        if (code == KeyEvent.VK_ESCAPE){

            gp.stopMusic();
            gp.gameState = gp.optionState;
        }
        //press R to open dex
        if (code == KeyEvent.VK_R) {
            gp.gameState = gp.dexState;
        }

        //Debugging
        if (code == KeyEvent.VK_T){
            if(checkDrawTime == false){
                checkDrawTime = true;
            } else if (checkDrawTime == true) {
                checkDrawTime = false;
            }
            return;
        }
    }
    public void dialogueState(int code){
        if (gp.ui.quizPanelOpen) {
            gp.ui.quizPanel.handleKey(code);
            return;
        }

        if (code == KeyEvent.VK_F) {
            gp.gameState = gp.playState;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
    }
    public void characterState(int code){
        if (code == KeyEvent.VK_C) {
            gp.gameState = gp.playState;
        }

        if (code == KeyEvent.VK_ENTER) {
            int itemIndex = gp.ui.getItemIndexOnSlot();

            if (itemIndex < gp.player.inventory.size()) {
                entity.Entity item = gp.player.inventory.get(itemIndex);
                if (item.name.equals("Sa Aking Mga Kabata")) {
                    gp.gameState = gp.playState;
                    gp.ui.showPoemPanel = true;
                    gp.ui.activeLetter = item.name;

                } else if (item.name.equals("Draft of Noli Me Tangere")) {
                    gp.gameState = gp.playState;
                    gp.ui.activeLetter = "Draft of Noli Me Tangere";
                    gp.ui.showPoemPanel = true;

                }
                else if (item.name.equals("Draft of El Filibusterismo")) {
                    gp.gameState = gp.playState;
                    gp.ui.activeLetter = "Draft of El Filibusterismo";
                    gp.ui.showPoemPanel = true;
                } else if (item.name.equals("Mi Ultimo Adios")) {
                    gp.gameState = gp.playState;
                    gp.ui.activeLetter = "Mi Ultimo Adios";
                    gp.ui.showPoemPanel = true;
                }

            }
        }

        if (code == KeyEvent.VK_W){
            if (gp.ui.slotRow != 0){
                gp.ui.slotRow--;
                //gp.playSE([index]);
            }
        }
        if (code == KeyEvent.VK_A){
            if (gp.ui.slotCol != 0){
                gp.ui.slotCol--;
                //gp.playSE([index]);
            }
        }
        if (code == KeyEvent.VK_S){
            if (gp.ui.slotRow != 3){
                gp.ui.slotRow++;
                //gp.playSE([index]);
            }
        }
        if (code == KeyEvent.VK_D){
            if (gp.ui.slotCol != 4){
                gp.ui.slotCol++;
                //gp.playSE([index]);
            }
        }
    }
    public void questState(int code){
        if (code == KeyEvent.VK_Q) {
            gp.gameState = gp.playState;
        }

        // A / LEFT > previous quest page
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            if (gp.ui.questPageNum > 0) gp.ui.questPageNum--;
        }

        // D / RIGHT > next quest page
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            if (gp.ui.questPageNum < 3) gp.ui.questPageNum++;
        }


    }
    public void optionState(int code){

        if (code == KeyEvent.VK_ESCAPE) {

            gp.playMusic(0);
            gp.gameState = gp.playState;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

        int maxCmdNum = 0;
        switch (gp.ui.optionSubState){
            case 0:
                maxCmdNum = 6;
                break;
            case 3:
                maxCmdNum = 2;
                break;
        }

        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
            gp.ui.commandNum--;
            //gp.playSE(index here);
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = maxCmdNum;
            }

        }
        if(code == KeyEvent.VK_S  || code == KeyEvent.VK_DOWN){
            gp.ui.commandNum++;
            //gp.playSE(index here);
            if (gp.ui.commandNum > maxCmdNum) {
                gp.ui.commandNum = 0;
            }
        }

        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT){
            if (gp.ui.optionSubState == 0){

                //MUSIC
                if (gp.ui.commandNum == 1 && gp.music.volumeScale > 0){
                    gp.music.volumeScale--;
                    gp.music.checkVolume();
                }
            }

            if (gp.ui.optionSubState == 0){

                //SOUND EFFECTS
                if (gp.ui.commandNum == 2 && gp.sound.volumeScale > 0){
                    gp.sound.volumeScale--;
                }
            }
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT){
            if (gp.ui.optionSubState == 0){

                //MUSIC
                if (gp.ui.commandNum == 1 && gp.music.volumeScale < 5){
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                }
            }

            if (gp.ui.optionSubState == 0){

                //SOUND EFFECTS
                if (gp.ui.commandNum == 2 && gp.sound.volumeScale < 5){
                    gp.sound.volumeScale++;
                }
            }
        }

    }
    public void newGameConfirmState(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.ui.newGameConfirmCursor = 0;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.ui.newGameConfirmCursor = 1;
        }
        if (code == KeyEvent.VK_ENTER && gp.inputDelay <= 0) {
            if (gp.ui.newGameConfirmCursor == 0) {
                java.io.File sf = gp.userManager.getSaveFile();
                if (sf.exists()) sf.delete();
                main.NPCDatabase.deleteForUser(gp.userManager.getCurrentUser());
                gp.resetGame();
                gp.ui.newGameConfirmCursor = 0;
                gp.cutsceneManager.startIntroCutscene();
            } else {
                gp.gameState = gp.titleState;
                gp.ui.titleScreenState = 0;
                gp.ui.commandNum = 0;
                gp.ui.newGameConfirmCursor = 0;
            }
        }
    }



    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
        if(code == KeyEvent.VK_ENTER){
            enterPressed = false;
        }
    }
}
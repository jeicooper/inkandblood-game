package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;

    public boolean upPressed, downPressed, leftPressed, rightPressed, fPressed, cPressed, enterPressed;

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

        //TITLE STATE
        if (gp.gameState == gp.titleState) {
            titleState(code);
        }
        //PLAY STATE
        if (gp.gameState == gp.playState) {
            playState(code);
        }
        //PAUSE STATE
        else if (gp.gameState == gp.pauseState) {
            pauseState(code);
        }
        //DIALOGUE STATE
        else if (gp.gameState == gp.dialogueState) {
            dialogueState(code);
        }
        //CHARACTER STATE
        else if (gp.gameState == gp.characterState) {
            characterState(code);
        }
        //OPTION STATE
        else if (gp.gameState == gp.optionState) {
            optionState(code);
        }
    }

    public void titleState(int code){


        //MENU SCREEN
        if (gp.ui.titleScreenState == 0){

            if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 3;
                }

            }
            if(code == KeyEvent.VK_S  || code == KeyEvent.VK_DOWN){
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 3) {
                    gp.ui.commandNum = 0;
                }
            }

            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {

                //new game
                if (gp.ui.commandNum == 0) {
                    gp.ui.titleScreenState = 1;
                }

                //load game
                if (gp.ui.commandNum == 1){
                    //load game
                }

                //credits
                if (gp.ui.commandNum == 2){
                    gp.ui.titleScreenState = 2;
                }

                if (gp.ui.commandNum == 3){
                    System.exit((0));
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
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
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

    }
    public void playState(int code){
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
        //press P to pause game
        if(code == KeyEvent.VK_P){
            gp.stopMusic();
            gp.gameState = gp.pauseState;
            return;
        }
        //press F to speak to NPC
        if(code == KeyEvent.VK_F){
            fPressed = true;
        }
        //press C to view character stats
        if (code == KeyEvent.VK_C){
            gp.gameState = gp.characterState;
            cPressed = true;
        }

        if (code == KeyEvent.VK_ESCAPE){

            gp.stopMusic();
            gp.gameState = gp.optionState;
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
    public void pauseState(int code){
        if(code == KeyEvent.VK_P){
            gp.playMusic(0);
            gp.gameState = gp.playState;
            return;
        }

        if (code == KeyEvent.VK_ESCAPE){

            gp.stopMusic();
            gp.gameState = gp.titleState;
            gp.ui.titleScreenState = 0;
        }
    }
    public void dialogueState(int code){
        if (code == KeyEvent.VK_F) {
            gp.gameState = gp.playState;
        }
    }
    public void characterState(int code){
        if (code == KeyEvent.VK_C) {
            gp.gameState = gp.playState;
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
                maxCmdNum = 5;
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
                if (gp.ui.commandNum == 2 && gp.sound.volumeScale > 0){
                    gp.sound.volumeScale++;
                }
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
    }
}

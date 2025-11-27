package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;

    public boolean upPressed, downPressed, leftPressed, rightPressed, fPressed, cPressed;

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
                if (gp.ui.commandNum == 0) {
                    gp.ui.titleScreenState = 1;
                }
                if (gp.ui.commandNum == 1){
                    //load game
                }
                if (gp.ui.commandNum == 2){
                    gp.ui.titleScreenState = 2;


                }
                if (gp.ui.commandNum == 4){
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
                    gp.ui.commandNum = 0;
                }

            }
            if(code == KeyEvent.VK_S  || code == KeyEvent.VK_DOWN){
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 0) {
                    gp.ui.commandNum = 0;
                }
            }

            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                if (gp.ui.commandNum == 0){
                    gp.ui.titleScreenState = 0;
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
            gp.gameState = gp.titleState;
            gp.ui.titleScreenState = 0;
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

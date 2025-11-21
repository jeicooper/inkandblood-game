package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;

    public boolean upPressed, downPressed, leftPressed, rightPressed, fPressed;

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


        //PLAY STATE
        if (gp.gameState == gp.playState) {
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
                gp.gameState = gp.pauseState;
                return;
            }
            //press F to speak to NPC
            if(code == KeyEvent.VK_F){
                fPressed = true;
            }
            if (code == KeyEvent.VK_ESCAPE){

                gp.stopMusic();
                gp.gameState = gp.titleState;
                gp.ui.titleScreenState = 0;
            }
        }

        //DEBUGGING
        if (code == KeyEvent.VK_T){

        }

        //PAUSE STATE
        else if (gp.gameState == gp.pauseState) {
            if(code == KeyEvent.VK_P){
                gp.gameState = gp.playState;
                return;
            }
        }

        //DIALOGUE STATE
        else if (gp.gameState == gp.dialogueState) {
            if (code == KeyEvent.VK_F) {
                gp.gameState = gp.playState;
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

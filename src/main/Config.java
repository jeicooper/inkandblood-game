package main;

import java.io.*;

public class Config {

    GamePanel gp;

    public Config (GamePanel gp){
        this.gp = gp;
    }

    public void saveConfig(){

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));

            //Full Screen
            if(gp.fullscreenOn == true) {
                bw.write("On");
            }
            if (gp.fullscreenOn == false) {
                bw.write("Off");
            }
            bw.newLine();

            bw.write(String.valueOf(gp.music.volumeScale));
            bw.newLine();

            bw.write(String.valueOf(gp.sound.volumeScale));
            bw.newLine();

            bw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadConfig(){

        try {
            BufferedReader br = new BufferedReader(new FileReader("config.txt"));

            String s = br.readLine();

            if (s != null) {
                if ("On".equals(s)) {
                    gp.fullscreenOn = true;
                } else if ("Off".equals(s)) {
                    gp.fullscreenOn = false;
                }
            } else {
                // Handle the case where the file is empty/corrupt
                // Perhaps set a default value or log an error
                System.err.println("Configuration file is empty or corrupt. Using default settings for fullscreen.");
            }

            s = br.readLine(); // Reads Music Volume
            if (s != null) {
                gp.music.volumeScale = Integer.parseInt(s);
            }

            s = br.readLine(); // Reads Music Volume
            if (s != null) {
                gp.sound.volumeScale = Integer.parseInt(s);
            }

            br.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}

package main;

import java.io.*;

public class Config {

    GamePanel gp;

    private File getConfigFile() {
        String home = System.getProperty("user.home");
        File dir = new File(home, "InkAndBlood");
        dir.mkdirs();
        return new File(dir, "config.txt");
    }

    public Config(GamePanel gp) {
        this.gp = gp;
    }

    public void saveConfig() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(getConfigFile()));

            if (gp.fullscreenOn == true) {
                bw.write("On");
            } else {
                bw.write("Off");
            }
            bw.newLine();

            bw.write(String.valueOf(gp.music.volumeScale));
            bw.newLine();

            bw.write(String.valueOf(gp.sound.volumeScale));
            bw.newLine();

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        File configFile = getConfigFile();

        if (!configFile.exists()) {
            gp.fullscreenOn = false;
            gp.music.volumeScale = 3;
            gp.sound.volumeScale = 3;
            return;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(configFile));

            String s = br.readLine();
            if (s != null) {
                if ("On".equals(s)) gp.fullscreenOn = true;
                else if ("Off".equals(s)) gp.fullscreenOn = false;
            }

            s = br.readLine();
            if (s != null) gp.music.volumeScale = Integer.parseInt(s);

            s = br.readLine();
            if (s != null) gp.sound.volumeScale = Integer.parseInt(s);

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
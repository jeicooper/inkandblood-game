package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class TileManager {

    GamePanel gp;
    public Tile[]  tile;
    public int[][] mapTileNum;


    public TileManager(GamePanel gp){

        this.gp = gp;

        tile = new Tile[100];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/maps/Map_calamba.txt");
    }

    public void getTileImage(){
            setup(0, "grass", false);
            setup(1, "grass", false);
            setup(2, "grass", false);
            setup(3, "grass", false);
            setup(4, "grass", false);
            setup(5, "grass", false);
            setup(6, "grass", false);
            setup(7, "grass", false);
            setup(8, "grass", false);
            setup(9, "grass", false);
            setup(10, "grass", false);

            setup(11, "grass", false);
            setup(12, "path", false);

            setup(13, "halfpath_up", false);
            setup(14, "halfpath_down", false);
            setup(15, "halfpath_left", false);
            setup(16, "halfpath_right", false);


            setup(17, "pathcorner_bottomright", false);
            setup(18, "pathcorner_bottomleft", false);
            setup(19, "pathcorner_topright", false);
            setup(20, "pathcorner_topleft", false);

            setup(21, "grasscorner_bottomright", false);
            setup(22, "grasscorner_bottomleft", false);
            setup(23, "grasscorner_topright", false);
            setup(24, "grasscorner_topleft", false);

            setup(25, "stone", true);
            setup(26, "stonegrass", true);
            setup(27, "brickwall", true);

            setup(28, "water", false);
            setup(29, "halfwater_down", true);
            setup(30, "halfwater_up", true);
            setup(31, "halfwater_left", true);
            setup(32, "halfwater_right", true);

            setup(33, "water_bottomleft", true);
            setup(34, "water_bottomright", true);
            setup(35, "water_topright", true);
            setup(36, "water_topleft", true);

            setup(37, "bush00", true);
            setup(38, "bush01", true);
            setup(39, "bush02", true);

            setup(40, "leaf00", true);
            setup(41, "leaf01", true);
            setup(42, "leaf02", true);

            setup(43, "rock00", true);
            setup(44, "rock01", true);

    }

    public void setup(int index, String imagePath, boolean collision){
        UtilityTool uTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imagePath + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    //GENERATES THE TILE MAP
    public void loadMap (String filePath){

        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));


            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow){

                String line = br.readLine();

                while(col < gp.maxWorldCol){

                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }
            }
            br.close();

        }catch (Exception e) {
        }


    }
    public void draw(Graphics2D g2){

        int worldCol = 0;
        int worldRow = 0;


        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY ){

                g2.drawImage(tile[tileNum].image, screenX, screenY, null);

            }
            worldCol++;


            if (worldCol == gp.maxWorldCol){
                worldCol = 0;
                worldRow++;
            }



        }
    }
}

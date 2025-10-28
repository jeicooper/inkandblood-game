package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
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
        loadMap("/maps/Map_calamba2.txt");
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

            setup(28, "water", true);
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

            setup(45, "sand", false);

            setup(46, "sand_top", true);
            setup(47, "sand_bottom", true);
            setup(48, "sand_left", true);
            setup(49, "sand_right", true);

            setup(50, "sand_topleft", true);
            setup(51, "sand_topright", true);

            setup(52, "sand_bottomleft", true);
            setup(53, "sand_bottomright", true);

            setup(54, "sand_middle1", false);
            setup(55, "sand_middle2", false);
            setup(56, "sand_middle3", false);
            setup(57, "sand_middle4", false);

            setup(58, "sand_grass_top", false);
            setup(59, "sand_grass_bottom", false);
            setup(60, "sand_grass_left", false);
            setup(61, "sand_grass_right", false);

            setup(62, "sand_grass_topleft", false);
            setup(63, "sand_grass_topright", false);

            setup(64, "sand_grass_bottomleft", false);
            setup(65, "sand_grass_bottomright", false);

            setup(66, "sand_grass_middle1", false);
            setup(67, "sand_grass_middle2", false);
            setup(68, "sand_grass_middle3", false);
            setup(69, "sand_grass_middle4", false);
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
    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            if (is == null) {
                System.out.println("Map file not found: " + filePath);
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < gp.maxWorldRow) {
                String[] numbers = line.trim().split("\\s+");
                for (int col = 0; col < numbers.length && col < gp.maxWorldCol; col++) {
                    try {
                        mapTileNum[col][row] = Integer.parseInt(numbers[col]);
                    } catch (NumberFormatException nfe) {
                        System.out.println("Invalid number at row " + row + " col " + col + ": '" + numbers[col] + "'");
                        mapTileNum[col][row] = 0; // fallback
                    }
                }
                row++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
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

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY ){

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

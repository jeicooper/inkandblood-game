package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class    OBJ_Quil extends SuperObject{

    GamePanel gp;
    public OBJ_Quil(GamePanel gp){

        this.gp = gp;
        name = "Quil";

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/quil.png"));

            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        }catch (IOException e){
            e.printStackTrace();
        }



    }
}

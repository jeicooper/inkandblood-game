package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Book extends SuperObject{

    GamePanel gp;
    public OBJ_Book(GamePanel gp){

        this.gp = gp;

        name = "Book";

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/book.png"));

            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        }catch (IOException e){
            e.printStackTrace();
        }
        collision = true;
    }
}

package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Book extends Entity {

    public OBJ_Book(GamePanel gp){
        super(gp);

        name = "Book";
        down1 = setup("/objects/book.png");
        collision = true;

        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 16;
        solidArea.height = 16;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}

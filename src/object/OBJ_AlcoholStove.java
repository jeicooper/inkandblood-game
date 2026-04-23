package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_AlcoholStove extends Entity {

    public OBJ_AlcoholStove(GamePanel gp) {
        super(gp);
        name        = "Alcohol Stove";
        image       = setup("/objects/lamp");
        description = "A small alcohol stove.\nSomething is hidden inside.";
        collision   = true;

        solidArea.x      = 0;
        solidArea.y      = 0;
        solidArea.width  = gp.tileSize;
        solidArea.height = gp.tileSize;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}

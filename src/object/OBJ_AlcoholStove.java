package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_AlcoholStove extends Entity {

    public OBJ_AlcoholStove(GamePanel gp) {
        super(gp);
        name        = "Alcohol Stove";
        image       = setup("/objects/alcohol_stove");
        description = "A small alcohol stove.\nSomething is hidden inside.";
        collision   = true;

        // use full-tile solid area so F-interaction registers like an NPC
        solidArea.x      = 0;
        solidArea.y      = 0;
        solidArea.width  = gp.tileSize;
        solidArea.height = gp.tileSize;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}

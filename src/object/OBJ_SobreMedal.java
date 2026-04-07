package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_SobreMedal extends Entity {

    public OBJ_SobreMedal (GamePanel gp) {
        super(gp);

        name = "Medal";
        image = setup("/objects/sobre_medal");

        description = "A medal awarded by\nFr. Ferrando for \noutstanding academic\nperformance.";
        collision   = false;

        solidArea.x      = 0;
        solidArea.y      = 0;
        solidArea.width  = gp.tileSize;
        solidArea.height = gp.tileSize;
    }
}

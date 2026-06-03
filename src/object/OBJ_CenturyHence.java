package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_CenturyHence extends Entity {

    public OBJ_CenturyHence(GamePanel gp) {
        super(gp);
        name        = "Draft: The Philippines a Century Hence";
        image       = setup("/objects/century hence");
        description = "Rizal's essay forecasting\nthe future of the Philippines.\n[ ENTER ] to pick up";
        collision   = true;

        solidArea.x       = 0;
        solidArea.y       = 0;
        solidArea.width   = gp.tileSize;
        solidArea.height  = gp.tileSize;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}

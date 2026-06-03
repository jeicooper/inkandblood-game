package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_IndolenceEssay extends Entity {

    public OBJ_IndolenceEssay(GamePanel gp) {
        super(gp);
        name        = "Draft: The Indolence of the Filipinos";
        image       = setup("/objects/indolence");
        description = "Rizal's essay defending\nFilipinos against accusations\nof laziness.\n[ ENTER ] to pick up";
        collision   = true;

        solidArea.x       = 0;
        solidArea.y       = 0;
        solidArea.width   = gp.tileSize;
        solidArea.height  = gp.tileSize;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_MalolosLetter extends Entity {

    public OBJ_MalolosLetter(GamePanel gp) {
        super(gp);
        name        = "Letter to the Women of Malolos";
        image       = setup("/objects/malolos");
        description = "Rizal's Tagalog letter\nhonoring the brave women\nof Malolos.\n[ ENTER ] to pick up";
        collision   = true;

        solidArea.x       = 0;
        solidArea.y       = 0;
        solidArea.width   = gp.tileSize;
        solidArea.height  = gp.tileSize;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
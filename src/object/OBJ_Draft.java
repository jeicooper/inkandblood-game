package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Draft extends Entity {
    public OBJ_Draft(GamePanel gp) {
        super(gp);
        name = "Draft of Noli Me Tangere";
        image = setup("/objects/book2");
        description = "A draft of Noli Me\nTangere.\n[ ENTER ] to read";
        collision = true;
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
    }
}
package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Quil extends Entity {

    public OBJ_Quil(GamePanel gp) {
        super(gp);
        name = "Quill";
        image = setup("/objects/quill");
        description = "A fine quill pen.\nBelongs to Uncle Gregorio.";
        collision = false;
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
    }
}
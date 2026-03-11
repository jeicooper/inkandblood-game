package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Notebook extends Entity {

    public OBJ_Notebook(GamePanel gp){
        super(gp);
        name = "Notebook";
        image = setup("/objects/book");
        description = "A blank notebook.";
        collision = false;
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
    }
}

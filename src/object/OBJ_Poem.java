package object;

import main.GamePanel;
import entity.Entity;

public class OBJ_Poem extends Entity {

    public OBJ_Poem (GamePanel gp) {
        super(gp);
        name = "Sa Aking Mga Kabata";
        image = setup("/objects/book");
        description = "A poem by Jose Rizal.\n[ ENTER ] to read";
        collision = false;
    }
}
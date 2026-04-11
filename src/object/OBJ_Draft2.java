package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Draft2 extends Entity {
    public OBJ_Draft2 (GamePanel gp) {
        super(gp);
        name = "Draft of El Filibusterismo";
        image = setup("/objects/book3");
        description = "A draft of El\nFilibusterismo.\n[ ENTER ] to read";
        collision = true;
        setHitbox();
    }
}
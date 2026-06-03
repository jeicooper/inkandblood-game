package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Book2 extends Entity {

    public OBJ_Book2(GamePanel gp) {
        super(gp);
        name        = "Republic Act No. 1425";
        image       = setup("/objects/19th_book2");
        description = "Republic Act No. 1425\n(The Rizal Law).\n[ ENTER ] to read";
        collision   = false;
    }
}
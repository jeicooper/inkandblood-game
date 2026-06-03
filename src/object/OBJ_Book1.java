package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Book1 extends Entity {

    public OBJ_Book1(GamePanel gp) {
        super(gp);
        name        = "Records for Qualification of a Hero";
        image       = setup("/objects/19th_book1");
        description = "Records for\nQualification of a\nHero.\n[ ENTER ] to read";
        collision   = false;
    }
}
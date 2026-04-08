package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Scalpel extends Entity {
    public OBJ_Scalpel(GamePanel gp) {
        super(gp);
        name = "Scalpel";
        image = setup("/objects/scalpel");
        description = "A medical scalpel.\nThe Symbolism of the Title.";
        collision = true;
    }
}
package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Portrait extends Entity {
    public OBJ_Portrait(GamePanel gp) {
        super(gp);
        name = "Portrait";
        image = setup("/objects/portrait");
        description = "A medical scalpel.\nThe Symbolism of the Title.";
        collision = true;
    }
}
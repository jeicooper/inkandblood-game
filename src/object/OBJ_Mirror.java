package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Mirror extends Entity {
    public OBJ_Mirror(GamePanel gp) {
        super(gp);
        name = "Mirror";
        image = setup("/objects/mirror");
        description = "A medical scalpel.\nThe Symbolism of the Title.";
        collision = true;
    }
}
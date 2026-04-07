package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Rosary extends Entity {
    public OBJ_Rosary(GamePanel gp) {
        super(gp);
        name = "Rosary";
        image = setup("/objects/rosary");
        description = "A medical scalpel.\nThe Symbolism of the Title.";
        collision = true;
    }
}
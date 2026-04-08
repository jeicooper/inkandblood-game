package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_DriedFlower extends Entity {
    public OBJ_DriedFlower(GamePanel gp) {
        super(gp);
        name = "Dried Flower";
        image = setup("/objects/dried_flower");
        description = "A dried flower.\nRepresenting Maria Clara.";
        collision = true;
    }
}
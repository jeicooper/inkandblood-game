package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Boots extends Entity {

    public OBJ_Boots(GamePanel gp) {
        super(gp);
        name = "Boots";
        image = setup("/objects/boots");
        description = "Enchanted boots from\nUncle Manuel. Gives\nincredible speed!";
        collision = false;
    }
}
package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Glasses extends Entity {

    public OBJ_Glasses (GamePanel gp){
        super(gp);
        name = "Glasses";
        image = setup("/objects/circular_glasses");
        description = "A worn pair of\nglasses.";
        collision = true;
    }
}
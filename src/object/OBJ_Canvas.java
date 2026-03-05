package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Canvas extends Entity {

    public OBJ_Canvas(GamePanel gp) {
        super(gp);
        name = "Canvas";
        image = setup("/objects/blank_canvas");
        description = "A blank canvas.\nNeeded for art lessons.";
        collision = false;
    }
}
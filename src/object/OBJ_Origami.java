package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Origami extends Entity{
    public OBJ_Origami (GamePanel gp){
        super(gp);
        name = "Origami Crane";
        image = setup("/objects/origami");
        description = "";
        collision = true;
    }
}

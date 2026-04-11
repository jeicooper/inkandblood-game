package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Newspaper extends Entity {

    public OBJ_Newspaper (GamePanel gp){
        super(gp);
        name = "Newspaper";
        image = setup("/objects/newspaper");
        description = "Containing today's news";
        collision = true;
    }
}
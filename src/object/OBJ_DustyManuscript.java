package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_DustyManuscript extends Entity{
    public OBJ_DustyManuscript (GamePanel gp){
        super(gp);
        name = "Dusty Manuscript";
        image = setup("/objects/manuscript");
        description = "";
        collision = true;
    }
}

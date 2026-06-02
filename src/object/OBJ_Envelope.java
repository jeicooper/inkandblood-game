package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Envelope extends Entity{
    public OBJ_Envelope (GamePanel gp){
        super(gp);
        name = "Envelope";
        image = setup("/objects/torn envelope");
        description = "";
        collision = true;
    }
}

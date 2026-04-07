package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_ScrapMetal extends Entity {
    public OBJ_ScrapMetal(GamePanel gp) {
        super(gp);
        name = "Scrap Metal";
        image = setup("/objects/scrap_metal");
        description = "A scrap metal.\nRepresenting Elias.";
        collision = true;
    }
}

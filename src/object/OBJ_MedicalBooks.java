package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_MedicalBooks extends Entity {
    public OBJ_MedicalBooks(GamePanel gp) {
        super(gp);
        name = "Medical Books";
        image = setup("/objects/Medical Books");
        description = "";
        collision = true;
    }
}
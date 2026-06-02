package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_MedicalBag extends Entity{
    public OBJ_MedicalBag (GamePanel gp){
        super(gp);
        name = "Medical Bag";
        image = setup("/objects/medicalbag");
        description = "";
        collision = true;
    }
}

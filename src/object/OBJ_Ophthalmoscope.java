package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Ophthalmoscope extends Entity{
    public OBJ_Ophthalmoscope (GamePanel gp){
        super(gp);
        name = "Ophthalmoscope";
        image = setup("/objects/opthalmoscope");
        description = "";
        collision = true;
    }
}

package object;

import entity.Entity;
import main.GamePanel;


public class OBJ_Quil extends Entity {

    public OBJ_Quil(GamePanel gp){
    super(gp);
        name = "Quil";
        image = setup("/objects/quil");

    }
}

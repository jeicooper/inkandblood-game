package object;

import entity.Entity;
import main.GamePanel;


public class OBJ_EXP extends Entity {

    public OBJ_EXP(GamePanel gp) {
        super(gp);

        name = "Exp";
        image = setup("/objects/empty_exp");
        image2 = setup("/objects/half_exp");
        image3 = setup("/objects/full_exp");
        image4 = setup("/objects/pepe_icon");

    }
}
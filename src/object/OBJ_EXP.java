package object;

import entity.Entity;
import main.GamePanel;


public class OBJ_EXP extends Entity {

    public OBJ_EXP(GamePanel gp) {
        super(gp);

        name = "exp";

        image = setup("/objects/full_exp_start");
        image2 = setup("/objects/full_exp_mid") ;
        image3 = setup("/objects/full_exp_end") ;

        image4 = setup("/objects/half_exp_start");
        image5 = setup("/objects/half_exp_mid");
        image6 = setup("/objects/half_exp_end");

        image7 = setup("/objects/empty_exp_start");
        image8 = setup("/objects/empty_exp_mid");
        image9 = setup("/objects/empty_exp_end");

    }
}
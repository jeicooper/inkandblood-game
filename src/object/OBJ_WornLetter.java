package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_WornLetter extends Entity {

    public OBJ_WornLetter (GamePanel gp){
        super(gp);
        name = "Worn Letter";
        image = setup("/objects/worn_letter");
        description = "A worn letter addresed\n to Dr. Jose P. Rizal";
        collision = true;
    }
}
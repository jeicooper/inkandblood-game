package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_OldLetter extends Entity {

    public OBJ_OldLetter (GamePanel gp){
        super(gp);
        name = "Old Letter";
        image = setup("/objects/old_letter");
        description = "An old letter written\nby Fathers Gomez, Burgos,\nand Zamora";
        collision = true;
    }
}

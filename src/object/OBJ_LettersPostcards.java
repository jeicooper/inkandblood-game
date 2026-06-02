package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_LettersPostcards extends Entity{
    public OBJ_LettersPostcards (GamePanel gp){
        super(gp);
        name = "Letters and Postcards";
        image = setup("/objects/letter and envelope");
        description = "";
        collision = true;
    }
}

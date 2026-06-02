package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Ticket extends Entity{
    public OBJ_Ticket (GamePanel gp){
        super(gp);
        name = "Ship Ticket";
        image = setup("/objects/ship ticket");
        description = "";
        collision = true;
    }
}

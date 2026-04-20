package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_FinalPaper extends Entity {

    public OBJ_FinalPaper(GamePanel gp) {
        super(gp);
        name        = "Final Thoughts";
        image       = setup("/objects/old_letter");
        description = "A folded piece of paper.\nSomething written in Rizal's hand.";
        collision   = true;

        // full-tile hitbox so F-interaction works like an NPC — not picked up
        solidArea.x      = 0;
        solidArea.y      = 0;
        solidArea.width  = gp.tileSize;
        solidArea.height = gp.tileSize;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
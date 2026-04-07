package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_EmptyPlate extends Entity {
    public OBJ_EmptyPlate(GamePanel gp) {
        super(gp);
        name = "Empty Plate";
        image = setup("/objects/empty_plate");
        description = "An empty plate.\nThe Reality of Berlin.";
        collision = true;
    }
}

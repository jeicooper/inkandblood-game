package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_PaintBucket extends Entity {

    public OBJ_PaintBucket(GamePanel gp, String spritePath) {
        super(gp);
        name = "Paint Bucket";
        image = setup(spritePath);
        description = "A bucket of paint.\nNeeded for art lessons.";
        collision = false;
    }
}
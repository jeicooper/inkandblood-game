package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_PaintBucket extends Entity {

    public OBJ_PaintBucket(GamePanel gp, String spritePath) {
        super(gp);
        name = "Paint Bucket";
        image = setup(spritePath);
        description = "A bucket of colorful\npaint. Needed for\nart lessons.";
        collision = false;
    }
}
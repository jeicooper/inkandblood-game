package object;

import entity.Entity;
import main.GamePanel;

import java.awt.*;

public class OBJ_PaintBrush extends Entity {

    public OBJ_PaintBrush(GamePanel gp) {
        super(gp);
        name = "Paintbrush";
        image = setup("/objects/paintbrush");
        description = "A fine paintbrush.\nNeeded for art lessons.";
        collision = false;
        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
    }
}
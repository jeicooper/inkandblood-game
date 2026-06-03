package object;
import entity.Entity;
import main.GamePanel;
public class OBJ_Bible extends Entity {
    public int lineIndex = 0;
    public OBJ_Bible (GamePanel gp) {
        super(gp);
        name = "Bible";
        image = setup("/objects/bible");
        description = "A wooden box on the desk.";
        collision = true;
    }
}
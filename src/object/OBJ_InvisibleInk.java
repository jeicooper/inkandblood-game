package object;
import entity.Entity;
import main.GamePanel;
public class OBJ_InvisibleInk extends Entity {
    public int lineIndex = 0;
    public OBJ_InvisibleInk(GamePanel gp) {
        super(gp);
        name = "Invisible Ink";
        image = setup("/objects/invisible ink");
        description = "A wooden box on the desk.";
        collision = true;
    }
}
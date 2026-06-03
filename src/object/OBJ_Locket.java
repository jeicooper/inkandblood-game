package object;
import entity.Entity;
import main.GamePanel;
public class OBJ_Locket extends Entity {
    public int lineIndex = 0;
    public OBJ_Locket(GamePanel gp) {
        super(gp);
        name = "Locket";
        image = setup("/objects/locket");
        description = "A wooden box on the desk.";
        collision = true;
    }
}
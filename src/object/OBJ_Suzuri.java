package object;
import entity.Entity;
import main.GamePanel;
public class OBJ_Suzuri extends Entity {
    public int lineIndex = 0;
    public OBJ_Suzuri(GamePanel gp) {
        super(gp);
        name = "Suzuri";
        image = setup("/objects/suzuri");
        description = "A wooden box on the desk.";
        collision = true;
    }
}
package object;
import entity.Entity;
import main.GamePanel;
public class OBJ_Abanico extends Entity {
    public int lineIndex = 0;
    public OBJ_Abanico(GamePanel gp) {
        super(gp);
        name = "Abanico";
        image = setup("/objects/abanico");
        description = "A wooden box on the desk.";
        collision = true;
    }
}
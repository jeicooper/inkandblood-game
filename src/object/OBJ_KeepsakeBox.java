package object;
import entity.Entity;
import main.GamePanel;
public class OBJ_KeepsakeBox extends Entity {
    public int lineIndex = 0;
    public OBJ_KeepsakeBox(GamePanel gp) {
        super(gp);
        name = "Wooden Keepsake Box";
        image = setup("/objects/keepsakebox");
        description = "A wooden box on the desk.";
        collision = true;
    }
}
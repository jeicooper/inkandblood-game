package object;
import entity.Entity;
import main.GamePanel;
public class OBJ_BelgianBiscuits extends Entity {
    public int lineIndex = 0;
    public OBJ_BelgianBiscuits(GamePanel gp) {
        super(gp);
        name = "Belgian Biscuits";
        image = setup("/objects/belgian biscuits");
        description = "A wooden box on the desk.";
        collision = true;
    }
}
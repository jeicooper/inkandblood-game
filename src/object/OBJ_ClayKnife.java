package object;
import entity.Entity;
import main.GamePanel;
public class OBJ_ClayKnife extends Entity {
    public int lineIndex = 0;
    public OBJ_ClayKnife(GamePanel gp) {
        super(gp);
        name = "Clay Knife";
        image = setup("/objects/clay knife");
        description = "A wooden box on the desk.";
        collision = true;
    }
}
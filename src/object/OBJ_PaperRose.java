package object;
import entity.Entity;
import main.GamePanel;
public class OBJ_PaperRose extends Entity {
    public int lineIndex = 0;
    public OBJ_PaperRose(GamePanel gp) {
        super(gp);
        name = "Paper Rose";
        image = setup("/objects/paper rose");
        description = "A wooden box on the desk.";
        collision = true;
    }
}
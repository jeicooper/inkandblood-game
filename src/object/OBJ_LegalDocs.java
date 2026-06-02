package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_LegalDocs extends Entity{
    public OBJ_LegalDocs (GamePanel gp){
        super(gp);
        name = "Legal Docs";
        image = setup("/objects/legal documents");
        description = "";
        collision = true;
    }
}

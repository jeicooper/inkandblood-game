package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_MiUltimoAdios extends Entity {

    public OBJ_MiUltimoAdios(GamePanel gp) {
        super(gp);
        name        = "Mi Ultimo Adios";
        image       = setup("/objects/book2");
        description = "Rizal's farewell poem,\nhidden inside an oil lamp.\n[ ENTER ] to read";
        collision   = true;
        setHitbox();
    }
}
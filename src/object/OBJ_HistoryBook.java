package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_HistoryBook extends Entity {

    public int bookIndex;

    public boolean inWorld;

    public OBJ_HistoryBook(GamePanel gp, int bookIndex) {
        super(gp);
        this.bookIndex = bookIndex;
        this.inWorld   = true;

        switch (bookIndex) {
            case 0:
                name  = "History I";
                image = setup("/objects/19th_book1");
                description = "History book I left by\nTatay.\n[ ENTER ] to read";
                break;
            case 1:
                name  = "History II";
                image = setup("/objects/19th_book2");
                description = "History book II left by\nTatay.\n[ ENTER ] to read";
                break;
            case 2:
                name  = "History III";
                image = setup("/objects/19th_book3");
                description = "History book III left by\nTatay.\n[ ENTER ] to read";
                break;
            default:
                name  = "History Book";
                image = setup("/objects/19th_book1");
                description = "A history book.\n[ ENTER ] to read";
        }

        collision = true;
    }

    public OBJ_HistoryBook(GamePanel gp, int bookIndex, boolean inWorld) {
        this(gp, bookIndex);
        this.inWorld = inWorld;
    }
}
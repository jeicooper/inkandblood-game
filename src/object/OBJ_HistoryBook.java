package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_HistoryBook extends Entity {

    // Which book slot this is (0, 1, or 2) — used by QuestManager to track collection
    public int bookIndex;

    // Whether this book is a world object (pickupable) or already in inventory (readable)
    public boolean inWorld;

    public OBJ_HistoryBook(GamePanel gp, int bookIndex) {
        super(gp);
        this.bookIndex = bookIndex;
        this.inWorld   = true;

        switch (bookIndex) {
            case 0:
                name  = "History I";
                image = setup("/objects/19th_book1");
                description = "A history book left by Tatay.\n[ ENTER ] to read";
                break;
            case 1:
                name  = "History II";
                image = setup("/objects/19th_book2");
                description = "A history book left by Tatay.\n[ ENTER ] to read";
                break;
            case 2:
                name  = "History III";
                image = setup("/objects/19th_book3");
                description = "A history book left by Tatay.\n[ ENTER ] to read";
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
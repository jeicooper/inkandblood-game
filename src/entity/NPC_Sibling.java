package entity;

import main.GamePanel;
import main.QuestManager;

import java.util.LinkedList;

public class NPC_Sibling extends Entity {

    public String  siblingName;
    public boolean isFollowing = false;
    public boolean delivered   = false;

    public Entity followTarget = null;

    private final LinkedList<int[]> posHistory = new LinkedList<>();
    private static final int HISTORY_DELAY = 30;

    private String[] greetDialogue;

    public NPC_Sibling(GamePanel gp, String siblingName, String spritePath) {
        super(gp);
        this.siblingName = siblingName;

        setHitbox();
        direction = "down";
        speed     = 5;

        loadSprites(spritePath);
        buildDialogue();
    }

    private void loadSprites(String basePath) {
        up1    = setup(basePath + "_up_1");
        up2    = setup(basePath + "_up_2");
        down1  = setup(basePath + "_down_1");
        down2  = setup(basePath + "_down_2");
        left1  = setup(basePath + "_left_1");
        left2  = setup(basePath + "_left_2");
        right1 = setup(basePath + "_right_1");
        right2 = setup(basePath + "_right_2");

        if (down1 == null) {
            String fallback = "/npc/sibling/sibling_default";
            up1 = up2 = down1 = down2 = left1 = left2 = right1 = right2 = setup(fallback);
        }
    }

    private void buildDialogue() {
        String n = siblingName;

        if (n.contains("Saturnina")) {
            greetDialogue = new String[]{
                    "As the eldest of us all, I usually keep an eye on everyone, but I got\ndistracted by Sisa.",
                    "I am Saturnina, though you always call me Neneng.",
                    "Let’s get back, Nanay Teodora is waiting for us to eat.",
                    null
            };

        } else if (n.contains("Paciano")) {
            greetDialogue = new String[]{
                    "There you are, little brother. I am Paciano, the second child and your\nonly brother.",
                    "I was just thinking about the future of our family, but my stomach\nsays the present requires dinner.",
                    "Let's go.",
                    null
            };

        } else if (n.contains("Narcisa")) {
            greetDialogue = new String[]{
                    "Hello, Pepe! I am Narcisa, the third in our line, but I prefer Sisa.",
                    "I’ve been tired all day",
                    "I hope Nanay has prepared something delicious for us tonight",
                    null
            };

        } else if (n.contains("Olimpia")) {
            greetDialogue = new String[]{
                    "Pepe! I am Olimpia, your fourth sibling",
                    " Is the food already? Nanay must be worried.",
                    "Let’s hurry back to the Nanay!",
                    null
            };
        } else if (n.contains("Lucia")) {
            greetDialogue = new String[]{
                    "Hi, Pepe! I am Lucia, the fifth child, or Lucing as you like to call me.",
                    "I’m glad you’re here because I’m starting to get very hungry",
                    "Let’s go!",
                    null
            };

        } else if (n.contains("Maria")) {
            greetDialogue = new String[]{
                    "You found me, Pepe! I am Maria, the sixth of us...though you know\nme best as Biang",
                    "I’ve been watching the town from this view",
                    "but I’m ready for supper and share a meal with everyone.",
                    null
            };

        } else if (n.contains("Josefa")) {
            greetDialogue = new String[]{
                    "I’m here! I am Josefa, the ninth child, call me Panggoy.",
                    "I was just watching this painting, but a home-cooked meal sounds\nmuch better right now.",
                    "Take me to Nanay, Pepe!",
                    null
            };

        } else if (n.contains("Trinidad")) {
            greetDialogue = new String[]{
                    "Oh, Pepe! I am Trinidad, the tenth sibling.",
                    "I was just enjoying the fresh air, but if Nanay sent you, food must be\nready.",
                    "Let’s go before the food gets cold!",
                    null
            };

        } else if (n.contains("Soledad")) {
            greetDialogue = new String[]{
                    "Pepe, you’re here! I am Soledad, the youngest of all eleven, often\ncalled Choleng.",
                    "I was just playing, but I’m ready to go home and eat with the whole\nfamily",
                    "Let’s go!",
                    null
            };

        } else {
            greetDialogue = new String[]{
                    "...",
                    null
            };
        }

        dialogues = greetDialogue;
    }

    @Override
    public void speak() {
        gp.ui.currentSpeakerName = siblingName;

        if (gp.questManager.quest1Stage == QuestManager.QUEST1_NOT_STARTED) {
            gp.ui.currentDialogue = "...";
            dialogueIndex = 0;
            facePlayer();
            return;
        }

        if (!isFollowing) {
            dialogues = greetDialogue;
            dialogueIndex = 0;
            super.speak();
            startFollowing();
        } else {
            super.speak();
        }
    }

    public void startFollowing() {
        isFollowing = true;
        posHistory.clear();
    }

    @Override
    public void setAction() { }

    @Override
    public void update() {
        if (delivered) return;

        if (isFollowing && followTarget != null) {
            followUpdate();

            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum     = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

    private void followUpdate() {
        posHistory.addLast(new int[]{ followTarget.worldX, followTarget.worldY });

        if (posHistory.size() > HISTORY_DELAY) {
            int[] targetPos = posHistory.removeFirst();
            int destX = targetPos[0];
            int destY = targetPos[1];

            int dx = destX - worldX;
            int dy = destY - worldY;
            double dist = Math.sqrt(dx * dx + dy * dy);

            if (dist > speed) {
                if (Math.abs(dx) > Math.abs(dy)) {
                    direction = (dx > 0) ? "right" : "left";
                } else {
                    direction = (dy > 0) ? "down" : "up";
                }

                collisionOn = false;
                gp.cChecker.checkTile(this);
                gp.cChecker.checkObject(this, false);

                if (!collisionOn) {
                    double stepX = (dx / dist) * speed;
                    double stepY = (dy / dist) * speed;
                    worldX += (int) stepX;
                    worldY += (int) stepY;
                } else {
                    collisionOn = false;
                    if (Math.abs(dx) > Math.abs(dy)) {
                        direction = (dy > 0) ? "down" : "up";
                        gp.cChecker.checkTile(this);
                        if (!collisionOn) worldY += (dy > 0) ? speed : -speed;
                    } else {
                        direction = (dx > 0) ? "right" : "left";
                        gp.cChecker.checkTile(this);
                        if (!collisionOn) worldX += (dx > 0) ? speed : -speed;
                    }
                }
            }
        }
    }

    private void facePlayer() {
        int dx = gp.player.worldX - worldX;
        int dy = gp.player.worldY - worldY;
        if (Math.abs(dx) > Math.abs(dy)) {
            direction = (dx > 0) ? "right" : "left";
        } else {
            direction = (dy > 0) ? "down" : "up";
        }
    }
}
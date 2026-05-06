package entity;

import main.GamePanel;
import main.QuestManager;

import java.util.LinkedList;

public class NPC_Sibling extends Entity {

    public String  siblingName;
    public boolean isFollowing = false;
    public boolean delivered   = false;
    private int lastTargetX = -1;
    private int lastTargetY = -1;
    public Entity followTarget = null;
    private boolean dialogueFinished = false;
    private boolean joiningLine = false;
    private int stuckCounter = 0;
    public int recruitOrder = -1;
    private static int nextRecruitOrder = 0;

    private final LinkedList<int[]> posHistory = new LinkedList<>();
    private static final int FOLLOW_DISTANCE = 10;

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
                    "As the **eldest** of us all, I usually keep an eye on everyone, but I got\ndistracted by Sisa.",
                    "I am Saturnina, though you always call me Neneng.",
                    "Let's get back, Nanay Teodora is waiting for us to eat.",
                    null
            };
        } else if (n.contains("Paciano")) {
            greetDialogue = new String[]{
                    "There you are, little brother. I am Paciano, the **second child and your\nonly brother**.",
                    "I was just thinking about the future of our family, but my stomach\nsays the present requires dinner.",
                    "Let's go.",
                    null
            };
        } else if (n.contains("Narcisa")) {
            greetDialogue = new String[]{
                    "Hello, Pepe! I am Narcisa, the **third** in our line, but I prefer Sisa.",
                    "I've been tired all day",
                    "I hope Nanay has prepared something delicious for us tonight",
                    null
            };
        } else if (n.contains("Olimpia")) {
            greetDialogue = new String[]{
                    "Pepe! I am Olimpia, your **fourth sibling**",
                    " Is the food already? Nanay must be worried.",
                    "Let's hurry back to the Nanay!",
                    null
            };
        } else if (n.contains("Lucia")) {
            greetDialogue = new String[]{
                    "Hi, Pepe! I am Lucia, the **fifth child**, or Lucing as you like to call me.",
                    "I'm glad you're here because I'm starting to get very hungry",
                    "Let's go!",
                    null
            };
        } else if (n.contains("Maria")) {
            greetDialogue = new String[]{
                    "You found me, Pepe! I am Maria, the **sixth** of us...though you know\nme best as Biang",
                    "I've been watching the town from this view",
                    "but I'm ready for supper and share a meal with everyone.",
                    null
            };
        } else if (n.contains("Josefa")) {
            greetDialogue = new String[]{
                    "I'm here! I am Josefa, the **ninth child**, call me Panggoy.",
                    "I was just watching this painting, but a home-cooked meal sounds\nmuch better right now.",
                    "Take me to Nanay, Pepe!",
                    null
            };
        } else if (n.contains("Trinidad")) {
            greetDialogue = new String[]{
                    "Oh, Pepe! I am Trinidad, the **tenth sibling**.",
                    "I was just enjoying the fresh air, but if Nanay sent you, food must be\nready.",
                    "Let's go before the food gets cold!",
                    null
            };
        } else if (n.contains("Soledad")) {
            greetDialogue = new String[]{
                    "Pepe, you're here! I am Soledad, **the youngest of all** eleven, often\ncalled Choleng.",
                    "I was just playing, but I'm ready to go home and eat with the whole\nfamily",
                    "Let's go!",
                    null
            };
        } else {
            greetDialogue = new String[]{ "...", null };
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

            if (dialogues[dialogueIndex] == null) {
                dialogueIndex = 0;
                dialogueFinished = true;
                startFollowing();
                gp.gameState = gp.playState;
                return;
            }

            super.speak();
        } else {
            super.speak();
        }
    }

    public void startFollowing() {
        isFollowing = true;
        joiningLine = true;
        posHistory.clear();
        stuckCounter = 0;

        recruitOrder = nextRecruitOrder++;

        lastTargetX = followTarget != null ? followTarget.worldX : worldX;
        lastTargetY = followTarget != null ? followTarget.worldY : worldY;

        gp.player.rebuildCongoLine();
    }

    @Override
    public void setAction() { }

    @Override
    public void update() {
        if (delivered) return;

        if (isFollowing && followTarget != null) {
            if (joiningLine) {
                joinLineUpdate();
            } else {
                boolean targetMoving = (followTarget.worldX != lastTargetX
                        || followTarget.worldY != lastTargetY);

                if (targetMoving) {
                    followUpdate();
                    tickSprite();
                }
            }
        }
    }

    private void joinLineUpdate() {
        int destX = followTarget.worldX;
        int destY = followTarget.worldY;

        int dx = destX - worldX;
        int dy = destY - worldY;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist <= gp.tileSize * 1.5) {
            joiningLine = false;
            posHistory.clear();
            for (int i = 0; i < FOLLOW_DISTANCE; i++) {
                posHistory.addLast(new int[]{ worldX, worldY });
            }
            lastTargetX = followTarget.worldX;
            lastTargetY = followTarget.worldY;
            stuckCounter = 0;
            return;
        }

        if (stuckCounter > 120) {
            worldX = destX;
            worldY = destY;
            stuckCounter = 0;
            return;
        }

        if (Math.abs(dx) > Math.abs(dy)) {
            direction = (dx > 0) ? "right" : "left";
        } else {
            direction = (dy > 0) ? "down" : "up";
        }

        collisionOn = false;
        gp.cChecker.checkTile(this);

        if (!collisionOn) {
            double stepX = (dx / dist) * speed;
            double stepY = (dy / dist) * speed;
            worldX += (int) stepX;
            worldY += (int) stepY;
            stuckCounter = 0;
        } else {
            collisionOn = false;
            direction = (dx > 0) ? "right" : "left";
            gp.cChecker.checkTile(this);
            if (!collisionOn && dx != 0) {
                worldX += (dx > 0) ? speed : -speed;
                stuckCounter = 0;
            } else {
                collisionOn = false;
                direction = (dy > 0) ? "down" : "up";
                gp.cChecker.checkTile(this);
                if (!collisionOn && dy != 0) {
                    worldY += (dy > 0) ? speed : -speed;
                    stuckCounter = 0;
                } else {
                    stuckCounter++;
                }
            }
        }

        tickSprite();
    }

    private void followUpdate() {
        if (followTarget.worldX != lastTargetX || followTarget.worldY != lastTargetY) {
            posHistory.addLast(new int[]{
                    followTarget.worldX,
                    followTarget.worldY
            });
            lastTargetX = followTarget.worldX;
            lastTargetY = followTarget.worldY;
        }

        if (posHistory.size() <= FOLLOW_DISTANCE) return;

        int[] targetPos = posHistory.removeFirst();
        int destX = targetPos[0];
        int destY = targetPos[1];

        int dx = destX - worldX;
        int dy = destY - worldY;

        if (Math.abs(dx) > Math.abs(dy)) {
            direction = (dx > 0) ? "right" : "left";
        } else if (dy != 0) {
            direction = (dy > 0) ? "down" : "up";
        }

        worldX = destX;
        worldY = destY;
    }

    private void tickSprite() {
        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum     = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
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
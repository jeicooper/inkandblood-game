package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class AssessmentPanel {

    private final GamePanel gp;
    private final UI ui;

    // ---- Phases ----
    private static final int PHASE_INTRO    = 0;
    private static final int PHASE_QUESTION = 1;
    private static final int PHASE_OUTRO    = 2;
    private int phase = PHASE_INTRO;

    // ---- Narrator dialogue ----
    private final String[] introLines = {
            "Ah... another young mind, eager and unshaped.",
            "Before your story begins, let us see what you already carry.",
            "I will ask you fifteen questions about our nation's hero.",
            "Answer honestly. There is no shame in not yet knowing.",
            "Use [ W ] and [ S ] to choose, [ ENTER ] to confirm.",
            "Let us begin."
    };
    private final String[] outroLines = {
            "There. The measure is taken.",
            "Remember these answers, child.",
            "When your journey ends, we shall see how far you have come.",
            "Now... let the story of Pepe unfold."
    };
    private int narrationIndex = 0;

    public static final int ASSESSMENT_SIZE = 15;
    private static final int CORRECT_INDEX  = 0;

    private final String[]   questions = new String[ASSESSMENT_SIZE];
    private final String[][] choices   = new String[ASSESSMENT_SIZE][3];
    private final String[]   facts     = new String[ASSESSMENT_SIZE];

    private int currentQuestion = 0;
    private int selectedChoice  = 0;
    private boolean answerConfirmed = false;
    private int score = 0;

    // ---- Narrator sprite ----
    private BufferedImage narratorImg;

    public AssessmentPanel(GamePanel gp, UI ui) {
        this.gp = gp;
        this.ui = ui;
        buildPlaceholderContent();
        loadNarratorImage();
    }

    private void buildPlaceholderContent() {
        for (int i = 0; i < ASSESSMENT_SIZE; i++) {
            questions[i] = "Placeholder question " + (i + 1) + " — insert question here.";
            choices[i][0] = "Placeholder answer A";
            choices[i][1] = "Placeholder answer B";
            choices[i][2] = "Placeholder answer C";
            facts[i]      = "Did you know? Placeholder fact for question " + (i + 1) + ".";
        }
    }

    private void loadNarratorImage() {
        try {
            var stream = getClass().getResourceAsStream("/npc/rector/rector_down_1.png");
            if (stream != null) narratorImg = ImageIO.read(stream);
        } catch (Exception e) {
            narratorImg = null;
        }
    }

    public void start() {
        phase = PHASE_INTRO;
        narrationIndex = 0;
        currentQuestion = 0;
        selectedChoice = 0;
        answerConfirmed = false;
        score = 0;
        gp.gameState = gp.assessmentState;
        gp.inputDelay = 15;
    }

    // ====================== INPUT ======================
    public void handleKey(int code) {
        if (gp.inputDelay > 0) return;

        switch (phase) {
            case PHASE_INTRO:   handleNarration(code, true);  break;
            case PHASE_QUESTION:handleQuestion(code);         break;
            case PHASE_OUTRO:   handleNarration(code, false); break;
        }
    }

    private void handleNarration(int code, boolean isIntro) {
        if (code == java.awt.event.KeyEvent.VK_ENTER || code == java.awt.event.KeyEvent.VK_SPACE) {
            narrationIndex++;
            String[] lines = isIntro ? introLines : outroLines;
            if (narrationIndex >= lines.length) {
                if (isIntro) {
                    phase = PHASE_QUESTION;
                    narrationIndex = 0;
                } else {
                    finish();
                }
            }
        }
    }

    private void handleQuestion(int code) {
        if (!answerConfirmed) {
            if (code == java.awt.event.KeyEvent.VK_W || code == java.awt.event.KeyEvent.VK_UP) {
                selectedChoice = (selectedChoice - 1 + 3) % 3;
            }
            if (code == java.awt.event.KeyEvent.VK_S || code == java.awt.event.KeyEvent.VK_DOWN) {
                selectedChoice = (selectedChoice + 1) % 3;
            }
            if (code == java.awt.event.KeyEvent.VK_ENTER) {
                answerConfirmed = true;
                if (selectedChoice == CORRECT_INDEX) score++;
            }
        } else {
            if (code == java.awt.event.KeyEvent.VK_ENTER || code == java.awt.event.KeyEvent.VK_SPACE) {
                if (currentQuestion >= ASSESSMENT_SIZE - 1) {
                    phase = PHASE_OUTRO;
                    narrationIndex = 0;
                } else {
                    currentQuestion++;
                    selectedChoice = 0;
                    answerConfirmed = false;
                }
            }
        }
    }

    private void finish() {

        gp.questManager.assessmentScore = score;
        gp.cutsceneManager.startIntroCutscene();
    }

    // ====================== DRAW ======================
    public void draw(Graphics2D g2) {
        int sw = gp.screenWidth;
        int sh = gp.screenHeight;

        // Dim background
        g2.setColor(new Color(15, 12, 20));
        g2.fillRect(0, 0, sw, sh);

        drawNarrator(g2);

        if (phase == PHASE_INTRO) {
            drawNarrationBubble(g2, introLines[Math.min(narrationIndex, introLines.length - 1)]);
        } else if (phase == PHASE_OUTRO) {
            drawNarrationBubble(g2, outroLines[Math.min(narrationIndex, outroLines.length - 1)]);
        } else {
            drawNarrationBubble(g2, answerConfirmed ? facts[currentQuestion]
                    : "Take your time. Choose your answer.");
            drawQuizPanel(g2);
        }
    }

    private void drawNarrator(Graphics2D g2) {
        int sz = gp.tileSize * 3;
        int nx = 40;
        int ny = gp.screenHeight - sz - 40;
        if (narratorImg != null) {
            g2.drawImage(narratorImg, nx, ny, sz, sz, null);
        } else {
            g2.setColor(new Color(60, 50, 70));
            g2.fillRoundRect(nx, ny, sz, sz, 16, 16);
        }
    }

    private void drawNarrationBubble(Graphics2D g2, String text) {
        int bx = 40;
        int by = 60;
        int bw = 360;
        int bh = gp.screenHeight - size3() - 160;
        if (bh < 160) bh = 160;

        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillRoundRect(bx, by, bw, bh, 24, 24);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3f));
        g2.drawRoundRect(bx, by, bw, bh, 24, 24);

        // little tail pointing down to the narrator
        int[] tx = { bx + 50, bx + 90, bx + 60 };
        int[] ty = { by + bh, by + bh, by + bh + 30 };
        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillPolygon(tx, ty, 3);
        g2.setColor(Color.white);
        g2.drawLine(tx[0], ty[0], tx[2], ty[2]);
        g2.drawLine(tx[1], ty[1], tx[2], ty[2]);

        g2.setFont(ui.maruMonica.deriveFont(Font.PLAIN, 22f));
        g2.setColor(new Color(255, 235, 200));
        drawWrapped(g2, text, bx + 22, by + 44, bw - 44, 30);

        // continue hint
        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 16f));
        g2.setColor(new Color(170, 170, 170));
        String hint = (phase == PHASE_QUESTION && !answerConfirmed)
                ? "" : "[ ENTER ]";
        if (!hint.isEmpty())
            g2.drawString(hint, bx + bw - 90, by + bh - 18);
    }

    private int size3() { return gp.tileSize * 3 + 80; }

    private void drawQuizPanel(Graphics2D g2) {
        int sw = gp.screenWidth;

        int pad = 30;
        int pw = sw - 480 - pad;
        int px = 440;
        int py = 60;
        int ph = gp.screenHeight - 120;

        // Title
        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 30f));
        g2.setColor(new Color(255, 210, 80));
        String title = "Initial Assessment";
        g2.drawString(title, px + (pw - g2.getFontMetrics().stringWidth(title)) / 2, py + 6);

        // progress
        g2.setFont(ui.maruMonica.deriveFont(Font.PLAIN, 18f));
        g2.setColor(new Color(180, 180, 180));
        String prog = "Question " + (currentQuestion + 1) + " / " + ASSESSMENT_SIZE;
        g2.drawString(prog, px, py + 34);

        // Panel
        int boxY = py + 50;
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(px, boxY, pw, ph - 50, 28, 28);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3f));
        g2.drawRoundRect(px, boxY, pw, ph - 50, 28, 28);

        // Question text
        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 24f));
        g2.setColor(Color.white);
        int qy = boxY + 50;
        qy = drawWrapped(g2, questions[currentQuestion], px + 30, qy, pw - 60, 32);

        // Choices
        String[] labels = { "a", "b", "c" };
        int choiceH = 56;
        int gap = 18;
        int choiceStartY = boxY + 150;
        for (int i = 0; i < 3; i++) {
            boolean isSel = (i == selectedChoice);
            boolean isCorrect = (i == CORRECT_INDEX);

            Color bg;
            if (answerConfirmed) {
                if (isCorrect)      bg = new Color(50, 170, 50, 190);
                else if (isSel)     bg = new Color(190, 50, 50, 190);
                else                bg = new Color(40, 40, 40, 150);
            } else {
                bg = isSel ? new Color(255, 197, 0, 200) : new Color(40, 40, 40, 150);
            }

            int rowY = choiceStartY + i * (choiceH + gap);
            g2.setColor(bg);
            g2.fillRoundRect(px + 30, rowY, pw - 60, choiceH, 18, 18);
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(px + 30, rowY, pw - 60, choiceH, 18, 18);

            g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 22f));
            g2.setColor(answerConfirmed && !isCorrect && !isSel ? new Color(180,180,180) : Color.white);
            g2.drawString(labels[i] + ".  " + choices[currentQuestion][i],
                    px + 50, rowY + choiceH / 2 + 8);
        }

        // Footer hint
        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 16f));
        g2.setColor(new Color(170, 170, 170));
        String hint = answerConfirmed
                ? (currentQuestion >= ASSESSMENT_SIZE - 1 ? "[ ENTER ] Finish" : "[ ENTER ] Next")
                : "[ W ] / [ S ] choose    [ ENTER ] confirm";
        g2.drawString(hint, px + 30, boxY + ph - 70);
    }

    private int drawWrapped(Graphics2D g2, String text, int x, int y, int maxW, int lineH) {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            String test = line.length() == 0 ? word : line + " " + word;
            if (g2.getFontMetrics().stringWidth(test) > maxW) {
                g2.drawString(line.toString(), x, y);
                y += lineH;
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(test);
            }
        }
        if (line.length() > 0) {
            g2.drawString(line.toString(), x, y);
            y += lineH;
        }
        return y;
    }
}
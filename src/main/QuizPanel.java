package main;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * QuizPanel — a self-contained 5-question multiple-choice quiz.
 *
 * HOW TO INTEGRATE:
 *  1. Add a field in UI.java:
 *         public QuizPanel quizPanel;
 *         public boolean   quizPanelOpen = false;
 *
 *  2. Initialise it in the UI constructor:
 *         quizPanel = new QuizPanel(gp, this);
 *
 *  3. In UI.draw(), inside the playState block, add:
 *         if (quizPanelOpen) quizPanel.draw(g2);
 *
 *  4. In KeyHandler.playState(), add at the top:
 *         if (gp.ui.quizPanelOpen) {
 *             gp.ui.quizPanel.handleKey(code);
 *             return;
 *         }
 *
 *  5. Call gp.ui.openQuizPanel() from NPC_Student to open the quiz.
 */

public class QuizPanel {

    private final GamePanel gp;
    private final UI        ui;

    // QUESTIONS AND ANSWERS
    private static final String[] QUESTIONS = {
            "In your Latin studies, we look for precision. What is the correct translation of the phrase `Scientia potentia est`?",

            "What do we call a logical argument that applies deductive reasoning to arrive at a conclusion based on two or more propositions?",

            "In our Latin studies, what is the correct translation and significance of the phrase `Sobresaliente` which we all strive for in our grades?",

            "You study the `Bachelor of Arts.` In the classical sense, which of these is a core subject of the Trivium we study here?",

            "If you were to graduate from this school with your degree, what would your title be in Latin?"
    };

    // Each row: [A, B, C, D]
    private static final String[][] CHOICES = {
            { "Science is difficult.",
              "Knowledge is power.",
              "Time is Fleeting"
            },

            { "Soliloquy", "Syllogism", "Paradox"},

            { "Passing", "Excellent", "Failure" },

            { "Botany", "Rhetoric", "Agriculture"},

            { "Magister Philosophiae",
              "Bachiller en Artes",
              "Perito Agrimensor"}
    };

    // Index of the correct answer (0=A, 1=B, 2=C)
    private static final int[] CORRECT = { 1, 0, 2, 1, 1 };

    // STATE
    private int     currentQuestion = 0;
    private int     selectedChoice  = 0;

    private boolean answerConfirmed = false;
    private boolean showResult      = false;
    private int     score           = 0;
    private boolean[] correct;

    // CONSTRUCTOR
    public QuizPanel(GamePanel gp, UI ui) {
        this.gp = gp;
        this.ui = ui;
    }

    // OPEN/RESET
    public void open() {
        currentQuestion = 0;
        selectedChoice  = 0;
        answerConfirmed = false;
        showResult      = false;
        score           = 0;
        correct         = new boolean[QUESTIONS.length];
        ui.quizPanelOpen = true;
    }

    // KEY HANDLER
    public void handleKey(int code) {
        if (showResult) {
            // Any ENTER closes the result screen and reports score
            if (code == KeyEvent.VK_ENTER) {
                ui.quizPanelOpen = false;
                gp.questManager.onQuizResult(score);
            }
            return;
        }

        if (!answerConfirmed) {
            // Navigate choices with W/S or UP/DOWN
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                selectedChoice = (selectedChoice - 1 + 3) % 3;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                selectedChoice = (selectedChoice + 1) % 3;
            }
            // Confirm with ENTER
            if (code == KeyEvent.VK_ENTER) {
                answerConfirmed = true;
                if (selectedChoice == CORRECT[currentQuestion]) {
                    correct[currentQuestion] = true;
                    score++;
                }
            }
        } else {
            // After seeing feedback, ENTER moves to next question
            if (code == KeyEvent.VK_ENTER) {
                currentQuestion++;
                if (currentQuestion >= QUESTIONS.length) {
                    showResult = true;
                } else {
                    selectedChoice  = 0;
                    answerConfirmed = false;
                }
            }
        }
    }

    // ── Draw ──────────────────────────────────────────────────────────────
    public void draw(Graphics2D g2) {
        // Dim the background
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int panelW = gp.tileSize * 16;
        int panelH = gp.tileSize * 10;
        int panelX = gp.screenWidth  / 2 - panelW / 2;
        int panelY = gp.screenHeight / 2 - panelH / 2;
        ui.drawSubWindow(panelX, panelY, panelW, panelH);

        if (showResult) {
            drawResultScreen(g2, panelX, panelY, panelW, panelH);
        } else {
            drawQuestionScreen(g2, panelX, panelY, panelW, panelH);
        }
    }

    // ── Question screen ───────────────────────────────────────────────────
    private void drawQuestionScreen(Graphics2D g2, int px, int py, int pw, int ph) {
        int pad = gp.tileSize;

        // Progress indicator
        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 22f));
        g2.setColor(new Color(180, 180, 180));
        String progress = "Question " + (currentQuestion + 1) + " / " + QUESTIONS.length;
        g2.drawString(progress, px + pad, py + 30);

        // Question text
        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 26f));
        g2.setColor(Color.white);
        String q = QUESTIONS[currentQuestion];
        drawWrapped(g2, q, px + pad, py + 70, pw - pad * 2, 30);

        // Choices
        String[] labels = { "A", "B", "C" };
        int choiceY = py + gp.tileSize * 3;
        int lineH   = gp.tileSize - 4;

        for (int i = 0; i < 3; i++) {
            boolean isSelected = (i == selectedChoice);
            boolean isCorrect  = (i == CORRECT[currentQuestion]);

            Color bg;
            if (answerConfirmed) {
                if (isCorrect)                          bg = new Color(50, 180, 50, 180);
                else if (isSelected && !isCorrect)      bg = new Color(200, 50, 50, 180);
                else                                    bg = new Color(40, 40, 40, 120);
            } else {
                bg = isSelected ? new Color(80, 80, 180, 200) : new Color(40, 40, 40, 120);
            }

            g2.setColor(bg);
            g2.fillRoundRect(px + pad - 6, choiceY - 22, pw - pad * 2 + 12, lineH, 10, 10);

            // Cursor arrow for selected
            g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 24f));
            if (isSelected && !answerConfirmed) {
                g2.setColor(new Color(255, 220, 80));
                g2.drawString(">", px + pad - 4, choiceY);
            }

            g2.setColor(Color.white);
            g2.drawString("[" + labels[i] + "]  " + CHOICES[currentQuestion][i],
                    px + pad + 20, choiceY);

            choiceY += lineH + 6;
        }

        // Prompt
        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 20f));
        g2.setColor(new Color(180, 180, 180));
        if (!answerConfirmed) {
            g2.drawString("[ W / S ] Navigate    [ ENTER ] Confirm",
                    px + pad, py + ph - 20);
        } else {
            boolean wasCorrect = (selectedChoice == CORRECT[currentQuestion]);
            g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 24f));
            g2.setColor(wasCorrect ? new Color(100, 230, 100) : new Color(230, 80, 80));
            g2.drawString(wasCorrect ? "Correct!" : "Incorrect!", px + pad, py + ph - 40);
            g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 20f));
            g2.setColor(new Color(180, 180, 180));
            g2.drawString("[ ENTER ] Next", px + pad, py + ph - 16);
        }
    }

    // ── Result screen ─────────────────────────────────────────────────────
    private void drawResultScreen(Graphics2D g2, int px, int py, int pw, int ph) {
        int pad = gp.tileSize;

        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 36f));
        boolean passed = score >= QUESTIONS.length;
        g2.setColor(passed ? new Color(100, 230, 100) : new Color(230, 80, 80));
        String headline = passed ? "Excellent!" : "Not quite...";
        int hw = g2.getFontMetrics().stringWidth(headline);
        g2.drawString(headline, px + pw / 2 - hw / 2, py + pad + 10);

        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 28f));
        g2.setColor(Color.white);
        String scoreStr = "Score: " + score + " / " + QUESTIONS.length;
        int sw = g2.getFontMetrics().stringWidth(scoreStr);
        g2.drawString(scoreStr, px + pw / 2 - sw / 2, py + pad + 50);

        // Per-question breakdown
        int itemY = py + pad + 90;
        g2.setFont(ui.maruMonica.deriveFont(Font.PLAIN, 22f));
        for (int i = 0; i < QUESTIONS.length; i++) {
            g2.setColor(correct[i] ? new Color(80, 220, 80) : new Color(220, 80, 80));
            g2.drawString((correct[i] ? "/ " : "X ") + "Q" + (i + 1) + ": "
                    + QUESTIONS[i], px + pad, itemY);
            itemY += 28;
        }

        // Closing prompt
        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 20f));
        g2.setColor(new Color(180, 180, 180));
        g2.drawString(passed ? "[ ENTER ] Continue"
                        : "[ ENTER ] Try Again",
                px + pad, py + ph - 16);
    }

    // ── Utility: word-wrap text ────────────────────────────────────────────
    private void drawWrapped(Graphics2D g2, String text, int x, int y, int maxW, int lineH) {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int drawY = y;
        for (String word : words) {
            String test = line.isEmpty() ? word : line + " " + word;
            if (g2.getFontMetrics().stringWidth(test) > maxW) {
                g2.drawString(line.toString(), x, drawY);
                drawY += lineH;
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(test);
            }
        }
        if (!line.isEmpty()) g2.drawString(line.toString(), x, drawY);
    }
}

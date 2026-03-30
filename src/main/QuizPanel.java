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

        if (currentQuestion >= QUESTIONS.length) {
            showResult = true;
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
                if (currentQuestion >= QUESTIONS.length - 1) {
                    showResult = true;
                } else {
                    currentQuestion++;
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

        // Safety guard
        if (currentQuestion >= QUESTIONS.length) {
            showResult = true;
            return;
        }

        int pad    = gp.tileSize;
        int innerW = pw - pad * 2;   // usable width inside the panel

        // ── Progress indicator ────────────────────────────────────────────
        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 20f));
        g2.setColor(new Color(180, 180, 180));
        g2.drawString("Question " + (currentQuestion + 1) + " / " + QUESTIONS.length,
                px + pad, py + 35);

        // ── Question text (word-wrapped) ──────────────────────────────────
        // Measure how many lines the question needs, then position choices below
        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 24f));
        g2.setColor(Color.white);

        int questionLineH = 30;
        int questionStartY = py + 70;

        // Count lines first so we know where choices start
        int questionLines = countWrappedLines(g2,
                QUESTIONS[currentQuestion], innerW);

        // Draw the question
        drawWrapped(g2, QUESTIONS[currentQuestion],
                px + pad, questionStartY, innerW, questionLineH);

        // ── Choices ───────────────────────────────────────────────────────
        // Start choices with a fixed gap below the last question line
        int choiceGap  = 20;   // gap between question block and first choice
        int choiceH    = gp.tileSize;       // height of each choice row
        int choiceGapH = 8;                 // gap between choice rows

        int choiceStartY = questionStartY
                + (questionLines * questionLineH)
                + choiceGap;

        String[] labels = { "A", "B", "C" };

        for (int i = 0; i < CHOICES[currentQuestion].length; i++) {
            boolean isSelected = (i == selectedChoice);
            boolean isCorrect  = (i == CORRECT[currentQuestion]);

            // Background highlight
            Color bg;
            if (answerConfirmed) {
                if (isCorrect)                     bg = new Color(50, 180, 50, 180);
                else if (isSelected)               bg = new Color(200, 50, 50, 180);
                else                               bg = new Color(40, 40, 40, 120);
            } else {
                bg = isSelected
                        ? new Color(80, 80, 180, 200)
                        : new Color(40, 40, 40, 120);
            }

            int rowY = choiceStartY + i * (choiceH + choiceGapH);

            g2.setColor(bg);
            g2.fillRoundRect(px + pad - 6, rowY - 22,
                    innerW + 12, choiceH, 10, 10);

            // Cursor arrow
            g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 22f));
            if (isSelected && !answerConfirmed) {
                g2.setColor(new Color(255, 220, 80));
                g2.drawString(">", px + pad - 4, rowY);
            }

            g2.setColor(Color.white);
            g2.drawString("[" + labels[i] + "]  " + CHOICES[currentQuestion][i],
                    px + pad + 20, rowY);
        }

        // ── Bottom prompt ─────────────────────────────────────────────────
        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 20f));
        g2.setColor(new Color(180, 180, 180));

        if (!answerConfirmed) {
            String nav = "[ W / S ] Navigate    [ ENTER ] Confirm";
            int navW = g2.getFontMetrics().stringWidth(nav);
            g2.drawString(nav, px + pw / 2 - navW / 2, py + ph - 20);
        } else {
            boolean wasCorrect = (selectedChoice == CORRECT[currentQuestion]);

            g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 24f));
            g2.setColor(wasCorrect ? new Color(100, 230, 100) : new Color(230, 80, 80));

            String feedback = wasCorrect ? "Correct!" : "Incorrect!";
            int fbW = g2.getFontMetrics().stringWidth(feedback);
            g2.drawString(feedback, px + pw / 2 - fbW / 2, py + ph - 44);

            g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 20f));
            g2.setColor(new Color(180, 180, 180));
            String next = "[ ENTER ] Next";
            int nextW = g2.getFontMetrics().stringWidth(next);
            g2.drawString(next, px + pw / 2 - nextW / 2, py + ph - 18);
        }
    }

    // ── Counts how many lines drawWrapped would produce ───────────────────
    private int countWrappedLines(Graphics2D g2, String text, int maxW) {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int lines = 1;
        for (String word : words) {
            String test = line.isEmpty() ? word : line + " " + word;
            if (g2.getFontMetrics().stringWidth(test) > maxW) {
                lines++;
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(test);
            }
        }
        return lines;
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

        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 30f));
        for (int i = 0; i < QUESTIONS.length; i++) {

            g2.setColor(correct[i] ? new Color(80, 220, 80) : new Color(220, 80, 80));
            String mark   = correct[i] ? "/" : "X";
            String answer = correct[i] ? "Correct" : "Incorrect";
            g2.drawString(mark + "  Q" + (i + 1) + ":  " + answer, px + pad, itemY);
            itemY += 32;
        }

        // Closing prompt
        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 20f));
        g2.setColor(new Color(180, 180, 180));
        String closing = passed ? "[ ENTER ] Continue" : "[ ENTER ] Try Again";
        int closingW = g2.getFontMetrics().stringWidth(closing);
        g2.drawString(closing, px + pw / 2 - closingW / 2, py + ph - 16);
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

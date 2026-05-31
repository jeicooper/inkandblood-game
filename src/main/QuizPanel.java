package main;

import java.awt.*;
import java.awt.event.KeyEvent;

public class QuizPanel {

    private final GamePanel gp;
    private final UI        ui;

    // QUESTIONS AND ANSWERS
    private static final String[] POOL_QUESTIONS = {
            //1
            "In your Latin studies, we look for precision. What is the correct translation of the phrase `Scientia potentia est`?",

            //2
            "What is your full name?",

            //3
            "In our Latin studies, what is the correct translation and significance of the phrase `Sobresaliente` which we all strive for in our grades?",

            //4
            "You study the `Bachelor of Arts.` In the classical sense, which of these is a core subject of the Trivium we study here?",

            //5
            "What is the position of our Fr. Magin Ferrando in Ateneo Municipal De Manila?",

            //6
            "Which specific group is associated with the Roman Empire and identified as 'interns' or boarders within the school?",

            //7
            "What was the highest rank a student could achieve within his respective empire through academic merit?",

            //8
            "In the hierarchy of the empires, which rank was responsible for carrying the empire's banner during competitions?",

            //9
            "If you are categorized as an 'extern' or a non-boarder, which empire would you belong to in this system?",

            //10
            "How did a student lose their rank or cause their empire to lose its standing in the classroom?"
    };

    private static final String[][] POOL_CHOICES = {
            //1
            { "Science is difficult.",
                    "Knowledge is power.",
                    "Time is Fleeting"
            },

            //2
            { "José Protacio Rizal Mercado y Alonso Realonda",
                    "José Protacio Rizal y Alonzo Mercado Realonda",
                    "José Protacio Rizal Alonso y Mercado Realonda"
            },

            //3
            { "Passing",
                    "Excellent",
                    "Failure" },

            //4
            { "Botany",
                    "Rhetoric",
                    "Agriculture"},

            //5
            { "Professor",
                    "Headmaster",
                    "Registrar"},

            //6
            { "Carthaginians",
                    "Tribunes",
                    "Romans", },

            //7
            { "Centurion",
                    "Standard-bearer",
                    "Emperor" },

            //8
            { "Standard-bearer",
                    "Emperor",
                    "Decurion" },

            //9
            {"Roman Empire",
                    "Byzantine Empire",
                    "Carthaginian Empire"},

            //10
            {"Losing a physical match",
                    "Arriving late to class",
                    "Failing to answer questions during a challenge"}

    };

    private static final int[] POOL_CORRECT = { 1, 0, 1, 1, 2,
            2, 2, 0, 2, 2 };

    private static final int QUIZ_SIZE = 10;

    private static final int INTELLECT_5050_THRESHOLD = 2;
    private int eliminatedChoice = -1;
    private int singleEliminated = -1;

    private String[]   QUESTIONS;
    private String[][] CHOICES;
    private int[]      CORRECT;


    // STATE
    private int     currentQuestion = 0;
    private int     selectedChoice  = 0;

    private boolean answerConfirmed = false;
    private boolean showResult      = false;
    private int     score           = 0;
    private boolean[] correct;

    public QuizPanel(GamePanel gp, UI ui) {
        this.gp = gp;
        this.ui = ui;
    }

    public void open() {

        java.util.List<Integer> indices = new java.util.ArrayList<>();
        for (int i = 0; i < POOL_QUESTIONS.length; i++) indices.add(i);
        java.util.Collections.shuffle(indices);

        QUESTIONS = new String[QUIZ_SIZE];
        CHOICES   = new String[QUIZ_SIZE][];
        CORRECT   = new int[QUIZ_SIZE];
        for (int i = 0; i < QUIZ_SIZE; i++) {
            int pick     = indices.get(i);
            QUESTIONS[i] = POOL_QUESTIONS[pick];
            CHOICES[i]   = POOL_CHOICES[pick];
            CORRECT[i]   = POOL_CORRECT[pick];
        }

        currentQuestion = 0;
        selectedChoice  = 0;
        answerConfirmed = false;
        showResult      = false;
        score           = 0;
        correct         = new boolean[QUESTIONS.length];
        ui.quizPanelOpen = true;
        recomputeElimination();
    }

    private void recomputeElimination() {
        eliminatedChoice = -1;
        if (gp.player.intellect < INTELLECT_5050_THRESHOLD) return;
        if (QUESTIONS == null || currentQuestion >= QUESTIONS.length) return;

        java.util.List<Integer> wrong = new java.util.ArrayList<>();
        for (int i = 0; i < CHOICES[currentQuestion].length; i++) {
            if (i != CORRECT[currentQuestion]) wrong.add(i);
        }
        if (!wrong.isEmpty()) {
            eliminatedChoice = wrong.get(new java.util.Random().nextInt(wrong.size()));
        }
        // Don't leave the cursor sitting on the dimmed choice.
        if (selectedChoice == eliminatedChoice) {
            selectedChoice = (selectedChoice + 1) % 3;
        }
    }

    // KEY HANDLER
    public void handleKey(int code) {

        if (singleMode) {
            if (!singleConfirmed) {
                if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                    singleSelected = (singleSelected - 1 + singleChoices.length) % singleChoices.length;
                    if (singleSelected == singleEliminated)
                        singleSelected = (singleSelected - 1 + singleChoices.length) % singleChoices.length;
                }
                if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                    singleSelected = (singleSelected + 1) % singleChoices.length;
                    if (singleSelected == singleEliminated)
                        singleSelected = (singleSelected + 1) % singleChoices.length;
                }
                if (code == KeyEvent.VK_ENTER) {
                    singleConfirmed = true;
                }
            } else {
                if (code == KeyEvent.VK_ENTER) {
                    boolean correct = (singleSelected == singleCorrect);
                    singleMode       = false;
                    ui.quizPanelOpen = false;
                    if (singleCallback != null) singleCallback.onResult(correct);
                }
            }
            return;
        }

        if (showResult) {
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
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                selectedChoice = (selectedChoice - 1 + 3) % 3;
                if (selectedChoice == eliminatedChoice) selectedChoice = (selectedChoice - 1 + 3) % 3;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                selectedChoice = (selectedChoice + 1) % 3;
                if (selectedChoice == eliminatedChoice) selectedChoice = (selectedChoice + 1) % 3;
            }
            if (code == KeyEvent.VK_ENTER) {
                answerConfirmed = true;
                if (selectedChoice == CORRECT[currentQuestion]) {
                    correct[currentQuestion] = true;
                    score++;
                }
            }
        } else {
            if (code == KeyEvent.VK_ENTER) {
                if (currentQuestion >= QUESTIONS.length - 1) {
                    showResult = true;
                } else {
                    currentQuestion++;
                    selectedChoice  = 0;
                    answerConfirmed = false;
                    recomputeElimination();
                }
            }

        }
    }

    public void draw(Graphics2D g2) {

        if (singleMode) {
            drawSingleQuestion(g2);
            return;
        }

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int panelW = gp.tileSize * 16;
        int panelH = gp.tileSize * 8;
        int panelX = gp.screenWidth  / 2 - panelW / 2;
        int panelY = gp.screenHeight / 2 - panelH / 2;
        ui.drawSubWindow(panelX, panelY, panelW, panelH);

        if (showResult) {
            drawResultScreen(g2, panelX, panelY, panelW, panelH);
        } else {
            drawQuestionScreen(g2, panelX, panelY, panelW, panelH);
        }
    }

    private void drawSingleQuestion(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int panelW = gp.tileSize * 16;
        int panelH = gp.tileSize * 8;
        int panelX = gp.screenWidth  / 2 - panelW / 2;
        int panelY = gp.screenHeight / 2 - panelH / 2;
        ui.drawSubWindow(panelX, panelY, panelW, panelH);

        int pad    = gp.tileSize;
        int innerW = panelW - pad * 2;

        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 20f));
        g2.setColor(new Color(180, 180, 180));
        g2.drawString("Discipline Challenge", panelX + pad, panelY + 35);

        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 24f));
        g2.setColor(Color.white);
        drawWrapped(g2, singleQuestion, panelX + pad, panelY + 70, innerW, 30);

        int questionLines = countWrappedLines(g2, singleQuestion, innerW);
        int choiceStartY  = panelY + 70 + questionLines * 30 + 20;

        String[] labels = {"A", "B", "C"};
        int choiceH   = gp.tileSize;
        int choiceGap = 8;

        for (int i = 0; i < singleChoices.length; i++) {
            boolean isSelected = (i == singleSelected);
            boolean isCorrect  = (i == singleCorrect);
            boolean dimmed     = (!singleConfirmed && i == singleEliminated);

            Color bg;
            if (dimmed) {
                bg = new Color(25, 25, 25, 120);
            } else if (singleConfirmed) {
                if (isCorrect)       bg = new Color(50, 180, 50, 180);
                else if (isSelected) bg = new Color(200, 50, 50, 180);
                else                 bg = new Color(40, 40, 40, 120);
            } else {
                bg = isSelected ? new Color(255, 197, 0, 200) : new Color(40, 40, 40, 120);
            }

            int rowY = choiceStartY + i * (choiceH + choiceGap);
            g2.setColor(bg);
            g2.fillRoundRect(panelX + pad - 6, rowY - 22, innerW + 12, choiceH, 10, 10);

            g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 22f));
            if (isSelected && !singleConfirmed && !dimmed) {
                g2.setColor(new Color(255, 220, 80));
                g2.drawString(">", panelX + pad - 4, rowY);
            }

            String text = "[" + labels[i] + "]  " + singleChoices[i];
            if (dimmed) {
                g2.setColor(new Color(110, 110, 110));
                g2.drawString(text, panelX + pad + 20, rowY);
                int tw = g2.getFontMetrics().stringWidth(text);
                g2.drawLine(panelX + pad + 20, rowY - 7, panelX + pad + 20 + tw, rowY - 7);
            } else {
                g2.setColor(Color.white);
                g2.drawString(text, panelX + pad + 20, rowY);
            }
        }

        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 20f));
        g2.setColor(new Color(180, 180, 180));
        if (!singleConfirmed) {
            String nav = "[ W / S ] Navigate    [ ENTER ] Confirm";
            int nw = g2.getFontMetrics().stringWidth(nav);
            g2.drawString(nav, panelX + panelW / 2 - nw / 2, panelY + panelH - 20);
        } else {
            boolean wasCorrect = (singleSelected == singleCorrect);
            g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 24f));
            g2.setColor(wasCorrect ? new Color(100, 230, 100) : new Color(230, 80, 80));
            String fb = wasCorrect ? "Correct!" : "Incorrect!";
            int fw = g2.getFontMetrics().stringWidth(fb);
            g2.drawString(fb, panelX + panelW / 2 - fw / 2, panelY + panelH - 44);

            g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 20f));
            g2.setColor(new Color(180, 180, 180));
            String next = "[ ENTER ] Continue";
            int nw = g2.getFontMetrics().stringWidth(next);
            g2.drawString(next, panelX + panelW / 2 - nw / 2, panelY + panelH - 18);
        }
    }

    private void drawQuestionScreen(Graphics2D g2, int px, int py, int pw, int ph) {

        // Safety guard
        if (currentQuestion >= QUESTIONS.length) {
            showResult = true;
            return;
        }

        int pad    = gp.tileSize;
        int innerW = pw - pad * 2;

        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 20f));
        g2.setColor(new Color(180, 180, 180));
        g2.drawString("Question " + (currentQuestion + 1) + " / " + QUESTIONS.length,
                px + pad, py + 35);

        if (!answerConfirmed && eliminatedChoice >= 0) {
            g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 16f));
            g2.setColor(new Color(120, 200, 255));
            String aid = "Intellect Perk: one wrong answer dimmed";
            g2.drawString(aid, px + pw - pad - g2.getFontMetrics().stringWidth(aid), py + 35);
        }

        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 24f));
        g2.setColor(Color.white);

        int questionLineH = 30;
        int questionStartY = py + 70;

        int questionLines = countWrappedLines(g2,
                QUESTIONS[currentQuestion], innerW);

        drawWrapped(g2, QUESTIONS[currentQuestion],
                px + pad, questionStartY, innerW, questionLineH);

        int choiceGap  = 20;
        int choiceH    = gp.tileSize;
        int choiceGapH = 8;

        int choiceStartY = questionStartY
                + (questionLines * questionLineH)
                + choiceGap;

        String[] labels = { "A", "B", "C" };

        for (int i = 0; i < CHOICES[currentQuestion].length; i++) {
            boolean isSelected = (i == selectedChoice);
            boolean isCorrect  = (i == CORRECT[currentQuestion]);
            boolean dimmed     = (!answerConfirmed && i == eliminatedChoice);

            // Background highlight
            Color bg;
            if (dimmed) {
                bg = new Color(25, 25, 25, 120);
            } else if (answerConfirmed) {
                if (isCorrect) bg = new Color(50, 180, 50, 180);
                else if (isSelected) bg = new Color(200, 50, 50, 180);
                else bg = new Color(40, 40, 40, 120);
            } else {
                bg = isSelected
                        ? new Color(255, 197, 0, 200)
                        : new Color(40, 40, 40, 120);
            }

            int rowY = choiceStartY + i * (choiceH + choiceGapH);

            g2.setColor(bg);
            g2.fillRoundRect(px + pad - 6, rowY - 22,
                    innerW + 12, choiceH, 10, 10);

            // Cursor arrow
            g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 22f));
            if (isSelected && !answerConfirmed && !dimmed) {
                g2.setColor(new Color(255, 220, 80));
                g2.drawString(">", px + pad - 4, rowY);
            }

            String text = "[" + labels[i] + "]  " + CHOICES[currentQuestion][i];
            if (dimmed) {
                g2.setColor(new Color(110, 110, 110));
                g2.drawString(text, px + pad + 20, rowY);
                int tw = g2.getFontMetrics().stringWidth(text);
                g2.drawLine(px + pad + 20, rowY - 7, px + pad + 20 + tw, rowY - 7); // strike-through
            } else {
                g2.setColor(Color.white);
                g2.drawString(text, px + pad + 20, rowY);
            }
        }

        // prompt
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


    // RESULTS SCREEN
    private void drawResultScreen(Graphics2D g2, int px, int py, int pw, int ph) {
        int pad = gp.tileSize;

        // --- Headline ---
        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 36f));
        boolean passed = score >= QUESTIONS.length;
        g2.setColor(passed ? new Color(100, 230, 100) : new Color(230, 80, 80));
        String headline = passed ? "Excellent!" : "Not quite...";
        int hw = g2.getFontMetrics().stringWidth(headline);
        g2.drawString(headline, px + pw / 2 - hw / 2, py + pad + 10);

        // --- Score ---
        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 28f));
        g2.setColor(Color.white);
        String scoreStr = "Score: " + score + " / " + QUESTIONS.length;
        int sw = g2.getFontMetrics().stringWidth(scoreStr);
        g2.drawString(scoreStr, px + pw / 2 - sw / 2, py + pad + 50);

        // --- Questions List ---
        int startY = py + pad + 100;
        int itemY = startY;
        int columnX = px + pad;
        int columnWidth = pw / 2;

        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 26f));

        for (int i = 0; i < QUESTIONS.length; i++) {

            if (i == 5) {
                itemY = startY;
                columnX += columnWidth;
            }

            g2.setColor(correct[i] ? new Color(80, 220, 80) : new Color(220, 80, 80));

            String mark   = correct[i] ? "/" : "X";
            String answer = correct[i] ? "Correct" : "Incorrect";

            g2.drawString(mark + " Q" + (i + 1) + ": " + answer, columnX, itemY);

            itemY += 35;
        }

        // Closing prompt
        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 20f));
        g2.setColor(new Color(180, 180, 180));
        String closing = passed ? "[ ENTER ] Continue" : "[ ENTER ] Try Again";
        int closingW = g2.getFontMetrics().stringWidth(closing);
        g2.drawString(closing, px + pw / 2 - closingW / 2, py + ph - 16);
    }

    // WORD WRAP
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

    public interface QuizCallback {
        void onResult(boolean correct);
    }

    private boolean singleMode = false;
    private QuizCallback singleCallback = null;
    private String singleQuestion;
    private String[] singleChoices;
    private int singleCorrect;
    private int singleSelected = 0;
    private boolean singleConfirmed = false;

    public void openSingleQuestion(String question, String[] choices, int correctIndex, QuizCallback callback) {

        singleMode = true;
        singleCallback = callback;
        singleQuestion = question;
        singleChoices = choices;
        singleCorrect = correctIndex;
        singleSelected = 0;
        singleConfirmed = false;
        ui.quizPanelOpen = true;
        gp.gameState = gp.playState;

        singleEliminated = -1;
        if (gp.player.intellect >= INTELLECT_5050_THRESHOLD) {
            java.util.List<Integer> wrong = new java.util.ArrayList<>();
            for (int i = 0; i < choices.length; i++) if (i != correctIndex) wrong.add(i);
            if (!wrong.isEmpty())
                singleEliminated = wrong.get(new java.util.Random().nextInt(wrong.size()));
            if (singleSelected == singleEliminated)
                singleSelected = (singleSelected + 1) % choices.length;
        }
    }
}
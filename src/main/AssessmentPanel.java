package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class AssessmentPanel {

    private final GamePanel gp;
    private final UI ui;

    // ---- Phases ----
    private static final int PHASE_DEBATE   = -1;
    private static final int PHASE_INTRO    = 0;
    private static final int PHASE_QUESTION = 1;
    private static final int PHASE_OUTRO    = 2;
    private int phase = PHASE_DEBATE;

    // ---- Debate dialogue ----
    private static final int NARRATOR = 0;
    private static final int RENATO   = 1;
    private final int[] debateSpeaker = {
            NARRATOR, NARRATOR, NARRATOR,
            RENATO,
            NARRATOR,
            RENATO, RENATO,
            NARRATOR,
            RENATO,
            NARRATOR,
            RENATO, RENATO, RENATO,
            NARRATOR,
            RENATO,
            NARRATOR, NARRATOR, NARRATOR, NARRATOR, NARRATOR
    };
    private final String[] debateLines = {
            "Ah... another young mind, eager and unshaped.",
            "Before your story begins, let us see what you already carry.",
            "I will ask you TEN different questions about our nation's hero.",
            "Unshaped? Or simply conditioned to practice veneration without understanding? You prepare to quiz them on a flawless deity, Narrator," +
                    " rather than a historical figure who openly rejected our own Revolution!",
            "Professor Constantino! This is just a basic quiz to see what the player knows before the game starts.",
            "A basic quiz heavily skewed by historical amnesia!",
            "I pointed out that other national heroes led their revolutions, but Rizal openly condemned Bonifacio and the Katipunan!",
            "But his writings and his tragic death inspired the entire country to fight.",
            "He wanted peaceful reforms, not violence. As a wealthy ilustraso, he feared the raw power of the masses.",
            "We don't erase them, Professor. We respect all our heroes.",
            "No, you push them into the background! Governor Taft and the Americans pushed Rizal in 1901 because he was safely dead and didn't " +
                    "preach armed rebellion.",
            "Why? Because he was safely dead and never preached rebellion. The Americans chose him over leaders who actually threatened their rule.",
            "Andres Bonifacio was too radical, Emilio Aguinaldo was too militant, and Apolinario Mabini completely refused to surrender to " +
                    "American rule!",
            "Rizal still died for our country, Professor. That sacrifice cannot be ignored.",
            "Of course not, and he deserves his place. But he doesn't own patriotism! Ordinary citizens built this nation too, and we shouldn't be" +
                    " afraid to look at his weaknesses.",
            "You give us a lot to think about, Professor. But the baseline evaluation must still proceed so the player can begin.",
            "Let us see what you already carry.",
            "Answer honestly. There is no shame in not yet knowing.",
            "Use [ W ] and [ S ] to choose, [ ENTER ] to confirm.",
            "Let us begin."
    };
    private int debateIndex = 0;

    // ---- Narrator dialogue ----
    private final String[] introLines = {
            "Ah... another young mind, eager and unshaped.",
            "Before your story begins, let us see what you already carry.",
            "I will ask you ten questions about our nation's hero.",
            "Answer honestly. There is no shame in not yet knowing.",
            "Use [ W ] and [ S ] to choose, [ ENTER ] to confirm.",
            "Let us begin."
    };
    private final String[] outroLines = {
            "There. The measure is taken.",
            "Remember these answers, child.",
            "These information shall prove useful to you as you go along this journey",
            "Now... let the story of Pepe unfold."
    };
    private int narrationIndex = 0;

    public static final int ASSESSMENT_SIZE = 10;
    private static final int POOL_SIZE       = 10;

    private final String[]   questions = new String[POOL_SIZE];
    private final String[][] choices   = new String[POOL_SIZE][3];
    private final String[]   facts     = new String[POOL_SIZE];

    private final int[]      correctIdx = new int[POOL_SIZE];

    private int[] questionOrder = new int[ASSESSMENT_SIZE];

    private int currentQuestion = 0;
    private int selectedChoice  = 0;
    private boolean answerConfirmed = false;
    private int score = 0;

    // ---- Narrator sprite ----
    private BufferedImage narratorImg;
    private BufferedImage renatoImg;

    public AssessmentPanel(GamePanel gp, UI ui) {
        this.gp = gp;
        this.ui = ui;
        buildQuestionContent();
        loadNarratorImage();
        loadRenatoImage();
    }

    private void buildQuestionContent() {
        int q = 0;

        questions[q] = "What's the full name of Jose Rizal?";
        choices[q] = new String[]{ "Jose Protacio Rizal Mercado Y Alonso Realonda",
                "Jose Protacio Rizal Mercado Y Realonda Alonso",
                "Jose Protacio Rizal Alonso Y Mercado Realonda" };
        correctIdx[q] = 0;
        facts[q] = "The Rizal family's real last name is **\"Mercado.\"** They changed it to \"Rizal\" to avoid Spanish blacklisting.";
        q++;

        questions[q] = "When was Rizal born?";
        choices[q] = new String[]{ "January 19, 1986", "December 30, 1896", "June 19, 1861" };
        correctIdx[q] = 2;
        facts[q] = "He was born in **Calamba, Laguna**.";
        q++;

        questions[q] = "How many siblings does Rizal have?";
        choices[q] = new String[]{ "8", "11", "10" };
        correctIdx[q] = 2;
        facts[q] = "Rizal was the **7th** among his siblings.";
        q++;

        questions[q] = "Which sibling of Rizal died at a young age?";
        choices[q] = new String[]{ "Concepcion", "Olimpia", "Trinidad" };
        correctIdx[q] = 0;
        facts[q] = "Concepcion's death was Rizal's **first experience of grief**. The two were close as children, and her death from illness deeply" +
                " devastated him.";
        q++;

        questions[q] = "When did Rizal die?";
        choices[q] = new String[]{ "December 30, 1896", "January 19, 1986", "June 19, 1861" };
        correctIdx[q] = 0;
        facts[q] = "His death anniversary is **a national holiday called \"Rizal Day**,\" observed annually to commemorate his martyrdom as the " +
                "country's national hero.";
        q++;

        questions[q] = "What law was created for Rizal?";
        choices[q] = new String[]{ "RA 11313", "RA 1425", "RA 10931" };
        correctIdx[q] = 1;
        facts[q] = "The Catholic Church **opposed the Rizal Law** because it mandates reading Noli Me "
                + "Tangere and El Filibusterismo, which contain passages critical of the church.";
        q++;

        questions[q] = "Who authored the Rizal Law?";
        choices[q] = new String[]{ "Bato Dela Rosa", "Claro M. Recto", "Juan Ponce Enrile" };
        correctIdx[q] = 1;
        facts[q] = "The Rizal Law was **approved on June 12, 1956**. It integrated the study of the "
                + "life and works of Dr. Jose Rizal into the tertiary-level curriculum.";
        q++;

        questions[q] = "Who sponsored the Rizal Law?";
        choices[q] = new String[]{ "Diosdado Macapagal", "Manuel L. Quezon", "Jose P. Laurel" };
        correctIdx[q] = 2;
        facts[q] = "Section 5 of the law **authorized three hundred thousand pesos** from the National "
                + "Treasury to carry out its purposes.";
        q++;

        questions[q] = "Who was recommended as a national hero other than Rizal?";
        choices[q] = new String[]{ "Gabriela Silang", "Diego Silang", "Gregorio Del Pilar" };
        correctIdx[q] = 0;
        facts[q] = "Rizal was an **American -sponsored hero**. In 1901, Governor William Howard Taft "
                + "suggested the Philippine Commission give the Filipinos a national hero.";
        q++;

        questions[q] = "Who was Rizal's first love?";
        choices[q] = new String[]{ "Josephine Bracken", "Leonor Rivera", "Segunda Katigbak" };
        correctIdx[q] = 2;
        facts[q] = "Rizal's friend **Mariano was Segunda Katigbak's brother**; he introduced them to "
                + "each other when they were teenagers.";
        q++;
    }

    private void loadNarratorImage() {
        try {
            var stream = getClass().getResourceAsStream("/npc/narrator.png");
            if (stream != null) narratorImg = ImageIO.read(stream);
        } catch (Exception e) {
            narratorImg = null;
        }
    }

    private void loadRenatoImage() {
        try {
            var stream = getClass().getResourceAsStream("/npc/renato.png");
            if (stream != null) renatoImg = ImageIO.read(stream);
        } catch (Exception e) {
            renatoImg = null;
        }
    }

    public void start() {
        phase = PHASE_DEBATE;
        debateIndex = 0;
        narrationIndex = 0;
        currentQuestion = 0;
        selectedChoice = 0;
        answerConfirmed = false;
        score = 0;
        buildQuestionOrder();
        gp.gameState = gp.assessmentState;
        gp.inputDelay = 15;
    }

    private void buildQuestionOrder() {
        java.util.List<Integer> pool = new java.util.ArrayList<>();
        for (int i = 0; i < POOL_SIZE; i++) pool.add(i);
        java.util.Collections.shuffle(pool);
        questionOrder = new int[ASSESSMENT_SIZE];
        for (int i = 0; i < ASSESSMENT_SIZE; i++) {
            questionOrder[i] = pool.get(i % POOL_SIZE);
        }
    }

    private int qIndex() {
        return questionOrder[currentQuestion];
    }

    // ====================== INPUT ======================
    public void handleKey(int code) {
        if (gp.inputDelay > 0) return;

        switch (phase) {
            case PHASE_DEBATE:   handleDebate(code);           break;
            case PHASE_INTRO:   handleNarration(code, true);  break;
            case PHASE_QUESTION:handleQuestion(code);         break;
            case PHASE_OUTRO:   handleNarration(code, false); break;
        }
    }

    private void handleDebate(int code) {
        if (code == java.awt.event.KeyEvent.VK_ENTER || code == java.awt.event.KeyEvent.VK_SPACE) {
            debateIndex++;
            if (debateIndex >= debateLines.length) {
                phase = PHASE_QUESTION;
            }
            gp.inputDelay = 8;
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
                if (selectedChoice == correctIdx[qIndex()]) score++;
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

    public void draw(Graphics2D g2) {
        int sw = gp.screenWidth;
        int sh = gp.screenHeight;

        // Dim background
        g2.setColor(new Color(15, 12, 20));
        g2.fillRect(0, 0, sw, sh);

        if (phase == PHASE_DEBATE) {
            int safeIndex = Math.min(debateIndex, debateLines.length - 1);
            boolean narratorActive = (debateSpeaker[safeIndex] == NARRATOR);
            drawDebateSprites(g2, narratorActive);
            drawDebateBubble(g2, debateLines[safeIndex], narratorActive);
            return;
        }

        drawNarrator(g2);

        if (phase == PHASE_INTRO) {
            drawNarrationBubble(g2, introLines[Math.min(narrationIndex, introLines.length - 1)]);
        } else if (phase == PHASE_OUTRO) {
            drawNarrationBubble(g2, outroLines[Math.min(narrationIndex, outroLines.length - 1)]);
        } else {
            drawNarrationBubble(g2, answerConfirmed ? facts[qIndex()]
                    : "Take your time. Choose your answer.");
            drawQuizPanel(g2);
        }
    }

    // Draws both sprites; dims whichever one is not speaking
    private void drawDebateSprites(Graphics2D g2, boolean narratorActive) {
        int sz = gp.tileSize * 3;
        int ny = bubbleBottomY() + 40;

        // Narrator — left, same position as always
        int nx = 20;
        float narratorAlpha = narratorActive ? 1.0f : 0.3f;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, narratorAlpha));
        if (narratorImg != null) {
            g2.drawImage(narratorImg, nx, ny, sz, sz, null);
        } else {
            g2.setColor(new Color(60, 50, 70));
            g2.fillRoundRect(nx, ny, sz, sz, 16, 16);
        }

        // Renato — right side, mirrored
        int rx = gp.screenWidth - 20 - sz;
        float renatoAlpha = narratorActive ? 0.3f : 1.0f;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, renatoAlpha));
        if (renatoImg != null) {
            g2.drawImage(renatoImg, rx + sz, ny, -sz, sz, null);
        } else {
            g2.setColor(new Color(50, 60, 80));
            g2.fillRoundRect(rx, ny, sz, sz, 16, 16);
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    // Draws the active speaker's bubble using the exact same style as drawNarrationBubble.
    // Narrator bubble stays on the left (same as always). Renato's is mirrored on the right.
    private void drawDebateBubble(Graphics2D g2, String text, boolean narratorActive) {
        int bx = narratorActive ? bubbleX() : gp.screenWidth - bubbleX() - bubbleW();
        int by = bubbleY();
        int bw = bubbleW();
        int bh = bubbleH();

        // box — identical fill/border to drawNarrationBubble
        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillRoundRect(bx, by, bw, bh, 22, 22);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3f));
        g2.drawRoundRect(bx, by, bw, bh, 22, 22);

        // tail — points toward the sprite below it
        int tailInset = narratorActive ? 50 : bw - 86;
        int[] tx = { bx + tailInset, bx + tailInset + 36, bx + tailInset + 8 };
        int[] ty = { by + bh, by + bh, by + bh + 26 };
        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillPolygon(tx, ty, 3);
        g2.setColor(Color.white);
        g2.drawLine(tx[0], ty[0], tx[2], ty[2]);
        g2.drawLine(tx[1], ty[1], tx[2], ty[2]);

        // text — same call as drawNarrationBubble
        drawWrapped(g2, text, bx + 20, by + 38, bw - 40, 32, 24f);

        // hint — same style as drawNarrationBubble
        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 15f));
        g2.setColor(new Color(170, 170, 170));
        g2.drawString("[ ENTER ]", bx + bw - 86, by + bh - 16);
    }

    private void drawNarrator(Graphics2D g2) {
        int sz = gp.tileSize * 3;
        int nx = 20;
        int ny = bubbleBottomY() + 40;
        if (narratorImg != null) {
            g2.drawImage(narratorImg, nx, ny, sz, sz, null);
        } else {
            g2.setColor(new Color(60, 50, 70));
            g2.fillRoundRect(nx, ny, sz, sz, 16, 16);
        }
    }

    private int bubbleX()      { return 20; }
    private int bubbleY()      { return 30; }
    private int bubbleW()      { return 320; }
    private int bubbleH()      { return 300; }
    private int bubbleBottomY(){ return bubbleY() + bubbleH(); }

    private void drawNarrationBubble(Graphics2D g2, String text) {
        int bx = bubbleX();
        int by = bubbleY();
        int bw = bubbleW();
        int bh = bubbleH();

        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillRoundRect(bx, by, bw, bh, 22, 22);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3f));
        g2.drawRoundRect(bx, by, bw, bh, 22, 22);

        // little tail pointing down to the narrator
        int[] tx = { bx + 50, bx + 86, bx + 58 };
        int[] ty = { by + bh, by + bh, by + bh + 26 };
        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillPolygon(tx, ty, 3);
        g2.setColor(Color.white);
        g2.drawLine(tx[0], ty[0], tx[2], ty[2]);
        g2.drawLine(tx[1], ty[1], tx[2], ty[2]);

        drawWrapped(g2, text, bx + 20, by + 38, bw - 40, 32, 24f);

        // continue hint
        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 15f));
        g2.setColor(new Color(170, 170, 170));
        String hint = (phase == PHASE_QUESTION && !answerConfirmed) ? "" : "[ ENTER ]";
        if (!hint.isEmpty())
            g2.drawString(hint, bx + bw - 86, by + bh - 16);
    }

    private void drawQuizPanel(Graphics2D g2) {
        int sw = gp.screenWidth;

        int pw = 600;
        int px = 350;
        int boxH = 400;
        int boxY = (gp.screenHeight - boxH) / 2 + 20;
        int ph = boxH;

        // Title (above the box)
        g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 28f));
        g2.setColor(new Color(255, 210, 80));
        String title = "Initial Assessment";
        g2.drawString(title, px + (pw - g2.getFontMetrics().stringWidth(title)) / 2, boxY - 40);

        // progress (above the box, left-aligned)
        g2.setFont(ui.maruMonica.deriveFont(Font.PLAIN, 17f));
        g2.setColor(new Color(180, 180, 180));
        String prog = "Question " + (currentQuestion + 1) + " / " + ASSESSMENT_SIZE;
        g2.drawString(prog, px, boxY - 14);

        // Panel
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(px, boxY, pw, ph, 28, 28);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3f));
        g2.drawRoundRect(px, boxY, pw, ph, 28, 28);

        // Question text
        int qy = boxY + 55;
        drawWrapped(g2, questions[qIndex()], px + 28, qy, pw - 56, 34, 30f);


        String[] labels = { "a", "b", "c" };
        int choiceH = 50;
        int gap = 14;
        int choiceStartY = boxY + 132;
        for (int i = 0; i < 3; i++) {
            boolean isSel = (i == selectedChoice);
            boolean isCorrect = (i == correctIdx[qIndex()]);

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
            g2.fillRoundRect(px + 28, rowY, pw - 56, choiceH, 16, 16);
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(px + 28, rowY, pw - 56, choiceH, 16, 16);

            g2.setFont(ui.maruMonica.deriveFont(Font.BOLD, 25f));
            g2.setColor(answerConfirmed && !isCorrect && !isSel ? new Color(180,180,180) : Color.white);
            g2.drawString(labels[i] + ".  " + choices[qIndex()][i],
                    px + 48, rowY + choiceH / 2 + 7);
        }

        // Footer hint (inside the box, near the bottom)
        g2.setFont(ui.maruMonica.deriveFont(Font.ITALIC, 15f));
        g2.setColor(new Color(170, 170, 170));
        String fhint = answerConfirmed
                ? (currentQuestion >= ASSESSMENT_SIZE - 1 ? "[ ENTER ] Finish" : "[ ENTER ] Next")
                : "[ W ] / [ S ] choose    [ ENTER ] confirm";
        g2.drawString(fhint, px + 28, boxY + ph - 22);
    }

    private int drawWrapped(Graphics2D g2, String text, int x, int y, int maxW, int lineH) {
        return drawWrapped(g2, text, x, y, maxW, lineH, 20f); // default size
    }

    private int drawWrapped(Graphics2D g2, String text, int x, int y, int maxW, int lineH, float fontSize) {
        String[] segments = text.split("\\*\\*");

        java.util.List<String[]> tokens = new java.util.ArrayList<>();
        for (int s = 0; s < segments.length; s++) {
            boolean highlight = (s % 2 == 1);
            for (String word : segments[s].split(" ")) {
                if (!word.isEmpty()) tokens.add(new String[]{ word, String.valueOf(highlight) });
            }
        }

        StringBuilder line = new StringBuilder();
        java.util.List<String[]> lineTokens = new java.util.ArrayList<>();

        for (String[] token : tokens) {
            String word = token[0];
            g2.setFont(ui.maruMonica.deriveFont(Font.PLAIN, fontSize));
            String test = line.length() == 0 ? word : line + " " + word;

            if (g2.getFontMetrics().stringWidth(test) > maxW) {
                drawHighlightedLine(g2, lineTokens, x, y, fontSize);
                y += lineH;
                lineTokens.clear();
                line = new StringBuilder(word);
            } else {
                if (line.length() > 0) line.append(" ");
                line.append(word);
            }
            lineTokens.add(new String[]{ word, String.valueOf(token[1]) });
        }

        if (!lineTokens.isEmpty()) {
            drawHighlightedLine(g2, lineTokens, x, y, fontSize);
            y += lineH;
        }
        return y;
    }

    private void drawHighlightedLine(Graphics2D g2, java.util.List<String[]> tokens, int x, int y, float fontSize) {
        Font normalFont      = ui.maruMonica.deriveFont(Font.BOLD, fontSize);   // BOLD for narrator
        Font boldFont        = ui.maruMonica.deriveFont(Font.BOLD, fontSize);
        Color normalColor    = new Color(255, 255, 255);
        Color highlightColor = new Color(255, 220, 80);

        int drawX = x;
        for (int i = 0; i < tokens.size(); i++) {
            String word = tokens.get(i)[0];
            boolean highlight = Boolean.parseBoolean(tokens.get(i)[1]);
            String wordWithSpace = (i < tokens.size() - 1) ? word + " " : word;

            g2.setFont(highlight ? boldFont : normalFont);
            g2.setColor(highlight ? highlightColor : normalColor);
            g2.drawString(wordWithSpace, drawX, y);
            drawX += g2.getFontMetrics().stringWidth(wordWithSpace);
        }
    }
}
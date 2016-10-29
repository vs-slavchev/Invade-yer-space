package utility;


import main.Game;

import java.awt.*;

/*
 * The TextBoxes are used to display some information for a short time;
 * examples: the name of the song that started playing; small tutorials.
 * Boxes start under the screen and go up until they reach their targetY.
 * The x is never changed. Each new line of text is in a separate String
 * of the text array. A whole String is given to the constructor, but
 * it is split up (by semicolon) in separate lines. The width of the
 * text box and its duration on screen depends on the amount of text.
 */
public class TextBox {

    private static final int PIXELS_PER_CHAR = 14;
    private static final int DURATION_PER_CHAR = 6;
    private String[] text;
    private int x, y, targetY, width, height, duration;
    private boolean isTutorial;

    public TextBox(String textString, int x, int targetY) {
        this.text = textString.split(";");
        this.x = x;
        this.targetY = targetY;
        this.width = PIXELS_PER_CHAR * maxCharsPerLine(text);
        this.height = text.length * 30 + 20;
        y = (targetY > Game.getGameHeight() / 2) ? Game.getGameHeight() : 0;
        this.duration = DURATION_PER_CHAR * textString.length();
        isTutorial = !text[0].contains("Song");
    }

    private int maxCharsPerLine(String[] text) {
        int maxCharsPerLine = 0;
        for (String line : text) {
            maxCharsPerLine = Math.max(maxCharsPerLine, line.length());
        }
        return maxCharsPerLine;
    }

    public boolean isExpired() {
        return duration <= 0;
    }

    public void draw(Graphics2D g) {
        moveBox();

        if (isTutorial) {
            g.setColor(ContentValues.COLOR_TEXT_BOX_FILL_TUTORIAL);
        } else {
            g.setColor(ContentValues.COLOR_TEXT_BOX_FILL_SONG);
        }

        drawBoxBase(g);
        if (isTutorial) {
            drawTutorialBoxElements(g);
        }
    }

    private void drawTutorialBoxElements(Graphics2D g) {
        g.setColor(ContentValues.COLOR_TEXT_BOX_FILL_TUTORIAL);
        g.fillRect(x - 40, y - 20, 45, 45);
        g.setColor(Color.blue);
        g.drawRect(x - 40, y - 20, 45, 45);
        g.setColor(Color.yellow);
        g.setFont(ContentValues.QUESTION_FONT);
        g.drawString("?", x - 30, y + 20);
    }

    private void drawBoxBase(Graphics2D g) {
        g.fillRect(x, y, width, height);
        g.setColor(Color.blue);
        g.drawRect(x + 2, y + 2, width - 4, height - 4);
        g.setColor(Color.yellow);
        g.setFont(ContentValues.TEXT_BOX_FONT);
        for (int i = 0; i < text.length; i++) {
            g.drawString(text[i], x + 15, y + (i + 1) * 30);
        }
    }

    private void moveBox() {
        duration--;
        if (y > targetY) {
            y -= (y - targetY) / 20;
        } else if (y < targetY) {
            y += (targetY - y) / 20;
        }
    }

    public boolean isTutorial() {
        return isTutorial;
    }
}
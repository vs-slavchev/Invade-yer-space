package utility;


import main.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
    private ArrayList<String> text;
    private int x, y, targetY, width, height, duration;
    private boolean isTutorial;

    public TextBox(String textString, int x, int targetY) {
        String[] lines = textString.split(";");
        this.text = new ArrayList<>(Arrays.asList(lines));
        this.x = x;
        this.targetY = targetY;
        this.width = PIXELS_PER_CHAR * maxCharsPerLine(text);
        this.height = text.size() * 30 + 20;
        boolean targetYisInUpperScreenHalf = targetY > Game.SCREEN_HEIGHT / 2;
        y = targetYisInUpperScreenHalf ? Game.SCREEN_HEIGHT : 0;
        this.duration = DURATION_PER_CHAR * textString.length();
        isTutorial = !text.get(0).contains("Song");
    }

    private int maxCharsPerLine(ArrayList<String> text) {
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

        Color boxColor = isTutorial ? ContentValues.COLOR_TEXT_BOX_FILL_TUTORIAL
                : ContentValues.COLOR_TEXT_BOX_FILL_SONG;
        g.setColor(boxColor);
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
        for (int i = 0; i < text.size(); i++) {
            g.drawString(text.get(i), x + 15, y + (i + 1) * 30);
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
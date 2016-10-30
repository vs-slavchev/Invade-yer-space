package utility.image;

import main.Game;
import utility.ContentValues;

public class Gem {
    public double x, y, speed;
    public byte type;

    public Gem() {
        x = (int) (Math.random() * Game.SCREEN_WIDTH);
        y = (int) (Math.random() * Game.SCREEN_HEIGHT);
        speed = 0.5 + Math.random() * 0.4;
        type = (byte) (1 + Math.random() * ContentValues.NUMBER_DIFFERENT_GEMS);
    }

    public void move() {
        y += speed;
        if (y > Game.SCREEN_HEIGHT) {
            y = -50;
        }
    }
}

package utility.image;

import main.Game;
import utility.ContentValues;

import java.awt.*;
import java.util.ArrayList;

public class BackgroundStarsManager {

    private Particle[] backgroundStars = new Particle[ContentValues.NUM_BACKGROUND_STARS];

    public void generateBackgroundStars() {
        for (int i = 0; i < backgroundStars.length; i++) {
            int x = (int) (Math.random() * Game.SCREEN_WIDTH);
            int y = (int) (Math.random() * Game.SCREEN_HEIGHT);
            backgroundStars[i] = new Particle(x, y, 0, new Color(128, 128, 128), 0);
        }
    }

    public void drawStars(Graphics2D g) {
        for (Particle particle : backgroundStars) {
            particle.drawParticle(g);
        }
    }
}

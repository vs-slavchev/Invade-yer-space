package utility.image;

import main.Game;
import utility.ContentValues;

import java.awt.*;
import java.util.ArrayList;

public class BackgroundStarsManager {

    private ArrayList<Particle> backgroundStars = new ArrayList<>();

    public void drawStars(Graphics2D g) {
        for (Particle particle : backgroundStars) {
            particle.drawParticle(g);
        }
    }

    public void generateBackgroundStars() {
        for (int i = 0; i < ContentValues.NUM_BACKGROUND_STARS; i++) {
            int x = (int) (Math.random() * Game.getGameWidth());
            int y = (int) (Math.random() * Game.getGameHeight());
            backgroundStars.add(new Particle(x, y, 0, new Color(128, 128, 128), 0));
        }
    }
}

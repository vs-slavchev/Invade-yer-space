package utility.image;

import main.Game;
import utility.ContentValues;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class BackgroundPlanetManager {

    private BackgroundStarsManager starsManager = new BackgroundStarsManager();
    private CopyOnWriteArrayList<Animation> backgroundObjects = new CopyOnWriteArrayList<>();
    private String imageName;

    public BackgroundPlanetManager(String imageName) {
        this.imageName = imageName;
        starsManager.generateBackgroundStars();
    }

    public void update() {
        for (Animation animation : backgroundObjects) {
            animation.modifyPosition(0, ContentValues.PLANET_Y_VEL);
            if (animation.getY() > Game.SCREEN_HEIGHT) {
                backgroundObjects.remove(animation);
            }
        }
        generateNewObjects();
    }

    private void generateNewObjects() {
        Animation newBackgroundObj = generateBackgroundObject();
        if (newBackgroundObj != null) {
            backgroundObjects.add(newBackgroundObj);
        }
    }

    private Animation generateBackgroundObject() {
        int minY = Game.SCREEN_HEIGHT;
        for (Animation animation : backgroundObjects) {
            minY = Math.min(minY, animation.getY());
        }
        if (Math.random() < ContentValues.PLANET_SPAWN_CHANCE && minY > 0) {
            int x = (int) (-30 + Math.random() * Game.SCREEN_WIDTH);
            String name = imageName + (int) (1 + Math.random() * (ContentValues.NUMBER_DIFFERENT_PLANETS - 1));
            double scale = 1 + Math.random();
            return new Animation(x, ContentValues.PLANET_SPACING, 0, 1, name, false, false, scale);
        }
        return null;
    }

    public void drawBackgroundObjects(Graphics2D g) {
        starsManager.drawStars(g);
        for (Animation animation : backgroundObjects) {
            animation.drawAnimation(g);
        }
    }
}

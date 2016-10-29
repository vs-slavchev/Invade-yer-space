package utility.image;

import main.Game;
import utility.ContentValues;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class BackgroundPlanetManager {

    private BackgroundStarsManager starsManager = new BackgroundStarsManager();
    private CopyOnWriteArrayList<Animation> backgroundObjects = new CopyOnWriteArrayList<>();
    private int numberOfDifferentAnimations;
    private int yVel;
    private double chanceToSpawn;
    private int spacing;
    private String imageName;

    public BackgroundPlanetManager(String imageName) {
        this.numberOfDifferentAnimations = ContentValues.NUMBER_DIFFERENT_PLANETS;
        this.chanceToSpawn = ContentValues.PLANET_SPAWN_CHANCE;
        this.imageName = imageName;
        this.yVel = ContentValues.PLANET_Y_VEL;
        this.spacing = ContentValues.PLANET_SPACING;
        starsManager.generateBackgroundStars();
    }

    public void update() {
        for (Animation animation : backgroundObjects) {
            animation.modifyPosition(0, yVel);
            if (animation.getY() > Game.SCREEN_HEIGHT) {
                animation = generateBackgroundObject();
            }
        }
        generateNewObjects();
    }

    protected void generateNewObjects() {
        Animation newBackgroundObj = generateBackgroundObject();
        if (newBackgroundObj != null) {
            backgroundObjects.add(newBackgroundObj);
        }
    }

    private Animation generateBackgroundObject() {
        int minY = Game.SCREEN_HEIGHT;
        if (!backgroundObjects.isEmpty()) {
            for (Animation animation : backgroundObjects) {
                minY = Math.min(minY, animation.getY());
            }
        }
        if (Math.random() < chanceToSpawn && minY > 0) {
            int x = (int) (-30 + Math.random() * Game.SCREEN_WIDTH);
            String name = imageName + (int) (1 + Math.random() * (numberOfDifferentAnimations - 1));
            double scale = 1 + Math.random();
            return new Animation(x, spacing, 0, 1, name, false, false, scale);
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

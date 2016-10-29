package entities.utility;

import entities.Entity;
import entities.ShipEntity;
import entities.aliens.AlienEntity;
import main.Game;
import utility.ContentValues;
import utility.InvadeError;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class EntityManager {

    private final int maxAlienShipType = 8;
    private final int maxRows = 8;
    private final int maxColumns = ContentValues.ALIENS_MAX_COLUMNS;
    private Game game;
    private CopyOnWriteArrayList<Entity> alienEntities = new CopyOnWriteArrayList<>();
    private ArrayList<Entity> entities = new ArrayList<>();
    private HashSet<Entity> removeList = new HashSet<>();
    private ArrayList<Rectangle> aoeObjects = new ArrayList<>();
    private Entity ship;
    private int alienCount = 0;
    private String entitiesLevelMap;
    private int typesArray[] = new int[maxRows * maxColumns / 2];
    private double levelDifficulty = 1.0;

    public EntityManager(Game game) {
        this.game = game;
        generateNewEntitiesMap();
        if (entitiesLevelMap.length() != this.maxColumns * this.maxRows) {
            InvadeError.show("entitiesMap is not the exactly correct size!");
        }
    }

    public void generateNewEntitiesMap() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maxRows; i++) {
            StringBuilder innerSb = new StringBuilder();
            for (int j = 0; j < maxColumns / 2; j++) {
                innerSb.append(generateAlienType(i, j));
            }
            sb.append(innerSb);
            sb.append(innerSb.reverse());
        }
        entitiesLevelMap = sb.toString();
        levelDifficulty += ContentValues.LEVEL_DIFFICULTY_STEP;
    }

    private int generateAlienType(final int i, final int j) {
        int currentType = 1 + (int) (Math.random() * (maxAlienShipType - 1));
        currentType += balanceAccordingToMean(i * maxColumns / 2 + j);
        currentType = controlTypeBoundaries(currentType);
        typesArray[i * maxColumns / 2 + j] = currentType;
        return currentType;
    }

    private int balanceAccordingToMean(int size) {
        return (int) (levelDifficulty - calculateMean(size));
    }

    private double calculateMean(int size) {
        int sum = 0;
        for (int i = 0; i < size; i++) {
            sum += typesArray[i];
        }
        if (size == 0) {
            return 0;
        }
        return sum / size;
    }

    private int controlTypeBoundaries(int inType) {
        int type = Math.max(inType, 1);
        type = Math.min(type, maxAlienShipType);
        return type;
    }

    public void moveEntities(long delta) {
        for (int i = 0; i < alienEntities.size(); i++) {
            alienEntities.get(i).move(delta);
        }
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).move(delta);
        }
    }

    public void drawEntities(Graphics2D g) {
        for (int i = 0; i < alienEntities.size(); i++) {
            alienEntities.get(i).draw(g);
        }
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).draw(g);
        }
    }

    public void collideEntities() {
        for (int i = 0; i < entities.size(); i++) {
            for (int j = 0; j < alienEntities.size(); j++) {

                Entity first = entities.get(i);
                Entity second = alienEntities.get(j);

                if (first.checkCollisionWith(second)) {
                    first.collidedWith(second);
                    second.collidedWith(first);
                }
            }
        }

        if (!aoeObjects.isEmpty()) {
            for (int i = 0; i < alienEntities.size(); i++) {
                for (int j = 0; j < aoeObjects.size(); j++) {
                    Entity alien = alienEntities.get(i);
                    if (alien.getCollisionRectangle().intersects(aoeObjects.get(j))) {
                        removeList.add(alienEntities.get(i));
                    }
                }
            }
        }
        aoeObjects.clear();
    }

    public void removeEntities() {
        entities.removeAll(this.removeList);

        for (Entity entity : removeList) {
            if (entity instanceof AlienEntity) {
                game.notifyAlienKilled();
            }
        }
        alienEntities.removeAll(this.removeList);
        removeList.clear();
    }

    public void applyLogic() {
        for (Entity alien : alienEntities) {
            alien.doLogic();
        }
    }

    public void initEntities() {
        cleanUpEntities();

        ship = new ShipEntity(this.game, Game.getGameWidth() / 2, Game.getGameHeight() * 5 / 6);
        entities.add(this.ship);

        alienCount = 0;
        int type;
        for (int row = 0; row < maxRows; row++) {
            for (int col = 0; col < maxColumns; col++) {
                type = Character.getNumericValue(entitiesLevelMap.charAt(col + row * maxColumns));
                Entity alien = produceAlien(type, row, col);
                if (type != 0 && alien != null) {
                    alienEntities.add(alien);
                    alienCount++;
                }
            }
        }
    }

    public void cleanUpEntities() {
        entities.clear();
        alienEntities.clear();
        System.gc();
    }

    private AlienEntity produceAlien(int type, int row, int col) {
        AlienEntity alien = new AlienEntity(game, type, -100, -100);
        int offsetX = alien.getAnimation().getWidth() / 2;
        int offsetY = alien.getAnimation().getHeight() / 2;
        alien.setXY(100 + (col * Game.getGameWidth() / 30) - offsetX,
                20 + (row * Game.getGameHeight() / 20) - offsetY);
        return alien;
    }

    public void addToAlienEntities(Entity entity) {
        alienEntities.add(entity);
    }

    public EntityManager addToEntities(Entity entity) {
        entities.add(entity);
        return this;
    }

    public void speedUpAlienEntities() {
        for (int i = 0; i < alienEntities.size(); i++) {
            Entity entity = alienEntities.get(i);
            if (entity instanceof AlienEntity) {
                entity.setDeltaX(entity.getDeltaX() * 1.01);
                ((AlienEntity) entity).reduceShootTimeInterval();
            }
        }
    }

    public void createAoeObject(int x, int y, int widthAndHeight) {
        createAoeObject(x, y, widthAndHeight, widthAndHeight);
    }

    public void createAoeObject(int x, int y, int width, int height) {
        aoeObjects.add(new Rectangle(x, y, width, height));
    }

    // used for debugging
    private void occurencesOfNumber(String ch) {
        int occursOfOne = 0;
        int index = entitiesLevelMap.indexOf(ch);
        while (index != -1) {
            occursOfOne++;
            index = entitiesLevelMap.indexOf(ch, index + 1);
        }
        System.out.println(ch + "s: " + occursOfOne);
    }

    public void removeEntity(Entity entity) {
        removeList.add(entity);
    }

    public void decrementAlienCount() {
        alienCount--;
    }

    public int getAlienCount() {
        return alienCount;
    }

    public ShipEntity getShip() {
        return (ShipEntity) ship;
    }
}
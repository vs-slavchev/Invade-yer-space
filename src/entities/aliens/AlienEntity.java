package entities.aliens;

import entities.Entity;
import main.Game;
import utility.ContentValues;
import utility.image.Animation;
import utility.image.AnimationManager;
import utility.image.ImageManager;
import utility.sound.SoundManager;

import java.awt.*;
import java.util.Random;

public class AlienEntity extends Entity {

    private final int type;
    private final String spriteName;
    private int healthPoints;
    private long shootTimeInterval;
    private long lastShotTime = 60;
    private Animation animation;

    public AlienEntity(Game game, int type, int x, int y) {
        super(game, x, y);
        this.game = game;
        this.type = type;
        this.spriteName = "aliens/alienShip" + type;
        this.healthPoints = calculateMaxHealth(type);
        this.animation = new Animation(x, y, 0.07, 5, spriteName, true, false, 1);
        this.collisionWidth = animation.getWidth();
        this.collisionHeight = animation.getHeight();
        this.dx = -150;
        this.shootTimeInterval = getRandomRange(ContentValues.MIN_ATTACK_INTERVAL,
                ContentValues.MAX_ATTACK_INTERVAL);
    }

    public static int calculateMaxHealth(int type) {
        return ContentValues.ENEMY_HP_BASE + type * ContentValues.ENEMY_HP_PER_LVL_MULTIPLIER;
    }

    public void move(long delta) {
        //if an alien reaches the end of the screen - change direction
        if (dx < 0 && x < 10)
            game.updateLogic();
        if (dx > 0 && this.x > Game.SCREEN_WIDTH - 50) {
            game.updateLogic();
        }
        animation.update();
        animation.setPosition((int) x, (int) y);
        super.move(delta);
        tryToShoot();
    }

    public void doLogic() {
        dx = -dx; //swap direction
        y += 10; //move lower

        boolean enemyReachedPlayer = y > Game.SCREEN_HEIGHT - 30;
        if (enemyReachedPlayer) {
            game.notifyDeath();
        }
    }

    public void tryToShoot() {
        lastShotTime++;
        if (lastShotTime >= shootTimeInterval) {
            lastShotTime = 0;
            double horizontalDifference = Math.abs(game.getEntityManager().getShip().getX() - x);
            if (horizontalDifference < ContentValues.X_SHOOT_RANGE) {
                animation.setRunning(true);
                Entity alienShot = new AlienShotEntity(game,
                        (int) (x + collisionWidth / 2), (int) (y + 5), type);
                game.getEntityManager().addToAlienEntities(alienShot);
                AnimationManager.spawnAnimation(
                        "effects/muzzleFlash",
                        (int) x - 8 + animation.getWidth(),
                        (int) y - 8 + animation.getHeight(), 1);
            }
        }
    }

    public void reduceShootTimeInterval() {
        shootTimeInterval *= ContentValues.ALIEN_SHOOT_TIME_REDUCTION;
    }

    public int getRandomRange(int min, int max) {
        return min + new Random().nextInt(max - min);
    }

    public void draw(Graphics2D g) {
        animation.drawAnimation(g);
        if (game.isAlienHPBarDrawn()) {
            drawAlienHpBar(g);
        }
    }

    private void drawAlienHpBar(Graphics2D g) {
        g.setColor(Color.red);
        int barWidth = 7;
        g.fillRect((int) x, (int) y - barWidth, animation.getWidth(), barWidth);
        g.setColor(Color.green);
        int fillerBarWidth = (int) (healthPoints / (double) (calculateMaxHealth(type))
                * (animation.getWidth() - 2));
        g.fillRect((int) x + 1, (int) y - (barWidth - 1), fillerBarWidth, barWidth - 2);
    }

    public void takeDamage() {
        SoundManager.playExplosionSfx();
        int sparksX = (int) (x + 10 + Math.random() * animation.getWidth() / 2);
        int sparksY = (int) (y + 10 + Math.random() * animation.getHeight() / 2);
        AnimationManager.spawnAnimation("sparks", sparksX, sparksY, 1);
        healthPoints--;
    }

    public boolean isDead() {
        return healthPoints <= 0;
    }

    public void spawnExplosionAnimation() {
        if (Math.random() < ContentValues.CHANCE_TO_EXPLODE_ALIEN) {
            game.getEntityManager().createAoeObject((int) x - ContentValues.ROCKET_EXPLOSION_RADIUS,
                    (int) y - ContentValues.ROCKET_EXPLOSION_RADIUS,
                    ContentValues.ROCKET_EXPLOSION_RADIUS * 2);
            AnimationManager.spawnAnimation("explosion",
                    (int) x - ContentValues.ROCKET_EXPLOSION_RADIUS,
                    (int) y - ContentValues.ROCKET_EXPLOSION_RADIUS,
                    ContentValues.ROCKET_EXPLOSION_RADIUS
                            / (ImageManager.getImage("effects/explosion1").getHeight(null) / 2));
        } else {
            AnimationManager.spawnAnimation("explosion", (int) x, (int) y, 1);
        }
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void collidedWith(Entity other) {
        // collisions with aliens are handled in ship and shot classes
    }

}

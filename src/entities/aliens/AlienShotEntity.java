package entities.aliens;

import entities.Entity;
import entities.ShipEntity;
import main.Game;
import utility.ContentValues;
import utility.image.AnimationManager;
import utility.image.ImageManager;

import java.awt.*;

public class AlienShotEntity extends Entity {

    private int type;
    private boolean reflected = false;

    public AlienShotEntity(Game game, final int x, final int y, final int type) {
        super(x, y);
        this.game = game;
        this.dy = ContentValues.BASE_BULLET_VELOCITY + type * 50;
        this.type = type;
        Image sprite = ImageManager.getImage("projectiles/alienShot" + type);
        this.collisionWidth = sprite.getWidth(null);
        this.collisionHeight = sprite.getHeight(null);
    }

    public void move(final long delta) {
        super.move(delta);
        boolean outOfScreen = y > Game.SCREEN_HEIGHT + 20 || y < -20;
        if (outOfScreen) {
            game.getEntityManager().removeEntity(this);
        }
    }

    public void collidedWith(Entity other) {
        if (other instanceof ShipEntity) {
            ShipEntity ship = (ShipEntity) other;
            if (ship.isInvulnerable()) {
                if (!reflected) {
                    dx = calculateReflectedDeltaX(ship);
                    dy = -dy;
                    reflected = true;
                    AnimationManager.spawnAnimation("effects/reflectionSparks", (int) x - 10, (int) y - 15, 2.0);
                }
                return;
            }
            game.getEntityManager().removeEntity(this);
            game.notifyDeath();
        }
    }

    @Override
    public void doLogic() {
        // intentionally left empty
    }

    private double calculateReflectedDeltaX(ShipEntity ship) {
        // multiplying by 10 increases the angle
        return (x - ship.getCenteredX()) * 10;
    }

    @Override
    public void draw(Graphics2D g) {
        Image sprite = ImageManager.getImage("projectiles/alienShot" + type);
        int width = sprite.getWidth(null);
        int height = sprite.getHeight(null);
        // reflected bullets are facing up: image is flipped using negative height
        height = reflected ? -height : height;
        g.drawImage(sprite, (int) x, (int) y, width, height, null);

    }

}

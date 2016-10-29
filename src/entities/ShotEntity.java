package entities;

import entities.aliens.AlienEntity;
import main.Game;
import utility.ContentValues;
import utility.InvadeError;
import utility.image.AnimationManager;
import utility.image.ImageManager;

import java.awt.*;

public class ShotEntity extends Entity {

    // name is required for drawing and explosions
    private final int imageId;
    private boolean used = false;

    public ShotEntity(Game game, final int imageId, final int x, final int y, final int dx) {
        super(x, y);
        this.game = game;
        this.imageId = imageId;
        this.collisionWidth = ImageManager.getImage(getImageName(imageId)).getWidth(null);
        this.collisionHeight = ImageManager.getImage(getImageName(imageId)).getHeight(null);
        this.dx = dx;

        switch (imageId) {
            case 1:
                dy = ContentValues.ROCKET_VELOCITY;
                this.dx = -60 + Math.random() * 120;
                break;
            case 2:
                dy = ContentValues.MACHINE_GUN_VELOCITY;
                break;
            case 3:
                dy = ContentValues.SINE_WAVE_VELOCITY;
                break;
            case 4:
                dy = ContentValues.GREEN_SHOT_VELOCITY;
                break;
            case 5:
                dy = ContentValues.BOMB_VELOCITY;
                break;
            case 6:
                dy = ContentValues.SPEAR_VELOCITY;
                break;
            case 7:
                dy = ContentValues.SCATTER_SHOT_VELOCITY;
                break;
            case 8:
                dy = ContentValues.FLAKE_VELOCITY;
                break;
            default:
                InvadeError.show(getImageName(imageId) + " not supported by ShotEntity.");
                break;
        }
    }

    private String getImageName(int imageId) {
        return "projectiles/shot" + imageId;
    }

    public void move(final long delta) {
        super.move(delta);
        if (imageId == 2) {
            dx += Math.sin(y) * 20;
        }

        if (y < -20) {
            game.getEntityManager().removeEntity(this);
        }
    }

    public void collidedWith(Entity other) {
        if (used)
            return;
        if (other instanceof AlienEntity) {
            used = true;
            game.getEntityManager().removeEntity(this);

            aoeExplode();

            ((AlienEntity) other).takeDamage();
            if (((AlienEntity) other).isDead()) {
                ((AlienEntity) other).spawnExplosionAnimation();
                game.getEntityManager().removeEntity(other);
            }
        }
    }

    private void aoeExplode() {
        if (imageId == 4 || imageId == 7) {
            game.getEntityManager().createAoeObject(
                    (int) x - ContentValues.ROCKET_EXPLOSION_RADIUS,
                    (int) y - ContentValues.ROCKET_EXPLOSION_RADIUS,
                    ContentValues.ROCKET_EXPLOSION_RADIUS * 2);
            // the scale is the explosion radius or image radius
            AnimationManager.spawnAnimation("effects/explosion",
                    (int) x - ContentValues.ROCKET_EXPLOSION_RADIUS,
                    (int) y - ContentValues.ROCKET_EXPLOSION_RADIUS,
                    ContentValues.ROCKET_EXPLOSION_RADIUS
                            / (ImageManager.getImage("effects/explosion1").getHeight(null) / 2));
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(ImageManager.getImage(getImageName(imageId)), (int) x, (int) y, null);
    }

    @Override
    public void doLogic() {
        // intentionally left empty
    }
}

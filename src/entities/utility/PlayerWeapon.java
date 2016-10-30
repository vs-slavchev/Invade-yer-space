package entities.utility;

import entities.ShipEntity;
import entities.ShotEntity;
import main.Game;
import utility.ContentValues;
import utility.InvadeError;
import utility.image.ImageManager;
import utility.sound.SoundManager;

import java.awt.*;

public class PlayerWeapon {

    private final int bulletId;
    private final double heatAmplifier;
    private final int firingInterval;
    private Game game;
    private ShipEntity ship;
    private double overheatPercent = 0;
    private double overheatCoolingSpeed = 0.0;
    private long lastFireTime;

    public PlayerWeapon(Game game, ShipEntity ship, final int bulletId) {
        this.game = game;
        this.ship = ship;
        this.bulletId = bulletId;
        lastFireTime = System.currentTimeMillis();

        switch (bulletId) {
            case 1:
                firingInterval = 100;
                heatAmplifier = 1.75;
                break;
            case 2:
                firingInterval = 250;
                heatAmplifier = 3;
                break;
            case 3:
                firingInterval = 400;
                heatAmplifier = 7;
                break;
            case 4:
                firingInterval = 1_200;
                heatAmplifier = 60;
                break;
            case 5:
                firingInterval = 1_500;
                heatAmplifier = 0;
                break;
            case 6:
                firingInterval = 500;
                heatAmplifier = 0;
                break;
            case 7:
                firingInterval = 1_000;
                heatAmplifier = 0;
                break;
            case 8:
                firingInterval = 400;
                heatAmplifier = 0;
                break;
            default:
                firingInterval = 0;
                heatAmplifier = 0;
                InvadeError.show("bulletId: " + bulletId + " not supported by PlayerWeapon.");
                break;
        }
    }

    public void resetFireTimer() {
        lastFireTime = System.currentTimeMillis();
    }

    public boolean tryToFire() {
        if (overheatPercent >= 100) {
            if (!game.isWaitingForKeyPress()) {
                SoundManager.play("meCannon");
            }
            return false;
        }
        boolean readyToFire = System.currentTimeMillis() - lastFireTime >= firingInterval;
        if (!readyToFire || game.isWaitingForKeyPress()) {
            return false;
        }

        lastFireTime = System.currentTimeMillis();
        Image bullet = ImageManager.getImage("projectiles/shot" + bulletId);
        int x = ship.getX() + ship.getAnimation().getWidth() / 2 - bullet.getWidth(null) / 2;
        int y = ship.getY() - bullet.getHeight(null);
        ship.getAnimation().setRunning(true);
        createShot(x, y);

        overheatPercent += heatAmplifier;
        overheatCoolingSpeed = ContentValues.INITIAL_WEAPON_COOLING_SPEED;
        return true;
    }

    public void coolDown() {
        if (overheatPercent > 0) {
            if (overheatCoolingSpeed < ContentValues.MAX_WEAPON_COOLING_SPEED) {
                overheatCoolingSpeed *= ContentValues.WEAPON_COOLING_SPEED_MULTIPLIER;
            }
            overheatPercent -= overheatCoolingSpeed;
        }
    }

    public void createShot(final int x, final int y) {
        switch (bulletId) {
            case 1:
                game.getEntityManager().addToEntities(new ShotEntity(game, 1, x, y, 0));
                SoundManager.play("frequentShoot");
                break;
            case 2:
                game.getEntityManager()
                        .addToEntities(new ShotEntity(game, 2, x - 20, y, 0))
                        .addToEntities(new ShotEntity(game, 2, x + 20, y, 0));
                SoundManager.play("sineShoot");
                break;
            case 3:
                game.getEntityManager()
                        .addToEntities(new ShotEntity(game, 3, x - 15, y + 20, -80))
                        .addToEntities(new ShotEntity(game, 3, x + 15, y + 20, 80))
                        .addToEntities(new ShotEntity(game, 3, x, y, 0));
                SoundManager.play("tripleShoot");
                break;
            case 4:
                game.getEntityManager().addToEntities(new ShotEntity(game, 4, x, y, 0));
                SoundManager.play("cannonShoot");
                break;
            case 5:
                for (int i = -800; i <= 800; i += 100) {
                    game.getEntityManager()
                            .addToEntities(new ShotEntity(game, 5, x + i, y, 0));
                }
                break;
            case 6:
                for (int i = 0; i <= 180; i += 30) {
                    game.getEntityManager()
                            .addToEntities(new ShotEntity(game, 6, x - 30 + i / 3,
                            (int) (y + 30 - 30 * Math.sin(Math.toRadians(i))), -90 + i));
                }
                break;
            case 7:
                game.getEntityManager()
                        .addToEntities(new ShotEntity(game, 7, x, y + 15, 0))
                        .addToEntities(new ShotEntity(game, 7, x + 90, y + 50, 30))
                        .addToEntities(new ShotEntity(game, 7, x - 90, y + 50, -30));
                break;
            case 8:
                game.getEntityManager()
                        .addToEntities(new ShotEntity(game, 8, x - 160, y, 100))
                        .addToEntities(new ShotEntity(game, 8, x - 80, y, 80))
                        .addToEntities(new ShotEntity(game, 8, x + 160, y, -100))
                        .addToEntities(new ShotEntity(game, 8, x + 80, y, -80));
                break;
            default:
                InvadeError.show(bulletId + " not recognized by player weapon.");
                break;
        }
    }

    public double getOverheatPercent() {
        double visualRepresentation = Math.min(overheatPercent, 100);
        return Math.max(visualRepresentation, 0);
    }
}

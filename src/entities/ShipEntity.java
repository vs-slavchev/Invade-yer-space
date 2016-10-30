package entities;

import entities.aliens.AlienEntity;
import entities.utility.PlayerWeapon;
import entities.utility.StatusEffect;
import entities.utility.StatusEffect.StatusEffectType;
import main.Game;
import utility.ComboManager;
import utility.ContentValues;
import utility.InputController;
import utility.image.Animation;
import utility.image.AnimationManager;
import utility.image.ImageManager;
import utility.image.ParticleEmitter;
import utility.sound.SoundManager;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ShipEntity extends Entity {

    private double moveSpeed = ContentValues.PLAYER_MOVE_SPEED;
    private Animation animation;
    private ParticleEmitter particleEmitter = new ParticleEmitter(40, 6);
    private ComboManager comboManager;
    private CopyOnWriteArrayList<StatusEffect> buffs = new CopyOnWriteArrayList<>();
    private PlayerWeapon[] weapons = new PlayerWeapon[8];
    private int currentWeapon = 0;
    private boolean invulnerable = false;
    private boolean laserOn = false;
    private boolean autoFireOn = false;

    public ShipEntity(Game game, int x, int y) {
        super(x, y);
        this.game = game;
        comboManager = new ComboManager(game);
        animation = new Animation(x, y, 0.5, 6, "text/mainShip", true, false, 1);
        this.collisionWidth = animation.getWidth();
        this.collisionHeight = animation.getHeight();
        for (int i = 0; i < weapons.length; i++) {
            weapons[i] = new PlayerWeapon(game, this, i + 1);
        }
    }

    private static double applyFriction(double deltaSpeed) {
        if (deltaSpeed > ContentValues.FRICTION) {
            return deltaSpeed - ContentValues.FRICTION;
        } else if (deltaSpeed < -ContentValues.FRICTION) {
            return deltaSpeed + ContentValues.FRICTION;
        }
        return 0;
    }

    public void move(final long delta) {
        particleEmitter.emitParticle((int) x + animation.getWidth() / 2,
                (int) y + animation.getHeight());
        animation.update();
        animation.setPosition((int) x, (int) y);

        for (int i = 0; i < weapons.length; i++) {
            weapons[i].coolDown();
        }

        receiveComboBonusStatusEffect();
        updateStatusEffects();

        boolean limitedLeft = dx < 0 && x < 10;
        boolean limitedRight = dx > 0 && x > Game.SCREEN_WIDTH - collisionWidth - 15;
        boolean limitedDown = dy > 0 && y >= Game.SCREEN_HEIGHT - collisionHeight - 50;
        boolean limitedUp = dy < 0 && y < 10;

        if (limitedLeft || limitedRight || limitedDown || limitedUp) {
            return;
        }
        super.move(delta);
    }

    private void updateStatusEffects() {
        for (StatusEffect statusEffect : buffs) {
            statusEffect.update();
            if (!statusEffect.isActive()) {
                buffs.remove(statusEffect);
            }
        }

        invulnerable = false;
        laserOn = false;
        for (StatusEffect statusEffect : buffs) {
            if (statusEffect.getType() == StatusEffectType.SCATTER) {
                weapons[5].tryToFire();
            } else if (statusEffect.getType() == StatusEffectType.SPEARS) {
                weapons[4].tryToFire();
            } else if (statusEffect.getType() == StatusEffectType.QUAD_ROCKETS) {
                weapons[6].tryToFire();
            } else if (statusEffect.getType() == StatusEffectType.SHIELD) {
                invulnerable = true;
            } else if (statusEffect.getType() == StatusEffectType.LASER) {
                int laserWidth = ImageManager.getImage("projectiles/laser").getWidth(null);
                game.getEntityManager().createAoeObject(calculateLaserX(laserWidth), 0,
                        laserWidth, (int) y);
                laserOn = true;
            } else if (statusEffect.getType() == StatusEffectType.FLAKES) {
                weapons[7].tryToFire();
            }
        }
    }

    private void receiveComboBonusStatusEffect() {
        comboManager.updateRecentKillCount();
        StatusEffect newBuff = comboManager.giveBonusStatusEffect();
        if (newBuff != null) {
            buffs.add(newBuff);
        }
    }

    public void collidedWith(final Entity other) {
        if (other instanceof AlienEntity) {
            game.notifyDeath();
            autoFireOn = false;
        }
    }

    @Override
    public void doLogic() {
        // intentionally left empty
    }

    public void processInput(InputController inputController) {

        if (inputController.isLeftPressed()) {
            if (dx > -moveSpeed) {
                dx -= ContentValues.ACCELERATION;
            }
        } else if (inputController.isRightPressed()) {
            if (dx < moveSpeed) {
                dx += ContentValues.ACCELERATION;
            }
        } else {
            dx = applyFriction(dx);
        }

        if (inputController.isUpPressed()) {
            if (dy > -moveSpeed * 2 / 3) {
                dy -= ContentValues.ACCELERATION;
            }
        } else if (inputController.isDownPressed()) {
            if (dy < moveSpeed * 2 / 3) {
                dy += ContentValues.ACCELERATION;
            }
        } else {
            dy = applyFriction(dy);
        }

        if (inputController.isFirePressed() || autoFireOn) {
            if (weapons[currentWeapon].tryToFire()) {
                // if successfully shot then get knocked back as a result
                y += 2;
                y = Math.min(y, Game.SCREEN_HEIGHT - collisionHeight - 50);
                AnimationManager.spawnAnimation("effects/muzzleFlash", (int) x + 8, (int) y - 10, 1);
            }
        }
        if (inputController.isAutoFirePressed()) {
            autoFireOn = !autoFireOn;
            inputController.setAutoFirePressed(false);
        }

        checkShootingWeaponsInput(inputController);
    }

    private void checkShootingWeaponsInput(InputController inputController) {
        for (int numPress = 1; numPress < inputController.numberPressed.size(); numPress++) {
            if (inputController.numberPressed.get(numPress)) {
                int weaponIndex = numPress - 1;
                if (currentWeapon != weaponIndex) {
                    currentWeapon = weaponIndex;
                    weapons[currentWeapon].resetFireTimer();
                    SoundManager.play("weaponSwitch");
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        particleEmitter.drawParticles(g);
        animation.drawAnimation(g);

        drawLaser(g);
        drawShield(g);
        drawWeaponUI(g);
        comboManager.drawComboUI(g);
    }

    private void drawLaser(Graphics2D g) {
        if (laserOn) {
            Image laser = ImageManager.getImage("projectiles/laser");
            int laserWidth = laser.getWidth(null);
            int laserHeight = laser.getHeight(null);

            final int laserX = calculateLaserX(laserWidth);
            g.drawImage(laser, laserX, (int) (y - laserHeight), null);
            g.drawImage(laser,
                    laserX, 0,
                    (int) this.x + animation.getWidth() / 2 + laserWidth / 2,
                    (int) (y - laserHeight),
                    0, 0,
                    laserWidth, 2,
                    null);
        }
    }

    private int calculateLaserX(final int laserWidth) {
        return (int) this.x + animation.getWidth() / 2 - laserWidth / 2;
    }

    private void drawShield(Graphics2D g) {
        if (invulnerable) {
            int angle = 0;
            for (StatusEffect statusEffect : buffs) {
                if (statusEffect.getType() == StatusEffectType.SHIELD) {
                    angle = (int) (((double) statusEffect.getDuration()
                            / (double) (ContentValues.MAX_PLAYER_SHIELD_DURATION)) * 360);
                }
            }

            int arcOrigin = 90 - angle / 2;
            g.setColor(Color.CYAN);
            g.drawArc((int) x - 7, (int) y - 10, 46, 76, arcOrigin, angle);
            g.drawArc((int) x - 3, (int) y - 6, 38, 68, arcOrigin, angle);
            g.drawArc((int) x + 1, (int) y - 2, 30, 60, arcOrigin, angle);
        }
    }

    private void drawWeaponUI(Graphics2D g) {
        int baseX = ContentValues.WEAPON_UI_BASE_X;
        int baseY = game.getHeight() - 130;

        // if the player ship is to the far left, draw the UI on the right
        if (x < 180) {
            baseX = game.getWidth() - 150;
        }

        for (int i = 0; i < ContentValues.NUM_NORMAL_WEAPONS; i++) {
            int greenColorComponent = (int) ((100 - weapons[i].getOverheatPercent()) * 2.55);
            g.setColor(new Color(255, greenColorComponent, 0));
            g.fillRect(baseX + i * 35, (int) (baseY + 100 - weapons[i].getOverheatPercent()),
                    30, (int) weapons[i].getOverheatPercent());

            drawExclamationMark(g, baseX, baseY, i);
            drawProjectileIcons(g, baseX, i);
        }
    }

    private void drawProjectileIcons(Graphics2D g, int baseX, int i) {
        String iconName = "projectiles/shot" + (i + 1);
        int iconX = baseX + i * 35 + 15 - ImageManager.getImage(iconName).getWidth(null) / 2;
        g.drawImage(ImageManager.getImage(iconName),
                iconX, game.getHeight() - 30 + (ContentValues.NUM_NORMAL_WEAPONS - i) * 3, null);
    }

    private void drawExclamationMark(Graphics2D g, int baseX, int baseY, int i) {
        boolean weaponIsHot = weapons[i].getOverheatPercent() > 70;
        if (weaponIsHot) {
            int exclamationX = baseX + i * 35 + 12;
            g.fillRect(exclamationX, baseY - 40, 6, 20);
            g.fillRect(exclamationX, baseY - 15, 6, 5);
        }
    }

    public int getCenteredX() {
        return (int) (x + animation.getWidth() / 2);
    }

    public ComboManager getComboManager() {
        return comboManager;
    }

    public Animation getAnimation() {
        return animation;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setAutoFireOn(boolean autoFireOn) {
        this.autoFireOn = autoFireOn;
    }
}

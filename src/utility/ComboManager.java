package utility;

import entities.utility.StatusEffect;
import entities.utility.StatusEffect.StatusEffectType;
import main.Game;
import utility.image.ImageManager;

import java.awt.*;

public class ComboManager {

    private Game game;
    private long recentKillTime = 0;
    private int recentKillCount = 0;
    private int maxComboAchieved = 0;
    private int recentKillTimeWindow = ContentValues.KILL_TIME_WINDOW;

    private boolean shieldBonusReady = true;
    private boolean spearsBonusReady = true;
    private boolean scatterBonusReady = true;
    private boolean quadRocketsBonusReady = true;
    private boolean laserBonusReady = true;
    private boolean flakesBonusReady = true;

    public ComboManager(Game game) {
        this.game = game;
    }

    public void updateRecentKillCount() {
        if (System.currentTimeMillis() - recentKillTime > recentKillTimeWindow) {
            recentKillCount = 0;
            shieldBonusReady = true;
            spearsBonusReady = true;
            scatterBonusReady = true;
            quadRocketsBonusReady = true;
            laserBonusReady = true;
            flakesBonusReady = true;
        }
    }

    public StatusEffect giveBonusStatusEffect() {
        if (shieldBonusReady && recentKillCount >= 10) {
            shieldBonusReady = false;
            return new StatusEffect(StatusEffectType.SHIELD);
        } else if (quadRocketsBonusReady && recentKillCount >= 25) {
            quadRocketsBonusReady = false;
            return new StatusEffect(StatusEffectType.QUAD_ROCKETS);
        } else if (scatterBonusReady && recentKillCount >= 50) {
            scatterBonusReady = false;
            return new StatusEffect(StatusEffectType.SCATTER);
        } else if (spearsBonusReady && recentKillCount >= 70) {
            spearsBonusReady = false;
            return new StatusEffect(StatusEffectType.SPEARS);
        } else if (laserBonusReady && recentKillCount >= 85) {
            laserBonusReady = false;
            return new StatusEffect(StatusEffectType.LASER);
        } else if (flakesBonusReady && recentKillCount >= 120) {
            flakesBonusReady = false;
            return new StatusEffect(StatusEffectType.FLAKES);
        }
        return null;
    }

    public void incrementRecentKillCount() {
        recentKillCount++;
        recentKillTime = System.currentTimeMillis();

        if (recentKillCount > maxComboAchieved) {
            maxComboAchieved = recentKillCount;
        }
    }

    public void drawComboUI(Graphics2D g) {
        if (recentKillCount > 5) {
            int yBase = game.getHeight() - 430;
            boolean playerIsLeft = game.getEntityManager().getShip().getX() < game.getWidth() - 280;
            int xBase = playerIsLeft ? game.getWidth() - 200 : 35;
            g.drawImage(ImageManager.getImage("text/comboText"), xBase - 25, yBase + 150, null);

            drawRevolvingCircle(g, yBase, xBase);
            drawComboDigits(g, recentKillCount, xBase + 40, yBase + 23);
        }
    }

    private void drawRevolvingCircle(Graphics2D g, int yBase, int xBase) {
        int arcLength = (int) (((double) (System.currentTimeMillis() - recentKillTime) /
                (double) recentKillTimeWindow) * 360);
        int greenComponent = 255 - (int) (arcLength / 360.00 * 255);
        greenComponent = Math.max(greenComponent, 0);
        greenComponent = Math.min(greenComponent, 255);
        g.setColor(new Color(0, greenComponent, 255));
        g.fillArc(xBase, yBase, 150, 150, 90, -arcLength);
        g.setColor(Color.BLACK);
        g.drawArc(xBase + 5, yBase + 5, 140, 140, 90, -arcLength);
    }

    private void drawComboDigits(Graphics2D g, final int value, final int xBase, final int yBase) {
        Image comboDigitsImg = ImageManager.getImage("text/comboDigits");
        int digitWidth = comboDigitsImg.getWidth(null) / 10;
        int digitHeight = comboDigitsImg.getHeight(null);
        String scoreText = Integer.toString(value);
        int modified_xBase = xBase - (scoreText.length() - 1) * (digitWidth / 2);

        for (int i = 0; i < scoreText.length(); i++) {
            int currentDigit = Integer.parseInt(Character
                    .toString(scoreText.charAt(i)));
            g.drawImage(comboDigitsImg, modified_xBase + i * digitWidth, yBase,
                    modified_xBase + (i * digitWidth) + digitWidth, yBase + digitHeight,
                    currentDigit * digitWidth, 0,
                    (currentDigit + 1) * digitWidth, digitHeight,
                    null);
        }
    }

    public void drawComboScore(Graphics2D g) {
        if (maxComboAchieved > 0) {
            Image maxComboImg = ImageManager.getImage("text/maxComboText");
            g.drawImage(maxComboImg,
                    Game.SCREEN_WIDTH / 2 - maxComboImg.getWidth(null) / 2, Game.SCREEN_HEIGHT / 2,
                    null);

            drawComboDigits(g, Game.getScore(), Game.SCREEN_WIDTH / 2 - 30,
                    Game.SCREEN_HEIGHT * 2 / 3 - 50);
        }
    }

    public int getMaxComboAchieved() {
        return maxComboAchieved;
    }
}

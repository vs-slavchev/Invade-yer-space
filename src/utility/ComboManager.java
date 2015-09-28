package utility;

import java.awt.Color;
import java.awt.Graphics2D;

import entities.ShipEntity;
import entities.utility.StatusEffect;
import main.Game;
import utility.image.ImageManager;

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
	
	public ComboManager(Game game){
		this.game = game;
	}
	
	public void updateRecentKillCount(){
		if (System.currentTimeMillis() - recentKillTime  > recentKillTimeWindow){
			recentKillCount = 0;
			shieldBonusReady = true;
			spearsBonusReady = true;
			scatterBonusReady = true;
			quadRocketsBonusReady = true;
			laserBonusReady = true;
			flakesBonusReady = true;
		}
	}
	
	public StatusEffect giveBonusStatusEffect(){
		if (shieldBonusReady && recentKillCount >= 10){
			shieldBonusReady = false;
			return new StatusEffect("shield");
		}
		else if(quadRocketsBonusReady && recentKillCount >= 25){
			quadRocketsBonusReady = false;
			return new StatusEffect("quadRockets");
		}
		else if(scatterBonusReady && recentKillCount >= 50){
			scatterBonusReady = false;
			return new StatusEffect("scatter");
		}
		else if (spearsBonusReady && recentKillCount >= 70){
			spearsBonusReady = false;
			return new StatusEffect("spears");
		}
		else if (laserBonusReady && recentKillCount >= 85){
			laserBonusReady = false;
			return new StatusEffect("laser");
		}
		else if (flakesBonusReady && recentKillCount >= 120){
			flakesBonusReady = false;
			return new StatusEffect("flakes");
		}
		return null;
	}
	
	public void incrementRecentKillCount(){
		recentKillCount++;
		recentKillTime = System.currentTimeMillis();
		
		if (recentKillCount > maxComboAchieved){
			maxComboAchieved = recentKillCount;
		}
	}
	
	public void drawComboUI(Graphics2D g) {
		if (recentKillCount > 5) {
			// prepare the bases for the X and Y
			int yBase = game.getHeight() - 430;
			int xBase = (game.getEntityManager().getShip().getX() < game.getWidth() - 280) ?
					game.getWidth() - 200 : 35;
			g.drawImage(ImageManager.getImage("text/comboText"),
					xBase - 25, yBase + 150, null);
			
			// draw the revolving circle
			int arcLength = (int) (((double) (System.currentTimeMillis() - recentKillTime) /
					(double) recentKillTimeWindow) * 360);
			int greenComponent = 255 - (int)((arcLength/360.00)*255);
			greenComponent = ContentValues.controlMinMax(greenComponent, 0, 255);
			g.setColor(new Color(0, greenComponent, 255));
			g.fillArc(xBase, yBase, 150, 150, 90, -arcLength);
			g.setColor(Color.BLACK);
			g.drawArc(xBase + 5, yBase + 5, 140, 140, 90, -arcLength);
			
			/*	prepare values for drawing ingame; this preparation is not
				needed when drawing the digits in the menu */
			xBase += 40;
			yBase += 23;
			drawComboDigits(g, recentKillCount, xBase, yBase);
		}
	}

	public void drawComboDigits(Graphics2D g, int value, int xBase, int yBase) {
		
		int digitWidth = ImageManager.getImage("text/comboDigits").getWidth(null) / 10;
		int digitHeight = ImageManager.getImage("text/comboDigits").getHeight(null);
		String scoreText = Integer.toString(value);
		xBase -= (scoreText.length() - 1) * (digitWidth/2);

		for (int i = 0; i < scoreText.length(); i++) {
			int currentDigit = Integer.parseInt(Character
					.toString(scoreText.charAt(i)));
			g.drawImage(ImageManager.getImage("text/comboDigits"), xBase + i
					* digitWidth, yBase, xBase
					+ i * digitWidth + digitWidth, yBase + digitHeight, currentDigit * digitWidth, 0,
					(currentDigit + 1) * digitWidth, digitHeight, null);
		}
	}
	
	// draw the combo score if needed
	public void drawComboScore(Graphics2D g) {
		if (maxComboAchieved > 0) {
			g.drawImage(ImageManager.getImage("text/maxComboText"),
					Game.WIDTH	/ 2	- ImageManager.getImage("text/maxComboText")
					.getWidth(null) / 2, Game.HEIGHT / 2, null);
			
			drawComboDigits(g, Game.getScore(), Game.WIDTH / 2 - 30,
					Game.HEIGHT * 2 / 3 - 50);
		}
	}
	
	public int getMaxComboAchieved() {
		return maxComboAchieved;
	}
	
}

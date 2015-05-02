package utility;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import entities.utility.StatusEffect;
import main.Game;
import utility.image.ImageManager;

public class ComboManager {
	
	private Game game;
	private long recentKillTime = 0;
	private int recentKillCount = 6;
	private int recentKillTimeWindow = 5_000;
	
	private boolean shieldBonusReady = true;
	private boolean spearsBonusReady = true;
	private boolean scatterBonusReady = true;
	private boolean quadRocketsBonusReady = true;
	private boolean laserBonusReady = true;
	
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
		else if (laserBonusReady && recentKillCount >= 90){
			laserBonusReady = false;
			return new StatusEffect("laser");
		}
		return null;
	}
	
	public void incrementRecentKillCount(){
		recentKillCount++;
		recentKillTime = System.currentTimeMillis();
	}
	
	public void drawComboUI(Graphics2D g) {
		if (recentKillCount > 5) {
			int yBase = game.getHeight() - 430;
			int xBase = (game.getEntityManager().getShip().getX() < game.getWidth() - 280) ? game.getWidth() - 200 : 25;
			g.drawImage(ImageManager.getImage("text/comboText"), xBase - 25, yBase + 150, null);
			
			int arcLength = (int) (((double) (System.currentTimeMillis() - recentKillTime) / (double) recentKillTimeWindow) * 360);
			int greenComponent = 255 - (int)((arcLength/360.00)*255);
			greenComponent = ContentValues.controlMinMax(greenComponent, 0, 255);
			g.setColor(new Color(0, greenComponent, 255));
			g.fillArc(xBase, yBase, 150, 150, 90, -arcLength);
			g.setColor(Color.BLACK);
			g.drawArc(xBase + 5, yBase + 5, 140, 140, 90, -arcLength);
			
			
			
			int digitWidth = ImageManager.getImage("text/comboDigits").getWidth(null) / 10;
			int digitHeight = ImageManager.getImage("text/comboDigits").getHeight(null);
			String scoreText = Integer.toString(recentKillCount);
			int numbersX = xBase + 40 - (scoreText.length() - 1) * (digitWidth/2);
			int numbersY = yBase + 23;
			for (int i = 0; i < scoreText.length(); i++) {
				int currentDigit = Integer.parseInt(Character
						.toString(scoreText.charAt(i)));
				g.drawImage(ImageManager.getImage("text/comboDigits"), numbersX + i
						* digitWidth, numbersY, numbersX
						+ i * digitWidth + digitWidth, numbersY + digitHeight, currentDigit * digitWidth, 0,
						(currentDigit + 1) * digitWidth, digitHeight, null);
			}

			/*int secondGreenComponent = 255 - (int)((recentKillCount / 50.00) * 255);
			secondGreenComponent = ContentValues.controlMinMax(secondGreenComponent, 0, 255);
			g.setColor(new Color(255, secondGreenComponent, 0));
			g.setFont(new Font("Dialog", Font.BOLD, 60+recentKillCount*3/2));
			g.drawString(String.valueOf(recentKillCount),
					xBase + 75
					- g.getFontMetrics().stringWidth(String.valueOf(recentKillCount)) / 2, (int)(yBase + 95 + recentKillCount/3.00));*/
		}
	}
}

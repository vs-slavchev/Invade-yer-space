package entities.utility;

import javax.swing.JOptionPane;

import entities.ShipEntity;
import entities.ShotEntity;
import utility.image.ImageManager;
import utility.sound.SoundManager;
import main.Game;

public class PlayerWeapon {
	
	private Game game;
	private String bulletName;
	private double overheatPercent = 0;
	private double overheatCoolingSpeed = 0.0;
	private double heatAmplifier;
	private long lastFireTime;
	private int firingInterval;
	
	public PlayerWeapon(Game game, String bulletName){
		this.game = game;
		this.bulletName = bulletName;
		lastFireTime = System.currentTimeMillis();
		
		switch(bulletName){
		case "projectiles/shot1":
			firingInterval = 100;
			heatAmplifier = 1.75;
			break;
		case "projectiles/shot2":
			firingInterval = 250;
			heatAmplifier = 3;
			break;
		case "projectiles/shot3":
			firingInterval = 400;
			heatAmplifier = 7;
			break;
		case "projectiles/shot4":
			firingInterval = 1200;
			heatAmplifier = 60;
			break;
		case "projectiles/shot5":
			firingInterval = 1500;
			heatAmplifier = 0;
			break;
		case "projectiles/shot6":
			firingInterval = 500;
			heatAmplifier = 0;
			break;
		case "projectiles/shot7":
			firingInterval = 1_000;
			heatAmplifier = 0;
			break;
		case "projectiles/shot8":
			firingInterval = 400;
			heatAmplifier = 0;
			break;
		default:
			JOptionPane.showMessageDialog(null,
					"Error: \n" + bulletName + "\nnot supported by PlayerWeapon.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			 System.exit(0);
			break;
		}
	}
	
	public void resetFireTimer(){
		lastFireTime = System.currentTimeMillis();
	}
	
	/*
	 * returns true on successful shooting
	 * false when not allowed to shoot
	 */
	public boolean tryToFire(){
		if (overheatPercent >= 100){
			if (!game.isWaitingForKeyPress()) {
				SoundManager.play("meCannon");
			}
			return false;
		}
		if( System.currentTimeMillis() - lastFireTime < firingInterval || game.isWaitingForKeyPress()){
			return false;
		}
		
		lastFireTime = System.currentTimeMillis();
		int x = game.getEntityManager().getShip().getX()
				+ ((ShipEntity) game.getEntityManager().getShip()).getAnimation().getDimensionX()/2
				- ImageManager.getImage(bulletName).getWidth(null)/2;
		int y = game.getEntityManager().getShip().getY() - ImageManager.getImage(bulletName).getHeight(null);
		((ShipEntity) game.getEntityManager().getShip()).getAnimation().setRunning(true);
		createShot(x, y);
		
		overheatPercent += heatAmplifier;
		overheatCoolingSpeed = 0.0000001; // same for all weapons
		return true;
	}
	
	public void coolDown(){
		if (overheatPercent > 0){
			if (overheatCoolingSpeed < 0.15){ // maximum cooling speed
				overheatCoolingSpeed *= 1.05; // cooling speed is increased
			}
			overheatPercent -= overheatCoolingSpeed;
		}
	}
	
	public void createShot(int x, int y) {
		switch (bulletName) {
		case "projectiles/shot1":
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x, y, 0));
			SoundManager.play("frequentShoot");
			break;
		case "projectiles/shot2":
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x-20, y, 0));
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x+20, y, 0));
			SoundManager.play("sineShoot");
			break;
		case "projectiles/shot3":
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x-15, y+20, -80));
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x+15, y+20, 80));
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x, y, 0));
			SoundManager.play("tripleShoot");
			break;
		case "projectiles/shot4":
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x, y, 0));
			SoundManager.play("cannonShoot");
			break;
		case "projectiles/shot5":
			for (int i = -800; i <= 800; i += 100) {
				game.getEntityManager().addToEntities(
						new ShotEntity(game, bulletName, x + i, y, 0));
			}
			break;
		case "projectiles/shot6":
			for (int i = 0; i <= 180; i += 30){
				game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x-30+i/3, (int)(y+30-30*Math.sin(Math.toRadians(i))), -90 + i));
			}
			break;
		case "projectiles/shot7":
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x, y+15, 0));
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x+90, y+50, 30));
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x-90, y+50, -30));
			break;
		case "projectiles/shot8":
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x - 160, y, 100));
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x - 80, y, 80));
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x + 160, y, -100));
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x + 80, y, -80));
			break;
		default:
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x, y, 0));
			break;
		}
	}
	
	public double getOverheatPercent() {
		if (overheatPercent > 100){
			return 100;
		}else if (overheatPercent < 0){
			return 0;
		}
		return overheatPercent;
	}
}

package entities;

import utility.image.ImageManager;
import main.Game;

public class PlayerWeapon {
	
	private Game game;
	private String bulletName;
	private double overheatPercent = 0;
	private double overheatCoolingSpeed = 0.0;
	private double heatAmplifier;
	private int ammo;
	private long lastFireTime;
	private int firingInterval;
	
	public PlayerWeapon(Game game, String bulletName){
		this.game = game;
		this.bulletName = bulletName;
		
		switch(bulletName){
		case "projectiles/shot1":
			firingInterval = 100;
			ammo = 200;
			heatAmplifier = 1.8;
			break;
		case "projectiles/shot2":
			firingInterval = 250;
			ammo = 100;
			heatAmplifier = 3;
			break;
		case "projectiles/shot3":
			firingInterval = 400;
			ammo = 20;
			heatAmplifier = 7;
			break;
		case "projectiles/shot4":
			firingInterval = 600;
			ammo = 10;
			heatAmplifier = 20;
			break;
		default:
			this.bulletName = "projectiles/shot1";
			firingInterval = 100;
			ammo = 200;
			heatAmplifier = 1.3;
			break;
		}
	}
	
	public void resetFireTimer(){
		lastFireTime = System.currentTimeMillis();
	}
	
	public void tryToFire(){
		if (overheatPercent >= 100){
			return;
		}
		if( System.currentTimeMillis() - lastFireTime < firingInterval)
			return;
		
		lastFireTime = System.currentTimeMillis();
		int x = game.getEntityManager().getShip().getX()
				+ ((ShipEntity) game.getEntityManager().getShip()).getAnimation().getDimensionX()/2
				- ImageManager.getImage(bulletName).getWidth(null)/2;
		int y = game.getEntityManager().getShip().getY() - ImageManager.getImage(bulletName).getHeight(null);
		((ShipEntity) game.getEntityManager().getShip()).getAnimation().setRunning(true);
		createShot(x, y);
		
		overheatPercent += heatAmplifier;
		overheatCoolingSpeed = 0.0000001;
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
			break;
		case "projectiles/shot2":
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x-20, y, 0));
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x+20, y, 0));
			break;
		case "projectiles/shot3":
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x-15, y+20, -80));
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x+15, y+20, 80));
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x, y, 0));
			break;
		case "projectiles/shot4":
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x-15, y+35, -20));
			game.getEntityManager().addToEntities(new ShotEntity(game, bulletName, x+15, y+35, 20));
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

	public int getAmmo() {
		return ammo;
	}
}

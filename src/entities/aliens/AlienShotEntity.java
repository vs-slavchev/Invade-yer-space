package entities.aliens;

import java.awt.Graphics2D;

import utility.image.ImageManager;
import entities.Entity;
import entities.ShipEntity;
import main.Game;

public class AlienShotEntity extends Entity{

	private double moveSpeed = 400;
	
	public AlienShotEntity(Game game, int x, int y){
		super( x, y);
		this.game = game;
		this.dy = this.moveSpeed;
		this.collisionWidth = 5;
		this.collisionHeight = 8;
	}
	
	public void move(long delta){
		super.move(delta);
		if( y > Game.getGameHeight() + 20 ){
			game.getEntityManager().removeEntity(this);
		}
	}
	
	public void collidedWith(Entity other){
		if( other instanceof ShipEntity){
			game.getEntityManager().removeEntity(this);
			game.notifyDeath();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(ImageManager.getImage("projectiles/alienShot1"), (int)x, (int)y, null);
		
	}
	
}

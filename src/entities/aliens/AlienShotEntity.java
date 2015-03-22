package entities.aliens;

import java.awt.Graphics2D;

import utility.image.ImageManager;
import entities.Entity;
import entities.ShipEntity;
import main.Game;

public class AlienShotEntity extends Entity{

	private int type;
	
	public AlienShotEntity(Game game, int x, int y, int type){
		super(x, y);
		this.game = game;
		this.dy = 250 + type*50;
		this.type = type;
		this.collisionWidth = ImageManager.getImage("projectiles/alienShot" + type).getWidth(null);
		this.collisionHeight = ImageManager.getImage("projectiles/alienShot" + type).getHeight(null);
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
		g.drawImage(ImageManager.getImage("projectiles/alienShot" + type), (int)x, (int)y, null);
		
	}
	
}

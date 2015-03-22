package entities;

import java.awt.Graphics2D;

import utility.image.ImageManager;
import entities.aliens.AlienEntity;
import main.Game;

public class ShotEntity extends Entity{
	
	private boolean used = false;
	
	public ShotEntity(Game game, int x, int y){
		super( x, y);
		this.game = game;
		this.dy = -600;
		this.dx = -50 + Math.random()*100;
		this.collisionWidth = 5;
		this.collisionHeight = 8;
	}
	
	public void move(long delta){
		super.move(delta);
		if( y < -20 ){
			game.getEntityManager().removeEntity(this);
		}
	}
	
	public void collidedWith(Entity other){
		if(used)
			return;
		if( other instanceof AlienEntity){
			used = true;
			game.getEntityManager().removeEntity(this);
			((AlienEntity) other).takeDamage(1);
			if( ((AlienEntity) other).isDead() ){
				((AlienEntity) other).spawnExplosionAnimation();
				game.getEntityManager().removeEntity(other);
				game.notifyAlienKilled();
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(ImageManager.getImage("projectiles/shot1"), (int)x, (int)y, null);
	}

}

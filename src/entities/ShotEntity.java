package entities;

import java.awt.Graphics2D;

import utility.ContentValues;
import utility.image.AnimationManager;
import utility.image.ImageManager;
import entities.aliens.AlienEntity;
import main.Game;

public class ShotEntity extends Entity{
	
	private boolean used = false;
	private String name;
	
	public ShotEntity(Game game, String name, int x, int y, int dx){
		super(x, y);
		this.game = game;
		this.name = name;
		this.collisionWidth = ImageManager.getImage(name).getWidth(null);
		this.collisionHeight = ImageManager.getImage(name).getHeight(null);
		
		switch (name) {
		case "projectiles/shot1":
			dy = -600;
			this.dx = -60 + Math.random() * 120;
			break;
		case "projectiles/shot2":
			dy = -400;
			this.dx = dx;
			break;
		case "projectiles/shot3":
			dy = -500;
			this.dx = dx;
			break;
		case "projectiles/shot4":
			dy = -250;
			this.dx = dx;
			break;
		case "projectiles/shot5":
			dy = -450;
			this.dx = dx;
			break;
		case "projectiles/shot6":
			dy = -400;
			this.dx = dx;
			break;
		case "projectiles/shot7":
			dy = -200;
			this.dx = dx;
			break;
		}
	}
	
	public void move(long delta){
		super.move(delta);
		if (name.equals("projectiles/shot2")){
			dx += Math.sin(y)*20;
		}
		
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
			
			performRocketCollision();
			
			((AlienEntity) other).takeDamage();
			if( ((AlienEntity) other).isDead() ){
				((AlienEntity) other).spawnExplosionAnimation();
				game.getEntityManager().removeEntity(other);
			}
		}
	}

	private void performRocketCollision() {
		if (name.equals("projectiles/shot4") || name.equals("projectiles/shot7")){
			game.getEntityManager().createAoEObject((int)x - ContentValues.ROCKET_EXPLOSION_RADIUS,
					(int)y - ContentValues.ROCKET_EXPLOSION_RADIUS,
					ContentValues.ROCKET_EXPLOSION_RADIUS*2, ContentValues.ROCKET_EXPLOSION_RADIUS*2);
			// the scale is = explosion radius/image raduis
			AnimationManager.getAnimationManager().spawnAnimation("effects/explosion",
					(int)x - ContentValues.ROCKET_EXPLOSION_RADIUS,
					(int)y - ContentValues.ROCKET_EXPLOSION_RADIUS, ContentValues.ROCKET_EXPLOSION_RADIUS/(ImageManager.getImage("effects/explosion1").getHeight(null)/2));
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(ImageManager.getImage(name), (int)x, (int)y, null);
	}

}

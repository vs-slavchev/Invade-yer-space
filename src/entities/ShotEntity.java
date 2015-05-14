package entities;

import java.awt.Graphics2D;

import javax.swing.JOptionPane;

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
			dy = ContentValues.BULLETS_VELOCITY[0];
			this.dx = -60 + Math.random() * 120;
			break;
		case "projectiles/shot2":
			dy = ContentValues.BULLETS_VELOCITY[1];
			this.dx = dx;
			break;
		case "projectiles/shot3":
			dy = ContentValues.BULLETS_VELOCITY[2];
			this.dx = dx;
			break;
		case "projectiles/shot4":
			dy = ContentValues.BULLETS_VELOCITY[3];
			this.dx = dx;
			break;
		case "projectiles/shot5":
			dy = ContentValues.BULLETS_VELOCITY[4];
			this.dx = dx;
			break;
		case "projectiles/shot6":
			dy = ContentValues.BULLETS_VELOCITY[5];
			this.dx = dx;
			break;
		case "projectiles/shot7":
			dy = ContentValues.BULLETS_VELOCITY[6];
			this.dx = dx;
			break;
		case "projectiles/shot8":
			dy = ContentValues.BULLETS_VELOCITY[7];
			this.dx = dx;
			break;
		default:
			JOptionPane.showMessageDialog(null,
					"Error: \n" + name + "\nnot supported by ShotEntity.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			 System.exit(0);
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
			AnimationManager.spawnAnimation("effects/explosion",
					(int)x - ContentValues.ROCKET_EXPLOSION_RADIUS,
					(int)y - ContentValues.ROCKET_EXPLOSION_RADIUS, ContentValues.ROCKET_EXPLOSION_RADIUS/(ImageManager.getImage("effects/explosion1").getHeight(null)/2));
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(ImageManager.getImage(name), (int)x, (int)y, null);
	}

}

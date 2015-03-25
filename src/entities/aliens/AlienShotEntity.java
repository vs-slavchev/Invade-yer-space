package entities.aliens;

import java.awt.Graphics2D;

import utility.image.AnimationManager;
import utility.image.ImageManager;
import entities.Entity;
import entities.ShipEntity;
import main.Game;

public class AlienShotEntity extends Entity{

	private int type;
	// reflected by the player shield; reflected bullets cannot be reflected again
	private boolean reflected = false;
	
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
		if( y > Game.getGameHeight() + 20  || y < -20){
			game.getEntityManager().removeEntity(this);
		}
	}
	
	public void collidedWith(Entity other){
		if( other instanceof ShipEntity){
			if (((ShipEntity) other).isInvulnerable()){
				if (!reflected) {
					dx = (x - ((ShipEntity) other).getCenteredX())*10;
					dy = -dy;
					reflected = true;
					AnimationManager.getAnimationManager().spawnAnimation("effects/reflectionSparks", (int)x-10, (int)y-15, 2.0);
				}
				return;
			}
			game.getEntityManager().removeEntity(this);
			game.notifyDeath();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		int width = ImageManager.getImage("projectiles/alienShot" + type).getWidth(null);
		int height = ImageManager.getImage("projectiles/alienShot" + type).getHeight(null);
		// reflected bullets are facing up i.e. image is flipped using negative height
		height = reflected ? -height : height;
		g.drawImage(ImageManager.getImage("projectiles/alienShot" + type), (int)x, (int)y, width, height, null);
		
	}
	
}

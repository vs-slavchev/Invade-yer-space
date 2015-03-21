package entities;

import java.awt.Graphics2D;

import entities.aliens.AlienEntity;
import main.Game;
import utility.image.ImageManager;
import utility.InputController;
import utility.image.ParticleEmitter;

public class ShipEntity extends Entity{

	private long lastFire = 0;
	private long firingInterval = 200;
	private double moveSpeed = 300;
	private ParticleEmitter particleEmitter = new ParticleEmitter(40, 6);
	
	//private int overheat = 0; // max is always 100, used to draw rect indicator
	
	public ShipEntity(Game game, int x, int y) {
		super( x, y);
		this.game = game;
		this.collisionWidth = ImageManager.getImage("mainShip").getWidth(null);
		this.collisionHeight = ImageManager.getImage("mainShip").getHeight(null);
	}
	
	public void move(long delta){
		particleEmitter.emittParticle((int)x + ImageManager.getImage("mainShip").getWidth(null)/2, (int)y + ImageManager.getImage("mainShip").getHeight(null));
		
		if( dx < 0 && x < 10 ){
			return;
		}
		if( dx > 0 && x > game.getGameWidth() - 50 ){
			return;
		}
		if( dy > 0 && y > game.getGameHeight() - collisionHeight ){
			return;
		}
		if( dy < 0 && y < 10 ){
			return;
		}
		super.move(delta);
	}
	
	public void collidedWith(Entity other){
		if( other instanceof AlienEntity){
			this.game.notifyDeath();
		}
	}
	
	public void processInput(InputController inputController){
		
		dx = 0;
		dy = 0;
		if( inputController.isLeftPressed() )
			dx = -this.moveSpeed;
		else if( inputController.isRightPressed() )
			dx =  this.moveSpeed;
		if( inputController.isUpPressed() )
			dy = -this.moveSpeed/2;
		else if( inputController.isDownPressed() )
			dy = this.moveSpeed/2;
		if( inputController.isFirePressed() )
			tryToFire();
	}
	
	public void tryToFire(){
		if( System.currentTimeMillis() - lastFire < firingInterval)
			return;
		
		lastFire = System.currentTimeMillis();
		ShotEntity shot = new ShotEntity( game, (int)x+collisionWidth/2 - 2, (int)y  );
		game.getEntityManager().addToEntities(shot);
	}
	
	public ParticleEmitter getParticleEmitter(){
		return particleEmitter;
	}

	@Override
	public void draw(Graphics2D g) {
		particleEmitter.drawParticles(g);
		g.drawImage(ImageManager.getImage("mainShip"), (int)x, (int)y, null);
		
	}
}

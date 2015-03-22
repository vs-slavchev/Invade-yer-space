package entities;

import java.awt.Graphics2D;

import entities.aliens.AlienEntity;
import main.Game;
import utility.image.Animation;
import utility.image.ImageManager;
import utility.InputController;
import utility.image.ParticleEmitter;

public class ShipEntity extends Entity{

	private long lastFire = 0;
	private long firingInterval = 200;
	private double moveSpeed = 350;
	private Animation animation;
	private ParticleEmitter particleEmitter = new ParticleEmitter(40, 6);
	
	//private int overheat = 0; // max is always 100, used to draw rect indicator
	
	public ShipEntity(Game game, int x, int y) {
		super( x, y);
		this.game = game;
		animation = new Animation( x, y, 0.5, 6, "mainShip", true, false, 1);
		this.collisionWidth = animation.getDimensionX();
		this.collisionHeight = animation.getDimensionY();
	}
	
	public void move(long delta){
		particleEmitter.emittParticle((int)x + animation.getDimensionX()/2, (int)y + animation.getDimensionY());
		animation.update();
		animation.setPosition((int)x, (int)y);
		
		if( dx < 0 && x < 10 ){
			return;
		}
		if( dx > 0 && x > Game.getGameWidth() - 50 ){
			return;
		}
		if( dy > 0 && y > Game.getGameHeight() - collisionHeight ){
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
		animation.setRunning(true);
		ShotEntity shot = new ShotEntity( game, (int)x+collisionWidth/2 - 2, (int)y  );
		game.getEntityManager().addToEntities(shot);
	}
	
	public ParticleEmitter getParticleEmitter(){
		return particleEmitter;
	}

	@Override
	public void draw(Graphics2D g) {
		particleEmitter.drawParticles(g);
		animation.drawAnimation(g);
		
	}
}

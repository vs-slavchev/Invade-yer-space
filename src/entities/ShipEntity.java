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
	private ParticleEmitter particleEmitter = new ParticleEmitter(20, 6);
	
	//private int overheat = 0; // max is always 100, used to draw rect indicator
	
	public ShipEntity(Game game, int x, int y) {
		super( x, y);
		this.game = game;
		this.collisionWidth = ImageManager.getImage("mainShip").getWidth(null);
		this.collisionHeight = ImageManager.getImage("mainShip").getHeight(null);
	}
	
	public void move(long delta){
		particleEmitter.emittParticle((int)x + 20, (int)y + 25);
		
		if( this.dx < 0 && this.x < 10 ){
			return;
		}
		if( this.dx > 0 && this.x > this.game.getGameWidth() - 50 ){
			return;
		}
		if( this.dy > 0 && this.y > this.game.getGameHeight() - 30 ){
			return;
		}
		if( this.dy < 0 && this.y < 10 ){
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
		
		this.dx = 0;
		this.dy = 0;
		if( inputController.isLeftPressed() )
			this.dx = -this.moveSpeed;
		else if( inputController.isRightPressed() )
			this.dx =  this.moveSpeed;
		if( inputController.isUpPressed() )
			this.dy = -this.moveSpeed/2;
		else if( inputController.isDownPressed() )
			this.dy = this.moveSpeed/2;
		if( inputController.isFirePressed() )
			tryToFire();
	}
	
	public void tryToFire(){
		if( System.currentTimeMillis() - this.lastFire < this.firingInterval)
			return;
		
		this.lastFire = System.currentTimeMillis();
		ShotEntity shot = new ShotEntity( this.game, "sprites/shot.gif", (int)this.x+collisionWidth/2 - 2, (int)this.y  );
		this.game.getEntityManager().addToEntities(shot);
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

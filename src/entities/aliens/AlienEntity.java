package entities.aliens;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import entities.Entity;
import main.Game;
import utility.image.Animation;
import utility.image.AnimationManager;
import utility.image.ImageManager;
import utility.sound.SoundManager;

public class AlienEntity extends Entity{
	
	private int type;
	private int healthPoints = 1;
	private long shootTimeInterval = 200;
	private long lastShotTime = 60;
	private Random random = new Random();
	private final String spriteName;
	private Animation animation;

	public AlienEntity(Game game, int type, int x, int y, int healthPoints) {
		super(game, x, y);
		this.game = game;
		this.type = type;
		this.spriteName = "aliens/alienShip" + type;
		this.healthPoints = healthPoints;
		this.animation = new Animation( x, y, 0.07, 5, spriteName, true, false, 1);
		this.collisionWidth = animation.getDimensionX();
		this.collisionHeight = animation.getDimensionY();
		this.dx = -150;
		this.shootTimeInterval = getrandom( game.getDifficulty()*80, game.getDifficulty() * 450);
	}

	public void move(long delta){
		//if an alien reaches the end of the screen request direction change
		if( dx < 0 && x < 10 )
			game.updateLogic();
		if( dx > 0 && this.x > Game.getGameWidth() - 50 ){
			game.updateLogic();
		}
		animation.update();
		animation.setPosition((int)x, (int)y);
		super.move(delta);
		tryToShoot();
	}
	
	public void doLogic(){
		dx = -dx; //swap direction
		y += 10; //move lower
		
		//if aliens reach the player then gg
		if(y > Game.getGameHeight() - 30 ){
			game.notifyDeath();
		}
	}
	
	/* if alien can shoot transition to firing animation and
	 *  after it ends transition to default still animation
	 */
	public void tryToShoot(){
		lastShotTime++;
		if(lastShotTime >= shootTimeInterval){
			lastShotTime = 0;
			//set the animation to start running the shoot animation
			animation.setRunning(true);
			Entity alienShot = new AlienShotEntity( game,
					(int)(x + collisionWidth/2), (int)(y + 5), type );
			game.getEntityManager().addToAlienEntities(alienShot);
		}
	}
	
	public void reduceShootTimeInterval(){
		shootTimeInterval *= 0.98;
	}
	
	public int getrandom( int min, int max ){
		return min + random.nextInt( max - min );
	}
	
	public void draw(Graphics2D g){
		animation.drawAnimation(g);
	}
	
	public void takeDamage(){
		SoundManager.getSoundManager().play("explosion");
		int sparksX = (int) (x + 10 + Math.random()*animation.getDimensionX()/2);
		int sparksY = (int) (y + 10 + Math.random()*animation.getDimensionY()/2);
		AnimationManager.getAnimationManager().spawnAnimation("effects/sparks", sparksX, sparksY, 1);
		healthPoints--;
	}
	
	public boolean isDead(){
		if( healthPoints <= 0 )
			return true;
		return false;
	}
	
	public void spawnExplosionAnimation(){
		AnimationManager.getAnimationManager().spawnAnimation("effects/explosion", (int)x, (int)y, 1);
	}
	
	public void setXY(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Animation getAnimation(){
		return animation;
	}
	
	public void collidedWith(Entity other){
		// collisions with aliens are handled in ship and shot classes
	}
	
}

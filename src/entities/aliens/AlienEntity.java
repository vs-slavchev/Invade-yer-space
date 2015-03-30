package entities.aliens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import entities.Entity;
import main.Game;
import utility.ContentValues;
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
		this.shootTimeInterval = getrandom(ContentValues.MIN_ATTACK_INTERVAL, ContentValues.MAX_ATTACK_INTERVAL);
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
			if (Math.abs(game.getEntityManager().getShip().getX() - x) < ContentValues.X_SHOOT_RANGE) {
				// set the animation to start running the shoot animation
				animation.setRunning(true);
				Entity alienShot = new AlienShotEntity(game,
						(int) (x + collisionWidth / 2), (int) (y + 5), type);
				game.getEntityManager().addToAlienEntities(alienShot);
				AnimationManager.getAnimationManager().spawnAnimation(
						"effects/muzzleFlash",
						(int) x - 8 + animation.getDimensionX(),
						(int) y - 8 + animation.getDimensionY(), 1);
			}
		}
	}
	
	public void reduceShootTimeInterval(){
		shootTimeInterval *= 0.99;
	}
	
	public int getrandom( int min, int max ){
		return min + random.nextInt( max - min );
	}
	
	public void draw(Graphics2D g){
		animation.drawAnimation(g);
		if (game.isAlienHPBarDrawn()) {
			g.setColor(Color.red);
			g.fillRect((int) x, (int) y - 7, animation.getDimensionX(), 7);
			g.setColor(Color.green);
			int fillerBarWidth = (int) (((double) healthPoints / (double) (type * ContentValues.ENEMY_HP_PER_LVL_MULTIPLIER)) * (animation
					.getDimensionX() - 2));
			g.fillRect((int) x + 1, (int) y - 6, fillerBarWidth, 5);
		}
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
		if (Math.random() < ContentValues.CHANCE_TO_EXPLODE_ALIEN){
			game.getEntityManager().createAoEObject((int)x - ContentValues.ROCKET_EXPLOSION_RADIUS,
					(int)y - ContentValues.ROCKET_EXPLOSION_RADIUS,
					ContentValues.ROCKET_EXPLOSION_RADIUS*2, ContentValues.ROCKET_EXPLOSION_RADIUS*2);
			// the scale is = explosion radius/image raduis
			AnimationManager.getAnimationManager().spawnAnimation("effects/explosion",
					(int)x - ContentValues.ROCKET_EXPLOSION_RADIUS,
					(int)y - ContentValues.ROCKET_EXPLOSION_RADIUS, ContentValues.ROCKET_EXPLOSION_RADIUS/(ImageManager.getImage("effects/explosion1").getHeight(null)/2));
		} else {
			AnimationManager.getAnimationManager().spawnAnimation(
					"effects/explosion", (int) x, (int) y, 1);
		}
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

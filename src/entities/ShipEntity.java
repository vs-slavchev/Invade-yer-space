package entities;

import java.awt.Color;
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
	private PlayerWeapon[] weapons = new PlayerWeapon[4];
	private int currentWeapon = 1;
	
	public ShipEntity(Game game, int x, int y) {
		super(x, y);
		this.game = game;
		animation = new Animation( x, y, 0.5, 6, "mainShip", true, false, 1);
		this.collisionWidth = animation.getDimensionX();
		this.collisionHeight = animation.getDimensionY();
		for ( int i = 0; i < weapons.length; i++){
			weapons[i] = new PlayerWeapon(game, "projectiles/shot" + (i+1));
		}
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
		
		for(int i = 0; i < weapons.length; i++){
			weapons[i].coolDown();
		}
	}
	
	public void collidedWith(Entity other){
		if( other instanceof AlienEntity){
			this.game.notifyDeath();
		}
	}
	
	public void processInput(InputController inputController){
		
		dx = 0;
		dy = 0;
		if( inputController.isLeftPressed() ){
			dx = -this.moveSpeed;
		}
		else if( inputController.isRightPressed() ){
			dx =  this.moveSpeed;
		}
		if( inputController.isUpPressed() ){
			dy = -this.moveSpeed/2;
		}
		else if( inputController.isDownPressed() ){
			dy = this.moveSpeed/2;
		}
		if( inputController.isFirePressed() ){
			weapons[currentWeapon].tryToFire();
		}
		if (inputController.isOnePressed()){
			currentWeapon = 0;
			weapons[currentWeapon].resetFireTimer();
		}else if (inputController.isTwoPressed()){
			currentWeapon = 1;
			weapons[currentWeapon].resetFireTimer();
		}else if (inputController.isThreePressed()){
			currentWeapon = 2;
			weapons[currentWeapon].resetFireTimer();
		}else if (inputController.isFourPressed()){
			currentWeapon = 3;
			weapons[currentWeapon].resetFireTimer();
		}
	}
	
	public ParticleEmitter getParticleEmitter(){
		return particleEmitter;
	}

	@Override
	public void draw(Graphics2D g) {
		particleEmitter.drawParticles(g);
		animation.drawAnimation(g);
		drawWeaponUI(g);
	}

	private void drawWeaponUI(Graphics2D g) {
		int baseX = 20;
		if (x < 180){
			baseX = game.getWidth() - 150;
		}
		for (int i = 0; i < weapons.length; i++){
			
			int greenComponent = (int)(100 - weapons[i].getOverheatPercent())*255/100;
			g.setColor(new Color(255, greenComponent, 0));
			g.fillRect(baseX + i*35, (int)(game.getHeight() - 120 + 100 - weapons[i].getOverheatPercent()), 30, (int) weapons[i].getOverheatPercent());
			
			// draw an exclamation mark over heatbar
			if (weapons[i].getOverheatPercent() > 50) {
				g.fillRect(baseX + i * 35 + 12,
						game.getHeight() - 120 - 40, 6, 20);
				g.fillRect(baseX + i * 35 + 12,
						game.getHeight() - 120 - 15, 6, 5);
			}
		}
	}
	
	public Animation getAnimation(){
		return animation;
	}
}

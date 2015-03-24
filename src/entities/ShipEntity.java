package entities;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.CopyOnWriteArrayList;

import entities.aliens.AlienEntity;
import entities.utility.PlayerWeapon;
import entities.utility.StatusEffect;
import main.Game;
import utility.image.Animation;
import utility.image.ImageManager;
import utility.ComboManager;
import utility.ContentValues;
import utility.InputController;
import utility.image.ParticleEmitter;

public class ShipEntity extends Entity{

	private double moveSpeed = 350;
	private Animation animation;
	private ParticleEmitter particleEmitter = new ParticleEmitter(40, 6);
	private ComboManager comboManager;
	private CopyOnWriteArrayList<StatusEffect> buffs = new CopyOnWriteArrayList<>();
	private PlayerWeapon[] weapons = new PlayerWeapon[6];
	private final int numberOfNormalWeapons = 4;
	private int currentWeapon = 0;
	private boolean invulnerable = false;
	
	public ShipEntity(Game game, int x, int y) {
		super(x, y);
		this.game = game;
		comboManager = new ComboManager(game);
		animation = new Animation( x, y, 0.5, 6, "mainShip", true, false, 1);
		this.collisionWidth = animation.getDimensionX();
		this.collisionHeight = animation.getDimensionY();
		for ( int i = 0; i < weapons.length; i++){
			weapons[i] = new PlayerWeapon(game, "projectiles/shot" + (i+1));
		}
		//buffs.add(new StatusEffect("shield"));
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
		
		comboManager.updateRecentKillCount();
		StatusEffect newBuff = comboManager.giveBonusStatusEffect();
		if (newBuff != null){
			buffs.add(newBuff);
		}
		for (StatusEffect statusEffect : buffs) {
			statusEffect.update();
			if (!statusEffect.isActive()){
				buffs.remove(statusEffect);
			}
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
		
		invulnerable = false;
		if (!buffs.isEmpty()) {
			for (StatusEffect statusEffect : buffs) {
				if (statusEffect.getName().equals("scatter")) {
					weapons[5].tryToFire();
				} else if (statusEffect.getName().equals("spears")) {
					weapons[4].tryToFire();
				}
				else if (statusEffect.getName().equals("shield")) {
					invulnerable = true;
				}
			}
		}
	}
	
	public ParticleEmitter getParticleEmitter(){
		return particleEmitter;
	}

	@Override
	public void draw(Graphics2D g) {
		particleEmitter.drawParticles(g);
		animation.drawAnimation(g);
		
		//draw shield
		if (invulnerable) {
			
			float opacity = 1.0f;
			if (!buffs.isEmpty()) {
				for (StatusEffect statusEffect : buffs) {
					 if (statusEffect.getName().equals("shield")) {
						 // 1/4 + 3/4 = 1; 3/4 = maxDuration/MAX_PLAYER_SHIELD_DURATION*1.34
						opacity = (float) (0.25 + statusEffect.getDuration()/(float)(ContentValues.MAX_PLAYER_SHIELD_DURATION*1.34));
					}
				}
			}
			
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
			g.drawImage(ImageManager.getImage("effects/shield"),
					getCenteredX() - ImageManager.getImage("effects/shield").getWidth(null),
					(int) y - 10,
					ImageManager.getImage("effects/shield").getWidth(null)*2,
					ImageManager.getImage("effects/shield").getHeight(null)*2, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}
		
		drawWeaponUI(g);
		comboManager.drawComboUI(g);
	}

	private void drawWeaponUI(Graphics2D g) {
		int baseX = 20;
		if (x < 180){
			baseX = game.getWidth() - 150;
		}
		for (int i = 0; i < numberOfNormalWeapons; i++){
			
			int greenComponent = (int)(100 - weapons[i].getOverheatPercent())*255/100;
			g.setColor(new Color(255, greenComponent, 0));
			g.fillRect(baseX + i*35, (int)(game.getHeight() - 120 + 100 - weapons[i].getOverheatPercent()), 30, (int) weapons[i].getOverheatPercent());
			
			// draw an exclamation mark over heat bar
			if (weapons[i].getOverheatPercent() > 70) {
				g.fillRect(baseX + i * 35 + 12,
						game.getHeight() - 120 - 40, 6, 20);
				g.fillRect(baseX + i * 35 + 12,
						game.getHeight() - 120 - 15, 6, 5);
			}
		}
	}
	
	public int getCenteredX(){
		return (int) (x + animation.getDimensionX()/2);
	}
	
	public ComboManager getComboManager(){
		return comboManager;
	}
	
	public Animation getAnimation(){
		return animation;
	}
	
	public boolean isInvulnerable(){
		return invulnerable;
	}
}

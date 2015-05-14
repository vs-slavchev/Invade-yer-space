package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.CopyOnWriteArrayList;

import entities.aliens.AlienEntity;
import entities.utility.PlayerWeapon;
import entities.utility.StatusEffect;
import main.Game;
import utility.image.Animation;
import utility.image.AnimationManager;
import utility.image.ImageManager;
import utility.ComboManager;
import utility.ContentValues;
import utility.InputController;
import utility.image.ParticleEmitter;
import utility.sound.SoundManager;

public class ShipEntity extends Entity{

	private double moveSpeed = 300;
	private Animation animation;
	private ParticleEmitter particleEmitter = new ParticleEmitter(40, 6);
	private ComboManager comboManager;
	private CopyOnWriteArrayList<StatusEffect> buffs = new CopyOnWriteArrayList<>();
	private PlayerWeapon[] weapons = new PlayerWeapon[8];
	private int currentWeapon = 0;
	private boolean invulnerable = false;
	private boolean laserOn = false;
	private boolean autoFireOn = false;

	public ShipEntity(Game game, int x, int y) {
		super(x, y);
		this.game = game;
		comboManager = new ComboManager(game);
		animation = new Animation( x, y, 0.5, 6, "text/mainShip", true, false, 1);
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
		
		for(int i = 0; i < weapons.length; i++){
			weapons[i].coolDown();
		}
		
		receiveComboBonusStatusEffect();
		updateStatusEffects();
		
		if( dx < 0 && x < 10 ){
			return;
		}
		if( dx > 0 && x > Game.getGameWidth() - 50 ){
			return;
		}
		if( dy > 0 && y >= Game.getGameHeight() - collisionHeight ){
			return;
		}
		if( dy < 0 && y < 10 ){
			return;
		}
		super.move(delta);
	}

	private void updateStatusEffects() {
		for (StatusEffect statusEffect : buffs) {
			statusEffect.update();
			if (!statusEffect.isActive()){
				buffs.remove(statusEffect);
			}
		}
		
		invulnerable = false;
		laserOn = false;
		if (!buffs.isEmpty()) {
			for (StatusEffect statusEffect : buffs) {
				if (statusEffect.getName().equals("scatter")) {
					weapons[5].tryToFire();
				} else if (statusEffect.getName().equals("spears")) {
					weapons[4].tryToFire();
				}else if (statusEffect.getName().equals("quadRockets")) {
					weapons[6].tryToFire();
				} else if (statusEffect.getName().equals("shield")) {
					invulnerable = true;
				} else if (statusEffect.getName().equals("laser")) {
					int laserWidth = ImageManager.getImage("projectiles/laser").getWidth(null);
					game.getEntityManager().createAoEObject((int)x + animation.getDimensionX()/2 - laserWidth/2, 0, laserWidth, (int)y);
					laserOn = true;
				} else if (statusEffect.getName().equals("flakes")) {
					weapons[7].tryToFire();
				}
			}
		}
	}

	private void receiveComboBonusStatusEffect() {
		comboManager.updateRecentKillCount();
		StatusEffect newBuff = comboManager.giveBonusStatusEffect();
		if (newBuff != null){
			buffs.add(newBuff);
		}
	}
	
	public void collidedWith(Entity other){
		if( other instanceof AlienEntity){
			this.game.notifyDeath();
			autoFireOn = false;
		}
	}
	
	public void processInput(InputController inputController){
		
		if( inputController.isLeftPressed() ){
			if (dx > -moveSpeed){
				dx -= ContentValues.ACCELERATION;
			}
		}
		else if( inputController.isRightPressed() ){
			if (dx < moveSpeed){
				dx += ContentValues.ACCELERATION;
			}
		}else{ // apply horizontal friction
			if (dx > ContentValues.FRICTION){
				dx -= ContentValues.FRICTION;
			}else if (dx < -ContentValues.FRICTION){
				dx += ContentValues.FRICTION;
			}else{
				dx = 0;
			}
		}
		
		if( inputController.isUpPressed() ){
			if (dy > -moveSpeed*2/3){
				dy -= ContentValues.ACCELERATION;
			}
		}
		else if( inputController.isDownPressed() ){
			if (dy < moveSpeed*2/3){
				dy += ContentValues.ACCELERATION;
			}
		}else{ // apply vertical friction
			if (dy > ContentValues.FRICTION){
				dy -= ContentValues.FRICTION;
			}else if (dy < -ContentValues.FRICTION){
				dy += ContentValues.FRICTION;
			}else{
				dy = 0;
			}
		}
		
		
		if( inputController.isFirePressed() || autoFireOn ){
			if (weapons[currentWeapon].tryToFire()) {
				// if successfully shot then get knocked back as a result
				y += 2;
				if (y > Game.getGameHeight() - collisionHeight) {
					y = Game.getGameHeight() - collisionHeight;
				}
				AnimationManager.spawnAnimation("effects/muzzleFlash", (int)x + 8, (int)y - 10, 1);
			}
		}
		if( inputController.isAutoFirePressed()){
			autoFireOn = !autoFireOn;
			inputController.setAutoFirePressed(false);
		}
		if (inputController.isOnePressed()) {
			if (currentWeapon != 0) {
				currentWeapon = 0;
				weapons[currentWeapon].resetFireTimer();
				SoundManager.play("weaponSwitch");
			}
		} else if (inputController.isTwoPressed()) {
			if (currentWeapon != 1) {
				currentWeapon = 1;
				weapons[currentWeapon].resetFireTimer();
				SoundManager.play("weaponSwitch");
			}
		} else if (inputController.isThreePressed()) {
			if (currentWeapon != 2) {
				currentWeapon = 2;
				weapons[currentWeapon].resetFireTimer();
				SoundManager.play("weaponSwitch");
			}
		} else if (inputController.isFourPressed()) {
			if (currentWeapon != 3) {
				currentWeapon = 3;
				weapons[currentWeapon].resetFireTimer();
				SoundManager.play("weaponSwitch");
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
		
		drawLaser(g);
		drawShield(g);
		drawWeaponUI(g);
		comboManager.drawComboUI(g);
	}

	private void drawLaser(Graphics2D g) {
		if (laserOn){
			g.drawImage(ImageManager.getImage("projectiles/laser"),
					(int)x + animation.getDimensionX()/2 - ImageManager.getImage("projectiles/laser").getWidth(null)/2,
					(int) (y - ImageManager.getImage("projectiles/laser").getHeight(null)), null);
			g.drawImage(ImageManager.getImage("projectiles/laser"),
					(int)x + animation.getDimensionX()/2 - ImageManager.getImage("projectiles/laser").getWidth(null)/2,
					0,
					(int)x + animation.getDimensionX()/2 + ImageManager.getImage("projectiles/laser").getWidth(null)/2,
					(int) (y - ImageManager.getImage("projectiles/laser").getHeight(null)),
					0, 0,
					ImageManager.getImage("projectiles/laser").getWidth(null), 2, null);
		}
	}

	private void drawShield(Graphics2D g) {
		if (invulnerable) {
			int angle = 0;
			if (!buffs.isEmpty()) {
				for (StatusEffect statusEffect : buffs) {
					 if (statusEffect.getName().equals("shield")) {
						angle = (int) (((double)statusEffect.getDuration()/(double)(ContentValues.MAX_PLAYER_SHIELD_DURATION)) * (double)360);
						//angle -= ContentValues.MAX_PLAYER_SHIELD_DURATION;
					}
				}
			}

			int arcOrigin = 90 - angle/2;
			g.setColor(Color.CYAN);
			g.drawArc((int)x - 7, (int)y - 10, 46, 76, arcOrigin, angle);
			g.drawArc((int)x - 3, (int)y - 6, 38, 68, arcOrigin, angle);
			g.drawArc((int)x + 1, (int)y - 2, 30, 60, arcOrigin, angle);
		}
	}

	private void drawWeaponUI(Graphics2D g) {
		int baseX = ContentValues.WEAPON_UI_BASE_X;
		int baseY = game.getHeight() - 130;
		
		// if the player ship is to the far left, draw the UI on the right
		if (x < 180){
			baseX = game.getWidth() - 150;
		}
		
		for (int i = 0; i < ContentValues.NUM_NORMAL_WEAPONS; i++){
			
			int greenComponent = (int)(100 - weapons[i].getOverheatPercent())*255/100;
			g.setColor(new Color(255, greenComponent, 0));
			g.fillRect(baseX + i*35, (int)(baseY + 100 - weapons[i].getOverheatPercent()), 30, (int) weapons[i].getOverheatPercent());
			
			// draw an exclamation mark over heat bar
			if (weapons[i].getOverheatPercent() > 70) {
				g.fillRect(baseX + i * 35 + 12,
						baseY - 40, 6, 20);
				g.fillRect(baseX + i * 35 + 12,
						baseY - 15, 6, 5);
			}
			String iconName = "projectiles/shot" + (i+1);
			int iconX = baseX + i*35 + 15 - ImageManager.getImage(iconName).getWidth(null)/2;
			g.drawImage(ImageManager.getImage(iconName), iconX, game.getHeight() - 30 + (ContentValues.NUM_NORMAL_WEAPONS-i)*3, null);
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
	
	public boolean isAutoFireOn() {
		return autoFireOn;
	}

	public void setAutoFireOn(boolean autoFireOn) {
		this.autoFireOn = autoFireOn;
	}
}

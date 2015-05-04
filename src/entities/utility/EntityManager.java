package entities.utility;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;

import utility.ContentValues;
import entities.Entity;
import entities.ShipEntity;
import entities.aliens.AlienEntity;
import main.Game;

public class EntityManager {
	
	private Game game;
	private CopyOnWriteArrayList<Entity> alienEntities = new CopyOnWriteArrayList<>(); // aliens and alienShots
	private ArrayList<Entity> entities = new ArrayList<>(); // ship and playerProjectiles
	private HashSet<Entity> removeList = new HashSet<>(); // set of entities to be removed
	// list manages the AoE objects: explosions and lasers
	// that affect more than 1 entity at the same time
	private ArrayList<Rectangle> aoeObjects = new ArrayList<>();
	private Entity ship;
	private int alienCount = 0;
	private final int maxAlienShipType = 8;
	private final int maxRows = 8;
	private final int maxColumns = 20; // MUST be an even number
	private String entitiesLevelMap;
	private int typesArray[] = new int[maxRows*maxColumns/2];
	private double levelDifficulty = 1.0;
	private final double levelDifficultyModifier = 0.3;
	
	public EntityManager(Game game){
		this.game = game;
		generateNewEntitiesMap();
		if (entitiesLevelMap.length() != this.maxColumns*this.maxRows){
			System.out.println("entitiesMap is not the exact size!");
		}
	}
	
	/* generates a symmetric map of entities that is random
	 * but also near the difficulty of the level */
	public void generateNewEntitiesMap(){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < maxRows; i++){
			StringBuilder innerSb = new StringBuilder();
			for (int j = 0; j < maxColumns/2; j++){
				int currentType = 1 + (int)(Math.random()*(maxAlienShipType-1));
				currentType += balanceAccordingToMean(i*maxColumns/2 + j);
				currentType = controllTypeBoundaries(currentType);
				typesArray[i*maxColumns/2 + j] = currentType;
				innerSb.append(currentType);
			}
			sb.append(innerSb);
			sb.append(innerSb.reverse());
		}
		entitiesLevelMap  = sb.toString();
		//printEntitiesMapInfo();
		levelDifficulty += levelDifficultyModifier;
	}
	
	/* method balances the alien type according to the mean
	 * of so far created alien types. Returned int is to be used
	 * to modify the currentType of the newly created alien
	 */
	private int balanceAccordingToMean(int size) {
		return (int) (levelDifficulty - calculateMean(size));
	}
	
	private double calculateMean(int size) {
		int sum = 0;
		for (int i = 0; i < size; i++){
			sum += typesArray[i];
		}
		if (size == 0){
			return 0;
		}
		return sum/size;
	}
	
	private int controllTypeBoundaries(int inType){
		int type = inType;
		if( type < 1){
			type = 1;
		}else if (type > maxAlienShipType){
			type = maxAlienShipType;
		}
		return type;
	}

	
	public void moveEntities(long delta){
		for( int i = 0; i < alienEntities.size(); i++ ){
			Entity entity = alienEntities.get(i);
			entity.move(delta);
		}
		for( int i = 0; i < entities.size(); i++ ){
			Entity entity = entities.get(i);
			entity.move(delta);
		}
	}
	
	public synchronized void drawEntities(Graphics2D g){
		for( int i = 0; i < alienEntities.size(); i++){
			Entity entity = alienEntities.get(i);
			if (!(entity == null)) {
				entity.draw(g);
			}
		}
		for( int i = 0; i < entities.size(); i++){
			Entity entity = entities.get(i);
			entity.draw(g);
		}
	}
	
	public void collideEntities(){
		for( int i = 0; i < entities.size(); i++){
			for(int j = 0; j < alienEntities.size(); j++){
				
				Entity first = entities.get(i);
				Entity second = alienEntities.get(j);

				if( first.checkCollisionWith(second)){
					first.collidedWith(second);
					second.collidedWith(first);
				}
			}
		}
		
		// collide alienEntities with aoeObjects
		if (!aoeObjects.isEmpty()) {
			for (int i = 0; i < alienEntities.size(); i++) {
				for (int j = 0; j < aoeObjects.size(); j++) {
					Entity alien = alienEntities.get(i);
					if (alien.getCollisionRectangle().intersects(aoeObjects.get(j))){
						removeList.add(alienEntities.get(i));
					}
				}
			}
		}
		// clear the list after the AoE collision checks have been performed
		aoeObjects.clear();
	}
	
	public void removeEntities(){		
		entities.removeAll(this.removeList);
		
		for (Entity entity : removeList) {
			if (entity instanceof AlienEntity){
				game.notifyAlienKilled();
			}
		}
		alienEntities.removeAll(this.removeList);
		removeList.clear();
	}
	
	public void forceLogic(){
		for( int i = 0; i < this.alienEntities.size(); i++){
			Entity entity = this.alienEntities.get(i);
			entity.doLogic();
		}
	}
	
	public void initEntities(){
		//clear the ArrayLists before using them
		entities.clear();
		alienEntities.clear();
		// after clearing up the big collections request a GC; changing levels happens rarely and minor lag is unnoticed
		System.gc();
		
		ship = new ShipEntity( this.game, Game.getGameWidth()/2, Game.getGameHeight()*5/6);
		entities.add(this.ship);
		
		//create a block of aliens by getting a char id of the alien from the entitiesLevelMap Array of Strings
		alienCount = 0;
		int type = 0;
		for( int row = 0; row < maxRows; row++){
			for( int col = 0; col < maxColumns; col++){
				type = Character.getNumericValue( entitiesLevelMap.charAt( col + row*maxColumns ) );
				Entity alien = produceAlien( type, row, col);
				if( type != 0 && alien != null){
					alienEntities.add(alien);
					alienCount++;
				}
			}
		}
	}
	
	private AlienEntity produceAlien( int type, int row, int col){
		int healthPoints = type * ContentValues.ENEMY_HP_PER_LVL_MULTIPLIER;
		
		/* aliens are centered according to their image; aliens are created first,
		 * so their image is not null, and then according to the image dimensions
		 * their position is set correctly */
		// spawn off the screen
		AlienEntity alien = new AlienEntity( game, type, -100, -100, healthPoints);
		int offsetX = alien.getAnimation().getDimensionX()/2;
		int offsetY = alien.getAnimation().getDimensionY()/2;
		// set to the correct position, after having access to the animation
		alien.setXY(100+(col*Game.getGameWidth()/30) - offsetX, 20+(row*Game.getGameHeight()/20) - offsetY);
		return alien;
	}
	
	public void addToAlienEntities(Entity entity){
		alienEntities.add(entity);
	}
	
	public void addToEntities(Entity entity){
		entities.add(entity);
	}
	
	public void speedUpAlienEntities(){
		for( int i = 0; i < alienEntities.size(); i++){
			Entity entity = alienEntities.get(i);
			if( entity instanceof AlienEntity){
				entity.setHorizontalMovement( entity.getHorizontalMovement() * 1.013 );
				((AlienEntity) entity).reduceShootTimeInterval();
			}
		}
	}
	
	public void createAoEObject(int x, int y, int width, int height){
		aoeObjects.add(new Rectangle(x, y, width, height));
	}
	
	// 2 methods for debugging
	public void printEntitiesMapInfo(){
		for( int i = 1; i < 9; i++){
			occurencesOfNumber(Integer.toString(i));
		}
	}

	private void occurencesOfNumber(String ch) {
		int occursOfOne = 0;
		int index = entitiesLevelMap.indexOf(ch);
		while ( index != -1){
			occursOfOne++;
			index = entitiesLevelMap.indexOf(ch, index+1);
		}
		System.out.println( ch + "s: " + occursOfOne);
	}
	
	public void removeEntity(Entity entity){
		removeList.add(entity);
	}
	
	public void decrementAlienCount(){
		alienCount--;
	}
	
	public int getAlienCount(){
		return alienCount;
	}
	
	public Entity getShip(){
		return ship;
	}
	
	public boolean existAliens(){
		if (alienEntities.isEmpty()){
			return false;
		}
		return true;
	}
	

}


package utility.image;

import java.awt.Graphics2D;
import java.util.concurrent.CopyOnWriteArrayList;

import utility.ContentValues;
import main.Game;

public class BackgroundImageManager {
	private CopyOnWriteArrayList<Animation> backgroundObjects = new CopyOnWriteArrayList<>();
	private int maxObjects;
	private int numberOfDifferentAnimations;
	private int yVel;
	private double chanceToSpawn;
	private String imageName;
	
	public BackgroundImageManager(int maxObjects, int numAnims, double chanceToSpawn, String imageName, int yVel){
		this.maxObjects = maxObjects;
		numberOfDifferentAnimations = numAnims;
		this.chanceToSpawn = chanceToSpawn;
		this.imageName = imageName;
		this.yVel = yVel;
	}
	
	public void update(){
		for (Animation animation : backgroundObjects) {
			// move animation lower: x+0,y+1
			animation.modifyPosition(0, yVel);
			if( animation.getY() > Game.HEIGHT){
				animation = generateBackgroundObject();
			}
		}
		generateNewObjects(maxObjects);
	}

	protected void generateNewObjects(int max) {
		if (backgroundObjects.size() < max){
			Animation newBackgroundObj = generateBackgroundObject();
			if (!(newBackgroundObj == null)) {
				backgroundObjects.add(newBackgroundObj);
			}
		}
	}
	
	private Animation generateBackgroundObject(){
		int minY = Game.HEIGHT;
		if (!backgroundObjects.isEmpty()){
			for (Animation animation : backgroundObjects) {
				if( animation.getY() < minY){
					minY = animation.getY();
				}
			}
		}
		if (Math.random() < chanceToSpawn && minY > 0) {
			int x = (int) (-30 + Math.random() * Game.WIDTH);
			int y = -200;
			String name = imageName + (int) (1 + Math.random() * (numberOfDifferentAnimations - 1));
			double scale = 1 + Math.random();
			return new Animation(x, y, 0, 1, name, false, false, scale);
		}
		return null;
	}
	
	public void drawBackgroundObjects(Graphics2D g){
		for (Animation animation : backgroundObjects) {
			animation.drawAnimation(g);
		}
	}

}

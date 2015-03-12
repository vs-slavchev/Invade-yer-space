package utility.image;

import java.awt.Graphics2D;
import java.util.concurrent.CopyOnWriteArrayList;

import main.Game;

public class BackgroundImageManager {
	private static CopyOnWriteArrayList<Animation> backgroundObjects = new CopyOnWriteArrayList<>();
	private final static int maxObjects = 5;
	
	public static void update(){
		for (Animation animation : backgroundObjects) {
			// move animation lower: x+0,y+1
			animation.modifyPosition(0, 1);
			if( animation.getY() > Game.HEIGHT){
				backgroundObjects.remove(animation);
			}
		}
		generateBackgroundObject();
	}
	
	public static void generateBackgroundObject(){
		if (backgroundObjects.size() >= maxObjects){
			return;
		}
		if (Math.random() < 0.002) {
			int x = (int) (-30 + Math.random() * Game.WIDTH);
			int y = -100;
			String name = "planet" + (int) (1 + Math.random() * 6);
			double scale = 1 + Math.random();
			backgroundObjects.add(new Animation(x, y, 0, 1, name, false, scale));
		}
	}
	
	public static void drawBackgroundObjects(Graphics2D g){
		for (Animation animation : backgroundObjects) {
			animation.drawAnimation(g);
		}
	}
	
	

}

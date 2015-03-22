package utility.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import main.Game;

public class BackgroundImageManager {
	private static CopyOnWriteArrayList<Animation> backgroundObjects = new CopyOnWriteArrayList<>();
	private static ArrayList<Particle> backgroundStars = new ArrayList<>();
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
		if (Math.random() < 0.0005) {
			int x = (int) (-30 + Math.random() * Game.WIDTH);
			int y = -250;
			String name = "planets/planet" + (int) (1 + Math.random() * 13);
			double scale = 1 + Math.random();
			backgroundObjects.add(new Animation(x, y, 0, 1, name, false, false, scale));
		}
	}
	
	public static void drawBackgroundObjects(Graphics2D g){
		for (Particle particle : backgroundStars) {
			particle.drawParticle(g);
		}
		for (Animation animation : backgroundObjects) {
			animation.drawAnimation(g);
		}
	}
	
	public static void generateBackgroundStars(int numberStars){
		for ( int i = 0; i < numberStars; i++){
			int x = (int) (Math.random()*Game.getGameWidth());
			int y = (int) (Math.random()*Game.getGameHeight());
			backgroundStars.add(new Particle(x, y, 0, new Color(128,128,128), 0));
		}
	}
	
	

}

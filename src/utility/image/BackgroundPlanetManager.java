package utility.image;

import java.awt.Graphics2D;
import java.util.concurrent.CopyOnWriteArrayList;

import main.Game;

/*Class manages the background planets. */
public class BackgroundPlanetManager{
	
	private BackgroundStarsManager starsManager = new BackgroundStarsManager();
	private CopyOnWriteArrayList<Animation> backgroundObjects = new CopyOnWriteArrayList<>();
	private int numberOfDifferentAnimations;
	private int yVel;
	private double chanceToSpawn;
	// required to prevent overlapping of images
	private int spacing;
	private String imageName;
	
	public BackgroundPlanetManager(int numAnims, double chanceToSpawn, String imageName, int yVel, int spacing) {
		this.numberOfDifferentAnimations = numAnims;
		this.chanceToSpawn = chanceToSpawn;
		this.imageName = imageName;
		this.yVel = yVel;
		this.spacing = spacing;
		starsManager.generateBackgroundStars();
	}
	
	public void update(){
		for (Animation animation : backgroundObjects) {
			// move animation lower: x+0,y+yVel
			animation.modifyPosition(0, yVel);
			// if object is off the screen, replace it with a new one
			if( animation.getY() > Game.SCREEN_HEIGHT){
				animation = generateBackgroundObject();
			}
		}
		generateNewObjects();
	}

	protected void generateNewObjects() {
		Animation newBackgroundObj = generateBackgroundObject();
		if (newBackgroundObj != null) {
			backgroundObjects.add(newBackgroundObj);
		}
	}
	
	/* Called in the method "generateNewObjects()".
	 * Creates 1 concrete background object.
	 */
	private Animation generateBackgroundObject(){
		int minY = Game.SCREEN_HEIGHT;
		if (!backgroundObjects.isEmpty()){
			for (Animation animation : backgroundObjects) {
				if( animation.getY() < minY){
					minY = animation.getY();
				}
			}
		}
		if (Math.random() < chanceToSpawn && minY > 0) {
			int x = (int) (-30 + Math.random() * Game.SCREEN_WIDTH);
			String name = imageName + (int) (1 + Math.random() * (numberOfDifferentAnimations - 1));
			double scale = 1 + Math.random();
			return new Animation(x, spacing, 0, 1, name, false, false, scale);
		}
		return null;
	}

	public void drawBackgroundObjects(Graphics2D g){
		// stars are drawn first; they are farther away
		starsManager.drawStars(g);
		for (Animation animation : backgroundObjects) {
			animation.drawAnimation(g);
		}
	}

}

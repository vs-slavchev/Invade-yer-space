package utility.image;

import java.awt.Graphics2D;
import utility.ContentValues;

/*Class manages the background planets*/

public class BackgroundPlanetManager extends BackgroundImageManager{
	
	private BackgroundStarsManager starsManager = new BackgroundStarsManager();
	
	public BackgroundPlanetManager() {
		super(ContentValues.MAX_BACKGROUND_PLANETS, ContentValues.NUMBER_DIFFERENT_PLANETS,
				ContentValues.PLANET_SPAWN_CHANCE, "planets/planet", ContentValues.PLANET_Y_VEL);
		starsManager.generateBackgroundStars();
	}

	public void drawBackgroundObjects(Graphics2D g){
		// stars are drawn first; they are farther away
		starsManager.drawStars(g);
		super.drawBackgroundObjects(g);
	}

}

package utility;

import java.awt.Color;
import java.awt.Font;

public class ContentValues {
	// player related:
	public static final int MAX_PLAYER_SHIELD_DURATION = 1_000;
	public static final int[] BULLETS_VELOCITY = {-700, -500, -600, -250, -550, -500, -200};
	public static final int ACCELERATION = 25;
	public static final int FRICTION = 12;
	public static final int WEAPON_UI_BASE_X = 20;
	public static final int NUM_NORMAL_WEAPONS = 4;
	
	// enemy related:
	public static final int ENEMY_HP_PER_LVL_MULTIPLIER = 2;
	public static final double CHANCE_TO_EXPLODE_ALIEN = 0.1;
	public static final int X_SHOOT_RANGE = 300;
	public static final int MIN_ATTACK_INTERVAL = 800; // 250-1500
	public static final int MAX_ATTACK_INTERVAL = 1800;
	public static final int BASE_BULLET_VELOCITY = 350;
	
	// effect related:
	public static final int ROCKET_EXPLOSION_RADIUS = 80;
	
	// sound and music
	public static final float MUSIC_PER_TICK_MODIFIER = 0.007f;
	
	// used colors
	public static final Color COLOR_TEXT_BOX_FILL_TUTORIAL = new Color(80, 80, 255);
	public static final Color COLOR_TEXT_BOX_FILL_SONG = new Color(66, 105, 140);
	
	// user interface
	public static final Font INFO_HEADING_FONT = new Font("Dialog", Font.ITALIC, 50);
	public static final Font INFO_TEXT_FONT = new Font("Dialog", Font.BOLD, 40);
	
	//visual effects
	public static final int MAX_BACKGROUND_PLANETS = 7;
	public static final int MAX_BACKGROUND_GEMS = 50;
	public static final int NUM_BACKGROUND_STARS = 400;
	public static final double PLANET_SPAWN_CHANCE = 0.0005;
	public static final double GEM_SPAWN_CHANCE = 0.5;
	public static final int NUMBER_DIFFERENT_PLANETS = 14;
	public static final int NUMBER_DIFFERENT_GEMS = 8;
	public static final int PLANET_Y_VEL = 1;
	public static final int GEM_Y_VEL = 4;
	
	
	// utility method for values
	public static int controlMinMax(final int i, final int min, final int max){
		if (min >= max){
			// min is more than max!
			return i;
		}
		int result = i;
		if(result < min){
			result = min; 
		}else if(result > max){
			result = max;
		}
		return result;
	}
}

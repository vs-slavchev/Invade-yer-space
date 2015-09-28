package utility;

import java.awt.Color;
import java.awt.Font;

public class ContentValues {
	// player related:
	public static final int MAX_PLAYER_SHIELD_DURATION = 800;
	public static final int MAX_PLAYER_LASER_DURATION = 170;
	public static final int KILL_TIME_WINDOW = 7_000;
	public static final int[] BULLETS_VELOCITY = {-700, -500, -600, -250, -550, -500, -200, -400};
	public static final int ACCELERATION = 30;
	public static final int FRICTION = 18;
	public static final int WEAPON_UI_BASE_X = 20;
	public static final int NUM_NORMAL_WEAPONS = 4;
	
	// enemy related:
	public static final int ENEMY_HP_PER_LVL_MULTIPLIER = 2;
	public static final int ENEMY_HP_BASE = 0;
	public static final double CHANCE_TO_EXPLODE_ALIEN = 0.1;
	public static final int X_SHOOT_RANGE = 250;
	public static final int MIN_ATTACK_INTERVAL = 900;
	public static final int MAX_ATTACK_INTERVAL = 2_000;
	public static final int BASE_BULLET_VELOCITY = 270;
	
	// effect related:
	public static final int ROCKET_EXPLOSION_RADIUS = 80;
	
	// sound and music
	public static final float MUSIC_PER_TICK_MODIFIER = 0.007f;
	public static final boolean DECREASE_CLIPS_GAIN = true;
	
	// used colors
	public static final Color COLOR_TEXT_BOX_FILL_TUTORIAL = new Color(80, 80, 255);
	public static final Color COLOR_TEXT_BOX_FILL_SONG = new Color(66, 105, 140);
	
	// user interface
	public static final Font INFO_FONT = new Font("Monospaced", Font.BOLD, 18);
	public static final Font TEXT_BOX_FONT = new Font("Dialog", Font.BOLD, 20);
	public static final Font QUESTION_FONT = new Font("Dialog", Font.BOLD, 48);
	
	//visual effects
	public static final int NUM_BACKGROUND_STARS = 400;
	public static final double PLANET_SPAWN_CHANCE = 0.0005;
	public static final double GEM_SPAWN_CHANCE = 0.5;
	public static final int	PLANET_SPACING = -300;
	public static final int GEM_SPACING = -100;
	public static final int NUMBER_DIFFERENT_PLANETS = 14;
	public static final int NUMBER_DIFFERENT_GEMS = 11;
	public static final int PLANET_Y_VEL = 1;
	public static final int GEM_Y_VEL = 4;
	
	
	// utility method for values
	public static int controlMinMax(final int i, final int min, final int max){
		if (min >= max){
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

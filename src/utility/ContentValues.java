package utility;

import java.awt.Color;

public class ContentValues {
	// player related:
	public static final int MAX_PLAYER_SHIELD_DURATION = 100_000; // 1_000
	public static final int[] BULLETS_VELOCITY = {-700, -500, -600, -250, -550, -500, -200};
	public static final int ACCELERATION = 25;
	public static final int FRICTION = 12;
	public static final int WEAPON_UI_BASE_X = 20;
	
	// enemy related:
	public static final int ENEMY_HP_PER_LVL_MULTIPLIER = 3;
	public static final double CHANCE_TO_EXPLODE_ALIEN = 0.1;
	public static final int X_SHOOT_RANGE = 300;
	public static final int MIN_ATTACK_INTERVAL = 250;
	public static final int MAX_ATTACK_INTERVAL = 1500;
	public static final int BASE_BULLET_VELOCITY = 350;
	
	// effect related:
	public static final int ROCKET_EXPLOSION_RADIUS = 80;
	
	// sound and music
	public static final float MUSIC_PER_TICK_MODIFIER = 0.002f;
	
	// used colors
	public static final Color COLOR_TEXT_BOX_FILL = new Color(80, 80, 255);
	
	// user interface
	public static final int STUFF = 0;
	
	
	// utility method for values
	public static int controlMinMax(final int i, final int min, final int max){
		if (min >= max){
			System.out.println(" min more than max: " + min + ">=" + max);
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

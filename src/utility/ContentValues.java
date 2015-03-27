package utility;

public class ContentValues {
	// player related:
	public static final int MAX_PLAYER_SHIELD_DURATION = 100_000; // 1_000
	
	// enemy related:
	public static final int ENEMY_HP_PER_LVL_MULTIPLIER = 3;
	
	// effect related:
	public static final int ROCKET_EXPLOSION_RADIUS = 80;
	
	// sound and music
	public static final float MUSIC_PER_TICK_MODIFIER = 0.002f;
	
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

package utility.image;

import main.Game;
import utility.ContentValues;

/* The Gem class does not use setters or getters
 * or any good practices because it doesn't
 * need to. */
public class Gem {
	public float x, y;
	public byte type;
	
	public Gem(){
		x = (int) (Math.random() * Game.SCREEN_WIDTH);
		y = (int) (Math.random() * Game.SCREEN_HEIGHT);
		type = (byte) (1+ Math.random() * ContentValues.NUMBER_DIFFERENT_GEMS);
	}
}

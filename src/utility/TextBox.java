package utility;

/*
 * The TextBoxes are used to display some information for a short time;
 * examples: the name of the song that started playing; small tutorials.
 * Boxes start under the screen and go up until they reach their targetY.
 * The x is never changed. Each new line of text is in a separate String
 * of the text array. A whole String is given to the constructor, but
 * it is split up (by semicolon) in separate lines.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import main.Game;

public class TextBox {

	private String[] text;
	private int x, y, targetY, width, height, duration;
	
	public TextBox(String textString, int x, int targetY, int width, int duration){
		this.text = textString.split(";");
		this.x = x;
		this.targetY = targetY;
		this.width = width;
		this.height = text.length*30 + 20;
		y = Game.getGameHeight();
		this.duration = duration;
	}
	
	public int getDuration(){
		return duration;
	}
	
	public void draw(Graphics2D g){
		duration--;
		if (y > targetY){
			y -= (y - targetY)/20;
		}
		g.setColor(ContentValues.COLOR_TEXT_BOX_FILL);
		g.fillRect(x, y, width, height);
		g.setColor(Color.blue);
		g.drawRect(x+2, y+2, width-4, height-4);
		g.setColor(Color.RED);
		g.setFont(new Font("Dialog", Font.BOLD, 20));
		for (int i = 0; i < text.length; i++) {
			g.drawString(text[i], x + 10, y + (i+1)*30);
		}
	}
}







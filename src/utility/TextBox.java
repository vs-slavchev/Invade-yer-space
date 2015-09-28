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
	private boolean isTutorial;
	
	public TextBox(String textString, int x, int targetY, int width, int duration){
		this.text = textString.split(";");
		this.x = x;
		this.targetY = targetY;
		this.width = width;
		this.height = text.length*30 + 20;
		y = (targetY > Game.getGameHeight()/2) ? Game.getGameHeight() : 0;
		this.duration = duration;
		isTutorial = text[0].charAt(0) == 'S' ? false : true;
	}
	
	public int getDuration(){
		return duration;
	}
	
	public void draw(Graphics2D g){
		duration--;
		if (y > targetY){
			y -= (y - targetY)/20;
		} else if (y < targetY){
			y += (targetY - y)/20;
		}
		
		if (isTutorial){
			g.setColor(ContentValues.COLOR_TEXT_BOX_FILL_TUTORIAL);
		} else {
			g.setColor(ContentValues.COLOR_TEXT_BOX_FILL_SONG);
		}
		
		g.fillRect(x, y, width, height);
		g.setColor(Color.blue);
		g.drawRect(x+2, y+2, width-4, height-4);
		g.setColor(Color.yellow);
		g.setFont(ContentValues.TEXT_BOX_FONT);
		for (int i = 0; i < text.length; i++) {
			g.drawString(text[i], x + 10, y + (i+1)*30);
		}
		
		if (isTutorial){
			g.setColor(ContentValues.COLOR_TEXT_BOX_FILL_TUTORIAL);
			g.fillRect(x - 40, y - 20, 45, 45);
			g.setColor(Color.blue);
			g.drawRect(x - 40, y - 20, 45, 45);
			g.setColor(Color.yellow);
			g.setFont(ContentValues.QUESTION_FONT);
			g.drawString("?", x - 30, y + 20);
		}
	}

	public boolean isTutorial() {
		return isTutorial;
	}
}







package utility;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.CopyOnWriteArrayList;

public class TextBoxManager {
	
	private static CopyOnWriteArrayList<TextBox> textBoxes = new CopyOnWriteArrayList<>();
	
	public static void createTextBox(String type){
		switch(type){
		case "songPirateManol":
			textBoxes.add(new TextBox("Song: Pirate Manol,Artist: Marto,MnM Studios", 20, 800, 200, 250));
			break;
		case "songMidnightSun":
			textBoxes.add(new TextBox("Song: Midnight Sun,Artist: Marto,MnM Studios", 20, 300, 100, 250));
			break;
		default:
			textBoxes.add(new TextBox("missing text", 20, 300, 100, 400));
			break;
		}
	}
	
	public static void drawTextBoxes(Graphics2D g){
		if (!textBoxes.isEmpty()) {
			for (TextBox textBox : textBoxes) {
				if (textBox.getDuration() <= 0) {
					textBoxes.remove(textBox);
					continue;
				}
				textBox.draw(g);
			}
		}
	}
	

}

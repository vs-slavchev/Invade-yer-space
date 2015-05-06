package utility;

import java.awt.Graphics2D;
import java.util.concurrent.CopyOnWriteArrayList;

import main.Game;

public class TextBoxManager {
	
	private static CopyOnWriteArrayList<TextBox> textBoxes = new CopyOnWriteArrayList<>();
	
	public static void showTextBox(TextBox textBox){
		textBoxes.add(textBox);
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

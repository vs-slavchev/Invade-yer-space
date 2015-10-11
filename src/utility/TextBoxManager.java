package utility;

import java.awt.Graphics2D;
import java.util.concurrent.CopyOnWriteArrayList;

public class TextBoxManager {
	
	private static CopyOnWriteArrayList<TextBox> textBoxes = new CopyOnWriteArrayList<>();
	
	public static void showTextBox(TextBox textBox){
		textBoxes.add(textBox);
	}
	
	// used in the play state
	public static void drawTextBoxes(Graphics2D g){
		if (!textBoxes.isEmpty()) {
			for (TextBox textBox : textBoxes) {
				// remove if duration expired
				if (textBox.getDuration() <= 0) {
					textBoxes.remove(textBox);
					continue;
				}
				textBox.draw(g);
			}
		}
	}
	
	// used in the menu state
	public static void drawSongTextBoxes(Graphics2D g) {
		if (!textBoxes.isEmpty()) {
			for (TextBox textBox : textBoxes) {
				if (!textBox.isTutorial()) {
					if (textBox.getDuration() <= 0) {
						textBoxes.remove(textBox);
						continue;
					}
					textBox.draw(g);
				}
			}
		}
	}
	

}

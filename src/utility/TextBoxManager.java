package utility;

import java.awt.Graphics2D;
import java.util.concurrent.CopyOnWriteArrayList;

public class TextBoxManager {
	
	private static CopyOnWriteArrayList<TextBox> textBoxes = new CopyOnWriteArrayList<>();
	
	public static void showTextBox(TextBox textBox) {
		textBoxes.add(textBox);
	}

	public static void drawAllTextBoxes(Graphics2D g) {
		for (TextBox textBox : textBoxes) {
			checkExpired(textBox);
			textBox.draw(g);
		}
	}

	public static void drawSongTextBoxesOnly(Graphics2D g) {
		for (TextBox textBox : textBoxes) {
			if (!textBox.isTutorial()) {
				checkExpired(textBox);
				textBox.draw(g);
			}
		}
	}

	private static void checkExpired(TextBox textBox) {
		if (textBox.isExpired()) {
            textBoxes.remove(textBox);
			return;
        }
	}
}

package utility;

import java.awt.Graphics2D;
import java.util.concurrent.CopyOnWriteArrayList;

import main.Game;

public class TextBoxManager {
	
	private static CopyOnWriteArrayList<TextBox> textBoxes = new CopyOnWriteArrayList<>();
	
	public static void createTextBox(String type){
		switch(type){
		case "songPirateManol":
			textBoxes.add(new TextBox("Song: Pirate Manol;Artist: Marto;MnM Studios", 20, 800, 200, 250));
			break;
		case "songMidnightSun":
			textBoxes.add(new TextBox("Song: Midnight Sun;Artist: Marto;MnM Studios", 20, 800, 200, 250));
			break;
		case "tutorialMovement":
			textBoxes.add(new TextBox("Hold down the arrow keys;to move and space to shoot.", Game.getGameWidth()*2/3, Game.getGameHeight()-250, 285, 300));
			break;
		case "tutorialWeapons":
			textBoxes.add(new TextBox("Using a weapon will cause it;to overheat! Press 1, 2, 3 or 4;to change your weapon.", Game.getGameWidth()/5, 300, 300, 400));
			break;
		case "tutorialAutofire":
			textBoxes.add(new TextBox("Press T to toggle autofire.", Game.getGameWidth()*3/4, 200, 270, 300));
			break;
		case "tutorialMusicVolume":
			textBoxes.add(new TextBox("Hold < or > to decrease or;increase the music volume.", Game.getGameWidth()/2, 70, 280, 400));
			break;
		case "tutorialHealthbars":
			textBoxes.add(new TextBox("Hold R pressed to show;aliens' healthbars.", 100, 150, 260, 350));
			break;
		case "tutorialCombos":
			textBoxes.add(new TextBox("High combos grant special;bonus effects!", Game.getGameWidth()*3/4, Game.getGameHeight()/2, 280, 300));
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

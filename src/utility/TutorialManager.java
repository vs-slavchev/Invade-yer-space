package utility;

import java.util.Stack;

import utility.sound.SoundManager;
import main.Game;

public class TutorialManager {

	private static boolean tutorialsOn = true;
	private static Stack<TextBox> tutorials = new Stack<>();
	private static int timer = -70;
	
	public static void initTutorialList(){
		tutorials.push(new TextBox("Esc returns you to the menu.",
				Game.getGameWidth()*2/3, Game.getGameHeight()-250));
		tutorials.push(new TextBox("High combos grant special;bonus effects!",
				Game.getGameWidth()*3/4, Game.getGameHeight()/2));
		tutorials.push(new TextBox("Hold R pressed to show;aliens' healthbars.",
				100, 150));
		tutorials.push(new TextBox("Hold - or + to adjust;the music volume.",
				Game.getGameWidth()/2, 70));
		tutorials.push(new TextBox("Press T to toggle autofire.",
				Game.getGameWidth()*3/4, 200));
		tutorials.push(new TextBox("Using a weapon causes it to;overheat! Press 2, 3, 4 or 1;" +
				"to switch between weapons.", Game.getGameWidth()/5, 300));
		tutorials.push(new TextBox("Space bar to shoot.",
				Game.getGameWidth()*2/3, Game.getGameHeight()-250));
	}
	
	public static void updateTutorials(){
        if (timer > ContentValues.NUM_TUTORIALS * ContentValues.TIME_STEP) {
            return;
        }
        if (timer % ContentValues.TIME_STEP == 0) {
            showNextTutorial();
        }
        timer++;
    }

	private static void showNextTutorial() {
		if (!tutorials.isEmpty() && tutorialsOn) {
			TextBoxManager.showTextBox(tutorials.pop());
			SoundManager.play("tutorialSound");
		}
	}
}

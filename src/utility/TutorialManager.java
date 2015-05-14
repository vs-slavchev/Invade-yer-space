package utility;

import java.util.Stack;

import utility.sound.SoundManager;
import main.Game;

public class TutorialManager {

	private static boolean tutorialsOn = true;
	private static Stack<TextBox> tutorials = new Stack<>();
	private static int timer = -70; // 70 steps delay before the 1st tutorial
	private static int numTutorials = 7;
	private static int timeStep = 400;
	
	// the first tutorials are at the bottom of the method; top of the stack
	public static void initTutorialList(){
		tutorials.push(new TextBox("Esc returns you to the menu.", Game.getGameWidth()*2/3, Game.getGameHeight()-250, 300, 250));
		tutorials.push(new TextBox("High combos grant special;bonus effects!", Game.getGameWidth()*3/4, Game.getGameHeight()/2, 280, 300));
		tutorials.push(new TextBox("Hold R pressed to show;aliens' healthbars.", 100, 150, 260, 420));
		tutorials.push(new TextBox("Hold - or + to decrease or;increase the music volume.", Game.getGameWidth()/2, 70, 280, 420));
		tutorials.push(new TextBox("Press T to toggle autofire.", Game.getGameWidth()*3/4, 200, 270, 400));
		tutorials.push(new TextBox("Using a weapon will cause it;to overheat! Press 1, 2, 3 or 4;to switch between weapons.", Game.getGameWidth()/5, 300, 300, 500));
		tutorials.push(new TextBox("Hold down the space bar;to shoot.", Game.getGameWidth()*2/3, Game.getGameHeight()-250, 285, 400));
	}
	
	public static void updateTutorials(){
		if (timer % timeStep == 0 && tutorials.size() == numTutorials - timer/timeStep){
			showNextTutorial();
		}
		if (timer < numTutorials*timeStep){
			timer++;
		}
	}

	private static void showNextTutorial() {
		// show next tutorial
		if (!tutorials.isEmpty() && tutorialsOn) {
			TextBoxManager.showTextBox(tutorials.pop());
			SoundManager.play("tutorialSound");
		}
	}
}

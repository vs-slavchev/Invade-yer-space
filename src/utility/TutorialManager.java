package utility;

import java.util.Stack;

public class TutorialManager {

	private static boolean tutorialsOn = true;
	private static Stack<String> tutorialNames = new Stack<>();
	private static int timer = 0;
	private static int numTutorials = 6;
	private static int timeStep = 500;
	
	public static void initTutorialList(){
		tutorialNames.push("tutorialCombos");
		tutorialNames.push("tutorialHealthbars");
		tutorialNames.push("tutorialMusicVolume");
		tutorialNames.push("tutorialAutofire");
		tutorialNames.push("tutorialWeapons");
		tutorialNames.push("tutorialMovement");
	}
	
	public static void updateTutorials(){
		if (timer % timeStep == 0 && tutorialNames.size() == numTutorials - timer/timeStep){
			showNextTutorial();
		}
		if (timer < numTutorials*timeStep){
			timer++;
		}
	}

	private static void showNextTutorial() {
		// show next tutorial
		if (!tutorialNames.isEmpty() && tutorialsOn) {
			TextBoxManager.createTextBox(tutorialNames.pop());
		}
	}
}

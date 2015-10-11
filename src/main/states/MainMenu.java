package main.states;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JOptionPane;

import utility.ContentValues;
import utility.image.Gem;
import utility.image.ImageManager;
import utility.sound.SoundManager;
import main.Game;
import main.states.StateManager.States;


public class MainMenu {
	private static int currentOption = 0;
	private static final String[] options = { "play", "quit" };
	private static Gem[] gems = new Gem[ContentValues.MAX_GEMS];
	
	public static void goUp() {
		
		if (currentOption == 0) {
			currentOption = options.length - 1;
		} else {
			currentOption--;
		}
	}

	public static void goDown() {
		if (currentOption == options.length - 1) {
			currentOption = 0;
		} else {
			currentOption++;
		}
	}

	public static void select(Game game) {
		String choice = options[currentOption];
		switch (choice) {
		case "play":
			SoundManager.play("yarr");
			Game.getMusicManager().loopBackgroundMusic(1);
			StateManager.setState(States.PLAY);
			game.switchToInputKeyHandler();
			game.setWaitingForKeyPress(true);
			break;
		case "quit":
			System.exit(0);
			break;
		default:
			JOptionPane.showMessageDialog(null,
					"Error: \n" + choice + "\nnot a valid menu choice!",
				    "Error in menu!",
				    JOptionPane.ERROR_MESSAGE);
			 System.exit(0);
			break;
		}
	}

	private static void drawMenuBackground(Graphics2D g) {
		g.drawImage(ImageManager.getImage("text/skyBackground"), 0, 0, null);
		drawGems(g);
		g.drawImage(ImageManager.getImage("text/captainManol"), 0, 0, null);

	}
	
	public static void initGems(){
		for (int i = 0; i < ContentValues.MAX_GEMS; i++){
			// the Gem constructor assigns all fields random values
			gems[i] = new Gem();
		}
	}

	private static void drawGems(Graphics2D g) {
		for (Gem gem : gems){
			g.drawImage(ImageManager.getImage("effects/gems/gem" + gem.type), (int)gem.x, (int)gem.y, null);
		}
	}
	
	private static void updateGems(){
		for (Gem gem : gems){
			gem.y += 0.7;
			if (gem.y > Game.SCREEN_HEIGHT){
				gem.y = -50; 
			}
		}
	}

	public static void drawMenu(Graphics2D g) {
		updateGems();
		
		drawMenuBackground(g);
		
		// draw title
		g.drawImage(ImageManager.getImage("text/titleText"), Game.SCREEN_WIDTH/2
				- ImageManager.getImage("text/titleText").getWidth(null) / 2,
				Game.SCREEN_HEIGHT / 9, null);
		
		int baseX = 300;
		int offset = Game.SCREEN_HEIGHT / 9;
		for (int i = 0; i < options.length; i++) {
			int imgWidth = ImageManager.getImage("text/" + options[i]).getWidth(null);

			if (i == currentOption) {
				g.drawImage(ImageManager.getImage("text/selector"),
						baseX - imgWidth / 2 + 180,
								Game.SCREEN_HEIGHT * 4 / 10 + i * offset - 5*i, null);
			}
			g.drawImage(ImageManager.getImage("text/" + options[i]),
					baseX - imgWidth / 2,
					Game.SCREEN_HEIGHT * 4 / 10 + i * offset, null);
		}
		
		int creditsX = Game.SCREEN_WIDTH - ImageManager.getImage("text/credits").getWidth(null);
		int creditsY = Game.SCREEN_HEIGHT - ImageManager.getImage("text/credits").getHeight(null);
		
		g.drawImage(ImageManager.getImage("text/credits"), creditsX, creditsY, null);
		
		g.setFont(ContentValues.INFO_FONT);
		g.setColor(Color.BLACK);
		//TODO correct version
		g.drawString(ContentValues.GAME_VERSION, 100, Game.getGameHeight() - 10);
	}

}

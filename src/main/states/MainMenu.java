package main.states;

import java.awt.Color;
import java.awt.Graphics2D;

import utility.ContentValues;
import utility.image.BackgroundImageManager;
import utility.image.ImageManager;
import utility.sound.SoundManager;
import main.Game;
import main.states.StateManager.States;


public class MainMenu {
	private static int currentOption = 0;
	private static final String[] options = { "play", "quit" };
	private static BackgroundImageManager gemsManager = new BackgroundImageManager(ContentValues.MAX_BACKGROUND_GEMS,
			ContentValues.NUMBER_DIFFERENT_GEMS, ContentValues.GEM_SPAWN_CHANCE, "effects/gems/gem", ContentValues.GEM_Y_VEL);
	
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

	@SuppressWarnings("incomplete-switch")
	public static void select(Game game) {
		String choice = options[currentOption];
		switch (choice) {
		case "play":
			SoundManager.play("yarr");
			Game.getMusicManager().loopBackgroundMusic(1);
			/* sleep to let the sound and music
			 * threads start and get running */
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			StateManager.setState(States.PLAY);
			game.switchToInputKeyHandler();
			game.setWaitingForKeyPress(true);
			break;
		case "quit":
			System.exit(0);
			break;
		}

	}

	private static void drawMenuBackground(Graphics2D g) {
		g.drawImage(ImageManager.getImage("text/skyBackground"), 0, 0, null);
		gemsManager.drawBackgroundObjects(g);
		g.drawImage(ImageManager.getImage("text/captainManol"), 0, 0, null);

	}

	public static void drawMenu(Graphics2D g) {
		gemsManager.update();
		
		drawMenuBackground(g);
		
		// draw title
		g.drawImage(ImageManager.getImage("text/titleText"), Game.WIDTH/2
				- ImageManager.getImage("text/titleText").getWidth(null) / 2,
				Game.HEIGHT / 9, null);
		
		int baseX = 300;
		int offset = Game.HEIGHT / 9;
		for (int i = 0; i < options.length; i++) {
			int imgWidth = ImageManager.getImage("text/" + options[i]).getWidth(null);

			if (i == currentOption) {
				g.drawImage(ImageManager.getImage("text/selector"),
						baseX - imgWidth / 2 + 180,
								Game.HEIGHT * 4 / 10 + i * offset - 5*i, null);
			}
			g.drawImage(ImageManager.getImage("text/" + options[i]),
					baseX - imgWidth / 2,
					Game.HEIGHT * 4 / 10 + i * offset, null);
		}
		
		int creditsX = Game.WIDTH - ImageManager.getImage("text/credits").getWidth(null);
		int creditsY = Game.HEIGHT - ImageManager.getImage("text/credits").getHeight(null);
		
		g.drawImage(ImageManager.getImage("text/credits"), creditsX, creditsY, null);
	}

}

package main;

import java.awt.Color;
import java.awt.Graphics2D;

import utility.ContentValues;
import utility.image.ImageManager;
import main.StateManager.States;


public class MainMenu {
	private static int currentOption = 0;
	private static final String[] options = { "play", "quit" };
	
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
	public static void select() {
		String choice = options[currentOption];
		switch (choice) {
		case "play":
			StateManager.setState(States.PLAY);
			Game.setWaitingForKeyPress(true);
			break;
		case "quit":
			System.exit(0);
			break;
		}

	}

	private static void drawMenuBackground(Graphics2D g) {
		int imgWidth = ImageManager.getImage("captainManol").getWidth(null);
		int imgHeight = ImageManager.getImage("captainManol").getHeight(null);

		g.drawImage(ImageManager.getImage("captainManol"), 0, 0, null);

	}

	public static void drawMenu(Graphics2D g) {
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
		
		int creditsY = Game.HEIGHT - 60;
		
		// headings
		g.setColor(Color.RED);
		g.setFont(ContentValues.INFO_HEADING_FONT);
		Game.drawStringCentered(g, "Programming:", Game.WIDTH/2, creditsY);
		Game.drawStringCentered(g, "Soundtrack:", Game.WIDTH/2, creditsY + 50);

		// text
		g.setColor(Color.ORANGE);
		g.setFont(ContentValues.INFO_TEXT_FONT);
		Game.drawStringCentered(g, "Veselin Slavchev", (Game.WIDTH * 3 / 4) - 100,
				creditsY);
		Game.drawStringCentered(g, "Marto D", (Game.WIDTH * 3 / 4) - 100, creditsY + 50);

	}

}

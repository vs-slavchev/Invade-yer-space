package utility;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import utility.sound.SoundManager;
import main.Game;
import main.states.MainMenu;

public class MenuKeys extends KeyAdapter {

	private Game game;
	
	public MenuKeys(Game game){
		this.game = game;
	}

	@SuppressWarnings("incomplete-switch")
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_UP:
			SoundManager.play("menuSound");
			MainMenu.goUp();
			break;
		case KeyEvent.VK_DOWN:
			SoundManager.play("menuSound");
			MainMenu.goDown();
			break;
		case KeyEvent.VK_SPACE:
			SoundManager.play("menuSound");
			MainMenu.select(game);
			break;
		case KeyEvent.VK_ENTER:
			SoundManager.play("menuSound");
			MainMenu.select(game);
			break;
		case KeyEvent.VK_ESCAPE:
			game.setGameRunning(false);
			break;
		}
	}

}
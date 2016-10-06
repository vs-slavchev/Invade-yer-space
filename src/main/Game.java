package main;

import utility.ContentValues;
import utility.InputController;
import utility.MenuKeys;
import utility.TextBoxManager;
import utility.TutorialManager;
import utility.image.AnimationManager;
import utility.image.BackgroundPlanetManager;
import utility.image.ImageManager;
import utility.sound.MusicManager;
import utility.sound.SoundManager;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.states.MainMenu;
import main.states.StateManager;
import main.states.StateManager.States;
import entities.ShipEntity;
import entities.utility.EntityManager;

public class Game extends Canvas {

	private static final long serialVersionUID = -4872814357516418820L;
	private BufferStrategy strategy;
	
	public final static GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	public final static GraphicsDevice device = env.getScreenDevices()[0]; 
	public static final Rectangle SCREEN_RECTANGLE = device.getDefaultConfiguration().getBounds(); 
	public static final int SCREEN_WIDTH = SCREEN_RECTANGLE.width; 
	public static final int SCREEN_HEIGHT = SCREEN_RECTANGLE.height; 
	
	private final int FPS = 90;
	private volatile long delta = 1000 / this.FPS;
	volatile boolean gameRunning = true;
	private boolean logicRequiredThisLoop = false;
	boolean waitingForKeyPress = false;
	InputController inputController;
	private MenuKeys menuKeys = new MenuKeys(this);
	private KeyInputHandler keyInputHandler = new KeyInputHandler();
	
	EntityManager entityManager = new EntityManager(this);
	private static MusicManager musicManager = new MusicManager();
	private static boolean alienHPBarDrawn = false;
	String message = "startText";
	static int score = 0;
	private BackgroundPlanetManager planetManager =
			new BackgroundPlanetManager("planets/planet");

	public Game() {

		JFrame container = new JFrame("Invade yer space ye scurvy dog!");
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		panel.setLayout(null);

		setBounds(0, 0, SCREEN_WIDTH , SCREEN_HEIGHT);
		setLocation(0, 0);
		container.setUndecorated(true);
		panel.add(this);

		// make the cursor transparent
		container.setCursor( container.getToolkit().createCustomCursor(
				new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ), new Point(), null ) );
		/* tell awt not to repaint the canvas since I am doing it myself in
		 * accelerated mode */
		setIgnoreRepaint(true);

		container.pack();
		container.setResizable(false);
		container.setVisible(true);

		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		addKeyListener(menuKeys);
		
		requestFocus();

		createBufferStrategy(2);
		strategy = getBufferStrategy();

		inputController = new InputController();
		ImageManager.initImages();
		
		entityManager.initEntities();
		SoundManager.initSoundManager();
		TutorialManager.initTutorialList();
		MainMenu.initGems();

		musicManager.loopBackgroundMusic(0);
	}

	public static void main(String argv[]) {
		(new Game()).playGame();
		System.exit(0);
	}

	public void playGame() {

		long lastLoopTime = System.currentTimeMillis();

		while (gameRunning) {
			delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
				
			if (StateManager.getState() == States.PLAY){
				if (!waitingForKeyPress) {
					entityManager.moveEntities(delta);
					planetManager.update();
					TutorialManager.updateTutorials();
				}
	
				if (!waitingForKeyPress) {
					entityManager.collideEntities();
				}
	
				AnimationManager.updateAnimations();
				entityManager.removeEntities();
				if (logicRequiredThisLoop) {
					entityManager.applyLogic();
					logicRequiredThisLoop = false;
				}
				entityManager.getShip().processInput(inputController);
			}

			controlMusicGain();
			drawGame();
			sleepAndFPSControl(lastLoopTime);
			
		}
		SoundManager.close();
	}

	private void controlMusicGain() {
		if (inputController.isMusicDownPressed()) {
			musicManager.modifyGain(-ContentValues.MUSIC_PER_TICK_MODIFIER);
		} else if (inputController.isMusicUpPressed()) {
			musicManager.modifyGain(ContentValues.MUSIC_PER_TICK_MODIFIER);
		}
		musicManager.update();
	}

	private void sleepAndFPSControl(final long lastLoopTime) {
		try {
			long sleepTime = lastLoopTime +  (1000 / FPS) - System.currentTimeMillis();
			Thread.sleep(sleepTime > 0 ? sleepTime : 1);
		} catch (InterruptedException e) {
			System.out.println("mainThread.sleep was interrupted!");
		}
	}

	private void drawGame() {
		// get a graphics context for the accelerated surface and blank it
		Graphics2D g = (Graphics2D) this.strategy.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		if (StateManager.getState() == States.PLAY){
			planetManager.drawBackgroundObjects(g);
			entityManager.drawEntities(g);
			AnimationManager.drawAnimations(g);
			
			if (waitingForKeyPress) {
				g.drawImage(ImageManager.getImage("text/" + message),
						(SCREEN_WIDTH - ImageManager.getImage("text/" + message).getWidth(null))/2,
						(SCREEN_HEIGHT/2 - ImageManager.getImage("text/" + message).getHeight(null))/2, null);
				
				entityManager.getShip().getComboManager().drawComboScore(g);
				
			} else {
				TextBoxManager.drawTextBoxes(g);
			}
			
		} else if (StateManager.getState() == States.MENU){
			MainMenu.drawMenu(g);
			TextBoxManager.drawSongTextBoxes(g);
		}
		
		drawSpeakerIcon(g);
		
		// after drawing clean up and flip the buffer
		g.dispose();
		strategy.show();
	}

	private void drawSpeakerIcon(Graphics2D g) {
		int sX = 0;
		boolean willDraw = false;
		if (inputController.isMusicDownPressed()){
			sX = 1;
			willDraw = true;
		}else if (inputController.isMusicUpPressed()){
			willDraw = true;
		}
		if (willDraw) {
			g.drawImage(ImageManager.getImage("effects/speakerUI"),
					SCREEN_WIDTH - 90, 30, SCREEN_WIDTH - 90 + 64, 30 + 64, 64 * sX, 0, 64 * (sX + 1), 64, null);
		}
	}

	public void switchToMenuKeyHandler(){
		removeKeyListener(keyInputHandler);
		addKeyListener(menuKeys);
	}
	
	public void switchToInputKeyHandler(){
		removeKeyListener(menuKeys);
		addKeyListener(keyInputHandler);
	}

	public void notifyDeath() {
		// turn autoFire off to prevent from shooting in the next game
		ShipEntity ship =  entityManager.getShip();
		ship.setAutoFireOn(false);
		message = "deathText";
		waitingForKeyPress = true;
		SoundManager.play("nooo");
		score += ship.getComboManager().getMaxComboAchieved();
	}

	public void notifyWin() {
		// turn autoFire off to prevent from shooting in the next game
		entityManager.getShip().setAutoFireOn(false);
		message = "winText";
		waitingForKeyPress = true;
		musicManager.temporaryDecreaseGain();
		SoundManager.playOnly("manolWin");
		entityManager.generateNewEntitiesMap();
		score += entityManager.getShip().getComboManager().getMaxComboAchieved();
	}

	public void notifyAlienKilled() {
		entityManager.decrementAlienCount();
		entityManager.getShip().getComboManager().incrementRecentKillCount();
		if (entityManager.getAlienCount() < 1){
			notifyWin();
		}
		entityManager.speedUpAlienEntities();
	}
	
	public boolean isAlienHPBarDrawn(){
		return alienHPBarDrawn;
	}
	
	public static void setAlienHPBarDrawn(boolean value){
		alienHPBarDrawn = value;
	}

	public void updateLogic() {
		logicRequiredThisLoop = true;
	}

	void startGame() {
		inputController.reset();
		AnimationManager.clearAll();
		entityManager.initEntities();
	}

	public static int getGameWidth() {
		return SCREEN_WIDTH;
	}

	public static int getGameHeight() {
		return SCREEN_HEIGHT;
	}
	
	public InputController getInputController() {
		return inputController;
	}
	
	public static int getScore(){
		return score;
	}
	
	public void setGameRunning(boolean gameRunning) {
		this.gameRunning = gameRunning;
	}

	public static MusicManager getMusicManager() {
		return musicManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public MusicManager getSoundManager() {
		return getMusicManager();
	}

	public boolean isWaitingForKeyPress() {
		return waitingForKeyPress;
	}
	
	public static void drawStringCentered(Graphics2D g, String text, int x, int y){
		g.drawString(text, x - g.getFontMetrics().stringWidth(String.valueOf(text))/2, y);
	}
	
	public void setWaitingForKeyPress(boolean val){
		waitingForKeyPress = val;
	}

	private class KeyInputHandler extends KeyAdapter {

		private int pressCount;

		public KeyInputHandler() {
			pressCount = 1;
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				inputController.setLeftPressed(true);
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				inputController.setRightPressed(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				inputController.setUpPressed(true);
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				inputController.setDownPressed(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
				inputController.setFirePressed(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_R) {
				Game.setAlienHPBarDrawn(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_T) {
				inputController.setAutoFirePressed(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_COMMA) {
				inputController.setMusicDownPressed(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_PERIOD) {
				inputController.setMusicUpPressed(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_1) {
				inputController.numberPressed.set(1, true);
			}else if (e.getKeyCode() == KeyEvent.VK_2) {
				inputController.numberPressed.set(2, true);
			}else if (e.getKeyCode() == KeyEvent.VK_3) {
				inputController.numberPressed.set(3, true);
			}else if (e.getKeyCode() == KeyEvent.VK_4) {
				inputController.numberPressed.set(4, true);
			}
		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				inputController.setLeftPressed(false);
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				inputController.setRightPressed(false);
			}
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				inputController.setUpPressed(false);
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				inputController.setDownPressed(false);
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
				inputController.setFirePressed(false);
			}
			if (e.getKeyCode() == KeyEvent.VK_R) {
				Game.setAlienHPBarDrawn(false);
			}
			if (e.getKeyCode() == KeyEvent.VK_COMMA) {
				inputController.setMusicDownPressed(false);
			}
			if (e.getKeyCode() == KeyEvent.VK_PERIOD) {
				inputController.setMusicUpPressed(false);
			}
			if (e.getKeyCode() == KeyEvent.VK_1) {
				inputController.numberPressed.set(1, false);
			}else if (e.getKeyCode() == KeyEvent.VK_2) {
				inputController.numberPressed.set(2, false);
			}else if (e.getKeyCode() == KeyEvent.VK_3) {
				inputController.numberPressed.set(3, false);
			}else if (e.getKeyCode() == KeyEvent.VK_4) {
				inputController.numberPressed.set(4, false);
			}
		}

		public void keyTyped(KeyEvent e) {
			// keyCodes: 32 = space; 10 = enter
			if (waitingForKeyPress && (e.getKeyChar() == 32 || e.getKeyChar() == 10)) {
				if (pressCount == 1) {
					setWaitingForKeyPress(false);
					inputController.setFirePressed(false);
					if (message.equals("deathText")){
						score = 0;
					}
					startGame();
					pressCount = 1;
				} else {
					pressCount++;
				}
			}
			if (e.getKeyChar() == 27) { // escape to menu
				// turn autoFire off to prevent from shooting in the next game
				entityManager.getShip().setAutoFireOn(false);
				StateManager.setState(States.MENU);
				getMusicManager().loopBackgroundMusic(0);
				switchToMenuKeyHandler();
			}
		}
	}
}
package main;

import utility.ContentValues;
import utility.InputController;
import utility.TextBoxManager;
import utility.TutorialManager;
import utility.image.AnimationManager;
import utility.image.BackgroundImageManager;
import utility.image.ImageManager;
import utility.sound.MusicManager;
import utility.sound.SoundManager;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import entities.ShipEntity;
import entities.utility.EntityManager;

public class Game extends Canvas {

	private static final long serialVersionUID = -4872814357516418820L;
	private BufferStrategy strategy;
	
	public final static GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	public final static GraphicsDevice device = env.getScreenDevices()[0]; 
	public static final Rectangle RECTANGLE = device.getDefaultConfiguration().getBounds(); 
	public static final int WIDTH = RECTANGLE.width; 
	public static final int HEIGHT = RECTANGLE.height; 
	
	private int FPS = 60;
	private volatile long delta = 1000 / this.FPS;
	volatile boolean gameRunning = true;
	private volatile boolean paused = false;
	private boolean logicRequiredThisLoop = false;
	boolean waitingForKeyPress = true;
	private EntityManager entityManager = new EntityManager(this);
	private MusicManager musicManager = new MusicManager();
	private SoundManager soundManager = SoundManager.getSoundManager();
	InputController inputController;
	private static boolean alienHPBarDrawn = false;
	private String message = "startText";

	public Game() {

		JFrame container = new JFrame("Invade yer space ye scurvy dog!");
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.setLayout(null);

		setBounds(0, 0, WIDTH , HEIGHT);
		setLocation(0, 0);
		container.setUndecorated(true);
		panel.add(this);

		// make the cursor transparent
		container.setCursor( container.getToolkit().createCustomCursor(
				new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ), new Point(), null ) );
		// tell awt not to repaint the canvas since I am doing it myself in
		// accelerated mode
		setIgnoreRepaint(true);

		container.pack();
		container.setResizable(false);
		container.setVisible(true);

		// add a listener to respond to closing the window
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// add input key system; InputKeyHandler class defined in Game class
		addKeyListener(new KeyInputHandler());

		requestFocus();

		// use BufferStrategy class to manage buffers for the accelerated
		// graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		inputController = new InputController();
		ImageManager.initImages();
		
		entityManager.initEntities();
		soundManager.initSoundManager();
		TutorialManager.initTutorialList();

		// start playing background music
		musicManager.loopBackgroundMusic();
		BackgroundImageManager.generateBackgroundStars(400);

	}

	public static void main(String argv[]) {
		(new Game()).gameLoop();
		System.exit(0);
	}

	public void gameLoop() {

		long lastLoopTime = System.currentTimeMillis();
		float minimumFPS = 100.00f;

		while (gameRunning) {

			// calculate how far entities should move this loop, based on the
			// time since last update
			delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			if (minimumFPS > 1000.00f / delta) {
				minimumFPS = 1000.00f / delta;
			}
			
			
			if (!paused) {
				// cycle trough entities and move them
				if (!waitingForKeyPress) {
					entityManager.moveEntities(delta);
					BackgroundImageManager.update();
					TutorialManager.updateTutorials();
				}

				// collision detection: ship,shots are checked against the
				// aliens,
				// alienShots
				if (!waitingForKeyPress) {
					entityManager.collideEntities();
				}

				AnimationManager.getAnimationManager().updateAnimations();
				// remove destroyed entities that are to be removed and clean up
				entityManager.removeEntities();
				// if we need to do logic go through alienEntities only, since
				// only
				// aliens have a doLogic() implemented
				if (logicRequiredThisLoop) {
					entityManager.forceLogic();
					logicRequiredThisLoop = false;
				}
				// give the input to the ship to be processed
				((ShipEntity) entityManager.getShip())
						.processInput(inputController);

			}
			
			controlPause();
			controlMusicGain();
			drawGame();

			sleepAndFPSControl(lastLoopTime);
		} // close while
		soundManager.close();
	}

	private void controlPause() {
		if (inputController.isPausePressed()){
			paused = !paused;
			inputController.setPausePressed(false);
		}
	}

	private void controlMusicGain() {
		if (inputController.isMusicDownPressed()){
			musicManager.modifyGain( -ContentValues.MUSIC_PER_TICK_MODIFIER);
		}else if (inputController.isMusicUpPressed()){
			musicManager.modifyGain(ContentValues.MUSIC_PER_TICK_MODIFIER);
		}
	}

	// pause and fps control
	private void sleepAndFPSControl(long lastLoopTime) {
		try {
			long sleepTime = lastLoopTime +  (1000 / FPS) - System.currentTimeMillis();
			if (sleepTime <= 1){
				sleepTime = 1000 / FPS;
			}
			Thread.sleep(sleepTime);
			//Thread.sleep(1000 / FPS);
		} catch (InterruptedException e) {
			System.out.println("mainThread.sleep was interrupted!");
		}
	}

	private void drawGame() {
		// get a graphics context for the accelerated surface and blank it
		Graphics2D g = (Graphics2D) this.strategy.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		BackgroundImageManager.drawBackgroundObjects(g);

		// draw cycle
		entityManager.drawEntities(g);
		
		AnimationManager.getAnimationManager().drawAnimations(g);
		drawSpeakerIcon(g);
		
		TextBoxManager.drawTextBoxes(g);
		
		if (paused){
			g.drawImage(ImageManager.getImage("text/pausedText"),
					WIDTH/2 - ImageManager.getImage("text/pausedText").getWidth(null)/2,
					HEIGHT/2 - ImageManager.getImage("text/pausedText").getHeight(null)/2, null);
		}

		// if game is waiting for "any key press" show message
		if (waitingForKeyPress) {
			g.drawImage(ImageManager.getImage("text/" + message),
					(WIDTH - ImageManager.getImage("text/" + message).getWidth(null))/2,
					(HEIGHT - ImageManager.getImage("text/" + message).getHeight(null))/2, null);
		}

		// after drawing clean up and flip the buffer
		g.dispose();
		strategy.show();
	}

	private void drawSpeakerIcon(Graphics2D g) {
		if (inputController.isMusicDownPressed()){
			g.drawImage(ImageManager.getImage("effects/speakerUI"), WIDTH-90, 30, WIDTH-90+64, 30+64, 64, 0, 128, 64, null);
		}else if (inputController.isMusicUpPressed()){
			g.drawImage(ImageManager.getImage("effects/speakerUI"), WIDTH-90, 30, WIDTH-90+64, 30+64, 0, 0, 64, 64, null);
		}
	}

	public void notifyDeath() {
		message = "deathText";
		waitingForKeyPress = true;
	}

	public void notifyWin() {
		message = "winText";
		waitingForKeyPress = true;
		entityManager.generateNewEntitiesMap();
	}

	public void notifyAlienKilled() {
		entityManager.decrementAlienCount();
		((ShipEntity) entityManager.getShip()).getComboManager().incrementRecentKillCount();
		if (entityManager.getAlienCount() < 1){
			notifyWin();
		}
		// speed up the remaining aliens by 1%
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
		entityManager.initEntities();
		inputController.reset();
	}

	public static int getGameWidth() {
		return WIDTH;
	}

	public static int getGameHeight() {
		return HEIGHT;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public MusicManager getSoundManager() {
		return musicManager;
	}
	
	public void drawStringCentered(Graphics2D g, String text, int x, int y){
		g.drawString(text, x - g.getFontMetrics().stringWidth(String.valueOf(text))/2, y);
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
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
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
			if (e.getKeyCode() == KeyEvent.VK_P) {
				inputController.setPausePressed(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_1) {
				inputController.setOnePressed(true);
			}else if (e.getKeyCode() == KeyEvent.VK_2) {
				inputController.setTwoPressed(true);
			}else if (e.getKeyCode() == KeyEvent.VK_3) {
				inputController.setThreePressed(true);
			}else if (e.getKeyCode() == KeyEvent.VK_4) {
				inputController.setFourPressed(true);
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
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
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
				inputController.setOnePressed(false);
			}else if (e.getKeyCode() == KeyEvent.VK_2) {
				inputController.setTwoPressed(false);
			}else if (e.getKeyCode() == KeyEvent.VK_3) {
				inputController.setThreePressed(false);
			}else if (e.getKeyCode() == KeyEvent.VK_4) {
				inputController.setFourPressed(false);
			}
		}

		public void keyTyped(KeyEvent e) {
			if (waitingForKeyPress && e.getKeyChar() == 32) {
				inputController.setFirePressed(false); // added to cancel instant shooting
				if (pressCount == 1) {
					waitingForKeyPress = false;
					startGame();
					pressCount = 1;
				} else {
					pressCount++;
				}
			}
			if (e.getKeyChar() == 27) { // escape
				gameRunning = false;
			}
		}
	}// close KeyInputHandler class
}
	
	/* TODO:
	 * 
	 * [-] level ending delay
	 * [-] low prio: fix showing healthbars while not playing
	 * [-] pirate themed weapon/powerups names
	 * [-] cooltext combo digits
	 * [-] scoring mechanic: max combo achieved this level
	 * [-] basic main menu; states: menu, credits, playing
	 * [-] menu items: play, tutorials on/off, credits, exit
	 * [+] tutoral style: "press 1, 2, 3 or 4 to switch to that weapon" a textBox
	 * [-] refactoring:
		 * 	= fix switch cases to look like StatusEffect constructor default
	 * [-] sfx - only 1 Manol response active at any time; if Manol is talking ignore new response requests
		 * 	= yarr!; on powerup pickup
		 * 	= random pirate swears on events
		 * 	= yarr! me cannon is too hot'h!
		 *  = yarr! i got shield
		 *  = shooting sfx for diff weapons - dr.petter
		 *  = reflecting the shots sfx
		 *  = more explosions sfx
		 *  = weapon switch sound (responsiveness: every keypress should be indicated by a sound)
		 *  = shield buff goes up/down (powerups sound)
 	 */


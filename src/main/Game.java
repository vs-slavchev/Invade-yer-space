package main;

import utility.InputController;
import utility.image.AnimationManager;
import utility.image.BackgroundImageManager;
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
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import entities.EntityManager;
import entities.ShipEntity;

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
	private volatile boolean gameRunning = true;
	private boolean logicRequiredThisLoop = false;
	private boolean waitingForKeyPress = true;
	private EntityManager entityManager = new EntityManager(this);
	private MusicManager musicManager = new MusicManager();
	private SoundManager soundManager = SoundManager.getSoundManager();
	InputController inputController;
	private String message = "startText";
	private int difficulty = 3; // 3-ez, 2-normal, 1-hard

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
		container.setCursor( container.getToolkit().createCustomCursor( new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ), new Point(), null ) );
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
		try {
			ImageManager.initImages();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println(" Initializing images failed!");
		}
		
		entityManager.initEntities();
		soundManager.initSoundManager();

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

			// cycle trough entities and move them
			if (!waitingForKeyPress) {
				entityManager.moveEntities(delta);
				BackgroundImageManager.update();
			}
			// collision detection: ship,shots are checked against the aliens,
			// alienShots
			if (!waitingForKeyPress) {
				entityManager.collideEntities();
			}
			AnimationManager.getAnimationManager().updateAnimations();
			// remove destroyed entities that are to be removed and clean up
			entityManager.removeEntities();
			// if we need to do logic go through alienEntities only, since only
			// aliens have a doLogic() implemented
			if (logicRequiredThisLoop) {
				entityManager.forceLogic();
				logicRequiredThisLoop = false;
			}
			// give the input to the ship to be processed
			((ShipEntity) entityManager.getShip())
					.processInput(inputController);
			
			drawGame();

			pauseAndFPSControl(lastLoopTime);

		} // close while
	}

	// pause and fps control
	private void pauseAndFPSControl(long lastLoopTime) {
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
		if (entityManager.getAlienCount() < 1)
			notifyWin();

		// speed up the remaining aliens by 1/2/3% depending on difficulty
		entityManager.speedUpAlienEntities(this.difficulty);
	}

	public void updateLogic() {
		logicRequiredThisLoop = true;
	}

	private void startGame() {
		entityManager.initEntities();
		inputController.reset();
	}

	public int getDifficulty() {
		return difficulty;
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
			if (waitingForKeyPress) {
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
	
	/* TODO:
	 * - basic powerups - very rare
	 * 	= immune to alienShots, timed
	 * 	= laser: up to the top of the screen, timed
	 * 	= 2 rockets get fired every second
	 * - aliens shoot different bullets
	 * - more weapons: 
	 * 	= random pruska4ka
	 * 	= zalp
	 * 	= burza kurte4nica
	 * 	= bavni raketi AoE
	 * 	= laser
	 * - combos
	 * 	= 10 consecutive kills grant resources
	 * 	= 30 consecutive hits grant laser
	 * - multiple weapons available
	 * 	= use 1,2,3,4 to change weapon
	 * 	= overheating of one weapon forces player to use more weapons
	 * 	= limited ammo too
	 * 	= side UI with 4 vertical bars representing heat levels and ammo
	 * - sfx
	 * 	= yarr!; on powerup pickup
	 * 	= random pirate swears on events
	 * 	= yarr! me cannon is too hot'h!
 	 */

}

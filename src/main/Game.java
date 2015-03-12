package main;

import utility.InputController;
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
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.Font;
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
	private InputController inputController;
	private String message = "titleText";
	private int difficulty = 3; // 3-ez, 2-normal, 1-hard

	public Game() {

		JFrame container = new JFrame("Invade yer space ye scurvy dog!");
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(this.WIDTH, this.HEIGHT)); // *scale
		panel.setLayout(null);

		setBounds(0, 0, this.WIDTH , this.HEIGHT); // *scale
		setLocation(0, 0);
		container.setUndecorated(true);
		panel.add(this);

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
		this.strategy = getBufferStrategy();

		this.inputController = new InputController();
		try {
			ImageManager.initImages();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println(" Initializing images failed!");
		}
		
		entityManager.initEntities();
		SoundManager.initSoundManager();

		// start playing background music
		this.musicManager.loopBackgroundMusic();

	}

	public static void main(String argv[]) {
		(new Game()).gameLoop();
		System.exit(0);
	}

	public void gameLoop() {

		long lastLoopTime = System.currentTimeMillis();
		float minimumFPS = 100.00f;

		while (this.gameRunning) {

			// calculate how far entities should move this loop, based on the
			// time since last update
			this.delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			// System.out.println( "FPS: " + 1000.00f/ this.delta );
			if (minimumFPS > 1000.00f / this.delta) {
				minimumFPS = 1000.00f / this.delta;
			}

			// get a graphics context for the accelerated surface and blank it
			Graphics2D g = (Graphics2D) this.strategy.getDrawGraphics();
			//g.scale(scale, scale);
			g.setColor(Color.black);
			g.fillRect(0, 0, this.WIDTH, this.HEIGHT);
			BackgroundImageManager.drawBackgroundObjects(g);

			// cycle trough entities and move them
			if (!this.waitingForKeyPress) {
				this.entityManager.moveEntities(this.delta);
				BackgroundImageManager.update();
			}

			// draw cycle
			this.entityManager.drawEntities(g);

			// collision detection: ship,shots are checked against the aliens,
			// alienShots
			if (!this.waitingForKeyPress) {
				this.entityManager.collideEntities();
			}

			// remove destroyed entities that are to be removed and clean up
			this.entityManager.removeEntities();

			// if we need to do logic go through alienEntities only, since only
			// aliens have a doLogic() implemented
			if (this.logicRequiredThisLoop) {
				this.entityManager.forceLogic();
				this.logicRequiredThisLoop = false;
			}

			// if game is waiting for "any key press" show message
			if (this.waitingForKeyPress) {
				/*g.setColor(new Color(0, 0, 100));
				g.fillRect(WIDTH/5, HEIGHT*2/5,
						WIDTH*3/5, HEIGHT/5);
				g.setColor(Color.red);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 48));
				g.drawString(this.message,
						(this.WIDTH - g.getFontMetrics().stringWidth(this.message)) / 2,
						this.HEIGHT/2);*/
				g.drawImage(ImageManager.getImage(message),
						(this.WIDTH - ImageManager.getImage(message).getWidth(null))/2,
						(this.HEIGHT - ImageManager.getImage(message).getHeight(null))/2, null);
			}

			// after drawing clean up and flip the buffer
			g.dispose();
			this.strategy.show();

			// give the input to the ship to be processed
			((ShipEntity) this.entityManager.getShip())
					.processInput(this.inputController);

			// pause and fps control
			try {
				Thread.sleep(1000 / this.FPS);

			} catch (InterruptedException e) {
				System.out.println("Main thread.sleep was interrupted!");
			}

		} // close while
			// System.out.println("minimumFPS: " + minimumFPS);
	}

	public void notifyDeath() {
		this.message = "deathText";
		this.waitingForKeyPress = true;
	}

	public void notifyWin() {
		this.message = "winText";
		this.waitingForKeyPress = true;
		entityManager.generateNewEntitiesMap();
	}

	public void notifyAlienKilled() {
		this.entityManager.decrementAlienCount();
		if (this.entityManager.getAlienCount() < 1)
			notifyWin();

		// speed up the remaining aliens by 1/2/3% depending on difficulty
		this.entityManager.speedUpAlienEntities(this.difficulty);
	}

	public void updateLogic() {
		this.logicRequiredThisLoop = true;
	}

	private void startGame() {

		this.entityManager.initEntities();
		this.inputController.reset();
	}

	public int getDifficulty() {
		return this.difficulty;
	}

	public int getGameWidth() {
		return this.WIDTH;
	}

	public int getGameHeight() {
		return this.HEIGHT;
	}

	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	public MusicManager getSoundManager() {
		return this.musicManager;
	}

	private class KeyInputHandler extends KeyAdapter {

		private int pressCount;

		public KeyInputHandler() {
			this.pressCount = 1;
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				Game.this.inputController.setLeftPressed(true);
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				Game.this.inputController.setRightPressed(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				Game.this.inputController.setUpPressed(true);
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				Game.this.inputController.setDownPressed(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				Game.this.inputController.setFirePressed(true);
			}
		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				Game.this.inputController.setLeftPressed(false);
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				Game.this.inputController.setRightPressed(false);
			}
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				Game.this.inputController.setUpPressed(false);
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				Game.this.inputController.setDownPressed(false);
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				Game.this.inputController.setFirePressed(false);
			}
		}

		public void keyTyped(KeyEvent e) {
			if (Game.this.waitingForKeyPress) {
				if (this.pressCount == 1) {
					Game.this.waitingForKeyPress = false;
					startGame();
					this.pressCount = 1;
				} else {
					this.pressCount++;
				}
			}
			if (e.getKeyChar() == 27) { // escape
				Game.this.gameRunning = false;
			}
		}
	}// close KeyInputHandler class
	
	/* TODO:
	 * -animation after playerbullert hits and enemy
	 * -animation frameMod does not afftect anim speed
	 */

}

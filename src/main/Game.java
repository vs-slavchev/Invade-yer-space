package main;

import entities.ShipEntity;
import entities.utility.EntityManager;
import main.states.MainMenu;
import main.states.StateManager;
import main.states.StateManager.States;
import utility.*;
import utility.image.AnimationManager;
import utility.image.BackgroundPlanetManager;
import utility.image.ImageManager;
import utility.sound.MusicManager;
import utility.sound.SoundManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas {

    private final static GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private final static GraphicsDevice device = env.getScreenDevices()[0];
    private static final Rectangle SCREEN_RECTANGLE = device.getDefaultConfiguration().getBounds();
    public static final int SCREEN_WIDTH = SCREEN_RECTANGLE.width;
    public static final int SCREEN_HEIGHT = SCREEN_RECTANGLE.height;
    private static final long serialVersionUID = -4872814357516418820L;
    static int score = 0;
    private static MusicManager musicManager = new MusicManager();
    private static boolean alienHPBarDrawn = false;
    private final int FPS = 90;
    boolean gameRunning = true;
    boolean waitingForKeyPress = false;
    InputController inputController;
    EntityManager entityManager = new EntityManager(this);
    String message = "startText";
    private BufferStrategy strategy;
    private volatile long delta = 1000 / this.FPS;
    private boolean logicRequiredThisLoop = false;
    private MenuKeys menuKeys = new MenuKeys(this);
    private KeyInputHandler keyInputHandler = new KeyInputHandler();
    private BackgroundPlanetManager planetManager = new BackgroundPlanetManager("planets/planet");

    public Game() {
        JFrame container = new JFrame("Invade yer space ye scurvy dog!");
        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        panel.setLayout(null);

        setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        setLocation(0, 0);
        container.setUndecorated(true);
        panel.add(this);
        makeCursorTransparent(container);

        /* tell awt not to repaint the canvas since we do it in accelerated mode */
        setIgnoreRepaint(true);

        container.pack();
        container.setResizable(false);
        container.setVisible(true);
        container.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        requestFocus();
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        addKeyListener(menuKeys);
        inputController = new InputController();
        ImageManager.initImages();

        entityManager.initEntities();
        SoundManager.initSoundManager();
        TutorialManager.initTutorialList();
        MainMenu.initGems();
        musicManager.loopBackgroundMusic(0);
    }

    private void makeCursorTransparent(JFrame container) {
        container.setCursor(container.getToolkit().createCustomCursor(
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(), null));
    }

    public static void main(String argv[]) {
        (new Game()).playGame();
    }

    public static int getScore() {
        return score;
    }

    public static MusicManager getMusicManager() {
        return musicManager;
    }

    public void playGame() {

        long lastLoopTime = System.currentTimeMillis();

        while (gameRunning) {
            delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            if (StateManager.getState() == States.PLAY) {
                if (!waitingForKeyPress) {
                    entityManager.moveEntities(delta);
                    planetManager.update();
                    TutorialManager.updateTutorials();
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
            controlFPS(lastLoopTime);

        }
        SoundManager.close();
    }

    private void controlMusicGain() {
        if (inputController.isMusicDownPressed()) {
            musicManager.modifyGain(-ContentValues.GAIN_PER_TICK_MODIFIER);
        } else if (inputController.isMusicUpPressed()) {
            musicManager.modifyGain(ContentValues.GAIN_PER_TICK_MODIFIER);
        }
        musicManager.update();
    }

    private void controlFPS(final long lastLoopTime) {
        try {
            long sleepTime = lastLoopTime + (1000 / FPS) - System.currentTimeMillis();
            Thread.sleep(sleepTime > 0 ? sleepTime : 1);
        } catch (InterruptedException e) {
            InvadeError.show("sleep was interrupted");
        }
    }

    private void drawGame() {
        // get a graphics context for the accelerated surface and blank it
        Graphics2D g = (Graphics2D) this.strategy.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        if (StateManager.getState() == States.PLAY) {
            planetManager.drawBackgroundObjects(g);
            entityManager.drawEntities(g);
            AnimationManager.drawAnimations(g);

            if (waitingForKeyPress) {
                Image messageImg = ImageManager.getImage("text/" + message);
                g.drawImage(messageImg,
                        (SCREEN_WIDTH - messageImg.getWidth(null)) / 2,
                        (SCREEN_HEIGHT / 2 - messageImg.getHeight(null)) / 2, null);

                entityManager.getShip().getComboManager().drawComboScore(g);

            } else {
                TextBoxManager.drawAllTextBoxes(g);
            }

        } else if (StateManager.getState() == States.MENU) {
            MainMenu.drawMenu(g);
            TextBoxManager.drawSongTextBoxesOnly(g);
        }

        drawAdditionalIcons(g);

        // after drawing clean up and flip the buffer
        g.dispose();
        strategy.show();
    }

    private void drawAdditionalIcons(Graphics2D g) {
        if (inputController.isMusicDownPressed()) {
            drawSpeakerIcon(g, 1);
        } else if (inputController.isMusicUpPressed()) {
            drawSpeakerIcon(g, 0);
        }
    }

    private void drawSpeakerIcon(Graphics2D g, int spriteId) {
        g.drawImage(ImageManager.getImage("effects/speakerUI"),
                SCREEN_WIDTH - 90, 30, SCREEN_WIDTH - 90 + 64, 30 + 64,
                64 * spriteId, 0, 64 * (spriteId + 1), 64, null);
    }

    public void switchToMenuKeyHandler() {
        removeKeyListener(keyInputHandler);
        addKeyListener(menuKeys);
    }

    public void switchToInputKeyHandler() {
        removeKeyListener(menuKeys);
        addKeyListener(keyInputHandler);
    }

    public void notifyDeath() {
        ShipEntity ship = entityManager.getShip();
        // turn autoFire off to prevent shooting into the next game
        ship.setAutoFireOn(false);
        message = "deathText";
        waitingForKeyPress = true;
        SoundManager.play("nooo");
        score += ship.getComboManager().getMaxComboAchieved();
    }

    public void notifyWin() {
        // turn autoFire off to prevent shooting into the next game
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
        boolean allAliensDead = entityManager.getAlienCount() <= 0;
        if (allAliensDead) {
            notifyWin();
        }
        entityManager.speedUpAliens();
    }

    public boolean isAlienHPBarDrawn() {
        return alienHPBarDrawn;
    }

    public static void setAlienHPBarDrawn(boolean value) {
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

    public InputController getInputController() {
        return inputController;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public boolean isWaitingForKeyPress() {
        return waitingForKeyPress;
    }

    public void setWaitingForKeyPress(boolean val) {
        waitingForKeyPress = val;
    }

    private class KeyInputHandler extends KeyAdapter {

        private static final int SPACE_KEY = 32;
        private static final int ENTER_KEY = 10;
        private static final int ESCAPE_KEY = 27;
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
            } else if (e.getKeyCode() == KeyEvent.VK_2) {
                inputController.numberPressed.set(2, true);
            } else if (e.getKeyCode() == KeyEvent.VK_3) {
                inputController.numberPressed.set(3, true);
            } else if (e.getKeyCode() == KeyEvent.VK_4) {
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
            } else if (e.getKeyCode() == KeyEvent.VK_2) {
                inputController.numberPressed.set(2, false);
            } else if (e.getKeyCode() == KeyEvent.VK_3) {
                inputController.numberPressed.set(3, false);
            } else if (e.getKeyCode() == KeyEvent.VK_4) {
                inputController.numberPressed.set(4, false);
            }
        }

        public void keyTyped(KeyEvent e) {
            // keyCodes: 32 = space; 10 = enter
            if (waitingForKeyPress
                    && (e.getKeyChar() == SPACE_KEY || e.getKeyChar() == ENTER_KEY)) {
                if (pressCount == 1) {
                    setWaitingForKeyPress(false);
                    inputController.setFirePressed(false);
                    if (message.equals("deathText")) {
                        score = 0;
                    }
                    startGame();
                    pressCount = 1;
                } else {
                    pressCount++;
                }
            }
            if (e.getKeyChar() == ESCAPE_KEY) {
                escapeToMenu();

            }
        }

        private void escapeToMenu() {
            // turn autoFire off to prevent from shooting in the next game
            entityManager.getShip().setAutoFireOn(false);
            StateManager.setState(States.MENU);
            getMusicManager().loopBackgroundMusic(0);
            switchToMenuKeyHandler();
        }
    }
}
package main.states;

import main.Game;
import main.states.StateManager.States;
import utility.ContentValues;
import utility.InvadeError;
import utility.image.Gem;
import utility.image.ImageManager;
import utility.sound.SoundManager;

import javax.swing.*;
import java.awt.*;


public class MainMenu {
    private static final String[] options = {"play", "quit"};
    private static int currentOption = 0;
    private static Gem[] gems = new Gem[ContentValues.MAX_GEMS];

    public static void goUp() {
        currentOption = currentOption == 0 ? options.length - 1 : currentOption - 1;
    }

    public static void goDown() {
        currentOption = currentOption == options.length - 1 ? 0 : currentOption + 1;
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
                InvadeError.show(choice + " not a valid menu choice!");
                break;
        }
    }

    private static void drawMenuBackground(Graphics2D g) {
        g.drawImage(ImageManager.getImage("text/skyBackground"), 0, 0, null);
        drawGems(g);
        g.drawImage(ImageManager.getImage("text/captainManol"), 0, 0, null);
    }

    public static void initGems() {
        for (int i = 0; i < ContentValues.MAX_GEMS; i++) {
            gems[i] = new Gem();
        }
    }

    private static void drawGems(Graphics2D g) {
        for (Gem gem : gems) {
            g.drawImage(ImageManager.getImage("effects/gems/gem" + gem.type),
                    (int) gem.x, (int) gem.y, null);
        }
    }

    private static void updateGems() {
        for (Gem gem : gems) {
            gem.move();
        }
    }

    public static void drawMenu(Graphics2D g) {
        updateGems();
        drawMenuBackground(g);

        String path = "text/";

        Image titleImg = ImageManager.getImage(path + "titleText");
        g.drawImage(titleImg, Game.SCREEN_WIDTH / 2 - titleImg.getWidth(null) / 2,
                Game.SCREEN_HEIGHT / 9, null);

        int baseX = 300;
        int offset = Game.SCREEN_HEIGHT / 9;
        for (int i = 0; i < options.length; i++) {
            int imgWidth = ImageManager.getImage(path + options[i]).getWidth(null);

            if (i == currentOption) {
                g.drawImage(ImageManager.getImage(path + "selector"),
                        baseX - imgWidth / 2 + 180,
                        Game.SCREEN_HEIGHT * 4 / 10 + i * offset - 5 * i, null);
            }
            g.drawImage(ImageManager.getImage(path + options[i]),
                    baseX - imgWidth / 2,
                    Game.SCREEN_HEIGHT * 4 / 10 + i * offset, null);
        }

        Image creditsImg = ImageManager.getImage(path + "credits");
        int creditsX = Game.SCREEN_WIDTH - creditsImg.getWidth(null);
        int creditsY = Game.SCREEN_HEIGHT - creditsImg.getHeight(null);
        g.drawImage(creditsImg, creditsX, creditsY, null);

        g.setFont(ContentValues.INFO_FONT);
        g.setColor(Color.BLACK);
        g.drawString(ContentValues.GAME_VERSION, 100, Game.SCREEN_HEIGHT - 10);
    }

}

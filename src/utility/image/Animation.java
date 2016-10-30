package utility.image;

import java.awt.*;

/* Some instances of this class can be independent: they are not stored in the AnimationManager.
 * example is the AlienShip animation. It belongs to the alienShip instance and its management is
 * handled by the alienShip.
 */
public class Animation {
    private int x, y;
    private boolean active = true;
    private boolean running = false;
    private boolean doesLoop = false;
    private volatile double currentFrame = 0.0;
    private double frameModifier;
    private int numFrame;
    private String name;
    private double scale;

    public Animation(int x, int y, double frameModifier, int numFrame,
                     String name, boolean doesLoop, boolean running, double scale) {
        this.x = x;
        this.y = y;
        this.frameModifier = frameModifier;
        this.numFrame = numFrame;
        this.name = name;
        this.doesLoop = doesLoop;
        this.scale = scale;
        this.running = running;
    }

    public void update() {
        if (running) {
            currentFrame += frameModifier;

            if (currentFrame >= numFrame) {
                if (doesLoop) {
                    currentFrame = 0.0;
                    running = false;
                } else {
                    active = false;
                }
            }
        }
    }

    public void drawAnimation(Graphics2D g) {
        if (!active) {
            return;
        }
        g.drawImage(ImageManager.getImage(name),
                x, y,
                x + (int) (getWidth() * scale), y + (int) (getHeight() * scale),
                (int) currentFrame * getWidth(), 0,
                (int) (currentFrame + 1) * getWidth(), getHeight(),
                null);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void modifyPosition(int modX, int modY) {
        this.x += modX;
        this.y += modY;
    }

    public int getWidth() {
        return ImageManager.getImage(name).getWidth(null) / numFrame;
    }

    public int getHeight() {
        return ImageManager.getImage(name).getHeight(null);
    }

    public boolean getActive() {
        return active;
    }
}

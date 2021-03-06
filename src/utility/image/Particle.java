package utility.image;

import java.awt.*;

public class Particle {
    private int x, y;
    private int yModifier;
    private Color color;

    public Particle(int x, int y, int positionOffset, Color color, int yModifier) {
        this.x = (int) (x - positionOffset + Math.random() * positionOffset * 2);
        this.y = (int) (y - positionOffset + Math.random() * positionOffset * 2);
        this.color = color;
        this.yModifier = yModifier;
    }

    private void updateParticle() {
        y += yModifier;
    }

    public void drawParticle(Graphics g) {
        updateParticle();
        g.setColor(this.color);
        // java does not support single pixel drawing :(
        g.drawLine(x, y, x, y);
    }
}

package utility.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Particle {
	private int x, y;
	private Color color;
	
	public Particle(int x, int y, int positionOffset, Color color){
		this.x = (int) (x - positionOffset +  Math.random()*positionOffset*2);
		this.y = (int) (y - positionOffset +  Math.random()*positionOffset*2);
		this.color = color;
	}
	
	private void updateParticle(){
		y += 3;
	}
	
	public void drawParticle(Graphics g){
		updateParticle();
		
		g.setColor(this.color);
		// java does not support single pixel drawing :(
		g.drawLine(x, y, x, y);
	}
}

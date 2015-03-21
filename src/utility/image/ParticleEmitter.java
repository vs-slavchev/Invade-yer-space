package utility.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParticleEmitter {
	
	private CopyOnWriteArrayList<Particle> particles = new CopyOnWriteArrayList<>();
	private int numMaxParticles;
	private int particlePositionOffset;
	private int currentIndex;
	
	public ParticleEmitter(int numMaxParticles, int particlePositionOffset){
		this.numMaxParticles = numMaxParticles;
		this.particlePositionOffset = particlePositionOffset;
		this.currentIndex = 0;
	}
	
	public void emittParticle(int x, int y) {
		Color randomColor = new Color((int) (200 + Math.random() * 55),
				(int) (128 + Math.random() * 127), 0);
		
		if (this.particles.size() > this.currentIndex && this.particles.get(this.currentIndex) != null){
			this.particles.remove(this.currentIndex);
			this.particles.add(this.currentIndex, new Particle(x, y, this.particlePositionOffset,
					randomColor, 3));
		} else {
			this.particles.add(new Particle(x, y, this.particlePositionOffset,
					randomColor, 3));
		}
		
		this.currentIndex++;
		if (this.currentIndex >= this.numMaxParticles) {
			this.currentIndex = 0;
		}
	}
	
	public synchronized void drawParticles(Graphics g){
		for (final Iterator<Particle> iterator = this.particles.iterator(); iterator.hasNext(); ) {
			iterator.next().drawParticle(g);
		}
	}

}


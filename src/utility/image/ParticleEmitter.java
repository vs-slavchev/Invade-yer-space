package utility.image;

import java.awt.*;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParticleEmitter {

    private final int numMaxParticles;
    private final int particlePositionOffset;
    private CopyOnWriteArrayList<Particle> particles = new CopyOnWriteArrayList<>();
    private int currentIndex;

    public ParticleEmitter(int numMaxParticles, int particlePositionOffset) {
        this.numMaxParticles = numMaxParticles;
        this.particlePositionOffset = particlePositionOffset;
        this.currentIndex = 0;
    }

    public void emitParticle(final int x, final int y) {
        Color randomColor = new Color((int) (200 + Math.random() * 55),
                (int) (128 + Math.random() * 127), 0);

        Particle newParticle = new Particle(x, y, particlePositionOffset, randomColor, 5);
        boolean reusing = particles.size() > currentIndex && particles.get(currentIndex) != null;
        if (reusing) {
            particles.remove(currentIndex);
            particles.add(currentIndex, newParticle);
        } else {
            particles.add(newParticle);
        }

        this.currentIndex++;
        if (currentIndex >= numMaxParticles) {
            currentIndex = 0;
        }
    }

    public synchronized void drawParticles(Graphics g) {
        for (final Iterator<Particle> iterator = particles.iterator(); iterator.hasNext(); ) {
            iterator.next().drawParticle(g);
        }
    }
}
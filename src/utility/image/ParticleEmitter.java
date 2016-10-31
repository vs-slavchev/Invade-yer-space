package utility.image;

import java.awt.*;

public class ParticleEmitter {

    private final int particlePositionOffset;
    private Particle[] particles;
    private int currentIndex;

    public ParticleEmitter(int numMaxParticles, int particlePositionOffset) {
        particles = new Particle[numMaxParticles];
        this.particlePositionOffset = particlePositionOffset;
        this.currentIndex = 0;
    }

    public void emitParticle(final int x, final int y) {
        // make a color between yellow and red
        int redComponent = (int) (200 + Math.random() * 55);
        int greenComponent = (int) (128 + Math.random() * 127);
        Color randomColor = new Color(redComponent, greenComponent, 0);

        Particle newParticle = new Particle(x, y, particlePositionOffset, randomColor, 5);
        particles[currentIndex] = newParticle;
        this.currentIndex++;
        if (currentIndex >= particles.length) {
            currentIndex = 0;
        }
    }

    public void drawParticles(Graphics g) {
        for (Particle particle : particles) {
            if (particle != null) {
                particle.drawParticle(g);
            } else {
                // if a particle is null then all that follow are null too
                break;
            }
        }
    }
}
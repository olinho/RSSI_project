package model;

import java.util.Random;

/**
 * Created by sbk on 12.03.17.
 */
public class WorldMap {
    private static final double PARTICLE_COVERAGE = 0.1;
    private int maxParticles;
    private int width;
    private  int height;
    private Particle [] particles;

    public WorldMap(World w){
        width = w.getWidth();
        height = w.getHeight();
        maxParticles = width*height;

        resetParticles();
    }

    public void resetParticles(){
        particles = new Particle[(int)(maxParticles*PARTICLE_COVERAGE)];

        for(int i = 0; i < particles.length;i++){
            Random r = new Random();
            int x = r.nextInt(width);
            int y = new Random().nextInt(height);

            Particle p = new Particle(new Location(x,y),1.0/particles.length);
            particles[i] = p;
        }

    }

    public Particle[] getParticles() {
        return particles;
    }

    public void setParticles(Particle[] particles) {
        this.particles = particles;
    }
}

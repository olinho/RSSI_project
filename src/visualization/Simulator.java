package visualization;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import model.Location;
import model.Particle;
import model.Robot;
import model.Telegram;
import model.Tower;
import model.World;

/**
 * Created by sbk on 23.02.17.
 */
public class Simulator {
    private static final double NOISE_RATIO = 0.2;
    private static final long SIMULATION_RATE= 500;
    private PrintWriter out = null;

    private World w;
    private Visualizer v;
    private boolean stop = false;

    public Simulator(World w, Visualizer v) {
        this.w = w;
        this.v = v;
        try {
			out = new PrintWriter(new FileWriter("wyniki_txt.txt", true));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void simulationStep() throws IOException{
        Robot r = w.getRobot();
        Tower t1 = w.getT1();
        Tower t2 = w.getT2();
        Tower t3 = w.getT3();

        Telegram t1Beam  = t1.getRSSIForLocation(r.getRealLocation(),w.getN(),NOISE_RATIO);
        Telegram t2Beam = t2.getRSSIForLocation(r.getRealLocation(),w.getN(),NOISE_RATIO);
        Telegram t3Beam = t3.getRSSIForLocation(r.getRealLocation(),w.getN(),NOISE_RATIO);
        
        Location robotLocation = r.determineLocation(t1Beam,t2Beam,t3Beam);
        r.setCalculatedLocation(robotLocation);
        System.out.println("RobotEstimatedPosition: ("+(int)robotLocation.getX()+","+(int)robotLocation.getY()+")");
//        out.write("RobotEstimatedPosition: ("+(int)robotLocation.getX()+","+(int)robotLocation.getY()+")");
       	
        r.resampleParticles();
        r.weightParticles(t1Beam, t2Beam, t3Beam);
        Particle [] particles = r.getMap().getParticles();
        double meanXFromParticleFiltering = 0.0;
        double meanYFromParticleFiltering = 0.0;
        for (Particle p : particles) {
        	double x = p.getLocation().getX();
        	double y = p.getLocation().getY();
        	double wage = p.getWeight();
        	meanXFromParticleFiltering += x*wage;
        	meanYFromParticleFiltering += y*wage;
        }
        System.out.println("MeanRobotLocation: (" + (int)meanXFromParticleFiltering + ", " + (int)meanYFromParticleFiltering + ")");
//        out.write("MeanRobotLocation: (" + (int)meanXFromParticleFiltering + ", " + (int)meanYFromParticleFiltering + ")");
//        out.flush();
        robotLocation = new Location(meanXFromParticleFiltering, meanYFromParticleFiltering);
        r.setCalculatedLocation(robotLocation);
        
        v.repaint();
    }

    public synchronized Simulator setStop(boolean stop) {
        this.stop = stop;
        return this;
    }

    public synchronized boolean isStop() {
        return stop;
    }

    public void run(){
        while(!isStop()){
            try {
				simulationStep();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            try {
                Thread.sleep(SIMULATION_RATE);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}

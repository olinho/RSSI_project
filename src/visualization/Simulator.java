package visualization;

import model.*;

/**
 * Created by sbk on 23.02.17.
 */
public class Simulator {
    private static final double NOISE_RATIO = 0;
    private static final long SIMULATION_RATE= 500;

    private World w;
    private Visualizer v;
    private boolean stop = false;
    
    private int stepNumber = 0;

    public Simulator(World w, Visualizer v) {
        this.w = w;
        this.v = v;
    }

    public void simulationStep(){
        Robot r = w.getRobot();
        Tower t1 = w.getT1();
        Tower t2 = w.getT2();
        Tower t3 = w.getT3();

//        Telegram t1Beam  = t1.getRSSIForLocation(r.getRealLocation(),w.getN(),NOISE_RATIO);
//        Telegram t2Beam = t2.getRSSIForLocation(r.getRealLocation(),w.getN(),NOISE_RATIO);
//        Telegram t3Beam = t3.getRSSIForLocation(r.getRealLocation(),w.getN(),NOISE_RATIO);
        Telegram t1Beam  = t1.getNextRSSIForLocation(r.getRealLocation(),World.getN(),NOISE_RATIO, stepNumber);
        Telegram t2Beam = t2.getNextRSSIForLocation(r.getRealLocation(),World.getN(),NOISE_RATIO, stepNumber);
        Telegram t3Beam = t3.getNextRSSIForLocation(r.getRealLocation(),World.getN(),NOISE_RATIO, stepNumber);

        Location robotLocation = r.determineLocation(t1Beam,t2Beam,t3Beam);
        System.out.println("RobotPos: (" + robotLocation.getX() + "," + robotLocation.getY() + ")");
        r.setCalculatedLocation(robotLocation);

        v.repaint();
        stepNumber++;
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
            simulationStep();
            try {
                Thread.sleep(SIMULATION_RATE);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}

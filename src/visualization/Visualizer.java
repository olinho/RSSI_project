package visualization;

import exceptions.WorldBuilderException;
import model.*;
import model.Robot;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by sbk on 23.02.17.
 */
public class Visualizer extends JFrame {

    BufferedImage robotIcon = null;
    BufferedImage robotGhostIcon = null;
    BufferedImage towerIcon = null;
    double r1,r2,r3;
    World w;
    private double r1p,r2p,r3p;
    Particle [] oldParticles;


    public Visualizer(World w) throws HeadlessException, IOException {
        robotGhostIcon = ImageIO.read(new File("robotGhostIcon.png"));
        robotIcon = ImageIO.read(new File("robotIcon.png"));
        towerIcon = ImageIO.read(new File("towerIcon.png"));
        this.w = w;
    }

    public void paintRobot(Graphics graphics) {
        Location robotLocation = w.getRobot().getCalculatedLocation();
        graphics.drawImage(robotGhostIcon, (int) w.getRobot().getRealLocation().getX(), (int) w.getRobot().getRealLocation().getY(), this);
        graphics.drawImage(robotIcon, (int)robotLocation.getX(), (int)robotLocation.getY(), this);
    }

    public void paintTowers(Graphics graphics){
        graphics.drawImage(towerIcon, (int) w.getT1().getLocation().getX(),
                (int) w.getT1().getLocation().getY(), this);

        graphics.drawImage(towerIcon, (int) w.getT2().getLocation().getX(),
                (int) w.getT2().getLocation().getY(), this);

        graphics.drawImage(towerIcon, (int) w.getT3().getLocation().getX(),
                (int) w.getT3().getLocation().getY(), this);
    }

    public void paintParticles(Graphics g){
        double mult = 1;
        if(oldParticles != null){
            mult = oldParticles.length;
            ((Graphics2D) g).setColor(this.getBackground());
            for(int i = 0; i < oldParticles.length; i++){
                g.fillRect((int)oldParticles[i].getLocation().getX(),(int)oldParticles[i].getLocation().getY(),1,1);

            }
        }
        ((Graphics2D) g).setColor(Color.GREEN);
        Particle[] p = w.getRobot().getMap().getParticles();
        oldParticles = p;
        
        for(int i = 0; i < p.length; i++){
            g.fillRect((int)p[i].getLocation().getX(),(int)p[i].getLocation().getY(),1,1);

        }
    }

    @Override
    public void paint(Graphics graphics) {
        paintRobot(graphics);
        paintTowers(graphics);
        paintBeams(graphics);
        paintParticles(graphics);
    }

    private synchronized void paintBeams(Graphics graphics) {
        if(r1 == 0 || r2 == 0 || r3 == 0) return;

        Location t1l  = w.getT1().getLocation();
        Location t2l  = w.getT2().getLocation();
        Location t3l  = w.getT3().getLocation();

        Graphics2D g2 = (Graphics2D) graphics;


        double xAdd= towerIcon.getWidth()/2;
        double yAdd= towerIcon.getHeight()/2;

        Color bgcolr = this.getBackground();

        Ellipse2D e1p = new Ellipse2D.Double(t1l.getX()+xAdd-r1p,t1l.getY()+yAdd-r1p,2*r1p,2*r1p);
        Ellipse2D e2p = new Ellipse2D.Double(t2l.getX()+xAdd-r2p,t2l.getY()+yAdd-r2p,2*r2p,2*r2p);
        Ellipse2D e3p = new Ellipse2D.Double(t3l.getX()+xAdd-r3p,t3l.getY()+yAdd-r3p,2*r3p,2*r3p);

        g2.setColor(bgcolr);
        g2.draw(e1p);
        g2.draw(e2p);
        g2.draw(e3p);

        if(r1p != 0) {
            ((Graphics2D) graphics).setColor(Color.BLACK);

            Ellipse2D e1 = new Ellipse2D.Double(t1l.getX() + xAdd - r1, t1l.getY() + yAdd - r1, 2 * r1, 2 * r1);
            Ellipse2D e2 = new Ellipse2D.Double(t2l.getX() + xAdd - r2, t2l.getY() + yAdd - r2, 2 * r2, 2 * r2);
            Ellipse2D e3 = new Ellipse2D.Double(t3l.getX() + xAdd - r3, t3l.getY() + yAdd - r3, 2 * r3, 2 * r3);
            g2.draw(e1);
            g2.draw(e2);
            g2.draw(e3);
        }
        r1p = r1;
        r2p = r2;
        r3p = r3;


    }


    private synchronized void prepareBeams() {
        r1+=3;
        r2+=3;
        r3+=3;
        if(r1 > w.getWidth() && r2 > w.getWidth() && r3 > w.getWidth()) {
            r1 = r2 = r3 = 1;
        }
        repaint();

    }


    public static void main(String [] argv){
        Visualizer dp = null;
        try {
            World world = new World.WorldBuilder()
                    .setHeight(600)
                    .setWidth(800)
                    .setRobot(new Robot(new Location(400,400)))
                    .setT1(new Tower(new Location(10,10),100))
                    .setT2(new Tower(new Location(750,150),100))
                    .setT3(new Tower(new Location(100,500),100))
                    .build();
            world.getRobot().setMap(new WorldMap(world));
            dp = new Visualizer(world);

            dp.setSize(world.getWidth(), world.getHeight());
            dp.setVisible(true);

            Simulator s = new Simulator(world,dp);
            dp.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent we){
                    System.exit(0);
                    s.setStop(true);
                }
            });

            Visualizer finalDp = dp;
            
            Thread painThread = new Thread(){
                @Override
                public void run() {
                    while(true) {
                        finalDp.prepareBeams();
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            painThread.start();

            s.run();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (WorldBuilderException e) {
            e.printStackTrace();
        }

        ;

    }

}

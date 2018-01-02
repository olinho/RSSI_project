package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by sbk on 23.02.17.
 */
public class Robot {

    private Location realLocation;
    private Location calculatedLocation;
    private WorldMap map;
    private static final double MIN_RESAMPLING_NOISE =-10;
    private static final double MAX_RESAMPLING_NOISE = 10;

    public Robot(WorldMap map, Location realLocation) {
        this.map = map;
        this.realLocation = realLocation;
        calculatedLocation = new Location(0,0);
    }

    public Robot(Location realLocation) {
        this.realLocation = realLocation;
        calculatedLocation = new Location(0,0);
    }

    public Location getRealLocation(){
        return realLocation;
    }

    public synchronized Location getCalculatedLocation() {
        return calculatedLocation;
    }

    public synchronized Robot setCalculatedLocation(Location calculatedLocation) {
        this.calculatedLocation = calculatedLocation;
        return this;
    }

    public void resampleParticles(){
        //calculate cumulative sum, and resample with weight 1/length
        double[] cumsum = new double[map.getParticles().length];
        int idx = 0;
        double previous = 0;
        for(Particle p : map.getParticles()){
            cumsum[idx] = previous + p.getWeight();
            previous = cumsum[idx];
            idx++;
        }

        //Nasty fix for the numerical problems
        cumsum[idx-1] = 1.0;
        Random r = new Random();
        Particle [] newParticles = new Particle[map.getParticles().length];

        for(int i = 0; i < map.getParticles().length;i++){
            double val  = r.nextDouble();
            for(int j =0; j < cumsum.length;j++){
                if(val <= cumsum[j]){
                    newParticles[i]  = new Particle(map.getParticles()[j]);
                    Location newLocation = new Location(newParticles[i].location);
                    double resamplingNoiseX = (MIN_RESAMPLING_NOISE + (MAX_RESAMPLING_NOISE-MIN_RESAMPLING_NOISE)*r.nextDouble());
                    double resamplingNoiseY = (MIN_RESAMPLING_NOISE + (MAX_RESAMPLING_NOISE-MIN_RESAMPLING_NOISE)*r.nextDouble());
                    newLocation.setX(newLocation.getX()+resamplingNoiseX);
                    newLocation.setY(newLocation.getY()+resamplingNoiseY);
                    newParticles[i].setLocation(newLocation);
                    break;
                }
            }
        }
        getMap().setParticles(newParticles);

        normalizeWeights();

    }

    public void weightParticles(Telegram tower1Message, Telegram tower2Message, Telegram tower3Message){
        double r1 = Math.pow(10, (tower1Message.getTxPower()-tower1Message.getRssi())/(10*tower1Message.getN()));
        double r2 = Math.pow(10, (tower2Message.getTxPower()-tower2Message.getRssi())/(10*tower2Message.getN()));
        double r3 = Math.pow(10, (tower3Message.getTxPower()-tower3Message.getRssi())/(10*tower3Message.getN()));

        for(Particle p : map.getParticles()){
            //calculate distance;
            double d1 = tower1Message.getTowerLocation().euclideanDistanceTo(p.getLocation());
            double d2 = tower2Message.getTowerLocation().euclideanDistanceTo(p.getLocation());
            double d3 = tower3Message.getTowerLocation().euclideanDistanceTo(p.getLocation());

            double weight =gaussian(d1,r1,10)*gaussian(d2,r2,10)*gaussian(d3,r3,10);
            if(Double.isNaN(weight)){
                    weight = Double.MIN_VALUE;
            }
            p.setWeight(weight);
        }

        normalizeWeights();


    }

    public void normalizeWeights(){
        double sum = 0;
        for(Particle p: map.getParticles()){
            sum += p.getWeight();
        }

        for(Particle p: map.getParticles()){
            double normWeight = p.getWeight()/sum;
            if(Double.isNaN(normWeight)){
                normWeight = Double.MIN_NORMAL;
            }
            p.setWeight(normWeight);
        }
    }

    public void resetParticles(){
        map.resetParticles();
    }

    private double gaussian(double x, double mu, double sdv){
        return 1/(sdv*Math.sqrt(2*Math.PI))*Math.exp(-(x-mu)*(x-mu)/(2*sdv*sdv));
    }



    public synchronized Location determineLocation(Telegram tower1Message, Telegram tower2Message, Telegram tower3Message){
        ////////////////////////////// YOUR CODE HERE ////////////////////////////////////////

    	double A1,A2,A3,n1,n2,n3,RSSI1,RSSI2,RSSI3,r1,r2,r3,x,y,w1,w2,a,b,c,d;
    	A1 = tower1Message.getTxPower();
    	A2 = tower2Message.getTxPower();
    	A3 = tower3Message.getTxPower();
    	n1 = tower1Message.getN();
    	n2 = tower2Message.getN();
    	n3 = tower3Message.getN();
    	RSSI1 = tower1Message.getRssi();
    	RSSI2 = tower2Message.getRssi();
    	RSSI3 = tower3Message.getRssi();
    	r1 = Math.pow(10, (A1 - RSSI1) / (10*n1));
    	r2 = Math.pow(10, (A2 - RSSI2) / (10*n2));
    	r3 = Math.pow(10, (A3 - RSSI3) / (10*n3));
    	a = tower2Message.getTowerLocation().getX();
    	b = tower2Message.getTowerLocation().getY();
    	c = tower3Message.getTowerLocation().getX();
    	d = tower3Message.getTowerLocation().getY();
    	x = -1;
    	y = -1;
    	w1 = (r2*r2 - r3*r3 + c*c - a*a + d*d - b*b) / (2*(d-b));
    	w2 = (a-c) / (d-b);
    	
    	List<Double> results = getSquareFunctionSollutions((1 + w2*w2), (-2*w1), (w1*w1 - w2*w2*r1*r1));
    	if (results.size() > 0) {
        	y = getSquareFunctionSollutions((1 + w2*w2), (-2*w1), (w1*w1 - w2*w2*r1*r1)).get(0);
        	x = Math.sqrt(r1*r1 - y*y);
    	}
    	else {
    		x = 0;
    		y = 0;
    	}
    	
        return new Location(x,y);


        /////////////////////////////////////////////////////////////////////////////////////
    }
    
    public List<Double> getPositiveSollutionForSquareFunction(List<Double> input) {
    	List<Double> result = new ArrayList<>();
    	if (input.size() == 2) {
    		double x1 = input.get(0);
    		double x2 = input.get(1);
    		if (x1 > 0 && x2 < 0) {
    			result.add(x1);
    		}
    		else if (x2 > 0 && x1 < 0) {
    			result.add(x2);
    		}
    		else {
    			return input;
    		}
    	}
    	return result;
    }
    
    public List<Double> getSquareFunctionSollutions(double a, double b, double c){
    	List<Double> result = new ArrayList<>();
    	try {
    	
	    	double delta  = b*b - 4*a*c;
	    	if (delta > 0) {
		    	double x1 = (-b + Math.sqrt(delta))/(2*a);
		    	double x2 = (-b - Math.sqrt(delta))/(2*a);
		    	result.add(x1);
		    	result.add(x2);
	    	}
	    	else if (delta == 0) {
	    		double x = (-b)/(2*a);
	    		result.add(x);
	    	}
    	} catch(Exception e) {
    		System.out.println(e.toString());
    	}
    	return result;
    }


    public WorldMap getMap() {
        return map;
    }

    public Robot setMap(WorldMap map) {
        this.map = map;
        return this;
    }
}

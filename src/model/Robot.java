package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbk on 23.02.17.
 */
public class Robot {
    private Location realLocation;
    private Location calculatedLocation;

    public Robot(Location realLocation) {
        this.realLocation = realLocation;
        calculatedLocation = new Location(0,0);
    }

    public Location getRealLocation(){
        return realLocation;
    }

    public Location getCalculatedLocation() {
        return calculatedLocation;
    }

    public Robot setCalculatedLocation(Location calculatedLocation) {
        this.calculatedLocation = calculatedLocation;
        return this;
    }

    public Location determineLocation2(Telegram tower1Message, Telegram tower2Message, Telegram tower3Message){
    	
    	// r1
    	double A = tower1Message.getTxPower();
    	double n = tower1Message.getN();
    	double RSSI = tower1Message.getRssi();
    	double d = Math.pow(10, (A - RSSI) / (10*n)); 
    	System.out.println(d);
    	double r1 = d;
    	
    	// r2
    	A = tower2Message.getTxPower();
    	n = tower2Message.getN();
    	RSSI = tower2Message.getRssi();
    	d = Math.pow(10, (A - RSSI) / (10*n));
    	double r2 = d;
    	
    	double p = tower2Message.getTowerLocation().getX();
    	double x = (r1*r1 - r2*r2 + p*p) / (2*p);
    	
    	double q = tower3Message.getTowerLocation().getX();
    	double t = tower3Message.getTowerLocation().getY();
    	
    	double y = Math.sqrt(r1*r1 - Math.pow((r1*r1 - r2*r2 + p*p)/(2*p) , 2));
    	
    	// r3
    	A = tower3Message.getTxPower();
    	n = tower3Message.getN();
    	RSSI = tower3Message.getRssi();
    	d = Math.pow(10, (A - RSSI) / (10*n));
    	double r3 = d;
    	
    	System.out.println("r1="+r1 + "; r2="+r2 + "; r3="+r3);
//    	System.out.println(getSquareFunctionSollutions(3, -2, 1).get(0));
//    	System.out.println(getSquareFunctionSollutions(3, -2, 1).get(1));
    	
        return new Location(x,y);
    }
    
    public Location determineLocation(Telegram tower1Message, Telegram tower2Message, Telegram tower3Message){
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
    	System.out.println("R1: " + r1);
    	r2 = Math.pow(10, (A2 - RSSI2) / (10*n2));
    	System.out.println("R2: " + r2);
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
    		System.out.println(getSquareFunctionSollutions((1 + w2*w2), (-2*w1), (w1*w1 - w2*w2*r1*r1)).get(0));
        	y = getSquareFunctionSollutions((1 + w2*w2), (-2*w1), (w1*w1 - w2*w2*r1*r1)).get(0);
        	x = Math.sqrt(r1*r1 - y*y);
    	}
    	else {
    		x = 0;
    		y = 0;
    	}
    		
    	
    	
        return new Location(x,y);
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

}

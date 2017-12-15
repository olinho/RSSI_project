package model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import reader.DataReader;
import reader.Measurement;

/**
 * Created by sbk on 23.02.17.
 */
public class Tower {
    private Location location;
    private double txPower;
    private Telegram lastBeam;
    private final String towerId; 
    private List<Measurement> measurements = new ArrayList<>();
    
    public Tower(Location location, double txPower, String towerId, List<Measurement> measurements) throws FileNotFoundException {
        this.location = location;
        this.txPower = txPower;
        this.towerId = towerId;
        this.measurements = measurements;
    }
    

    public String getTowerId() {
    	return towerId;
    }
    
    public Location getLocation() {
        return location;
    }

    public Telegram getLastBeam() {
        return lastBeam;
    }

    public double getTxPower() {
        return txPower;
    }

    public Telegram getRSSIForLocation(Location destination, double n, double noiseRatio, int stepNumber){
        double distance = destination.euclideanDistanceTo(location);
        double rssiValue = txPower-10*n*Math.log10(distance);

        Random r = new Random();
        double rangeMin = -noiseRatio*rssiValue;
        double rangeMax = -rangeMin;
        double noise = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        rssiValue += noise;

        lastBeam = new Telegram(rssiValue,txPower,n, getLocation());
        return lastBeam;
    }
    
    public Telegram getNextRSSIForLocation(Location destination, double n, double noiseRatio, int stepNumber){
        double distance = destination.euclideanDistanceTo(location);
        System.out.println("DIstance: " + distance);
        double rssiValue = txPower-10*n*Math.log10(distance);
        Measurement measurement = measurements.get(stepNumber);
        measurement.show();
        rssiValue = measurement.getRssi();
        System.out.println("Rssi tower" + rssiValue);
        System.out.println(measurement.getRssi());
        
        Random r = new Random();
        double rangeMin = -noiseRatio*rssiValue;
        double rangeMax = -rangeMin;
        double noise = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        rssiValue += noise;

        lastBeam = new Telegram(rssiValue,txPower,n, getLocation());
        return lastBeam;
    }



}

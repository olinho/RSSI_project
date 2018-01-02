package model;

import java.util.Random;

/**
 * Created by sbk on 23.02.17.
 */
public class Tower {
    private Location location;
    private double txPower;
    private Telegram lastBeam;

    public Tower(Location location, double txPower) {
        this.location = location;
        this.txPower = txPower;
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

    public Telegram getRSSIForLocation(Location destination, double n, double noiseRatio){
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



}

package model;


import model.Location;

/**
 * Created by sbk on 23.02.17.
 */
public class Telegram {
    private double rssi;
    private double txPower;
    private double n;
    private Location towerLocation;

    public Telegram(double rssi, double txPower, double n, Location towerLocation) {
        this.rssi = rssi;
        this.txPower = txPower;
        this.n = n;
        this.towerLocation = towerLocation;
    }

    public double getRssi() {
        return rssi;
    }

    public double getTxPower() {
        return txPower;
    }

    public double getN() {
        return n;
    }

    public Location getTowerLocation() {
        return towerLocation;
    }
}

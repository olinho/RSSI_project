package model;

/**
 * Created by sbk on 23.02.17.
 */
public class Location {
    double x, y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Location(Location location) {
        this.x = location.x;
        this.y = location.y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }



    public double euclideanDistanceTo(Location other){
        return Math.sqrt(Math.pow((getX()-other.getX()),2)+Math.pow((getY()-other.getY()),2));
    }
}

package Graphing;

public class Coordinates {
    private double x, y;


    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public Coordinates(Double[] ar) {
        this.x = ar[0];
        this.y = ar[1];
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
}

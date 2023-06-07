package Graphing;

import ML_Model.LinearRegression;
import ML_Model.Matrix;

import java.util.ArrayList;


public class ML_Model {

    private Matrix matrix;
    private ArrayList<Double> x, y;

    private LinearRegression linearRegression;

    public ML_Model(){
        this.matrix = new Matrix(2,  5);
        this.x = new ArrayList<>();
        this.y = new ArrayList<>();
    }


    public void setMatrix(Matrix m){
        this.matrix = m;
    }

    private void fitMatrix(){
        this.matrix.addRow(0, this.x);
        this.matrix.addRow(1, this.y);
        this.linearRegression = new LinearRegression(this.matrix);
    }


    public void addX(Double x){
        this.x.add(x);
    }

    public void addY(Double y){
        this.y.add(y);
    }


    public void fit(){
        fitMatrix();
        this.linearRegression.fit();
    }

    public String getEquation(){
        return linearRegression.getM() + " * x + " + linearRegression.getB();
    }

    @Override
    public String toString() {
        return "ML_Model{" +
                "matrix=" + matrix +
                ", x=" + x +
                ", y=" + y +
                ", linearRegression=" + linearRegression +
                '}';
    }
}

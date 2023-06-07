package Graphing;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

/***

 Grapher Class: Only draws the given function on the canvas
 The purpose of thread for this class is to draw a function with individual core so that main core wont have to do much work
 this thread works on its own pace and completes the task and dies

 * */
public class Grapher extends Thread {

    private final double center_x;
    private final double center_y;
    private final double box_width;
    private final GraphicsContext gc;
    private final Function function;
    private final boolean erase;
    private final Integer[] col;
    private Thread grapherThread;

    Grapher(GraphicsContext gc, Function f, boolean erase, double center_x, double center_y, double box_width){
        this.center_x = center_x;
        this.center_y = center_y;
        this.box_width = box_width;
        this.function = f;
        this.erase = erase;
        this.gc = gc;
        this.col = new Integer[]{getRandomColor(),getRandomColor(),getRandomColor()};
        this.start();
    }


    public void drawFunction() throws Exception{
        // F(x) = x**2
        // f(2) = 4 => (2, 4)
        // f(0) = 0 @ center_x, center_y

        if(!this.isErase()){
            gc.setLineWidth(3);
            gc.setStroke(Color.rgb(this.col[0], this.col[1], this.col[2]));
        }else{
            gc.setLineWidth(3.5);
            gc.setStroke(Color.WHITE);
        }

        double step = 0.05;
        double x = -10;
        for(; x <= 10; x+=step){
            Double[] f_y = myCustom(x, function);  // the current function
            Double[] f_y2 = myCustom(x+step, function); // the next function
            gc.strokeLine(f_y[0], f_y[1], f_y2[0] , f_y2[1]);
        }

        if(isErase() && x >= 10){
            GraphController.grid.refreshGraph();
        }
    }


    private int getRandomColor(){

        Random random = new Random();

        // create a big random number - maximum is ffffff (hex) = 16777215 (dez)

        return  random.nextInt(225);
    }

    private Double[] myCustom(double x, Function function) throws Exception{
        return new Double[]{center_x + (box_width*x), center_y - (box_width*function.solve(x).doubleValue())};
    }


    public boolean isErase() {
        return erase;
    }

    public void run() {
        try {
            drawFunction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(){
        if(grapherThread == null){
            grapherThread = new Thread(this, "Grapher");
            grapherThread.start();
        }
    }

}

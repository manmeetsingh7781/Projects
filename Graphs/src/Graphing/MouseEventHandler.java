package Graphing;

import javafx.scene.canvas.GraphicsContext;

public class MouseEventHandler extends Thread{

    private Thread mouseThread;
    private GraphicsContext gc;
    private Double c_x, c_y;

    public MouseEventHandler(GraphicsContext graphicsContext, double center_x, double center_y){
        this.gc =  graphicsContext;
        this.c_x = center_x;
        this.c_y = center_y;
    }

    public void run(){
        stop();
    }



    public void start(){
        if(mouseThread == null){
            mouseThread = new Thread(this, "MouseEventHandler");
            mouseThread.start();
        }
    }


}

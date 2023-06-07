package Graphing;



import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;

import java.util.LinkedList;


// All of the drawing
public class Grid extends Thread {
    private final GraphicsContext gc;
    private double spacer = 0;
    private final double box_width = 40;
    private final double step = 2;
    private final double center_x;
    private final double center_y;
    private final double width;
    private final double height;
    private final MouseEventHandler mouseEventHandler;
    private Thread gridThread;

    // points to plot => the coordinates based on the graph system
    // origianl_points => the coordinates that are originally passed values
    private final LinkedList<Double[]> ploints_to_plot;



    public Grid(GraphicsContext graphicsContext) {
        this.gc = graphicsContext;
        width = graphicsContext.getCanvas().getWidth();
        height = graphicsContext.getCanvas().getHeight();
        center_x = width / 2;
        center_y = height / 2;

        mouseEventHandler = new MouseEventHandler(gc, center_x, center_y);
        mouseEventHandler.start();

        gc.setFontSmoothingType(FontSmoothingType.LCD);
        gc.setFont(new Font(12));


        this.ploints_to_plot = new LinkedList<>();

        this.start();

    }


    private void drawGrid() {


        spacer = 0;

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        // draw the grid
        for (int i = 0; i < width; i++) {
            gc.strokeLine(spacer, 0, spacer, height);
            gc.strokeLine(0, spacer, width, spacer);
            spacer += box_width;
        }

        this.drawAxes();

    }

    private void drawAxes() {
        spacer = 0;
        int radius = 6;
        gc.setFill(Color.BLACK);

        for (double i = 0; i < width / box_width; i++) {

            if (i % step == 0) {

                // the axis
                gc.fillOval(spacer - (radius >> 1), center_y - (radius >> 1), radius, radius);

                gc.fillOval(center_x - (radius >> 1), spacer - (radius >> 1), radius, radius);

                if (i > 0) {

                    // cover the bound so that text does not go outside the boundaries

                    // x-axis
                    gc.fillText((int) i + "", (spacer + center_x) - 5, center_y + box_width / 2);

                    // y-axis
                    gc.fillText((int) i + "", center_x + box_width / 2 - 10, center_y - spacer + 5);

                    // -y - axis
                    gc.fillText("-" + (int) i, center_x + box_width / 2 - 10, center_y + spacer + 5);

                    // -x-axis
                    gc.fillText("-" + (int) ((width / box_width) - i), ((spacer) - center_x) - 5, center_y + box_width / 2);

                }
            }
            spacer += box_width;
        }

    }



    // plots point with respect to origin
    public void plotPoint(double x, double y) {
        gc.setFill(Color.BLUE);

        x = (center_x + (box_width * x));
        y = (center_y - (box_width * y));

        this.ploints_to_plot.add(new Double[]{x, y});

        // the calculations are respect to size of canvas
        gc.fillOval(x-5, y-5, 10, 10);
    }


    // plots points with respect to mouse coordinates
    public Double[] plotPointWRMouse(double x, double y){
        gc.setFill(Color.BLUE);

        this.ploints_to_plot.add(new Double[]{x, y});

        gc.fillOval(x - 5, y - 5, 10, 10);
        return new Double[]{-(center_x-x)/(box_width), (center_y-y)/(box_width)};
    }


    // erase the plotted points
    public void erasePlottedPoints(){
        while(!ploints_to_plot.isEmpty()){
            gc.setFill(Color.WHITE);
            gc.fillOval(ploints_to_plot.get(0)[0] - 5.5, ploints_to_plot.get(0)[1] - 5.5, 10.5, 10.5);
            ploints_to_plot.remove(0);
        }
        refreshGraph();
    }


    public void refreshGraph() {
        // sync this so that other threads wait till the graph redraws
        synchronized (this) {
            drawGrid();
        }
    }

    public double getBox_width() {
        return box_width;
    }

    public double getCenter_x() {
        return center_x;
    }

    public double getCenter_y() {
        return center_y;
    }

    private void print(Object... vars) {
        for (Object s : vars) {
            System.out.print(s + " ");
        }
    }


    public void run() {
        refreshGraph();
    }

    public void start() {
        if (gridThread == null) {
            gridThread = new Thread(this, "Grid");
            gridThread.start();
        }
    }
}

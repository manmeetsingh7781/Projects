import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

import java.util.HashMap;


public class MyMarker extends SimplePointMarker{

    private String title;
    private float title_width, radius;
    private double magnitude;

    public MyMarker(Location location, String title, float text_w, float radius, double magnitude){
        super(location);
        this.title = title;
        this.title_width = text_w;
        this.magnitude = magnitude;
        this.radius = radius;
    }


    public void draw(PGraphics pg, float x, float y) {
        pg.pushStyle();
        pg.fill(0);
        pg.text(this.title, x - this.title_width / 2 , y);
        pg.noStroke();

        pg.ellipse(x, y, 30 + this.radius , 30 + this.radius);
        pg.popStyle();
    }



}

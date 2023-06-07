
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;

import processing.core.PApplet;


import java.util.ArrayList;
import java.util.List;

public class MyEarthQuackData  extends PApplet{

    // map variable
    private UnfoldingMap map;
    private int zoomLevel = 10;

    // API variables
    private final ApiDATA data = new ApiDATA();
    private final MyCoords my_info = new MyCoords();




    // client variables
    private Location client;
    private SimplePointMarker client_marker;
    private String client_address, client_lat, client_lon;

    private List<SimplePointMarker> myMarker;
    List<Marker> country_Markers;

    public void setup(){
        myMarker = new ArrayList<SimplePointMarker>();

        // you can use provide which will be last argument in map but lets for now go with default one


        size(800, 800);
        fill(255, 0, 0);
        rect(0, 0, 50, 50);


        map = new UnfoldingMap(this, 200, 50, 700, 500);
        // get the data from url
        data.init();  // this method gets the data from earthquack
        my_info.init(); // this method gets the data from client


        // this block will set the map center at clients location
        // get the clients location and make an location object

        client = new Location(my_info.getLatitude(), my_info.getLongitude());
        client_address = "My location: " + my_info.getCity() + ", " + my_info.getState() + ", " + my_info.getCountry() + ", zip: " + my_info.getZipcode();
        client_lat = "My latitude: " + my_info.getLatitude();
        client_lon = "My longitude: " + my_info.getLongitude();


        map.zoomAndPanTo(zoomLevel, client);


        // init the clients market on the map
        client_marker = new SimplePointMarker(client);
        client_marker.setColor(color(48, 124, 255));
        client_marker.setStrokeColor(color(48, 124, 255));
        map.addMarker(client_marker);
        
        // let's draw all the markers of earth quack
        for(int i =  0; i < data.getData().size(); i++) {

            // magnitude will be writen on the circle of impacted area
            double magnitude = Double.parseDouble(data.getData().get(i).get("magnitude").toString());
            SimplePointMarker m = new SimplePointMarker();

            // 3 or lower are yellow
            if(magnitude < 4){
                // light yellow color
                m.setColor(color(126, 94, 255, 100));
                // above 3 and below or equal 5 are little red
            }else if(magnitude >= 4 && magnitude <= 5){
                // little red
                m.setColor(color(255, 196, 79, 100));
                // above 5 are red
            }else if(magnitude > 5 ){
                // dark red
                m.setColor(color(255, 0, 0,  100));
            }

            Location each_location =   new Location(Double.parseDouble(data.getData().get(i).get("latitude").toString()),
                    Double.parseDouble(data.getData().get(i).get("longitude").toString()));

            if(magnitude >= 4)
            m.setRadius(Float.parseFloat(data.getData().get(i).get("radius").toString()) + 30);

            Feature f = new PointFeature(each_location);
            f.setProperties(data.getData().get(i));
            m.setLocation(each_location);
            m.setProperties(f.getProperties());

            myMarker.add(m);

//            myMarker.add(new MyMarker(f, each_location,magnitude,textWidth(magnitude), r, Double.parseDouble(data.getData().get(i).get("magnitude").toString())));


        }


//        List<Feature> countries = GeoJSONReader.loadData(this, "data/countries.geo.json");
//        country_Markers = MapUtils.createSimpleMarkers(countries);

//        shadeCountry();

//        map.addMarkers(country_Markers);

        // attach the event handler to the map
        MapUtils.createDefaultEventDispatcher(this, map);

    }

    private void shadeCountry(){
        for(Marker marker: country_Markers){
            String id = marker.getId();

            if(id.equals("USA")){
                marker.setColor(color(0, 150,0, 100));
            }
        }
    }

    public void draw(){
        // lets draw the map here
        // we will not render the data every frame instead use can restart the application which is for more efficiency


        map.draw();

        // the outer box
        fill(0, 0, 0, 20);
        stroke(255, 0, 0, 100);
        rect(0, height/4, 200, height/4);

        // below 4.0 magnitude symbol and text
        fill(126, 94, 255);
        stroke(0);
        ellipse(20, height/4 + 20, 20, 20);

        text("Below 4.0 magnitude", 40, height/4 + 25);


        // 4.0+ magnitude symbol and text
        fill(255, 196, 79);
        stroke(0);
        ellipse(20, height/4 + 50, 20, 20);

        text("4.0+ magnitude", 40, height/4 + 55);

        // 5.0+ magnitude symbol and text
        fill(255, 0, 0);
        stroke(0);
        ellipse(20, height/4 + 80, 20, 20);


        text("5.0+ magnitude", 40, height/4 + 85);

        fill(0);
        text("Updates every minute", 5, height/4 + 115);
        text("Today's Data", 5, height/4 + 135);




        // this block prints the client information on top of the map
        fill(0);
        text(client_address, 10, 20);
        text(client_lat, 10, 40);
        text(client_lon, 10, 60);
        for(SimplePointMarker m: myMarker){
            map.addMarker(m);
        }
    }


    public static void main(String[] args) {
        PApplet.main(new String[] {"MyEarthQuackData"});
    }
}


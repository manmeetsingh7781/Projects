import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.text.DecimalFormat;


public class ApiDATA{

    // this url gets the data from all day and updates every minute
    // if you want to do for all weak then use this /summary/all_week.geojson
    private final String api_url = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson";
    private ArrayList<HashMap<String, Object >> data;
    private final DecimalFormat format = new DecimalFormat("##.00");

    public ApiDATA(){
        data  = new ArrayList<>();
    }



    // initialize the variable from the API and assigns the variables
    // must call this function after creating an object
    public void init(){

        // get json from api is a class that has a function which takes the api url and sends back the jsonobject
        JsonObject jsonObject = new GetJsonFromAPI().getJsonObject(api_url);


        JsonArray obj = jsonObject.get("features").getAsJsonArray();
        for(JsonElement e: obj) {
            HashMap<String, Object> each = new HashMap<>();

            JsonObject each_obj = e.getAsJsonObject();
            double[] geometry = {each_obj.get("geometry").getAsJsonObject().get("coordinates").getAsJsonArray().get(0).getAsDouble(),
                    each_obj.get("geometry").getAsJsonObject().get("coordinates").getAsJsonArray().get(1).getAsDouble()};

            JsonObject properties = each_obj.get("properties").getAsJsonObject();

            String radius;
            try {
                radius = properties.get("dmin").getAsString();
            } catch (Exception err) {
                radius = "1";
            }

            double mag = Double.parseDouble(format.format(BigDecimal.valueOf(properties.get("mag").getAsDouble())));
            if (mag > 1){
            each.put("magnitude", "" + mag);
            each.put("latitude", "" + geometry[1]);
            each.put("longitude", "" + geometry[0]);

            each.put("radius", radius);
            each.put("title", properties.get("title").getAsString());
            each.put("place", properties.get("place").getAsString());
            each.put("time", new Timestamp(TimeUnit.MICROSECONDS.convert(properties.get("time").getAsLong(), TimeUnit.MICROSECONDS)).toString());

            data.add(each);
        }

        }

    }

    public ArrayList<HashMap<String, Object>> getData() {
        return data;
    }
}

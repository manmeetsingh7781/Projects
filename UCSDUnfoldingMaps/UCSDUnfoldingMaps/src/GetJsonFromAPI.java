import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GetJsonFromAPI {


    private URL url;
    private URLConnection request;


     public JsonObject getJsonObject(String api){
         try{
             url = new URL(api);
             request = url.openConnection();
             request.connect();
         }catch (Exception e){
             System.out.println("Error retrieving data from API");
         }

         JsonParser json = new JsonParser();
         JsonElement jsonElement = null;
         JsonObject jsonObject = null;
         try {
             jsonElement = json.parse(new InputStreamReader( (InputStream) request.getContent()));

             jsonObject = jsonElement.getAsJsonObject();
         } catch (IOException e) {
             System.out.println("Invalid to parse Json");
         }

         return jsonObject;

     }


}

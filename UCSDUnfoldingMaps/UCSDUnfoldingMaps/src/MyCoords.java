import com.google.gson.JsonObject;


public class MyCoords {

    private double latitude, longitude;
    private String city, state, country, zipcode;


    // this function must run after creating an object
    public void init(){
        String api = "http://api.ipstack.com/check?access_key=d67ee5541d13b5421c93720c09da64a1";
        JsonObject jsonObject = new GetJsonFromAPI().getJsonObject(api);

        latitude = jsonObject.get("latitude").getAsDouble();
        longitude = jsonObject.get("longitude").getAsDouble();

        city = jsonObject.get("city").getAsString();
        state = jsonObject.get("region_name").getAsString();
        country = jsonObject.get("country_name").getAsString();
        zipcode = jsonObject.get("zip").getAsString();

    }

    public static void main(String[] args) {
        new MyCoords().init();
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getZipcode() {
        return zipcode;
    }
}

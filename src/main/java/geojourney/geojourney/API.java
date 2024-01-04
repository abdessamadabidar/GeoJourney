package geojourney.geojourney;

import javafx.scene.image.Image;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class API {
    protected static final String KEY = System.getenv("GOOGLE_MAPS_API_KEY");
    private static final double LATITUDE = 35.173867226238855;
    private static final double LONGITUDE = -3.862133730680905;
    private static final int RADIUS = 15000;

    public ArrayList<String> getPlacesId(String type) throws IOException {
        String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + LATITUDE + "%2C" + LONGITUDE + "&radius=" + RADIUS + "&type=" + type + "&key=" + KEY;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(URL);
            request.addHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                System.out.println(response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());   // 200
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String stringResult = EntityUtils.toString(entity);
                    JSONObject result =  (JSONObject) new JSONParser().parse(stringResult);
                    JSONArray arrayResult =  (JSONArray) result.get("results");
                    ArrayList<String> places_id = new ArrayList<>();
                    for (int i=0 ; i<arrayResult.size(); ++i) {
                        JSONObject JSONItem = (JSONObject) arrayResult.get(i);
                        places_id.add((String) JSONItem.get("place_id"));
                    }

                    return places_id;

                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }


    public ArrayList<Place> getPlacesDetails(ArrayList<String> placesId) {
        ArrayList<Place> places  = new ArrayList<>();
        for (String placeId : placesId) {
            String URL = "https://maps.googleapis.com/maps/api/place/details/json?fields=name%2Copening_hours/open_now%2Cformatted_address%2Cformatted_phone_number%2Cgeometry/location%2Cuser_ratings_total%2Crating&place_id=" + placeId + "&key=";
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(URL + KEY);
                request.addHeader("Content-Type", "application/json");
                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String stringResult = EntityUtils.toString(entity);
                        JSONObject jsonResult = (JSONObject) new JSONParser().parse(stringResult);
                        JSONObject JSONPlace = (JSONObject) jsonResult.get("result");
                        String address = (String) JSONPlace.get("formatted_address");
                        String phoneNumber = (String) JSONPlace.get("formatted_phone_number");
                        String name = (String) JSONPlace.get("name");
                        double rating = (JSONPlace.get("rating") instanceof Double) ? (double) JSONPlace.get("rating") : 0.0;
                        JSONObject coordinates = (JSONObject) ((JSONObject) (JSONPlace.get("geometry"))).get("location");
                        double latitude = (double) coordinates.get("lat");
                        double longitude = (double) coordinates.get("lng");
                        Boolean isOpenNow = (JSONPlace.get("opening_hours") == null) ? null : (boolean) ((JSONObject)(JSONPlace.get("opening_hours"))).get("open_now");
                        long totalRating = (JSONPlace.get("rating") != null ) ? (Long) JSONPlace.get("user_ratings_total") : 0;
                        int tr = Integer.parseInt(String.valueOf(totalRating).replace(".0", ""));

                        places.add(new Place(name, address, latitude, longitude, rating, isOpenNow, phoneNumber, tr));

                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        return places;

    }


}




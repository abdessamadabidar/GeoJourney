package geojourney.geojourney;

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

import java.io.IOException;
import java.util.ArrayList;


public class RestaurantsAPI {


    private final String KEY = System.getenv("GOOGLE_MAPS_API_KEY");


    public ArrayList<String> getRestaurantsID() throws IOException {
        String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=35.170446999175276%2C-3.861099048903415&radius=5000&type=restaurant&key=";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(URL + KEY);
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


    public ArrayList<Restaurant> getRestaurantsDetails(ArrayList<String> restaurantsID) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        for (String restaurantID : restaurantsID) {
            String URL = "https://maps.googleapis.com/maps/api/place/details/json?fields=name%2Copening_hours/open_now%2Cformatted_address%2Cformatted_phone_number%2Cgeometry/location%2Cuser_ratings_total%2Crating&place_id=" + restaurantID + "&key=";
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(URL + KEY);
                request.addHeader("Content-Type", "application/json");
                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String stringResult = EntityUtils.toString(entity);
                        JSONObject jsonResult = (JSONObject) new JSONParser().parse(stringResult);
                        JSONObject JSONRestaurant = (JSONObject) jsonResult.get("result");
                        String address = (String) JSONRestaurant.get("formatted_address");
                        String phoneNumber = (String) JSONRestaurant.get("formatted_phone_number");
                        String name = (String) JSONRestaurant.get("name");
                        double rating = (JSONRestaurant.get("rating") instanceof Double) ? (double) JSONRestaurant.get("rating") : 0.0;
                        JSONObject coordinates = (JSONObject) ((JSONObject) (JSONRestaurant.get("geometry"))).get("location");
                        double latitude = (double) coordinates.get("lat");
                        double longitude = (double) coordinates.get("lng");
                        Boolean isOpenNow = (JSONRestaurant.get("opening_hours") == null) ? null : (boolean) ((JSONObject)(JSONRestaurant.get("opening_hours"))).get("open_now");
                        restaurants.add(new Restaurant(name, rating, address, phoneNumber, isOpenNow, latitude, longitude));
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        return restaurants;

    }



    public void getPlacePhoto(String photo_reference) {

        String URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + photo_reference + "&key=";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(URL + KEY);
            request.addHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                System.out.println(response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());   // 200
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String stringResult = EntityUtils.toString(entity);
                    System.out.println(stringResult);

                }
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
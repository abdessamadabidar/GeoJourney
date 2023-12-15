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

public class GeocodeAPI {

    private final String KEY = System.getenv("GOOGLE_MAPS_API_KEY");


    public ArrayList<String> getAutocomplete(String searchValue) {
        String handledSearchInput = searchValue.replace(" ", "%20").replace("+", "%2B");
        JSONObject location = getCoordinates(handledSearchInput);
        String radius = "5000";

        assert location != null;
        String URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
                + handledSearchInput + "&location=" + location.get("lat") + "%2C" + location.get("lng") + "&radius=" + radius + "&strictbounds=true&key=";


        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(URL + KEY);
            request.addHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                System.out.println(response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());   // 200
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String stringResult = EntityUtils.toString(entity);
                    JSONObject jsonResult = (JSONObject) new JSONParser().parse(stringResult);
                    JSONArray arrayResult =  (JSONArray) jsonResult.get("predictions");
                    ArrayList<String> places_id = new ArrayList<>();
                    for (int i=0 ; i < arrayResult.size(); ++i) {
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


    private JSONObject getCoordinates(String searchInput) {
        String URL = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?fields=geometry/location&input=" + searchInput + "&inputtype=textquery&key=";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(URL + KEY);
            request.addHeader("Content-Type", "application/json");
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String stringResult = EntityUtils.toString(entity);
                    JSONObject result =  (JSONObject) new JSONParser().parse(stringResult);
                    return (JSONObject) ((JSONObject) ((JSONObject) ((JSONArray) result.get("candidates")).get(0)).get("geometry")).get("location");


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


    public ArrayList<Location> getPlaceDetails(ArrayList<String> placesID) {

        ArrayList<Location> locations = new ArrayList<>();
        for (String place_id : placesID) {
            String URL = "https://maps.googleapis.com/maps/api/place/details/json?fields=formatted_address%2Cgeometry/location&place_id=" + place_id + "&key=";

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(URL + KEY);
                request.addHeader("Content-Type", "application/json");
                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String stringResult = EntityUtils.toString(entity);
                        JSONObject jsonResult = (JSONObject) new JSONParser().parse(stringResult);
                        JSONObject result = (JSONObject) jsonResult.get("result");

                        String formatted_address = (String) result.get("formatted_address");
                        JSONObject location = (JSONObject) ((JSONObject) result.get("geometry")).get("location");
                        locations.add(new Location(formatted_address, (double) location.get("lat"), (double) location.get("lng")));


                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        return locations;

    }
}

package geojourney.geojourney;

import javafx.scene.image.Image;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class API {
//    protected static final String KEY = System.getenv("GOOGLE_MAPS_API_KEY");
    protected static final String KEY = OpeningController.GOOGLE_MAPS_API_KEY;
    private static double LATITUDE;
    private static double LONGITUDE;

    public static boolean resultFound;

//    public static ArrayList<String> getPlacesId(String type, int radius) throws IOException {
//        System.out.println(getLATITUDE());
//        System.out.println(getLONGITUDE());
//        String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + getLATITUDE() + "%2C" + getLONGITUDE() + "&radius=" + radius + "&type=" + type + "&key=" + KEY;
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//
//            HttpGet request = new HttpGet(URL);
//            request.addHeader("Content-Type", "application/json");
//
//            try (CloseableHttpResponse response = httpClient.execute(request)) {
//                System.out.println(response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase() + " from get places id API");   // 200
//                HttpEntity entity = response.getEntity();
//                if (entity != null) {
//                    String stringResult = EntityUtils.toString(entity);
//                    JSONObject result =  (JSONObject) new JSONParser().parse(stringResult);
//                    if(result.get("status").equals("ZERO_RESULTS")) return null;
//                    JSONArray arrayResult =  (JSONArray) result.get("results");
//                    ArrayList<String> places_id = new ArrayList<>();
//                    for (int i=0 ; i<arrayResult.size(); ++i) {
//                        JSONObject JSONItem = (JSONObject) arrayResult.get(i);
//                        places_id.add((String) JSONItem.get("place_id"));
//                    }
//
//                    return places_id;
//
//                }
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//
//        return null;
//    }


//    public static ArrayList<Place> getPlacesDetails(ArrayList<String> placesId) {
//
//        if (placesId != null) {
//            ArrayList<Place> places  = new ArrayList<>();
//            for (String placeId : placesId) {
//                String URL = "https://maps.googleapis.com/maps/api/place/details/json?fields=name%2Copening_hours/open_now%2Cformatted_address%2Cformatted_phone_number%2Cgeometry/location%2Cuser_ratings_total%2Crating&place_id=" + placeId + "&key=";
//                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//                    HttpGet request = new HttpGet(URL + KEY);
//                    request.addHeader("Content-Type", "application/json");
//                    try (CloseableHttpResponse response = httpClient.execute(request)) {
//                        System.out.println(response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase() + " from get places details API");   // 200
//                        HttpEntity entity = response.getEntity();
//                        if (entity != null) {
//                            String stringResult = EntityUtils.toString(entity);
//                            JSONObject jsonResult = (JSONObject) new JSONParser().parse(stringResult);
//                            JSONObject JSONPlace = (JSONObject) jsonResult.get("result");
//                            String address = (String) JSONPlace.get("formatted_address");
//                            String phoneNumber = (String) JSONPlace.get("formatted_phone_number");
//                            String name = (String) JSONPlace.get("name");
//                            double rating = (JSONPlace.get("rating") instanceof Double) ? (double) JSONPlace.get("rating") : 0.0;
//                            JSONObject coordinates = (JSONObject) ((JSONObject) (JSONPlace.get("geometry"))).get("location");
//                            double latitude = (double) coordinates.get("lat");
//                            double longitude = (double) coordinates.get("lng");
//                            Boolean isOpenNow = (JSONPlace.get("opening_hours") == null) ? null : (boolean) ((JSONObject)(JSONPlace.get("opening_hours"))).get("open_now");
//                            long totalRating = (JSONPlace.get("rating") != null ) ? (Long) JSONPlace.get("user_ratings_total") : 0;
//                            int tr = Integer.parseInt(String.valueOf(totalRating).replace(".0", ""));
//
//                            places.add(new Place(name, address, latitude, longitude, rating, isOpenNow, phoneNumber, tr));
//
//                        }
//                    } catch (ParseException e) {
//                        throw new RuntimeException(e);
//                    }
//                    finally {
//                        httpClient.close();
//                    }
//                }
//                catch (IOException e) {
//                    System.out.println(e.getMessage());
//                }
//
//            }
//            return places;
//        }
//
//
//        return null;
//
//    }

    public static void setLATITUDE(double LATITUDE) {
        API.LATITUDE = LATITUDE;
    }

    public static void setLONGITUDE(double LONGITUDE) {
        API.LONGITUDE = LONGITUDE;
    }

    public static double getLATITUDE() {
        return LATITUDE;
    }

    public static double getLONGITUDE() {
        return LONGITUDE;
    }



    public static ArrayList<Place> getNearbyPlace(String type, int radius) {
        String jsonBody = "{\n" +
                "  \"includedTypes\": [\"" + type + "\"],\n" +
                "  \"maxResultCount\": " + 10 +  ",\n" +
                "  \"locationRestriction\": {\n" +
                "    \"circle\": {\n" +
                "      \"center\": {\n" +
                "        \"latitude\": " + getLATITUDE() + ",\n" +
                "        \"longitude\": " + getLONGITUDE() + " },\n" +
                "      \"radius\": " + radius + "\n" +
                "    }\n" +
                "  }\n" +
                "}";

        try {
            HttpPost request = new HttpPost(new URI("https://places.googleapis.com/v1/places:searchNearby"));
            request.addHeader("Content-Type", "application/json");
            request.addHeader("X-Goog-Api-Key", "AIzaSyBGto3yQEzfnIeJaBaLI94Eht5aiD1DVzI");
            request.addHeader("X-Goog-FieldMask", "places.displayName,places.formattedAddress,places.location,places.nationalPhoneNumber,places.rating,places.userRatingCount");

            CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new DefaultRedirectStrategy()).build();

            // Set the request body
            request.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));

            // Execute the request
            CloseableHttpResponse response = httpClient.execute(request);

            try {
                // Check the status code
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Handle the successful response, you can parse the JSON here
                    String responseBody = EntityUtils.toString(response.getEntity());
                    JSONObject res = (JSONObject) new JSONParser().parse(responseBody);
                    JSONArray arr = (JSONArray) res.get("places");
                    ArrayList<Place> hotels = new ArrayList<>();
                    if (!arr.isEmpty()) {
                        for (int i=0; i< arr.size(); ++i) {
                            JSONObject placeJSON = (JSONObject) arr.get(i);
                            String phone = (String) placeJSON.get("nationalPhoneNumber");
                            String address = (String) placeJSON.get("formattedAddress");
                            double lat = (double) ((JSONObject) placeJSON.get("location")).get("latitude");
                            double lng = (double) ((JSONObject) placeJSON.get("location")).get("longitude");
                            double rating = (placeJSON.get("rating") instanceof Double) ? (double) placeJSON.get("rating") : 0.0;
                            long totalRating = (placeJSON.get("rating") != null ) ? (Long) placeJSON.get("userRatingCount") : 0;
                            String name = (String) ((JSONObject) placeJSON.get("displayName")).get("text");
                            Place place = new Place(name, address, lat, lng, rating, true, phone, (int) totalRating);
                            hotels.add(place);
                        }
                        return hotels;
                    }

                }
                    // Handle the error response
                System.err.println("Error: " + response.getStatusLine().getReasonPhrase());
                return null;

            } catch (ParseException e) {
                throw new RuntimeException(e);
            } finally {
                // Close the response and the HttpClient
                response.close();
                httpClient.close();
            }

        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

    }

}




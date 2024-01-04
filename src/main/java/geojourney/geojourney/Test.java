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

public class Test {

    private static final String KEY = System.getenv("GOOGLE_MAPS_API_KEY");


    public static void fetch() {

        String URL = "https://maps.googleapis.com/maps/api/directions/json?destination=Imzouren&origin=35.173867226238855%2C-3.862133730680905&key=AIzaSyBGto3yQEzfnIeJaBaLI94Eht5aiD1DVzI";



        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(URL);
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


    public static void main(String[] args) {
        Test.fetch();
    }

}

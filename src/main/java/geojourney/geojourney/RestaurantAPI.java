package geojourney.geojourney;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class RestaurantAPI {


    private final String KEY = "AIzaSyBGto3yQEzfnIeJaBaLI94Eht5aiD1DVzI";


    public void fetch() throws IOException {
        String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=35.170446999175276%2C-3.861099048903415&radius=1500&type=restaurant&key=";
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

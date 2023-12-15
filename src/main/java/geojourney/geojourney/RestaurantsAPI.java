package geojourney.geojourney;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.io.IOException;


public class RestaurantsAPI extends API {


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

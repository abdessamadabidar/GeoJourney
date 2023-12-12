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

public class GeocodeAPI {



    private final String KEY = "c3751eee6c464cc78ccb3b5c4f73d2c4";



    public Location fetch(String query)  {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {


            HttpGet request = new HttpGet("https://api.geoapify.com/v1/geocode/search?text=" + query +"&lang=en&limit=5&format=json&apiKey=".trim() + this.KEY);
            request.addHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
//                System.out.println(response.getProtocolVersion());              // HTTP/1.1
//                System.out.println(response.getStatusLine().getStatusCode());   // 200
//                System.out.println(response.getStatusLine().getReasonPhrase()); // OK
//                System.out.println(response.getStatusLine().toString());

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String stringResult = EntityUtils.toString(entity);
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(stringResult);

                    JSONArray jsonArray = (JSONArray) jsonObject.get("results");

                    if (jsonArray != null && !jsonArray.isEmpty()) {
                        JSONObject jsonResult = (JSONObject) jsonArray.get(0);
                        Location location = new Location();
                        location.setAddress((String) jsonResult.get("formatted"));
                        location.setLatitude((Double) jsonResult.get("lat"));
                        location.setLongitude((Double) jsonResult.get("lon"));
                        return location;
                    }



                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return new Location();
    }
}

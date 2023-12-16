package geojourney.geojourney;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WebController implements Initializable {

    @FXML
    public Slider slider;

    @FXML
    private WebView webView;
    @FXML
    private WebEngine webEngine;
    @FXML
    private TextField search;

    @FXML
    private Button clearSearchBtn;
    @FXML
    private Button openScrollPane;
    @FXML
    private Button closeScrollPane;
    @FXML
    private ScrollPane aside;
    @FXML
    private RadioButton osm;
    @FXML
    private RadioButton googleHybrid;
    @FXML
    private RadioButton googleStreets;
    @FXML
    private RadioButton cyclOSM;
    @FXML
    private RadioButton googleTerrain;
    @FXML
    private VBox radioGroup;
    @FXML
    private VBox autocomplete_results;
    @FXML
    private Button geocodeBtn;
    @FXML
    private VBox placesList;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("/web/index.html").toExternalForm());

        search.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            clearSearchBtn.setVisible(!newValue.isEmpty());
            if(newValue.isEmpty()) {
                autocomplete_results.getChildren().clear();
                autocomplete_results.setPrefHeight(0.0);
                autocomplete_results.setVisible(false);
            }
        }));
        radioGroup.setVisible(false);
        autocomplete_results.setVisible(false);
        webEngine.setJavaScriptEnabled(true);
        closeScrollPane.setVisible(false);

        slider.setMin(0);
        slider.setMax(5000);
        slider.setValue(500);
        slider.setMajorTickUnit(500);
        slider.setMinorTickCount(250);


        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                webEngine.executeScript("changeCircleRadius(" + newValue + ")");
            }
        });

    }



    public void showAsidePane(ActionEvent event) {
        aside.setVisible(true);
        closeScrollPane.setVisible(true);
        openScrollPane.setVisible(false);
    }

    public void hideAsidePane(ActionEvent event) {
        closeScrollPane.setVisible(false);
        aside.setVisible(false);
        openScrollPane.setVisible(true);
    }

    public void clearSearch(ActionEvent event) {
        search.clear();
        autocomplete_results.getChildren().clear();
        autocomplete_results.setPrefHeight(0.0);
        autocomplete_results.setVisible(false);
    }


    @FXML
    public void handleTileLayerChange(ActionEvent event) {
        if (osm.isSelected()) {
            osm.setStyle("-fx-background-color: #e9ecef; -fx-background-radius: 8 8 0 0");
            googleHybrid.setStyle("-fx-background-color: transparent");
            googleStreets.setStyle("-fx-background-color: transparent");
            cyclOSM.setStyle("-fx-background-color: transparent");
            googleTerrain.setStyle("-fx-background-color: transparent");
            webEngine.executeScript("changeTileLayer('OpenStreetMap')");
        }
        else if (googleHybrid.isSelected()) {
            googleHybrid.setStyle("-fx-background-color: #e9ecef;");
            osm.setStyle("-fx-background-color: transparent");
            googleStreets.setStyle("-fx-background-color: transparent");
            cyclOSM.setStyle("-fx-background-color: transparent");
            googleTerrain.setStyle("-fx-background-color: transparent");
            webEngine.executeScript("changeTileLayer('Google Hybrid')");
        }
        else if (googleStreets.isSelected()) {
            googleStreets.setStyle("-fx-background-color: #e9ecef;");
            osm.setStyle("-fx-background-color: transparent");
            googleHybrid.setStyle("-fx-background-color: transparent");
            cyclOSM.setStyle("-fx-background-color: transparent");
            googleTerrain.setStyle("-fx-background-color: transparent");
            webEngine.executeScript("changeTileLayer('Google Streets')");
        }
        else if (cyclOSM.isSelected()) {
            cyclOSM.setStyle("-fx-background-color: #e9ecef;");
            osm.setStyle("-fx-background-color: transparent");
            googleStreets.setStyle("-fx-background-color: transparent");
            googleHybrid.setStyle("-fx-background-color: transparent");
            googleTerrain.setStyle("-fx-background-color: transparent");
            webEngine.executeScript("changeTileLayer('Cycle OpenStreetMap')");
        }
        else {
            googleTerrain.setStyle("-fx-background-color: #e9ecef; -fx-background-radius: 0 0 8 8");
            osm.setStyle("-fx-background-color: transparent");
            googleHybrid.setStyle("-fx-background-color: transparent");
            googleStreets.setStyle("-fx-background-color: transparent");
            cyclOSM.setStyle("-fx-background-color: transparent");
            webEngine.executeScript("changeTileLayer('Google Terrain')");
        }
    }

    @FXML
    public void showLayers(ActionEvent event) {
        radioGroup.setVisible(!radioGroup.isVisible());
    }

    @FXML
    public void handleZoom(ActionEvent event) {
        Button zoomBtn = (Button)event.getSource();
        String clickedZoomBtn = zoomBtn.getId();
        webEngine.executeScript("setZoom('" + clickedZoomBtn + "')");

    }

    @FXML
    public void handleLocate(ActionEvent event) {
        webEngine.executeScript("locateMe()");
    }

    @FXML
    public void setSatelliteLayer(ActionEvent event) {
        webEngine.executeScript("setSatellite();");
    }


    @FXML
    public void handleGeocode(ActionEvent event) {
        GeocodeAPI geocodeAPI = new GeocodeAPI();
        String searchValue = search.getText().trim();
        if (searchValue.length() > 2) {
            autocomplete_results.setVisible(true);
            ArrayList<Location> locations = geocodeAPI.getPlaceDetails(geocodeAPI.getAutocomplete(searchValue));
            for (Location location : locations) {
                if (!location.isNoWhere()) {
                    Button result = getResult(location);
                    result.setOnAction(e -> {setMarker(location.getLatitude(), location.getLongitude());});
                    autocomplete_results.getChildren().add(result);
                }
            }


        }

    }

    private Button getResult(Location location) {
        Button result = new Button();
        result.setText(location.getAddress());

        result.setStyle("-fx-background-color: transparent; -fx-fill: #353535; -fx-cursor: hand; -fx-font-family:  'Product Sans'");
        result.setPadding(new Insets(10, 20, 10, 20));

        result.setOnMouseEntered(e -> {
            result.setStyle("-fx-background-color: #e2e8f0; ");
        });
        result.setOnMouseExited(e -> {
            result.setStyle("-fx-background-color: transparent; ");
        });
        return result;
    }

    public void setMarker(double lat, double lng) {
        webEngine.executeScript("markLocation("+ lng + "," + lat + ");");
        autocomplete_results.getChildren().clear();
        autocomplete_results.setPrefHeight(0.0);
        autocomplete_results.setVisible(false);

    }

    @FXML
    public void fetchRestaurants(ActionEvent event) {
        aside.setVisible(true);
        try {
            RestaurantsAPI restaurantsAPI = new RestaurantsAPI();
            ArrayList<Place> restaurants = restaurantsAPI.getPlacesDetails(restaurantsAPI.getPlacesId("restaurant"));

            JSONArray coordinates = new JSONArray();

            for (Place restaurant : restaurants) {
                JSONObject location = new JSONObject();
                location.put("lat", restaurant.getLatitude());
                location.put("lng", restaurant.getLongitude());
                location.put("name", restaurant.getName());
                coordinates.add(location);

                loadRestaurantContainer(restaurant);
            }

            JSONObject data = new JSONObject();
            data.put("coordinates", coordinates);

            webEngine.executeScript("markRestaurants(" +  data + ")");

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    public void fetchBanks(ActionEvent event) {
        try {
            BanksAPI banksAPI = new BanksAPI();
            ArrayList<Place> banks = banksAPI.getPlacesDetails(banksAPI.getPlacesId("bank"));

            JSONArray coordinates = new JSONArray();

            for (Place bank : banks) {
                JSONObject location = new JSONObject();
                location.put("lat", bank.getLatitude());
                location.put("lng", bank.getLongitude());
                location.put("name", bank.getName());
                coordinates.add(location);
            }

            JSONObject data = new JSONObject();
            data.put("coordinates", coordinates);

            webEngine.executeScript("markBanks(" +  data + ")");

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void fetchHospitals(ActionEvent event) {
        try {
            HospitalsAPI hospitalsAPI = new HospitalsAPI();
            ArrayList<Place> hospitals = hospitalsAPI.getPlacesDetails(hospitalsAPI.getPlacesId("hospital"));

            JSONArray coordinates = new JSONArray();

            for (Place hospital : hospitals) {
                JSONObject location = new JSONObject();
                location.put("lat", hospital.getLatitude());
                location.put("lng", hospital.getLongitude());
                location.put("name", hospital.getName());
                coordinates.add(location);
            }

            JSONObject data = new JSONObject();
            data.put("coordinates", coordinates);

            webEngine.executeScript("markHospitals(" +  data + ")");

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadRestaurantContainer(Place place) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/components/restaurant-container.fxml"));
        HBox restaurant = fxmlLoader.load();
        if (restaurant != null) {
            RestaurantController restaurantController = fxmlLoader.getController();
            restaurantController.setData(place);
            placesList.getChildren().add(restaurant);
        }

    }

}

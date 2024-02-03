package geojourney.geojourney;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import netscape.javascript.JSObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class WebController implements Initializable {

    @FXML
    public Slider sliderPlaceCircleRadius;
    @FXML
    public Slider sliderAreaCircleRadius;
    @FXML
    private WebView webView;
    @FXML
    private WebEngine webEngine;
    @FXML
    private TextField search;

    @FXML
    private Button clearSearchBtn;
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
    private Text place;
    @FXML
    private Button shutdown;
    @FXML
    private Button restart;
    @FXML
    private Button back;
    @FXML
    private HBox alert;
    private Timeline alertTimeline;

    @FXML
    private TextField source;
    @FXML
    private TextField destination;
    @FXML
    private Label radiusPlaceCircleLabel;
    @FXML
    private Label radiusAreaCircleLabel;
    @FXML
    private VBox formContainer;

    @FXML
    private Button route;
    private double radiusPlaceCircle;
    private double radiusAreaCircle;

    @FXML
    private Button restaurantsBtn;
    @FXML
    private Button hospitalsBtn;
    @FXML
    private Button banksBtn;
    @FXML
    private Button hotelsBtn;
    @FXML
    private Button pharmaciesBtn;

    @FXML
    private Text routeName;
    @FXML
    private Text routeDistance;
    @FXML
    private Text routeTime;
    @FXML
    private HBox routeInfoContainer;
    private WebController instance;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webEngine = webView.getEngine();
        webEngine.load(Objects.requireNonNull(getClass().getResource("/web/index.html")).toExternalForm());

        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                instance = this;
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("WebController", instance);

            }


        });

        search.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            clearSearchBtn.setVisible(!newValue.isEmpty());
            if(newValue.isEmpty()) {
                autocomplete_results.getChildren().clear();
                autocomplete_results.setPrefHeight(0.0);
                autocomplete_results.setVisible(false);
            }
        }));

        formContainer.setVisible(false);
        radioGroup.setVisible(false);
        autocomplete_results.setVisible(false);
        webEngine.setJavaScriptEnabled(true);
        clearSearchBtn.setVisible(false);

        sliderAreaCircleRadius.setMin(100);
        sliderAreaCircleRadius.setMax(10000);
        sliderAreaCircleRadius.setValue(0);
        sliderAreaCircleRadius.setMajorTickUnit(2);
        sliderAreaCircleRadius.setMinorTickCount(1);

        sliderAreaCircleRadius.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                radiusAreaCircle = newValue.doubleValue();
                radiusAreaCircleLabel.setText(String.format("%.0f", newValue) + " m");
//                webEngine.executeScript("changeCircleRadius(" + newValue + ")");
            }
        });



        sliderPlaceCircleRadius.setMin(10);
        sliderPlaceCircleRadius.setMax(50);
        sliderPlaceCircleRadius.setValue(0);
        sliderPlaceCircleRadius.setMajorTickUnit(2);
        sliderPlaceCircleRadius.setMinorTickCount(1);

        alert.setVisible(false);
        alertTimeline = new Timeline(new KeyFrame(Duration.seconds(3), this::hideAlert));

        sliderPlaceCircleRadius.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                radiusPlaceCircle = newValue.doubleValue();
                radiusPlaceCircleLabel.setText(String.format("%.0f", newValue) + " m");
//                webEngine.executeScript("changeCircleRadius(" + newValue + ")");
            }
        });



        routeInfoContainer.setVisible(false);


        shutdown.setOnAction(e -> {
            Platform.exit();
        });

        restart.setOnAction(e -> {
            webEngine.executeScript("removeMarkers(); removeCircles(); removePolylines(); removeRoutingContainer();");
        });



        route.setOnAction(e -> {
            if (formContainer.isVisible()) this.closeRouting(e);
            else this.openRouting(e);
        });

        restaurantsBtn.setOnAction(e -> {
            if (API.getLATITUDE() != 0 && API.getLONGITUDE() != 0) {
                this.fetchRestaurants(e);
            }else {
                Text textNode = (Text) alert.getChildren().get(1);
                textNode.setText("Could not fetch restaurants! Address required");
                alert.setVisible(true);
                alertTimeline.play();
            }
        });
        hospitalsBtn.setOnAction(e -> {
            if (API.getLATITUDE() != 0 && API.getLONGITUDE() != 0) {
                this.fetchHospitals(e);
            }else {
                Text textNode = (Text) alert.getChildren().get(1);
                textNode.setText("Could not fetch hospitals! Address required");
                alert.setVisible(true);
                alertTimeline.play();
            }
        });
        banksBtn.setOnAction(e -> {
            if (API.getLATITUDE() != 0 && API.getLONGITUDE() != 0) {
                this.fetchBanks(e);
            }else {
                Text textNode = (Text) alert.getChildren().get(1);
                textNode.setText("Could not fetch banks! Address required");
                alert.setVisible(true);
                alertTimeline.play();
            }
        });

        hotelsBtn.setOnAction(e -> {
            if (API.getLATITUDE() != 0 && API.getLONGITUDE() != 0) {
                this.fetchHotels(e);
            }else {
                Text textNode = (Text) alert.getChildren().get(1);
                textNode.setText("Could not fetch hotels! Address required");
                alert.setVisible(true);
                alertTimeline.play();
            }
        });
        pharmaciesBtn.setOnAction(e -> {
            if (API.getLATITUDE() != 0 && API.getLONGITUDE() != 0) {
                this.fetchPharmacies(e);
            }else {
                Text textNode = (Text) alert.getChildren().get(1);
                textNode.setText("Could not fetch pharmacies! Address required");
                alert.setVisible(true);
                alertTimeline.play();
            }
        });

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

    private void hideAlert(ActionEvent event) {
        alert.setVisible(false);
    }



    @FXML
    public void handleGeocode(ActionEvent event) {
        AtomicBoolean resultIsFound = new AtomicBoolean(true);
        GeocodeAPI geocodeAPI = new GeocodeAPI();
        String searchValue = search.getText().trim();
        AtomicReference<ArrayList<Location>> locations = new AtomicReference<>(new ArrayList<>());
        if (searchValue.length() > 2) {
            autocomplete_results.setVisible(true);

            CompletableFuture<Void> searchTask = CompletableFuture.runAsync(() -> {
                ArrayList<Location> locationArrayList = geocodeAPI.getPlaceDetails(geocodeAPI.getAutocomplete(searchValue));
                if (locationArrayList == null) resultIsFound.set(false);
                locations.set(locationArrayList);

            });
            searchTask.thenRun(() -> {
                Platform.runLater(() -> {

                    if (!resultIsFound.get()) {
                        Text textNode = (Text) alert.getChildren().get(1);
                        textNode.setText("No address found");
                        alert.setVisible(true);
                        alertTimeline.play();
                    }
                    else {
                        for (Location location : locations.get()) {
                            if (!location.isNoWhere()) {
                                Button result = getResult(location);
                                result.setOnAction(e -> {
                                    setMarker(location.getLatitude(), location.getLongitude());
                                });
                                autocomplete_results.getChildren().add(result);
                            }
                        }
                    }
                });
            });
        }
    }

    private Button getResult(Location location) {
        Button result = new Button();
        result.setText(location.getAddress());

        result.setStyle("-fx-background-color: transparent; -fx-fill: #353535; -fx-cursor: hand; -fx-font-family:  'Product Sans'");
        result.setPadding(new Insets(10, 20, 10, 20));
        result.setPrefWidth(346);
        result.setAlignment(Pos.CENTER_LEFT);

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

    public void fetchHotels(ActionEvent event) {
        ArrayList<Place> hotels = API.getNearbyPlace("hotel", (int) radiusAreaCircle);

        JSONArray coordinates = new JSONArray();
        for (Place hotel : hotels) {
            System.out.println(place);
            JSONObject location = new JSONObject();
            location.put("lat", hotel.getLatitude());
            location.put("lng", hotel.getLongitude());
            location.put("name", hotel.getName());
            location.put("rating", hotel.getRating());
            location.put("totalRating", hotel.getTotalRating());
            location.put("isOpenNow", hotel.getOpenNow());
            location.put("phone", hotel.getPhone());
            location.put("address", hotel.getAddress());
            coordinates.add(location);
        }

        JSONObject data = new JSONObject();
        data.put("coordinates", coordinates);

        webEngine.executeScript("markHotels(" +  data + ", " + radiusAreaCircle + ", " + radiusPlaceCircle + ", " +  API.getLATITUDE() + ", " +  API.getLONGITUDE() + ")");

    }

    public void fetchPharmacies(ActionEvent event) {
        try {
            ArrayList<Place> pharmacies = API.getNearbyPlace("pharmacy", (int) radiusAreaCircle);
            JSONArray coordinates = new JSONArray();
            for (Place pharmacy : pharmacies) {
                JSONObject location = new JSONObject();
                location.put("lat", pharmacy.getLatitude());
                location.put("lng", pharmacy.getLongitude());
                location.put("name", pharmacy.getName());
                location.put("rating", pharmacy.getRating());
                location.put("totalRating", pharmacy.getTotalRating());
                location.put("isOpenNow", pharmacy.getOpenNow());
                location.put("phone", pharmacy.getPhone());
                location.put("address", pharmacy.getAddress());
                coordinates.add(location);
            }

            JSONObject data = new JSONObject();
            data.put("coordinates", coordinates);

            webEngine.executeScript("markPharmacies(" +  data + ", " + radiusAreaCircle + ", " + radiusPlaceCircle + ", " +  API.getLATITUDE() + ", " +  API.getLONGITUDE() + ")");
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    public void fetchRestaurants(ActionEvent event){

        try {
            ArrayList<Place> restaurants = API.getNearbyPlace("restaurant", (int) radiusAreaCircle);
            JSONArray coordinates = new JSONArray();
            for (Place restaurant : restaurants) {
                JSONObject location = new JSONObject();
                location.put("lat", restaurant.getLatitude());
                location.put("lng", restaurant.getLongitude());
                location.put("name", restaurant.getName());
                location.put("rating", restaurant.getRating());
                location.put("totalRating", restaurant.getTotalRating());
                location.put("isOpenNow", restaurant.getOpenNow());
                location.put("phone", restaurant.getPhone());
                location.put("address", restaurant.getAddress());
                coordinates.add(location);
            }

            JSONObject data = new JSONObject();
            data.put("coordinates", coordinates);

            webEngine.executeScript("markRestaurants(" +  data + ", " + radiusAreaCircle + ", " + radiusPlaceCircle + ", " +  API.getLATITUDE() + ", " +  API.getLONGITUDE() + ")");


        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    public void fetchBanks(ActionEvent event) {

        try {
            ArrayList<Place> banks = API.getNearbyPlace("bank", (int) radiusAreaCircle);
            JSONArray coordinates = new JSONArray();

            for (Place bank : banks) {
                JSONObject location = new JSONObject();
                location.put("lat", bank.getLatitude());
                location.put("lng", bank.getLongitude());
                location.put("name", bank.getName());
                location.put("rating", bank.getRating());
                location.put("totalRating", bank.getTotalRating());
                location.put("isOpenNow", bank.getOpenNow());
                location.put("phone", bank.getPhone());
                location.put("address", bank.getAddress());
                coordinates.add(location);
            }

            JSONObject data = new JSONObject();
            data.put("coordinates", coordinates);

            System.out.println("before execute");
            webEngine.executeScript("markBanks(" +  data + ", " + radiusAreaCircle + ", " + radiusPlaceCircle + ", " +  API.getLATITUDE() + ", " +  API.getLONGITUDE() +")");
            System.out.println("after execute");


        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void fetchHospitals(ActionEvent event) {

        try {
            ArrayList<Place>hospitals = API.getNearbyPlace("hospital", (int) radiusAreaCircle);
            JSONArray coordinates = new JSONArray();

            for (Place hospital : hospitals) {
                JSONObject location = new JSONObject();
                location.put("lat", hospital.getLatitude());
                location.put("lng", hospital.getLongitude());
                location.put("name", hospital.getName());
                location.put("rating", hospital.getRating());
                location.put("totalRating", hospital.getTotalRating());
                location.put("isOpenNow", hospital.getOpenNow());
                location.put("phone", hospital.getPhone());
                location.put("address", hospital.getAddress());
                coordinates.add(location);
            }

            JSONObject data = new JSONObject();
            data.put("coordinates", coordinates);

            webEngine.executeScript("markHospitals("  +  data + ", " + radiusAreaCircle + ", " + radiusPlaceCircle + ", " +  API.getLATITUDE() + ", " +  API.getLONGITUDE() + ")");



        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void goBack(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(OpeningApplication.class.getResource("views/opening-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setWidth(1250);
        stage.setHeight(550);
        stage.setScene(scene);
        String css = Objects.requireNonNull(this.getClass().getResource("stylesheets/opening.css")).toExternalForm();
        scene.getStylesheets().add(css);
        stage.show();
    }

    public void saveImage(String imagePath, File distDir) throws IOException {
        File imageFile = new File(imagePath);
        BufferedImage bufferedImage = ImageIO.read(imageFile);

        String fileName = imageFile.getName();
        String distPath = distDir.getAbsolutePath() + File.separator + fileName;

        File outputFile = new File(distPath);
        ImageIO.write(bufferedImage, "png", outputFile);

        imageFile.delete();
    }

    public void takeScreenshot(ActionEvent event) throws IOException {
        String IMAGE_PATH = "src/main/resources/tmp/screenshot.png";

        // save the image to tmp directory
        Scene scene = ((Node) event.getSource()).getScene();
        WritableImage img = new WritableImage(1490, 765);
        scene.snapshot(img);
        File imageFile = new File(IMAGE_PATH);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(img, null);
        ImageIO.write(bufferedImage, "png", imageFile);



        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setDialogTitle("save your snapshot directory");
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int response = jFileChooser.showDialog(null, "save");
        if (response == JFileChooser.APPROVE_OPTION) {
            File destinationDirectory = jFileChooser.getSelectedFile();
            saveImage(IMAGE_PATH, destinationDirectory);

        }

    }



    @FXML
    public void getRoute(ActionEvent event) {
        String src = source.getText();
        String dist = destination.getText();

        if (!src.isEmpty() && !dist.isEmpty() && src.length()>2 && dist.length()>2) {
            String formattedSourceQuery = src.replaceAll(" ", "%20");
            String formattedDestinationQuery = dist.replaceAll(" ", "%20");

            GeocodeAPI geocodeAPI = new GeocodeAPI();
            JSONObject srcJSON = geocodeAPI.getCoordinates(formattedSourceQuery);
            JSONObject distJSON  = geocodeAPI.getCoordinates(formattedDestinationQuery);

            if (srcJSON != null && distJSON != null) {

                JSONObject data = new JSONObject();
                data.put("src", srcJSON);
                data.put("dist", distJSON);


                webEngine.executeScript("drawRoute(" + data + ");");
                routeInfoContainer.setVisible(true);
                formContainer.setVisible(false);

            }


        }

    }

    public void cancelRoute(ActionEvent event) {
        source.clear();
        destination.clear();
        routeInfoContainer.setVisible(false);
        webEngine.executeScript("removePolylines(); removeMarkers(); removeRoutingContainer();");
    }

    public void openRouting(ActionEvent event) {
        formContainer.setVisible(true);
    }
    public void closeRouting(ActionEvent e) {
        formContainer.setVisible(false);
    }


    public void handleCoordinatesChange(double lat, double lng) {
        API.setLATITUDE(lat);
        API.setLONGITUDE(lng);

    }

    public void getRouteInfos(String name, double distance, double time) {
        routeName.setText(name);
        routeDistance.setText(String.format("%.1f", distance / 1000) + " km");
        routeTime.setText((int) time / 3600 + " h " + (int) (time % 3600) / 60 + " min");

    }
}

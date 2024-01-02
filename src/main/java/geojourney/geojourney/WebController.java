package geojourney.geojourney;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
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
import java.util.concurrent.atomic.AtomicReference;

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
    private Button closePane;
    @FXML
    private Pane aside;
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
    @FXML
    private Text place;
    @FXML
    private Button shutdown;
    @FXML
    private Button restart;
    @FXML
    private Button back;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webEngine = webView.getEngine();
        webEngine.load(Objects.requireNonNull(getClass().getResource("/web/index.html")).toExternalForm());

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
        aside.setVisible(false);
        closePane.setVisible(false);
        clearSearchBtn.setVisible(false);

        slider.setMin(0);
        slider.setMax(1000);
        slider.setValue(50);
        slider.setMajorTickUnit(100);
        slider.setMinorTickCount(50);



        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                webEngine.executeScript("changeCircleRadius(" + newValue + ")");
            }
        });


        closePane.setOnAction(e -> {
            aside.setVisible(false);
            webEngine.executeScript("removeMarkers(); removeCircles();");
            closePane.setVisible(false);
        });


        shutdown.setOnAction(e -> {
            Platform.exit();
        });

        restart.setOnAction(e -> {
            webEngine.executeScript("removeMarkers(); removeCircles();");
            aside.setVisible(false);
            closePane.setVisible(false);
        });






    }



    public void hideAsidePane(ActionEvent event) {
        closePane.setVisible(false);
        aside.setVisible(false);
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
        AtomicReference<ArrayList<Location>> locations = new AtomicReference<>(new ArrayList<>());
        if (searchValue.length() > 2) {
            autocomplete_results.setVisible(true);
            CompletableFuture<Void> searchTask = CompletableFuture.runAsync(() -> {
                locations.set(geocodeAPI.getPlaceDetails(geocodeAPI.getAutocomplete(searchValue)));
            });
            searchTask.thenRun(() -> {
                Platform.runLater(() -> {
                    for (Location location : locations.get()) {
                        if (!location.isNoWhere()) {
                            Button result = getResult(location);
                            result.setOnAction(e -> {setMarker(location.getLatitude(), location.getLongitude());});
                            autocomplete_results.getChildren().add(result);
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

    @FXML
    public void fetchRestaurants(ActionEvent event){

        try {
            API api = new API();
            AtomicReference<ArrayList<Place>> restaurants = new AtomicReference<>(new ArrayList<>());
            CompletableFuture<Void> fetchTask = CompletableFuture.runAsync(() -> {
                try {
                    restaurants.set(api.getPlacesDetails(api.getPlacesId("restaurant")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            fetchTask.thenRun(() -> {
                Platform.runLater(() -> {
                    aside.setVisible(true);
                    place.setText("Restaurant");
                    place.setFill(Paint.valueOf("#ff842f"));
                    aside.setVisible(true);
                    closePane.setVisible(true);
                    placesList.getChildren().clear();
                    JSONArray coordinates = new JSONArray();

                    for (Place restaurant : restaurants.get()) {
                        JSONObject location = new JSONObject();
                        location.put("lat", restaurant.getLatitude());
                        location.put("lng", restaurant.getLongitude());
                        location.put("name", restaurant.getName());
                        coordinates.add(location);
                        loadPlaceContainer(restaurant);
                    }

                    JSONObject data = new JSONObject();
                    data.put("coordinates", coordinates);

                    webEngine.executeScript("markRestaurants(" +  data + ")");
                });
            });


        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    public void fetchBanks(ActionEvent event) {

        try {
            API api = new API();
            AtomicReference<ArrayList<Place>> banks = new AtomicReference<>(new ArrayList<>());
            CompletableFuture<Void> fetchTask = CompletableFuture.runAsync(() -> {
                try {
                    banks.set(api.getPlacesDetails(api.getPlacesId("bank")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            fetchTask.thenRun(() -> {
                Platform.runLater(() -> {
                    aside.setVisible(true);
                    place.setText("Bank");
                    place.setFill(Paint.valueOf("#1dd878"));
                    closePane.setVisible(true);
                    placesList.getChildren().clear();
                    JSONArray coordinates = new JSONArray();

                    for (Place bank : banks.get()) {
                        JSONObject location = new JSONObject();
                        location.put("lat", bank.getLatitude());
                        location.put("lng", bank.getLongitude());
                        location.put("name", bank.getName());
                        coordinates.add(location);
                        loadPlaceContainer(bank);
                    }

                    JSONObject data = new JSONObject();
                    data.put("coordinates", coordinates);

                    webEngine.executeScript("markBanks(" +  data + ")");
                });
            });


        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void fetchHospitals(ActionEvent event) {

        try {
            API api = new API();
            AtomicReference<ArrayList<Place>> hospitals = new AtomicReference<>(new ArrayList<>());
            CompletableFuture<Void> fetchTask = CompletableFuture.runAsync(() -> {
                try {
                    hospitals.set(api.getPlacesDetails(api.getPlacesId("hospital")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            fetchTask.thenRun(() -> {
                Platform.runLater(() -> {
                    aside.setVisible(true);
                    closePane.setVisible(true);
                    place.setText("Hospital");
                    place.setFill(Paint.valueOf("#ff2e54"));
                    placesList.getChildren().clear();
                    JSONArray coordinates = new JSONArray();

                    for (Place hospital : hospitals.get()) {
                        JSONObject location = new JSONObject();
                        location.put("lat", hospital.getLatitude());
                        location.put("lng", hospital.getLongitude());
                        location.put("name", hospital.getName());
                        coordinates.add(location);
                        loadPlaceContainer(hospital);
                    }

                    JSONObject data = new JSONObject();
                    data.put("coordinates", coordinates);

                    webEngine.executeScript("markHospitals(" +  data + ")");
                });
            });



        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadPlaceContainer(Place place)  {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/components/place-container.fxml"));
            VBox restaurant = fxmlLoader.load();
            if (restaurant != null) {
                PlaceController placeController = fxmlLoader.getController();
                placeController.setData(place);
                placesList.getChildren().add(restaurant);
            }
        }
       catch (IOException e) {
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
}

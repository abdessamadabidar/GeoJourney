package geojourney.geojourney;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class OpeningController   implements Initializable {
    @FXML
    private Button startBtn;

    @FXML
    private TextField apiKey;

    @FXML
    private HBox alert;
    private Timeline alertTimeline;

    public static String GOOGLE_MAPS_API_KEY ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        alert.setVisible(false);
         alertTimeline = new Timeline(new KeyFrame(Duration.seconds(3), this::hideAlert));
    }

    private void hideAlert(ActionEvent event) {
        alert.setVisible(false);
    }


    public void openWebStage(ActionEvent event) throws IOException {
        String api = apiKey.getText().trim();
        if (!api.isEmpty()) {
            GOOGLE_MAPS_API_KEY = api;
            FXMLLoader fxmlLoader = new FXMLLoader(OpeningApplication.class.getResource("views/web-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setX(15);
            stage.setY(15);
            stage.setWidth(1500);
            stage.setHeight(800);
            stage.setScene(scene);
            String css = Objects.requireNonNull(this.getClass().getResource("stylesheets/web.css")).toExternalForm();
            scene.getStylesheets().add(css);
            stage.show();
        }
        else {
            alert.setVisible(true);
            alertTimeline.play();
        }

    }


}
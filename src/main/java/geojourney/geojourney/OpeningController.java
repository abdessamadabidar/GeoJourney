package geojourney.geojourney;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class OpeningController   {
    @FXML
    private Button startBtn;


    public void openWebStage(ActionEvent event) throws IOException {
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

}
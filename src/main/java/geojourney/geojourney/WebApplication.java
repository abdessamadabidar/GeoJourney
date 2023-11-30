package geojourney.geojourney;

import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WebApplication extends Application {

    public void start(Stage stage) {

        try {
            Parent root2 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("views/web-view.fxml")));
            Scene scene = new Scene(root2);


            // CSS
            String css = this.getClass().getResource("stylesheets/web.css").toExternalForm();
            scene.getStylesheets().add(css);


            stage.setTitle("GeoJourney Application");
//            stage.setResizable(false);


            // set width & and height to stage
            stage.setWidth(1500);
            stage.setHeight(800);


            // Add Icon
            // Image image = new Image("./resources/images/icons8-location-94.png");
            // stage.getIcons().add(image);


            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch();
    }

}
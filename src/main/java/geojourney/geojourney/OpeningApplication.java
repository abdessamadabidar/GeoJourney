package geojourney.geojourney;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import java.io.IOException;
import java.util.Objects;

public class OpeningApplication extends Application {
    @Override
    public void start(Stage stage) {

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("views/opening-view.fxml")));
            Scene scene = new Scene(root);


            // CSS
            String css = Objects.requireNonNull(this.getClass().getResource("stylesheets/opening.css")).toExternalForm();
            scene.getStylesheets().add(css);


            stage.setTitle("GeoJourney Application");
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/geojourney/geojourney/assets/icons8-location-94.png")));
            stage.getIcons().add(image);
//            stage.setResizable(false);




            // set width & and height to stage
            stage.setWidth(1250);
            stage.setHeight(550);



            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch();
    }
}

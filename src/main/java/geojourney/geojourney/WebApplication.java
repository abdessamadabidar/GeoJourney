package geojourney.geojourney;

import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



public class WebApplication extends Application {

    public void start(Stage stage) {

        System.out.println(System.getenv("GEOAPIFY_API_KEY"));

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

            // set default visibility to
            // --> close Search Button
            Button closeSearch = (Button) scene.lookup("#closeSearch");
            closeSearch.setVisible(false);

            // --> search input
            TextField searchInput = (TextField) scene.lookup("#search");
            searchInput.setVisible(false);

            // --> clear search button
            Button clearSearchButton = (Button) scene.lookup("#clearSearchBtn");
            clearSearchButton.setVisible(false);

            // --> aside pane
            Pane asidePane = (Pane) scene.lookup("#aside");
            asidePane.setVisible(false);


            RestaurantAPI hotelsAPI = new RestaurantAPI();
            hotelsAPI.fetch();


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
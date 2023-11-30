package geojourney.geojourney;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class OpeningController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
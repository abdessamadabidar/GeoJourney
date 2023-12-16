package geojourney.geojourney;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;


public class PlaceController implements Initializable {

    @FXML
    private Text name;

    @FXML
    private Text address;

    @FXML
    private Text rating;

    @FXML
    private Text isOpenNow;

    @FXML
    private Text totalRating;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setData(Place place) {
        name.setText(place.getName());
        address.setText(place.getAddress());
        rating.setText(String.valueOf(place.getRating()));
        totalRating.setText("(" + place.getTotalRating() + ")");
        if (place.getOpenNow() != null) {
            if (place.getOpenNow()) {
                isOpenNow.setText("Opened");
                isOpenNow.setFill(Paint.valueOf("#008000"));
            }
            else {
                isOpenNow.setText("Closed");
                isOpenNow.setFill(Paint.valueOf("#fd151b"));
            }

        }
        else isOpenNow.setText("---");
    }
}

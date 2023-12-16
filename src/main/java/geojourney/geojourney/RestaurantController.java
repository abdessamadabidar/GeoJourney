package geojourney.geojourney;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;


public class RestaurantController  implements Initializable {

    @FXML
    private Text name;

    @FXML
    private Text address;

    @FXML
    private Text rating;

    @FXML
    private Text isOpenNow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setData(Place restaurant) {
        name.setText(restaurant.getName());
        address.setText(restaurant.getAddress());
        rating.setText(String.valueOf(restaurant.getRating()));
        if (restaurant.getOpenNow() != null) {
            isOpenNow.setText((restaurant.getOpenNow())? "Opened" : "Closed" );
        }
        else isOpenNow.setText("---");
    }
}

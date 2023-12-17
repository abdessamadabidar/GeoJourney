package geojourney.geojourney;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;




public class OpeningController   {
    @FXML
    private Button startBtn;


    public void openWebStage(ActionEvent event)  {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        WebApplication webApplication = new WebApplication();
        webApplication.start(stage);
    }

}
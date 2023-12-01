package geojourney.geojourney;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WebController implements Initializable {

    @FXML
    private WebView webView;
    @FXML
    private TextField search;
    @FXML
    private Button openSearch;
    @FXML
    private Button closeSearch;
    @FXML
    private Button clearSearchBtn;
    @FXML
    private Button openPane;
    @FXML
    private Pane aside;
    @FXML
    private RadioButton radioButton1;
    @FXML
    private RadioButton radioButton2;
    @FXML
    private RadioButton radioButton3;
    @FXML
    private VBox radioGroup;
    @FXML
    private Button layers;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WebEngine webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("/web/index.html").toExternalForm());
        search.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            clearSearchBtn.setVisible(!newValue.isEmpty());
        }));
        radioGroup.setVisible(false);
    }

    public void showSearchInput(ActionEvent event) {
        search.setVisible(true);
        if (!search.getText().isEmpty()) {
            clearSearchBtn.setVisible(true);
        }
        openSearch.setVisible(false);
        closeSearch.setVisible(true);

    }

    public void hideSearchInput(ActionEvent event) {
        search.setVisible(false);
        clearSearchBtn.setVisible(false);
        closeSearch.setVisible(false);
        openSearch.setVisible(true);
    }

    public void showAsidePane(ActionEvent event) {
        aside.setVisible(true);
        openPane.setVisible(false);
    }

    public void hideAsidePane(ActionEvent event) {
        aside.setVisible(false);
        openPane.setVisible(true);
    }

    public void clearSearch(ActionEvent event) {
        search.clear();
    }

    public void handleTileLayerChange(ActionEvent event) {
        if (radioButton1.isSelected()) {
            radioButton1.setStyle("-fx-background-color: #e9ecef; -fx-background-radius: 8 8 0 0");
            radioButton2.setStyle("-fx-background-color: transparent");
            radioButton3.setStyle("-fx-background-color: transparent");
        }
        else if (radioButton2.isSelected()) {
            radioButton2.setStyle("-fx-background-color: #e9ecef;");
            radioButton1.setStyle("-fx-background-color: transparent");
            radioButton3.setStyle("-fx-background-color: transparent");
        }
        else {
            radioButton3.setStyle("-fx-background-color: #e9ecef; -fx-background-radius: 0 0 8 8");
            radioButton1.setStyle("-fx-background-color: transparent");
            radioButton2.setStyle("-fx-background-color: transparent");
        }
    }

    public void showLayers(ActionEvent event) {
        radioGroup.setVisible(!radioGroup.isVisible());
    }






}
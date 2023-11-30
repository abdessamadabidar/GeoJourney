module geojourney.geojourney {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    opens geojourney.geojourney to javafx.fxml;
    exports geojourney.geojourney;
}
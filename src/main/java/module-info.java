module geojourney.geojourney {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires jdk.jsobject;
    requires json.simple;


    opens geojourney.geojourney to javafx.fxml;
    exports geojourney.geojourney;
}
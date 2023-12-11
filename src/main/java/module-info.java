module geojourney.geojourney {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires jdk.jsobject;
    requires json.simple;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;


    opens geojourney.geojourney to javafx.fxml;
    exports geojourney.geojourney;
}
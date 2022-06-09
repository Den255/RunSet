module runset {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires gson;
    requires org.controlsfx.controls;

    opens org.openjfx to javafx.fxml;
    exports org.openjfx;
}
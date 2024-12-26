module org.example.frontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.net.http;

    opens org.example.frontend to javafx.fxml;
    exports org.example.frontend;
    exports org.example.frontend.controller;
    opens org.example.frontend.controller to javafx.fxml;
}
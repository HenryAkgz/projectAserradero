module com.example.projectaserradero {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;
    requires javafx.swing;
    requires org.controlsfx.controls;
    requires java.desktop;

    opens layout to javafx.fxml;
    exports controls;
    opens css;
    opens controls;
}
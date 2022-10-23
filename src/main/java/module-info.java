module com.example.projectaserradero {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;

    opens layout to javafx.fxml;
    exports controls;
    opens css;
    opens controls;
}
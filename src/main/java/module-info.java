module com.example.projectaserradero {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens layout to javafx.fxml;
    exports controls;
    opens css;
}
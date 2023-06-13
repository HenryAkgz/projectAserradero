package controls;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class MaintenanceItemLargeController {
    @FXML
    private AnchorPane root;
    @FXML
    private Label lbl_id;
    @FXML
    private Label lbl_estado;
    @FXML
    private Label lbl_fecha;
    @FXML
    private Label lbl_tipo;
    @FXML
    private Label lbl_descripcion;
    StringProperty estado = new SimpleStringProperty("");
    StringProperty id = new SimpleStringProperty("");
    StringProperty fecha = new SimpleStringProperty("");
    StringProperty tipo = new SimpleStringProperty("");
    StringProperty descripción = new SimpleStringProperty("");


    public MaintenanceItemLargeController() {



        id.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                lbl_id.setText(t1);
            }
        });
        estado.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                lbl_estado.setText(t1);
            }
        });

        fecha.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                lbl_fecha.setText(t1);
            }
        });
        tipo.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                lbl_tipo.setText(t1);
            }
        });
        descripción.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                lbl_descripcion.setText(t1);
            }
        });
    }

    public void setInfo(String id, String estado, String fecha, String tipo, String descripcion){
    this.id.set(id);
    this.estado.set(estado);
    this.fecha.set(LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
    this.tipo.set(tipo);
    this.descripción.set(descripcion);
        root.setOnMouseEntered(action -> {
            root.setStyle("-fx-background-color: #0D2C54; -fx-background-radius: 5px;");
            lbl_id.setStyle("-fx-text-fill: white;");
            lbl_estado.setStyle("-fx-text-fill: white;");
            lbl_fecha.setStyle("-fx-text-fill: white;");
            lbl_descripcion.setStyle("-fx-text-fill: white;");
        });

        root.setOnMouseExited(action -> {
            root.setStyle("-fx-background-color: white;");
            lbl_id.setStyle("-fx-text-fill: #373F51;");
            lbl_estado.setStyle("-fx-text-fill: #373F51;");
            lbl_fecha.setStyle("-fx-text-fill: #373F51;");
            lbl_descripcion.setStyle("-fx-text-fill: #373F51;");
        });
    }
}

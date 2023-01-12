package clases;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ItemModelo extends VBox {

    String id_modelo, motor;

    public ItemModelo(String id_modelo, String motor) {
        this.id_modelo = id_modelo;
        this.motor = motor;
        initItem();
    }

    private void initItem() {

        super.setStyle("-fx-background-color: #161925; -fx-padding: 10px; -fx-background-radius: 10px;");

        HBox title_container = new HBox();
        title_container.setAlignment(Pos.CENTER_LEFT);
        Label labelIdModelo = new Label();
        labelIdModelo.setText(id_modelo);
        labelIdModelo.setStyle("-fx-text-fill: #fec701; -fx-font-size: 14px; -fx-font-weight: bold;");
        title_container.getChildren().add(labelIdModelo);

        Label labelMotor = new Label(motor);
        labelMotor.setStyle("-fx-text-fill: white;");


        super.setAlignment(Pos.CENTER);
        super.getChildren().add(title_container);
        super.getChildren().add(labelMotor);

        super.setOnMouseEntered(mouseEvent -> {
            super.setStyle("-fx-background-color: #4B21C3; -fx-padding: 10px; -fx-background-radius: 10px;");
        });

        super.setOnMouseExited(mouseEvent -> {
            super.setStyle("-fx-background-color: #161925; -fx-padding: 10px; -fx-background-radius: 10px;");
        });

    }
}

package clases;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ItemUnidad extends VBox {

   Unidad unidad;

    public ItemUnidad(Unidad unidad) {
         this.unidad = unidad;
        initItem();
    }

    private void initItem() {

        super.setStyle("-fx-background-color: #161925; -fx-padding: 10px; -fx-background-radius: 10px;");

        HBox title_container = new HBox();
        title_container.setAlignment(Pos.CENTER_LEFT);
        Label labelIdUnidad = new Label();
        labelIdUnidad.setText(unidad.getId_unidad());
        labelIdUnidad.setStyle("-fx-text-fill: #fec701; -fx-font-size: 14px; -fx-font-weight: bold;");
        title_container.getChildren().add(labelIdUnidad);

        Label labelEstado = new Label(unidad.getEstado());
        labelEstado.setStyle(Util.getStyleByState(unidad.getEstado()));


        super.setAlignment(Pos.CENTER);
        super.getChildren().add(title_container);
        super.getChildren().add(labelEstado);

        super.setOnMouseEntered(mouseEvent -> {
            super.setStyle("-fx-background-color: #4B21C3; -fx-padding: 10px; -fx-background-radius: 10px;");
        });

        super.setOnMouseExited(mouseEvent -> {
            super.setStyle("-fx-background-color: #161925; -fx-padding: 10px; -fx-background-radius: 10px;");
        });

    }
}

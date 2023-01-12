package clases;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ItemPieza extends VBox {

    String nombrePieza;
    int cantidadEnInventario;

    public ItemPieza(String nombrePieza, int cantidadEnInventario) {
        this.nombrePieza = nombrePieza;
        this.cantidadEnInventario = cantidadEnInventario;
        initItem();
    }

    private void initItem(){
        super.setStyle("-fx-background-color: #161925; -fx-padding: 10px; -fx-background-radius: 10px;");

        HBox title_container = new HBox();
        title_container.setAlignment(Pos.CENTER_LEFT);
        Label labelNombrePieza = new Label();
        labelNombrePieza.setText(nombrePieza);
        labelNombrePieza.setStyle("-fx-text-fill: #fec701; -fx-font-size: 14px; -fx-font-weight: bold;");
        title_container.getChildren().add(labelNombrePieza);

        Label labelCantidad = new Label(cantidadEnInventario+"x piezas en el inventario");
        labelCantidad.setStyle("-fx-text-fill: white;");


        super.setAlignment(Pos.CENTER);
        super.getChildren().add(title_container);
        super.getChildren().add(labelCantidad);

        super.setOnMouseEntered(mouseEvent -> {
            super.setStyle("-fx-background-color: #4B21C3; -fx-padding: 10px; -fx-background-radius: 10px;");
        });

        super.setOnMouseExited(mouseEvent -> {
            super.setStyle("-fx-background-color: #161925; -fx-padding: 10px; -fx-background-radius: 10px;");
        });
    }
}

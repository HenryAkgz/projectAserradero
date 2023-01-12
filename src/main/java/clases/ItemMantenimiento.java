package clases;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ItemMantenimiento extends VBox {
Mantenimiento data;

    public ItemMantenimiento(Mantenimiento data) {
        this.data = data;
        init();
    }

    private void init(){
        String styleCard = "-fx-padding: 10px; -fx-background-color: #161925; -fx-background-radius: 10px;";
        String styleTextDate = "-fx-text-fill: #fec701; -fx-font-size: 14px; -fx-font-weight: bold;";
        String styleCardHover = "-fx-padding: 10px; -fx-background-color: #4B21C3; -fx-background-radius: 10px;";


        setStyle(styleCard);
        Label fechaLabel = new Label(data.getMaintenanceDate());
        fechaLabel.setStyle(styleTextDate);
        getChildren().add(fechaLabel);
        setAlignment(Pos.CENTER);


        setOnMouseEntered(event -> setStyle(styleCardHover));
        setOnMouseExited(event -> setStyle(styleCard));
    }
}

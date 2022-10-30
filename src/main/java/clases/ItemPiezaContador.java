package clases;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ItemPiezaContador extends BorderPane {
    Pieza pieza;
    Integer cantidadDePiezasDelModelo = 1;

    boolean showSpinner;

    public ItemPiezaContador(Pieza pieza, boolean showSpinner) {
        this.pieza = pieza;
        this.showSpinner = showSpinner;
        pieza.setCantidadEnInvetario(cantidadDePiezasDelModelo);
        initItem();
    }

    public ItemPiezaContador(Pieza pieza, boolean showSpinner, boolean rebootValue) {
        this.pieza = pieza;
        this.showSpinner = showSpinner;
        if (rebootValue){
            pieza.setCantidadEnInvetario(cantidadDePiezasDelModelo);
        }
        initItem();
    }

    public void initItem() {
        String styleSpinnerButton = "-fx-background-color: white; -fx-font-size: 14; -fx-font-weight: bold;";
        String styleSpinnerButtonHover = "-fx-background-color: #fec701; -fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: white;";
        String styleMainContainer = "-fx-background-color: #332E3C; -fx-padding: 10px; -fx-background-radius: 10px;";

        super.setStyle(styleMainContainer);

        VBox contentMainInfo = new VBox();
        contentMainInfo.setAlignment(Pos.CENTER);
        Label nombrePiezaLabel = new Label();
        nombrePiezaLabel.setText(pieza.nombrePieza);
        nombrePiezaLabel.setStyle("-fx-text-fill: #fec701; -fx-font-size: 14px; -fx-font-weight: bold;");
        contentMainInfo.getChildren().add(nombrePiezaLabel);

        if (showSpinner) {
            HBox contentSpinnerContainer = new HBox();
            contentSpinnerContainer.setPrefWidth(15.0);
            contentSpinnerContainer.setSpacing(5);
            contentSpinnerContainer.setStyle("-fx-padding: 10 0 0 0;");
            contentSpinnerContainer.setAlignment(Pos.CENTER);
            Button increment = new Button();
            increment.setStyle(styleSpinnerButton);
            increment.setText("+");

            Button decrement = new Button();
            decrement.setStyle(styleSpinnerButton);
            decrement.setText("-");


            Label contadorLabel = new Label(pieza.getCantidadEnInvetario() + "x Piezas");
            contadorLabel.setAlignment(Pos.CENTER);
            contadorLabel.setStyle("-fx-text-fill: white");

            decrement.setOnAction(actionEvent -> {
                if (pieza.cantidadEnInvetario != 0) {
                    cantidadDePiezasDelModelo--;
                    pieza.setCantidadEnInvetario(cantidadDePiezasDelModelo);
                    contadorLabel.setText(cantidadDePiezasDelModelo + "x Piezas");
                }
            });

            increment.setOnAction(actionEvent -> {
                cantidadDePiezasDelModelo++;
                pieza.setCantidadEnInvetario(cantidadDePiezasDelModelo);
                contadorLabel.setText(cantidadDePiezasDelModelo + "x Piezas");
            });

            increment.setOnMouseEntered(mouseEvent -> increment.setStyle(styleSpinnerButtonHover));
            increment.setOnMouseExited(mouseEvent -> increment.setStyle(styleSpinnerButton));
            decrement.setOnMouseEntered(mouseEvent -> decrement.setStyle(styleSpinnerButtonHover));
            decrement.setOnMouseExited(mouseEvent -> decrement.setStyle(styleSpinnerButton));

            contentSpinnerContainer.getChildren().addAll(decrement, contadorLabel, increment);
            super.setBottom(contentSpinnerContainer);
        }
        super.setCenter(contentMainInfo);

    }

}

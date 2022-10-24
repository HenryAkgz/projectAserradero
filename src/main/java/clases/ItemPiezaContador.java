package clases;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ItemPiezaContador extends BorderPane {
    int id_Pieza;
    String nombrePieza;
    int cantidadDePiezasDelModelo;

    public ItemPiezaContador(int id_Pieza, String nombrePieza) {
        this.id_Pieza = id_Pieza;
        this.nombrePieza = nombrePieza;
        this.cantidadDePiezasDelModelo = 1;
        initItem();
    }

    public void initItem(){
        String styleSpinnerButton = "";
        String styleMainContainer = "-fx-background-color: white; -fx-padding: 10px; -fx-background-radius: 10px;";

        super.setStyle(styleMainContainer);

        VBox contentMainInfo = new VBox();
        contentMainInfo.setAlignment(Pos.CENTER);
        Label nombrePiezaLabel = new Label();
        nombrePiezaLabel.setText(nombrePieza);
        nombrePiezaLabel.setStyle("-fx-text-fill: #fec701; -fx-font-size: 14px; -fx-font-weight: bold;");
        contentMainInfo.getChildren().add(nombrePiezaLabel);

        HBox contentSpinnerContainer = new HBox();
        contentSpinnerContainer.setPrefWidth(15.0);
        contentSpinnerContainer.setSpacing(5);
        contentSpinnerContainer.setAlignment(Pos.CENTER);
        Button increment = new Button();
        increment.setStyle(styleSpinnerButton);
        increment.setText("+");

        Button decrement = new Button();
        decrement.setStyle(styleSpinnerButton);
        decrement.setText("-");


        Label contadorLabel = new Label(Integer.toString(cantidadDePiezasDelModelo));
        contadorLabel.setAlignment(Pos.CENTER);
        contadorLabel.setStyle("");

        decrement.setOnAction(actionEvent -> {
            if(cantidadDePiezasDelModelo !=0){
                cantidadDePiezasDelModelo--;
                contadorLabel.setText(Integer.toString(cantidadDePiezasDelModelo));
            }
        });

        increment.setOnAction(actionEvent -> {
            cantidadDePiezasDelModelo++;
            contadorLabel.setText(Integer.toString(cantidadDePiezasDelModelo));
        });

        contentSpinnerContainer.getChildren().addAll( decrement, contadorLabel, increment);

        super.setCenter(contentMainInfo);
        super.setBottom(contentSpinnerContainer);





    }


}

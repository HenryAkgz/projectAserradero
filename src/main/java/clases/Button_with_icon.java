package clases;



import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;


public class Button_with_icon extends HBox {
    Pane icono = new Pane();
    Label textLabel = new Label();

    //variables del contendor
    String borderRadius = "0px";
    String backgroundColor= "red";
    String padding= "0px";
    Double spacing = 5.0;

//variable del contenido del botón
    String idCssIcon = "";
    Double iconSize = 10.0;

    String textColor= "white";
    String textoBotón = "Text Example";



    public Button_with_icon() {
    }

    //setters del contenedor
    public void makeContainer(String backgroundColor, String borderRadius, String padding, Double gap){
        this.backgroundColor = backgroundColor;
        this.borderRadius = borderRadius;
        this.padding = padding;
        this.spacing = spacing;
    }

    public void makeContentButton(String idCssIcon, Double iconSize, String textColor, String textoBotón){
        this.idCssIcon = idCssIcon;
        this.iconSize = iconSize;
        this.textColor = textColor;
        this.textoBotón = textoBotón;
    }




    public void createButton(){

        //creamos el contenedor del botón
        setStyle("-fx-background-color: "+backgroundColor+"; -fx-background-radius: "+borderRadius+"; -fx-padding: "+padding+";");
        setSpacing(spacing);
        //agregamos el icono

        icono.setStyle("-fx-background-color: "+textColor+";");
        icono.setId(idCssIcon);
        icono.setPrefSize(iconSize, iconSize);

        getChildren().add(icono);

        //agregamos el texto
         textLabel.setText(textoBotón);
        textLabel.setStyle("-fx-text-fill: "+ textColor+";");
        getChildren().add(textLabel);


   super.setOnMouseEntered(mouseEvent -> {
       setStyle("-fx-background-color: "+textColor+"; -fx-background-radius: "+borderRadius+"; -fx-padding: "+padding+";");

       icono.setStyle("-fx-background-color: "+backgroundColor+";");
       textLabel.setStyle("-fx-text-fill: "+ backgroundColor+";");

   });

   super.setOnMouseExited(mouseEvent -> {
       setStyle("-fx-background-color: "+backgroundColor+"; -fx-background-radius: "+borderRadius+"; -fx-padding: "+padding+";");

       icono.setStyle("-fx-background-color: "+textColor+";");
       textLabel.setStyle("-fx-text-fill: "+ textColor+";");
   });

    }
}

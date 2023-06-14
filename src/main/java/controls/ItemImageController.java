package controls;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.File;

public class ItemImageController extends Node {

    @FXML
    private Label lbl_nombre_foto;
    @FXML
    private ImageView imagen_imv;
    @FXML
    private AnchorPane btn_delete;

    public void setImage(String imgPath){
        File imagen = new File(imgPath);
        lbl_nombre_foto.setText(imagen.getName());
        imagen_imv.setImage(new Image(imagen.getAbsolutePath()));
    }

    public void setImage(Image ima, String name){
        imagen_imv.setImage(ima);
        lbl_nombre_foto.setText(name);
    }

    public AnchorPane getDeleteButton(){
        return this.btn_delete;
    }
    public void setHideDeleteButton(Boolean visible){
        btn_delete.setVisible(!visible);
    }
}

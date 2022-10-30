package controls;

import clases.Unidad;
import conexión.Conexión;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SubSceneUnitController implements Initializable {
    @FXML
    private AnchorPane root;
    @FXML
    private HBox layoutBox_container;

    @FXML
    private Pane empty_data_container;

    //Variables
    Conexión con = new Conexión();
    ArrayList<Unidad> listAllUnits;


    private void getAllUnitsFromDB(){
        listAllUnits = con.getALLUnitsFromDB();
        showUnitInUI(listAllUnits);
    }

    private void showUnitInUI(List<Unidad> elementos){

        if(elementos != null && elementos.size() > 0){
            root.getChildren().remove(empty_data_container);


        }else{
            if(!root.getChildren().contains(empty_data_container)){
                root.getChildren().add(empty_data_container);
            }
        }

    }

    public void handleNewUnit(){

    }

    public void handleSearch(){

    }

   public void handleChangeModelPhoto(){

   }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
      con.connectWidthDB();
     root.getChildren().remove(layoutBox_container);
     getAllUnitsFromDB();

    }
}

package controls;

import clases.Button_with_icon;
import clases.ModeloMaquina;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SubSceneModelController implements Initializable {
    @FXML
    private Pane modelo_empty_data_container;
    @FXML
    private Pane list_all_models_container;

    @FXML
     private HBox menu_item_model_container;

    //variables java
    ArrayList<ModeloMaquina> listaModelos = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listaModelos.add(new ModeloMaquina("sdsd", "sdsd"));

        Button_with_icon btn_showsDetails = new Button_with_icon();
        btn_showsDetails.makeContainer("white", "10px", "10px 15px", 10.0);
        btn_showsDetails.makeContentButton("icon_show_details", 15.0, "#161925","Mostrar detalles");
        btn_showsDetails.createButton();

        Button_with_icon btn_updateData = new Button_with_icon();
        btn_updateData.makeContainer("white", "10px", "10px 15px", 10.0);
        btn_updateData.makeContentButton("icon_update_details", 15.0, "#161925","Actualizar Modelo");
        btn_updateData.createButton();

        Button_with_icon btn_delete_model = new Button_with_icon();
        btn_delete_model.makeContainer("white", "10px", "10px 15px", 10.0);
        btn_delete_model.makeContentButton("icon_delete_model", 15.0, "#161925","Eliminar modelo");
        btn_delete_model.createButton();

        menu_item_model_container.getChildren().add(btn_showsDetails);
        menu_item_model_container.getChildren().add(btn_updateData);
        menu_item_model_container.getChildren().add(btn_delete_model);

        if(listaModelos.size() < 1){
          modelo_empty_data_container.setVisible(true);
          list_all_models_container.setVisible(false);
        }else{
            modelo_empty_data_container.setVisible(false);
            list_all_models_container.setVisible(true);
        }

    }
}

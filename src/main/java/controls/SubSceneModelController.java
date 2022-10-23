package controls;

import clases.Button_with_icon;
import clases.Constants;
import clases.ItemModelo;
import clases.Modelo;
import conexión.Conexión;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SubSceneModelController implements Initializable {
    @FXML
    private Pane modelo_empty_data_container;
    @FXML
    private Pane list_all_models_container;

    @FXML
    private HBox menu_item_model_container;

    @FXML
    private VBox aaside_list_models;
    @FXML
    private TextField txtBusqueda;

    //variables java
    ArrayList<Modelo> listaModelos = new ArrayList<>();
    Conexión con;

    private void getModelsDataToUI() {
       listaModelos = con.getModelosFromDB();

      /* Modelo modelo = new Modelo();
       modelo.setIdModelo("HM126-9.5");
       modelo.setMotor("9.5 hp Kohler Command Pro");

       listaModelos.add(modelo);*/

        for (Modelo item : listaModelos) {
            aaside_list_models.getChildren().add(new ItemModelo(item.getIdModelo(), item.getMotor()));
        }
    }


    /*
    * este método crea una nueva ventana para poder agregar una nueva unidad a la base datos
    * */

    public void handleShowAddUnitTODBForm() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/layout/addNewModel.fxml"));

        Scene scene = new Scene(loader.load(), 600, 665);
        scene.getStylesheets().add(getClass().getResource("/css/addUnit.css").toExternalForm());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }


    /*
    * Filtra las unidades que coincidan con la búsqueda y las muestra en la lista de unidades
    * */
    public void handleSearch() {
        aaside_list_models.getChildren().clear();
        List<Modelo> listaFiltrada = listaModelos.stream().filter(item -> item.getIdModelo().contains(txtBusqueda.getText())).toList();

        for (Modelo item : listaFiltrada) {
            aaside_list_models.getChildren().add(new ItemModelo(item.getIdModelo(), item.getMotor()));
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       con = new Conexión();
       con.connectWidthDB();

        getModelsDataToUI();

        Button_with_icon btn_showsDetails = new Button_with_icon();
        btn_showsDetails.makeContainer("white", "10px", "10px 15px", 10.0);
        btn_showsDetails.makeContentButton("icon_show_details", 15.0, "#161925", "Mostrar detalles");
        btn_showsDetails.createButton();

        Button_with_icon btn_updateData = new Button_with_icon();
        btn_updateData.makeContainer("white", "10px", "10px 15px", 10.0);
        btn_updateData.makeContentButton("icon_update_details", 15.0, "#161925", "Actualizar Modelo");
        btn_updateData.createButton();

        Button_with_icon btn_delete_model = new Button_with_icon();
        btn_delete_model.makeContainer("white", "10px", "10px 15px", 10.0);
        btn_delete_model.makeContentButton("icon_delete_model", 15.0, "#161925", "Eliminar modelo");
        btn_delete_model.createButton();

        menu_item_model_container.getChildren().add(btn_showsDetails);
        menu_item_model_container.getChildren().add(btn_updateData);
        menu_item_model_container.getChildren().add(btn_delete_model);

        if (listaModelos.size() < 1) {
            modelo_empty_data_container.setVisible(true);
            list_all_models_container.setVisible(false);
        } else {
            modelo_empty_data_container.setVisible(false);
            list_all_models_container.setVisible(true);
        }

    }
}

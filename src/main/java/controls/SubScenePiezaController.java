package controls;

import clases.ItemModelo;
import clases.Modelo;
import clases.Pieza;
import conexión.Conexión;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SubScenePiezaController implements Initializable {

    @FXML
    private Pane modelo_empty_data_container;
    @FXML
    private BorderPane pn_part_content;
    @FXML
    private VBox aaside_list_models;
    @FXML
    private TextField txtBusqueda;

    //variables de la subScene
    Conexión con;
    ArrayList<Pieza> listAllPiecesFromDB;

    //abre el formulario para añadir una nueva pieza a la BD
    public void handleAddNewPart() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/layout/addNewPart.fxml"));

        Scene scene = new Scene(loader.load(), 464, 476);
        scene.getStylesheets().add(getClass().getResource("/css/addUnit.css").toExternalForm());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }


    private void getPiecesDataToUI() {
        listAllPiecesFromDB = con.getAllPiecesFromDB();

        for (Pieza item : listAllPiecesFromDB) {

            aaside_list_models.getChildren().add(new ItemModelo(item.getNombrePieza(), "Piezas en inventario x" + item.getCantidadEnInvetario()));
        }
    }


    /*
     * Filtra las unidades que coincidan con la búsqueda y las muestra en la lista de unidades
     * */
    public void handleSearch() {
        aaside_list_models.getChildren().clear();
        List<Pieza> listaFiltrada = listAllPiecesFromDB.stream().filter(item -> item.getNombrePieza().contains(txtBusqueda.getText())).toList();

        for (Pieza item : listaFiltrada) {
            aaside_list_models.getChildren().add(new ItemModelo(item.getNombrePieza(), "Piezas en inventario x" + item.getCantidadEnInvetario()));
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = new Conexión();
        con.connectWidthDB();

        getPiecesDataToUI();

        if (listAllPiecesFromDB.size() > 0) {
            modelo_empty_data_container.setVisible(false);
            pn_part_content.setVisible(true);

        } else {
            modelo_empty_data_container.setVisible(true);
            pn_part_content.setVisible(false);
        }
    }
}

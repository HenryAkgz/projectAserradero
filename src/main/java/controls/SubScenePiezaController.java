package controls;

import clases.*;
import conexión.Conexión;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;
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
    @FXML
    private VBox pane_waiting_clic;
    @FXML
    private VBox pane_infoPieza;
    @FXML
    private ImageView photoPieza_imageView;
    @FXML
    private Label nombrePieza_label;
    @FXML
    private Label cantidadEnInventario_label;
    @FXML
    private HBox menu_item_pieza_container;
    @FXML
    private VBox form_aside_container;


    //variables de la subScene
    Conexión con;
    ArrayList<Pieza> listAllPiecesFromDB;
    ArrayList<ItemPieza> listaAllPiecesUI;

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
            ItemPieza itemPieza = new ItemPieza(item.getNombrePieza(), item.getCantidadEnInvetario());

            itemPieza.setOnMouseClicked(mouseEvent -> {
                mostrarInfoPieza(item);
            });

            aaside_list_models.getChildren().add(itemPieza);
        }
    }


    public void mostrarInfoPieza(Pieza pieza) {
        if (!pn_part_content.getChildren().contains(pane_infoPieza)) {
            pane_waiting_clic.setVisible(false);
            pn_part_content.getChildren().remove(pane_waiting_clic);

            pn_part_content.getChildren().add(pane_infoPieza);
            pane_infoPieza.setVisible(true);
          
        }

        photoPieza_imageView.setImage(new Image(new ByteArrayInputStream(pieza.getPhotoPieza())));
        nombrePieza_label.setText(pieza.getNombrePieza());
        cantidadEnInventario_label.setText(pieza.getCantidadEnInvetario() + "x Piezas en el inventario");
    }


    public void loadButtonsInfoMenu() {
        Button_with_icon btnDeletePieza = new Button_with_icon();
        btnDeletePieza.makeContainer("white", "10px", "10px 15px", 10.0);
        btnDeletePieza.makeContentButton("icon_delete_model", 15.0, "#161925", "Eliminar Pieza");
        btnDeletePieza.createButton();


        Button_with_icon btnUpdatePieza = new Button_with_icon();
        btnUpdatePieza.makeContainer("white", "10px", "10px 15px", 10.0);
        btnUpdatePieza.makeContentButton("icon_update_details", 15.0, "#161925", "Actualizar Pieza");
        btnUpdatePieza.createButton();

        btnUpdatePieza.setOnMouseClicked(mouseEvent -> {
            if (pn_part_content.getChildren().contains(form_aside_container)) {
                pn_part_content.getChildren().remove(form_aside_container);
            } else {
                pn_part_content.getChildren().add(form_aside_container);
            }
        });


        menu_item_pieza_container.getChildren().add(btnUpdatePieza);
        menu_item_pieza_container.getChildren().add(btnDeletePieza);
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
            loadButtonsInfoMenu();
            pane_infoPieza.setVisible(false);
            pn_part_content.getChildren().remove(form_aside_container);
            pn_part_content.getChildren().remove(pane_infoPieza);
        } else {
            modelo_empty_data_container.setVisible(true);
            pn_part_content.setVisible(false);
        }
    }
}

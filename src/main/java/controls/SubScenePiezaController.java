package controls;

import clases.*;
import conexión.Conexión;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.xml.transform.Result;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SubScenePiezaController implements Initializable {
    @FXML
    private AnchorPane root;
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
    private BorderPane pane_infoPieza;
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
    @FXML
    private ImageView update_photo_imageView;
    @FXML
    private TextField updateNombrePieza_textfield;
    @FXML
    private Button updatePhoto_button;
    @FXML
    private Spinner updateCantidadInventario_spinner;
    //variables de la subScene
    Conexión con;
    ArrayList<Pieza> listAllPiecesFromDB;
    ArrayList<ItemPieza> listaAllPiecesUI;
    Pieza currentPieza;

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

        if (listAllPiecesFromDB != null && listAllPiecesFromDB.size() > 0) {
            for (Pieza item : listAllPiecesFromDB) {
                ItemPieza itemPieza = new ItemPieza(item.getNombrePieza(), item.getCantidadEnInvetario());

                itemPieza.setOnMouseClicked(mouseEvent -> {
                    mostrarInfoPieza(item);
                    currentPieza = item;
                });

                aaside_list_models.getChildren().add(itemPieza);
            }


            modelo_empty_data_container.setVisible(false);
            pn_part_content.setVisible(true);
            loadButtonsInfoMenu();
            updateCantidadInventario_spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999999));
            root.getChildren().remove(pane_infoPieza);
            pn_part_content.getChildren().remove(form_aside_container);

        } else {
            modelo_empty_data_container.setVisible(true);
            pn_part_content.setVisible(false);
        }


    }


    public void mostrarInfoPieza(Pieza pieza) {
        if (!pn_part_content.getChildren().contains(pane_infoPieza)) {
            pane_waiting_clic.setVisible(false);
            pn_part_content.getChildren().remove(pane_waiting_clic);
            pn_part_content.setCenter(pane_infoPieza);
            pane_infoPieza.setVisible(true);
        }

        if (pn_part_content.getChildren().contains(form_aside_container)) {
            pn_part_content.getChildren().remove(form_aside_container);
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
                pn_part_content.setRight(form_aside_container);
                update_photo_imageView.setImage(new Image(new ByteArrayInputStream(currentPieza.getPhotoPieza())));
                updateNombrePieza_textfield.setText(currentPieza.getNombrePieza());
                updateCantidadInventario_spinner.getValueFactory().setValue((int) currentPieza.getCantidadEnInvetario());
            }
        });

        btnDeletePieza.setOnMouseClicked(mouseEvent -> {
            Alert mensaje = new Alert(Alert.AlertType.CONFIRMATION);
            mensaje.setTitle("Eliminar pieza");
            mensaje.setContentText("¿Desea eliminar la siguiente pieza? \n"+currentPieza.getNombrePieza());
            mensaje.showAndWait();

            if(mensaje.getResult() == ButtonType.OK){
                Alert aviso;

                if(con.deletePartFromBD(currentPieza.getId_Pieza())){
                    aviso = new Alert(Alert.AlertType.INFORMATION);
                    aviso.setTitle("Elemento eliminado!");
                    aviso.setContentText("La pieza se elimino correctamente");
                    listAllPiecesFromDB = new ArrayList<>();
                    aaside_list_models.getChildren().clear();
                    pn_part_content.getChildren().remove(pane_infoPieza);
                    pn_part_content.setCenter(pane_waiting_clic);
                    pane_infoPieza.setVisible(true);
                    getPiecesDataToUI();
                }else{
                   aviso = new Alert(Alert.AlertType.ERROR);
                    aviso.setTitle("Error!");
                    aviso.setContentText("Algo salio mal y no pudimos eliminar la pieza");
                }

                aviso.show();


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

    }
}

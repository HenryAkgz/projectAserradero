package controls;

import clases.Button_with_icon;
import clases.ItemPieza;
import clases.Pieza;
import clases.Util;
import conexión.Conexión;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class SubScenePiezaController implements Initializable {
    //variables de la subScene
    Conexión con;
    ArrayList<Pieza> listAllPiecesFromDB;
    Pieza currentPieza;
    Pieza piezaActualizada;
    @FXML
    private AnchorPane root;
    @FXML
    private HBox layoutBox_container;
    @FXML
    private Pane empty_data_container;
    @FXML
    private BorderPane pn_part_content;
    @FXML
    private VBox aside_list_pieces;
    @FXML
    private TextField txtBúsqueda;
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
    private TextField updateNombrePieza_textField;
    @FXML
    private Spinner<Integer> updateCantidadInventario_spinner;
    @FXML
    private BorderPane formUpdatePieza;
    @FXML
    private BorderPane formDeletePart;
    @FXML
    private ImageView imvMensaje;
    @FXML
    private Label lblMensaje;
    @FXML
    private BorderPane formMensaje;
    @FXML
    private Label nombreDeleteLabel;


    /* #############################################  Métodos de clase  ############################################## */

    /*
     * se ejecuta al cargar el stage
     * inicia la conexión con la Base de datos;
     * remueve los forms
     * llama a getAllPiecesFromDB
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = new Conexión();
        root.getChildren().remove(layoutBox_container);
        getAllPiecesFromDB();
    }

    //Trae todas las piezas guardadas de la base de datos.
    private void getAllPiecesFromDB() {
        listAllPiecesFromDB = con.getAllPiecesFromDB();
        showPiecesInUI(listAllPiecesFromDB);
    }

    //Carga en la interfaz los datos de las piezas de lisAllPiecesFromDB.
    private void showPiecesInUI(List<Pieza> list) {
        if (list != null && list.size() > 0) {

            //muestra la interfaz de control si no está cargada
            if (!root.getChildren().contains(pn_part_content)) {
                //remueve el panel que se muestra cuando no hay elementos para mostrar
                root.getChildren().remove(empty_data_container);
                //Añade el panel de control principal
                root.getChildren().add(pn_part_content);
                //carga los botones de control
                loadButtonsInfoMenu();
            }

            //se eliminan todos los componentes que pueda tener la lista gráfica
            aside_list_pieces.getChildren().clear();

            list.forEach(item -> {
                //se crea el item grafico
                ItemPieza u = new ItemPieza(item.getNombrePieza(), item.getCantidadEnInvetario());
                //se le asigna una función
                u.setOnMouseClicked(action -> mostrarInfoPieza(item));
                //se añada a la interfaz grafica
                aside_list_pieces.getChildren().add(u);
            });

        } else {
            if ((listAllPiecesFromDB == null || listAllPiecesFromDB.size() == 0) && !root.getChildren().contains(empty_data_container)) {
                root.getChildren().clear();
                root.getChildren().add(empty_data_container);
            }

        }
    }

    //Carga en la interfaz los botones de actualizar y eliminar Pieza.
    public void loadButtonsInfoMenu() {
        Button_with_icon btnDeletePieza = new Button_with_icon();
        btnDeletePieza.makeContainer("white", "10px", "10px 15px", 10.0);
        btnDeletePieza.makeContentButton("icon_delete_model", 15.0, "#161925", "Eliminar Pieza");
        btnDeletePieza.createButton();

        Button_with_icon btnUpdatePieza = new Button_with_icon();
        btnUpdatePieza.makeContainer("white", "10px", "10px 15px", 10.0);
        btnUpdatePieza.makeContentButton("icon_update_details", 15.0, "#161925", "Actualizar Pieza");
        btnUpdatePieza.createButton();

        btnUpdatePieza.setOnMouseClicked(mouseEvent -> showUpdate());

        btnDeletePieza.setOnMouseClicked(mouseEvent -> showDelete());

        menu_item_pieza_container.getChildren().add(btnUpdatePieza);
        menu_item_pieza_container.getChildren().add(btnDeletePieza);
    }

    //muestra la información de la pieza en la interfaz de control.
    public void mostrarInfoPieza(Pieza item) {

        if (pn_part_content.getChildren().contains(pane_waiting_clic)) {
            pn_part_content.getChildren().remove(pane_waiting_clic);
            pn_part_content.setCenter(pane_infoPieza);
        }
        currentPieza = item;
        photoPieza_imageView.setImage(new Image(new ByteArrayInputStream(item.getPhotoPieza())));
        nombrePieza_label.setText(item.getNombrePieza());
        cantidadEnInventario_label.setText(item.getCantidadEnInvetario() + "x Piezas en el inventario");
    }

    /*
     * Muestra u oculta la ventana lateral en la interfaz, al mostrar la ventana
     * la rellena con el formulario node
     * */
    private void mostrarForm(Node node) {
        if (isVisibleFormContainer()) {
            if (form_aside_container.getChildren().contains(node)) {
                pn_part_content.getChildren().remove(form_aside_container);
            } else {
                form_aside_container.getChildren().clear();
                form_aside_container.getChildren().add(node);
            }

        } else {
            pn_part_content.setRight(form_aside_container);
            form_aside_container.getChildren().clear();
            form_aside_container.getChildren().add(node);
        }
    }

    //retorna true si el contenedor derecho está abierto, y si está cerrado retorna false.
    private boolean isVisibleFormContainer() {
        return pn_part_content.getChildren().contains(form_aside_container);
    }

    /* ****************************************** Métodos Update part ******************************* */

    //muestra el formulario para actualizar los datos de la pieza.
    private void showUpdate() {
        mostrarForm(formUpdatePieza);
        piezaActualizada = new Pieza();
        update_photo_imageView.setImage(new Image(new ByteArrayInputStream(currentPieza.getPhotoPieza())));
        updateNombrePieza_textField.setText(currentPieza.getNombrePieza());
        updateCantidadInventario_spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999999));
        updateCantidadInventario_spinner.getValueFactory().setValue(currentPieza.getCantidadEnInvetario());
    }

    //actualiza la interfaz cada que se actualiza un elemento
    private void updateUI() {
        getAllPiecesFromDB();
        mostrarInfoPieza(listAllPiecesFromDB.stream().filter(item -> item.getId_Pieza() == currentPieza.getId_Pieza()).findFirst().get());
    }

    /* ****************************************** Métodos Delete part ******************************* */

    //muestra el formulario para eliminar el elemento pieza de la Base de datos.
    private void showDelete() {
        mostrarForm(formDeletePart);
        nombreDeleteLabel.setText(currentPieza.getNombrePieza());
    }

    /* #############################################  Métodos handle  ############################################# */

    /* ****************************************** Métodos ventana lateral izquierda ***************************** */

    //Abre el formulario para añadir una nueva pieza a la BD.
    public void handleAddNewPart() {
        Runnable addPartThread = new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/layout/addNewPart.fxml"));
                Scene scene;
                try {
                    scene = new Scene(loader.load(), 464, 476);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                scene.getStylesheets().add(getClass().getResource("/css/addUnit.css").toExternalForm());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.showAndWait();

                Platform.runLater(() -> getAllPiecesFromDB());

            }
        };

        addPartThread.run();
    }

    //Filtra las unidades que coincidan con la búsqueda y las muestra en la lista de unidades.
    public void handleSearch() {
        aside_list_pieces.getChildren().clear();
        List<Pieza> listaFiltrada = listAllPiecesFromDB.stream().filter(item -> item.getNombrePieza().toLowerCase().contains(txtBúsqueda.getText().toLowerCase())).toList();
        showPiecesInUI(listaFiltrada);
    }

    /* ****************************************** Métodos delete part ***************************** */

    //Borra la pieza seleccionada de la base de datos.
    public void handleDeleteOk() {
        String textResult;
        if (con.deletePartFromBD(currentPieza.getId_Pieza())) {
            textResult = "Pieza eliminada correctamente!";
            imvMensaje.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/deleteOk.gif"))));
            lblMensaje.setStyle("-fx-text-fill: green;");
        } else {
            textResult = "Ops! Algo salio mal...";
            imvMensaje.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/saveError.gif"))));
            lblMensaje.setStyle("-fx-text-fill: red;");
        }

        lblMensaje.setText(textResult);
        mostrarForm(formMensaje);
        getAllPiecesFromDB();
        pn_part_content.getChildren().remove(pane_infoPieza);
        pn_part_content.setCenter(pane_waiting_clic);
    }

    // cierra el cuadro de confirmación de eliminación de la pieza actual.
    public void handleDeleteCancel() {
        mostrarForm(formDeletePart);
    }

    /* ****************************************** Métodos Update part ******************************* */

    //Obtiene los datos del formulario update y actualiza la pieza correspondiente.
    public void handleUpdateData() {
        String textResult;
        piezaActualizada.setId_Pieza(currentPieza.getId_Pieza());
        piezaActualizada.setNombrePieza(updateNombrePieza_textField.getText());
        piezaActualizada.setCantidadEnInvetario(updateCantidadInventario_spinner.getValue());

        if (piezaActualizada.getPhotoPieza() == null) {
            piezaActualizada.setPhotoPieza(currentPieza.getPhotoPieza());
        }

        if (con.updatePart(piezaActualizada)) {
            textResult = "Pieza actualizada correctamente!";
            imvMensaje.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/saveOk.gif"))));
            lblMensaje.setStyle("-fx-text-fill: green;");
        } else {
            textResult = "Ops! Algo salio mal...";
            imvMensaje.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/saveError.gif"))));
            lblMensaje.setStyle("-fx-text-fill: red;");
        }

        lblMensaje.setText(textResult);

        mostrarForm(formMensaje);
        updateUI();
    }

    //carga una nueva foto al objeto pieza que se mandara para actualizar la pieza.
    public void handleChangePhotoUpdate() {
        String path = Util.getPhotoFromStorage("Actualizar foto de la pieza");
        if (path != null) {
            byte[] bytePhoto = Util.readFile(path);
            if (bytePhoto != null) {
                update_photo_imageView.setImage(new Image(new ByteArrayInputStream(bytePhoto)));
                piezaActualizada.setPhotoPieza(bytePhoto);
            }
        }
    }

    /* ****************************************** Métodos mensaje form ******************************* */

    //Cierra el formulario mensaje.
    public void handleCloseMessage() {
        mostrarForm(formMensaje);
    }

}

package controls;

import clases.*;
import conexión.Conexión;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

public class SubSceneModelController implements Initializable {
    @FXML
    private HBox layoutBox_container;
    @FXML
    private TextField txtBúsqueda;
    @FXML
    private VBox aside_list_models;
    @FXML
    private BorderPane pn_part_content;
    @FXML
    private VBox pane_waiting_clic;
    @FXML
    private BorderPane pane_infoPieza;
    @FXML
    private Pane empty_data_container;
    @FXML
    private AnchorPane root;
    @FXML
    private VBox form_aside_container;
    @FXML
    private ImageView photoModel_imageView;
    @FXML
    private Label nombreModel_label;
    @FXML
    private HBox menu_item_model_container;
    @FXML
    private ImageView name_imv_showdetails;
    @FXML
    private Label name_label_showdetails;
    @FXML
    private Label unidadesactivas_label_showdetais;
    @FXML
    private Label peso_label_showdetails;
    @FXML
    private Label motor_label_showdetails;
    @FXML
    private Label altura_label_showdetails;
    @FXML
    private Label marca_label_showDetails;
    @FXML
    private Label profundidad_label_showDetails;
    @FXML
    private Label capacidad_label_showDetails;
    @FXML
    private Label tipo_combustible_label_showDetails;
    @FXML
    private Label ancho_label_showDetails;
    @FXML
    private Label tamaño_ruedas_label_showDetails;
    @FXML
    private TextField peso_textField_update;
    @FXML
    private TextField motor_textField_update;
    @FXML
    private TextField altura_textField_update;
    @FXML
    private TextField ancho_textField_update;
    @FXML
    private TextField profundidad_textField_update;
    @FXML
    private TextField capacidad_textField_update;
    @FXML
    private TextField no_ruedas_textField_update;
    @FXML
    private TextField marca_textField_update;
    @FXML
    private ComboBox<String> combustible_comboBox_update;
    @FXML
    private ImageView fotoModelo_imv_update;
    @FXML
    private Label nameModel_label_update;

    @FXML
    private BorderPane formPiecesModelUpdate;
    @FXML
    private VBox piecesItemModelContainerUpdate;
    @FXML
    private VBox piecesGlobalContainerUpdate;
    @FXML
    private Label cantidadUnidades_label;
    @FXML
    private BorderPane formDataUpdateModel;
    @FXML
    private BorderPane formShowDetails;
    @FXML
    private BorderPane formDeleteModel;
    @FXML
    private BorderPane formMensaje;
    @FXML
    private ImageView imvMensaje;
    @FXML
    private Label lblMensaje;
    @FXML
    private Label nombreDeleteLabel;
    @FXML
    private TextField txtSearchUpdatePieces;


    //variables de la subScene
    Conexión con = Conexión.getInstancia();
    ArrayList<Modelo> listAllModelsFromDB;
    Modelo currentModelo;
    Modelo currentModeloUpdate;

    ArrayList<Pieza> piezaCurrentModelo;
    ArrayList<Pieza> allGlobalPieces;

    /* #############################################  Métodos de clase  ############################################## */

    /*
     * se ejecuta al cargar el stage
     * inicia la conexión con la Base de datos;
     * remueve los forms
     * llama a getAllPiecesFromDB
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.getChildren().remove(layoutBox_container);
        getAllModelsFromDB();
        combustible_comboBox_update.getItems().addAll("Diésel", "Gasolina", "Híbridos", "Eléctrico");
    }

    //Trae todos los modelos guardadas de la base de datos.
    public void getAllModelsFromDB() {
        listAllModelsFromDB = con.obtenerTodosLosModelos();
        showModelsInUI(listAllModelsFromDB);
    }

    //Carga en la interfaz los datos de los modelos de listAllModelsFromDB.
    private void showModelsInUI(List<Modelo> lista) {

        if (lista != null && lista.size() > 0) {

            //muestra la interfaz de control si no está cargada
            if (!root.getChildren().contains(pn_part_content)) {
                //remueve el panel que se muestra cuando no hay elementos para mostrar
                root.getChildren().remove(empty_data_container);
                //Añade el panel de control principal
                root.getChildren().add(pn_part_content);
                //carga los botones de control
                loadOptionButtonItemModel();
            }

            //se eliminan todos los componentes que pueda tener la lista gráfica
            aside_list_models.getChildren().clear();

            lista.forEach(item -> {
                //se crea el item grafico
                ItemModelo modeloUI = new ItemModelo(item.getId_modelo(), item.getMotor());
                //se le asigna una función
                modeloUI.setOnMouseClicked(mouseEvent -> mostrarInfoModelo(item));
                //se añada a la interfaz grafica
                aside_list_models.getChildren().add(modeloUI);
            });

        } else {
            if (listAllModelsFromDB.size() == 0 && !root.getChildren().contains(empty_data_container)) {
                root.getChildren().clear();
                root.getChildren().add(empty_data_container);
            }
        }

    }

    //Carga en la interfaz los botones de mostrar detalles, actualizar y eliminar modelo.
    private void loadOptionButtonItemModel() {

        Button_with_icon modelButtonDelete = new Button_with_icon();
        modelButtonDelete.makeContainer("white", "10px", "10px 15px", 10.0);
        modelButtonDelete.makeContentButton("icon_delete_model", 15.0, "#161925", "Eliminar modelo");
        modelButtonDelete.createButton();
        modelButtonDelete.setOnMouseClicked(mouseEvent -> showDelete());

        Button_with_icon modelButtonUpdate = new Button_with_icon();
        modelButtonUpdate.makeContainer("white", "10px", "10px 15px", 10.0);
        modelButtonUpdate.makeContentButton("icon_update_details", 15.0, "#161925", "Actualizar modelo");
        modelButtonUpdate.createButton();
        modelButtonUpdate.setOnMouseClicked(mouseEvent -> showUpdate());

        Button_with_icon modelButtonShowDetails = new Button_with_icon();
        modelButtonShowDetails.makeContainer("white", "10px", "10px 15px", 10.0);
        modelButtonShowDetails.makeContentButton("icon_show_details", 15.0, "#161925", "Mostrar detalles");
        modelButtonShowDetails.createButton();
        modelButtonShowDetails.setOnMouseClicked(mouseEvent -> showDetails());

        menu_item_model_container.getChildren().addAll(modelButtonShowDetails, modelButtonUpdate, modelButtonDelete);

    }

    //muestra la información del modelo en la interfaz de control.
    private void mostrarInfoModelo(Modelo item) {
        if (pn_part_content.getChildren().contains(pane_waiting_clic)) {
            pn_part_content.getChildren().remove(pane_waiting_clic);
            pn_part_content.setCenter(pane_infoPieza);
        }
        photoModel_imageView.setImage(new Image(new ByteArrayInputStream(item.getFoto_modelo())));
        nombreModel_label.setText(item.getId_modelo());
        currentModelo = item;
        cantidadUnidades_label.setText(con.obtenerTotalUnidadesPorModelo(item.getId_modelo()) + " Unidades");
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

    /* ****************************************** Métodos details Model ******************************* */

    //muestra el form en la ventana lateral izquierda con toda la información sobre el modelo.
    private void showDetails() {
        mostrarForm(formShowDetails);

        name_imv_showdetails.setImage(new Image(new ByteArrayInputStream(currentModelo.getFoto_modelo())));
        name_label_showdetails.setText(currentModelo.getId_modelo());
        unidadesactivas_label_showdetais.setText(con.obtenerTotalUnidadesPorModelo(currentModelo.getId_modelo()) + " Unidades");
        marca_label_showDetails.setText(currentModelo.getMarca());
        tipo_combustible_label_showDetails.setText(currentModelo.getTipo_combustible());
        motor_label_showdetails.setText(currentModelo.getMotor());
        peso_label_showdetails.setText(currentModelo.getPeso());
        altura_label_showdetails.setText(currentModelo.getAltura());
        ancho_label_showDetails.setText(currentModelo.getAncho());
        profundidad_label_showDetails.setText(currentModelo.getProfundidad());
        capacidad_label_showDetails.setText(currentModelo.getCapacidad_pasajeros());
        tamaño_ruedas_label_showDetails.setText(currentModelo.getNo_ruedas());

    }

    /* ****************************************** Métodos delete Model ******************************* */

    //muestra el formulario para eliminar el elemento modelo de la Base de datos.
    private void showDelete() {
        mostrarForm(formDeleteModel);
        nombreDeleteLabel.setText(currentModelo.getId_modelo());
    }

    /* ****************************************** Métodos update Model ******************************* */

    //Muestra el formulario para actualizar los datos del modelo.
    private void showUpdate() {
        mostrarForm(formDataUpdateModel);
        currentModeloUpdate = new Modelo();
        fotoModelo_imv_update.setImage(new Image(new ByteArrayInputStream(currentModelo.getFoto_modelo())));
        nameModel_label_update.setText(currentModelo.getId_modelo());
        marca_textField_update.setText(currentModelo.getMarca());
        peso_textField_update.setText(currentModelo.getPeso());
        combustible_comboBox_update.getSelectionModel().select(currentModelo.getTipo_combustible());
        motor_textField_update.setText(currentModelo.getMotor());
        altura_textField_update.setText(currentModelo.getAltura());
        ancho_textField_update.setText(currentModelo.getAncho());
        profundidad_textField_update.setText(currentModelo.getProfundidad());
        capacidad_textField_update.setText(currentModelo.getCapacidad_pasajeros());
        no_ruedas_textField_update.setText(currentModelo.getNo_ruedas());

    }


    /*
     * Crea un Item gráfico en la interfaz que hace referencia a las piezas pertenecientes al modelo,
     * asi como a todas las piezas disponibles en la base de datos que pueden para ser agregadas
     * a la lista del modelo
     *
     * Cada item gráfico tiene una función diferente dependiendo si se encuentra en la lista de piezas del
     * modelo, o de sí se encuentra en la lista de piezas globales.
     *
     * Los items gráficos de la lista de piezas del modelo eliminarán al instante la pieza del modelo en
     * la base de datos cuando se dé clic sobre ellas.
     *
     * Los item gráficos de la lista global añadirán al instante la pieza al modelo en la base datos.
     * */
    private void showInUIModelsPieces(String idModelo) {
        piecesItemModelContainerUpdate.getChildren().clear();
        piezaCurrentModelo = con.obtenerPiezasPorModelo(idModelo);

        piezaCurrentModelo.forEach(item -> {
            ItemPiezaContador i = new ItemPiezaContador(item, true, false);
            actionItemModelPieces(i, item);
            piecesItemModelContainerUpdate.getChildren().add(i);
            System.out.println(item.getId_Pieza());
        });

        allGlobalPieces = con.obtenerTodasLasPiezas();
        showAllGlobalPiecesInFormUpdate(allGlobalPieces);
    }

    //muestra en el formulario update todas las piezas disponibles en la bd
    private void showAllGlobalPiecesInFormUpdate(List<Pieza> lista) {
        piecesGlobalContainerUpdate.getChildren().clear();
        lista.forEach(item -> {
            actionItemGlobalPart(item);
        });
    }

    //añade una pieza al modelo al instante en la base de datos.
    private void actionItemGlobalPart(Pieza item) {
        ItemPiezaContador itemPiezaContador = new ItemPiezaContador(item, false);

        itemPiezaContador.setOnMouseClicked(mouseEvent -> {

            if (!piezaCurrentModelo.stream().anyMatch(element -> item.getId_Pieza() == element.getId_Pieza())) {
                ItemPiezaContador modeloPart = new ItemPiezaContador(item, true);
                actionItemModelPieces(modeloPart, item);
                piecesItemModelContainerUpdate.getChildren().add(modeloPart);
                piezaCurrentModelo.add(item);
            }


        });
        piecesGlobalContainerUpdate.getChildren().add(itemPiezaContador);
    }

    //elimina una pieza del modelo al instante en la base de datos
    private void actionItemModelPieces(ItemPiezaContador e, Pieza item) {
        e.setOnMouseClicked(mouseEvent1 -> {
            if (!con.existeRelacionModeloPieza(currentModeloUpdate.getId_modelo(), item.getId_Pieza()) || con.eliminarRelacionModeloPieza(currentModeloUpdate.getId_modelo(), item.getId_Pieza())) {
                piecesItemModelContainerUpdate.getChildren().remove(e);
                piezaCurrentModelo.removeIf(elemento -> elemento.getId_Pieza() == item.getId_Pieza());
            }
        });
    }

    //manda los datos a la BD para actualizar el modelo.
    private void updateDataModel() {
        if (con.actualizarModelo(currentModeloUpdate)) {

            if (piezaCurrentModelo.size() > 0) {

                //retorna true si todas las piezas de la lista del modelo se guardan o editan de manera correcta
                //de lo contrario retorna false
                boolean resultado = piezaCurrentModelo.stream().map(item -> {
                    if (con.existeRelacionModeloPieza(currentModeloUpdate.getId_modelo(), item.getId_Pieza())) {
                        return con.actualizarCantidadRelacionPiezasModelo(currentModeloUpdate.getId_modelo(), item.getId_Pieza(), item.getCantidadEnInvetario());
                    } else {
                        return con.agregarRelacionModeloPieza(currentModeloUpdate.getId_modelo(), item.getId_Pieza(), item.getCantidadEnInvetario());
                    }
                }).noneMatch(item -> !item);

                showUpdateResult(resultado);

            } else {
                showUpdateResult(true);
            }

        } else {
            showUpdateResult(false);
        }

        updateUI();
    }

    /*
     * muestra el formMensaje, con un mensaje positivo si la actualización fue exitosa,
     * y un mensaje negativo si la actualización termino con errores.
     * */
    private void showUpdateResult(Boolean result) {

        String textResult = "";

        if (result) {
            textResult = "Modelo actualizado correctamente!";
            imvMensaje.setImage(new Image(getClass().getResourceAsStream("/icons/saveOk.gif")));
            lblMensaje.setStyle("-fx-text-fill: green;");
        } else {
            textResult = "Ops! Algo salio mal...";
            imvMensaje.setImage(new Image(getClass().getResourceAsStream("/icons/saveError.gif")));
            lblMensaje.setStyle("-fx-text-fill: red;");
        }
        lblMensaje.setText(textResult);
        mostrarForm(formMensaje);
    }

    //actualiza la interfaz cada que se actualiza un elemento
    private void updateUI() {
        getAllModelsFromDB();

        mostrarInfoModelo(listAllModelsFromDB.stream().filter(item -> item.getId_modelo().equals(currentModelo.getId_modelo())).findFirst().get());
    }


    /* ##############################################  Métodos handle  ############################################### */

    /* ****************************************** Métodos ventana lateral izquierda ***************************** */

    //Abre el formulario para añadir un nuevo modelo a la BD.
    public void handleAddNewModel() {
        Runnable addNewModelThread = new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/layout/addNewModel.fxml"));
                Scene scene;
                try {
                    scene = new Scene(loader.load(), 600, 665);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                scene.getStylesheets().add(getClass().getResource("/css/addUnit.css").toExternalForm());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.showAndWait();
                Platform.runLater(() -> getAllModelsFromDB());
            }
        };

        addNewModelThread.run();
    }

    //Este método busca entre la lista de modelos, algún elemento que
    //coincida con la búsqueda.
    public void handleSearch() {
        aside_list_models.getChildren().clear();
        List<Modelo> listaFiltrada = listAllModelsFromDB.stream().filter(item -> item.getId_modelo().toLowerCase().contains(txtBúsqueda.getText().toLowerCase())).toList();
        showModelsInUI(listaFiltrada);
    }


    /* ****************************************** Métodos delete part ***************************** */

    //Borra la pieza seleccionada de la base de datos.
    public void handleDeleteOk() {
        String textResult;
        if (con.eliminarModelo(currentModelo.getId_modelo())) {
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
        getAllModelsFromDB();
        pn_part_content.getChildren().remove(pane_infoPieza);
        pn_part_content.setCenter(pane_waiting_clic);
    }

    // cierra el cuadro de confirmación de eliminación de la pieza actual.
    public void handleDeleteCancel() {
        mostrarForm(formDeleteModel);
    }

    /* ****************************************** Métodos mensaje form ******************************* */

    //Cierra el formulario mensaje.
    public void handleCloseMessage() {
        mostrarForm(formMensaje);
    }

    /* ****************************************** Métodos Update Model ******************************* */

    //carga una nueva foto al objeto modelo que se mandara para actualizar el modelo en la BD.
    public void handleChangePhotoUpdate() {
        String path = Util.getPhotoFromStorage("Actualizar foto de la pieza");
        if (path != null) {
            byte[] bytePhoto = Util.readFile(path);
            if (bytePhoto != null) {
                fotoModelo_imv_update.setImage(new Image(new ByteArrayInputStream(bytePhoto)));
                currentModeloUpdate.setFoto_modelo(bytePhoto);
            }
        }
    }

    //guarda los datos en una clase modelo
    public void handleNextDataUpdateButton() {

        currentModeloUpdate.setId_modelo(currentModelo.getId_modelo());
        currentModeloUpdate.setMarca(marca_textField_update.getText().trim());
        currentModeloUpdate.setPeso(peso_textField_update.getText().trim());
        currentModeloUpdate.setTipo_combustible(combustible_comboBox_update.getSelectionModel().getSelectedItem());
        currentModeloUpdate.setMotor(motor_textField_update.getText().trim());
        currentModeloUpdate.setAltura(altura_textField_update.getText().trim());
        currentModeloUpdate.setAncho(ancho_textField_update.getText().trim());
        currentModeloUpdate.setProfundidad(profundidad_textField_update.getText().trim());
        currentModeloUpdate.setCapacidad_pasajeros(capacidad_textField_update.getText().trim());
        currentModeloUpdate.setNo_ruedas(no_ruedas_textField_update.getText().trim());

        if (currentModeloUpdate.getFoto_modelo() == null) {
            currentModeloUpdate.setFoto_modelo(currentModelo.getFoto_modelo());
        }

        mostrarForm(formPiecesModelUpdate);

        showInUIModelsPieces(currentModelo.getId_modelo());
    }

    //ejecuta el método que actualiza los datos del modelo en la BD.
    public void handleNextPiecesUpdateButton() {
        updateDataModel();
    }

    //método de búsqueda, filtra de entre todas las piezas disponibles en la bd, las que coincidan con los terminus de búsqueda
    public void handleSearchUpdate() {
        List<Pieza> listaFiltrada = allGlobalPieces.stream().filter(item -> item.getNombrePieza().toLowerCase().contains(txtSearchUpdatePieces.getText().toLowerCase())).toList();
        showAllGlobalPiecesInFormUpdate(listaFiltrada);
    }


}

package controls;

import clases.*;
import conexión.Conexión;
import conexión.Conexión_old;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SubSceneUnitController implements Initializable {
    //Variables
    Conexión con;
    ArrayList<Unidad> listAllUnits;
    ArrayList<Modelo> listAllModels;
    Unidad currentUnidad;
    Unidad unidadUpdateUnit;
    @FXML
    private AnchorPane root;
    @FXML
    private HBox layoutBox_container;
    @FXML
    private Pane empty_data_container;
    @FXML
    private BorderPane pn_part_content;
    @FXML
    private VBox aside_list_units;
    @FXML
    private VBox pane_waiting_clic;
    @FXML
    private BorderPane pane_infoUnidad;
    @FXML
    private HBox menu_item_unit_container;
    @FXML
    private Label lblModeloUnidad;
    @FXML
    private Label lblNombreUnidad;
    @FXML
    private Label lblEstadoUnidad;
    @FXML
    private ImageView imvFotoUnidad;
    @FXML
    private VBox form_aside_container;
    @FXML
    private BorderPane pn_showDetails;
    @FXML
    private BorderPane pn_update;
    @FXML
    private ImageView foto_imv_showDetails;
    @FXML
    private Label name_label_showDetails;
    @FXML
    private Label modelo_label_showDetails;
    @FXML
    private Label estado_label_showDetails;
    @FXML
    private Label ultimoMantenimiento_label_showDetails;
    @FXML
    private Label próximoMantenimiento_label_showDetails;
    @FXML
    private Label notasUnidad_label_showDetail;
    @FXML
    private VBox formUpdateData;
    @FXML
    private ImageView imvFotoUpdate;
    @FXML
    private Label labelNameUpdate;
    @FXML
    private ComboBox<String> comboBoxModeloUpdate;
    @FXML
    private ComboBox<String> comboBoxEstadoUpdate;
    @FXML
    private TextArea textAreaNotasUpdate;
    @FXML
    private ImageView imvMensaje;
    @FXML
    private Label lblMensaje;
    @FXML
    private BorderPane formMensaje;
    @FXML
    private Button updateDataButtonUpdate;
    @FXML
    private TextField txtBúsqueda;
    @FXML
    private BorderPane formDeleteUnit;
    @FXML
    private Label nombreDeleteLabel;
    @FXML
    private Button btnDeleteOk;
    @FXML
    private Button btnDeleteCancel;

    /* #############################################  Métodos de clase  ############################################## */

    /*
     * se ejecuta al cargar el stage
     * inicia la conexión con la Base de datos;
     * remueve los forms
     * llama a getAllUnitsFromDB
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = Conexión.getInstancia();
        root.getChildren().remove(layoutBox_container);
        getAllUnitsFromDB();

        listAllModels = con.obtenerTodosLosModelos();
        listAllModels.forEach(item -> comboBoxModeloUpdate.getItems().add(item.getId_modelo()));
        comboBoxEstadoUpdate.getItems().addAll(Constants.ESTADO_UNIDAD_INACTIVO, Constants.ESTADO_UNIDAD_ACTIVO);
    }

    //Trae todas las unidades guardadas de la base de datos.
    private void getAllUnitsFromDB() {
        listAllUnits = con.obtenerTodasLasUnidades();
        showUnitInUI(listAllUnits);
    }

    //Carga en la interfaz los datos de las unidades de listAllUnits.
    private void showUnitInUI(List<Unidad> elementos) {

        if (elementos != null && elementos.size() > 0) {

            //muestra la interfaz de control si no está cargada
            if (!root.getChildren().contains(pn_part_content)) {
                //remueve el panel que se muestra cuando no hay elementos para mostrar
                root.getChildren().remove(empty_data_container);
                //Añade el panel de control principal
                root.getChildren().add(pn_part_content);
                //carga los botones de control
                loadUnitItemButtons();
            }

            //se eliminan todos los componentes que pueda tener la lista gráfica
            aside_list_units.getChildren().clear();

            elementos.forEach(item -> {
                ItemUnidad unitUI = new ItemUnidad(item);
                unitUI.setOnMouseClicked(action -> mostrarInfoUnidad(item));
                aside_list_units.getChildren().add(unitUI);
            });

        } else {
            if (listAllUnits.size() == 0 && !root.getChildren().contains(empty_data_container)) {
                root.getChildren().clear();
                root.getChildren().add(empty_data_container);
            }
        }

    }

    //Carga en la interfaz los botones de mostrar detalles, mantenimiento, actualizar y eliminar unidad.
    public void loadUnitItemButtons() {
        Button_with_icon unitButtonDelete = new Button_with_icon();
        unitButtonDelete.makeContainer("white", "10px", "10px 15px", 10.0);
        unitButtonDelete.makeContentButton("icon_delete_model", 15.0, "#161925", "Eliminar unidad");
        unitButtonDelete.createButton();
        unitButtonDelete.setOnMouseClicked(mouseEvent -> showDelete());


        Button_with_icon unitButtonUpdate = new Button_with_icon();
        unitButtonUpdate.makeContainer("white", "10px", "10px 15px", 10.0);
        unitButtonUpdate.makeContentButton("icon_update_details", 15.0, "#161925", "Actualizar unidad");
        unitButtonUpdate.createButton();
        unitButtonUpdate.setOnMouseClicked(mouseEvent -> showUpdate());

        Button_with_icon unitButtonShowDetails = new Button_with_icon();
        unitButtonShowDetails.makeContainer("white", "10px", "10px 15px", 10.0);
        unitButtonShowDetails.makeContentButton("icon_show_details", 15.0, "#161925", "Mostrar detalles");
        unitButtonShowDetails.createButton();
        unitButtonShowDetails.setOnMouseClicked(mouseEvent -> showDetails());

        Button_with_icon unitButtonShowHistory = new Button_with_icon();
        unitButtonShowHistory.makeContainer("white", "10px", "10px 15px", 10.0);
        unitButtonShowHistory.makeContentButton("icon_mantenimiento", 15.0, "#161925", "Mantenimiento");
        unitButtonShowHistory.createButton();
        unitButtonShowHistory.setOnMouseClicked(mouseEvent -> {
            try{
                //showMaintenance();
                showAlternativeMaintenance();
            }catch(OutOfMemoryError e){
                System.out.println("Error de memoria: "+e.getMessage());
            }
        });


        menu_item_unit_container.getChildren().addAll(unitButtonShowDetails, unitButtonShowHistory, unitButtonUpdate, unitButtonDelete);

    }

    //muestra la información del modelo en la interfaz de control.
    public void mostrarInfoUnidad(Unidad item) {
        if (pn_part_content.getChildren().contains(pane_waiting_clic)) {
            pn_part_content.getChildren().remove(pane_waiting_clic);
            pn_part_content.setCenter(pane_infoUnidad);
        }
        currentUnidad = item;
        lblNombreUnidad.setText(item.getId_unidad());
        lblModeloUnidad.setText(item.getId_modelo());
        lblEstadoUnidad.setText(item.getEstado());
        lblEstadoUnidad.setStyle(Util.getStyleByState(item.getEstado()));

        imvFotoUnidad.setImage(new Image(new ByteArrayInputStream(item.getPhotoUnidad())));
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

    /* ****************************************** Métodos details Unit ******************************* */

    //muestra el form en la ventana lateral izquierda con toda la información sobre la unidad.
   public void showDetails() {
        mostrarForm(pn_showDetails);
        foto_imv_showDetails.setImage(new Image(new ByteArrayInputStream(currentUnidad.getPhotoUnidad())));
        name_label_showDetails.setText(currentUnidad.getId_unidad());
        modelo_label_showDetails.setText(currentUnidad.getId_modelo());
        estado_label_showDetails.setText(currentUnidad.getEstado());
        estado_label_showDetails.setStyle(Util.getStyleByState(currentUnidad.getEstado()));

       LocalDate proximoMantenimientoProgramado = con.obtenerFechaProximoMantenimiento(currentUnidad.getId_unidad());

       if(proximoMantenimientoProgramado == null){
           próximoMantenimiento_label_showDetails.setText(Constants.UNIDAD_SIN_MANTENIMIENTO_PROGRAMADO);
       }else{
           próximoMantenimiento_label_showDetails.setText(proximoMantenimientoProgramado.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
       }

       LocalDate ultimoMantenimientoRealizado = con.obtenerFechaDelUltimoMantenimientoRealizado(currentUnidad.getId_unidad());

       if(ultimoMantenimientoRealizado == null){
           ultimoMantenimiento_label_showDetails.setText(Constants.UNIDAD_SIN_MANTENIMIENTO);
       }else{
           ultimoMantenimiento_label_showDetails.setText(ultimoMantenimientoRealizado.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
       }

        if (currentUnidad.getNotas_de_la_unidad() == null || currentUnidad.getNotas_de_la_unidad().isBlank()) {
            notasUnidad_label_showDetail.setText("Sin descripción");
        } else {
            notasUnidad_label_showDetail.setText(currentUnidad.getNotas_de_la_unidad());
        }
    }

    /* ****************************************** Métodos delete Model ******************************* */

    //muestra el formulario para eliminar el elemento Unidad de la Base de datos.
    private void showDelete() {
        mostrarForm(formDeleteUnit);
        nombreDeleteLabel.setText(currentUnidad.getId_unidad());
    }

    //Muestra el formulario para actualizar los datos de la Unidad.
    public void showUpdate() {
        mostrarForm(pn_update);

        unidadUpdateUnit = new Unidad();
        imvFotoUpdate.setImage(new Image(new ByteArrayInputStream(currentUnidad.getPhotoUnidad())));
        labelNameUpdate.setText(currentUnidad.getId_unidad());
        comboBoxModeloUpdate.setValue(currentUnidad.getId_modelo());
        comboBoxEstadoUpdate.setValue(currentUnidad.getEstado());
        textAreaNotasUpdate.setText(currentUnidad.getNotas_de_la_unidad());
    }

    //actualiza la interfaz cada que se actualiza un elemento
    private void updateUI() {
        getAllUnitsFromDB();
        mostrarInfoUnidad(listAllUnits.stream().filter(item -> item.getId_unidad().equals(currentUnidad.getId_unidad())).findFirst().get());
    }

    /* ****************************************** Métodos de unit maintenance ******************************* */

    //muestra la ventana de administration de mantenimiento de la unidad
    public void showMaintenance() {
        Runnable openAdminMaintenanceThread = new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/layout/adminMaintenance.fxml"));

                Scene scene;
                try {
                    scene = new Scene(loader.load(), 1280, 720);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                scene.getStylesheets().add(getClass().getResource("/css/adminMaintenance.css").toExternalForm());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.UNDECORATED);

                AdminMaintenanceController controller = loader.getController();
                controller.setIdUnidad(currentUnidad.getId_unidad());
                stage.showAndWait();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        getAllUnitsFromDB();
                    }
                });
            }
        };

        openAdminMaintenanceThread.run();
    }

    public void  showAlternativeMaintenance(){
        Runnable openNewMaintenanceWindowThread = new Runnable() {
            @Override
            public void run() {
                 try{

                     FXMLLoader loader = new FXMLLoader();
                     loader.setLocation(getClass().getResource(Constants.ALTERNATIVE_MAINTENANCE_PATH));
                     Util.newStageWindow(loader, 1280, 720, getClass().getResource("/css/alternativeMaintenance.css").toExternalForm(), true, (FXMLLoader content)-> {
                         AlternativeMaintenanceController controller = content.getController();
                         controller.setIdUnidad(currentUnidad.getId_unidad());
                     });

                 }catch (Exception e){
                     e.printStackTrace();
                 }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        getAllUnitsFromDB();
                    }
                });
            }
        };

        openNewMaintenanceWindowThread.run();
    }

    /* ##############################################  Métodos handle  ############################################### */

    /* ****************************************** Métodos ventana lateral izquierda ***************************** */

    //Abre el formulario para añadir una nueva unidad a la BD.
   public void handleNewUnit() throws IOException {
        Runnable threadAddUnit = new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/layout/addNewUnit.fxml"));

                Scene scene = null;
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

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        getAllUnitsFromDB();
                    }
                });
            }
        };

        threadAddUnit.run();
    }

    //Este método busca entre la lista de modelos, algún elemento que
    //coincida con la búsqueda.
    public void handleSearch() {
        aside_list_units.getChildren().clear();
        List<Unidad> listaFiltrada = listAllUnits.stream().filter(item -> item.getId_unidad().toLowerCase().contains(txtBúsqueda.getText().toLowerCase())).toList();
        showUnitInUI(listaFiltrada);
    }

    /* ****************************************** Métodos delete unit ***************************** */

    //Borra la unidad seleccionada de la base de datos.
   public void handleDeleteOk() {
       String textResult;
        if (con.eliminarUnidad(currentUnidad.getId_unidad())) {
            textResult = "Modelo actualizado correctamente!";
            imvMensaje.setImage(new Image(getClass().getResourceAsStream("/icons/deleteOk.gif")));
            lblMensaje.setStyle("-fx-text-fill: green;");
        } else {
            textResult = "Ops! Algo salio mal...";
            imvMensaje.setImage(new Image(getClass().getResourceAsStream("/icons/saveError.gif")));
            lblMensaje.setStyle("-fx-text-fill: red;");
        }

        lblMensaje.setText(textResult);
        mostrarForm(formMensaje);
        getAllUnitsFromDB();
        pn_part_content.getChildren().remove(pane_infoUnidad);
        pn_part_content.setCenter(pane_waiting_clic);
    }

    // cierra el formulario de confirmación de eliminación de la unidad seleccionada.
    public void handleDeleteCancel() {
        mostrarForm(formDeleteUnit);
    }

    /* ****************************************** Métodos mensaje form ******************************* */

    //Cierra el formulario mensaje.
    public void handleCloseMessage() {
        mostrarForm(formMensaje);
    }

    /* ****************************************** Métodos Update Model ******************************* */

    //carga una nueva foto al objeto modelo que se mandara para actualizar el modelo en la BD.
   public void handleChangeUnitPhoto() {
        String pathFoto = Util.getPhotoFromStorage("Actualizar foto de la unidad");

        if (pathFoto != null) {
            byte[] photo = Util.readFile(pathFoto);
            unidadUpdateUnit.setPhotoUnidad(photo);
            imvFotoUpdate.setImage(new Image(new ByteArrayInputStream(photo)));
        }

    }

    //manda los datos a la BD para actualizar la Unidad.
    public void handleUpdateUnit() {
        unidadUpdateUnit.setId_unidad(currentUnidad.getId_unidad());
        unidadUpdateUnit.setId_modelo(comboBoxModeloUpdate.getSelectionModel().getSelectedItem());
        unidadUpdateUnit.setEstado(comboBoxEstadoUpdate.getSelectionModel().getSelectedItem());
        unidadUpdateUnit.setNotas_de_la_unidad(textAreaNotasUpdate.getText());
        if (unidadUpdateUnit.getPhotoUnidad() == null) {
            unidadUpdateUnit.setPhotoUnidad(currentUnidad.getPhotoUnidad());
        }

        String textResult;

        if (con.actualizarUnidad(unidadUpdateUnit)) {
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
        updateUI();
    }



}
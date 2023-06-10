package controls;

import clases.*;
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
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminMaintenanceController implements Initializable, DraggedScene {
    //variables
    String idUnidad;
    Unidad unidad;
    Conexión_old con = new Conexión_old();
    Mantenimiento currentMantenimiento;
    MantenimientoHistorial currentMantenimientoHistorial;
    ArrayList<Mantenimiento> listAllMaintenance;
    ArrayList<MantenimientoHistorial> listAllMaintenanceHistory;
    @FXML
    private AnchorPane root;
    @FXML
    private Label IDUnidad_lbl;
    @FXML
    private Button shrink_button;
    @FXML
    private Button close_button;
    @FXML
    private Label fechaProgramada_lbl;
    @FXML
    private Label ultimoMantenimiento_lbl;
    @FXML
    private VBox contentForms;
    @FXML
    private BorderPane mantenimientoProgramado_borderPane;
    @FXML
    private BorderPane mantenimientoHistorialBorderPane;
    @FXML
    private VBox emptyView;
    @FXML
    private Label empty_main_message;
    @FXML
    private Label empty_description;
    @FXML
    private Button empty_button;
    @FXML
    private BorderPane nextMaintenance_borderPane;
    @FXML
    private VBox list_asidde_next_date;
    @FXML
    private VBox waiting_clic;
    @FXML
    private Label mensaje_waiting_clic;
    @FXML
    private BorderPane form_info_nextMaintenance;
    @FXML
    private Label notaMantenimiento_nextDate_label;
    @FXML
    private Label date_nextDate_label;
    @FXML
    private HBox buttons_options_container;
    @FXML
    private ImageView imvMensaje;
    @FXML
    private Label lblMensaje;
    @FXML
    private BorderPane formMensaje;
    @FXML
    private VBox form_aside_container;
    @FXML
    private BorderPane formDeleteUnit;
    @FXML
    private Label nombreDeleteLabel;
    @FXML
    private BorderPane formUpdateMaintenance;
    @FXML
    private BorderPane formChangeState;
    @FXML
    private DatePicker picker_programarMantenimiento;
    @FXML
    private Label lblFechaEstablecida;
    @FXML
    private Label estado_unidad_label;
    @FXML
    private Label nombreUnidad_label_change_state;
    @FXML
    private TextArea txtNotasMantenimientoUpdate;
    @FXML
    private BorderPane infoContent;
    @FXML
    private TabPane tabPane;
    @FXML
    private BorderPane maintenanceHistory_borderPane;
    @FXML
    private VBox list_aside_history_date;
    @FXML
    private BorderPane form_info_maintenance_history;
    @FXML
    private BorderPane form_change_maintenance_history;
    @FXML
    private Label lbl_finiicio_change_change;
    @FXML
    private TextField txt_encargado_change_change;
    @FXML
    private ComboBox<String> cbx_estado_change_change;
    @FXML
    private Label lbl_fecha_programada_change_change;
    @FXML
    private Label lbl_notas_mantenimiento_change_change;
    @FXML
    private TextArea txt_notas_encargado_change_change;
    @FXML
    private Label lbl_estado_view_change;
    @FXML
    private Label lbl_fecha_inicio_view_change;
    @FXML
    private Label lbl_fecha_finalizacion_view_change;
    @FXML
    private Label lbl_fecha_programada_view_change;
    @FXML
    private Label lbl_encargado_view_change;
    @FXML
    private Label lbl_notas_mantenimiento_view_change;
    @FXML
    private Label lbl_notas_encargado_view_change;
    @FXML
    private VBox vb_container_saver_button_change;
    @FXML
    private TextField txtBúsquedaHistory;
    @FXML
    private TextField txtBúsquedaNextDate;
    @FXML
    private Label lbl_mensaje_estate_maintenance_info_form_history;


    /* #############################################  Métodos de clase  ############################################## */

    //---------------------------------------------- general -------------------------------------

    //Inicializa la ventana de mantenimiento.
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.onDraggedScene(this.root);
        root.getChildren().remove(contentForms);
        empty_main_message.setText(Constants.UNIDAD_SIN_MANTENIMIENTO_PROGRAMADO);
        empty_description.setText("Clic en 'Añadir una fecha' para poder programar el próximo mantenimiento.");
        empty_button.setText("Añadir una fecha");
        cbx_estado_change_change.getItems().addAll(Constants.ESTADO_MANTENIMIENTO_OK, Constants.ESTADO_MANTENIMIENTO_PENDIENTE, Constants.UNIDAD_EN_MANTENIMIENTO, Constants.ESTADO_MANTENIMIENTO_STOP);
    }

    //recibe el ID de la unidad que se manda desde la sección de unidades del menu principal.
    public void setIdUnidad(String id) {
        idUnidad = id;
        IDUnidad_lbl.setText(id);
        getUnitInfo(idUnidad);
    }

    //Obtiene la información de la unidad (próximo y último mantenimiento)
    public void getUnitInfo(String idUnidad) {
        unidad = con.getUnitByID(idUnidad);
        if (unidad != null) {
            ultimoMantenimiento_lbl.setText(con.getLastMaintenanceDateByUnit(idUnidad));
            fechaProgramada_lbl.setText(con.getNextMaintenanceDateByUnit(idUnidad));
            loadViewDataNextMaintenance();
            loadOptionButtonsNexDate();
        }
    }

    //---------------------------------------------- mantenimiento programado-------------------------------------

    /* ****************************************** Métodos generales ******************************* */

    //carga una ventana u otra dependiendo si existen mantenimientos programados.
    public void loadViewDataNextMaintenance() {
        if (con.getNextMaintenanceDateByUnit(idUnidad).equals(Constants.UNIDAD_SIN_MANTENIMIENTO_PROGRAMADO)) {

            if (!mantenimientoProgramado_borderPane.getChildren().contains(emptyView)) {
                mantenimientoProgramado_borderPane.getChildren().clear();
                mantenimientoProgramado_borderPane.setCenter(emptyView);
                empty_main_message.setText(Constants.UNIDAD_SIN_MANTENIMIENTO_PROGRAMADO);
                empty_description.setText("Clic en 'Añadir una fecha' para poder programar el próximo mantenimiento.");
                empty_button.setText("Añadir una fecha");
                empty_button.setOnAction(action -> handleAddNewNextMaintenance());
            }

        } else {

            if (!mantenimientoProgramado_borderPane.getChildren().contains(nextMaintenance_borderPane)) {
                mantenimientoProgramado_borderPane.getChildren().clear();
                mantenimientoProgramado_borderPane.setCenter(nextMaintenance_borderPane);
            }

            if (!nextMaintenance_borderPane.getChildren().contains(waiting_clic)) {
                nextMaintenance_borderPane.getChildren().remove(form_info_nextMaintenance);
                nextMaintenance_borderPane.setCenter(waiting_clic);
                mensaje_waiting_clic.setText("Clic en una fecha de la lista para continuar.");
            }

            getNextMaintenanceData();
            loadDatePicker();
        }
    }

    //Obtiene la lista de mantenimientos programados existentes.
    public void getNextMaintenanceData() {
        listAllMaintenance = con.getAllNextMaintenanceData(idUnidad);
        showNextDateInUI(listAllMaintenance);
    }

    //recibe del método getNextMaintenanceData la lista de mantenimientos programados, y las agrega de manera gráfica a la interfaz
    private void showNextDateInUI(List<Mantenimiento> listItems) {
        list_asidde_next_date.getChildren().clear();
        listItems.forEach(item -> {
            ItemMantenimiento itemUI = new ItemMantenimiento(item);
            itemUI.setOnMouseClicked(event -> showInfoNextDate(item));
            list_asidde_next_date.getChildren().add(itemUI);
        });
    }

    //recibe un objeto mantenimiento y muestra su información en la interfaz de mantenimientos programados.
    private void showInfoNextDate(Mantenimiento item) {
        currentMantenimiento = item;

        if (nextMaintenance_borderPane.getChildren().contains(waiting_clic)) {
            nextMaintenance_borderPane.getChildren().remove(waiting_clic);
            nextMaintenance_borderPane.setCenter(form_info_nextMaintenance);

            if (!form_info_nextMaintenance.getChildren().contains(infoContent)) {
                form_info_nextMaintenance.getChildren().clear();
                form_info_nextMaintenance.setCenter(infoContent);
            }
        }

        notaMantenimiento_nextDate_label.setText(item.getMaintenanceNotes() == null || item.getMaintenanceNotes().isBlank() ? "Sin notas de mantenimiento" : currentMantenimiento.getMaintenanceNotes());
        date_nextDate_label.setText(item.getMaintenanceDate());

    }

    /*
     * Muestra u oculta la ventana lateral en la interfaz, al mostrar la ventana
     * la rellena con el formulario node
     * */
    private void mostrarForm(Node node) {
        if (isVisibleFormContainer()) {
            if (form_aside_container.getChildren().contains(node)) {
                form_info_nextMaintenance.getChildren().remove(form_aside_container);
            } else {
                form_aside_container.getChildren().clear();
                form_aside_container.getChildren().add(node);
            }

        } else {
            form_info_nextMaintenance.setRight(form_aside_container);
            form_aside_container.getChildren().clear();
            form_aside_container.getChildren().add(node);
        }
    }

    //retorna true si el contenedor derecho está abierto, y si está cerrado retorna false.
    private boolean isVisibleFormContainer() {
        return form_info_nextMaintenance.getChildren().contains(form_aside_container);
    }

    //carga los botones de opciones en la interfaz de mantenimientos programados
    private void loadOptionButtonsNexDate() {

        Button_with_icon btnDelete = new Button_with_icon();
        btnDelete.makeContainer("white", "10px", "10px 15px", 10.0);
        btnDelete.makeContentButton("icon_delete_model", 15.0, "#161925", "Eliminar mantenimiento");
        btnDelete.createButton();
        btnDelete.setOnMouseClicked(action -> showDelete());

        Button_with_icon btnUpdate = new Button_with_icon();
        btnUpdate.makeContainer("white", "10px", "10px 15px", 10.0);
        btnUpdate.makeContentButton("icon_update_details", 15.0, "#161925", "Editar datos del mantenimiento");
        btnUpdate.createButton();
        btnUpdate.setOnMouseClicked(action -> showUpdateMaintenanceData());

        Button_with_icon btnMantenimiento = new Button_with_icon();
        btnMantenimiento.makeContainer("white", "10px", "10px 15px", 10.0);
        btnMantenimiento.makeContentButton("icon_mantenimiento", 15.0, "#161925", "Realizar mantenimiento");
        btnMantenimiento.createButton();
        btnMantenimiento.setOnMouseClicked(action -> showChangeState());


        buttons_options_container.getChildren().addAll(btnMantenimiento, btnUpdate, btnDelete);
    }


    /* ****************************************** Métodos show mantenimiento ******************************* */

    //Muestra en la ventana lateral derecha, la ventana para mandar poner en mantenimiento la unidad.
    private void showChangeState() {
        mostrarForm(formChangeState);
        nombreUnidad_label_change_state.setText(unidad.getId_unidad());
        estado_unidad_label.setText("\"" + unidad.getEstado() + "\"");
        estado_unidad_label.setStyle(Util.getStyleByState(unidad.getEstado()));
    }

    /* ****************************************** Métodos update mantenimiento ******************************* */

    //Muestra en la ventana lateral derecha, la ventana para actualizar los datos del mantenimiento.
    private void showUpdateMaintenanceData() {
        mostrarForm(formUpdateMaintenance);
        picker_programarMantenimiento.setValue(LocalDate.parse(currentMantenimiento.getMaintenanceDate(), DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        lblFechaEstablecida.setText("Fecha actual del mantenimiento: " + currentMantenimiento.getMaintenanceDate());
        txtNotasMantenimientoUpdate.setText(currentMantenimiento.getMaintenanceNotes());
    }

    //actualiza la interfaz cada que se actualiza un elemento
    private void updateUINextMaintenanceDate(LocalDate fecha) {
        loadViewDataNextMaintenance();
        showInfoNextDate(listAllMaintenance.stream().filter(item ->
                item.getMaintenanceDate().equals(fecha.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
        ).findFirst().get());
    }

    //carga la configuración del datePicker para actualizar la fecha programada dem mantenimiento.
    public void loadDatePicker() {
        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {

                super.updateItem(item, empty);

                Paint paint = Color.WHITE;

                BackgroundFill fill = new BackgroundFill(paint, null, null);

                this.setDisable(false);
                this.setBackground(new Background(fill));
                this.setTextFill(Color.BLACK);

                // deshabilitar las fechas pasadas
                if (item.isBefore(LocalDate.now())) {
                    this.setDisable(true);
                }

            }
        };

        picker_programarMantenimiento.setDayCellFactory(dayCellFactory);
        picker_programarMantenimiento.setOnAction(action -> {
            lblFechaEstablecida.setText("Nueva fecha del mantenimiento: \n" + picker_programarMantenimiento.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
        });

    }

    /* ****************************************** Métodos delete mantenimiento ******************************* */

    //muestra el formulario para eliminar el elemento Unidad de la Base de datos.
    private void showDelete() {
        mostrarForm(formDeleteUnit);
        nombreDeleteLabel.setText(currentMantenimiento.getMaintenanceDate());
    }

//---------------------------------------------- maintenanceHistory-------------------------------------

    //carga una ventana u otra dependiendo si existen mantenimientos en el historial de mantenimientos.
    public void loadViewDataMaintenanceHistory() {

        if (con.getAllLastMaintenanceDateByUnit(unidad.getId_unidad()).equals(Constants.UNIDAD_SIN_MANTENIMIENTO)) {

            if (!mantenimientoHistorialBorderPane.getChildren().contains(emptyView)) {
                mantenimientoHistorialBorderPane.getChildren().clear();
                mantenimientoHistorialBorderPane.setCenter(emptyView);
                empty_main_message.setText(Constants.UNIDAD_SIN_MANTENIMIENTO);
                empty_description.setText("Clic en 'Añadir mantenimiento' para poder realizar el mantenimiento.");
                empty_button.setText("Añadir mantenimiento");

            }

        } else {
            if (!mantenimientoHistorialBorderPane.getChildren().contains(maintenanceHistory_borderPane)) {
                mantenimientoHistorialBorderPane.getChildren().clear();
            }
            mantenimientoHistorialBorderPane.setCenter(maintenanceHistory_borderPane);

            if (!maintenanceHistory_borderPane.getChildren().contains(waiting_clic)) {
                //maintenanceHistory_borderPane.getChildren().remove();
                maintenanceHistory_borderPane.setCenter(waiting_clic);
                mensaje_waiting_clic.setText("Clic en un mantenimiento de la lista para continuar.");

            }

            getMaintenanceHistoryData();
        }
    }

    //Obtiene la lista de mantenimientos existentes en el historial
    private void getMaintenanceHistoryData() {
        listAllMaintenanceHistory = con.getAllMaintenanceHistoryDataByUnit(unidad.getId_unidad());
        showInUIMaintenanceHistory(listAllMaintenanceHistory);
    }

    //recibe del método getMaintenanceHistoryData la lista de mantenimientos del historial, y las agrega de manera gráfica a la interfaz
    private void showInUIMaintenanceHistory(List<MantenimientoHistorial> lista) {
        list_aside_history_date.getChildren().clear();
        lista.forEach(item -> {
            ItemMantenimientoHistorial itemUI = new ItemMantenimientoHistorial(item);
            itemUI.setOnMouseClicked(action -> showInfoMaintenanceHistory(item));
            list_aside_history_date.getChildren().add(itemUI);
        });

    }

    //muestra en la interfaz la información del mantenimiento del historial seleccionado.
    private void showInfoMaintenanceHistory(MantenimientoHistorial item) {
        showFormByState(item);
    }

    //muestra un formulario u otro dependiendo si el mantenimiento seleccionado del historial esta "terminado" o "en mantenimiento.
    private void showFormByState(MantenimientoHistorial item) {

        maintenanceHistory_borderPane.getChildren().remove(waiting_clic);


        if (item.getEstado().equals(Constants.ESTADO_MANTENIMIENTO_OK) || item.getEstado().equals(Constants.ESTADO_MANTENIMIENTO_STOP)) {

            maintenanceHistory_borderPane.getChildren().remove(form_change_maintenance_history);

            if (!maintenanceHistory_borderPane.getChildren().contains(form_info_maintenance_history)) {
                maintenanceHistory_borderPane.setCenter(form_info_maintenance_history);
            }

            lbl_mensaje_estate_maintenance_info_form_history.setText(item.getEstado().equals(Constants.ESTADO_MANTENIMIENTO_OK) ? "Mantenimiento terminado" : "Mantenimiento terminado con observaciones");
            lbl_mensaje_estate_maintenance_info_form_history.setStyle(Util.getStyleByState(item.getEstado()));
            lbl_encargado_view_change.setText(item.getEncargado());
            lbl_fecha_programada_view_change.setText(item.getFecha_programada());
            lbl_fecha_inicio_view_change.setText(item.getFecha_inicio_mantenimiento());
            lbl_fecha_finalizacion_view_change.setText(item.getFecha_finalizacion_mantenimiento());
            lbl_estado_view_change.setText(item.getEstado());
            lbl_estado_view_change.setStyle(Util.getStyleByState(item.getEstado()));

            lbl_notas_mantenimiento_view_change.setText(item.getNotas_mantenimiento_programado() == null || item.getNotas_mantenimiento_programado().isBlank() ? "Sin notas de mantenimiento" : item.getNotas_mantenimiento_programado());

            lbl_notas_encargado_view_change.setText(item.getNotas_mantenimiento_encargado() == null || item.getNotas_mantenimiento_encargado().isBlank() ? "Sin observaciones" : item.getNotas_mantenimiento_encargado());


        } else {
            maintenanceHistory_borderPane.getChildren().remove(form_info_maintenance_history);

            if (!maintenanceHistory_borderPane.getChildren().contains(form_change_maintenance_history)) {
                maintenanceHistory_borderPane.setCenter(form_change_maintenance_history);
            }

            lbl_fecha_programada_change_change.setText(item.getFecha_programada());
            lbl_finiicio_change_change.setText(item.getFecha_inicio_mantenimiento());
            cbx_estado_change_change.setValue(item.getEstado());
            lbl_notas_mantenimiento_change_change.setText(item.getNotas_mantenimiento_programado());
            txt_encargado_change_change.setText(item.getEncargado());
            txt_notas_encargado_change_change.setText(item.getNotas_mantenimiento_encargado());

            currentMantenimientoHistorial = item;
        }

    }


    /* #############################################  Métodos handle  ############################################## */

    //---------------------------------------------- generales -------------------------------------

    //Minimiza la ventana
    public void handleShrinkButton() {
        Stage stage = (Stage) shrink_button.getScene().getWindow();
        stage.setIconified(true);
    }

    //cierra la ventana
    public void handleCloseButton() {
        Stage stage = (Stage) close_button.getScene().getWindow();
        stage.close();
    }

    //abre la ventana para añadir un nuevo mantenimiento.
    public void handleAddNewNextMaintenance() {
        Runnable addNextMaintenanceThread = new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/layout/addNewNextMaintenance.fxml"));

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

                AddNextMaintenanceController controller = loader.getController();
                controller.setUnitID(unidad.getId_unidad());
                stage.showAndWait();

                Platform.runLater(() -> {
                    loadViewDataNextMaintenance();
                });
            }
        };

        addNextMaintenanceThread.run();
    }

    //---------------------------------------------- next maintenance date -------------------------------------

    /* ****************************************** Métodos delete mantenimiento ******************************* */

    //Borra la unidad seleccionada de la base de datos.
    public void handleDeleteOk() {
        String textResult;

        if (con.deleteNextMaintenanceDate(unidad.getId_unidad(), currentMantenimiento.getMaintenanceDate())) {
            textResult = "Mantenimiento programado eliminado correctamente!";
            imvMensaje.setImage(new Image(getClass().getResourceAsStream("/icons/deleteOk.gif")));
            lblMensaje.setStyle("-fx-text-fill: green;");
        } else {
            textResult = "Ops! Algo salio mal...";
            imvMensaje.setImage(new Image(getClass().getResourceAsStream("/icons/saveError.gif")));
            lblMensaje.setStyle("-fx-text-fill: red;");
        }

        lblMensaje.setText(textResult);
        mostrarForm(formMensaje);
        loadViewDataNextMaintenance();
        nextMaintenance_borderPane.getChildren().remove(form_info_nextMaintenance);
        nextMaintenance_borderPane.setCenter(waiting_clic);
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

    /* ****************************************** Métodos update mantenimiento ******************************* */

    //guarda los datos actualizados en la base de datos del mantenimiento programado actual
    public void handleUpdateMaintenance() {

        Mantenimiento nMantenimiento = new Mantenimiento();
        nMantenimiento.setMaintenanceDate(picker_programarMantenimiento.getValue().toString());
        nMantenimiento.setMaintenanceNotes(txtNotasMantenimientoUpdate.getText());

        String textResult;

        if (con.updateNextDateMaintenance(currentMantenimiento, nMantenimiento)) {
            textResult = "Mantenimiento actualizado correctamente!";
            imvMensaje.setImage(new Image(getClass().getResourceAsStream("/icons/saveOk.gif")));
            lblMensaje.setStyle("-fx-text-fill: green;");
        } else {
            textResult = "Ops! Algo salio mal...";
            imvMensaje.setImage(new Image(getClass().getResourceAsStream("/icons/saveError.gif")));
            lblMensaje.setStyle("-fx-text-fill: red;");
        }

        lblMensaje.setText(textResult);
        mostrarForm(formMensaje);
        updateUINextMaintenanceDate(picker_programarMantenimiento.getValue());
    }

    /* ****************************************** Métodos realizar mantenimiento ******************************* */

    //manda el mantenimiento programado seleccionado al historial de mantenimiento con el estado "En mantenimiento".
    public void handleChangeOk() {

        MantenimientoHistorial mh = new MantenimientoHistorial();
        mh.setIdUnidad(unidad.getId_unidad());
        mh.setFecha_programada(currentMantenimiento.getMaintenanceDate());
        mh.setFecha_inicio_mantenimiento(LocalDate.now().toString());
        mh.setEstado(Constants.UNIDAD_EN_MANTENIMIENTO);
        mh.setNotas_mantenimiento_programado(currentMantenimiento.getMaintenanceNotes());

        String textResult;

        if (con.addToMaintenanceHistory(mh)) {

            if (con.deleteNextMaintenanceDate(unidad.getId_unidad(), currentMantenimiento.getMaintenanceDate())) {

                if (con.updateStateUnit(unidad.getId_unidad(), Constants.UNIDAD_EN_MANTENIMIENTO)) {
                    textResult = "La unidad entro en mantenimiento correctamente!";
                    imvMensaje.setImage(new Image(getClass().getResourceAsStream("/icons/deleteOk.gif")));
                    lblMensaje.setStyle("-fx-text-fill: green;");
                    form_info_nextMaintenance.getChildren().remove(infoContent);
                    form_info_nextMaintenance.setCenter(waiting_clic);
                } else {
                    textResult = "Ops! Algo salio mal...";
                    imvMensaje.setImage(new Image(getClass().getResourceAsStream("/icons/saveError.gif")));
                    lblMensaje.setStyle("-fx-text-fill: red;");
                }

            } else {
                textResult = "Ops! Algo salio mal...";
                imvMensaje.setImage(new Image(getClass().getResourceAsStream("/icons/saveError.gif")));
                lblMensaje.setStyle("-fx-text-fill: red;");
            }

        } else {
            textResult = "Ops! Algo salio mal...";
            imvMensaje.setImage(new Image(getClass().getResourceAsStream("/icons/saveError.gif")));
            lblMensaje.setStyle("-fx-text-fill: red;");
        }

        lblMensaje.setText(textResult);
        mostrarForm(formMensaje);
        loadViewDataNextMaintenance();
    }

    //---------------------------------------------- maintenanceHistory-------------------------------------

    /* ****************************************** Métodos change state form ******************************* */

    //Cierra la ventana lateral derecha, la ventana para mandar poner en mantenimiento la unidad.
    public void handleChangeCancel() {
        mostrarForm(formChangeState);
    }

    //guarda los datos actualizados del mantenimiento seleccionado del historial.
    public void handleSaveChangeState() {
        MantenimientoHistorial mh = new MantenimientoHistorial();
        mh.setIdUnidad(unidad.getId_unidad());
        mh.setFecha_programada(currentMantenimientoHistorial.getFecha_programada());
        mh.setNotas_mantenimiento_programado(currentMantenimientoHistorial.getNotas_mantenimiento_programado());
        mh.setFecha_inicio_mantenimiento(currentMantenimientoHistorial.getFecha_inicio_mantenimiento());


        if (txt_encargado_change_change.getText() != null && !txt_encargado_change_change.getText().isBlank()) {
            mh.setEncargado(txt_encargado_change_change.getText());
        }

        mh.setEstado(cbx_estado_change_change.getSelectionModel().getSelectedItem());

        if (txt_notas_encargado_change_change.getText() != null && !txt_notas_encargado_change_change.getText().isBlank()) {
            mh.setNotas_mantenimiento_encargado(txt_notas_encargado_change_change.getText());
        }

        if (cbx_estado_change_change.getSelectionModel().getSelectedItem().equals(Constants.ESTADO_MANTENIMIENTO_OK) || cbx_estado_change_change.getSelectionModel().getSelectedItem().equals(Constants.ESTADO_MANTENIMIENTO_STOP)) {
            mh.setFecha_finalizacion_mantenimiento(LocalDate.now().toString());
        }

        if (con.updateMaintenanceHistory(mh)) {

            // si el usuario marco el mantenimiento como terminado, revisa el historial para saber si quedan mantenimientos activos en el hsirial
            //si todos lo mantenimientos están terminados pone el estado de la unidad en "activa"
            if ((cbx_estado_change_change.getSelectionModel().getSelectedItem().equals(Constants.ESTADO_MANTENIMIENTO_STOP) || cbx_estado_change_change.getSelectionModel().getSelectedItem().equals(Constants.ESTADO_MANTENIMIENTO_OK)) && areYourMaintenancesOver()) {
                con.updateStateUnit(unidad.getId_unidad(), Constants.ESTADO_UNIDAD_ACTIVO);
            } else {
                form_change_maintenance_history.getChildren().remove(vb_container_saver_button_change);
            }
        }

        loadViewDataMaintenanceHistory();
        showFormByState(listAllMaintenanceHistory.stream().filter(item -> item.getFecha_programada().equals(currentMantenimientoHistorial.getFecha_programada()) && item.getNotas_mantenimiento_programado().equals(currentMantenimientoHistorial.getNotas_mantenimiento_programado())).findFirst().get());

    }


    //revisa los datos del formulario que actualiza el mantenimiento en el historial
    public void handleValidarChangeState() {

        if (txt_encargado_change_change.getText() == null || txt_notas_encargado_change_change.getText() == null || txt_notas_encargado_change_change.getText().isBlank() || txt_notas_encargado_change_change.getText().isBlank()) {
            if (cbx_estado_change_change.getSelectionModel().getSelectedItem().equals(Constants.ESTADO_MANTENIMIENTO_OK) || cbx_estado_change_change.getSelectionModel().getSelectedItem().equals(Constants.ESTADO_MANTENIMIENTO_STOP)) {
                form_change_maintenance_history.getChildren().remove(vb_container_saver_button_change);
            } else {
                if (!form_change_maintenance_history.getChildren().contains(vb_container_saver_button_change)) {
                    form_change_maintenance_history.setBottom(vb_container_saver_button_change);
                }
            }
        } else {
            if (!form_change_maintenance_history.getChildren().contains(vb_container_saver_button_change)) {
                form_change_maintenance_history.setBottom(vb_container_saver_button_change);
            }
        }

    }

    private boolean areYourMaintenancesOver() {
        ArrayList<MantenimientoHistorial> listaHistorial = con.getAllMaintenanceHistoryDataByUnit(unidad.getId_unidad());
        return listaHistorial.stream().allMatch(item -> item.getEstado().equals(Constants.ESTADO_MANTENIMIENTO_OK) || item.getEstado().equals(Constants.ESTADO_MANTENIMIENTO_STOP));
    }

    //______________________búsqueda historial _________________

    //filtra los mantenimientos en el historial que coincidan con los terminus de búsqueda.
    public void handleSearchHistory() {
        list_aside_history_date.getChildren().clear();
        List<MantenimientoHistorial> tempMH = listAllMaintenanceHistory.stream().filter(item -> item.getFecha_programada().toLowerCase().contains(txtBúsquedaHistory.getText().toLowerCase()) || item.getEstado().toLowerCase().contains(txtBúsquedaHistory.getText().toLowerCase())).toList();
        showInUIMaintenanceHistory(tempMH);
    }

    //______________________búsqueda mantenimiento programado _________________

    //filtra los mantenimientos programados que coincidan con los terminus de búsqueda.
    public void handleSearchNextDate() {
        list_asidde_next_date.getChildren().clear();
        List<Mantenimiento> tempMP = listAllMaintenance.stream().filter(item -> item.getMaintenanceDate().toLowerCase().contains(txtBúsquedaNextDate.getText().toLowerCase())).toList();
        showNextDateInUI(tempMP);
    }

}


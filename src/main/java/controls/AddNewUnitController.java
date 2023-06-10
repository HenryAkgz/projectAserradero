package controls;

import clases.*;
import conexión.Conexión;
import conexión.Conexión_old;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddNewUnitController implements Initializable, DraggedScene {
    @FXML
    private AnchorPane root;
    @FXML
    private Button shrink_button;
    @FXML
    private Button close_button;
    @FXML
    private DatePicker picker_programarMantenimiento;
    @FXML
    private ImageView imvUnit;
    @FXML
    private TextField txtIDUnit;
    @FXML
    private Label lblMensaje;
    @FXML
    private ComboBox cbxEstado;
    @FXML
    private TextArea txtNotadDeLaUnidad;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnNext;
    @FXML
    private Pane pn_data_general;
    @FXML
    private Pane pnMantenimiento;
    @FXML
    private Label lblFechaEstablecida;
    @FXML
    private Pane pnResultado;
    @FXML
    private ComboBox cbxModelo;
    @FXML
    private Label lblResultMensaje;
    @FXML
    private ImageView imvResult;
    @FXML
    private TextArea txtNotasMantenimiento;
    //variables
    String pathPhoto = "";
    Conexión con = Conexión.getInstancia();
    Unidad currentUnidad = new Unidad();
    Mantenimiento currentMantenimiento = new Mantenimiento();


    /*
     * Estos métodos controlan el estado de la aplicación
     *
     * handleCloseButton terminará con la ejecución de la aplicación
     *
     * handleShrinkButton minimizará en la barra de tareas la aplicación
     * */
    public void handleCloseButton() {
        Stage stage = (Stage) close_button.getScene().getWindow();
        stage.close();
    }

    public void handleShrinkButton() {
        Stage stage = (Stage) shrink_button.getScene().getWindow();
        stage.setIconified(true);
    }

    //Abre un fileChooser para escoger la foto de la unidad
    public void handleGetUnitPhoto() {
        pathPhoto = Util.getPhotoFromStorage("Seleccione una foto de la unidad");

        if (pathPhoto != null) {
            currentUnidad.setPhotoUnidad(Util.readFile(pathPhoto));
            imvUnit.setImage(new Image(new ByteArrayInputStream(currentUnidad.getPhotoUnidad())));
        }
        handleValidarDatos();
    }

    //Comprueba que no exista otra unidad con el mismo nombre
    public void handleValidateID() {
        lblMensaje.setVisible(true);

        String mysteriousId = txtIDUnit.getText();

        if (!mysteriousId.isBlank()) {
            if (con.existeUnidad(mysteriousId.trim())) {
                lblMensaje.setText("Ya existe una Unidad con este ID.");
                lblMensaje.setStyle("-fx-text-fill: red;");
            } else {
                lblMensaje.setText("ID Disponible.");
                lblMensaje.setStyle("-fx-text-fill: green;");
                currentUnidad.setId_unidad(mysteriousId.trim());
                currentMantenimiento.setIdUnit(mysteriousId.trim());
            }
        } else {
            lblMensaje.setText("El ID de la unidad es obligatorio.");
            lblMensaje.setStyle("-fx-text-fill: red;");
        }

        handleValidarDatos();
    }


    public void handleNextAction() {

        if (pn_data_general.isVisible()) {
            if(!txtNotadDeLaUnidad.getText().isBlank()){
                currentUnidad.setNotas_de_la_unidad(txtNotadDeLaUnidad.getText().trim());
            }
            pn_data_general.setVisible(false);
            pnMantenimiento.setVisible(true);
            btnBack.setVisible(true);
        } else if (pnMantenimiento.isVisible()) {
            btnBack.setVisible(false);
            btnNext.setVisible(false);
            saveDataUnitInBD();
        }

    }

    public void handleBackButton() {
        if (pnMantenimiento.isVisible()) {
            pn_data_general.setVisible(true);
            pnMantenimiento.setVisible(false);
            btnBack.setVisible(false);
        }
    }

    public void handleValidarDatos() {
        btnNext.setVisible(!(currentUnidad.getPhotoUnidad() == null || cbxEstado.getSelectionModel().isEmpty() || txtIDUnit.getText().isBlank() || con.existeUnidad(txtIDUnit.getText().trim()) || cbxModelo.getSelectionModel().isEmpty()));
    }

    public void saveDataUnitInBD() {
        pnMantenimiento.setVisible(false);
        pnResultado.setVisible(true);

        if(picker_programarMantenimiento.getValue() != null){

           String strDate = LocalDate.parse(picker_programarMantenimiento.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).toString();;
            currentMantenimiento.setMaintenanceDate(strDate);
            currentMantenimiento.setMaintenanceType(Constants.TIPO_DE_MANTENIMIENTO_PREVENTIVO);
            currentMantenimiento.setMaintenanceNotes(txtNotasMantenimiento.getText() != null ? "" : txtNotasMantenimiento.getText().trim());
        }

        if (con.agregarUnidad(currentUnidad)) {

            if (currentMantenimiento.getMaintenanceDate() != null) {

                if (con.agregarMantenimientoProgramado(currentMantenimiento)) {
                    imvResult.setImage(new Image(getClass().getResourceAsStream("/icons/saveOk.gif")));
                    lblResultMensaje.setText("Modelo y mantenimiento agregado correctamente.");
                    lblResultMensaje.setStyle("-fx-text-fill: green;");
                } else {
                    imvResult.setImage(new Image(getClass().getResourceAsStream("/icons/saveError.gif")));
                    lblResultMensaje.setText("Lo siento, algo salio mal...");
                    lblResultMensaje.setStyle("-fx-text-fill: red;");
                }

            } else {
                imvResult.setImage(new Image(getClass().getResourceAsStream("/icons/saveOk.gif")));
                lblResultMensaje.setText("Modelo agregado correctamente.");
                lblResultMensaje.setStyle("-fx-text-fill: green;");
            }

        } else {
            imvResult.setImage(new Image(getClass().getResourceAsStream("/icons/saveError.gif")));
            lblResultMensaje.setText("Lo siento, algo salio mal...");
            lblResultMensaje.setStyle("-fx-text-fill: red;");
        }
    }

    public void handleChangeModelo() {
        currentUnidad.setId_modelo(cbxModelo.getSelectionModel().getSelectedItem().toString());
        handleValidarDatos();
    }

    public void handleChangeEstado() {
        currentUnidad.setEstado(cbxEstado.getSelectionModel().getSelectedItem().toString());
        handleValidarDatos();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.onDraggedScene(this.root);

        cbxEstado.getItems().addAll(Constants.ESTADO_UNIDAD_ACTIVO, Constants.ESTADO_UNIDAD_INACTIVO);


        // picker_programarMantenimiento.setValue(LocalDate.now());

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
            lblFechaEstablecida.setVisible(true);
            lblFechaEstablecida.setText("Próximo mantenimiento: \n" + picker_programarMantenimiento.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
        });

       /* ArrayList<Modelo> listaModelos = con.getModelosFromDB();
        listaModelos.forEach(item -> {
            cbxModelo.getItems().add(item.getIdModelo());
        });*/

        cbxModelo.getItems().addAll("UNIDI", "SORO");
    }
}

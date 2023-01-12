package controls;

import clases.DraggedScene;
import conexión.Conexión;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

public class AddNextMaintenanceController implements Initializable, DraggedScene {
    @FXML
    private AnchorPane root;
    @FXML
    private Button shrink_button;
    @FXML
    private Button close_button;
    @FXML
    private DatePicker picker_programarMantenimiento;
    @FXML private Label lblFechaEstablecida;
    @FXML private Button addOk_btn;
    @FXML
    private TextArea txtNotasMantenimiento;
    @FXML
    private ImageView imvResult;
    @FXML
    private Label lblResultMensaje;
    @FXML
    private Pane pnMantenimiento;
    @FXML
    private Pane pnResultado;

    String idUnidad;
    Conexión con = new Conexión();
    public void setUnitID(String idUnidad){
        this.idUnidad = idUnidad;
    }


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



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
     onDraggedScene(root);

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
        picker_programarMantenimiento.setOnAction(action -> validarFecha());
        picker_programarMantenimiento.setOnKeyTyped(action -> validarFecha());

    }

    private void validarFecha() {
       try{
           LocalDate temp = picker_programarMantenimiento.getValue();

           if(temp != null && !temp.isBefore(LocalDate.now())){
               lblFechaEstablecida.setText("Fecha del mantenimiento: \n" + temp.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
               lblFechaEstablecida.setVisible(true);
               txtNotasMantenimiento.requestFocus();
               addOk_btn.setVisible(true);
           }else{
               lblFechaEstablecida.setVisible(false);
               addOk_btn.setVisible(false);
           }
       }catch (Exception e){
           lblFechaEstablecida.setVisible(false);
           addOk_btn.setVisible(false);
       }

    }

    public void handleSaveMaintenanceData(){
        pnMantenimiento.setVisible(false);
        pnResultado.setVisible(true);
        addOk_btn.setVisible(false);

        if(con.saveMantenimientoProgramado(idUnidad, picker_programarMantenimiento.getValue().toString(), txtNotasMantenimiento.getText())){
            imvResult.setImage(new Image(getClass().getResourceAsStream("/icons/saveOk.gif")));
            lblResultMensaje.setText("Mantenimiento programado correctamente.");
            lblResultMensaje.setStyle("-fx-text-fill: green;");
        } else {
            imvResult.setImage(new Image(getClass().getResourceAsStream("/icons/saveError.gif")));
            lblResultMensaje.setText("Lo siento, algo salio mal...");
            lblResultMensaje.setStyle("-fx-text-fill: red;");
        }

    }
}

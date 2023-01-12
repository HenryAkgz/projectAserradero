package controls;

import clases.DraggedScene;
import clases.Pieza;
import clases.Util;
import conexión.Conexión;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddNewPartController implements Initializable, DraggedScene {
    @FXML
    private AnchorPane root;
    @FXML
    private Button shrink_button;
    @FXML
    private Button close_button;
    @FXML
    private ImageView model_photo_imageView;
    @FXML
    private Button next_button;
    @FXML
    private TextField nombrePieza_textField;
    @FXML
    private Spinner piezasInventario_spinner;
    @FXML
    private Pane pn_result;
    @FXML
    private Pane part_pane;
    @FXML
    private ImageView result_imageView;
    @FXML
    private Label mensaje_result_label;

    //variables del stage
    String pathPhoto;
    Conexión con;

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

    //handleAddPhotoModel: abre un filechooser para seleccionar la imagen del modelo y mostrarla en el imageView model_photo_imageView
    public void hanndleAddPhotoModel() {
        pathPhoto = Util.getPhotoFromStorage("Selecciona la foto del modelo");

        if (pathPhoto != null) {
            model_photo_imageView.setImage(new Image(pathPhoto));
        }
        handleValidarDatos();
    }

    //Muestra el botón de guardar si todos los datos necesarios están añadidos
    public void handleValidarDatos() {
        next_button.setVisible(!(pathPhoto == null || nombrePieza_textField.getText().isBlank() || piezasInventario_spinner.getValue() == null));
    }

    //agrega la pieza a la base de datos
    public void handleSavePartInBD() {
        con = new Conexión();
        con.connectWidthDB();

        Pieza nuevaPieza = new Pieza();
        nuevaPieza.setNombrePieza(nombrePieza_textField.getText());
        nuevaPieza.setPhotoPieza(Util.readFile(pathPhoto));
        nuevaPieza.setCantidadEnInvetario((int) piezasInventario_spinner.getValue());

        if (con.savePartInBD(nuevaPieza)) {
            mensaje_result_label.setText("La pieza se agrego correctamente");
           result_imageView.setImage(new Image(getClass().getResourceAsStream("/icons/saveOk.gif")));
            mensaje_result_label.setStyle("-fx-text-fill: #4B21C3;");
        }else{
            mensaje_result_label.setText("Lo siento, algo salio mal...");
            mensaje_result_label.setStyle("-fx-text-fill: red;");
           result_imageView.setImage(new Image(getClass().getResourceAsStream("/icons/saveError.gif")));
        }

        part_pane.setVisible(false);
        pn_result.setVisible(true);
        next_button.setVisible(false);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.onDraggedScene(this.root);
        piezasInventario_spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999999));
    }
}

package controls;

import clases.*;
import conexión.Conexión;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddNewModelController implements Initializable, DraggedScene {
    @FXML
    private AnchorPane root;
    @FXML
    private Button shrink_button;
    @FXML
    private Button close_button;
    @FXML
    private Button back_button;
    @FXML
    private Button next_button;
    @FXML
    private ImageView model_photo_imageView;
    @FXML
    private Pane pn_data_general;
    @FXML
    private Pane pnPieces;
    @FXML
    private Label model_exist_label;
    @FXML
    private TextField modelo_textField;
    @FXML
    private TextField motor_textField;
    @FXML
    private TextField peso_textField;
    @FXML
    private TextField diametroMaximo_textField;
    @FXML
    private TextField anchoTablero_textField;
    @FXML
    private TextField grosorPlaca_textField;
    @FXML
    private TextField tamanoHoja_textField;
    @FXML
    private TextField anchoPista_textField;
    @FXML
    private TextField ajustabilidad_textField;
    @FXML
    private ComboBox tipoCombustible_comboBox;
    @FXML
    private VBox vbListaAllPieces;
    @FXML
    private VBox vbListaAllModelPieces;
    @FXML
    private Label contador_label;
    @FXML
    private TextField txtBusqueda;
    @FXML
    private ImageView done_imv;
    @FXML
    private Label done_lbl;
    @FXML
    private Pane pn_done;


    //Variables del stage
    String pathPhoto;
    Conexión con;
    ArrayList<Pieza> listaPiezasFromDB;
    ArrayList<Pieza> listaPiezasDelModelo;
    boolean idExist = true;

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

    /*
     * métodos del stage
     * */

    //handleAddPhotoModel: abre un filechooser para seleccionar la imagen del modelo y mostrarla en el imageView model_photo_imageView
    public void handleAddPhotoModel() {
        pathPhoto = Util.getPhotoFromStorage("Selecciona la foto del modelo");

        if (pathPhoto != null) {
            model_photo_imageView.setImage(new Image(pathPhoto));
        }
        handleValidarCampos();
    }

    //handleValidarCampos: si todos los campos del formulario están rellenos entonces muestra el botón de continuar, de no ser asi lo oculta
    public void handleValidarCampos() {
        if (pn_data_general.isVisible()) {
            next_button.setVisible(!(modelo_textField.getText().isBlank() || motor_textField.getText().isBlank() || peso_textField.getText().isBlank() || diametroMaximo_textField.getText().isBlank() || anchoTablero_textField.getText().isBlank() || grosorPlaca_textField.getText().isBlank() || tamanoHoja_textField.getText().isBlank() || anchoPista_textField.getText().isBlank() || ajustabilidad_textField.getText().isBlank() || pathPhoto == null || tipoCombustible_comboBox.getSelectionModel().isEmpty() || idExist));
        }
    }


    //muestra la siguiente pantalla del registro del modelo
    public void handleNextAction() {
        if (pn_data_general.isVisible()) {
            pn_data_general.setVisible(false);
            pnPieces.setVisible(true);
            back_button.setVisible(true);
            next_button.setText("Terminar");
            loadPiecesFromDB();
        } else if (pnPieces.isVisible()) {

            Modelo modelo = new Modelo();
            modelo.setIdModelo(modelo_textField.getText());
            modelo.setPeso(peso_textField.getText()+ " kg");
            modelo.setMotor(motor_textField.getText());
            modelo.setMaxLogDiameter(diametroMaximo_textField.getText());
            modelo.setMaxBoardWidth(anchoTablero_textField.getText());
            modelo.setMaxBoardThickness(grosorPlaca_textField.getText());
            modelo.setBladeSize(tamanoHoja_textField.getText());
            modelo.setTrackLength("disable");
            modelo.setTrackWidth(anchoPista_textField.getText());
            modelo.setTrackHeightAdjustability(ajustabilidad_textField.getText());
            modelo.setFoto_modelo(Util.readFile(pathPhoto));

            boolean isSaveModelo = con.saveModeloInDB(modelo);

            if (isSaveModelo && listaPiezasDelModelo.size() > 0) {
                con.addPiecesToModel(listaPiezasDelModelo, modelo.getIdModelo());
            }

            pnPieces.setVisible(false);

            if (isSaveModelo) {
                done_imv.setImage(new Image(getClass().getResourceAsStream("/icons/saveOk.gif")));
                done_lbl.setText("Modelo agregado correctamente.");
                done_lbl.setStyle("-fx-text-fill: green;");
            } else {
                done_imv.setImage(new Image(getClass().getResourceAsStream("/icons/saveError.gif")));
                done_lbl.setText("Lo siento, algo salio mal...");
                done_lbl.setStyle("-fx-text-fill: red;");
            }

            pn_done.setVisible(true);
            next_button.setVisible(false);
            back_button.setVisible(false);
        }
    }

    //muestra la pantalla anterior del registro del modelo
    public void handleBackAction() {
        if (pnPieces.isVisible()) {
            pn_data_general.setVisible(true);
            pnPieces.setVisible(false);
            back_button.setVisible(false);
            next_button.setText("Siguiente");
        }
    }


    //muestra todas las piezas disponibles en la base de datos
    private void loadPiecesFromDB() {
        vbListaAllPieces.getChildren().clear();
        listaPiezasFromDB = con.getAllPiecesFromDB();
        listaPiezasDelModelo = new ArrayList<>();

        for (Pieza item : listaPiezasFromDB) {
            actionItemPart(item);
        }
    }

    private void actionItemPart(Pieza item) {
        ItemPiezaContador itemPiezaContador = new ItemPiezaContador(item, false);

        itemPiezaContador.setOnMouseClicked(mouseEvent -> {


            if (!listaPiezasDelModelo.contains(item)) {

                ItemPiezaContador modeloPart = new ItemPiezaContador(item, true);

                modeloPart.setOnMouseClicked(mouseEvent1 -> {
                    vbListaAllModelPieces.getChildren().remove(modeloPart);
                    listaPiezasDelModelo.removeIf(elemento -> elemento.getId_Pieza() == item.getId_Pieza());
                    contador_label.setText(listaPiezasDelModelo.size() + " Piezas añadidas");
                });


                vbListaAllModelPieces.getChildren().add(modeloPart);
                listaPiezasDelModelo.add(item);
                contador_label.setText(listaPiezasDelModelo.size() + " Piezas añadidas");
            }

        });
        vbListaAllPieces.getChildren().add(itemPiezaContador);
    }


    /*
     * Filtra las unidades que coincidan con la búsqueda y las muestra en la lista
     * */
    public void handleSearch() {
        vbListaAllPieces.getChildren().clear();
        List<Pieza> listaFiltrada = listaPiezasFromDB.stream().filter(item -> item.getNombrePieza().contains(txtBusqueda.getText())).toList();

        for (Pieza item : listaFiltrada) {
            actionItemPart(item);
        }

    }

    public void handleValidarID() {
        if (!modelo_textField.getText().isBlank()) {
            idExist = con.existModel(modelo_textField.getText());

            model_exist_label.setVisible(idExist);

        }
        handleValidarCampos();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = new Conexión();
       this.onDraggedScene(this.root);

        tipoCombustible_comboBox.getItems().addAll("Diésel" , "Gasolina", "Híbridos", "Eléctrico");
    }
}

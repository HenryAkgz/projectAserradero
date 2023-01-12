package controls;

import clases.DraggedScene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuControl implements Initializable, DraggedScene {
    @FXML
    private AnchorPane root;
    @FXML
    private Button close_button;
    @FXML
    private Button shrink_button;

    @FXML
    private BorderPane subScene_container;

    /*
     * carga cada la subScena y su controlador dentro de la scene principal
     * */
    private void loadSubScene(String name) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/layout/" + name + ".fxml"));
        Pane view = loader.load();
        view.setPrefSize(subScene_container.getWidth(), subScene_container.getHeight());
        subScene_container.setCenter(view);
    }

    //carga la sub escena de los modelos de aserraderos
    public void handleLoadSubSceneModel() throws IOException {
        loadSubScene("subSceneModel");
    }

    //carga la sub escena de las piezas de aserraderos
    public void handleLoadSubScenePart() throws IOException {
        loadSubScene("SubScenePieza");
    }

    //carga la sub escena de las unidades de aserraderos
    public void handleLoadSubSceneUnit() throws IOException {
        loadSubScene("SubSceneUnit");
    }


    /*
     * Estos métodos controlan el estado de la aplicación
     *
     * handleCloseButton terminará con la ejecución de la aplicación
     *
     * handleShrinkButton minimizará en la barra de tareas la aplicación
     * */
    public void handleCloseButton() {
        System.exit(0);
    }

    public void handleShrinkButton() {
        Stage stage = (Stage) shrink_button.getScene().getWindow();
        stage.setIconified(true);
    }

    /*
     * Este método es el primero que se ejecuta al cargar la escena
     *
     * en esta ocasión se carga la subSceneHome que muestra el logo de la empresa
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.onDraggedScene(this.root);
            loadSubScene("subSceneHome");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

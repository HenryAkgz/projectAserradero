package controls;

import clases.Constants;
import clases.DraggedScene;
import clases.Util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.controlsfx.tools.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class windowLayoutController implements Initializable, DraggedScene {
    @FXML
    private BorderPane window_root;
    @FXML
    private HBox button_window_controller_container;
    @FXML
    private AnchorPane shrink_button;
    @FXML
    private Label shrink_label;
    @FXML
    private AnchorPane maximize_button;
    @FXML
    private Label maximize_label;
    @FXML
    private AnchorPane close_button;
    @FXML
    private Label close_label;
    @FXML
    private AnchorPane anchorContent;

    FXMLLoader fxmlContentLoader = new FXMLLoader();

    private void loadWindowStyle() {
        button_window_controller_container.setStyle(Constants.WINDOW_CONTROLLER_CONTAINER_STYLE);
        shrink_button.setStyle(Constants.MAX_MIN_BUTTON_STYLE);
        shrink_label.setStyle(Constants.SHRINK_SHAPE_STYLE);
        shrink_button.setOnMouseClicked(action -> handleShrinkApp());
        maximize_button.setStyle(Constants.MAX_MIN_BUTTON_STYLE);
        maximize_label.setStyle(Constants.MAXIMIZE_SHAPE_STYLE);
        maximize_button.setOnMouseClicked(action -> handleMaximizeApp());
        close_button.setStyle(Constants.CLOSE_BUTTON_STYLE);
        close_label.setStyle(Constants.CLOSE_SHAPE_STYLE);
        close_button.setOnMouseClicked(action -> handleCloseApp());

        Util.setHover(shrink_button, shrink_label, Constants.MAX_MIN_BUTTON_STYLE, Constants.MAX_MIN_BUTTON_HOVER_STYLE, Constants.SHRINK_SHAPE_STYLE, Constants.SHRINK_SHAPE_HOVER_STYLE);
        Util.setHover(maximize_button, maximize_label, Constants.MAX_MIN_BUTTON_STYLE, Constants.MAX_MIN_BUTTON_HOVER_STYLE, Constants.MAXIMIZE_SHAPE_STYLE, Constants.MAXIMIZE_SHAPE_HOVER_STYLE);
        Util.setHover(close_button, close_label, Constants.CLOSE_BUTTON_STYLE, Constants.CLOSE_BUTTON_HOVER_STYLE, Constants.CLOSE_SHAPE_STYLE, Constants.CLOSE_SHAPE_HOVER_STYLE);
    }


    private void handleCloseApp() {
        System.exit(0);
    }

    private void handleShrinkApp() {
        Stage stage = (Stage) shrink_button.getScene().getWindow();
        stage.setIconified(true);
    }

    private void handleMaximizeApp() {
        Stage stage = (Stage) close_button.getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
    }

    public void setContent(FXMLLoader loader, Util.MiFuncion fun) {

        loadContent(loader, fun);

    }

    private void loadContent(FXMLLoader loader, Util.MiFuncion fun) {
        Pane view = null;
        try {
            view = loader.load();

            if(fun != null) {
                fun.ejecutar(loader);
            }
            view.setPrefSize(anchorContent.getWidth(), anchorContent.getHeight());

            anchorContent.getChildren().add(view);
            // window_root.setPrefSize(view.getWidth(), view.getHeight());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadWindowStyle();
        this.onDraggedScene(this.window_root);
    }
}

package controls;

import clases.ItemUnidad;
import clases.Unidad;
import conexión.Conexión;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SubSceneUnitController implements Initializable {
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

    //Variables
    Conexión con = new Conexión();
    ArrayList<Unidad> listAllUnits;


    private void getAllUnitsFromDB() {
        listAllUnits = con.getALLUnitsFromDB();
        showUnitInUI(listAllUnits);
    }

    private void showUnitInUI(List<Unidad> elementos) {

        if (elementos != null && elementos.size() > 0) {

            root.getChildren().remove(empty_data_container);

            if (!root.getChildren().contains(pn_part_content)) {
                root.getChildren().add(pn_part_content);
            }

            aside_list_units.getChildren().clear();
            elementos.forEach(item -> {
                ItemUnidad unitUI = new ItemUnidad(item);
                unitUI.setOnMouseClicked(action -> itemUnitAction(item));
                aside_list_units.getChildren().add(unitUI);
            });


        } else {
            if (!root.getChildren().contains(empty_data_container)) {
                root.getChildren().add(empty_data_container);
            }
        }

    }

    public void itemUnitAction(Unidad item) {
        if (pn_part_content.getChildren().contains(pane_waiting_clic)) {
            pn_part_content.getChildren().remove(pane_waiting_clic);
            pn_part_content.setCenter(pane_infoUnidad);
        }

    }

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

    public void handleSearch() {

    }

    public void handleChangeModelPhoto() {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con.connectWidthDB();
        root.getChildren().remove(layoutBox_container);
        getAllUnitsFromDB();

    }
}

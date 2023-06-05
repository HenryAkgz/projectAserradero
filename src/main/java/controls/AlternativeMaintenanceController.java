package controls;

import clases.ItemMantenimientoHistorial;
import clases.Mantenimiento;
import clases.MantenimientoHistorial;
import conexión.Conexión;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AlternativeMaintenanceController implements Initializable {
    @FXML
    private AnchorPane root;
    @FXML
    private BorderPane content_pane;

    @FXML
    private VBox extra_nodes;
    @FXML
    private VBox empty_view;
    @FXML
    private Label lbl_counter_1, lbl_counter_2, lbl_counter_3, lbl_counter_4, lbl_counter_5, lbl_step_1, lbl_step_2, lbl_step_3, lbl_step_4, lbl_step_5, lbl_back_button;
    @FXML
    private AnchorPane add_new_date_view, btn_back_setp_addNewData;
    @FXML
    private Pane icon_btn_back;
    @FXML
    private AnchorPane add_new_date_content_container;
    @FXML
    private VBox form_step_1_add_new_date, form_step_2_add_new_date, form_step_3_add_new_date, result_add_new_date;
    @FXML
    private AnchorPane form_step_4_add_new_date;
    @FXML
    private DatePicker add_new_date_picker;
    @FXML
    private ChoiceBox<String> add_new_date_type_cbx;
    @FXML
    private Button btn_next_1_add_new_date, btn_next_2_add_new_date, btn_next_3_add_new_date, btn_next_4_add_new_date;
    @FXML
    private ProgressBar progres_bar_new_date;


    Conexión con = new Conexión();
    ArrayList<Mantenimiento> listaMantenimientosProgrmados = new ArrayList<Mantenimiento>();
    ArrayList<MantenimientoHistorial> listaMantenimientoHistorial = new ArrayList<MantenimientoHistorial>();

    private String id_unidad = "";


    private void getDataFromDataBase() {
        listaMantenimientosProgrmados = con.getAllNextMaintenanceData(id_unidad);
        listaMantenimientoHistorial = con.getAllMaintenanceHistoryDataByUnit(id_unidad);
    }

    public void initContent() {
        root.getChildren().remove(extra_nodes);

        if (listaMantenimientosProgrmados.size() == 0 && listaMantenimientoHistorial.size() == 0) {
            setCenterContent(empty_view);
        }

    }

    public void setIdUnidad(String id) {
        this.id_unidad = id;
    }

    public void setCenterContent(Node content) {

        if (!content_pane.getCenter().equals(content)) {

            content_pane.getChildren().remove(content_pane.getCenter());

            content_pane.setCenter(content);
        }
    }


    public void handleShowAddNewDateMaintenanceForm() {

        //se añade a los botones de cada formulario la funcion que cambia al siguiente formulario
         btn_next_1_add_new_date.setOnMouseClicked(action->{
             setAddNewDateFormToMainView(form_step_2_add_new_date);
         });

         btn_next_2_add_new_date.setOnMouseClicked(action->{
             setAddNewDateFormToMainView(form_step_3_add_new_date);
         });

        btn_next_3_add_new_date.setOnMouseClicked(action->{
            setAddNewDateFormToMainView(form_step_4_add_new_date);
        });

        btn_next_4_add_new_date.setOnMouseClicked(action->{
            setAddNewDateFormToMainView(result_add_new_date);
        });

        //añade el primer form a la vista
        setAddNewDateFormToMainView(form_step_1_add_new_date);
        //añade la vista ala ventana principal
        setCenterContent(add_new_date_view);


        btn_back_setp_addNewData.setOnMouseEntered(action-> {
            btn_back_setp_addNewData.setStyle("-fx-background-color: white;");
            icon_btn_back.setStyle("-fx-background-color: #1f2233;");
            lbl_back_button.setStyle("-fx-text-fill: #1f2233;");
        });

        btn_back_setp_addNewData.setOnMouseExited(action-> {
            btn_back_setp_addNewData.setStyle("-fx-background-color: #1f2233;");
            icon_btn_back.setStyle("-fx-background-color: white;");
            lbl_back_button.setStyle("-fx-text-fill: white;");
        });
/*

        btn_next_1_add_new_date.setOnMouseClicked(action->{
            setContentFormAddNewDate(form_step_2_add_new_date, form_step_1_add_new_date);
            lbl_counter_2.setStyle("-fx-background-color: #8367C7;");
             progres_bar_new_date.progressProperty().set(0.32);
             lbl_step_2.setStyle("-fx-text-fill: #8367C7;");
            btn_back_setp_addNewData.setVisible(true);
            btn_back_setp_addNewData.setOnMouseClicked(back->{
                setContentFormAddNewDate(form_step_1_add_new_date, null);
                btn_back_setp_addNewData.setVisible(false);
                lbl_counter_2.setStyle("-fx-background-color: gray;");
                progres_bar_new_date.progressProperty().set(0.00);
                lbl_step_2.setStyle("-fx-text-fill: white;");

            });
        });*/


    }


    //############################# metodos para añadir una nueva fecha de mantenimiento ##############################

    private void setAddNewDateFormToMainView(Node form) {
        btn_back_setp_addNewData.setVisible(true);

        switch (form.getId()) {
            case "form_step_1_add_new_date":
                setContentFormAddNewDate(form_step_1_add_new_date);
                btn_back_setp_addNewData.setVisible(false);
                progres_bar_new_date.progressProperty().set(0.00);
                break;

            case "form_step_2_add_new_date":
                setContentFormAddNewDate(form_step_2_add_new_date);
                progres_bar_new_date.progressProperty().set(0.24);
                lbl_counter_2.setStyle("-fx-background-color: #8367C7; -fx-background-radius: 100");
                lbl_step_2.setStyle("-fx-text-fill: #8367C7;");
                btn_back_setp_addNewData.setOnMouseClicked(action -> {
                    setAddNewDateFormToMainView(form_step_1_add_new_date);
                });
                break;

            case "form_step_3_add_new_date":
                setContentFormAddNewDate(form_step_3_add_new_date);
                progres_bar_new_date.progressProperty().set(0.52);
                lbl_counter_3.setStyle("-fx-background-color: #8367C7; -fx-background-radius: 100");
                lbl_step_3.setStyle("-fx-text-fill: #8367C7;");
                btn_back_setp_addNewData.setOnMouseClicked(action -> {
                    setAddNewDateFormToMainView(form_step_2_add_new_date);
                });
                break;
            case "form_step_4_add_new_date":
                setContentFormAddNewDate(form_step_4_add_new_date);
                progres_bar_new_date.progressProperty().set(0.71);
                lbl_counter_4.setStyle("-fx-background-color: #8367C7; -fx-background-radius: 100");
                lbl_step_4.setStyle("-fx-text-fill: #8367C7;");
                btn_back_setp_addNewData.setOnMouseClicked(action -> {
                    setAddNewDateFormToMainView(form_step_3_add_new_date);
                });
                break;

            case "result_add_new_date":
                setContentFormAddNewDate(result_add_new_date);
                progres_bar_new_date.progressProperty().set(1.00);
                lbl_counter_5.setStyle("-fx-background-color: #8367C7; -fx-background-radius: 100");
                lbl_step_5.setStyle("-fx-text-fill: #8367C7;");
                btn_back_setp_addNewData.setOnMouseClicked(action -> {
                    setAddNewDateFormToMainView(form_step_4_add_new_date);
                });
                break;



            default:
                System.out.println(form.getId());
                break;
        }


    }

    private void setContentFormAddNewDate(Node current) {
        if (!add_new_date_content_container.getChildren().contains(current)) {

            add_new_date_content_container.getChildren().removeIf(node -> !node.getId().equals("btn_back_setp_addNewData"));

            add_new_date_content_container.getChildren().add(current);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initContent();

    }
}

package controls;

import clases.ItemMantenimientoHistorial;
import clases.Mantenimiento;
import clases.MantenimientoHistorial;
import conexión.Conexión;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AlternativeMaintenanceController implements Initializable {
    @FXML
    private AnchorPane root;
    @FXML
    private BorderPane content_pane;
    @FXML
    private Button btn_añadir_fotos;
    @FXML
    private VBox extra_nodes;
    @FXML
    private VBox empty_view;
    @FXML
    private Label lbl_counter_1, lbl_counter_2, lbl_counter_3, lbl_counter_4, lbl_counter_5, lbl_step_1, lbl_step_2, lbl_step_3, lbl_step_4, lbl_step_5, lbl_back_button, idUnit_add_new_date;
    @FXML
    private Label lbl_fecha_establecida, lbl_counter_fotos, add_newDate_lbl_fecha_programada_value, add_newDate_lbl_maintenance_type_value, add_new_label_comments_value;
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
    @FXML
    private TextArea text_area_detalles_mantenimiento;
    @FXML
    private VBox add_foto_info_container, card_añadir_fotos;
    @FXML
    private HBox contenodr_imagenes;
    @FXML
    private Button add_more_photos;
    @FXML
    private VBox evidence_result_container;
    @FXML
    private Label label_no_fotos_add_new, lblResultMensaje;
    @FXML
    private HBox contenodr_imagenes_result;
    @FXML
    private ImageView img_result_add_new_date;


    Conexión con = new Conexión();
    ArrayList<Mantenimiento> listaMantenimientosProgrmados = new ArrayList<Mantenimiento>();
    ArrayList<MantenimientoHistorial> listaMantenimientoHistorial = new ArrayList<MantenimientoHistorial>();

    // variables para añadir una nueva fecha de mantenimiento
    Mantenimiento mantenimientoTemporal;

    private String id_unidad = "";


    private void getDataFromDataBase() {
        listaMantenimientosProgrmados = con.getAllNextMaintenanceData(id_unidad);
        listaMantenimientoHistorial = con.getAllMaintenanceHistoryDataByUnit(id_unidad);
    }

    public void initContent() {
        //elimina de la ventana los contenedor de los forms extras
        root.getChildren().remove(extra_nodes);

        if (listaMantenimientosProgrmados.size() == 0 && listaMantenimientoHistorial.size() == 0) {
            setCenterContent(empty_view);
        } else if (listaMantenimientosProgrmados.size() > 0 || listaMantenimientoHistorial.size() > 0) {
            //no hacer nada... de momento
        }

    }

    public void setIdUnidad(String id) {
        System.out.println("uid unidad: " + id);
        this.id_unidad = id;
    }

    public void setCenterContent(Node content) {

        if (!content_pane.getCenter().equals(content)) {

            content_pane.getChildren().remove(content_pane.getCenter());

            content_pane.setCenter(content);
        }
    }


    //############################# metodos para añadir una nueva fecha de mantenimiento ##############################

    private void validarFecha() {
        try {
            LocalDate temp = add_new_date_picker.getValue();

            if (temp != null && !temp.isBefore(LocalDate.now())) {
                lbl_fecha_establecida.setText("Fecha del mantenimiento: " + temp.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
                mantenimientoTemporal.setMaintenanceDate(temp.toString());

            } else {
                lbl_fecha_establecida.setText("dd/mm/aaaa");

            }
        } catch (Exception e) {
            lbl_fecha_establecida.setText("dd/mm/aaaa");

        }
        validateAddNextDateForm(1);
    }

    private void validateAddNextDateForm(int form) {
        switch (form) {
            case 1:
                LocalDate temp = add_new_date_picker.getValue();

                btn_next_1_add_new_date.setVisible(temp != null && !temp.isBefore(LocalDate.now()) && add_new_date_type_cbx.getSelectionModel().getSelectedIndex() != 0);

                break;
            default:
                break;
        }
    }

    public void handleShowAddNewDateMaintenanceForm() {

        //se crea una instancia nueva de la clase mantenimiento
        idUnit_add_new_date.setText(id_unidad);
        mantenimientoTemporal = new Mantenimiento();
        mantenimientoTemporal.setIdUnit(id_unidad);
        btn_next_1_add_new_date.setVisible(false);

        //date filter para el datePicker
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

        //se configura el datePicker
        add_new_date_picker.setDayCellFactory(dayCellFactory);
        add_new_date_picker.setOnMouseClicked(action -> validarFecha());
        add_new_date_picker.setOnKeyTyped(action -> validarFecha());
        add_new_date_picker.setOnAction(action -> {
            validarFecha();
        });

        //se agregan los tipos de mantenimiento al cbx
        add_new_date_type_cbx.setOnAction(action -> validateAddNextDateForm(1));
        add_new_date_type_cbx.getItems().addAll("Selecciona un tipo", "Correctivo", "Preventivo");
        add_new_date_type_cbx.setValue("Selecciona un tipo");


        //se añade a los botones de cada formulario la funcion que cambia al siguiente formulario
        btn_next_1_add_new_date.setOnMouseClicked(action -> {
            setAddNewDateFormToMainView(form_step_2_add_new_date);
        });

        btn_next_2_add_new_date.setOnMouseClicked(action -> {
            if (!text_area_detalles_mantenimiento.getText().isBlank()) {
                mantenimientoTemporal.setMaintenanceNotes(text_area_detalles_mantenimiento.getText().trim());
            }
            mantenimientoTemporal.setMaintenanceType(add_new_date_type_cbx.getSelectionModel().getSelectedItem().toString());
            setAddNewDateFormToMainView(form_step_3_add_new_date);
        });

        btn_next_3_add_new_date.setOnMouseClicked(action -> {
            setAddNewDateFormToMainView(form_step_4_add_new_date);
        });

        btn_next_4_add_new_date.setOnMouseClicked(action -> {
            handleSaveMaintenanceData();
        });

        //añade el primer form a la vista
        setAddNewDateFormToMainView(form_step_1_add_new_date);
        //añade la vista ala ventana principal
        setCenterContent(add_new_date_view);


        btn_back_setp_addNewData.setOnMouseEntered(action -> {
            btn_back_setp_addNewData.setStyle("-fx-background-color: white;");
            icon_btn_back.setStyle("-fx-background-color: #1f2233;");
            lbl_back_button.setStyle("-fx-text-fill: #1f2233;");
        });

        btn_back_setp_addNewData.setOnMouseExited(action -> {
            btn_back_setp_addNewData.setStyle("-fx-background-color: #1f2233;");
            icon_btn_back.setStyle("-fx-background-color: white;");
            lbl_back_button.setStyle("-fx-text-fill: white;");
        });


    }

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
                    lbl_counter_2.setStyle("-fx-background-color: gray; -fx-background-radius: 100");
                    lbl_step_2.setStyle("-fx-text-fill: white;");
                    setAddNewDateFormToMainView(form_step_1_add_new_date);
                });
                break;

            case "form_step_3_add_new_date":
                setContentFormAddNewDate(form_step_3_add_new_date);
                progres_bar_new_date.progressProperty().set(0.52);
                lbl_counter_3.setStyle("-fx-background-color: #8367C7; -fx-background-radius: 100");
                lbl_step_3.setStyle("-fx-text-fill: #8367C7;");
                btn_back_setp_addNewData.setOnMouseClicked(action -> {
                    lbl_counter_3.setStyle("-fx-background-color: gray; -fx-background-radius: 100");
                    lbl_step_3.setStyle("-fx-text-fill: white;");
                    setAddNewDateFormToMainView(form_step_2_add_new_date);
                });

                break;
            case "form_step_4_add_new_date":
                setContentFormAddNewDate(form_step_4_add_new_date);
                progres_bar_new_date.progressProperty().set(0.71);
                lbl_counter_4.setStyle("-fx-background-color: #8367C7; -fx-background-radius: 100");
                lbl_step_4.setStyle("-fx-text-fill: #8367C7;");
                add_newDate_lbl_fecha_programada_value.setText(LocalDate.parse(mantenimientoTemporal.getMaintenanceDate()).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                add_newDate_lbl_maintenance_type_value.setText(mantenimientoTemporal.getMaintenanceType());

                if (mantenimientoTemporal.getMaintenanceNotes() == null || mantenimientoTemporal.getMaintenanceNotes().isBlank()) {
                    add_new_label_comments_value.setText("Sin comentarios...");
                } else {
                    add_new_label_comments_value.setText(mantenimientoTemporal.getMaintenanceNotes());
                }

                contenodr_imagenes_result.getChildren().clear();
                evidence_result_container.getChildren().removeAll(label_no_fotos_add_new, contenodr_imagenes_result);

                if (mantenimientoTemporal.getMaintenanceImagesPaths().size() > 0) {
                    evidence_result_container.getChildren().add(contenodr_imagenes_result);
                    mantenimientoTemporal.getMaintenanceImagesPaths().forEach(path -> {
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/layout/evidence_item.fxml"));
                            AnchorPane itemImagen = loader.load();
                            ItemImageController itemController = loader.getController();
                            itemController.setImage(path);
                            itemController.setHideDeleteButton(true);
                            contenodr_imagenes_result.getChildren().add(itemImagen);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    evidence_result_container.getChildren().add(label_no_fotos_add_new);
                }
                btn_back_setp_addNewData.setOnMouseClicked(action -> {
                    setAddNewDateFormToMainView(form_step_3_add_new_date);
                });
                break;

            case "result_add_new_date":
                setContentFormAddNewDate(result_add_new_date);
                progres_bar_new_date.progressProperty().set(1.00);
                lbl_counter_5.setStyle("-fx-background-color: #8367C7; -fx-background-radius: 100");
                lbl_step_5.setStyle("-fx-text-fill: #8367C7;");
                btn_back_setp_addNewData.setVisible(false);
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


    public void handleGetPhotoPathsFromHDSK() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
        chooser.setTitle("Selecciona las fotos que deseas agregar");

        List<File> selectedFiles = chooser.showOpenMultipleDialog(new Stage());

        contenodr_imagenes.getChildren().clear();

        if (selectedFiles != null) {

            if (card_añadir_fotos.getChildren().contains(add_foto_info_container)) {
                card_añadir_fotos.getChildren().remove(add_foto_info_container);
                card_añadir_fotos.getChildren().add(contenodr_imagenes);
            }


            if (mantenimientoTemporal.getMaintenanceImagesPaths().size() == 0) {
                mantenimientoTemporal.setMaintenanceImagesPaths(new ArrayList<>(selectedFiles.stream().map(item -> item.getAbsolutePath()).toList()));
            } else {
                selectedFiles.forEach(item -> {
                    if (mantenimientoTemporal.getMaintenanceImagesPaths().size() < 7) {
                        mantenimientoTemporal.getMaintenanceImagesPaths().add(item.getAbsolutePath());
                    }
                });
            }

            mantenimientoTemporal.getMaintenanceImagesPaths().forEach(path -> {
                try {
                    System.out.println(path);
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/layout/evidence_item.fxml"));
                    AnchorPane itemImagen = loader.load();
                    ItemImageController itemController = loader.getController();
                    itemController.setImage(path);

                    itemController.getDeleteButton().setOnMouseClicked(action -> {
                        deletePhotoFromReport(path);
                        contenodr_imagenes.getChildren().removeIf(view -> view == itemImagen);
                        lbl_counter_fotos.setText(Integer.toString(mantenimientoTemporal.getMaintenanceImagesPaths().size()));
                        if (mantenimientoTemporal.getMaintenanceImagesPaths().size() == 0) {
                            card_añadir_fotos.getChildren().clear();
                            card_añadir_fotos.getChildren().add(add_foto_info_container);
                            lbl_counter_fotos.setText("0");
                            add_more_photos.setVisible(false);
                        } else {
                            lbl_counter_fotos.setText(Integer.toString(mantenimientoTemporal.getMaintenanceImagesPaths().size()));
                            add_more_photos.setVisible(mantenimientoTemporal.getMaintenanceImagesPaths().size() < 7);
                        }
                    });

                    contenodr_imagenes.getChildren().add(itemImagen);
                    add_more_photos.setVisible(mantenimientoTemporal.getMaintenanceImagesPaths().size() > 0 && mantenimientoTemporal.getMaintenanceImagesPaths().size() < 6);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            lbl_counter_fotos.setText(Integer.toString(mantenimientoTemporal.getMaintenanceImagesPaths().size()));

        }


    }


    private void deletePhotoFromReport(String path) {
        mantenimientoTemporal.getMaintenanceImagesPaths().remove(path);
    }

    public void handleSaveMaintenanceData(){

        if(con.saveMantenimientoProgramado(id_unidad, mantenimientoTemporal.getMaintenanceDate(), mantenimientoTemporal.getMaintenanceNotes())){
            img_result_add_new_date.setImage(new Image(getClass().getResourceAsStream("/icons/saveOk.gif")));
            lblResultMensaje.setText("Mantenimiento programado correctamente.");
            lblResultMensaje.setStyle("-fx-text-fill: green;");
        } else {
            img_result_add_new_date.setImage(new Image(getClass().getResourceAsStream("/icons/saveError.gif")));
            lblResultMensaje.setText("Lo siento, algo salio mal...");
            lblResultMensaje.setStyle("-fx-text-fill: red;");
        }

        setAddNewDateFormToMainView(result_add_new_date);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initContent();

    }
}

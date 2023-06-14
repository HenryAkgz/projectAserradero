package controls;

import clases.*;
import conexión.Conexión;
import conexión.Conexión_old;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.*;

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
    @FXML
    private AnchorPane main_menu_view;
    @FXML
    private Label main_menu_id_label;
    @FXML
    private ImageView main_menu_unit_photo;
    @FXML
    private Label main_menu_inicio_unidad_label, lbl_main_menu_counter_programados, lbl_main_menu_counter_en_progreso, lbl_main_menu_counter_terminados, lbl_main_menu_title_ultimos_meses;
    @FXML
    private BarChart<String, Number> bc_main_menu_inicio_ultmimos_meses;
    @FXML
    private ChoiceBox<String> cbx_main_menu_inicio_tiempo_ultimos_meses;
    @FXML
    private CategoryAxis ultimos_meses_category_axis;
    @FXML
    private NumberAxis ultimos_meses_values_axis;
    @FXML
    private ComboBox<String> cbx_main_menu_mes, cbx_main_menu_tipo, cbx_main_menu_estado;
    @FXML
    private VBox vb_main_menu_table_item_container, showData_container_fecha, showData_container_tipo, showData_container_notas, showData_container_evidence;
    @FXML
    private AnchorPane showMaintenanceData_view, btn_back_showMaintenanceData;
    @FXML
    private HBox btn_container_showMaintenanceData;
    @FXML
    private Label lbl_showData_fecha, lbl_showData_tipo, lbl_showData_descripcion, lbl_showData_evidence, lbl_contador_imagenes_showData;
    @FXML
    private DatePicker dtp_showData_fecha;
    @FXML
    private ChoiceBox<String> cbx_showData_tipo;
    @FXML
    private TextArea txt_showData_descripcion;
    @FXML
    private HBox showData_list_imageItem;
    @FXML
    private BorderPane showData_container_imagenes_evidence;
    @FXML
    private Button btn_showData_saveUpdate, add_more_image_showData;



    Conexión con = Conexión.getInstancia();

    //ArrayList<Mantenimiento> listaMantenimientosProgrmados = new ArrayList<Mantenimiento>();
    //ArrayList<MantenimientoHistorial> listaMantenimientoHistorial = new ArrayList<MantenimientoHistorial>();

    ObservableList<Mantenimiento> listaMantenimientosProgrmados = FXCollections.observableArrayList();
    ObservableList<MantenimientoHistorial> listaMantenimientoHistorial = FXCollections.observableArrayList();
    String ultimo_mantenimiento_programado = Constants.UNIDAD_SIN_MANTENIMIENTO_PROGRAMADO;
    String ultimo_mantenimiento_realizado = Constants.UNIDAD_SIN_MANTENIMIENTO;


    // variables para añadir una nueva fecha de mantenimiento
    private Mantenimiento mantenimientoTemporal;
    private Mantenimiento mantenimientoUpdate;
    private Unidad unidad;
    Button_with_icon btnUpdate = new Button_with_icon();
    private StringProperty id_unidad = new SimpleStringProperty("");

    List<Image> listaImagenesUpdate = new ArrayList<Image>();


    private void getDataFromDataBase() {
        System.out.println("buscando datos de la unidad:" + id_unidad.get());
        listaMantenimientosProgrmados.clear();
        listaMantenimientoHistorial.clear();
        listaMantenimientosProgrmados.addAll(con.obtenerFechasMantenimientoProgramadoDeLaUnidad(id_unidad.get()));
        listaMantenimientoHistorial.addAll(con.obtenerMantenimientoHistorialPorUnidad(id_unidad.get()));
        System.out.println("Resultados..... programmados: " + listaMantenimientosProgrmados.size() + " elementos encontrados    \n historial: " + listaMantenimientoHistorial.size());
    }

    public void initContent() {
        //elimina de la ventana los contenedor de los forms extras
        System.out.println("entrando en init " + id_unidad.get());
        root.getChildren().remove(extra_nodes);

        unidad = con.obtenerUnidadPorId(id_unidad.get());

        LocalDate fecha_programada_temp = con.obtenerFechaProximoMantenimiento(id_unidad.get());

        if(fecha_programada_temp != null ){
            ultimo_mantenimiento_programado = fecha_programada_temp.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
        }

        LocalDate fecha_realizada_temp = con.obtenerFechaDelUltimoMantenimientoRealizado(id_unidad.get());

        if(fecha_realizada_temp != null ) {
            ultimo_mantenimiento_realizado = fecha_programada_temp.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
        }

        System.out.println(ultimo_mantenimiento_programado);
        System.out.println(ultimo_mantenimiento_realizado);

        getDataFromDataBase();

        if (ultimo_mantenimiento_programado.equals(Constants.UNIDAD_SIN_MANTENIMIENTO_PROGRAMADO) && ultimo_mantenimiento_realizado.equals(Constants.UNIDAD_SIN_MANTENIMIENTO)) {
            setCenterContent(empty_view);
        } else if (listaMantenimientosProgrmados.size() > 0 || listaMantenimientoHistorial.size() > 0) {
            main_menu_id_label.setText(id_unidad.get());
            main_menu_unit_photo.setImage(new Image(new ByteArrayInputStream(unidad.getPhotoUnidad())));
            Circle clip = new Circle();
            clip.setCenterX(main_menu_unit_photo.getFitWidth() / 2);
            clip.setCenterY(main_menu_unit_photo.getFitHeight() / 2);
            clip.setRadius(Math.min(main_menu_unit_photo.getFitWidth(), main_menu_unit_photo.getFitHeight()) / 2);
            main_menu_unit_photo.setClip(clip);
            main_menu_inicio_unidad_label.setText(id_unidad.get());
            crearGraficaUltimosMantenimientos();
            setCenterContent(main_menu_view);
            rellenarTablaMainMenu();
        }

    }

    public void initComponents(){

        add_new_date_type_cbx.getItems().addAll("Selecciona un tipo", "Correctivo", "Preventivo");
        cbx_showData_tipo.getItems().addAll("Correctivo", "Preventivo");
        listaMantenimientosProgrmados.addListener(new ListChangeListener<Mantenimiento>() {
            @Override
            public void onChanged(Change<? extends Mantenimiento> change) {
                lbl_main_menu_counter_programados.setText(Integer.toString(listaMantenimientosProgrmados.size()));
            }
        });

        listaMantenimientoHistorial.addListener(new ListChangeListener<MantenimientoHistorial>() {
            @Override
            public void onChanged(Change<? extends MantenimientoHistorial> change) {
                int total_terminado = 0;
                int total_progreso = 0;

                for (MantenimientoHistorial m : listaMantenimientoHistorial) {
                    if (m.getEstado().equals(Constants.ESTADO_MANTENIMIENTO_OK)) {
                        total_terminado++;
                    } else if (m.getEstado().equals(Constants.UNIDAD_EN_MANTENIMIENTO)) {
                        total_progreso++;
                    }

                    lbl_main_menu_counter_terminados.setText(Integer.toString(total_terminado));
                    lbl_main_menu_counter_en_progreso.setText(Integer.toString(total_progreso));
                }

            }
        });
        id_unidad.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                initContent();
            }
        });

        cbx_main_menu_mes.getItems().addAll("Cualquier mes", Month.JANUARY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                Month.FEBRUARY.getDisplayName(TextStyle.FULL, Locale.getDefault()), Month.MARCH.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                Month.APRIL.getDisplayName(TextStyle.FULL, Locale.getDefault()), Month.MAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                Month.JUNE.getDisplayName(TextStyle.FULL, Locale.getDefault()), Month.JULY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                Month.AUGUST.getDisplayName(TextStyle.FULL, Locale.getDefault()), Month.SEPTEMBER.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                Month.OCTOBER.getDisplayName(TextStyle.FULL, Locale.getDefault()), Month.NOVEMBER.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                Month.DECEMBER.getDisplayName(TextStyle.FULL, Locale.getDefault()));

        cbx_main_menu_tipo.getItems().addAll("Cualquier tipo", Constants.TIPO_DE_MANTENIMIENTO_PREVENTIVO, Constants.TIPO_DE_MANTENIMIENTO_CORRECTIVO);
        cbx_main_menu_estado.getItems().addAll("Cualquier estado", Constants.ESTADO_MANTENIMIENTO_PROGRAMADO, Constants.ESTADO_MANTENIMIENTO_PENDIENTE, Constants.ESTADO_MANTENIMIENTO_OK,
                Constants.ESTADO_MANTENIMIENTO_STOP, Constants.UNIDAD_EN_MANTENIMIENTO);

        cbx_main_menu_estado.getSelectionModel().select(0);
        cbx_main_menu_tipo.getSelectionModel().select(0);
        cbx_main_menu_mes.getSelectionModel().select(0);
        cbx_main_menu_mes.setOnAction(action -> rellenarTablaMainMenu());
        cbx_main_menu_tipo.setOnAction(action -> rellenarTablaMainMenu());
        cbx_main_menu_estado.setOnAction(action -> rellenarTablaMainMenu());

        Button_with_icon btnDelete = new Button_with_icon();
        btnDelete.makeContainer("white", "10px", "10px 15px", 10.0);
        btnDelete.makeContentButton("icon_delete_model", 15.0, "#161925", "Eliminar mantenimiento");
        btnDelete.createButton();
        btnDelete.setOnMouseClicked(action -> {
            //showDelete()
        });


        btnUpdate.makeContainer("white", "10px", "10px 15px", 10.0);
        btnUpdate.makeContentButton("icon_update_details", 15.0, "#161925", "Editar datos del mantenimiento");
        btnUpdate.createButton();


        Button_with_icon btnMantenimiento = new Button_with_icon();
        btnMantenimiento.makeContainer("white", "10px", "10px 15px", 10.0);
        btnMantenimiento.makeContentButton("icon_mantenimiento", 15.0, "#161925", "Realizar mantenimiento");
        btnMantenimiento.createButton();
        btnMantenimiento.setOnMouseClicked(action -> {
           // showChangeState()
        });

        btn_container_showMaintenanceData.getChildren().addAll(btnMantenimiento, btnUpdate, btnDelete);
    }

    public void setIdUnidad(String id) {
        System.out.println("uid unidad: " + id);
        this.id_unidad.set(id);
    }

    public void setCenterContent(Node content) {

        if (!content_pane.getCenter().equals(content)) {

            content_pane.getChildren().remove(content_pane.getCenter());

            content_pane.setCenter(content);
        }
    }

    //############################# metodos para el menu principal ##############################
    private void rellenarGraficaPastel(int numeroDeUltimosMeses) {
        XYChart.Series<String, Number> seriePreventivo = new XYChart.Series<>();
        XYChart.Series<String, Number> serieCorrectivo = new XYChart.Series<>();

        seriePreventivo.setName("Preventivo");
        serieCorrectivo.setName("Correctivo");


        List<Month> ultimosMeses = getLastMonths(numeroDeUltimosMeses);


        for (Month mes: ultimosMeses) {
            int[] contador= getMaintenanceCountForLastMonthChart(mes);
            seriePreventivo.getData().add(new XYChart.Data<>(mes.getDisplayName(TextStyle.FULL, Locale.getDefault()),  contador[0])); // contador[0] numero de preventivos
            serieCorrectivo.getData().add(new XYChart.Data<>(mes.getDisplayName(TextStyle.FULL, Locale.getDefault()), contador[1])); // contador[1] numero de correctivos
        };

        bc_main_menu_inicio_ultmimos_meses.getData().clear();
        bc_main_menu_inicio_ultmimos_meses.getData().addAll(seriePreventivo, serieCorrectivo);
    }

    private int[] getMaintenanceCountForLastMonthChart(Month mes){
        int[] contador = new int[2];

        contador[0] = 0; // contador[0] numero de preventivos
        contador[1] = 0; //contador[1] numero de correctivos

     List<Mantenimiento> mantenimientosProgramadosFiltrados = listaMantenimientosProgrmados.stream().filter(
              item -> {
                  LocalDate fecha_item = LocalDate.parse(item.getMaintenanceDate());
                  System.out.println(fecha_item.getMonth());
                  return fecha_item.getMonth().equals(mes);
              }
      ).toList();

        for (Mantenimiento item: mantenimientosProgramadosFiltrados ) {
            if(item.getMaintenanceType().equals(Constants.TIPO_DE_MANTENIMIENTO_PREVENTIVO)){
                contador[0]++;
            }else if (item.getMaintenanceType().equals(Constants.TIPO_DE_MANTENIMIENTO_CORRECTIVO)){
                contador[1]++;
            }
        }

        List<MantenimientoHistorial> mantenimientosEnHistorialFiltrados = listaMantenimientoHistorial.stream().filter(
                item -> {
                    LocalDate fecha_item = LocalDate.parse(item.getFecha_inicio_mantenimiento(), DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
                    System.out.println(fecha_item.getMonth());
                    return fecha_item.getMonth().equals(mes);
                }
        ).toList();

        for (MantenimientoHistorial item: mantenimientosEnHistorialFiltrados ) {
            if(item.getTipoMantenimiento().equals(Constants.TIPO_DE_MANTENIMIENTO_PREVENTIVO)){
                contador[0]++;
            }else if (item.getTipoMantenimiento().equals(Constants.TIPO_DE_MANTENIMIENTO_CORRECTIVO)){
                contador[1]++;
            }
        }

        System.out.println("resultados del contadpr");
        System.out.println("preventivos "+contador[0]);
        System.out.println("correctivos "+ contador[1]);

        return contador;
    }
    public List<Month> getLastMonths(int numberMonths) {
        Month[] monthNames = {Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY, Month.JUNE,
                Month.JULY, Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER};
        List<Month> lastMonths = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        // Obtener la fecha actual
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
// Retroceder en el tiempo para obtener los últimos meses
        for (int i = 0; i < numberMonths; i++) {
            // Agregar el mes al resultado
            lastMonths.add(monthNames[currentMonth]);

            // Retroceder un mes
            currentMonth--;
            if (currentMonth < 0) {
                // Retroceder un año y volver al mes de diciembre
                currentMonth = 11;
                currentYear--;
            }
        }

        return lastMonths;
    }

    private void crearGraficaUltimosMantenimientos() {
        cbx_main_menu_inicio_tiempo_ultimos_meses.getItems().addAll("1 mes", "3 meses", "6 meses", "Año");
        cbx_main_menu_inicio_tiempo_ultimos_meses.setOnAction(optionSelected -> {
            String title = "";
            int noMeses =0;

            switch (cbx_main_menu_inicio_tiempo_ultimos_meses.getSelectionModel().getSelectedItem()) {
                case "1 mes":
                    title += "Ultimo mes";
                    noMeses = 1;
                    break;
                case "3 meses":
                    title += "Ultimos 3 meses";
                    noMeses = 3;
                    break;

                case "6 meses":
                    title += "Ultimos 6 meses";
                    noMeses = 6;
                    break;

                case "Año":
                    title += "Ultimo año";
                    noMeses = 12;
                    break;
            }
            lbl_main_menu_title_ultimos_meses.setText(title);
            rellenarGraficaPastel(noMeses);
        });
        cbx_main_menu_inicio_tiempo_ultimos_meses.getSelectionModel().select(0);
    }

    private void rellenarTablaMainMenu(){
      vb_main_menu_table_item_container.getChildren().clear();

        for (Mantenimiento item : listaMantenimientosProgrmados) {
            if (cbx_main_menu_estado.getSelectionModel().isSelected(0) || cbx_main_menu_estado.getSelectionModel().getSelectedItem().equals(Constants.ESTADO_MANTENIMIENTO_PROGRAMADO)) {
                if (cbx_main_menu_mes.getSelectionModel().isSelected(0) || cbx_main_menu_mes.getSelectionModel().getSelectedItem().equals(Util.getMonthOfDateString(item.getMaintenanceDate()))) {
                    if (cbx_main_menu_tipo.getSelectionModel().isSelected(0) || cbx_main_menu_tipo.getSelectionModel().getSelectedItem().equals(item.getMaintenanceType())) {
                        agregarItemALaTablaMainMenu(item.getId_mantenimiento_programado(), Constants.ESTADO_MANTENIMIENTO_PROGRAMADO, item.getMaintenanceDate(), item.getMaintenanceType(), item.getMaintenanceNotes());
                    }
                }
            }
        }

        for (MantenimientoHistorial item : listaMantenimientoHistorial) {
            if (cbx_main_menu_estado.getSelectionModel().isSelected(0) || cbx_main_menu_estado.getSelectionModel().getSelectedItem().equals(item.getEstado())) {
                if (cbx_main_menu_mes.getSelectionModel().isSelected(0) || cbx_main_menu_mes.getSelectionModel().getSelectedItem().equals(Util.getMonthOfDateString(item.getFecha_programada()))) {
                    if (cbx_main_menu_tipo.getSelectionModel().isSelected(0) || cbx_main_menu_tipo.getSelectionModel().getSelectedItem().equals(item.getTipoMantenimiento())) {
                        agregarItemALaTablaMainMenu(item.getIdMantenimiento(), item.getEstado(), item.getFecha_programada(), item.getTipoMantenimiento(), item.getNotas_mantenimiento_programado());
                    }
                }
            }
        }

    }

    private void agregarItemALaTablaMainMenu(int id, String estado, String fecha, String tipo, String descripcion){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/layout/maintenance_item_large.fxml"));
            AnchorPane itemUi = loader.load();
            MaintenanceItemLargeController itemController = loader.getController();
            itemController.setInfo(id, estado, fecha, tipo, descripcion);
            itemUi.setOnMouseClicked(action->{
                if(action.getClickCount() == 2){
                    openMaintenanceData(id, estado);
                    btnUpdate.setOnMouseClicked(event -> {
                        initShowData(id);
                        showDataShowUpdateForm(true);
                    });
                }
            });
            vb_main_menu_table_item_container.getChildren().add(itemUi);
        }catch (Exception e){
           // System.out.println("Error al agregar mantenimiento a la interfaz: " + e.getMessage());
            e.printStackTrace();
        }
    }


    //############################# metodos para añadir una nueva fecha de mantenimiento ##############################

    private void validarFecha() {
        try {
            LocalDate temp = add_new_date_picker.getValue();

            if (temp != null && !temp.isBefore(LocalDate.now())) {
                lbl_fecha_establecida.setText("Fecha del mantenimiento: " + temp.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
                mantenimientoTemporal.setMaintenanceDate(temp.toString());
                System.out.println(temp.toString());

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
        idUnit_add_new_date.setText(id_unidad.getValue());
        mantenimientoTemporal = new Mantenimiento();
        mantenimientoTemporal.setIdUnit(id_unidad.getValue());
        btn_next_1_add_new_date.setVisible(false);
        text_area_detalles_mantenimiento.setText("");
        add_new_date_picker.setValue(null);

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
        add_new_date_type_cbx.getSelectionModel().select(0);


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
                //btn_back_setp_addNewData.setVisible(false);
                progres_bar_new_date.progressProperty().set(0.00);
                btn_back_setp_addNewData.setOnMouseClicked(action ->{
                    initContent();
                });
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

                if (!card_añadir_fotos.getChildren().contains(add_foto_info_container)) {
                    card_añadir_fotos.getChildren().remove(contenodr_imagenes);
                    card_añadir_fotos.getChildren().add(add_foto_info_container);
                }

                lbl_counter_fotos.setText(Integer.toString(mantenimientoTemporal.getMaintenanceImagesPaths().size()));
                add_more_photos.setVisible(false);

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
                add_newDate_lbl_fecha_programada_value.setText(LocalDate.parse(mantenimientoTemporal.getMaintenanceDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
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

    public void handleSaveMaintenanceData() {

        if (con.agregarMantenimientoProgramado(mantenimientoTemporal)) {
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

    public void handleGotoMainView() {
        initContent();
    }

    //############################# metodos para ver datos de mantenimiento ##############################

    private void initShowData(int idMantenimiento){
       mantenimientoUpdate = new Mantenimiento();
       listaImagenesUpdate = new ArrayList<Image>();
       mantenimientoUpdate = con.obtenerMantenimientoProgramadoPorId(idMantenimiento);
       btn_showData_saveUpdate.setOnMouseClicked(action -> {
           handleSaveUpdateData(idMantenimiento);
       });


       LocalDate dateTempUpdate = LocalDate.parse(mantenimientoUpdate.getMaintenanceDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
       lbl_showData_fecha.setText(dateTempUpdate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
       lbl_showData_tipo.setText(mantenimientoUpdate.getMaintenanceType());
       dtp_showData_fecha.setValue(dateTempUpdate);
       cbx_showData_tipo.getSelectionModel().select(mantenimientoUpdate.getMaintenanceType());

        if(mantenimientoUpdate.getMaintenanceNotes().isBlank()){
            lbl_showData_descripcion.setText("No hay notas de mantenimiento...");
            txt_showData_descripcion.setText("No hay notas de mantenimiento...");
        }else{
            lbl_showData_descripcion.setText(mantenimientoUpdate.getMaintenanceNotes());
            txt_showData_descripcion.setText(mantenimientoUpdate.getMaintenanceNotes());
        }

        if(mantenimientoUpdate.getJSONArrayImagenes().isBlank()){
            showData_container_evidence.getChildren().remove(showData_container_imagenes_evidence);
            if(!showData_container_evidence.getChildren().contains(lbl_showData_evidence)){
                showData_container_evidence.getChildren().add(lbl_showData_evidence);
            }
        }else {
            showData_container_evidence.getChildren().remove(lbl_showData_evidence);
            if(!showData_container_evidence.getChildren().contains(showData_container_imagenes_evidence)){
                showData_container_evidence.getChildren().add(showData_container_imagenes_evidence);
            }
            if(!showData_container_imagenes_evidence.getChildren().contains(showData_list_imageItem)){
                showData_container_imagenes_evidence.getChildren().add(showData_list_imageItem);
            }


            listaImagenesUpdate = mantenimientoUpdate.getDecodeImages();
            rellenarImagenesContainerUpdate(false);
        }

       showDataShowUpdateForm(false);
    }

    private void rellenarImagenesContainerUpdate(boolean enableDelete){
        showData_list_imageItem.getChildren().clear();


        if(listaImagenesUpdate.size()> 0){

            lbl_contador_imagenes_showData.setText(Integer.toString(listaImagenesUpdate.size()));


            int imac = 0;

            try{
                for (Image imagen: listaImagenesUpdate ) {

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/layout/evidence_item.fxml"));
                    AnchorPane itemImagen = loader.load();
                    ItemImageController itemController = loader.getController();
                    String name = "image_"+imac++;
                    itemController.setImage(imagen, name);
                    itemController.setHideDeleteButton(!enableDelete);

                    if(enableDelete){
                     itemController.getDeleteButton().setOnMouseClicked(action->{
                         listaImagenesUpdate.remove(imagen);
                          showData_list_imageItem.getChildren().remove(itemImagen);
                          lbl_contador_imagenes_showData.setText(Integer.toString(listaImagenesUpdate.size()));
                         add_more_image_showData.setVisible((listaImagenesUpdate.size()==6 || listaImagenesUpdate.size() == 0));
                     });
                    }

                    showData_list_imageItem.getChildren().add(itemImagen);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void showDataShowUpdateForm(boolean showForm){

        if(showForm){
            showData_container_fecha.getChildren().remove(lbl_showData_fecha);
            showData_container_tipo.getChildren().remove(lbl_showData_tipo);
            showData_container_notas.getChildren().remove(lbl_showData_descripcion);

            if(!showData_container_fecha.getChildren().contains(dtp_showData_fecha)){
                showData_container_fecha.getChildren().add(dtp_showData_fecha);
            }

            if(!showData_container_tipo.getChildren().contains(cbx_showData_tipo)){
                showData_container_tipo.getChildren().add(cbx_showData_tipo);
            }

            if(!showData_container_notas.getChildren().contains(txt_showData_descripcion)){
                showData_container_notas.getChildren().add(txt_showData_descripcion);
            }

            rellenarImagenesContainerUpdate(true);

            btn_container_showMaintenanceData.setVisible(false);
            btn_showData_saveUpdate.setVisible(true);
            add_more_image_showData.setVisible(!(listaImagenesUpdate.size()==6 || listaImagenesUpdate.size() == 0));

        }else{
            btn_container_showMaintenanceData.setVisible(true);
            showData_container_fecha.getChildren().remove(dtp_showData_fecha);
            showData_container_tipo.getChildren().remove(cbx_showData_tipo);
            showData_container_notas.getChildren().remove(txt_showData_descripcion);
            btn_showData_saveUpdate.setVisible(false);
            add_more_image_showData.setVisible(false);


            if(!showData_container_fecha.getChildren().contains(lbl_showData_fecha)){
                showData_container_fecha.getChildren().add(lbl_showData_fecha);
            }

            if(!showData_container_tipo.getChildren().contains(lbl_showData_tipo)){
                showData_container_tipo.getChildren().add(lbl_showData_tipo);
            }

            if(!showData_container_notas.getChildren().contains(lbl_showData_descripcion)){
                showData_container_notas.getChildren().add(lbl_showData_descripcion);
            }

        }
    }
    private void openMaintenanceData(int id, String estado){
       if(estado.equals(Constants.ESTADO_MANTENIMIENTO_PROGRAMADO)){
           setCenterContent(showMaintenanceData_view);
           initShowData(id);
       }
    }

    public void handleSaveUpdateData(int idMantenimiento){
       Mantenimiento temp = new Mantenimiento();
       temp.setId_mantenimiento_programado(idMantenimiento);
       temp.setMaintenanceDate(dtp_showData_fecha.getValue().toString());
       temp.setMaintenanceType(cbx_showData_tipo.getSelectionModel().getSelectedItem());
       temp.setMaintenanceNotes(txt_showData_descripcion.getText());
       temp.encodeImagesToJson(listaImagenesUpdate);

       if(con.actualizarMantenimientoProgramado(temp)){
           initShowData(idMantenimiento);
       }else{
           System.out.println("Algo salio mal");
       }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initComponents();
    }
}

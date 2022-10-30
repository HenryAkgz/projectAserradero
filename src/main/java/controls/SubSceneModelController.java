package controls;

import clases.*;
import conexión.Conexión;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SubSceneModelController implements Initializable {
    @FXML
    private TextField txtBusqueda;
    @FXML
    private VBox aaside_list_models;
    @FXML
    private BorderPane pn_part_content;
    @FXML
    private VBox pane_waiting_clic;
    @FXML
    private BorderPane pane_infoPieza;
    @FXML
    private Pane modelo_empty_data_container;
    @FXML
    private AnchorPane root;
    @FXML
    private VBox form_aside_container;
    @FXML
    private ImageView photoModel_imageView;
    @FXML
    private Label nombreModel_label;
    @FXML
    private HBox menu_item_model_container;
    @FXML
    private ImageView name_imv_showdetails;
    @FXML
    private Label name_label_showdetails;
    @FXML
    private Label unidadesactivas_label_showdetais;
    @FXML
    private Label peso_label_showdetails;
    @FXML
    private Label motor_label_showdetails;
    @FXML
    private Label diametroMaximoTronco_label_showdetails;
    @FXML
    private Label anchoMaximoTablero_label_showdetails;
    @FXML
    private Label grosorPlaca_label_showdetails;
    @FXML
    private Label tamanoHoja_label_showdetails;
    @FXML
    private Label longitudPista_label_showdetails;
    @FXML
    private Label anchoPista_label_showdetails;
    @FXML
    private Label ajustabilidadAltura_label_showdetails;
    @FXML
    private TextField peso_textField_update;
    @FXML
    private TextField motor_textField_update;
    @FXML
    private TextField diametroMaximoTronco_textField_update;
    @FXML
    private TextField anchoMaximoTablero_textField_update;
    @FXML
    private TextField grosorPlaca_textField_update;
    @FXML
    private TextField tamanoHoja_textField_update;
    @FXML
    private TextField longitudPista_textField_update;
    @FXML
    private TextField anchoPista_textField_update;
    @FXML
    private TextField ajustabilidadAltura_textField_update;
    @FXML
    private BorderPane pn_update;
    @FXML
    private VBox pn_showdetails;
    @FXML
    private ComboBox peso_combobox_update;
    @FXML
    private Button changePhoto_button_update;
    @FXML
    private ImageView fotoModelo_imv_update;
    @FXML
    private Label nameModel_label_update;
    @FXML
    private ScrollPane formGeneralDataModelUpdate;
    @FXML
    private ScrollPane formPiecesModelUpdate;
    @FXML
    private VBox formMensajeModelUpdate;
    @FXML
    private VBox piecesItemModelContainerUpdate;
    @FXML
    private VBox piecesGlobalContainerUpdate;
    @FXML
    private Button updateDataModelButtonUpdate;
    @FXML
    private ImageView imv_result_update;
    @FXML
    private Label label_result_update;
    @FXML
    private Label cantidadUnidades_label;


    //variables de la subScene
    Conexión con;
    ArrayList<Modelo> listAllModelsFromDB;
    Modelo currentModelo;
    Modelo currentModeloUpdate;

    ArrayList<Pieza> piezaCurrentModelo;
    ArrayList<Pieza> allGlobalPieces;


    //########## métodos de la interfaz ##########

    /*
     * Este método busca entre la lista de modelos, algun elemento que
     * coincida con la búsqueda
     * */
    public void handleSearch() {
        aaside_list_models.getChildren().clear();
        List<Modelo> listaFiltrada = listAllModelsFromDB.stream().filter(item -> item.getIdModelo().contains(txtBusqueda.getText())).toList();
        showModelsInUI(listaFiltrada);
    }

    //este método abre un nuevo formulario para añadir un nuevo modelo
    public void handleAddNewModel() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/layout/addNewModel.fxml"));

        Scene scene = new Scene(loader.load(), 600, 665);
        scene.getStylesheets().add(getClass().getResource("/css/addUnit.css").toExternalForm());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);

        AddNewModelController modelController = (AddNewModelController) loader.getController();
        modelController.setParentModel(this);

        stage.showAndWait();
    }


    //########## métodos del controlador ##########

    public void getAllModelsFromDB() {
        listAllModelsFromDB = con.getModelosFromDB();
        showModelsInUI(listAllModelsFromDB);
    }

    private void showModelsInUI(List<Modelo> lista) {

        if (lista != null && lista.size() > 0) {
            if (root.getChildren().contains(modelo_empty_data_container)) {
                modelo_empty_data_container.setVisible(false);
                root.getChildren().remove(modelo_empty_data_container);
                root.getChildren().remove(form_aside_container);
                root.getChildren().remove(pane_infoPieza);
                root.getChildren().remove(pn_update);
                root.getChildren().remove(formGeneralDataModelUpdate);
                root.getChildren().remove(formPiecesModelUpdate);
                root.getChildren().remove(formMensajeModelUpdate);
                pn_part_content.setVisible(true);
                loadOptionButtonItemModel();
            }

            lista.forEach(item -> {
                ItemModelo modeloUI = new ItemModelo(item.getIdModelo(), item.getMotor());
                modeloUI.setOnMouseClicked(mouseEvent -> itemModelUIAction(item));
                aaside_list_models.getChildren().add(modeloUI);
            });

        } else {
            modelo_empty_data_container.setVisible(true);
            pn_part_content.setVisible(false);
            root.getChildren().remove(pn_part_content);
            root.getChildren().remove(pane_infoPieza);
        }


    }

    private void itemModelUIAction(Modelo item) {
        if (pn_part_content.getChildren().contains(pane_waiting_clic)) {
            pn_part_content.getChildren().remove(pane_waiting_clic);
            pn_part_content.setCenter(pane_infoPieza);
        }

        photoModel_imageView.setImage(new Image(new ByteArrayInputStream(item.getFoto_modelo())));
        nombreModel_label.setText(item.getIdModelo());
        currentModelo = item;
        cantidadUnidades_label.setText(con.getCountUnitsFromModels(item.getIdModelo()) + " Unidades");
    }

    private void loadOptionButtonItemModel() {

        Button_with_icon modelButtonDelete = new Button_with_icon();
        modelButtonDelete.makeContainer("white", "10px", "10px 15px", 10.0);
        modelButtonDelete.makeContentButton("icon_delete_model", 15.0, "#161925", "Eliminar modelo");
        modelButtonDelete.createButton();


        Button_with_icon modelButtonUpdate = new Button_with_icon();
        modelButtonUpdate.makeContainer("white", "10px", "10px 15px", 10.0);
        modelButtonUpdate.makeContentButton("icon_update_details", 15.0, "#161925", "Actualizar modelo");
        modelButtonUpdate.createButton();
        modelButtonUpdate.setOnMouseClicked(mouseEvent -> showUpdate());

        Button_with_icon modelButtonShowDetails = new Button_with_icon();
        modelButtonShowDetails.makeContainer("white", "10px", "10px 15px", 10.0);
        modelButtonShowDetails.makeContentButton("icon_show_details", 15.0, "#161925", "Mostrar detalles");
        modelButtonShowDetails.createButton();
        modelButtonShowDetails.setOnMouseClicked(mouseEvent -> {
            showDetails();
        });

        menu_item_model_container.getChildren().addAll(modelButtonShowDetails, modelButtonUpdate, modelButtonDelete);

    }

    private void showDetails() {

        if (isVisibleFormContainer()) {

            if (form_aside_container.getChildren().contains(pn_showdetails)) {
                pn_part_content.getChildren().remove(form_aside_container);
            }

        } else {
            pn_part_content.setRight(form_aside_container);
        }

        form_aside_container.getChildren().clear();
        form_aside_container.getChildren().add(pn_showdetails);

        name_imv_showdetails.setImage(new Image(new ByteArrayInputStream(currentModelo.getFoto_modelo())));
        name_label_showdetails.setText(currentModelo.getIdModelo());
        unidadesactivas_label_showdetais.setText("12 unidades en total");

        peso_label_showdetails.setText(currentModelo.getPeso());
        motor_label_showdetails.setText(currentModelo.getMotor());
        diametroMaximoTronco_label_showdetails.setText(currentModelo.getMaxLogDiameter());
        anchoMaximoTablero_label_showdetails.setText(currentModelo.getMaxBoardWidth());
        grosorPlaca_label_showdetails.setText(currentModelo.getMaxBoardThickness());
        tamanoHoja_label_showdetails.setText(currentModelo.getBladeSize());
        longitudPista_label_showdetails.setText(currentModelo.getTrackLength());
        anchoPista_label_showdetails.setText(currentModelo.getTrackWidth());
        ajustabilidadAltura_label_showdetails.setText(currentModelo.getTrackHeightAdjustability());


    }

    private void showUpdate() {
        if (isVisibleFormContainer()) {

            if (form_aside_container.getChildren().contains(pn_update)) {
                pn_part_content.getChildren().remove(form_aside_container);
            }

        } else {
            pn_part_content.setRight(form_aside_container);
        }

        form_aside_container.getChildren().clear();
        form_aside_container.getChildren().add(pn_update);

        if (!pn_update.getChildren().contains(formGeneralDataModelUpdate)) {
            pn_update.getChildren().remove(formPiecesModelUpdate);
            pn_update.getChildren().remove(formMensajeModelUpdate);
        }

        pn_update.setCenter(formGeneralDataModelUpdate);
        updateDataModelButtonUpdate.setText("Siguiente");
        fotoModelo_imv_update.setImage(new Image(new ByteArrayInputStream(currentModelo.getFoto_modelo())));
        nameModel_label_update.setText(currentModelo.getIdModelo());
        showPrecioUpdate(currentModelo.getPeso());
        motor_textField_update.setText(currentModelo.getMotor());
        diametroMaximoTronco_textField_update.setText(currentModelo.getMaxLogDiameter());
        anchoMaximoTablero_textField_update.setText(currentModelo.getMaxBoardWidth());
        grosorPlaca_textField_update.setText(currentModelo.getMaxBoardThickness());
        tamanoHoja_textField_update.setText(currentModelo.getBladeSize());
        longitudPista_textField_update.setText(currentModelo.getTrackLength());
        anchoPista_textField_update.setText(currentModelo.getTrackWidth());
        ajustabilidadAltura_textField_update.setText(currentModelo.getTrackHeightAdjustability());


    }

    private boolean isVisibleFormContainer() {
        return pn_part_content.getChildren().contains(form_aside_container);
    }

    private void showPrecioUpdate(String peso) {
        String unidad = peso.substring(peso.length() - 3, peso.length());
        String pesoTemp = peso.substring(0, peso.length() - 3);
        peso_textField_update.setText(pesoTemp);
        peso_combobox_update.setValue(unidad.trim());
    }

    public void handleChangeModelPhoto() {
        byte[] byteTempArray = Util.readFile(Util.getPhotoFromStorage("Actualizar foto del modelo"));
        currentModelo.setFoto_modelo(byteTempArray);
        fotoModelo_imv_update.setImage(new Image(new ByteArrayInputStream(byteTempArray)));
    }

    public void handleNextUpdateButton() {
        if (pn_update.getChildren().contains(formGeneralDataModelUpdate)) {

            currentModeloUpdate = new Modelo();
            currentModeloUpdate.setIdModelo(currentModelo.getIdModelo());
            currentModeloUpdate.setPeso(peso_textField_update.getText().trim() + " " + peso_combobox_update.getSelectionModel().getSelectedItem().toString());
            currentModeloUpdate.setMotor(motor_textField_update.getText().trim());
            currentModeloUpdate.setMaxLogDiameter(diametroMaximoTronco_textField_update.getText().trim());
            currentModeloUpdate.setMaxBoardWidth(anchoMaximoTablero_textField_update.getText().trim());
            currentModeloUpdate.setMaxBoardThickness(grosorPlaca_textField_update.getText().trim());
            currentModeloUpdate.setBladeSize(tamanoHoja_textField_update.getText().trim());
            currentModeloUpdate.setTrackLength(longitudPista_textField_update.getText().trim());
            currentModeloUpdate.setTrackWidth(anchoPista_textField_update.getText().trim());
            currentModeloUpdate.setTrackHeightAdjustability(ajustabilidadAltura_textField_update.getText().trim());
            currentModeloUpdate.setFoto_modelo(currentModelo.getFoto_modelo());

            pn_update.getChildren().remove(formGeneralDataModelUpdate);
            pn_update.setCenter(formPiecesModelUpdate);
            showInUIModelsPieces(currentModelo.getIdModelo());
            updateDataModelButtonUpdate.setText("Actualizar datos");


        } else if (pn_update.getChildren().contains(formPiecesModelUpdate)) {

            updateDataModel();

        }else if(pn_update.getChildren().contains(formMensajeModelUpdate)){
            showUpdate();
        }
    }

    private void showInUIModelsPieces(String idModelo) {
        piecesItemModelContainerUpdate.getChildren().clear();
        piezaCurrentModelo = con.getAllPiecesByModel(idModelo);

        piezaCurrentModelo.forEach(item -> {
            ItemPiezaContador i = new ItemPiezaContador(item, true, false);
            actionItemModelPieces(i, item);
            piecesItemModelContainerUpdate.getChildren().add(i);
        });

        allGlobalPieces = con.getAllPiecesFromDB();
        piecesGlobalContainerUpdate.getChildren().clear();
        allGlobalPieces.forEach(item -> {
            actionItemGlobalPart(item);
        });

    }

    private void actionItemGlobalPart(Pieza item) {
        ItemPiezaContador itemPiezaContador = new ItemPiezaContador(item, false);

        itemPiezaContador.setOnMouseClicked(mouseEvent -> {

            if (!piezaCurrentModelo.stream().anyMatch(element -> item.getId_Pieza() == element.getId_Pieza())) {


                ItemPiezaContador modeloPart = new ItemPiezaContador(item, true);
                actionItemModelPieces(modeloPart, item);
                piecesItemModelContainerUpdate.getChildren().add(modeloPart);
                piezaCurrentModelo.add(item);


            }


        });
        piecesGlobalContainerUpdate.getChildren().add(itemPiezaContador);
    }

    private void actionItemModelPieces(ItemPiezaContador e, Pieza item) {
        e.setOnMouseClicked(mouseEvent1 -> {
            if (con.deletePartFromModel(currentModelo.getIdModelo(), item.getId_Pieza())) {
                piecesItemModelContainerUpdate.getChildren().remove(e);
                piezaCurrentModelo.removeIf(elemento -> elemento.getId_Pieza() == item.getId_Pieza());
            }
        });
    }


    private void updateDataModel() {
        if (con.updateModel(currentModeloUpdate)) {

            if(piezaCurrentModelo.size()>0){
                piezaCurrentModelo.forEach(item -> {

                    if (con.existPartInModel(currentModeloUpdate.getIdModelo(), item.getId_Pieza())) {
                        showUpdateResult(con.updatePartOfModel(currentModeloUpdate.getIdModelo(), item.getId_Pieza(), item.getCantidadEnInvetario()));
                    } else {
                        showUpdateResult(con.addPartToModel(currentModeloUpdate.getIdModelo(), item.getId_Pieza(), item.getCantidadEnInvetario()));
                    }

                });
            }else{
                showUpdateResult(true);
            }


        } else {
            showUpdateResult(false);
        }
    }

    private void showUpdateResult(Boolean result) {

        String textResult = "";
        pn_update.getChildren().remove(formPiecesModelUpdate);
        pn_update.setCenter(formMensajeModelUpdate);

        if (result) {
            textResult = "Modelo actualizado correctamente!";
            imv_result_update.setImage(new Image(getClass().getResourceAsStream("/icons/saveOk.gif")));
            label_result_update.setStyle("-fx-text-fill: green;");
        }else{
            textResult = "Ops! Algo salio mal...";
            imv_result_update.setImage(new Image(getClass().getResourceAsStream("/icons/saveError.gif")));
            label_result_update.setStyle("-fx-text-fill: red;");
        }

        label_result_update.setText(textResult);
        updateDataModelButtonUpdate.setText("Cerrar");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = new Conexión();
        con.connectWidthDB();
        getAllModelsFromDB();
        peso_combobox_update.getItems().addAll("lb", "kg", "ton");
    }
}

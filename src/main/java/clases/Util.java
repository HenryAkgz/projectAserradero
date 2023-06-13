package clases;

import controls.windowLayoutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class Util {

    // Interfaz funcional con un método abstracto
    public static interface MiFuncion {
        void ejecutar(FXMLLoader content);
    }

    public static void newStageWindow(FXMLLoader content, double width, double height, String style, boolean showAndWait, MiFuncion funcion) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(windowLayoutController.class.getResource(Constants.WINDOW_FRAME_PATH));
            Scene scene = new Scene(fxmlLoader.load(), width, height);
            scene.getStylesheets().add(style);
            windowLayoutController controller = fxmlLoader.getController();
            controller.setContent(content, funcion);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            if(showAndWait){
                stage.showAndWait();
            }else{
                stage.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setHover(AnchorPane pane, Label label, String paneNormalStyle, String paneHoverStyle, String labelNormalStyle, String labelHoverStyle) {
        pane.setOnMouseEntered(action -> {
            pane.setStyle(paneHoverStyle);
            label.setStyle(labelHoverStyle);
        });
        pane.setOnMouseExited(action -> {
            pane.setStyle(paneNormalStyle);
            label.setStyle(labelNormalStyle);
        });
    }
    public static String getPhotoFromStorage(String title){

        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        File archivo = chooser.showOpenDialog(null);

        if(archivo != null){
            return archivo.getAbsolutePath();
        }

        return null;
    }

    public static byte[] readFile(String file) {
        ByteArrayOutputStream bos = null;
        try {
            File f = new File(file);
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            bos = new ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1;) {
                bos.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e2) {
            System.err.println(e2.getMessage());
        }
        return bos != null ? bos.toByteArray() : null;
    }

    public static String getStyleByState(String estado) {
        String style;

        switch (estado) {
            case Constants.ESTADO_UNIDAD_ACTIVO:
            case Constants.ESTADO_MANTENIMIENTO_OK:
                style = "-fx-text-fill: #91C499;";
                break;

            case Constants.ESTADO_UNIDAD_INACTIVO:
                style = "-fx-text-fill: red;";
                break;

            case Constants.ESTADO_MANTENIMIENTO_PENDIENTE:
            case Constants.UNIDAD_EN_MANTENIMIENTO:
                style = "-fx-text-fill: #fec701;";
                break;

            case Constants.ESTADO_MANTENIMIENTO_STOP:
                style = "-fx-text-fill: #E56399;";
                break;

            default:
                style = "-fx-text-fill: white";
                break;
        }
        return style;
    }

    public static String getMonthOfDateString(String fecha){
        LocalDate tempDate = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
       return tempDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
    }
}

package clases;

import javafx.stage.FileChooser;

import java.io.*;

public class Util {

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
}

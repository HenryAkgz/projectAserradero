package clases;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.imageio.ImageIO;

public class Mantenimiento {
    private int id_mantenimiento_programado;
    private String idUnit;
    private String maintenanceDate;
    private String maintenanceType;
    private String maintenanceNotes;
    private ArrayList<String> maintenanceImagesPaths = new ArrayList<>();

    private String JSONImagenes;

    public Mantenimiento() {
    }

    public int getId_mantenimiento_programado() {
        return id_mantenimiento_programado;
    }

    public void setId_mantenimiento_programado(int id_mantenimiento_programado) {
        this.id_mantenimiento_programado = id_mantenimiento_programado;
    }

    public String getIdUnit() {
        return idUnit;
    }

    public void setIdUnit(String idUnit) {
        this.idUnit = idUnit;
    }

    public String getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(String maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public String getMaintenanceNotes() {
        return maintenanceNotes;
    }

    public void setMaintenanceNotes(String maintenanceNotes) {
        this.maintenanceNotes = maintenanceNotes;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public List<String> getMaintenanceImagesPaths() {
        return maintenanceImagesPaths;
    }

    public void setMaintenanceImagesPaths(ArrayList<String> maintenanceImagesPaths) {
        this.maintenanceImagesPaths = maintenanceImagesPaths;
    }

    public String getJSONArrayImagenes() {
        if (this.maintenanceImagesPaths.size() > 0 && this.JSONImagenes == null) {
            this.JSONImagenes = generarJSONImagenes();
            System.out.println(this.JSONImagenes);
        }

        return this.JSONImagenes;
    }

    public List<Image> getDecodeImages() {

        List<Image> imagenes = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(this.JSONImagenes);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has("image_base64")) {
                    String imageBase64 = jsonObject.getString("image_base64");
                    byte[] imageBytes = Base64.getDecoder().decode(imageBase64);
                    Image imagen = new Image(new ByteArrayInputStream(imageBytes));
                    imagenes.add(imagen);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al decodificar el JSON de imágenes: " + e.getMessage());
        }

        return imagenes;

    }

    public void encodeImagesToJson(List<Image> listaImages){
        JSONArray jsonArray = new JSONArray();

        try {
            for (Image imagen : listaImages) {
                JSONObject jsonObject = new JSONObject();

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                ImageIO.write(SwingFXUtils.fromFXImage(imagen, null), "png", outputStream);

                byte[] imageBytes = outputStream.toByteArray();
                String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);

                jsonObject.put("image_base64", imageBase64);
                jsonArray.put(jsonObject);
            }
        } catch (Exception e) {
            System.out.println("Error al generar el JSON de las imágenes: " + e.getMessage());
        }

        this.JSONImagenes = jsonArray.toString();
    }

    public String generarJSONImagenes() {
        JSONArray jsonArray = new JSONArray();
        for (String imagePath : this.maintenanceImagesPaths) {
            JSONObject jsonObject = new JSONObject();
            try {
                byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
                String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
                jsonObject.put("image_base64", imageBase64);
                jsonArray.put(jsonObject);
            } catch (IOException | JSONException e) {
                System.out.println("Error al generar el JSON de la imagen: " + e.getMessage());
            }
        }
        return jsonArray.toString();
    }

    public void setJSONImagenes(String jsonImagenes) {
        this.JSONImagenes = jsonImagenes;
    }
}

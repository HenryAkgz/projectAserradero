package clases;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Mantenimiento {

    private String idUnit;
    private String maintenanceDate;
    private String maintenanceType;
    private String maintenanceNotes;
    private ArrayList<String> maintenanceImagesPaths = new ArrayList<>();

    private String JSONImagenes;

    public Mantenimiento() {
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

    public String getJSONArrayImagenes(){
        if(this.maintenanceImagesPaths.size()>0 && this.JSONImagenes == null){
            this.JSONImagenes = generarJSONImagenes();
            return JSONImagenes;
        }else{
            return "";
        }
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
}

package clases;

import java.util.ArrayList;
import java.util.List;

public class Mantenimiento {

    private String idUnit;
    private String maintenanceDate;
    private String maintenanceType;
    private String maintenanceNotes;
    private ArrayList<String> maintenanceImagesPaths = new ArrayList<>();

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
}

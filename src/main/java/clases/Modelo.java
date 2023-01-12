package clases;


public class Modelo {
    private String idModelo;
    private String peso;
    private String motor;
    private String maxLogDiameter;
    private String maxBoardWidth;
    private String maxBoardThickness;
    private String bladeSize;
    private String trackLength;
    private String trackWidth;
    private String trackHeightAdjustability;

    private byte[] foto_modelo;

    public Modelo() {
    }

    public String getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(String idModelo) {
        this.idModelo = idModelo;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getMaxLogDiameter() {
        return maxLogDiameter;
    }

    public void setMaxLogDiameter(String maxLogDiameter) {
        this.maxLogDiameter = maxLogDiameter;
    }

    public String getMaxBoardWidth() {
        return maxBoardWidth;
    }

    public void setMaxBoardWidth(String maxBoardWidth) {
        this.maxBoardWidth = maxBoardWidth;
    }

    public String getMaxBoardThickness() {
        return maxBoardThickness;
    }

    public void setMaxBoardThickness(String maxBoardThickness) {
        this.maxBoardThickness = maxBoardThickness;
    }

    public String getBladeSize() {
        return bladeSize;
    }

    public void setBladeSize(String bladeSize) {
        this.bladeSize = bladeSize;
    }

    public String getTrackLength() {
        return trackLength;
    }

    public void setTrackLength(String trackLength) {
        this.trackLength = trackLength;
    }

    public String getTrackWidth() {
        return trackWidth;
    }

    public void setTrackWidth(String trackWidth) {
        this.trackWidth = trackWidth;
    }

    public String getTrackHeightAdjustability() {
        return trackHeightAdjustability;
    }

    public void setTrackHeightAdjustability(String trackHeightAdjustability) {
        this.trackHeightAdjustability = trackHeightAdjustability;
    }

    public byte[] getFoto_modelo() {
        return foto_modelo;
    }

    public void setFoto_modelo(byte[] foto_modelo) {
        this.foto_modelo = foto_modelo;
    }
}

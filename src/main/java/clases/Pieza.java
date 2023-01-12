package clases;

public class Pieza {
    int id_Pieza;
    String nombrePieza;
    byte[] photoPieza;
    int cantidadEnInvetario;

    public Pieza() {
    }

    public Pieza (int id_Pieza){
        this.id_Pieza = id_Pieza;
    }

    public int getId_Pieza() {
        return id_Pieza;
    }

    public void setId_Pieza(int id_Pieza) {
        this.id_Pieza = id_Pieza;
    }

    public String getNombrePieza() {
        return nombrePieza;
    }

    public void setNombrePieza(String nombrePieza) {
        this.nombrePieza = nombrePieza;
    }

    public byte[] getPhotoPieza() {
        return photoPieza;
    }

    public void setPhotoPieza(byte[] photoPieza) {
        this.photoPieza = photoPieza;
    }

    public int getCantidadEnInvetario() {
        return cantidadEnInvetario;
    }

    public void setCantidadEnInvetario(int cantidadEnInvetario) {
        this.cantidadEnInvetario = cantidadEnInvetario;
    }
}

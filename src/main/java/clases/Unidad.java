package clases;

public class Unidad {
    private String id_unidad;
    private String id_modelo;
    private String estado;
    private String notas_de_la_unidad;
    private byte[] photoUnidad;

    public Unidad() {
    }

    public String getId_unidad() {
        return id_unidad;
    }

    public void setId_unidad(String id_unidad) {
        this.id_unidad = id_unidad;
    }

    public String getId_modelo() {
        return id_modelo;
    }

    public void setId_modelo(String id_modelo) {
        this.id_modelo = id_modelo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNotas_de_la_unidad() {
        return notas_de_la_unidad;
    }

    public void setNotas_de_la_unidad(String notas_de_la_unidad) {
        this.notas_de_la_unidad = notas_de_la_unidad;
    }

    public byte[] getPhotoUnidad() {
        return photoUnidad;
    }

    public void setPhotoUnidad(byte[] photoUnidad) {
        this.photoUnidad = photoUnidad;
    }
}

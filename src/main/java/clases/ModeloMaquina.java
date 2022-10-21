package clases;

public class ModeloMaquina {
    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ModeloMaquina(String modelo, String nombre) {
        this.modelo = modelo;
        this.nombre = nombre;
    }

    String modelo;
    String nombre;
}

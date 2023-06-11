package clases;


public class Modelo {
  private String id_modelo;
  private String marca;
  private String peso;
  private String tipo_combustible;
  private String motor;
  private String altura;
  private String ancho;
  private String profundidad;
  private String capacidad_pasajeros;
  private String no_ruedas;
  private byte[] foto_modelo;

    public Modelo() {
    }

    public String getId_modelo() {
        return id_modelo;
    }

    public void setId_modelo(String id_modelo) {
        this.id_modelo = id_modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getTipo_combustible() {
        return tipo_combustible;
    }

    public void setTipo_combustible(String tipo_combustible) {
        this.tipo_combustible = tipo_combustible;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getAncho() {
        return ancho;
    }

    public void setAncho(String ancho) {
        this.ancho = ancho;
    }

    public String getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(String profundidad) {
        this.profundidad = profundidad;
    }

    public String getCapacidad_pasajeros() {
        return capacidad_pasajeros;
    }

    public void setCapacidad_pasajeros(String capacidad_pasajeros) {
        this.capacidad_pasajeros = capacidad_pasajeros;
    }

    public String getNo_ruedas() {
        return no_ruedas;
    }

    public void setNo_ruedas(String no_ruedas) {
        this.no_ruedas = no_ruedas;
    }

    public byte[] getFoto_modelo() {
        return foto_modelo;
    }

    public void setFoto_modelo(byte[] foto_modelo) {
        this.foto_modelo = foto_modelo;
    }
}

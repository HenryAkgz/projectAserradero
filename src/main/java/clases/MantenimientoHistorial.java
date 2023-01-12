package clases;

public class MantenimientoHistorial {
    private String idUnidad;
    private String fecha_programada;
    private String fecha_inicio_mantenimiento;
    private String encargado;
    private String estado;
    private String notas_mantenimiento_programado;
    private String notas_mantenimiento_encargado;
    private String fecha_finalizacion_mantenimiento;

    public String getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(String idUnidad) {
        this.idUnidad = idUnidad;
    }

    public String getFecha_programada() {
        return fecha_programada;
    }

    public void setFecha_programada(String fecha_programada) {
        this.fecha_programada = fecha_programada;
    }

    public String getFecha_inicio_mantenimiento() {
        return fecha_inicio_mantenimiento;
    }

    public void setFecha_inicio_mantenimiento(String fecha_inicio_mantenimiento) {
        this.fecha_inicio_mantenimiento = fecha_inicio_mantenimiento;
    }

    public String getEncargado() {
        return encargado;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNotas_mantenimiento_programado() {
        return notas_mantenimiento_programado;
    }

    public void setNotas_mantenimiento_programado(String notas_mantenimiento_programado) {
        this.notas_mantenimiento_programado = notas_mantenimiento_programado;
    }

    public String getNotas_mantenimiento_encargado() {
        return notas_mantenimiento_encargado;
    }

    public void setNotas_mantenimiento_encargado(String notas_mantenimiento_encargado) {
        this.notas_mantenimiento_encargado = notas_mantenimiento_encargado;
    }

    public String getFecha_finalizacion_mantenimiento() {
        return fecha_finalizacion_mantenimiento;
    }

    public void setFecha_finalizacion_mantenimiento(String fecha_finalizacion_mantenimiento) {
        this.fecha_finalizacion_mantenimiento = fecha_finalizacion_mantenimiento;
    }
}

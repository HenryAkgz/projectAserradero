package conexión;

import clases.Constants;
import clases.Mantenimiento;
import clases.Unidad;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Conexión {
    private static Conexión instancia;
    private static Connection conexion;

    private static final String NOMBRE_BASE_DATOS = "datos_de_la_aplicación.db";

    private Conexión() {
        crearConexion();
    }

    public static Conexión getInstancia() {
        if (instancia == null) {
            instancia = new Conexión();
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }

    private void crearConexion() {
        try {
            // Verificar si el archivo de la base de datos ya existe
            File archivoBD = new File(NOMBRE_BASE_DATOS);
            boolean existeBaseDatos = archivoBD.exists();

            if (!existeBaseDatos) {
                // Crear el archivo de la base de datos vacío
                archivoBD.createNewFile();
                System.out.println("Se creo el archivo " + NOMBRE_BASE_DATOS + " en " + archivoBD.getAbsolutePath());
            }

            // Establecer la conexión a la base de datos
            conexion = DriverManager.getConnection("jdbc:sqlite:" + NOMBRE_BASE_DATOS);
            System.out.println("Conexión establecida con éxito a la base de datos.");

            if (!existeBaseDatos) {
                Statement statement = conexion.createStatement();
                statement.executeUpdate("BEGIN TRANSACTION;" +
                        "CREATE TABLE IF NOT EXISTS unidades (" +
                        "id_unit varchar," +
                        "id_modelo varchar," +
                        "estado varchar," +
                        "notas_unidad varchar," +
                        "foto_unidad blob);" +
                        "CREATE TABLE IF NOT EXISTS mantenimiento_historial (" +
                        "id_mantenimiento_historial varchar," +
                        "id_unit varchar," +
                        "fecha_mantenimiento varchar," +
                        "encargado_mantenimiento varchar," +
                        "estado_mantenimiento varchar," +
                        "notas_mantenimiento varchar," +
                        "tipo_mantenimiento varchar," +
                        "fotos_evidencia varchar);" +
                        "CREATE TABLE IF NOT EXISTS mantenimiento_programado (" +
                        "id_mantenimiento_programado varchar," +
                        "id_unit varchar," +
                        "fecha_mantenimiento datetime," +
                        "nota_mantenimiento varchar," +
                        "tipo_mantenimiento varchar," +
                        "fotos_evidencia varchar);" +
                        "CREATE TABLE IF NOT EXISTS piezas (" +
                        "id_pieza integer," +
                        "nombre_pieza varchar," +
                        "foto_pieza blob," +
                        "piezas_inventario integer," +
                        "PRIMARY KEY(id_pieza AUTOINCREMENT));" +
                        "CREATE TABLE IF NOT EXISTS piezas_modelo (" +
                        "id_modelo varchar," +
                        "id_pieza integer," +
                        "cantidad integer);" +
                        "CREATE TABLE IF NOT EXISTS modelo (" +
                        "id_modelo varchar," +
                        "marca varchar," +
                        "peso varchar," +
                        "tipo_combustible varchar," +
                        "motor varchar," +
                        "altura varchar," +
                        "ancho varchar," +
                        "profundidad varchar," +
                        "capacidad_pasajeros varchar," +
                        "no_ruedos varchar," +
                        "foto_modelo blob);" +
                        "CREATE TRIGGER on_delete_pieza AFTER DELETE ON piezas BEGIN " +
                        "DELETE FROM piezas_modelo WHERE id_pieza = old.id_pieza; " +
                        "END;" +
                        "CREATE TRIGGER on_delete_modelo AFTER DELETE ON modelo BEGIN " +
                        "DELETE FROM piezas_modelo WHERE id_modelo = old.id_modelo; " +
                        "DELETE FROM unidades WHERE id_modelo = old.id_modelo; " +
                        "END;" +
                        "CREATE TRIGGER on_delete_unidad AFTER DELETE ON unidades BEGIN " +
                        "DELETE FROM mantenimiento_historial WHERE id_unit = old.id_unit; " +
                        "DELETE FROM mantenimiento_programado WHERE id_unit = old.id_unit; " +
                        "END;" +
                        "COMMIT;");
                System.out.println("Tablas creadas con éxito.");
            }
        } catch (Exception e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    /* #############################################  Métodos de piezas  ############################################# */


    /* #############################################  Métodos de Modelo  ############################################# */


    /* #############################################  Métodos de Unidades  ############################################# */
    public boolean existeUnidad(String idUnidad) {
        boolean existe = false;
        String consulta = "SELECT COUNT(*) FROM unidades WHERE id_unit = ?";
        try {
            PreparedStatement statement = conexion.prepareStatement(consulta);
            statement.setString(1, idUnidad);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                existe = count > 0;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error al verificar la existencia de la unidad: " + e.getMessage());
        }
        return existe;
    }

    public boolean agregarUnidad(Unidad u) {
        String consulta = "INSERT INTO unidades (id_unit, id_modelo, estado, notas_unidad, foto_unidad) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = conexion.prepareStatement(consulta);
            statement.setString(1, u.getId_unidad());
            statement.setString(2, u.getId_modelo());
            statement.setString(3, u.getEstado());
            statement.setString(4, u.getNotas_de_la_unidad());
            statement.setBytes(5, u.getPhotoUnidad());
            statement.executeUpdate();
            statement.close();
            System.out.println("Unidad agregada con éxito.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al agregar la unidad: " + e.getMessage());
        }
        return false;
    }

    public ArrayList<Unidad> obtenerTodasLasUnidades() {
        ArrayList<Unidad> unidades = new ArrayList<>();

        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM unidades");
            while (rs.next()) {
                Unidad unidad = new Unidad();
                unidad.setId_unidad(rs.getString("id_unit"));
                unidad.setId_modelo(rs.getString("id_modelo"));
                unidad.setEstado(rs.getString("estado"));
                unidad.setNotas_de_la_unidad(rs.getString("notas_unidad"));
                unidad.setPhotoUnidad(rs.getBytes("foto_unidad"));
                unidades.add(unidad);
            }
            stmt.close();
            rs.close();
            return unidades;
        } catch (SQLException e) {
            System.out.println("Error al obtener las unidades de la base de datos: " + e.getMessage());
        }
        return unidades;
    }

    /* #############################################  Métodos de Mantenimiento ########################################## */
    public boolean agregarMantenimientoProgramado(Mantenimiento m) {
        String consulta = "INSERT INTO mantenimiento_programado (id_unit, fecha_mantenimiento, nota_mantenimiento, tipo_mantenimiento, fotos_evidencia) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = conexion.prepareStatement(consulta);
            statement.setString(1, m.getIdUnit());
            statement.setDate(2,  Date.valueOf(m.getMaintenanceDate()));
            statement.setString(3, m.getMaintenanceNotes());
            statement.setString(4, m.getMaintenanceType());
            statement.setString(5, m.getJSONArrayImagenes());
            statement.executeUpdate();
            statement.close();
            System.out.println("Mantenimiento programado añadido con éxito.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al añadir el mantenimiento programado: " + e.getMessage());
        }
        return false;
    }

    public LocalDate obtenerFechaProximoMantenimiento(String idUnit) {
        LocalDate fechaProximoMantenimiento = null;

        try {
            PreparedStatement stmt = conexion.prepareStatement("SELECT fecha_mantenimiento FROM mantenimiento_programado WHERE id_unit = ? ORDER BY fecha_mantenimiento ASC LIMIT 1");
            stmt.setString(1, idUnit);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Date fechaMantenimientoStr = rs.getDate("fecha_mantenimiento");
                fechaProximoMantenimiento = LocalDate.parse(fechaMantenimientoStr.toString());
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener la fecha del próximo mantenimiento programado: " + e.getMessage());
        }

        return fechaProximoMantenimiento;
    };
    public LocalDate obtenerFechaDelUltimoMantenimientoRealizado(String idUnit) {
        LocalDate fechaMantenimientoReciente = null;

        try {
            PreparedStatement stmt = conexion.prepareStatement("SELECT fecha_mantenimiento FROM mantenimiento_historial WHERE id_unit = ? AND estado_mantenimiento = '"+ Constants.ESTADO_MANTENIMIENTO_OK +"' ORDER BY fecha_mantenimiento DESC LIMIT 1");

            stmt.setString(1, idUnit);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String fechaMantenimientoStr = rs.getString("fecha_mantenimiento");
                fechaMantenimientoReciente = LocalDate.parse(fechaMantenimientoStr);
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener la fecha del mantenimiento más reciente: " + e.getMessage());
        }

        return fechaMantenimientoReciente;
    }

}

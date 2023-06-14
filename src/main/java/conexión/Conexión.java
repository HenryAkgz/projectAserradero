package conexión;

import clases.*;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
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
                        "id_mantenimiento_historial integer," +
                        "id_unit varchar," +
                        "fecha_mantenimiento_programada datetime," +
                        "fecha_mantenimiento_iniciado datetime," +
                        "tipo_mantenimiento varchar," +
                        "encargado_mantenimiento varchar," +
                        "estado_mantenimiento varchar," +
                        "notas_mantenimiento_programado varchar," +
                        "notas_mantenimiento_encargado varchar," +
                        "fecha_finalización_mantenimiento datetime,"+
                        "fotos_evidencia_programada varchar," +
                        "PRIMARY KEY(id_mantenimiento_historial AUTOINCREMENT));" +
                        "CREATE TABLE IF NOT EXISTS mantenimiento_programado (" +
                        "id_mantenimiento_programado integer," +
                        "id_unit varchar," +
                        "fecha_mantenimiento datetime," +
                        "nota_mantenimiento varchar," +
                        "tipo_mantenimiento varchar," +
                        "fotos_evidencia varchar," +
                        "PRIMARY KEY(id_mantenimiento_programado AUTOINCREMENT));" +
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

    public boolean agregarPieza(Pieza p) {
        boolean agregada = false;

        try {
             PreparedStatement stmt = conexion.prepareStatement("INSERT INTO piezas (nombre_pieza, piezas_inventario, foto_pieza) VALUES (?, ?, ?)");

            stmt.setString(1, p.getNombrePieza());
            stmt.setInt(2, p.getCantidadEnInvetario());
            stmt.setBytes(3, p.getPhotoPieza());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                agregada = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar la pieza: " + e.getMessage());
        }

        return agregada;
    }

    public ArrayList<Pieza> obtenerTodasLasPiezas() {
        ArrayList<Pieza> piezas = new ArrayList<>();

        try {
             PreparedStatement stmt = conexion.prepareStatement("SELECT * FROM piezas");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pieza pieza = new Pieza();
                pieza.setId_Pieza(rs.getInt("id_pieza"));
                pieza.setNombrePieza(rs.getString("nombre_pieza"));
                pieza.setPhotoPieza(rs.getBytes("foto_pieza"));
                pieza.setCantidadEnInvetario(rs.getInt("piezas_inventario"));
                piezas.add(pieza);
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener las piezas de la base de datos: " + e.getMessage());
        }

        return piezas;
    }

    public void agregarPiezasModelo(List<Pieza> piezas, String idModelo) {
        try {

            String sql = "INSERT INTO piezas_modelo (id_modelo, id_pieza, cantidad) VALUES (?, ?, ?)";

            for (Pieza pieza : piezas) {
                PreparedStatement stmt = conexion.prepareStatement(sql);
                    stmt.setString(1, idModelo);
                    stmt.setInt(2, pieza.getId_Pieza());
                    stmt.setInt(3, pieza.getCantidadEnInvetario());
                    stmt.executeUpdate();
                stmt.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar las piezas al modelo en la base de datos: " + e.getMessage());
        }
    }

    public ArrayList<Pieza> obtenerPiezasPorModelo(String idModelo) {
        ArrayList<Pieza> piezas = new ArrayList<>();

        try {
             PreparedStatement stmt = conexion.prepareStatement("SELECT p.id_pieza, p.nombre_pieza, p.foto_pieza, pm.cantidad FROM piezas p INNER JOIN piezas_modelo pm ON p.id_pieza = pm.id_pieza WHERE pm.id_modelo = ?");

            stmt.setString(1, idModelo);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pieza pieza = new Pieza();
                pieza.setId_Pieza(rs.getInt("id_pieza"));
                pieza.setNombrePieza(rs.getString("nombre_pieza"));
                pieza.setPhotoPieza(rs.getBytes("foto_pieza"));
                pieza.setCantidadEnInvetario(rs.getInt("cantidad"));
                piezas.add(pieza);
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener las piezas por modelo: " + e.getMessage());
        }

        return piezas;
    }

    public boolean existeRelacionModeloPieza(String idModelo, int idPieza) {
        boolean existeRelacion = false;

        try {
             PreparedStatement stmt = conexion.prepareStatement("SELECT 1 FROM piezas_modelo WHERE id_modelo = ? AND id_pieza = ?");

            stmt.setString(1, idModelo);
            stmt.setInt(2, idPieza);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                existeRelacion = true;
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println("Error al verificar la existencia de la relación en la tabla piezas_modelo: " + e.getMessage());
        }

        return existeRelacion;
    }

    public boolean actualizarPieza(Pieza p) {
        boolean actualizada = false;

        try {
             PreparedStatement stmt = conexion.prepareStatement("UPDATE piezas SET nombre_pieza = ?, piezas_inventario = ? WHERE id_pieza = ?");

            stmt.setString(1, p.getNombrePieza());
            stmt.setInt(2, p.getCantidadEnInvetario());
            stmt.setInt(3, p.getId_Pieza());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                actualizada = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar la pieza: " + e.getMessage());
        }

        return actualizada;
    }

    public boolean eliminarPieza(int idPieza) {
        boolean eliminada = false;

        try {
             PreparedStatement stmt = conexion.prepareStatement("DELETE FROM piezas WHERE id_pieza = ?");

            stmt.setInt(1, idPieza);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                eliminada = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar la pieza: " + e.getMessage());
        }

        return eliminada;
    }

    public boolean eliminarRelacionModeloPieza(String idModelo, int idPieza) {
        boolean eliminado = false;
        try {
             PreparedStatement stmt = conexion.prepareStatement("DELETE FROM piezas_modelo WHERE id_modelo = ? AND id_pieza = ?");

            stmt.setString(1, idModelo);
            stmt.setInt(2, idPieza);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                eliminado = true;
            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error al eliminar la relación en la tabla piezas_modelo: " + e.getMessage());
        }
        return eliminado;
    }

    public boolean agregarRelacionModeloPieza(String idModelo, int idPieza, int cantidad) {
        boolean agregado = false;

        try {
             PreparedStatement stmt = conexion.prepareStatement("INSERT INTO piezas_modelo (id_modelo, id_pieza, cantidad) VALUES (?, ?, ?)");

            stmt.setString(1, idModelo);
            stmt.setInt(2, idPieza);
            stmt.setInt(3, cantidad);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                agregado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar la relación en la tabla piezas_modelo: " + e.getMessage());
        }

        return agregado;
    }

    public boolean actualizarCantidadRelacionPiezasModelo(String idModelo, int idPieza, int nuevaCantidad) {
        boolean actualizado = false;

        try {
             PreparedStatement stmt = conexion.prepareStatement("UPDATE piezas_modelo SET cantidad = ? WHERE id_modelo = ? AND id_pieza = ?");

            stmt.setInt(1, nuevaCantidad);
            stmt.setString(2, idModelo);
            stmt.setInt(3, idPieza);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                actualizado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar la cantidad en la tabla piezas_modelo: " + e.getMessage());
        }

        return actualizado;
    }

        /* #############################################  Métodos de Modelo  ############################################# */

    public ArrayList<Modelo> obtenerTodosLosModelos() {
        ArrayList<Modelo> modelos = new ArrayList<>();

        try {
             PreparedStatement stmt = conexion.prepareStatement("SELECT * FROM modelo");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Modelo m = new Modelo();
                m.setId_modelo(rs.getString("id_modelo"));
                m.setMarca(rs.getString("marca"));
                m.setPeso(rs.getString("peso"));
                m.setTipo_combustible(rs.getString("tipo_combustible"));
                m.setMotor(rs.getString("motor"));
                m.setAltura(rs.getString("altura"));
                m.setAncho(rs.getString("ancho"));
                m.setProfundidad(rs.getString("profundidad"));
                m.setCapacidad_pasajeros(rs.getString("capacidad_pasajeros"));
                m.setNo_ruedas(rs.getString("no_ruedos"));
                m.setFoto_modelo(rs.getBytes("foto_modelo"));
                modelos.add(m);
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener los modelos de la base de datos: " + e.getMessage());
        }

        return modelos;
    }

    public boolean verificarExistenciaModelo(String idModelo) {
        try {
             PreparedStatement stmt = conexion.prepareStatement("SELECT COUNT(*) FROM modelo WHERE id_modelo = ?");

            stmt.setString(1, idModelo);
            ResultSet rs = stmt.executeQuery();

            int count = rs.getInt(1);
            rs.close();

            return count > 0;
        } catch (SQLException e) {
            System.out.println("Error al verificar la existencia del modelo en la base de datos: " + e.getMessage());
            return false;
        }
    }

    public boolean agregarModelo(Modelo modelo) {
        try {
             PreparedStatement stmt = conexion.prepareStatement("INSERT INTO modelo (id_modelo, marca, peso, tipo_combustible, motor, altura, ancho, profundidad, capacidad_pasajeros, no_ruedos, foto_modelo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            stmt.setString(1, modelo.getId_modelo());
            stmt.setString(2, modelo.getMarca());
            stmt.setString(3, modelo.getPeso());
            stmt.setString(4, modelo.getTipo_combustible());
            stmt.setString(5, modelo.getMotor());
            stmt.setString(6, modelo.getAltura());
            stmt.setString(7, modelo.getAncho());
            stmt.setString(8, modelo.getProfundidad());
            stmt.setString(9, modelo.getCapacidad_pasajeros());
            stmt.setString(10, modelo.getNo_ruedas());
            stmt.setBytes(11, modelo.getFoto_modelo());

            stmt.executeUpdate();
            stmt.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al agregar el modelo a la base de datos: " + e.getMessage());
            return false;
        }
    }

    public int obtenerTotalUnidadesPorModelo(String idModelo) {
        int totalUnidades = 0;

        try {
             PreparedStatement stmt = conexion.prepareStatement("SELECT COUNT(*) AS total FROM unidades WHERE id_modelo = ?");

            stmt.setString(1, idModelo);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                totalUnidades = rs.getInt("total");
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener el total de unidades por modelo: " + e.getMessage());
        }

        return totalUnidades;
    }

    public boolean actualizarModelo(Modelo m) {
        boolean actualizado = false;

        try {
             PreparedStatement stmt = conexion.prepareStatement("UPDATE modelo SET marca = ?, peso = ?, tipo_combustible = ?, motor = ?, altura = ?, ancho = ?, profundidad = ?, capacidad_pasajeros = ?, no_ruedos = ?, foto_modelo = ? WHERE id_modelo = ?");

            stmt.setString(1, m.getMarca());
            stmt.setString(2, m.getPeso());
            stmt.setString(3, m.getTipo_combustible());
            stmt.setString(4, m.getMotor());
            stmt.setString(5, m.getAltura());
            stmt.setString(6, m.getAncho());
            stmt.setString(7, m.getProfundidad());
            stmt.setString(8, m.getCapacidad_pasajeros());
            stmt.setString(9, m.getNo_ruedas());
            stmt.setBytes(10, m.getFoto_modelo());
            stmt.setString(11, m.getId_modelo());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                actualizado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar los datos del modelo: " + e.getMessage());
        }

        return actualizado;
    }

    public boolean eliminarModelo(String idModelo) {
        boolean eliminado = false;

        try {
             PreparedStatement stmt = conexion.prepareStatement("DELETE FROM modelo WHERE id_modelo = ?");

            stmt.setString(1, idModelo);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                eliminado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el modelo: " + e.getMessage());
        }

        return eliminado;
    }

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

    public Unidad obtenerUnidadPorId(String idUnit) {
        Unidad unidad = null;

        try {
             PreparedStatement stmt = conexion.prepareStatement("SELECT * FROM unidades WHERE id_unit = ?");

            stmt.setString(1, idUnit);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    unidad = new Unidad();
                    unidad.setId_unidad(idUnit);
                    unidad.setId_modelo(rs.getString("id_modelo"));
                    unidad.setEstado(rs.getString("estado"));
                    unidad.setNotas_de_la_unidad(rs.getString("notas_unidad"));
                    unidad.setPhotoUnidad(rs.getBytes("foto_unidad"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener los datos de la unidad desde la base de datos: " + e.getMessage());
        }

        return unidad;
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

    public boolean eliminarUnidad(String idUnit) {
        try {
             PreparedStatement stmt = conexion.prepareStatement("DELETE FROM unidades WHERE id_unit = ?");

            stmt.setString(1, idUnit);
            stmt.executeUpdate();

            System.out.println("Unidad eliminada de la base de datos.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar la unidad de la base de datos: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarUnidad(Unidad u) {
        try {
             PreparedStatement stmt = conexion.prepareStatement("UPDATE unidades SET id_modelo = ?, estado = ?, notas_unidad = ?, foto_unidad = ? WHERE id_unit = ?");

            stmt.setString(1, u.getId_modelo());
            stmt.setString(2, u.getEstado());
            stmt.setString(3, u.getNotas_de_la_unidad());
            stmt.setBytes(4,  u.getPhotoUnidad());
            stmt.setString(5, u.getId_unidad());

            stmt.executeUpdate();

            System.out.println("Datos de la unidad actualizados en la base de datos.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar los datos de la unidad en la base de datos: " + e.getMessage());
        }
        return false;
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
            PreparedStatement stmt = conexion.prepareStatement("SELECT fecha_finalización_mantenimiento FROM mantenimiento_historial WHERE id_unit = ? AND estado_mantenimiento = '"+ Constants.ESTADO_MANTENIMIENTO_OK +"' ORDER BY fecha_finalización_mantenimiento DESC LIMIT 1");

            stmt.setString(1, idUnit);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String fechaMantenimientoStr = rs.getDate("fecha_finalización_mantenimiento").toString();
                fechaMantenimientoReciente = LocalDate.parse(fechaMantenimientoStr);
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener la fecha del mantenimiento más reciente: " + e.getMessage());
        }

        return fechaMantenimientoReciente;
    }

    public ArrayList<Mantenimiento> obtenerFechasMantenimientoProgramadoDeLaUnidad(String idUnidad) {
        ArrayList<Mantenimiento> mantenimientos  = new ArrayList<>();

        try {
            PreparedStatement stmt = conexion.prepareStatement("SELECT * FROM mantenimiento_programado WHERE id_unit = ?");

                stmt.setString(1, idUnidad);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {

                    Mantenimiento mantenimiento = new Mantenimiento();
                    mantenimiento.setId_mantenimiento_programado(rs.getInt("id_mantenimiento_programado"));
                    mantenimiento.setIdUnit(rs.getString("id_unit"));
                    mantenimiento.setMaintenanceDate(rs.getDate("fecha_mantenimiento").toString());
                    mantenimiento.setMaintenanceType(rs.getString("tipo_mantenimiento"));
                    mantenimiento.setMaintenanceNotes(rs.getString("nota_mantenimiento"));
                    mantenimiento.setJSONImagenes(rs.getString("fotos_evidencia"));



                    mantenimientos.add(mantenimiento);
                }

            } catch (SQLException e) {
            System.out.println("Error al obtener las fechas de mantenimiento programadas: " + e.getMessage());
        }

        return mantenimientos;
    }

    public List<MantenimientoHistorial> obtenerMantenimientoHistorialPorUnidad(String idUnit) {
        List<MantenimientoHistorial> mantenimientos = new ArrayList<>();

        try {
             PreparedStatement stmt = conexion.prepareStatement("SELECT * FROM mantenimiento_historial WHERE id_unit = ?");

            stmt.setString(1, idUnit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {


                    MantenimientoHistorial m = new MantenimientoHistorial();
                    m.setIdMantenimiento(rs.getInt("id_mantenimiento_historial"));
                    m.setIdUnidad(rs.getNString("id_unit"));
                    m.setFecha_programada(rs.getDate("fecha_mantenimiento_programada").toString());
                    m.setFecha_inicio_mantenimiento(rs.getDate("fecha_mantenimiento_iniciado").toString());
                    m.setTipoMantenimiento(rs.getString("tipo_mantenimiento"));
                    m.setEncargado(rs.getString("encargado_mantenimiento"));
                    m.setEstado(rs.getString("estado_mantenimiento"));
                    m.setNotas_mantenimiento_programado(rs.getNString("notas_mantenimiento_programado"));
                    m.setNotas_mantenimiento_programado(rs.getString("notas_mantenimiento_encargado"));
                    m.setFecha_finalizacion_mantenimiento(rs.getDate("fecha_finalización_mantenimiento").toString());
                    m.setImagenesJSONEvidencia(rs.getString("fotos_evidencia_programada"));

                    mantenimientos.add(m);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener los registros de mantenimiento de la tabla mantenimiento_historial: " + e.getMessage());
        }

        return mantenimientos;
    }

    public Mantenimiento obtenerMantenimientoProgramadoPorId(int idMantenimiento) {
        Mantenimiento mantenimiento = null;

        try {
             PreparedStatement stmt = conexion.prepareStatement("SELECT * FROM mantenimiento_programado WHERE id_mantenimiento_programado = ?");

            stmt.setInt(1, idMantenimiento);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    mantenimiento = new Mantenimiento();
                    mantenimiento.setId_mantenimiento_programado(rs.getInt("id_mantenimiento_programado"));
                    mantenimiento.setIdUnit(rs.getString("id_unit"));
                    mantenimiento.setMaintenanceDate(rs.getDate("fecha_mantenimiento").toString());
                    mantenimiento.setMaintenanceType(rs.getString("tipo_mantenimiento"));
                    mantenimiento.setMaintenanceNotes(rs.getString("nota_mantenimiento"));
                    mantenimiento.setJSONImagenes(rs.getString("fotos_evidencia"));

                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener los datos del mantenimiento desde la base de datos: " + e.getMessage());
        }

        return mantenimiento;
    }

    public boolean actualizarMantenimientoProgramado(Mantenimiento mantenimientoProgramado) {
        boolean actualizado = false;

        try {
             PreparedStatement stmt = conexion.prepareStatement("UPDATE mantenimiento_programado SET fecha_mantenimiento = ?, nota_mantenimiento = ?, tipo_mantenimiento = ?, fotos_evidencia = ? WHERE id_mantenimiento_programado = ?");

            stmt.setDate(1, Date.valueOf(mantenimientoProgramado.getMaintenanceDate()));
            stmt.setString(2, mantenimientoProgramado.getMaintenanceNotes());
            stmt.setString(3, mantenimientoProgramado.getMaintenanceType());
            stmt.setString(4, mantenimientoProgramado.getJSONArrayImagenes());
            stmt.setInt(5, mantenimientoProgramado.getId_mantenimiento_programado());

            int filasActualizadas = stmt.executeUpdate();

            if (filasActualizadas > 0) {
                actualizado = true;
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar los datos del mantenimiento programado en la base de datos: " + e.getMessage());
        }

        return actualizado;
    }
}

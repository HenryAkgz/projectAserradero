package conexión;

import clases.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

public class Conexión_old {

    String dbName = "aserradero.db";
    String URL_DB = "jdbc:sqlite:" + dbName;


    public Connection connectWidthDB() {

        try {
            Connection conexión = DriverManager.getConnection(URL_DB);
            return conexión;
        } catch (SQLException e) {
            return null;
        }
    }

    /* #############################################  Métodos de piezas  ############################################# */

    //obtiene los datos de todas las piezas disponibles en la BD.
    public ArrayList<Pieza> getAllPiecesFromDB() {
        try {
            Connection con = connectWidthDB();

            ArrayList<Pieza> listaDePiezas = new ArrayList<Pieza>();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT *  FROM piezas");

            while (resultSet.next()) {
                Pieza pieza = new Pieza();

                pieza.setId_Pieza(resultSet.getInt(1));
                pieza.setNombrePieza(resultSet.getString(2));
                pieza.setPhotoPieza(resultSet.getBytes(3));
                pieza.setCantidadEnInvetario(resultSet.getInt(4));


                listaDePiezas.add(pieza);
            }

            resultSet.close();
            con.close();

            return listaDePiezas;
        } catch (SQLException ex) {
            return null;
        }
    }

    //obtiene un elemento pieza de la base de datos según su ID
    public Pieza getPiezaById(int idPieza) {
        try {
            Connection con = connectWidthDB();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM piezas WHERE id_pieza = " + idPieza);
            Pieza pieza = new Pieza();

            if (resultSet.next()) {
                pieza.setId_Pieza(resultSet.getInt(1));
                pieza.setNombrePieza(resultSet.getString(2));
                pieza.setPhotoPieza(resultSet.getBytes(3));
            }

            statement.close();
            con.close();

            return pieza;

        } catch (SQLException e) {
            return null;
        }
    }

    //guarda un elemento pieza en la base de datos
    public boolean savePartInBD(Pieza pieza) {
        try {
            Connection con = connectWidthDB();
            String query = "INSERT INTO piezas (nombre_pieza, foto_pieza, piezas_inventario) VALUES ( ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, pieza.getNombrePieza());
            preparedStatement.setBytes(2, pieza.getPhotoPieza());
            preparedStatement.setInt(3, pieza.getCantidadEnInvetario());
            preparedStatement.execute();
            preparedStatement.close();
            con.close();

            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    //actualiza un elemento pieza de la base de datos
    public boolean updatePart(Pieza u) {
        try {
            Connection con = connectWidthDB();
            String query = "UPDATE piezas SET nombre_pieza = ?, foto_pieza = ?, piezas_inventario = ? WHERE id_pieza = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, u.getNombrePieza());
            ps.setBytes(2, u.getPhotoPieza());
            ps.setInt(3, u.getCantidadEnInvetario());
            ps.setInt(4, u.getId_Pieza());
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //elimina una pieza de la BD.
    public boolean deletePartFromBD(int idPieza) {
        try {
            Connection con = connectWidthDB();
            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM piezas WHERE id_pieza = ?");
            preparedStatement.setInt(1, idPieza);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            con.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /* #############################################  Métodos de Modelo  ############################################# */

    //guarda en la BD los datos de un nuevo modelo.
    public boolean saveModeloInDB(Modelo e) {
       /* try {
            Connection con = connectWidthDB();
            String query = "INSERT INTO modelo (id_modelo, peso, motor, maxLogDiameter, maxBoardWidth, maxBoardThickness, bladeSize, trackLength, trackWidth, trackHeightAdjustability, foto_modelo) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, e.getIdModelo());
            preparedStatement.setString(2, e.getPeso());
            preparedStatement.setString(3, e.getMotor());
            preparedStatement.setString(4, e.getMaxLogDiameter());
            preparedStatement.setString(5, e.getMaxBoardWidth());
            preparedStatement.setString(6, e.getMaxBoardThickness());
            preparedStatement.setString(7, e.getBladeSize());
            preparedStatement.setString(8, e.getTrackLength());
            preparedStatement.setString(9, e.getTrackWidth());
            preparedStatement.setString(10, e.getTrackHeightAdjustability());
            preparedStatement.setBytes(11, e.getFoto_modelo());

            preparedStatement.execute();
            preparedStatement.close();
            con.close();

            return true;
        } catch (SQLException ex) {
            return false;
        }*/
            return true;
    }

    //obtiene todas las unidades almacenadas en la BD
  /*  public ArrayList<Modelo> getModelosFromDB() {
        try {
            Connection con = connectWidthDB();

            ArrayList<Modelo> listaDeModelos = new ArrayList<Modelo>();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT *  FROM modelo");

            while (resultSet.next()) {
                Modelo modelo = new Modelo();
                modelo.setIdModelo(resultSet.getString(1));
                modelo.setPeso(resultSet.getString(2));
                modelo.setMotor(resultSet.getString(3));
                modelo.setMaxLogDiameter(resultSet.getString(4));
                modelo.setMaxBoardWidth(resultSet.getString(5));
                modelo.setMaxBoardThickness(resultSet.getString(6));
                modelo.setBladeSize(resultSet.getString(7));
                modelo.setTrackLength(resultSet.getString(8));
                modelo.setTrackWidth(resultSet.getString(9));
                modelo.setTrackHeightAdjustability(resultSet.getString(10));
                modelo.setFoto_modelo(resultSet.getBytes(11));

                listaDeModelos.add(modelo);
            }

            resultSet.close();
            con.close();

            return listaDeModelos;
        } catch (SQLException ex) {
            return null;
        }

    }*/

    //Borra un elemento de la base de datos
    public boolean deleteModelFromDB(String idModelo) {
        try {
            Connection con = connectWidthDB();
            String query = "DELETE FROM modelo WHERE id_modelo = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, idModelo);
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //elimina la pieza de la relación modelo-pieza
    public boolean deletePartFromModel(String idModel, int idPieza) {
        try {
            Connection con = connectWidthDB();
            String query = "DELETE FROM piezas_modelo WHERE id_modelo = ? AND id_pieza = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, idModel);
            ps.setInt(2, idPieza);

            int b = ps.executeUpdate();

            ps.close();
            con.close();
            return b == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //crea una relación entre una pieza existente y un modelo
    public boolean addPartToModel(String idModel, int idPieza, int cantidad) {
        try {
            Connection con = connectWidthDB();
            String query = "INSERT INTO piezas_modelo (id_modelo, id_pieza, cantidad) VALUES ( ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, idModel);
            ps.setInt(2, idPieza);
            ps.setInt(3, cantidad);
            ps.execute();
            ps.close();
            con.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addPiecesToModel(List<Pieza> listaPieza, String idModelo) {

        try {
            Connection con = connectWidthDB();
            String query = "INSERT INTO piezas_modelo (id_modelo, id_pieza, cantidad) VALUES ( ?, ?, ?)";


            for (Pieza item : listaPieza) {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, idModelo);
                preparedStatement.setInt(2, item.getId_Pieza());
                preparedStatement.setInt(3, item.getCantidadEnInvetario());
                preparedStatement.execute();
                preparedStatement.close();
            }
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //retorna true si existe un modelo en la bd con el ID "idmodelo".
    public boolean existModel(String idModelo) {
        try {
            Connection con = connectWidthDB();
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id_modelo FROM modelo WHERE id_modelo = " + idModelo);

            if (resultSet.next()) {
                statement.close();
                con.close();
                return true;
            } else {
                statement.close();
                con.close();
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    //actualiza en la BD los datos del modelo.
  /*  public boolean updateModel(Modelo modelo) {
        try {
            Connection con = connectWidthDB();
            String query = "UPDATE modelo SET peso = ?, motor = ?, maxLogDiameter = ?, maxBoardWidth = ?, maxBoardThickness = ?, bladeSize = ?, trackLength = ?, trackWidth = ?, trackHeightAdjustability = ?, foto_modelo = ? WHERE id_modelo = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, modelo.getPeso());
            ps.setString(2, modelo.getMotor());
            ps.setString(3, modelo.getMaxLogDiameter());
            ps.setString(4, modelo.getMaxBoardWidth());
            ps.setString(5, modelo.getMaxBoardThickness());
            ps.setString(6, modelo.getBladeSize());
            ps.setString(7, modelo.getTrackLength());
            ps.setString(8, modelo.getTrackWidth());
            ps.setString(9, modelo.getTrackHeightAdjustability());
            ps.setBytes(10, modelo.getFoto_modelo());
            ps.setString(11, modelo.getIdModelo());

            ps.executeUpdate();

            ps.close();
            con.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }*/

    //Obtiene todas piezas que conforman el modelo.
    public ArrayList<Pieza> getAllPiecesByModel(String idModel) {
        try {
            Connection con = connectWidthDB();

            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM piezas_modelo WHERE id_modelo = '" + idModel + "'");
            ArrayList<Pieza> listaPiezas = new ArrayList<>();


            while (resultSet.next()) {
                Pieza pieza = getPiezaById(resultSet.getInt(2));
                pieza.setCantidadEnInvetario(resultSet.getInt(3));
                listaPiezas.add(pieza);
            }

            statement.close();
            con.close();

            return listaPiezas;
        } catch (SQLException e) {
            return null;
        }
    }

    //actualiza la cantidad de piezas que necesita un modelo.
    public boolean updatePartOfModel(String idModel, int idPieza, int cantidad) {
        try {
            Connection con = connectWidthDB();
            String query = "UPDATE piezas_modelo SET cantidad = ? WHERE id_modelo = ? AND id_pieza = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, cantidad);
            ps.setString(2, idModel);
            ps.setInt(3, idPieza);

            ps.executeUpdate();

            ps.close();
            con.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //retorna true si el modelo actualmente tiene relación con una pieza.
    public boolean existPartInModel(String idModel, int idPieza) {
        try {
            Connection con = connectWidthDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT cantidad FROM piezas_modelo WHERE id_modelo = '" + idModel + "' AND id_pieza = " + idPieza);

            Boolean resultado = rs.next();

            st.close();
            con.close();

            return resultado;
        } catch (SQLException e) {
            return false;
        }
    }

   //obtiene el número de unidades existentes de un modelo especificado.
    public int getCountUnitsFromModels(String idModel) {

        try {
            int count = 0;
            Connection con = connectWidthDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT id_unit FROM unidades WHERE id_modelo = '" + idModel + "'");

            while (rs.next()) {
                count++;
            }

            return count;
        } catch (SQLException e) {
            return 0;
        }

    }

    /* #############################################  Métodos de Unidades  ############################################# */

    //obtiene de la BD los datos de todas las unidades.
    public ArrayList<Unidad> getALLUnitsFromDB() {
        try {
            Connection con = connectWidthDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM unidades");

            ArrayList<Unidad> unidadList = new ArrayList<>();

            while (rs.next()) {
                Unidad u = new Unidad();
                u.setId_unidad(rs.getString(1));
                u.setId_modelo(rs.getString(2));
                u.setEstado(rs.getString(3));
                u.setNotas_de_la_unidad(rs.getString(4));
                u.setPhotoUnidad(rs.getBytes(5));

                unidadList.add(u);
            }

            st.close();
            con.close();
            return unidadList;
        } catch (SQLException e) {
            return null;
        }
    }

    //retorna true si una unidad existe en la bd, si no retorna false.
    public boolean existUnit(String idUnit) {
        try {
            Connection con = connectWidthDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT estado FROM unidades WHERE id_unit = '" + idUnit + "'");

            boolean resultado = rs.next();

            st.close();
            con.close();
            return resultado;
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    //agrega una nueva unidad a la BD
    public boolean saveUnit(Unidad u) {

        try {
            Connection con = connectWidthDB();
            String query = "INSERT INTO unidades (id_unit, id_modelo, estado, notas_unidad, foto_unidad) VALUES ( ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, u.getId_unidad());
            ps.setString(2, u.getId_modelo());
            ps.setString(3, u.getEstado());
            ps.setString(4, u.getNotas_de_la_unidad());
            ps.setBytes(5, u.getPhotoUnidad());

            ps.execute();
            ps.close();
            con.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //actualiza los datos de una unidad en la bd
    public boolean updateUnit(Unidad u) {
        try {
            Connection con = connectWidthDB();
            String query = "UPDATE unidades SET id_modelo = ?, estado = ?, notas_unidad = ?, foto_unidad = ? WHERE id_unit = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, u.getId_modelo());
            ps.setString(2, u.getEstado());
            ps.setString(3, u.getNotas_de_la_unidad());
            ps.setBytes(4, u.getPhotoUnidad());
            ps.setString(5, u.getId_unidad());

            ps.executeUpdate();

            ps.close();
            con.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //elimina una unidad de la BD
    public boolean deleteUnitFromDB(String idUnit) {
        try {
            Connection con = connectWidthDB();
            String query = "DELETE FROM unidades WHERE id_unit = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, idUnit);
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //obtiene los datos de una unidad especificada.
    public Unidad getUnitByID(String id) {
        try {
            Connection con = connectWidthDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM unidades WHERE id_unit = '" + id + "'");

            if (rs.next()) {
                Unidad u = new Unidad();
                u.setId_unidad(rs.getString(1));
                u.setId_modelo(rs.getString(2));
                u.setEstado(rs.getString(3));
                u.setNotas_de_la_unidad(rs.getString(4));
                u.setPhotoUnidad(rs.getBytes(5));

                st.close();
                con.close();
                return u;
            }

            st.close();
            con.close();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //actualiza el estado de una unidad.
    public boolean updateStateUnit(String idUnidad, String state) {
        try {
            Connection con = connectWidthDB();
            String query = "UPDATE unidades SET estado = ? WHERE id_unit = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, state);
            ps.setString(2, idUnidad);
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /* #############################################  Métodos de mantenimiento  ############################################# */

    //añade un nuevo mantenimiento a la lista de mantenimientos programados.
    public boolean saveMantenimientoProgramado(String idUnidad, String fechaMantenimiento, String notas) {
        try {
            Connection con = connectWidthDB();
            String query = "INSERT INTO mantenimiento_programado (id_unit, fecha_mantenimiento, nota_mantenimiento) VALUES ( ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, idUnidad);
            ps.setDate(2, Date.valueOf(fechaMantenimiento));
            ps.setString(3, notas);

            ps.execute();
            ps.close();
            con.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //obtiene la fecha del mantenimiento más próximo.
    public String getNextMaintenanceDateByUnit(String idUnidad) {
        try {
            Connection con = connectWidthDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT fecha_mantenimiento FROM mantenimiento_programado WHERE id_unit = '" + idUnidad + "' ORDER BY fecha_mantenimiento ASC");

            String lastMaintenance = Constants.UNIDAD_SIN_MANTENIMIENTO_PROGRAMADO;

            if (rs.next()) {
                lastMaintenance = rs.getDate(1).toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
            }

            st.close();
            con.close();

            return lastMaintenance;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //obtiene la fecha del último mantenimiento realizado.
    public String getLastMaintenanceDateByUnit(String idUnidad) {
        try {
            Connection con = connectWidthDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT fecha_programada FROM mantenimiento_historial WHERE id_unit = '" + idUnidad + "' AND estado_mantenimiento = '" + Constants.ESTADO_MANTENIMIENTO_OK + "' ORDER BY fecha_programada ASC");

            String lastMaintenance = Constants.UNIDAD_SIN_MANTENIMIENTO;

            if (rs.next()) {
                lastMaintenance = rs.getDate(1).toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
            }

            st.close();
            con.close();

            return lastMaintenance;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Obtiene las fechas de mantenimiento que estan en el historial sin importar el estado.
    public String getAllLastMaintenanceDateByUnit(String idUnidad) {
        try {
            Connection con = connectWidthDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT fecha_programada FROM mantenimiento_historial WHERE id_unit = '" + idUnidad + "' ORDER BY fecha_programada ASC");

            String lastMaintenance = Constants.UNIDAD_SIN_MANTENIMIENTO;

            if (rs.next()) {
                lastMaintenance = rs.getDate(1).toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
            }

            st.close();
            con.close();

            return lastMaintenance;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //obtiene los datos de todos los mantenimientos programados.
    public ArrayList<Mantenimiento> getAllNextMaintenanceData(String idUnidad) {
        try {
            Connection con = connectWidthDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM mantenimiento_programado WHERE id_unit = '" + idUnidad + "' ORDER BY fecha_mantenimiento ASC");

            ArrayList<Mantenimiento> lista = new ArrayList<>();

            while (rs.next()) {

                Mantenimiento item = new Mantenimiento();
                item.setIdUnit(rs.getString(1));
                item.setMaintenanceDate(rs.getDate(2).toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                item.setMaintenanceNotes(rs.getString(3));

                lista.add(item);
            }

            st.close();
            con.close();

            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //elimina un mantenimiento de la tabla de mantenimientos programados.
    public boolean deleteNextMaintenanceDate(String idUnidad, String fechaProgramada) {
        try {
            Connection con = connectWidthDB();
            String query = "DELETE FROM mantenimiento_programado WHERE id_unit = ? AND fecha_mantenimiento = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, idUnidad);
            ps.setDate(2, Date.valueOf(LocalDate.parse(fechaProgramada, DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))));
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //actualiza los datos de un mantenimiento programado
    public boolean updateNextDateMaintenance(Mantenimiento old, Mantenimiento update) {
        try {
            Connection con = connectWidthDB();
            String query = "UPDATE mantenimiento_programado SET fecha_mantenimiento = ?, nota_mantenimiento = ? WHERE id_unit = ? AND fecha_mantenimiento = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setDate(1, Date.valueOf(update.getMaintenanceDate()));
            ps.setString(2, update.getMaintenanceNotes());

            //where

            ps.setString(3, old.getIdUnit());
            ps.setDate(4, Date.valueOf(LocalDate.parse(old.getMaintenanceDate(), DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))));
            ps.executeUpdate();
            ps.close();
            con.close();

            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    //añade al historial un mantenimiento.
    public boolean addToMaintenanceHistory(MantenimientoHistorial u) {
        try {
            Connection con = connectWidthDB();
            String query = "INSERT INTO mantenimiento_historial (id_unit, fecha_programada, inicio_mantenimiento, encargado_mantenimiento, estado_mantenimiento, notas_mantenimiento, notas_encargado) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, u.getIdUnidad());
            ps.setDate(2, Date.valueOf(LocalDate.parse(u.getFecha_programada(), DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))));
            ps.setDate(3, Date.valueOf(u.getFecha_inicio_mantenimiento()));
            ps.setString(4, u.getEncargado());
            ps.setString(5, u.getEstado());
            ps.setString(6, u.getNotas_mantenimiento_programado());
            ps.setString(7, u.getNotas_mantenimiento_encargado());

            ps.execute();
            ps.close();
            con.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //actualiza los datos de un mantenimiento en la tabla del historial de mantenimiento.
    public boolean updateMaintenanceHistory(MantenimientoHistorial u) {
        try {
            Connection con = connectWidthDB();
            String query = "UPDATE mantenimiento_historial SET encargado_mantenimiento = ?, estado_mantenimiento = ?, notas_encargado = ?, fecha_finalizacion = ? WHERE id_unit = ? AND fecha_programada = ?";
            String query2 = "UPDATE mantenimiento_historial SET encargado_mantenimiento = ?, estado_mantenimiento = ?, notas_encargado = ? WHERE id_unit = ? AND fecha_programada = ?";
            PreparedStatement ps = con.prepareStatement(u.getEstado().equals(Constants.ESTADO_MANTENIMIENTO_OK) ? query : query2);


            ps.setString(1, u.getEncargado());
            ps.setString(2, u.getEstado());
            ps.setString(3, u.getNotas_mantenimiento_encargado());

            if (u.getEstado().equals(Constants.ESTADO_MANTENIMIENTO_OK)) {
                ps.setDate(4, Date.valueOf(u.getFecha_finalizacion_mantenimiento()));
                ps.setString(5, u.getIdUnidad());
                ps.setDate(6, Date.valueOf(LocalDate.parse(u.getFecha_programada(), DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))));
            } else {
                ps.setString(4, u.getIdUnidad());
                ps.setDate(5, Date.valueOf(LocalDate.parse(u.getFecha_programada(), DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))));
            }

            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //obtiene los datos de todos los mantenimientos almacenados en el historial de mantenimiento.
    public ArrayList<MantenimientoHistorial> getAllMaintenanceHistoryDataByUnit(String idUnidad) {

        try {
            Connection con = connectWidthDB();
            Statement st = con.createStatement();
            String query = "SELECT * FROM mantenimiento_historial WHERE id_unit = '" + idUnidad + "'";
            ResultSet rs = st.executeQuery(query);

            ArrayList<MantenimientoHistorial> listaMantenimientoHistorial = new ArrayList<>();

            while (rs.next()) {
                MantenimientoHistorial mh = new MantenimientoHistorial();
                mh.setIdUnidad(rs.getString(1));
                mh.setFecha_programada(rs.getDate(2).toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                mh.setFecha_inicio_mantenimiento(rs.getDate(3).toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                mh.setEncargado(rs.getString(4));
                mh.setEstado(rs.getString(5));
                mh.setNotas_mantenimiento_programado(rs.getString(6));
                mh.setNotas_mantenimiento_encargado(rs.getString(7));

                if (rs.getDate(8) != null) {
                    mh.setFecha_finalizacion_mantenimiento(rs.getDate(8).toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                }

                listaMantenimientoHistorial.add(mh);
            }

            st.close();
            con.close();
            return listaMantenimientoHistorial;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

}

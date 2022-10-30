package conexión;

import clases.Modelo;
import clases.Pieza;
import clases.Unidad;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class
Conexión {

    String dbName = "aserradero.db";
    String URL_DB = "jdbc:sqlite:" + dbName;


    public Connection connectWidthDB() {

        try {
            Connection conexion = DriverManager.getConnection(URL_DB);
            return conexion;
        } catch (SQLException e) {
            return null;
        }
    }


    //obtiene todas las unidades almacenadas en la BD
    public ArrayList<Modelo> getModelosFromDB() {
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

    }

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

    public boolean saveModeloInDB(Modelo e) {
        try {
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

    public boolean updateModel(Modelo modelo){
        try{
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
        }catch (SQLException e){
            return false;
        }
    }

    public boolean deletePartFromModel(String idModel, int idPieza){
        try {
            Connection con = connectWidthDB();
            String query = "DELETE FROM piezas_modelo WHERE id_modelo = ? AND id_pieza = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, idModel);
            ps.setInt(2, idPieza);

            ps.executeUpdate();

            ps.close();
            con.close();
            return true;
        }catch (SQLException e){
            return false;
        }
    }

    public boolean addPartToModel(String idModel, int idPieza, int cantidad){
        try{
            Connection con = connectWidthDB();
            String query = "INSERT INTO piezas_modelo (id_modelo, id_pieza, cantidad) VALUES ( ?, ?, ?)";
             PreparedStatement ps = con.prepareStatement(query);
             ps.setString(1, idModel);
             ps.setInt(2, idPieza);
             ps.setInt(3, cantidad);
             ps.execute();
             con.close();
            return true;
        }catch (SQLException e){
            return false;
        }
    }


    public boolean updatePartOfModel(String idModel, int idPieza, int cantidad){
        try{
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
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }


    public boolean existPartInModel(String idModel, int idPieza){
        try {
            Connection con = connectWidthDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT cantidad FROM piezas_modelo WHERE id_modelo = '" + idModel + "' AND id_pieza = " + idPieza);

            Boolean resultado = rs.next();

            st.close();
            con.close();

           return resultado;
        }catch (SQLException e){
            return false;
        }
    }

    public ArrayList<Unidad> getALLUnitsFromDB(){
        try{
            Connection con = connectWidthDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM unidades");

            ArrayList<Unidad> unidadList = new ArrayList<>();

            while (rs.next()){
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
        }catch (SQLException e){
            return null;
        }
    }

    public int getCountUnitsFromModels(String idModel){

        try{
            int count =0;
            Connection con = connectWidthDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT id_unit FROM unidades WHERE id_modelo = '"+idModel+"'");

            while(rs.next()){
                count++;
            }

         return count;
        }catch(SQLException e){
            return 0;
        }

    }

}

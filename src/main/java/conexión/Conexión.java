package conexión;

import clases.Modelo;
import clases.Pieza;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class Conexión {

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


}

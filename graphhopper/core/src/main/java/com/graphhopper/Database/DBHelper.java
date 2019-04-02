package com.graphhopper.Database;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class DBHelper {

    private GetDBSource dbSource;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;

    public void DBConnection() {

        try{
            dbSource = new GetDBSource();
            connection = dbSource.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);

            if(!connection.isClosed()) {
                System.out.println("database open...");
            }

            DBWrite();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void DBClose() throws SQLException {

        dbSource.closeConnection(connection);

        if(connection.isClosed()) {
            System.out.println("database close...");
        }
    }


    private void DBWrite(){
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO gpx VALUES(?,?)");

            preparedStatement.setInt(1,377858);
            preparedStatement.setDouble(2,365.6);

            preparedStatement.executeUpdate();
            preparedStatement.clearParameters();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void DBWrite(int edgeId){
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO gpx VALUES(?,?)");

            preparedStatement.setInt(1,edgeId);
            preparedStatement.setDouble(2,1);

            preparedStatement.executeUpdate();
            preparedStatement.clearParameters();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void DBRead(){
        try {
            ResultSet resultSet = preparedStatement.executeQuery("SELECT * FROM gpx");

            while (resultSet.next()){
                System.out.print(resultSet.getInt(1) + "\t");
                System.out.print(resultSet.getInt(2) + "\t");
                System.out.print(resultSet.getInt(3) + "\t");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getEdgeWeighting(ArrayList<Integer> EdgeID){

        boolean DataExit = false;
        double EdgeWeighting =0.0;

        for(int id =0; id < EdgeID.size(); id++){

            try {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM gpx WHERE edge =" + EdgeID.get(id));

                while(resultSet.next()){
                    System.out.print(resultSet.getInt("edge") + "\t");
                    System.out.println(resultSet.getDouble("weighting") + "\t");
                    EdgeWeighting = resultSet.getDouble("weighting");
                    DataExit = true;
                }

                if(!DataExit){
                    System.out.println("NOT Value Edge: " + EdgeID.get(id));
                    DBWrite(EdgeID.get(id));
                }

                DataExit = false;

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return EdgeWeighting;
    }

}

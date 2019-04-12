package com.graphhopper.Database;

import com.graphhopper.GPXUtil.GPXTraining;
import com.graphhopper.matching.GPXFile;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class DBHelper {

    private GetDBSource dbSource;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private GPXTraining gpxTraining = new GPXTraining();

    public void DBConnection() {

        try{
            dbSource = new GetDBSource();
            connection = dbSource.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);

            if(!connection.isClosed()) {
                System.out.println("database open...");
            }

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

    private void DBWrite(int edgeId){
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO gpx VALUES(?,?,?,?)");

            preparedStatement.setInt(1,edgeId);
            preparedStatement.setDouble(2,0);
            preparedStatement.setInt(3,1);
            preparedStatement.setInt(4,1);

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

    public void TrainEdgeWeighting(ArrayList<Integer> EdgeID){

        boolean DataExist = false;
        double EdgeWeighting;

        for(int id =0; id < EdgeID.size(); id++){
            try {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM gpx WHERE edge =" + EdgeID.get(id));

                while(resultSet.next()){
                    EdgeWeighting = resultSet.getDouble("weighting");
                    EdgeWeighting = gpxTraining.TrainWeighting(EdgeWeighting,resultSet.getInt("CurrentTrain"),resultSet.getInt("LastTrain"));
                    DBUpdate(resultSet.getInt("edge"),EdgeWeighting,resultSet.getInt("CurrentTrain")+1,resultSet.getInt("LastTrain")+1);
                    DataExist = true;
                }

                if(!DataExist){
                    System.out.println("NOT Value Edge: " + EdgeID.get(id));
                    DBWrite(EdgeID.get(id));
                }

                DataExist = false;

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public void DBUpdate(int edgeId, double weight, int current_time, int last_time){
        try {
            preparedStatement = connection.prepareStatement("UPDATE gpx SET edge=?, weighting=?, CurrentTrain=?, LastTrain=? WHERE edge="+ edgeId);

            preparedStatement.setInt(1,edgeId);
            preparedStatement.setDouble(2,weight);
            preparedStatement.setInt(3,current_time);
            preparedStatement.setInt(4,last_time);

            preparedStatement.executeUpdate();
            preparedStatement.clearParameters();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double DBGetEdgeWeighting(int EdgeID){

        double EdgeWeighting = 0;
        boolean DataExist = false;

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM gpx WHERE edge =" + EdgeID);

            while(resultSet.next()){
                EdgeWeighting = resultSet.getDouble("weighting");
                DataExist = true;
            }

            if(!DataExist)
                return EdgeWeighting;


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return EdgeWeighting;
    }



}

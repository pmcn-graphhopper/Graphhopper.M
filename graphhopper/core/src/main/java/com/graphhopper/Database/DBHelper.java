package com.graphhopper.Database;

import java.io.IOException;
import java.sql.*;

public class DBHelper {

    private GetDBSource dbSource;
    private Connection connection;
    private PreparedStatement preparedStatement;

    public void DBConnection() {

        try{
            dbSource = new GetDBSource();
            connection = dbSource.getConnection();


            if(!connection.isClosed()) {
                System.out.println("database open...");
            }

            DBWrite();
            DBRead();
            DBClose();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void DBClose() throws SQLException {

        dbSource.closeConnection(connection);

        if(connection.isClosed()) {
            System.out.println("database close...");
        }
    }

    public void DBWrite(){
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO gpx VALUES(?,?,?)");

            preparedStatement.setInt(1,1);
            preparedStatement.setInt(2,365);
            preparedStatement.setInt(3,5);

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
}

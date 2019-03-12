package com.graphhopper.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {

    public void DBConnection(){

        String url = "jdbc:mysql://localhost:3306/world";
        String user = "user";
        String password = "nutnpmcn";

        try{
            Class.forName("com.mysql.jdbc.Driver'");
            Connection connection = DriverManager.getConnection(url,user,password);

            if(connection !=null && ! connection.isClosed()){
                System.out.println("Connect Database Success!");
            }
        }
        catch (ClassNotFoundException e){
            System.out.println("NoT Found Driver");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

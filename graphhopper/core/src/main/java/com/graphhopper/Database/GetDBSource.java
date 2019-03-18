package com.graphhopper.Database;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class GetDBSource implements DBSource{
    private Properties props;
    private String url;
    private String user;
    private String passwd;

    public GetDBSource()throws IOException,ClassCastException{
        this("jdbc.properties");
    }

    public GetDBSource(String configFile) throws IOException,ClassCastException{
        props = new Properties();
        props.load(new FileInputStream(configFile));

        url = props.getProperty("com.graphhopper.Database.url");
        user = props.getProperty("com.graphhopper.Database.user");
        passwd = props.getProperty("com.graphhopper.Database.password");

        try {
            Class.forName(props.getProperty("com.graphhopper.Database.driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url, user, passwd);
    }

    public void closeConnection(Connection conn) throws SQLException {
        conn.close();
    }
}

package com.iec.ics.emi.subscription.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

//import oracle.jdbc.pool.OracleDataSource;

public class DBConnection {
	
	private static final String URL = "jdbc:oracle:thin:@";
    //private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
         
    public static final String USERNAME = "ceif";
    public static final String PASSWORD = "ceif";
    
    //The port is the one you used when you created the Tunnel.
    public static final String LOCAL_DEFAULT_CONNECT_DESCRIPTOR = "162.16.157.240:1521:raciec1"; 

    private static Connection connection = null;
    private static DBConnection instance = null;

    private DBConnection() {
        try {
            Class.forName(DRIVER).newInstance();
        } catch (Exception sqle) {
            sqle.printStackTrace();
        }
    }

    public static DBConnection getInstance() {
        if (connection == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
             /*   OracleDataSource ods = new OracleDataSource();
                ods.setURL(URL + LOCAL_DEFAULT_CONNECT_DESCRIPTOR);
                ods.setUser(USERNAME);
                ods.setPassword(PASSWORD);*/
            	connection = 
            	DriverManager.getConnection("jdbc:mysql://localhost/test?" +
                        "user=minty&password=greatsqldb");
               // connection = ods.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

}

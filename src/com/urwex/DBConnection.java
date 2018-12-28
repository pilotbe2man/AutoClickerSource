package com.urwex;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	public Connection getConnection() throws SQLException
	{
		String connectionString = null;
		String username = null;
		String password = null;
		Connection conn = null;
		try {
			Config config = new Config();
			connectionString = config.getConnectionString();
			username = config.getUsername();
			password = config.getPassword();
			 
//            String dbURL = "jdbc:sqlserver://localhost\\sqlexpress";
//            String user = "sa";
//            String pass = "secret";
            conn = DriverManager.getConnection(connectionString, username, password);
            if (conn != null) {
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
            }
 
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
		return conn;
	}
}

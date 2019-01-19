package com.jostens.jemm2.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Get/return a JEMM2 connection
 */
public class ConnectionHelper
{

	public static Connection getJEMM2Connection()
	{
		Connection conn = null;
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");

//			String url = "jdbc:oracle:thin:@db_tstt.jostens.com:1565/tstt";
//			String username = "xt11781";
//			String password = "Tigers30";

			String url = "jdbc:oracle:thin:@devapps:1521/orcl";
			String username = "jemm2";
			String password = "Jostens01.";


			conn = DriverManager.getConnection(url, username, password);
			conn.setAutoCommit(false);		// All connections require a user to commit the transaction
			
			conn.close();
	
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return conn;
	}
	
	public static void closeConnection(Connection conn)
	{
		try
		{
			conn.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
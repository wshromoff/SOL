package com.jostens.dam.shared.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.jostens.dam.shared.common.ExceptionHelper;

/**
 * Make a jdbc connection using the supplied credentials
 */
public class JDBCConnection
{
	private static boolean errorsToStdout = false;

	public static Connection getJDBCConnection(String url, String username, String password)
	{
		Connection conn = null;
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");

//			System.out.println("URL=" + url);
//			System.out.println("username=" + username);
//			System.out.println("password=" + password);
			conn = DriverManager.getConnection(url, username, password);
			conn.setAutoCommit(false);


		} 
		catch (Exception e)
		{
			if (errorsToStdout)
			{
				System.out.println("Cannot make JDBC Connection: " + ExceptionHelper.getStackTraceAsString(e));
			}
			else
			{
				ExceptionHelper.logExceptionToFile("getJDBCConnection (JDBCConnection)", e);
			}
		}
		
		return conn;
	}
	
	/**
	 * Get a Connection after logging in as the MB users found in the connection.properties file
	 */
	public static Connection getMediabinConnection()
	{
		ConnectionProperties properties = new ConnectionProperties();
		
		// Get the needed properties
		String url = properties.getProperty(ConnectionProperties.MB_URL);
		String user = properties.getProperty(ConnectionProperties.MB_USER);
		String password = properties.getProperty(ConnectionProperties.MB_PASSWORD);
		
		// Need to decrpt the password
		Encryption encryption = new Encryption();
		password = encryption.decrypt(password);
		
		Connection conn =  getJDBCConnection(url, user, password);
		return conn;
	}

	/**
	 * Get a Connection after logging in as the MB users found in the connection.properties file
	 * 
	 * By Default this connection is not auto commit - Must issue commit statement.
	 */
	public static Connection getCustomerProfileConnection() throws SQLException
	{
		ConnectionProperties properties = new ConnectionProperties();
		
		// Get the needed properties
		String url = properties.getProperty(ConnectionProperties.CP_URL);
		String user = properties.getProperty(ConnectionProperties.CP_USER);
		String password = properties.getProperty(ConnectionProperties.CP_PASSWORD);

//		System.out.println("URL = " + url);
//		System.out.println("user = " + user);
//		System.out.println("password = " + password);

		// Need to decrpt the password
		Encryption encryption = new Encryption();
		password = encryption.decrypt(password);
		
		Connection conn =  getJDBCConnection(url, user, password);
		conn.setAutoCommit(false);
		return conn;
	}

	/**
	 * Close a Connection
	 */
	public static void close(Connection conn)
	{
		if (conn == null)
		{
			return;
		}
		try
		{
			conn.close();
		} 
		catch (Exception e)
		{
			ExceptionHelper.logExceptionToFile("close (JDBCConnection)", e);
//			System.out.println("Cannot make JDBC Connection: " + ExceptionHelper.getStackTraceAsString(e));
		}

	}
	
	public static void setErrorsToStdout(boolean errorsToStdout)
	{
		JDBCConnection.errorsToStdout = errorsToStdout;
	}	
}

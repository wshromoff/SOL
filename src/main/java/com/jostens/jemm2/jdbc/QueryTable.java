package com.jostens.jemm2.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class QueryTable
{
	public void performQuery()
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
			
			String sql = "select count(*) from TEST";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				int rowCount = rs.getInt(1);
				System.out.println("Rows found=" + rowCount);
			}
			rs.close();
			ps.close();
			conn.close();
	
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}

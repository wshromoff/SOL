package com.jostens.dam.shared.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.dam.shared.testing.SharedBaseTest;

/**
 * Test JDBC foundation code
 * @author shromow
 *
 */
public class JDBCConnectionTest extends SharedBaseTest
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
//		FolderPath.setUnitTest(true);
//		setProductionEnvironment(true);
//		setIntegrationEnvironment(true);
		JDBCConnection.setErrorsToStdout(true);

		connectToMB();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		disconnectFromMB();
	}

//	@Test
	public void testGetJDBCConnection() throws SQLException
	{
		Connection c = JDBCConnection.getJDBCConnection("jdbc:oracle:thin:@//owbsljtdbb.jostens.com:1565/tst_t.jostens.com", "shromow", "Flower51");

		JDBCConnection.close(c);
	}

//	@Test
	public void testGetMediabinConnection() throws SQLException
	{
		
		Connection c = JDBCConnection.getMediabinConnection();

		JDBCConnection.close(c);
	}

	@Test
	public void testGetCustomerProfileConnection() throws SQLException
	{
		
		Connection c = JDBCConnection.getCustomerProfileConnection();

		JDBCConnection.close(c);
	}

//	@Test
	public void testSelectStatementMediaBin() throws SQLException
	{
		
		Connection c = JDBCConnection.getMediabinConnection();

		String sql = "select count(*) from MBSCHEDULEDTASKS";
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		if (rs.next())
		{
			int rowCount = rs.getInt(1);
			System.out.println("Rows found=" + rowCount);
		}
		rs.close();
		ps.close();
		JDBCConnection.close(c);
	}

//	@Test
	public void testSelectStatementCustomerProfile() throws SQLException
	{
		
		Connection c = JDBCConnection.getCustomerProfileConnection();

		String sql = "select count(*) from MB_ASSETS";
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		if (rs.next())
		{
			int rowCount = rs.getInt(1);
			System.out.println("Rows found=" + rowCount);
		}
		rs.close();
		ps.close();
		JDBCConnection.close(c);
	}

}

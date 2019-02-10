package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.jdbc.helpers.ImagePathHelper;

public class ImagePathHelperTest
{
	private static Connection c = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		Jemm2Statements statements = new Jemm2Statements();
		statements.initializeStatements();
		c = ConnectionHelper.getJEMM2Connection();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		ConnectionHelper.closeConnection(c);
	}

//	@Test
	public void testGetPartPath() throws SQLException
	{
		ImagePathHelper helper = new ImagePathHelper();
		
		String partPath = helper.getImagePath(c, "PR_003352", "1b");
		System.out.println("Part Image Path = " + partPath);
	}

//	@Test
	public void testGetCustomerPath() throws SQLException
	{
		ImagePathHelper helper = new ImagePathHelper();
		
		String partPath = helper.getImagePath(c, "CU_000007", "ba");
		System.out.println("Customer Image Path = " + partPath);
	}

	@Test
	public void testGetPackagePath() throws SQLException
	{
		ImagePathHelper helper = new ImagePathHelper();
		
		String partPath = helper.getImagePath(c, "PK_000260", "ba");
		System.out.println("Package Image Path = " + partPath);
	}

//	@Test
	public void testGetCustomerPackagePath() throws SQLException
	{
		ImagePathHelper helper = new ImagePathHelper();
		
		String partPath = helper.getImagePath(c, "CP_000260", "ba");
		System.out.println("Customer Package Image Path = " + partPath);
	}

}

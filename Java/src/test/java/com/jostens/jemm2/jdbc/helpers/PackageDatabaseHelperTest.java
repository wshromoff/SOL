package com.jostens.jemm2.jdbc.helpers;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;

public class PackageDatabaseHelperTest
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
	public void testGetNextPackageSequence()
	{
		PackageDatabaseHelper helper = new PackageDatabaseHelper();
		int sequence = helper.getNextPackageSequence(c);
		System.out.println("Sequence Value = " + sequence);
		sequence = helper.getNextPackageSequence(c);
		System.out.println("Sequence Value = " + sequence);
	}
	
	@Test
	public void testGetPackageID() throws SQLException
	{
		PackageDatabaseHelper helper = new PackageDatabaseHelper();
		int packageId = helper.getPackageID(c, "1234|567");
		System.out.println("Package Value = " + packageId);
		packageId = helper.getPackageID(c, "222|333");
		System.out.println("Package Value = " + packageId);
	}

}

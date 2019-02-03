package com.jostens.jemm2.jdbc.helpers;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.pojo.AssetPackage;
import com.jostens.jemm2.pojo.Customer;

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
	
//	@Test
	public void testGetPackageByIdentifier() throws SQLException
	{
		PackageDatabaseHelper helper = new PackageDatabaseHelper();
		int packageId = helper.getPackageIDByIdentifier(c, "1234|567");
		System.out.println("Package Value = " + packageId);
		packageId = helper.getPackageIDByIdentifier(c, "222|333");
		System.out.println("Package Value = " + packageId);
	}

//	@Test
	public void testGetPackageIDByName() throws SQLException
	{
		PackageDatabaseHelper helper = new PackageDatabaseHelper();
		int packageId = helper.getPackageIDByName(c, "a");
		System.out.println("Package Value = " + packageId);
		packageId = helper.getPackageIDByName(c, "xx");
		System.out.println("Package Value = " + packageId);
	}

	@Test
	public void testPersistPackage() throws SQLException
	{
		PackageDatabaseHelper helper = new PackageDatabaseHelper();

		AssetPackage aPackage = new AssetPackage();
		aPackage.setName("b");
		aPackage.setIdentifier("22|444");
		aPackage.setPartID(141);
		helper.persistPackage(c, aPackage);		
	}

}

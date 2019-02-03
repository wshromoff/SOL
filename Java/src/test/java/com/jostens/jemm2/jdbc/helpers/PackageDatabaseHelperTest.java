package com.jostens.jemm2.jdbc.helpers;

import static org.junit.Assert.assertTrue;

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

//	@Test
	public void testPersistPackage() throws SQLException
	{
		PackageDatabaseHelper helper = new PackageDatabaseHelper();

		AssetPackage aPackage = new AssetPackage();
		aPackage.setName("b");
		aPackage.setIdentifier("22|444");
		aPackage.setPartID(141);
		helper.persistPackage(c, aPackage);		
	}

	@Test
	public void testImportPackage() throws SQLException
	{
		AssetPackage aPackage = new AssetPackage("MO076155_1045582_crest_vector_flat_2t_dbks_0x_svs_rds_bks_x_x_x_x_x_x_x.cdr|Crest|MO076155|1045582|US|School|Silver|Silver|Red|Black|||||||Silver, Red, Black|Announcement (Traditional)|Announcement (Traditional)|Complete (Publish-Ready)|Cataloged|||2|Black|Customer Default|true|true|true|true|");
		PackageDatabaseHelper helper = new PackageDatabaseHelper();
		helper.persistPackage(c, aPackage);
		
		AssetPackage aPackage2 = new AssetPackage();
		aPackage2.setID(aPackage.getID());
		
		// Populate package2 from database
		helper.getPackage(c, aPackage2);
		
		assertTrue(aPackage.equals(aPackage2));
				
		System.out.println("Customer ID = " + aPackage.getID());
	}

}

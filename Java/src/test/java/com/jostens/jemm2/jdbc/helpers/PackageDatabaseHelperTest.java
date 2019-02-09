package com.jostens.jemm2.jdbc.helpers;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.pojo.Asset;
import com.jostens.jemm2.pojo.AssetPackage;
import com.jostens.jemm2.pojo.CustomerPackage;

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
				
		System.out.println("Package ID = " + aPackage.getID());
		
		aPackage = new AssetPackage("MO076155_x_crest_vector_flat_3t_dbks_0x_svs_rds_bks_x_x_x_x_x_x_x.cdr|Crest|MO076155|x|US|School|Silver|Silver|Red|Black|||||||Silver, Red, Black|Announcement (Traditional)|Announcement (Traditional)|Complete (Publish-Ready)|Cataloged|||3|Black|Customer Default|true|true|true|true|");
		helper.persistPackage(c, aPackage);
		System.out.println("Package ID = " + aPackage.getID());
	}

//	@Test
	public void testPersistAssets() throws SQLException
	{
		PackageDatabaseHelper helper = new PackageDatabaseHelper();

		List<Asset> assets = new ArrayList<Asset>();
		
		Asset asset = new Asset();
		asset.setFolderPath("/abc/def/");
		asset.setIsBestAvailable(1);
		asset.setName("Asset1");
		
		assets.add(asset);
		
		helper.persistAssets(c, 432, assets);
		
	}

//	@Test
	public void testPersistCustomerPackage() throws SQLException
	{
		PackageDatabaseHelper helper = new PackageDatabaseHelper();
		CustomerPackage customerPackage = new CustomerPackage();
		customerPackage.setPackageID(23);
		customerPackage.setCustomerID(432);
		customerPackage.setBusinessDefaultUse("BDU");

		helper.persistCustomerPackage(c, customerPackage);
		
	}

//	@Test
	public void testGetCustomerPackageIDByIdentifiers() throws SQLException
	{
		PackageDatabaseHelper helper = new PackageDatabaseHelper();
		int id = helper.getCustomerPackageIDByIdentifiers(c, 45, 32);
		System.out.println("Cusomer Package ID = " + id);
	}

//	@Test
	public void testGetNextCustomerPackageSequence()
	{
		PackageDatabaseHelper helper = new PackageDatabaseHelper();
		int sequence = helper.getNextCustomerPackageSequence(c);
		System.out.println("Sequence Value = " + sequence);
		sequence = helper.getNextCustomerPackageSequence(c);
		System.out.println("Sequence Value = " + sequence);
	}

}

package com.jostens.dam.brand.jdbc;

import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.dam.brand.helpers.StoreAssetPackageHelper;
import com.jostens.dam.brand.process.StoreAssetPackage;
import com.jostens.dam.brand.testing.BrandBaseTest;
import com.jostens.dam.shared.assetclass.BaseAsset;
import com.jostens.dam.shared.assetclass.MetadataGUIDS;
import com.jostens.dam.shared.assetclass.MetadataNames;
import com.jostens.dam.shared.helpers.MetadataHelper;
import com.jostens.dam.shared.jdbc.JDBCConnection;
import com.viragemediabin.www.MBMetadataParameter;

public class CustomerProfileDatabaseTest extends BrandBaseTest
{
	String[] metadataToPopulate = {MetadataGUIDS.ASSET_ID_JOSTENS, MetadataGUIDS.CUSTOMER_ID_ORACLE, MetadataGUIDS.NAME};
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
//		setProductionEnvironment(true);
//		setIntegrationEnvironment(true);
		useLiveFolders();
		connectToMB();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		disconnectFromMB();
	}

//	@Test
	public void testConstructor() throws Exception
	{
		CustomerProfileDatabase database = new CustomerProfileDatabase();
		System.out.println("Column Names=" + database.getAllColumnNames());
		System.out.println("Column Values=" + database.getAllColumnValues());
	}

	//                           --- SEQUENCES ---
//	@Test
	public void testGetNextMBAssetsSequence() throws Exception
	{
		Connection c = JDBCConnection.getCustomerProfileConnection();
		
		CustomerProfileDatabase database = new CustomerProfileDatabase();
		
		int sequence1 = database.getNextMBAssetsSequence(c);
		int sequence2 = database.getNextMBAssetsSequence(c);
		
		System.out.println("Sequence 1 = " + sequence1);
		System.out.println("Sequence 2 = " + sequence2);
		
		JDBCConnection.close(c);
	}
//	@Test
	public void testGetNextMBActionsSequence() throws Exception
	{
		Connection c = JDBCConnection.getCustomerProfileConnection();
		
		CustomerProfileDatabase database = new CustomerProfileDatabase();
		
		int sequence1 = database.getNextMBActionsSequence(c);
		int sequence2 = database.getNextMBActionsSequence(c);
		
		System.out.println("Sequence 1 = " + sequence1);
		System.out.println("Sequence 2 = " + sequence2);
		
		JDBCConnection.close(c);
	}

	//                           --- TABLES ---
	@Test
	public void testInsertMBAssets() throws Exception
	{	
		Connection c = JDBCConnection.getCustomerProfileConnection();		

		StoreAssetPackage storePackage = StoreAssetPackageHelper.getStoreAssetPackage(getWebService(), "BR000747_1051982_mascot_vector_flat_1t_dx_0x_gds_x_x_x_x_x_x_x_x_x.cdr", false, true);
		MetadataHelper.populateAssetMetadata(getWebService(), storePackage.getRenditions(), CustomerProfileDatabase.getMetadataToPopulate());
		performAssetInsert(c, storePackage.getRenditions());

		StoreAssetPackage etchingPackage = StoreAssetPackageHelper.getStoreAssetPackage(getWebService(), "CE000446_1041984_building_bitmap_flat_1t_dx_0x_bks_x_x_x_x_x_x_x_x_x.cdr", false, true);
		MetadataHelper.populateAssetMetadata(getWebService(), etchingPackage.getRenditions(), CustomerProfileDatabase.getMetadataToPopulate());
		performAssetInsert(c, etchingPackage.getRenditions());

		StoreAssetPackage etchingPackage2 = StoreAssetPackageHelper.getStoreAssetPackage(getWebService(), "CE018800_x_mascot_bitmap_ctone_ut_dx_0x_bks_x_x_x_x_x_x_x_x_x.cdr", false, true);
		MetadataHelper.populateAssetMetadata(getWebService(), etchingPackage2.getRenditions(), CustomerProfileDatabase.getMetadataToPopulate());
		performAssetInsert(c, etchingPackage2.getRenditions());

		c.commit();		// Auto-commit is disabled
		JDBCConnection.close(c);
	}	
	private void performAssetInsert(Connection c, List<BaseAsset> assets) throws SQLException
	{
		CustomerProfileDatabase database = new CustomerProfileDatabase();	
		
		for (BaseAsset ba : assets)
		{
			int s = database.getNextMBAssetsSequence(c);
			database.insertMBAssets(c, s, ba, "DAM/1/03");
		}
	}

	// Cannot really run this test anymore because of the integrity constraint to the MB_ASSETS table
//	@Test
	public void testInsertMBMetadata() throws Exception
	{
		
		Connection c = JDBCConnection.getCustomerProfileConnection();		

		StoreAssetPackage storePackage = StoreAssetPackageHelper.getStoreAssetPackage(getWebService(), "BR017630_1053694_crest_vector_flat_1t_dx_0x_bks_x_x_x_x_x_x_x_x_x.cdr", false, true);
		MetadataHelper.populateAssetMetadata(getWebService(), storePackage.getRenditions(), CustomerProfileDatabase.getMetadataToPopulate());
		performMetadataInsert(c, storePackage.getRenditions());
		
		StoreAssetPackage etchingPackage = StoreAssetPackageHelper.getStoreAssetPackage(getWebService(), "CE000446_1041984_building_bitmap_flat_1t_dx_0x_bks_x_x_x_x_x_x_x_x_x.cdr", false, true);
		MetadataHelper.populateAssetMetadata(getWebService(), etchingPackage.getRenditions(), CustomerProfileDatabase.getMetadataToPopulate());
		performMetadataInsert(c, etchingPackage.getRenditions());
		
		c.commit();		// Auto-commit is disabled
		JDBCConnection.close(c);
	}
	private void performMetadataInsert(Connection c, List<BaseAsset> assets) throws SQLException
	{
		CustomerProfileDatabase database = new CustomerProfileDatabase();		
		
		for (BaseAsset ba : assets)
		{
			int s = database.getNextMBAssetsSequence(c);
			database.insertMBMetadata(c, s, ba);			
		}
	}

//	@Test
	public void testAddNewAsset() throws Exception
	{
		
		Connection c = JDBCConnection.getCustomerProfileConnection();		

		StoreAssetPackage storePackage = StoreAssetPackageHelper.getStoreAssetPackage(getWebService(), "BR017630_1053694_crest_vector_flat_1t_dx_0x_bks_x_x_x_x_x_x_x_x_x.cdr", false, true);
		MetadataHelper.populateAssetMetadata(getWebService(), storePackage.getRenditions(), CustomerProfileDatabase.getMetadataToPopulate());
		performAddNewAsset(c, storePackage.getRenditions());

		StoreAssetPackage etchingPackage = StoreAssetPackageHelper.getStoreAssetPackage(getWebService(), "CE000446_1041984_building_bitmap_flat_1t_dx_0x_bks_x_x_x_x_x_x_x_x_x.cdr", false, true);
		MetadataHelper.populateAssetMetadata(getWebService(), etchingPackage.getRenditions(), CustomerProfileDatabase.getMetadataToPopulate());
		performAddNewAsset(c, etchingPackage.getRenditions());
		
		JDBCConnection.close(c);

	}	
	private void performAddNewAsset(Connection c, List<BaseAsset> assets) throws SQLException
	{
		CustomerProfileDatabase database = new CustomerProfileDatabase();		
		
		for (BaseAsset ba : assets)
		{
			database.addNewAsset(c, ba, "DAM/1/03");			
		}
	}

//	@Test
	public void testInsertDeleteAction() throws Exception
	{
		Connection c = JDBCConnection.getCustomerProfileConnection();
		
		CustomerProfileDatabase driver = new CustomerProfileDatabase();
		
		StoreAssetPackage storePackage = StoreAssetPackageHelper.getStoreAssetPackage(getWebService(), "BR017630_1053694_crest_vector_flat_1t_dx_0x_bks_x_x_x_x_x_x_x_x_x.cdr", false, false);
		MetadataHelper.populateAssetMetadata(getWebService(), storePackage.getIngestedAssets(), metadataToPopulate);
		
		driver.insertDeleteAction(c, storePackage.getMasterCDR());
		
		JDBCConnection.close(c);
	}

//	@Test
	public void testInsertAddAction() throws Exception
	{
		Connection c = JDBCConnection.getCustomerProfileConnection();
		
		CustomerProfileDatabase driver = new CustomerProfileDatabase();
		
		StoreAssetPackage storePackage = StoreAssetPackageHelper.getStoreAssetPackage(getWebService(), "BR017630_1053694_crest_vector_flat_1t_dx_0x_bks_x_x_x_x_x_x_x_x_x.cdr", false, false);
		MetadataHelper.populateAssetMetadata(getWebService(), storePackage.getIngestedAssets(), metadataToPopulate);
		
		driver.insertAddAction(c, storePackage.getMasterCDR());
		
		JDBCConnection.close(c);
	}
//	@Test
	public void testInsertMetadataAction() throws Exception
	{
		Connection c = JDBCConnection.getCustomerProfileConnection();
		
		CustomerProfileDatabase driver = new CustomerProfileDatabase();
		
		StoreAssetPackage storePackage = StoreAssetPackageHelper.getStoreAssetPackage(getWebService(), "BR017630_1053694_crest_vector_flat_1t_dx_0x_bks_x_x_x_x_x_x_x_x_x.cdr", false, false);
		MetadataHelper.populateAssetMetadata(getWebService(), storePackage.getIngestedAssets(), metadataToPopulate);
		
		driver.insertMetadataAction(c, storePackage.getMasterCDR(), "4=wowwy sauce|23=nope|");
		
		JDBCConnection.close(c);
	}


//	@Test
	public void testGetAssetForName() throws Exception
	{
		Connection c = JDBCConnection.getCustomerProfileConnection();
		
		CustomerProfileDatabase driver = new CustomerProfileDatabase();
		
		BaseAsset ba = driver.getAssetForName(c, "BR001920_2432847_mascot_vector_flat_1t_dx_0x_bks_x_x_x_x_x_x_x_x_x_1s.svg");
		
		System.out.println("Name = " + ba.getAssetName());
		System.out.println("ID = " + ba.getAssetId());
		// Display all found values for metadata on asset
		Map<String, MBMetadataParameter> assetMetadata = ba.getAssetMetadata();
		Iterator<Entry<String, MBMetadataParameter>> iter = assetMetadata.entrySet().iterator();
		while (iter.hasNext())
		{
			Entry<String, MBMetadataParameter> entry = iter.next();
			String guid = entry.getKey();
			System.out.println(MetadataNames.getNameForGUID(guid) + " = " + ba.getMetaDataValue(guid));
		}	
		
		// Now try an invalid name
		ba = driver.getAssetForName(c, "BR001920_2432847_mascot_vector_flat_1t_dx_0x_bks_x_x_x_x_x_x_x_x_x_1s.svgXXX");
		assertNull(ba);
		JDBCConnection.close(c);
	}

//	@Test
	public void testGetAssetNamesDateRange() throws Exception
	{
		Connection c = JDBCConnection.getCustomerProfileConnection();
		
		CustomerProfileDatabase driver = new CustomerProfileDatabase();
		
		List<String> namesInRange = driver.getAssetNamesDateRange(c, "13/04/2015", "14/04/2015");
		
		System.out.println("count=" + namesInRange.size());
		
		JDBCConnection.close(c);
	}

//	@Test
	public void testGetAssetNamesTimeDateRange() throws Exception
	{
		Connection c = JDBCConnection.getCustomerProfileConnection();
		
		CustomerProfileDatabase driver = new CustomerProfileDatabase();
		
		List<String> namesInRange = driver.getAssetNamesTimeDateRange(c, "16:00:00 13/04/2015", "17:00:00 13/04/2015");

		System.out.println("count=" + namesInRange.size());
		
		for (String name : namesInRange)
		{
			System.out.println(name);
		}
		
		JDBCConnection.close(c);
	}

//	@Test
	public void testUpdateModifiedTime() throws Exception
	{
		Connection c = JDBCConnection.getCustomerProfileConnection();
		
		CustomerProfileDatabase driver = new CustomerProfileDatabase();
		
		driver.updateModifiedTime(c, "16477", "" + System.currentTimeMillis());

		JDBCConnection.close(c);
	}

//	@Test
	public void testGetActionsToProcess() throws Exception
	{
		Connection c = JDBCConnection.getCustomerProfileConnection();
		
		CustomerProfileDatabase driver = new CustomerProfileDatabase();
		
		List<CustomerProfileAction> actions = driver.getActionsToProcess(c);
		
		JDBCConnection.close(c);
		
		for (CustomerProfileAction action : actions)
		{
			System.out.println(action.getID());
		}
	}

//	@Test
	public void testMarkActionComplete() throws Exception
	{
		Connection c = JDBCConnection.getCustomerProfileConnection();
		
		CustomerProfileDatabase driver = new CustomerProfileDatabase();
		
		CustomerProfileAction action = new CustomerProfileAction();
		action.setID("64");
		
		driver.markActionComplete(c, action, "Test");
		
		JDBCConnection.close(c);
		
	}

//	@Test
	public void testRemoveAsset() throws Exception
	{
		Connection c = JDBCConnection.getCustomerProfileConnection();
		
		CustomerProfileDatabase driver = new CustomerProfileDatabase();

		BaseAsset ba = new BaseAsset();
		ba.setAssetName("BR001948_2432847_mascot_vector_flat_1t_dx_0x_bks_x_x_x_x_x_x_x_x_x_ba.svg");
		
		driver.removeAsset(c, ba);
		
		JDBCConnection.close(c);
		
	}

//	@Test
	public void testGetAssetNamesForCustomerID() throws Exception
	{
		Connection c = JDBCConnection.getCustomerProfileConnection();
		
		CustomerProfileDatabase driver = new CustomerProfileDatabase();
		
		List<String> namesForID = driver.getAssetNamesForCustomerID(c, "%694");
		for (String name : namesForID)
		{
			System.out.println(name);
		}
		System.out.println("  --- ");
		namesForID = driver.getAssetNamesForCustomerID(c, "%984");
		for (String name : namesForID)
		{
			System.out.println(name);
		}
		System.out.println("  --- ");
		namesForID = driver.getAssetNamesForCustomerID(c, "%000");
		for (String name : namesForID)
		{
			System.out.println(name);
		}
		System.out.println("  --- ");
		namesForID = driver.getAssetNamesForCustomerID(c, "%99a");
		for (String name : namesForID)
		{
			System.out.println(name);
		}
		
		JDBCConnection.close(c);
	}

}

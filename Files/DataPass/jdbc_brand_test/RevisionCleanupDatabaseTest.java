package com.jostens.dam.brand.jdbc;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.dam.brand.folders.BrandFolderGUIDS;
import com.jostens.dam.brand.testing.BrandBaseTest;
import com.jostens.dam.shared.assetclass.BaseAsset;
import com.jostens.dam.shared.folders.FolderGUIDS;

public class RevisionCleanupDatabaseTest extends BrandBaseTest
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
//		setProductionEnvironment(true);
//		setIntegrationEnvironment(true);
		useLiveFolders();
		connectToMB();		// Do a bogus connect to initialize FolderGUIDS correctly for test execution
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		disconnectFromMB();
	}

//	@Test
	public void testAddFolders()
	{
		RevisionCleanupDatabase database = new RevisionCleanupDatabase();
		database.newStatement();
		database.addFolders(getWebService(), BrandFolderGUIDS.PART);
		
		assertEquals("'" + FolderGUIDS.getFolderGuid(BrandFolderGUIDS.PART) + "'", database.getFolders());

		database.newStatement();
		database.addFolders(getWebService(), BrandFolderGUIDS.PART);
		database.addFolders(getWebService(), BrandFolderGUIDS.DESIGN_CONCEPT);

		assertEquals("'" + FolderGUIDS.getFolderGuid(BrandFolderGUIDS.PART) + "', '" + FolderGUIDS.getFolderGuid(BrandFolderGUIDS.DESIGN_CONCEPT) + "'", database.getFolders());

		database.newStatement();
		database.addFolders(getWebService(), BrandFolderGUIDS.PART + ", " + BrandFolderGUIDS.DESIGN_CONCEPT);

		assertEquals("'" + FolderGUIDS.getFolderGuid(BrandFolderGUIDS.PART) + "', '" + FolderGUIDS.getFolderGuid(BrandFolderGUIDS.DESIGN_CONCEPT) + "'", database.getFolders());

		database.newStatement();
		database.addFolders(getWebService(), BrandFolderGUIDS.PART + ",{9F0A70AD-2456-4B0A-970E-B3DCDC004688}");

		assertEquals("'" + FolderGUIDS.getFolderGuid(BrandFolderGUIDS.PART) + "', '" + FolderGUIDS.getFolderGuid(BrandFolderGUIDS.DESIGN_CONCEPT) + "'", database.getFolders());

	}

//	@Test
	public void testSetupStatement()
	{
		RevisionCleanupDatabase database = new RevisionCleanupDatabase();

		database.newStatement();
		database.addFolders(getWebService(), BrandFolderGUIDS.PART);
		database.setupStatementAllAssets("0");
		
		System.out.println("Statement 1 = " + database.getStatement());
		
		database.newStatement();
		database.addFolders(getWebService(), BrandFolderGUIDS.PART);
		database.setupStatementAllAssets("-1");
		
		System.out.println("Statement 2 = " + database.getStatement());

		database.newStatement();
		database.addFolders(getWebService(), BrandFolderGUIDS.PART);
		database.setupStatementForCleanup("-1");
		
		System.out.println("Statement 3 = " + database.getStatement());

	}

//	@Test
	public void testPerformQueryAnyRevisions()
	{
		RevisionCleanupDatabase database = new RevisionCleanupDatabase();

		// Retrieve all Parts - no matter modified date
		database.newStatement();
		database.addFolders(getWebService(), BrandFolderGUIDS.PART);
		database.setupStatementAllAssets("0");

		startTiming();
		List<BaseAsset> cleanupAssets = database.performQuery(-1);
		stopTiming();
		System.out.println("Part count (No modified date restriction, all revisions) = " + cleanupAssets.size());
		
		// Retrieve all Parts - modified date more than 60 days ago
		database.newStatement();
		database.addFolders(getWebService(), BrandFolderGUIDS.PART);
		database.setupStatementAllAssets("-60");

		startTiming();
		cleanupAssets = database.performQuery(-1);
		stopTiming();
		System.out.println("Part count (Modified date restriction of -60, all revisions) = " + cleanupAssets.size());

	}
	
//	@Test
	public void testPerformQueryMultipleRevisions()
	{
		RevisionCleanupDatabase database = new RevisionCleanupDatabase();

		// Retrieve all Parts - no matter modified date
		database.newStatement();
		database.addFolders(getWebService(), BrandFolderGUIDS.PART);
		database.setupStatementForCleanup("0");

		startTiming();
		List<BaseAsset> cleanupAssets = database.performQuery(-1);
		stopTiming();
		System.out.println("Part count (No modified date restriction, multiple revisions) = " + cleanupAssets.size());
		
		// Retrieve all Parts - modified date more than 60 days ago
		database.newStatement();
		database.addFolders(getWebService(), BrandFolderGUIDS.PART);
		database.setupStatementForCleanup("-60");

		startTiming();
		cleanupAssets = database.performQuery(-1);
		stopTiming();
		System.out.println("Part count (Modified date restriction of -60, multiple revisions) = " + cleanupAssets.size());

	}

//	@Test
	public void testCountRevisions()
	{
		RevisionCleanupDatabase database = new RevisionCleanupDatabase();

		// Retrieve all Parts - no matter modified date
		database.newStatement();
		database.addFolders(getWebService(), BrandFolderGUIDS.PART);
		database.setupStatementAllAssets("0");
		List<BaseAsset> assets = database.performQuery(-1);
		
		System.out.println("PART - Asset Count = " + assets.size());
		System.out.println("PART - All Revision count = " + database.getAllRevisionCount(assets));
		System.out.println("PART - Cleanup Revision count = " + database.getCleanupRevisionCount(assets));
	}
	
//	@Test
	public void testDisplayPARTRevisions()
	{
		displayRevisionInformation(BrandFolderGUIDS.PART);
		displayRevisionInformation(BrandFolderGUIDS.DESIGN_CONCEPT);
		displayRevisionInformation(BrandFolderGUIDS.COLOR_SCHEME);
		displayRevisionInformation(BrandFolderGUIDS.COLOR_SEP_PNG);
		displayRevisionInformation(BrandFolderGUIDS.COLOR_SEP_SVG);
		displayRevisionInformation(BrandFolderGUIDS.RENDITION_PNG);
		displayRevisionInformation(BrandFolderGUIDS.RENDITION_SVG);
		displayRevisionInformation(BrandFolderGUIDS.RENDITION_PDF);
		displayRevisionInformation(BrandFolderGUIDS.RENDITION_EPS);
		displayRevisionInformation(BrandFolderGUIDS.MASTER_CDR);
	}

	private void displayRevisionInformation(String folder)
	{
		StringBuffer sb = new StringBuffer(folder + "  ");
		List<BaseAsset> assets = null;
		int allRevisions = 0;
		
		RevisionCleanupDatabase database = new RevisionCleanupDatabase();

//		// Retrieve all assets from folder and all revisions for those assets
//		database.newStatement();
//		database.addFolders(folder);
//		database.setupStatement(false, "0");
//		assets = database.performQuery(-1);		
//		allRevisions = database.getAllRevisionCount(assets);
//		sb.append(assets.size() + ":" + allRevisions + "  ");
//
//		// Retrieve all assets from folder with more than 1 revison and all revisions for those assets
//		database.newStatement();
//		database.addFolders(folder);
//		database.setupStatement(true, "0");
//		assets = database.performQuery(-1);		
//		allRevisions = database.getAllRevisionCount(assets);
//		sb.append(assets.size() + ":" + allRevisions + "  ");

		// Retrieve assets from the folder with more than 1 revision and modified date over 30 days previous
		database.newStatement();
		database.addFolders(getWebService(), folder);
		database.setupStatementForCleanup("-30");
		assets = database.performQuery(-1);		
		allRevisions = database.getCleanupRevisionCount(assets);
		sb.append(assets.size() + ":" + allRevisions + "  ");
		
		// Retrieve assets from the folder with more than 1 revision and modified date over 60 days previous
		database.newStatement();
		database.addFolders(getWebService(), folder);
		database.setupStatementForCleanup("-60");
		assets = database.performQuery(-1);		
		allRevisions = database.getCleanupRevisionCount(assets);
		sb.append(assets.size() + ":" + allRevisions + "  ");

		// Retrieve assets from the folder with more than 1 revision and modified date over 90 days previous
		database.newStatement();
		database.addFolders(getWebService(), folder);
		database.setupStatementForCleanup("-90");
		assets = database.performQuery(-1);		
		allRevisions = database.getCleanupRevisionCount(assets);
		sb.append(assets.size() + ":" + allRevisions + "  ");

		System.out.println(sb.toString());
	}
	
//	@Test
	public void testGetDateRangeFolders()
	{
		RevisionCleanupDatabase database = new RevisionCleanupDatabase();
		RevisionCleanupProperties properties = new RevisionCleanupProperties();

		Map<String, String> dateRangeFolders = database.getDateRangeFolders(properties);
		
		System.out.println("Date Range Folders: \n" + dateRangeFolders.toString());
	}

	@Test
	public void testGetAssetsForCleanup()
	{
		RevisionCleanupDatabase database = new RevisionCleanupDatabase();
		
		List<BaseAsset> cleanupAssets = database.getAssetsForCleanup(getWebService());
		System.out.println("Cleanup Count = " + cleanupAssets.size());
		
		for (BaseAsset ba : cleanupAssets)
		{
			System.out.println(ba.getAssetName() + "   " + ba.getFolderName()   + "   " + ba.getFolderGUID());
		}
	}
	
//	@Test
	public void testPerformRevisionCleanupOnAsset() throws Exception
	{
		RevisionCleanupDatabase database = new RevisionCleanupDatabase();
		
		List<BaseAsset> cleanupAssets = database.getAssetsForCleanup(getWebService());
		System.out.println("Cleanup Count = " + cleanupAssets.size());

		String tempFolderGUID = database.getTempFolderGUID(getWebService());
		if (tempFolderGUID == null)
		{
			return;
		}
		for (BaseAsset ba : cleanupAssets)
		{
			System.out.println(ba.getAssetName() + "   " + ba.getFolderName()   + "   " + ba.getFolderGUID());
			
			boolean cleanupSucceeded = database.performRevisionCleanupOnAsset(getWebService(), ba, tempFolderGUID);
			System.out.println("     " + cleanupSucceeded);
			
			break;		// Only cleanup 1 asset
		}
	}

//	@Test
	public void testPerformCleanupCycle() throws Exception
	{
		RevisionCleanupDatabase database = new RevisionCleanupDatabase();

		database.setPerformUpdate(false);
		database.performCleanupCycle(getWebService());
	}
	
//	@Test
	public void testRunAsThread() throws Exception
	{
		RevisionCleanupDatabase database = new RevisionCleanupDatabase();
		database.setPerformUpdate(false);
		database.start();		// Start the processing

		Thread.sleep(5000);
		
		while(database.isAlive())
		{
			System.out.println(" - SLEEP - ");
			Thread.sleep(5000);
		}

	}

}

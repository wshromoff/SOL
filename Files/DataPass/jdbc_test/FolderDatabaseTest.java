package com.jostens.dam.shared.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.dam.shared.assetclass.BaseAsset;
import com.jostens.dam.shared.folders.FolderGUIDS;
import com.jostens.dam.shared.testing.SharedBaseTest;

public class FolderDatabaseTest extends SharedBaseTest
{
	private static Connection c = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
//		setProductionEnvironment(true);
//		setIntegrationEnvironment(true);
		JDBCConnection.setErrorsToStdout(true);

		connectToMB();
		
		c = JDBCConnection.getMediabinConnection();
		
    	// Initialize any BrandStatements
		SharedStatements statements = new SharedStatements();
		statements.initializeStatements();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		JDBCConnection.close(c);

		disconnectFromMB();		
	}

//	@Test
	public void testGetMediaDatabaseGUID() throws Exception
	{
		FolderDatabase fdb = new FolderDatabase();
		startTiming();
		String guid = fdb.getMediaDatabaseGUID(c);
		stopTiming("Attempt 1");
		System.out.println("GUID = " + guid);
		startTiming();
		guid = fdb.getMediaDatabaseGUID(c);
		stopTiming("Attempt 2");
		System.out.println("GUID = " + guid);
	}

//	@Test
	public void testGetDeletedItemsGUID() throws Exception
	{
		FolderDatabase fdb = new FolderDatabase();
		startTiming();
		String guid = fdb.getDeletedItemsGUID(c);
		stopTiming("Attempt 1");
		System.out.println("GUID = " + guid);
		startTiming();
		guid = fdb.getDeletedItemsGUID(c);
		stopTiming("Attempt 2");
		System.out.println("GUID = " + guid);
	}

//	@Test
	public void testGetCompleteFolderTree()
	{
		// Need to find guid for the top level marketing folder test
		String TEST_PATH = "/Media Database/Development/JEMConnect_Test/JC_Test1";
		String folderGUID = FolderGUIDS.findGuidForFolderPath(getWebService(), TEST_PATH);
		if (folderGUID == null)
		{
			System.out.println("Cannot find a GUID for path: " + TEST_PATH);
			return;
		}

		System.out.println("Folder GUID = " + folderGUID);

		
		FolderDatabase database = new FolderDatabase();
		startTiming();
		Map<String, List<BaseAsset>> foldersByParent = database.getCompleteFolderTree(c, folderGUID);
		stopTiming();

		System.out.println("Number of parent folders found = " + foldersByParent.size());
        Iterator<Map.Entry<String, List<BaseAsset>>> iter = foldersByParent.entrySet().iterator();
        while (iter.hasNext())
        {
        	Map.Entry<String, List<BaseAsset>> entry = iter.next();
        	String parentGUID = entry.getKey();
        	List<BaseAsset> childFolders = entry.getValue();
        	
        	System.out.println(parentGUID);
        	for (BaseAsset ba : childFolders)
        	{
        		System.out.println("    " + ba.getAssetName() + " : " + ba.getAssetId() + " : " + ba.getFolderGUID());
        	}
		}
	}

//	@Test
	public void testGetAllAssetsForFolderTree()
	{
		// Need to find guid for the top level marketing folder test
		String TEST_PATH = "/Media Database/Development/JEMConnect_Test/JC_Test1";
		String folderGUID = FolderGUIDS.findGuidForFolderPath(getWebService(), TEST_PATH);
		if (folderGUID == null)
		{
			System.out.println("Cannot find a GUID for path: " + TEST_PATH);
			return;
		}

		System.out.println("Folder GUID = " + folderGUID);
		
		FolderDatabase database = new FolderDatabase();
		startTiming();
//		Map<String, List<BaseAsset>> assetsByParent = database.getAllAssetsForFolderTree(c, folderGUID, "Done.txt");
		Map<String, List<BaseAsset>> assetsByParent = database.getAllAssetsForFolderTree(c, folderGUID);
		stopTiming();

		System.out.println("Number of assets found = " + assetsByParent.size());
        Iterator<Map.Entry<String, List<BaseAsset>>> iter = assetsByParent.entrySet().iterator();
        while (iter.hasNext())
        {
        	Map.Entry<String, List<BaseAsset>> entry = iter.next();
        	String parentGUID = entry.getKey();
        	List<BaseAsset> assets = entry.getValue();
        	
        	System.out.println(parentGUID);
        	for (BaseAsset ba : assets)
        	{
        		System.out.println("    " + ba.getAssetName() + " : " + ba.getAssetId());
        	}
		}
	}

//	@Test
	public void testAreParentChild()
	{
		// Need to find guid for the top level marketing folder test
		String fg1 = FolderGUIDS.findGuidForFolderPath(getWebService(), "/Media Database/Development/JEMConnect_Test/JC_Test1");
		String fg2 = FolderGUIDS.findGuidForFolderPath(getWebService(), "/Media Database/Development/JEMConnect_Test/JC_Test1/Test");
		String fg3 = FolderGUIDS.findGuidForFolderPath(getWebService(), "/Media Database/Development/JEMConnect_Test/JC_Test2");
		
		assertNotNull(fg1);
		assertNotNull(fg2);
		assertNotNull(fg3);
		
		FolderDatabase database = new FolderDatabase();
		startTiming();
		assertTrue(database.areParentChild(c, fg1, fg2));
		assertFalse(database.areParentChild(c, fg2, fg3));
		assertFalse(database.areParentChild(c, fg1, fg3));
		stopTiming();
	}
	
	@Test
	public void testGetParentFolderGUID() throws RemoteException
	{
		// Need to find guid for the top level marketing folder test
		String folderGUID = FolderGUIDS.findGuidForFolderPath(getWebService(), "/Media Database/Development/JEMConnect_Test/JC_Test1");
		// Find guid for an asset in the folder
		BaseAsset testAsset = getBaseAssetInFolder("/Media Database/Development/JEMConnect_Test/JC_Test1", "BE043912");
		
		// Use the SQL method to find the parent GUID which should match folderGUID
		FolderDatabase database = new FolderDatabase();
		String folderGUIDFromSQL = database.getParentFolderGUID(c, testAsset.getAssetId());
		
		assertEquals(folderGUID, folderGUIDFromSQL);
	}

}

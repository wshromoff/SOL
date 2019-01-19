package com.jostens.dam.shared.jdbc;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.dam.shared.assetclass.BaseAsset;
import com.jostens.dam.shared.assetclass.MetadataGUIDS;
import com.jostens.dam.shared.folders.FolderGUIDS;
import com.jostens.dam.shared.testing.SharedBaseTest;

public class SharedDatabaseTest extends SharedBaseTest
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
		
		// Initialize any Statements
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
	public void testGetNoAssetClassAssets()
	{
		String folderGUID1 = FolderGUIDS.findGuidForFolderPath(getWebService(), "/Media Database/Development/riehlek/_yearbook/Test1");
		assertNotNull(folderGUID1);
		String folderGUID2 = FolderGUIDS.findGuidForFolderPath(getWebService(), "/Media Database/Development/riehlek/_yearbook/Test2");
		assertNotNull(folderGUID2);
		
		SharedDatabase sharedDatabase = new SharedDatabase();

		startTiming();
		List<BaseAsset> assets = sharedDatabase.getAssetsMissingMetadata(c, MetadataGUIDS.ASSET_CLASS, folderGUID1 + "," + folderGUID2, null);
		stopTiming();
		System.out.println("Assets missing asset class: " + assets.size());		
		for (BaseAsset ba : assets)
		{
			System.out.println(ba.getAssetName() + "   :    " + ba.getAssetId());
		}
	}
	
	@Test
	public void testGetNoAssetClassAssetsTimeRestriction()
	{
		String folderGUID1 = FolderGUIDS.findGuidForFolderPath(getWebService(), "/Media Database/Development/riehlek/_yearbook/Test1");
		assertNotNull(folderGUID1);
		String folderGUID2 = FolderGUIDS.findGuidForFolderPath(getWebService(), "/Media Database/Development/riehlek/_yearbook/Test2");
		assertNotNull(folderGUID2);
		
		SharedDatabase sharedDatabase = new SharedDatabase();

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -5);
		startTiming();
		List<BaseAsset> assets = sharedDatabase.getAssetsMissingMetadata(c, MetadataGUIDS.ASSET_CLASS, folderGUID1 + "," + folderGUID2, cal);
		stopTiming();
		System.out.println("Assets missing asset class: " + assets.size());		
		for (BaseAsset ba : assets)
		{
			System.out.println(ba.getAssetName() + "   :    " + ba.getAssetId());
		}
	}

}

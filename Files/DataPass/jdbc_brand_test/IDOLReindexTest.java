package com.jostens.dam.brand.jdbc;

import java.sql.Connection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.dam.brand.folders.BrandFolderGUIDS;
import com.jostens.dam.brand.testing.BrandBaseTest;
import com.jostens.dam.shared.folders.FolderGUIDS;
import com.jostens.dam.shared.folders.SharedFolderPath;
import com.jostens.dam.shared.jdbc.JDBCConnection;

public class IDOLReindexTest extends BrandBaseTest
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		SharedFolderPath.setUnitTest(true);
//		setProductionEnvironment(true);
		useLiveFolders();
		connectToMB();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		disconnectFromMB();
	}

	@Test
	public void testGetIndexNeededCount()
	{

		Connection c = JDBCConnection.getMediabinConnection();
		
		IDOLReindex reindex = new IDOLReindex();
		
		int rowCount = reindex.getIndexNeededCount(c);
		System.out.println("Reindex needed = " + rowCount);
		
		JDBCConnection.close(c);
	}

//	@Test
	public void testDeleteIDOLTask()
	{

		Connection c = JDBCConnection.getMediabinConnection();
		
		IDOLReindex reindex = new IDOLReindex();
		
		reindex.deleteIDOLTask(c);
				
		JDBCConnection.close(c);
	}

//	@Test
	public void testInsertIDOLTask()
	{

		Connection c = JDBCConnection.getMediabinConnection();
		
		IDOLReindex reindex = new IDOLReindex();
		
		reindex.insertIDOLTask(c);
				
		JDBCConnection.close(c);
	}

//	@Test
	public void testMarkAssetsToIndex()
	{

		Connection c = JDBCConnection.getMediabinConnection();
		
		IDOLReindex reindex = new IDOLReindex();

		String folderGUID = FolderGUIDS.getFolderGuid(BrandFolderGUIDS.PART);
		System.out.println("GUID=" + folderGUID);
		reindex.markAssetsToReIndex(c, folderGUID);
				
		JDBCConnection.close(c);
	}

}

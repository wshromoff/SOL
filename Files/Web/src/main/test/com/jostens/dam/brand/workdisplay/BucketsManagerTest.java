package com.jostens.dam.brand.workdisplay;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.dam.brand.testing.BrandBaseTest;

public class BucketsManagerTest extends BrandBaseTest
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
//		setProductionEnvironment(true);
		useLiveFolders();
		connectToMB();		// Do a bogus connect to initialize FolderGUIDS correctly for test execution
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		disconnectFromMB();
	}

	@Test
	public void testGetBucketRow()
	{
		BucketsManager bm = new BucketsManager();
		DisplayAsset da = new DisplayAsset();
		
		da.setStatusAutomation("FS_COMPLETE MD_COMPLETE ");		
		assertEquals(3, bm.getBucketRow(da));
		da.setStatusAutomation("FS_COMPLETE MD_COMPLETE DR_COMPLETE PR_NEW GO ");		
		assertEquals(7, bm.getBucketRow(da));
		da.setStatusAutomation("FS_COMPLETE MD_COMPLETE AI_COMPLETE DR_POSTINGEST DR_CATALOG DR_DONE_CATALOG ");		
		assertEquals(5, bm.getBucketRow(da));
		da.setStatusAutomation("FS_COMPLETE MD_COMPLETE DR_COMPLETE PR_NEW GO PR_PROCESSING ");		
		assertEquals(8, bm.getBucketRow(da));
		da.setStatusAutomation("FS_COMPLETE MD_COMPLETE AI_COMPLETE DR_POSTINGEST DR_CATALOG DR_INUSE_CATALOG ");		
		assertEquals(5, bm.getBucketRow(da));
	}

	
	// FS_COMPLETE MD_COMPLETE AI_COMPLETE DR_POSTINGEST DR_CATALOG DR_INUSE_CATALOG 
	
	// FS_COMPLETE MD_COMPLETE AI_COMPLETE DR_POSTINGEST DR_CATALOG DR_DONE_CATALOG 
	
	// FS_COMPLETE MD_COMPLETE DR_COMPLETE PR_NEW GO PR_PROCESSING 	
}

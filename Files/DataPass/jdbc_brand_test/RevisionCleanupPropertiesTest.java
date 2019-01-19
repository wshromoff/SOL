package com.jostens.dam.brand.jdbc;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.dam.brand.testing.BrandBaseTest;

public class RevisionCleanupPropertiesTest extends BrandBaseTest
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
//		setProductionEnvironment(true);
		useLiveFolders();
		connectToMB();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		disconnectFromMB();
	}


//	@Test
	public void testControlOptions()
	{
		RevisionCleanupProperties properties = new RevisionCleanupProperties();
		
		System.out.println("Revision Cleanup Enabled: " + properties.isRevisionCleanupActive());
		System.out.println("Cleanup Maximum: " + properties.getMaximumToCleanup());
		System.out.println("Folders To Clean: " + properties.getFoldersToClean().toString());
		
	}

	@Test
	public void testGetFolderDateRanges()
	{
		RevisionCleanupProperties properties = new RevisionCleanupProperties();
		
		for (String folder : properties.getFoldersToClean())
		{
			System.out.println(folder + " : " + properties.getFolderDateRange(folder));
		}
	}
}

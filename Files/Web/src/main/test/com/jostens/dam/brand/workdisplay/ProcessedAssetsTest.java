package com.jostens.dam.brand.workdisplay;

import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.dam.brand.testing.BrandBaseTest;

public class ProcessedAssetsTest extends BrandBaseTest
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
	public void test()
	{
		fail("Not yet implemented");
	}

}

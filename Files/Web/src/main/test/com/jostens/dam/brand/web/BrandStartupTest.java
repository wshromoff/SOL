package com.jostens.dam.brand.web;

import org.junit.Test;

import com.jostens.dam.brand.testing.BrandBaseTest;
import com.jostens.dam.brand.workdisplay.CurrentAssets;
import com.jostens.dam.shared.control.BaseStartup;

public class BrandStartupTest extends BrandBaseTest
{
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception
//	{
////		setProductionEnvironment(true);
//		useLiveFolders();
//		connectToMB();		// Do a bogus connect to initialize FolderGUIDS correctly for test execution
//	}
//
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception
//	{
//		disconnectFromMB();
//	}

	/**
	 * To use the following test, DO NOT use the the BeforeClass and AfterClass methods above.
	 * The environment startup is fully handled within the test method.
	 */
//	@Test
	public void testStartStopWorkDisplay()
	{
		BaseStartup startup = new BrandStartup();
		// The following set startup properties which take precedence over
		// anything found in WebStartup.properties
		//    *** NOTE *** This test will start work display ONLY and display the work display HTML after one cycle
		//					The environment is also specified by a value of: DEV INT PRD
		startup.addUnitTestProperty(BaseStartup.ENVIRONMENT_SELECT, "DEV");
		startup.addUnitTestProperty(BrandStartup.START_WORKDISPLAY, "true");
		startup.addUnitTestProperty(BrandStartup.START_AUTOMATIONS, "false");
		startup.start();
		
		try
		{
			Thread.sleep(75000);
		} catch (InterruptedException e) {}
		
		startup.contextBeingDestroyed();
		
		String currentAssetHTML = HTMLHelper.getCachedHTML(CurrentAssets.CURRENT_ASSETS_HTML);
		System.out.println(currentAssetHTML);

	}

	/**
	 * To use the following test, DO NOT use the the BeforeClass and AfterClass methods above.
	 * The environment startup is fully handled within the test method.
	 */
	@Test
	public void testStartStopWorkAutomations()
	{
		BaseStartup startup = new BrandStartup();
		// The following set startup properties which take precedence over
		// anything found in WebStartup.properties
		//    *** NOTE *** This test will start automation's ONLY and display the work display HTML after one cycle
		//					The environment is also specified by a value of: DEV INT PRD
		startup.addUnitTestProperty(BaseStartup.ENVIRONMENT_SELECT, "DEV");
		startup.addUnitTestProperty(BrandStartup.START_WORKDISPLAY, "false");
		startup.addUnitTestProperty(BrandStartup.START_AUTOMATIONS, "true");
		startup.start();
		
		try
		{
			Thread.sleep(250000);
		} catch (InterruptedException e) {}
		
		startup.contextBeingDestroyed();
	}

//	@Test
	public void testReadStartupProperties() throws Exception
	{
		BrandStartup startup = new BrandStartup();
		startup.readStartupProperties();
		
		System.out.println("Startup Properties: " + startup.getStartupProperties());
		
	}
}

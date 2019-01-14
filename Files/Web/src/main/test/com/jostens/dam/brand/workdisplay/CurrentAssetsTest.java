package com.jostens.dam.brand.workdisplay;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.dam.brand.testing.BrandBaseTest;
import com.jostens.dam.brand.web.HTMLHelper;
import com.jostens.dam.shared.common.OutputFormatters;

public class CurrentAssetsTest extends BrandBaseTest
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

//	@Test
	public void testPerformThreadAction()
	{
		CurrentAssets ca = new CurrentAssets();
		ca.performThreadAction();
	}

//	@Test
	public void testRunningasThread() throws InterruptedException
	{
		CurrentAssets ca = new CurrentAssets();
		ca.start();
		
		Thread.sleep(111000);

		System.out.println("Sending interrupt at: " + OutputFormatters.getCurrentDateFormat6());
		ca.interrupt();
		
		while (ca.isAlive())
		{
			System.out.println("Thread is still ALIVE");
			Thread.sleep(1000);
		}
		System.out.println("Thread has stopped at: " + OutputFormatters.getCurrentDateFormat6());

	}

	@Test
	public void testGettingCachedHTML() throws InterruptedException
	{
		CurrentAssets ca = new CurrentAssets();
		ca.start();
		
		Thread.sleep(47000);
		
		String cachedHTML = HTMLHelper.getCachedHTML(CurrentAssets.CURRENT_ASSETS_HTML);
		
		System.out.println("Cache size = " + cachedHTML.length());
		System.out.println("> " + cachedHTML);

		System.out.println("\n\nSending interrupt at: " + OutputFormatters.getCurrentDateFormat6());
		ca.interrupt();
		
		while (ca.isAlive())
		{
			System.out.println("Thread is still ALIVE");
			Thread.sleep(1000);
		}
		System.out.println("Thread has stopped at: " + OutputFormatters.getCurrentDateFormat6());

	}

}

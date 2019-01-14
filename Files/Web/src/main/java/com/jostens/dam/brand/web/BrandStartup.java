package com.jostens.dam.brand.web;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.jostens.dam.brand.common.BrandAutomationManager;
import com.jostens.dam.brand.common.PantoneColorsMap;
import com.jostens.dam.brand.folders.BrandFolderGUIDS;
import com.jostens.dam.brand.resource.BrandResourceController;
import com.jostens.dam.brand.workdisplay.CompletedAssets;
import com.jostens.dam.brand.workdisplay.CurrentAssets;
import com.jostens.dam.shared.automations.BaseThread;
import com.jostens.dam.shared.common.MediaBinConnectionPool;
import com.jostens.dam.shared.control.BaseEnvironment;
import com.jostens.dam.shared.control.BaseStartup;
import com.jostens.dam.shared.resource.ResourceController;
import com.viragemediabin.www.VirageMediaBinServerSoap;

public class BrandStartup extends BaseStartup
{
	// Define constants that represent web applications that can be selectively started.  These values
	// should be found WebStartup.properties as either a true or false.  If the value for an application start
	// is not found, it's assumed to be false and so will not be started.
	public static String START_WORKDISPLAY = "StartWorkdisplay";
	public static String START_AUTOMATIONS = "StartAutomations";
	
	private boolean workDisplayActive = false;
	private boolean automationsActive = false;
	
	// Hold a List of BaseThread objects that will together build the data needed for a complete WorkDisplay application
	private List<BaseThread> workdisplayThreads = new ArrayList<BaseThread>();
	// The manager application that controls the automation's
	private static BrandAutomationManager automationManager = null;
	
	@Override
	public boolean startWebApplication()
	{
		
		// Check web startup properties to determine which applications need to start
		workDisplayActive = shouldStartApplication(START_WORKDISPLAY);
		automationsActive = shouldStartApplication(START_AUTOMATIONS);
		
		if (workDisplayActive)
		{
			System.out.println("\nSTARTING WORKDISPLAY\n");
			boolean workDisplayStarted = startWorkDisplay();
			if (!workDisplayStarted)
			{
				System.out.println("Error starting workdisplay application");
				return false;
			}
		}
		
		if (automationsActive)
		{
			System.out.println("\nSTARTING AUTOMATIONS\n");
			boolean automationsStarted = startAutomations();
			if (!automationsStarted)
			{
				System.out.println("Error starting Automations application");
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean stopWebApplication()
	{
		if (workDisplayActive)
		{
			System.out.println("\nSTOPPING WORKDISPLAY\n");
			boolean workDisplayStoppped = stopWorkDisplay();
			if (!workDisplayStoppped)
			{
				System.out.println("Error stopping workdisplay application");
				return false;
			}
		}

		if (automationsActive)
		{
			System.out.println("\nSTOPPING AUTOMATIONS\n");
			boolean automationsStopped = stopAutomations();
			if (!automationsStopped)
			{
				System.out.println("Error stopping automations application");
				return false;
			}
		}

		return true;
	}

	@Override
	public String getStartupPropertiesPath()
	{
		return "/com/jostens/dam/brand/web/WebStartup.properties";
		
	}

	public boolean startWorkDisplay()
	{
		// Create all the Threaded objects and add to threads list, then start them all at once
		workdisplayThreads.add(new CurrentAssets());
		workdisplayThreads.add(new CompletedAssets());
		
		// Start them
		for (BaseThread thread : workdisplayThreads)
		{
			thread.start();
		}

		return true;
	}

	public boolean stopWorkDisplay()
	{
		// Interrupt all the threads
		for (BaseThread thread : workdisplayThreads)
		{
			thread.interrupt();
		}
		
		// Wait until they are all no longer alive
		boolean allStopped = false;
		for (int i = 0; i < 50; i++)
		{
			allStopped = true;
			try
			{
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

			for (BaseThread thread : workdisplayThreads)
			{
				if (thread.isAlive())
				{
					allStopped = false;
					break;
				}
			}
			if (allStopped)
			{
				return true;
			}
		}
		return false;
	}

	public boolean startAutomations()
	{
		// Need to get a web service connection to continue startup
		VirageMediaBinServerSoap ws = MediaBinConnectionPool.getNonPoolConnection();
		
		// Using this connection to initialize FolderGUIDS
		BrandFolderGUIDS folderGUIDS = new BrandFolderGUIDS();
		folderGUIDS.initialize(ws);
		
		try
		{
			ws.MBSession_Logoff();
		} catch (RemoteException e) {}

		List<String> startupMessages = folderGUIDS.getStartupMessages();

		// Perform additional FolderGUIDS startup functionality
		// Initialize keywords - Populate the keyword list for use on cataloger form
//		KeywordsManager.initializeKeywords();
//		startupMessages.add("Keywords found: " + KeywordsManager.getKeywords().size());

		// Initialize Pantone colors map
		PantoneColorsMap.initializeColors();

		// Call the AutomationManager to start all automations
		BrandStartup.automationManager = new BrandAutomationManager();
		BrandStartup.automationManager.startResource();

		ResourceController resourceController = new BrandResourceController();
		startupMessages.addAll(resourceController.getVersionInformation());
		startupMessages.addAll(BrandStartup.automationManager.getStartupMessages());
		
		for (String msg : startupMessages)
		{
			System.out.println(msg);
		}
		
//		out.flush();
//		out.close();

		return true;
	}

	public boolean stopAutomations()
	{
		BrandStartup.automationManager.stopResource();
		
		return true;
	}

	public static BrandAutomationManager getAutomationManager()
	{
		return automationManager;
	}

	@Override
	public BaseEnvironment getEnvironment()
	{
		return new BrandEnvironment();
	}

//	public void readStartupProperties() throws IOException
//	{
//		Properties props = new Properties();
//		props.load(this.getClass().getResourceAsStream("/com/jostens/dam/brand/web/WebStartup.properties"));
//		System.out.println(" SIZE = " + props.size());
//	}
}

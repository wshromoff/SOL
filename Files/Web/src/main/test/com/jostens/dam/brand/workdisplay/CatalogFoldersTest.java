package com.jostens.dam.brand.workdisplay;

import java.rmi.RemoteException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.dam.brand.testing.BrandBaseTest;
import com.jostens.dam.shared.common.Folder;

public class CatalogFoldersTest extends BrandBaseTest
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
	public void testSetupRootFolders() throws RemoteException
	{
		CatalogFolders catalogFolders = new CatalogFolders();
		startTiming();
		catalogFolders.setupRootFolders(getWebService());
		stopTiming();
		
		// Loop through the found folders
		System.out.println("Total Root Folders Found = " + CatalogFolders.getRootFolders().size());
		for (Folder folder : CatalogFolders.getRootFolders())
		{
			System.out.println(folder.getPath() + " : " + folder.getName() + " : " + folder.getGUID());
		}
	}

//	@Test
	public void testSetupChildFolders() throws RemoteException
	{
		CatalogFolders catalogFolders = new CatalogFolders();
		catalogFolders.setupRootFolders(getWebService());
		startTiming();
		for (Folder folder : CatalogFolders.getRootFolders())
		{
			catalogFolders.setupChildFolders(getWebService(), folder);
		}
		stopTiming();
		
		// Loop through the found folders
		for (Folder folder : CatalogFolders.getRootFolders())
		{
			System.out.println(folder.getPath() + " : " + folder.getChildFolders().size());
		}
	}

	@Test
	public void testInitializeCatalogFolders() throws RemoteException
	{
		CatalogFolders catalogFolders = new CatalogFolders();
		startTiming();
		catalogFolders.initializeCatalogFolders(getWebService());
		stopTiming();
		
		// Loop through the found folders
		for (Folder folder : CatalogFolders.getRootFolders())
		{
			System.out.println(folder.getPath() + " : " + folder.getName() + " : " + folder.getChildrenAsString());
		}
	}

}

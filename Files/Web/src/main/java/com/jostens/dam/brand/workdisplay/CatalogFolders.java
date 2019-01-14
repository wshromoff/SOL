package com.jostens.dam.brand.workdisplay;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jostens.dam.brand.folders.BrandFolderGUIDS;
import com.jostens.dam.brand.helpers.FolderHelper;
import com.jostens.dam.shared.common.Folder;
import com.viragemediabin.www.VirageMediaBinServerSoap;

/**
 * Manage catalog folders / child folders for usage by the monitor application
 */
public class CatalogFolders
{

	// Root folders are the parents of artist folders that hold asset packages populated from CorelDraw with a save to JEMM.
	// Only one level down search is performed, so that search is non-recursive.  This folder list is generated once.
	private static List<Folder> rootFolders = new ArrayList<Folder>();

	/**
	 * Find the 3 root folders for the new, replace and revise catalog folders.  These catalog folders contains the assets
	 * that are monitored by this demo project.
	 */
	public void setupRootFolders(VirageMediaBinServerSoap ws) throws RemoteException
	{
		// Clear any previous root folders
		CatalogFolders.rootFolders.clear();
		// Add the 3 catalog root folders
		CatalogFolders.rootFolders.add(FolderHelper.getFolderForPath(ws, BrandFolderGUIDS.getPathForMapping(BrandFolderGUIDS.CATALOG_NEW_BRAND)));
		CatalogFolders.rootFolders.add(FolderHelper.getFolderForPath(ws, BrandFolderGUIDS.getPathForMapping(BrandFolderGUIDS.CATALOG_REPLACE)));
		CatalogFolders.rootFolders.add(FolderHelper.getFolderForPath(ws, BrandFolderGUIDS.getPathForMapping(BrandFolderGUIDS.CATALOG_REVISE)));
	}
	
	/**
	 * For the specified root folder, find all child folders which are the artist folders that their work is
	 * saved to.
	 */
	public void setupChildFolders(VirageMediaBinServerSoap ws, Folder rootFolder) throws RemoteException
	{
		FolderHelper.findChildFolders(ws, rootFolder, false);
		// Need to remove folders that are project or temporary named
		// Remove any project or temporary folders from child folders list
		Iterator<Folder> iter = rootFolder.getChildFolders().iterator();
		while (iter.hasNext())
		{
			Folder childFolder = iter.next();
			String childFolderName = childFolder.getName();
			if (childFolderName == null || childFolderName.length() == 0)
			{
				iter.remove();
				continue;
			}
			if (childFolderName.startsWith("_"))
			{	// Invalid if name begins with _, these have typically been temporary or invalid asset folders
				iter.remove();
				continue;
			}
			if (childFolderName.startsWith("©"))
			{	// Invalid if name begins with ©, these have typically been project related folders
				iter.remove();
				continue;
			}
		}
		FolderHelper.getChildFolderGUIDS(rootFolder);
	}
	
	/**
	 * Setup catalog Folder objects that include child folders.
	 */
	public void initializeCatalogFolders(VirageMediaBinServerSoap ws) throws RemoteException
	{
		setupRootFolders(ws);
		
		for (Folder rootFolder : CatalogFolders.rootFolders)
		{
			setupChildFolders(ws, rootFolder);
		}
	}

	public static List<Folder> getRootFolders()
	{
		return CatalogFolders.rootFolders;
	}

	
}

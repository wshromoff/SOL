package com.jostens.dam.shared.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jostens.dam.shared.assetclass.BaseAsset;
import com.jostens.dam.shared.common.ExceptionHelper;

/**
 * This class will have methods that perform common folder functionality that
 * needs to occur using SQL.
 *
 * Some data is reused a lot and some values might be cached.
 */
public class FolderDatabase
{
	// Static to cache values that will be used frequently
	private static String mediaDatabaseGUID = null;
	private static String deletedItemsGUID = null;

	/**
	 * Return the GUID of the 'Media Database' folder which is the top level folder for all MB installs.
	 * 
	 * This class will cache the value so it isn't retrieved over and over.
	 */
	public String getMediaDatabaseGUID(Connection c) throws Exception
	{
		if (mediaDatabaseGUID != null)
		{
			return mediaDatabaseGUID;
		}
		// First step is to find the top most folder GUID, the is the @Media Database folder
		String stmt = Statements.getStatement(SharedStatements.MEDIA_DATABASE_GUID);
		StatementProcessor processor = new StatementProcessor(c, stmt);
		ResultSet rs = processor.getResultSet();
		boolean validRow = rs.next();
		if (!validRow)
		{
			return null;
		}
		mediaDatabaseGUID = rs.getString(1);			
		processor.closeStatement();

		return mediaDatabaseGUID;
	}
	
	/**
	 * Return the GUID of the 'Deleted Items' folder which is where deleted items are stored until cleanup and this is consistent for all MB installs.
	 * 
	 * This class will cache the value so it isn't retrieved over and over.
	 */
	public String getDeletedItemsGUID(Connection c) throws Exception
	{
		if (deletedItemsGUID != null)
		{
			return deletedItemsGUID;
		}
		// First step is to find the top most folder GUID, the is the @Media Database folder
		String stmt = Statements.getStatement(SharedStatements.DELETED_ITEMS_GUID);
		StatementProcessor processor = new StatementProcessor(c, stmt);
		ResultSet rs = processor.getResultSet();
		boolean validRow = rs.next();
		if (!validRow)
		{
			return null;
		}
		deletedItemsGUID = rs.getString(1);			
		processor.closeStatement();

		return deletedItemsGUID;
	}

	/**
	 * Return a complete folder tree starting from a top level folder.
	 * 
	 * This method will return a Map where the KEY is the GUID of a folder in the tree that
	 * has children folders.  The value with be a List of BaseAsset objects that represent
	 * the child folders with supplied folder name, guid and parent folder guid.
	 * 
	 * The first entry in the returned Map will be the root folder with a single asset that is
	 * the folder name, folder guid and parent folder guid for the parent folder.  In this case the
	 * asset ID and parent ID will match.  This entry can be used to assure the topmost folder 
	 * guid is always found first.
	 * 
	 * Assumed a caller will eventually build a FolderTree of these objects in whatever form
	 * is needed for the usage.
	 */
	public Map<String, List<BaseAsset>> getCompleteFolderTree(Connection c, String rootFolderGUID)
	{
		Map<String, List<BaseAsset>> returnFolders = new LinkedHashMap<String, List<BaseAsset>>();

		try
		{
			String stmt = Statements.getStatement(SharedStatements.FIND_COMPLETE_FOLDER_TREE);
			// Some replacements are needed
			stmt = stmt.replace("[PARENTID_GUID]", rootFolderGUID);

			String workingParentGUID = "ROOT";
			List<BaseAsset> children = new ArrayList<BaseAsset>();
			StatementProcessor processor = new StatementProcessor(c, stmt);
			ResultSet rs = processor.getResultSet();
			while (rs.next())
			{
				// SQL statement returns parent folder GUID, current folder GUID, current folder name
				String parentGUID = rs.getString(1);
				String guid = rs.getString(2);
				String name = rs.getString(3);
				// Build a BaseAsset to hold above information
				BaseAsset ba = new BaseAsset(name, guid);
				ba.setFolderGUID(parentGUID);

				// Handle root case specially
				if ("ROOT".equals(workingParentGUID))
				{
					children.add(ba);
					returnFolders.put(workingParentGUID, children);
					children = new ArrayList<BaseAsset>();
					workingParentGUID = parentGUID;
					continue;
				}
				if (!parentGUID.equals(workingParentGUID))
				{	// New parent GUID and not the first one
					returnFolders.put(workingParentGUID, children);
					children = new ArrayList<BaseAsset>();
					workingParentGUID = parentGUID;
				}
				
				children.add(ba);
			}
			returnFolders.put(workingParentGUID, children);		// Add final parent GUID

			rs.close();
			processor.closeStatement();
			
		} catch (Exception e)
		{
			System.out.println("EXCEPTION!!!!!");
			ExceptionHelper.logExceptionToFile("getFoldersByParent", e);
			returnFolders.clear();
		}		
		return returnFolders;
	}

	/**
	 * Return all assets found for a complete folder tree starting from a top level folder.
	 * Ability to pass in a name restriction to reduce assets.  If no restriction on name
	 * call the 2 parameter method.
	 * 
	 * This method will return a Map where the KEY is a parent folder and then the Value
	 * is a List of BaseAssets which would be the assets for that folder.
	 * 
	 * Use the valid attribute of BaseAsset to indicate if the asset is original or a shortcut.  This attribute will
	 * be true if original and false if shortcut.
	 * 
	 * Assumed a caller will eventually populate a FolderTree with these objects.
	 */
	public Map<String, List<BaseAsset>> getAllAssetsForFolderTree(Connection c, String rootFolderGUID)
	{
		return getAllAssetsForFolderTree(c, rootFolderGUID, null);
	}
	
	public Map<String, List<BaseAsset>> getAllAssetsForFolderTree(Connection c, String rootFolderGUID, String nameRestriction)
	{
		Map<String, List<BaseAsset>> returnAssets = new LinkedHashMap<String, List<BaseAsset>>();

		try
		{
			String stmt = Statements.getStatement(SharedStatements.FIND_ALL_ASSETS_FOR_FOLDER_TREE);
			
			// Some replacements are needed
			stmt = stmt.replace("[PARENTID_GUID]", rootFolderGUID);
			if (nameRestriction == null)
			{
				stmt = stmt.replace("[NAME_RESTRICTED]", "");				
			}
			else
			{
				stmt = stmt.replace("[NAME_RESTRICTED]", "and ar.name = '" + nameRestriction + "'");
			}

			String workingParentGUID = "";
			List<BaseAsset> children = new ArrayList<BaseAsset>();
			StatementProcessor processor = new StatementProcessor(c, stmt);
			ResultSet rs = processor.getResultSet();
			while (rs.next())
			{
				String guid = rs.getString(1);
				String name = rs.getString(2);
				String assetGUID = rs.getString(3);
				int isOriginal = rs.getInt(4);
				// Build a BaseAsset to hold above information
				BaseAsset ba = new BaseAsset(name, assetGUID);
				// If asset is not original, set valid on the BaseAsset to false.  This could be helpful
				// information for further processing and reduce some overhead
				if (isOriginal == 0)
				{
					// MB marks isOriginal = 1 for original and = 0 for shortcut.  Found a shortcut, set valid to false
					ba.setValid(false);
				}

				if (!guid.equals(workingParentGUID))
				{	// New parent GUID and not the first one
					if (!"".equals(workingParentGUID))
					{
						returnAssets.put(workingParentGUID, children);
					}
					children = new ArrayList<BaseAsset>();
					workingParentGUID = guid;
				}
				
				children.add(ba);
			}
			if (!"".equals(workingParentGUID))
			{
				returnAssets.put(workingParentGUID, children);	// Add final parent GUID
			}

			rs.close();
			processor.closeStatement();
			
		} catch (Exception e)
		{
			System.out.println("EXCEPTION!!!!!");
			ExceptionHelper.logExceptionToFile("getFoldersByParent", e);
			returnAssets.clear();
		}

		
		return returnAssets;
	}

	/**
	 * Return a boolean if a row in the container tree table can be found indicating there is
	 * a parent/child relationship in the folder hierarchy.  THIS DOES NOT have to 
	 * be an immediate relationship 
	 */
	public boolean areParentChild(Connection c, String parentFolderGUID, String childFolderGUID)
	{
		try
		{

			// Get the SQL statement and perform replacements based on parameters 
			String stmt = Statements.getStatement(SharedStatements.FIND_CONTAINERTREE_COUNT);
			// Some replacements are needed
			stmt = stmt.replace("[PARENT_FOLDER_GUID]", parentFolderGUID);
			stmt = stmt.replace("[CHILD_FOLDER_GUID]", childFolderGUID);
	
			// If the result set has a valid row, then there is a parent/child relations so return true
			StatementProcessor processor = new StatementProcessor(c, stmt);
			ResultSet rs = processor.getResultSet();
			rs.next();
			int rowCount = rs.getInt(1);
			rs.close();
			processor.closeStatement();
			
			if (rowCount > 0)
			{
				return true;
			}

		} catch (Exception e)
		{
			ExceptionHelper.logExceptionToFile("areParentChild", e);
		}

		return false;
	}

	/**
	 * For the supplied asset GUID return the parent folder GUID
	 */
	public String getParentFolderGUID(Connection c, String assetGUID)
	{
		String guid = null;
		try
		{
			String stmt = Statements.getStatement(SharedStatements.FIND_PARENT_FOLDER_GUID);
			stmt = stmt.replace("[ASSET_GUID]", assetGUID);

			Statement statement = c.createStatement();
			ResultSet rs = statement.executeQuery(stmt);
			rs.next();
			guid = rs.getString(1);
			rs.close();
			statement.close();
		} 
		catch (Exception e)
		{
			ExceptionHelper.logExceptionToFile("FolderDatabase.getParentFolderGUID", e);
			//System.out.println("Exception in retrieving next MB Assets Sequence value: " + ExceptionHelper.getStackTraceAsString(e));
		}
		return guid;
	}


}

package com.jostens.dam.shared.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import com.jostens.dam.shared.assetclass.BaseAsset;
import com.jostens.dam.shared.assetclass.BaseFolderMetadataAsset;
import com.jostens.dam.shared.common.ExceptionHelper;
import com.jostens.dam.shared.common.OutputFormatters;
import com.jostens.dam.shared.helpers.MetadataHelper;
import com.viragemediabin.www.VirageMediaBinServerSoap;

public class SharedDatabase
{
	/**
	 * Return assets that are missing a defined metadata value.  The GUID that is missing must be provided.
	 * 
	 * Beyond that, the following information must also be provided:
	 * 		c - Database connection (Required)
	 * 		folders - Comma separated folder GUIDS to search beneath.  The GUIDS can be enclosed by ' but isn't required  (Required)
	 * 		Calendar - If no date restrictions, supply a null value.  Otherwise supply a properly set time in a Calendar object
	 * 				that any potential asset must have been modified prior to this time period.
	 */
	public List<BaseAsset> getAssetsMissingMetadata(Connection c, String missingGUID, String startingFolderGUIDS, Calendar timeRestriction)
	{
		// Need to assure that all starting folder GUIDS are single quote enclosed 
		StringBuffer sb = new StringBuffer();
		StringTokenizer st = new StringTokenizer(startingFolderGUIDS, ",");
		while (st.hasMoreTokens())
		{
			String folderGUID = st.nextToken();
			if (!folderGUID.startsWith("'"))
			{
				folderGUID = "'" + folderGUID + "'";
			}
			
			sb.append(folderGUID + ",");
		}

		String guidList = OutputFormatters.removeTrailingCharacter(sb.toString(), ",");
		List<BaseAsset> assetsMissingMetadata = findAssetsMissingMetadata(c, missingGUID, guidList, timeRestriction);

		return assetsMissingMetadata;
	}
	private List<BaseAsset> findAssetsMissingMetadata(Connection c, String missingGUID, String startingFolderGUIDS, Calendar timeRestriction)
	{
		List<BaseAsset> assets = new ArrayList<BaseAsset>();

		try
		{
			String stmt = Statements.getStatement(SharedStatements.FIND_ASSETS_MISSING_METADATA_FIELD);
			// Some replacements are needed
			stmt = stmt.replace("[STARTING_FOLDER_GUIDS]", startingFolderGUIDS);
			stmt = stmt.replace("[METADATA_GUID]", missingGUID);
			if (timeRestriction == null)
			{
				stmt = stmt.replace("[DATE_RESTRICTION]", "");
			}
			else
			{		// Must supply a Date restriction value into SQL statement
				String dateFormat = "and ar.REFMODIFIED < TO_DATE('[EARLIER_THAN_DATE]', 'HH24:MI:SS DD/MM/YYYY')";
				String dateString = OutputFormatters.getDateFormat7(timeRestriction.getTimeInMillis());
				dateFormat = dateFormat.replace("[EARLIER_THAN_DATE]", dateString);
				
				stmt = stmt.replace("[DATE_RESTRICTION]", dateFormat);
			}
			
//			System.out.println("\n" + stmt);

			StatementProcessor processor = new StatementProcessor(c, stmt);
			ResultSet rs = processor.getResultSet();
			while (rs.next())
			{
				BaseAsset ba = new BaseAsset(rs.getString(1), rs.getString(2));
				ba.setFolderGUID(rs.getString(3));
				assets.add(ba);
			}
			rs.close();
			processor.closeStatement();
		} catch (Exception e)
		{
			ExceptionHelper.logExceptionToFile("getNoAssetClassAssets", e);
			assets.clear();
		}
		
		return assets;
	}
	
	/**
	 * Be provided a starting folder GUID, usually the parent folder for an asset.  This method will execute a query
	 * and return all the parent folders for that starting folder.  The first folder in the List will be the starting folder, then sequential
	 * items in the List will be the next parent up the chain.  As part of the returned assets is a depth indicator that comes from
	 * MediaBin tables (so we have no control on this).  Depth of 0 is the starting folder and each parent folder will have this value
	 * increased by 1.
	 * 
	 * * NOTE * the top level '/Media Database' folder is not included in the returned list since we have no control over that folder.
	 * 
	 * Users of this method should look into caching the results if examining multiple assets from the same folder.
	 * 
	 * There is a parameter to load metadata found on each folder if desired.
	 */
	public List<BaseFolderMetadataAsset> getParentFolders(VirageMediaBinServerSoap ws, Connection c, String startingFolderGUID, boolean includeMetadata)
	{
		List<BaseFolderMetadataAsset> parentFolders = new ArrayList<BaseFolderMetadataAsset>();

		try
		{
			String stmt = Statements.getStatement(SharedStatements.FIND_PARENT_FOLDERS);
			// Some replacements are needed
			stmt = stmt.replace("[STARTING_FOLDER_GUID]", startingFolderGUID);

			StatementProcessor processor = new StatementProcessor(c, stmt);
			ResultSet rs = processor.getResultSet();
			while (rs.next())
			{
				BaseFolderMetadataAsset fmda = new BaseFolderMetadataAsset(rs.getString(1), rs.getString(2));
				fmda.setDepth(rs.getInt(3));
				parentFolders.add(fmda);
			}
			rs.close();
			processor.closeStatement();
			
			// if metadata is to be included, perform that action now
			if (includeMetadata)
			{
				for (BaseFolderMetadataAsset folder : parentFolders)
				{
					MetadataHelper.populateALLContainerMetadata(ws, folder);
				}
			}
		} catch (Exception e)
		{
			ExceptionHelper.logExceptionToFile("getAssetParentFolders", e);
			parentFolders.clear();
		}

		return parentFolders;
	}
}

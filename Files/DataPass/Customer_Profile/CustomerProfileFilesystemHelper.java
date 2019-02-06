package com.jostens.dam.brand.helpers;

import java.io.File;

import com.jostens.dam.brand.folders.BrandFolderPath;
import com.jostens.dam.shared.assetclass.BaseAsset;
import com.jostens.dam.shared.assetclass.MetadataGUIDS;
import com.jostens.dam.shared.common.Converters;
import com.viragemediabin.www.VirageMediaBinServerSoap;

/**
 * This object manages asset files within the customer profile filesystem.
 * 
 * There are also validation methods in place to help manage asset files are correctly
 * being placed in folders.
 */
public class CustomerProfileFilesystemHelper
{
	private String stagePath = null;
	private String damPath = null;
	private String downloadPath = null;

	public CustomerProfileFilesystemHelper()
	{
		stagePath = BrandFolderPath.getCustomerProfileStagePath();
		damPath = BrandFolderPath.getCustomerProfileDAMPath();
		
		// When BaseDownload is called to download an asset, MediaBin a web service is called which requires
		// back slashes in the path instead of forward slashes.  So take the stage path and replace
		// / with \.
		downloadPath = Converters.convertPathToUNC(stagePath);
//		System.out.println("STAGE PATH=" + stagePath);
//		System.out.println("DAM PATH=" + damPath);
//		System.out.println("Download PATH=" + downloadPath);
	}
	
	/**
	 * Investigate if the supplied asset can be found in the Stage folder, return true
	 * if found.  Caller needs to determine if this is an error condition or can a over-write happen.
	 */
	public boolean isFoundStageFolder(BaseAsset ba)
	{
		File stageFile = new File(stagePath + "/" + ba.getAssetName());
		return stageFile.exists() && stageFile.length() > 0;
	}

	/**
	 * Delete a file out of the stage folder
	 */
	public void deleteStageFolder(BaseAsset ba)
	{
		File stageFile = new File(stagePath + "/" + ba.getAssetName());
		stageFile.delete();
	}

	/**
	 * Download asset to stage folder, this is the first step in getting an asset file to customer profile.  This method will wait
	 * until the download completes or a max error count is reached and then a false method return.
	 */
	public boolean downloadAssetFileToStageFolder(VirageMediaBinServerSoap ws, BaseAsset ba) throws Exception
	{
		ws.MBAsset_RetrieveFile(ba.getAssetId(), MetadataGUIDS.GET_ORIGINAL_TASK_ID, downloadPath, null);
		
		for (int i = 0; i < 15; i++)
		{
//			System.out.println("Loop!");
			Thread.sleep(1000);
			File stageFile = new File(stagePath + "/" + ba.getAssetName());
			if (stageFile.exists() && stageFile.length() > 0)
			{
				Thread.sleep(1000);
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Return the path to where a BaseAsset will be placed in the DAM/X/YY folder structure.
	 * 
	 * REQUIREMENT: All assets passed to this method must have CUSTOMER_ID_ORACLE metadata
	 * set.  This is needed because it's used to determine the DAM folder the asset is placed in.
	 * Under DAM folder, there is a structure X/YY, where X is the last digit of the customer ID and
	 * YY is the 2 digits before the last digit.  This alleviates too many assets in one folder. 
	 */
	public String getDAMFolderPath(BaseAsset ba)
	{
		// Return the DAM folder path for where the asset will be placed
		String customerID = ba.getMetaDataValue(MetadataGUIDS.CUSTOMER_ID_ORACLE);
		if (customerID == null || customerID.length() != 7)
		{	// This is the case for etching with no customer defined
			customerID = "0000000";
		}
		String level1 = customerID.substring(6);
		String level2 = customerID.substring(4, 6);
		// Build the path and then return the value
		String fullPath = damPath + level1 + "/" + level2;
		return fullPath;
	}
	
	/**
	 * Investigate if the supplied asset can be found in the DAM folders, return true
	 * if found.  Caller needs to determine if this is an error condition or can a over-write happen.
	 */
	public boolean isFoundDAMFolder(BaseAsset ba, String damPath)
	{
		File damFile = new File(damPath + "/" + ba.getAssetName());
		return damFile.exists();
	}

	/**
	 * Delete a asset file out of the DAM folder
	 */
	public void deleteFromDAMFolder(BaseAsset ba, String damPath)
	{
		File damFile = new File(damPath + "/" + ba.getAssetName());
		damFile.delete();
	}


	/**
	 * Move the asset to the supplied DAM folder path.
	 * 
	 * If successful a null is returned.  Otherwise a text message describing the issue is returned.
	 */
	public String moveAssetToDAMFolder(BaseAsset ba, String damPath) throws Exception
	{
		if (isFoundDAMFolder(ba, damPath))
		{
			return "Already found in DAM folder";
		}
		
//		System.out.println("1-->" + stagePath + ba.getAssetName());
//		System.out.println("2-->" + damPath + "/" + ba.getAssetName());
		
		// Perform the Move from stageFile to damFile
		File stageFile = new File(stagePath + ba.getAssetName());
		File damFile = new File(damPath + "/" + ba.getAssetName());

		// Create any missing folders - Returns true if directories were created, false if not
		// So really doesn't make sense to check return value
		damFile.getParentFile().mkdirs();

		// Perform the move, the result of which will determine if this move is successful
		boolean moveSuccessful = stageFile.renameTo(damFile);
		if (!moveSuccessful)
		{
			return "RENAMETO did not succeed";
		}
		
		return null;		// Successful move - no error string returned
	}

}

package com.jostens.dam.brand.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jostens.dam.brand.attributes.brand.StatusAutomation;
import com.jostens.dam.brand.automations.folderstructure.AUTOMATIONFolderStructure;
import com.jostens.dam.brand.helpers.AttributesMetadata;
import com.jostens.dam.brand.process.StoreAssetPackage;
import com.jostens.dam.shared.assetclass.BaseAsset;
import com.jostens.dam.shared.assetclass.MetadataGUIDS;
import com.jostens.dam.shared.common.Folder;
import com.jostens.dam.shared.helpers.MetadataHelper;
import com.viragemediabin.www.MBMetadataParameter;

/**
 * Represets an asset as it progresses through TaskGroup processing.
 */
public class TaskAsset extends BaseAsset
{

	// The revision values are used to determine if revision count has changed which means
	// this asset should be processed once again through the TaskGroups
	private int lastProcessedRevision = -1;		// Last revision # when this asset went through TaskGroups
	private int currentRevision = -1;	// Current revision, which if different that lastProcessedRevision means an update happened and so 
										// TaskGroups should process again.
	private int lastPackageAssetCount = -1;		// Keep count of previous asst count in package
	private int currentPackageAssetCount = -1;	// Current package asset count, if different than lastPackageAssetCount means assets were
										// added to the package so it should be processed again
	// Each automation cycle all matches will be marked inactive.  As each match is encountered in the child folder search
	// this value will be set to true.  Anything remaining set false will be removed because it no longer is found 
	// as the catalog asset was removed or was processed out of automation status values.
	private boolean active = false;	
	// Flag to indicate if this is a newly found asset by the folder structure automation
	private boolean newAsset = false;
	// Flag to indicate if updatefix examining is needed
	private boolean updateFixExamine = false;
	// Flag to indicate if a failure condition exists on the asset
	private boolean failure = false;
	// Flag to indicate if metadata was just loaded
	private boolean metadataLoaded = false;
	// Folder object this Asset can be found in - With this a task can have logic related to the folder
	private Folder folder = null;
	// The current status of this asset (STATUS_AUTOMATION metadata)
	private String statusAutomation = "";
	// The status automation value at the start of a processing cycle
	private String startingStatusAutomation = "";
	// This attribute holds a String that defines where in the processing cycle this asset currently is
	private String processingStatus = null;

	// Additional data about this baseasset that is needed as it's processed through Tasks.  This includes the assets in the full package
	// and a structure to help with caching metadata changes for a single save of the assets
	// Several of the tasks need to perform work on the assets which make up a package
	// .cdr, .pngs and .svgs	// This enables only one query to be needed
	private List<BaseAsset> packageAssets = new ArrayList<BaseAsset>();

	// As the packageAssets are processed by each task, Attribute data is found that
	// needs to be added to the asset or removed from the asset.  The following structure
	// will keep that data as tasks are processing so only a single update is performed for all tasks
	private Map<String, AttributesMetadata> packageMetaData = new HashMap<String, AttributesMetadata>();

	// Tasks need to perform actions if a PART is found, the following is the GUID of the PART referenced by this asset
	private String partGUID = null;

	// Unique store assets that have been found as possible matches
	private List<StoreAssetPackage> uniqueStoreAssetMatches = new ArrayList<StoreAssetPackage>();

	// Create a TaskAsset from a BaseAsset
	public TaskAsset(BaseAsset asset)
	{
		this(asset.getAssetName(), asset.getAssetId());		
		newAsset = true;
	}
	// Create a TaskAsset by providing name and GUID
	public TaskAsset(String assetName, String assetID)
	{
		super(assetName, assetID);		
	}
	public int getCurrentRevision()
	{
		return currentRevision;
	}
	public void setCurrentRevision(int currentRevision)
	{
		if (currentRevision != lastProcessedRevision && !isNewAsset())
		{
			AUTOMATIONFolderStructure.logAutomationMsg("[setCurrentRevision] (" + currentRevision + ") " + getPathInformation());
		}
//		active = true;		// If a current revision is found, the asset must be active
		this.currentRevision = currentRevision;
	}
	public boolean isActive()
	{
		return active;
	}
	public void setActive(boolean active)
	{
		this.active = active;
	}
	public Folder getFolder()
	{
		return folder;
	}
	public void setFolder(Folder folder)
	{
		this.folder = folder;
		setFolderName(folder.getName());
	}
	
	public List<BaseAsset> getPackageAssets()
	{
		return packageAssets;
	}
	public void setPackageAssets(List<BaseAsset> packageAssets)
	{
		this.packageAssets = packageAssets;
		currentPackageAssetCount = packageAssets.size();
	}
	public Map<String, AttributesMetadata> getPackageMetaData()
	{
		return packageMetaData;
	}
	public String getPartGUID()
	{
		return partGUID;
	}
	public void setPartGUID(String partGUID)
	{
		this.partGUID = partGUID;
	}
	public List<StoreAssetPackage> getUniqueStoreAssetMatches()
	{
		return uniqueStoreAssetMatches;
	}
	public void setUniqueStoreAssetMatches(
			List<StoreAssetPackage> uniqueStoreAssetMatches)
	{
		this.uniqueStoreAssetMatches = uniqueStoreAssetMatches;
	}
	/**
	 * If currentRevision is larger than lastProcessedRevision, this asset needs to examined by Task Groups
	 */
	public boolean shouldTaskAssetBeProcessed()
	{
//		System.out.println("  1   :" + currentRevision + ":" + lastProcessedRevision + ":");
//		System.out.println("  2   :" + currentPackageAssetCount + ":" + lastPackageAssetCount + ":");
//		System.out.println("  3   :" + startingStatusAutomation + ":" + statusAutomation + ":");
		// If status contains PROCESSING - don't process this asset
		if (isNewAsset())
		{
			return false;		// New assets aren't processed for 1 cycle
		}
		if (isFailure())
		{
			return false;		// Failure condition on asset so cannot process
		}
		if (startingStatusAutomation.contains(StatusAutomation.PR_PROCESSING))
		{
			return false;
		}
		if (startingStatusAutomation.contains(StatusAutomation.DR_INUSE_CATALOG))
		{
			return false;
		}
		if (currentRevision > lastProcessedRevision)
		{
			return true;
		}
		if (getStatusAutomation().length() == 0)
		{
			return true;
		}
//		if (currentPackageAssetCount != lastPackageAssetCount)
//		{
//			return true;
//		}
//		if (startingStatusAutomation.length() == 0)
//		{
//			return true;
//		}
		return false;
	}
	
	public String getDebugString()
	{
		return currentRevision + " + " + lastProcessedRevision + " | " + currentPackageAssetCount + " + " + lastPackageAssetCount;
	}
	/**
	 * Mark that this asset was processed.
	 */
	public void assetWasProcessed()
	{
		lastProcessedRevision = currentRevision;
		lastPackageAssetCount = currentPackageAssetCount;
	}
	
	/**
	 * Increment current revision
	 */
	public void incrementCurrentRevision()
	{
		currentRevision++;
	}
	
	/**
	 * Reset task needed attributes found as part of this TaskAsset
	 */
	public void resetTaskAsset()
	{
		active = false;
		newAsset = false;
		updateFixExamine = false;
		failure = false;
//		metadataLoaded = false;
//		lastProcessedRevision = currentRevision;
//		getPackageAssets().clear();
//		getPackageMetaData().clear();
//		partGUID = null;
	}
	
	/**
	 * Clear all metadata from this TaskAsset and in maintained structures.  This enables a TaskGroup to process a clean asset.
	 */
	public void clearMetadata()
	{
//		getAssetMetadata().clear();
//		for (BaseAsset ba : getPackageAssets())
//		{
//			ba.getAssetMetadata().clear();
//		}
//		for (AttributesMetadata amd : getPackageMetaData().values())
//		{
//			amd.getUpdateMetaData().clear();
//			amd.getRemoveMetaData().clear();
//		}
	}
	/**
	 * For sorting, return the folder name in a 10 character string
	 */
	public String getFolderNameForSort()
	{
		String sortName = null;
		if (getFolderName().length() >= 10)
		{
			sortName = String.format("%-10s", getFolderName().substring(0, 10));
		}
		else
		{
			sortName = String.format("%-10s", getFolderName());			
		}
		return sortName;
	}
	
	/**
	 * Return true if status automation value changed during processing cycle
	 */
	public boolean didProcessingOccur()
	{
//		System.out.println(" START = " + startingStatusAutomation);
//		System.out.println(" END = " + statusAutomation);
		if (!statusAutomation.equals(startingStatusAutomation))
		{
			return true;
		}
		return false;
	}
	public void setStartingStatusAutomation(String startingStatusAutomation)
	{
		// Also set status automation with the starting value
//		setStatusAutomation(startingStatusAutomation);
		
		if (startingStatusAutomation == null)
		{
			this.startingStatusAutomation = "";
			return;
		}
		this.startingStatusAutomation = startingStatusAutomation;
	}
	public String getStartingStatusAutomation()
	{
		return startingStatusAutomation;
	}
	public String getProcessingStatus()
	{
		return getPathInformation() + " : " + processingStatus;
	}
	public void setProcessingStatus(String processingStatus)
	{
		this.processingStatus = processingStatus;
	}
	public String getPathInformation()
	{
		if (getFolder() == null)
		{
			return "ERROR Path/";
		}
		return getFolder().getFolderType() + "/" + getFolderName() + "/" + getAssetName();
	}
	
	/*
	 * Return the task status with the supplied asset name added on.
	 */
	public String getStatusWithAssetName(String status, String assetName)
	{
		return status + ":" + assetName;
	}
	/*
	 * Return the task status with the asset name taken from the first entry found in uniqueStoreAssetMatches
	 */
	public String getStatusWithAssetName(String status)
	{
		if (uniqueStoreAssetMatches.isEmpty())
		{
			return "Error:Empty Store Asset matches";
		}
		if (uniqueStoreAssetMatches.size() != 1)
		{
			return "Error:Store Asset matches has more than a single entry";
		}
		StoreAssetPackage assetPackage = uniqueStoreAssetMatches.get(0);
		return getStatusWithAssetName(status, assetPackage.getMasterCDR().getAssetName());
	}
	
	/**
	 * Return a folder identifier for use on organizing assets in UpdateFixCache
	 */
	public String getFolderIdentifier()
	{
		return folder.getFolderType() + ":" + getFolderName();
	}

	/**
	 * Implement the equals method based on assetId
	 */
	public boolean equals(Object obj)
	{
		if (!(obj instanceof TaskAsset))
		{
			return false;
		}
		TaskAsset compareAsset = (TaskAsset)obj;
		return getAssetId().equals(compareAsset.getAssetId());
	}
	public boolean isUpdateFixExamine()
	{
		return updateFixExamine;
	}
	public void setUpdateFixExamine(boolean updateFixExamine)
	{
		this.updateFixExamine = updateFixExamine;
	}
	public boolean isNewAsset()
	{
		return newAsset;
	}
	public void setNewAsset(boolean newAsset)
	{
		this.newAsset = newAsset;
	}
	public boolean isFailure()
	{
		return failure;
	}
	public void setFailure(boolean failure)
	{
		this.failure = failure;
	}
	public boolean isMetadataLoaded()
	{
		return metadataLoaded;
	}
	public void setMetadataLoaded(boolean metadataLoaded)
	{
		this.metadataLoaded = metadataLoaded;
	}	

	public String getAssetClass()
	{
		return getMetaDataValue(MetadataGUIDS.ASSET_CLASS);
	}
	public String getStatusAutomation()
	{
		String statusAutomation = getMetaDataValue(MetadataGUIDS.STATUS_AUTOMATION);
		if (statusAutomation == null)
		{
			return "";
		}
		return statusAutomation;
	}
	public void setStatusAutomation(String statusAutomation)
	{
		if (statusAutomation == null)
		{
			getAssetMetadata().remove(MetadataGUIDS.STATUS_AUTOMATION);		// Remove status automation from metadata definitions
			return;
		}
		MBMetadataParameter param = MetadataHelper.getSingleValueParameter(MetadataGUIDS.STATUS_AUTOMATION, statusAutomation);
		addAssetMetadata(MetadataGUIDS.STATUS_AUTOMATION, param);
//		if (statusAutomation == null)
//		{
//			this.statusAutomation = "";
//			return;
//		}
//		this.statusAutomation = statusAutomation;
	}

	public int getCurrentPackageAssetCount()
	{
		return currentPackageAssetCount;
	}
	public void setCurrentPackageAssetCount(int currentPackageAssetCount)
	{
		this.currentPackageAssetCount = currentPackageAssetCount;
	}
}

package com.jostens.dam.brand.tasks;

import java.rmi.RemoteException;
import java.util.List;

import com.jostens.dam.brand.assetclass.interfaces.BrandNamedAsset;
import com.jostens.dam.brand.attributes.Attribute;
import com.jostens.dam.brand.helpers.AttributesHelper;
import com.jostens.dam.brand.helpers.AttributesMetadata;
import com.jostens.dam.shared.assetclass.BaseAsset;
import com.jostens.dam.shared.common.JostensLog;
import com.viragemediabin.www.VirageMediaBinServerSoap;

/**
 * Defines methods that all tasks must implement to be part of a task group.
 */
public abstract class BaseTask
{
	protected JostensLog log = new JostensLog();

	// Perform the task for the specified asset found in the supplied Folder
	public abstract boolean performTask(VirageMediaBinServerSoap ws, TaskAsset taskAsset) throws RemoteException;
	
	// Return the status value represented by this task
	public abstract String getCompleteStatus();
	
	// Default implementation - Return a new status automation value if this task successfully completed.  The default format
	//							is previous status + complete status + a space.  Tasks with more complex logic should implement method.
	public String getStatusAutomation(String status)
	{
		return status + getCompleteStatus() + " ";
	}

	// Default implementation - Valid in case where a task executing is dependent on a previous task being completed.
	//							The getPreviousTaskCompleteStatus needs to be setup with that value. Task with more complex requirements
	//							should implement this method.
	public boolean shouldPerformTask(VirageMediaBinServerSoap ws, TaskAsset taskAsset) throws RemoteException
	{
		String statusAutomation = taskAsset.getStatusAutomation();
		if (!statusAutomation.contains(getPreviousTaskCompleteStatus()))
		{
			return false;
		}
		return true;		
	}
	// Default implementation - Return a bad value for previous task complete status.  Users of default implementation of
	//							shouldPerformTask need to implement this method and supply the correct value
	public String getPreviousTaskCompleteStatus()
	{
		return "BAD_VALUE";
	}


	// Status Messages a Task can add
//	private List<String> statusMessages = new ArrayList<String>();	// Status messages related to last execution


	/*
	 * Apply the supplied attribute list to the packageAssets, using packageMetaData to store any meta data
	 * additions requried.  Attributes are defined by their GUID.  This method assumes the data to set
	 * can be based on information contained within the asset itself.
	 */
	protected boolean applyAttributesToAssets(TaskAsset taskAsset, List<String> attributes)
	{
		applyAttributesToAssets(taskAsset, null, attributes);
		return true;
	}
	
	/*
	 * Apply the supplied attribute list to the packageAssets, using packageMetaData to store any meta data
	 * additions requried.  Attributes are defined by their GUID.  This method assumes the data to set
	 * is based on metadata contained on the Master asset.
	 */
	protected boolean applyAttributesToAssetsFromMaster(TaskAsset taskAsset, List<String> attributes)
	{
		BaseAsset masterAsset = getMasterAsset(taskAsset);
		if (masterAsset == null)
		{
			return false;
		}
		return applyAttributesToAssets(taskAsset, masterAsset, attributes);
	}

	/*
	 * Find the CDR asset in packageAssets
	 */
	protected BaseAsset getMasterAsset(TaskAsset taskAsset)
	{
		for (BaseAsset asset : taskAsset.getPackageAssets())
		{
			if (!(asset instanceof BrandNamedAsset))
			{
				continue;
			}
			if (((BrandNamedAsset)asset).isMasterAsset())
			{
				return asset;
			}
		}
		return null;
	}

	/*
	 * Apply the supplied attribute list to the packageAssets, using packageMetaData to store any meta data
	 * additions requried.  Attributes are defined by their GUID.  This method assumes the data to set
	 * can be based on information contained within the asset itself.
	 */
	protected boolean applyAttributesToAssets(TaskAsset taskAsset, BaseAsset masterAsset, List<String> attributes)
	{
		for (BaseAsset asset : taskAsset.getPackageAssets())
		{
			AttributesMetadata attributeMetaData = taskAsset.getPackageMetaData().get(asset.getAssetId());
			// Find the type of asset so the call to applyAttributesToAsset is applicable to whatever type being processed
			int assetType = Attribute.NO_TYPE;
			if (asset.getAssetName().endsWith(".cdr"))
			{		
				assetType = Attribute.MASTER;
			}
			if (asset.getAssetName().endsWith(".png"))
			{		
				assetType = Attribute.PNG;
			}
			if (asset.getAssetName().endsWith(".svg"))
			{		
				assetType = Attribute.SVG;
			}
			if (asset.getAssetName().endsWith(".pdf"))
			{		
				assetType = Attribute.PDF;
			}
			if (asset.getAssetName().endsWith(".eps"))
			{		
				assetType = Attribute.EPS;
			}
			if (assetType != Attribute.NO_TYPE)
			{
				AttributesHelper.applyAttributesToAsset(assetType, taskAsset.getFolder(), asset, attributes, attributeMetaData);
			}
			else
			{
				log.error("Unable to determine asset type for name: " + asset.getAssetName());
//				System.out.println("Unable to determine asset type for name: " + asset.getAssetName());
				return false;
			}
		}
		return true;
	}

	/**
	 * For complex tasks, like determine action tasks, the complete status includes asset names in built status value.  This doesn't work
	 * real well for determining if a task should process. In these cases, implement the following returning true to indicate
	 * the task should always process.  It's assumed that other logic will be found in the shouldProcess method to actually stop processing.
	 */
	public boolean alwaysProcessTask()
	{
		return false;
	}

//	protected void addStatusMessage(String msg)
//	{
//		statusMessages.add(msg);
//	}
//	protected void clearStatusMessage()
//	{
//		statusMessages.clear();
//	}
//	public List<String> getStatusMessages()
//	{
//		return statusMessages;
//	}

}

package com.jostens.dam.brand.tasks;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jostens.dam.brand.attributes.brand.StatusAutomation;
import com.jostens.dam.brand.helpers.AttributesHelper;
import com.jostens.dam.brand.helpers.AttributesMetadata;
import com.jostens.dam.shared.assetclass.MetadataGUIDS;
import com.jostens.dam.shared.common.ExceptionHelper;
import com.jostens.dam.shared.helpers.MetadataHelper;
import com.viragemediabin.www.VirageMediaBinServerSoap;

/**
 * Abstract class that implements processing framework for groups of tasks.
 */
public abstract class TaskGroup
{
	
	private List<BaseTask> tasksInGroup = new ArrayList<BaseTask>();
	protected List<String> requiredStatusAutomations = new ArrayList<String>();
	protected List<String> excludedStatusAutomations = new ArrayList<String>();

	public TaskGroup()
	{
		// Setup status automation requirements used to determine if this task group can process the supplied asset
		String[] automations = getRequiredStatusAutomations();
		if (automations != null)
		{
			for (String automation : automations)
			{
				requiredStatusAutomations.add(automation);
			}
		}
		automations = gettExcludedStatusAutomations();
		if (automations != null)
		{
			for (String automation : automations)
			{
				excludedStatusAutomations.add(automation);
			}
		}
	}
	/*
	 * Add a task to this group.  Tasks need to be added in the order of processing.
	 */
	protected void addTask(BaseTask taskToAdd)
	{
		tasksInGroup.add(taskToAdd);
	}

	/**
	 * Have this TaskGroup process the supplied TaskAsset
	 */
	public void processTaskAsset(VirageMediaBinServerSoap ws, TaskAsset taskAsset) throws RemoteException
	{
		// Get current status automation
		String statusAutomation = taskAsset.getStatusAutomation();
		// Verify all required values are found
		for (String required : requiredStatusAutomations)
		{
			if (!statusAutomation.contains(required))
			{
				return;
			}
		}
		// Verify all excluded values are not found
		for (String excluded : excludedStatusAutomations)
		{
			if (statusAutomation.contains(excluded))
			{
				return;
			}
		}
		
		// Next, process all the tasks in the TaskGroup
		applyTasksToAsset(ws, taskAsset);
	}
	
	protected void applyTasksToAsset(VirageMediaBinServerSoap ws, TaskAsset taskAsset) throws RemoteException
	{
		String status = taskAsset.getStatusAutomation();
		String originalStatus = status;

		// Reset the changed flag and reset maps for data update
		boolean statusChanged = false;		
		for (BaseTask task : tasksInGroup)
		{
			// If the statusAutomation on the task asset contains the tasks complete status value, don't process the task
			if (!task.alwaysProcessTask() && !task.getCompleteStatus().isEmpty() && status.contains(task.getCompleteStatus()))
			{
				continue;
			}

			boolean performTask = task.shouldPerformTask(ws, taskAsset);
			if (!performTask)
			{	// Task should not be performed
				continue;
			}

			boolean taskCompleted = task.performTask(ws, taskAsset);
			if (!taskCompleted)
			{	// Task didn't complete
				break;		// If task didn't complete than fail out of updating tasks
			}
			
			statusChanged = true;
			status = task.getStatusAutomation(status);
			// If new status contains TG_METADATA_SAVED - A save was performed, but status shouldn't be updated
			if (status != null && status.contains(StatusAutomation.TG_METADATA_SAVED))
			{
				status = status.replace(StatusAutomation.TG_METADATA_SAVED + " ", "");
				continue;
			}
			if (status != null && status.contains(StatusAutomation.NO_STATUS))
			{
				status = status.replace(StatusAutomation.NO_STATUS + " ", "");
				continue;
			}
			taskAsset.setStatusAutomation(status);
			if (status == null)
			{
				break;		// Asset has been processed out of folder
			}
			if (status.length() == 0)
			{
				break;		// Status returned is empty string which is code to remove the field
			}
		}

		Collection<AttributesMetadata> attributeMetaDataList = taskAsset.getPackageMetaData().values();

		// Did the task processing cause status automation to change values or the value to be cleared.  In either case, the
		// AttributesMetadata for the .cdr needs to be updated so the status automation change is updated through web services
		if (status != null && statusChanged && (status.length() == 0 || !originalStatus.equals(status)))
		{
			// Status automation change is needed.  First step is to find AttributesMetadata for .cdr asset
			AttributesMetadata amdMaster = null;
			for (AttributesMetadata amd : attributeMetaDataList)
			{
				if (amd.getAssetName().endsWith(".cdr"))
				{	// Found the proper asset
					amdMaster = amd;
					break;
				}
			}
			// If the master asset wasn't found, then log information and do not proceed
			if (amdMaster == null)
			{
				String msg = "Could not find amdMaster for: " + taskAsset.getAssetName() + "  : " + taskAsset.getPackageAssets().size() + " : " + taskAsset.getPackageMetaData().size();
				ExceptionHelper.logIssueToFile("Update Status Automation", msg);
				return;
			}
			
			if (status.length() == 0)
			{
				// removing status automation
				amdMaster.addRemoveMetaData(MetadataGUIDS.STATUS_AUTOMATION);
				taskAsset.getAssetMetadata().remove(MetadataGUIDS.STATUS_AUTOMATION);		// remove the status automation field from the assets metadata
				taskAsset.setStatusAutomation(null);				
			}
			else
			{
				// Updating status automation
				amdMaster.addUpdateMetaData(MetadataGUIDS.STATUS_AUTOMATION, MetadataHelper.getSingleValueParameter(MetadataGUIDS.STATUS_AUTOMATION, status));				
			}
			
		}
		
		// Perform any updates that occurred due to task processing - Master Asset should be updated last
		AttributesMetadata masterAmd = null;
		for (AttributesMetadata amd : attributeMetaDataList)
		{
			if (amd.getAssetGUID().equals(taskAsset.getAssetId()))
			{	// Skip the master asset, but keep a reference to its AttributesMetadata so it can be easily updated
				masterAmd = amd;
				continue;
			}
			AttributesHelper.storeAttributeMetaData(ws, amd);	// Store any meta data changes
		}
		// update the master asset last
		AttributesHelper.storeAttributeMetaData(ws, masterAmd);
	}

	/**
	 * Get from the implementer any statusAutomation values that are required
	 */
	public abstract String[] getRequiredStatusAutomations();
	
	/**
	 * Get from the implementer any statusAutomation values that must be excluded
	 */
	public abstract String[] gettExcludedStatusAutomations();

}

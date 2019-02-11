package com.jostens.dam.brand.tasks;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.jostens.dam.brand.automations.folderstructure.AUTOMATIONFolderStructure;
import com.jostens.dam.brand.folders.BrandFolderGUIDS;
import com.jostens.dam.brand.helpers.AttributesMetadata;
import com.jostens.dam.brand.helpers.AutomationsHelper;
import com.jostens.dam.brand.tasks.deploymentready.DeploymentReadyTaskGroup;
import com.jostens.dam.brand.tasks.determineaction.DetermineActionTaskGroup;
import com.jostens.dam.brand.tasks.folderstructure.FolderStructureTaskGroup;
import com.jostens.dam.brand.tasks.metadata.MetadataTaskGroup;
import com.jostens.dam.shared.assetclass.BaseAsset;
import com.jostens.dam.shared.assetclass.MetadataGUIDS;
import com.jostens.dam.shared.automations.BaseThread;
import com.jostens.dam.shared.common.Converters;
import com.jostens.dam.shared.common.ExceptionHelper;
import com.jostens.dam.shared.folders.FolderGUIDS;
import com.jostens.dam.shared.helpers.AssetRevisionsHelper;
import com.jostens.dam.shared.helpers.MetadataHelper;
import com.jostens.dam.shared.idol.IDOLMetadataHelper;
import com.jostens.dam.shared.idol.IDOLSearchHelper;
import com.viragemediabin.www.VirageMediaBinServerSoap;

/**
 * Entry point for processing a TaskAsset by the defined Task Groups.
 * 
 * When a TaskAsset is supplied, there is some further initialization done and then all defined TaskGroups process the TaskAsset.
 */
public class TaskGroupProcessor extends BaseThread
{
	private List<TaskGroup> taskGroups = new ArrayList<TaskGroup>();
	private String[] statusAutomationGUID = {MetadataGUIDS.STATUS_AUTOMATION};
	private boolean available = false;		// If = true, this Processor is available for TaskAsset processing
	private int threadNumber = 0;		// Keep the thread number this is part of
	private TaskAsset taskAsset = null;		// TaskAsset to process
	private VirageMediaBinServerSoap ws = null;		// Web service to use for data access
	private boolean finishProcessing = false;

	public TaskGroupProcessor(int threadNumber)
	{
//		taskGroups.add(new ResetTaskGroup());
		taskGroups.add(new FolderStructureTaskGroup());
		taskGroups.add(new MetadataTaskGroup());
		taskGroups.add(new DeploymentReadyTaskGroup());
		taskGroups.add(new DetermineActionTaskGroup());
		this.threadNumber = threadNumber;
	}

	/**
	 * Primary run method for all threads.  This provides the ability for the thread system to shut down only.
	 * 
	 * It's assumed that extenders of this will use the isAvailable method to determine when a thread can handle
	 * more work.
	 */
	@Override
	public void performThreadAction()
	{
//		System.out.println("Thread Loop: " + threadNumber);
		if (taskAsset != null)
		{
			taskAsset.setProcessingStatus("Processing.");
			long startTime = System.currentTimeMillis();
			processTaskAsset();
			
			try
			{
				// TaskGroup processing could have changed status automation multiple times.  To make sure the asset
				// isn't processed again because the revision would have increased, get the current revision and add
				// it to the task asset prior to marking it as processed.
				//
				// HOWEVER - Only do this activity if the status automation value is the same as what is on the 
				// TaskAsset.  I.E. If another process (cataloger form) has updated this value during processing time
				// don't set the current revision because the asset needs to be processed next cycle.  If we don't do
				// this, an asset can get stuck and not process because an update happened that the folder structure
				// automation wasn't aware of.
				String assetStatusAutomation = AutomationsHelper.getStatusAutomation(ws, taskAsset.getAssetId());
				if (taskAsset != null && taskAsset.getStatusAutomation().length() > 0 && taskAsset.getStatusAutomation().equals(assetStatusAutomation))
				{
					int currentRevision = -1;
					if (taskAsset.getStatusAutomation().length() > 0)
					{
						// After processing the asset, get the current revision value and update onto taskasset so there
						// isn't a second processing of the asset needed.
						currentRevision = AssetRevisionsHelper.getCurrentAssetRevision(ws, taskAsset);
					}
					if (taskAsset.getCurrentRevision() != currentRevision)
					{
						taskAsset.setCurrentRevision(currentRevision);
					}
				}
				taskAsset.assetWasProcessed();
				
			} 
			catch (RemoteException e)
			{
				ExceptionHelper.logExceptionToFile("run (TaskGroupProcessor)", e);
			}
			
			if (taskAsset.getStatusAutomation().length() == 0)
			{
				taskAsset.setProcessingStatus("Removed Status Automation");
			}
			else if (taskAsset.getStatusAutomation().contains("FAILURE:"))
			{
				int i = taskAsset.getStatusAutomation().indexOf("FAILURE:");
				taskAsset.setProcessingStatus(taskAsset.getStatusAutomation().substring(i));
			}
			else if (taskAsset.getStatusAutomation().equals(taskAsset.getStartingStatusAutomation()))
			{
				taskAsset.setProcessingStatus("Status Automation not changed");
			}
			else
			{
				taskAsset.setProcessingStatus(taskAsset.getStatusAutomation());
			}

			String processingMsg = Converters.convertLongDateToString(System.currentTimeMillis() - startTime,  true);
			AUTOMATIONFolderStructure.logAutomationMsg("[PROCESSED] " + processingMsg + " : " + taskAsset.getProcessingStatus(), true);

//				if (taskAsset.didProcessingOccur())
//				{
//					if (taskAsset.getStatusAutomation().length() == 0)
//					{
//						taskAsset.setProcessingStatus("Removed Status Automation");
//					}
//					else
//					{
//						taskAsset.setProcessingStatus(taskAsset.getStatusAutomation());
//					}
//				}
//				else if (taskAsset.getStatusAutomation().contains("FAILURE:"))
//				{
//					int i = taskAsset.getStatusAutomation().indexOf("FAILURE:");
//					taskAsset.setProcessingStatus(taskAsset.getStatusAutomation().substring(i));
//				}
//				else
//				{
//					taskAsset.setProcessingStatus("Status Automation not changed");
//				}

			taskAsset = null;
			available = true;
			
			// If this is the last task asset to process, interrupt the processor
			if (finishProcessing)
			{
				interrupt();
			}
		}
	}

	/**
	 * Process a TaskAsset
	 */
	public void processTaskAsset()
	{
		try
		{
			// Call method to initialize metadata on TaskAsset.  This data can then be used by tasks
			// to determine actions to be performed.
			initializeTaskAsset();
			
			// Process through all defined TaskGroups
			for (TaskGroup taskGroup : taskGroups)
			{
				// If process was interrupted, stop processing taskgroups
				if (interruptHappened())
				{
					break;
				}
				callTaskGroup(taskGroup);
			}
		} 
		catch (Exception e)
		{
			ExceptionHelper.logExceptionToFile("processTaskAsset (TaskGroupProcessor)", e);
		}
		available = true;
	}
	
	/**
	 * Call a TaskGroup for processing.  This call is wrapped in the following ways.
	 * 		1. Before - Reset values on AttributesMetadata objects
	 * 		2. Before - Load all metadata on all package assets
	 * 		3. Before - Set current status automation value on Task Asset
	 */
	public void callTaskGroup(TaskGroup taskGroup) throws RemoteException
	{
		// Reset AttributesMetadata objects
		for (AttributesMetadata amd : taskAsset.getPackageMetaData().values())
		{
			amd.resetData();
		}
		// Load metadata on all current assets
//		MetadataHelper.populateALLAssetMetadata(getWs(), taskAsset.getPackageAssets());
		IDOLMetadataHelper.populateALLAssetMetadata(taskAsset.getPackageAssets());

		// Set status automation value on Task Asset
		setStatusAutomationOnTaskAsset();
		
		// Call the task Group for processing
		taskGroup.processTaskAsset(getWs(), taskAsset);
	}
	
	/**
	 * Initialize any additional information about the TaskAsset that is needed for all Tasks processing
	 */
	public void initializeTaskAsset() throws RemoteException
	{
		// Clear any packageMetaData found on the taskAsset
		taskAsset.getPackageMetaData().clear();
		
		// Initialize the packageMetaData Map with a new AttributesMetaData object for usage in Attributes processing and saving metadata
		for (BaseAsset packageAsset : taskAsset.getPackageAssets())
		{
			taskAsset.getPackageMetaData().put(packageAsset.getAssetId(), new AttributesMetadata(packageAsset));
			// Set foldername on all assets in package from value set on taskAsset
			packageAsset.setFolderName(taskAsset.getFolderName());
		}

		// Initialize the partGUID
		int i = taskAsset.getAssetName().indexOf("_");
		if (i > -1)
		{
			String partName = taskAsset.getAssetName().substring(0, i);
			IDOLSearchHelper idolSearchHelper = new IDOLSearchHelper();
			String partGUID = idolSearchHelper.findInFolderWithName(FolderGUIDS.getFolderGuid(BrandFolderGUIDS.PART), partName);
			if (partGUID != null)
			{
				taskAsset.setPartGUID(partGUID);
			}
			else
			{
				taskAsset.setPartGUID(null);
			}
		}
		else
		{
			taskAsset.setPartGUID(null);
		}

		
	}
	
	public void setStatusAutomationOnTaskAsset() throws RemoteException
	{
		// Setup Status Automation value
		// Verify this task group should process the asset
		MetadataHelper.populateAssetMetadata(ws, taskAsset, statusAutomationGUID);
		String statusAutomation = taskAsset.getMetaDataValue(MetadataGUIDS.STATUS_AUTOMATION);
		if (statusAutomation == null)
		{
			statusAutomation = "";	// easier to work with an empty string
		}
		taskAsset.setStatusAutomation(statusAutomation);		
	}
	
	/**
	 * Method to alert the TaskGroupProcessor that it is done processing assets 
	 * and should stop when the current asset is completed.
	 */
	public void finishProcessing()
	{
		finishProcessing = true;
		if (taskAsset == null)
		{
			// If no asset is set to process, interrupt the processor
			interrupt();
		}
	}

	public boolean isNotAvailable()
	{
		return !available;		// if available return false, if not available return true
	}

	public int getThreadNumber()
	{
		return threadNumber;
	}
	public void setThreadNumber(int threadNumber)
	{
		this.threadNumber = threadNumber;
	}
	public void setTaskAsset(TaskAsset taskAsset)
	{
		available = false;
		this.taskAsset = taskAsset;
	}
	public void setWs(VirageMediaBinServerSoap ws)
	{
		this.ws = ws;
	}
	public VirageMediaBinServerSoap getWs()
	{
		return ws;
	}

	@Override
	protected String getLogFileName()
	{
		return null;
	}

	@Override
	protected int getSleepSeconds()
	{
		// Sleep 1 second
		return 1;
	}

	@Override
	protected void initializeThread()
	{
		available = true;
	}

	@Override
	protected void cleanupThread()
	{
		// No cleanup needed
	}
}

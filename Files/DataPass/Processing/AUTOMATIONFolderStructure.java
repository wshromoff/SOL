package com.jostens.dam.brand.automations.folderstructure;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.jostens.dam.brand.attributes.brand.StatusAutomation;
import com.jostens.dam.brand.email.ArtistIngestEmail;
import com.jostens.dam.brand.email.CatalogerFormEmail;
import com.jostens.dam.brand.folders.BrandFolderGUIDS;
import com.jostens.dam.brand.helpers.AutomationsHelper;
import com.jostens.dam.brand.helpers.CatalogAssetPackageHelper;
import com.jostens.dam.brand.helpers.FolderHelper;
import com.jostens.dam.brand.helpers.MetadataCorelDrawHelper;
import com.jostens.dam.brand.process.CatalogAssetPackage;
import com.jostens.dam.brand.tasks.TaskAsset;
import com.jostens.dam.brand.tasks.TaskAssetComparator;
import com.jostens.dam.brand.tasks.TaskGroupProcessor;
import com.jostens.dam.shared.assetclass.BaseAsset;
import com.jostens.dam.shared.assetclass.MetadataGUIDS;
import com.jostens.dam.shared.assetclass.MetadataNames;
import com.jostens.dam.shared.automations.BaseAutomation;
import com.jostens.dam.shared.common.Converters;
import com.jostens.dam.shared.common.ExceptionHelper;
import com.jostens.dam.shared.common.Folder;
import com.jostens.dam.shared.common.JostensLog;
import com.jostens.dam.shared.common.JostensLogFile;
import com.jostens.dam.shared.common.MediaBinConnectionPool;
import com.jostens.dam.shared.common.OutputFormatters;
import com.jostens.dam.shared.folders.SharedFolderPath;
import com.jostens.dam.shared.helpers.AssetRevisionsHelper;
import com.jostens.dam.shared.helpers.MetadataHelper;
import com.jostens.dam.shared.idol.IDOLSearchHelper;
import com.jostens.dam.shared.properties.RuntimeProperties;
import com.viragemediabin.www.MBMetadataParameter;
import com.viragemediabin.www.VirageMediaBinServerSoap;

/**
 * This automation is responsible for finding the Assets that need to be processed by Tasks.  It also can provide
 * those assets to other automation's if needed.
 */
public class AUTOMATIONFolderStructure extends BaseAutomation
{
	// 								---- Constants controlling functionality of this automation ---
	// Gathering the list of child folders using MB webservice calls is an expensive activity, so this list is generated
	// after the specified number of interations below.  There is also a counter below for the iteration times before this list is rebuilt.
	// Whenever the counter reaches 0, child folders are generated.
	// Child folders don't change often, so why attempt to regenerate child folders lists all the time
	private int FOLDER_ITERATIONS_BETWEEN = 60;	// # of iterations between refreshing child folders
	private static int folderIterationCounter = 0;		// When this has a value of 0 - gather folder list
//	private int ASSET_ITERATIONS_BETWEEN = 120;	// # of iterations between refreshing task assets in child folders
//	private int assetIterationCounter = 0;		// When this has a value of 0 - gather asset list

	// Counter values that control processing assets and usage of the TaskGroupProcessors
	private int TASK_GROUP_PROCESSORS = 4;
	private int TASK_ASSETS_MAX_PROCESSED = 12;	
	
	// Root folders that should be searched for assets
	private String[] rootPathsToExamine = {
			BrandFolderGUIDS.CATALOG_REPLACE,
			BrandFolderGUIDS.CATALOG_REVISE,
			BrandFolderGUIDS.CATALOG_NEW_BRAND
	};

	// Root folders are the parents of artist folders that hold asset packages populated from CorelDraw with a save to JEMM.
	// Only one level down search is performed, so that search is non-recursive.  This folder list is generated once.
	private List<Folder> rootFolders = new ArrayList<Folder>();
	
	// Maintain a Map of all assets found in the child folders of rootFolders List
	private static Map<String, TaskAsset> taskAssets = new HashMap<String, TaskAsset>();

	// Failure asset List.  This takes assets out of main processing and allows the status information to indicate failures
	private List<TaskAsset> failureAssets = new ArrayList<TaskAsset>();

	// The subset of folderAssets which need to be processed by the task groups (all assets that have an increased revision number)
	// This means they have a new revision since last processing loop
	private List<TaskAsset> assetsToProcess = new ArrayList<TaskAsset>();

	// These are assets which have made it through task group processing (I.E. determine action task group).  These are eligable for 
	// update/fix automation or processing automation
	private static List<TaskAsset> taskGroupCompleteAssets = new ArrayList<TaskAsset>();
	
	// When assets are found in folders, this is the required metadata that needs to be added to them
//	private String[] loadMetadata = {MetadataGUIDS.STATUS_AUTOMATION};
	
	// Boolean to indicate that processing preparation is happening
	private boolean preparingForProcessing = false;

	// Boolean to indicate currently processing
	private boolean currentlyProcessing = false;
	
	// Create some Email objects for sending emails
	public static ArtistIngestEmail AIEmail = null;
	public static CatalogerFormEmail CFEmail = null;
	
	// For UNIT tests, limit folders examined to this value.  This is to speed up the number of assets found
	private String limitFoldersTo = null;
	// For UNIT tests, only gather TaskAssets for processing
	private boolean onlyGatherAssets = false;
	
	// Define a JostensLog that will be used for automation logging.  If = null, no logging is happening
	private static JostensLog automationLog = null;
	private long cycleStartTime = 0;

	// Values for timing services
	private long startTime = 0;
	private long stopTime = 0;
	
	// Booleans related to logging
	private static boolean hourlyLogging = false;		// Each hour force a logging cycle to occur
	private static boolean activityOccurring = false;	// If = true, then activity is occurring so all messages should be logged

	/**
	 * This initialize automation is called from the AutomationManager.  Since a web service connection is not provided
	 * one will be retrieved from the connection pool and then passed to the initialize method as a parameter.
	 */
	@Override
	public void initializeAutomation()
	{
		// Get the web service to be used for the attempted GUID values from web service
		VirageMediaBinServerSoap ws = null;
		try
		{
			ws = MediaBinConnectionPool.getPoolConnection("InitializeAutomation");
			initializeAutomation(ws);
		}
		catch ( Exception e )
		{
			ExceptionHelper.logExceptionToFile("initializeAutomation (AUTOMATIONFolderStructure)", e);			
		}
		finally
		{
			if ( ws != null )
			{
				MediaBinConnectionPool.returnPoolConnection(ws, "InitializeAutomation");
			}
		}	        
	}
	
	/**
	 * Entry point to initialize the automation providing a web service connection for initialization usage.
	 * This method should be the entry point for unit tests.
	 */
	public void initializeAutomation(VirageMediaBinServerSoap ws)
	{
		try
		{
			// Use the paths to examine to generate Folder objects for the root folders
			// For each root folder path, find folders that have passed validations
			for (String rootFolder : rootPathsToExamine)
			{
				Folder folder = FolderHelper.getFolderForPath(ws, BrandFolderGUIDS.getMappings().get(rootFolder));
				rootFolders.add(folder);
			}
		}
		catch (RemoteException e)
		{
			ExceptionHelper.logExceptionToFile("initializeAutomation(ws) (AUTOMATIONFolderStructure)", e);
		}
	}

	@Override
	public String getAutomationName()
	{
		return "TaskGroupAutomation";
	}

	public static void logAutomationMsg(String message)
	{
		logAutomationMsg(message, false);
	}
	public static void logAutomationMsg(String message, boolean includeCurrentTime)
	{
		if (automationLog == null)
		{
			return;
		}
		// If not hourly logging, the first message forces the header to be logged
		if (!hourlyLogging && !activityOccurring)
		{
			automationLog.info("  ");
			automationLog.info(OutputFormatters.getCurrentDateFormat6() + ": START Cycle (" + folderIterationCounter + ")");			
		}
		activityOccurring = true;
		if (includeCurrentTime)
		{
			automationLog.info(OutputFormatters.getCurrentDateFormat6() + ": " + message);
		}
		else
		{
			automationLog.info(message);
		}
	}
	/**
	 * Each time the automation is to perform its action, this is the entry point.
	 */
	@Override
	public void performAutomation() throws RemoteException
	{
		preparingForProcessing = true;
		VirageMediaBinServerSoap ws = null;
		

		try
		{
//			startTiming();
			// Determine if this automation cycle should log details
			RuntimeProperties props = new RuntimeProperties();
			String logFile = props.getProperty(RuntimeProperties.AUTOMATION_LOG);
			if (logFile == null || onlyGatherAssets)
			{
				automationLog = null;
			}
			else
			{
				automationLog = new JostensLogFile(SharedFolderPath.getLogFilePath() + logFile);
			}
			cycleStartTime = System.currentTimeMillis();
			activityOccurring = false;
			
			// Create a processor and process assets
			ws = MediaBinConnectionPool.getPoolConnection("TaskGroupAutomation");
		
			// For all the rootFolders, the child folders need to be found.  There is some
			// validation based on folder name to determine if a folder should be examined for .cdr assets.
			// Because these don't change often and finding them is expensive, there is a counter for
			// a number of iterations before child folders are found.
			if (folderIterationCounter == 0)
			{
				hourlyLogging = true;		// Force hourly logging and log the START cycle message
				logAutomationMsg("  ");
				logAutomationMsg("START Cycle (" + folderIterationCounter + ")", true);
				findChildFolders(ws);
				folderIterationCounter = FOLDER_ITERATIONS_BETWEEN;
			}
			else
			{
				hourlyLogging = false;		// Clear hourly logging
				folderIterationCounter--;
			}
	
			// Update the folderAssets Map based on current child folders
//			if (assetIterationCounter == 0)
//			{
//				taskAssets.clear();
//				assetIterationCounter = ASSET_ITERATIONS_BETWEEN;
//			}
//			else
//			{
//				assetIterationCounter--;
//			}
			
			// Reset all existing TaskAssets and failure assets
			resetTaskAssets();
			failureAssets.clear();

//			System.out.println(" #1=" + stopTimingString());
//			startTiming();
			// Perform search for all assets in folders
			searchForFolderAssets(ws);			
//			System.out.println(" #1A=" + stopTimingString());
//			startTiming();

			// Remove any task assets that aren't marked as active.  Must no longer be found in a MB folder.
			removeInactiveAssets();
//			System.out.println(" #1A2=" + stopTimingString());
//			startTiming();
			
			// Inspect all TaskAssets and load metadata from the Corel Draw file for those that haven't had that done previously.  This 
			// loading only happens on the .cdr file. Set a flag on TaskAsset indicating metadata loaded
			loadCorelDrawAttributes(ws);
//			System.out.println(" #1B=" + stopTimingString());
//			startTiming();
			
			// Remaining assets are active, so perform initialization on them
			initializeTaskAssets(ws);

//			System.out.println(" #1B2=" + stopTimingString());
//			startTiming();

			// Validate all assets have an asset class value on them - Only look at task asset
			validateAssetClass(ws, false);
//			System.out.println(" #1C=" + stopTimingString());
//			startTiming();
			
			// Check that all assets have their catalog package loaded
			setPackageAssets(ws);

			// Check if a MD_RESET is on status automation
			checkForMDReset(ws);
//			System.out.println(" #1D=" + stopTimingString());
//			startTiming();

			// Validate all assets in package have an asset class value on them - Only look at package assets
//			validateAssetClass(ws, true);

//			// For new assets only - Load the possible corel draw metadata fields from the master cdr and 
//			// assure those values are on all assets in the package exactly
//			checkMetadataOnPackageAssets(ws);
//			System.out.println(" #1D2=" + stopTimingString());
//			startTiming();

			// Need to load required metadata defined by the comparator on the TaskAssets 
			MetadataHelper.populateAssetMetadata(ws, (Collection)taskAssets.values(), TaskAssetComparator.COMPARATOR_METADATA);
//			System.out.println(" #1D3=" + stopTimingString());
//			startTiming();

			// Return the collection used to find assets
			MediaBinConnectionPool.returnPoolConnection(ws, "TaskGroupAutomation");
			ws = null;

//			System.out.println(" #2=" + stopTimingString());
//			startTiming();
			// Setup required email objects
			setupForEmail();
		
			// If a unit test has instructed to only gather assets, return now
			if (onlyGatherAssets)
			{
				logFinish(1);
				return;
			}

			// Remove Task Assets that for some reason don't have package assets processed.  This happens for an unknown
			// reason and need to remove that task assets so it starts the process over.  Seemed like the easiest solution.
			removeNoPackageAssets();
			
			// Find assets that need to be processed
			findToProcessAssets();
			
//			System.out.println(" To PROCESS ASSETS = " + assetsToProcess.size());
	
			// If there are no assets to process for this iteration, just return.
			if (assetsToProcess.isEmpty())
			{
				preparingForProcessing = false;
				// Build task group complete List so assets are available for other automations to process
				buildTaskGroupCompleteAssets();
				logFinish(2);
				return;
			}
			
			// Sort to process assets by priority
			prioritizeToProcessAssets();

//			System.out.println(" #3=" + stopTimingString());
//			startTiming();

		// The assets to process List contains the work that should be performed this cycle.  These assets will be
		// passed to a TaskGroupProcessor for processing.  The number of assets that can be processed each cycle is
		// limited to a defined constant.  There is also a limited number of concurrent processing that can occur
		// defined by another constant.
//		addStatusMessage("Assets to Process: " + assetsToProcess.size());

			preparingForProcessing = false;
			
			// Process TaskAssets through processors
			currentlyProcessing = true;
			processTaskAssets();
			currentlyProcessing = false;
			
			// Send any emails if needed
			sendEmail();
	
			// Build task group complete List so assets are available for other automations to process
			buildTaskGroupCompleteAssets();

			logFinish(3);
//			System.out.println(" #4=" + stopTimingString());

		} 
		catch (Exception e)
		{
			ExceptionHelper.logExceptionToFile("performAutomation (AUTOMATIONFolderStructure)", e);
			//System.out.println("Exception during automation folder structure processing. " + ExceptionHelper.getStackTraceAsString(e));
		}
		finally
		{
			if (ws != null)
			{
				MediaBinConnectionPool.returnPoolConnection(ws, "TaskGroupAutomation");
			}
		}
	}
	
	private void logFinish(int where)
	{
		long diffTime = System.currentTimeMillis() - cycleStartTime;
		if (activityOccurring)
		{	// Activity occurred requiring logging, so log the Finish cycle message
			logAutomationMsg("FINISH Cycle (" + where + ") " + Converters.convertLongDateToString(diffTime, true) + "  -  Assets (" + taskAssets.size() + ")", true);			
		}
		if (automationLog != null)
		{
			automationLog.closeLog();
		}
	}

	// Build the List of task group complete assets so other automations have assets to process
	private void buildTaskGroupCompleteAssets() throws RemoteException
	{
		synchronized (taskGroupCompleteAssets)
		{
			taskGroupCompleteAssets.clear();
			for (TaskAsset taskAsset : taskAssets.values())
			{
				if (!taskAsset.getStatusAutomation().contains("PR_") || taskAsset.getStatusAutomation().contains(StatusAutomation.PR_CATALOGER))
				{
					continue;
				}
				TaskAsset duplicateAsset = new TaskAsset(taskAsset);
				duplicateAsset.setFolderName(taskAsset.getFolderName());
				duplicateAsset.setFolder(taskAsset.getFolder());
				duplicateAsset.setStatusAutomation(taskAsset.getStatusAutomation());
				taskGroupCompleteAssets.add(duplicateAsset);
			}
			
			// Now sort assetsToProcess
			Collections.sort(taskGroupCompleteAssets, new TaskAssetComparator());		
		}
	}

	protected void performStopActions()
	{
		if (MediaBinConnectionPool.getUrlForJunit() == null)
		{	// Only clear folders for non unit test executions
			taskAssets.clear();		// Clear folder assets and reset interation counter
			folderIterationCounter = 0;
//			assetIterationCounter = 0;
		}
	}

	protected int getStartupDelay()
	{
		return 0;
	}


	public List<Folder> getRootFolders()
	{
		return rootFolders;
	}

	/**
	 * For rootFolders, find child folders if iteration count indicates the list should be generated
	 */
	public void findChildFolders(VirageMediaBinServerSoap ws) throws RemoteException
	{
		for (Folder rootFolder : rootFolders)
		{
			// Clear previous child folders
			rootFolder.getChildFolders().clear();
			// Find child folders using helper (not recursive so it examines 1 folder level below)
			FolderHelper.findChildFolders(ws, rootFolder, false);
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
				if (limitFoldersTo != null && !childFolderName.equals(limitFoldersTo))
				{
					iter.remove();
					continue;
				}
			}
		}
	}
	
	/**
	 * Reset all task assets
	 */
	public void resetTaskAssets()
	{
		// For all current folder assets mark them as reset
		Collection<TaskAsset> currentAssets = taskAssets.values();
		for (TaskAsset asset : currentAssets)
		{
			// If asset is in a failure state, don't reset
			if (asset.isFailure())
			{
				continue;
			}
			asset.resetTaskAsset();
		}		
	}

	/**
	 * Initialize all active task assets - Existing metadata is cleared, then some
	 * base metadata is loaded onto the .cdr's found in taskAssets list.
	 */
	public void initializeTaskAssets(VirageMediaBinServerSoap ws) throws RemoteException
	{
		String[] metadataToLoad = {MetadataGUIDS.ASSET_CLASS, MetadataGUIDS.STATUS_AUTOMATION};
		
		Collection<TaskAsset> currentAssets = taskAssets.values();
		for (TaskAsset asset : currentAssets)
		{
			if (asset.isNewAsset())
			{
				continue;
			}
			asset.getAssetMetadata().clear();
			MetadataHelper.populateAssetMetadata(ws, asset, metadataToLoad);
			asset.setStartingStatusAutomation(asset.getStatusAutomation());
			// If package assets are found, load metadata on them also
			if (!asset.getPackageAssets().isEmpty())
			{
				// If package assets don't have ASSET_CLASS on them, add it now.  This should only be needed once.
				for (BaseAsset ba : asset.getPackageAssets())
				{
					if (ba.getMetaDataValue(MetadataGUIDS.ASSET_CLASS) == null)
					{
						ba.addAssetMetadata(MetadataGUIDS.ASSET_CLASS, ba.getAssetMetadata().get(MetadataGUIDS.ASSET_CLASS));
					}
				}
			}
			// If status automation contains FAILURE, then add to failures List
			if (asset.getStartingStatusAutomation().contains("FAILURE:"))
			{
				failureAssets.add(asset);
//				asset.setFailure(true);
			}
		}
		
	}

	/**
	 * Find all .cdr's in the current child folders list and update folderAssets accordingly
	 */
	public void searchForFolderAssets(VirageMediaBinServerSoap ws) throws RemoteException
	{
		
		for (Folder rootFolder : rootFolders)
		{
			FolderHelper.getChildFolderGUIDS(rootFolder);
			String childFolderGUIDS = rootFolder.getChildrenAsString();
			IDOLSearchHelper idolSearchHelper = new IDOLSearchHelper();
			Set<BaseAsset> assets = idolSearchHelper.findInFolderAssetsMatchingNamePattern(childFolderGUIDS, "*.cdr");
			if (assets.isEmpty())
			{
				continue;
			}
			// Look at each asset and determine if it was previously found or not
			for (BaseAsset asset : assets)
			{
				// Check current status automation value, if this value contains PR_PROCESSING, this asset should not be set
				// to active.  At this point just continue, so no processing can happen with asset.  Doing this will cause it to removed from TaskAssets
				String statusAutomation = AutomationsHelper.getStatusAutomation(ws, asset.getAssetId());
				if (statusAutomation != null && statusAutomation.contains(StatusAutomation.PR_PROCESSING))
				{
					continue;		// Asset is processing so don't examine any further
				}

				TaskAsset taskAsset = taskAssets.get(asset.getAssetId());
				if (taskAsset == null)
				{	// This asset has not been previously found - so need a new TaskAsset
					taskAsset = new TaskAsset(asset);
					String parentFolderGUID = AssetRevisionsHelper.getParentGUID(ws, asset);
					// Find the parent Folder from the parent GUID
					for (Folder childFolder : rootFolder.getChildFolders())
					{
						if (parentFolderGUID.equals(childFolder.getGUID()))
						{
							taskAsset.setFolder(childFolder);
						}
					}
					
					// If only gathering assets, don't set New Asset flag. Want all methods to execute.
					if (onlyGatherAssets)
					{
						taskAsset.setNewAsset(false);
					}
					
					taskAssets.put(asset.getAssetId(), taskAsset);
					logAutomationMsg("[searchForFolderAssets] New Asset " + taskAsset.getPathInformation());
				}
				taskAsset.setActive(true);
				// Set some needed values on the task asset for this processing loop
				int currentRevision = AssetRevisionsHelper.getCurrentAssetRevision(ws, asset);		// Find current revision of the asset
				taskAsset.setCurrentRevision(currentRevision);
				// Find all assets in the package
				
				
//				CatalogAssetPackage catalogPackage = CatalogAssetPackageHelper.getCatalogAssetPackage(ws, taskAsset.getAssetId(), false);
//				List<BaseAsset> assetList =  catalogPackage.getPackageAssets();
//				taskAsset.setPackageAssets(assetList);
//				// Get the current status automation on the asset
//				MetadataHelper.populateAssetMetadata(ws, taskAsset, loadMetadata);
//				taskAsset.setStartingStatusAutomation(taskAsset.getMetaDataValue(MetadataGUIDS.STATUS_AUTOMATION));
			}
		}
	}
	
	/**
	 * Process the TaskAssets that have been found to be needing to processed.  Each thread will get it's own
	 * connection from the pool.
	 */
	public void processTaskAssets() throws RemoteException
	{
		for (TaskAsset taskAsset : assetsToProcess)
		{
			logAutomationMsg("[TO PROCESS] " + taskAsset.getPathInformation());
		}
//		System.out.println("PROCESSTASKASSETS: " + assetsToProcess.size());
		// Calculate the number of processors to start.  No sense starting them all if not enough assets need to be processed.
		int neededProcessors = TASK_GROUP_PROCESSORS;
		if (assetsToProcess.size() < neededProcessors)
		{
			neededProcessors = assetsToProcess.size();
		}
		
		// Start all the TaskGroupProcessor Threads
		List<TaskGroupProcessor> processors = new ArrayList<TaskGroupProcessor>();
		for (int i = 0; i < neededProcessors; i++)
		{
			// Get a web service from the connection pool to process the assets
			VirageMediaBinServerSoap ws = MediaBinConnectionPool.getPoolConnection("ProcessTaskAssets");
			TaskGroupProcessor processor = new TaskGroupProcessor(i);
			processors.add(processor);
			processor.setWs(ws);
			processor.start();
		}

		for (TaskAsset taskAsset : assetsToProcess)
		{
			boolean processorFound = false;
			while (true)
			{
				// If process was interrupted, stop finding processors for assets
				if (interruptHappened())
				{
					break;
				}
				
				// Find a processor to process the TaskAsset
				for (TaskGroupProcessor processor : processors)
				{
					if (processor.isNotAvailable())
					{	// Processor is not available
						continue;
					}
					processorFound = true;
					processor.setTaskAsset(taskAsset);
					break;
				}
				
				if (processorFound)
				{
					break;
				}
				
				try
				{
					Thread.sleep(10000);
				} 
				catch (InterruptedException e)
				{
				}
			}
		}
		
		// Alert threads that no more TaskAssets will be processed
		for (TaskGroupProcessor processor : processors)
		{
			processor.finishProcessing();
		}

		// Loop to check if all threads have stopped
		boolean allStopped = true;
		boolean processorsInterrupted = false;
		while (true)
		{
			// If process is interrupted, stop the processors early.
			if (interruptHappened() && !processorsInterrupted)
			{
				for (TaskGroupProcessor processor : processors)
				{
					processor.interrupt();
				}
				processorsInterrupted = true;
			}
			
			allStopped = true;
			for (TaskGroupProcessor processor : processors)
			{
				if (processor.isAlive())
				{
					allStopped = false;
				}
			}
			if (allStopped)
			{
				break;
			}
			try
			{
				Thread.sleep(2000);
			} 
			catch (InterruptedException e)
			{
				return;
			}
		}

		// Return all connections to the pool
		for (TaskGroupProcessor processor : processors)
		{
			if (processor.getThreadNumber() > neededProcessors)
			{
				continue;		// Don't return connection from this processor
			}
			VirageMediaBinServerSoap ws = processor.getWs();
			MediaBinConnectionPool.returnPoolConnection(ws, "ProcessTaskAssets");
		}
		
	}
	
	/**
	 * Examine TaskAssets found in folderAssets and remove any that haven't been marked as active.  This means the asset
	 * is not found in a folder.
	 */
	public void removeInactiveAssets()
	{
		Iterator<Entry<String, TaskAsset>> iter = taskAssets.entrySet().iterator();
		while (iter.hasNext())
		{
			Entry<String, TaskAsset> entry = iter.next();
			TaskAsset asset = entry.getValue();
			if (!asset.isActive())
			{
				logAutomationMsg("[removeInactiveAssets] " + asset.getPathInformation());
				iter.remove();
			}
		}
	}

	/**
	 * Examine TaskAssets found in folderAssets and remove any that don't have valid package assets.  This means something didn't go 
	 * correct when the package assets were loaded.
	 */
	public void removeNoPackageAssets()
	{
		Iterator<Entry<String, TaskAsset>> iter = taskAssets.entrySet().iterator();
		while (iter.hasNext())
		{
			Entry<String, TaskAsset> entry = iter.next();
			TaskAsset asset = entry.getValue();
			if (asset.getPackageAssets().isEmpty() && !asset.isNewAsset() && !asset.isFailure())
			{
				logAutomationMsg("[removeNoPackageAssets] " + asset.getPathInformation());
				ExceptionHelper.logIssueToFile("removeNoPackageAssets", "No Package Assets: " + asset.getPathInformation());
				iter.remove();
			}
		}
	}

	/**
	 * Find any assets that need to be processed.  This is based on current revision is larger than last processed revision.
	 */
	public void findToProcessAssets()
	{
		assetsToProcess.clear();
		int processedCount = 0;
		for (TaskAsset taskAsset : taskAssets.values())
		{
			if (taskAsset.shouldTaskAssetBeProcessed())
			{
				assetsToProcess.add(taskAsset);
				taskAsset.setProcessingStatus("Waiting To Process");
				processedCount++;
			}
			if (processedCount >= TASK_ASSETS_MAX_PROCESSED)
			{
				break;
			}
		}
	}

	/**
	 * Prepare email objects that tasks can use for sending emails
	 */
	public void setupForEmail()
	{
		AUTOMATIONFolderStructure.AIEmail = new ArtistIngestEmail();
		AUTOMATIONFolderStructure.CFEmail = new CatalogerFormEmail();
	}
	public void sendEmail()
	{
		AUTOMATIONFolderStructure.AIEmail.sendCurlEmail();
		AUTOMATIONFolderStructure.CFEmail.sendCurlEmail();
	}
	
	/**
	 * Use the TaskAssetComparator to prioritize the assetsToProcess
	 */
	public void prioritizeToProcessAssets() throws RemoteException
	{
		// Now sort assetsToProcess
		Collections.sort(assetsToProcess, new TaskAssetComparator());
	}

	public Map<String, TaskAsset> getTaskAssets()
	{
		return taskAssets;
	}
	
	public List<TaskAsset> getAssetsToProcess()
	{
		return assetsToProcess;
	}

	public List<String> getStatusMessages()
	{
		clearStatusMessage();
		if (isInterrupted() || !isAlive())
		{
			List<String> statusMessages = new ArrayList<String>();
			statusMessages.add(OutputFormatters.getCurrentDateFormat1() + ": "  + getAutomationName() + " Finished, has been instructed to stop");
			return statusMessages;
		}
		else if (preparingForProcessing)
		{
			addStatusMessage("Preparing to process assets.");
		}
		else if (assetsToProcess.isEmpty())
		{
			addStatusMessage("Assets in folders: " + taskAssets.size());			
		}
		else
		{
			addStatusMessage("Assets to Process: " + assetsToProcess.size());
			for (TaskAsset taskAsset : assetsToProcess)
			{
				addStatusMessage(taskAsset.getProcessingStatus());
			}
		}
		if (!preparingForProcessing && !currentlyProcessing)
		{
			// TODO: Look at failures array for messages
			List<String> failures = new ArrayList<String>();
			for (TaskAsset taskAsset : failureAssets)
			{
				String statusAutomation = taskAsset.getStatusAutomation();
				int i = statusAutomation.indexOf("FAILURE");
				if (i > -1)
				{
					failures.add(taskAsset.getPathInformation() + " : " + statusAutomation.substring(i));
				}
			}
			if (!failures.isEmpty())
			{
				addStatusMessage("--- FAILURES ---");
				for (String failure : failures)
				{
					addStatusMessage(failure);
				}
			}
		}
		
		return super.getStatusMessages();
	}

	public static List<TaskAsset> getTaskGroupCompleteAssets()
	{
		synchronized (taskGroupCompleteAssets)
		{
			// Build a new List of of TaskAssets to return to caller.  TaskGroupCompleteAssets has already been sorted.
			List<TaskAsset> returnAssets = new ArrayList<TaskAsset>();
			for (TaskAsset taskAsset : taskGroupCompleteAssets)
			{
				TaskAsset duplicateAsset = new TaskAsset(taskAsset);
				duplicateAsset.setFolderName(taskAsset.getFolderName());
				duplicateAsset.setFolder(taskAsset.getFolder());
				duplicateAsset.setStatusAutomation(taskAsset.getStatusAutomation());
				returnAssets.add(duplicateAsset);
			}
			return returnAssets;
		}
	}
	
	public static void removeTaskGroupCompleteAsset(TaskAsset asset)
	{
		synchronized (taskGroupCompleteAssets)
		{
			Iterator<TaskAsset> iter = taskGroupCompleteAssets.iterator();
			while (iter.hasNext())
			{
				TaskAsset iterAsset = iter.next();
				if (iterAsset.getAssetId().equals(asset.getAssetId()))
				{
					iter.remove();
					return;
				}
			}
		}		
	}
	
	public void setLimitFoldersTo(String limitFoldersTo)
	{
		this.limitFoldersTo = limitFoldersTo;
	}

	/**
	 * For any TaskAsset without an asset class, attempt to find a corel draw text property file and
	 * load those properties on the .cdr asset
	 */
	public void loadCorelDrawAttributes(VirageMediaBinServerSoap ws) throws RemoteException
	{
		// For all current folder assets mark them as reset
		Collection<TaskAsset> currentAssets = taskAssets.values();
		for (TaskAsset asset : currentAssets)
		{
			if (asset.isNewAsset())
			{
				continue;
			}
			if (asset.isMetadataLoaded())
			{
				continue;		// Corel Draw attributes have already been loaded on this asset
			}
			
			// Attempt to load metadata in .cdr from corel draw property file
			MetadataCorelDrawHelper helper = new MetadataCorelDrawHelper(asset);
			if (!helper.isValidProperties())
			{
				continue;			// No property file found
			}
			Map<String, MBMetadataParameter> loadedMetadataParameters = helper.getMetadataParameters();
			// Put all values from property file on task asset
			asset.setAssetMetadata(loadedMetadataParameters);
			// Determine if any of these metadata fields need to be saved to the asset
			BaseAsset ba = new BaseAsset("", asset.getAssetId());
			MetadataHelper.populateAssetMetadata(ws, ba, loadedMetadataParameters.keySet().toArray(new String[0]));

	        Iterator<Map.Entry<String, MBMetadataParameter>> iter = asset.getAssetMetadata().entrySet().iterator();
	        while (iter.hasNext())
	        {
	        	Map.Entry<String, MBMetadataParameter> entry = iter.next();
	        	String guid = entry.getKey();

				// If matching value found for both TaskAsset and current metadata found in base asset, then value already set
				// on asset so remove from what needs to be updated
				String taskValue = asset.getMetaDataValue(guid);
				String existingValue = ba.getMetaDataValue(guid);
				if (taskValue.equals(existingValue))
				{
					// Already there, so no need to update again - Remove GUID from asset metadata values
					iter.remove();
//					asset.getAssetMetadata().remove(guid);
				}
			}
			
			if (!asset.getAssetMetadata().isEmpty())
			{
				// Update needed
				MetadataHelper.updateMetaData(ws, asset, asset.getAssetMetadata());
			}
			asset.setMetadataLoaded(true);
		}		
	}
	
	/**
	 * Perform a validation that AssetClass is populated correctly on the task asset.  If not
	 * mark with a failure and store that information.
	 * 
	 * testAssetPackage - if false, the task asset is checked
	 * 					  if true, the package assets are checked
	 */
	public void validateAssetClass(VirageMediaBinServerSoap ws, boolean testAssetPackage) throws RemoteException
	{
		String failureMSG = "FAILURE: Missing Asset Class";
		Iterator<Entry<String, TaskAsset>> iter = taskAssets.entrySet().iterator();
		while (iter.hasNext())
		{
			Entry<String, TaskAsset> entry = iter.next();
			TaskAsset asset = entry.getValue();
			
			if (asset.isNewAsset())
			{
				continue;		// No new asset processing
			}

			// asset class is missing and this is the same error as previous execution so nothing to do until resolved
			if (failureMSG.equals(asset.getStartingStatusAutomation()) && asset.getAssetClass() == null)
			{
				iter.remove();
				continue;		
			}
			// There was a missing asset class on asset package which has been resolved, so remove
			// status automation from asset, remove from further processing this cycle
			if (failureMSG.equals(asset.getStartingStatusAutomation()) && asset.getAssetClass() != null)
			{
				AutomationsHelper.removeStatusAutomation(ws, asset.getAssetId());
				iter.remove();
				continue;		// Failure already marked on asset
			}
			if (!testAssetPackage && asset.getAssetClass() != null)
			{
				continue;		// Asset class found so all is ok
			}
			if (testAssetPackage)
			{
				boolean allHaveAssetClass = true;
				for (BaseAsset ba : asset.getPackageAssets())
				{
					String assetClass = ba.getMetaDataValue(MetadataGUIDS.ASSET_CLASS);
					if (assetClass == null)
					{
						allHaveAssetClass = false;
						break;
					}
				}
				if (allHaveAssetClass)
				{
					continue;
				}
			}
			// Set the error message into Status Automation
			AutomationsHelper.setStatusAutomation(ws, asset.getAssetId(), failureMSG);
			asset.setStatusAutomation(failureMSG);
			asset.setFailure(true);
			failureAssets.add(asset);
			iter.remove();
		}
	}
	
	/**
	 * For any TaskAsset to not have package assets loaded, load them at this time
	 */
	public void setPackageAssets(VirageMediaBinServerSoap ws) throws RemoteException
	{
		String failureMSG = "FAILURE: Catalog - ";
//		boolean showAlreadyLoaded = false;
//		if (assetIterationCounter % 10 == 0)
//		{
//			showAlreadyLoaded = true;		// Every 10 cycles show already loaded package count
//		}
		Collection<TaskAsset> currentAssets = taskAssets.values();
		for (TaskAsset asset : currentAssets)
		{
			if (asset.isNewAsset())
			{
				continue;		// No new asset processing
			}

			if (!asset.getPackageAssets().isEmpty())
			{
//				if (showAlreadyLoaded)
//				{
//					logAutomationMsg("[setPackageAssets] Already Loaded (" + asset.getPackageAssets().size() + ") " + asset.getPathInformation());
//				}
				continue;		// Package assets already found
			}
			CatalogAssetPackage catalogPackage = CatalogAssetPackageHelper.getCatalogAssetPackage(ws, asset.getAssetId(), false);
			// Is the just found package valid?
			if (catalogPackage.isValid())
			{
				// Package is now valid, was there previously a catalog failure.  If so, remove status automation and process no more
				// until the next cycle.
				if (asset.getStartingStatusAutomation().startsWith(failureMSG))
				{
					AutomationsHelper.removeStatusAutomation(ws, asset.getAssetId());
					continue;
				}
				// Package assets have been found in a valid package so processing can continue
				asset.setPackageAssets(catalogPackage.getPackageAssets());
				// Call method to check that loaded Corel Draw data has been placed on all assets in package
				checkMetadataOnPackageAssets(ws, asset);
				logAutomationMsg("[setPackageAssets] Loaded (" + asset.getPackageAssets().size() + ") " + asset.getPathInformation());
				asset.setFailure(false);	// Re-set any failures that could have been caused by loading packages
				continue;
			}

			String failure = failureMSG + catalogPackage.getErrorCondition();
			if (!failure.equals(asset.getStartingStatusAutomation()))
			{	// Different status automation value, update to be performed
				// Set the error message into Status Automation
				AutomationsHelper.setStatusAutomation(ws, asset.getAssetId(), failure);
				asset.setStatusAutomation(failure);
				failureAssets.add(asset);
			}
			// Mark the asset as a failure
			asset.setFailure(true);
			asset.assetWasProcessed();
		} 
	}
	
	/**
	 * Once Corel Draw metadata has been loaded onto the .cdr, this method validates that metdata is correct
	 * on all other assets in the package.
	 */
	public void checkMetadataOnPackageAssets(VirageMediaBinServerSoap ws, TaskAsset asset) throws RemoteException
	{
		String[] dataToLoad = MetadataCorelDrawHelper.VALID_ATTRIBUTES.toArray(new String[0]);
		Map<String,MBMetadataParameter> mdMap = new HashMap<String,MBMetadataParameter>();
		
		MetadataHelper.populateAssetMetadata(ws, asset, dataToLoad);
		MetadataHelper.populateAssetMetadata(ws, asset.getPackageAssets(), dataToLoad);
		
		for (BaseAsset ba : asset.getPackageAssets())
		{
			if (asset.getAssetName().equals(ba.getAssetName()))
			{
				continue;		// Don't check the taskasset
			}
			mdMap.clear();
			// Search all valid attributes
			for (String attr : dataToLoad)
			{
				String assetValue = asset.getMetaDataValue(attr);
				if (assetValue == null)
				{
					continue;		// Attribute not present
				}
				String baValue = ba.getMetaDataValue(attr);
				if (assetValue.equals(baValue))
				{
					continue;		// Values match
				}
				MBMetadataParameter param = asset.getAssetMetadata().get(attr);
				mdMap.put(attr, param);
			}
			if (mdMap.isEmpty())
			{
				continue;	// Nothing to update
			}
			MetadataHelper.updateMetaData(ws, ba, mdMap);
		}
	}
	
	/**
	 * For any TaskAsset with a starting status automation of MD_RESET, remove all metadata on asset package
	 */
	public void checkForMDReset(VirageMediaBinServerSoap ws) throws RemoteException
	{
		// Cannot remove all metadata so build a string array that can have some values removed
		List<String> allGUIDS = new ArrayList<String>();
		allGUIDS.addAll(MetadataNames.ALL_DEFINED_GUIDS_LIST);
		// Only field to keep is Asset Class - All others including version scenario should be re-populated
		allGUIDS.remove(MetadataGUIDS.ASSET_CLASS);
		
		String[] guidsToRemove = allGUIDS.toArray(new String[0]);

		// For all current folder assets mark them as reset
		Iterator<Entry<String, TaskAsset>> iter = taskAssets.entrySet().iterator();
		while (iter.hasNext())
		{
			Entry<String, TaskAsset> entry = iter.next();
			TaskAsset asset = entry.getValue();

			if (asset.isNewAsset())
			{
				continue;
			}
			if (asset.getStartingStatusAutomation() == null || !asset.getStartingStatusAutomation().contains("MD_RESET"))
			{
				continue;		// No MD_RESET setting
			}

			for (BaseAsset ba : asset.getPackageAssets())
			{
				MetadataHelper.removeMetaData(ws, ba, guidsToRemove);
			}
			iter.remove();
		}
	}

	public void setOnlyGatherAssets(boolean onlyGatherAssets)
	{
		this.onlyGatherAssets = onlyGatherAssets;
	}
	
	//
	// The following methods provide information for the upload QC email.  These methods will help give an indication of system load.
	//
	public static int getUserAssetCount(String userName)
	{
		Collection<TaskAsset> assets = taskAssets.values();
		int cnt = 0;
		for (TaskAsset taskAsset : assets)
		{
			if (taskAsset.getFolderName() != null && taskAsset.getFolderName().equals(userName))
			{
				cnt++;
			}
		}
		return cnt;
	}
	public static int getAllUserAssetCount()
	{
		return taskAssets.size();
	}

	protected void startTiming()
	{
		startTime = Calendar.getInstance().getTimeInMillis();
	}
	protected String stopTimingString()
	{
		stopTime = Calendar.getInstance().getTimeInMillis();
		float f = (float)(stopTime-startTime)/1000;
		String timing = String.format("Processing Time - %.2f Seconds\n", f);
		return timing;
	}

	@Override
	protected String getLogFileName()
	{
		return null;
	}

}

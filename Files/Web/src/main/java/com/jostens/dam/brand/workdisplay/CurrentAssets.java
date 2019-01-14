package com.jostens.dam.brand.workdisplay;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jostens.dam.brand.web.HTMLHelper;
import com.jostens.dam.shared.assetclass.BaseAsset;
import com.jostens.dam.shared.assetclass.BaseAssetRevision;
import com.jostens.dam.shared.assetclass.MetadataGUIDS;
import com.jostens.dam.shared.automations.BaseThread;
import com.jostens.dam.shared.common.CalendarAdjust;
import com.jostens.dam.shared.common.Folder;
import com.jostens.dam.shared.common.MediaBinConnectionPool;
import com.jostens.dam.shared.common.OutputFormatters;
import com.jostens.dam.shared.helpers.AssetRevisionsHelper;
import com.jostens.dam.shared.helpers.MetadataHelper;
import com.jostens.dam.shared.idol.IDOLSearchHelper;
import com.viragemediabin.www.VirageMediaBinServerSoap;

public class CurrentAssets extends BaseThread
{
	// Static name that HTML is stored using in the cache
	public static final String CURRENT_ASSETS_HTML = "CurrentAssets";
	
	// Static value used to track servlet hits
	private static Integer servletHits = 0;

	// Temporary assets is the asset list being built during a cycle processing
	private Map<String, DisplayAsset> temporaryAssets = new HashMap<String, DisplayAsset>();
	
	// Attributes maintained by this object to help in maintaining static information
	private CatalogFolders catalogFolders = null;

	// Static Values that are being updated on a regular schedule and have all the information needed to have
	// the WorkDisplay page through AJAX calls display correctly.  Next 2 maps, the KEY = asset GUID, VALUE = DisplayAsset
	private static Map<String, DisplayAsset> inProgressAssets = new HashMap<String, DisplayAsset>();		// Assets currently working through automation's

	// Bucketsmanager object for use throughout the class
	private BucketsManager bmgr = new BucketsManager();

	@Override
	protected int getSleepSeconds()
	{
		return 60;		// Sleep for 60 seconds between iterations
	}

	@Override
	protected void initializeThread()
	{
		// No initialization code required
	}

	@Override
	protected void cleanupThread()
	{
		// No cleanup code required
	}

	@Override
	protected void performThreadAction()
	{
		System.out.println(OutputFormatters.getCurrentDateFormat6() + "  (" + CurrentAssets.servletHits + ")  (Assets: " + inProgressAssets.size() + ")");

		try
		{
			// Reset servlet hits count
			synchronized(CurrentAssets.servletHits)
			{
				CurrentAssets.servletHits = 0;
			}
			
			// Clear previous assets
			temporaryAssets.clear();
	
			VirageMediaBinServerSoap ws = MediaBinConnectionPool.getPoolConnection("WorkDisplay");
	
			findDisplayAssets(ws);
			
			synchronized(inProgressAssets)
			{
				inProgressAssets.clear();
				inProgressAssets.putAll(temporaryAssets);
			}

			MediaBinConnectionPool.returnPoolConnection(ws, "WorkDisplay");

			// Need to update the cache with most recent information
			String html = getHTMLForCache();
			HTMLHelper.cacheHTML(CURRENT_ASSETS_HTML, html);

		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
		
	}

	/**
	 * This method is where each cycle processing occurs.  Having the primary functionality in a separate
	 * method allows for it to be called from a unit test so timing improvements can be investigated.
	 */
	public void findDisplayAssets(VirageMediaBinServerSoap ws) throws RemoteException
	{
		if (catalogFolders == null)
		{
			catalogFolders = new CatalogFolders();
			catalogFolders.initializeCatalogFolders(ws);				
		}

		// Find child assets for root folders
		for (Folder folder : CatalogFolders.getRootFolders())
		{
			List<BaseAsset> folderAssets = findChildFolderAssets(ws, folder);
			for (BaseAsset ba : folderAssets)
			{
				// Determine if there is an existing inProgressAsset that can be used as is without any further processing
				DisplayAsset inProgressAsset = inProgressAssets.get(ba.getAssetId());
				if (inProgressAsset != null)
				{	// Test 1 passed - in progress asset found
					if (inProgressAsset.getCurrentRevision().equals(ba.getMetaDataValue(MetadataGUIDS.CURRENT_REVISION)))
					{	// Test 2 passed - revision hasn't changed since last processing, this asset can be used
//						System.out.println("Using Previous Asset = " + inProgressAsset.getAssetName());
						inProgressAsset.setChangeThisCycle(false);
						long currentTime = System.currentTimeMillis();
						inProgressAsset.setLastCurrentTime(currentTime);
						temporaryAssets.put(inProgressAsset.getAssetId(), inProgressAsset);
						continue;		// go to next asset without any need to check further
					}
				}
				CalendarAdjust.adjustInsertionTimeFromGMT(ba);

				DisplayAsset da = new DisplayAsset(ba);
				da.setArtistName(ba.getMetaDataValue(MetadataGUIDS.UPLOAD_QC_USER));
				da.setStatusAutomation(ba.getMetaDataValue(MetadataGUIDS.STATUS_AUTOMATION));
				da.setCurrentRevision(ba.getMetaDataValue(MetadataGUIDS.CURRENT_REVISION));
				da.addAssetMetadata(MetadataGUIDS.INSERTION_TIME, ba.getAssetMetadata().get(MetadataGUIDS.INSERTION_TIME));
				da.addAssetMetadata(MetadataGUIDS.JOB_SCHEDULE_PRIORITY, ba.getAssetMetadata().get(MetadataGUIDS.JOB_SCHEDULE_PRIORITY));
				da.setNewAsset(folder.isNewFolder());
				da.setReplaceAsset(folder.isReplacementFolder());
				da.setReviseAsset(folder.isRevisionFolder());
				da.setChangeThisCycle(true);
//				bmgr.addAssetToBucket(da);
				setDisplayAssetTiming(ws, da);
				System.out.println("NAME=" + da.getAssetName());
				temporaryAssets.put(da.getAssetId(), da);
			}
			
		}
		
	}

	/**
	 * Return all assets found for a root Folder as maintained by CatalogFolders object
	 */
	public List<BaseAsset> findChildFolderAssets(VirageMediaBinServerSoap ws, Folder folder) throws RemoteException
	{
		String[] metadataToLoad = {MetadataGUIDS.STATUS_AUTOMATION, MetadataGUIDS.CURRENT_REVISION, MetadataGUIDS.UPLOAD_QC_USER, MetadataGUIDS.INSERTION_TIME, MetadataGUIDS.JOB_SCHEDULE_PRIORITY};
		List<BaseAsset> childAssets = new ArrayList<BaseAsset>();
		
		IDOLSearchHelper idolSearchHelper = new IDOLSearchHelper();
		Set<BaseAsset> assets = idolSearchHelper.findInFolderAssetsMatchingNamePattern(folder.getChildrenAsString(), "*.cdr");

		childAssets.addAll(assets);
		
		MetadataHelper.populateAssetMetadata(ws, childAssets, metadataToLoad);
		
		return childAssets;
	}

	/**
	 * Look at the revisions for the supplied asset to setup timing values which will affect
	 * the display background color for the asset.
	 */
	public void setDisplayAssetTiming(VirageMediaBinServerSoap ws, DisplayAsset da) throws RemoteException
	{
		String[] metadataToLoad = {MetadataGUIDS.STATUS_AUTOMATION, MetadataGUIDS.CURRENT_REVISION, MetadataGUIDS.MODIFICATION_TIME, MetadataGUIDS.INSERTION_TIME};

		long currentTime = System.currentTimeMillis();
//		System.out.println("Current time = " + currentTime);
//		System.out.println("Insertion time = " + da.getMetaDataValue(MetadataGUIDS.INSERTION_TIME));

		// Adjust the insertion time to correct value
//		CalendarAdjust.adjustInsertionTimeFromGMT(da);
		long insertTime = Long.parseLong(da.getMetaDataValue(MetadataGUIDS.INSERTION_TIME));

//		System.out.println("Insertion time = " + insertTime);
		
		long diffFromInsert = (currentTime - insertTime) / 1000;
//		System.out.println("DIFF = " + diffFromInsert);
//		int minutes = (int)(diffFromInsert/60);
//		System.out.println("Minutes = " + minutes);
		da.setTotalTime(diffFromInsert);

		// Set the current time into the display asset
		da.setLastCurrentTime(currentTime);
		
		// Get the revisions for the package
		List<BaseAssetRevision> revisions = AssetRevisionsHelper.getAssetRevisions(ws, da.getAssetId());
		long lastModifiedEnteringStage = 0;		// Last Modified time for when asset entered stage, this value is used to determine in stage time
		int stageRowNumber = -1;		// Start at an invalid value
		for (BaseAssetRevision revision : revisions)
		{
			MetadataHelper.populateAssetRevisionMetadata(ws, revision, revision.getRevisionNumber(), metadataToLoad);
			
			// Modification time should already be adjusted on BaseAssetRevision, so get that value and not use the metadata value
			long modTime = revision.getRevisionTime();

//			System.out.println("Revision #" + revision.getRevisionNumber() + "  " + revision.getRevisionTime() + "   " + 
//					revision.getMetaDataValue(MetadataGUIDS.INSERTION_TIME) + "   " + revision.getMetaDataValue(MetadataGUIDS.STATUS_AUTOMATION));
			String revisionStatusAutomation = revision.getMetaDataValue(MetadataGUIDS.STATUS_AUTOMATION);
			int bucketRow = bmgr.getBucketRow(revisionStatusAutomation);
			if (stageRowNumber == -1)
			{
				// Finding out what the first bucket row is
				stageRowNumber = bucketRow;
				lastModifiedEnteringStage = modTime;
				continue;
			}
			if (bucketRow == stageRowNumber)
			{
				// Same bucket row, but take this revisions modification time
				lastModifiedEnteringStage = modTime;
				continue;
			}
			if (bucketRow != stageRowNumber || revisionStatusAutomation == null)
			{
				// Found a new stage bucket so break out of loop
				break;
			}
		}
		long diffFromStage = (currentTime - lastModifiedEnteringStage) / 1000;
		da.setInStageTime(diffFromStage);

	}

	public String getHTMLForCache() throws RemoteException
	{
		CurrentAssetsComparator comparator = new CurrentAssetsComparator();

		bmgr.resetBuckets();	// Reset previous bucket assets
		
		synchronized(inProgressAssets)
		{
			for (DisplayAsset da : inProgressAssets.values())
			{
				bmgr.addAssetToBucket(da);
			}
		}
		
		String workDisplay = HTMLHelper.getTemplateHTML("/com/jostens/dam/brand/web/CurrentAssetsTemplate.html");
		BucketMap bm = new BucketMap();
		int fromIndex = 0;
		while (true)
		{
			int i = workDisplay.indexOf("[BUCKET_", fromIndex);
			if (i == -1)
			{
				break;		// No more bucket definitions
			}
			
			int row = Integer.parseInt(workDisplay.substring(i+8, i+9));
			int col = Integer.parseInt(workDisplay.substring(i+10, i+11));

			List<DisplayAsset> bucketAssets = bmgr.getBucketAssets(row, col);
			// Sort the bucket assets
			Collections.sort(bucketAssets, comparator);		

			String toReplaceText = "[BUCKET_" + row + "_" + col + "]";
//			System.out.println("BUCKETASSETS= " + toReplaceText + ":" + bucketAssets.size());
			
			workDisplay = workDisplay.replace(toReplaceText, bm.getBucketHTML(bucketAssets));
			
			fromIndex += 12;
		}

//		System.out.println("\n\n" + workDisplay);
		return workDisplay;
	}

	public static void addServletHit()
	{
		synchronized(servletHits)
		{
			CurrentAssets.servletHits++;
		}
	}

	@Override
	protected String getLogFileName()
	{
		return null;
	}

}

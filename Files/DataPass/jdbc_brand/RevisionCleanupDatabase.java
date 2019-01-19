package com.jostens.dam.brand.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.jostens.dam.shared.assetclass.BaseAsset;
import com.jostens.dam.shared.automations.BaseThread;
import com.jostens.dam.shared.common.ExceptionHelper;
import com.jostens.dam.shared.common.MediaBinConnectionPool;
import com.jostens.dam.shared.common.OutputFormatters;
import com.jostens.dam.shared.folders.FolderGUIDS;
import com.jostens.dam.shared.helpers.AssetSearchHelper;
import com.jostens.dam.shared.jdbc.JDBCConnection;
import com.jostens.dam.shared.jdbc.StatementProcessor;
import com.jostens.dam.shared.jdbc.Statements;
import com.viragemediabin.www.MBObjectType;
import com.viragemediabin.www.VirageMediaBinServerSoap;

/**
 * Methods that are used to perform revision cleanup actions.
 * 
 * This code started as a fixit but has been moved into the main source tree so the functionality
 * could be added to a TOD automation.
 */
public class RevisionCleanupDatabase extends BaseThread
{
	private int fullCleanupCount = 0;	// full count of assets found for possible cleanup.  This value is
							// only logged as an informational item.
	
	private boolean performUpdate = true;	// Unit test can set to false to avoid actual processing

	private String stmt = "";
	private List<String> folders = new ArrayList<String>();
	private String tempFolderGUID = null;
	private VirageMediaBinServerSoap ws = null;	// Web service connection for running process in a thread
	
	public void newStatement()
	{
		stmt = "";
		folders.clear();
	}

	/**
	 * Add folder(s) to what is searchable.  This could be already found GUIDS as indicated by starting with '{' or its a constant
	 * definition found in FolderGUIDS.  This method can take one folder definition at a time or a comma list of them
	 */
	public void addFolders(VirageMediaBinServerSoap ws, String folder)
	{
		StringTokenizer st = new StringTokenizer(folder, ",");
		while (st.hasMoreTokens())
		{
			String nextFolder = st.nextToken().trim();
//			System.out.println("NEXTFOLDER=" + nextFolder);
			if (nextFolder.startsWith("{"))
			{
				// Full GUID folder so just add to folders List
				folders.add(nextFolder);
			}
			else if (nextFolder.startsWith("/"))
			{
				// October 2017 - If starts with /Media Database/Store (Repository) -> add '© ' before Store
				// Having difficulties encoding copyright symbol and reading from property file
				if (nextFolder.startsWith("/Media Database/Store (Repository)"))
				{
					nextFolder = nextFolder.replace("Store", "© Store");
				}
				// GUID is a full path
				String GUID = FolderGUIDS.findGuidForFolderPath(ws, nextFolder);
//				System.out.println("NextFolder = " + nextFolder + "  GUID=" + GUID);
				if (GUID != null)
				{
					folders.add(GUID);
				}
			}
			else
			{
				// GUID constant from folder guids
				folders.add(FolderGUIDS.getFolderGuid(nextFolder));
			}
		}
	}
	/**
	 * Return the folders as a comma separated String for inclusion into a SQL statement
	 */
	public String getFolders()
	{
		StringBuffer sb = new StringBuffer();
		
		int cnt = 1;
		for (String folder : folders)
		{
			sb.append("'" + folder + "'");
			if (cnt < folders.size())
			{
				sb.append(", ");
			}
			cnt++;
		}
		return sb.toString();
	}
	
	/**
	 * Define the 2 entry points for setting up a statement that is to be executed.
	 * 		setupStatementForCleanup()	- Call this to get assets that are applicable for revision cleanup.  More than 1 revision only.
	 * 		setupStatementAllAssets()	- Call this to get all assets for date range even if they only have a revision 0.  Nothing to cleanup.
	 * 
	 * Both methods must supply a dateRange which controls the query:
	 * 		dateRange can be a set date like: 03/22/2016 which defines a set date.
	 * 		or set # which indicates how many days previous is the modified date (must be specified as a negative value).
	 */
	public void setupStatementForCleanup(String dateRange)
	{
		setupStatement(true, dateRange);
	}
	public void setupStatementAllAssets(String dateRange)
	{
		setupStatement(false, dateRange);
	}

	/**
	 * Setup the SQL statement for execution.  This will prepare a statement for getting
	 * desired assets for revision cleanup or all asset count.  The SQL statement is controlled by 3 parameters.
	 * @param folders					- Folders as setup by calls to the addFolders statement 
	 * @param multipleRevisionsOnly		- Set to true if only assets with more than 1 revision should be included, required for revision cleanup
	 * @param dateRange					- Date restrictions to impose on the query.  If = to null or a value of 0, there are no restrictions
	 */
	private void setupStatement(boolean multipleRevisionsOnly, String dateRange)
	{
		stmt = Statements.getStatement(BrandStatements.FIND_REVISION_CLEANUP);

		// Should the query only look for assets with multiple revisions.  This would be the case
		// when performing a revision cleanup.  
		if (multipleRevisionsOnly)
		{
			stmt = stmt.replace("[MAX_REVISION]", "and a.maxrevision > 0");
		}
		else
		{
			stmt = stmt.replace("[MAX_REVISION]", "");			
		}
		
		// Setup parent GUIDS
		stmt = stmt.replace("[IMMEDIATE_PARENT_FOLDER_GUID]", getFolders());

		String dateRestriction = "";		// If no date restrictions found, this will clear value in stmt
		if (dateRange != null && !"0".equals(dateRange))
		{
			int i = dateRestriction.indexOf("/");
			if (i > -1)
			{
				// A specific date was supplied that modified date must be prior to
				dateRestriction = "and r.modified < TO_DATE('00:00:00 " + dateRange + "', 'HH24:MI:SS DD/MM/YYYY')";
			}
			else
			{
				// Need to calculate the modification date asset values must be prior to
				GregorianCalendar cal = new GregorianCalendar();
				cal.add(Calendar.DATE, Integer.parseInt(dateRange));
				String earlyDate = OutputFormatters.getDateFormat5(cal.getTimeInMillis());
				dateRestriction = "and r.modified < TO_DATE('00:00:00 " + earlyDate + "', 'HH24:MI:SS DD/MM/YYYY')";
			}
		}
		stmt = stmt.replace("[DATE_RESTRICTION]", dateRestriction);
	}
	
	/**
	 * Execute the statement that has been setup.  The results of the query are sent back as a List of BaseAsset
	 * objects which should have all the information needed to build summaries of the revision cleanup counts and 
	 * can then be passed to the cleanup revision method.
	 */
	public List<BaseAsset> performQuery(int maxProcessedCount)
	{
// Uncomment to see query issued for revision cleanup
//		System.out.println("DO QUERY\n" + stmt);
		List<BaseAsset> cleanupAssets = new ArrayList<BaseAsset>();
		
		Connection c = JDBCConnection.getMediabinConnection();

		try
		{
			StatementProcessor processor = new StatementProcessor(c, stmt);
			ResultSet rs = processor.getResultSet();
			while (rs.next())
			{
				BaseAsset ba = new BaseAsset();
				ba.setAssetNameNoPathCheck(rs.getString(1));		// Asset Name
				ba.setAssetId(rs.getString(2));						// Asset ID
				ba.setFolderName("" + rs.getInt(3));				// Keep the max revision # in folder name
				ba.setFolderGUID(rs.getString(4));					// GUID of where folder is found
				cleanupAssets.add(ba);
				
				if (maxProcessedCount != -1 && cleanupAssets.size() >= maxProcessedCount)
				{
					break;
				}
			}
			rs.close();
			processor.closeStatement();
		} catch (Exception e)
		{
			ExceptionHelper.logIssueToFile("RevisionCLeanupDatabase.performQuery()", "Statement: \n" + stmt);
			ExceptionHelper.logExceptionToFile("Exception reading assets to process: \n", e);
		}
		if (c != null)
		{
			JDBCConnection.close(c);
		}

		return cleanupAssets;
	}

	/**
	 * Be provided a List of search result BaseAsset return the revision count for those assets.
	 * Two method types - 
	 * 		1) Return all revisions for the assets
	 * 		2) If a cleanup was being performed, how many revisions would be saved
	 */
	public int getAllRevisionCount(List<BaseAsset> assets)
	{
		return countRevisions(assets, true);		
	}
	public int getCleanupRevisionCount(List<BaseAsset> assets)
	{
		return countRevisions(assets, false);
	}
	private int countRevisions(List<BaseAsset> assets, boolean allRevisionCount)
	{
		int revisionCount = 0;
		for (BaseAsset ba : assets)
		{
			revisionCount += Integer.parseInt(ba.getFolderName());		// Using folder name to hold revision count for this asset
			if (allRevisionCount)
			{
				// For all revision count, need to increase by 1 the count to account for the revision 0 asset
				revisionCount++;
			}
		}
		return revisionCount;
	}
	
	public String getStatement()
	{
		return stmt;
	}
	
	/**
	 * Return a Map of folders for each unique date range found in RevisionCleanup.properties
	 * 
	 * Map to hold folders being examined grouped by unique DateRange parameters.  This enables folders for cleanup to
	 * have different dateRange values which increases the code flexibility.  For Map:
	 *		KEY = DateRange value
	 *		VALUE = Folders found to have that date range
	 */
	public Map<String, String> getDateRangeFolders(RevisionCleanupProperties props)
	{
		Map<String, String> dateRangeFolders = new HashMap<String, String>();
		
		for (String folder : props.getFoldersToClean())
		{
			String dateRangeForFolder = props.getFolderDateRange(folder);
			if (dateRangeForFolder == null)
			{
				continue;		// Invalid date range
			}
			
			String newFoldersForRange = props.getFoldersForDateRange(folder);
			
			String currentFoldersForRange = dateRangeFolders.get(dateRangeForFolder);
			if (currentFoldersForRange == null)
			{
				// First entry
				currentFoldersForRange = newFoldersForRange + ",";
			}
			else
			{
				// Add to end of folder list
				currentFoldersForRange = currentFoldersForRange + newFoldersForRange + ",";
			}
			dateRangeFolders.put(dateRangeForFolder, currentFoldersForRange);
		}

		return dateRangeFolders;
	}
	
	/**
	 * Return the List of assets that should be processed by revision cleanup procedure.  The RevisionCleanup.properties file is used to
	 * find the revisions in the defined folders.  That folder contains the maximum that can be processed.  This method also handles folders
	 * having different dateRanges defined.
	 * 
	 * The assets are returned by most revisions first.  The idea is to get the biggest bang for cycle so a sort by maximum number of
	 * revisions for each asset is performed.
	 */
	public List<BaseAsset> getAssetsForCleanup(VirageMediaBinServerSoap ws)
	{
		List<BaseAsset> cleanupAssets = new ArrayList<BaseAsset>();		// Assets that will be sorted
		
		RevisionCleanupProperties properties = new RevisionCleanupProperties();

		// Map to hold folders being examined grouped by unique DateRange parameters.  This enables folders for cleanup to
		// have different dateRange values which increases the code flexibility.  For Map:
		//		KEY = DateRange value
		//		VALUE = Folders found to have that date range
		Map<String, String> dateRangeFolders = getDateRangeFolders(properties);
		
		if (dateRangeFolders.isEmpty())
		{
			return cleanupAssets;
		}

		int maximumToCleanup = properties.getMaximumToCleanup();
		
		// For all dateRangeFolders, do the query and add found assets to the cleanupAssets List
        Iterator<Map.Entry<String, String>> iter = dateRangeFolders.entrySet().iterator();
        while (iter.hasNext())
        {
        	Map.Entry<String, String> entry = iter.next();
        	String dateRange = entry.getKey();
        	String folders = entry.getValue();
 
//        	System.out.println("DR=" + dateRange + "   FLDRS= " + folders);
    		newStatement();
    		addFolders(ws, folders);
    		setupStatementForCleanup(dateRange);
    		// Find assets to be cleaned up
    		List<BaseAsset> queryAssets = performQuery(maximumToCleanup);
    		cleanupAssets.addAll(queryAssets);
        }
        
        // Save the total # of found assets
        fullCleanupCount = cleanupAssets.size();
        
        // Sort the cleanup assets using the comparator
		Collections.sort(cleanupAssets, new RevisionCleanupAssetComparator());

		// And finally limit to only the maximum to cleanup
		int cnt = 0;
		Iterator<BaseAsset> assetIter = cleanupAssets.iterator();
		while (assetIter.hasNext())
		{
			assetIter.next();
			cnt++;
			if (cnt > maximumToCleanup)
			{
				assetIter.remove();
			}
		}
		
		return cleanupAssets;
	}

	// These next two methods perform the needed asset move and copy back functionality.  Any exceptions to success are logged.
	/**
	 * Move asset from sourceFolderGUID to tempFolderGUID.  Validation will be performed that the move happened successfully
	 * before a true is returned indicating next step can proceed.
	 */
	public boolean moveToTempFolder(VirageMediaBinServerSoap ws, BaseAsset ba, String tempFolderGUID) throws Exception
	{
		// Validate asset found in source folder
		String assetGUID = AssetSearchHelper.findInFolderWithName(ws, ba.getFolderGUID(), ba.getAssetName());
		if (assetGUID == null || !assetGUID.equals(ba.getAssetId()))
		{
			return false;		// Not found correctly in source folder
		}
		
		// Perform the move
		ws.MBObject_Move(MBObjectType.Asset, ba.getAssetId(), tempFolderGUID, ba.getAssetName());
		
		// Wait for up to 5 seconds for move to succeed
		for (int i = 0; i < 5; i++)
		{
			// Wait until not found in source folder - First step of a successful move
			assetGUID = AssetSearchHelper.findInFolderWithName(ws, ba.getFolderGUID(), ba.getAssetName());
			if (assetGUID == null)
			{	// No longer is asset found in source folder - SO - 
				// On to next test, is an asset in the destination folder with the same guid.  If so, move completed successfully
				assetGUID = AssetSearchHelper.findInFolderWithName(ws, tempFolderGUID, ba.getAssetName());
				if (assetGUID != null && assetGUID.equals(ba.getAssetId()))
				{
					return true;  // Move has been completed and validated
				}
				
			}
			
			Thread.sleep(1000);
		}
		return false;		// Caller must handle error condition
	}
	
	/**
	 * Copy asset from tempFolderGUID to sourceFolderGUID.  Validation will be performed that the copy happened successfully
	 * before a true is returned indicating next step can proceed.
	 */
	public boolean copyToSourceFolder(VirageMediaBinServerSoap ws, BaseAsset ba, String tempFolderGUID) throws Exception
	{
		// Validate asset found in Temp folder
		String assetGUID = AssetSearchHelper.findInFolderWithName(ws, tempFolderGUID, ba.getAssetName());
		if (assetGUID == null || !assetGUID.equals(ba.getAssetId()))
		{
			return false;		// Not found correctly in temporary folder
		}
		
		// Perform the copy from the temporary folder to the assets original source folder
		String copiedGUID = ws.MBObject_Copy(MBObjectType.Asset, ba.getAssetId(), ba.getFolderGUID(), ba.getAssetName());
		
		// Wait for up to 5 seconds for move to succeed
		for (int i = 0; i < 5; i++)
		{
			// Wait until found in source folder and guid from search matches guid from copy command
			assetGUID = AssetSearchHelper.findInFolderWithName(ws, ba.getFolderGUID(), ba.getAssetName());
			if (assetGUID != null && assetGUID.equals(copiedGUID))
			{
				return true;  // copy has been completed and validated
			}
			
			Thread.sleep(1000);
		}
		return false;
	}
	
	/**
	 * Driver method to perform cleanup on a asset.  Will return true if the cleanup succeeded or false if a failure occurred.
	 * The caller is responsible for handling the failure.
	 */
	public boolean performRevisionCleanupOnAsset(VirageMediaBinServerSoap ws, BaseAsset ba, String tempFolderGUID) throws Exception
	{
		if (!performUpdate)
		{
			return true;
		}

		// First action is to move the asset to the temporary folder.  Call method that performs this activity which will return
		// true if successful
		boolean successfulMove = moveToTempFolder(ws, ba, tempFolderGUID);
		if (!successfulMove)
		{
			return false;
		}
		
		// Next action is to copy the asset back to the source folder.  Call method that performs this activity which will return
		// true if successful
		boolean successfulCopy = copyToSourceFolder(ws, ba, tempFolderGUID);
		if (!successfulCopy)
		{
			return false;
		}

		// Now should be to delete asset from temporary folder
		ws.MBObject_Delete(MBObjectType.Asset, ba.getAssetId());

		return true;
	}
	
	public String getTempFolderGUID(VirageMediaBinServerSoap ws)
	{
		if (tempFolderGUID == null)
		{
			// Build the temporary path based on the thread number
			String tempPath = "/Media Database/Temp/Revision_Cleanup/TOD";
			tempFolderGUID = FolderGUIDS.findGuidForFolderPath(ws, tempPath);
			if (tempFolderGUID == null)
			{
				System.out.println("Cannot find temporary folder path for: " + tempPath);
			}
		}
		return tempFolderGUID;
	}
	
	/**
	 * Starting point for peforming a cleanup cycle.  Actions that will be taken.
	 * 		- Get assets to be cleaned up
	 * 		- Perform the cleanup on each asset
	 * 		- Log results about the cleanup including any asset errors
	 */
	public void performCleanupCycle(VirageMediaBinServerSoap ws) throws Exception
	{
		// Get assets that can be cleaned up
		List<BaseAsset> cleanupAssets = getAssetsForCleanup(ws);

		// If no cleanup assets found, just return
		if (cleanupAssets.isEmpty())
		{
			return;
		}
		
		// Since some cleanup is going to happen, make a log entry to start the cycle
		logMsg("  ");
		logMsg("START Cycle - All assets ready for cleanup: " + fullCleanupCount + "  Cleaned this cycle: " + cleanupAssets.size(), true);
		
		int removedRevisions = 0;
		String temporaryFolder = getTempFolderGUID(ws);
		
		for (BaseAsset ba : cleanupAssets)
		{
//			System.out.println(ba.getAssetName() + "   " + ba.getFolderName()   + "   " + ba.getFolderGUID());
			
			// If process was interrupted, stop processing assets.
			if (interruptHappened())
			{
				break;
			}

			boolean success = performRevisionCleanupOnAsset(ws, ba, temporaryFolder);
			if (success)
			{
				removedRevisions += Integer.parseInt(ba.getFolderName());
				continue;
			}
			logMsg("Error: " + ba.getAssetName() + " : " + ba.getAssetId() + " : " + ba.getFolderGUID());
		}
		
		logMsg("END Cycle - Revisions Removed: " + removedRevisions, true);
	}
	
	@Override
	protected String getLogFileName()
	{
		return "RevisionCleanup.txt";
	}
	
	@Override
	public void performThreadAction()
	{
		try
		{
			// Perform a revision cleanup
			performCleanupCycle(ws);
		}
		catch (Exception e)
		{
			ExceptionHelper.logExceptionToFile("run (RevisionCleanupDatabase)", e);
		}
	}
	
	public void setPerformUpdate(boolean performUpdate)
	{
		this.performUpdate = performUpdate;
	}

	/**
	 * When assets have been found to be processed by revision cleanup, this comparator will assure that the
	 * assets with the most revisions will sort first 
	 */
	private class RevisionCleanupAssetComparator implements Comparator<BaseAsset>
	{
		
		@Override
		public int compare(BaseAsset asset1, BaseAsset asset2)
		{
			String scoreAsset1 = getScore(asset1);
			String scoreAsset2 = getScore(asset2);
			return scoreAsset2.compareTo(scoreAsset1);
		}

		public String getScore(BaseAsset asset)
		{
			StringBuffer sb = new StringBuffer();
			int revisionCount = Integer.parseInt(asset.getFolderName());
			sb.append(String.format("%03d", revisionCount) + "_");
			sb.append(asset.getAssetName());
//			System.out.println("SCORE=" + sb.toString());
			return sb.toString();
		}
		
	}

	@Override
	protected int getSleepSeconds()
	{
		return 0;
	}

	@Override
	protected void initializeThread()
	{
		// Get a web service connection
		ws = MediaBinConnectionPool.getPoolConnection("RevisionCleanupDatabase");
	}

	@Override
	protected void cleanupThread()
	{
		// Return ws connection
		if ( ws != null )
		{
			MediaBinConnectionPool.returnPoolConnection(ws, "RevisionCleanupDatabase");
		}
	}
}

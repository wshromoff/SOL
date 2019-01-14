package com.jostens.dam.brand.workdisplay;

import com.jostens.dam.brand.web.HTMLHelper;
import com.jostens.dam.shared.assetclass.BaseAsset;

/**
 * This object represents an asset that is part of work flow display application.  The functionality of
 * BaseAsset is extended to information helpful to its work flow display. 
 */
public class DisplayAsset extends BaseAsset implements Comparable
{
	private boolean inStore = false;		// Is this asset in the store?
	private boolean changeThisCycle = false;		// Set to true if the information about this asset changed compared to previous cycle
	private long inStageTime = 0;			// How many seconds in the current stage
	private long totalTime = 0;				// How many seconds this asset has been processing for
	private long lastCurrentTime = 0;		// What was the current time in mills when this asset was setup
	private String artistName = "";			// Artist name who last worked on this asset

//	private final String rowTemplate = "<tr><td bgcolor=\"[BG_COLOR]\"><font size=\"2\"><pre>[ASSET_NAME]</font><br><font size=\"4\">" +
//			"[ARTIST]  [STAGE_TIME] [TOTAL_TIME]</pre></font></td></tr>";
	private static String currentAssetTemplate = null;	// Row template for current assets
	private static String completedAssetTemplate = null;	// Row template for completed assets

	private String statusAutomation;		// Current value for status automation
	private boolean newAsset = false;			// Is this asset a new, replace or revision asset
	private boolean replaceAsset = false;
	private boolean reviseAsset = false;

	private String currentRevision;			// Current revision which is used to determine if asset changed and so should be fully re-evaluated.
											// If it hasn't changed, then previous built asset can be re-used speeding up processing
	public DisplayAsset()
	{
		super();
		initializeCurrentRowTemplate();
	}
	public DisplayAsset(BaseAsset ba)
	{
		setAssetName(ba.getAssetName());
		setAssetId(ba.getAssetId());
		initializeCurrentRowTemplate();
	}
	public DisplayAsset(String metricsKey)
	{
		// Initialize asset based on a property KEY from Metrics.properties file
		// These rows have a format of 'N_hauserk_ET087060_1588112_mascot_bitmap_ctone_ut_dx_0x_bks_x_x_x_x_x_x_x_x_x.cdr'
		char c = metricsKey.charAt(0);
		switch (c)
		{
			case 'N':
				newAsset = true;
				break;
			case 'P':
				replaceAsset = true;
				break;
			case 'V':
				reviseAsset = true;
				break;
		}
		int i = metricsKey.indexOf("_", 2);
		artistName = metricsKey.substring(2, i);
		setAssetName(metricsKey.substring(i+1));
		inStore = true;
		initializeCompletedRowTemplate();
	}
	
	private void initializeCurrentRowTemplate()
	{
		if (DisplayAsset.currentAssetTemplate != null)
		{
			return;		// Already initialized
		}
		// Need to read the row template
		String templateHTML = HTMLHelper.getTemplateHTML("/com/jostens/dam/brand/web/CurrentAssetTemplate.html");
		DisplayAsset.currentAssetTemplate = templateHTML;
	}
	private void initializeCompletedRowTemplate()
	{
		if (DisplayAsset.completedAssetTemplate != null)
		{
			return;		// Already initialized
		}
		// Need to read the row template
		String templateHTML = HTMLHelper.getTemplateHTML("/com/jostens/dam/brand/web/CompletedAssetTemplate.html");
		DisplayAsset.completedAssetTemplate = templateHTML;
	}

	/**
	 * Return a representation of this asset as a row element in the display
	 */
	public String toString()
	{
		// Start with row template
		String rowDisplay = DisplayAsset.currentAssetTemplate;
		if (inStore)
		{
			rowDisplay = DisplayAsset.completedAssetTemplate;
		}
//		rowDisplay = rowDisplay.replace("[BG_COLOR]", getBackgroundColor());
		rowDisplay = rowDisplay.replace("[DISPLAY_STYLE]", getDisplayStyle());
		rowDisplay = rowDisplay.replace("[ASSET_NAME]", getAssetName());
		rowDisplay = rowDisplay.replace("[ARTIST]", getArtistName());
		rowDisplay = rowDisplay.replace("[STAGE_TIME]", getInStageTime());
		rowDisplay = rowDisplay.replace("[TOTAL_TIME]", getTotalTime());
//		System.out.println("\n" + rowDisplay + "\n");
		return rowDisplay;
	}

	public String getDisplayStyle()
	{
		if (totalTime >= 7200)
		{	// More than 2 hours in total processing time
			return "twoHoursLate";			
		}
		if (inStageTime >= 1800)
		{	// More than 30 minutes in stage processing time
			return "bucketLate";			
		}
		if (changeThisCycle)
		{
			return "changed";
		}
		return "noChanges";
	}

	public String getBackgroundColor()
	{
		if (totalTime >= 7200)
		{	// More than 2 hours in total processing time
			return "Red";			
		}
		if (inStageTime >= 1800)
		{	// More than 30 minutes in stage processing time
			return "DodgerBlue";			
		}
		if (changeThisCycle)
		{
			return "LightBlue";
		}
		return "LightYellow";
	}
	public String getArtistName()
	{
		if (artistName == null)
		{
			return "";
		}
		return artistName;
	}
	public void setArtistName(String artistName)
	{
		this.artistName = artistName;
	}
	public String getInStageTime()
	{
		return getSecondsForDisplay(inStageTime);
	}
	public void setInStageTime(long inStageTime)
	{
		this.inStageTime = inStageTime;
	}
	public String getTotalTime()
	{
		return getSecondsForDisplay(totalTime);
	}
	public void setTotalTime(long totalTime)
	{
		this.totalTime = totalTime;
	}
	public void setChangeThisCycle(boolean changeThisCycle)
	{
		this.changeThisCycle = changeThisCycle;
	}
	
	private String getSecondsForDisplay(long seconds)
	{
		if (seconds >= 600000)
		{
			return "XXXX:XX";
		}
		int minutes = (int)(seconds / 60);
		int remainder = (int)(seconds - (minutes * 60));
		String display = String.format("%d:%02d", minutes, remainder);
		return display;
	}

//	public boolean isInStore()
//	{
//		return inStore;
//	}
//	public void setInStore(boolean inStore)
//	{
//		this.inStore = inStore;
//	}

	public boolean isNewAsset()
	{
		return newAsset;
	}
	public void setNewAsset(boolean newAsset)
	{
		this.newAsset = newAsset;
	}
	public boolean isReplaceAsset()
	{
		return replaceAsset;
	}
	public void setReplaceAsset(boolean replaceAsset)
	{
		this.replaceAsset = replaceAsset;
	}
	public boolean isReviseAsset()
	{
		return reviseAsset;
	}
	public void setReviseAsset(boolean reviseAsset)
	{
		this.reviseAsset = reviseAsset;
	}
	public String getStatusAutomation()
	{
		return statusAutomation;
	}
	public void setStatusAutomation(String statusAutomation)
	{
		this.statusAutomation = statusAutomation;
	}
	public String getCurrentRevision()
	{
		return currentRevision;
	}
	public void setCurrentRevision(String currentRevision)
	{
		this.currentRevision = currentRevision;
	}
	public void setLastCurrentTime(long lastCurrentTime)
	{
		if (this.lastCurrentTime != 0)
		{		// Asset is being re-used, so increment other times in seconds by the difference
				// between supplied current time and previous value
			long deltaSeconds = (lastCurrentTime - this.lastCurrentTime) / 1000;
			inStageTime += deltaSeconds;
			totalTime += deltaSeconds;
		}
		this.lastCurrentTime = lastCurrentTime;

	}
	@Override
	public int compareTo(Object o)
	{
		if (!(o instanceof DisplayAsset))
		{
			return 0;
		}
		DisplayAsset da = (DisplayAsset)o;
		return getAssetName().compareTo(da.getAssetName());
	}

	
	
}

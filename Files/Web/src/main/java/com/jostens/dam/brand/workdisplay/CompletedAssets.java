package com.jostens.dam.brand.workdisplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.jostens.dam.brand.automations.timeofday.MetricsPropertyFile;
import com.jostens.dam.brand.web.HTMLHelper;
import com.jostens.dam.shared.automations.BaseThread;
import com.jostens.dam.shared.common.OutputFormatters;

public class CompletedAssets extends BaseThread
{
	// Static name that HTML is stored using in the cache
	public static final String COMPLETED_ASSETS_HTML = "CompletedAssets";
	// Hold the completed assets Map used to build the completed assets display
	private Map<String, List<DisplayAsset>> artistAssets = null;

	@Override
	protected int getSleepSeconds()
	{
		return 180;
	}

	@Override
	protected void initializeThread()
	{
		// No initialization code required
	}

	@Override
	protected void cleanupThread()
	{
//		System.out.println("CLEANUP STARTED!!!");
//		try
//		{
//			Thread.sleep(60000);
//		} catch (InterruptedException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("CLEANUP ENDED!!!");
		// No cleanup code required
	}

	@Override
	protected void performThreadAction()
	{
		System.out.println("CompletedAssets - " + OutputFormatters.getCurrentDateFormat6());
//		try
//		{
//			Thread.sleep(20000);
//		} catch (InterruptedException e)
//		{
//		}

//		MetricsPropertyFile properties = new MetricsPropertyFile();
//		System.out.println("PROPERTIES SIZE = " + properties.size());
		
		// Build up Maps of those with completed assets.  There are 3 Maps, one for New, Replacement, Revision.
		// In each Map, KEY = artist name, VALUE = List of DisplayAsset objects completed by that artist for the day
		artistAssets = getArtistCompletedAssets();

		// Need to sort assets correctly
		sortArtistAssets();
		
		// Need to update the cache with most recent information
		String html = getHTMLForCache();
//		System.out.println("HTML\n" + html);
		HTMLHelper.cacheHTML(COMPLETED_ASSETS_HTML, html);

	}

	/**
	 * Return a Map of completed DisplayAsset objects for each artist.  The KEY is the artist name and the VALUE is a
	 * List of DisplayAsset objects that artist completed for the day. 
	 */
	public Map<String, List<DisplayAsset>> getArtistCompletedAssets()
	{
		Map<String, List<DisplayAsset>> artistAssets = new LinkedHashMap<String, List<DisplayAsset>>();
		MetricsPropertyFile properties = new MetricsPropertyFile();
        Iterator<Map.Entry<Object, Object>> iter = properties.entrySet().iterator();
        while (iter.hasNext())
        {
        	Map.Entry<Object, Object> entry = iter.next();
        	String property = (String)entry.getKey();
        	if (!property.startsWith("N_") && !property.startsWith("P_") && !property.startsWith("V_"))
        	{	// This property isn't related to an asset
        		continue;
        	}
        	DisplayAsset completedAsset = new DisplayAsset(property);
        	String timings = (String)entry.getValue();
        	// Last time value is the total time for processing
        	int i = timings.lastIndexOf("|");
        	if (i != -1)
        	{
        		long totalTime = Integer.parseInt(timings.substring(i+1));
        		completedAsset.setTotalTime(totalTime);
        	}
        	String artistName = completedAsset.getArtistName();
//        	System.out.println(artistName);
        	
        	// Now add the built DisplayAsset to the artistAssets Map
        	List<DisplayAsset> assets = artistAssets.get(artistName);
        	if (assets == null)
        	{
        		assets = new ArrayList<DisplayAsset>();
        		assets.add(completedAsset);
        		artistAssets.put(artistName, assets);
        	}
        	else
        	{
        		assets.add(completedAsset);
        	}
        }
		
		return artistAssets;
	}
	
	public String getHTMLForCache()
	{
//		DisplayAssetComparator comparator = new DisplayAssetComparator();
//
//		bmgr.resetBuckets();	// Reset previous bucket assets
//		
//		synchronized(inProgressAssets)
//		{
//			for (DisplayAsset da : inProgressAssets.values())
//			{
//				bmgr.addAssetToBucket(da);
//			}
//		}
		BucketMap bm = new BucketMap();
		
		// Build the HTML for the COMPLETED ASSETS portion of the work display web application
		String rowTemplate = HTMLHelper.getTemplateHTML("/com/jostens/dam/brand/web/CompletedAssetsRowTemplate.html");
		StringBuffer sb = new StringBuffer();
		
        Iterator<Map.Entry<String, List<DisplayAsset>>> iter = artistAssets.entrySet().iterator();
        while (iter.hasNext())
        {
        	Map.Entry<String, List<DisplayAsset>> entry = iter.next();
        	String artistName = (String)entry.getKey();
        	List<DisplayAsset> assets = entry.getValue();
        	
        	String aRow = rowTemplate;
        	aRow = aRow.replace("[ARTIST_NAME]", artistName + " (" + assets.size() + ")");
        	aRow = aRow.replace("[NEW]", bm.getBucketHTML(getAssetsOfType(assets, true, false, false)));
        	aRow = aRow.replace("[REPLACE]", bm.getBucketHTML(getAssetsOfType(assets, false, true, false)));
        	aRow = aRow.replace("[REVISE]", bm.getBucketHTML(getAssetsOfType(assets, false, false, true)));
        	sb.append(aRow);
       }
		
		
		String completedHTML = HTMLHelper.getTemplateHTML("/com/jostens/dam/brand/web/CompletedAssetsTemplate.html");
		completedHTML = completedHTML.replace("[COMPLETED_HTML]", sb.toString());

		return completedHTML;
	}

	/**
	 * Get assets from the provided List of DisplayAsset's that are either new, replace or revise
	 */
	private List<DisplayAsset> getAssetsOfType(List<DisplayAsset> displayAssets, boolean findNew, boolean findReplace, boolean findRevise)
	{
       	List<DisplayAsset> assetsOfType = new ArrayList<DisplayAsset>();

       	for (DisplayAsset da : displayAssets)
       	{
       		if (findNew && da.isNewAsset())
       		{
       			assetsOfType.add(da);
       		}
       		if (findReplace && da.isReplaceAsset())
       		{
       			assetsOfType.add(da);
       		}
       		if (findRevise && da.isReviseAsset())
       		{
       			assetsOfType.add(da);
       		}
       	}
       	Collections.sort(assetsOfType);
       	return assetsOfType;
	}
	
	/**
	 * Sort artistAssets Map so that the artist with the most completed assets sorts first
	 */
	public void sortArtistAssets()
	{
        Set<Entry<String, List<DisplayAsset>>> set = artistAssets.entrySet();

        List<Entry<String, List<DisplayAsset>>> list = new ArrayList<Entry<String, List<DisplayAsset>>>(set);

        Collections.sort( list, new CompletedAssetsComparator());
        
        artistAssets.clear();
        for (Entry<String, List<DisplayAsset>> entry : list)
        {
        	artistAssets.put(entry.getKey(), entry.getValue());
        }        		
 	}

	public Map<String, List<DisplayAsset>> getArtistAssets()
	{
		return artistAssets;
	}

	@Override
	protected String getLogFileName()
	{
		return null;
	}
}

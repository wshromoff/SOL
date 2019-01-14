package com.jostens.dam.brand.workdisplay;

import com.jostens.dam.brand.web.HTMLHelper;
import com.jostens.dam.shared.assetclass.BaseAsset;

/**
 * This object manages the ProcessedAssets display.  The source of information
 * for this object is the Metrics.properties file.  This file also is reset at the start
 * of each day so that functionality comes for free.
 * 
 * An instance of this object will be held by WorkDisplay class.  This object will get called
 * to reload processed assets and hold them in this object.  Since this information doesn't change
 * frequently, this will be called every 5 minutes to update since there is no sense doing
 * it every 30 seconds like workdisplay is refreshed.
 */
public class ProcessedAssets extends BaseAsset
{

	public void refreshProcessedAssets()
	{
		System.out.println("Refreshing processed assets");
	}
	
	/**
	 * Get the HTML that will display the asset information maintained by this object
	 */
	public String getDisplayHTML()
	{
		String workDisplay = HTMLHelper.getTemplateHTML("/com/jostens/dam/brand/web/CompletedAssetsTemplate.html");

		return workDisplay;
	}
}

package com.jostens.dam.brand.workdisplay;

import java.util.List;

import com.jostens.dam.brand.web.HTMLHelper;

/**
 * Generate the HTML for a bucket's display
 */
public class BucketMap
{

	// Template HTML for bucket with no assets and one with one or more assets
	private static String emptyBucket = null;
	private static String assetsInBucket = null;


	public String getBucketHTML(List<DisplayAsset> bucketAssets)
	{
		if (emptyBucket == null)
		{	// Load in template HTML
			emptyBucket = HTMLHelper.getTemplateHTML("/com/jostens/dam/brand/web/EmptyBucket.html");
			assetsInBucket = HTMLHelper.getTemplateHTML("/com/jostens/dam/brand/web/AssetsInBucket.html");
		}
		if (bucketAssets.isEmpty())
		{
//			System.out.println(emptyBucket);
			return emptyBucket;
		}
		
		// There are assets in the bucket, need to build the HTML that represents the assets
		StringBuffer sb = new StringBuffer();
		for (DisplayAsset da : bucketAssets)
		{
			sb.append(da.toString());
		}
		
		String bucketHTML = assetsInBucket;
		bucketHTML = bucketHTML.replace("[ASSET_ROWS]", sb.toString());
//		System.out.println(bucketHTML);
		return bucketHTML;
	}
}

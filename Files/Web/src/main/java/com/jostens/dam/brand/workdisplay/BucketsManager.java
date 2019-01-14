package com.jostens.dam.brand.workdisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jostens.dam.brand.attributes.brand.StatusAutomation;

/**
 * Manage the buckets of assets that are part of each quadrant of the work monitor display.  Each quadrant is a List<DisplayAsset>
 * and this object gets passed a DisplayAsset and makes the decision as to which bucket the asset should reside in and
 * adds that asset to the bucket.
 * 
 * The buckets are kept in a Map.
 * 		KEY : The bucket ID named:    'BUCKET_ROW_COL'
 * 		VALUE: The List of DisplayAsset objects to be found in that bucket
 *
 */
public class BucketsManager
{
	private int rows = 8;
	private int cols = 3;
	private Map<String, List<DisplayAsset>> buckets = new HashMap<String, List<DisplayAsset>>();
	
	public BucketsManager()
	{
		// Initialize the Buckets Map
		for (int i = 1; i <= rows; i++)
		{
			for (int j = 1; j <= cols; j++)
			{
				buckets.put(getBucketName(i, j), new ArrayList<DisplayAsset>());
			}
		}
	}
	
	private String getBucketName(int row, int col)
	{
		return "BUCKET_" + row + "_" + col;
	}
	
	public void resetBuckets()
	{
		// Clear all assets from buckets
		for (List<DisplayAsset> bucket : buckets.values())
		{
			bucket.clear();
		}
	}
	
	/**
	 * Add the supplied asset to one of the buckets
	 */
	public void addAssetToBucket(DisplayAsset da)
	{
		int bucketRow = getBucketRow(da);
		int bucketCol = getBucketColumn(da);
		
		List<DisplayAsset> bucketAssets = getBucketAssets(bucketRow, bucketCol);
		bucketAssets.add(da);
	}

	public List<DisplayAsset> getBucketAssets(String bucketName)
	{
		return buckets.get(bucketName);
	}
	public List<DisplayAsset> getBucketAssets(int row, int col)
	{
		String bucketName = getBucketName(row, col);
		return getBucketAssets(bucketName);
	}
	
	/**
	 * Get column value for the bucket the supplied DisplayAsset.  The asset is checked for
	 * it being part of new, replace or revise folders
	 */
	public int getBucketColumn(DisplayAsset da)
	{
		if (da.isNewAsset())
		{
			return 1;
		}
		if (da.isReplaceAsset())
		{
			return 2;
		}
		return 3;
	}
	/**
	 * Get row value for the bucket the supplied DisplayAsset.  The assets current status automation
	 * is examined to determine the needed row.  Can also provide a status automation value instead of the display asset.
	 * Having this flexibility is why there are 2 method options.
	 */
	public int getBucketRow(DisplayAsset da)
	{
		String statusAutomation = da.getStatusAutomation();
		return getBucketRow(statusAutomation);
	}	
	public int getBucketRow(String statusAutomation)
	{
		if (statusAutomation == null)
		{
			// Asset is new, return the value of 1
			return 1;
		}
		if (statusAutomation.contains(StatusAutomation.PR_PROCESSING))
		{
			return 8;		// Waiting to process asset
		}
		if (statusAutomation.contains("GO") && !statusAutomation.contains(StatusAutomation.PR_PROCESSING))
		{
			return 7;		// Waiting to process asset
		}
		if (statusAutomation.contains(StatusAutomation.PR_NONE) || statusAutomation.contains(StatusAutomation.PR_ORPHAN) || 
				statusAutomation.contains(StatusAutomation.PR_POSSIBLE_REPLACE) || statusAutomation.contains(StatusAutomation.PR_POSSIBLE_REVISE) ||
				statusAutomation.contains(StatusAutomation.PR_REPLACE) || statusAutomation.contains(StatusAutomation.PR_REVISE))
		{
			return 6;		// Cataloger's need to act to help asset
		}
		if (statusAutomation.contains(StatusAutomation.DR_INUSE_CATALOG) || statusAutomation.contains(StatusAutomation.DR_DONE_CATALOG))
		{
			return 5;		// Cataloger form has started
		}
		if ((statusAutomation.contains(StatusAutomation.DR_CATALOG) || statusAutomation.contains(StatusAutomation.PR_CATALOGER)) &&
				(!statusAutomation.contains(StatusAutomation.DR_INUSE_CATALOG) && !statusAutomation.contains(StatusAutomation.DR_DONE_CATALOG)))
		{
			return 4;		// Needs catalog form but hasn't started yet
		}
		if (statusAutomation.equals("FS_COMPLETE MD_COMPLETE ") || statusAutomation.equals("FS_COMPLETE MD_COMPLETE AI_COMPLETE ") || 
				statusAutomation.contains(StatusAutomation.DR_COMPLETE))
		{
			return 3;		// Artist Validation
		}
		if (statusAutomation.contains("FAILURE"))
		{
			return 1;		// Failure Bucket is now displayed as part of new
		}

//		if (statusAutomation == null || statusAutomation.length() == 0 || !statusAutomation.contains(StatusAutomation.MD_COMPLETE))
//		{
//			return 1;		// NEW Bucket
//		}
		return 1;		// Asset is New
	}

}

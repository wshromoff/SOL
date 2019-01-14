package com.jostens.dam.brand.workdisplay;

import java.util.Comparator;

import com.jostens.dam.shared.assetclass.MetadataGUIDS;

public class CurrentAssetsComparator implements Comparator<DisplayAsset>
{

	@Override
	public int compare(DisplayAsset displayAsset1, DisplayAsset displayAsset2)
	{
		String scoreAsset1 = getScore(displayAsset1);
		String scoreAsset2 = getScore(displayAsset2);
		return scoreAsset2.compareTo(scoreAsset1);
	}

	public String getScore(DisplayAsset displayAsset)
	{

		StringBuffer sb = new StringBuffer();
		sb.append(getJobSchedulePriorityScore(displayAsset));
		sb.append(getUploadQCPriorityScore(displayAsset));
		sb.append(getInCycleTime(displayAsset));
		sb.append(displayAsset.getAssetName());
//		System.out.println("SCORE=" + sb.toString());
		return sb.toString();
	}
	
	// Get in cycle time - max time comes first
	private String getInCycleTime(DisplayAsset displayAsset)
	{
		String display = String.format("%8s_", displayAsset.getInStageTime());
		return display;

	}
	
	// JobSechedule Priority - 'Top Priority' highest priority, so smallest value
	private String getJobSchedulePriorityScore(DisplayAsset displayAsset)
	{
		String jobSchedulePriority = displayAsset.getMetaDataValue(MetadataGUIDS.JOB_SCHEDULE_PRIORITY);
		if ("Top Priority".equals(jobSchedulePriority))
		{
			return "3_";
		}
		if ("Rush".equals(jobSchedulePriority))
		{
			return "2_";
		}
		return "1_";
	}
	
	// Prioritize artist jobs ahead of DAM team
	private String getUploadQCPriorityScore(DisplayAsset displayAsset)
	{
		String uploadQCUser = displayAsset.getArtistName();
		if (uploadQCUser == null || uploadQCUser.length() == 0)
		{
			return "2_";		// Handle no upload QC users as top priority - this gets the asset into the system and started processing
		}
		if ("shromow".equals(uploadQCUser) || "rude".equals(uploadQCUser) || "riehlek".equals(uploadQCUser) || "stoenb".equals(uploadQCUser) || "hibbarp".equals(uploadQCUser) || "hinshab".equals(uploadQCUser))
		{
			return "0_";
		}
		return "1_";
	}

}

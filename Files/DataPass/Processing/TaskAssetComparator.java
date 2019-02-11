package com.jostens.dam.brand.tasks;

import java.util.Comparator;

import com.jostens.dam.shared.assetclass.MetadataGUIDS;

/**
 * Provide the logic for comparing to TaskAsset objects and return how they compare.
 * 
 * This comparator will be used to define the priority of a TaskAsset so the ones deemed highest
 * priority by scoring rules will be processed first.
 */
public class TaskAssetComparator implements Comparator<TaskAsset>
{
	// Expose the metadata this comparator needs on each asset to be compared
	public static String[] COMPARATOR_METADATA = {MetadataGUIDS.WORKFLOW_TYPE, MetadataGUIDS.JOB_SCHEDULE_PRIORITY, MetadataGUIDS.UPLOAD_QC_USER};
	
	@Override
	public int compare(TaskAsset taskAsset1, TaskAsset taskAsset2)
	{
		String scoreAsset1 = getScore(taskAsset1);
		String scoreAsset2 = getScore(taskAsset2);
		return scoreAsset1.compareTo(scoreAsset2);
	}

	public String getScore(TaskAsset taskAsset)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getUploadQCPriorityScore(taskAsset));
		sb.append(getJobSchedulePriorityScore(taskAsset));
		sb.append(getWorkflowTypeScore(taskAsset));
		sb.append(getFolderTypeScore(taskAsset));
		sb.append(taskAsset.getFolderNameForSort() + "_");
		sb.append(taskAsset.getAssetName());
//		System.out.println("SCORE=" + sb.toString());
		return sb.toString();
	}
	
	// WorkflowType - Production highest priority, so smallest value
	private String getWorkflowTypeScore(TaskAsset taskAsset)
	{
		String workflowType = taskAsset.getMetaDataValue(MetadataGUIDS.WORKFLOW_TYPE);
		if (workflowType != null && workflowType.contains("Production"))
		{
			return "1_";
		}
		return "2_";
	}
	
	// JobSechedule Priority - 'Top Priority' highest priority, so smallest value
	private String getJobSchedulePriorityScore(TaskAsset taskAsset)
	{
		String jobSchedulePriority = taskAsset.getMetaDataValue(MetadataGUIDS.JOB_SCHEDULE_PRIORITY);
		if ("Top Priority".equals(jobSchedulePriority))
		{
			return "1_";
		}
		if ("Rush".equals(jobSchedulePriority))
		{
			return "2_";
		}
		return "3_";
	}
	
	// Prioritize artist jobs ahead of DAM team
	private String getUploadQCPriorityScore(TaskAsset taskAsset)
	{
		String uploadQCUser = taskAsset.getMetaDataValue(MetadataGUIDS.UPLOAD_QC_USER);
		if (uploadQCUser == null || uploadQCUser.length() == 0)
		{
			return "0_";		// Handle no upload QC users as top priority - this gets the asset into the system and started processing
		}
		if ("shromow".equals(uploadQCUser) || "rude".equals(uploadQCUser) || "riehlek".equals(uploadQCUser) || "stoenb".equals(uploadQCUser) || "hibbarp".equals(uploadQCUser) || "hinshab".equals(uploadQCUser))
		{
			return "2_";
		}
		return "1_";
	}

	// Folder Type Priority - Prioritize folders by: replace, revise, new
	private String getFolderTypeScore(TaskAsset taskAsset)
	{
		if (taskAsset.getFolder().isReplacementFolder())
		{
			return "1_";			
		}
		if (taskAsset.getFolder().isRevisionFolder())
		{
			return "2_";			
		}
		return "3_";
	}

}

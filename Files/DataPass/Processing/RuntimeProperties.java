package com.jostens.dam.shared.properties;

public class RuntimeProperties extends PropertyFileOrdered
{
	// Log file Definitions
	public final static String AUTOMATION_LOG = "AutomationLog";
	public final static String EXCEPTION_LOG = "ExceptionLog";
	public final static String ASSETMETADATA_LOG = "AssetMetadataLog";
	public final static String SYNCHRONIZE_LOG = "SynchronizeLog";
	public final static String ALERT_LOG = "AlertLog";
	public final static String REMOVE_METADATA_LOG = "RemoveMetadataLog";
	public final static String USERMETRICS_LOG = "UserMetricsLog";
	public final static String ASSOCIATION_LOG = "AssociationLog";
	public final static String ASSOCIATION_ASSET_LOG = "AssociationAssetLog";
	public final static String PROJECT_ASSET_LOG = "ProjectAssetLog";
	public final static String DATABASE_VALIDATION_LOG = "DataBaseValidationLog";
	public final static String UPLOAD_LOG = "UploadLog";
	
	public final static String METRICS = "Metrics";
	public final static String PROPAGATION = "Propagation";
	public final static String BDU_EVALUATE = "BDUEvaluate";
	public final static String REINDEX = "Reindex";
	public final static String CUSTOMER_RESOLVE = "CustomerResolve";
	public final static String CUSTOMER_PROFILE_SYNC = "CustomerProfileSync";
	public final static String ASSET_METADATA = "AssetMetadata";
	public final static String METADATA_MONITOR = "MetadataMonitor";
	public final static String CUSTOMER_PROFILE = "CustomerProfile";
	public final static String CUSTOMER_PROFILE_PROCESS_ACTIONS = "CustomerProfileProcessActions";
	public final static String CUSTOMER_PROFILE_MODIFY_ACTIONS = "CustomerProfileModifyActions";
	public final static String CUSTOMER_PROFILE_VALIDATE = "CustomerProfileValidate";
	public final static String CUSTOMER_PROFILE_MAX_ACTIONS = "CustomerProfileMaxActions";
	public final static String RETIRE_DELETE_MOVE = "RetireDeleteMove";
	public final static String BUSINESS_RULES_MOVE = "BusinessRulesMove";
	public final static String REDUNDANCY = "Redundancy";
	public final static String HOTFOLDERS = "HotFolders";
	public final static String USER_METRICS = "UserMetrics";
	public final static String REVISION_CLEANUP = "RevisionCleanup";
	public final static String ASSOCIATION = "Association";
	public final static String RIGHTS_MANAGEMENT = "RightsManagement";
	public final static String APA_START_AUTOMATIONS = "APAStartAutomations";
	public final static String ASSOCIATION_ASSET = "AssociationAsset";
	public final static String DATABASE_VALIDATION = "DataBaseValidation";
	public final static String PROJECT_ASSET = "ProjectAsset";
	public final static String UPLOAD = "Upload";
	public final static String JDAM_SCHEMA_NAME = "JDAM_SCHEMA";
	public final static String JEMCONNECT = "JEMProperties";
	public final static String YEARBOOK_METADATA = "YearbookMetadata";
	
	@Override
	public String getPropertyFileName()
	{
		return RUNTIME_PROPERTIES;
	}

	public boolean isEnabled(String property)
	{
		String propertyValue = getProperty(property);
		if (propertyValue == null || !"Enabled".equals(propertyValue))
		{
			return false;
		}
		return true;		
	}
	public boolean isDisabled(String property)
	{
		String propertyValue = getProperty(property);
		if (propertyValue == null || !"Enabled".equals(propertyValue))
		{
			return true;
		}
		return false;		
	}
}

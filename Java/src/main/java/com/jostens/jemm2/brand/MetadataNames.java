package com.jostens.jemm2.brand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * For each GUID defined in MetadataGUIDS, there should be corresponding entry in this class
 * to define the string name for the GUID.  These string names can be used when descriptive output
 * of a metadata value is needed. 
 * 
 * To get access to these names, there is a single getter method provided.
 * 
 * PLEASE KEEP IN ALPHABETICAL ORDER - Same as MetadataGUIDS order
 */
public class MetadataNames
{

	/**
	 * Return a String name representative of the GUID value provided.  If not found, the value
	 * 'No Name Found' is returned.
	 */
	public static String getNameForGUID(String metadataGUID)
	{
		String name = NAMES.get(metadataGUID);
		if (name != null)
		{
			return name;
		}
		name = MEDIABIN_METADATA.get(metadataGUID);
		if (name != null)
		{
			return name;
		}
		return "No Name Found";
	}
	
	private static final Map<String, String> NAMES = new HashMap<String, String>()
	{
		{
			put(MetadataGUIDS.AFFILIATION_BY_DEPICTION, "Affiliation (By Depiction)");
			put(MetadataGUIDS.AFFILIATION_BY_USE, "Affiliation (By Use)");
			put(MetadataGUIDS.ARTFORM, "Artform");
//			put(MetadataGUIDS.ARTISTIC_STYLE, "Artistic Style");
			put(MetadataGUIDS.ASSET_CLASS, "Asset Class");
			put(MetadataGUIDS.ASSET_CREATOR, "Asset Creator");
//			put(MetadataGUIDS.ASSET_SUBCLASS, "Asset Subclass");
			put(MetadataGUIDS.ASSET_ID_COLOR_SCHEME, "Asset ID (Color Scheme)");
			put(MetadataGUIDS.ASSET_ID_DESIGN_CONCEPT, "Asset ID (Design Concept)");
			put(MetadataGUIDS.ASSET_ID_JOSTENS, "Asset ID (Jostens)");
			put(MetadataGUIDS.ASSET_ID_PARENT_MASTER, "Asset ID (Parent Master)");
			put(MetadataGUIDS.ASSET_ID_PARENT_RENDITION, "Asset ID (Parent Rendition)");
			put(MetadataGUIDS.BASE_COLOR, "Base Color");
			put(MetadataGUIDS.BASE_COLOR_TONES_RENDERED, "Base Color Tones Rendered");
			put(MetadataGUIDS.BRAND_ASSET_SUBCLASS, "Brand Asset Subclass");
			put(MetadataGUIDS.BRAND_ASSET_TYPE, "Brand Asset Type");
			put(MetadataGUIDS.BRAND_VALIDATION, "Brand Validation");
			put(MetadataGUIDS.BRAND_VALIDATION_DATE, "Brand Validation Date");
			put(MetadataGUIDS.BRAND_VALIDATION_SOURCE, "Brand Validation Source");
			put(MetadataGUIDS.BUSINESS_DEFAULT_USE, "Business Default Use");
			put(MetadataGUIDS.BUSINESS_DEFAULT_USE_VALIDATION_DATE, "Business Default Use Validation Date");
			put(MetadataGUIDS.BUSINESS_DEFAULT_USE_VALIDATION_SOURCE, "Business Default Use Validation Source");
			put(MetadataGUIDS.COLOR_0, "Color 0");
			put(MetadataGUIDS.COLOR_1, "Color 1");
			put(MetadataGUIDS.COLOR_2, "Color 2");
			put(MetadataGUIDS.COLOR_3, "Color 3");
			put(MetadataGUIDS.COLOR_4, "Color 4");
			put(MetadataGUIDS.COLOR_5, "Color 5");
			put(MetadataGUIDS.COLOR_6, "Color 6");
			put(MetadataGUIDS.COLOR_7, "Color 7");
			put(MetadataGUIDS.COLOR_8, "Color 8");
			put(MetadataGUIDS.COLOR_9, "Color 9");
			put(MetadataGUIDS.COLOR_10, "Color 10");
			put(MetadataGUIDS.COLOR_PALETTE, "Color Palette");
			put(MetadataGUIDS.COLOR_SCHEME, "Color Scheme");
			put(MetadataGUIDS.COLOR_SCHEME_NATURE, "Color Scheme Nature");
			put(MetadataGUIDS.COLOR_VALUES_CMYK, "Color Values (CMYK)");
			put(MetadataGUIDS.COLOR_VALUES_LAB, "Color Values (Lab)");
			put(MetadataGUIDS.COLOR_VALUES_PMS, "Color Values (PMS)");
			put(MetadataGUIDS.COLOR_VALUES_RGB, "Color Values (RGB)");
			put(MetadataGUIDS.COLOR_VALUES_WEB, "Color Values (Web)");
			put(MetadataGUIDS.COLORIZATION_LEVEL, "Colorization Level");
			put(MetadataGUIDS.COLORIZATION_MODE, "Colorization Mode");
			put(MetadataGUIDS.COLORIZATION_TECHNIQUE, "Colorization Technique");
//			put(MetadataGUIDS.COPYRIGHT_OWNER, "Copyright Owner");
//			put(MetadataGUIDS.COPYRIGHT_STATUS, "Copyright Status");
			put(MetadataGUIDS.CREATIVE_INTENT_COLOR, "Creative Intent Color");
			put(MetadataGUIDS.CREATIVE_INTENT_DESIGN, "Creative Intent Design");
			put(MetadataGUIDS.CURRENT_ASSET_EDITION, "Current Asset Edition");
			put(MetadataGUIDS.CURRENT_ASSET_OWNER, "Current Asset Owner");
			put(MetadataGUIDS.CUSTOMER_CITY, "Customer City");
			put(MetadataGUIDS.CUSTOMER_CLASS_ATHLETIC, "Customer Class (Athletic)");
			put(MetadataGUIDS.CUSTOMER_CONFERENCE, "Customer Conference");
			put(MetadataGUIDS.CUSTOMER_COUNTRY, "Customer Country");
			put(MetadataGUIDS.CUSTOMER_DISTRICT_OR_SECTION, "Customer District or Section");
			put(MetadataGUIDS.CUSTOMER_EFFECTIVE_USE_END_DATE, "Customer Effective Use End Date");
			put(MetadataGUIDS.CUSTOMER_EFFECTIVE_USE_START_DATE_EARLIEST_KNOWN, "Customer Effective Use Start Date (Earliest Known)");
			put(MetadataGUIDS.CUSTOMER_HISTORIC_USE_COLOR, "Customer Historic Use Color");
			put(MetadataGUIDS.CUSTOMER_HISTORIC_USE_DESIGN, "Customer Historic Use Design");
			put(MetadataGUIDS.CUSTOMER_ID_COMMON, "Customer ID (Common)");
			put(MetadataGUIDS.CUSTOMER_ID_JUNS, "Customer ID (JUNS)");
			put(MetadataGUIDS.CUSTOMER_ID_ORACLE, "Customer ID (Oracle)");
			put(MetadataGUIDS.CUSTOMER_ID_WORKFLOW, "Customer ID (Workflow)");
			put(MetadataGUIDS.CUSTOMER_ID_PARENT_ORACLE, "Customer ID Parent (Oracle)");
			put(MetadataGUIDS.CUSTOMER_MARKET, "Customer Market");
			put(MetadataGUIDS.CUSTOMER_NAME, "Customer Name");
			put(MetadataGUIDS.CUSTOMER_NAME_WORKFLOW, "Customer Name (Workflow)");
			put(MetadataGUIDS.CUSTOMER_STATE, "Customer State");
			put(MetadataGUIDS.CUSTOMER_STATUS, "Customer Status");
			put(MetadataGUIDS.CUSTOMER_USE_APPROVAL_DATE, "Customer Use Approval Date");
			put(MetadataGUIDS.CUSTOMER_USE_APPROVER, "Customer Use Approver");
			put(MetadataGUIDS.CUSTOMER_USE_CURRENT_AS_OF_DATE, "Customer Use Current As Of Date");
			put(MetadataGUIDS.DATE_ART_DUE, "Date Art Due");
			put(MetadataGUIDS.DATE_PLANT_NEEDS, "Date Plant Needs");
			put(MetadataGUIDS.DESIGN_CONCEPT_ARTIST, "Design Concept Artist");
			put(MetadataGUIDS.DESIGN_CONCEPT_DATE_CREATED, "Design Concept Date Created");
			put(MetadataGUIDS.DESIGN_CONCEPT_OWNER, "Design Concept Owner");
			put(MetadataGUIDS.DETAIL_ENHANCEMENT_COLOR, "Detail Enhancement Color");
			put(MetadataGUIDS.DISPLAYED_INITIALS_AFFILIATION, "Displayed Initials (Affiliation)");
			put(MetadataGUIDS.DISPLAYED_MASCOT_NAME, "Displayed Mascot Name");
			put(MetadataGUIDS.DISPLAYED_MOTTO, "Displayed Motto");
			put(MetadataGUIDS.DISPLAYED_NAME_AFFILIATION, "Displayed Name (Affiliation)");
			put(MetadataGUIDS.DISPLAYED_OTHER_INSCRIPTION, "Displayed Other Inscription");
			put(MetadataGUIDS.DISPLAYED_YEARDATE, "Displayed Yeardate");
			put(MetadataGUIDS.EXTENT_OF_USABILITY, "Extent of Usability");
			put(MetadataGUIDS.FORMAT_FILE_CONTENT, "Format (File Content)");
			put(MetadataGUIDS.FORMAT_SOURCE_ASSET, "Format (Source Asset)");
			put(MetadataGUIDS.FUNCTIONAL_INTENT, "Functional Intent");
			put(MetadataGUIDS.FUNCTIONAL_UNIT, "Functional Unit");
			put(MetadataGUIDS.JEM_FACET, "Jostens Facet");
			put(MetadataGUIDS.JOB_ID, "Job ID");
			put(MetadataGUIDS.JOB_ID_COMPONENT, "Job ID (Component)");
			put(MetadataGUIDS.JOB_ID_WORKFLOW, "Job ID (Workflow)");
//			put(MetadataGUIDS.JOB_OWNER_ARTIST_NAME, "Job Owner (Artist Name)");
			put(MetadataGUIDS.JOB_SCHEDULE_PRIORITY, "Job Schedule Priority");
			put(MetadataGUIDS.JOB_STATUS, "Job Status");
			put(MetadataGUIDS.JOSTENS_ASSET_CREATOR, "Jostens Asset Creator");
			put(MetadataGUIDS.JOSTENS_ASSET_DATE_CREATED, "Jostens Asset Date Created");
			put(MetadataGUIDS.KEYWORD_ALL, "Keyword (All)");
//			put(MetadataGUIDS.KEYWORD_CLASS, "Keyword (Class)");
			put(MetadataGUIDS.KEYWORD_MAIN_SUBJECT, "Keyword (Main Subject)");
//			put(MetadataGUIDS.KEYWORD_ID, "Keyword ID");
			put(MetadataGUIDS.KEYWORD_MULTIPLE_MAIN_SUBJECT, "Keyword Multiple (Main Subject)");
			put(MetadataGUIDS.KEYWORD_PORTION_MAIN_SUBJECT, "Keyword Portion (Main Subject)");
			put(MetadataGUIDS.KEYWORD_VIEW_MAIN_SUBJECT, "Keyword View (Main Subject)");
			put(MetadataGUIDS.LICENSE_CONTRACT_EFFECTIVE_USE_END_DATE, "License Contract Effective Use End Date");
			put(MetadataGUIDS.LICENSE_CONTRACT_EFFECTIVE_USE_START_DATE, "License Contract Effective Use Start Date");
			put(MetadataGUIDS.LICENSE_CONTRACT_ID_NUMBER, "License Contract ID Number");
			put(MetadataGUIDS.LICENSE_CONTRACT_REVISION_DATE, "License Contract Revision Date");
			put(MetadataGUIDS.LICENSE_CONTRACT_STATUS, "License Contract Status");
			put(MetadataGUIDS.LICENSE_CONTRACT_USE_MEDIUM, "License Contract Use Medium");
			put(MetadataGUIDS.ORDER_ORIGINATOR_EMAIL, "Order Originator (Email)");
			put(MetadataGUIDS.ORDER_TYPE, "Order Type");
			put(MetadataGUIDS.PART_ID_PARENT, "Part ID (Parent)");
			put(MetadataGUIDS.PART_ID, "Part ID");
			put(MetadataGUIDS.PART_ID_DERIVATIVE, "Part ID (Derivative)");
			put(MetadataGUIDS.PART_ID_TYPE, "Part ID Type");
			put(MetadataGUIDS.PART_SIZE, "Part Size");
			put(MetadataGUIDS.PART_SIZE_ACCURACY, "Part Size Accuracy");
			put(MetadataGUIDS.PART_TYPE, "Part Type");
			put(MetadataGUIDS.PART_SIZE_HEIGHT, "Part Size Height");
			put(MetadataGUIDS.PART_SIZE_WIDTH, "Part Size Width");
			put(MetadataGUIDS.PART_VALIDATION, "Part Validation");
//			put(MetadataGUIDS.PERMISSION_TO_REUSE_GRANTED_BY_OWNER, "Permission to Reuse Granted By Owner");
//			put(MetadataGUIDS.PERMISSION_TO_REUSE_REQUIRED, "Permission to Reuse Required");
//			put(MetadataGUIDS.PERMISSION_TO_USE_GRANTED_BY_OWNER, "Permission to Use Granted By Owner");
//			put(MetadataGUIDS.PERMISSION_TO_USE_REQUIRED, "Permission to Use Required");
			put(MetadataGUIDS.PRODUCT_ORDERED_ART, "Product Ordered (Art)");
			put(MetadataGUIDS.PRODUCT_ORDERED_DIE, "Product Ordered (Die)");
			put(MetadataGUIDS.QUALITY_LEVEL, "Quality Level");
			put(MetadataGUIDS.RIGHTS_MANAGEMENT, "Rights Management");
			put(MetadataGUIDS.RIGHTS_MANAGEMENT_SET_DATE, "Rights Management Set Date");
			put(MetadataGUIDS.RIGHTS_MANAGEMENT_STATUS, "Rights Management Status");
			put(MetadataGUIDS.RIGHTS_MANAGEMENT_VALIDATION_DATE, "Rights Management Validation Date");
			put(MetadataGUIDS.RIGHTS_MANAGEMENT_VALIDATION_SOURCE, "Rights Management Validation Source");
			put(MetadataGUIDS.SALES_AREA, "Sales Area");
			put(MetadataGUIDS.SALES_REGION, "Sales Region");
			put(MetadataGUIDS.SALES_REP_ID, "Sales Rep ID");
			put(MetadataGUIDS.SALES_REP_NAME, "Sales Rep Name");
			put(MetadataGUIDS.SCHOOL_COLOR_1, "School Color 1");
			put(MetadataGUIDS.SCHOOL_COLOR_2, "School Color 2");
			put(MetadataGUIDS.SCHOOL_COLOR_3, "School Color 3");
			put(MetadataGUIDS.SCHOOL_INITIALS_OR_ABBREVIATION, "School Initials or Abbreviation");
			put(MetadataGUIDS.SCHOOL_MASCOT, "School Mascot");
			put(MetadataGUIDS.SCHOOL_MOTTO, "School Motto");
			put(MetadataGUIDS.SCHOOL_LICENSES_ITS_BRAND, "School Licenses Its Brand");
			put(MetadataGUIDS.STATUS_AUTOMATION, "Status (Automation)");
			put(MetadataGUIDS.STATUS_AUTOMATION_UPDATE, "Status (Automation Update)");
			put(MetadataGUIDS.STATUS_AVAILABILITY, "Status (Availability)");
			put(MetadataGUIDS.STATUS_CATALOGING, "Status (Cataloging)");
			put(MetadataGUIDS.STATUS_LIFE_CYCLE, "Status (Life Cycle)");
			put(MetadataGUIDS.STATUS_METADATA_DEPLOYMENT, "Status (Metadata Deployment)");
//			put(MetadataGUIDS.TRADEMARK_OWNER, "Trademark Owner");
//			put(MetadataGUIDS.TRADEMARK_STATUS, "Trademark Status");
			put(MetadataGUIDS.UPLOAD_QC_USER, "Upload QC User");
			put(MetadataGUIDS.VERSION_SCENARIO, "Version Scenario");
			put(MetadataGUIDS.VISUALLY_IDENTICAL_PART_DERIVATIVE, "Visually Identical Part (Derivative)");
			put(MetadataGUIDS.VISUALLY_IDENTICAL_PART, "Visually Identical Part");
//			put(MetadataGUIDS.VISUALLY_SIMILAR_PART_DERIVATIVE, "Visually Similar Part (Derivative)");
//			put(MetadataGUIDS.VISUALLY_SIMILAR_PART_MASTER, "Visually Similar Part (Master)");		
			put(MetadataGUIDS.WORKFLOW_HISTORY_AUTOMATION, "Workflow History Automation");
			put(MetadataGUIDS.WORKFLOW_ISSUE, "Workflow Issue");
			put(MetadataGUIDS.WORKFLOW_ISSUE_AUTOMATION, "Workflow Issue Automation");
			put(MetadataGUIDS.WORKFLOW_ISSUE_COMMENT, "Workflow Issue Comment");
			put(MetadataGUIDS.WORKFLOW_PROJECT, "Workflow Project");
			put(MetadataGUIDS.WORKFLOW_STAGE, "Workflow Stage");
			put(MetadataGUIDS.WORKFLOW_TYPE, "Workflow Type");
			// New for creative services
			put(MetadataGUIDS.SCHOOL_YEAR, "School Year");
			put(MetadataGUIDS.BUSINESS_UNIT, "Business Unit");
			put(MetadataGUIDS.PROJECT_NAME, "Project Name");
			put(MetadataGUIDS.MARKETING_CONTACT, "Marketing Contact");
			put(MetadataGUIDS.PROJECT_FORMAT, "Project Format");
			put(MetadataGUIDS.PROJECT_YEAR, "Project Year");
			put(MetadataGUIDS.METADATA_MAPPED, "Metadata Mapped");
			put(MetadataGUIDS.METADATA_SET_TIME, "Metadata Set Time");
			put(MetadataGUIDS.PROJECT_ART_DIRECTOR, "Project Art Director");
			put(MetadataGUIDS.ASSET_ARCHIVE, "Asset Archive");
			put(MetadataGUIDS.DIGITAL_ID, "Digital ID");
			put(MetadataGUIDS.ITEM_ID, "Item ID");
			put(MetadataGUIDS.IMAGE_SOURCE, "Image Source");
			put(MetadataGUIDS.IMAGE_SUBJECT, "Image Subject");
			put(MetadataGUIDS.LICENSE_AGREEMENT, "License Agreement");
			put(MetadataGUIDS.PRODUCT_AND_SERVICE, "Product & Service");
			put(MetadataGUIDS.PROJECT_RELEASE_DATE, "Project Release Date");
			put(MetadataGUIDS.DATE_MODIFIED, "Date Modified");
			put(MetadataGUIDS.IMAGE_SOURCE_BUSINESS_CONTEXT, "Image Source (Business Context)");
			put(MetadataGUIDS.COLOR_QUANTITY, "Color Quantity");
		}
	};

	// Flag metadata maintained by Mediabin, so don't try to alter these values
	private static final Map<String, String> MEDIABIN_METADATA = new HashMap<String, String>()
	{
		{
			put(MetadataGUIDS.COMMENT, "Comment");
			put(MetadataGUIDS.LAYER_CRC, "LAYER CRC");
			put(MetadataGUIDS.NAME, "Name");
			put(MetadataGUIDS.REVISION_COMMENT, "Revision Comment");
			put(MetadataGUIDS.MODIFICATION_TIME, "Modification Time");
			put(MetadataGUIDS.MODIFICATION_USER, "Modification User");
			put(MetadataGUIDS.CURRENT_REVISION, "Current Revision");
			put(MetadataGUIDS.DIMENSIONS_PIXELS, "Dimensions Pixels");
			put(MetadataGUIDS.RESOLUTION_PPI, "Resolution PPI");
			put(MetadataGUIDS.INSERTION_TIME, "Insertion Time");
			put(MetadataGUIDS.ASSET_TYPE, "Asset Type");
//			put(MetadataGUIDS.KEYWORDS, "Keywords");
			put(MetadataGUIDS.IMAGE_COLOR_CHANNELS, "Image Color Channels");
			put(MetadataGUIDS.BITS_PER_CHANNEL, "Bits Per Channel");
			put(MetadataGUIDS.ORIGINAL_FILE_SIZE_KB, "Original File Size (KB)");
		}
	};

	// Define a List of all known metadata GUIDS based on the defined Names as an Array
	public static final String[] ALL_DEFINED_GUIDS_ARRAY = (String[])NAMES.keySet().toArray(new String[0]);

	// Define a List of all known metadata GUIDS based on the defined Names as a List
	public static final List<String> ALL_DEFINED_GUIDS_LIST = new ArrayList<String>(NAMES.keySet());

	// Define a List of all known Mediabin metadata GUIDS based on the defined MEDIABIN_METADATA as a List
	public static final List<String> MEDIABIN_GUIDS_LIST = new ArrayList<String>(MEDIABIN_METADATA.keySet());

	// Define a List of all known Mediabin metadata GUIDS based on the defined MEDIABIN_METADATA as a List
	public static final String[] MEDIABIN_GUIDS_ARRAY = (String[])MEDIABIN_METADATA.keySet().toArray(new String[0]);

}

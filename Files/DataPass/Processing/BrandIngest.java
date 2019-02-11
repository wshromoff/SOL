package com.jostens.dam.brand.assetclass.brand;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jostens.dam.brand.assetclass.BaseIngest;
import com.jostens.dam.brand.assetclass.ShallowCopyAsset;
import com.jostens.dam.brand.assetclass.interfaces.BrandNamedAsset;
import com.jostens.dam.brand.attributes.brand.AssetClass;
import com.jostens.dam.brand.folders.BrandFolderGUIDS;
import com.jostens.dam.brand.helpers.FolderGUIDSHelper;
import com.jostens.dam.brand.process.CatalogAssetPackage;
import com.jostens.dam.shared.assetclass.BaseAsset;
import com.jostens.dam.shared.assetclass.MetadataGUIDS;
import com.jostens.dam.shared.common.JostensGUID;
import com.jostens.dam.shared.folders.FolderGUIDS;
import com.jostens.dam.shared.helpers.AssetSearchHelper;
import com.jostens.dam.shared.helpers.MetadataHelper;
import com.jostens.dam.shared.idol.IDOLSearchHelper;
import com.jostens.dam.shared.properties.RuntimeProperties;
import com.viragemediabin.www.MBMetadataParameter;
import com.viragemediabin.www.MBObject;
import com.viragemediabin.www.MBObjectType;
import com.viragemediabin.www.VirageMediaBinServerSoap;

public class BrandIngest extends BaseIngest
{
	// Groupings object for use throughout
	private BrandGroupings brandGroupings = null;
	// Object that determines metadata values to use for use throughout
	private BrandSetMetadata brandSetMetadata = null;
	// Keep the master asset for later usage
	private BrandAsset masterAsset = null;
	// Keep customer meta data for updating into the needed assets
	private Map<String,MBMetadataParameter> customerMetadata = null;
	// Hold value of design concept for future use - This is a Jostens ID
	private String designConceptId = null;
	// Created Design concept ID
	private String createdDesignConceptGUID = null;
	// Created Part GUID
	private String createdPartGUID = null;
	// Store color scheme jostens GUIDs based on a key defined by method getColorSchemeKey() for later reuse
	private Map<String, String> colorSchemeIds = new HashMap<String, String>();
	// These are some constants defined as arrays which are needed for MB webservice calls
	public static String[] designConceptFieldGUID = {MetadataGUIDS.ASSET_ID_DESIGN_CONCEPT};
	// These fields are used to get needed information for an existing color scheme
	public static String[] colorSchemeFieldGUIDs = {MetadataGUIDS.ASSET_ID_COLOR_SCHEME, MetadataGUIDS.COLOR_SCHEME, 
														MetadataGUIDS.BASE_COLOR_TONES_RENDERED, MetadataGUIDS.DETAIL_ENHANCEMENT_COLOR};

	/**
	 * Implementers must define a initialize method for the ingester.  If nothing of significance is needed, leave empty method.
	 */
	protected void initIngestProcess()
	{
	}

	/**
	 * Implementers must define a reset method prior to the processing of each cdr package.  This is where class level variables should be cleared.
	 */
	protected void resetIngestProcess()
	{
		if (brandSetMetadata == null)
		{
			brandSetMetadata = new BrandSetMetadata();
			brandGroupings = new BrandGroupings();
		}
		colorSchemeIds.clear();
		masterAsset = null;
		customerMetadata = null;
		designConceptId = null;
		createdDesignConceptGUID = null;
		createdPartGUID = null;
	}

	/**
	 * For the specified masterAsset, implementors should load all assets that should be ingested with the master asset and
	 * populate those assets as required.
	 * Included is a signiture method where a populateMetadata flag can be provided.
	 */
	@Override
	public List<BaseAsset> getAssetsInPackage(VirageMediaBinServerSoap ws, BaseAsset masterAsset) throws RemoteException
	{
		return getAssetsInPackage(ws, masterAsset, true);
	}
	@Override
	public List<BaseAsset> getAssetsInPackage(VirageMediaBinServerSoap ws, BaseAsset masterAsset, boolean populateMetadata) throws RemoteException
	{
		log.debug("getAllAssetsInPackage: Retrieving asset package for assetId: " + masterAsset.getAssetId());
		List<BaseAsset> assetPackage = new ArrayList<BaseAsset>();
		assetPackage.add(masterAsset);		// Add master Asset

		// Perform a search for all assets in  the masterAsset's parent folder whose base name matches
		BrandAsset ba = (BrandAsset)masterAsset;
		String containingFolder = MetadataGUIDS.removeSpecialCharacters(ba.getFolderGUID());
		log.debug("Performing search in master asset folder for all assets with base name: " + ba.getBaseName());
		String search = "WILD{" + ba.getBaseName() + "*}:MBT" + MetadataGUIDS.removeSpecialCharacters(MetadataGUIDS.NAME);
		
		IDOLSearchHelper idolSearchHelper = new IDOLSearchHelper();
		List<BaseAsset> idolFoundAssets = idolSearchHelper.findInFolderConstrained(containingFolder, search, populateMetadata);
		for ( BaseAsset asset : idolFoundAssets )
		{	// Loop through assets, watch out for master asset because it's already been added
			String id = asset.getAssetId();
			if ( !masterAsset.getAssetId().equals(id))
			{
				BrandAsset renditionAsset = new BrandAsset( asset.getAssetName(), id );
				renditionAsset.getAssetMetadata().putAll(asset.getAssetMetadata());
				if ( renditionAsset.isRendition())
				{
					renditionAsset.setFolderGUID(ba.getFolderGUID());
					renditionAsset.setParentMasterId(ba.getJostensGUID());
					assetPackage.add(renditionAsset);
				}
				else
				{
					log.error("AssetID is not a rendition asset: " + asset.getAssetName());
				}
			}				
		}

		log.debug("getAllAssetsInPackage: Assets in package " + assetPackage.size());
		return assetPackage;		
	}

	/**
	 * Sort a set of BrandAssets by the custom sort option defined in BrandAsset.
	 * 		Assets should return in this order: black PNG, standard PNGs, other renditions, master  
	 */
	@Override
	public BaseAsset[] sortPackageAssets(List<BaseAsset> assetPackage)
	{
		BaseAsset[] packageAsArray = assetPackage.toArray( new BaseAsset[0]);
		Arrays.sort(packageAsArray);
		return packageAsArray;
	}

	@Override
	public boolean validSortedAssets(BaseAsset[] sortedAssets)
	{
		// Determine if this is a pantone colored asset - This affects if a package is considered valid
		BrandNamedAsset bna = (BrandNamedAsset)sortedAssets.clone()[0];
		boolean pantonePackage = bna.isPantoneColored();
		
		validateErrorCondition = "";		// Reset error condition
		// Step through looking for failures
		// Needs to be an odd number of assets
		log.debug("validating a sorted asset package of size " + sortedAssets.length);
		if (!pantonePackage && sortedAssets.length % 2 == 0)
		{
			validateErrorCondition = "Failed: even number of assets";
			return false;
		}
		if (pantonePackage && sortedAssets.length % 2 != 0)
		{
			validateErrorCondition = "Failed: odd number of assets for pantone package";
			return false;
		}
		BrandAsset firstAsset = (BrandAsset)sortedAssets[0];
		if (!firstAsset.isBlackRendition())
		{
			validateErrorCondition = "Failed: first asset not black PNG";
			return false;
		}
		BrandAsset lastAsset = (BrandAsset)sortedAssets[sortedAssets.length - 1];
		if (!lastAsset.isMasterAsset())
		{
			validateErrorCondition = "Failed: last asset is not master";
			return false;
		}
		int pngCount = 0;
		int svgCount = 0;
		int epsCount = 0;
		boolean baFound = false;
		boolean c1Found = false;
		boolean c0Found = false;
		for (BaseAsset baseAsset : sortedAssets)
		{
			BrandAsset asset = (BrandAsset)baseAsset;
			if ("png".equals(asset.getExtension()))
			{
				pngCount++;
				if (asset.getNameFields()[18].equals("ba"))
				{
					baFound = true;
				}
				if (asset.getNameFields()[18].equals("c1"))
				{
					c1Found = true;
				}
				if (asset.getNameFields()[18].equals("c0"))
				{
					c0Found = true;
				}
				continue;
			}
			if ("svg".equals(asset.getExtension()))
			{
				svgCount++;
				if (asset.getNameFields()[18].equals("c0"))
				{
					c0Found = true;
				}
				continue;
			}
			if ("eps".equals(asset.getExtension()))
			{
				epsCount++;
				continue;
			}
			if (asset.isMasterAsset())
			{
				break;
			}
			validateErrorCondition = "Failed: extension other than .png, .svg, .eps and .cdr was found";
			return false;
		}
		if (pngCount != svgCount)
		{
			validateErrorCondition = "Failed: .png and .svg counts don't match";
			return false;
		}
		if (!baFound && !c1Found)
		{
			validateErrorCondition = "Failed: ba or c1 asset not found";
			return false;
		}
		if (pantonePackage && epsCount != 3)
		{
			validateErrorCondition = "Failed: invalid number of .eps assets";
			return false;
		}
		
		// Make sure total assets in package is correct
		// Based on .cdr, 2 ba and 2 c1 so start with 5, then add in others that should be there and match to actual
		int expectedCount = 5;
		// If a pantone package, there will be 3 .eps assets
		if (pantonePackage)
		{
			expectedCount += epsCount;
		}
		// It can't be determined from the filename if c0 assets should be in the package, so add 2 if any c0 assets were found
		if (c0Found)
		{
			expectedCount += 2;
		}
		
		// Check master asset filename to determine number of renditions. 
		// If "Base Color Tones Rendered" ="1t" and "Detail Enhancement Color" ="dx" and "Color 1" ="bks", "gds", or "svs" and "Color 2" = "x" then +4, else +6
		BrandAsset masterAsset = (BrandAsset)sortedAssets[sortedAssets.length - 1];
		if (!"1t".equals(masterAsset.getNameFields()[5]) || !"dx".equals(masterAsset.getNameFields()[6]) || !("bks".equals(masterAsset.getNameFields()[8]) || 
				"gds".equals(masterAsset.getNameFields()[8]) || "svs".equals(masterAsset.getNameFields()[8])) || !"x".equals(masterAsset.getNameFields()[9]))
		{
			expectedCount += 6;
		}
		else
		{
			expectedCount += 4;
		}
		// Add 2 for each additional color found in master asset filename
		for (int i = 9; i <= 17; i++)
		{
			if (!masterAsset.getNameFields()[i].equals("x"))
			{
				expectedCount += 2;
			}
		}
		// If a detail enhancement color is found add 2
		if (masterAsset.getDetailEnhancementColor() != null)
		{
			expectedCount += 2;
		}

		// Compare expectedCount to actual count
		if (expectedCount != sortedAssets.length)
		{
			validateErrorCondition = "Failed: Total asset counts don't match: expected: " + expectedCount + "  actual: " + sortedAssets.length;
			return false;
		}
		return true;
	}

	@Override
	public boolean validShallowCopies(List<ShallowCopyAsset> shallowCopies, BaseAsset cdr)
	{
		validateErrorCondition = "";	// Reset error condition
		int expectedCount = 4; 			// Start with 1 design concept and 3 color schemes
		BrandAsset cdrBrand = (BrandAsset)cdr;
		
		// If asset has multiple tones, has detail enhancement, is not gold, black, or silver for color 1, or has a color 2, it should have one additional color scheme
		if (!"1t".equals(cdrBrand.getNameFields()[5]) || !"dx".equals(cdrBrand.getNameFields()[6]) || (!("bks".equals(cdrBrand.getNameFields()[8]) || "gds".equals(cdrBrand.getNameFields()[8]) || "svs".equals(cdrBrand.getNameFields()[8]))) || !"x".equals(cdrBrand.getNameFields()[9]))
		{
			expectedCount += 1;
		}
		
		// If asset is not a derivative, it should have a part
		if (cdrBrand.isManufacturingPart())
		{
			expectedCount += 1;
		}
		
		// Compare the expected count to the actual number of shallow copies
		if (expectedCount != shallowCopies.size())
		{
			validateErrorCondition = "Failed: Total asset counts don't match: expected: " + expectedCount + "  actual: " + shallowCopies.size();
			return false;
		}
		return true;
	}
	
	/**
	 * Take the supplied sorted asset package and perform the ingest of the package.  This processing is very asset class
	 * specific and will use helper methods found in this base class.
	 * 
	 * This is the start of ingesting assets.
	 */
	@Override
	public boolean performIngest(VirageMediaBinServerSoap ws, CatalogAssetPackage catalogPackage) throws RemoteException
	{
		Map<String,MBMetadataParameter> mdMap = new HashMap<String,MBMetadataParameter>();

		// Setup some class attributes
		BaseAsset[] sortedAssets = catalogPackage.getSortedAssets();
		masterAsset = (BrandAsset)catalogPackage.getMasterCDR();

		// Get customer metadata and store for later usage
		customerMetadata = getCustomerData(ws, masterAsset.getCustomerID());
		// Customer metadata needs to be applied to all sortedAssets.  At this point it's not being saved to the asset
		// only added to the BaseAsset metadata information.  This way it's available for attribute class processing.  An asset 
		// setup method will be resonable for adding customer data to the update list so it's saved to the asset
		for (BaseAsset asset : sortedAssets)
		{
			asset.getAssetMetadata().putAll(customerMetadata);
		}

		// March 2014 - Unique functionality for 'Business Default (Generic)' assets and Business Default Use field
		if ("Business Default (Generic)".equals(masterAsset.getMetaDataValue(MetadataGUIDS.BUSINESS_DEFAULT_USE)))
		{
			// For BaseStandardColorRenditions, add business default use to them - all other BDU values, this value is not applicable
			// With this added to the 1b if present it will then get applied to shallow copies also
			for (BaseAsset asset : sortedAssets)
			{
				BrandAsset ba = (BrandAsset)asset;
				mdMap.clear();
				if (ba.isBaseStandardColorRendition())
				{
					ba.addAssetMetadata(MetadataGUIDS.BUSINESS_DEFAULT_USE, MetadataHelper.getMultiSelectParameter(MetadataGUIDS.BUSINESS_DEFAULT_USE, "Business Default (Generic)"));
					mdMap.put(MetadataGUIDS.BUSINESS_DEFAULT_USE, MetadataHelper.getMultiSelectParameter(MetadataGUIDS.BUSINESS_DEFAULT_USE, "Business Default (Generic)"));
					MetadataHelper.updateMetaData(ws, ba, mdMap);
				}			
			}
		}
		
		// 
		// Create Shallow Copies if needed
		for (BaseAsset asset : sortedAssets)
		{
			BrandAsset ba = (BrandAsset)asset;
			// If asset is the black PNG rendition - make sure the PART and DESIGN CONCEPT shallow copies are created
			if (ba.isBlackRendition())
			{
				setupPartAndDesignConcept(ws, ba);
			}

			// If a design concept is not set, the ingest should not proceed
			if (designConceptId == null)
			{
				log.info("Design concept is not set, so ending ingest!");
				return false;
			}

			ba.setDesignId(designConceptId);	// Set design concept on all assets
			
			// If the asset is a png and a 1b, 1g, 1s or ba it is used to create color scheme and
			// brand shallow copies
			if ( ba.isStandardColorPNGRendition())
			{
				// Color scheme must be setup prior to setting up the brand
				setupColorScheme(ws, ba);
			}		

		}
		
		// Now that shallow copies are created, move .cdr, .svg, .png, .eps files to archive location
		for (BaseAsset asset : sortedAssets)
		{
			BrandAsset ba = (BrandAsset)asset;
			if (ba.isMasterAsset())
			{
				if (performRevision)
				{	// Revision means the master is rev'd and not moved into place
					revAssetToArchive(ws, ba);
				}
				else
				{
					moveAssetToArchive(ws, ba);
				}
				setupMasterAsset(ws, ba);
			}
			else
			{
				moveAssetToArchive(ws, ba);
				setupRenditionAsset(ws, ba);
			}
		}
		
		// Perform update of part and design concept shallow copies with color scheme for the Black part
		setPartColorScheme(ws);
		setDesignConceptColorScheme(ws);

		return true;
	}

	/**
	 * Setup a shallow copy PART and DESIGN CONCEPT from the supplied black PNG rendition.
	 */
	public void setupPartAndDesignConcept( VirageMediaBinServerSoap ws, BrandAsset blackPNGRendition ) throws RemoteException
	{
		String[] designConceptGUID = {MetadataGUIDS.ASSET_ID_DESIGN_CONCEPT};

		// Step 1 - Find an asset GUID that could contain an Asset_ID_Design_Concept value.  The search is done different
		// depending if it's a manufacturing part or not.
		String assetWithDesignConcept = null;
		if (blackPNGRendition.isManufacturingPart())
		{	// Manufacturing parts, look in PART folder for a already created shallow copy
			assetWithDesignConcept = AssetSearchHelper.findInFolderWithName(ws, FolderGUIDS.getFolderGuid(BrandFolderGUIDS.PART), blackPNGRendition.getPartID());
			
			// 6/23/2014 - In the situation where there were 2 matching derivative packages sharing the same part ID but different asset types, the previous
			// logic would set them to use the same design concept even if the images were different.  This issue was because only a search was done for a matchingn derivative
			// part and if found the design concept was used without looking at the image.  To resolve, it seemed best to always do a CRC check for the
			// derivative asset.  This will enable the different designs to get the unique design concept shallow copy.
		
			// Step 2 - If a manufacturing part and a asset was found that should contain a Asset_ID_Design_Concept value, retrieve that value from the asset
			if (assetWithDesignConcept != null)
			{
				designConceptId = MetadataHelper.getSingleMetaDataForAsset(ws, assetWithDesignConcept, designConceptGUID);
				if (designConceptId != null)
				{	// A asset_id_design_concept value was found on the asset like it should have been
					// Validate a design concept is found with that name
					String designConceptAssetGUID = AssetSearchHelper.findInFolderWithName(ws, FolderGUIDS.getFolderGuid(BrandFolderGUIDS.DESIGN_CONCEPT), designConceptId);
					if (designConceptAssetGUID != null)
					{
						log.debug("Part (" + blackPNGRendition.getPartID() + ") and Design Concept (" + designConceptId + ") already exists.");
						return;
					}
					log.info("Part (" + blackPNGRendition.getPartID() + ") was found, but Design Concept (" + designConceptId + ") could not be found. ");
				}
				else
				{
					log.info("Part (" + blackPNGRendition.getPartID() + ") was unable to find expected asset_id_design_concept value. ");
				}
			}
		}

		// Step 3 - Need to find an existing Design concept using a CRC check or create a new one. 
		// Design concept retrieval and creation is synchronized because a CRC check is used and it can cause problems
		// one thread were to be creating a design concept while another package which uses the same DC tried to find
		// the match using a CRC check.
		synchronized(this)
		{
			String crcAssetId = findDesignConceptUsingCRC(ws, blackPNGRendition);
			if (crcAssetId != null)
			{	// DC found using CRC check. Get that DCs JostensGUID and set on blackPNGRendition
				designConceptId = MetadataHelper.getSingleMetaDataForAsset(ws, crcAssetId, designConceptGUID);
				blackPNGRendition.setDesignId(designConceptId);
			}
			else
			{	// DC not found using CRC check.  Need to get a new jostensGUID which will also be the name of the newly created DC
				designConceptId = JostensGUID.randomGUID();
				blackPNGRendition.setDesignId(designConceptId);
				Map<String, MBMetadataParameter> designConceptMetadata = brandSetMetadata.getDesignConceptMetaData(blackPNGRendition);
				createdDesignConceptGUID = createDesignConcept(ws, designConceptId, blackPNGRendition, designConceptMetadata, brandGroupings.getMetadataGroup(BrandGroupings.DESIGNCONCEPT_METADATA_GROUP));
				log.info("Created design concept with Jostens ID: " + designConceptId);
			}
		}

		// Step 4 - For manufacturing parts, create a new part if needed.  If not a new part, update an existing part with the designConceptId value found in Step 3
		if (blackPNGRendition.isManufacturingPart())
		{
			Map<String, MBMetadataParameter> partMetadata = brandSetMetadata.getPartMetaData(blackPNGRendition);
			if (assetWithDesignConcept == null)
			{
				createdPartGUID = createPart(ws, blackPNGRendition.getPartID(), blackPNGRendition, partMetadata, brandGroupings.getMetadataGroup(BrandGroupings.PART_METADATA_GROUP));
				log.info("Created part with name: " + blackPNGRendition.getPartID());
			}
			else
			{	// Update part with new metadata including design concept
				BaseAsset ba = new BaseAsset();
				ba.setAssetId(assetWithDesignConcept);
				MetadataHelper.updateMetaData(ws, ba, partMetadata);
			}
		}
	}

	/**
	 * Setup a COLOR SCHEME shallow copie from the supplied asset.
	 * 				asset is .png, and is 1b, 1g, 1s or ba.
	 * 
	 * Method is synchronized so two threads don't attempt to create a color scheme at the same time being
	 * an idol query is used.
	 * 
	 * The query portion was moved to its own method in March 2017
	 */
	public MBObject[] getColorSchemeAssets(BrandAsset asset)
	{
		// Build an IDOL query that will determine if a color scheme exists with these fields as the keys:
		// 		design concept ID, color scheme, base color tones rendered, detailed enhancement color
		StringBuffer query = new StringBuffer();
		query.append("MATCH{");
		query.append(designConceptId);
		query.append("}:MBT");
		query.append(MetadataGUIDS.removeSpecialCharacters(MetadataGUIDS.ASSET_ID_DESIGN_CONCEPT));
		if ( asset.getColorScheme() != null )
		{
			query.append("+AND+MATCH{");
			query.append(asset.getColorScheme().replaceAll(",", "\\\\,"));	// comma needs to be escaped or will be treated as an 'or'
			query.append("}:MBT");
			query.append(MetadataGUIDS.removeSpecialCharacters(MetadataGUIDS.COLOR_SCHEME));
		}
		if ( asset.getBaseColorTones() != null )
		{
			query.append("+AND+MATCH{");
			query.append(asset.getBaseColorTones());
			query.append("}:MBT");
			query.append(MetadataGUIDS.removeSpecialCharacters(MetadataGUIDS.BASE_COLOR_TONES_RENDERED));
		}
		if ( asset.getDetailEnhancementColor() != null )
		{
			query.append("+AND+MATCH{");
			query.append(asset.getDetailEnhancementColor());
			query.append("}:MBT");
			query.append(MetadataGUIDS.removeSpecialCharacters(MetadataGUIDS.DETAIL_ENHANCEMENT_COLOR));
		}
		else
		{
			query.append("+AND+EMPTY{}:MBT");
			query.append(MetadataGUIDS.removeSpecialCharacters(MetadataGUIDS.DETAIL_ENHANCEMENT_COLOR));			
		}
//		log.debug("Constraint Query: " + query.toString());		

		IDOLSearchHelper searchHelper = new IDOLSearchHelper();
		MBObject[] colorSchemeAssets = searchHelper.findInFolderConstrained(FolderGUIDS.getFolderGuid(BrandFolderGUIDS.COLOR_SCHEME), query.toString());
		return colorSchemeAssets;
	}
	public synchronized void setupColorScheme( VirageMediaBinServerSoap ws, BrandAsset asset ) throws RemoteException
	{
		log.debug("Setting up color scheme for asset: " + asset.getAssetName());

		// Step 1 - Get color scheme assets that match the supplied asset
		MBObject[] colorSchemeAssets = getColorSchemeAssets(asset);
		
		// For an existing color scheme, keep the GUID and name of the asset
		String colorSchemeGUID = null;
		String colorSchemeName = null;
		if (colorSchemeAssets != null && colorSchemeAssets.length > 0)
		{
			colorSchemeGUID = colorSchemeAssets[0].getIdentifier();
			colorSchemeName = colorSchemeAssets[0].getName();
		}
		log.debug("Found asset in Color_Scheme folder with GUID:" + colorSchemeGUID);

		// Step 2 - Create a new color scheme if needed, otherwise keep jostens GUID for the color scheme
		// for use in Brand creation
		if ( colorSchemeGUID == null )
		{	// Need to create a new color scheme
				String colorSchemeJostensId = JostensGUID.randomGUID();			
				asset.setColorSchemeId(colorSchemeJostensId);
				colorSchemeName = asset.getColorScheme() + " (" + colorSchemeJostensId + ")";
				Map<String, MBMetadataParameter> colorSchemeMetadata = brandSetMetadata.getColorSchemeMetaData(asset);

				colorSchemeGUID = createColorScheme(ws, colorSchemeName, asset, colorSchemeMetadata, brandGroupings.getMetadataGroup(BrandGroupings.COLORSCHEME_METADATA_GROUP));
				colorSchemeIds.put(asset.getColorSchemeKey(), colorSchemeJostensId);
				log.info("Created color scheme shallow copy with name = " + colorSchemeName);				

		}
		else
		{	
			// For the existing color scheme create a shallow copy asset and populate with the fields that enable an 
			// accurate entry into colorSchemesIds
			ShallowCopyAsset sc = new ShallowCopyAsset(colorSchemeName, colorSchemeGUID, ShallowCopyAsset.COLOR_SCHEME);
			MetadataHelper.populateAssetMetadata(ws, sc, colorSchemeFieldGUIDs);
			// Build a color scheme key for entry into the colorSchemeIds Map
			String colorSchemeJostensId = sc.getMetaDataValue(MetadataGUIDS.ASSET_ID_COLOR_SCHEME);
			String colorScheme = sc.getMetaDataValue(MetadataGUIDS.COLOR_SCHEME);
			String baseColorTones = sc.getMetaDataValue(MetadataGUIDS.BASE_COLOR_TONES_RENDERED);
			String detailEnhancementColor = sc.getMetaDataValue(MetadataGUIDS.DETAIL_ENHANCEMENT_COLOR);
			if (detailEnhancementColor == null)
			{
				detailEnhancementColor = "None";
			}
			String colorSchemeKey = colorScheme + "_" + baseColorTones + "_" + detailEnhancementColor;
			colorSchemeIds.put(colorSchemeKey, colorSchemeJostensId);
			asset.setColorSchemeId(colorSchemeJostensId);
		}

	}

	/**
	 * After the color schemes have all been found, if a part was created set the Black color scheme on the part. 
	 */
	public void setPartColorScheme( VirageMediaBinServerSoap ws) throws RemoteException
	{
		if (createdPartGUID == null)
		{
			return;		// Part wasn't created so don't set a color scheme
		}
		// Get the color scheme value for this rendition
		Map<String,MBMetadataParameter> mdValues = new HashMap<String,MBMetadataParameter>();
		String colorSchemeGUID = colorSchemeIds.get("Black_1_None");
		if (colorSchemeGUID != null)
		{
			MBMetadataParameter colorGuids = new MBMetadataParameter();
			colorGuids.setIdentifier(MetadataGUIDS.ASSET_ID_COLOR_SCHEME);
			Object[] obs = {colorSchemeGUID};
			colorGuids.setValues(obs);
			mdValues.put(MetadataGUIDS.ASSET_ID_COLOR_SCHEME,colorGuids);
		}
		else
		{
			log.warn("Could not find a Black color scheme for part asset");
			return;
		}
		
		ws.MBObject_ReviseMetadataValues(MBObjectType.Asset, createdPartGUID, mdValues.values().toArray(new MBMetadataParameter[0]), null);
	}

	/**
	 * After the color schemes have all been found, if a design concept was created set the Black color scheme on the part. 
	 */
	public void setDesignConceptColorScheme( VirageMediaBinServerSoap ws) throws RemoteException
	{
		if (createdDesignConceptGUID == null)
		{
			return;		// Part wasn't created so don't set a color scheme
		}
		// Get the color scheme value for this rendition
		Map<String,MBMetadataParameter> mdValues = new HashMap<String,MBMetadataParameter>();
		String colorSchemeGUID = colorSchemeIds.get("Black_1_None");
		if (colorSchemeGUID != null)
		{
			MBMetadataParameter colorGuids = new MBMetadataParameter();
			colorGuids.setIdentifier(MetadataGUIDS.ASSET_ID_COLOR_SCHEME);
			Object[] obs = {colorSchemeGUID};
			colorGuids.setValues(obs);
			mdValues.put(MetadataGUIDS.ASSET_ID_COLOR_SCHEME,colorGuids);
		}
		else
		{
			log.warn("Could not find a Black color scheme for Design Concept asset");
			return;
		}
		
		ws.MBObject_ReviseMetadataValues(MBObjectType.Asset, createdDesignConceptGUID, mdValues.values().toArray(new MBMetadataParameter[0]), null);
	}

	/**
	 * Move all .cdr, .png, .svg, and .eps assets to the archive folder structure
	 */
	public void moveAssetToArchive( VirageMediaBinServerSoap ws, BrandAsset asset ) throws RemoteException
	{
		String destinationFolder = FolderGUIDS.getFolderGuid(FolderGUIDSHelper.getFolderGUIDSName(asset.getAssetName()));
		if ( !asset.getFolderGUID().equals(destinationFolder))
		{
			String duplicate = AssetSearchHelper.findInFolderWithName(ws,destinationFolder,asset.getAssetName());
			
			if ( duplicate == null )
			{
				// Check property file to determine if assets should be copied or moved to the Store
				RuntimeProperties properties = new RuntimeProperties();
				if ("Enabled".equals(properties.getProperty(RuntimeProperties.BUSINESS_RULES_MOVE)))
				{
					ws.MBObject_Move(MBObjectType.Asset, asset.getAssetId(), destinationFolder, asset.getAssetName());
				}
				else
				{
					ws.MBObject_Copy(MBObjectType.Asset, asset.getAssetId(), destinationFolder, asset.getAssetName());
					String copiedGUID = AssetSearchHelper.findInFolderWithName(ws, destinationFolder, asset.getAssetName());
					ws.MBObject_Delete(MBObjectType.Asset, asset.getAssetId());
					
					// Set GUID so process can continue with copied version of the asset
					asset.setAssetId(copiedGUID);
				}
				
				if (asset.isMasterAsset())
				{
					log.info(asset.getAssetName() + " was moved to Master/CDR folder.");
				}
				else
				{
					if ("png".equals(asset.getExtension()))
					{
						log.info(asset.getAssetName() + " was moved to Rendition/PNG folder.");				
					}
					else if ("svg".equals(asset.getExtension()))
					{
						log.info(asset.getAssetName() + " was moved to Rendition/SVG folder.");				
					}
					else
					{
						log.info(asset.getAssetName() + " was moved to Rendition/EPS folder.");	
					}
				}
			}
			else
			{
				log.warn("Duplicate asset: " + asset.getAssetName());
				return;
			}			
		}
	}
	
	@Override
	protected String getAssetClassName()
	{
		return AssetClass.BRAND_ASSET;
	}

	/**
	 * Setup Master Asset with color schemeIds for the color schemes in Archive Master Folder.
	 * 
	 * Need to also remove metadata fields along with the final master asset updates.
	 */
	public void setupMasterAsset( VirageMediaBinServerSoap ws, BrandAsset masterAsset ) throws RemoteException
	{
		String[] removeFieldGUIDs = {MetadataGUIDS.STATUS_AUTOMATION, MetadataGUIDS.JOB_SCHEDULE_PRIORITY};
		
		Map<String,MBMetadataParameter> mdValues = brandSetMetadata.getMasterMetaData(masterAsset);

		MBMetadataParameter colorGuids = new MBMetadataParameter();
		colorGuids.setIdentifier(MetadataGUIDS.ASSET_ID_COLOR_SCHEME);
		Object[] obs = colorSchemeIds.values().toArray();
//			for (Object o : obs)
//			{
//				System.out.println("-->" + o.toString());
//			}
		colorGuids.setValues(obs);
		mdValues.put(MetadataGUIDS.ASSET_ID_COLOR_SCHEME,colorGuids);
		if (customerMetadata != null)
		{
			mdValues.putAll(customerMetadata);		// Add customer master asset to asset
		}

		MetadataHelper.updateAndRemoveMetaData(ws, masterAsset, mdValues, removeFieldGUIDs);
	}

	/**
	 * Setup Master Asset with color schemeIds for the color schemes in Archive Master Folder
	 */
	public void setupRenditionAsset( VirageMediaBinServerSoap ws, BrandAsset renditionAsset ) throws RemoteException
	{
		String[] standardColorRemovals = {MetadataGUIDS.BUSINESS_DEFAULT_USE, MetadataGUIDS.JOB_SCHEDULE_PRIORITY};
		String[] nonStandardColorRemovals = {MetadataGUIDS.JOB_SCHEDULE_PRIORITY};
		Map<String,MBMetadataParameter> mdValues = brandSetMetadata.getRenditionMetaData(renditionAsset);

		// Get the color scheme value for this rendition
		String colorSchemeGUID = colorSchemeIds.get(renditionAsset.getColorSchemeKey());
		if (colorSchemeGUID != null)
		{
			MBMetadataParameter colorGuids = new MBMetadataParameter();
			colorGuids.setIdentifier(MetadataGUIDS.ASSET_ID_COLOR_SCHEME);
			Object[] obs = {colorSchemeGUID};
			colorGuids.setValues(obs);
			mdValues.put(MetadataGUIDS.ASSET_ID_COLOR_SCHEME,colorGuids);
		}
		else
		{
			log.warn("Could not find a color scheme for rendition asset: " + renditionAsset.getAssetName() + " with color scheme: " + renditionAsset.getColorScheme());
		}
		if (customerMetadata != null)
		{
			mdValues.putAll(customerMetadata);		// Add customer master asset to asset
		}

		// March 2014 - Unique functionality for 'Business Default (Generic)' assets and Business Default Use field
		if ("Business Default (Generic)".equals(masterAsset.getMetaDataValue(MetadataGUIDS.BUSINESS_DEFAULT_USE)))
		{
			// Don't remove the business default use from any renditions
			ws.MBObject_ReviseMetadataValues(MBObjectType.Asset, renditionAsset.getAssetId(), mdValues.values().toArray(new MBMetadataParameter[0]), nonStandardColorRemovals);			
			return;
		}
		if (renditionAsset.isBaseStandardColorRendition())
		{
			ws.MBObject_ReviseMetadataValues(MBObjectType.Asset, renditionAsset.getAssetId(), mdValues.values().toArray(new MBMetadataParameter[0]), standardColorRemovals);
		}
		else
		{
			ws.MBObject_ReviseMetadataValues(MBObjectType.Asset, renditionAsset.getAssetId(), mdValues.values().toArray(new MBMetadataParameter[0]), nonStandardColorRemovals);			
		}
	}

	/**
	 * For use by unit tests ONLY!!!
	 */
	public void setDesignConceptId(String designConceptId)
	{
		this.designConceptId = designConceptId;
	}
}

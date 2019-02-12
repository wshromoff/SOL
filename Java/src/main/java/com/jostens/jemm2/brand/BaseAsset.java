package com.jostens.jemm2.brand;

import java.util.HashMap;
import java.util.Map;

/**
 * Attributes and methods that are common across all MediaBin assets.
 * 
 * Should avoid adding any attributes that are part of business logic processing.  Objective is to keep
 * this object as clean as possible.
 */
public class BaseAsset
{
	// Attributes defining the asset
	private String assetName = null;
	private String assetId = null;
	// Attributes defining folder the asset is found in
	private String folderName;
	private String folderGUID;
	// Metadata defined for this asset
	private Map<String, String> assetMetadata = new HashMap<String, String>();
	
	// An attribute that can be leveraged for tracking if an asset is 'valid', whatever that may mean.  Some examples include if this asset
	// is still valid for steps to process -also- during IDOL metadata populating if oddities were found while processing.
	private boolean valid = true;

	public BaseAsset()
	{
	}
	public BaseAsset(String assetName, String assetId)
	{
		setAssetName(assetName);
		setAssetId(assetId);
	}
	
	public String getAssetName()
	{
		return assetName;
	}
	public void setAssetName(String assetName)
	{
		// Find name fields after any \\ or / which indicate path(s)
		int idx = assetName.lastIndexOf("\\");		
		if ( idx == -1 )
		{
			idx = assetName.lastIndexOf("/");
		}
		this.assetName = assetName.substring(idx+1);
	}
	/**
	 * Set asset name without pathchecks as part of the name.  This enables names to have "/" in them
	 */
	public void setAssetNameNoPathCheck(String assetName)
	{
		this.assetName = assetName;
	}

	public String getAssetId()
	{
		return assetId;
	}
	public void setAssetId(String assetId)
	{
		this.assetId = assetId;
	}
	
	public Map<String, String> getAssetMetadata()
	{
		return assetMetadata;
	}
	public void setAssetMetadata(Map<String, String> assetMetadata)
	{
		this.assetMetadata = assetMetadata;
	}
	public void addAssetMetadata(String identifier, String metaData)
	{
		getAssetMetadata().put(identifier, metaData);
	}	
	public void removeAssetMetadata(String identifier)
	{
		getAssetMetadata().remove(identifier);
	}	

	/**
	 * Search this base assets meta data for a matching GUID.  IF found, return that value, if not found
	 * return a null.  Multi values are returned as a comma delimited string. In method with alternate
	 * signature, if substitutePipe is true, commas within a value will be replaced with pipes so they
	 * can be differentiated from delimiter commas.
	 */
	public String getMetaDataValue(String metaDataGUID)
	{
		return getMetaDataValue(metaDataGUID, false);
	}
	
	public String getMetaDataValue(String metaDataGUID, boolean substitutePipe)
	{
		String md = getAssetMetadata().get(metaDataGUID);
		if (md == null)
		{
			return null;	// no match
		}
		return md;
	}
	
	public boolean isValid()
	{
		return valid;
	}
	public void setValid(boolean valid)
	{
		this.valid = valid;
	}	
	
	public String getFolderName()
	{
		return folderName;
	}
	public void setFolderName(String folderName)
	{
		this.folderName = folderName;
	}	
	public String getFolderGUID()
	{
		return folderGUID;
	}
	public void setFolderGUID(String folderGUID)
	{
		this.folderGUID = folderGUID;
	}
		
	/**
	 * Define the asset class for this asset based on AssetClass attribute constants.  Extenders of this class
	 * should over-ride this method with their class name for AssetRegistry Object.
	 */
	public String getAssetClassName()
	{
		// TODO  DAM-59 - Moved AssetClass and AttributesHelper back to JostensScript-java to get code to run under stand alone Tomcat
		// Need to move AssetClass back to shared project and then change below back to AssetClass.BaseAsset
		return "Base Asset";
	}

	public String toString()
	{
		return assetName;
	}
	
	/**
	 * Declare two Base assets to be equal if they share an identical asset GUID
	 */	
	public boolean equals( Object o )
	{
		if ( this == o )
		{
			return true;
		}
		if ( o == null )
		{
			return false;
		}
		if (getAssetId() == null)
		{
			return false;		// Cannot compare a missing asset ID
		}
		if ( o instanceof BaseAsset )
		{			
			return getAssetId().equals(((BaseAsset) o).getAssetId());
		}
		else
		{
			return false;
		}
	}

	public int hashCode()
	{
		return getAssetId().hashCode();
	}

}

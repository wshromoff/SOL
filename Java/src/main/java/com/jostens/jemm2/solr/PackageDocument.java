package com.jostens.jemm2.solr;

import org.apache.solr.client.solrj.beans.Field;

public class PackageDocument extends SOLRBase
{
	private String designID = null;
	private String partID = null;
	private String brandAssetType = null;

	

	public String getDesignID()
	{
		return designID;
	}
	@Field
	public void setDesignID(String designID)
	{
		this.designID = designID;
	}
	public String getPartID()
	{
		return partID;
	}
	@Field
	public void setPartID(String partID)
	{
		this.partID = partID;
	}
	public String getBrandAssetType()
	{
		return brandAssetType;
	}
	@Field 
	public void setBrandAssetType(String brandAssetType)
	{
		this.brandAssetType = brandAssetType;
	}
	
	public String getContentTypeFull()
	{
		return "Package";
	}
	public String getContentTypeAbbrev()
	{
		return "PK";
	}

}

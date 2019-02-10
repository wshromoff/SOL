package com.jostens.jemm2.solr;

import org.apache.solr.client.solrj.beans.Field;

public class CustomerPackageDocument extends SOLRBase
{
	private String designID = null;
	private String partID = null;
	private String clientID = null;
	private String packageID = null;
	private String statusLifeCycle = null;
	private String statusAvailability = null;
	private String businessDefaultUse = null;

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
	public String getClientID()
	{
		return clientID;
	}
	@Field
	public void setClientID(String clientID)
	{
		this.clientID = clientID;
	}
	public String getStatusLifeCycle()
	{
		return statusLifeCycle;
	}
	@Field
	public void setStatusLifeCycle(String statusLifeCycle)
	{
		this.statusLifeCycle = statusLifeCycle;
	}
	public String getStatusAvailability()
	{
		return statusAvailability;
	}
	@Field
	public void setStatusAvailability(String statusAvailability)
	{
		this.statusAvailability = statusAvailability;
	}
	public String getBusinessDefaultUse()
	{
		return businessDefaultUse;
	}
	@Field
	public void setBusinessDefaultUse(String businessDefaultUse)
	{
		this.businessDefaultUse = businessDefaultUse;
	}	
	public String getPackageID()
	{
		return packageID;
	}
	@Field
	public void setPackageID(String packageID)
	{
		this.packageID = packageID;
	}
	
	public String getContentTypeFull()
	{
		return "CustomerPackage";
	}
	public String getContentTypeAbbrev()
	{
		return "CP";
	}

}

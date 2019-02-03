package com.jostens.jemm2.solr;

import org.apache.solr.client.solrj.beans.Field;

public class PartDocument extends SOLRBase
{
	private String designID = null;
	private String partValidation = null;
	
	
	public String getDesignID()
	{
		return designID;
	}
	@Field
	public void setDesignID(String designID)
	{
		this.designID = designID;
	}
	public String getPartValidation()
	{
		return partValidation;
	}
	@Field
	public void setPartValidation(String partValidation)
	{
		this.partValidation = partValidation;
	}
	public String getContentTypeFull()
	{
		return "Part";
	}
	public String getContentTypeAbbrev()
	{
		return "PR";
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		sb.append("Part Validation: " + getPartValidation() + "\n");
		sb.append("Design ID: " + getDesignID() + "\n");
		
		return sb.toString();
	}

}

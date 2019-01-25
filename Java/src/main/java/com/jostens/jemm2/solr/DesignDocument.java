package com.jostens.jemm2.solr;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

public class DesignDocument extends SOLRBase
{
	private List<String> keywords = new ArrayList<String>();
	private	String mainSubject = null;
	private String affiliationByDepiction = null;
	private String brandAssetType = null;
	private String mascotName = null;

	
	public List<String> getKeywords()
	{
		return keywords;
	}
	@Field 
	public void setKeywords(List<String> keywords)
	{
		this.keywords = keywords;
	}
	public void addKeyword(String keyword)
	{
		keywords.add(keyword);
	}
	public String getMainSubject()
	{
		return mainSubject;
	}
	@Field 
	public void setMainSubject(String mainSubject)
	{
		this.mainSubject = mainSubject;
	}
	
	public String getContentTypeFull()
	{
		return "Design";
	}
	public String getContentTypeAbbrev()
	{
		return "DS";
	}
	public String getAffiliationByDepiction()
	{
		return affiliationByDepiction;
	}
	@Field 
	public void setAffiliationByDepiction(String affiliationByDepiction)
	{
		this.affiliationByDepiction = affiliationByDepiction;
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
	public String getMascotName()
	{
		return mascotName;
	}
	@Field 
	public void setMascotName(String mascotName)
	{
		this.mascotName = mascotName;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		sb.append("Keywords: " + keywords + "\n");
		sb.append("Main Subject: " + mainSubject + "\n");
		
		return sb.toString();
	}
}

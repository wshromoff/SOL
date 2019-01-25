package com.jostens.jemm2.solr;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

public class DesignDocument extends SOLRBase
{
	private List<String> keywords = new ArrayList<String>();
	private	String mainSubject = null;
	private boolean multipleMainSubject = false;
	private String portion = null;
	private String view = null;

	
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
	public boolean isMultipleMainSubject()
	{
		return multipleMainSubject;
	}
	public String getMultipleMainSubject()
	{
		return Boolean.toString(multipleMainSubject);
	}
	@Field 
	public void setMultipleMainSubject(boolean multipleMainSubject)
	{
		this.multipleMainSubject = multipleMainSubject;
	}
	public String getPortion()
	{
		return portion;
	}
	@Field 
	public void setPortion(String portion)
	{
		this.portion = portion;
	}
	public String getView()
	{
		return view;
	}
	@Field 
	public void setView(String view)
	{
		this.view = view;
	}
	
	public String getContentTypeFull()
	{
		return "Design";
	}
	public String getContentTypeAbbrev()
	{
		return "DS";
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		sb.append("Keywords: " + keywords + "\n");
		sb.append("Main Subject: " + mainSubject + "\n");
		sb.append("Multiple Main Subject: " + multipleMainSubject + "\n");
		sb.append("portion: " + portion + "\n");
		sb.append("view: " + view + "\n");
		
		return sb.toString();
	}
}

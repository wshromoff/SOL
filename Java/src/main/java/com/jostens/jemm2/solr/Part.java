package com.jostens.jemm2.solr;

import org.apache.solr.client.solrj.beans.Field;

public class Part extends SOLRBase
{
	private String name = null;
	private String designID = null;
	
	
	public String getName()
	{
		return name;
	}
	@Field
	public void setName(String name)
	{
		this.name = name;
	}
	public String getDesignID()
	{
		return designID;
	}
	@Field
	public void setDesignID(String designID)
	{
		this.designID = designID;
	}

	public String getContentType()
	{
		return "Part";
	}

	public String getContentAbbrev()
	{
		return "PR";
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		sb.append("Name: " + name + "\n");
		sb.append("Design ID: " + designID + "\n");
		
		return sb.toString();
	}

}

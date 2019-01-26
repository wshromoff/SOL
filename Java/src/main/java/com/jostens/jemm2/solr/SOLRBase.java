package com.jostens.jemm2.solr;

import org.apache.solr.client.solrj.beans.Field;

public abstract class SOLRBase implements SOLRNeeded
{
//	private String id = null;
	private int databaseID = 0;
	private String name = null;
//	private String contentType = null;
	
	public String getId()
	{
//		String formattedID = String.format("%06d", getDatabaseID());
		return getContentTypeAbbrev() + "_" + getFormattedID(getDatabaseID());
	}
	@Field
	public void setId(String id)
	{
		int i = id.indexOf("_");
		setDatabaseID(new Integer(id.substring(i+1).toString()));
//		setDatabaseID(Integer.parseInt(id.substring(i+1)));
	}

	public String getContentType()
	{
		return getContentTypeFull();
	}
	@Field
	public void setContentType(String contentType)
	{
//		this.contentType = contentType;
	}
	
	public int getDatabaseID()
	{
		return databaseID;
	}
	@Field
	public void setDatabaseID(int databaseID)
	{
//		String formatted = String.format("%06d", databaseID);
		this.databaseID = databaseID;
	}

	public String getName()
	{
		return name;
	}
	@Field
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Return the provided int as a 0 pre padded String
	 */
	public String getFormattedID(int id)
	{
		return String.format("%06d", id);
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("ID: " + getId() + "\n");
		sb.append("Name: " + getName() + "\n");
		sb.append("Database ID: " + getDatabaseID() + "\n");
		sb.append("Content Type: " + getContentType() + "\n");
		return sb.toString();
	}
}

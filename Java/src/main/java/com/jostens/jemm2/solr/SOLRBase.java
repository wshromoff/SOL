package com.jostens.jemm2.solr;

import org.apache.solr.client.solrj.beans.Field;

public abstract class SOLRBase implements SOLRNeeded
{
	private String id = null;
	private String databaseID = null;
	
	public String getId()
	{
		return getContentAbbrev() + "_" + getDatabaseID();
	}
	@Field
	public void setId(String id)
	{
		int i = id.indexOf("_");
		setDatabaseID(Integer.parseInt(id.substring(i+1)));
	}
	public String getDatabaseID()
	{
		return databaseID;
	}
	public void setDatabaseID(int databaseID)
	{
		String formatted = String.format("%06d", databaseID);
		this.databaseID = formatted;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Database ID: " + databaseID + "\n");
		return sb.toString();
	}
}

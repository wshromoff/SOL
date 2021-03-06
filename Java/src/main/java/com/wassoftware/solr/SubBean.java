package com.wassoftware.solr;

import org.apache.solr.client.solrj.beans.Field;

public class SubBean
{

	private String id;
	private String itemName;
	private int cost;
	private String contentType = "subBean";

	public SubBean(String id, String name, int cost)
	{
		this.id = id;
		itemName = name;
		this.cost = cost;
	}

	public String getContentType()
	{
		return contentType;
	}
	@Field("contentType")
	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	@Field("id")
	protected void setId(String id)
	{
		this.id = id;
	}
	public String getId()
	{
		return id;
	}

	public String getItemName()
	{
		return itemName;
	}
	
	@Field("itemName")
	public void setItemName(String itemName)
	{
		this.itemName = itemName;
	}
	public int getCost()
	{
		return cost;
	}
	@Field("cost")
	public void setCost(int cost)
	{
		this.cost = cost;
	}
	
	
}

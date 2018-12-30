package com.wassoftware.solr;

import org.apache.solr.client.solrj.beans.Field;

public class ProductBean
{

	private String id;
	private String name;
	private String price;
	
	public ProductBean(String id, String name, String price)
	{
		this.id = id;
		this.name = name;
		this.price = price;
	}

	@Field("id")
	protected void setId(String id)
	{
		this.id = id;
	}

	@Field("name")
	protected void setName(String name)
	{
		this.name = name;
	}

	@Field("price")
	protected void setPrice(String price)
	{
		this.price = price;
	}

	public String getId()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}
	public String getPrice()
	{
		return price;
	}

}